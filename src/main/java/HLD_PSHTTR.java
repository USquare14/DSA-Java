
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class HLD_PSHTTR {
    
    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static ArrayList<Pair>[] g;
    static int[] val;
    static int t,ptr;    
    static int[] baseArray;
    static int chainNo;
    static int[] chainInd;
    static int[] chainHead;
    static int[] posInBase;
    static int[] size;
    static int[][] up;
    static int[] otherEnd;
    static int[] qt;
    static int[] tin;
    static int[] tout;
    static int len;
    
    static void dfs(int u,int p){
        tin[u] = t++;
        up[0][u] = p;
        size[u] = 1;
        for (int i = 1; i < len; i++)
          up[i][u] = up[i - 1][up[i - 1][u]];

        for (Pair p1 : g[u]) {
            if (p1.x != p) {
                otherEnd[p1.i] = p1.x;
                dfs(p1.x, u);
                size[u] += size[p1.x];
            }
        }
        tout[u] = t++;
    }
    
    static void HLD(int curNode,int cost,int prev) {
	if(chainHead[chainNo] == -1) {
		chainHead[chainNo] = curNode;
	}
	chainInd[curNode] = chainNo;
	posInBase[curNode] = ptr;
        baseArray[ptr++] = cost;
        
	int sc = -1;
	int ncost = 0;

        for (Pair p1 : g[curNode])
            if (p1.x != prev) {
                if (sc == -1 || size[sc] < size[p1.x]) {
                    sc = p1.x;
                    ncost = 0;
                }
            }

        if (sc != -1) {
            HLD(sc, ncost, curNode);
        }

        for (Pair p1 : g[curNode]) {
            if (sc != p1.x && prev != p1.x) {
                chainNo++;
                HLD(p1.x, 0, curNode);
            }
        }
        
    }

    static int merge(int a, int b) {
        return a ^ b;
    }

    static void build(int ss, int se, int si) {
        if (ss == se - 1) {
            val[si] = baseArray[ss];
            return;
        }
        int mid = getMid(ss, se);
        build(ss, mid, si * 2);
        build(mid, se, si * 2 + 1);
        val[si] = merge(val[si * 2], val[si * 2 + 1]);
    }

    static int getMid(int s, int e) {
        return (s + e) >> 1;
    }

    static void update(int s, int e, int x, int c, int si) {
        if (s > x || e <= x) {
            return;
        }
        if (s == x && s == e - 1) {
            val[si] = c;
        } else {
            int mid = getMid(s, e);
            update(s, mid, x, c, 2 * si);
            update(mid, e, x, c, 2 * si + 1);
            val[si] = merge(val[2 * si], val[2 * si + 1]);
        }
    }

    static void get(int s, int e, int x, int y, int si) {
        if (s >= y || e <= x) {
            qt[si] = 0;
            return;
        }
        if (s >= x && e <= y) {
            qt[si] = val[si];
            return;
        }
        int mid = getMid(s, e);
        get(s, mid, x, y, 2 * si);
        get(mid, e, x, y, 2 * si + 1);
        qt[si] = merge(qt[2 * si], qt[2 * si + 1]);
    }

    static int query_up(int u, int v) {
        if (u == v) {
            return 0;
        }
        int uchain, vchain = chainInd[v];
        int ans = 0;
        while (true) {
            uchain = chainInd[u];
            if (uchain == vchain) {
                if (u == v) {
                    break;
                }
                get(0, ptr, posInBase[v] + 1, posInBase[u] + 1, 1);
                ans ^= qt[1];
                break;
            }
            get(0, ptr, posInBase[chainHead[uchain]], posInBase[u] + 1, 1);
            ans ^= qt[1];
            u = chainHead[uchain];
            u = up[0][u];
        }
        return ans;
    }

    static boolean isParent(int parent, int child) {
        return tin[parent] <= tin[child] && tout[child] <= tout[parent];
    }

    static int lca(int a, int b) {
        if (isParent(a, b)) {
            return a;
        }
        if (isParent(b, a)) {
            return b;
        }
        for (int i = len - 1; i >= 0; i--) {
            if (!isParent(up[i][a], b)) {
                a = up[i][a];
            }
        }
        return up[0][a];
    }
    
    public static void main(String[] args) throws FileNotFoundException {
 
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);  
        
        int q = in.nextInt();
        
        while(q-->0){
            int n = in.nextInt();
            Pair[] edges = new Pair[n-1];
            g = new ArrayList[n];
            val = new int[4*n];
            qt = new int[4*n];
            len = 18;
            up = new int[len][n];
            tin = new int[n];
            tout = new int[n];
            size = new int[n];
            baseArray = new int[n+5];
            chainInd = new int[n];
            chainHead = new int[n+5];
            posInBase = new int[n+5];
            otherEnd = new int[n+5];
            //Don't forget extra space and -1
            
            Arrays.fill(chainHead, -1);
            
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }

            for (int i = 0; i < n - 1; i++) {
                int u = in.nextInt() - 1;
                int v = in.nextInt() - 1;
                int w = in.nextInt();
                g[u].add(new Pair(v, i));
                g[v].add(new Pair(u, i));
                edges[i] = new Pair(u, v, w, i);
            }
            ptr = 0;
            t = 0;
            chainNo = 0;
            dfs(0, 0);
            HLD(0,0,-1);
            build(0, ptr, 1);
            
            int m = in.nextInt();
            Pair[] query = new Pair[m];
            int[] ans = new int[m];
            
            for(int i=0;i<m;i++){
                int u = in.nextInt()-1;
                int v = in.nextInt()-1;
                int k = in.nextInt();
                query[i] = new Pair(u,v,k,i);
            }
            
            Arrays.sort(edges);
            Arrays.sort(query);

            int j = 0;
            for(int i=0;i<m;i++){
                Pair p = query[i];
                while(j<edges.length && edges[j].k<=p.k){
                    update(0, ptr, posInBase[otherEnd[edges[j].i]], edges[j].k, 1);
                    j++;
                }
                int l=lca(p.x, p.y);
                ans[p.i] = query_up(p.x, l)^query_up(p.y, l);
            }
            
            for(int i:ans) out.println(i);
        }
        
        out.close();
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

