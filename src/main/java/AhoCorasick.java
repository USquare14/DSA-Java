import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Codeforces 163E

public class AhoCorasick
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static int[][] node;
    static int[] which;
    static int total;
    static int[] suflink;
    static ArrayList<Integer>[] g;
    static int MAXCHAR = 26;
    static int[] tin;
    static int[] tout;
    static int time;
    static int maxn = 1000001;

    static void insert(String s, int id){
        int curr = 0;
        for(int i = 0; i < s.length(); i++){
            int x = s.charAt(i) - 'a';
            if(node[curr][x] == -1) node[curr][x] = ++total;
            curr = node[curr][x];
        }
        which[id] = curr;
    }

    static void bfs(){
        suflink[0] = 0;
        Queue<Integer> q = new LinkedList<>();
        for(int i = 0; i < MAXCHAR; i++){
            if(node[0][i] != -1){
                suflink[node[0][i]] = 0;
                q.add(node[0][i]);
                g[0].add(node[0][i]);
            }
            else
                node[0][i] = 0;
        }
        while(!q.isEmpty()){
            int u = q.poll();
            for(int i = 0; i < MAXCHAR; i++){
                int v = node[u][i];
                if(v == -1) continue;
                int f = suflink[u];
                while(node[f][i] == -1) f = suflink[f];
                suflink[v] = node[f][i];
                if(suflink[v] != v) g[suflink[v]].add(v);
                q.add(v);
            }
        }
    }

    static void dfs(int u){
        tin[u] = time++;
        g[u].stream().forEach((v) -> {
            dfs(v);
        });
        tout[u] = time;
    }

    static int next_state(int curr, int c){
        int x = curr;
        while(node[x][c] == -1) x = suflink[x];
        return node[x][c];
    }

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

    static int solve(String s, int[] bit){
        int curr = 0;
        int ans = 0;
        for(int i = 0; i < s.length(); i++){
            int x = s.charAt(i) - 'a';
            curr = next_state(curr, x);
            ans += query(tin[curr], bit);
        }
        return ans;
    }

    static void solve()
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        int n = in.nextInt();
        int k = in.nextInt();

        node = new int[maxn][MAXCHAR];
        which = new int[k];
        suflink = new int[maxn];
        g = new ArrayList[maxn];
        int[] bit = new int[maxn + 2];
        total = 0;
        tin = new int[maxn];
        tout = new int[maxn];
        time = 0;

        for(int i = 0; i < maxn; i++){
            g[i] = new ArrayList<>();
            Arrays.fill(node[i], -1);
        }

        for(int i = 0; i < k; i++){
            String str = in.readString();
            insert(str, i);
        }

        bfs();
        dfs(0);
        boolean[] is = new boolean[k];
        for(int i = 0; i < k; i++){
            is[i] = true;
            update(tin[which[i]], 1, bit);
            update(tout[which[i]], -1, bit);
        }

        while(n-- > 0){
            String query = in.readString();
            if(query.charAt(0) == '+'){
                int id = Integer.parseInt(query.substring(1)) - 1;
                if(is[id]) continue;
                is[id] = true;
                update(tin[which[id]], 1, bit);
                update(tout[which[id]], -1, bit);
            }
            else if(query.charAt(0) == '-'){
                int id = Integer.parseInt(query.substring(1)) - 1;
                if(!is[id]) continue;
                is[id] = false;
                update(tin[which[id]], -1, bit);
                update(tout[which[id]], 1, bit);
            }
            else{
                out.println(solve(query.substring(1), bit));
            }
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
        },"1",1<<MAXCHAR).start();
    }

    static class Pair implements Comparable<Pair>{

        int x,k;
        int y,i;

        Pair (int x,int i){
            this.x=x;
            this.i=i;
        }

        Pair (int x,int y,int k,int i){
            this.x=x;
            this.y=y;
            this.k=k;
            this.i=i;
        }

        public int compareTo(Pair o) {
            return Integer.compare(this.k,o.k);
            //return 0;
        }

        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair p = (Pair)o;
                return p.x == x && p.y == y && p.i==i;
            }
            return false;
        }

        @Override
        public String toString() {
            return x+" "+y+" "+k+" "+i;
        }

        public int hashCode() {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode()+new Long(i).hashCode()*37;
        }

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
