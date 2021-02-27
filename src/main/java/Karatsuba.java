/**
 * @author umangupadhyay
 */

public class Karatsuba {
    
    static java.math.BigInteger karatsuba(java.math.BigInteger x, java.math.BigInteger y) {

        int N = Math.max(x.bitLength(), y.bitLength());
        if (N <= 2000) return x.multiply(y);  

        N = (N / 2) + (N % 2);

        java.math.BigInteger b = x.shiftRight(N);
        java.math.BigInteger a = x.subtract(b.shiftLeft(N));
        java.math.BigInteger d = y.shiftRight(N);
        java.math.BigInteger c = y.subtract(d.shiftLeft(N));

        java.math.BigInteger ac    = karatsuba(a, c);
        java.math.BigInteger bd    = karatsuba(b, d);
        java.math.BigInteger abcd  = karatsuba(a.add(b), c.add(d));

        return ac.add(abcd.subtract(ac).subtract(bd).shiftLeft(N)).add(bd.shiftLeft(2*N));
    }
    
    public long multiply(long x, long y)
    {
        int size1 = getSize(x);
        int size2 = getSize(y);
        int N = Math.max(size1, size2);
        
        if (N < 10)
            return x * y;
        
        N = (N / 2) + (N % 2);
        
        long m = (long)Math.pow(10, N);
       
        long b = x / m;
        long a = x - (b * m);
        long d = y / m;
        long c = y - (d * N);
        long z0 = multiply(a, c);
        long z1 = multiply(a + b, c + d);
        long z2 = multiply(b, d);          
 
        return z0 + ((z1 - z0 - z2) * m) + (z2 * (long)(Math.pow(10, 2 * N)));        
    }
    
    public int getSize(long num)
    {
        int ctr = 0;
        while (num != 0)
        {
            ctr++;
            num /= 10;
        }
        return ctr;
    }
    
}
