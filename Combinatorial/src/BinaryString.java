import java.util.*;

class BinaryString {
    /**
     * Returns a list of all binary strings of length n
     */
    public static List<String> getBinaryStrings(int n) {
        List<String> retList = new ArrayList<>();
        helper(retList, "", n);
        return retList;
    }

    public static void helper(List<String> retList, String str, int n) {
        if (n == 0) {
            retList.add(str);
        }
        else {
            helper(retList, str + "0", n - 1);
            helper(retList, str + "1", n - 1);
        }
    }
}

