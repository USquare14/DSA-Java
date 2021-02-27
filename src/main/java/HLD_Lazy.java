import java.io.*;
import java.util.*;

/**
 * @author umangupadhyay
 */

public class HLD_Lazy
{
	private static InputStream stream;
	private static byte[] buf = new byte[1024];
	private static int curChar;
	private static int numChars;
	private static SpaceCharFilter filter;
	private static PrintWriter pw;
	private static long mod = 1000000000 + 7;

	private static int[] cHead;
	private static int[] cPos;
	private static int[] cInd;
	private static int[] bArr;
	private static int cNo = 0;

	private static int[] pa;
	private static int[] size;
	private static int[] depth;
	private static int[][] lca;
	private static int dNo = 0;
	private static int log;
	private static int[] arr;
	private static Segment seg;

	private static void soln()
	{
		int n = nextInt();
		int q = nextInt();
		cNo = 0;
		dNo = 0;
		cHead = new int[n];
		cPos = new int[n];
		cInd = new int[n];
		bArr = new int[n];
		size = new int[n];
		pa = new int[n];
		depth = new int[n];
		log = (int) Math.ceil(Math.log(n) / Math.log(2));
		lca = new int[log][n];

		Arrays.fill(cHead, -1);
		for (int i = 0; i < log; i++)
			Arrays.fill(lca[i], -1);

		LinkedList<Integer>[] tree = new LinkedList[n];
		for (int i = 0; i < n; i++)
			tree[i] = new LinkedList<>();
		for (int i = 0; i < n - 1; i++) {
			int u = nextInt() - 1;
			int v = nextInt() - 1;
			tree[u].add(0, v);
			tree[v].add(0, u);
		}

		dfs(0, 0, tree);
		hld(0, 0, tree);

		seg = new Segment(n + 5);

		for (int j = 1; j < log; j++) {
			for (int i = 0; i < n; i++) {
				if (lca[j - 1][i] != -1)
					lca[j][i] = lca[j - 1][lca[j - 1][i]];
			}
		}
		// debug(q);
		while (q-- > 0) {
			int x = nextInt() - 1;
			int y = nextInt() - 1;
			int a = nextInt() - 1;
			int b = nextInt() - 1;

			int lca1 = lca(x, y);
			int lca2 = lca(a, b);

			update(x, lca1, 1);
			update(y, lca1, 1);
			update(lca1, lca1, -1);

			int ans = query(a, lca2);
			ans += query(b, lca2);
			// debug(ans);
			ans -= query(lca2, lca2);

			update(x, lca1, -1);
			update(y, lca1, -1);
			update(lca1, lca1, 1);

			pw.println(ans);
		}
	}

	private static void debug(Object... o)
	{
		System.out.println(Arrays.deepToString(o));
	}

	private static int query(int u, int v)
	{
		int ans = 0;
		while (true) {
			// debug(cPos[v]+" "+cPos[u]);
			if (cInd[u] == cInd[v]) {
				ans += seg.query(cPos[v], cPos[u]);
				break;
			}
			// debug(cPos[cHead[cInd[u]]]+" "+cPos[u]);
			ans += seg.query(cPos[cHead[cInd[u]]], cPos[u]);
			u = cHead[cInd[u]];
			u = pa[u];
		}
		return ans;
	}

	private static void update(int u, int v, int val)
	{
		while (true) {
			if (cInd[u] == cInd[v]) {
				// debug(cPos[v]+" "+cPos[u]);
				seg.update(cPos[v], cPos[u], val);
				break;
			}
			// debug(cPos[cHead[cInd[u]]]+" "+cPos[u]);
			seg.update(cPos[cHead[cInd[u]]], cPos[u], val);
			u = cHead[cInd[u]];
			u = pa[u];
		}
	}

	private static int lca(int a, int b)
	{
		if (depth[a] < depth[b]) {
			a = a ^ b;
			b = a ^ b;
			a = a ^ b;
		}
		int diff = depth[a] - depth[b];
		for (int j = 0; j < log; j++)
			if ((1 & (diff >> j)) != 0)
				a = lca[j][a];

		if (a == b)
			return a;

		for (int j = log - 1; j >= 0; j--)
			if (lca[j][a] != lca[j][b]) {
				a = lca[j][a];
				b = lca[j][b];
			}
		return lca[0][a];
	}

	private static void hld(int cur, int prev, LinkedList<Integer>[] tree)
	{
		if (cHead[cNo] == -1)
			cHead[cNo] = cur;
		cInd[cur] = cNo;
		cPos[cur] = dNo++;
		int ind = -1, max_size = -1;
		Iterator<Integer> it = tree[cur].listIterator();
		while (it.hasNext()) {
			long x1 = it.next();
			int x = (int) x1;
			if (x != prev) {
				if (size[x] > max_size) {
					max_size = size[x];
					ind = x;
				}
			}
		}
		if (ind >= 0)
			hld(ind, cur, tree);

		it = tree[cur].listIterator();
		while (it.hasNext()) {
			long x1 = it.next();
			int x = (int) x1;
			if (x != prev && x != ind) {
				cNo++;
				hld(x, cur, tree);
			}
		}
	}

	private static void dfs(int cur, int prev, LinkedList<Integer>[] tree)
	{
		pa[cur] = prev;
		size[cur] = 1;
		lca[0][cur] = prev;
		Iterator<Integer> it = tree[cur].listIterator();
		while (it.hasNext()) {
			int x = it.next();
			if (x != prev) {
				depth[x] = depth[cur] + 1;
				dfs(x, cur, tree);
				size[cur] += size[x];
			}
		}
	}

	public static class Segment
	{
		private int[] tree;
		private int[] lazy;
		private int size;
		private int n;

