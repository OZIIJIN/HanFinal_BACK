package org.onesentence.onesentence.domain.dijkstra.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Graph {

	private final Map<Long, List<Edge>> edges = new HashMap<>(); // Long 인 todoId 와 관련되었기에 Map 선택
	private final List<Node> nodes;

	public void addEdge(Long from, Long to, Double weight) {
		edges.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, weight));
		edges.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, weight)); // 무방향 그래프: 양방향 에지 추가
	}

	public List<Edge> getEdges(Long nodeId) {
		return edges.getOrDefault(nodeId, new ArrayList<>());
	}

}
