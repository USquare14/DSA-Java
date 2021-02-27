
import java.math.BigInteger;
import java.util.*;

/**
 * @author umangupadhyay
 */

public class Hashing {

        static final int multiplier = 43;
        static final Random rnd = new Random();
        static final int mod1 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
        static final int mod2 = BigInteger.valueOf((int) (1e9 + rnd.nextInt((int) 1e9))).nextProbablePrime().intValue();
        static final int invMultiplier1 = BigInteger.valueOf(multiplier).modInverse(BigInteger.valueOf(mod1)).intValue();
        static final int invMultiplier2 = BigInteger.valueOf(multiplier).modInverse(BigInteger.valueOf(mod2)).intValue();

        long[] hash1, hash2;
        long[] inv1, inv2;
        int n;

        Hashing(String s) {
                n = s.length();
                hash1 = new long[n + 1];
                hash2 = new long[n + 1];
                inv1 = new long[n + 1];
                inv2 = new long[n + 1];
                inv1[0] = 1;
                inv2[0] = 1;

                long p1 = 1;
                long p2 = 1;
                for (int i = 0; i < n; i++) {
                        hash1[i + 1] = (hash1[i] + s.charAt(i) * p1) % mod1;
                        p1 = p1 * multiplier % mod1;
                        inv1[i + 1] = inv1[i] * invMultiplier1 % mod1;
                        hash2[i + 1] = (hash2[i] + s.charAt(i) * p2) % mod2;
                        p2 = p2 * multiplier % mod2;
                        inv2[i + 1] = inv2[i] * invMultiplier2 % mod2;
                }
        }

        long getHash(int i, int len) {
                return (((hash1[i + len] - hash1[i] + mod1) * inv1[i] % mod1) << 32)
                                + (hash2[i + len] - hash2[i] + mod2) * inv2[i] % mod2;
        }
}