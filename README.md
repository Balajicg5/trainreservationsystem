# Train Reservation System

This is a command-line based train reservation system written in Java. It allows users to find trains, book tickets, cancel tickets, and view ticket details. The system uses a MySQL database to store information about stations, trains, routes, and bookings.

## Features

*   **Find Trains:** Users can search for available trains between two stations.
*   **Book Tickets:** Users can book tickets for a specific train. If seats are not available, they are added to a waitlist.
*   **Cancel Tickets:** Users can cancel their booked tickets.
*   **View Ticket Details:** Users can view the details of their booking using their ticket ID.
*   **Waitlist Management:** Automatically allocates tickets to waitlisted passengers when a booking is canceled.
*   **Station Autocomplete (Conceptual):** A `Trie` data structure is implemented, which can be used for suggesting station names, although it is not fully integrated into the command-line interface in this version.

## Tech Stack and Design Choices

### Core Language: Java
*   **Reasoning:** Java is a robust, object-oriented language with strong memory management and a vast ecosystem of libraries. Its platform independence ("write once, run anywhere") makes it suitable for a wide range of applications. The JDBC (Java Database Connectivity) API provides a standard way to interact with databases.

### Database: MySQL
*   **Reasoning:** MySQL is a popular, open-source relational database management system (RDBMS). It's known for its reliability, performance, and ease of use. A relational database is a good choice for this project because the data (stations, trains, tickets) is well-structured and has clear relationships. For example, a ticket is related to a specific passenger, train, source station, and destination station.

### Data Structures and Algorithms

#### 1. Graph (`Graph.java`)
*   **Purpose:** To model the train network. Each station is a *vertex* (or node) in the graph, and a direct train route between two stations is an *edge*.
*   **Algorithm:** The `findRoutes` method uses a **Breadth-First Search (BFS)** algorithm.
*   **Reasoning:**
    *   **Why a graph?** A graph is the most natural way to represent a network of interconnected points, like train stations. It allows us to easily model routes and connections.
    *   **Why BFS?** BFS is a good choice for finding paths in an unweighted graph. It explores the graph layer by layer, which means it can find a path (though not necessarily the shortest in terms of distance or time, as the graph is unweighted) between two stations. In this implementation, it finds all possible paths.

#### 2. Trie (`Trie.java`)
*   **Purpose:** To provide efficient searching and prefix-based lookups for station names.
*   **Algorithm:** The `Trie` data structure is a tree-like structure where each node represents a character in a string.
*   **Reasoning:**
    *   **Why a Trie?** A Trie (also known as a prefix tree) is highly efficient for searching for strings or for features like autocomplete. If a user starts typing a station name, a Trie can provide suggestions almost instantly. It's much faster for prefix searches than iterating through a list of all station names.

## How to Compile and Run

### Prerequisites
*   Java Development Kit (JDK) 8 or higher
*   MySQL Server
*   MySQL Connector/J library

### 1. Database Setup
1.  Make sure your MySQL server is running.
2.  Create a database named `train_reservation_db`.
    ```sql
    CREATE DATABASE train_reservation_db;
    ```
3.  Use the created database.
    ```sql
    USE train_reservation_db;
    ```
4.  Run the `schema.sql` script to create the necessary tables.
    ```bash
    mysql -u your_username -p train_reservation_db < schema.sql
    ```
5.  **Important:** Open `src/com/trainreservation/DatabaseManager.java` and update the database credentials (`DB_USER` and `DB_PASSWORD`) to match your MySQL setup.

### 2. Compilation

#### Using Command Line
1.  Open a terminal or command prompt in the root directory of the project.
2.  Compile the Java files using the following command:

    ```bash
    javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" src/com/trainreservation/*.java
    ```
    *   If you are using a shell that does not expand the `*` wildcard (like some versions of Git Bash on Windows), you may need to list the files explicitly:
        ```bash
        javac -d bin -cp "lib/mysql-connector-j-9.3.0.jar" src/DatabaseManager.java src/Graph.java src/Station.java src/Ticket.java src/Train.java src/TrainReservationSystem.java src/Trie.java
        ```

    This command compiles all `.java` files and places the compiled `.class` files in the `bin` directory.

### 3. Running the Application

1.  From the project's root directory, run the main class, making sure to include the MySQL connector and the compiled classes in the classpath.

    **For Windows:**
    ```bash
    java -cp "bin;lib/mysql-connector-j-9.3.0.jar" com.trainreservation.TrainReservationSystem
    ```

    **For macOS/Linux:**
    ```bash
    java -cp "bin:lib/mysql-connector-j-9.3.0.jar" com.trainreservation.TrainReservationSystem
    ```

2.  You should now see the main menu of the Train Reservation System in your terminal.

## Future Improvements
*   **Integrate the Trie:** Fully integrate the Trie for station name autocomplete in the user interface.
*   **GUI:** Develop a graphical user interface (GUI) using a framework like Swing or JavaFX for a more user-friendly experience.
*   **Web Application:** Convert this into a web application using technologies like Servlets, JSP, or a modern framework like Spring Boot.
*   **Shortest Path:** Enhance the graph algorithm to find the shortest path between stations (e.g., using Dijkstra's algorithm) by adding weights to the edges (representing distance or travel time).
*   **User Authentication:** Add a user authentication system to manage user accounts and booking history.
