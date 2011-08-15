package game;

import game.dicegames.DiceGame;
import game.promotiongames.PromotionGame;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import pieces.Piece;
import player.Player;

/**
 * Represents a turn-based board game, where the board is a rectangular grid of
 * squares.
 * 
 * <p>
 * The fundamental interface for any object that represents a game. While very
 * simple games may just implement Game, most will probably implement a more
 * specific subinterface.
 * 
 * 
 * 
 * @author Tom McIntyre
 * @see DiceGame
 * @see PromotionGame
 * @see AbstractGame
 * 
 */

public interface Game {

  /**
   * Adds an <code>Observer</code> to the game.
   * 
   * @param observer
   *          the <code>Observer</code> to be added
   */
  public void addObserver(Observer observer);

  /**
   * Adds a <code>Piece</code> to the game at the position specified.
   * 
   * <p>
   * If the piece already occupied another position in the game it will be
   * removed from that position.
   * 
   * @param row
   *          the row in which the <code>Piece</code> will be added
   * @param col
   *          the column in which the <code>Piece</code> will be added
   * @param piece
   *          the <code>Piece</code> to be added
   * @throws IndexOutOfBoundsException
   *           if row and col do not specify a valid board position
   */
  public void addPieceAt(int row, int col, Piece piece);

  /**
   * Adds <code>Player</code>s to the game.
   * 
   * @param players
   *          the <code>List</code> of <code>Player</code>s to be added
   * @throws
   * 
   */
  public void addPlayers(List<Player> players);

  /**
   * Instructs the game to change the <code>Color</code> of the current player.
   * 
   * @param newColor
   *          the <code>Color</code> to which the current player is to be
   *          changed
   */
  public void changeCurrentPlayerColor(Color newColor);

  /**
   * Returns <code>true</code> if the current player has won the game. Currently
   * no support for draws.
   * 
   * @return <code>true</code> if the current player has won the game
   */
  public boolean checkWinConditions();

  /**
   * Processes the <code>Move</code> and updates the game accordingly.
   * 
   * If isDummy is <code>true</code> the move is treated as a dummy move.
   * Observers are not notified of the move, and win conditions are not checked
   * for. Typically a dummy move will be undone, returning the game to its state
   * before the move. Dummy moves are useful when, for example, a piece must
   * look several moves ahead to learn which moves are legal now.
   * 
   * If isDummy is <code>false</code> the move is treated as a real move.
   * Observers are notified and win conditions checked for.
   * 
   * @param move
   *          the <code>Move</code> to be processed
   * @param isDummy
   *          determines whether the move is to be treated as a dummy move
   */
  public void doMove(Move move, boolean isDummy);

  /**
   * Returns the <code>Color</code> of the game board at the specified position.
   * 
   * @param row
   * @param col
   * @return the <code>Color</code> of the game board at the specified position
   * @throws IndexOutOfBoundsException
   *           if row and col do not specify a valid board position
   */
  public Color getBoardColorAt(int row, int col);

  /**
   * Returns the height of the game board.
   * 
   * @return the height of the game board
   */
  public int getBoardHeight();

  /**
   * Returns the width of the game board.
   * 
   * @return the width of the game board.
   */
  public int getBoardWidth();

  /**
   * Returns the <code>Player</code> who's turn it is to act.
   * 
   * @return the <code>Player</code> who's turn it is to act
   */
  public Player getCurrentPlayer();

  /**
   * Returns the GameType constant associated with this game.
   * 
   * @return the GameType constant associated with this game
   */
  public GameType getGameType();

  /**
   * Returns the most recently played <code>Move</code> in the game. Editing
   * this <code>Move</code> may affect the accuracy of methods that use
   * historical moves, such as <code>undoMove</code>.
   * 
   * @return the most recently played <code>Move</code>
   */
  public Move getLastMove();

  /**
   * Returns the set of customizable options specific to this game type.
   * 
   * <p>
   * The set of options is implemented as a map. The key is a
   * <code>String</code> describing the parameter in question, and the value is
   * an array of <code>String</code>s containing all the available choices for
   * that parameter. If the value is <code>null</code> it is interpreted as
   * there being no limit on the available choices.
   * 
   * @return a map describing the set of customizable options specific to this
   *         game type
   * @see implementSelectedOptions
   */
  public Map<String, String[]> getGameSpecificOptions();

