import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Timetable {
    private final List<Lesson> lessons = new ArrayList<>();

    public Timetable() { buildTimetable(); }

    private void buildTimetable() {
        Exercise yoga      = new Exercise("Yoga",       10.00);
        Exercise zumba     = new Exercise("Zumba",      12.00);
        Exercise aquacise  = new Exercise("Aquacise",    9.00);
        Exercise boxFit    = new Exercise("Box Fit",    15.00);
        Exercise bodyBlitz = new Exercise("Body Blitz", 13.00);

        Exercise[][] satPattern = {
            {yoga,       zumba,      aquacise  },
            {boxFit,     bodyBlitz,  yoga      },
            {zumba,      aquacise,   boxFit    },
            {bodyBlitz,  yoga,       zumba     },
            {aquacise,   boxFit,     bodyBlitz },
            {yoga,       zumba,      aquacise  },
            {boxFit,     bodyBlitz,  yoga      },
            {zumba,      aquacise,   boxFit    }
        };
        Exercise[][] sunPattern = {
            {boxFit,     bodyBlitz,  yoga      },
            {zumba,      aquacise,   boxFit    },
            {bodyBlitz,  yoga,       zumba     },
            {aquacise,   boxFit,     bodyBlitz },
            {yoga,       zumba,      aquacise  },
            {boxFit,     bodyBlitz,  yoga      },
            {zumba,      aquacise,   boxFit    },
            {bodyBlitz,  yoga,       zumba     }
        };

        String[] slots = {"Morning", "Afternoon", "Evening"};

        for (int w = 1; w <= 8; w++) {
            int month = (w <= 4) ? 1 : 2;
            for (int s = 0; s < 3; s++) {
                lessons.add(new Lesson(satPattern[w-1][s], "Saturday", slots[s], w, month));
                lessons.add(new Lesson(sunPattern[w-1][s], "Sunday",   slots[s], w, month));
            }
        }
    }

    public List<Lesson> getAllLessons() { return lessons; }

    public List<Lesson> getByDay(String day) {
        return lessons.stream()
                .filter(l -> l.getDay().equalsIgnoreCase(day))
                .collect(Collectors.toList());
    }

    public List<Lesson> getByExercise(String name) {
        return lessons.stream()
                .filter(l -> l.getExercise().getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Lesson> getByMonth(int month) {
        return lessons.stream()
                .filter(l -> l.getMonth() == month)
                .collect(Collectors.toList());
    }

    public Lesson getById(int id) {
        return lessons.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }
}
