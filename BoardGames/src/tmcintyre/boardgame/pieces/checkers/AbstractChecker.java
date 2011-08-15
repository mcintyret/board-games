package tmcintyre.boardgame.pieces.checkers;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.promotiongames.CheckersGame;
import tmcintyre.boardgame.pieces.AbstractPiece;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.player.Player;

/**
 * A checkers piece.
 * 
 * <p>
 * This class provides a set of methods that can be used by a checkers piece to
 * add legal moves. It is up to the concrete classes to select which
 * combinations of methods to use, depending on the piece's abilities and the
 * <code>CheckersRule</code> rules in play.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractChecker extends AbstractPiece {

  protected final CheckersGame game;

  public AbstractChecker(CheckersGame game, Player player) {
    super(player);
    this.game = game;
  }

  /**
   * Adds a capturing move in the diagonal direction specified by rowDif and
   * colDif, if that move is legal.
   * 
   * <p>
   * A capturing move is legal if the adjacent square is occupied by a piece of
   * the opposite color, and if the square beyond it is free.
   * 
   * @param rowDif
   * @param colDif
   */
  private void addCapturingMove(int rowDif, int colDif) {
    SquareState oneSquareAway = getSquareState(row + rowDif, col + colDif);
    if (oneSquareAway != SquareState.DIFF_COLOR) return;
    SquareState twoSquaresAway = getSquareState(row + 2 * rowDif, col + 2 * colDif);
    if (twoSquaresAway != SquareState.EMPTY) return;
    legalMoves.add(new Move(this, row + 2 * rowDif, col + 2 * colDif, game.getPieceAt(row + rowDif,
        col + colDif)));
  }

  /**
   * Adds a non-capturing move in the diagonal direction specified by rowDif and
   * colDif, if that move is legal.
   * 
   * 
   * <p>
   * A non-capturing move is legal if the adjacent square is free.
   * 
   * @param rowDif
   * @param colDif
   */
  private void addNonCapturingMove(int rowDif, int colDif) {
    SquareState oneSquareAway = getSquareState(row + rowDif, col + colDif);
    if (oneSquareAway != SquareState.EMPTY) return;
    legalMoves.add(new Move(this, row + rowDif, col + colDif));
  }

  /**
   * Adds all the legal capturing moves in the specified direction.
   * 
   * The direction is from the point of view of this piece. The arguments passed
   * on to <code>addCapturingMove</code> therefore depend on both the direction
   * specified and the <code>Color</code> of this <code>Piece</code>.
   * 
   * @param forwards
   *          <tt>true</tt> if this piece is to move forwards, <tt>false</tt>
   *          otherwise.
   */
  private void addCapturingMoves(boolean forwards) {
    boolean upBoard = color == game.getBlackColor() ^ forwards;
    int rowDif = upBoard ? -1 : +1;
    addCapturingMove(rowDif, +1);
    addCapturingMove(rowDif, -1);
  }

  /**
   * Adds all the legal non-capturing moves in the specified direction.
   * 
   * The direction is from the point of view of this piece. The arguments passed
   * on to <code>addNonCapturingMove</code> therefore depend on both the
   * direction specified and the <code>Color</code> of this <code>Piece</code>.
   * 
   * @param forwards
   *          <tt>true</tt> if this piece is to move forwards, <tt>false</tt>
   *          otherwise.
   */
  private void addNonCapturingMoves(boolean forwards) {
    boolean upBoard = color == game.getBlackColor() ^ forwards;
    int rowDif = upBoard ? -1 : +1;
    addNonCapturingMove(rowDif, +1);
    addNonCapturingMove(rowDif, -1);
  }

  @Override
  public void updateLegalMoves() {
    legalMoves.clear();
    Move lastMove = game.getLastMove();
    if (lastMove == null) {
      // If there are no previous moves then all legal moves are valid
      addAllMoves();
    } else {
      Piece lastMovingPiece = lastMove.getMovingPiece();
      if (lastMovingPiece.getPlayer() != player) {
        // If the previous move was by the other player then all legal moves are
        // valid.
        addAllMoves();
      } else if (this == lastMovingPiece) {
        // Otherwise, only the piece that moved last turn may move this turn.
        // Futhermore, the only legal moves are taking moves.
        addCapturingMoves();
      }
    }
  }

  /**
   * Adds all legal moves available to this piece.
   */
  protected abstract void addAllMoves();

  /**
   * Adds just the capturing moves available to this <code>Piece</code>.
   * 
   * <p>
   * This is called when this <code>Piece</code> has just captured another
   * <code>Piece</code> in a previous move. Therefore the only legal moves are
   * capturing moves.
   */
  protected abstract void addCapturingMoves();

  protected void addForwardsCapturingMoves() {
    addCapturingMoves(true);
  }

  protected void addBackwardsCapturingMoves() {
    addCapturingMoves(false);
  }

  protected void addForwardsNonCapturingMoves() {
    addNonCapturingMoves(true);
  }

  protected void addBackwardsNonCapturingMoves() {
    addNonCapturingMoves(false);
  }

  @Override
  public Game getGame() {
    return game;
  }

}
