package it.diegorigo.palio;

import it.diegorigo.exceptions.UtilityException;
import it.diegorigo.graph.Edge;
import it.diegorigo.graph.GraphUtils;
import it.diegorigo.random.RandomUtils;
import it.diegorigo.strings.StringUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PalioUtils {
    public static final int TEAMS = 12;
    public static final String DELIMITER = " | ";
    public static final int MAX_ATTEMPTS = 100000;
    public static final int MAX_STEPS = 3;
    private static final List<String> NODES;
    private static final List<Edge> EDGES;

    static {
        String IMPIANTI = "Impianti";
        String PONTE = "Ponte";
        String FARMACIA = "Farmacia";
        String PARROCCHIA = "Parrocchia";
        String BAITA = "Baita";
        String TONIDANDEL = "Tonidandel";
        //
        //        NODES = List.of(IMPIANTI, PONTE, FARMACIA, PARROCCHIA, BAITA);
        //        EDGES = List.of(new Edge(IMPIANTI, PONTE),
        //                        new Edge(PONTE, FARMACIA),
        //                        new Edge(FARMACIA, PARROCCHIA),
        //                        new Edge(PARROCCHIA, BAITA),
        //                        new Edge(PONTE, PARROCCHIA));

        NODES = List.of(IMPIANTI, PONTE, FARMACIA, PARROCCHIA, BAITA, TONIDANDEL);
        EDGES = List.of(new Edge(IMPIANTI, TONIDANDEL),
                        new Edge(TONIDANDEL, PONTE),
                        new Edge(PONTE, FARMACIA),
                        new Edge(FARMACIA, PARROCCHIA),
                        new Edge(PARROCCHIA, BAITA),
                        new Edge(PONTE, PARROCCHIA));
    }

    public static void main(String[] args) throws UtilityException {
        checkFeasibility();
        List<String[][]> matrices = getSolutions();
        printSolutions(matrices);
    }

    private static void printSolutions(List<String[][]> matrices) {
        System.out.println(toTitle("INFO GLOBALI"));
        System.out.println("Numero squadre: " + TEAMS);
        System.out.println("Numero postazioni: " + getNodeList().size());
        System.out.println("Trovate " + matrices.size() + " soluzioni in " + MAX_ATTEMPTS + " iterazioni");
        System.out.println("Percentuale di successo: " + Math.ceil(((double) matrices.size()) / MAX_ATTEMPTS * 100) + "%");

        // Trovo la migliore soluzione in base ad un punteggio
        String[][] bestSolution = getBestSolution(matrices);
        Map<String, Set<String>> opponentsMap = getOpponentsMap(bestSolution);
        List<String> teams = new ArrayList<>(opponentsMap.keySet());
        teams.sort(Comparator.comparing(item -> Integer.valueOf(item.replace("Squadra ",""))));
        teams.forEach(k -> System.out.println(k + " -> SFIDA: " + String.join(", ",
                                                                              opponentsMap.get(k))));
        System.out.println(toTitle("MIGLIOR SOLUZIONE"));
        display(bestSolution);
    }

    private static String toTitle(String text) {
        String row = "############################################";
        return "\n" + row + "\n# " + text + "\n" + row;
    }

    private static void checkFeasibility() throws UtilityException {
        if (TEAMS > NODES.size() * 2) {
            throw new UtilityException(
                    "Troppe squadre rispetto alle postazioni. Aggiungere postazione");
        }
    }

    private static List<String[][]> getSolutions() {

        Graph<String, DefaultEdge> graph = GraphUtils.createStringGraph(NODES, EDGES);
        Map<String, Double> distances = GraphUtils.getDistances(graph, NODES);

        List<String[][]> matrices = new ArrayList<>();
        int i = 1;
        while (i <= MAX_ATTEMPTS) {
            String[][] matrix = new String[NODES.size()][TEAMS];
            if (i % 200 == 0) {
                System.out.println("Tentativo " + i + "/" + MAX_ATTEMPTS);
            }
            try {
                solve(matrix, distances);
                matrices.add(matrix);
            } catch (UtilityException e) {
                i++;
            }
        }
        return matrices;
    }

    private static String[][] getBestSolution(List<String[][]> matrices) {
        int score = 0;
        String[][] bestMatrix = new String[0][0];

        for (String[][] m : matrices) {
            int matrixScore = getMatrixScore(m);
            if (matrixScore > score) {
                score = matrixScore;
                bestMatrix = m;
            }
        }
        return bestMatrix;
    }

    private static int getMatrixScore(String[][] matrix) {
        int score = 0;
        // più punteggio alle combinazioni in cui ci le squadre non giocano da sole
        //        for (int i = 0; i < matrix.length; i++) {
        //            score += getRowNodesDuplicated(matrix, i).size();
        //        }

        // Creo mappa per ogni squadra, insieme di altre squadre avversarie con cui si sfida
        Map<String, Set<String>> opponentsMap = getOpponentsMap(matrix);

        // più punteggio alle combinazioni in cui ogni squadra non gioca 2 volte con la stessa squadra
        for (String team : opponentsMap.keySet()) {
            score += opponentsMap.get(team).size() * 10;
        }

        return score;
    }

    // Creo mappa per ogni postazione, per ogni turno, lista squadre presenti in quella postazione
    private static Map<String, Map<String, List<String>>> getDayMap(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        List<String> roundNames = getRoundNames();
        List<String> nodeList = getNodeList();
        List<String> teamNames = getTeamNames();

        nodeList.forEach(item -> {
            Map<String, List<String>> rounds = new HashMap<>();
            roundNames.forEach(r -> rounds.put(r, new ArrayList<>()));
            map.put(item, rounds);
        });


        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = 0; j < TEAMS; j++) {
                String position = matrix[i][j];
                String round = roundNames.get(i);
                String team = teamNames.get(j);
                map.get(position).get(round).add(team);
            }
        }
        return map;
    }

    private static Map<String, Set<String>> getOpponentsMap(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = getDayMap(matrix);
        List<String> teamNames = getTeamNames();
        Map<String, Set<String>> opponentsMap = new HashMap<>();
        teamNames.forEach(item -> opponentsMap.put(item, new HashSet<>()));

        map.forEach((position, roundsMap) -> roundsMap.forEach((round, teams) -> teams.forEach(team -> {
            List<String> otherTeams = new ArrayList<>(teams);
            otherTeams.remove(team);
            opponentsMap.get(team).addAll(otherTeams);
        })));
        return opponentsMap;
    }

    private static void solve(String[][] matrix,
                              Map<String, Double> distances) throws UtilityException {
        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
                List<String> availableNodes = getNodeList();

                // Rimuovo nodi già visitati in precedenza
                List<String> colNodes = getColumnInsertedNodes(matrix, colIndex);
                availableNodes.removeAll(colNodes);

                // Rimuovo nodi con già 2 squadre associate in quel turno
                List<String> rowNode = getRowNodesDuplicated(matrix, rowIndex);
                availableNodes.removeAll(rowNode);

                // Rimuovo nodi troppo distanti
                String previousPosition = getPreviousPosition(matrix, rowIndex, colIndex);
                if (previousPosition != null) {
                    availableNodes.removeIf(item -> getDistance(item,
                                                                previousPosition,
                                                                distances) > MAX_STEPS);
                }

                String randomElement = RandomUtils.getRandomElement(availableNodes);
                if (randomElement == null) {
                    throw new UtilityException("Matrice non risolvibile");
                }
                matrix[rowIndex][colIndex] = randomElement;
            }
        }
    }

    private static void display(String[][] matrix) {
        int textSize = 10;
        String emptyString = StringUtils.emptyString(textSize);
        StringBuilder display = new StringBuilder();

        List<String> colNames = getTeamNames().stream()
                                              .map(item -> StringUtils.leftPadSpaces(item,
                                                                                     textSize))
                                              .toList();
        List<String> firstRow = new ArrayList<>();
        firstRow.add(emptyString);
        firstRow.addAll(colNames);
        display.append(String.join(DELIMITER, firstRow)).append("\n");


        List<String> rowNames = getRoundNames().stream()
                                               .map(item -> StringUtils.leftPadSpaces(item,
                                                                                      textSize))
                                               .toList();
        for (int i = 0; i < matrix.length; i++) {
            List<String> rowValues = Stream.of(matrix[i])
                                           .map(item -> item == null ? emptyString : StringUtils.leftPadSpaces(
                                                   item.substring(0,
                                                                  Math.min(textSize,
                                                                           item.length())),
                                                   textSize))
                                           .toList();
            List<String> row = new ArrayList<>();
            row.add(rowNames.get(i));
            row.addAll(rowValues);
            display.append(String.join(DELIMITER, row)).append("\n");
        }
        System.out.println(display);
    }

    private static List<String> getRoundNames() {
        return IntStream.range(0, getNodeList().size()).mapToObj(i -> "Turno " + (i + 1)).toList();
    }

    private static List<String> getTeamNames() {
        return IntStream.range(0, TEAMS).mapToObj(i -> "Squadra " + (i + 1)).toList();
    }

    private static double getDistance(String node1,
                                      String node2,
                                      Map<String, Double> distances) {
        return distances.get(GraphUtils.getCode(node1, node2));
    }

    private static String getPreviousPosition(String[][] matrix,
                                              int rowIndex,
                                              int colIndex) {
        if (rowIndex > 0) {
            return matrix[rowIndex - 1][colIndex];
        } else {
            return null;
        }
    }

    private static List<String> getRowNodesDuplicated(String[][] matrix,
                                                      int rowIndex) {
        List<String> list = new ArrayList<>();
        List<String> duplicatedList = new ArrayList<>();
        int n = matrix[0].length;
        for (int i = 0; i < n; i++) {
            String value = matrix[rowIndex][i];
            if (StringUtils.isNotEmpty(value)) {
                if (list.contains(value)) {
                    duplicatedList.add(value);
                } else {
                    list.add(value);
                }
            }
        }
        return duplicatedList;

    }

    private static List<String> getColumnInsertedNodes(String[][] matrix,
                                                       int colIndex) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            String value = matrix[i][colIndex];
            if (StringUtils.isNotEmpty(value)) {
                list.add(value);
            }
        }
        return list;
    }

    private static List<String> getNodeList() {
        return new ArrayList<>(NODES);
    }
}
