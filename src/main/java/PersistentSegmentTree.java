
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Deepu And Market

public class PersistentSegmentTree {

    static int mod = (int) (1e9+7);
    static InputReader in;
    static PrintWriter out;
    static int maxnode=2000000;
    static Node[] tree;
    static int nnode=1;
    static int n;
    static Pair[] p;
    static int[] root;
    
    static class Node{
        long sum;
        int cnt;
        int lchild;
        int rchild;
        
        Node(){
            lchild=-1;
            rchild=-1;
            sum=0;
            cnt=0;
        }
        
    }
    
    static int insert(int v,int l,int r,int pos,long val){
        
        int cur=nnode++;
        if(v!=-1){
            tree[cur].sum = tree[v].sum;
            tree[cur].lchild = tree[v].lchild;
            tree[cur].rchild = tree[v].rchild;
            tree[cur].cnt = tree[v].cnt;
        }
        tree[cur].sum+=val;
        tree[cur].cnt++;
 
        if(l==r) return cur;
        
        int mid=(l+r)>>1;
        
        if(pos<=mid)
            tree[cur].lchild = insert(tree[cur].lchild, l, mid, pos, val);
        else
            tree[cur].rchild = insert(tree[cur].rchild, mid+1, r, pos, val);
        
        return cur;
    }
    
    static long fsum(int v, int l, int r, int ll, int rr) {
        if (v == -1)
            return 0;
        if (l == ll && rr == r)
            return tree[v].sum;
        int xx = (ll + rr) / 2;
        long res = 0;
        if (l <= xx)
            res += fsum(tree[v].lchild, l, Math.min(r, xx), ll, xx);
        if (xx + 1 <= r)
            res += fsum(tree[v].rchild, Math.max(xx + 1, l), r, xx + 1, rr);
        return res;
    }
    
    static int getCnt(int v,int l,int r,int ll,int rr){
        
        if(v==-1) return 0;
        if ( l == ll && rr == r ) return tree[v].cnt;
	int mid = (ll + rr) / 2, res = 0;
        
	if ( l <= mid )
		res += getCnt(tree[v].lchild, l, min( r, mid ), ll, mid );
	if ( mid + 1 <= r )
		res += getCnt(tree[v].rchild, max( mid + 1, l ), r, mid + 1, rr );
	return res;
        
    }
    
    static int query(int l,int r,long k){
        
        int ll=1,rr=n,res=0;
        
        while(rr-ll>=0){
            int mid=(ll+rr)>>1;
            long s=fsum(root[mid], l, r, 1, n);
            if(s<=k){
                res=mid;
                ll=mid+1;
            }
            else rr=mid-1;
        }
            
        return getCnt(root[res],l,r,1,n);
        
    }
    
    static void solve(){
        InputReader in = new InputReader(System.in);
        PrintWriter out = new PrintWriter(System.out);
        
        tree = new Node[maxnode];
        for(int i=0;i<tree.length;i++) tree[i]=new Node();
        
        n=in.nextInt();
        p = new Pair[n+1];
        root = new int[n+1];
        p[0]=new Pair(0, 0);
        
        for(int i=1;i<=n;i++){
            long a=in.nextLong();
            p[i]=new Pair(a, i);
        }
        Arrays.sort(p);
        root[0]=0;
        
        for(int i=1;i<=n;i++){
            root[i] = insert(root[i-1], 1, n, p[i].y, p[i].x);
        }

        int m=in.nextInt();
        
        while(m-->0){
            int l=in.nextInt();
            int r=in.nextInt();
            long k=in.nextLong();
            out.println(query(l, r,k));
        }
        
        
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
        },"1",1<<28).start();
    }
    
    static class Pair implements Comparable<Pair>{

        int y,k,i;
        long x;
	Pair (int x,int y,int k,int i){
		this.x=x;
                this.k=k;
		this.y=y;
		this.i=i;
	}

	Pair (long x,int y){
		this.x=x;
		this.y=y;
	}
        
	public int compareTo(Pair o) {
            if(this.x==o.x) return Integer.compare(this.y, o.y);
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
        
    } 
    public static class Merge {
        
        public static void sort(int inputArr[]) {
            int length = inputArr.length;
            doMergeSort(inputArr,0, length - 1);
        }
        
        private static void doMergeSort(int[] arr,int lowerIndex, int higherIndex) {        
            if (lowerIndex < higherIndex) {
                int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
                doMergeSort(arr,lowerIndex, middle);
                doMergeSort(arr,middle + 1, higherIndex);
                mergeParts(arr,lowerIndex, middle, higherIndex);
            }
        }
        
        private static void mergeParts(int[]array,int lowerIndex, int middle, int higherIndex) {
            int[] temp=new int[higherIndex-lowerIndex+1];
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
    
    static long[] shuffle(long[] a, Random gen){ 
        for(int i = 0, n = a.length;i < n;i++){ 
            int ind = gen.nextInt(n-i)+i; 
            long d = a[i]; 
            a[i] = a[ind]; 
            a[ind] = d; 
        } 
        return a; 
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

    static int abs(int a,int b){
    return (int)Math.abs(a-b);
    }

    static long abs(long a,long b){
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


    public static long pow(long n,long p,long m){
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
    
    public static long pow(long n,long p){
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

            InputReader(InputStream stream) {
                    this.stream = stream;
            }

            int snext() {
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

            int nextInt() {
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

            long nextLong() {
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

            int[] nextIntArray(int n) {
                    int a[] = new int[n];
                    for (int i = 0; i < n; i++) {
                            a[i] = nextInt();
                    }
                    return a;
            }

            long[] nextLongArray(int n) {
                    long a[] = new long[n];
                    for (int i = 0; i < n; i++) {
                            a[i] = nextLong();
                    }
                    return a;
            }
            
            String readString() {
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

            String nextLine() {
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

            boolean isSpaceChar(int c) {
                    if (filter != null)
                            return filter.isSpaceChar(c);
                    return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
            }

            private boolean isEndOfLine(int c) {
                    return c == '\n' || c == '\r' || c == -1;
            }

            interface SpaceCharFilter {
                    boolean isSpaceChar(int ch);
            }
    }
}    

