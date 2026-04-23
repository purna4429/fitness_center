import java.util.*;

public class BookingSystem {
    private final Timetable timetable = new Timetable();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Booking> allBookings = new ArrayList<>();

    public BookingSystem() {
        preRegisterMembers();
        preLoadData();
    }

    private void preRegisterMembers() {
        for (String n : new String[]{"Aarav","Priya","Rohan","Sneha","Vikram",
                                     "Ananya","Kiran","Deepak","Meera","Arjun"})
            members.put(n, new Member(n));
    }

    private void preLoadData() {
        makeBooking("Aarav",  1);  makeBooking("Aarav",  4);  makeBooking("Aarav",  7);
        makeBooking("Priya",  1);  makeBooking("Priya",  6);  makeBooking("Priya",  10);
        makeBooking("Rohan",  2);  makeBooking("Rohan",  5);  makeBooking("Rohan",  9);
        makeBooking("Sneha",  2);  makeBooking("Sneha",  3);  makeBooking("Sneha",  8);
        makeBooking("Vikram", 3);  makeBooking("Vikram", 4);  makeBooking("Vikram", 11);
        makeBooking("Ananya", 5);  makeBooking("Ananya", 6);  makeBooking("Ananya", 12);
        makeBooking("Kiran",  7);  makeBooking("Kiran",  8);  makeBooking("Kiran",  13);
        makeBooking("Deepak", 9);  makeBooking("Deepak", 10); makeBooking("Deepak", 14);
        makeBooking("Meera",  11); makeBooking("Meera",  12); makeBooking("Meera",  15);
        makeBooking("Arjun",  13); makeBooking("Arjun",  14); makeBooking("Arjun",  16);

        makeBooking("Aarav",  25); makeBooking("Priya",  26); makeBooking("Rohan",  27);
        makeBooking("Sneha",  28); makeBooking("Vikram", 29); makeBooking("Ananya", 30);
        makeBooking("Kiran",  31); makeBooking("Deepak", 32); makeBooking("Meera",  33);
        makeBooking("Arjun",  34);

        attend("Aarav",  1,  5, "Loved the Yoga session!");
        attend("Aarav",  4,  4, "Zumba is so fun and energetic.");
        attend("Aarav",  7,  4, "Calm and focused throughout.");
        attend("Priya",  1,  4, "Very relaxing and peaceful.");
        attend("Priya",  6,  2, "Body Blitz too intense for me.");
        attend("Priya",  10, 3, "Box Fit was very challenging.");
        attend("Rohan",  2,  3, "Box Fit was tough but ok.");
        attend("Rohan",  5,  4, "Enjoyed Aquacise a lot.");
        attend("Rohan",  9,  4, "Refreshing Aquacise session.");
        attend("Sneha",  2,  4, "Good instructor, enjoyed it.");
        attend("Sneha",  8,  4, "Zumba afternoon was great.");
        attend("Vikram", 3,  5, "Body Blitz was amazing!");
        attend("Vikram", 4,  5, "Great energy in Zumba class.");
        attend("Vikram", 11, 5, "Yoga was very peaceful.");
        attend("Ananya", 5,  3, "Aquacise was decent.");
        attend("Ananya", 6,  3, "Body Blitz needs better music.");
        attend("Ananya", 12, 2, "Aquacise not really my thing.");
        attend("Kiran",  7,  5, "Yoga morning session was perfect.");
        attend("Kiran",  8,  5, "Best Zumba class ever!");
        attend("Kiran",  13, 4, "Zumba evening was great.");
        attend("Deepak", 9,  3, "Aquacise was ok.");
        attend("Deepak", 10, 4, "Box Fit evening - solid workout.");
        attend("Deepak", 14, 4, "Zumba was fun.");
        attend("Meera",  11, 4, "Really enjoyed the Yoga class.");
        attend("Meera",  12, 3, "Aquacise was average overall.");
        attend("Meera",  15, 5, "Box Fit was brilliant!");
        attend("Arjun",  13, 3, "Zumba was ok.");
        attend("Arjun",  14, 4, "Enjoyed the Zumba session.");
        attend("Arjun",  16, 5, "Yoga evening was wonderful.");
        attend("Sneha",  28, 5, "Loved Yoga this month.");
        attend("Vikram", 29, 4, "Zumba Sunday was energetic.");
    }

    private void makeBooking(String memberName, int lessonId) {
        Member m = members.get(memberName);
        Lesson l = timetable.getById(lessonId);
        if (m == null || l == null || !l.hasSpace() || m.hasConflict(l)) return;
        Booking b = new Booking(m, l);
        m.addBooking(b);
        l.incrementBookings();
        allBookings.add(b);
    }

    private void attend(String memberName, int lessonId, int rating, String comment) {
        Member m = members.get(memberName);
        Lesson l = timetable.getById(lessonId);
        if (m == null || l == null) return;
        Booking b = m.findActiveBooking(l);
        if (b == null) return;
        b.setStatus(Booking.Status.ATTENDED);
        Review r = new Review(m, l, rating, comment);
        l.addReview(r);
    }

    public Timetable getTimetable()          { return timetable; }
    public Member getMember(String name)     { return members.get(name); }
    public Collection<Member> getAllMembers(){ return members.values(); }
    public List<Booking> getAllBookings()    { return allBookings; }

