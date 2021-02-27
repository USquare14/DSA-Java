import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class MySegmentTree {
    
    static int mod = (int) (1e9+7);
    static long[] arr;

    public static void add(int[] t, int i, int value) {
        i += t.length / 2;
        t[i] = value;
        for (; i > 0; i >>= 1) {
            t[i >> 1] = t[i] + t[i ^ 1];
        }
    }

    public static int get(int[] t, int a, int b) {
        int res = 0;
        for (a += t.length / 2, b += t.length / 2; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
            if ((a & 1) != 0) {
                res = res + t[a];
            }
            if ((b & 1) == 0) {
                res = res + t[b];
            }
        }
        return res;
    }
    
    static class SegmentTree {
        long st[];
 
        SegmentTree(int n)  {
            st = new long[4*n];
            build(0, n - 1, 1);
        }
        
        int getMid(int s, int e) {
            return (s+e)>>1;
        }

        long merge(long a,long b){
            return a+b;
        }
        
        void update(int s, int e, int x, int y, int c, int si){
            if(s == x && e == y){
                st[si] += c;
            }
            else{
                int mid = getMid(s, e);
                if(y <= mid)    
                    update(s, mid, x, y, c, 2*si);
                else if(x > mid)
                    update(mid + 1, e, x ,y ,c ,2*si + 1);
                else{
                    update(s, mid, x, mid, c, 2*si);
                    update(mid + 1, e, mid + 1, y, c, 2*si + 1);
                }
                st[si] = merge(st[2*si],st[2*si+1]);
            }
        }

        long  get(int s, int e, int x, int y, int si){

            if(s == x && e == y){
                return st[si];
            }
            int mid = getMid(s, e);
            if(y <= mid)
                return get(s, mid, x, y, 2*si);
            else if(x > mid)
                return get(mid + 1, e, x, y, 2*si + 1);
            return merge(get(s, mid, x, mid, 2*si), get(mid + 1, e, mid + 1, y, 2*si + 1));
        }
/*
        int get(int s, int e, int n, int si) {

            if (s == e) {
                if (n == st[si]) {
                    return s;
                }
                return -1;
            }
            int mid = getMid(s, e);
            if (st[2 * si] >= n) {
                return get(s, mid, n, 2 * si);
            } else {
                return get(mid + 1, e, n - st[2 * si], 2 * si + 1);
            }
        }
*/
        void build(int ss, int se, int si){
            if (ss == se) {
                st[si] = arr[ss];
                return;
            }

            int mid = getMid(ss, se);
            build(ss, mid, si * 2 );
            build(mid + 1, se, si * 2 + 1);
            st[si] = merge(st[2*si],st[2*si+1]);
        }
        
    }

    
    static class SegmentTree1 {
        long st[];
        long lazy[];
 
        SegmentTree1(int n)  {
            st = new long[4*n];
            lazy = new long[4*n];
            build(0, n - 1, 1);
        }

        SegmentTree1(int n,int[] arr) {
            st = new long[4*n];
            build(0, n - 1, 1,arr);
        }
        
        int getMid(int s, int e) {
            return (s+e)>>1;
        }
        
        void lazyupdate(int ss,int ee,int si){
            if(lazy[si] != 0){
                if(ss != ee){
                    lazy[2*si] += lazy[si];
                    lazy[2*si+1] += lazy[si];
                }
                st[si] += (ee-ss+1) * lazy[si];
                lazy[si] = 0;
            }
        }

        long merge(long a,long b){
            return a + b;
        }
        
        void update(int s, int e, int x, int y, int c, int si){
            if(s == x && e == y){
                lazy[si] += c;
            }
            else{
                lazyupdate(s, e, si);
                int mid = getMid(s, e);
                if(y <= mid)    
                    update(s, mid, x, y, c, 2*si);
                else if(x > mid)
                    update(mid + 1, e, x ,y ,c ,2*si + 1);
                else{
                    update(s, mid, x, mid, c, 2*si);
                    update(mid + 1, e, mid + 1, y, c, 2*si + 1);
                }
                lazyupdate(s, mid, 2*si);
                lazyupdate(mid + 1, e, 2*si + 1);
                st[si] = merge(st[2*si],st[2*si+1]);
            }
        }

        long  get(int s, int e, int x, int y, int si){

            lazyupdate(s, e,si);
            if(s == x && e == y){
                return st[si];
            }
            int mid = getMid(s, e);
            if(y <= mid)
                return get(s, mid, x, y, 2*si);
            else if(x > mid)
                return get(mid + 1, e, x, y, 2*si + 1);
            return merge(get(s, mid, x, mid, 2*si), get(mid + 1, e, mid + 1, y, 2*si + 1));
        }

        void build(int ss, int se, int si){
            if (ss == se) {
                st[si] = arr[ss];
                lazy[si] = 0;
                return;
            }

            int mid = getMid(ss, se);
            build(ss, mid, si * 2 );
            build(mid + 1, se, si * 2 + 1);
            lazy[si] = 0;
            st[si] = merge(st[2*si],st[2*si+1]);
        }
        
        void build(int ss, int se, int si,int[] arr){
            if (ss == se) {
                st[si] = arr[ss];
                lazy[si] = 0;
                return;
            }

            int mid = getMid(ss, se);
            build(ss, mid, si * 2 ,arr);
            build(mid + 1, se, si * 2 + 1,arr);
            lazy[si] = 0;
            st[si] = merge(st[2*si],st[2*si+1]);
        }
    }
    
    static class MergeTree {
        int st[][];
 
        MergeTree(int n)  {
            st = new int[4*n][];
            build(0, n - 1, 1);
        }
        
        int getMid(int s, int e) {
            return (s+e)>>1;
        }

        int[] merge(int[] a,int[] b){
            if(a == null) return b;
            if(b == null) return a;
            int[] c = new int[a.length+b.length];
            int i=0;
            int j=0;
            int k=0;
            while(true){
                if(i==a.length){
                    while(j<b.length) c[k++]=b[j++];
                    break;
                }
                if(j==b.length){
                    while(i<a.length) c[k++]=a[i++];
                    break;
                }
                if(a[i]<b[j])
                    c[k++]=a[i++];
                else c[k++]=b[j++];
            }
            
            return c;
        }

        int getAns(int[] a,int key){
            int l=0;
            int r=a.length-1;
            int ans=0;

            while(r-l>=0){
                int m=(r+l)>>1;
                if(a[m]>key){
                    ans=a.length-m;
                    r=m-1;
                }
                else{
                    l=m+1;
                }
            }
            return ans;
        }
        
        int get(int s, int e, int x, int y,int key ,int si){

            if(s == x && e == y){
                return getAns(st[si],key);
            }
            int mid = getMid(s, e);
            if(y <= mid)
                return get(s, mid, x, y,key, 2*si);
            else if(x > mid)
                return get(mid + 1, e, x, y,key, 2*si + 1);
            return get(s, mid, x, mid,key, 2*si) + get(mid + 1, e, mid + 1, y,key, 2*si + 1);
        }

        void build(int ss, int se, int si){
            if (ss == se) {
                st[si]=new int[]{(int)arr[ss]};
                return;
            }

            int mid = getMid(ss, se);
            build(ss, mid, si * 2 );
            build(mid + 1, se, si * 2 + 1);
            st[si]=merge(st[2*si],st[2*si+1]);
        }
        
    }

}    

