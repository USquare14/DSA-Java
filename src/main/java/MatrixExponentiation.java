
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */


public class MatrixExponentiation {
    
    static int mod = 1000000007;
    static int m=3;
    static long[][] temp = new long[m][m];
    static long[][] res = new long[m][m];
    static long[][] mat = new long[m][m];
    static long A;
    static long B;
    static long X;
    //f(x)=A*f(x-1)+b
    
    static long solve(long b){

        for(int i = 0; i < m; i++){
            for(int j = 0; j < m; j++){
                if(i == j)
                    res[i][j] = 1;
                else
                    res[i][j] = 0;
            }
        }

        mat[0][0] = A;
        mat[0][1] = 0;
        mat[0][2] = 1;
        mat[1][0] = 1;
        mat[1][1] = 0;
        mat[1][2] = 0;
        mat[2][0] = 0;
        mat[2][1] = 0;
        mat[2][2] = 1;

        while(b > 0){
            if(b % 2 == 1){
                for(int i = 0; i < m; i++)
                    for(int j = 0; j < m; j++)
                        temp[i][j] = 0;
                for(int i = 0; i < m; i++)
                    for(int j = 0; j < m; j++)
                        for(int k = 0; k < m; k++)
                            temp[i][j] = (temp[i][j]+(res[i][k]*mat[k][j])%mod+mod)%mod;
                for(int i = 0; i < m; i++)
                    for(int j = 0; j < m; j++)
                        res[i][j] = temp[i][j];
            }
            for(int i = 0; i < m; i++)
                for(int j = 0; j < m; j++)
                    temp[i][j] = 0;
            for(int i = 0; i < m; i++)
                for(int j = 0; j < m; j++)
                    for(int k = 0; k < m; k++)
                        temp[i][j] = (temp[i][j]+(mat[i][k]*mat[k][j])%mod+mod)%mod;
        
            for(int i = 0; i <m ; i++)
                for(int j = 0 ; j < m; j++)
                    mat[i][j] = temp[i][j];
            b/=2;
        }
        long ans = (res[0][0]*X)%mod+res[0][2]*B;
        return ans%mod;
    }    
       
/*    public static void multiply(long [][] a, long[][] b){
        long[][] mul = new long[3][3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    mul[i][j] += (a[i][k]%mod*b[k][j]%mod)%mod;
                    mul[i][j]%=mod;
                }
            }
        }
 
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
                a[i][j] = mul[i][j]%mod;
    }

    public static long power(long[][] F, long n){
        long[][] M = {{2,-1,1}, {1,0,0}, {0,0,1}};
 
        if (n==1){
            long ans = F[0][0]*6%mod+F[0][1]*1%mod+F[0][2]*4%mod;
            return (ans%mod);
        }
        power(F, n/2);
 
        multiply(F, F);
 
        if (n%2 != 0)
            multiply(F, M);
     
        long ans = F[0][0]*6%mod+F[0][1]*1%mod+F[0][2]*4%mod;
        return (ans%mod);
    }*/
    
    
    public static void main(String[] args) {
 
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);
                    
        int t = in.nextInt();
        while(t>0){
            long n = in.nextLong();
            w.println(solve(n));
            t--;
        }
        
        w.close();
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

