import java.util.*;
import java.util.stream.Collectors;

public class Schedule {

    double distance;
    List<Route> routes;
    Map<Integer, Station> stations;
    Map<Integer, Student> students;
    Random rand;
    int capacity;
    int minBusCount;
    int extraBuses;
    double maxDistance;

    public Schedule(Map<Integer, Station> stations, Map<Integer, Student> students, int capacity, int minBusCount, int extraBuses, double maxDistance) {
        this.stations = stations;
        this.students = students;
        this.capacity = capacity;
        this.minBusCount = minBusCount;
        this.extraBuses = extraBuses;
        this.maxDistance = maxDistance;
        this.rand = new Random(System.currentTimeMillis());
    }


    void removeRedundant() {
        Set<Integer> selectedStationIds = new HashSet<>(stations.keySet());
        stations.forEach((stationId, station) -> {
            boolean redundant = true;
            for (int studentId : station.students.values()) {
                if (students.get(studentId).stations.values().size() == 1) {
                    redundant = false;
                    break;
                }
            }
            if (redundant && rand.nextDouble() < 0.5) {
                selectedStationIds.remove(stationId);
                station.students.values().forEach(studentId -> {
                    students.get(studentId).stations.remove(stationId);
                });
            }
        });
        stations = stations.entrySet().stream().filter(entry -> selectedStationIds.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    void buildRoutesRandom() {
        routes = new ArrayList<>();
        List<Station> leftoverStations = stations.values().stream().map(Station::copy).collect(Collectors.toList());
        routes.add(new Route(0, capacity));
        Station currentStation;

        while (leftoverStations.size() > 0) {
            if (leftoverStations.size() == 1) {
                currentStation = leftoverStations.get(0);
            } else {
                currentStation = leftoverStations.get(rand.nextInt(leftoverStations.size() - 1));
            }
            if (leftoverStations.size() == stations.size()) {
                routes.get(0).stations.add(currentStation);
                leftoverStations.remove(currentStation);
                continue;
            }
            double operation = rand.nextDouble();
            if (operation < randFactor(routes.size(), minBusCount, extraBuses)) {
                Route newRoute = new Route(routes.size(), capacity);
                newRoute.stations.add(currentStation);
                routes.add(newRoute);
            } else {
                int randRoute = rand.nextInt(routes.size() - 1);
                routes.get(randRoute).stations.add(currentStation);
            }
            leftoverStations.remove(currentStation);
        }
    }

    boolean distributeStudentsRandom() {
        // map station to route
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            for (Station station : route.stations) {
                stations.get(station.id).routeId = route.id;
            }
        }
        List<Student> leftoverStudents = students.values().stream().map(Student::copy).collect(Collectors.toList());
        while (leftoverStudents.size() > 0) {
            // System.out.println(leftoverStudents.size());
            // find student with min possible stations
            Student minStudent = findMinStudent(leftoverStudents);
            if (minStudent.stations.size() == 0) {
                return false;
            }
            List<Integer> studentStations = new ArrayList<>(minStudent.stations.values());
            int randStationId = studentStations.get(rand.nextInt(minStudent.stations.size()));
            Station randStation = stations.get(randStationId);
            routes.get(randStation.routeId).addStudent(randStation, minStudent);
            // assign station to student and vice versa
            leftoverStudents.remove(minStudent);
            if (!routes.get(randStation.routeId).canAddStudent()) {
                removeRoute(randStation.routeId, leftoverStudents);
            }
        }
        return true;
    }

    List<Route> permuteRoute() {
        List<Route> newRoutes = new ArrayList<>(routes);
        int randRoute1 = rand.nextInt(routes.size());
        int randRoute2 = rand.nextInt(routes.size());
        while (randRoute1 == randRoute2) {
            randRoute2 = rand.nextInt(routes.size());
        }
        Route route1 = newRoutes.get(randRoute1);
        Route route2 = newRoutes.get(randRoute2);
        int randStation1 = rand.nextInt(route1.stations.size());
        int randStation2 = rand.nextInt(route2.stations.size());
        Station temp = route1.stations.get(randStation1);
        route1.stations.set(randStation1, route2.stations.get(randStation2));
        route2.stations.set(randStation2, temp);
        return newRoutes;
    }

    void bestIntraRouteSchedule() {
        routes.forEach(Route::findBestSchedule);
    }

    double distance() {
        double distance = 0;
        for (Route route : routes) {
            distance += route.distance(route.stations);
        }
        this.distance = distance;
        return distance;
    }

    Student findMinStudent(Collection<Student> students) {
        Student minStudent = students.iterator().next();
        for (Student student : students) {
            if (student.stations.size() < minStudent.stations.size()) {
                minStudent = student;
            }
        }
        return minStudent;
    }

    void removeRoute(Integer routeId, List<Student> students) {
        students.forEach(student -> {
            Map<Integer, Integer> newStations = new HashMap<>(student.stations);
            for (int stationId : student.stations.values()) {
                if (stations.get(stationId).routeId == routeId) {
                    newStations.remove(stationId);
                }
            }
            student.stations = newStations;
        });
    }

    double randFactor(int routesCount, int minBusCount, int extraBusses) {
        return 1 - (routesCount / (minBusCount + extraBusses));
    }

    public List<String> getLines() {
        return routes.stream().map(Route::toString).collect(Collectors.toList());
    }
}
