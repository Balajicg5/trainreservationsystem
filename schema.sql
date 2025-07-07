
CREATE DATABASE IF NOT EXISTS train_reservation_db;
USE train_reservation_db;

-- Drop tables if they exist to start fresh
DROP TABLE IF EXISTS waitlist;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS stations;

-- Stations Table
CREATE TABLE stations (
    station_id VARCHAR(10) PRIMARY KEY,
    station_name VARCHAR(255) NOT NULL UNIQUE
);

-- Trains Table
CREATE TABLE trains (
    train_id VARCHAR(10) PRIMARY KEY,
    train_name VARCHAR(255) NOT NULL,
    total_seats INT NOT NULL
);

-- Routes Table (Junction Table for Train and Station)
-- Defines the sequence of stations for each train
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    train_id VARCHAR(10),
    station_id VARCHAR(10),
    sequence_number INT NOT NULL, -- e.g., 1 for the first stop, 2 for the second
    FOREIGN KEY (train_id) REFERENCES trains(train_id),
    FOREIGN KEY (station_id) REFERENCES stations(station_id)
);

-- Tickets Table
CREATE TABLE tickets (
    ticket_id VARCHAR(20) PRIMARY KEY,
    passenger_name VARCHAR(255) NOT NULL,
    train_id VARCHAR(10),
    source_station_id VARCHAR(10),
    destination_station_id VARCHAR(10),
    num_seats INT NOT NULL,
    FOREIGN KEY (train_id) REFERENCES trains(train_id),
    FOREIGN KEY (source_station_id) REFERENCES stations(station_id),
    FOREIGN KEY (destination_station_id) REFERENCES stations(station_id)
);

-- Waitlist Table
CREATE TABLE waitlist (
    waitlist_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(255) NOT NULL,
    train_id VARCHAR(10),
    num_seats INT NOT NULL,
    FOREIGN KEY (train_id) REFERENCES trains(train_id)
);

-- Insert Mock Data
INSERT INTO stations (station_id, station_name) VALUES
('STA', 'Station A'),
('STB', 'Station B'),
('STC', 'Station C'),
('STD', 'Station D');

INSERT INTO trains (train_id, train_name, total_seats) VALUES
('T001', 'Express 1', 100),
('T002', 'Express 2', 80);

INSERT INTO routes (train_id, station_id, sequence_number) VALUES
('T001', 'STA', 1),
('T001', 'STB', 2),
('T001', 'STC', 3),
('T002', 'STA', 1),
('T002', 'STD', 2),
('T002', 'STC', 3);

