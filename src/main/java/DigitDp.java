
import java.util.*;

/**
 * @author umangupadhyay
 */

//Another Ex : Count Pairs Morgan Stanley Campus Codeathon 2017

public class DigitDp {
    
    static long[][][] dp;
    static int k;

    static long solve(String s, int index, int smaller, int mod1) {

        if (index == s.length()) {
            if (mod1 == 0) {
                return 1;
            }
            return 0;
        }
        if (dp[index][smaller][mod1] != -1)
            return dp[index][smaller][mod1];
        else {
            int limit = 9;

            if (smaller != 0) {
                limit = (int) (s.charAt(index) - '0');
            }
            long init_count = 0;

            for (int i = 0; i <= limit; i++) {
                int ns;
                if (i < (int) (s.charAt(index) - '0')) {
                    ns = 0;
                } else {
                    ns = smaller;
                }
                init_count += solve(s, index + 1, ns, (mod1 + i * i * i) % k);
            }
            dp[index][smaller][mod1] = init_count;
            return init_count;
        }
    }
    
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        //dp[index][smaller][mod1]
        dp = new long[20][3][10000];
        String s = in.nextLine();
        String s2 = in.nextLine();
        k = in.nextInt();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 10000; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        long t1 = solve(s, 0, 1, 0);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 10000; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        long t2 = solve(s2, 0, 1, 0);
        System.out.println((t2 - t1));
    }
    
}
