import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Week of Code 36 - Expert Computation

public class PersistanceCHTExample
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    
    static class MaxCht{
        double[] M;
        double[] B;
        int len;
        int ptr;
        
        public MaxCht(int n)
        {
                M = new double[n];
                B = new double[n];
                ptr = 0;
                len = 0;
        }
        
        void add(double m, double b)
        {
                while(len >= 2 && 
                        (double)(B[len - 2] - B[len - 1]) * (m - M[len - 1]) >= (double)(B[len - 1] - b) * (M[len - 1] - M[len - 2]))
                {
                    len--;
                }
                M[len] = m;
                B[len] = b;
                len++;
                
        }
        double get(double x,int i) {
                return M[i] * x + B[i];
        }

        double query(double x)
        {
            int i = 0;
            double ans = Long.MIN_VALUE;
            for(; i < len; i++){
                double tmp = get(x, i);
                if(tmp > ans){
                    ans = tmp;
                }
                else break;
            }
            return ans;
        }
    }

    static void update(int i, int[] cnt){

        for (i++; i < cnt.length; i += (i & -i)) {
            cnt[i]++;
        }
        
    }
    
    static void update(int i, double m, double b, MaxCht[] bit) {
        for (i++; i < bit.length; i += (i & -i)) {
            bit[i].add(m, b);
        }
    }

    static double query(int i, double x, MaxCht[] bit) {
        double ans = Long.MIN_VALUE;

        for (i++; i > 0; i -= (i & -i)) {
            ans = Math.max(ans, bit[i].query(x));
        }

        return ans;
    }
    
    public static void main(String[] args)
    {
        
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);            
        
        int n = in.nextInt();
        int[] cnt = new int[n + 2];
        
        for(int i = 0; i < n; i++)
            update(i, cnt);
        
        MaxCht[] bit = new MaxCht[n + 2];
        for(int i = 0; i < bit.length; i++)
            bit[i] = new MaxCht(cnt[i]);
        
        long[] x = in.nextLongArray(n);
        long[] y = in.nextLongArray(n);
        long[] z = in.nextLongArray(n);
        
        long[] h = new long[n];
        long[] c = new long[n];
        long[] l = new long[n];
        long g = 0;
        
        for(int i = 0; i < n; i++){
            h[i] = x[i] ^ g;
            c[i] = y[i] ^ g;
            l[i] = z[i] ^ g;
            update(i, h[i], -c[i], bit);
            double mx = query((int) (i - l[i]), (c[i] * 1.0) / h[i], bit);
            mx = mx * h[i];
            long here = Math.round(mx) % mod;
            g = (g + here + mod) % mod;
        }
        out.println(g);
        out.close();
    }
    
    static void debug(Object... o)
    {
            System.out.println(Arrays.deepToString(o));
    }
    
    static class Pair implements Comparable<Pair>
    {

        int x,y,i,t;
        
        
        Pair (int x,int y,int i, int t)
        {
		this.x = x;
                this.y = y;
                this.i = i;
                this.t = t;
	}
        
	public int compareTo(Pair o)
        {
            return Integer.compare(this.x,o.x);
		//return 0;
	}

        public boolean equals(Object o)
        {
            if (o instanceof Pair)
            {
                Pair p = (Pair)o;
                return p.x == x && p.y==y;
            }
            return false;
        }

        @Override
        public String toString()
        {
            return x + " "+ y + " "+ i + " "+t;
        }
        
        public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }
    
    } 

    static long gcd(long x, long y) {
        if (y == 0) {
            return x;
        } else {
            return gcd(y, x % y);
        }
    }

    static int gcd(int x, int y) {
        if (y == 0) {
            return x;
        } else {
            return gcd(y, x % y);
        }
    }

    static long pow(long n, long p, long m) {
        long result = 1;
        if (p == 0) {
            return 1;
        }

        while (p != 0) {
            if (p % 2 == 1) {
                result *= n;
            }
            if (result >= m) {
                result %= m;
            }
            p >>= 1;
            n *= n;
            if (n >= m) {
                n %= m;
            }
        }
        return result;
    }

    static long pow(long n, long p) {
        long result = 1;
        if (p == 0) {
            return 1;
        }

        while (p != 0) {
            if (p % 2 == 1) {
                result *= n;
            }
            p >>= 1;
            n *= n;
        }
        return result;
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
