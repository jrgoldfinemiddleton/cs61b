/* Board.java */
package game;

import list.DList;
import list.InvalidNodeException;
import list.List;
import player.Move;

/**
 * A game board for Network.
 * 
 * @author      Jason Goldfine-Middleton
 * @version     1.0, 8 October 2014
 */
public class Board {

  /**
   * A direction away from a square on a game board.  The ordering of
   * directions is: none, left, up-left, up, up-right, right, down-right, down,
   * down-left.
   * 
   * @author      Jason Goldfine-Middleton
   * @version     1.0, 8 October 2014
   */
  private static enum Direction { NA, L, UL, U, UR, R, DR, D, DL }

  /**
   * Constant for the player of color black.
   */
  public static final int BLACK = -1;
  
  /**
   * Constant for the player of color white.
   */
  public static final int WHITE = 1;
  
  /**
   * Constant for neither player, to be used when referring to empty squares.
   */
  static final int NONE = 0;
  
  /**
   * Constant for the maximum number of chips a player may have on a board.
   */
  static final int MAX_CHIPS = 10;
  
  /**
   * Constant for the minimum number of chips a path needs to be a network.
   */
  static final int NETWORK_LENGTH = 6;
  
  /**
   * Constant for the width and height (in number of squares) a board has.
   */
  static final int SIZE = 8;

  /**
   * Returns an empty list.
   * 
   * @return    a new empty {@code List}.
   */
  private static List genEmptyList() {
    return new DList();
  }

  /**
   * Returns the integer value representing the opposite of a given color.
   * 
   * @param side      the color of the given side.
   * @return          WHITE if side is BLACK, BLACK otherwise.
   */
  public static int oppColor(int side) {
    if (side == BLACK) {
      return WHITE;
    } else {
      return BLACK;
    }
  }

  /**
   * Returns the opposite of the given direction.
   * 
   * @param dir     the given direction.
   * @return        the Direction pointing the opposite way.  Returns 
   *                Direction.NA (none) otherwise.
   */
  private static Direction oppDirection(Direction dir) {
    switch (dir) {
      case L:
        return Direction.R;
      case UL:
        return Direction.DR;
      case U:
        return Direction.D;
      case UR:
        return Direction.DL;
      case R:
        return Direction.L;
      case DR:
        return Direction.UL;
      case D:
        return Direction.U;
      case DL:
        return Direction.UR;
      default:
        return Direction.NA;
    }
  }

  /**
   * List of all black chips on this board.
   */
  private List bChips;
  
  /**
   * List of all white chips on this board.
   */
  private List wChips;
  
  /**
   * The total number of networks of black chips on this board.
   */
  int blackNets;
  
  /**
   * The total number of networks of white chips on this board.
   */
  int whiteNets;
  
  /**
   * A 2-D array representation of the game grid.
   */
  private int[][] board;

  /**
   * Constructs a new game board for the Network game.  Every square is set to
   * empty initially.
   */
  public Board() {
    board = new int[SIZE][SIZE];
    bChips = genEmptyList();
    wChips = genEmptyList();
    blackNets = 0;
    whiteNets = 0;
  }

  /**
   * Counts and returns the number of chips in either of a given player's goals.
   * 
   * @param side      the color of the player.  Must be BLACK or WHITE.
   * @return          the total number of chips in either of the given player's
   *                  goals.
   */
  int chipsInGoal(int side) {
    int numGoalChips = 0;
    for (Object o : listChips(side)) {
      Chip c = (Chip)o;
      if (isInGoal(c.side, c.x, c.y)) {
        numGoalChips++;
      }
    }
    return numGoalChips;
  }

  /**
   * Counts and returns the number of neighboring chips to the chip at a given
   * location with the same color as {@code side}.  A neighbor is a chip that is
   * in a square adjacent to the one located at (x,y) diagonally, horizontally,
   * or vertically.
   * 
   * @param side      the color of the chip at (x,y)
   * @param x         the x-coordinate of the square whose neighbors are sought.
   * @param y         the y-coordinate of the square whose neighbors are sought.
   * @return          the number of "touching" chips to the given chip.
   */
  private int countNeighbors(int side, int x, int y) {
    int result = countNeighborsHelper(side, x, y, 1);
    return result;
  }

