/* Chip.java */
package game;

/**
 * A chip located on a single square on a Network game board.  It is immutable.
 * 
 * @author Jason Goldfine-Middleton
 * @version 1.0, 8 October 2014
 */
class Chip {
  
  /**
   * The x-coordinate of this chip.
   */
  final int x;
  
  /**
   * The y-coordinate of this chip.
   */
  final int y;
  
  /**
   * The color of this chip.
   */
  final int side;
  
  /**
   * Constructor for an invalid chip.
   */
  Chip() {
    this(-1, -1, Board.NONE);
  }
  
  /**
   * Constructor for a chip at square (x,y) for a given side, either black or
   * white.
   *     
   * @param x         the x-coordinate of the chip's location
   * @param y         the y-coordinate of the chip's location
   * @param side      the color of the chip, either Board.BLACK or Board.WHITE
   */
  Chip(int x, int y, int side) {
    this.x = x;
    this.y = y;
    this.side = side;
  }
  
  /**
   * Compares an {@code Object} with this {@code Chip} and returns true if they
   * represent the same chip.
   * 
   * @param o the {@code Object} to compare.
   * @return true if o is representing the same chip as this {@code Chip}.
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Chip)) {
      return false;
    }
    Chip c = (Chip)o;
    return x == c.x && y == c.y && side == c.side;
  }
  
  /**
   * Returns a string representation of this chip.
   * @return the string representing this chip.
   */
  @Override
  public String toString() {
    return "" + x + y;
  }
}
