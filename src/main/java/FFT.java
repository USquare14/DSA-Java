/**
 * @author umangupadhyay
 */

public class FFT {
    
    static class FastFourierTransform {
        
        public void fft(double[] a, double[] b, boolean invert) {
            
            int count = a.length;
            for (int i = 1, j = 0; i < count; i++) {
                int bit = count >> 1;
                for (; j >= bit; bit >>= 1)
                    j -= bit;
                j += bit;
                if (i < j) {
                    double temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    temp = b[i];
                    b[i] = b[j];
                    b[j] = temp;
                }
            }
            for (int len = 2; len <= count; len <<= 1) {
                int halfLen = len >> 1;
                double angle = 2 * Math.PI / len;
                if (invert)
                    angle = -angle;
                double wLenA = Math.cos(angle);
                double wLenB = Math.sin(angle);
                for (int i = 0; i < count; i += len) {
                    double wA = 1;
                    double wB = 0;
                    for (int j = 0; j < halfLen; j++) {
                        double uA = a[i + j];
                        double uB = b[i + j];
                        double vA = a[i + j + halfLen] * wA - b[i + j + halfLen] * wB;
                        double vB = a[i + j + halfLen] * wB + b[i + j + halfLen] * wA;
                        a[i + j] = uA + vA;
                        b[i + j] = uB + vB;
                        a[i + j + halfLen] = uA - vA;
                        b[i + j + halfLen] = uB - vB;
                        double nextWA = wA * wLenA - wB * wLenB;
                        wB = wA * wLenB + wB * wLenA;
                        wA = nextWA;
                    }
                }
            }
            if (invert) {
                for (int i = 0; i < count; i++) {
                    a[i] /= count;
                    b[i] /= count;
                }
            }
        }

        public boolean[] multiply(boolean[] a, boolean[] b) {
            int resultSize = Integer.highestOneBit(Math.max(a.length, b.length) - 1) << 2;
            resultSize = Math.max(resultSize, 1);
            double[] aReal = new double[resultSize];
            double[] aImaginary = new double[resultSize];
            double[] bReal = new double[resultSize];
            double[] bImaginary = new double[resultSize];
            for (int i = 0; i < a.length; i++)
                aReal[i] = a[i] ? 1 : 0;
            for (int i = 0; i < b.length; i++)
                bReal[i] = b[i] ? 1 : 0;
            fft(aReal, aImaginary, false);
            fft(bReal, bImaginary, false);
            for (int i = 0; i < resultSize; i++) {
                double real = aReal[i] * bReal[i] - aImaginary[i] * bImaginary[i];
                aImaginary[i] = aImaginary[i] * bReal[i] + bImaginary[i] * aReal[i];
                aReal[i] = real;
            }
            fft(aReal, aImaginary, true);
            boolean[] result = new boolean[resultSize];
            for (int i = 0; i < resultSize; i++)
                result[i] = ((int) (aReal[i] + 0.5) >= 1);
            int okId = resultSize - 1;
            while (okId >= 0 && result[okId] == false) okId--;
            okId++;
            boolean[] nResult = new boolean[okId];
            for (int i = 0; i < okId; i++)
                nResult[i] = result[i];
            return nResult;
        }

        public long[] multiply(long[] a, long[] b) {
			
            int resultSize = Integer.highestOneBit(Math.max(a.length, b.length) - 1) << 2;
            resultSize = Math.max(resultSize, 1);

            double[] aReal = new double[resultSize], aImaginary = new double[resultSize];
            double[] bReal = new double[resultSize], bImaginary = new double[resultSize];

            for (int i = 0; i < a.length; i++) aReal[i] = a[i];
            for (int i = 0; i < b.length; i++) bReal[i] = b[i];

            fft(aReal, aImaginary, false);

            if (a == b) {
                    System.arraycopy(aReal, 0, bReal, 0, aReal.length);
                    System.arraycopy(aImaginary, 0, bImaginary, 0, aImaginary.length);
            } 
            else 
                    fft(bReal, bImaginary, false);

            for (int i = 0; i < resultSize; i++) {
                    double real = aReal[i] * bReal[i] - aImaginary[i] * bImaginary[i];
                    aImaginary[i] = aImaginary[i] * bReal[i] + bImaginary[i] * aReal[i];
                    aReal[i] = real;
            }

            fft(aReal, aImaginary, true);

            long[] result = new long[resultSize];
            for (int i = 0; i < resultSize; i++) result[i] = Math.round(aReal[i]);
            return result;
        }
        public boolean[] bpow(boolean[] a, int k) {
            if (k == 1) return a;
            if (k % 2 == 1) return multiply(a, bpow(a, k - 1));
            a = bpow(a, k / 2);
            return multiply(a, a);
        }

    }
    
