import java.io.*;
public class ValidInputChecker {
    public static boolean isValidCityName(String city, String latDegree, String latMinutes,
                                          String longDegree, String longMinutes) {
        try {
            for (Stop s : TripPlanner.possibleStops.getItems()) {
                if (s.getCity().equalsIgnoreCase(city)) {
                    return false;
                }
            }}catch (NullPointerException npe) {
        } catch (NumberFormatException nfe) {
        }
        return true;
    }

    public static boolean isValidLatitudeDegree(String latDegree) {
        try {
            double latitudeDegree = Double.parseDouble(latDegree);
            if (latitudeDegree >= 25 && latitudeDegree <= 50) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidMinutes(String stringMinutes) {
        try {
            double minutes = Double.parseDouble(stringMinutes);
            if (minutes >= 0 && minutes <= 59) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidLongitudeDegree(String longDegree) {
        try {
            double longitudeDegree = Double.parseDouble(longDegree);
            if (longitudeDegree >= 65 && longitudeDegree <= 125) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidFileName(String fileName) {
        File folder = new File(System.getProperty("user.dir"));
        File[] listOfFiles = folder.listFiles();
        System.out.println();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(fileName + ".txt")) {
                    return false;
                }
            }
        }
        return true;
    }
}

// public boolean isValidCity(String city) {
//   for (Stop s : TripPlanner.possibleStops) {
//     if (s.getCity().equalsIgnoreCase(city)) {
//         return true;
//      }
//   }
//     return false;
// }