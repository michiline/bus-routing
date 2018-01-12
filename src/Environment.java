import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Environment {

    double maxDistance;
    int capacity;
    Map<Integer, Station> stations;
    Map<Integer, Student> students;
    Station school;
    int minBusCount;

    void parseFile(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {

            stations = new HashMap<>();
            students = new HashMap<>();
            String[] params = in.readLine().trim().split(",");

            int stationCount = Integer.parseInt(params[0].split(" ")[0]);
            int studentCount = Integer.parseInt(params[1].split(" ")[1]);
            maxDistance = Double.parseDouble(params[2].split(" ")[1]);
            capacity = Integer.parseInt(params[3].split(" ")[1]);

            in.readLine();

            params = in.readLine().trim().split("\t");
            int id = Integer.parseInt(params[0]);
            double x = Double.parseDouble(params[1]);
            double y = Double.parseDouble(params[2]);

            school = new Station(id, x, y);

            stations = parseStations(in, stationCount);
            in.readLine();
            in.readLine();
            students = parseStudents(in, studentCount);
            minBusCount = (int) Math.ceil(students.size() / capacity);
            findFeasible(stations, students);
            schoolDistances();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Map<Integer, Station> parseStations(BufferedReader in, int count) throws IOException {
        Map<Integer, Station> stations = new HashMap<>();
        for (int i = 0; i < count - 1; i++) {
            String[] params = in.readLine().trim().split("\t");
            int id = Integer.parseInt(params[0]);
            double x = Double.parseDouble(params[1]);
            double y = Double.parseDouble(params[2]);
            stations.put(id, new Station(id, x, y));
        }
        return stations;
    }

    Map<Integer, Student> parseStudents(BufferedReader in, int count) throws IOException {
        Map<Integer, Student> students = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String[] params = in.readLine().trim().split("\t");
            int id = Integer.parseInt(params[0]);
            double x = Double.parseDouble(params[1]);
            double y = Double.parseDouble(params[2]);
            students.put(id, new Student(id, x, y));
        }
        return students;
    }

    void findFeasible(Map<Integer, Station> stations, Map<Integer, Student> students) {
        stations.forEach((stationId, station) -> students.forEach((studentId, student) -> {
            if (station.distanceToStudent(student) <= maxDistance) {
                student.stations.put(stationId, stationId);
                station.students.put(studentId, studentId);
            }
        }));
    }

    void schoolDistances() {
        stations.forEach((stationId, station) -> station.schoolDistance = station.distanceToStation(school));
    }

    Map<Integer, Student> copyStudents() {
        Map<Integer, Student> studentsCopy = new HashMap<>();
        students.forEach((id, student) -> studentsCopy.put(id, student.copy()));
        return studentsCopy;
    }

    Map<Integer, Station> copyStations() {
        Map<Integer, Station> stationsCopy = new HashMap<>();
        stations.forEach((id, station) -> stationsCopy.put(id, station.copy()));
        return stationsCopy;
    }
}
