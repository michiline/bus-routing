import java.util.HashMap;
import java.util.Map;

public class Station {

    int id;
    int routeId;
    double x;
    double y;
    Map<Integer, Integer> students;
    Map<Integer, Integer> pickedStudents;
    double schoolDistance;

    public Station(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.students = new HashMap<>();
        this.pickedStudents = new HashMap<>();
    }

    public Station(int id, int routeId, double x, double y, Map<Integer, Integer> students, Map<Integer, Integer> pickedStudents, double schoolDistance) {
        this.id = id;
        this.routeId = routeId;
        this.x = x;
        this.y = y;
        this.students = students;
        this.pickedStudents = pickedStudents;
        this.schoolDistance = schoolDistance;
    }

    Station copy() {
        return new Station(id, routeId, x, y, new HashMap<>(students), new HashMap<>(pickedStudents), schoolDistance);
    }

    @Override
    public String toString() {
        return "id: " + id + ", route: " + routeId + ", students:  " + pickedStudents.size() + "/" + students.size();
    }

    double distanceToStudent(Student student) {
        return Math.sqrt(Math.pow(x - student.x, 2) + Math.pow(y - student.y, 2));
    }

    double distanceToStation(Station station) {
        return Math.sqrt(Math.pow(x - station.x, 2) + Math.pow(y - station.y, 2));
    }
}
