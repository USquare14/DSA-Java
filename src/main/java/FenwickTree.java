/**
 * @author umangupadhyay
 */

public class FenwickTree {
    
                
    static void update(int i, int val, int[] bit) {
        for (i++; i < bit.length; i += (i & -i)) {
            bit[i] += val;
        }
    }

    static int query(int i, int[] bit) {
        int ans = 0;

        for (i++; i > 0; i -= (i & -i)) {
            ans += bit[i];
        }

        return ans;
    }

    static int get(int l, int r, int[] bit) {
        if (l > r) {
            return 0;
        }
        return query(r, bit) - query(l - 1, bit);
    }

//2D    
    static int[][] f;

    static void update(int x, int y, int val) {
        for (int i = x; i < f.length; i = i | (i + 1)) {
            for (int j = y; j < f[0].length; j = j | (j + 1)) {
                f[i][j] += val;
            }
        }
    }

    static int getSum(int x, int y) {
        int res = 0;
        for (int i = x; i >= 0; i = (i & (i + 1)) - 1) {
            for (int j = y; j >= 0; j = (j & (j + 1)) - 1) {
                res += f[i][j];
            }
        }
        return res;
    }

    static int getSum(int xFrom, int xTo, int yFrom, int yTo) {
        return getSum(xTo, yTo) - getSum(xTo, yFrom - 1) - getSum(xFrom - 1, yTo) + getSum(xFrom - 1, yFrom - 1);
    }

}
