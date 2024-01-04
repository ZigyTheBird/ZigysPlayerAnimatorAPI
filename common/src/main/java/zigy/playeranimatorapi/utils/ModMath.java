package zigy.playeranimatorapi.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//I stole these from Stack Overflow.
public class ModMath {
    public static Integer[] getDigits(int num) {
        if (num < 0) { return new Integer[0]; }
        List<Integer> digits = new ArrayList<>();
        collectDigits(num, digits);
        Collections.reverse(digits);
        return digits.toArray(new Integer[]{});
    }

    private static void collectDigits(int num, List<Integer> digits) {
        if(num / 10 > 0) {
            collectDigits(num / 10, digits);
        }
        digits.add(num % 10);
    }
}
