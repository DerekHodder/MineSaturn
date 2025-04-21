import java.util.Collections;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class Board extends Grid {
	private int numOfMines;
	private int numOfOpened = 0;
	private boolean exploded = false;

	public Board(int h, int w, int mines) {
		height = h;
		width = w;
		numOfMines = mines;
		array = new Cell[height][width];
		ArrayList<Boolean> minesList = new ArrayList<Boolean>();
		for (int i = 0; i < numOfMines; i++) {
			minesList.add(true);
		}
		for (int i = 0; i < height * width - numOfMines; i++) {
			minesList.add(false);
		}
		Collections.shuffle(minesList);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				array[row][col] = new Cell(row, col, minesList.get(row * width + col));
			}
		}
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Cell cell = array[row][col];
				Set<Cell> neighbors = getNeighbors(cell);
				int surrounding = 0;
				for (Cell neighbor : neighbors) {
					if (neighbor.hasMine()) {
						surrounding++;
					}
				}
				cell.setInternalNumber(surrounding);
				cell.setExternalNumber(surrounding);
			}
		}
		Set<Cell> safeCells = new HashSet<Cell>();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Cell cell = array[row][col];
				if (!cell.hasMine()) {
					safeCells.add(cell);
				}
			}
		}
		Cell firstCell = getRandomCell(safeCells);
		firstCell.open();
		Set<Cell> neighbors = getNeighbors(firstCell);
		for (Cell neighbor : neighbors) {
			neighbor.setVisible();
		}
		numOfOpened = 1;
	}

	private Cell getRandomCell(Set<Cell> cellSet) {
		int cellIndex = new Random().nextInt(cellSet.size());
		int currentIndex = 0;
		for (Cell cell : cellSet) {
			if (currentIndex == cellIndex) {
				return cell;
			}
			currentIndex++;
		}
		return null;
	}

	public Set<Cell> getNeighbors(Cell cell) {
		Set<Cell> neighbors = new HashSet<Cell>();
			for (int updown = -1; updown <= 1; updown++) {
				int actualRow = cell.getRow() + updown;
				for (int leftright = -1; leftright <= 1; leftright++) {
					int actualCol = cell.getCol() + leftright;
					if (!(updown == 0 && leftright == 0) && actualRow >= 0 && actualRow < height
					&& actualCol >= 0 && actualCol < width) {
						neighbors.add(array[actualRow][actualCol]);
					}
				}
			}
		return neighbors;
	}

	public void setNumOfOpened(int newNum) {
		numOfOpened = newNum;
	}

	public int getNumOfOpened() {
		return numOfOpened;
	}

	public void explode() {
		exploded = true;
	}

	public boolean isExploded() {
		return exploded;
	}

	public boolean hasWon() {
		return numOfOpened + numOfMines == height * width;
	}
}