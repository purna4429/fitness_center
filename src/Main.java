import java.util.List;
import java.util.Scanner;

public class Main {
    private static final BookingSystem system = new BookingSystem();
    private static final Scanner sc = new Scanner(System.in);

    private static final String[] MEMBER_NAMES = {
        "Aarav", "Priya", "Rohan", "Sneha", "Vikram",
        "Ananya", "Kiran", "Deepak", "Meera", "Arjun"
    };

    public static void main(String[] args) {
        System.out.println("Furzefield Leisure Centre - Group Exercise Booking System");
        System.out.println("----------------------------------------------------------");
        printMemberList();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> bookLesson();
                case "2" -> changeOrCancelBooking();
                case "3" -> attendLesson();
                case "4" -> monthlyLessonReport();
                case "5" -> monthlyChampionReport();
                case "6" -> viewMyBookings();
                case "0" -> running = false;
                default  -> System.out.println("Invalid option. Please enter 0-6.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMemberList() {
        System.out.println("\nPre-registered Members:");
        for (int i = 0; i < MEMBER_NAMES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + MEMBER_NAMES[i]);
        }
        System.out.println();
    }

    private static void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Book a group exercise lesson");
        System.out.println("2. Change / Cancel a booking");
        System.out.println("3. Attend a lesson (write review)");
        System.out.println("4. Monthly lesson report");
        System.out.println("5. Monthly champion exercise report");
        System.out.println("6. View my bookings");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    // 1. Book a lesson
    private static void bookLesson() {
        String memberName = promptMember();
        if (memberName == null) return;

        System.out.println("\nBrowse timetable by:");
        System.out.println("1. Day (Saturday / Sunday)");
        System.out.println("2. Exercise type");
        System.out.print("Choice: ");
        String browse = sc.nextLine().trim();

        List<Lesson> lessons;
        if (browse.equals("1")) {
            System.out.print("Enter day (Saturday / Sunday): ");
            lessons = system.getTimetable().getByDay(sc.nextLine().trim());
        } else if (browse.equals("2")) {
            System.out.print("Enter exercise (Yoga / Zumba / Aquacise / Box Fit / Body Blitz): ");
            lessons = system.getTimetable().getByExercise(sc.nextLine().trim());
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        if (lessons.isEmpty()) { System.out.println("No lessons found."); return; }
        System.out.println("\nAvailable Lessons:");
        printLessonList(lessons);

        System.out.print("\nEnter Lesson ID to book (0 to go back): ");
        int id = readInt();
        if (id == 0) return;
        String result = system.bookLesson(memberName, id);
        System.out.println("\n" + result);
        // If full, return to main menu immediately (spec requirement)
        if (result.startsWith("FULL:")) {
            System.out.println("Returning to main menu.");
        }
    }

    // 2. Change / Cancel a booking
    private static void changeOrCancelBooking() {
        String memberName = promptMember();
        if (memberName == null) return;

        Member m = system.getMember(memberName);
        List<Booking> myBookings = m.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED
                          || b.getStatus() == Booking.Status.CHANGED)
                .toList();
        if (myBookings.isEmpty()) { System.out.println("No active bookings to change or cancel."); return; }

        System.out.println("\nYour bookings:");
        printBookingList(myBookings);

        System.out.print("\nEnter Booking ID to manage (0 to go back): ");
        int bookingId = readInt();
        if (bookingId == 0) return;

        Booking selected = m.findBookingById(bookingId);
        if (selected == null || !myBookings.contains(selected)) {
            System.out.println("Booking ID not found.");
            return;
        }

        System.out.println("1. Change to a different lesson");
        System.out.println("2. Cancel this booking");
        System.out.print("Choice: ");
        String action = sc.nextLine().trim();

        if (action.equals("1")) {
            System.out.println("\nBrowse new lesson by:");
            System.out.println("1. Day    2. Exercise type");
            System.out.print("Choice: ");
            String browse = sc.nextLine().trim();
            List<Lesson> lessons;
            if (browse.equals("1")) {
                System.out.print("Enter day (Saturday / Sunday): ");
                lessons = system.getTimetable().getByDay(sc.nextLine().trim());
            } else {
                System.out.print("Enter exercise (Yoga / Zumba / Aquacise / Box Fit / Body Blitz): ");
                lessons = system.getTimetable().getByExercise(sc.nextLine().trim());
            }
            if (lessons.isEmpty()) { System.out.println("No lessons found."); return; }
            printLessonList(lessons);
            System.out.print("\nEnter new Lesson ID (0 to go back): ");
            int newId = readInt();
            if (newId == 0) return;
            System.out.println(system.changeBooking(memberName, bookingId, newId));
        } else if (action.equals("2")) {
            System.out.println(system.cancelBooking(memberName, bookingId));
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // 3. Attend a lesson
    private static void attendLesson() {
        String memberName = promptMember();
        if (memberName == null) return;

        Member m = system.getMember(memberName);
        List<Booking> active = m.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED
                          || b.getStatus() == Booking.Status.CHANGED)
                .toList();

        if (active.isEmpty()) { System.out.println("No active bookings to attend."); return; }

        System.out.println("\nYour active bookings:");
        printBookingList(active);

        System.out.print("\nEnter Booking ID to attend (0 to go back): ");
        int bookingId = readInt();
        if (bookingId == 0) return;

        System.out.println("Rating: 1=Very dissatisfied  2=Dissatisfied  3=Ok  4=Satisfied  5=Very satisfied");
        System.out.print("Your rating (1-5): ");
        int rating = readInt();
        System.out.print("Your review: ");
        String comment = sc.nextLine().trim();

        System.out.println(system.attendLesson(memberName, bookingId, rating, comment));
    }

    // 4. Monthly lesson report
    private static void monthlyLessonReport() {
        System.out.print("Enter month number (1 or 2): ");
        int month = readInt();
        system.printMonthlyLessonReport(month);
    }

    // 5. Monthly champion report
    private static void monthlyChampionReport() {
        System.out.print("Enter month number (1 or 2): ");
        int month = readInt();
        system.printMonthlyIncomeReport(month);
    }

    // 6. View my bookings
    private static void viewMyBookings() {
        String memberName = promptMember();
        if (memberName == null) return;
        Member m = system.getMember(memberName);

        List<Booking> attended = m.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.ATTENDED)
                .toList();
        List<Booking> upcoming = m.getBookings().stream()
                .filter(b -> b.getStatus() == Booking.Status.BOOKED
                          || b.getStatus() == Booking.Status.CHANGED)
                .toList();

        if (attended.isEmpty() && upcoming.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        if (!attended.isEmpty()) {
            System.out.println("\nAttended lessons:");
            printBookingList(attended);
        }
        if (!upcoming.isEmpty()) {
            System.out.println("\nUpcoming bookings:");
            printBookingList(upcoming);
        }
    }

    // Helpers

    private static void printLessonList(List<Lesson> lessons) {
        System.out.printf("%-6s  %-11s  %-9s  %-9s  %-5s  %-8s  %s%n",
                "ID", "Exercise", "Day", "Time", "Week", "Spaces", "Price(GBP)");
        System.out.println("------------------------------------------------------------");
        for (Lesson l : lessons) {
            String spaces = l.hasSpace() ? l.getSpacesLeft() + " left" : "FULL";
            System.out.printf("%-6d  %-11s  %-9s  %-9s  %-5d  %-8s  %.2f%n",
                    l.getId(),
                    l.getExercise().getName(),
                    l.getDay(),
                    l.getTimeSlot(),
                    l.getWeekNumber(),
                    spaces,
                    l.getExercise().getPrice());
        }
    }

    private static void printBookingList(List<Booking> bookings) {
        System.out.printf("%-8s  %-11s  %-9s  %-9s  %s%n",
                "Bkg ID", "Lesson", "Day", "Time", "Status");
        System.out.println("------------------------------------------------------");
        for (Booking b : bookings) {
            Lesson l = b.getLesson();
            System.out.printf("%-8d  %-11s  %-9s  %-9s  %s%n",
                    b.getBookingId(),
                    l.getExercise().getName(),
                    l.getDay(),
                    l.getTimeSlot(),
                    b.getStatus());
        }
    }

    private static String promptMember() {
        System.out.println("\nPre-registered Members:");
        for (int i = 0; i < MEMBER_NAMES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + MEMBER_NAMES[i]);
        }
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (system.getMember(name) == null) {
            System.out.println("Member '" + name + "' not found.");
            return null;
        }
        return name;
    }

    private static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Defaulting to 0.");
            return 0;
        }
    }
}
