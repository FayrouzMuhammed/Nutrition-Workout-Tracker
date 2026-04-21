package model;

public class Yoga extends Workout {

    public Yoga(int duration) {
        super("Yoga", duration);
    }

    @Override
    public double calculateCaloriesBurned() {
        return duration * 4.0;
    }

    @Override
    public Workout withDuration(int duration) {
        return new Yoga(duration);
    }
}