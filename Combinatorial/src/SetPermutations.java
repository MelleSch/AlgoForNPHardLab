import java.util.*;

public class SetPermutations {
    /**
     * Returns a list of all permutations in the set {1,...,n}
     */
    public static List<int[]> getSetPermutations(int n) {
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
        else {
            for (int i = 0; i < takeList.size(); i++) {
                int[] currcopy = current.clone();
                List<Integer> takeListCopy = new ArrayList<>(takeList);
                currcopy[index] = takeListCopy.remove(i);
                helper(takeListCopy, answer, index + 1, currcopy);
            }
        }
    }
}
