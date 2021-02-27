
import java.util.Random;
import java.util.Stack;

/**
 * @author umangupadhyay
 */

public class Treap {
    
    int y;
    int value;
    int cnt;
    Treap l,r;
    boolean reversed;
    int size;

    public Treap(int y, int value,Treap l, Treap r)
    {
        this.y = y;
        this.value = value;
        this.l = l;
        this.r = r;
        this.size = size(l) + size(r) + 1;
        this.reversed = false;
        this.cnt = 1;
    }

    void fix()
    {
        if(reversed){
            reversed = false;
            if(l != null){
                l.reversed ^= true;
            }
            if(r != null){
                r.reversed ^= true;
            }
            Treap tmp = l;
            l = r;
            r = tmp;
        }
    }

    static int size(Treap t){
        return t == null ? 0 : t.size;
    }

    static Treap merge(Treap left, Treap right){
        if(left == null)
            return right;
        if(right == null)
            return left;
        left.fix();
        right.fix();
        if(left.y <= right.y){
            left.r = merge(left.r, right);
            left.size = size(left.l) + size(left.r) + 1;
            return left;
        }
        else{
            right.l = merge(left, right.l);
            right.size = size(right.l) + size(right.r) + 1;
            return right;
        }
    }

    static Treap splitL, splitR;

    static void splitRecursive(Treap t, int x){
        if(t == null){
            splitL = null;
            splitR = null;
            return;
        }
        t.fix();
        if(size(t.l) < x){
            splitRecursive(t.r, x - size(t.l) - 1);
            t.r = splitL;
            splitL = t;
        }
        else{
            splitRecursive(t.l, x);
            t.l = splitR;
            splitR = t;
        }
        t.size = size(t.l) + size(t.r) + 1;
    }

    static Random rng = new Random(1);

    static Treap build(int n){
        Stack<Treap> st = new Stack<>();
        Treap first = new Treap(rng.nextInt(), 1, null, null);
        st.add(first);
        Treap root = first;

        for(int i = 2; i <= n; i++){
            Treap cur = new Treap(rng.nextInt(), i, null, null);
            if(cur.y < root.y){
                cur.l = root;
                root = cur;
                st.clear();
            }
            else{
                while(st.peek().y > cur.y) st.pop();
                Treap t = st.peek();
                cur.l = t.r;
                t.r = cur;
            }
            st.add(cur);
        }
        root.calcSize();
        return root;
    }

    void calcSize()
    {
        if(l != null)
            l.calcSize();
        if(r != null)
            r.calcSize();
        size = size(l) + size(r) + 1;
    }

    int[] toArray()
    {
        int[] array = new int[size(this)];
        toArray(array, 0);
        return array;
    }

    void toArray(int[] array, int off) {
        fix();
        if (l != null) {
                l.toArray(array, off);
                off += l.size;
        }
        array[off++] = value;
        if (r != null) {
                r.toArray(array, off);
        }
    }

}
