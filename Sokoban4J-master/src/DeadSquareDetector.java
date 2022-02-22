import game.board.compact.BoardCompact;
import game.board.compact.CTile;
import game.board.oop.Tile;

import java.util.*;


// TRUE IS DEAD
public class DeadSquareDetector {

    public static boolean[][] detect(BoardCompact board) {

        boolean[][] answer = new boolean[board.width()][board.height()];
        Queue<Pair> queue = new LinkedList<>();
        List<Pair> targets = new ArrayList<>();
        boolean[][] visited = new boolean[board.width()][board.height()];

        boolean upTile = true;
        boolean bottomTile = true;
        boolean leftTile = true;
        boolean rightTile = true;
        boolean upTileWall = false;
        boolean bottomTileWall = false;
        boolean leftTileWall = false;
        boolean rightTileWall = false;

        // pass 1
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {

                int tile = board.tile(x, y);

                if (CTile.isWall(tile)) {
                    answer[x][y] = true;
                    visited[x][y] = true;
                    continue;
                }

                if (CTile.forSomeBox(tile)) {
                    targets.add(new Pair(x, y));
                }

                answer[x][y] = false;
            }
        }


        // pass 2
        for (Pair target : targets) {

            queue.add(target);
            visited[target.x][target.y] = true;

            while (!queue.isEmpty()) {
                Pair v = queue.remove();
                int x = v.x;
                int y = v.y;


                // upper tile
                if (y != 0) {
                    upTile = answer[x][y - 1];
                    if (!upTile && !visited[x][y - 1]) {
                        queue.add(new Pair(x, y - 1));
                        visited[x][y - 1] = true;
                    }
                }

                // bottom tile
                if (y != board.height() - 1 ) {
                    bottomTile = answer[x][y + 1];
                    if (!bottomTile && !visited[x][y + 1]) {
                        queue.add(new Pair(x, y + 1));
                        visited[x][y + 1] = true;
                    }
                }

                // left tile
                if (x != 0) {
                    leftTile = answer[x - 1][y];
                    if (!leftTile && !visited[x - 1][y]) {
                        queue.add(new Pair(x - 1, y));
                        visited[x - 1][y] = true;
                    }
                }

                // right tile
                if (x != board.width() - 1) {
                    rightTile = answer[x + 1][y];
                    if (!rightTile && !visited[x + 1][y]){
                        queue.add(new Pair(x + 1, y));
                        visited[x + 1][y] = true;
                    }
                }

                // if v is target, we can ignore it.
                if (targets.contains(v)) {
                    continue;
                }

                // this is a dead square.
                if ((upTile || bottomTile) && (leftTile || rightTile)) answer[x][y] = true;

            }

        }

        // Pass 3
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {

                int tile = board.tile(x, y);

                if (CTile.isWall(tile) || CTile.forSomeBox(tile)) continue;

                if (y != 0) {
                    upTile = answer[x][y - 1];
                    upTileWall = CTile.isWall(board.tile(x, y - 1));
                }
                if (y != board.height() - 1 ) {
                    bottomTile = answer[x][y + 1];
                    bottomTileWall = CTile.isWall(board.tile(x, y + 1));
                }
                if (x != 0) {
                    leftTile = answer[x - 1][y];
                    leftTileWall = CTile.isWall(board.tile(x - 1, y));
                }
                if (x != board.width() - 1) {
                    rightTile = answer[x + 1][y];
                    rightTileWall = CTile.isWall(board.tile(x + 1, y));
                }

                if (upTile && bottomTile && (leftTileWall || rightTile)) answer[x][y] = true;
                if ((upTileWall || bottomTileWall) && leftTile && rightTile) answer[x][y] = true;
                if (upTileWall && bottomTileWall && (!leftTile || !rightTile)) answer[x][y] = false;
                if ((!upTile || !bottomTile) && leftTileWall && rightTileWall) answer[x][y] = false;


            }
        }

        return answer;
    }

}
