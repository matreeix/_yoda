package algorithm.IA.SA;

import java.util.Random;

/**
 * @Description: 给定一个 N 边形所有顶点坐标 x,y ，求其费马点到所有顶点距离和。费马点是指到多边形所有顶点距离和最小的点。
 * @Date: 2021/9/5
 */

public class SimulateAnnealDemo {
    private int N = 11, M = 1010, RAND_MAX = Integer.MAX_VALUE;
    private double delta = 0.996, Te = 1e-10;// D和最终温度
    private int n, m, x[] = new int[N], y[] = new int[N], r[] = new int[N], p[] = new int[M], q[] = new int[M], R, ansout = 0;
    private double ansx, ansy;

    private double dis(double ax, double ay, double bx, double by) {// 求两点间距离
        double res = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
        res = Math.sqrt(res);
        return res;
    }

    private int calc(double xx, double yy) { // 估价函数
        double nowr = R;
        for (int i = 1; i <= n; i++)
            nowr = Math.min(nowr, dis(xx, yy, x[i], y[i]) - (double) r[i]);
        int cnt = 0;
        for (int i = 1; i <= m; i++)
            if (dis(xx, yy, p[i], q[i]) <= nowr)
                cnt++;
        return cnt;
    }

    public void SA() {
        double T = 6000;//初始温度
        int ans = 0;
        Random r = new Random();
        while (T > Te) {
            double nowx = ansx + (r.nextInt() * 2 - RAND_MAX) * T;
            double nowy = ansy + (r.nextInt() * 2 - RAND_MAX) * T;
            int nowans = calc(nowx, nowy);
            int DelE = nowans - ans;
            if (DelE > 0) {//更优的解，必然接受
                ansx = nowx;
                ansy = nowy;
                ans = nowans;
                ansout = Math.max(ansout, ans);
            } else if (Math.exp(DelE / Math.sqrt(T)) > (double) r.nextInt() / 1215) { //按概率接受，保证了越到后期，和最优解的差距越大，越难被接受
                ansx = nowx;
                ansy = nowy;
                ans = nowans;
            }
            T *= delta;
        }
    }

}