  /**
   * Serves as an internal helper method for the {@code countNeighbors} method.
   * It calls itself once per discovered neighbor to determine whether this
   * neighbor has any neighbors. It then returns the total number of 
   * same-colored neighbors to the chip located at (x,y).
   * 
   * @param side      the color of the chip at (x,y).
   * @param x         the x-coordinate of the square whose neighbors are sought.
   * @param y         the y-coordinate of the square whose neighbors are sought.
   * @param run       either 1 or 2 signifying whether we are looking for
   *                  primary neighbors or secondary neighbors, respectively.
   * @return          the total number of neighbors of the same color found
   *                  recursively.
   */
  private int countNeighborsHelper(int side, int x, int y, int run) {
    int count = 0;
    for (Direction d : Direction.values()) {
      if (d != Direction.NA) {
        Chip c = getInDirection(d, 1, x, y);
        if (c.side == side) {
          count++;
          if (run == 1) {
            count += countNeighborsHelper(side, c.x, c.y, 2) - 1;
          }
          if (count >= 2) {
            return count;
          }
        }
      }
    }
    return count;
  }

  /**
   * Updates the game board with the given move for the given side (color).
   * This method is only to be called after determining the validity of the
   * move with {@code isValidMove}, or in the special case of 
   * {@code formsCluster}, which is called at the end of {@code isValidMove}.
   * 
   * @param side      the color of the player doing the move.  Must be BLACK or
   *                  WHITE.
   * @param move      the valid move to be made.  Must be an add or a step move.
   */
  public void doMove(int side, Move move) {
    if (move.moveKind == Move.STEP) {
      board[move.x2][move.y2] = NONE;
    }
    board[move.x1][move.y1] = side; 
    updateChips(side);
  }

  /**
   * Determines whether a move by a given player will cause more than two of 
   * the same-colored chips to be touching on the board.  This method is only
   * to be called by {@code isValidMove} after all other validity checks have
   * completed with success.
   * 
   * @param side      the color of the player doing the move.
   * @param move      the move to validate.
   * @return          true if the move forms a cluster, false otherwise.
   */
  private boolean formsCluster(int side, Move move) {
    boolean flag = true;
    doMove(side, move);
    if (countNeighbors(side, move.x1, move.y1) < 2) {
      flag = false;
    }
    undoMove(side, move);
    return flag;
  }

  /**
   * Walks through every possible path between connected chips of color "side",
   * stores the paths discovered in a list, and returns the list.  Every element
   * in the returned list represents a valid path.
   * <p>
   * <b>Note:</b> a "network" is not treated specially by this method.  Use the
   * {@code hasNetwork} method to determine whether any of the paths is a
   * network.
   * 
   * @param side      the color of the player whose paths are sought.
   * @return          a list of {@code List} objects with {@code Chip} elements
   *                  in the order they are connected in to form a path.  The
   *                  returned list contains every valid path discovered.
   */
  List getAllPaths(int side) {
    List paths = genEmptyList();
    List chips = listChips(side); 
    for (Object o : chips) {
      List curPath = genEmptyList();
      curPath.insertBack(o);
      List newPaths = getAllPathsHelper(curPath, Direction.NA);
      for (Object list : newPaths) {
        paths.insertBack(list);
      }
    }
    return paths;
  }

