
package com.trainreservation;

public class Ticket {
    private String ticketId;
    private String passengerName;
    private Train train;
    private Station source;
    private Station destination;
    private int numSeats;

    public Ticket(String ticketId, String passengerName, Train train, Station source, Station destination, int numSeats) {
        this.ticketId = ticketId;
        this.passengerName = passengerName;
        this.train = train;
        this.source = source;
        this.destination = destination;
        this.numSeats = numSeats;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Train getTrain() {
        return train;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
    }

    public int getNumSeats() {
        return numSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + "'" +
                ", passengerName='" + passengerName + "'" +
                ", train=" + train.getTrainName() +
                ", source=" + source.getStationName() +
                ", destination=" + destination.getStationName() +
                ", numSeats=" + numSeats +
                '}';
    }
}
