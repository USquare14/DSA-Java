import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * @author umangupadhyay
 */

public class BridgeTree {
    
    static long mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static boolean[] isbridge;
    static boolean[] vis;
    static int[] arr;
    static int time;
    static int cmpno;
    static int[] u;
    static int[] v;
    static int[] cmpid;
    static ArrayList<Integer>[] g;
    static ArrayList<Integer>[] tree;
    static Queue<Integer>[] queue;
    
    static int adj(int a, int e){
        return u[e] == a ? v[e] : u[e];
    }
    
    static int dfs0(int u, int e){
        
        vis[u] = true;
        arr[u] = time ++;
        int dbe =  arr[u];
        
        for(int i: g[u]){
            int v = adj(u, i);
            if(!vis[v])
                dbe = min(dbe, dfs0(v, i));
            else if(i != e)
                dbe = min(dbe, arr[v]);
        }   
        
        if(dbe == arr[u] && e != -1) {
            isbridge[e] = true;
        }
        
        return dbe;
    }
    
    static void dfs1(int u){
        
        int curcmp = cmpno;
        queue[curcmp].add(u);
        vis[u] = true;
        while(!queue[curcmp].isEmpty()){
 
            int v = queue[curcmp].poll();
            cmpid[v] = curcmp;
            for(int i: g[v]){
                int w = adj(v, i);
                if(vis[w]) continue;
                if(isbridge[i]){
                    cmpno++;
                    tree[curcmp].add(cmpno);
                    tree[cmpno].add(curcmp);
                    dfs1(w);
                }
                else{
                    queue[curcmp].add(w);
                    vis[w] = true;
                }
            }
            
        }
    }
    
    static int len;
    static int[][] up;
    static int[] tin;
    static int[] tout;
    static int[] depth;

    static void dfs(int u, int p) {
        tin[u] = time++;
        up[0][u] = p;
        for (int i = 1; i < len; i++)
            up[i][u] = up[i - 1][up[i - 1][u]];
        for (int v :tree[u])
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
        depth = new int[n];
        dfs(root, root);
        
    }
    
    static int dist(int a, int b){
        return depth[a] + depth[b] - 2*depth[lca(a, b)];
    }
    
    static int getPath(int a,int u,int b, int v){
        
        if(isParent(u, v)){
            if(isParent(v, a)){
                return dist(v, lca(a,b));
            }
        }
        if(isParent(v, u)){
            if(isParent(u, b)){
                return dist(u, lca(a,b));
            }
        }
        
        return 0;
    }
    
    static int get(int c, int d, int a, int b){
        int ans = dist(c, d);
        int lcd = lca(c, d);
        int lab = lca(a, b);
        
        ans -= getPath(c, lcd, b, lab);
        ans -= getPath(c, lcd, a, lab);
        ans -= getPath(d, lcd, b, lab);
        ans -= getPath(d, lcd, a, lab);
        
        return ans;
    }
    
    static void solve() {
        
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);  
        
        int n = in.nextInt();
        int m = in.nextInt();
        int q = in.nextInt();
        
        isbridge = new boolean[m];
        vis = new boolean[n];
        arr = new int[n];
        g = new ArrayList[n];
        tree = new ArrayList[n];
        queue = new Queue[n];
        u = new int[m];
        v = new int[m];
        cmpid = new int[n];
        
        for(int i = 0; i < n; i++){
            g[i] = new ArrayList<>();
            tree[i] = new ArrayList<>();
            queue[i] = new LinkedList<>();
        }
        
        for(int i = 0; i < m; i++){
            u[i] = in.nextInt() - 1;
            v[i] = in.nextInt() - 1;
            g[u[i]].add(i);
            g[v[i]].add(i);
        }
        
        time = 0;
        cmpno = 0;
        dfs0(0, -1);
        Arrays.fill(vis, false);
        dfs1(0);
        time = 0;
        preprocess(n, 0);   
        
        while(q-- > 0){
            int a = in.nextInt() - 1;
            int b = in.nextInt() - 1;
            int c = in.nextInt() - 1;
            int d = in.nextInt() - 1;
            out.println(get(cmpid[c], cmpid[d], cmpid[a], cmpid[b]));
        }
        
