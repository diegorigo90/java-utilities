package it.diegorigo.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Edge {
    private String firstNode;
    private String secondNode;
}
