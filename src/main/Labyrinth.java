package main;
import java.util.Random;

public class Labyrinth {
    final int rows;
    final int cols;
    final int[][] map;
    final Random random = new Random();

    public static final int path = 0;
    public static final int wall = 1;
    public static final int small = 2;
    public static final int mid = 3;
    public static final int big = 4;
    public static final int start = 5;
    public static final int end = 5;
    public static final int leftwall = 6;
    public static final int rightwall = 7;
    public static final int bottomwall = 8;
    public static final int topwall = 9;

    public Labyrinth(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.map = new int[rows][cols];
        resetMap();
    }

    public void resetMap(){
        initializeMap();
        mapBorders();
        generateLabyrinth(1, 1);
        pathRandomizer();
        rewardsGenerator();
    }

    private void initializeMap() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = wall;
            }
        }
    }
    private void mapBorders() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 ) {
                    map[i][j] = topwall;
                } else if (i == rows - 1 ) {
                    map[i][j] = bottomwall;
                } else if (j == 0 && i != 0 && i != rows - 1) {
                    map[i][j] = leftwall;
                } else if (j == cols - 1 && i != 0 && i != rows - 1) {
                    map[i][j] = rightwall;
                }
            }
        }
    }

    private void generateLabyrinth(int x, int y) {
        map[x][y] = path;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  //right, down, left and up directions
        shuffleArray(directions);

        for (int[] d : directions) {
            int nx = x + d[0] * 2;
            int ny = y + d[1] * 2;
            if (isValidTile(nx, ny)) {
                map[x + d[0]][y + d[1]] = path;
                generateLabyrinth(nx, ny);
            }
        }
    }

    private void shuffleArray(int[][] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    //return true if the tile is within the borders and not already a path tile (it's a wall tile)
    private boolean isValidTile(int x, int y) {
        return x > 0 && x < rows - 1 && y > 0 && y < cols - 1 && map[x][y] == wall;
    }

    private void pathRandomizer() {
        int x = 1, y = 1;
        while (x < rows - 2 || y < cols - 2) {
            if (random.nextBoolean() && x < rows - 2) {
                map[x][y] = path;
                x++;
            } else if (y < cols - 2) {
                map[x][y] = path;
                y++;
            }
        }
        map[rows - 2][cols - 2] = end;
    }

    private void rewardsGenerator() {
        map[1][1] = start;  // starting point
        map[rows - 2][cols - 2] = end;  // end point

        int s = 0, b = 0, m = 0;


        while (s < 4 || m < 3 || b < 2) {
            int i = random.nextInt(rows);
            int j = random.nextInt(cols);


            if (map[i][j] == path && map[i][j] != start && map[i][j] != end) {

                if (s < 4 && (random.nextInt(3) == 0)) {
                    map[i][j] = small;
                    s++;
                } else if (m < 3 && (random.nextInt(3) == 1)) {
                    map[i][j] = mid;
                    m++;
                } else if (b < 2 && (random.nextInt(3) == 2)) {
                    map[i][j] = big;
                    b++;
                }
            }
        }
    }

    public int[][] getmap() {
        return map;
    }
}