        out.close();
        
    }
    
    public static void main(String[] args) {
 
        new Thread(null,new Runnable() {
            @Override
            public void run() {
                try{
                    solve();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        },"1",1<<26).start();
    }
    
    static class Pair implements Comparable<Pair>{
 
        int x,y;
        int i;
        
	Pair (int x,int y,int i){
		this.x=x;
		this.y=y;
		this.i=i;
	}
 
	Pair (int x,int y){
		this.x = x;
		this.y = y;
	}
        
	public int compareTo(Pair o) {
            return Long.compare(this.x,o.x);
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
            return x+" "+y+" "+i;
        }
        
        public int hashCode() {
            return new Integer(x).hashCode() * 31 + new Integer(y).hashCode()+new Integer(i).hashCode()*37;
        }

    } 
    
    static class Merge {
        
        public static void sort(long inputArr[]) {
            int length = inputArr.length;
            doMergeSort(inputArr,0, length - 1);
        }
        
        private static void doMergeSort(long[] arr,int lowerIndex, int higherIndex) {        
            if (lowerIndex < higherIndex) {
                int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
                doMergeSort(arr,lowerIndex, middle);
                doMergeSort(arr,middle + 1, higherIndex);
                mergeParts(arr,lowerIndex, middle, higherIndex);
            }
        }
        
        private static void mergeParts(long[]array,int lowerIndex, int middle, int higherIndex) {
            long[] temp=new long[higherIndex-lowerIndex+1];
            for (int i = lowerIndex; i <= higherIndex; i++) {
                temp[i-lowerIndex] = array[i];
            }
            int i = lowerIndex;
            int j = middle + 1;
            int k = lowerIndex;
            while (i <= middle && j <= higherIndex) {
                if (temp[i-lowerIndex] < temp[j-lowerIndex]) {
                    array[k] = temp[i-lowerIndex];
                    i++;
                } else {
                    array[k] = temp[j-lowerIndex];
                    j++;
                }
                k++;
            }
            while (i <= middle) {
                array[k] = temp[i-lowerIndex];
                k++;
                i++;
            }
            while(j<=higherIndex){
                array[k]=temp[j-lowerIndex];
                k++;
                j++;
            }
        }
 
    }
    
    static long add(long a,long b){
        long x=(a+b);
        while(x>=mod) x-=mod;
        return x;
    }
 
    static long sub(long a,long b){
        long x=(a-b);
        while(x<0) x+=mod;
        return x;
    }
    
    static long mul(long a,long b){
        a%=mod;
        b%=mod;
        long x=(a*b);
        return x%mod;
    }
 
    
    static boolean isPal(String s){
        for(int i=0, j=s.length()-1;i<=j;i++,j--){
                if(s.charAt(i)!=s.charAt(j)) return false;
        }
        return true;
    }
    static String rev(String s){
		StringBuilder sb=new StringBuilder(s);
		sb.reverse();
		return sb.toString();
    }
    
    static long gcd(long x,long y){
	if(y==0)
		return x;
	else
		return gcd(y,x%y);
    }
    
    static int gcd(int x,int y){
	if(y==0)
		return x;
	else 
		return gcd(y,x%y);
    }
    
    static long gcdExtended(long a,long b,long[] x){
        
        if(a==0){
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
    
 
    static long mulmod(long  a,long b,long m) {
        if (m <= 1000000009) return a * b % m;
 
        long res = 0;
        while (a > 0) {
            if ((a&1)!=0) {
                res += b;
                if (res >= m) res -= m;
            }
            a >>= 1;
            b <<= 1;
            if (b >= m) b -= m;
        }
        return res;
    }
    
    static int abs(int a,int b){
	return (int)Math.abs(a-b);
    }
 
    public static long abs(long a,long b){
	return (long)Math.abs(a-b);
    }
    
    static int max(int a,int b){
	if(a>b)
		return a;
	else
		return b;
    }
 
    static int min(int a,int b){
	if(a>b)
		return b;
	else 
		return a;
    }
    
    static long max(long a,long b){
	if(a>b)
		return a;
	else
		return b;
    }
 
    static long min(long a,long b){
	if(a>b)
		return b;
	else 
		return a;
    }
 
    static long pow(long n,long p,long m){
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
    
    static long pow(long n,long p){
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
    
    static void debug(Object... o) {
            System.out.println(Arrays.deepToString(o));
    }
     
    static class InputReader {
    
        private final InputStream stream;
        private final byte[] buf = new byte[8192];
        private int curChar, snumChars;
        private SpaceCharFilter filter;
 
        public InputReader(InputStream stream) {
                this.stream = stream;
        }
 
        public int snext() {
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
                int c = snext();
                while (isSpaceChar(c)) {
                        c = snext();
                }
                int sgn = 1;
                if (c == '-') {
                        sgn = -1;
                        c = snext();
                }
                int res = 0;
                do {
                        if (c < '0' || c > '9')
                                throw new InputMismatchException();
                        res *= 10;
                        res += c - '0';
                        c = snext();
                } while (!isSpaceChar(c));
                return res * sgn;
        }
 
        public long nextLong() {
                int c = snext();
                while (isSpaceChar(c)) {
                        c = snext();
                }
                int sgn = 1;
                if (c == '-') {
                        sgn = -1;
                        c = snext();
                }
                long res = 0;
                do {
                        if (c < '0' || c > '9')
                                throw new InputMismatchException();
                        res *= 10;
                        res += c - '0';
                        c = snext();
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
 
        public long[] nextLongArray(int n) {
                long a[] = new long[n];
                for (int i = 0; i < n; i++) {
                        a[i] = nextLong();
                }
                return a;
        }
 
        public String readString() {
                int c = snext();
                while (isSpaceChar(c)) {
                        c = snext();
                }
                StringBuilder res = new StringBuilder();
                do {
                        res.appendCodePoint(c);
                        c = snext();
                } while (!isSpaceChar(c));
                return res.toString();
        }
 
        public String nextLine() {
                int c = snext();
                while (isSpaceChar(c))
                        c = snext();
                StringBuilder res = new StringBuilder();
                do {
                        res.appendCodePoint(c);
                        c = snext();
                } while (!isEndOfLine(c));
                return res.toString();
        }
 
        public boolean isSpaceChar(int c) {
                if (filter != null)
                        return filter.isSpaceChar(c);
                return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }
 
        private boolean isEndOfLine(int c) {
                return c == '\n' || c == '\r' || c == -1;
        }
 
        public interface SpaceCharFilter {
                public boolean isSpaceChar(int ch);
        }
        
    }
}    
 
