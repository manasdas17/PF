package pf.gui;

import pf.graph.Vertex;

public interface SnapPolicy {
	Vertex request(GameBoard board, float x, float y);
}
