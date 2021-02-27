
import java.util.Arrays;

/**
 * @author umangupadhyay
 */

public class ImplicitSegmentTree {
    
    static class SegmentTree {
        long st[];
        long lazy[];
        int[] L;
        int[] R;
        int t;
        long N;
        int size;
 
        SegmentTree(long n)  {
            N = n;
            size = 15000000;
            st = new long[size];
            lazy = new long[size];
            L = new int[size];
            R = new int[size];
            Arrays.fill(L, -1);
            Arrays.fill(R, -1);
            Arrays.fill(st, 1);
            t = 0;
        }
        
        long getMid(long s, long e) {
            return (s+e)>>1;
        }
        
        void create(int si){
            if(L[si] == -1) L[si] = t++;
            if(R[si] == -1) R[si] = t++;
        }
        
        void lazyupdate(int si){
            if(lazy[si] != 0){
                st[L[si]] += lazy[si];
                st[R[si]] += lazy[si];
                lazy[L[si]]  += lazy[si];
                lazy[R[si]] += lazy[si];
                lazy[si] = 0;
            }
        }

        long merge(long a,long b){
            return Math.max(a, b);
        }
        
        void update(long x, long y, long c, int si, long l, long r){
            
            if(x <= l && r <= y){
                st[si] += c;
                lazy[si] += c;
                return;
            }

            if(x > r || y < l) 
                    return;
            long mid = getMid(l, r);
                
            create(si);
            lazyupdate(si);
            update(x, y, c, L[si], l, mid);
            update(x, y, c, R[si], mid + 1, r);

            st[si] = merge(st[L[si]],st[R[si]]);
        }
        
        void update(long x, long y, long c){
            update(x, y, c, 1, 1, N);
        }
        
        long query(long x, long y){
            return get(x, y, 1, 1, N);
        }
        
        long get(long x, long y, int si, long l, long r){

            if (l > y || x > r)
                return -1;
            if (x <= l && r <= y)
                return st[si];

            long mid = getMid(l, r);
            create(si);
            lazyupdate(si);
            return merge(get(x, y, L[si], l, mid), get(x, y, R[si], mid + 1, r));
        }

    }
    
    static class ST {

        long from, to;
        long value, lazy;
        ST left, right;

        ST() {
            from = 1;
            to = (long) 1e18;
            value = 1;
            lazy = 0;
            left = null;
            right = null;
        }

        void extend() {
            if (left == null) {
                left = new ST();
                right = new ST();
                left.from = from;
                left.to = (from + to) >> 1;
                right.from = ((from + to) >> 1) + 1;
                right.to = to;
            }
        }
    };

    static ST root;

    static void update_tree(ST curr, long left, long right, long value) {

        if (curr.lazy != 0) {
            curr.value = (curr.value) + curr.lazy;
            if (curr.from != curr.to) {
                curr.extend();
                curr.left.lazy += curr.lazy;
                curr.right.lazy += curr.lazy;
            }
            curr.lazy = 0;
        }
        if ((curr.from) > (curr.to) || (curr.from) > right || (curr.to) < left) {
            return;
        }

        if (curr.from >= left && curr.to <= right) {
            curr.value = curr.value + value;
            if (curr.from != curr.to) {
                curr.extend();
                curr.left.lazy += value;
                curr.right.lazy += value;
            }
            return;
        }
        curr.extend();
        update_tree(curr.left, left, right, value);
        update_tree(curr.right, left, right, value);
        curr.value = Math.max(curr.left.value, curr.right.value);
    }

    static long query_tree(ST curr, long left, long right) {
        if (curr.lazy != 0) {
            curr.value = (curr.value) + curr.lazy;
            curr.extend();
            curr.left.lazy += curr.lazy;
            curr.right.lazy += curr.lazy;
            curr.lazy = 0;
        }
        if ((curr.from) > (curr.to) || (curr.from) > right || (curr.to) < left) {
            return 1;
        }
        if (curr.from >= left && curr.to <= right) {
            return curr.value;
        }
        long q1, q2;
        curr.extend();
        q1 = query_tree(curr.left, left, right);
        q2 = query_tree(curr.right, left, right);
        return Math.max(q1, q2);
    }
    
}