    public String bookLesson(String memberName, int lessonId) {
        Member m = members.get(memberName);
        if (m == null) return "Member not found: " + memberName;
        Lesson l = timetable.getById(lessonId);
        if (l == null) return "Lesson not found: " + lessonId;
        if (!l.hasSpace()) return "FULL: This lesson is fully booked (max 4 members). Returning to main menu.";
        boolean alreadyBooked = m.getBookings().stream()
                .anyMatch(b -> b.getLesson() == l && b.getStatus() != Booking.Status.CANCELLED);
        if (alreadyBooked) return "You already have a booking for this lesson (duplicate not allowed).";
        if (m.hasConflict(l)) return "Time conflict: you already have a booking at this day and time slot.";
        Booking b = new Booking(m, l);
        m.addBooking(b);
        l.incrementBookings();
        allBookings.add(b);
        return String.format("Booking successful!%n  Booking ID : %d%n  Lesson     : %s%n  Day        : %s%n  Time       : %s%n  Price      : %.2f",
                b.getBookingId(), l.getExercise().getName(),
                l.getDay(), l.getTimeSlot(), l.getExercise().getPrice());
    }

    public String changeBooking(String memberName, int bookingId, int newLessonId) {
        Member m = members.get(memberName);
        if (m == null) return "Member not found.";
        Booking b = m.findBookingById(bookingId);
        if (b == null) return "Booking #" + bookingId + " not found for " + memberName + ".";
        if (b.getStatus() == Booking.Status.CANCELLED) return "Cannot change a cancelled booking.";
        if (b.getStatus() == Booking.Status.ATTENDED)  return "Cannot change an already attended booking.";
        Lesson newL = timetable.getById(newLessonId);
        if (newL == null) return "New lesson not found: " + newLessonId;
        if (!newL.hasSpace()) return "No space in new lesson " + newLessonId + ".";
        if (m.findActiveBooking(newL) != null) return "You already have a booking for lesson " + newLessonId + ".";
        Lesson oldL = b.getLesson();
        oldL.decrementBookings();
        b.setStatus(Booking.Status.CHANGED);
        if (m.hasConflict(newL)) {
            oldL.incrementBookings();
            b.setStatus(Booking.Status.BOOKED);
            return "Time conflict with new lesson. Booking unchanged.";
        }
        b.setLesson(newL);
        b.setStatus(Booking.Status.CHANGED);
        newL.incrementBookings();
        return "Booking #" + bookingId + " changed to: " + newL;
    }

    public String cancelBooking(String memberName, int bookingId) {
        Member m = members.get(memberName);
        if (m == null) return "Member not found.";
        Booking b = m.findBookingById(bookingId);
        if (b == null) return "Booking #" + bookingId + " not found.";
        if (b.getStatus() == Booking.Status.CANCELLED) return "Booking already cancelled.";
        if (b.getStatus() == Booking.Status.ATTENDED)  return "Cannot cancel an attended booking.";
        b.getLesson().decrementBookings();
        b.setStatus(Booking.Status.CANCELLED);
        return "Booking #" + bookingId + " cancelled.";
    }

    public String attendLesson(String memberName, int bookingId, int rating, String comment) {
        Member m = members.get(memberName);
        if (m == null) return "Member not found.";
        Booking b = m.findBookingById(bookingId);
        if (b == null) return "Booking #" + bookingId + " not found.";
        if (b.getStatus() == Booking.Status.ATTENDED) return "You have already attended this lesson.";
        if (b.getStatus() == Booking.Status.CANCELLED) return "This booking is cancelled.";
        if (rating < 1 || rating > 5) return "Rating must be between 1 and 5.";
        b.setStatus(Booking.Status.ATTENDED);
        b.getLesson().addReview(new Review(m, b.getLesson(), rating, comment));
        return "Attended successfully!\n  Lesson  : " + b.getLesson().getExercise().getName()
                + "\n  Day     : " + b.getLesson().getDay()
                + "\n  Time    : " + b.getLesson().getTimeSlot()
                + "\n  Rating  : " + rating + "/5"
                + "\n  Review  : " + comment;
    }

    public void printMonthlyLessonReport(int month) {
        List<Lesson> monthLessons = timetable.getByMonth(month);
        if (monthLessons.isEmpty()) { System.out.println("No lessons for month " + month); return; }
        System.out.println("\n====== MONTHLY LESSON REPORT — Month " + month + " ======");
        System.out.printf("%-6s %-4s %-9s %-9s %-11s | %-8s | %s%n",
                "LsnID","Wk","Day","Time","Exercise","Attended","Avg Rating");
        System.out.println("-".repeat(72));
        for (Lesson l : monthLessons) {
            long attended = l.getAttendedCount(allBookings);
            System.out.printf("%-6d %-4d %-9s %-9s %-11s | %-8d | %.1f%n",
                    l.getId(), l.getWeekNumber(), l.getDay(), l.getTimeSlot(),
                    l.getExercise().getName(), attended, l.getAverageRating());
        }
        System.out.println("=".repeat(72));
    }

    public void printMonthlyIncomeReport(int month) {
        List<Lesson> monthLessons = timetable.getByMonth(month);
        if (monthLessons.isEmpty()) { System.out.println("No lessons for month " + month); return; }
        System.out.println("\n====== MONTHLY CHAMPION REPORT — Month " + month + " ======");
        Map<String, Double> incomeMap = new LinkedHashMap<>();
        for (Lesson l : monthLessons) {
            String name = l.getExercise().getName();
            incomeMap.merge(name, l.getAttendedIncome(allBookings), Double::sum);
        }
        incomeMap.forEach((name, total) ->
                System.out.printf("  %-11s : %.2f%n", name, total));
        String champion = incomeMap.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse("N/A");
        System.out.println("\n  *** Champion: " + champion
                + " with " + String.format("%.2f", incomeMap.get(champion)) + " ***");
        System.out.println("=".repeat(50));
    }
}
