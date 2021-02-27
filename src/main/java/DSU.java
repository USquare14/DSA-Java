
/**
 * @author umangupadhyay
 */

public class DSU {
    
    static int[] rt;
    static int[] size;

    static void initialize(int n){
        rt = new int[n + 1];
        size = new int[n + 1];
        for(int i = 0; i < rt.length; i++){
            rt[i] = i;
            size[i] = 1;
        }
    }
    
    static int root(int x){
        while(rt[x] != x){
            rt[x] = rt[rt[x]];
            x = rt[x];
        }
        return x;
    }
    
    static boolean union(int x,int y){
        int root_x = root(x);
        int root_y = root(y);
        if(root_x == root_y) return true;
        
        if(size[root_x]<size[root_y]){
            rt[root_x] = rt[root_y];
            size[root_y] += size[root_x];
        }
        else{
            rt[root_y] = rt[root_x];
            size[root_x] += size[root_y];            
        }
        return false;
    }
}
