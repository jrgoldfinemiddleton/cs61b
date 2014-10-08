/* Engine.java */
package game;

import java.util.Random;

import list.List;
import player.Move;

/**
 * An artificial intelligence engine for a {@code MachinePlayer} with the
 * ability to select moves based on the probability that they will lead to a 
 * win in the Network game.
 * 
 * @author Jason Goldfine-Middleton
 * @version 1.0, 8 October 2014
 */
public class Engine {
  
  /**
   * A data container which stores the calculated best known {@code Move} with
   * its evaluated score.
   * 
   * @author Jason Goldfine-Middleton
   * @version 1.0, 8 October 2014
   */
  private class Best {

    /**
     * The current best known move.
     */
    private Move move;
    
    /**
     * The score of the best move.
     */
    private double score;

    /**
     * Constructor for a new best move.
     * 
     * @param move      the current best known move.
     * @param score     the score of the best move.
     */
    private Best(Move move, double score) {
      this.move = move;
      this.score = score;
    }
  }

  /**
   * The game board.
   */
  public final Board board;
  
  /**
   * The opponent's color.  Must be Board.BLACK or Board.WHITE.
   */
  private final int oppSide;
  
  /**
   * The number of turns to search through before calculating the score of the
   * current game board configuration if an explicit win or loss has not been
   * found.
   */
  private final int searchDepth;
  
  /**
   * The MachinePlayer's color.  Must be Board.BLACK or Board.WHITE.
   */
  public final int side;

  /**
   * Constructor for a new AI engine of a {@code MachinePlayer} with the given
   * board, color, and move search depth.
   * 
   * @param board       the game board upon which to keep track of moves.
   * @param side        the MachinePlayer's color, must be Board.BLACK or
   *                    Board.WHITE.
   * @param depth       how many turns to search before calculating the score
   *                    if a win or loss has not been found.
   */
  public Engine(Board board, int side, int depth) {
    this.board = board;
    this.side = side;
    searchDepth = depth;
    oppSide = Board.oppColor(side);
  }

  /**
   * Returns an opening add move to one of the four center squares on the game
   * board for the {@code MachinePlayer}.  It is only to be used when the
   * {@code MachinePlayer} is the white player.
   * 
   * @return        an add move to one of the four center squares.
   */
  private Move chooseOpeningMove() {
    Random rand = new Random();
    double val = rand.nextDouble();
    int x, y;
    if (val < 0.25) {
      x = y = Board.SIZE / 2 - 1;
    } else if (val < 0.5) {
      x = Board.SIZE / 2 - 1;
      y = Board.SIZE / 2;
    } else if (val < 0.75) {
      x = Board.SIZE / 2;
      y = Board.SIZE / 2 - 1;
    } else {
      x = y = Board.SIZE / 2;
    }
    return new Move(x, y);
  }

  /**
   * Selects the best move for the {@code MachinePlayer} based on a game tree 
   * search and an evaluation function.  If the {@code MachinePlayer} is white
   * and it is the first turn of the game, it returns an add move to one of the
   * four center squares.
   * 
   * @return      the {@code Move} determined to have the best score for the
   *              {@code MachinePlayer}.
   */
  public Move getBestMove() {
    Best myBest;
    if (board.getNumChips(Board.WHITE) == 0 && side == Board.WHITE) {
      myBest = new Best(chooseOpeningMove(), 0.0);
    } else if (board.getNumChips(Board.WHITE) >= Board.MAX_CHIPS - 1) {
      myBest = getBestMoveHelper(side, -Double.MAX_VALUE, Double.MAX_VALUE, 2);
    } else {
      myBest = getBestMoveHelper(side, -Double.MAX_VALUE, Double.MAX_VALUE, searchDepth);
    }
    return myBest.move;
  }

