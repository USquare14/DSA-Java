import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */


public class BipartiteMatchingMVC{

    public static int mod = 1000000007;
    public static InputReader in;
    public static PrintWriter out;
    public static int[] p = new int[200005];
    public static ArrayList<Integer>[] g;
    public static boolean[] vis;
    public static int[] match;

    public static void isPrime(){
        p[0]=-1;
        p[1]=-1;
        for(int i=2;i<p.length;i++){
            if(p[i]!=-1){
                p[i]=1;
                for(int j=2*i;j<p.length;j+=i){
                    p[j]=-1;
                }
            }
        }
    }

    public static boolean Match(int u){
        vis[u]=true;

        for(int v:g[u]){
            if(match[v]==-1){
                match[v]=u;
                match[u]=v;
                return true;
            }
        }

        for(int v:g[u]){
            if(!vis[match[v]] && Match(match[v])){
                match[v]=u;
                match[u]=v;
                return true;
            }
        }

        return false;
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


    public static void solve(){

        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        isPrime();
        int n=in.nextInt();
        ArrayList<Integer> even=new ArrayList<>();
        ArrayList<Integer> odd=new ArrayList<>();

        for(int i=0;i<n;i++){
            int a=in.nextInt();
            if((a&1)==0) even.add(a);
            else odd.add(a);
        }
        g=new ArrayList[n];
        for(int i=0;i<n;i++)
            g[i]=new ArrayList<>();
        vis=new boolean[n];
        match=new int[n];

        for(int i=0;i<even.size();i++){
            for(int j=0;j<odd.size();j++){
                if(p[even.get(i)+odd.get(j)]==-1) continue;
                g[i].add(even.size()+j);
            }
        }
        Arrays.fill(match,-1);
        boolean flag=true;
        while(flag){
            flag=false;
            for(int i=0;i<even.size();i++) vis[i]=false;

            for(int i=0;i<even.size();i++){
                if(vis[i] || match[i]!=-1) continue;
                if(Match(i)) flag=true;
            }
        }
        Queue<Integer> list=new ArrayDeque<>();
        boolean[] sel=new boolean[n];

        for(int i=0;i<even.size();i++){
            if(match[i]==-1) list.add(i);
            else sel[i]=true;
        }

        while(!list.isEmpty()){
            int u=list.poll();
            for(int v:g[u]){
                if(!sel[v]){
                    int x=match[v];
                    if(sel[x]){
                        list.add(x);
                        sel[x]=false;
                    }
                    sel[v]=true;
                }
            }
        }
        int c=0;
        for(int i=0;i<n;i++)
            if(sel[i]) c++;

        out.println(c);//number in mvc

        for(int i=0;i<n;i++){
            if(!sel[i]) continue;
            out.print(((i<even.size())?even.get(i):odd.get(i-even.size()))+" ");// elemnts in mvc
        }

        out.close();
    }

    public static void debug(Object... o) {
        System.out.println(Arrays.deepToString(o));
    }

    static class Pair implements Comparable<Pair>{

        char c;
        int x;
        int y;

        Pair (int x,int  y){
            this.x=x;
            this.y=y;
            this.c='a';
        }

        public int compareTo(Pair o) {
            return Long.compare(this.x,o.x);
        }

        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair p = (Pair)o;
                return (p.x == x && p.y == y && p.c==c);
            }
            return false;
        }


        @Override
        public String toString() {
            return  "("+x + " " + y +")";
        }

    }

    public static long add(long a,long b){
        long x=(a+b);
        while(x>=mod) x-=mod;
        return x;
    }

    public static long sub(long a,long b){
        long x=(a-b);
        while(x<0) x+=mod;
        return x;
    }

    public static long mul(long a,long b){
        long x=(a*b);
        while(x>=mod) x-=mod;
        return x;
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



