import java.util.*;
import java.io.*;

/**
 * @author umangupadhyay
 */

public class SCC {
    static ArrayList<Integer>[] g;
    static boolean[] vis;
    static Stack<Integer> stack;
    static int time;
    static int[] lowlink;
    static int n;
    
    static void scc() {
        vis = new boolean[n];
        stack = new Stack<>();
        time = 0;
        lowlink = new int[n];

        for (int u = 0; u < n; u++)
          if (!vis[u])
            dfs(u);

    }
    
    static void dfs(int u) {
        lowlink[u] = time++;
        vis[u] = true;
        stack.add(u);
        boolean isComponentRoot = true;

        for (int v : g[u]) {
            if (!vis[v])
                dfs(v);
            if (lowlink[u] > lowlink[v]) {
                lowlink[u] = lowlink[v];
                isComponentRoot = false;
            }
        }

        if (isComponentRoot) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int x = stack.pop();
                component.add(x);
                lowlink[x] = Integer.MAX_VALUE;
                if (x == u)
                break;
            }
        }
    }
    
}
