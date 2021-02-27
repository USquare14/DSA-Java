
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class HLDTrick {
    
    static int mod = (int) (1e9+7);
    static ArrayList<Integer>[] g;
    static int[] sub;
    static int[] depth;
    static int[] big;
    static int[] arr;
    static int m;
    static int max=(int) 1e6+5;
    static long ans;
    static SegmentTree[] segmentTrees;
    
    public static class SegmentTree{
        int st[];
 
        SegmentTree(int n)  {
            st = new int[4*n];
        }
    
        public int getMid(int s, int e) {
            return (s+e)>>1;
        }

        public int merge(int a,int b){
            return a+b;
        }
        
        public void update(int s, int e, int x, int y, int c, int si){
            if(s == x && e == y){
                st[si]=c;
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
                st[si]=merge(st[2*si],st[2*si+1]);
            }
        }

        public int get(int s, int e, int x, int y, int si){

            if(s == x && e == y){
                return st[si];
            }
            int mid = getMid(s, e);
            if(y <= mid)
                return get(s, mid, x, y, 2*si);
            else if(x > mid)
                return get(mid + 1, e, x, y, 2*si + 1);
            return merge(get(s, mid, x, mid, 2*si), get(mid + 1, e, mid + 1, y, 2*si + 1));
        }

    }
    
    public static void dfs(int u,int p){
        sub[u]=1;
        for(int v:g[u]){
            if(v==p) continue;
            depth[v]=depth[u]+1;
            dfs(v,u);
            sub[u]+=sub[v];
        }
    }
    
    static void add(int u,int p,int x){  
        segmentTrees[depth[u]%m].update(0, max-1, arr[u],arr[u], x, 1);
        for(int v: g[u])
            if(big[v] != 1 && v != p)
                add(v, u,x);
    }

    static void trick(int u,int p,boolean k){
        int mx = -1, b = 0;
        for(int v: g[u]){
            if(v == p) continue;
            if(sub[v]>mx){
                mx = sub[v];
                b = v;
            }
        }
        
        for(int v: g[u])
            if(b != v && v != p)
               trick(v,u,false);

        if(mx != -1){
            big[b] = 1;
            trick(b,u,true);
        }
        
        add(u, p,1);
        long tmp = segmentTrees[depth[u]%m].get(0, max-1, 0, arr[u]-1, 1);
        ans += tmp*arr[u];
        
        if(mx != -1) big[b] = 0;
        if(!k) add(u, p, 0);
    }
    
    public static void solve() throws FileNotFoundException{

        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        int n=in.nextInt();
        m = in.nextInt();
        sub = new int[n];
        depth = new int[n];
        big = new int[n];
        g = new ArrayList[n];
        segmentTrees = new SegmentTree[m];
        
        for(int i = 0;i < n; i++) g[i] = new ArrayList<>();
        for(int i = 0;i < m; i++)
            segmentTrees[i] = new SegmentTree(max);
        
        for(int i = 0; i < n - 1; i++){
            int u = in.nextInt()-1;
            int v = in.nextInt()-1;
            g[u].add(v);
            g[v].add(u);
        }
        arr = in.nextIntArray(n);
        dfs(0, -1);
        trick(0, -1, false);
        
        out.println(ans);
        out.close();
    }
    
    public static void main(String[] args) {
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
    
    static class Pair{
		int x;
		int y;
                int i;
		Pair(int x,int y,int i){
                        this.i=i;
			this.x=x;
			this.y=y;
		}
	}
    
     public static void debug(Object... o) {
            System.out.println(Arrays.deepToString(o));
    }
    public static class CompareTable implements Comparator{
        public int compare(Object o1,Object o2){
            Pair p1 = (Pair) o1;
            Pair p2 = (Pair) o2;
            
            if(p1.x>p2.x)
                return 1;
            else if(p1.x<p2.x)
                return -1;
            else{
                if(p1.y<p2.y)
                    return -1;
                else if(p1.y>p2.y)
                    return 1;
                else
                    return 0;
            }
        }
    }
    
    public static boolean isPal(String s){
        for(int i=0, j=s.length()-1;i<=j;i++,j--){
                if(s.charAt(i)!=s.charAt(j)) return false;
        }
        return true;
    }
    public static String rev(String s){
		StringBuilder sb=new StringBuilder(s);
		sb.reverse();
		return sb.toString();
    }
    
    public static long gcd(long x,long y){
	if(x%y==0)
		return y;
	else
		return gcd(y,x%y);
    }
    
    public static int gcd(int x,int y){
	if(x%y==0)
		return y;
	else 
		return gcd(y,x%y);
    }
    
    public static long gcdExtended(long a,long b,long[] x){
        
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
    
    public static int abs(int a,int b){
	return (int)Math.abs(a-b);
    }

    public static long abs(long a,long b){
	return (long)Math.abs(a-b);
    }
    
    public static int max(int a,int b){
	if(a>b)
		return a;
	else
		return b;
    }

    public static int min(int a,int b){
	if(a>b)
		return b;
	else 
		return a;
    }
    
    public static long max(long a,long b){
	if(a>b)
		return a;
	else
		return b;
    }

    public static long min(long a,long b){
	if(a>b)
		return b;
	else 
		return a;
    }

    public static long pow(long n,long p,long m){
	 long  result = 1;
	  if(p==0)
	    return 1;
	if (p==1)
	    return n;
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
    
    public static long pow(long n,long p){
	long  result = 1;
	  if(p==0)
	    return 1;
	if (p==1)
	    return n;
	while(p!=0)
	{
	    if(p%2==1)
	        result *= n;	    
	    p >>=1;
	    n*=n;	    
	}
	return result;
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
				a[i] = nextInt();
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

