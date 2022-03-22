class Solution {

    public static int[] primes;

    /**
     * Returns the filled in sudoku grid.
     *
     * @param grid the partially filled in grid. unfilled positions are -1.
     * @return the fully filled sudoku grid.
     */
    public static int[][] solve(int[][] grid) {
        primes = new int[grid.length];
        fillPrimes();
        return validSudoku(grid) ? grid : null;
    }

    public static void fillPrimes() {
        int i = 0;
        // Calculate magical upper bound of the n-th prime
        double v = Math.log(primes.length) / Math.log(Math.E);
        int ub = (int) (primes.length * (v + Math.log(v) / Math.log(Math.E)));
        boolean[] notPrime = new boolean[ub];
        int index = 2;
        while (i < primes.length) {
            while(notPrime[index]) {
                index++;
            }
            if (!notPrime[index]) {
                primes[i] = index;
                for (int j = 2; j < ub / index; j++) {
                    notPrime[j * index] = true;
                }
                i++;
                index++;
            }
        }
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

        // checks if n already exists in the sqrtnxsqrtn square, return false if yes.
        int sqrtn = ((int) Math.sqrt(grid.length));
        for (int x = row - row % sqrtn; x < row - row % sqrtn + sqrtn; x++) {
            for (int y = col - col % sqrtn; y < col - col % sqrtn + sqrtn; y++) {
                if (grid[x][y] == n) return false;
            }
        }
        return true;
    }


}



