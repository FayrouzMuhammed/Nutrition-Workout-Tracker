package model;

public abstract class Workout implements Burnable {

    protected String name;
    protected int duration; 

    public Workout(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

  
    public abstract double calculateCaloriesBurned();

    // convenience wrapper so callers don't need to call the abstract method directly
    public double getCaloriesBurned() {
        return calculateCaloriesBurned();
    }

    // return a new instance of the same workout type with a different duration
    public abstract Workout withDuration(int duration);

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

}