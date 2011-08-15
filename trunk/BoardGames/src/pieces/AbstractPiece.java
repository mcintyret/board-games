package pieces;

import game.Move;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import player.Player;

/**
 * A skeletal implementation of the {@link Piece} interface.
 * 
 * <p>
 * Provides default implementation of many of the methods in the
 * <code>Piece</code> interface.
 * 
 * <p>
 * Also provides the <code>getSquareState</code> method to determine the state
 * of a square on the game board. This can be used by more specific Piece
 * implementations to help determine which moves are legal.
 * 
 * <p>
 * Note that the <code>Piece.getGame</code> method is not implemented here,
 * neither is there a <code>Game</code> object field. This forces subclasses to
 * implement their own <code>getGame</code> method and provide their own
 * <code>Game</code> object. This allows subclasses to have as a member a more
 * specific implementation of <code>Game</code>, giving access to methods
 * specific to that game type that may be useful in, for example, determining
 * whether a potential move is legal or not.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractPiece implements Piece {
  protected int col;
  protected int row;

  protected Color color;
  protected final List<Move> legalMoves = new LinkedList<Move>();
  protected final Player player;


  public AbstractPiece(Player player) {
    this.player = player;
    this.color = player.getColor();
    player.addPiece(this);
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void doMove(Move move) {
    // Do nothing in most cases
  }

  public int getCol() {
    return col;
  }

  public Color getColor() {
    return color;
  }

  public List<Move> getLegalMoves() {
    return legalMoves;
  }

  public Player getPlayer() {
    return player;
  }

  public int getRow() {
    return row;
  }

  public char getSymbol() {
    return getType().symbol();
  }

  public void setCol(int col) {
    this.col = col;
  }

  public void setRow(int row) {
    this.row = row;
  }

  @Override
  public String toString() {
    return color + " " + getType() + " " + row + ", " + col;
  }

  public void undoMove(Move move) {
    // do nothing in most cases
  }

  protected SquareState getSquareState(int row, int col) {
    if (col < 0 || row < 0) return SquareState.OUT_OF_BOUNDS;
    if (col >= getGame().getBoardWidth() || row >= getGame().getBoardHeight())
      return SquareState.OUT_OF_BOUNDS;
    List<Piece> pieces = getGame().getPiecesAt(row, col);
    if (pieces.size() == 0) return SquareState.EMPTY;
    Piece piece = pieces.get(0);
    if (piece.getColor() == color) return SquareState.OWN_COLOR;
    return SquareState.DIFF_COLOR;
  }

  protected enum SquareState {
    DIFF_COLOR,
    EMPTY,
    OUT_OF_BOUNDS,
    OWN_COLOR;
  }

}
