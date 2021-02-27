import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class DPOnTree
{
    static InputReader in;
    static PrintWriter out;
    static int[] arr;
    static int[][] g;
    static int[] ans;
    static int[][] dp;
    
    static void dfs(int u, int p){
        dp[u][1] = arr[u];
        for(int v : g[u]){
            if(v == p) continue;
            dfs(v, u);
            dp[u][0] = Math.max(dp[u][0], dp[v][0]);
            dp[u][0] = Math.max(dp[u][0], dp[v][1] - arr[u]);
            dp[u][1] = Math.max(dp[u][1], dp[v][1]);
        }
    }
    
    static void dfs1(int u, int p, int[] a){
        ans[u] = dp[u][0];
        ans[u] = Math.max(ans[u], a[0]);
        ans[u] = Math.max(ans[u], a[1] - arr[u]);
        int max1 = 0;
        int max2 = 0;
        int ans1 = 0;
        int ans2 = 0;
        
        for(int v: g[u]){
            if(v == p) continue;
            if(dp[v][0] < ans1){
                if(dp[v][0] > ans2)
                    ans2 = dp[v][0];
            }
            else{
                ans2 = ans1;
                ans1 = dp[v][0];
            }
            if(dp[v][1] < max1){
                if(dp[v][1] > max2)
                    max2 = dp[v][1];
            }
            else{
                max2 = max1;
                max1 = dp[v][1];
            }
        }
        
        for(int v: g[u]){
            if(v == p) continue;
            int[] b = Arrays.copyOf(a, 2);
            if(ans1 == dp[v][0]){
                if(max1 == dp[v][1]){
                    b[0] = Math.max(b[0], ans2);
                    b[0] = Math.max(b[0], max2 - arr[u]);
                    b[0] = Math.max(b[0], a[1] - arr[u]);
                    b[1] = Math.max(b[1], max2);
                    b[1] = Math.max(b[1], arr[u]);
                }
                else{
                    b[0] = Math.max(b[0], ans2);
                    b[0] = Math.max(b[0], max1 - arr[u]);
                    b[0] = Math.max(b[0], a[1] - arr[u]);
                    b[1] = Math.max(b[1], max1);
                    b[1] = Math.max(b[1], arr[u]);
                }
            }
            else{
                if(max1 == dp[v][1]){
                    b[0] = Math.max(b[0], ans1);
                    b[0] = Math.max(b[0], max2 - arr[u]);
                    b[0] = Math.max(b[0], a[1] - arr[u]);
                    b[1] = Math.max(b[1], max2);
                    b[1] = Math.max(b[1], arr[u]);
                }
                else{
                    b[0] = Math.max(b[0], ans1);
                    b[0] = Math.max(b[0], max1 - arr[u]);
                    b[0] = Math.max(b[0], a[1] - arr[u]);
                    b[1] = Math.max(b[1], max1);
                    b[1] = Math.max(b[1], arr[u]);
                }
            }
            dfs1(v, u, b);
        }

    }
    static void solve(){
        
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);   
        
        int n = in.nextInt();
        int q = in.nextInt();
        arr = in.nextIntArray(n);
        int[] u = new int[n - 1];
        int[] v = new int[n - 1];
        dp = new int[n][2];
        ans = new int[n];
        
        for(int i = 0; i < n - 1; i++){
            u[i] = in.nextInt() - 1;
            v[i] = in.nextInt() - 1;
        }
        
        g = graph(u, v, n);
        dfs(0, -1);
        dfs1(0, -1, new int[2]);
        
        while(q-- > 0){
            int a = in.nextInt() - 1;
            out.println(ans[a]);
        }
        
        out.close();
        
        
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
        
    static int[][] graph(int from[], int to[], int n)
    {
        int g[][] = new int[n][];
        int cnt[] = new int[n];
        for (int i = 0; i < from.length; i++) {
            cnt[from[i]]++;
            cnt[to[i]]++;
        }
        for (int i = 0; i < n; i++) {
            g[i] = new int[cnt[i]];
        }
        Arrays.fill(cnt, 0);
        for (int i = 0; i < from.length; i++) {
            g[from[i]][cnt[from[i]]++] = to[i];
            g[to[i]][cnt[to[i]]++] = from[i];
        }
        return g;
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
 
        Pair (int x,int y, int i)
        {
                this.x = x;
                this.y = y;
                this.i = i;
        }
 
        public int compareTo(Pair o)
        {
            return -Long.compare(this.x,o.x);
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