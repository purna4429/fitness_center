public class Booking {
    public enum Status { BOOKED, ATTENDED, CHANGED, CANCELLED }

    private static int idCounter = 1;

    private final int bookingId;
    private final Member member;
    private Lesson lesson;
 

    public Booking(Member member, Lesson lesson) {
        this.bookingId = idCounter++;
        this.member = member;
        this.lesson = lesson;
        this.status = Status.BOOKED;
    }

    public static void resetIdCounter() { idCounter = 1; }

    public int getBookingId()  { return bookingId; }
    public Member getMember()  { return member; }
    public Lesson getLesson()  { return lesson; }
    public Status getStatus()  { return status; }

    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Booking#%d | %s | Status: %s", bookingId, lesson, status);
    }
}
