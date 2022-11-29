import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.*;

public class Main {

    /*
    Number of Knights Tour solutions by position for a 5x5 grid:

                        0   1   2   3   4
                    0   304 0   56  0   304
                    1   0   56  0   56  0
                    2   56  0   64  0   56
                    3   0   56  0   56  0
                    4   304 0   56  0   304
     */

    final static int rowL = 5;//number of rows for chess board
    final static int colL = 5;//number of cols for chess board
    static Stack<Location> stack = new Stack<Location>(); //store moves in order (backtrack capability)

    //list of exhausted locations for each location.  Must use method convertLocToIndex to find a Locations proper index
    static ArrayList<ArrayList<Location>> exhausted = new ArrayList<ArrayList<Location>>(25);
    static int board[][] = new int[rowL][colL];//2d array used to store the order of moves
    //static boolean visited[][] = new boolean[rowL][colL]; 2d array used to store what locations have been used, NOT REQUIRED
    static Location startLoc = new Location(1, 1);
    static Location endLoc = new Location(1,1);
    static int locIds[] = new int[50];

    public static void main(String[] args) {

        System.out.println("START");
        initExhausted();
        ArrayList<Location> currentPossible;
        obtainStartLoc();
        endLoc = startLoc;
        System.out.println("Start location is " + startLoc);

        stack.push(startLoc);
        initLocId(startLoc);
        //visited[startLoc.getRow()][startLoc.getCol()] = true;

        while (stack.size() != rowL * colL && stack.size() != 0) {
            printPossibleMoveLocations(startLoc);
            currentPossible = getPossibleMoves(startLoc);
            printExhaustedList(startLoc);
            Location next = getNextMove(startLoc, currentPossible);

            if (currentPossible.size() > 0 && next == null) {
                //begin reverse phase
                System.out.println();
                System.out.println();
                System.out.println("-------------No available moves. Beginning reverse phase-------------");
                clearExhausted(startLoc);
                System.out.println("Exhausted list cleared from " + startLoc + ": " + exhausted.get(convertLocToIndex(startLoc)));
                Location stuck = new Location(startLoc.getRow(), startLoc.getCol());
                System.out.println("Var stuck = " + startLoc);
                System.out.println("Board cleared for location " + stuck);
                zeroLocId(stuck);
                System.out.println();
                printBoard();
                System.out.println();
                System.out.println(stack.peek() + " cleared from stack");
                stack.pop();
                if(stack.size() > 0) {
                    startLoc.setRow(stack.peek().getRow());
                    startLoc.setCol(stack.peek().getCol());
                }
                System.out.println("startLoc now equals " + startLoc);
                addToExhausted(startLoc, stuck);
                System.out.println(stuck + " added to exhausted list for " + startLoc);
                printExhaustedList(startLoc);
                System.out.println();
                System.out.println();
                System.out.println();
            }
            else if(currentPossible.size() > 0 && next != null) {
                stack.push(next);
                System.out.println("Next move " + next + " added to stack");
                addToExhausted(startLoc, next);
                System.out.println("Move " + next + " added to exhausted list for index " + convertLocToIndex(next));
                System.out.println("New exhausted list for location " + startLoc + ": " + exhausted.get(convertLocToIndex(startLoc)));
                startLoc = next;
                initLocId(startLoc);
                System.out.println();
                printBoard();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println("New current location: " + endLoc);
                System.out.println();
            }

            /*
            try {
                //waits for one second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             */

        }
        if(board[startLoc.getRow()][startLoc.getCol()] == 0) {
            System.out.println("No solution found for " + endLoc);
        }
        else {
            System.out.println("Solution found for " + endLoc);
        }


    }

    /*
     * Printed out the exhausted list for a given Location
     */
    public static void printExhaustedList(Location loc) {
        int desIndex = convertLocToIndex(loc);
        if (exhausted.get(desIndex).size() == 0) {
            System.out.println("No exhausted locations for " + loc);
        } else {
            System.out.println("Exhausted locations for " + loc + ": " + exhausted.get(desIndex));
        }
    }

