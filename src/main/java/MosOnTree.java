import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Codeforces 100962F
//Another one : KRYP2 in codechef

public class MosOnTree
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static int[][][] g;
    static int[] tin;
    static int[] tout;
    static int time;
    static int len;
    static int[][] up;
    static int[] arr;
    static int[] a;
    static int n;
    static boolean[] vis;
    static int[] cnt;
    
    static void dfs(int u, int p){
        a[time] = u;
        tin[u] = time++;
        up[0][u] = p;
        for (int i = 1; i < len; i++)
            up[i][u] = up[i - 1][up[i - 1][u]];
        
        for(int[] item: g[u]){
            if(item[0] == p) continue;
            arr[item[0]] = item[1];
            dfs(item[0], u);
        }
        a[time] = u;
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
    
    static int block;
    static int cl,cr;
    static int[] bit;
    
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
    
    static void add(int idx){
        int u = a[idx];
        if(arr[u] > n || arr[u] < 0) return;
        if(vis[u]){
            if(cnt[arr[u]] == 1)
                update(arr[u], -1, bit);
            cnt[arr[u]]--;
        }
        else{
            if(cnt[arr[u]] == 0)
                update(arr[u], 1, bit);
            cnt[arr[u]]++;
        }
        vis[u] = !vis[u];
    }
    
    static void remove(int idx){
        int u = a[idx];
        if(arr[u] > n || arr[u] < 0) return;
        if(vis[u]){
            if(cnt[arr[u]] == 1)
                update(arr[u], -1, bit);
            cnt[arr[u]]--;
        }
        else{
            if(cnt[arr[u]] == 0)
                update(arr[u], 1, bit);
            cnt[arr[u]]++;
        }
        vis[u] = !vis[u];
    }
    
    public static void main(String[] args)
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);            
        
        n = in.nextInt();
        int q = in.nextInt();
        int[] u = new int[n];
        int[] v = new int[n];
        int[] w = new int[n];
        arr = new int[n];
        a = new int[2 * n];
        bit = new int[n + 3];
        vis = new boolean[n];
        cnt = new int[n + 1];

        for(int i = 0; i < n - 1; i++){
            u[i] = in.nextInt() - 1;
            v[i] = in.nextInt() - 1;
            w[i] = in.nextInt();
        }
        g = get(u, v, w, n);
        Arrays.fill(arr, -1);
        preprocess(n, 0);
        block = (int) Math.sqrt(time);
        Pair[] query = new Pair[q];
        
        for(int i = 0; i < q; i++){
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            if(tin[a] > tin[b]){
                a = a ^ b;
                b = a ^ b;
                a = a ^ b;
            }
            query[i] = new Pair(0, 0, i, -1);
            int l = lca(a, b);
            if(l == a){
                query[i].x = tin[a] + 1; // query on edge + 1, otherwise + 0
                query[i].y = tin[b];
            }
            else{
                query[i].x = tout[a];
                query[i].y = tin[b];
                query[i].k = l;
            }
        }
        Arrays.sort(query);
        int[] ans = new int[q];
        cl = 0;
        cr = -1;
        
        for(int i = 0; i < q; i++){
            int l = query[i].x;
            int r = query[i].y;
            
            while(cl > l) add(--cl);
            while(cr < r) add(++cr);
            while(cl < l) remove(cl++);
            while(cr > r) remove(cr--);
            
            l = 0;
            r = n;
            //debug(query[i],a);
/*            
            for(int j = 0; j <= n; j++){
                debug(j,get(j, j, bit));
            }*/
            while(r - l >= 0){
                int mid = (r + l) >> 1;
                //debug(query(mid,bit),mid + 1);
                if(query(mid, bit) < mid + 1){
                    r = mid - 1;
                    ans[query[i].i] = mid;
                }
                else l = mid + 1;
            }
            
        }
        
        for(int i : ans)
            out.println(i);
        
        out.close();

    }

    static int[][][] get(int from[], int to[], int[] w, int n)
    {
        int g[][][] = new int[n][][];
        int cnt[] = new int[n];
        for (int i = 0; i < from.length; i++) {
            cnt[from[i]]++;
            cnt[to[i]]++;
        }
        for (int i = 0; i < n; i++) {
            g[i] = new int[cnt[i]][2];
        }
        for (int i = 0; i < from.length; i++) {
            g[from[i]][--cnt[from[i]]][0] = to[i];
            g[from[i]][cnt[from[i]]][1] = w[i];
            g[to[i]][--cnt[to[i]]][0] = from[i];
            g[to[i]][cnt[to[i]]][1] = w[i];
        }
        
        return g;
    }

    static void debug(Object... o)
    {
            System.out.println(Arrays.deepToString(o));
    }

    static class Pair implements Comparable<Pair>
    {

        int x,y,i,k;
        
        Pair (int x,int y, int i, int k)
        {
		this.x = x;
                this.y = y;
                this.i = i;
                this.k = k;
	}
        
	public int compareTo(Pair o)
        {
            if(this.x / block !=  o.x / block)
                return Integer.compare(this.x / block, o.x / block);
            return Integer.compare(this.y,o.y);
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
            return x + " "+ y + " " + i+" "+k;
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
