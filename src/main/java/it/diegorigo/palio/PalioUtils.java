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
    public static final List<String> TEAMS_LIST;
    public static final String DELIMITER = " | ";
    public static final int MAX_ATTEMPTS = 1000000;
    public static final int MAX_STEPS = 3;
    private static final List<String> NODES;
    private static final List<Edge> EDGES;
    private static final List<String> ROUNDS;

    static {
        String IMPIANTI = "Impianti";
        String PONTE = "Ponte";
        String FARMACIA = "Farmacia";
        String PARROCCHIA = "Parrocchia";
        String BAITA = "Baita";
        //        String TONIDANDEL = "Tonidandel";

        NODES = List.of(FARMACIA, PARROCCHIA,IMPIANTI, BAITA, PONTE);
        EDGES = List.of(new Edge(IMPIANTI, PONTE),
                        new Edge(PONTE, FARMACIA),
                        new Edge(FARMACIA, PARROCCHIA),
                        new Edge(PARROCCHIA, BAITA),
                        new Edge(PONTE, PARROCCHIA));

        List<String> TEAM_NAMES = List.of("Masera",
                                          "Alpini",
                                          "Baracchini",
                                          "ObeyoTasi",
                                          "SOS straco",
                                          "Gruppo Fanti",
                                          "Li vuoi quei kiwi");

        TEAMS_LIST = new ArrayList<>();
        IntStream.range(0, NODES.size() * 2 - TEAM_NAMES.size())
                 .forEach(i -> TEAMS_LIST.add("Fantasma" + (i + 1)));
        //        IntStream.range(0, 7).forEach(i -> TEAMS_LIST.add("Squadra" + (i + 1)));
        TEAMS_LIST.addAll(TEAM_NAMES);

        //        NODES = List.of(IMPIANTI, PONTE, FARMACIA, PARROCCHIA, BAITA, TONIDANDEL);
        //        EDGES = List.of(new Edge(IMPIANTI, TONIDANDEL),
        //                        new Edge(TONIDANDEL, PONTE),
        //                        new Edge(PONTE, FARMACIA),
        //                        new Edge(FARMACIA, PARROCCHIA),
        //                        new Edge(PARROCCHIA, BAITA),
        //                        new Edge(PONTE, PARROCCHIA));

        ROUNDS = IntStream.range(0, getNodeList().size())
                          .mapToObj(i -> "Turno " + (i + 1))
                          .toList();
    }

    public static void main(String[] args) throws UtilityException {
        checkFeasibility();
        List<String[][]> matrices = getSolutions();
        if (matrices.isEmpty()) {
            throw new UtilityException("Nessuna soluzione trovata!");
        }
        printSolutions(matrices);
    }

    private static void printSolutions(List<String[][]> matrices) {
        System.out.println(toTitle("INFO GLOBALI"));
        System.out.println("Numero squadre: " + TEAMS_LIST.size());
        System.out.println("Numero postazioni: " + getNodeList().size());
        System.out.println("Trovate " + matrices.size() + " soluzioni in " + MAX_ATTEMPTS + " iterazioni");
        System.out.println("Percentuale di successo: " + Math.ceil(((double) matrices.size()) / MAX_ATTEMPTS * 100) + "%");

        // Trovo la migliore soluzione in base ad un punteggio
        System.out.println(toTitle("SQUADRE IN SFIDA"));
        String[][] bestSolution = getBestSolution(matrices);
        Map<String, Set<String>> opponentsMap = getOpponentsMap(bestSolution);
        TEAMS_LIST.forEach(team -> {
            Set<String> opponents = opponentsMap.get(team);
            System.out.println(team + " -> SFIDA: " + String.join(", ", opponents));
        });
        System.out.println(toTitle("MIGLIOR SOLUZIONE"));
        display(bestSolution);
    }

    private static String toTitle(String text) {
        String row = "############################################";
        return "\n" + row + "\n# " + text + "\n" + row;
    }

    private static void checkFeasibility() throws UtilityException {
        if (TEAMS_LIST.size() > NODES.size() * 2) {
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
            if (i % 1000 == 0) {
                System.out.println("Tentativo " + i + "/" + MAX_ATTEMPTS);
            }
            try {
                String[][] matrix = solve(distances);
                matrices.add(matrix);
            } catch (UtilityException e) {
                i++;
            }
        }
        return matrices;
    }

    private static String[][] getBestSolution(List<String[][]> matrices) {
        int score = -1000000;
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

        // Creo mappa per ogni squadra, insieme di altre squadre avversarie con cui si sfida
        Map<String, Set<String>> opponentsMap = getOpponentsMap(matrix);


        // più punteggio alle combinazioni in cui ogni squadra non gioca 2 volte con la stessa squadra
        for (String team : opponentsMap.keySet()) {
            Set<String> opponents = opponentsMap.get(team);
            if (team.startsWith("Squadra")) { // Conto i punti solo per le squadre vere
                int realOpponents = opponents.stream()
                                             .filter(item -> item.startsWith("Squadra"))
                                             .toList()
                                             .size();
                score += realOpponents * 10;
            } else { // Per i fantasmi
                if (opponents.size() == ROUNDS.size()) {
                    score += 1000;
                }
            }
        }

        return score;
    }

    // Creo mappa per ogni postazione, per ogni turno, lista squadre presenti in quella postazione
    private static Map<String, Map<String, List<String>>> getDayMap(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        List<String> nodeList = getNodeList();

        nodeList.forEach(item -> {
            Map<String, List<String>> rounds = new HashMap<>();
            ROUNDS.forEach(r -> rounds.put(r, new ArrayList<>()));
            map.put(item, rounds);
        });


        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = 0; j < TEAMS_LIST.size(); j++) {
                String position = matrix[i][j];
                String round = ROUNDS.get(i);
                String team = TEAMS_LIST.get(j);
                map.get(position).get(round).add(team);
            }
        }
        return map;
    }

    private static Map<String, Set<String>> getOpponentsMap(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = getDayMap(matrix);
        Map<String, Set<String>> opponentsMap = new HashMap<>();
        TEAMS_LIST.forEach(item -> opponentsMap.put(item, new HashSet<>()));

        map.forEach((position, roundsMap) -> roundsMap.forEach((round, teams) -> teams.forEach(team -> {
            List<String> otherTeams = new ArrayList<>(teams);
            otherTeams.remove(team);
            opponentsMap.get(team).addAll(otherTeams);
        })));
        return opponentsMap;
    }

    private static Map<String, List<String>> getOpponentsMapList(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = getDayMap(matrix);
        Map<String, List<String>> opponentsMap = new HashMap<>();
        TEAMS_LIST.forEach(item -> opponentsMap.put(item, new ArrayList<>()));

        map.forEach((position, roundsMap) -> roundsMap.forEach((round, teams) -> teams.forEach(team -> {
            List<String> otherTeams = new ArrayList<>(teams);
            otherTeams.remove(team);
            opponentsMap.get(team).addAll(otherTeams);
        })));
        return opponentsMap;
    }

    private static String[][] solve(Map<String, Double> distances) throws UtilityException {

        String[][] matrix = new String[NODES.size()][TEAMS_LIST.size()];
        // Gestione 2 squadre fantasma che giocano insieme
        setFixedValues(matrix);

        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            for (int colIndex = 0; colIndex < matrix[rowIndex].length; colIndex++) {
                if (matrix[rowIndex][colIndex] != null) {
                    continue;
                }
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

        return matrix;
    }

    private static void setFixedValues(String[][] matrix) {
        List<String> nodes = getNodeList();
        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            matrix[rowIndex][0] = nodes.get(rowIndex);
            matrix[rowIndex][1] = nodes.get(rowIndex);
        }
    }

    private static void display(String[][] matrix) {
        int textSize = 17;
        String emptyString = StringUtils.emptyString(textSize);
        StringBuilder display = new StringBuilder();

        List<String> colNames = TEAMS_LIST.stream()
                                          .map(item -> StringUtils.leftPadSpaces(item, textSize))
                                          .toList();
        List<String> firstRow = new ArrayList<>();
        firstRow.add(emptyString);
        firstRow.addAll(colNames);
        display.append(String.join(DELIMITER, firstRow)).append("\n");


        List<String> rowNames = ROUNDS.stream()
                                      .map(item -> StringUtils.leftPadSpaces(item, textSize))
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


        int textSize1 = 7;
        String emptyString1 = StringUtils.emptyString(textSize1);
        Map<String, Map<String, List<String>>> map = getRoundPositionsMap(matrix);
        StringBuilder display1 = new StringBuilder();
        List<String> positions = new ArrayList<>();
        positions.add(emptyString1);
        positions.addAll(NODES.stream().map(item -> StringUtils.leftPadSpaces(item, 38)).toList());
        display1.append(String.join(DELIMITER, positions)).append("\n");
        ROUNDS.forEach(round -> {
            List<String> row = new ArrayList<>();
            row.add(round);
            NODES.forEach(node -> {
                row.add(StringUtils.leftPadSpaces(String.join(" VS ", map.get(round).get(node)),
                                                  38));
            });
            display1.append(String.join(DELIMITER, row)).append("\n");
        });
        System.out.println(display1);


    }

    private static Map<String, Map<String, List<String>>> getRoundPositionsMap(String[][] matrix) {
        Map<String, Map<String, List<String>>> map = getDayMap(matrix);
        Map<String, Map<String, List<String>>> rMap = new HashMap<>();
        for (String node : NODES) {
            Map<String, List<String>> roundsMap = map.get(node);
            for (String round : ROUNDS) {
                rMap.putIfAbsent(round, new HashMap<>());
                List<String> teams = roundsMap.get(round);
                rMap.get(round).put(node, teams);
            }
        }
        return rMap;
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
