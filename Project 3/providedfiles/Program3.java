import java.lang.*;
public class Program3 {

    public int maxCoinValue(int[] cities) {
        // Implement your dynamic programming algorithm here
        // You may use helper functions as needed
        int N = cities.length;
        int[][] OPT = new int[N][N];
        int[][] Cuts = new int[N][N];

        // PreProcessing
        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                if (i == j) {
                    Cuts[i][j] = cities[j];
                } else {
                    Cuts[i][j] = cities[j] + Cuts[i][j - 1];
                }
            }
        }

        for (int i = N - 1; i >= 0; i--) {
            for (int j = i; j < N; j++) {
                if (i == j) {//Base case 3 coins
                    OPT[i][j] = 0;
                } else if (j - i == 1) { //Base case 2 coins
                    OPT[i][j] = Math.min(cities[i], cities[j]);
                } else { //recurrence case
                    int high = j;
                    int low = i;
                    int mid;
                    int midVal;
                    int midPlus1 = 0;
                    int midMinus1 = 0;

                    while (true) {
                        //mid value
                        mid = (high + low) / 2;

                        if(high-low == 1){
                            OPT[i][j] = Math.min(OPT[i][mid] + Cuts[i][mid],OPT[mid + 1][j] + Cuts[mid + 1][j]);
                            break;
                        }

                        if(high-low == 2){
                            int one = Math.min((OPT[i][i] + Cuts[i][i]),OPT[mid][j]+Cuts[mid][j]);
                            int a = (OPT[i][mid] + Cuts[i][mid]);
                            int b = (OPT[mid+1][j] + Cuts[mid+1][j]);
                            int two = Math.min(a,b);
                            OPT[i][j] = Math.max(one,two);
                            break;
                        }

                        midVal = Math.min(OPT[i][mid] + Cuts[i][mid],OPT[mid + 1][j] + Cuts[mid + 1][j]);
                        //mid + 1 val
                        midPlus1 = Math.min(OPT[i][mid + 1] + Cuts[i][mid + 1],OPT[mid + 2][j] + Cuts[mid + 2][j]);
                        //mid -1 val
                        midMinus1=Math.min(OPT[i][mid - 1] + Cuts[i][mid - 1],OPT[mid][j] + Cuts[mid][j]);

                        if (midVal >= midMinus1 && midVal >= midPlus1) {
                            OPT[i][j] = midVal;
                            break;
                        } else if (midVal > midMinus1) {
                            low = mid;
                        } else {
                            high = mid;
                        }
                    }
                }
            }
        }
        return OPT[0][N - 1];
    }
}



/*
n^3

int max = -1;
                for(int k = i;k<j;k++){
                    int min;
                    int left = OPT[i][k] + Cuts[i][k];
                    int right = OPT[k + 1][j] + Cuts[k + 1][j];
                    if(left>right)
                        min= right;
                    else
                        min = left;
                    if(min>max)
                        max = min;
                }
                OPT[i][j] = max;

 */

