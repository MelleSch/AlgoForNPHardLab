import game.board.compact.BoardCompact;
import game.board.compact.CTile;

import java.util.ArrayList;
import java.util.List;

public class MinTargetDistancesCalculator {

    public static int[][] distances(BoardCompact board) {

        int[][] answer = new int[board.width()][board.height()];
        List<Pair> targets = new ArrayList<>();

        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {

                int tile = board.tile(x, y);
                if (CTile.forSomeBox(tile)) {
                    targets.add(new Pair(x, y));
                }
            }
        }
        Pair firstTarget = targets.remove(0);
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                answer[x][y] = Math.abs(x - firstTarget.x) + Math.abs(y - firstTarget.y);
            }
        }

        for (Pair target : targets) {
            for (int x = 0; x < board.width(); x++) {
                for (int y = 0; y < board.height(); y++) {
                    int distance = Math.abs(x - target.x) + Math.abs(y - target.y);
                    if (distance < answer[x][y]) {
                        answer[x][y] = distance;
                    }
                }
            }
        }
        return answer;
    }
}
