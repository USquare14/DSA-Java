
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class SpojDQUERY {

    static long mod = 1000000007l;
    static int[] arr;
    static int[] cnt;
    static int[] ans;
    static int block;
    static int sum;
    
    static void add(int ind){
        cnt[arr[ind]]++;
        if(cnt[arr[ind]]==1) sum++;
    }
    
    static void remove(int ind){
        cnt[arr[ind]]--;
        if(cnt[arr[ind]]==0) sum--;
    }
    
    public static void main(String[] args) {
 
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        int n=in.nextInt();
        arr=in.nextIntArray(n);
        cnt=new int[1000005];
        
        block=(int) Math.sqrt(n);
        int q=in.nextInt();
        ans=new int[q];
        ArrayList<Pair> list=new ArrayList<>();
        
        for(int i=0;i<q;i++) list.add(new Pair(in.nextInt()-1, in.nextInt()-1,i));
        Collections.sort(list);
        
        int cl=0;
        int cr=-1;
        sum=0;
        for(int i=0;i<q;i++){
            Pair p = list.get(i);
            int l = p.x;
            int r = p.y;
            
            while(cl > l) add(--cl);
            while(cr < r) add(++cr);
            while(cl < l) remove(cl++);
            while(cr > r) remove(cr--);
            
            ans[p.i] = sum;
        }
        
        for(int i=0;i<q;i++) out.println(ans[i]);
        out.close();
    }
    
    static class Pair implements Comparable<Pair>{

        int x,y,i;
        
	Pair (int x,int y,int i){
		this.x=x;
		this.y=y;
		this.i=i;
	}

	Pair (int x,int y){
		this.x=x;
		this.y=y;
	}
	public int compareTo(Pair o) {
		if(this.x/block!=o.x/block)
                    return Long.compare(this.x/block,o.x/block);
		else
                    return Integer.compare(this.y,o.y);
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

