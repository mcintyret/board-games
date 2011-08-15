package tmcintyre.boardgame.player;


import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.pieces.Piece;

/**
 * A <code>Player</code> - an active participant in a {@link Game}.
 * 
 * <p>
 * The <code>Player</code> class is intentionally kept light. It is primarily a
 * container for a collection of <code>Piece</code>s on the same team, together
 * with a name and <code>Color</code> to identify that team. A
 * <code>Player</code> does not even know that it is part of a game, let alone
 * the type of game it is in. All the game-specific actions are taken by the
 * <code>Game</code> object that contains this <code>Player</code>, or by the
 * <code>Piece</code>s that this <code>Player</code> holds. This avoids having
 * to subclass an abstract Player class for each type of game implemented and
 * simplifies having the same <code>Player</code> play in different game types.
 * 
 * @author Tom McIntyre
 * 
 */
public class Player {
  private static int defaultPlayerCount = 1;

  protected Color color;

  protected final String name;

  protected final List<Piece> pieces = new LinkedList<Piece>();
  protected final List<Move> allLegalMoves = new LinkedList<Move>();

  public Player(Color color) {
    this(color, "Player " + defaultPlayerCount++);
  }

  public Player(Color color, String name) {
    this.color = color;
    this.name = name;
  }

  /**
   * Adds the provided <code>Piece</code> to this <code>Player</code>'s list of
   * <code>Piece</code>s.
   * 
   * @param piece
   *          the <code>Piece</code> to be added.
   */
  public void addPiece(Piece piece) {
    pieces.add(piece);
  }

  /**
   * Returns the <code>Color</code> of this <code>Player</code>.
   * 
   * @return the <code>Color</code> of this <code>Player</code>
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the <code>Color</code> of this <code>Player</code> to the provided
   * <code>Color</code>.
   * 
   * <p>
   * Also calls the <code>setColor</code> method on all of the
   * <code>Piece</code>s owned by this <code>Player</code>.
   * 
   * @param color
   *          the new <code>Color</code> for this <code>Player</code>
   */
  public void setColor(Color color) {
    this.color = color;
    for (Piece piece : pieces) {
      piece.setColor(color);
    }
  }

  /**
   * Returns the name of this <code>Player</code>.
   * 
   * @return the name of this <code>Player</code>
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the <tt>List</tt> of <code>Piece</code>s owned by this
   * <code>Player</code>.
   * 
   * @return the <tt>List</tt> of <code>Piece</code>s owned by this
   *         <code>Player</code>
   */
  public List<Piece> getPieces() {
    return pieces;
  }

  /**
   * Removes the specified <code>Piece</code> from this <code>Player</code>'s
   * list of <code>Piece</code>s.
   * 
   * @param piece
   *          the <code>Piece</code> to be removed
   */
  public void removePiece(Piece piece) {
    pieces.remove(piece);
  }

  /**
   * Updates all the legal <code>Move</code>s for each <code>Piece</code> owned
   * by this <code>Player</code>.
   * 
   * <p>
   * For convenience the <code>Player</code> also keeps track of all the legal
   * moves available to all of its pieces.
   */
  public void updateLegalMoves() {
    allLegalMoves.clear();
    for (Piece piece : pieces) {
      piece.updateLegalMoves();
      allLegalMoves.addAll(piece.getLegalMoves());
    }
  }

  /**
   * Returns a list of all the legal moves available to this player - that is,
   * all the legal moves for each <code>Piece</code> owned by this player.
   * 
   * @return
   */
  public List<Move> getAllLegalMoves() {
    return allLegalMoves;
  }

}
