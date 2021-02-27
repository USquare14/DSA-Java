import java.util.*;

/**
 * @author umangupadhyay
 */

public class LCASparseTable {

    static int len;
    static int[][] up;
    static int[] tin;
    static int[] tout;
    static int time;
    static int[][] g;

    static void dfs(int u, int p) {
        tin[u] = time++;
        up[0][u] = p;
        for (int i = 1; i < len; i++)
            up[i][u] = up[i - 1][up[i - 1][u]];
        for (int v :g[u])
            if (v != p)
                dfs( v, u);
        tout[u] = time++;
    }


    static boolean isParent(int parent, int child) {
        return tin[parent] <= tin[child] && tout[child] <= tout[parent];
    }

    static int lca(int a, int b) {
        if (isParent(a, b))
            return a;
        if (isParent(b, a))
            return b;
        for (int i = len - 1; i >= 0; i--)
            if (!isParent(up[i][a], b))
                a = up[i][a];
        return up[0][a];
    }

    static void preprocess(int n,int root){

        len = 1;
        while ((1 << len) <= n) ++len;
        up = new int[len][n];
        tin = new int[n];
        tout = new int[n];
        dfs(root, root);
        
    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> tree = new ArrayList<>();
        int n = 5;
      for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
      tree.get(0).add(1);
      tree.get(1).add(0);
      tree.get(1).add(2);
      tree.get(2).add(1);
      tree.get(3).add(1);
      tree.get(1).add(3);
      tree.get(0).add(4);
      tree.get(4).add(0);

      preprocess( n, 0);
      System.out.println(1 == lca(3, 2));
      System.out.println(0 == lca(2, 4));
    }
}