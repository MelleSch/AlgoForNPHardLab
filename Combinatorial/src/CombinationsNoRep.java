import java.util.*;

public class CombinationsNoRep {
    /**
     * Returns a list of all combinations of k elements from the set {1,...,n} without repetitions
     */
    public static List<int[]> getCombinationsWithoutRepetition(int n, int k) {
        List<int[]> answer = new ArrayList<>();
        int[] first = new int[k];
        for (int i = 0; i < k; i++) {
            first[i] = i + 1;
        }
        helper(answer, n, first);
        return answer;
    }

    public static void helper(List<int[]> answer, int n, int[] current) {
        while(current != null) {
            answer.add(current);
            int[] next = current.clone();
            next = promote(next, n);
            current = next;
        }
    }

    public static int[] promote(int[] prev, int n) {
        // Iterate over the array from last to first index looking for the index to promote
        for (int i = prev.length - 1; i >= 0; i--) {
            // If a value is less than the highest possible value for that index it and all of it's successors can be promoted
            // otherwise the previous index will need to be checked
            if (prev[i] < n - prev.length + i + 1) {
                prev[i]++;
                for (int j = i + 1; j < prev.length; j++) {
                    prev[j] = prev[j - 1] + 1;
                }
                return prev;
            }
        }
        return null;
    }
}