  /**
   * Serves as an internal helper method for the {@code getAllPaths} method.
   * Given a path and the final direction the path took, it looks for any
   * available chips connected to the last chip in the path so far, provided 
   * the path would change directions but not turn back on itself.  Then the
   * method recursively checks the path generated by adding each connected chip
   * to the given list, and the process repeats until there are no more
   * connected chips to add. All discovered paths are returned in a list.
   * 
   * @param pathSoFar       the path between connected chips to extend if 
   *                        possible.
   * @param lastDir         the direction the path took from the penultimate
   *                        chip to the last chip in the path so far.
   * @return                a list of every known path extending the given path.
   */
  private List getAllPathsHelper(List pathSoFar, Direction lastDir) {
    List paths = genEmptyList();
    int curPathLength = pathSoFar.length();
    Chip back = new Chip();
    try {
      back = (Chip)pathSoFar.back().item();
    } catch (InvalidNodeException ine) {
      // return empty list of paths
      System.err.println("getAllPathsHelper() received corrupted path list");
      ine.printStackTrace();
      return paths;
    }
    int x = back.x;
    int y = back.y;
    int side = back.side;
    List chips = listChips(side); 
    // no chance of continuing path, all chips already used
    if (chips.length() == curPathLength) {
      return paths;
    }
    // check the 8 directions for connecting chips
    Chip[] cons = getConnectedChips(side, x, y);
    // keep track of directions
    int index = 0;
    // see if each c in cons is already in the path, if not add it
    for (Chip c : cons) {
      Direction curDir = Direction.values()[index];
      if (c != null && curDir != lastDir &&
          curDir != oppDirection(lastDir)) {
        // assume chip has not already been used earlier in the pathSoFar
        boolean alreadyUsed = false;
        List newPath = genEmptyList();
        // clone the path so far
        for (Object o : pathSoFar) {
          newPath.insertBack(o);
          if (c.equals(o)) {
            alreadyUsed = true;
          }
        }
        if (!alreadyUsed) {
          newPath.insertBack(c);
          paths.insertBack(newPath);
          List newPaths = getAllPathsHelper(newPath,
              Direction.values()[index]);
          for (Object o : newPaths) {
            paths.insertBack(o);
          }
        }
      }
      index++;
    }
    return paths;
  }

  /**
   * Searches for every possible valid move for the player of the given color
   * and returns them all in a list.
   * 
   * @param side        the color of the player for whom the list of moves is
   *                    sought.
   * @return            a list of {@code Move} objects representing every valid
   *                    move from the current board for player {@code side}.
   */
  List getAllValidMoves(int side) {
    List moveList = genEmptyList();
    // get all valid add moves
    if (getNumChips(side) < MAX_CHIPS) {
      for (int x = 0; x < SIZE; x++) {
        for (int y = 0; y < SIZE; y++) {
          Move move = new Move(x, y);
          if (isValidMove(side, move)) {
            moveList.insertBack(move);
          }
        }
      }
    } else {
      // get all valid step moves
      List chips = listChips(side); 
      for (Object o : chips) {
        int xx = ((Chip)o).x;
        int yy = ((Chip)o).y;
        for (int x = 0; x < SIZE; x++) {
          for (int y = 0; y < SIZE; y++) {
            Move move = new Move(x, y, xx, yy);
            if (isValidMove(side, move)) {
              moveList.insertBack(move);
            }
          }
        }
      }
    }
    return moveList;
  }

  /**
   * Takes a location and a color as parameters and looks in each of the eight
   * directions for a chip of the same color.  If found, the chip is added to
   * an array.  If the search in a certain direction passes the edge of the
   * board or finds a chip of the opposite color first, no chip is added for
   * that direction.  After each direction has been checked the array is 
   * returned.  The order of the directions is as follows: left, up-left, up, 
   * up-right, right, down-right, down, down-left.
   * 
   * @param side      the color of the player whose chips are sought.
   * @param x         the x-coordinate of the Chip whose connected chips are
   *                  sought.
   * @param y         the y-coordinate of the Chip whose connected chips are
   *                  sought.
   * @return          an array of {@code Chip} objects representing connected
   *                  chips of the same color as {@code side}.  Any directions
   *                  without a connected chip contain a null reference.
   */
  Chip[] getConnectedChips(int side, int x, int y) {
    Chip[] cons = new Chip[8];
    int index = 0;
    for (Direction d : Direction.values()) {
      if (d != Direction.NA) {
        for (int i = 1; i < SIZE; i++) {
          Chip c = getInDirection(d, i, x, y);
          // if c is off the board or opposite color, no connected chip
          // in this direction
          if (c.x == -1 || c.side == oppColor(side)) {
            break;
          }
          // if c is of the same color, connected chip in this direction
          else if (c.side == side) {
            cons[index] = c;
            break;
          }
          // c is an empty square, keep looking
        }
        index++;
      } 
    }
    return cons;
  }

