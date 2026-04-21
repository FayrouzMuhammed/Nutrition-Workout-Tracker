package model;

public class Cycling extends Workout {

    public Cycling(int duration) {
        super("Cycling", duration);
    }

    @Override
    public double calculateCaloriesBurned() {
        return duration * 8.0;
    }

    @Override
    public Workout withDuration(int duration) {
        return new Cycling(duration);
    }
}