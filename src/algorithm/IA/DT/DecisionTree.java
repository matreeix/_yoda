package algorithm.IA.DT;

/**
 * 简化的决策树实现 (ID3算法)
 */
public class DecisionTree {
    static class Node {
        String attribute;
        String value;
        String label;
        Node[] children;

        Node(String attribute) {
            this.attribute = attribute;
        }

        Node(String value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    private Node root;

    /**
     * 训练决策树
     */
    public void train(String[][] data, String[] attributes, String targetAttribute) {
        root = buildTree(data, attributes, targetAttribute);
    }

    /**
     * 递归构建决策树
     */
    private Node buildTree(String[][] data, String[] attributes, String targetAttribute) {
        // 如果所有样本属于同一类，返回叶节点
        String commonLabel = getCommonLabel(data, targetAttribute);
        if (commonLabel != null) {
            return new Node(null, commonLabel);
        }

        // 如果没有属性可用，返回多数类
        if (attributes.length == 0) {
            String majorityLabel = getMajorityLabel(data, targetAttribute);
            return new Node(null, majorityLabel);
        }

        // 选择最佳分割属性
        String bestAttribute = selectBestAttribute(data, attributes, targetAttribute);

        // 创建节点
        Node node = new Node(bestAttribute);
        String[] values = getAttributeValues(data, bestAttribute);
        node.children = new Node[values.length];

        // 递归构建子树
        for (int i = 0; i < values.length; i++) {
            String[][] subset = getSubset(data, bestAttribute, values[i]);
            String[] remainingAttributes = removeAttribute(attributes, bestAttribute);

            if (subset.length == 0) {
                String majorityLabel = getMajorityLabel(data, targetAttribute);
                node.children[i] = new Node(values[i], majorityLabel);
            } else {
                node.children[i] = new Node(values[i], null);
                node.children[i].children = new Node[]{buildTree(subset, remainingAttributes, targetAttribute)};
            }
        }

        return node;
    }

    /**
     * 预测单个样本
     */
    public String predict(String[] sample, String[] attributes) {
        return predictSample(sample, attributes, root);
    }

    private String predictSample(String[] sample, String[] attributes, Node node) {
        if (node.label != null) {
            return node.label;
        }

        int attrIndex = getAttributeIndex(attributes, node.attribute);
        String value = sample[attrIndex];

        for (Node child : node.children) {
            if (child.value.equals(value)) {
                return predictSample(sample, attributes, child.children[0]);
            }
        }

        return null;
    }

    // 辅助方法
    private String getCommonLabel(String[][] data, String targetAttribute) {
        // 简化的实现
        return null;
    }

    private String getMajorityLabel(String[][] data, String targetAttribute) {
        // 简化的实现
        return "Yes";
    }

    private String selectBestAttribute(String[][] data, String[] attributes, String targetAttribute) {
        return attributes[0]; // 简化为选择第一个属性
    }

    private String[] getAttributeValues(String[][] data, String attribute) {
        return new String[]{"Yes", "No"}; // 简化的实现
    }

    private String[][] getSubset(String[][] data, String attribute, String value) {
        return data; // 简化的实现
    }

    private String[] removeAttribute(String[] attributes, String attribute) {
        return attributes; // 简化的实现
    }

    private int getAttributeIndex(String[] attributes, String attribute) {
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].equals(attribute)) {
                return i;
            }
        }
        return -1;
    }
}
