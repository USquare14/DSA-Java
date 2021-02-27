import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class SubTreesAndPaths
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static ArrayList<Integer>[] g;
    static int[] val;
    static int[] lazy;
    static int t;    
    static int[] chainHead;
    static int[] size;
    static int[][] up;
    static int[] tin;
    static int[] tout;
    static int[] st;
    static int[] et;
    static int len; 
    static int[] depth;
    
    static void dfs(int u,int p){
        tin[u] = t++;
        up[0][u] = p;
        size[u] = 1;
        for (int i = 1; i < len; i++)
          up[i][u] = up[i - 1][up[i - 1][u]];

        for(int v : g[u]){
            if(v != p) {
                depth[v] = depth[u] + 1;
                dfs(v,u);
                size[u] += size[v];
            }
        }
        tout[u] = t++;
    }
    
    static void HLD(int curNode,int prev, int s) {
        st[curNode] = ++t;
        chainHead[curNode] = s;

        int sc = -1;
        for(int v : g[curNode])
            if(v != prev)
                if(sc == -1 || size[sc] < size[v]){
                    sc = v;
                }

        if(sc != -1)  HLD(sc, curNode, s);

        for(int v : g[curNode]){
            if(sc != v && prev != v){
                HLD(v, curNode, v);
            }
        }
        et[curNode] = t;
    }
    
    static int merge(int a,int b){
        if(a >= b) return a;
        return b;
    }

    static void lazyupdate(int ss,int ee,int si){
        if(lazy[si] != 0){
            if(ss != ee){
                lazy[2*si] += lazy[si];
                lazy[2*si+1] += lazy[si];
            }
            val[si] += lazy[si];
            lazy[si] = 0;
        }
    }
    
    static int getMid(int s, int e) { return (s+e)>>1; }

    static void update(int s, int e, int x, int y, int c, int si){
        if(s == x && e == y){
            lazy[si] += c;
        }
        else{
            lazyupdate(s, e, si);
            int mid = getMid(s, e);
            if(y <= mid)    
                update(s, mid, x, y, c, 2*si);
            else if(x > mid)
                update(mid + 1, e, x ,y ,c ,2*si + 1);
            else{
                update(s, mid, x, mid, c, 2*si);
                update(mid + 1, e, mid + 1, y, c, 2*si + 1);
            }
            lazyupdate(s, mid, 2*si);
            lazyupdate(mid + 1, e, 2*si + 1);
            val[si] = merge(val[2*si],val[2*si+1]);
        }
    }

    static int get(int s, int e, int x, int y, int si){
        lazyupdate(s, e,si);
        if(s == x && e == y){
            return val[si];
        }
        int mid = getMid(s, e);
        if(y <= mid)
            return get(s, mid, x, y, 2*si);
        else if(x > mid)
            return get(mid + 1, e, x, y, 2*si + 1);
        return merge(get(s, mid, x, mid, 2*si), get(mid + 1, e, mid + 1, y, 2*si + 1));
    }
 
    static int query_up(int u, int v) { 
        int ans = Integer.MIN_VALUE;
	while(depth[u] >= depth[v]) {
            ans = Math.max(ans, get(0, t - 1, Math.max(st[chainHead[u]], st[v]), st[u], 1));
            u = up[0][chainHead[u]];
	}
	return ans;
    }
    
    static boolean isParent(int parent, int child) {
        return tin[parent] <= tin[child] && tout[child] <= tout[parent];
    }

    static int lca(int a, int b) {
        if (isParent(a, b))
            return a;
        if (isParent(b, a))
            return b;
        for (int i = len - 1; i >= 0; i--)
            if (!isParent(up[i][a], b))
                a = up[i][a];
        return up[0][a];
    }
    
    static void solve(){
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);            
        
        int n = in.nextInt();
        g = new ArrayList[n + 1];
        len = 18;
        up = new int[len][n + 1];
        st = new int[n + 1];
        et = new int[n + 1];
        tin = new int[n + 1];
        tout = new int[n + 1];
        size = new int[n + 1];
        chainHead = new int[n + 1];
        depth = new int[n + 1];
        
        for(int i = 0; i <= n; i++) {
            g[i] = new ArrayList<>();
        }

        for(int i = 1; i < n; i++){
            int u = in.nextInt();
            int v = in.nextInt();
            g[u].add(v);
            g[v].add(u);
        }
        
        t = 0;
        depth[1] = 1;
        dfs(1, 1);
        t = 0;
        HLD(1, 1, 0);
        t++;
        val = new int[4 * t];
        lazy = new int[4 * t];

        int q = in.nextInt();
        
        while(q-- > 0){
            char c = in.readString().charAt(0);
            if(c == 'a'){
                int u = in.nextInt();
                int x = in.nextInt();
                update(0, t - 1, st[u], et[u], x, 1);
            }   
            else{
                int u = in.nextInt();
                int v = in.nextInt();
                int l = lca(u, v);
                int ans = Math.max(query_up(u, l), query_up(v, l));
                out.println(ans);
            }
        }
        
        out.close();
    }
    
    public static void main(String[] args)
    {
        new Thread(null ,new Runnable(){
            public void run(){
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

        int x,y;
        int i;
        long c;

        Pair (int x, long c)
        {
                this.x = x;
                this.c = c;
        }

        Pair (int x,int y)
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
            return Long.compare(this.c,o.c);
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
            return x + " "+ y + " "+c;
        }

        /*public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }*/

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