  /**
   * Serves as an internal helper method for {@code getBestMove} which does a
   * game tree search through {@code depth} turns.
   * 
   * @param color       the color of the player seeking a best move.
   * @param alpha       the best (highest) score the given player knows it can
   *                    achieve thus far.
   * @param beta        the best (lowest) score the opponent knows it can 
   *                    achieve thus far.
   * @param depth       the number of turns to run in the search.  If 
   *                    {@code depth} is 0, the evaluation function is used to 
   *                    predict winning chances.
   * @return            a {@code Best} containing the best discovered
   *                    {@code Move} and its score.
   */
  private Best getBestMoveHelper(int color, double alpha, double beta, int depth) {
    Best myBest = new Best(null, 0.0);
    Best oppBest;

    // this should only happen when we just want to know how good
    // a move looks, not when we actually need a move
    if (depth == 0) {
      myBest.score = getScore();
      return myBest;
    }

    // wait to check for networks because if depth = 0
    // we want to lower the value of any discovered
    // networks (this makes the machine choose the sooner win)
    
    // we also must record a network for the opponent first because
    // in the event of both sides having a network following our move,
    // the opponent wins
    if (board.hasNetwork(oppSide)) {
      myBest.score = -Double.MAX_VALUE;
      return myBest;
    }
    if (board.hasNetwork(side)) {
      myBest.score = Double.MAX_VALUE;
      return myBest;
    }
    
    // if this is our search round, set our best known score to alpha
    if (color == side) {
      myBest.score = alpha;
      //myBest.score = -Double.MAX_VALUE;
    } 
    // otherwise this is the opponent's round, set its best known score
    // to beta
    else {
      myBest.score = beta;
      //myBest.score = Double.MAX_VALUE;
    }
    
    int oppColor = Board.oppColor(color);
    
    // start searching through moves to see which one offers the
    // highest probability of winning
    List moves = board.getAllValidMoves(color);
    for (Object o : moves) {
      Move curMove = (Move)o;
      // prevent NullPointerException in event of guaranteed loss in
      // upcoming moves
      if (myBest.move == null) {
        myBest.move = curMove;
      }

      // try move, then let other player see what its best move is
      board.doMove(color, curMove);
      // note: as we update alpha and beta, our search will prune
      // configurations that aren't more promising than what
      // we have already found
      oppBest = getBestMoveHelper(oppColor, alpha, beta, depth - 1);
      board.undoMove(color, curMove);
      
      // if we find that the opponent's best move (after we do "curMove")
      // raises the probability that we will win, we choose
      // "curMove" to be our best known move and update alpha,
      // which represents the best score we know we can force
      
      // note: for us (side), a higher score is better
      if (color == side && myBest.score < oppBest.score) {
        alpha = oppBest.score;
        myBest.move = curMove;
        myBest.score = oppBest.score;
      } 
      
      // if opponent finds that our best move (after it does "curMove")
      // raises the probability that it will win, it chooses
      // "curMove" to be its best known move and updates beta,
      // which represents the best score it knows it can force
      
      // note: for the opponent (oppSide), a lower score is better
      else if (color == oppSide &&
          myBest.score > oppBest.score) {
        beta = oppBest.score;
        myBest.move = curMove;
        myBest.score = oppBest.score;
      }
      
      // once the best move we know we can achieve is at least as good
      // as the best move the opponent can achieve, prune the game tree rooted
      // at this current grid
      if (alpha >= beta) {
        break;
      }
      
      // otherwise keep searching in hopes of finding salvation in a
      // better move from the list of valid moves
    }
    // return our best move and its score
    return myBest;
  }

  /**
   * Calculates the score of the given configuration of the board, giving a 
   * more positive score to a board in favor of the {@code MachinePlayer} and
   * a more negative score to a board in favor of its opponent.
   * 
   * @return       a double representing the score of the current board
   *               configuration.
   */
  private double getScore() {
    if (board.hasNetwork(oppSide)) {
      return -Double.MAX_VALUE / searchDepth +
          board.totalNetworks(oppSide);
    }
    if (board.hasNetwork(side)) {
      return Double.MAX_VALUE / searchDepth +
          board.totalNetworks(side);
    }

    List allMyPaths = board.getAllPaths(side);
    List allOppPaths = board.getAllPaths(oppSide);
    
    // key values used to determine the score //
    
    // connections between pieces (each connection is counted twice)
    int numMyConnections = board.getNumConnections(side);
    int numOppConnections = board.getNumConnections(oppSide);
    int numConnections = numMyConnections - numOppConnections;

    // length of longest path between pieces
    int myLongestPath = board.longestPathLength(allMyPaths);
    int oppLongestPath = board.longestPathLength(allOppPaths);
    int longestPath = myLongestPath - oppLongestPath;
    
    // currently possible valid moves
    int myAvailableMoves = board.getAllValidMoves(side).length();
    int oppAvailableMoves = board.getAllValidMoves(oppSide).length();
    int availableMoveDiff = myAvailableMoves - oppAvailableMoves;
    
    // number of chips in a goal
    int myChipsInGoal = board.chipsInGoal(side);
    int oppChipsInGoal = board.chipsInGoal(oppSide);
    int chipsInGoalDiff = myChipsInGoal - oppChipsInGoal;

    double score = 10 * numConnections + 2.6 * longestPath + 
        2.0 * availableMoveDiff + 2.0 * chipsInGoalDiff;
    return score;
  }
}
