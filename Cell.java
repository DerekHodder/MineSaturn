public class Cell {
	private int row;
	private int col;

	private boolean hasMine;
	private boolean opened = false;
	private boolean flagged = false;
	private boolean visible = false;

	private int internalNumber;
	private int externalNumber;

	private float score;

	public Cell(int r, int c, boolean mine) {
		row = r;
		col = c;
		hasMine = mine;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean hasMine() {
		return hasMine;
	}

	public void open() {
		if (opened) {
			throw new IllegalStateException("You cannot open an already-opened cell.");
		}
		if (flagged) {
			throw new IllegalStateException("You cannot open a flagged cell.");
		}
		opened = true;
	}

	public boolean isOpened() {
		return opened;
	}

	public void flag() {
		flagged = true;
	}

	public void unflag() {
		flagged = false;
	}

	public boolean hasFlag() {
		return flagged;
	}

	public void setVisible() {
		visible = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setInternalNumber(int unknowns) {
		internalNumber = unknowns;
	}

	public int getInternalNumber() {
		return internalNumber;
	}

	public void setExternalNumber(int surrounding) {
		externalNumber = surrounding;
	}

	public int getExternalNumber() {
		return externalNumber;
	}

	public void setScore(float s) {
		score = s;
	}

	public float getScore() {
		return score;
	}
}
