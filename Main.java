import java.util.Queue;
import java.util.LinkedList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	final int BOARD_HEIGHT = 16;
	final int BOARD_WIDTH = 30;
	final int NUM_OF_MINES = 99;

	final int CELL_LENGTH = 20; // MANY GRAPHICS HAVE BEEN HARD-CODED TO THIS VALUE; DO NOT CHANGE

	final int SPEED = 0;
	final int WAIT = 0;

	private Queue<Boolean> masteryList = new LinkedList<Boolean>();

	private Board gameBoard;
	private Solver boardSolver = new Solver();

	private MainPanel mainPanel;

	public static void main(String[] args) {
		System.out.println("Hello, Saturn!");
		new Main();
	}

	public Main() {
		gameBoard = new Board(BOARD_HEIGHT, BOARD_WIDTH, NUM_OF_MINES);
		JFrame frame = new JFrame("MineSaturn");
		ImageIcon icon = new ImageIcon("minesaturntransparent.png");
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new MainPanel();
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
		while(true) {
			gameBoard = new Board(BOARD_HEIGHT, BOARD_WIDTH, NUM_OF_MINES);
			solve();
			try {
				Thread.sleep(WAIT);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	public class MainPanel extends JPanel {
		public MainPanel() {

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(CELL_LENGTH * BOARD_WIDTH, CELL_LENGTH * BOARD_HEIGHT);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			Font pixelFont = new Font("Monospaced", Font.BOLD, 20);
			g2d.setFont(pixelFont);
			Color white = new Color(255, 255, 255);
			Color lightGray = new Color(192, 192, 192);
			Color darkGray = new Color(128, 128, 128);
			Color black = new Color(0, 0, 0);
			Color red = new Color(255, 0, 0);
			Color darkRed = new Color(127, 0, 0);
			Color darkGreen = new Color(0, 127, 0);
			Color blue = new Color(0, 0, 255);
			Color darkBlue = new Color(0, 0, 127);
			Color cyan = new Color(0, 127, 127);
			for (int row = 0; row < BOARD_HEIGHT; row++) {
				for (int col = 0; col < BOARD_WIDTH; col++) {
					Cell cell = gameBoard.array[row][col];
					if (cell.isOpened()) {
						g2d.setColor(darkGray);
						g2d.fillRect(CELL_LENGTH * col, CELL_LENGTH * row, CELL_LENGTH, CELL_LENGTH);
						g2d.setColor(lightGray);
						g2d.fillRect(CELL_LENGTH * col + 1,
							CELL_LENGTH * row + 1, CELL_LENGTH - 1, CELL_LENGTH - 1);
						if (cell.hasMine()) {
							g2d.setColor(black);
							g2d.fillOval(CELL_LENGTH * col + 5, CELL_LENGTH * row + 5, 10, 10);
							g2d.drawLine(CELL_LENGTH * col + 10, CELL_LENGTH * row + 4,
							CELL_LENGTH * col + 10, CELL_LENGTH * row + 16);
							g2d.drawLine(CELL_LENGTH * col + 6, CELL_LENGTH * row + 6,
							CELL_LENGTH * col + 14, CELL_LENGTH * row + 14);
							g2d.drawLine(CELL_LENGTH * col + 4, CELL_LENGTH * row + 10,
							CELL_LENGTH * col + 16, CELL_LENGTH * row + 10);
							g2d.drawLine(CELL_LENGTH * col + 6, CELL_LENGTH * row + 14,
							CELL_LENGTH * col + 14, CELL_LENGTH * row + 6);
						} else if (cell.getExternalNumber() != 0) {
							if (cell.getExternalNumber() == 1) {
								g2d.setColor(blue);
							} else if (cell.getExternalNumber() == 2) {
								g2d.setColor(darkGreen);
							} else if (cell.getExternalNumber() == 3) {
								g2d.setColor(red);
							} else if (cell.getExternalNumber() == 4) {
								g2d.setColor(darkBlue);
							} else if (cell.getExternalNumber() == 5) {
								g2d.setColor(darkRed);
							} else if (cell.getExternalNumber() == 6) {
								g2d.setColor(cyan);
							} else if (cell.getExternalNumber() == 7) {
								g2d.setColor(black);
							} else if (cell.getExternalNumber() == 8) {
								g2d.setColor(darkGray);
							}
							g2d.drawString(String.valueOf(cell.getExternalNumber()),
							CELL_LENGTH * col + 5, CELL_LENGTH * row + 17);
						}
					} else {
						g2d.setColor(white);
						g2d.fillRect(CELL_LENGTH * col, CELL_LENGTH * row, CELL_LENGTH, CELL_LENGTH);
						g2d.setColor(darkGray);
						int[] x1 = {CELL_LENGTH * col,
							CELL_LENGTH * col + CELL_LENGTH, CELL_LENGTH * col + CELL_LENGTH};
						int[] y1 = {CELL_LENGTH * row + CELL_LENGTH,
							CELL_LENGTH * row, CELL_LENGTH * row + CELL_LENGTH};
						g2d.fillPolygon(x1, y1, 3);
						g2d.setColor(lightGray);
						g2d.fillRect(CELL_LENGTH * col + 2,
							CELL_LENGTH * row + 2, CELL_LENGTH - 4, CELL_LENGTH - 4);
						if (cell.hasFlag()) {
							g2d.setColor(red);
							int[] x2 = {CELL_LENGTH * col + 5,
								CELL_LENGTH * col + 12, CELL_LENGTH * col + 12};
							int[] y2 = {CELL_LENGTH * row + 7,
								CELL_LENGTH * row + 10, CELL_LENGTH * row + 4};
							g2d.fillPolygon(x2, y2, 3);
							g2d.setColor(black);
							g2d.fillRect(CELL_LENGTH * col + 12,
								CELL_LENGTH * row + 4, 2, 12);
						}
					}
				}
			}
			if (gameBoard.isExploded()) {
				
			} else if (gameBoard.hasWon()) {
				
			}
			g2d.dispose();
		}
	}

	public void solve() {
		while (!gameBoard.isExploded()
		&& !gameBoard.hasWon()) {
			gameBoard = boardSolver.iterate(gameBoard);
			try {
				Thread.sleep(SPEED);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			mainPanel.repaint();
		}
		while (masteryList.size() >= 100) {
			masteryList.remove();
		}
		masteryList.add(gameBoard.hasWon());
		int wins = 0;
		for (Boolean result : masteryList) {
			if (result) {
				wins++;
			}
		}
		System.out.print(String.valueOf(wins));
		System.out.print(" / ");
		System.out.println(String.valueOf(masteryList.size()));
	}
}
