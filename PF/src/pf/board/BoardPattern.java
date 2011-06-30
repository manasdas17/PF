package pf.board;

import java.io.Writer;

import pf.analytics.Point;
import pf.board.BoardPattern.PointsEdge;

public interface BoardPattern extends Iterable<PointsEdge> {
	static class PointsEdge {
		final Point p1, p2;
		boolean used;

		public PointsEdge(Point p1, Point p2) {
			this(p1, p2, false);
		}

		public PointsEdge(Point p1, Point p2, boolean used) {
			if (p1 == null || p2 == null) {
				throw new IllegalArgumentException();
			}
			this.p1 = p1;
			this.p2 = p2;
			this.used = used;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			PointsEdge other = (PointsEdge) obj;
			return p1.equals(other.p1) && p2.equals(other.p2)
					|| p1.equals(other.p2) && p2.equals(other.p1);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (p1.hashCode() + p2.hashCode());
			return result;
		}

		@Override
		public String toString() {
			return "PointsEdge [p1=" + p1 + ", p2=" + p2 + ", used=" + used
					+ "]";
		}
	}

	void save(Writer w);
}
