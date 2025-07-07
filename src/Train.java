package com.trainreservation;

import java.util.List;

public class Train {
    private String trainId;
    private String trainName;
    private List<Station> route;
    private int totalSeats;
    private int availableSeats; 

    public Train(String trainId, String trainName, List<Station> route, int totalSeats) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.totalSeats = totalSeats;
       
    }

    public String getTrainId() { return trainId; }
    public String getTrainName() { return trainName; }
    public List<Station> getRoute() { return route; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

   
    public boolean hasStation(String stationId) {
        return route.stream().anyMatch(s -> s.getStationId().equals(stationId));
    }

    @Override
    public String toString() {
        return String.format("Train ID: %s, Name: %s, Available Seats: %d, Route: %s",
                trainId, trainName, availableSeats, route.stream().map(Station::getStationName).toList());
    }
}