import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * @author umangupadhyay
 */

// Sword Profit from university codesprint 5

public class LichaoSegmentTree
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static Point[] line;
    
    static class Point{
        long m,c;
        public Point(long m, long c) {
            this.m = m;
            this.c = c;
        }
        long get(long x) {
            if(m == 0 && c == 0)
                return Long.MAX_VALUE;
            return m * x + c;
        }
    }
    
    static void add_line(Point nw, int v, int l, int r) {
        int m = (l + r) >> 1;
        boolean lef = nw.get(l) < line[v].get(l);
        boolean mid = nw.get(m) < line[v].get(m);
        
        if(mid) {
            long tmp = nw.m;
            nw.m = line[v].m;
            line[v].m = tmp;
            tmp = nw.c;
            nw.c = line[v].c;
            line[v].c = tmp;
        }
        if(r - l == 1)
            return;
        else if(lef != mid)
            add_line(nw, 2 * v, l, m);
        else
            add_line(nw, 2 * v + 1, m, r);
    }
    
    static long get(long x, int v, int l, int r) {
        int m = (l + r) >> 1;
        if(r - l == 1)
            return line[v].get(x);
        else if(x < m)
            return Math.min(line[v].get(x), get(x, 2 * v, l, m));
        else
            return Math.min(line[v].get(x), get(x, 2 * v + 1, m, r));
    }
    
    static void solve() 
    {
        
        in = new InputReader(System.in);
        out = new PrintWriter(System.out); 
        
        int n = in.nextInt();
        long[] q = new long[n];
        long[] a = new long[n];
        long[] b = new long[n];
        long[] r = new long[n];
        long[] d = new long[n];
        
        for(int i = 0; i < n; i++) {
            q[i] = in.nextLong();
            a[i] = in.nextLong();
            b[i] = in.nextLong();
            r[i] = in.nextLong();
            d[i] = in.nextLong();
        }
        int maxn = 100005;
        line = new Point[4 * maxn];
        
        for(int i = 0; i < line.length; i++)
            line[i] = new Point(0, 0);
        
        long res = 0;
        long two = pow(2, mod - 2, mod);

        for(int i = n - 1; i >= 0; i--) {
            Point nw = new Point(i, (int)r[i]);
            add_line(nw, 1, 0, maxn);
            long x = q[i] + d[i] * i - get(d[i], 1, 0, maxn);
            if(x < 0) continue;
            long y = (x - a[i]) / b[i];
            if(y <= 0) continue;
            x %= mod;
            y %= mod;
            res = add(res, mul(y, x));
            long mul = mul(y, y + 1);
            mul = mul(mul, two);
            res = sub(res, add(mul(a[i], y), mul(mul, b[i])));
        }
        
        out.println(res);
        out.close(); 
    }
    
    static long add(long a, long b) {
        return (a + b) % mod;
    }
    
    static long mul(long a, long b) {
        return (a * b) % mod;
    }
    
    static long sub(long a, long b) {
        return (a - b + mod) % mod;
    } 
    
    public static void main(String[] args)
    {
        new Thread(null ,new Runnable(){
            public void run()
            {
                try{
                    solve();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        },"1",1<<26).start();
    }
    
    static class Pair implements Comparable<Pair>
    {

        long x,y;
        int i;
        
        Pair (long x,long y)
        {
                this.x = x;
                this.y = y;
        }

        public int compareTo(Pair o)
        {
            return -Long.compare(this.x, o.x);
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
            return x + " "+ y + " "+i;
        }

        /*public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }*/

    } 

    
    static String rev(String s){
        StringBuilder sb=new StringBuilder(s);
        sb.reverse();
        return sb.toString();
    }
    
    static long gcd(long x,long y)
    {
        if(y==0)
                return x;
        else
                return gcd(y,x%y);
    }

    static int gcd(int x,int y)
    {
        if(y==0)
                return x;
        else 
                return gcd(y,x%y);
    }

    static long pow(long n,long p,long m)
    {
         long  result = 1;
          if(p==0){
            return 1;
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

    static long pow(long n,long p)
    {
        long  result = 1;
          if(p==0)
            return 1;

        while(p!=0)
        {
            if(p%2==1)
                result *= n;	    
            p >>=1;
            n*=n;	    
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
