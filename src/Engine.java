import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    Environment env;
    Random rand;

    public Engine(Environment env) {
        this.env = env;
        this.rand = new Random(System.currentTimeMillis());
    }

    void run() throws IOException {
        double min = 10000;
        for (int j = 0; j < 15; j++) {
            Schedule schedule = new Schedule(env.copyStations(), env.copyStudents(), env.capacity, env.minBusCount, 8, env.maxDistance);
            schedule.removeRedundant();
            for (int i = 0; i < 2000; i++) {
                schedule.buildRoutesRandom();
                // schedule.bestIntraRouteSchedule();
                if (schedule.distributeStudentsRandom()) {
                    if (schedule.distance() < min) {
                        min = schedule.distance;
                        System.out.println(j + "-" + i + ": " + min);
                        //writeToFile(schedule, students);
                    }
                }
            }
        }


        System.out.println(min);
    }

    List<String> getStudentLines(Map<Integer, Student> students) {
        return students.values().stream().map(student -> student.id + " " + student.pickedStation).collect(Collectors.toList());
    }

    void writeToFile(Schedule schedule, Map<Integer, Student> students) throws IOException {
        List<String> scheduleLines = schedule.getLines();
        scheduleLines.set(scheduleLines.size() - 1, scheduleLines.get(scheduleLines.size() - 1) + "\n");
        List<String> studentLines = getStudentLines(students);
        Path file = Paths.get("out.txt");
        Files.write(file, scheduleLines, Charset.forName("UTF-8"));
        Files.write(file, studentLines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    }

}
