public class Grid {
	protected int height;
	protected int width;
	protected Cell[][] array;

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setCell(int row, int col, Cell cell) {
		array[row][col] = cell;
	}

	public Cell getCell(int row, int col) {
		return array[row][col];
	}
}