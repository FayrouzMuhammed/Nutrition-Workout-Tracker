package model;

public class Goal {

    private double targetWeight;
    private int durationInWeeks;
    private double TDEE;
    private double dailyCaloriesTarget;

    public Goal(double targetWeight, int durationInWeeks) {
        this.targetWeight = targetWeight;
        this.durationInWeeks = durationInWeeks;
    }

    public void calc_TDEE(double BMR, double activityLevel) {
        this.TDEE = BMR * activityLevel;
    }

    public void calcDailyCaloriesTarget(double currentWeight) {
        int days = Math.max(1, durationInWeeks * 7);
        this.dailyCaloriesTarget = TDEE - (((currentWeight - targetWeight) * 7700) / days);
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public double getTDEE() {
        return TDEE;
    }

    public void setTDEE(double tDEE) {
        TDEE = tDEE;
    }

    public double getDailyCaloriesTarget() {
        return dailyCaloriesTarget;
    }

    public void setDailyCaloriesTarget(double dailyCaloriesTarget) {
        this.dailyCaloriesTarget = dailyCaloriesTarget;
    }
    
}