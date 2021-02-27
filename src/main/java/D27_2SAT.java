import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * @author umangupadhyay
 */

public class D27_2SAT
{

    static long mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static ArrayList<Integer>[] g;
    static ArrayList<Integer>[] rg;
    static boolean[] vis;
    static int[] comp;
    static Stack<Integer> st;
    
    static void dfs1(int u){
        vis[u] = true;
        for(int v : g[u])
            if(!vis[v])
                dfs1(v);
        st.add(u);
    }

    static void dfs2(int u, int c){
        comp[u] = c;
        for(int v : rg[u])
            if(comp[v] == -1)
                dfs2(v, c);
    }

    static boolean[] solve2SAT(){
        int n = g.length;
        vis = new boolean[n];
        comp = new int[n];
        st = new Stack<>();
        
        for(int i = 0; i < n; i++){
            if(!vis[i])
                dfs1(i);
        }
        
        rg = new ArrayList[n];
        for(int i = 0; i < n; i++)
            rg[i] = new ArrayList<>();
        
        for(int i = 0; i < n; i++){
            for(int j : g[i])
                rg[j].add(i);
        }
        
        Arrays.fill(comp, -1);
        int c = 0;
        while(!st.isEmpty()){
            int u = st.pop();
            if(comp[u] == -1)
                dfs2(u, c++);
        }
        
        for(int i = 0; i < n; i++)
            if(comp[i] == comp[i ^ 1])
                return null;
        
        boolean[] ans = new boolean[n / 2];
        
        for(int i = 0; i < n; i += 2)
            ans[i / 2] = comp[i] > comp[i ^ 1];
        
        return ans;
    }
    
    static boolean intersect(Pair p1, Pair p2){
        
        if(p1.x < p2.x && p2.x < p1.y && p1.y < p2.y)
            return true;
        
        return false;
    }
    
