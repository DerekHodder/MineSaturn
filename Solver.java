import java.util.Set;
import java.util.HashSet;

public class Solver {
	Set<Pattern> patterns = constructPatterns();

	public Solver() {
		
	}

	public Board iterate(Board gameBoard) {
		for (int row = 0; row < gameBoard.getHeight(); row++) {
			for (int col = 0; col < gameBoard.getWidth(); col++) {
				Cell cell = gameBoard.array[row][col];
				if (!cell.isOpened() && !cell.hasFlag() && cell.isVisible()) {
					for (Pattern basePattern : patterns) {
						for (Pattern individualPattern : basePattern.get8Transformations()) {
							if (overlay(gameBoard, individualPattern, row, col)) {
								Set<Cell> neighbors = gameBoard.getNeighbors(row, col);
								if (individualPattern.openTarget()) {
									cell.open();
									gameBoard.setNumOfOpened(gameBoard.getNumOfOpened() + 1);
									if (cell.hasMine()) {
										gameBoard.explode();
									}
									for (Cell neighbor : neighbors) {
										neighbor.setVisible();
									}
								} else {
									cell.flag();
									for (Cell neighbor : neighbors) {
										neighbor.setInternalNumber(neighbor.getInternalNumber() - 1);
									}
								}
								return gameBoard;
							}
						}
					}
				}
			}
		}
		int randomRow = (int) (Math.random() * gameBoard.getHeight());
		int randomCol = (int) (Math.random() * gameBoard.getWidth());
		Cell randomCell = gameBoard.array[randomRow][randomCol];
		if (!randomCell.hasFlag()) {
			randomCell.open();
			gameBoard.setNumOfOpened(gameBoard.getNumOfOpened() + 1);
			if (randomCell.hasMine()) {
				gameBoard.explode();
			}
			Set<Cell> neighbors = gameBoard.getNeighbors(randomRow, randomCol);
			for (Cell neighbor : neighbors) {
				neighbor.setVisible();
			}
		}
		return gameBoard;
	}

	private boolean overlay(Board board, Pattern pattern, int row, int col) {
		for (int patternRow = 0; patternRow < pattern.getHeight(); patternRow++) {
			for (int patternCol = 0; patternCol < pattern.getWidth(); patternCol++) {
				int boardRow = row - pattern.getTargetRow() + patternRow;
				int boardCol = col - pattern.getTargetCol() + patternCol;
				boolean outOfBounds = (boardRow < 0) || (boardRow >= board.getHeight())
				|| (boardCol < 0) || (boardCol >= board.getWidth());

				if (outOfBounds) {
					if (pattern.array[patternRow][patternCol].isOpened()) {
						return false;
					} else {
						continue;
					}
				}

				if (pattern.array[patternRow][patternCol].isOpened()) {
					if (!board.array[boardRow][boardCol].isOpened()
					|| pattern.array[patternRow][patternCol].getInternalNumber()
					!= board.array[boardRow][boardCol].getInternalNumber()) {
						return false;
					}
				}

				if (pattern.array[patternRow][patternCol].hasFlag()
				&& !board.array[boardRow][boardCol].isOpened()
				&& !board.array[boardRow][boardCol].hasFlag()) {
					return false;
				}
			}
		}
		return true;
	}

