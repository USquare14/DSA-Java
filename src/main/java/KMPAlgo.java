
import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

//Removing SubString and KMP

public class KMPAlgo {

    static InputReader in;
    static PrintWriter out;
    
    static ArrayList<Integer> list;
    
    static void KMPSearch(String pat, String txt) {

        int M = pat.length();
        int N = txt.length();

        int lps[] = new int[M];
        computeLPSArray(pat, M, lps);
        int j = 0;
        int i = 0;

        while (i < N) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                list.add((i - j));
                System.out.println("Found pattern at index " + (i - j));
                j = lps[j - 1];
            } else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i = i + 1;
                }
            }
        }
    }

    static void computeLPSArray(String pat, int M, int lps[]) {

        int len = 0;
        int i = 1;
        lps[0] = 0;
        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
    }
    
    public static void main(String[] args)
    {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);            
        
        int t = in.nextInt();
        
        while(t-- > 0){
            String pat = in.readString();
            String txt = in.readString();
            int n = txt.length();
            int[] next = new int[n + 2];
            int[] prev = new int[n + 2];
            for(int i = 0; i <= n + 1; i++)
                next[i] = i + 1;
            for(int i = 0; i <= n + 1; i++)
                prev[i] = i - 1;
            
            boolean ok = true;
            int cnt = 0;
            int m = pat.length();
            int [] lps = new int[m];
            computeLPSArray(pat, m, lps);
            int[] idx = new int[n + 1];
            
            int last;
            while(ok){
                ok = false;
                int j = 0;
                int i = next[0];
                last = 0;
                while(i <= n){
                    if(pat.charAt(j) == txt.charAt(i - 1)){
                        j++;
                        idx[i] = j;
                        i = next[i];
                    }
                    if(j == m){
                        cnt++;
                        int k = 0;
                        int p = i;
                        while(k <= m){
                            p = prev[p];
                            k++;
                        }
                        prev[i] = p;
                        next[p] = i;
                        j = idx[p];
                        ok = true;
                    }
                    else if(i <= n && pat.charAt(j) != txt.charAt(i - 1)){
                        if(j != 0)
                            j = lps[j-1];
                        else{
                            i = next[i];
                        }
                    }
                }
            }
            out.println(cnt);
        }
        
        out.close();
        
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
