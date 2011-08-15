package tmcintyre.boardgame.pieces;


import java.awt.Color;
import java.util.List;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.player.Player;

/**
 * 
 * 
 * @author Tom McIntyre
 * 
 */
public interface Piece {
  /**
   * Informs the <code>Piece</code> that the supplied move has been done. The
   * <code>Game</code> object takes care of updating the game state; this method
   * is used to allow the Piece to update any internal state that may be
   * relevant.
   * 
   * <p>
   * For example, in chess certain moves (such as castling, or a pawn moving 2
   * squares) are only available if the piece in question has not moved before.
   * This method can be used by a piece to keep track of the number of times it
   * has moved.
   * 
   * <p>
   * Typically this will be called on the moving piece in the <code>Move</code>
   * provided, although this is not a requirement.
   * 
   * @param move
   */
  public void doMove(Move move);

  /**
   * Returns the column of this piece's current position in the game board.
   * 
   * @return the column of this piece's current position in the game board
   */
  public int getCol();

  /**
   * Returns this piece's <code>Color</code>.
   * 
   * @return this piece's <code>Color</code>
   */
  public Color getColor();

  /**
   * Returns the <code>Game</code> in which this piece is participating.
   * 
   * @return the <code>Game</code> in which this piece is participating
   */
  public Game getGame();

  /**
   * Returns a <code>List</code> of all the legal <code>Move</code>s available
   * to this <code>Piece</code>. If there are no such moves the list is empty;
   * the list should never be <tt>null</tt>.
   * 
   * @return a <code>List</code> of all the legal <code>Move</code>s available
   *         to this <code>Piece</code>
   */
  public List<Move> getLegalMoves();

  /**
   * Returns the <code>Player</code> to whom this <code>Piece</code> belongs.
   * 
   * @return the <code>Player</code> to whom this <code>Piece</code> belongs
   */
  public Player getPlayer();

  /**
   * Returns the row of this piece's current position in the game board.
   * 
   * @return the row of this piece's current position in the game board
   */
  public int getRow();

  /**
   * Returns the <code>PieceType</code> constant associated with this
   * <code>Piece</code>.
   * 
   * @return the <code>PieceType</code> constant associated with this
   *         <code>Piece</code>
   */
  public PieceType getType();

  /**
   * Returns the character used to graphically represent this <code>Piece</code>
   * .
   * <p>
   * Note that some fonts may not be able to display all the Unicode characters
   * used. If possible use a full Unicode font, such as 'Quivira'.
   * 
   * @return
   */
  public char getSymbol();

  /**
   * Sets the column of this piece's position in the game board to the provided
   * value.
   * 
   * <p>
   * This should be done in conjunction with the <code>addPieceAt</code> method
   * in the corresponding <code>Game</code> object to ensure a consistent view
   * of the pieces position in the game board. This is achieved in the
   * <code>AbstractGame</code> implementation by calling this method as part of
   * the <code>addPieceAt</code> method.
   * 
   * 
   * @param col
   *          the new value for the column of this piece's position
   */
  public void setCol(int col);

  /**
   * Sets the row of this piece's position in the game board to the provided
   * value.
   * 
   * <p>
   * This should be done in conjunction with the <code>addPieceAt</code> method
   * in the corresponding <code>Game</code> object to ensure a consistent view
   * of the pieces position in the game board. This is achieved in the
   * <code>AbstractGame</code> implementation by calling this method as part of
   * the <code>addPieceAt</code> method.
   * 
   * 
   * @param row
   *          the new value for the row of this piece's position
   */
  public void setRow(int row);

  /**
   * Informs the <code>Piece</code> that the supplied move has been undone. The
   * <code>Game</code> object takes care of updating the game state; this method
   * is used to allow the Piece to update any internal state that may be
   * relevant.
   * 
   * <p>
   * See <code>doMove</code> for an example of the use of this method.
   * 
   * @param move
   *          the move that has been undone.
   */
  public void undoMove(Move move);

  /**
   * Updates the list of legal <code>Move</code>s available to this piece.
   */
  public void updateLegalMoves();

  /**
   * Sets the <code>Color</code> of this piece to the provided
   * <code>Color</code>.
   * 
   * @param color
   *          the new <code>Color</code> for this piece.
   */
  public void setColor(Color color);

}
