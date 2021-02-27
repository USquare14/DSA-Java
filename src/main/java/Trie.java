/**
 * @author umangupadhyay
 */

public class Trie {
    
    public static int len=31;
    static int[] ans;
    static int trie[][];
    static int[] level;
    static int[] cnt;
    static int c;

    static void add(int k, String a)
    {
            for (int i = a.length() - 1; i >= 0; i--)
            {
                    if (trie[k][a.charAt(i) - 'a'] != -1){
                            k = trie[k][a.charAt(i) - 'a'];
                    }
                    else
                    {
                            trie[k][a.charAt(i) - 'a'] = c++;
                            level[c - 1] = level[k] + 1;
                            k = c - 1;
                    }
                    cnt[k]++;
            }
    }
    
    static void dfs(int u){

        ans[level[u]] = Math.max(ans[level[u]], cnt[u]);
        
        for(int i = 0; i < 26; i++){
            if(trie[u][i] == -1) continue;
            dfs(trie[u][i]);
        }
        
    }

    static class TrieNode{
        int value;
        TrieNode[] arr=new TrieNode[2];

        public TrieNode() {
            this.value = 0;
            this.arr[0]=null;
            this.arr[1]=null;
        }
    }
    
    static void insert(TrieNode root,int pre_xor){
        TrieNode tmp=root;
        for(int i=len;i>=0;i--){
            int val=(pre_xor&(1<<i))==0?0:1;
            if(tmp.arr[val]==null) tmp.arr[val]=new TrieNode();
            tmp=tmp.arr[val];
        }
        tmp.value=pre_xor;
    }
    
    static int query(TrieNode root, int pre_xor){
        TrieNode temp = root;
        for (int i=len; i>=0; i--)
        {
            int val = (pre_xor&(1<<i))==0?0:1;
            
            if (temp.arr[1-val]!=null)
                temp = temp.arr[1-val];
            else if (temp.arr[val] != null)
                temp = temp.arr[val];
        }
        return pre_xor^(temp.value);
    }
    
    static int maxSubarrayXOR(int arr[], int n){

        TrieNode root = new TrieNode();
        insert(root, 0);
 
        int result = Integer.MIN_VALUE, pre_xor =0;

        for (int i=0; i<n; i++){
            pre_xor = pre_xor^arr[i];
            insert(root, pre_xor);
            result = Math.max(result, query(root, pre_xor));
        }
        return result;
    }
    
}