  /**
   * Returns the chip located "distance" squares away from square (x,y) in 
   * direction {@code dir}.  If the location of the returned chip is outside of
   * the boundaries of the game board, an invalid chip is returned.  This
   * method returns a chip of color NONE if the square is empty.
   * <p>
   * <b>Note:</b>  If {@code dir} is Direction.NA or distance is non-positive,
   * the chip returned is the one at (x,y).
   * 
   * @param dir     the direction away from square (x,y).
   * @param         distance the number of squares away from square (x,y).
   * @param x       the x-coordinate of the original square.
   * @param y       the y-coordinate of the original square.
   * @return        a Chip object that represents the square located distance
   *                squares away from (x,y) in direction {@code dir}.  If no
   *                such location exists, returns an invalid {@code Chip}.  If
   *                the location exists but is empty, returns a chip with color
   *                NONE.
   */
  private Chip getInDirection(Direction dir, int distance, int x, int y) {
    if (distance < 0) {
      distance = 0;
    }
    int xShift = 0, yShift = 0;
    switch (dir) {
      case L:
        xShift = -1;
        break;
      case UL:
        xShift = -1;
        yShift = -1;
        break;
      case U:
        yShift = -1;
        break;
      case UR:
        xShift = 1;
        yShift = -1;
        break;
      case R:
        xShift = 1;
        break;
      case DR:
        xShift = 1;
        yShift = 1;
        break;
      case D:
        yShift = 1;
        break;
      case DL:
        xShift = -1;
        yShift = 1;
        break;
      default:
        xShift = 0;
        yShift = 0;
        break;
    }
    int newXPos = x + xShift * distance;
    int newYPos = y + yShift * distance;
    if (!onBoard(newXPos, newYPos)) {
      // return invalid chip
      return new Chip();
    }
    // may return a chip with color NONE if square is empty
    return new Chip(newXPos, newYPos, board[newXPos][newYPos]); 
  }

  /**
   * Returns the total number of chips on the board for a given player.
   * 
   * @param side      the color of the player whose chips are sought.
   * @return          the total number of chips the player has on the board. If
   *                  {@code side} is neither BLACK nor WHITE, returns 0.
   */
  int getNumChips(int side) {
    return listChips(side).length();
  }

  /**
   * Returns the total number of connections between pairs of chips of color
   * {@code side} on the board.  Two chips may only be connected diagonally
   * (45 degrees), horizontally, or vertically, and only if there is no
   * intervening chip of the opposite color between them.
   * 
   * @param side      the color of the player whose connections are sought.
   * @return          the total number of connections between pairs of chips
   *                  for the given player.
   */
  int getNumConnections(int side) {
    int numConnections = 0;
    for (Object o : listChips(side)) {
      Chip c = (Chip)o;

      Chip[] cons = getConnectedChips(side, c.x, c.y);
      for (Chip con : cons) {
        if (con != null) {
          numConnections++;
        }
      }
    }
    // divide by two because each connection was counted twice
    return numConnections / 2;
  }

  /**
   * Scans every known path between the chips of a given player
   * for the necessary conditions of forming a "network" and returns whether
   * the player has a network.  It also counts all the discovered
   * networks.  If the player is BLACK, it updates {@code blackNets},
   * otherwise it updates {@code whiteNets}.
   * <p>
   * Network conditions:
   * <ol>
   *    <li>There are at least 6 chips in the path.</li>
   *    <li>Every segment of the path is non-parallel with its adjacent
   *    segments.</li>
   *    <li>Only the first and last chips are in the player's goals.</li>
   *    <li>The first chip and last chip must be in opposite goals.</li>
   * </ol>
   * 
   * @param side      the color of the player to search for a network for.
   *                  Must be BLACK or WHITE.
   * @return          true if the player has a network, false otherwise.
   */
  public boolean hasNetwork(int side) {
    List paths = getAllPaths(side);
    int countNetworks = 0;
    for (Object o : paths) {
      List curPath = (List)o;
      if (curPath.length() >= NETWORK_LENGTH) {
        try {
          Chip first = (Chip)curPath.front().item();
          boolean firstChipInGoal = side == BLACK ? 
              (first.y == 0) : (first.x == 0);
              if (firstChipInGoal && hasNetworkHelper(side, curPath)) {
                countNetworks++;
              }
        } catch (InvalidNodeException ine) {
          System.err.println("hasNetwork() received corrupted path list");
          ine.printStackTrace();
        }
      }
    }
    // updates the number of networks
    if (side == BLACK) {
      blackNets = countNetworks;
    } else {
      whiteNets = countNetworks;
    }

    return countNetworks > 0;
  }

