import java.util.Set;
import java.util.HashSet;

public class Pattern extends Grid {
	private boolean openTarget;
	private int targetRow;
	private int targetCol;

	public Pattern(int h, int w, int r, int c, boolean x) {
		openTarget = x;
		height = h;
		width = w;
		array = new Cell[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				array[row][col] = new Cell(row, col, false);
			}
		}
		targetRow = r;
		targetCol = c;
	}

	public Set<Pattern> get8Transformations() {
		Set<Pattern> transformations = new HashSet<Pattern>();
		Pattern pattern1 = new Pattern(height, width, targetRow, targetCol, openTarget);
		Pattern pattern2 = new Pattern(height, width, targetRow, width - targetCol - 1, openTarget);
		Pattern pattern3 = new Pattern(height, width, height - targetRow - 1, targetCol, openTarget);
		Pattern pattern4 = new Pattern(height, width,
		height - targetRow - 1, width - targetCol - 1, openTarget);
		Pattern pattern5 = new Pattern(width, height, targetCol, targetRow, openTarget);
		Pattern pattern6 = new Pattern(width, height, width - targetCol - 1, targetRow, openTarget);
		Pattern pattern7 = new Pattern(width, height, targetCol, height - targetRow - 1, openTarget);
		Pattern pattern8 = new Pattern(width, height,
		width - targetCol - 1, height - targetRow - 1, openTarget);
		for (int originalRow = 0; originalRow < height; originalRow++) {
			for (int originalCol = 0; originalCol < width; originalCol++) {
				Cell cell = array[originalRow][originalCol];
				pattern1.array[originalRow][originalCol] = cell;
				pattern2.array[originalRow][width - originalCol - 1] = cell;
				pattern3.array[height - originalRow - 1][originalCol] = cell;
				pattern4.array[height - originalRow - 1][width - originalCol - 1] = cell;
				pattern5.array[originalCol][originalRow] = cell;
				pattern6.array[width - originalCol - 1][originalRow] = cell;
				pattern7.array[originalCol][height - originalRow - 1] = cell;
				pattern8.array[width - originalCol - 1][height - originalRow - 1] = cell;
			}
		}
		transformations.add(pattern1);
		transformations.add(pattern2);
		transformations.add(pattern3);
		transformations.add(pattern4);
		transformations.add(pattern5);
		transformations.add(pattern6);
		transformations.add(pattern7);
		transformations.add(pattern8);
		return transformations;
	}

	public boolean openTarget() {
		return openTarget;
	}

	public int getTargetRow() {
		return targetRow;
	}

	public int getTargetCol() {
		return targetCol;
	}
}