	private Set<Pattern> constructPatterns() {
		Set<Pattern> patterns = new HashSet<Pattern>();

		Pattern o0Side = new Pattern(2, 1, 0, 0, true);
		o0Side.array[1][0].setInternalNumber(0);
		o0Side.array[1][0].open();
		patterns.add(o0Side);

		Pattern o0Corner = new Pattern(2, 2, 0, 0, true);
		o0Corner.array[1][1].setInternalNumber(0);
		o0Corner.array[1][1].open();
		patterns.add(o0Corner);

		Pattern x1Side = new Pattern(3, 3, 0, 1, false);
		x1Side.array[0][0].flag();
		x1Side.array[0][2].flag();
		x1Side.array[1][0].flag();
		x1Side.array[1][1].setInternalNumber(1);
		x1Side.array[1][1].open();
		x1Side.array[1][2].flag();
		x1Side.array[2][0].flag();
		x1Side.array[2][1].flag();
		x1Side.array[2][2].flag();
		patterns.add(x1Side);

		Pattern x1Corner = new Pattern(3, 3, 0, 0, false);
		x1Corner.array[0][1].flag();
		x1Corner.array[0][2].flag();
		x1Corner.array[1][0].flag();
		x1Corner.array[1][1].setInternalNumber(1);
		x1Corner.array[1][1].open();
		x1Corner.array[1][2].flag();
		x1Corner.array[2][0].flag();
		x1Corner.array[2][1].flag();
		x1Corner.array[2][2].flag();
		patterns.add(x1Corner);

		Pattern o11Side = new Pattern(4, 3, 0, 1, true);
		o11Side.array[1][1].setInternalNumber(1);
		o11Side.array[1][1].open();
		o11Side.array[2][1].setInternalNumber(1);
		o11Side.array[2][1].open();
		o11Side.array[3][0].flag();
		o11Side.array[3][1].flag();
		o11Side.array[3][2].flag();
		patterns.add(o11Side);

		Pattern o11Corner = new Pattern(4, 3, 0, 0, true);
		o11Corner.array[1][1].setInternalNumber(1);
		o11Corner.array[1][1].open();
		o11Corner.array[2][1].setInternalNumber(1);
		o11Corner.array[2][1].open();
		o11Corner.array[3][0].flag();
		o11Corner.array[3][1].flag();
		o11Corner.array[3][2].flag();
		patterns.add(o11Corner);

		Pattern x21Side = new Pattern(3, 3, 0, 1, false);
		x21Side.array[0][0].flag();
		x21Side.array[0][2].flag();
		x21Side.array[1][1].setInternalNumber(2);
		x21Side.array[1][1].open();
		x21Side.array[2][1].setInternalNumber(1);
		x21Side.array[2][1].open();
		patterns.add(x21Side);

		Pattern x21Corner = new Pattern(3, 3, 0, 0, false);
		x21Corner.array[0][1].flag();
		x21Corner.array[0][2].flag();
		x21Corner.array[1][1].setInternalNumber(2);
		x21Corner.array[1][1].open();
		x21Corner.array[2][1].setInternalNumber(1);
		x21Corner.array[2][1].open();
		patterns.add(x21Corner);

		Pattern x2Adjacent = new Pattern(3, 3, 0, 0, false);
		x2Adjacent.array[0][2].flag();
		x2Adjacent.array[1][0].flag();
		x2Adjacent.array[1][1].setInternalNumber(2);
		x2Adjacent.array[1][1].open();
		x2Adjacent.array[1][2].flag();
		x2Adjacent.array[2][0].flag();
		x2Adjacent.array[2][1].flag();
		x2Adjacent.array[2][2].flag();
		patterns.add(x2Adjacent);

		Pattern x2Opposite = new Pattern(3, 3, 0, 0, false);
		x2Opposite.array[0][1].flag();
		x2Opposite.array[1][0].flag();
		x2Opposite.array[1][1].setInternalNumber(2);
		x2Opposite.array[1][1].open();
		x2Opposite.array[1][2].flag();
		x2Opposite.array[2][0].flag();
		x2Opposite.array[2][1].flag();
		x2Opposite.array[2][2].flag();
		patterns.add(x2Opposite);

		Pattern x2L = new Pattern(3, 3, 0, 0, false);
		x2L.array[0][1].flag();
		x2L.array[0][2].flag();
		x2L.array[1][0].flag();
		x2L.array[1][1].setInternalNumber(2);
		x2L.array[1][1].open();
		x2L.array[2][0].flag();
		x2L.array[2][1].flag();
		x2L.array[2][2].flag();
		patterns.add(x2L);

		Pattern x2Corners = new Pattern(3, 3, 0, 0, false);
		x2Corners.array[0][1].flag();
		x2Corners.array[0][2].flag();
		x2Corners.array[1][0].flag();
		x2Corners.array[1][1].setInternalNumber(2);
		x2Corners.array[1][1].open();
		x2Corners.array[1][2].flag();
		x2Corners.array[2][0].flag();
		x2Corners.array[2][1].flag();
		patterns.add(x2Corners);

		return patterns;
	}
}
