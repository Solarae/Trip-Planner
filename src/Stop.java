

public class Stop implements Comparable<Stop> {
    private String city;
    private String state;
    private double latDegree;
    private double latMinutes;
    ///private String latDirection;
    private double longDegree;
    private double longMinutes;
    //private String longDirection;
    private final int EARTH_RADIUS = 6371;
    private final double RADIAN_FACTOR = 180/Math.PI;
    private double circleXCoordinate;
    private double circleYCoordinate;

    public Stop() {}
    public Stop(String nameOfStop, String nameOfState, double latitudeDegree, double latitudeMinutes,
                double longitudeDegree, double longitudeMinutes) {
        city = nameOfStop;
        state = nameOfState;
        latDegree = latitudeDegree;
        latMinutes =latitudeMinutes;
        longDegree = longitudeDegree;
        longMinutes = longitudeMinutes;
        double imageWidth = 735;
        double lon = longDegree + (longMinutes / 60);
        double pixelsPerLong = (imageWidth) / (125 - 65);
        circleXCoordinate = (imageWidth - (((lon - 65)) * pixelsPerLong));
        double imageHeight = 380;
        double lat = latDegree + (latMinutes / 60);
        double pixelsPerLat = (imageHeight) / (50 - 25);
        circleYCoordinate = (imageHeight - (((lat - 25)) * pixelsPerLat));
    }

    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public double getLatDegree() {
        return latDegree;
    }
    public double getLatMinutes() {
        return latMinutes;
    }
    public double getLongDegree() {
        return longDegree;
    }
    public double getLongMinutes() {
        return longMinutes;
    }
    public double getCircleXCoordinate() {
        return circleXCoordinate;
    }
    public double getCircleYCoordinate() {
        return circleYCoordinate;
    }

    public void setCity(String newCity) {
        city = newCity;
    }
    public void setState(String newState) { state = newState; }
    public void setLatDegree(String newLatDegree) { latDegree = Double.parseDouble(newLatDegree); }
    public void setLatMinutes(String newLatMinutes) { latMinutes = Double.parseDouble(newLatMinutes); }
    public void setLongDegree(String newLongDegree) {longDegree = Double.parseDouble(newLongDegree); }
    public void setLongMinutes(String newLongMinutes) {
        longMinutes = Double.parseDouble(newLongMinutes);
    }

    @Override
    public int compareTo(Stop comparedStop) {
        return city.toUpperCase().compareTo(comparedStop.getCity().toUpperCase());
    }

    public String toString() {
        return city + ", " + state;
    }

    public String toFile() {
        return city + "|" + state + "|" + latDegree + "\u00b0" + "|" + latMinutes + "'" + "|"
                + longDegree + "\u00b0" + "|" + longMinutes + "'" + "|" + "\n";
    }

    public double CalculateDistance(Stop otherStop) {
        if (city.equalsIgnoreCase(otherStop.getCity())) {
            return 0;
        } else {
            double thisLat = (latDegree + (latMinutes / 60));
            double otherLat = (otherStop.getLatDegree() + (otherStop.getLatMinutes() / 60));
            double thisLong = (longDegree + (longMinutes/60));
            double otherLong = (otherStop.getLongDegree() + (otherStop.getLongMinutes() / 60));
            double x = (Math.sin(thisLat/RADIAN_FACTOR) * Math.sin(otherLat/RADIAN_FACTOR))
                    + (Math.cos(thisLat/RADIAN_FACTOR) * Math.cos(otherLat/RADIAN_FACTOR) *
                    Math.cos(thisLong/RADIAN_FACTOR - otherLong/RADIAN_FACTOR));
            return EARTH_RADIUS * Math.atan(Math.sqrt((1 - Math.pow(x, 2)))/x);
        }
    }
}
