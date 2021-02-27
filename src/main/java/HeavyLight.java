import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

// IDOLS

public class HeavyLight
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static int[][] g;
    static int[][] val;
    static int t,ptr;    
    static int[] baseArray;
    static int chainNo;
    static int[] chainInd;
    static int[] chainHead;
    static int[] posInBase;
    static int[] size;
    static int[][] up;
    static int[] tin;
    static int[] tout;
    static int len;
    static int[] depth;
    static int[] arr;
    
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
    
    static void HLD(int curNode,int prev) {
	if(chainHead[chainNo] == -1) {
		chainHead[chainNo] = curNode;
	}
	chainInd[curNode] = chainNo;
	posInBase[curNode] = ptr;
        baseArray[ptr++] = arr[curNode] - depth[curNode];
        
	int sc = -1;
        for(int v : g[curNode])
            if(v != prev)
                if(sc == -1 || size[sc] < size[v]){
                    sc = v;
                }

        if(sc != -1)  HLD(sc,curNode);

        for(int v : g[curNode]){
            if(sc != v && prev != v){
                chainNo++;
                HLD(v,curNode);
            }
        }
        
    }
    
    static int[] merge(int[] a,int[] b){
        int[] c = new int[2];
        if(a[0] > b[0]){
            c[0] = a[0];
            c[1] = a[1];
        }
        else if(a[0] < b[0]){
            c[0] = b[0];
            c[1] = b[1];
        }
        else{
            c[0] = a[0];
            c[1] = a[1] + b[1];
        }
        
        return c;
    }
    
    static void build(int ss, int se, int si){
        if (ss == se) {
            val[si][0] = baseArray[ss];
            val[si][1] = 1;
            return;
        }
        int mid = getMid(ss, se);
        build(ss, mid, si * 2 );
        build(mid  + 1, se, si * 2 + 1);
        val[si] = merge(val[si*2],val[si*2+1]);
    }
   
    static int getMid(int s, int e) { return (s+e)>>1; }

    static void update(int s, int e, int x, int y, int c, int si){
        if(s == x && e == y){
            val[si][0] = c;
            val[si][1] = 1;
        }   
        else{
            int mid = getMid(s, e);
            if(y <= mid)    
                update(s, mid, x, y, c, 2*si);
            else if(x > mid)
                update(mid + 1, e, x ,y ,c ,2*si + 1);
            else{
                update(s, mid, x, mid, c, 2*si);
                update(mid + 1, e, mid + 1, y, c, 2*si + 1);
            }
            val[si] = merge(val[2*si],val[2*si+1]);
        }
    }

    static int[] get(int s, int e, int x, int y, int si){

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
 
    static int[] query_up(int u, int v) { 
	int uchain, vchain = chainInd[v];
        int[] ans = new int[2];
        ans[0] = Integer.MIN_VALUE;
	while(true) {
            uchain = chainInd[u];
            if(uchain == vchain) {
                ans = merge(ans, get(0, ptr - 1, posInBase[v], posInBase[u],1));
                break;
            }
            ans = merge(ans, get(0, ptr - 1, posInBase[chainHead[uchain]], posInBase[u],1));
            u = chainHead[uchain];
            u = up[0][u];
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
        int q = in.nextInt();
        g = new int[n][];
        val = new int[4 * n][2];
        len = 18;
        up = new int[len][n];
        tin = new int[n];
        tout = new int[n];
        size = new int[n];
        baseArray = new int[n];
        chainInd = new int[n];
        chainHead = new int[n];
        posInBase = new int[n];
        depth = new int[n];
        arr = in.nextIntArray(n);
        
        Arrays.fill(chainHead, -1);
        int[] sz = new int[n];
        int[] u = new int[n];
        int[] v = new int[n];
        
        for(int i = 1; i < n; i++){
            u[i] = in.nextInt() - 1;
            v[i] = in.nextInt() - 1;
            sz[u[i]]++;
            sz[v[i]]++;
        }
        
        for(int i = 0; i < n; i++){
            g[i] = new int[sz[i]];
            sz[i] = 0;
        }
        
        for(int i = 1; i < n; i++){
            g[u[i]][sz[u[i]]++] = v[i];
            g[v[i]][sz[v[i]]++] = u[i];
        }
        
        ptr = 0;
        t = 0;
        chainNo = 1;
        
        dfs(0, 0);
        HLD(0,-1);
        build(0, ptr - 1, 1);
        
        while(q-- > 0){
            int t = in.nextInt();
            int z = in.nextInt() - 1;
            if(t == 0){
                int x = in.nextInt();
                update(0, ptr - 1, posInBase[z], posInBase[z], x - depth[z], 1);
            }
            else{
                int[] ans = query_up(z, 0);
                ans[0] += depth[z];
                out.println(ans[0] + " " + ans[1]);
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
