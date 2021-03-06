package pf.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Default implementation of {@link Direction}
 * 
 * @author Adam Juraszek
 * 
 */
public class DirectionsImpl implements Directions {

	private final Set<Direction> ds;

	public DirectionsImpl() {
		ds = new HashSet<Direction>();
	}

	public boolean addDirection(Direction d) {
		return ds.add(d);
	}

	/**
	 * returnes unmodifiable set
	 */
	@Override
	public Set<Direction> getDirections() {
		return Collections.unmodifiableSet(ds);
	}

	/**
	 * finds the nearest point to {@code [dx, dy]} of points [
	 * {@link Direction#getDx()}, {@link Direction#getDy()} ]
	 */
	@Override
	public Direction getNearestDirection(int dx, int dy) {
		if (ds.isEmpty()) {
			return null;
		}
		Direction best = null;
		int bestLen = Integer.MAX_VALUE;
		for (Direction d : ds) {
			int len = (dx - d.getDx()) * (dx - d.getDx()) + (dy - d.getDy())
					* (dy - d.getDy());
			if (len < bestLen) {
				best = d;
				bestLen = len;
			}
		}
		return best;
	}

	@Override
	public Direction getNearestDirection(Vertex v1, Vertex v2) {
		return getNearestDirection(v2.getX() - v1.getX(), v2.getY() - v1.getY());
	}

	@Override
	public Iterator<Direction> iterator() {
		return ds.iterator();
	}

}
