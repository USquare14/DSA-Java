
import java.util.Arrays;

/**
 * @author umangupadhyay
 */

public class SuffixArray {
    
    
    static int[] suffixArray(String S)
    {
        int n = S.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++)
                order[i] = n - 1 - i;
        Arrays.sort(order, (a, b) -> Character.compare(S.charAt(a), S.charAt(b)));
        int[] sa = new int[n];
        int[] classes = new int[n];
        for (int i = 0; i < n; i++)
        {
            sa[i] = order[i];
            classes[i] = S.charAt(i);
        }
        for (int len = 1; len < n; len <<= 1)
        {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++)
                classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n
                                    && c[sa[i - 1] + (len >> 1)] == c[sa[i] + (len >> 1)] ? classes[sa[i - 1]] : i;
            int[] cnt = new int[n];
            for (int i = 0; i < n; i++)
                cnt[i] = i;
            int[] s = sa.clone();
            for (int i = 0; i < n; i++)
            {
                int s1 = s[i] - len;
                if (s1 >= 0)
                    sa[cnt[classes[s1]]++] = s1;
            }
        }
        return sa;
    }

    static int[] lcp(int[] sa, String s)
    {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;
        int[] lcp = new int[n - 1];
        
        for (int i = 0, h = 0; i < n; i++)
        {
            if (rank[i] < n - 1)
            {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length()
                                && s.charAt(i + h) == s.charAt(j + h); ++h);
                lcp[rank[i]] = h;
                if (h > 0)
                    --h;
            }
        }
        return lcp;
    }   
}
