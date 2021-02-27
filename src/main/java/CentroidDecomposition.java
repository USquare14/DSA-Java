import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//look at Codeforces 766E for another version || Palindrome Tree in hackerearth || BSTMaintenance in hackerrank

public class CentroidDecomposition
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static int[][] g;
    static ArrayList<Integer>[] tree;
    static int[] sub;
    static boolean[] del;
    static int nn;
    static int len;
    static int[][] up;
    static int[] tin;
    static int[] tout;
    static int time;
    static int n;
    static int root;
    static long[][] downdp;
    static long[][] updp;
    static int[] depth;
    static int[] arr;
    static int[][] cnt;
    static int[] par;
    
    static void dfs(int u, int p) {
      tin[u] = time++;
      up[0][u] = p;
      for (int i = 1; i < len; i++)
        up[i][u] = up[i - 1][up[i - 1][u]];
      for (int v :g[u])
        if (v != p){
          depth[v] = depth[u] + 1;
          dfs( v, u);
        }
      tout[u] = time++;
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

    static void preprocess(int n,int root){
      len = 1;
      while ((1 << len) <= n) ++len;
      up = new int[len][n];
      tin = new int[n];
      tout = new int[n];
      dfs(root, root);
    }
    
    static long dist(int a,int b){
        return depth[a] + depth[b] - 2 * depth[lca(a,b)];
    }
    
    static void dfs1(int u,int p){
        sub[u] = 1;
        nn++;
        for(int v:g[u]){
            if(v != p && !del[v]){
                dfs1(v, u);
                sub[u] += sub[v];
            }
        }
    }
    
    static int find(int u,int p){
        for(int v: g[u]){
            if(v != p && sub[v] > nn/2 && !del[v])
                return find(v, u);
        }
        return u;
    }    
    
    static void decompose(int r,int p){
        nn = 0;
        dfs1(r, r);
        int centroid = find(r, r);
        if(p != -1){
            tree[centroid].add(p);
            tree[p].add(centroid);
        }
        else{
            p = centroid;
            root = centroid;
        }
        par[centroid] = p;
        del[centroid] = true;
        for(int v : g[centroid]){
            if(del[v]) continue;
            decompose(v, centroid); 
        }
    }
    
    static void dfs2(int u, int p,int x, int p2){
        downdp[arr[u]][x] += dist(u, x);
        cnt[arr[u]][x]++;
        if(p2 != -1) updp[arr[u]][x] += dist(u, p2);
        for(int v : tree[u]){
            if(v == p) continue;
            dfs2(v, u, x, p2);
        }
    }
    
    static void dfs3(int u, int p){
        dfs2(u, p, u, p);
        for(int v : tree[u]){
            if(v == p) continue;
            dfs3(v, u);
        }
    }
    
    static long query(int u, int x){
        long ans = downdp[x][u];
        int v = u;
        
        while(u != par[u]){
            ans += cnt[x][par[u]] * dist(v, par[u]) + downdp[x][par[u]];
            ans -= cnt[x][u] * dist(v, par[u]) + updp[x][u];
            u = par[u];
        }
        
        return ans;
    }
    
    static void add(int u, int x){
        int v = u;

        while(true){
            downdp[x][u] += dist(u, v);
            cnt[x][u]++;
            if(u == par[u]) break;
            updp[x][u] += dist(par[u], v);
            u = par[u];
        }
        
    }
    
    static void rem(int u, int x){
        int v = u;
        
        while(true){
            downdp[x][u] -= dist(u, v);
            cnt[x][u]--;
            if(u == par[u]) break;
            updp[x][u] -= dist(par[u], v);
            u = par[u];
        }
    }
    
    public static void main(String[] args)
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);            
        
        n = in.nextInt();
        int m = in.nextInt();
        depth = new int[n];
        downdp = new long[2][n];
        updp = new long[2][n];
        cnt = new int[2][n];
        g = new int[n][];
        tree = new ArrayList[n];
        sub = new int[n];
        del = new boolean[n];
        arr = in.nextIntArray(n);
        par = new int[n];
        
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
            tree[i] = new ArrayList<>();
            sz[i] = 0;
        }
        
        for(int i = 1; i < n; i++){
            g[u[i]][sz[u[i]]++] = v[i];
            g[v[i]][sz[v[i]]++] = u[i];
        }
        
        preprocess(n, 0);
        decompose(0, -1);
        dfs3(root, -1);
        
        while(m -- > 0){
            int t = in.nextInt();
            int x = in.nextInt() - 1;
            if(t == 1){
                rem(x, arr[x]);
                arr[x] ^= 1;
                add(x, arr[x]);
            }
            else{
                out.println(query(x, arr[x]));
            }
        }
        
        out.close();
    }
    
    static void debug(Object... o)
    {
            System.out.println(Arrays.deepToString(o));
    }

    static class Pair implements Comparable<Pair>
    {

        int x,y;
        
        Pair (int x, int y)
        {
		this.x = x;
                this.y = y;
	}
	
	public int compareTo(Pair o) {
                return Integer.compare(this.y,o.y);
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
            return x + " "+ y;
        }
        
        public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }
    
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
          if(p==0)
            return 1;

        while(p!=0)
        {
            if(p%2==1)
                result *= n;
            if(result>=m)
            result%=m;
            p >>=1;
            n*=n;
            if(n>=m)
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