  /**
   * Returns the <code>Piece</code> at the specified position, or
   * <code>null</code> if the position is empty.
   * 
   * This is a convenience method for games where more than one piece never
   * occupies the same position. Games that cannot make this guarantee should
   * use <code>getPiecesAt</code> to see all the pieces that occupy that
   * position.
   * 
   * <p>
   * Game-specific options should be selected and implemented before the
   * <code>start</code> method is called.
   * 
   * @param row
   * @param col
   * @return the <code>Piece</code> at the specified position, if any
   * @throws IndexOutOfBoundsException
   *           if row and col do not specify a valid board position
   */
  public Piece getPieceAt(int row, int col);

  /**
   * Returns a <code>List</code> of all the <code>Piece</code>s at the specified
   * position.
   * 
   * @param row
   * @param col
   * @return a <code>List</code> of all the <code>Piece</code>s at the specified
   *         position
   * @throws IndexOutOfBoundsException
   *           if row and col do not specify a valid board position
   */
  public List<Piece> getPiecesAt(int row, int col);

  /**
   * Returns the <code>List</code> of <code>Player</code>s currently in the
   * game.
   * 
   * @return the <code>List</code> of <code>Player</code>s currently in the game
   */
  public List<Player> getPlayers();

  /**
   * Returns <tt>true</tt> if the current player's turn has ended as a result of
   * the <code>Move</code> provided and any other relevant game state.
   * 
   * @param move
   *          the <code>Move</code> just performed by the current player
   * @return <tt>true</tt> if the current player's turn is over, <tt>false</tt>
   *         otherwise
   */
  public boolean isTurnOver(Move move);

  /**
   * Removes the specified <code>Piece</code> from the game.
   * 
   * @param piece
   *          the piece to be removed
   */
  public void removePiece(Piece piece);

  /**
   * Simultaneously sets both the height and width of the game board.
   * 
   * @param boardHeight
   *          the height of the board (number of rows)
   * @param boardWidth
   *          the width of the board (number of columns)
   * @throws IllegalArgumentException
   *           if either of the board dimensions are non-positive
   */
  public void setBoardDimensions(int boardHeight, int boardWidth);

  /**
   * Implements the options selected by the user after a call to
   * <code>getOptions</code>.
   * 
   * <p>
   * The selected options are represented as a map. The key is a
   * <code>String</code> describing the parameter in question, and the value is
   * a <code>String</code> describing the selection made. This information is
   * parsed and the specific game variables updated accordingly.
   * 
   * <p>
   * Game-specific options should be selected and implemented before the
   * <code>start</code> method is called.
   * 
   * @param selectedOptions
   *          a map describing the options selected by the user
   */
  public void implementSelectedOptions(Map<String, String> selectedOptions);

  /**
   * Starts the game. Typically this will involve initializing the game state,
   * adding any <code>Observer</code>s that need to be added, and notifying the
   * <code>Observer</code>s that the game has started.
   */
  public void start();

  /**
   * Undoes the most recent <code>Move</code> that was sent as a parameter to
   * <code>doMove</code>. Pieces are returned to their previous positions and
   * the current player is updated.
   * 
   * if <code>isDummy</code> is <tt>true</tt> it is assumed that the move in
   * question was itself treated as a dummy move when <code>doMove</code> was
   * called - that is, the <code>isDummy</code> parameter for
   * <code>doMove</code> was also set to <tt>true</tt>. Any actions that were
   * skipped when doing a dummy move are also skipped when undoing that move.
   * 
   * <p>
   * If the corresponding call to <code>doMove</code> had <code>isDummy</code>
   * set to <tt>false</tt> or, inversely, if the call to <code>undoMove</code>
   * has <code>isDummy</code> set to <tt>false</tt> and the corresponding call
   * to <code>doMove</code> had <code>isDummy</code> set to <tt>true</tt>, the
   * result may be unpredictable.
   * 
   * @param isDummy
   */
  public void undoMove(boolean isDummy);

}
