package bot;

import board.Board;
import board.BoardChange;
import javafx.util.Pair;

import java.util.*;

public class BotGenetic extends Bot implements Fallbackable {

    private final int limitGeneration = 500;

    private final int gameTreeDepth;

    private int crossoverRate = 2;

    private int mutationRate = 4;

    private char ownSymbol;

    private BoardChange boardChange;

    private final int nGenerate = 500;

    private final int nThreshold = 20;

    protected ReservationTree rt = new ReservationTree();

    protected List<Integer> availIdx = new ArrayList<>();

    public BotGenetic(Board board, boolean first, int nRound) {
        super(board);
        this.boardChange = new BoardChange();
        this.ownSymbol = first ? 'X' : 'O';
        this.gameTreeDepth = nRound;

        for (int i = 0; i < getBoard().getRowCount() * getBoard().getColCount(); i++) {
            availIdx.add(i);
        }
    }


    @Override
    public int[] move(int Xscore, int Oscore) {
        return generate();
    }

    protected int[] generate() {
        // From the current state, generate N chromosome
        // Need the first part (history), then the second part (future)
        // Concat (for processing) then divide again

        // Coding scheme is indexing the board square (x = 0, y = 0) -> 0

        // remove all filled indexes
        for (int i = 0; i < this.getBoard().getRowCount(); i++) {
            for (int j = 0; j < this.getBoard().getColCount(); j++) {
                if (this.getBoard().getCol(j, i) != 'n') {
                    int finalI = i;
                    int finalJ = j;
                    availIdx.removeIf(el -> el.equals(finalJ + finalI * this.getBoard().getColCount()));
                }
            }
        }


        // history
        List<Integer> history = new ArrayList<>();
        int gene;
        for (int[] pair : boardChange.getLocations()) {
            gene = pair[0] + pair[1] * getBoard().getColCount();
            history.add(gene);
            int finalGene = gene;
            availIdx.removeIf(el -> el.equals(finalGene));
        }

        // Generate random genes
        List<List<Integer>> nRandom = new ArrayList<>();

        for (int i = 0; i < nGenerate; i++) {
            List<Integer> generated = new ArrayList<>();
            Random random = new Random();
            for (int j = 0; j < gameTreeDepth - history.size(); j++) {
                int chosenIdx = random.nextInt(availIdx.size());
                int element = availIdx.get(chosenIdx);
                generated.add(element);
                availIdx.removeIf(e -> e.equals(element));
            }
            availIdx.addAll(generated);
            nRandom.add(generated);
        }

        // remove all filled indexes
        for (int i = 0; i < this.getBoard().getRowCount(); i++) {
            for (int j = 0; j < this.getBoard().getColCount(); j++) {
                if (this.getBoard().getCol(j, i) != 'n') {
                    int finalI = i;
                    int finalJ = j;
                    availIdx.removeIf(el -> el.equals(finalJ + finalI * this.getBoard().getColCount()));
                }
            }
        }


        // Concatenate history with current
        List<List<Integer>> genes = new ArrayList<>();

        for (List<Integer> i : nRandom) {
            List<Integer> concatenated = new ArrayList<>();
            concatenated.addAll(history);
            concatenated.addAll(i);
            genes.add(concatenated);
        }

        // Reservation tree


        for (List<Integer> i : genes) {
            rt.insertChromosome(new Chromosome(i, this.ownSymbol));
        }

        for (int x = 0; x < this.limitGeneration; x++) {
            if (Thread.currentThread().isInterrupted()) {
                return new int[2];
            }
            // Crossover and mutation
            List<Chromosome> selected = new ArrayList<>();
            List<List<Integer>> subGenes = new ArrayList<>();
            List<List<Integer>> newGenes = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                // Select 4 random chromosomes with probability
                selected.add(rt.selectRandomWithProbability(this.nThreshold));
                // Crossover and mutation index is n history + some number, max n_rounds
                // Select back-side of crossover
                subGenes.add(new ArrayList<>(selected.get(i).gene.subList(Math.min(history.size() - 1 + crossoverRate, gameTreeDepth-1), selected.get(i).gene.size())));
                // Crossover
                newGenes.add(new ArrayList<>(selected.get(i).gene.subList(0, Math.min(history.size() - 1 + crossoverRate, gameTreeDepth-1))));
            }

            // Crossover swap
            newGenes.get(0).addAll(subGenes.get(1));
            newGenes.get(1).addAll(subGenes.get(0));
            newGenes.get(2).addAll(subGenes.get(3));
            newGenes.get(3).addAll(subGenes.get(2));


            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                // Mutation
                availIdx.removeAll(newGenes.get(i));
                List<Integer> removedIdx = new ArrayList<>(newGenes.get(i));
                newGenes.get(i).set(Math.min(history.size() - 1 + mutationRate, newGenes.get(i).size()-1), availIdx.get(random.nextInt(availIdx.size())));
                availIdx.addAll(removedIdx);
            }

            // Offspring
            List<Chromosome> offsprings = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                offsprings.add(new Chromosome(newGenes.get(i), this.ownSymbol));
                rt.insertChromosome(offsprings.get(i));
            }


        }

        int[] move = new int[2];
        int i = 0;
        do {
            Chromosome result = rt.getNTopValues(15).get(i).getKey();
            Pair<Integer, Integer> coord = result.translate(result.gene.get(i));
            move[0] = coord.getKey();
            move[1] = coord.getValue();
            i += 1;
        } while (!availIdx.contains(move[0] + move[1] * this.getBoard().getColCount()));

        rt = new ReservationTree();
        return move;
    }


    @Override
    public int[] fallback(int Xscore, int Oscore) {
        System.out.println("FALLBACK");
        int[] move = new int[2];
        int i = 0;
        do {
            Chromosome result = rt.getNTopValues(15).get(i).getKey();
            Pair<Integer, Integer> coord = result.translate(result.gene.get(i));
            move[0] = coord.getKey();
            move[1] = coord.getValue();
            i += 1;
        } while (!availIdx.contains(move[0] + move[1] * this.getBoard().getColCount()));
        rt = new ReservationTree();
        return move;
    }
}