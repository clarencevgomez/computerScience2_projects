
/*
 * Clarence Gomez
 * Dr. Steinberg
 * COP3503 Fall 2023
 * Programming Assignment 5
 * 
 * import java.io.BufferedReader;
 * import java.util.Arrays;
 * import java.util.HashMap;
 * import java.io.IOException;
 * import java.io.FileReader;
 * import java.util.*;
 * 
 * public class Railroad {
 * private int numTracks; // total # of lines in each respective text file
 * private String tFile; // t contains the name of a text file w all possible
 * tracks the company has
 * // provided
 * HashMap<String, Integer> trackMap = new HashMap<String, Integer>();
 * HashMap<String, Integer> solutionMap = new HashMap<String, Integer>();
 * 
 * // overloaded constructor
 * public Railroad(int numTracks, String filename) {
 * this.numTracks = numTracks;
 * this.tFile = filename;
 * readFile(tFile);
 * }
 * 
 * // method to read files
 * public void readFile(String tFile) {
 * try {
 * BufferedReader reader = new BufferedReader(new FileReader(tFile));
 * HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
 * for (int i = 0; i < numTracks; ++i) {
 * String track = reader.readLine();
 * String[] trackList = track.split(" ");
 * String vertexes = String.format("%s---%s", trackList[0], trackList[1]);
 * tempMap.put(vertexes, Integer.parseInt(trackList[2])); // adds that track to
 * the hashmap
 * }
 * reader.close();
 * trackMap = sortByValue(tempMap);
 * } catch (IOException e) {
 * throw null;
 * }
 * }
 * 
 * // function to sort cities
 * public static HashMap<String, Integer> sortByValue(HashMap<String, Integer>
 * hm) {
 * // Create a list from elements of HashMap
 * List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String,
 * Integer>>(hm.entrySet());
 * 
 * // Sort the list
 * Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
 * public int compare(Map.Entry<String, Integer> o1,
 * Map.Entry<String, Integer> o2) {
 * return (o1.getValue()).compareTo(o2.getValue());
 * }
 * });
 * 
 * // put data from sorted list to hashmap
 * HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
 * for (Map.Entry<String, Integer> aa : list) {
 * temp.put(aa.getKey(), aa.getValue());
 * }
 * return temp;
 * }
 * 
 * // change this into a sort map
 * public void printMap() {
 * System.out.println(Arrays.asList(trackMap));
 * }
 * 
 * }
 */

import java.util.*;

class KruskalAlgorithm {
    static class Edge implements Comparable<Edge> {
        String source, destination;
        int cost;

        public Edge(String source, String destination, int cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    static class DisjointSet {
        Map<String, String> parent;

        public DisjointSet() {
            parent = new HashMap<>();
        }

        public void makeSet(String vertex) {
            parent.put(vertex, vertex);
        }

        public String find(String vertex) {
            if (!parent.get(vertex).equals(vertex)) {
                parent.put(vertex, find(parent.get(vertex)));
            }
            return parent.get(vertex);
        }

        public void union(String vertex1, String vertex2) {
            String root1 = find(vertex1);
            String root2 = find(vertex2);
            parent.put(root1, root2);
        }
    }

    static List<Edge> kruskal(List<Edge> edges, Set<String> vertices) {
        List<Edge> result = new ArrayList<>();
        DisjointSet disjointSet = new DisjointSet();

        for (String vertex : vertices) {
            disjointSet.makeSet(vertex);
        }

        Collections.sort(edges);

        for (Edge edge : edges) {
            String sourceRoot = disjointSet.find(edge.source);
            String destinationRoot = disjointSet.find(edge.destination);

            if (!sourceRoot.equals(destinationRoot)) {
                result.add(edge);
                disjointSet.union(edge.source, edge.destination);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        Set<String> vertices = new HashSet<>();

        // Add your edges and vertices here
        edges.add(new Edge("D", "B", 5));
        edges.add(new Edge("B", "C", 6));
        edges.add(new Edge("A", "C", 8));
        edges.add(new Edge("A", "B", 10));

        for (Edge edge : edges) {
            vertices.add(edge.source);
            vertices.add(edge.destination);
        }

        List<Edge> result = kruskal(edges, vertices);

        // Output the result
        for (Edge edge : result) {
            System.out.println(edge.source + " " + edge.destination + " " + edge.cost);
        }
    }
}