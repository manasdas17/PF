package pf.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import pf.analytics.PointImpl;

public class SchemaBoardPattern {

	public static class SquareSchemaBoardPattern extends ComplexBoardPattern {

		public SquareSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (l % 2 == 0) {
					if (!line.matches("([. ][- ])*[. ]?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("-", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 1,
								l / 2));
					}
				} else {
					if (!line.matches("([| ] )*[| ]?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("|", e + 1)) >= 0) {
						ee = e / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee, (l + 1) / 2));
					}
				}

				l++;
			}
		}
	}

	public static class DiagonalSchemaBoardPattern extends ComplexBoardPattern {

		public DiagonalSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (l % 2 == 0) {
					if (!line.matches("([. ][- ])*[. ]?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("-", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, l / 2), new PointImpl(ee + 1,
								l / 2));
					}
				} else {
					if (!line.matches("([| ][\\\\/X ])*[| ]?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("|", e + 1)) >= 0) {
						ee = e / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee - 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
					}
					e = -1;
					while ((e = line.indexOf("X", e + 1)) >= 0) {
						ee = (e - 1) / 2;
						addEdge(new PointImpl(ee, (l - 1) / 2), new PointImpl(
								ee + 1, (l + 1) / 2));
						addEdge(new PointImpl(ee + 1, (l - 1) / 2),
								new PointImpl(ee, (l + 1) / 2));
					}
				}

				l++;
			}
		}
	}

	public static class TriangleSchemaBoardPattern extends ComplexBoardPattern {

		protected TriangleSchemaBoardPattern(Board board, File f)
				throws FileNotFoundException {
			super(board, f);
		}

		@Override
		protected void readFromFile(File f) throws FileNotFoundException {
			Scanner s = new Scanner(f);
			s.nextLine();
			s.nextLine();
			String line;
			int l = 0;
			int e, ee;
			while (s.hasNextLine()) {
				line = s.nextLine();
				if (l % 2 == 0) {
					if (!line.matches("(  )?([. ]((   )|(---)))*[. ]"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("---", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 8, l / 2 * 7));
					}
				} else if (l % 4 == 1) {
					if (!line.matches("( [\\\\ ] [/ ])* ?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 4, (l + 1) / 2 * 7));
					}
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee - 4, (l + 1) / 2 * 7));
					}
				} else {
					if (!line.matches("( [/ ] [\\\\ ])* ?"))
						throw new InputMismatchException();
					e = -1;
					while ((e = line.indexOf("/", e + 1)) >= 0) {
						ee = (e + 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee - 4, (l + 1) / 2 * 7));
					}
					e = -1;
					while ((e = line.indexOf("\\", e + 1)) >= 0) {
						ee = (e - 1) * 2;
						addEdge(new PointImpl(ee, l / 2 * 7), new PointImpl(
								ee + 4, (l + 1) / 2 * 7));
					}
				}

				l++;
			}
		}
	}

	public static BoardPattern createSchemaBoardPattern(Board board, File f) {
		try {
			switch (board.getGrid().getGridType()) {
			case TRIANGLE:
				return new TriangleSchemaBoardPattern(board, f);
			case SQUARE:
				return new SquareSchemaBoardPattern(board, f);
			case DIAGONAL:
				return new DiagonalSchemaBoardPattern(board, f);
			case DIAGONALX:
				// TODO diagonalx schema
			}
		} catch (FileNotFoundException ex) {
			// FIXME return empty on file not found ex
			return null;
		}
		return null;
	}

	private SchemaBoardPattern() {
	}

}
