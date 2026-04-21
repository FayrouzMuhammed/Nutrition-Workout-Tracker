package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Meal {

    protected String mealtype;
    protected ArrayList<FoodItem> foods;

    public Meal(String mealtype) {
        this.mealtype = mealtype;
        this.foods = new ArrayList<>();
    }

    public Meal(String mealtype, ArrayList<FoodItem> foods) {
        this.mealtype = mealtype;
        this.foods = foods;
    }

    public void add(FoodItem food) {
        foods.add(food);
    }

    // add a food with a specified amount (e.g., 2 servings)
    public void add(FoodItem food, double amount) {
        foods.add(food.withAmount(amount));
    }

    public double getTotalCalories() {
        double total = 0;
        for (FoodItem f : foods) {
            total += f.getCalories();
        }
        return total;
    }

    public double getTotalProtein() {
        double total = 0;
        for (FoodItem f : foods) {
            total += f.getProtein();
        }
        return total;
    }

    public double getTotalCarbs() {
        double total = 0;
        for (FoodItem f : foods) {
            total += f.getCarbs();
        }
        return total;
    }

    public double getTotalFats() {
        double total = 0;
        for (FoodItem f : foods) {
            total += f.getFats();
        }
        return total;
    }

    public void showMealInfo() {
        System.out.println("----- " + mealtype + " -----");

        for (FoodItem f : foods) {
            System.out.println("- " + f.toString());
        }

        System.out.println("Total Calories: " + getTotalCalories());
        System.out.println("Total Protein: " + getTotalProtein());
        System.out.println("Total Carbs: " + getTotalCarbs());
        System.out.println("Total Fats: " + getTotalFats());
    }

    public List<FoodItem> getFoods() {
        return Collections.unmodifiableList(foods);
    }

    public String getMealType() {
        return mealtype;
    }
}
