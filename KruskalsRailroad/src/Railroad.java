/* Clarence Gomez
   Dr. Steinberg
   COP3503 Fall 2023
   Programming Assignment 5
*/

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileReader;
import java.util.*;

public class Railroad {
    private int numTracks; // total # of lines in each respective text file
    private String tFile; // t contains the name of a text file w all possible tracks the company has

    ArrayList<Edge> trackMap = new ArrayList<Edge>(); // contains the whole map
    Set<String> vertices = new HashSet<>(); // contains all the city names

    // overloaded constructor
    public Railroad(int numTracks, String filename) {
        this.numTracks = numTracks;
        this.tFile = filename;
        readFile(tFile);
    }

    // method to read files & sort them
    public void readFile(String tFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tFile));
            for (int i = 0; i < numTracks; ++i) {
                String track = reader.readLine();
                String[] trackList = track.split(" ");
                trackMap.add(new Edge(trackList[0], trackList[1], Integer.parseInt(trackList[2])));
            }
            reader.close();

            // sorts the list by cost
            trackMap.sort(new Comparator<Edge>() {
                @Override
                public int compare(Edge c1, Edge c2) {
                    return c1.cost - c2.cost;
                }
            });
        } catch (IOException e) {
            throw null;
        }
    }

    // method for designing cost-efficient railroad system
    public String buildRailroad() {
        List<Edge> solutionMap = new ArrayList<Edge>(); // contains the solution road
        DisjointSet cities = new DisjointSet(numTracks);
        int totalCost = 0;

        for (Edge cityName : trackMap) { // making disjoint sets for the cities
            vertices.add(cityName.vA);
            vertices.add(cityName.vB);
        }

        for (String city : vertices) { // initlizing set with a single city
            cities.makeSet(city);
        }

        // Kruskal's algorithm
        for (Edge city : trackMap) {
            String cityA = cities.find(city.vA);
            String cityB = cities.find(city.vB);

            if (!cityA.equals(cityB)) { // adds the city to the solution if it doesnt cause a cycle
                solutionMap.add(city);
                cities.union(cityA, cityB);
                totalCost += city.cost; // updating total cost
            }
        }

        // print results
        for (Edge result : solutionMap) {
            if (result.vA.compareTo(result.vB) > 0) { // prints in alphabetical order
                System.out.println(String.format("%s---%s\t$%d", result.vB, result.vA, result.cost));
            } else {
                System.out.println(String.format("%s---%s\t$%d", result.vA, result.vB, result.cost));
            }
        }
        return "The cost of the railroad is $" + totalCost + ".";
    }

    // edge class for city edge tracking
    static class Edge {
        String vA, vB;
        int cost;

        public Edge(String src, String dest, int cost) {
            this.vA = src;
            this.vB = dest;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return vA + "---" + vB + "\t$" + cost;
        }
    }

    // modified disjoint set class
    static class DisjointSet {
        Map<String, String> parent;

        public DisjointSet(int n) {
            parent = new HashMap<>();
        }

        // makeSet method
        public void makeSet(String city) {
            parent.put(city, city);
        }

        // find method
        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // path compression
            }
            return parent.get(x);
        }

        // union method
        public void union(String x, String y) {
            String cityA = find(x), cityB = find(y);
            if (cityA == cityB)
                return;
            parent.put(cityA, cityB); // making a subset
        }
    }
}