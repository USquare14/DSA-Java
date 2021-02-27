import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Travel in HackerLand

public class ParallelBinarySearch
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    
    static int[] rt;
    static int[] size;
    static HashSet<Integer>[] set;

    static void initialize(int n){
        for(int i = 1; i < rt.length; i++){
            rt[i] = i;
            size[i] = 1;
            set[i].clear();
        }
    }
    
    static int root(int x){
        while(rt[x] != x){
            rt[x] = rt[rt[x]];
            x = rt[x];
        }
        return x;
    }
    
    static boolean union(int x,int y){
        int root_x = root(x);
        int root_y = root(y);
        if(root_x == root_y) return true;
        
        if(size[root_x]<size[root_y]){
            rt[root_x] = rt[root_y];
            size[root_y] += size[root_x];
            set[root_y].addAll(set[root_x]);
        }
        else{
            rt[root_y] = rt[root_x];
            size[root_x] += size[root_y];   
            set[root_x].addAll(set[root_y]);         
        }
        return false;
    }
    
    public static void main(String[] args)
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);       
        
        int n = in.nextInt();
        int m = in.nextInt();
        int q = in.nextInt();
        int[] arr = new int[n + 1];

        for(int i = 1; i <= n; i++)
            arr[i] = in.nextInt();
        
        ArrayList<Pair> list = new ArrayList<>();
        
        for(int i = 0; i < m; i++){
            int u = in.nextInt();
            int v = in.nextInt();
            int w = in.nextInt();
            list.add(new Pair(u, v, w));
        }
        
        int[] lo = new int[q];
        int[] hi = new int[q];
        
        for(int i = 0; i < q; i++){
            lo[i] = 0;
            hi[i] = m;
        }
        
        ArrayList<Pair> query = new ArrayList<>();
        for(int i = 0; i < q; i++){
            int x = in.nextInt();
            int y = in.nextInt();
            int k = in.nextInt();
            Pair p = new Pair(x,y,k,i);
            query.add(p);
        }
        
        rt = new int[n + 1];
        size = new int[n + 1];
        set = new HashSet[n + 1];
        for(int i = 1; i <= n; i++)
            set[i] = new HashSet<>();
        
        Collections.sort(list);
        
        boolean flag = true;
        ArrayDeque<Pair>[] tocheck = new ArrayDeque[m];
        for(int i = 0; i < m; i++)
            tocheck[i] = new ArrayDeque<>();
        
        while(flag){
            flag = false;
            initialize(n);
            for(int i = 1; i <= n; i++)
                set[i].add(arr[i]);

            for(int i = 0; i < q; i++){
                if(lo[i] == hi[i]) continue;
                Pair p = query.get(i);
                tocheck[(lo[p.i] + hi[p.i]) >> 1].add(p);
            }
            
            for(int i = 0; i < m; i++){
                Pair p = list.get(i);
                union(p.x, p.y);
                while(!tocheck[i].isEmpty()){
                    flag = true;
                    Pair pq = tocheck[i].removeFirst();
                    int x = root(pq.x);
                    int y = root(pq.y);
                    if(x == y && set[x].size() >= pq.k){
                        hi[pq.i] = i;
                    }
                    else{
                        lo[pq.i] = i + 1;
                    }
                }
            }
            
        }
        
        for(int i = 0; i < q; i++){
            if(query.get(i).x == query.get(i).y && query.get(i).k == 1) out.println(0);
            else out.println((lo[i] == m) ? -1 : list.get(lo[i]).k);
        }
        
        out.close();
    }

    static class Pair implements Comparable<Pair>
    {

        int x,y,k,i;

        Pair (int x,int y, int k)
        {
                this.x = x;
                this.y = y;
                this.k = k;
        }

        Pair (int x,int y, int k, int i)
        {
                this.x = x;
                this.y = y;
                this.k = k;
                this.i = i;
        }
        
        public int compareTo(Pair o)
        {
            return Integer.compare(this.k,o.k);
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
            return x + " "+ y + " "+k;
        }

        /*public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }*/

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
        long x=(a*b);
        while(x>=mod) x-=mod;
        return x;
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
