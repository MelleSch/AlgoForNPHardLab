import java.util.*;

public class CombinationsNoRep {
    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        List<Integer> takeList = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            takeList.add(i);
        }
        List<int[]> answer = new ArrayList<>();
        helper(takeList, answer, 0, new int[n]);
        return answer;
    }

    public static void helper(List<Integer> takeList, List<int[]> answer, int index, int[] current) {
        if (takeList.isEmpty()) {
            answer.add(current);
        }
        for (int i = 0; i < takeList.size(); i++) {
            int[] currcopy = current.clone();
            List<Integer> takeListCopy = new ArrayList<>(takeList);
            currcopy[index] = takeListCopy.remove(i);
            helper(takeListCopy, answer, index + 1, currcopy);
        }
    }

    public static int[] promote(int[] prev, int n) {
        for (int i = n - 1; i >= 0; i--) {
            if (prev[i] < n) {
                prev[i]++;
                return prev;
            }
            if (i > 0) {

            }
        }
        return null;
    }
}
