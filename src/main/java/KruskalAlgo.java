
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class KruskalAlgo {
    
    static int mod = 1000000007;
    static int[] rt = new int[10001];
    static int[] size = new int[10001];
    static ArrayList<Pair> p = new ArrayList<>();
            
    static void initialize(){
        for(int i=0;i<rt.length;i++){
            rt[i]=i;
            size[i]=1;
        }
    }
    
    static int root(int x){
        while(rt[x]!=x){
            rt[x]=rt[rt[x]];
            x=rt[x];
        }
        return x;
    }
    
    static void union(int x,int y){
        int root_x = root(x);
        int root_y = root(y);
        if(size[root_x]<size[root_y]){
            rt[root_x]=rt[root_y];
            size[root_y]+=size[root_x];
        }
        else{
            rt[root_y]=rt[root_x];
            size[root_x]+=size[root_y];            
        }
    }
    
    static long kruskal(){
        long cost = 0;
        long mincost = 0;
        int x,y;

        initialize();
        for(int i=0;i<p.size();i++){
            x = p.get(i).x;
            y = p.get(i).y;
            cost = p.get(i).c;
            if(root(x)!=root(y)){
                mincost+=cost;
                union(x, y);
            }
        }
        
        return mincost;
    }
    

    public static void main(String[] args) {
 
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
        
        int n = in.nextInt();
        int m = in.nextInt();

        for(int i=0;i<m;i++){
            int x = in.nextInt();
            int y = in.nextInt();
            int c = in.nextInt();
            p.add(new Pair(x,y,c));
        }
      Collections.sort(p,new CompareCost());
        w.println(kruskal());
        w.close();
    }
    
    static class Pair{
        int c;
        int x;
        int y;
        
        public Pair(int x,int y,int c){
            this.x=x;
            this.y=y;
            this.c=c;
        }
        
    }
    
    static class CompareCost implements Comparator{

        @Override
        public int compare(Object t, Object t1) {
            Pair p1 = (Pair)t;
            Pair p2 = (Pair)t1;
            
            if(p1.c<p2.c){
                return -1;
            }
            else if(p1.c>p2.c){
                return 1;
            }
            else return 0;
        }
        
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
			while (isSpaceChar(c))
				c = snext();
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
			while (isSpaceChar(c))
				c = snext();
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
			for (int i = 0; i < n; i++)
				a[i] = nextInt();
			return a;
		}

		public String readString() {
			int c = snext();
			while (isSpaceChar(c))
				c = snext();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = snext();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		public boolean isSpaceChar(int c) {
			if (filter != null)
				return filter.isSpaceChar(c);
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		public interface SpaceCharFilter {
			public boolean isSpaceChar(int ch);
		}
	}
}    

