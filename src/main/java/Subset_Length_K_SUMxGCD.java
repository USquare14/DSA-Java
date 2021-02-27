import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

/*
Find sum of sum * gcd for all subset with size k
*/

public class Subset_Length_K_SUMxGCD
{

    static InputReader in;
    static PrintWriter out;
    static long mod = (long) (1e9 + 7);
    
    static long nCr(int n, int r, long[] fac, long[] invfac) {
        if(n < r) return 0;
        long res = fac[n] * invfac[r];
        res %= mod;
        res = res * invfac[n - r];
        res %= mod;
        return res;
    }
    
    public static void main(String[] args)
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);
        
        int n = in.nextInt();
        int k = in.nextInt();
        int maxn = 500001;
        long[] fac = new long[maxn];
        long[] invfac = new long[maxn];
        fac[0] = 1;
        for(int i = 1; i < maxn; i++)
            fac[i] = (fac[i - 1] * i) % mod;
        invfac[maxn - 1] = pow(fac[maxn - 1], mod - 2, mod);
        for(int i = maxn - 2; i >= 0; i--) {
            invfac[i] = (invfac[i + 1] * (i + 1)) % mod;
        }
        int[] cnt = new int[maxn];
        long[] sum = new long[maxn];
        
        for(int i = 0; i < n; i++) {
            int a = in.nextInt();
            cnt[a]++;
            sum[a] += a;
        }
        
        for(int i = 1; i < maxn; i++) {
            for(int j = i + i; j < maxn; j += i) {
                cnt[i] += cnt[j];
                sum[i] += sum[j];
            }
        }
        long res = 0;
        
        for(int i = maxn - 1; i >= 0; i--) {
            if(cnt[i] == 0) continue;
            long mul = nCr(cnt[i] - 1, k - 1, fac, invfac);
            sum[i] = (sum[i] * mul) % mod;
            for(int j = i + i; j < maxn; j += i) {
                sum[i] = (sum[i] - sum[j] + mod) % mod;
            }
            long here = i;
            here *= sum[i];
            here %= mod;
            res += here;
            res %= mod;
        }
        
        out.println(res);
        out.close(); 
    }

    static long pow(long n,long p,long m)
    {
         long  result = 1;
          if(p==0){
            return n;
          }
          
        while(p!=0)
        {
            if(p%2==1)
                result *= n;
            if(result >= m)
               result %= m;
            p >>=1;
            n*=n;
            if(n >= m)
                n%=m;
        }
        
        return result;
    }
 
    static void debug(Object... o)
    {
            System.out.println(Arrays.deepToString(o));
    }
 
 
    static class InputReader
    {
 
        private final InputStream stream;
        private final byte[] buf = new byte[8192];
        private int curChar, snumChars;
        private SpaceCharFilter filter;
 
        public InputReader(InputStream stream)
        {
                this.stream = stream;
        }
 
        public int snext()
        {
                if (snumChars == -1)
                        throw new InputMismatchException();
                if (curChar >= snumChars)
                {
                        curChar = 0;
                        try
                        {
                                snumChars = stream.read(buf);
                        } catch (IOException e)
                        {
                                throw new InputMismatchException();
                        }
                        if (snumChars <= 0)
                                return -1;
                }
                return buf[curChar++];
        }
 
        public int nextInt()
        {
                int c = snext();
                while (isSpaceChar(c))
                {
                        c = snext();
                }
                int sgn = 1;
                if (c == '-')
                {
                        sgn = -1;
                        c = snext();
                }
                int res = 0;
                do
                {
                        if (c < '0' || c > '9')
                                throw new InputMismatchException();
                        res *= 10;
                        res += c - '0';
                        c = snext();
                } while (!isSpaceChar(c));
                return res * sgn;
        }
 
        public long nextLong()
        {
                int c = snext();
                while (isSpaceChar(c))
                {
                        c = snext();
                }
                int sgn = 1;
                if (c == '-')
                {
                        sgn = -1;
                        c = snext();
                }
                long res = 0;
                do
                {
                        if (c < '0' || c > '9')
                                throw new InputMismatchException();
                        res *= 10;
                        res += c - '0';
                        c = snext();
                } while (!isSpaceChar(c));
                return res * sgn;
        }
 
        public int[] nextIntArray(int n)
        {
                int a[] = new int[n];
                for (int i = 0; i < n; i++)
                {
                        a[i] = nextInt();
                }
                return a;
        }
 
        public long[] nextLongArray(int n)
        {
                long a[] = new long[n];
                for (int i = 0; i < n; i++)
                {
                        a[i] = nextLong();
                }
                return a;
        }
 
        public String readString()
        {
                int c = snext();
                while (isSpaceChar(c))
                {
                        c = snext();
                }
                StringBuilder res = new StringBuilder();
                do
                {
                        res.appendCodePoint(c);
                        c = snext();
                } while (!isSpaceChar(c));
                return res.toString();
        }
 
        public String nextLine()
        {
                int c = snext();
                while (isSpaceChar(c))
                        c = snext();
                StringBuilder res = new StringBuilder();
                do
                {
                        res.appendCodePoint(c);
                        c = snext();
                } while (!isEndOfLine(c));
                return res.toString();
        }
 
        public boolean isSpaceChar(int c)
        {
                if (filter != null)
                        return filter.isSpaceChar(c);
                return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }
 
        private boolean isEndOfLine(int c)
        {
                return c == '\n' || c == '\r' || c == -1;
        }
 
        public interface SpaceCharFilter
        {
                public boolean isSpaceChar(int ch);
        }
 
    }
}    