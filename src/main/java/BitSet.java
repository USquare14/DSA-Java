/**
 * @author umangupadhyay
 */

public class BitSet {
    
    static class BitsetLong
    {

        static final int LOG = 6;
        static final int SIZE = 1 << LOG;
        static final int MASK = SIZE - 1;

        private long[] data;

        public BitsetLong(int n) {
            data = new long[(n >> LOG) + 2];
        }

        void set(int i) {
            data[i >> LOG] |= 1L << (i & MASK);
        }

        void unset(int i) {
            data[i >> LOG] &= ~(1L << (i & MASK));
        }

        void flip(int i) {
            data[i >> LOG] ^= 1L << (i & MASK);
        }

        int get(int i) {
            return (int) ((data[i >> LOG] >>> (i & MASK)) & 1);
        }

        void print() {
            for (int i = 0; i < data.length; i++) {
                System.out.print(Long.toString(data[i], 2));
            }
            System.out.println();
        }

        long getWord(int i) {
            if (i <= -SIZE || (i >> LOG) + 1 >= data.length) {
                return 0;
            }
            int rem = i & MASK;
            int idx = i >> LOG;

            if (rem == 0) {
                return data[idx];
            }

            long head = idx < 0 ? 0 : (data[idx] >>> rem);
            long tail = data[idx + 1] << (SIZE - rem);

            return head | tail;
        }
    }

    static int count(long[] bs, int len) {
        int ret = 0;
        for (int i = 0; i << 6 < len; i++) {
            if (((i + 1) << 6) <= len) {
                ret += Long.bitCount(bs[i]);
            } else {
                int rem = len & 63;
                ret += Long.bitCount(bs[i] << (64 - rem));
            }
        }
        return ret;
    }
    
    public static java.util.BitSet shiftLeft(java.util.BitSet a, int k) {
        long[] ar = a.toLongArray();
        int d = k >> 6;
        int r = k & 63;
        long[] rs = new long[Math.min(20000, ar.length + d + 1)];
        for (int i = d; i < rs.length; i++) {
                if (i - d < ar.length) {
                        rs[i] = ar[i - d] << r;
                }
                if (i - d - 1 >= 0 && r > 0) {
                        rs[i] |= ar[i - d - 1] >>> 64 - r;
                }
        }
        return java.util.BitSet.valueOf(rs);
    }
    
}
