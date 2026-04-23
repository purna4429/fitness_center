import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTest {

    private BookingSystem system;

    @BeforeEach
    void setUp() {
        Lesson.resetIdCounter();
        Booking.resetIdCounter();
    
    }

    @Test
    void bookLessonSuccess() {
        Member aarav = system.getMember("Aarav");
        Lesson free = system.getTimetable().getAllLessons().stream()
                .filter(l -> l.hasSpace() && !aarav.hasConflict(l)
                        && aarav.findActiveBooking(l) == null)
                .findFirst().orElse(null);
        assertNotNull(free);
        String result = system.bookLesson("Aarav", free.getId());
        assertTrue(result.startsWith("Booking successful!"));
    }

    @Test
    void bookLessonDuplicateNotAllowed() {
      
        assertEquals("You already have a booking for this lesson (duplicate not allowed).", result);
    }

    @Test
    void cancelBookingChangesStatusAndReleasesSlot() {
        Member aarav = system.getMember("Aarav");
        Booking booked = aarav.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED)
                .findFirst().orElse(null);
        assertNotNull(booked);
        int before = booked.getLesson().getActiveBookings();
        system.cancelBooking("Aarav", booked.getBookingId());
        assertEquals(Booking.Status.CANCELLED, booked.getStatus());
        assertEquals(before - 1, booked.getLesson().getActiveBookings());
    }

    @Test
    void changeBookingKeepsSameBookingId() {
        Member aarav = system.getMember("Aarav");
        Booking booked = aarav.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED)
                .findFirst().orElse(null);
        assertNotNull(booked);
        int originalId = booked.getBookingId();
        Lesson newLesson = system.getTimetable().getAllLessons().stream()
                .filter(l -> l.hasSpace() && !aarav.hasConflict(l)
                        && aarav.findActiveBooking(l) == null
                        && l != booked.getLesson())
                .findFirst().orElse(null);
        assertNotNull(newLesson);
        system.changeBooking("Aarav", originalId, newLesson.getId());
        assertEquals(originalId, booked.getBookingId());
        assertEquals(Booking.Status.CHANGED, booked.getStatus());
    }

    @Test
    void attendLessonByBookingId() {
        Member priya = system.getMember("Priya");
        Booking booked = priya.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED)
                .findFirst().orElse(null);
        assertNotNull(booked);
        String result = system.attendLesson("Priya", booked.getBookingId(), 5, "Great class!");
        assertTrue(result.startsWith("Attended successfully!"));
        assertEquals(Booking.Status.ATTENDED, booked.getStatus());
    }

    @Test
    void attendedLessonCannotBeChangedOrCancelled() {
        Member aarav = system.getMember("Aarav");
        Booking attended = aarav.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.ATTENDED)
                .findFirst().orElse(null);
        assertNotNull(attended);
        String changeResult = system.changeBooking("Aarav", attended.getBookingId(), 20);
        String cancelResult = system.cancelBooking("Aarav", attended.getBookingId());
        assertEquals("Cannot change an already attended booking.", changeResult);
        assertEquals("Cannot cancel an attended booking.", cancelResult);
    }
}
