package bot;

import java.util.*;

public class TreeNode {
    Integer value;
    Integer level;

    boolean isMax;

    Map<Integer, TreeNode> children;
    TreeNode parent;

    public boolean isLeaf = true;

    public TreeNode(Integer value, Integer level, boolean isMax, boolean isLeaf) {
        this.value = value;
        this.level = level;
        this.children = new HashMap<>();
        this.parent = null;
        this.isMax = isMax;
        this.isLeaf = isLeaf;
    }

    public void addChildren(Integer index, TreeNode child) {
        this.children.put(index, child);
    }

    public void updateValue(Integer value) {
        // Update node value, according to isMax
        if (this.value == null) {
            this.value = value;
            return;
        }
        if (this.isMax) {
            this.value = Math.max(this.value, value);
        } else {
            this.value = Math.min(this.value, value);
        }
    }

    public void updateValue(List<Integer> values) {
        // Update node value, according to isMax, get the min/max of a list
        if (this.isMax) {
            this.value = Collections.max(values);
        } else {
            this.value = Collections.min(values);
        }
    }
}