  /**
   * Serves as an internal helper method for {@code hasNetwork}.  It checks 
   * "network" conditions 2, 3, and 4, and returns whether a given path is a
   * network.
   * 
   * @param side      the color of the player whose path is to be checked.
   * @param path      the path with minimum length 6 chips to check if it is a
   *                  network.
   * @return          true if the given path is a network, false otherwise.
   */
  private boolean hasNetworkHelper(int side, List path) {
    try {
      Chip front = (Chip)path.front().item();
      Chip back = (Chip)path.back().item();
      int index = 0;
      if (isInGoal(side, front.x, front.y) && isInGoal(side, back.x, back.y)) {
        if (inSameGoal(side, front.x, front.y, back.x, back.y)) {
          return false;
        }
        for (Object o : path) {
          // skip first chip, which is in a goal
          if (index != 0) {
            Chip cur = (Chip)o;
            int x = cur.x;
            int y = cur.y;
            // if any other chip is in a goal too early in the path, not a
            // network
            if (index < path.length() - 1 && isInGoal(side, x, y)) {
              return false;
            }
          }
          index++;
        }
        return true;
      }
    } catch (InvalidNodeException ine) {
      System.err.println("hasNetworkHelper() received corrupted path list");
      ine.printStackTrace();
    }
    return false;
  }

  /**
   * Returns whether two chips in a player's goals are both in the same goal.
   * <p>
   * <b>Note:</b> it is assumed that the two chips are each in a goal.
   * 
   * @param side      the color of the player whose chips and goals are to be
   *                  considered.
   * @param x1        the x-coordinate of the first chip.
   * @param y1        the y-coordinate of the first chip.
   * @param x2        the x-coordinate of the second chip.
   * @param y2        the y-coordinate of the second chip.
   * @return          true if the chips are in the same goal, false otherwise.
   */
  private boolean inSameGoal(int side, int x1, int y1, int x2, int y2) {
    if (side == BLACK) {
      return y1 == y2;
    } else {
      return x1 == x2;
    }
  }

  /**
   * Returns whether a particular square is located inside of a given player's
   * goal.
   * 
   * @param side      the color of the player whose goals are to be considered.
   * @param x         the x-coordinate of the given square.
   * @param y         the y-coordinate of the given square.
   * @return          true if the square is in one of the given player's goal,
   *                  false otherwise.
   */
  private boolean isInGoal(int side, int x, int y) {
    if (side == BLACK) {
      return y == 0 || y == SIZE - 1; 
    } else {
      return x == 0 || x == SIZE - 1;
    }
  }

  /**
   * Considers a potential move (either a step move or an add move) for a given
   * player and returns whether it is valid given the current game board 
   * configuration.
   * 
   * @param side      the color of the player whose move is to be considered.
   * @param move      the move for which to determine the validity.
   * @return          true if the move is valid, false otherwise.  Also returns
   *                  false if {@code side} is neither BLACK nor WHITE.
   */
  public boolean isValidMove(int side, Move move) {
    // positions not on board
    if (move.moveKind == Move.STEP) {
      if (!onBoard(move.x2, move.y2)) {
        return false;
      }
    }
    if (!onBoard(move.x1, move.y1)) {
      return false;
    }

    // invalid color
    if (side != BLACK && side != WHITE) {
      return false;
    }

    // move cannot place chip in either a corner or the opposite
    // color's goal
    if (isInGoal(oppColor(side), move.x1, move.y1)) {
      return false;
    }

    if (move.moveKind == Move.STEP) {
      // no piece of correct color at original location
      if (board[move.x2][move.y2] != side) {
        return false;
      }
    }

    // terminal location already occupied (also prevents moving
    // chip to its current location)
    if (board[move.x1][move.y1] != NONE) {
      return false;
    }

    // can only add chips when there are less than 10 of given color
    if (getNumChips(side) >= MAX_CHIPS && move.moveKind == Move.ADD) {
      return false;
    }

    // can only step chips where there are at least 10 of given color
    if (getNumChips(side) < MAX_CHIPS && move.moveKind == Move.STEP) {
      return false;
    }

    // there should never be more black chips than white chips on board
    // this check was removed to improve the evaluation function in Engine 
    /*if (bChips.length() > wChips.length()) {
      return false;
    }*/

    // adding the new chip can never make a cluster larger than 2
    if (formsCluster(side, move)) {
      return false;
    }

    return true;
  }

