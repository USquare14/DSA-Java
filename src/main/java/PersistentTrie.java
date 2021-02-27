import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

// https://www.hackerrank.com/contests/irelay-4/challenges/move-towards-victory/problem

public class PersistentTrie
{

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    
    static class Node{
        Node left;
        Node right;
        int cnt;
        
        Node(int val) {
            left = null;
            right = null;
            this.cnt = val;
        }
        
        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            if (left != null) {
                cnt += left.cnt;
            }
            if (right != null) {
                cnt += right.cnt;
            }
        }

    }
    
    static Node insert(Node root, int y, int idx) {
        if(idx < 0){
            return new Node(root.cnt + 1);
        }
        int x = (1 << idx) & y;
        if(x == 0) {
            return new Node(insert(root.left == null ? new Node(0): root.left, y, idx - 1), root.right);
        }
        else{
            return new Node(root.left, insert(root.right == null ? new Node(0): root.right, y, idx - 1));
        }
    }
    
    static int get(Node root, int y, int z) {
        int res = 0;
        for(int i = 20; i >= 0; i--) {
            int x = (1 << i) & y;
            
            if(x == 0) {
                if(root.left == null) {
                    root = root.right;
                    res |= 1 << i;
                }
                else {
                    if(root.left.cnt >= z) {
                        root = root.left;
                    }
                    else {
                        z -= root.left.cnt;
                        res |= 1 << i;
                        root = root.right;
                    }
                }
            }
            else {
                if(root.right == null) {
                    root = root.left;
                    res |= 1 << i;
                }
                else {
                    if(root.right.cnt >= z) {
                        root = root.right;
                    }
                    else {
                        z -= root.right.cnt;
                        res |= 1 << i;
                        root = root.left;
                    }
                }
            }
        }
        
        return res;
    }
    
    static void solve()
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out); 
        
        int n = in.nextInt();
        int q = in.nextInt();
        int[] arr = new int[n + 1];

        Node[] tree = new Node[n + 1];
        tree[0] = new Node(0);
        
        for(int i = 1; i <= n; i++) {
            arr[i] = in.nextInt();
            tree[i] = insert(tree[i - 1], arr[i], 20);
        }
        
        while(q-- > 0) {
            int t = in.nextInt();
            int y = in.nextInt();
            int z = in.nextInt();

            if(t == 1) {
                arr[y] = z;
                tree[y] = insert(tree[y - 1], arr[y], 20);
            }
            else {
                int x = get(tree[y - 1], arr[y], z) ^ arr[y];
                out.println(x);
            }
        }
        
        out.close(); 
    }
    
    static long add(long a, long b) {
        return (a + b) % mod;
    }
    
    static long mul(long a, long b) {
        return (a * b) % mod;
    }
    
    static long sub(long a, long b) {
        return (a - b + mod) % mod;
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
        },"1",1<<26).start();
    }
    
    static class Pair implements Comparable<Pair>
    {

        int x,y;
        int i;
        
        Pair (int x, int y)
        {
                this.x = x;
                this.y = y;
        }

        public int compareTo(Pair o)
        {
            return -Integer.compare(this.x, o.x);
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

        /*public int hashCode()
        {
            return new Long(x).hashCode() * 31 + new Long(y).hashCode();
        }*/

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