    static class FFTMultiplication {
        
        public static long[] convolute(int[] a, int[] b){
        
            int m = Integer.highestOneBit(Math.max(Math.max(a.length, b.length)-1, 1))<<2;
            double[][] fa = fft(a, m, false);
            double[][] fb = a == b ? fa : fft(b, m, false);
            for(int i = 0;i < m;i++){
                double nfa0 = fa[0][i]*fb[0][i]-fa[1][i]*fb[1][i];
                double nfa1 = fa[0][i]*fb[1][i]+fa[1][i]*fb[0][i];
                fa[0][i] = nfa0;
                fa[1][i] = nfa1;
            }
            fft(fa[0], fa[1], true);
            long[] ret = new long[m];
            for(int i = 0;i < m;i++){
                ret[i] = Math.round(fa[0][i]);
            }

            return ret;
        }

        public static double[][] fft(int[] srcRe, int n, boolean inverse){

            int m = srcRe.length;
            double[] dstRe = new double[n];
            double[] dstIm = new double[n];
            for(int i = 0;i < m;i++){
                dstRe[i] = srcRe[i];
            }

            int h = Integer.numberOfTrailingZeros(n);
            for(int i = 0;i < n;i++){
                int rev = Integer.reverse(i)>>>32-h;
                if(i < rev){
                        double d = dstRe[i]; dstRe[i] = dstRe[rev]; dstRe[rev] = d;
                }
            }

            for(int s = 2;s <= n;s <<= 1){
                int nt = s >>> 1;
                double theta = inverse ? -2*Math.PI/s : 2*Math.PI/s;
                double wRe = Math.cos(theta);
                double wIm = Math.sin(theta);
                for(int j = 0; j < n;j += s){
                    double wr = 1, wi = 0;
                    for(int t = j;t < j+nt;t++){
                        int jp = t + nt;
                        double re = dstRe[jp]*wr - dstIm[jp]*wi;
                        double im = dstRe[jp]*wi + dstIm[jp]*wr;
                        dstRe[jp] = dstRe[t] - re;
                        dstIm[jp] = dstIm[t] - im;
                        dstRe[t] += re;
                        dstIm[t] += im;
                        double nwre = wr * wRe - wi * wIm;
                        double nwim = wr * wIm + wi * wRe;
                        wr = nwre; wi = nwim;
                    }
                }
            }

            if(inverse){
                for(int i = 0;i < n;i++){
                    dstRe[i] /= n;
                    dstIm[i] /= n;
                }
            }

            return new double[][]{dstRe, dstIm};
        }

        public static void fft(double[] re, double[] im, boolean inverse){

            int n = re.length;
            int h = Integer.numberOfTrailingZeros(n);
            for(int i = 0;i < n;i++){
                int rev = Integer.reverse(i)>>>32-h;
                if(i < rev){
                    double d = re[i]; re[i] = re[rev]; re[rev] = d;
                    d = im[i]; im[i] = im[rev]; im[rev] = d;
                }
            }

            for(int s = 2;s <= n;s <<= 1){
                int nt = s >>> 1;
                double theta = inverse ? -2*Math.PI/s : 2*Math.PI/s;
                double wRe = Math.cos(theta);
                double wIm = Math.sin(theta);
                for(int j = 0; j < n;j += s){
                    double wr = 1, wi = 0;
                    for(int t = j;t < j+nt;t++){
                        int jp = t + nt;
                        double lre = re[jp]*wr - im[jp]*wi;
                        double lim = re[jp]*wi + im[jp]*wr;
                        re[jp] = re[t] - lre;
                        im[jp] = im[t] - lim;
                        re[t] += lre;
                        im[t] += lim;
                        double nwre = wr * wRe - wi * wIm;
                        double nwim = wr * wIm + wi * wRe;
                        wr = nwre; wi = nwim;
                    }
                }
            }

            if(inverse){
                for(int i = 0;i < n;i++){
                    re[i] /= n;
                    im[i] /= n;
                }
            }
        }
        
    }
    
}
