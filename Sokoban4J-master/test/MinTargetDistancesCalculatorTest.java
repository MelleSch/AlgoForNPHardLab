import game.board.compact.BoardCompact;
import game.board.compact.CTile;
import game.board.oop.Board;
import game.board.oop.EPlace;
import game.board.oop.ESpace;

import java.io.File;

class MinTargetDistancesCalculatorTest {

    static void printLevelWithTargets(Board board) {
        for (int y = 0; y < board.height; ++y) {
            for (int x = 0; x < board.width; ++x) {
                EPlace place = board.tiles[x][y].place;
                ESpace space = board.tiles[x][y].space;

                if (place == EPlace.BOX_1) {
                    System.out.print('.');
                } else
                if (space != null) {
                    System.out.print(space.getSymbol());
                } else {
                    System.out.print("?");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        File levels = new File("Sokoban4J-master/levels/Aymeric_Medium.sok");
        if (!levels.canRead()) {
            System.out.printf("can't find level file %s\n", levels.getAbsolutePath());
            return;
        }

        System.out.printf("testing levels in %s\n\n", levels.getName());
        for (int i = 1 ; i <= 10 ; ++i) {
            System.out.printf("== level %d ==\n\n", i);
            Board board = Board.fromFileSok(levels, i);

            printLevelWithTargets(board);
            System.out.println();

            BoardCompact bc = board.makeBoardCompact();

            int[][] distances = MinTargetDistancesCalculator.distances(bc);

            System.out.println("Distances: \n");
            for (int y = 0 ; y < bc.height() ; ++y) {
                for (int x = 0 ; x < bc.width() ; ++x)
                    System.out.print(CTile.isWall(bc.tile(x, y)) ? '#' : "" + distances[x][y]);
                System.out.println();
            }
            System.out.println();
        }
    }
}