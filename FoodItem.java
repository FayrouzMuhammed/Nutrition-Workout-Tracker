package model;

import java.util.ArrayList;

public class FoodDB {

    private ArrayList<FoodItem> FoodList;

    public FoodDB() {
        FoodList = new ArrayList<>();

        FoodList.add(new FoodItem("Avocado", 240, 0.0, 12, 22, "piece"));
        FoodList.add(new FoodItem("Apple", 95, 0.5, 25, 0.3, "piece"));
        FoodList.add(new FoodItem("Banana", 105, 1.3, 27, 0.3, "piece"));
        FoodList.add(new FoodItem("Orange", 62, 1.2, 15, 0.2, "piece"));
        FoodList.add(new FoodItem("Tomato", 22, 1.1, 5, 0.2, "piece"));
        FoodList.add(new FoodItem("Cucamber", 45, 2, 11, 0.3, "piece"));
        FoodList.add(new FoodItem("Carot", 25, 0.6, 6, 0.1, "piece"));
        FoodList.add(new FoodItem("Bread Loaf", 75, 2.5, 13, 1, "piece"));
        FoodList.add(new FoodItem("Egg", 72, 6.3, 0.4, 5, "piece"));
        FoodList.add(new FoodItem("Croissant", 240, 4, 26, 12, "piece"));
        FoodList.add(new FoodItem("Chocolate", 5.25, 0, 0.6, 0.3, "g"));
        FoodList.add(new FoodItem("White Rice", 1.297, 0.027, 0.28, 0.0025, "g"));
        FoodList.add(new FoodItem("Plain Boiled Pasta", 1.57, 0.057, 0.31, 0.0093, "g"));
        FoodList.add(new FoodItem("Fried Chicken", 2.53, 0.187, 0.08, 0.14, "g"));
        FoodList.add(new FoodItem("Grilled Chicken Breast", 1.65, 0.31, 0.0, 0.036, "g"));
        FoodList.add(new FoodItem("Grilled Chicken Thigh", 2.09, 0.26, 0.0, 0.109, "g"));
        FoodList.add(new FoodItem("Grilled Beef Steak", 2.5, 0.26, 0.0, 0.17, "g"));
        FoodList.add(new FoodItem("Tuna Can", 116, 26, 0.0, 1, "piece"));
        FoodList.add(new FoodItem("Milk", 0.6, 0.053, 0.048, 0.032, "ml"));
        FoodList.add(new FoodItem("Orange Juice", 0.458, 0.0, 0.108, 0.0, "ml"));
    }

    public FoodItem getFoodByName(String name) {
        for (FoodItem f : FoodList) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }
        return null;
    }

    public ArrayList<FoodItem> getList() {
        return FoodList;
    }

}