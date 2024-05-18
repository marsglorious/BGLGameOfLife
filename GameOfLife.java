import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class GameOfLife extends JPanel {
    Timer timer;
    private Set<Cell> liveCells = new HashSet<>();
    public static final int WIDTH = 200, HEIGHT = 200, MAX_GENERATIONS = 100;;
    int generation = 0;
    private boolean isLive(Cell cell) {
        return liveCells.contains(cell);
    }

    private int countLiveNeighbors(Cell cell) {
        int x = cell.getX(), y = cell.getY(), count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (isLive(new Cell(x + i, y + j))) count++;
            }
        }
        return count;
    }

    private static class Cell {
        int x, y;
        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {return "[" + x + ", " + y + "]";}
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell other = (Cell) o;
            return x == other.x && y == other.y;
        }
        @Override
        public int hashCode() {return Objects.hash(x, y);}
        public int getX() {return x;}
        public int getY() {return y;}
    }

    private void nextGen() {
        if (generation > MAX_GENERATIONS) {
            timer.stop();
            return;
        }
        Set<Cell> nextLiveCells = new HashSet<>();
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                Cell cell = new Cell(x, y);
                int neighbors = countLiveNeighbors(cell);
                if (isLive(cell) && (neighbors == 2 || neighbors == 3)) nextLiveCells.add(cell);
                if (neighbors == 3) nextLiveCells.add(cell);
            }
        }
        System.out.println(generation + ": "+ liveCells);
        liveCells = nextLiveCells;
        generation++;
        repaint();
    }

    public GameOfLife(int[][] starterCells) {
        for (int[] cell : starterCells) {
            liveCells.add(new Cell(cell[0], cell[1]));
        }
        timer = new Timer(250, e -> nextGen());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth() / WIDTH, getHeight() / HEIGHT);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                g.setColor(isLive(new Cell(i, j)) ? Color.BLACK : Color.WHITE);
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    private static int[][] parseString(String s) {
        if (s.equals("[]") || s.equals("[ ]")) return new int[0][0];
        String[] cells = s.split("], \\[");
        int[][] result = new int[cells.length][2];
        for (int i = 0; i < cells.length; i++) {
            String[] cell = cells[i].replaceAll("\\[|]", "").split(", ");
            result[i] = new int[]{Integer.parseInt(cell[0]), Integer.parseInt(cell[1])};
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println("Author: Linus Maas");
        if (args.length != 1) {
            System.out.println("Usage: java GameOfLife \"[[x1, y1], [x2, y2], ...]\"");
            System.out.println("Example: java GameOfLife \"[[5, 5], [6, 5], [7, 5], [5, 6], [6, 6], [7, 6]]\"");
            return;
        }
        int[][] starterCells = parseString(args[0]);
        JFrame frame = new JFrame("Conway's Game of Life - Author: Linus Maas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500); // Adjust the size as needed
        frame.add(new GameOfLife(starterCells));
        frame.setVisible(true);
    }
}
