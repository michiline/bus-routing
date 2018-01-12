import java.util.HashMap;
import java.util.Map;

public class Student {
    int id;
    double x;
    double y;
    Map<Integer, Integer> stations;
    int pickedStation;


    public Student(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.stations = new HashMap<>();
    }

    public Student(int id, double x, double y, Map<Integer, Integer> stations, int pickedStation) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.stations = stations;
        this.pickedStation = pickedStation;
    }

    Student copy() {
        return new Student(id, x, y, new HashMap<>(stations), pickedStation);
    }

    @Override
    public String toString() {
        return "id: " + id +", station: " + pickedStation;
    }
}
