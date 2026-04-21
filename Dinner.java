package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based persistence for the app. Stores one USER record and multiple DAILY blocks.
 */
public class DailyLogFileManager {

    private static final String File_Name = "daily_log.txt";

    public static void saveUser(User user) {
        if (user == null) return;
        List<String> lines = readAllLines();
        String userLine = makeUserLine(user);
        // remove any existing USER lines (avoid duplicates), then insert at top
        List<String> newLines = new ArrayList<>();
        for (String l : lines) {
            if (l.startsWith("USER|")) continue;
            newLines.add(l);
        }
        newLines.add(0, userLine);
        lines = newLines;
        writeAllLines(lines);
    }

    public static User loadUser() {
        List<String> lines = readAllLines();
        for (String l : lines) {
            if (l.startsWith("USER|")) {
                String[] parts = l.split("\\|", -1);
                try {
                    String name = parts[1];
                    int age = Integer.parseInt(parts[2]);
                    String gender = parts[3];
                    double weight = Double.parseDouble(parts[4]);
                    double height = Double.parseDouble(parts[5]);
                    double goalTarget = Double.parseDouble(parts[6]);
                    int goalWeeks = Integer.parseInt(parts[7]);
                    int activityDays = Integer.parseInt(parts[8]);
                    Goal g = new Goal(goalTarget, goalWeeks);
                    return new User(name, age, gender, weight, height, g, activityDays);
                } catch (Exception ex) {
                    System.out.println("Error parsing USER line: " + ex.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    public static void saveDailyLog(DailyLog log) {
        if (log == null || log.getDate() == null) return;
        List<String> lines = readAllLines();
        String dateKey = "DAILY|" + log.getDate().toString();

        int start = -1, end = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(dateKey)) {
                start = i;
                for (int j = i + 1; j < lines.size(); j++) {
                    if (lines.get(j).startsWith("DAILY|") || lines.get(j).startsWith("USER|")) {
                        end = j;
                        break;
                    }
                }
                if (end == -1) end = lines.size();
                break;
            }
        }

        List<String> block = makeDailyBlock(log);

        if (start != -1) {
            List<String> newLines = new ArrayList<>();
            newLines.addAll(lines.subList(0, start));
            newLines.addAll(block);
            newLines.addAll(lines.subList(end, lines.size()));
            writeAllLines(newLines);
        } else {
            lines.addAll(block);
            writeAllLines(lines);
        }
    }

    public static DailyLog loadDailyLog(LocalDate date) {
        DailyLog d = new DailyLog();
        d.setDate(date);
        List<String> lines = readAllLines();
        String dateKey = "DAILY|" + date.toString();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(dateKey)) {
                Meal currentMeal = null;
                for (int j = i + 1; j < lines.size(); j++) {
                    String ln = lines.get(j);
                    if (ln.startsWith("DAILY|") || ln.startsWith("USER|")) break;
                    if (ln.startsWith("MEAL|")) {
                        String[] parts = ln.split("\\|", -1);
                        String mealType = parts[1];
                        switch (mealType.toLowerCase()) {
                            case "lunch": currentMeal = new Lunch(); break;
                            case "dinner": currentMeal = new Dinner(); break;
                            default: currentMeal = new Breakfast(); break;
                        }
                        d.addmeal(currentMeal);
                        continue;
                    }
                    if (ln.startsWith("FOOD|")) {
                        String[] parts = ln.split("\\|", -1);
                        try {
                            String name = unescape(parts[1]);
                            double calories = Double.parseDouble(parts[2]);
                            double protein = Double.parseDouble(parts[3]);
                            double carbs = Double.parseDouble(parts[4]);
                            double fats = Double.parseDouble(parts[5]);
                            String unit = parts.length > 6 ? unescape(parts[6]) : "";
                            FoodItem f = new FoodItem(name, calories, protein, carbs, fats, unit);
                            if (currentMeal != null) currentMeal.add(f);
                        } catch (Exception ex) {
                            System.out.println("Error parsing FOOD line: " + ex.getMessage());
                        }
                        continue;
                    }
                    if (ln.startsWith("WORKOUT|")) {
                        String[] parts = ln.split("\\|", -1);
                        try {
                            String name = parts[1];
                            int dur = Integer.parseInt(parts[2]);
                            Workout w = null;
                            if (name.equalsIgnoreCase("Running")) w = new Running(dur);
                            else if (name.equalsIgnoreCase("Cycling")) w = new Cycling(dur);
                            else if (name.equalsIgnoreCase("Yoga")) w = new Yoga(dur);
                            else if (name.equalsIgnoreCase("Walk") || name.equalsIgnoreCase("Walking")) w = new Walk(dur);
                            if (w != null) d.addworkout(w);
                        } catch (Exception ex) {
                            System.out.println("Error parsing WORKOUT line: " + ex.getMessage());
                        }
                        continue;
                    }
                }
                break;
            }
        }
        return d;
    }

    private static List<String> makeDailyBlock(DailyLog d) {
        List<String> out = new ArrayList<>();
        out.add("DAILY|" + d.getDate().toString());
        for (Meal m : d.getMeals()) {
            out.add("MEAL|" + m.getMealType());
            for (FoodItem f : m.getFoods()) {
                out.add(String.format("FOOD|%s|%f|%f|%f|%f|%s", escape(f.getName()), f.getCalories(), f.getProtein(), f.getCarbs(), f.getFats(), escape(f.getUnit())));
            }
        }
        for (Workout w : d.getWorkouts()) {
            out.add(String.format("WORKOUT|%s|%d", escape(w.getName()), w.getDuration()));
        }
        out.add("ENDDAILY");
        return out;
    }

    private static String makeUserLine(User user) {
        Goal g = user.getGoal();
        return String.format("USER|%s|%d|%s|%f|%f|%f|%d|%d", escape(user.getName()), user.getAge(), escape(user.getGender()), user.getWeight(), user.getHeight(), g.getTargetWeight(), g.getDurationInWeeks(), user.getActivityDaysPerWeek());
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "\\|");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|");
    }

    private static List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            // file may not exist yet; that's OK
        }
        return lines;
    }

    private static void writeAllLines(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(File_Name))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

}