class Solution {

    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */
    public static int[][] solve(int[][] grid) {
        return validSudoku(grid) ? grid : null;
    }

    public static boolean validSudoku(int[][] grid) {
        int row = -1;
        int col = -1;
        boolean empty = true;

        for (int i = 0; i < grid.length; i++) {

            // find the next empty tile.
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == -1) {
                    row = i;
                    col = j;

                    empty = false;
                    break;
                }
            }
            if (!empty) break;
        }

        // if no empty tiles exist, solution exists.
        if (empty) return true;

        // try possible combinations for the empty tile.
        for (int n = 1; n <= grid.length; n++) {
            if (validPlacement(grid, row, col, n)) {
                grid[row][col] = n;
                if (validSudoku(grid)) return true;
                else grid[row][col] = -1;
            }
        }

        // no solution exists
        return false;
    }


    public static boolean validPlacement(int[][] grid, int row, int col, int n) {

        // checks if n already exists in the current row or column, return false if yes.
        for (int i = 0; i < grid.length; i++) {
            if (grid[row][i] == n) return false;
            if (grid[i][col] == n) return false;
        }

        // checks if n already exists in the 3x3 square, return false if yes.
        for (int x = row - row % 3; x <= row - row % 3 + 2; x++) {
            for (int y = col - col % 3; y <= col - col % 3 + 2; y++) {
                if (grid[x][y] == n) return false;
            }
        }
        return true;
    }


}



