import javafx.scene.control.ListView;
import java.text.DecimalFormat;
import java.io.*;


public class TripFileEditor {

    public static void writeStopArrayListToFile(String fileName, ListView<Stop> tripStops) {
        try {
            new PrintWriter(fileName).close();
            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < tripStops.getItems().size(); i++) {
                printWriter.write(tripStops.getItems().get(i).toFile());
            }
            printWriter.close();
        } catch (IOException ioe) {
            System.out.println("File not found");
        }
    }

    public static void loadStopArrayListFromTextFile(File file, ListView<Stop> tripStops) {
        try {
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
                    tripStops.getItems().add(new Stop(cityName, stateName, latDegrees, latMinutes,
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


    public static String totalMileage(ListView<Stop> tripStops) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        double totalMileage = 0;
        if (tripStops.getItems().size() > 1) {
            for (int d = 0; d < tripStops.getItems().size() - 1; d++) {
                totalMileage += (tripStops.getItems().get(d).CalculateDistance(tripStops.getItems().get(d + 1))) / 1.609;
            }
        }
        return formatter.format(totalMileage);
    }
}
