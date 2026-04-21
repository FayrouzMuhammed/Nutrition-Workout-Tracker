package model;

import java.util.ArrayList;
import java.time.LocalDate;

public class DailyLog {

    private LocalDate date;
    private ArrayList<Meal> meals = new ArrayList<>();
    private ArrayList<Workout> workouts = new ArrayList<>();
    private double caloriesConsumed;
    private double caloriesBurned;
    private double caloriesRemaining;
    private double totalProtien;
    private double totalCarbs;
    private double totalFats;

    public DailyLog() {
        this.setDate(LocalDate.now());
    }

    public void addmeal(Meal meal) {
        meals.add(meal);
    }

    /** Camel-cased convenience wrapper kept for API cleanliness. */
    public void addMeal(Meal meal) { addmeal(meal); }

    public java.util.List<Meal> getMeals() {
        return java.util.Collections.unmodifiableList(meals);
    }

    public java.util.List<Workout> getWorkouts() {
        return java.util.Collections.unmodifiableList(workouts);
    }

    public void addworkout(Workout workout) {
        workouts.add(workout);
    }

    /** Camel-cased convenience wrapper kept for API cleanliness. */
    public void addWorkout(Workout workout) { addworkout(workout); }

    public void addFoodToMeal(String mealType, FoodItem food, double amount) {
        Meal target = null;
        for (Meal m : meals) {
            if (m.getMealType().equalsIgnoreCase(mealType)) {
                target = m;
                break;
            }
        }
        if (target == null) {
            switch (mealType.toLowerCase()) {
                case "lunch":
                    target = new Lunch();
                    break;
                case "dinner":
                    target = new Dinner();
                    break;
                default:
                    target = new Breakfast();
            }
            meals.add(target);
        }
        target.add(food, amount);
    }

    public void calculateCaloriesConsumed() {
        caloriesConsumed = 0;
        totalProtien = 0;
        totalCarbs = 0;
        totalFats = 0;
        for (Meal m : meals) {
            caloriesConsumed += m.getTotalCalories();
            totalProtien += m.getTotalProtein();
            totalCarbs += m.getTotalCarbs();
            totalFats += m.getTotalFats();
        }
    }

    public void calculateCaloriesBurned() {
        caloriesBurned = 0;
        for (Workout w : workouts) {
            // call the concrete workout's calculation; if Workout implements Burnable
            // in the future the same method will be available there as well.
            caloriesBurned += w.calculateCaloriesBurned();
        }
    }

    public void calculateCaloriesRemaining(double dailyCaloriesTarget) {
        // remaining should never be negative; clamp to zero
        caloriesRemaining = dailyCaloriesTarget - (caloriesConsumed - caloriesBurned);
        if (caloriesRemaining < 0) caloriesRemaining = 0;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCaloriesConsumed() { return caloriesConsumed; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public double getCaloriesRemaining() { return caloriesRemaining; }

    public double getTotalProtein() { return totalProtien; }
    public double getTotalCarbs() { return totalCarbs; }
    public double getTotalFats() { return totalFats; }

}