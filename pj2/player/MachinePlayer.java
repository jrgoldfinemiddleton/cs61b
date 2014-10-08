/* MachinePlayer.java */
package player;

import game.Board;
import game.Engine;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {
  
  /* Instance Fields
   * ai           the engine that will determine this player's moves
   * oppSide      the color of the opponent
   * side         the color of this player
   */
  Engine ai;
  int oppSide;
  int side;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    // depth must always be at least 1
    this(color, 3);
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    side = color == 0 ? Board.BLACK: Board.WHITE;
    oppSide = Board.oppColor(side);
    ai = new Engine(new Board(), side, searchDepth);
  } 

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  @Override
  public Move chooseMove() {
    Move m = ai.getBestMove(); 
    ai.board.doMove(side, m);
    return m;
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  @Override
  public boolean forceMove(Move m) {
    boolean moveValid = ai.board.isValidMove(side, m);
    if (moveValid) {
      ai.board.doMove(side, m);
    }    
    return moveValid;
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  @Override
  public boolean opponentMove(Move m) {
    boolean moveValid = ai.board.isValidMove(oppSide, m);
    if (moveValid) {
      ai.board.doMove(oppSide, m);
    }
    return moveValid;
  }
}
