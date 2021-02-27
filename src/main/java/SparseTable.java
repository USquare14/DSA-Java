/**
 * @author umangupadhyay
 */

public class SparseTable {

    static int[] logTable;
    static int[][] rmq;

    static void sparseTable(int[] a)
    {
        int n = a.length;

        logTable = new int[n + 1];
        for (int i = 2; i <= n; i++)
            logTable[i] = logTable[i >> 1] + 1;

        rmq = new int[logTable[n] + 1][n];

        for (int i = 0; i < n; ++i)
            rmq[0][i] = i;
      
        for (int k = 1; (1 << k) < n; ++k) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                int x = rmq[k - 1][i];
                int y = rmq[k - 1][i + (1 << k - 1)];
                rmq[k][i] = a[x] <= a[y] ? x : y;
            }
        }
    }

    static int get(int i, int j, int[] a)
    {
        int k = logTable[j - i];
        int x = rmq[k][i];
        int y = rmq[k][j - (1 << k) + 1];
        return a[x] <= a[y] ? x : y;
    }

    public static void main(String[] args) {
      int[] b = { 4, 6, 1, 5, 7, 3 };
      sparseTable(b);

      System.out.println(2 == get(0, 3, b));
    }
    
    static class sparseTable2D{
        
        static int[][][][] table;
        static int[] logTable;
        
        static int merge(int x, int y){
            return Math.max(x, y);
        }
        
        static void preprocess(int[][] arr){
            int n = arr.length;
            int m = arr[0].length;
            int maxmn = Math.max(m, n);
            logTable = new int[maxmn + 1];

            for(int i = 2; i <= maxmn; i++)
                logTable[i] = logTable[i >> 1] + 1;

            table = new int[logTable[n] + 1][n][logTable[m] + 1][m];

            for(int ir = 0; ir < n; ir++){

                for(int ic = 0; ic < m; ic++)
                    table[0][ir][0][ic] = arr[ir][ic];

                for(int jc = 1; (1 << jc) < m; jc++){
                    for(int ic = 0; ic + (1 << jc) <= m; ic++){
                        table[0][ir][jc][ic] = merge(table[0][ir][jc - 1][ic], table[0][ir][jc - 1][ic + (1 << jc - 1)]);
                    }
                }

            }

            for(int jr = 1; (1 << jr) < n; jr++){
                for(int ir = 0; ir + (1 << jr) <= n; ir++){
                    for(int jc = 0; (1 << jc) < m; jc++){
                        for(int ic = 0; ic + (1 << jc) <= m; ic++){
                            table[jr][ir][jc][ic] = merge(table[jr - 1][ir][jc][ic], table[jr - 1][ir + (1 << jr - 1)][jc][ic]);
                        }
                    }
                }
            }

        }
        
        static int get(int x1, int y1, int x2, int y2){
            int kx = logTable[x2 - x1];
            int ky = logTable[y2 - y1];
            
            return merge( merge(table[kx][x1][ky][y1], table[kx][x1][ky][y2 - (1 << ky)  + 1 ]),
                    merge(table[kx][x2 - (1 << kx) + 1][ky][y1], table[kx][x2 - (1 << kx) + 1][ky][y2 - (1 << ky)  + 1 ]));
        }
        
    }
    
}