package it.diegorigo.graph;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphUtils {

    public static Map<String, Double> getDistances(Graph<String, DefaultEdge> graph,
                                                   List<String> nodes) {
        Map<String, Double> distances = new HashMap<>();
        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        for (String node1 : nodes) {
            for (String node2 : nodes) {
                String code = getCode(node1, node2);
                if (!node1.equalsIgnoreCase(node2)) {
                    double dist = dijkstraAlg.getPathWeight(node1, node2);
                    distances.put(code, dist);
                }
            }
        }
        return distances;
    }

    public static String getCode(String node1,
                                 String node2) {
        return Stream.of(node1, node2).sorted().collect(Collectors.joining(" TO "));
    }

    public static Graph<String, DefaultEdge> createStringGraph(List<String> nodes,
                                                               List<Edge> edges) {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        nodes.forEach(g::addVertex);
        edges.forEach(item -> g.addEdge(item.getFirstNode(), item.getSecondNode()));
        return g;
    }
}
