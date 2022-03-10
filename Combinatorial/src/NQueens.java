import java.util.ArrayList;
import java.util.List;

public class NQueens {
    /**
     * Returns the number of N-Queen solutions
     */
    public static int getNQueenSolutions(int n) {
        int count = 0;
        List<Tuple> formation = new ArrayList<>(n);
        int col = 0;
        int row = 0;
        while (col < n && row < n) {
            // For the first column check only the first half of the board to avoid horizontal symmetry, multiply the answer by two to make up for it.
            if (col == 0 && n % 2 == 0 && row >= n/2.0) {
                break;
            }
            if (col == 1 && n % 2 == 1 && formation.get(0).row == n/2 && row >= n/2.0) {
                break;
            }
            // Make a tuple for the new field to check
            Tuple tuple = new Tuple(row, col + row, col - row);
            // Check if the tuple is already contained by using the overridden equals method
            if (!formation.contains(tuple)) {
                // If in the final column then increase count
                if (col == n-1) {
                    count++;
                    row++;
                    // Backtrack
                    while (row >= n) {
                        if (col == 0) {
                            col = n;
                            break;
                        }
                        else {
                            // If so we track back to the previous column
                            col--;
                            // And set the row index to be the row index previously taken + 1
                            row = formation.remove(formation.size() - 1).row + 1;
                        }
                    }
                    continue;
                }
                else {
                    // Add the tuple to the formation
                    formation.add(tuple);
                    // Increment the column index and reset row to 0 and restart the while loop
                    col++;
                    row = 0;
                    continue;
                }
            }
            row++;
            // Check if the final row was reached without adding a tuple to the formation
            while (row >= n) {
                if (col == 0) {
                    col = n;
                    break;
                }
                else {
                    // If so we track back to the previous column
                    col--;
                    // And set the row index to be the row index previously taken + 1
                    row = formation.remove(formation.size()-1).row + 1;
                }
            }
        }
        return n > 1 ? count * 2 : count;
    }
}


class Tuple {
    public int row;
    public int uprightdiag;
    public int upleftdiag;

    public Tuple(int row, int uprightdiag, int upleftdiag) {
        this.row = row;
        this.uprightdiag = uprightdiag;
        this.upleftdiag = upleftdiag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return row == tuple.row || uprightdiag == tuple.uprightdiag || upleftdiag == tuple.upleftdiag;
    }
}

