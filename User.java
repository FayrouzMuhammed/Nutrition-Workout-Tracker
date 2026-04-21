package model;

public class Running extends Workout {

    public Running(int duration) {
        super("Running", duration);
    }

    @Override
    public double calculateCaloriesBurned() {
        return duration * 10.0;
    }

    @Override
    public Workout withDuration(int duration) {
        return new Running(duration);
    }
}

