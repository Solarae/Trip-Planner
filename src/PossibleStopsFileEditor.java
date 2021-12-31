import java.io.*;
import java.util.*;
public class PossibleStopsFileEditor {
    public static void checkExistenceOfPossibleStopsFile() {
        File f = new File("PossibleStopsDataFile.txt");
        if (f.exists()) {
            loadStopArrayListFromTextFile();
        } else {
            initStopArrayList();
            initPossibleStopsFile();
        }
    }
    public static void initPossibleStopsFile() {
        try {
            FileWriter fileWriter = new FileWriter("PossibleStopsDataFile.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < TripPlanner.possibleStops.getItems().size(); i++) {
                printWriter.write(TripPlanner.possibleStops.getItems().get(i).toFile());
            }
            printWriter.close();

        } catch (IOException ioe) {
            System.out.println("File not found");
        }
    }

    public static void initStopArrayList() {
        TripPlanner.possibleStops.getItems().add(new Stop("Austin", "TX",
                30, 15, 97, 44));
        TripPlanner.possibleStops.getItems().add(new Stop("Boston", "MA",
                42, 21, 71, 3));
        TripPlanner.possibleStops.getItems().add(new Stop("Dallas", "TX",
                32, 51, 96, 51));
        TripPlanner.possibleStops.getItems().add(new Stop("Denver", "CO",
                39, 44, 104, 54));
        TripPlanner.possibleStops.getItems().add(new Stop("El Paso", "TX",
                31, 45, 106, 29));
        TripPlanner.possibleStops.getItems().add(new Stop("Houston", "TX",
                29, 59, 95, 22));
        TripPlanner.possibleStops.getItems().add(new Stop("Indianapolis", "IN",
                39, 46, 86, 9));
        TripPlanner.possibleStops.getItems().add(new Stop("Jacksonville", "FL",
                30, 19, 81, 39));
        TripPlanner.possibleStops.getItems().add(new Stop("Los Angeles", "CA",
                33, 56, 118, 24));
        TripPlanner.possibleStops.getItems().add(new Stop("New Orleans", "LA",
                29, 57, 90, 4));
    }

    public static void writeStopArrayListToFile () {
        try {
            new PrintWriter("PossibleStopsDataFile.txt").close();
            FileWriter fileWriter = new FileWriter("PossibleStopsDataFile.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < TripPlanner.possibleStops.getItems().size(); i++) {
                printWriter.write(TripPlanner.possibleStops.getItems().get(i).toFile());
            }
            printWriter.close();
        } catch (IOException ioe) {
            System.out.println("File not found");
        }
    }

    public static void loadStopArrayListFromTextFile () {
        try {
            File file = new File("PossibleStopsDataFile.txt");
            Reader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                try {
                    String[] stopData = line.split("\\|");
                    String cityName = stopData[0];
                    String stateName = stopData[1];
                    double latDegrees = Double.parseDouble(stopData[2].substring(0,stopData[2].lastIndexOf("\u00b0")));
                    double latMinutes = Double.parseDouble(stopData[3].substring(0,stopData[3].lastIndexOf("'")));
                    double longDegrees = Double.parseDouble(stopData[4].substring(0,stopData[4].lastIndexOf("\u00b0")));
                    double longMinutes = Double.parseDouble(stopData[5].substring(0,stopData[5].lastIndexOf("'")));
                    TripPlanner.possibleStops.getItems().add(new Stop(cityName, stateName, latDegrees, latMinutes,
                            longDegrees, longMinutes));
                    line = bufferedReader.readLine();
                } catch (EOFException eof) {
                    eof.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            System.out.println("File not found");
        }
    }

    public static void sort() {
        try {
            Collections.sort(TripPlanner.possibleStops.getItems());
        } catch (NullPointerException npe) {

        }

    }
}

