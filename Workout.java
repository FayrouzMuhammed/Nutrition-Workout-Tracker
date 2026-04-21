package model;

public class Walk extends Workout {

    public Walk(int duration) {
        super("Walk", duration);
    }

    @Override
    public double calculateCaloriesBurned() {
        return duration * 5.0;
    }

    @Override
    public Workout withDuration(int duration) {
        return new Walk(duration);
    }
}