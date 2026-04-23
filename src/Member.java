import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String name;
    private final List<Booking> bookings = new ArrayList<>();

    public Member(String name) { this.name = name; }

    public String getName() { return name; }
    public List<Booking> getBookings() { return bookings; }



    public boolean hasConflict(Lesson lesson) {
        for (Booking b : bookings) {
            if (b.getStatus() == Booking.Status.CANCELLED) continue;
            Lesson booked = b.getLesson();
            if (booked.getWeekNumber() == lesson.getWeekNumber()
                    && booked.getDay().equals(lesson.getDay())
                    && booked.getTimeSlot().equals(lesson.getTimeSlot())) {
                return true;
            }
        }
        return false;
    }

    public Booking findActiveBooking(Lesson lesson) {
        return bookings.stream()
                .filter(b -> b.getLesson() == lesson && b.getStatus() != Booking.Status.CANCELLED)
                .findFirst().orElse(null);
    }

    public Booking findBookingById(int id) {
        return bookings.stream().filter(b -> b.getBookingId() == id).findFirst().orElse(null);
    }

    @Override
    public String toString() { return name; }
}
