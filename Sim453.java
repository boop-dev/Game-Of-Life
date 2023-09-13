import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sim453 extends JPanel {

    private boolean[][] grid;
    private final int cellSize = 5;

    public Sim453(int width, int height, String config ) {
        grid = new boolean[height][width];
        int gridWidth = width * cellSize;
        int gridHeight = height * cellSize;
        setPreferredSize(new Dimension(gridWidth, gridHeight));

        if (Objects.equals(config, "R")){
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    grid[row][col] = Math.random() < 0.5;
                }
            }
        } else if (Objects.equals(config, "B")) {
            int [][] positions = {{2,5},{3,3},{3,5},{4,7},{5,2},{5,3},{5,4},{5,5},{5,6},{6,7},{7,3},{7,5},{8,5}};
            for (int[] position : positions) {
                grid[position[0]][position[1]] = true;
            }
        } else if (Objects.equals(config, "C")) {
            int [][] positions = {{2,10},{2,11},{3,9},{3,10},{4,11},{5,13},{5,14},{6,12},{8,11},{8,14},{9,3},{9,4},
                    {9,10},{9,11},{10,2},{10,3},{10,9},{11,4},{11,9},{11,11},{12,6},{12,7},{12,10},{13,6},{13,7}};
            for (int[] position : positions) {
                grid[position[0]][position[1]] = true;
            }
        }
    }


    public void update() {
        boolean[][] nextGen = new boolean[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);

                if (grid[row][col]) {
                    if (liveNeighbors < 2) {
                        nextGen[row][col] = false;
                    }
                    else nextGen[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    if (liveNeighbors == 3) {
                        nextGen[row][col] = true;
                    }
                }
            }
        }
        grid = nextGen;
    }

    private int countLiveNeighbors(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = (row + i + grid.length) % grid.length;
                int c = (col + j + grid[r].length) % grid[r].length;
                if (grid[r][c] && !(i == 0 && j == 0)) {
                    count++;
                }
            }
        }

        return count;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col]) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(col * cellSize,row * cellSize, cellSize, cellSize);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("How to run: java Sim453 <number of generations> <R, B or C> ");
            System.exit(1);
        }
        int width = 140;
        int height = 140;
        int numberOfGenerations = Integer.parseInt(args[0]);

        JFrame frame = new JFrame("Game of Life");
        Sim453 simulation = new Sim453(width, height, args[1]);
        frame.add(simulation);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        for (int i = 0; i < numberOfGenerations; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simulation.update();
            simulation.repaint();

        }
    }
}
