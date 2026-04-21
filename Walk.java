package model;

public class User {

    private String name;
    private int age;
    private String gender;
    private double weight;
    private double height;

    private Goal goal;
    private int activityDaysPerWeek;

    public User(String name, int age, String gender, double weight, double height, Goal goal, int activityDaysPerWeek) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.activityDaysPerWeek = activityDaysPerWeek;
    }


    /** Calculate BMI using height in meters. */
    public double calculateBMI() {
        return weight / (height * height);
    }

    public String getBMICategory() {
        double bmi = calculateBMI();
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    /** Calculate BMR using Mifflin-St Jeor (height expected in meters). */
    public double calculateBMR() {
        double heightCm = height * 100.0;
        if (gender.equalsIgnoreCase("male"))
            return (10 * weight) + (6.25 * heightCm) - (5 * age) + 5;
        else
            return (10 * weight) + (6.25 * heightCm) - (5 * age) - 161;
    }

    public double getActivityMultiplier() {
        int d = Math.max(0, Math.min(7, activityDaysPerWeek));
        if (d == 0) return 1.2;
        else if (d <= 2) return 1.375;
        else if (d <= 4) return 1.55;
        else if (d <= 6) return 1.725;
        else return 1.9;
    }
    public double GetDailyCalorieNeeds() {
        double BMR = calculateBMR();
        double activityLevel;
        int d = Math.max(0, Math.min(7, activityDaysPerWeek));
        if (d == 0) activityLevel = 1.2;
        else if (d <= 2) activityLevel = 1.375;
        else if (d <= 4) activityLevel = 1.55;
        else if (d <= 6) activityLevel = 1.725;
        else activityLevel = 1.9;

        goal.calc_TDEE(BMR, activityLevel);
        goal.calcDailyCaloriesTarget(weight);

        return goal.getDailyCaloriesTarget();
    }

    public Goal getGoal() {
        return goal;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public int getActivityDaysPerWeek() {
        return activityDaysPerWeek;
    }

}