    /*
     * Prints out the possible move locations for a given Location
     */
    public static void printPossibleMoveLocations(Location loc) {
        ArrayList<Location> possible = getPossibleMoves(loc);
        System.out.println("Possible move locations: " + possible);
    }

    /*
     * clear out the exhausted list for a given Location
     * This needs to be done everytime a Location is removed from the Stack
     */
    public static void clearExhausted(Location loc) {
        int index = convertLocToIndex(loc);
        exhausted.set(index, new ArrayList<Location>(25));
    }

    /*
     * set up the exhausted list with empty exhuasted lists.
     */
    public static void initExhausted() {
        for (int i = 0; i < 25; i++) {
            exhausted.add(new ArrayList<Location>(25));
        }
    }

    /*
     * is this next Location exhausted from the source Location
     */
    public static boolean inExhausted(Location source, Location dest) {
        int sourceIndex = convertLocToIndex(source);
        for (int i = 0; i < exhausted.get(sourceIndex).size(); i++) {
            Location current = exhausted.get(sourceIndex).get(i);
            if (current.getRow() == dest.getRow() && current.getCol() == dest.getCol())
                return true;
        }
        return false;
    }

    /*
    * prints out true/false for what spaces have been visited
    */
    /*public static void printVisited() {}
                return true;
            }
        }
        return false;
    }

    /*
     * returns the next valid move for a given Location on a given ArrayList of possible moves
     */

    public static Location getNextMove (Location loc, ArrayList<Location> list) {
        for (int i = 0; i < list.size(); i++) {
            Location current = list.get(i);
            if(!inExhausted(loc, current) && board[current.getRow()][current.getCol()] == 0) // && board[current.getRow()][current.getCol()] > 0
                return list.get(i);
        }
        return null;
    }

    /*
                 * adds a dest Location in the exhausted list for the source Location
                 */
    public static void addToExhausted (Location source, Location dest){
        exhausted.get(convertLocToIndex(source)).add(dest);
    }

    /*
     * is this Location a valid one
     */
    public static boolean isValid (Location loc){
        return loc.getRow() >= 0 && loc.getRow() < 5 && loc.getCol() >= 0 && loc.getCol() < 5;
    }

    /*
     * returns another Location for the knight to move in.  If no more possible move
     * locations exist from Location loc, then return null
     */
    public static ArrayList<Location> getPossibleMoves (Location loc){
        ArrayList<Location> possibleLocs = new ArrayList<>();
        Location currentLoc = new Location(loc.getRow() - 2, loc.getCol() + 1); //top upper right
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() - 1, loc.getCol() + 2); //top lower right
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() + 1, loc.getCol() + 2); //bottom upper right
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() + 2, loc.getCol() + 1); //bottom lower right
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() + 2, loc.getCol() - 1); //bottom lower left
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() + 1, loc.getCol() - 2); //bottom upper left
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() - 1, loc.getCol() - 2); //top lower left
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }
        currentLoc = new Location(loc.getRow() - 2, loc.getCol() - 1); //top upper left
        if (isValid(currentLoc)) {
            possibleLocs.add(currentLoc);
        }

        return possibleLocs;
    }

    /*
     * prompt for input and read in the start Location
     */
    public static void obtainStartLoc () {
        JFrame jFrame = new JFrame();
        String getMessage = JOptionPane.showInputDialog(jFrame, "Please enter a start location (i.e. (0,0)):");
        startLoc.setRow(Integer.valueOf(getMessage.substring(1, 2)));
        startLoc.setCol(Integer.valueOf(getMessage.substring(3, 4)));
    }

    /*
     * converts a (row,col) to an array index for use in the exhausted list
     */
    public static int convertLocToIndex (Location loc){
        return (loc.getRow() * 5) + loc.getCol();
    }

    public static void initLocId(Location loc) {
        int stackSize = stack.size();
        int index = convertLocToIndex(loc);
        locIds[index] = stackSize;
    }

    public static void zeroLocId(Location loc) {
        locIds[convertLocToIndex(loc)] = 0;
    }

    /*
     * prints out the board (numbers correspond to moves)
     */
    public static void printBoard () {
        int num = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(locIds[num] + "   ");
                board[i][j] = locIds[num];
                num++;
            }
            System.out.println();
        }
    }
}