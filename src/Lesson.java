import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private static int idCounter = 1;
    private static final int MAX_CAPACITY = 4;

    private final int id;
    private final Exercise exercise;
    private final String day;
    private final String timeSlot;
    private final int weekNumber;
    private final int month;
    private final List<Review> reviews = new ArrayList<>();
    private int activeBookings = 0;

    public Lesson(Exercise exercise, String day, String timeSlot, int weekNumber, int month) {
        this.id = idCounter++;
        this.exercise = exercise;
        this.day = day;
        this.timeSlot = timeSlot;
        this.weekNumber = weekNumber;
        this.month = month;
    }

    public static void resetIdCounter() { idCounter = 1; }

    public int getId()           { return id; }
    public Exercise getExercise(){ return exercise; }
    public String getDay()       { return day; }
    public String getTimeSlot()  { return timeSlot; }
    public int getWeekNumber()   { return weekNumber; }
    public int getMonth()        { return month; }
    public List<Review> getReviews() { return reviews; }

    public boolean hasSpace()    { return activeBookings < MAX_CAPACITY; }
    public int getActiveBookings(){ return activeBookings; }
    public int getSpacesLeft()    { return MAX_CAPACITY - activeBookings; }
    public void incrementBookings() { activeBookings++; }
    public void decrementBookings() { if (activeBookings > 0) activeBookings--; }

    public void addReview(Review review) { reviews.add(review); }

    public double getAverageRating() {
        if (reviews.isEmpty()) return 0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0);
    }

    public double getAttendedIncome(List<Booking> allBookings) {
        long attended = allBookings.stream()
                .filter(b -> b.getLesson() == this && b.getStatus() == Booking.Status.ATTENDED)
                .count();
        return attended * exercise.getPrice();
    }

    public long getAttendedCount(List<Booking> allBookings) {
        return allBookings.stream()
                .filter(b -> b.getLesson() == this && b.getStatus() == Booking.Status.ATTENDED)
                .count();
    }

    @Override
    public String toString() {
        return String.format("[ID:%2d] Month%d Wk%d %-9s %-9s %-11s | Spaces left: %d | Price: %.2f",
                id, month, weekNumber, day, timeSlot, exercise.getName(),
                MAX_CAPACITY - activeBookings, exercise.getPrice());
    }
}
