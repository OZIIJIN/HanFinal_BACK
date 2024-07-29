package org.onesentence.onesentence.domain.dijkstra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Edge {

	public Long to;
	public Double weight;

}
