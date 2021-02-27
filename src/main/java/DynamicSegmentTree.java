
/**
 * @author umangupadhyay
 */

public class DynamicSegmentTree {

	static class Node{
    	long val;
    	Node left;
    	Node right;
    	boolean swap;
    	int height;
    	boolean[][] lazy;
    	
    	
    	public Node(int x) {
    		this.val = x;
    		this.left = null;
    		this.right = null;
    		this.height = 0;
    		this.lazy = new boolean[1][2];
		}
    	
    	public Node(Node left, Node right) {
    		this.left = left;
    		this.right = right;
    		this.val = left.val + right.val;
    		this.height = left.height + 1;
    		this.lazy = new boolean[this.height + 1][2];
		}
    	
    	public void updateSwap(){
    		this.swap = !this.swap;
    	}
    	
    	public Node left() {
    		return this.swap ? this.right: this.left;
    	}
 
    	public Node right() {
    		return this.swap ? this.left: this.right;
    	}
    	
    	
    	public void lazyUpdate(){
    		if(this.left == null) return;
    		
    		for(int i = 1; i <= this.height; i++) {
    			for(int j = 0; j <= 1; j++) {
        			if(lazy[i][j]) {
            			this.left.lazy[i - 1][j] = !this.left.lazy[i - 1][j];
            			this.right.lazy[i - 1][j] = !this.right.lazy[i - 1][j];    				
            			this.lazy[i][j] = false;
        			}    				
    			}
    		}
    		if(this.lazy[0][0]) {
    			updateSwap();
    			this.left.lazy[0][0] = !this.left.lazy[0][0];
    			this.right.lazy[0][0] = !this.right.lazy[0][0];
    			this.lazy[0][0] = false;
    		}
    		
    		if(this.lazy[0][1]) {
    			updateSwap();
    			this.lazy[0][1] = false;
    		}
    	}
    }
    
    static Node build(int[] arr, int l, int r) {
    	if(l == r) {
    		return new Node(arr[l]);
    	}
    	int mid = (r + l) >> 1;
    	return new Node(build(arr, l, mid), build(arr, mid + 1, r));
    }
    
    static Node updateVal(int val, int x, int l, int r, Node root){
    	root.lazyUpdate();
    	if(x == l && x == r) {
    		return new Node(val);
    	}
    	int mid = (r + l) >> 1;
    	if(x <= mid) {
    		return new Node(updateVal(val, x, l, mid, root.left()), root.right());
    	}
    	else {
    		return new Node(root.left(), updateVal(val, x, mid + 1, r, root.right()));
    	}
    }
    
    static long getSum(int x, int y, int l, int r, Node root){
    	root.lazyUpdate();
    	if(x == l && y == r) {
    		return root.val;
    	}
    	int mid = (r + l) >> 1;
    	if(y <= mid) {
    		return getSum(x, y, l, mid, root.left());
    	}
    	else if(x > mid) {
    		return getSum(x, y, mid + 1, r, root.right());    		
    	}
    	else {
    		return getSum(x, mid, l, mid, root.left()) + getSum(mid + 1, y, mid + 1, r, root.right());   		
    	}
    }
    
//    static void traverse(Node root, int l, int r) {
//    	if(l == r) {
//    		debug(l, root.val);
//    		return;
//    	}
//    	int mid = (r + l) >> 1;
//    	traverse(root.left(), l, mid);
//    	traverse(root.right(), mid + 1, r);
//    }
    
}