    static void solve()
    {

        in = new InputReader(System.in);
        out = new PrintWriter(System.out);  
        
        int n = in.nextInt();
        int m = in.nextInt();
        
        Pair[] list = new Pair[m];
        
        for(int i = 0; i < m; i++){
            list[i] = new Pair(in.nextInt(), in.nextInt());
            if(list[i].x > list[i].y){
                list[i].x = list[i].x ^ list[i].y;
                list[i].y = list[i].x ^ list[i].y;
                list[i].x = list[i].x ^ list[i].y;
            }
        }
        
        g = new ArrayList[2 * m];
        for(int i = 0; i < 2 * m; i++)
            g[i] = new ArrayList<>();
        
        for(int i = 0; i < m; i++){
            for(int j = 0 ; j < m; j++){
                if(intersect(list[i],list[j]) || intersect(list[j], list[i])){
                    g[2 * i].add(2 * j + 1);
                    g[2 * i + 1].add(2 * j);
                }
            }
        }
        
        boolean [] ans = solve2SAT();
        
        if(ans == null) out.println("Impossible");
        else{
            for(int i = 0; i < m; i++)
                out.print(ans[i] ? "i" : "o");
            out.println();
        }
        
        out.close();

    }
    
    
    public static void main(String[] args)
    {

        new Thread(null,new Runnable()
        {
            @Override
            public void run()
            {
                try{
                    solve();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        },"1",1<<26).start();
    }

    static class Pair implements Comparable<Pair>
    {

        int i;
        int x, y;
        
	Pair (int x,int y,int i)
        {
		this.x = x;
		this.y = y;
                this.i = i;
	}
        
        Pair(int x, int y){
            this.x = x;
            this.y = y;
        }
        
	public int compareTo(Pair o)
        {
            return Long.compare(this.i,o.i);
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
        
        public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }
    
    } 


    static class Merge
    {

        public static void sort(int inputArr[])
        {
            int length = inputArr.length;
            doMergeSort(inputArr,0, length - 1);
        }

        private static void doMergeSort(int[] arr,int lowerIndex, int higherIndex)
        {        
            if (lowerIndex < higherIndex)
            {
                int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
                doMergeSort(arr,lowerIndex, middle);
                doMergeSort(arr,middle + 1, higherIndex);
                mergeParts(arr,lowerIndex, middle, higherIndex);
            }
        }

        private static void mergeParts(int[]array,int lowerIndex, int middle, int higherIndex)
        {
            int[] temp=new int[higherIndex-lowerIndex+1];
            for (int i = lowerIndex; i <= higherIndex; i++)
            {
                temp[i-lowerIndex] = array[i];
            }
            int i = lowerIndex;
            int j = middle + 1;
            int k = lowerIndex;
            while (i <= middle && j <= higherIndex)
            {
                if (temp[i-lowerIndex] < temp[j-lowerIndex])
                {
                    array[k] = temp[i-lowerIndex];
                    i++;
                } else
                {
                    array[k] = temp[j-lowerIndex];
                    j++;
                }
                k++;
            }
            while (i <= middle)
            {
                array[k] = temp[i-lowerIndex];
                k++;
                i++;
            }
            while(j<=higherIndex)
            {
                array[k]=temp[j-lowerIndex];
                k++;
                j++;
            }
        }

    }

    static long add(long a,long b)
    {
        long x = a + b;
        if(x >= mod) x -= mod;
        return x;
    }

    static long sub(long a,long b)
    {
        long x= a - b ;
        if(x < 0) x += mod;
        return x;
    }

    static long mul(long a,long b)
    {
        a %= mod;
        b %= mod;
        long x = a*b;
        return x%mod;
    }


    static boolean isPal(String s)
    {
        for(int i=0, j=s.length()-1;i<=j;i++,j--)
        {
                if(s.charAt(i)!=s.charAt(j)) return false;
        }
        return true;
    }
    static String rev(String s)
    {
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

    static long gcdExtended(long a,long b,long[] x)
    {

        if(a==0)
        {
            x[0]=0;
            x[1]=1;
            return b;
        }
        long[] y=new long[2];
        long gcd=gcdExtended(b%a, a, y);

        x[0]=y[1]-(b/a)*y[0];
        x[1]=y[0];

        return gcd;
    }


    static long mulmod(long  a,long b,long m)
    {
        if (m <= 1000000009) return a * b % m;

        long res = 0;
        while (a > 0)
        {
            if ((a&1)!=0)
            {
                res += b;
                if (res >= m) res -= m;
            }
            a >>= 1;
            b <<= 1;
            if (b >= m) b -= m;
        }
        return res;
    }

    static int abs(int a,int b)
    {
        return (int)Math.abs(a-b);
    }

    public static long abs(long a,long b)
    {
        return (long)Math.abs(a-b);
    }

    static int max(int a,int b)
    {
        if(a>b)
                return a;
        else
                return b;
    }

    static int min(int a,int b)
    {
        if(a>b)
                return b;
        else 
                return a;
    }

    static long max(long a,long b)
    {
        if(a>b)
                return a;
        else
                return b;
    }

    static long min(long a,long b)
    {
        if(a>b)
                return b;
        else 
                return a;
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

    static void debug(Object... o)
    {
            System.out.println(Arrays.deepToString(o));
    }

    static class InputReader
    {

        private final InputStream stream;
        private final byte[] buf = new byte[8192];
        private int curChar, snumChars;

        public InputReader(InputStream st) {
                this.stream = st;
        }

        public int read() {
                if (snumChars == -1)
                        throw new InputMismatchException();
                if (curChar >= snumChars) {
                        curChar = 0;
                        try {
                                snumChars = stream.read(buf);
                        } catch (IOException e) {
                                throw new InputMismatchException();
                        }
                        if (snumChars <= 0)
                                return -1;
                }
                return buf[curChar++];
        }

        public int nextInt() {
                int c = read();
                while (isSpaceChar(c)) {
                        c = read();
                }
                int sgn = 1;
                if (c == '-') {
                        sgn = -1;
                        c = read();
                }
                int res = 0;
                do {
                        res *= 10;
                        res += c - '0';
                        c = read();
                } while (!isSpaceChar(c));
                return res * sgn;
        }

        public long nextLong() {
                int c = read();
                while (isSpaceChar(c)) {
                        c = read();
                }
                int sgn = 1;
                if (c == '-') {
                        sgn = -1;
                        c = read();
                }
                long res = 0;
                do {
                        res *= 10;
                        res += c - '0';
                        c = read();
                } while (!isSpaceChar(c));
                return res * sgn;
        }

        public int[] nextIntArray(int n) {
                int a[] = new int[n];
                for (int i = 0; i < n; i++) {
                        a[i] = nextInt();
                }
                return a;
        }

        public String readString() {
                int c = read();
                while (isSpaceChar(c)) {
                        c = read();
                }
                StringBuilder res = new StringBuilder();
                do {
                        res.appendCodePoint(c);
                        c = read();
                } while (!isSpaceChar(c));
                return res.toString();
        }

        public String nextLine() {
                int c = read();
                while (isSpaceChar(c))
                        c = read();
                StringBuilder res = new StringBuilder();
                do {
                        res.appendCodePoint(c);
                        c = read();
                } while (!isEndOfLine(c));
                return res.toString();
        }

        public boolean isSpaceChar(int c) {
                return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        private boolean isEndOfLine(int c) {
                return c == '\n' || c == '\r' || c == -1;
        }

    }
}    
