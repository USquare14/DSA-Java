
import java.util.*;

/**
 * @author umangupadhyay
 */

public class Prime {

    static int mod = 1000000007;
    static long[][] nCr;
    
    static void inverse(int n){
        long[] inv = new long[n + 1];
        inv[1] = 1;

        for(int i = 2; i <= n; i++) {
            inv[i] = ((mod - mod / i) * inv[mod % i]) % mod;
        }
        
    }

    static void choose(int maxN, int maxR) {
        nCr = new long[maxN + 1][maxR + 1];
        for (int i = 0; i <= maxN; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i || i == 0) {
                    nCr[i][j] = 1;
                } else {
                    nCr[i][j] = (nCr[i - 1][j - 1] + nCr[i - 1][j]) % mod;
                }
            }
        }
    }

    static long nCr(int n, int r) {
        if (n == r) {
            return 1;
        }
        if (n < 0 || r < 0) {
            return 0;
        }
        return nCr[n][r];
    }

    static int[] phi = new int[10000001];
    static int[] p = new int[1000001];
    static int[] sp = new int[1000001];
    static int[] mu = new int[1000001];

    static void isPrime() {
        p[0] = -1;
        p[1] = -1;
        for (int i = 1; i < phi.length; i++) {
            phi[i] = i;
        }
        for (int i = 2; i < p.length; i++) {
            if (p[i] != -1) {
                sp[i] = i;
                p[i] = 1;
                phi[i] = i - 1;
                mu[i] = -1;
                for (int j = 2 * i; j < p.length; j += i) {
                    p[j] = -1;
                    if (sp[j] == 0) {
                        sp[j] = i;
                    }
                    mu[j] *= -1;
                    if(j % (i * 1l * i) == 0) mu[i] = 0;
                    phi[j] -= phi[j] / i;
                }
            }
        }
    }

    static void lognPrimeFactorisation(int n) {
        ArrayList<Pair> tmp = new ArrayList<>();

        for (int i = sp[n]; i <= Math.sqrt(n) && i != 0; i = sp[n]) {
            int c = 0;
            while (n % i == 0) {
                n /= i;
                c++;
            }
            tmp.add(new Pair(i, c));
        }

        if (n > 1) {
            tmp.add(new Pair(n, 1));
        }
    }

    static int phi(int n) {
        int ans = 1;
        for (int i = sp[n]; i <= Math.sqrt(n) && i != 0; i = sp[n]) {
            if (n % i == 0) {
                int c = 0;
                while (n % i == 0) {
                    n /= i;
                    c++;
                }
//                ans*=(i-1)*pow(i,c-1);
            }
        }
        if (n >= 2) {
            ans *= (n - 1);
        }
        return ans;
    }

    static ArrayList<Pair> list;

    static void primeFactorisation(int n) {
        //sp[n] helpful in primefactorisation
        int i = 2;
        if (n % 2 == 0) {
            int c = 0;
            while (n % 2 == 0) {
                n /= 2;
                c++;
            }
            list.add(new Pair(2, c));
        }
        for (i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                int c = 0;
                while (n % i == 0) {
                    n /= i;
                    c++;
                }
                list.add(new Pair(i, c));
            }
        }
        if (n > 2) {
            list.add(new Pair(n, 1));
        }
    }

    //segmented seive
    static HashSet<Long>[] set;

    static void seive(long a, long b) {

        for (long i = 2; i <= 1000000; i++) {
            if (p[(int) i] == -1) {
                continue;
            }
            long x = (long) Math.ceil((a * 1.0) / (i * 1.0));
            x = i * x;
            for (long k = x; k <= b; k += i) {
                int y = (int) (k - a);
                set[y].add(i);
            }
        }

    }

    static class Pair {

        int x;
        int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static long phi(long n) {

        long result = n;
        for (long p = 2; p * p <= n; ++p) {
            if (n % p == 0) {
                while (n % p == 0) {
                    n /= p;
                }
                result /= p;
                result *= p - 1;
            }
        }

        if (n > 1) {
            result /= n;
            result *= n - 1;
        }

        return result;
    }

    static boolean miillerTest(long d, long n) { 

        int a = 2 + (int)(Math.random() % (n - 4)); 
      
        long x = pow(a, d, n); 
      
        if (x == 1 || x == n - 1) 
            return true; 
      
        while (d != n - 1) { 
            x = (x * x) % n; 
            d *= 2; 
          
            if (x == 1) 
                return false; 
            if (x == n - 1) 
                return true; 
        } 
      
        return false; 
    }
    
    static boolean isPrime(long n, int k) { 
          
        if (n <= 1 || n == 4) 
            return false; 
        if (n <= 3) 
            return true; 
        
        long d = n - 1; 
          
        while (d % 2 == 0) 
            d /= 2; 
      
        for (int i = 0; i < k; i++) 
            if (miillerTest(d, n) == false) 
                return false; 
      
        return true; 
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
    
    static long gcdExtended(long a, long b, long[] x) {

        if (a == 0) {
            x[0] = 0;
            x[1] = 1;
            return b;
        }
        long[] y = new long[2];
        long gcd = gcdExtended(b % a, a, y);

        x[0] = y[1] - (b / a) * y[0];
        x[1] = y[0];

        return gcd;
    }

    static void shift_solution (long[] x, long a, long b, long cnt) {
	x[0] += cnt * b;
	x[1] -= cnt * a;
    }
 
    static long find_all_solutions (long a, long b, long c, long minx, long maxx, long miny, long maxy) {
        long[] x = new long[2];
	long g = gcdExtended(a, b, x);
        
        if(c % g != 0) return 0;
        
	a /= g;  b /= g;
 
	int sign_a = a>0 ? +1 : -1;
	int sign_b = b>0 ? +1 : -1;
 
	shift_solution (x, a, b, (minx - x[0]) / b);
	if (x[0] < minx)
		shift_solution (x, a, b, sign_b);
	if (x[0] > maxx)
		return 0;
	long lx1 = x[0];
 
	shift_solution (x, a, b, (maxx - x[0]) / b);
	if (x[0] > maxx)
		shift_solution (x, a, b, -sign_b);
	long rx1 = x[0];
 
	shift_solution (x, a, b, - (miny - x[1]) / a);
	if (x[1] < miny)
		shift_solution (x, a, b, -sign_a);
	if (x[1] > maxy)
		return 0;
        long lx2 = x[0];
 
	shift_solution (x, a, b, - (maxy - x[1]) / a);
	if (x[1] > maxy)
		shift_solution (x, a, b, sign_a);
	long rx2 = x[0];
 
	if (lx2 > rx2){
            lx2 = lx2 ^ rx2;
            rx2 = lx2 ^ rx2;
            lx2 = lx2 ^ rx2;
        }
	long lx = Math.max (lx1, lx2);
	long rx = Math.min (rx1, rx2);
 
	return (rx - lx) / Math.abs(b) + 1;
    }
    
}
