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


        for (Pair target : targets) {



            queue.add(target);
            visited[target.x][target.y] = true;

            while (!queue.isEmpty()) {
//                System.out.print("Removing from queue ... x: " + queue.peek().x + ", y: " + queue.peek().y + "\n");
                Pair v = queue.remove();

                boolean upTile = true;
                boolean bottomTile = true;
                boolean leftTile = true;
                boolean rightTile = true;


                //&& !answer[v.x][v.y - 1]
                if (v.y != 0) {
                    upTile = answer[v.x][v.y - 1];
                    if (!upTile && !visited[v.x][v.y - 1]) {
                        queue.add(new Pair(v.x, v.y - 1));
                        visited[v.x][v.y - 1] = true;
                    }
                }
                if (v.y != board.height() - 1 ) {
                    bottomTile = answer[v.x][v.y + 1];
                    if (!bottomTile && !visited[v.x][v.y + 1]) {
                        queue.add(new Pair(v.x, v.y + 1));
                        visited[v.x][v.y + 1] = true;
                    }
                }
                if (v.x != 0) {
                    leftTile = answer[v.x - 1][v.y];
                    if (!leftTile && !visited[v.x - 1][v.y]) {
                        queue.add(new Pair(v.x - 1, v.y));
                        visited[v.x - 1][v.y] = true;
                    }
                }
                if (v.x != board.width() - 1) {
                    rightTile = answer[v.x + 1][v.y];
                    if (!rightTile && !visited[v.x + 1][v.y]){
                        queue.add(new Pair(v.x + 1, v.y));
                        visited[v.x + 1][v.y] = true;
                    }
                }

                // if v is target, we can ignore it.
                if (targets.contains(v)) {
                    continue;
                }

                // this is a dead square.
                if ((upTile || bottomTile) && (leftTile || rightTile)) answer[v.x][v.y] = true;

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
