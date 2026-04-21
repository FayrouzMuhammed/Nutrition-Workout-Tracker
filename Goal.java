package model;

public class FoodItem {
    
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private String unit;

    public FoodItem(String name, double calories, double protein, double carbs, double fats, String unit) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getCalories(double amount) {
        return calories * amount;
    }

    public double getProtein(double amount) {
        return protein * amount;
    }

    public double getCarbs(double amount) {
        return carbs * amount;
    }

    public double getFats(double amount) {
        return fats * amount;
    }

    // Convenience getters for items that already represent a serving/amount
    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFats() {
        return fats;
    }

    // Return a new FoodItem scaled by amount (e.g., 2 means 2 servings)
    public FoodItem withAmount(double amount) {
        return new FoodItem(this.name, this.calories * amount, this.protein * amount, this.carbs * amount, this.fats * amount, this.unit);
    }

}