		public Segment(int n)
		{
			// this.base=arr;
			int x = (int) (Math.ceil(Math.log(n) / Math.log(2)));
			size = 2 * (int) Math.pow(2, x) - 1;
			tree = new int[size];
			lazy = new int[size];
			this.n = n;
		}

		public int query(int l, int r)
		{
			return queryUtil(l, r, 0, 0, n - 1);
		}

		private int queryUtil(int x, int y, int id, int l, int r)
		{
			if (l > y || x > r)
				return 0;
			if (x <= l && r <= y) {
				return tree[id];
			}
			int mid = ((l + r) >> 1);
			shift(id, l, r, mid);
			int q1 = queryUtil(x, y, (id << 1) | 1, l, mid);
			int q2 = queryUtil(x, y, (id << 1) + 2, mid + 1, r);
			return q1 + q2;
		}

		public void update(int x, int y, int c)
		{
			update1(x, y, c, 0, 0, n - 1);
		}

		private void update1(int x, int y, int colour, int id, int l, int r)
		{
			if (x > r || y < l)
				return;
			if (x <= l && r <= y) {
				tree[id] += (r - l + 1) * colour;
				lazy[id] += colour;
				return;
			}
			int mid = ((l + r) >> 1);
			shift(id, l, r, mid);
			if (y <= mid)
				update1(x, y, colour, (id << 1) | 1, l, mid);
			else if (x > mid)
				update1(x, y, colour, (id << 1) + 2, mid + 1, r);
			else {
				update1(x, y, colour, (id << 1) | 1, l, mid);
				update1(x, y, colour, (id << 1) + 2, mid + 1, r);
			}
			tree[id] = tree[(id << 1) | 1] + tree[(id << 1) + 2];

		}

		public void shift(int id, int l, int r, int mid)
		{
			if (lazy[id] != 0) {
				lazy[(id << 1) | 1] += lazy[id];
				tree[(id << 1) | 1] += (mid - l + 1) * lazy[id];
				lazy[(id << 1) + 2] += lazy[id];
				tree[(id << 1) + 2] += (r - mid) * lazy[id];
				lazy[id] = 0;
			}
		}
	}

	private static long pow(long a, long b, long c)
	{
		if (b == 0)
			return 1;
		long p = pow(a, b / 2, c);
		p = (p * p) % c;
		return (b % 2 == 0) ? p : (a * p) % c;
	}

	private static long gcd(long n, long l)
	{
		if (l == 0)
			return n;
		return gcd(l, n % l);
	}

	public static void main(String[] args) throws Exception
	{
		new Thread(null, new Runnable()
		{
			@Override
			public void run()
			{

                            InputReader(System.in);
                            pw = new PrintWriter(System.out);
                            soln();
                            pw.close();
			}
		}, "1", 1 << 26).start();
	}

	public static void InputReader(InputStream stream1)
	{
		stream = stream1;
	}

	private static boolean isWhitespace(int c)
	{
		return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
	}

	private static boolean isEndOfLine(int c)
	{
		return c == '\n' || c == '\r' || c == -1;
	}

	private static int read()
	{
		if (numChars == -1)
			throw new InputMismatchException();
		if (curChar >= numChars) {
			curChar = 0;
			try {
				numChars = stream.read(buf);
			} catch (IOException e) {
				throw new InputMismatchException();
			}
			if (numChars <= 0)
				return -1;
		}
		return buf[curChar++];
	}

	private static int nextInt()
	{
		int c = read();
		while (isSpaceChar(c))
			c = read();
		int sgn = 1;
		if (c == '-') {
			sgn = -1;
			c = read();
		}
		int res = 0;
		do {
			if (c < '0' || c > '9')
				throw new InputMismatchException();
			res *= 10;
			res += c - '0';
			c = read();
		} while (!isSpaceChar(c));
		return res * sgn;
	}

	private static long nextLong()
	{
		int c = read();
		while (isSpaceChar(c))
			c = read();
		int sgn = 1;
		if (c == '-') {
			sgn = -1;
			c = read();
		}
		long res = 0;
		do {
			if (c < '0' || c > '9')
				throw new InputMismatchException();
			res *= 10;
			res += c - '0';
			c = read();
		} while (!isSpaceChar(c));
		return res * sgn;
	}

	private static String nextToken()
	{
		int c = read();
		while (isSpaceChar(c))
			c = read();
		StringBuilder res = new StringBuilder();
		do {
			res.appendCodePoint(c);
			c = read();
		} while (!isSpaceChar(c));
		return res.toString();
	}

	private static String nextLine()
	{
		int c = read();
		while (isSpaceChar(c))
			c = read();
		StringBuilder res = new StringBuilder();
		do {
			res.appendCodePoint(c);
			c = read();
		} while (!isEndOfLine(c));
		return res.toString();
	}

	private static int[] nextIntArray(int n)
	{
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = nextInt();
		}
		return arr;
	}

	private static long[] nextLongArray(int n)
	{
		long[] arr = new long[n];
		for (int i = 0; i < n; i++) {
			arr[i] = nextLong();
		}
		return arr;
	}

	private static void pArray(int[] arr)
	{
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
		return;
	}

	private static void pArray(long[] arr)
	{
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
		return;
	}

	private static boolean isSpaceChar(int c)
	{
		if (filter != null)
			return filter.isSpaceChar(c);
		return isWhitespace(c);
	}

	private static char nextChar()
	{
		int c = read();
		while (isSpaceChar(c))
			c = read();
		char c1 = (char) c;
		while (!isSpaceChar(c))
			c = read();
		return c1;
	}

	private interface SpaceCharFilter
	{
		public boolean isSpaceChar(int ch);
	}
}