  /**
   * Returns a list of all the chips a given player currently has on this board.
   * 
   * @param side      the color of the player whose chips are to be listed.
   * @return          a list containing a {@code Chip} for each square taken
   *                  by the given player.
   */
  private List listChips(int side) {
    List chips = genEmptyList();
    if (side != NONE) {
      chips = (side == BLACK ? bChips : wChips);
    }
    return chips;
  }

  /**
   * Searches through a list of paths between chips and returns the length of
   * the longest path found.  This method should ideally be called with a list 
   * generated by {@code getAllPaths} as its parameter.
   * <p>
   * <b>Note:</b> this method does not stringently check paths with conditions
   * in the manner that {@code hasNetwork} does.  Some paths would not qualify
   * as valid networks but are still included in the search; therefore this is
   * a somewhat "dumb" statistic, but still useful as a general indicator.
   * 
   * @param paths     a list whose elements are {@code List}s containing 
   *                  {@code Chip}s representing connected paths assumed to be
   *                  for a single player.
   * @return          the length, in the number of chips, of the longest path
   *                  found.
   */
  int longestPathLength(List paths) {
    int length = 0;
    for (Object o : paths) {
      int curLength = ((List)o).length();
      if (curLength > length) {
        length = curLength;
      }
    }
    return length;
  }

  /**
   * Returns whether a given location is located within the boundaries of this
   * game board.
   * 
   * @param x       the x-coordinate of the location.
   * @param y       the y-coordinate of the location.
   * @return        true if the location is in bounds, false otherwise.
   */
  private boolean onBoard(int x, int y) {
    return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
  }

  /**
   * Returns a string representation of the current game board. A BLACK square
   * is marked "B", a WHITE square is marked "W", and an empty square is marked
   * with a dot.
   * 
   * @return        a string representation of the current game board.
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder(" ");
    for (int x = 0; x < SIZE; x++) {
      s.append(" " + x);
    }
    for (int y = 0; y < SIZE; y++) {
      s.append("\n" + y);
      for (int x = 0; x < SIZE; x++) {
        String side;
        if (board[x][y] == BLACK) {
          side = " B";
        } else if (board[x][y] == WHITE) {
          side = " W";
        } else {
          side = " \u00B7"; // a dot
        }
        s.append(side);
      }
    }
    return s.toString();
  }

  /**
   * Returns the total number of networks for a given player on the current 
   * game board.
   * 
   * @param side      the color of the player whose total networks is sought.
   *                  Must be BLACK or WHITE.
   * @return          the total number of networks on the board for the given
   *                  player.
   */
  int totalNetworks(int side) {
    return side == BLACK ? blackNets : whiteNets;
  }

  /**
   * Reverses a previously executed move and restores the game board to its 
   * prior configuration.
   * <p>
   * <b>Note:</b> the given move MUST have already been recorded to the game
   * board (that is, {@code doMove} must have been called already).
   * 
   * @param side        the color of the player who made the move.  Must be 
   *                    BLACK or WHITE.
   * @param move        the move to undo.  Must be an add or a step move.
   */
  void undoMove(int side, Move move) {
    if (move.moveKind == Move.STEP) {
      Move reverse = new Move(move.x2, move.y2, move.x1, move.y1);
      doMove(side, reverse);
    } else {
      board[move.x1][move.y1] = NONE;
      updateChips(side);
    }
  }

  /**
   * Scans the game board array and updates the correct list of chips for a 
   * given player.
   * <p>
   * <b>Note:</b> as the main purpose of this method is to update the correct
   * chip list, returning the list is simply a courtesy.
   * 
   * @param side      the color of the player whose chips are to be updated.
   * @return          {@code bChips} if the player is BLACK, {@code wChips}
   *                  otherwise.  Whichever list is returned will have been
   *                  updated first.
   */
  private List updateChips(int side) {
    List chips = genEmptyList();
    if (side == NONE) {
      return chips;
    }
    for (int x = 0; x < SIZE; x++) {
      for (int y = 0; y < SIZE; y++) {
        if (board[x][y] == side) {
          chips.insertBack(new Chip(x, y, side));
        }
      }
    }

    if (side == BLACK) {
      bChips = chips;
    } else {
      wChips = chips;
    }
    return chips;
  }
} 
