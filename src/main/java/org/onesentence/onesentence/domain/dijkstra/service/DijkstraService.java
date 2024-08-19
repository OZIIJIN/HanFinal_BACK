package org.onesentence.onesentence.domain.dijkstra.service;

import java.util.*;
import org.onesentence.onesentence.domain.dijkstra.model.Edge;
import org.onesentence.onesentence.domain.dijkstra.model.Graph;
import org.onesentence.onesentence.domain.dijkstra.model.Node;
import org.springframework.stereotype.Service;

@Service
public class DijkstraService {

	public List<Long> getOptimalOrder(Graph graph) {
		Map<Long, Double> totalDistances = new HashMap<>();
		for (Node node : graph.getNodes()) {
			totalDistances.put(node.getTodoId(), 0.0);
		}

		for (Node startNode : graph.getNodes()) {
			Map<Long, Double> distances = dijkstra(graph, startNode.getTodoId());
			for (Map.Entry<Long, Double> entry : distances.entrySet()) {
				Long todoId = entry.getKey();
				totalDistances.put(todoId, totalDistances.get(todoId) + entry.getValue());
			}
		}

		List<Map.Entry<Long, Double>> sortedEntries = new ArrayList<>(totalDistances.entrySet());
		sortedEntries.sort(Map.Entry.comparingByValue());

		List<Long> optimalOrder = new ArrayList<>();
		for (Map.Entry<Long, Double> entry : sortedEntries) {
			optimalOrder.add(entry.getKey());
		}

		return optimalOrder;
	}

	public Map<Long, Double> dijkstra(Graph graph, Long start) {
		Map<Long, Double> distances = new HashMap<>();
		PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(e -> e.getWeight()));
		Set<Long> visited = new HashSet<>();

		for (Node node : graph.getNodes()) {
			distances.put(node.getTodoId(), Double.MAX_VALUE);
		}
		distances.put(start, 0.0);
		queue.add(new Edge(start, 0.0));

		while (!queue.isEmpty()) {
			Edge currentEdge = queue.poll();
			Long current = currentEdge.getTo();

			if (!visited.contains(current)) {
				visited.add(current);

				for (Edge edge : graph.getEdges(current)) {
					Long nextNode = edge.getTo();
					if (!visited.contains(nextNode)) {
						double newDist = distances.get(current) + edge.getWeight();
						if (newDist < distances.get(nextNode)) {
							distances.put(nextNode, newDist);
							queue.add(new Edge(nextNode, newDist));
						}
					}
				}
			}
		}

		return distances;
	}

}
