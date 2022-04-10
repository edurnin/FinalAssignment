import edu.princeton.cs.algs4.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class busSystem {
    static ArrayList<Integer> stops;
    static EdgeWeightedDigraph graph;
    static TST<String> tst; // empty ternary search tree
    static DirectedEdge edge=new DirectedEdge(1,2,3); // edges to be added to graph for Dijkstra
    static ArrayList<String> all; // arrayList of all the information


    public static void main(String[] args) {
        readStops("stops.txt");
        readTransfers("transfers.txt");
        readStopTimes("stop_times.txt");

        boolean done = false;
        while (!done) {
            System.out.println("To search for the shortest path between two Stops using bus Stop IDs, enter '1'.\n");
            System.out.println("To search for a bus stop by name, enter '2'.\n");
            System.out.println("To search for a trip by its arrival time, enter '3'.\n");
            System.out.println("To quit enter \"quit\".");
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()) {
                int valueInputted = scanner.nextInt(); // get integer value user inputted
                if (valueInputted == 1) {
                    searchStop(); // functionality to search for shortest trip between two valid bus stops
                } else if (valueInputted == 2) {
                    searchByName(); // functionality to search for bus stop names given valid prefix
                } else if (valueInputted == 3) {
                    searchByArrivalTime(); // functionality to search for trip by its arrival time
                } else {
                    // error handling if user inputted invalid integer value
                    System.out.println("ERROR, enter 1,2 or 3\n");
                }
            } else {
                // get user input as string
                String input = scanner.next();
                // exit the program if they type "exit"
                if (input.equalsIgnoreCase("quit")) {
                    done = true;
                    System.out.println("Thank you.");
                } else {
                    // error handling if user inputted invalid string value
                    System.out.println("Please enter a valid integer value or \"quit\".");
                }
            }
        }

    }

    public static void searchByName() {}

    public static void searchStop() {}

        public static void searchArrivalTime(String arrivalTime){
        for (String allString : all) {
            String[] splitString = allString.split(",");
            if (splitString[1].trim().equals(arrivalTime)) {
                System.out.println(allString);
            }
        }
    }

    public static void searchByArrivalTime(){
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while(!done){
            System.out.println("Enter arrival time in (hour:min:seconds) or enter 'quit' to exit: ");
            if(scanner.hasNextLine()){
                String input = scanner.nextLine().trim();
                if(input.equalsIgnoreCase("quit")){
                    done = true;
                }else{
                    String[] splitInput = input.split(":");
                    try{
                        for (String string : splitInput) {
                            if (Integer.parseInt(string) >= 0) {
                                Integer.parseInt(string);
                            }
                        }
                        searchArrivalTime(input);
                    }catch(Exception ignored){
                        System.out.println("ERROR, Make sure to enter arrival time in (hour:min:seconds).");
                    }
                }
            }else if(scanner.hasNext()){
                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("quit")){
                    done = true;
                }else{
                    System.out.println("Enter arrival time in (hour:min:seconds) or enter 'quit' to exit: ");
                }
            } else{
                System.out.println("ERROR, Make sure to enter arrival time in (hour:min:seconds).Try again: ");
            }

        }
    }

    //reads in the stops file and gets required data
    public static void readStops(String file) {
        try {
            if (file == null) {
                return;
            }
            File myFile = new File(file);
            Scanner readFile = new Scanner(myFile);
            readFile.nextLine(); // ignores the first line in the file
            int count = 0;
            stops = new ArrayList<>();
            tst = new TST<>();
            // while the file still has data:
            while (readFile.hasNextLine()) {
                String[] line = readFile.nextLine().split(","); // separate the lines by the ","
                stops.add(Integer.parseInt(line[0])); // adds each stop to the stops array
                String stopName = edit(line[2]); // change bus stop name as it contains unneeded letters
                line[2] = stopName;
                shiftLeft(line, 2); //put the name of the stop at the start by shifting by 2
                String line2 = createString(line, ","); // create a line with the stop
                tst.put(line2, Integer.toString(count)); // adds the line to the tst
                count++; // add one to the count : line was added to the tst
            }
            Collections.sort(stops); // sort the stops in ascending order
            graph = new EdgeWeightedDigraph(stops.size()); // makes a graph with size of the stops being the no. of vertices
            readFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR.");
            e.printStackTrace();
        }
    }

    // removed all the unneeded letters and words from the string
    public static String edit(String string) {
        String[] splitString = string.split(" ");
        if (splitString[0].equalsIgnoreCase("flagstop") ||
                splitString[0].equalsIgnoreCase("wb") ||
                splitString[0].equalsIgnoreCase("nb") ||
                splitString[0].equalsIgnoreCase("sb") ||
                splitString[0].equalsIgnoreCase("eb")) {
            if (splitString[1].equalsIgnoreCase("flagstop") ||
                    splitString[1].equalsIgnoreCase("wb") ||
                    splitString[1].equalsIgnoreCase("nb") ||
                    splitString[1].equalsIgnoreCase("sb") ||
                    splitString[1].equalsIgnoreCase("eb")) {
                if (splitString[2].equalsIgnoreCase("flagstop") ||
                        splitString[2].equalsIgnoreCase("wb") ||
                        splitString[2].equalsIgnoreCase("nb") ||
                        splitString[2].equalsIgnoreCase("sb") ||
                        splitString[2].equalsIgnoreCase("eb")) {
                    shiftLeft(splitString, 3);
                } else {
                    shiftLeft(splitString, 2);
                }
            } else {
                shiftLeft(splitString, 1);
            }
            string = createString(splitString, " ");
        }
        return string.trim();
    }

    // shifts the string array left by x
    public static void shiftLeft(String[] string, int x) {
        for (int i = 0; i < x; i++) {
            int j = 0;
            String first = string[0];
            for (; j < string.length - 1; j++) {
                string[j] = string[j + 1];// shifts left
            }
            string[j] = first;//added to the end
        }
    }

    //creates string using string array and delimiter
    public static String createString(String[] array, String x) {
        StringBuilder result = new StringBuilder();
        for (String string : array) {
            result.append(string).append(x);
        }
        return result.toString();
    }


    public static void readTransfers(String file) {
        try {
            if (file == null) {
                return;
            }
            File myFile = new File(file);
            Scanner readFile = new Scanner(myFile);
            readFile.nextLine(); // ignores the first line in the file
            while (readFile.hasNextLine()) {
                String[] line = readFile.nextLine().split(","); // separate the lines by the ","
                if (Objects.equals(line[2], "0")) {
                    int matrixValueOne = binarySearch(stops, Integer.parseInt(line[0]));
                    int matrixValueTwo = binarySearch(stops, Integer.parseInt(line[1]));
                    edge = new DirectedEdge(matrixValueOne, matrixValueTwo, 2);// makes new edge with the related matrix values and sets 2 as the cost
                    graph.addEdge(edge); // add the new edge to the graph
                } else {
                    int cost = (Integer.parseInt(line[3])) / 100; // gets the weight of the edge
                    int matrixValueOne = binarySearch(stops, Integer.parseInt(line[0]));
                    int matrixValueTwo = binarySearch(stops, Integer.parseInt(line[1]));
                    DirectedEdge edge = new DirectedEdge(matrixValueOne, matrixValueTwo, cost); // create new edge
                    graph.addEdge(edge); // adds the new endge to the graph
                }
            }
            readFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    /**
     * Method to read in the file stop_times.txt and initialise all data that needs to be used.
     *
     * @param file name of the file to read
     */
    public static void readStopTimes(String file) {
        try {
            if (file == null) {
                return;
            }
            File myFile = new File(file);
            Scanner readFile = new Scanner(myFile);
            all = new ArrayList<>();
            readFile.nextLine(); // skip the first line
            String[] line = addTimes(readFile);
            while (readFile.hasNextLine()) {
                String[] line2 = addTimes(readFile);
                if (Objects.equals(line[0], line2[0])) {
                    // get corresponding matrix value for each of bus stop id
                    int matrixValueOne = binarySearch(stops, Integer.parseInt(line[3]));
                    int matrixValueTwo = binarySearch(stops, Integer.parseInt(line2[3]));
                    DirectedEdge edge = new DirectedEdge(matrixValueOne, matrixValueTwo, 1); // create edge with
                    // the corresponding matrix values and a cost of 1
                    graph.addEdge(edge); // add edge to the graph
                }
                line = line2; // set line one to line two to make sure not to miss any pairs
            }
            all.sort((l, l2) -> { // sort the array by trip ID
                String[] split = l.split(",");
                String[] split2 = l2.split(",");
                return split[0].compareTo(split2[0]);
            });

            readFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }
//Method to read and split line. Add any valid arrival times to array.
    private static String[] addTimes(Scanner readFile) {
        String line = readFile.nextLine(); // read the line
        String[] lineSplit = line.split(",");
        String[] time = lineSplit[1].split(":"); // split the time into string array
        time[0] = time[0].trim(); // gets rid of the whitespace
        if (Integer.parseInt(time[0]) >= 0 && Integer.parseInt(time[0]) <= 23) {
            all.add(line); // adds all stop info into the arrayList for use in printing later
        }
        return lineSplit;
    }

    public static int binarySearch(ArrayList<Integer> arr, int x) {
        int left = 0;
        int right = arr.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid) == x) return mid;
            if (arr.get(mid) < x) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
}


