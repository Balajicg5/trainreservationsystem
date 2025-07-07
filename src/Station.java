
package com.trainreservation;

public class Station {
    private String stationId;
    private String stationName;

    public Station(String stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationId='" + stationId + "'" +
                ", stationName='" + stationName + "'" +
                '}';
    }
}
