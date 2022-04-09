import edu.princeton.cs.algs4.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class busSystem {
    static ArrayList<Integer> stops;
    static EdgeWeightedDigraph graph;
    static TST<String> tst; // empty ternary search tree


    public static void main(String[] args) {

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
}


