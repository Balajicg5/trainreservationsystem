
package com.trainreservation;

import java.util.*;

public class Graph {
    private Map<Station, List<Station>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addStation(Station station) {
        adjacencyList.putIfAbsent(station, new ArrayList<>());
    }

    public void addEdge(Station source, Station destination) {
        adjacencyList.get(source).add(destination);
        // For undirected graph, add the reverse edge as well
        // adjacencyList.get(destination).add(source);
    }

    public List<Station> getNeighbors(Station station) {
        return adjacencyList.get(station);
    }

    public List<List<Station>> findRoutes(Station source, Station destination) {
        List<List<Station>> routes = new ArrayList<>();
        Queue<List<Station>> queue = new LinkedList<>();
        List<Station> initialPath = new ArrayList<>();
        initialPath.add(source);
        queue.add(initialPath);

        while (!queue.isEmpty()) {
            List<Station> currentPath = queue.poll();
            Station lastStation = currentPath.get(currentPath.size() - 1);

            if (lastStation.equals(destination)) {
                routes.add(new ArrayList<>(currentPath));
            }

            List<Station> neighbors = getNeighbors(lastStation);
            if (neighbors != null) {
                for (Station neighbor : neighbors) {
                    if (!currentPath.contains(neighbor)) {
                        List<Station> newPath = new ArrayList<>(currentPath);
                        newPath.add(neighbor);
                        queue.add(newPath);
                    }
                }
            }
        }
        return routes;
    }
}
