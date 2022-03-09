import java.util.*;

public class Subsets {
    /**
     * Returns a list of all subsets in the set {1,...,n}
     */
    public static List<int[]> getSubsets(int n) {
        List<int[]> answer = new ArrayList<>();
        Set<Integer> currset = new HashSet<>();
        helper(answer, 1, currset, n);
        return answer;
    }

    public static void helper(List<int[]> answer, int index, Set<Integer> currset, int max) {
        if (index > max) {
            answer.add(currset.stream()
                    .mapToInt(Integer::intValue)
                    .toArray());
        }
        else {
            Set<Integer> currclone1 = new HashSet<>(currset);
            Set<Integer> currclone2 = new HashSet<>(currset);
            currclone2.add(index);
            helper(answer, index + 1, currclone1, max);
            helper(answer, index + 1, currclone2, max);
        }
    }
}
