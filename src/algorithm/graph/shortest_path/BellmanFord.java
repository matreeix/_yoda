package algorithm.graph.shortest_path;

import algorithm.graph.WeightedGraph;

import java.util.Arrays;

/**
 * Bellman-Ford算法，时间复杂度O（V*E）
 * 当存在负权环时，没有最短路径
 */
public class BellmanFord {

    private WeightedGraph G;
    private int s;
    private int[] dis;
    private boolean hasNegCycle = false;

    public BellmanFord(WeightedGraph G, int s) {

        this.G = G;

        G.validateVertex(s);
        this.s = s;

        dis = new int[G.V()];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[s] = 0;

        for (int pass = 1; pass < G.V(); pass++) {//进行v-1轮的松弛操作
            for (int v = 0; v < G.V(); v++)
                for (int w : G.adj(v))
                    if (dis[v] != Integer.MAX_VALUE &&//防止权值溢出
                            dis[v] + G.getWeight(v, w) < dis[w])
                        dis[w] = dis[v] + G.getWeight(v, w);
        }

        for (int v = 0; v < G.V(); v++)//多进行一轮松弛操作，若权值变化就存在负权环
            for (int w : G.adj(v))
                if (dis[v] != Integer.MAX_VALUE
                        && dis[v] + G.getWeight(v, w) < dis[w])
                    hasNegCycle = true;
    }

    public boolean hasNegativeCycle() {
        return hasNegCycle;
    }

    public boolean isConnectedTo(int v) {
        G.validateVertex(v);
        return dis[v] != Integer.MAX_VALUE;
    }

    public int distTo(int v) {
        G.validateVertex(v);
        if (hasNegCycle) throw new RuntimeException("exist negative cycle.");
        return dis[v];
    }

    static public void main(String[] args) {

        WeightedGraph g = new WeightedGraph("C:\\Users\\daito\\ideaproject\\justforfun\\src\\_courses.imooc\\Graph_Algorithms\\Shortest_Path\\BellmanFordAlgorithm\\g.txt");
        BellmanFord bf = new BellmanFord(g, 0);
        if (!bf.hasNegativeCycle()) {
            for (int v = 0; v < g.V(); v++)
                System.out.print(bf.distTo(v) + " ");
            System.out.println();
        } else
            System.out.println("exist negative cycle.");

        WeightedGraph g2 = new WeightedGraph("C:\\Users\\daito\\ideaproject\\justforfun\\src\\_courses.imooc\\Graph_Algorithms\\Shortest_Path\\BellmanFordAlgorithm\\g2.txt");
        BellmanFord bf2 = new BellmanFord(g2, 0);
        if (!bf2.hasNegativeCycle()) {
            for (int v = 0; v < g2.V(); v++)
                System.out.print(bf2.distTo(v) + " ");
            System.out.println();
        } else
            System.out.println("exist negative cycle.");
    }
}
