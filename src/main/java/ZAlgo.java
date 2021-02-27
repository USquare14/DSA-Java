
import java.util.Scanner;

/**
 * @author umangupadhyay
 */

public class ZAlgo {
    
    //Codeforces 126B

    static int[] getZ(char pat[]){
        int n = pat.length;
        int z[] = new int[n];

        int l = 0,r = 0;
        for(int i=1;i<n;i++){
            if(i > r){
                l = r = i;
                while(r < n && pat[r-l] == pat[r]) r++;
                z[i] = r - l;
                r--;
            }
            else{
                int k = i - l;
                if(z[k] < r - i + 1)	z[i] = z[k];
                else{
                    l = i;
                    while(r < n && pat[r-l] == pat[r]) r++;
                    z[i] = r - l;
                    r--;
                }
            }
        }

        z[0] = 0;
        return z;
    }
    
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        
        char[] s=in.next().toCharArray();
        int n=s.length;
        int[] z=new int[n];
        
        z=getZ(s);
        int ans = 0;

        int max[]=new int[n];
        for(int i=1;i<n;i++)
            max[i]=Math.max(max[i-1], z[i]);

        for(int i=1;i<n;i++)
            if(z[i]+i==n && max[i-1]>=z[i])
                    ans = Math.max(ans, z[i]);
        
        System.out.println(ans==0?"Just a legend":new String(s, 0, ans));
        
    }
    
}
