/**
 * @author umangupadhyay
 */

public class LIS {
    
    static int ceilIndex(int A[], int l, int r, int key){
        
        while (r - l > 1){
            int m = l + (r - l)/2;
            if (A[m]>=key)
                r = m;
            else
                l = m;
        }
 
        return r;
    }
 
    static int LISLength(int A[], int l,int r){
        
        int[] tailTable   = new int[r-l+1];
        int len;
        //l=starting index of array and r=last index
 
        tailTable[0] = A[l];
        len = 1;
        for (int i = l+1; i <=r; i++)
        {
            if (A[i] < tailTable[0])
                tailTable[0] = A[i];
 
            else if (A[i] > tailTable[len-1])
                tailTable[len++] = A[i];
 
            else
                tailTable[ceilIndex(tailTable, -1, len-1, A[i])] = A[i];
        }
 
        return len;
    }
   
}
