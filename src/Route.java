import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Route {

    int id;
    List<Station> stations;
    int capacity;
    int filledCapacity;
    double distance;

    public Route(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.filledCapacity = 0;
        this.stations = new ArrayList<>();
    }

    boolean canAddStudent() {
        return filledCapacity + 1 <= capacity;
    }

    void addStudent(Station station, Student student) {
        filledCapacity += 1;
        student.pickedStation = station.id;
        station.pickedStudents.put(student.id, student.id);
    }

    double distance(List<Station> stations) {
        double distance = 0;
        Station firstStation = stations.get(0);
        Station lastStation = stations.get(stations.size() - 1);
        distance += firstStation.schoolDistance;
        distance += lastStation.schoolDistance;
        for (int i = 0; i < stations.size() - 1; i++) {
            distance += stations.get(i).distanceToStation(stations.get(i + 1));
        }
        this.distance = distance;
        return distance;
    }

    void findBestSchedule() {
        if (stations.size() >= 8) {
            return;
        }
        double minDistance = 100000;
        List<List<Station>> permutations = permute(stations);
        for (List<Station> permutation : permutations) {
            double distance = distance(permutation);
            if (distance < minDistance) {
                minDistance = distance;
                stations = permutation;
            }
        }
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        return stations.stream().map(station -> String.valueOf(station.id)).collect(Collectors.joining(" "));
    }

    public List<List<Station>> permute(List<Station> routes) {
        List<List<Station>> result = new ArrayList<>();

        //start from an empty list
        result.add(new ArrayList<>());

        for (int i = 0; i < routes.size(); i++) {
            //list of list in current iteration of the array num
            List<List<Station>> current = new ArrayList<>();

            for (List<Station> l : result) {
                // # of locations to insert is largest index + 1
                for (int j = 0; j < l.size() + 1; j++) {
                    // + add num[i] to different locations
                    l.add(j, routes.get(i));

                    List<Station> temp = new ArrayList<>(l);
                    current.add(temp);

                    //System.out.println(temp);

                    // - remove num[i] add
                    l.remove(j);
                }
            }

            result = new ArrayList<>(current);
        }

        return result;
    }
}
