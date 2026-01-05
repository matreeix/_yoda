package algorithm.base.greedy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 活动选择问题 (Activity Selection Problem)
 * 贪心策略：总是选择结束时间最早的活动
 * 时间复杂度：O(n log n) 主要来自于排序
 */
public class ActivitySelection {

    static class Activity {
        int start;
        int end;
        String name;

        Activity(int start, int end, String name) {
            this.start = start;
            this.end = end;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + "(" + start + "-" + end + ")";
        }
    }

    /**
     * 贪心选择活动
     * @param activities 活动数组
     * @return 选择的活动列表
     */
    public static Activity[] selectActivities(Activity[] activities) {
        if (activities == null || activities.length == 0) {
            return new Activity[0];
        }

        // 按结束时间排序
        Arrays.sort(activities, Comparator.comparingInt(a -> a.end));

        // 选择第一个活动（结束最早的）
        java.util.List<Activity> selected = new java.util.ArrayList<>();
        selected.add(activities[0]);

        // 从第二个活动开始，选择与已选活动不冲突的活动
        int lastEndTime = activities[0].end;
        for (int i = 1; i < activities.length; i++) {
            if (activities[i].start >= lastEndTime) {
                selected.add(activities[i]);
                lastEndTime = activities[i].end;
            }
        }

        return selected.toArray(new Activity[0]);
    }

    public static void main(String[] args) {
        // 测试用例
        Activity[] activities = {
            new Activity(1, 3, "A1"),
            new Activity(2, 5, "A2"),
            new Activity(3, 6, "A3"),
            new Activity(4, 8, "A4"),
            new Activity(5, 7, "A5"),
            new Activity(6, 9, "A6")
        };

        System.out.println("所有活动：");
        for (Activity activity : activities) {
            System.out.println(activity);
        }

        Activity[] selected = selectActivities(activities);
        System.out.println("\n选择的活动：");
        for (Activity activity : selected) {
            System.out.println(activity);
        }
        System.out.println("总共选择了 " + selected.length + " 个活动");
    }
}
