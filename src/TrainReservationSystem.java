package com.trainreservation;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TrainReservationSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Train Reservation System (DB Mode) ---");
            System.out.println("1. Find Trains Between Stations");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Cancel a Ticket");
            System.out.println("4. View Ticket Details");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    findTrains(scanner);
                    break;
                case 2:
                    bookTicket(scanner);
                    break;
                case 3:
                    cancelTicket(scanner);
                    break;
                case 4:
                    viewTicket(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void findTrains(Scanner scanner) {
        System.out.println("\n--- Available Stations ---");
        List<Station> stations = DatabaseManager.getAllStations();
        stations.forEach(System.out::println);

        System.out.print("\nEnter source station ID: ");
        String sourceId = scanner.nextLine();
        System.out.print("Enter destination station ID: ");
        String destId = scanner.nextLine();

        List<Train> allTrains = DatabaseManager.getAllTrains();
        List<Train> matchingTrains = allTrains.stream()
                .filter(train -> train.hasStation(sourceId) && train.hasStation(destId))
                .collect(Collectors.toList());

        if (matchingTrains.isEmpty()) {
            System.out.println("No trains found for the given route.");
        } else {
            System.out.println("\n--- Available Trains ---");
            matchingTrains.forEach(System.out::println);
        }
    }

    private static void bookTicket(Scanner scanner) {
        System.out.print("Enter your name: ");
        String passengerName = scanner.nextLine();
        System.out.print("Enter train ID: ");
        String trainId = scanner.nextLine();
        System.out.print("Enter source station ID: ");
        String sourceId = scanner.nextLine();
        System.out.print("Enter destination station ID: ");
        String destId = scanner.nextLine();
        System.out.print("Enter number of seats: ");
        int numSeats = Integer.parseInt(scanner.nextLine());

        Train train = DatabaseManager.getAllTrains().stream().filter(t -> t.getTrainId().equals(trainId)).findFirst().orElse(null);

        if (train == null) {
            System.out.println("Invalid Train ID.");
            return;
        }

        if (train.getAvailableSeats() >= numSeats) {
            String ticketId = DatabaseManager.bookTicket(passengerName, trainId, sourceId, destId, numSeats);
            if (ticketId != null) {
                System.out.println("Booking successful! Your ticket ID is: " + ticketId);
            } else {
                System.out.println("Booking failed. Please try again.");
            }
        } else {
            System.out.println("Not enough seats available. Adding to waitlist.");
            DatabaseManager.addToWaitlist(passengerName, trainId, numSeats);
        }
    }

    private static void cancelTicket(Scanner scanner) {
        System.out.print("Enter ticket ID to cancel: ");
        String ticketId = scanner.nextLine();

        Ticket ticket = DatabaseManager.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Invalid ticket ID.");
            return;
        }

        if (DatabaseManager.cancelTicket(ticketId)) {
            System.out.println("Ticket canceled successfully.");
          
            int availableSeats = ticket.getTrain().getAvailableSeats() + ticket.getNumSeats(); // Seats are now free
            String nextPassenger = DatabaseManager.getNextFromWaitlist(ticket.getTrain().getTrainId(), availableSeats);
            if (nextPassenger != null) {
                System.out.println("A spot has opened up! Booking confirmed for " + nextPassenger + " from the waitlist.");
               
            }
        } else {
            System.out.println("Cancellation failed.");
        }
    }

    private static void viewTicket(Scanner scanner) {
        System.out.print("Enter ticket ID: ");
        String ticketId = scanner.nextLine();
        Ticket ticket = DatabaseManager.getTicketById(ticketId);
        if (ticket != null) {
            System.out.println("\n--- Ticket Details ---");
            System.out.println(ticket);
        } else {
            System.out.println("Ticket not found.");
        }
    }
}