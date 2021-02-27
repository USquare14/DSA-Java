
import java.util.ArrayList;

/**
 * @author umangupadhyay
 */

public class SegmentTree {
        int st[];
 
        SegmentTree(int arr[], int n)  {
            int x = (int) (Math.ceil(Math.log(n) / Math.log(2)));
            int max_size = 2 * (int) Math.pow(2, x) - 1;
            st = new int[max_size]; // Memory allocation
            constructSTUtil(arr, 0, n - 1, 0);
        }
 
        int getMid(int s, int e) {
            return (s+e)>>1;
        }
 
        int getMaxUtil(int ss, int se, int qs, int qe, int si){
            if (qs <= ss && qe >= se)
                return st[si];
            if (se < qs || ss > qe)
                return 0;
 
            int mid = getMid(ss, se);
            return Math.max(getMaxUtil(ss, mid, qs, qe, 2 * si + 1),
                getMaxUtil(mid + 1, se, qs, qe, 2 * si + 2));
        }

        void updateValueUtil(int ss, int se, int i, int new_value, int si){
            if (ss>se || i < ss || i > se)
                return;
            if(se==ss){
                st[si]=new_value;
            }
            else{
                int mid = getMid(ss, se);
                if(i>=ss && i<=mid ) updateValueUtil(ss, mid, i, new_value, 2 * si + 1);
                if(i>=mid+1 && i<=se) updateValueUtil(mid + 1, se, i, new_value, 2 * si + 2);
                st[si] = Math.max(st[si*2+1], st[si*2+2]);
            }
        }
 
        void updateValue(int arr[], int n, int i){
            if (i < 0 || i > n - 1) {
                return;
            }
            
            updateValueUtil(0, n - 1, i,arr[i], 0);
        }
 
        int getMax(int n, int qs, int qe){
            if (qs < 0 || qe > n - 1 || qs > qe) {
                return -1;
            }
            return getMaxUtil(0, n - 1, qs, qe, 0);
        }
 
        int constructSTUtil(int arr[], int ss, int se, int si){
            if (ss == se) {
                st[si] = arr[ss];
                return st[si];
            }
 
            int mid = getMid(ss, se);
            st[si] = Math.max(constructSTUtil(arr, ss, mid, si * 2 + 1),
                 constructSTUtil(arr, mid + 1, se, si * 2 + 2));
            return st[si];
        }
        
}
