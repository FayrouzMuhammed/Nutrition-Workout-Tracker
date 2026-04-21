package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DailyLogPanel extends JPanel {
    private MainFrame parent;

    private DefaultListModel<String> mealListModel = new DefaultListModel<>();
    private JList<String> mealList = new JList<>(mealListModel);
    private JTextArea mealDetails = new JTextArea(10, 30);

    private JComboBox<String> workoutBox;
    private JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 600, 1));

    private java.util.List<Workout> availableWorkouts = new java.util.ArrayList<>();

    private DefaultListModel<String> workoutListModel = new DefaultListModel<>();
    private JList<String> workoutList = new JList<>(workoutListModel);

    private JTextArea summaryArea = new JTextArea(6, 40);

    public DailyLogPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Meals"), BorderLayout.NORTH);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealList.addListSelectionListener(e -> refreshMealDetails());
        left.add(new JScrollPane(mealList), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        mealDetails.setEditable(false);
        center.add(new JLabel("Meal details"), BorderLayout.NORTH);
        center.add(new JScrollPane(mealDetails), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        workoutBox = new JComboBox<>();
        // define available workouts locally (don't rely on WorkoutList in MainFrame)
        availableWorkouts.add(new Running(30));
        availableWorkouts.add(new Cycling(45));
        availableWorkouts.add(new Yoga(60));
        availableWorkouts.add(new Walk(30));
        for (Workout w : availableWorkouts) workoutBox.addItem(w.getName());
        bottom.add(new JLabel("Workout")); bottom.add(workoutBox);
        bottom.add(new JLabel("Duration (min)")); bottom.add(durationSpinner);
        JButton btnAddWorkout = new JButton("Add Workout");
        btnAddWorkout.addActionListener(this::onAddWorkout);
        bottom.add(btnAddWorkout);



        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.NORTH);

        // right side holds workouts + summary
        JPanel right = new JPanel(new BorderLayout());
        right.add(new JLabel("Workouts"), BorderLayout.NORTH);
        workoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        right.add(new JScrollPane(workoutList), BorderLayout.CENTER);

        summaryArea.setEditable(false);
        right.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        refreshMealList();
        refreshWorkoutList();
        refreshSummary();
    }

    private void refreshMealList() {
        mealListModel.clear();
        for (Meal m : parent.getDailyLog().getMeals()) {
            mealListModel.addElement(m.getMealType());
        }
    }

    public void refreshWorkoutList() {
        workoutListModel.clear();
        for (Workout w : parent.getDailyLog().getWorkouts()) {
            workoutListModel.addElement(String.format("%s - %d min (%.2f cal)", w.getName(), w.getDuration(), w.getCaloriesBurned()));
        }
    }

    public void refreshAll() {
        refreshMealList();
        refreshWorkoutList();
        refreshSummary();
    }

    private void refreshMealDetails() {
        int idx = mealList.getSelectedIndex();
        if (idx < 0) {
            mealDetails.setText("");
            return;
        }
        String name = mealListModel.getElementAt(idx);
        Meal target = null;
        for (Meal m : parent.getDailyLog().getMeals()) if (m.getMealType().equalsIgnoreCase(name)) {
            target = m;
            break;
        }
        if (target == null) return;
        StringBuilder sb = new StringBuilder();
        for (FoodItem f : target.getFoods()) {
            sb.append(f.getName()).append(" - ").append(String.format("%.2f cal", f.getCalories())).append("\n");
        }
        sb.append("\nTotal: ").append(String.format("%.2f cal", target.getTotalCalories()));
        mealDetails.setText(sb.toString());
    }

    private void onAddWorkout(ActionEvent ev) {
        String sel = (String) workoutBox.getSelectedItem();
        int dur = (Integer) durationSpinner.getValue();
        Workout chosen = null;
        for (Workout w : availableWorkouts) if (w.getName().equalsIgnoreCase(sel)) {
            chosen = w.withDuration(dur);
            break;
        }
        if (chosen == null) {
            JOptionPane.showMessageDialog(this, "Invalid workout selection.");
            return;
        }
        parent.getDailyLog().addworkout(chosen);
        JOptionPane.showMessageDialog(this, "Added workout: " + chosen.toString());
        // DailyLogFileManager.saveDailyLog(parent.getDailyLog());
        refreshWorkoutList();
        refreshSummary();
    }

    private void refreshSummary() {
        parent.getDailyLog().calculateCaloriesConsumed();
        parent.getDailyLog().calculateCaloriesBurned();
        Double dailyTarget = null;
        if (parent.getCurrentUser() != null) {
            dailyTarget = parent.getCurrentUser().GetDailyCalorieNeeds();
            parent.getDailyLog().calculateCaloriesRemaining(dailyTarget);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Calories consumed: ").append(String.format("%.2f", parent.getDailyLog().getCaloriesConsumed())).append("\n");
        sb.append("Calories burned: ").append(String.format("%.2f", parent.getDailyLog().getCaloriesBurned())).append("\n");
        double remaining = parent.getDailyLog().getCaloriesRemaining();
        if (dailyTarget != null) {
            sb.append("Daily target (estimate): ").append(String.format("%.2f", dailyTarget)).append("\n");
            if (remaining <= 0) sb.append("Status: Target reached or exceeded!\n");
            else sb.append(String.format("Status: %.2f calories to reach target\n", remaining));
            sb.append("Calories remaining: ").append(String.format("%.2f", remaining)).append("\n");
        }
        summaryArea.setText(sb.toString());

        refreshMealList();
        refreshMealDetails();
    }
}