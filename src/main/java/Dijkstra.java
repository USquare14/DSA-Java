
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class Dijkstra {
    
    static int mod = 1000000007;
    static ArrayList<Pair>[] g;
    
    static void dijkstra(int s, int [] dis){
        dis[s] = 0;
	PriorityQueue<Pair> pq = new PriorityQueue<>();
	pq.add(new Pair(s,0));
	boolean[] visited = new boolean[dis.length];
	while(!pq.isEmpty()){
            Pair p = pq.poll();
            int ii = p.x;
            if(visited[ii]) continue;
            visited[ii] = true;
            for(int i = 0;i < g[ii].size(); i++){
                int v = g[ii].get(i).x;
                int w = g[ii].get(i).y;
                if(dis[v] > dis[ii] + w){
                    dis[v] = dis[ii] + w;
                    pq.add(new Pair(v,dis[v]));
                }
            }
	}
    }

    /*public static ArrayList<ArrayList<Pair>> g;
    public static int[][] dis;
    
    public static void bfs(int s){
        Queue<Integer> q = new ArrayDeque<>();
        boolean[] vis=new boolean[dis[s].length];
        q.add(s);
        dis[s][s]=0;
        while(!q.isEmpty()){
            int x = q.remove();
            vis[x]=true;
            for(Pair p:g.get(x)){
                if(vis[p.x]) continue;
                dis[s][p.x]=Math.min(dis[s][p.x], dis[s][x]+p.y);
                q.add(p.x);
            }
        }
        
    }*/
    public static void main(String[] args) {
 
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
        
        int t = in.nextInt();
        
        while(t>0){
            int n = in.nextInt();
            int m = in.nextInt();
            g = new ArrayList[n+1];
            for(int i=0;i<=n;i++) g[i]=new ArrayList<>();
            
            for(int i=0;i<m;i++){
                int x = in.nextInt();
                int y = in.nextInt();
                int s = in.nextInt(); 
                g[x].add(new Pair(y, s));
                g[y].add(new Pair(x, s));
            }
            int r = in.nextInt();
            int[] dis = new int[n+1];
            Arrays.fill(dis, Integer.MAX_VALUE);
            dijkstra(r, dis);
            for(int i=1;i<n+1;i++){
                if(i!=r) w.print(dis[i]+" ");
            }
            w.println();
            t--;
        }
        
        w.close();
    }
        
    static class Pair implements Comparable<Pair>
    {

        int x,y;
        
        Pair (int x,int y)
        {
		this.x = x;
                this.y = y;
	}
        
	public int compareTo(Pair o)
        {
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
            return x + " "+ y;
        }
        
        public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
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

