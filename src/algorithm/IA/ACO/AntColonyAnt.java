package algorithm.IA.ACO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 蚂蚁类 - 表示蚁群算法中的单个蚂蚁
 */
public class AntColonyAnt {
    private int currentCity;              // 当前所在城市
    private boolean[] visited;            // 已访问城市标记数组
    private List<Integer> tour;           // 路径记录
    private double tourLength;            // 路径长度
    private Random random = new Random();

    public AntColonyAnt(int numCities) {
        this.visited = new boolean[numCities];
        this.tour = new ArrayList<>();
        this.tourLength = 0.0;
        reset();
    }

    /**
     * 重置蚂蚁状态
     */
    public void reset() {
        tour.clear();
        tourLength = 0.0;
        Arrays.fill(visited, false);
        // 随机选择起始城市
        currentCity = random.nextInt(visited.length);
        visited[currentCity] = true;
        tour.add(currentCity);
    }

    /**
     * 选择下一个城市
     */
    public int selectNextCity(double[][] pheromoneMatrix, double[][] distanceMatrix,
                             double alpha, double beta) {
        double[] probabilities = calculateProbabilities(pheromoneMatrix, distanceMatrix, alpha, beta);

        // 使用轮盘赌选择
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int city = 0; city < probabilities.length; city++) {
            if (!visited[city]) {
                cumulativeProbability += probabilities[city];
                if (randomValue <= cumulativeProbability) {
                    return city;
                }
            }
        }

        // 如果由于浮点精度问题没有选择到城市，返回第一个未访问的城市
        for (int city = 0; city < visited.length; city++) {
            if (!visited[city]) {
                return city;
            }
        }

        return -1; // 不应该到达这里
    }

    /**
     * 计算选择每个城市的概率
     */
    private double[] calculateProbabilities(double[][] pheromoneMatrix, double[][] distanceMatrix,
                                          double alpha, double beta) {
        double[] probabilities = new double[visited.length];
        double totalProbability = 0.0;

        // 计算每个未访问城市的吸引力
        for (int city = 0; city < visited.length; city++) {
            if (!visited[city]) {
                double pheromone = Math.pow(pheromoneMatrix[currentCity][city], alpha);
                double distance = distanceMatrix[currentCity][city];
                double heuristic = distance > 0 ? 1.0 / distance : 0.0;
                double attractiveness = pheromone * Math.pow(heuristic, beta);
                probabilities[city] = attractiveness;
                totalProbability += attractiveness;
            }
        }

        // 归一化概率
        if (totalProbability > 0) {
            for (int city = 0; city < probabilities.length; city++) {
                probabilities[city] /= totalProbability;
            }
        }

        return probabilities;
    }

    /**
     * 移动到指定城市
     */
    public void moveToCity(int city, double[][] distanceMatrix) {
        double distance = distanceMatrix[currentCity][city];
        tourLength += distance;
        currentCity = city;
        visited[city] = true;
        tour.add(city);
    }

    /**
     * 返回起始城市完成路径
     */
    public void returnToStart(double[][] distanceMatrix) {
        int startCity = tour.get(0);
        double distance = distanceMatrix[currentCity][startCity];
        tourLength += distance;
        tour.add(startCity);
    }

    // Getters
    public List<Integer> getTour() {
        return new ArrayList<>(tour);
    }

    public double getTourLength() {
        return tourLength;
    }

    public boolean[] getVisited() {
        return Arrays.copyOf(visited, visited.length);
    }

    public int getCurrentCity() {
        return currentCity;
    }
}
