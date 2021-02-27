/**
 * @author umangupadhyay
 */

public class ManacharAlgo {
    
    static int[] p = new int[200010];

    static String convertToNewString(String s) {

        StringBuilder newString = new StringBuilder();
        newString.append('@');
        
        for (int i = 0; i < s.length(); i++)
            newString.append('#').append(s.charAt(i));

        newString.append("#$");
        return newString.toString();
    }
    
    static String longestPalindromicSubString(String s){

        String q = convertToNewString(s);
        int c = 0, r = 0;
        
        for(int i = 1 ; i < q.length(); i++){
            int mirror = 2 * c - i;
            if(r > i) p[i] = Math.min(r - i, p[mirror]);
            while(i + 1 + p[i] < q.length() && q.charAt(i + 1 + p[i]) == q.charAt(i - 1 - p[i])) p[i]++;
            
            if(i + p[i] > r){
                c = i;
                r = i + p[i];
            }
        }
        int max = 0;
        int center = 0;
        
        for(int i = 1; i < q.length(); i++)
            if(p[i] > max){
                max = p[i];
                center = i;
            }
        
        return s.substring((center - 1 - max) / 2,(center - 1 - max) / 2 + max);
    }
    
}
