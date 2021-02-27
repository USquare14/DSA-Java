
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

/**
 * @author umangupadhyay
 */

//Pair Sums HourRank 26

public class ConvexHullOptimization {
    
    static class CHT {
        TreeSet<Line> hull;
        int type;
        boolean query  = false;
        Comparator<Line> comp = new Comparator<Line>() {
                public int compare(Line a, Line b) {
                        if(!query) return type * Long.compare(a.m, b.m);
                        if (a.left == b.left)
                                return Long.compare(a.m, b.m);
                        return Double.compare(a.left, b.left);
                }
        };
        public CHT(final int type) {
                this.type = type;
                hull = new TreeSet<>(comp);
        }

        public void add(long m, long b) {
                add(new Line(m, b));
        }

        public void add(Line a) {
                Line[] LR = { hull.lower(a), hull.ceiling(a) };
                for (int i = 0; i < 2; i++)
                        if (LR[i] != null && LR[i].m == a.m) {
                                if (type == 1 && LR[i].b >= a.b)
                                        return;
                                if (type == -1 && LR[i].b <= a.b)
                                        return;
                                remove(LR[i]);
                        }

                hull.add(a);
                Line L = hull.lower(a), R = hull.higher(a);
                if (L != null && R != null && a.inter(R) <= R.left) {
                        hull.remove(a);
                        return;
                }
                Line LL = (L != null) ? hull.lower(L) : null;
                Line RR = (R != null) ? hull.higher(R) : null;
                if (L != null) a.left = a.inter(L);
                if (R != null) R.left = a.inter(R);
                while (LL != null && L.left >= a.inter(LL)) {
                        remove(L);
                        a.left = a.inter(L = LL);
                        LL = hull.lower(L);
                }
                while (RR != null && R.inter(RR) <= a.inter(RR)) {
                        remove(R);
                        RR.left = a.inter(R = RR);
                        RR = hull.higher(R);
                }
        }

        public long query(long x) {
                Line temp = new Line(0, 0, 0);
                temp.left = x;
                query = true;
                long ans = (long) hull.floor(temp).eval(x);
                query = false;
                return ans;
        }

        private void remove(Line x) {
                hull.remove(x);
        }

        public int size() {
                return hull.size();
        }

        static class Line {
                long m, b;
                double left = Long.MIN_VALUE;

                public Line(long m, long x, long y) {
                        this.m = m;
                        this.b = -m * x + y;
                }

                public Line(long m, long b) {
                        this.m = m;
                        this.b = b;
                }

                public long eval(long x) {
                        return m * x + b;
                }

                public double inter(Line x) {
                        return (double) (x.b - this.b) / (double) (this.m - x.m);
                }
        }
    }
    
    static class MinCht{
        ArrayList<Long> M;
        ArrayList<Long> B;
        
        public MinCht()
        {
                M = new ArrayList<Long>();
                B = new ArrayList<Long>();
        }
        
        void add(long m, long b)
        {
                M.add(m);
                B.add(b);
                while(M.size() >= 3 && bad(M.size()-3, M.size()-2, M.size()-1))
                {
                        M.remove(M.size()-2);
                        B.remove(B.size()-2);
                }
        }
        long get(long x,int i) {
                return M.get(i)*x+B.get(i);
        }

        long query(long x)
        {
                int lo = 1;
                int hi = M.size()-1;
                int res = 0;
                
                while(hi - lo >= 0) {
                    int m = (lo + hi) >> 1;
                    long v1 = get(x, m);
                    long v2 = get(x, m - 1);
                    
                    if(v1 < v2) {
                        lo = m + 1;
                    }
                    else hi = m - 1;
                }
                return get(x, lo - 1);
                /*
                while(lo < hi)
                {
                        int m = lo + (hi-lo)/2;
                        long v1 = get(x,m);
                        long v2 = get(x,m+1);
                        //for max v1 < v2
                        if(v1 >= v2) { 
                                lo = m+1;
                                res = m+1;
                        }
                        else hi = m;
                }
                return get(x,res);*/
        }
        boolean bad(int l1, int l2, int l3)
        {
                return (double)(B.get(l3)-B.get(l1))/(double)(M.get(l1)-M.get(l3)) <= (double)(B.get(l2)-B.get(l1))/(double)(M.get(l1)-M.get(l2));
                //return (B.get(l3)-B.get(l1))*(M.get(l1)-M.get(l2)) <= (B.get(l2)-B.get(l1))*(M.get(l1)-M.get(l3));
        }
    }
    
    static Line[] st = new Line[1000005];
    static int ptr = 0;
    static int len = 0;
    
    static class Line implements Comparable<Line>{

        long m,c;
        
        public Line(long m, long c){
            this.m = m;
            this.c = c;
        }
        
        @Override
        public int compareTo(Line t) {
            return Long.compare(t.m, this.m);
        }
        
        public long value(long x){
            return m * x + c;
        }
        
    }
    
    static void addLine(long a, long b){
        
        Line toAdd = new Line(a, b);
        
        while(len >= 2  && (double)(st[len - 2].c - st[len - 1].c) * (a - st[len - 1].m) >= (double)(st[len - 1].c - b) * (st[len - 1].m - st[len - 2].m)) {
            len--;
        }
        st[len] = toAdd;
        len ++;
    }
    
    static long getMin(long x){
        
        ptr = Math.min(ptr, len - 1);
        
        while(ptr + 1 < len && st[ptr + 1].value(x) <= st[ptr].value(x)){
            ptr++;
        }
        
        return st[ptr].value(x);
    }
    
}
