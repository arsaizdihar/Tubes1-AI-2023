package bot;

import java.util.*;

public class ReservationTree {
    int height;
    TreeNode root;
    Map<Chromosome, Integer> Chromosomes = new HashMap<>();
    // bot.Chromosome

    public ReservationTree() {
        this.root = new TreeNode(null, 0, true, false);
    }

    public void insertChromosome(Chromosome chromosome) {
        // Insert chromosome into the tree
        TreeNode node = this.root;
        int count = 0;
        for (Integer i : chromosome.gene) {
            // If node has a child with the index, then insert at that node and only update the node's value
            // If no, then create a node child with the index and value then insert at that node
            if (node.children.containsKey(i)) {
                // System.out.print("Have\t" + i + "\t");
                // System.out.println(node.value);
                node.isLeaf = false;
            } else {
                if (count == chromosome.gene.size() - 1) {
                    node.addChildren(i, new TreeNode(chromosome.value, node.level+1, !node.isMax, true));
                    node.isLeaf = false;
                } else {
                    node.addChildren(i, new TreeNode(null, node.level+1, !node.isMax, true));
                    node.isLeaf = false;
                }
                this.height = Math.max(node.level+1, this.height);
                // System.out.print("Not have\t" + i + "\t");
                //System.out.println(node.value);
            }
            count += 1;
            node = node.children.get(i);
            // System.out.println("node " + i + " value " + node.value);

        }
        updateTreeValue(root);
        this.Chromosomes.put(chromosome, 1);
    }

    public Integer updateTreeValue(TreeNode node) {
        // To update / propagate the value from the leaf upwards
        List<Integer> values = new ArrayList<>();
        if (node.isLeaf) {
            values.add(node.value);
        }
        for (TreeNode child : node.children.values()) {
            values.add(this.updateTreeValue(child));
        }
        // System.out.println(values);
        node.updateValue(values);
        return node.value;
    }

    private void evaluateFitness() {
        // Evaluate the fitness of each chromosome in the reservation tree
        for (Map.Entry<Chromosome, Integer> entry : Chromosomes.entrySet()) {
            Chromosome chromosome = entry.getKey();

            // Update the value
            entry.setValue(getChromosomeValue(root, chromosome, 0));
        }
    }

    private int getChromosomeValue(TreeNode node, Chromosome chromosome, int i) {
        // Recursive exploration of node, bottom-up, according to chromosome
        // If node is the same value as the chromosome, then +1
        int val = 1;
        if (i < chromosome.gene.size()) {
            if (node.value == chromosome.value) {
                val = 1 + this.getChromosomeValue(node.children.get(chromosome.gene.get(i)), chromosome, i + 1);
            } else {
                val = this.getChromosomeValue(node.children.get(chromosome.gene.get(i)), chromosome, i + 1);
            }
        }
        return val;
    }
    public List<Map.Entry<Chromosome, Integer>> getNTopValues(int n) {
        // Return the top valued N chromosome
        this.evaluateFitness();

        if (n > this.Chromosomes.size()) {
            n = this.Chromosomes.size();
        }

        // Custom comparator
        Comparator<Map.Entry<Chromosome, Integer>> valueComparator = (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue());

        // Convert map entries to a list
        List<Map.Entry<Chromosome, Integer>> entryList = new ArrayList<>(Chromosomes.entrySet());

        // Sort list using custom comparator
        entryList.sort(valueComparator);

        // Take top n entries
        List<Map.Entry<Chromosome, Integer>> topNEntries = entryList.subList(0, Math.min(n, entryList.size()));

        // Extract keys (objects) from top N entries
        List<Map.Entry<Chromosome, Integer>> topNObjects = new ArrayList<>();
        for (Map.Entry<Chromosome, Integer> entry : topNEntries) {
            topNObjects.add(entry);
        }
        return topNObjects;
    }

    public Chromosome selectRandomWithProbability(int n) {
        // Select a random chromosome with probability
        double total = 0;
        Map<Chromosome, Double> probabilityMap = new HashMap<>();
        for (Map.Entry<Chromosome, Integer> entry : this.getNTopValues(n)) {
            total += entry.getValue();
        }
        // Value/Probability mapping
        for (Map.Entry<Chromosome, Integer> entry : this.Chromosomes.entrySet()) {
            probabilityMap.put(entry.getKey(), ((double) entry.getValue()) / total);
        }


        double totalProbability = 0.0;
        double randomValue = new Random().nextDouble();

        for (Map.Entry<Chromosome, Double> entry : probabilityMap.entrySet()) {
            totalProbability += entry.getValue();
            if (randomValue <= totalProbability) {
                return entry.getKey();
            }
        }

        // If no element selected (due to rounding errors), return last element
        return null;
    }

    public void print(TreeNode node) {
        System.out.println(node.level + " " + node.value);
        for (TreeNode child : node.children.values()) {
            this.print(child);
        }
    }

    public static void main(String[] args) {
        ReservationTree rt = new ReservationTree();
        List<Integer> genes = new ArrayList<>();
        genes.add(1);
        genes.add(2);
        genes.add(3);
        Chromosome c = new Chromosome(genes, 'X');
        rt.insertChromosome(c);
        genes.remove(2);
        genes.add(10);
        Chromosome d = new Chromosome(genes, 'X');
        rt.insertChromosome(d);
        List<Integer> genes2 = new ArrayList<>();
        genes2.add(1);
        genes2.add(4);
        genes2.add(5);
        Chromosome e = new Chromosome(genes2, 'X');
        rt.insertChromosome(e);

        rt.print(rt.root);
        System.out.println(rt.getNTopValues(3));
        System.out.println(rt.selectRandomWithProbability(3));
    }


}
