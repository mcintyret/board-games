package pieces.chess;

import game.Game;
import game.Move;
import game.promotiongames.ChessGame;

import java.util.LinkedList;
import java.util.List;

import pieces.AbstractPiece;
import pieces.Piece;
import pieces.PieceType;
import player.Player;

/**
 * A skeletal implementation of a chess piece.
 * 
 * <p>
 * Provides implementations of some @ Piece} methods common to all chess pieces.
 * 
 * <p>
 * Provides several methods for adding certain types of moves, to prevent
 * repeated code in the concrete classes. For example, Queens and Bishops may
 * move diagonally in the same manner, and Queens and Rooks may move linearly in
 * the same manner. Methods for adding such moves if legal are provided here.
 * 
 * <p>
 * For a chess piece the question of whether a move is legal does not depend on
 * just whether a piece can physically reach that location; it also depends on
 * whether making that move would leave the King in check. This in turn depends
 * on the moves that will be available to the pieces of the opponent player once
 * the move in question has been carried out. To deal with this situation the
 * piece keeps two lists of moves: one list that contains every move the piece
 * can physically make regardless of check (the 'line of sight' list), and
 * another list containing only those moves that are legal (the 'legal moves'
 * list inherited from <code>AbstractPiece</code>). Updating the legal moves is
 * a two-stage process: first update the line-of-sight moves, then remove the
 * moves that would leave the King in check. This process is transparent to any
 * object that deals with this piece through the <code>Piece</code> interface.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractChessPiece extends AbstractPiece {

  protected final List<Move> lineOfSight = new LinkedList<Move>();

  protected final ChessGame game;

  public AbstractChessPiece(ChessGame game, Player player) {
    super(player);
    this.game = game;
  }

  public List<Move> getLineOfSight() {
    return lineOfSight;
  }

  @Override
  public void updateLegalMoves() {
    legalMoves.clear();
    updateLineOfSight();
    legalMoves.addAll(lineOfSight);
    removeMovesThatLeaveKingInCheck();
  }

  /**
   * Updates the lineOfSight list to contain all moves that this
   * <code>Piece</code> can physically make, regardless of check.
   * 
   * <p>
   * The implementation depends on the capabilities of this piece.
   */
  public abstract void updateLineOfSight();

  protected void addMove(int row, int col, List<Move> moves) {
    Piece taken = game.getPieceAt(row, col);
    moves.add(new Move(this, row, col, taken));
  }

  private void removeMovesThatLeaveKingInCheck() {
    // Loop over each move potentially available to this piece.
    for (Move move : lineOfSight) {
      // Instruct the game to process the move as a dummy move.
      game.doMove(move, true);

      // Loop over each of the opponent's pieces and update its line of sight
      // list to reflect the updated game state.
      PIECES: for (Piece piece : game.getCurrentPlayer().getPieces()) {
        AbstractChessPiece acp = (AbstractChessPiece) piece;
        acp.updateLineOfSight();

        // Loop over each of the opponent piece's line of sight moves. If any of
        // the opponent piece's moves can capture the King then the move by this
        // piece being tested is not a legal move.
        for (Move opponentMove : acp.getLineOfSight()) {
          Piece taken = opponentMove.getCapturedPiece();
          if (taken == null) continue;
          if (taken.getType() != PieceType.KING) continue;
          legalMoves.remove(move);
          break PIECES;
        }
      }
      // Instruct the game to undo the move.
      game.undoMove(true);
    }
  }

  protected void addDiagonalMoves() {
    // Moving up and to the left
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
      if (addMoveIfValid(i, j, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving down and to the left
    for (int i = row + 1, j = col - 1; i < 8 && j >= 0; i++, j--) {
      if (addMoveIfValid(i, j, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving up and to the right
    for (int i = row - 1, j = col + 1; i >= 0 && j < 8; i--, j++) {
      if (addMoveIfValid(i, j, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving down and to the right
    for (int i = row + 1, j = col + 1; i < 8 && j < 8; i++, j++) {
      if (addMoveIfValid(i, j, lineOfSight) != SquareState.EMPTY) break;
    }
  }

  protected void addLinearMoves() {
    // Moving down the board
    for (int i = row + 1; i < 8; i++) {
      if (addMoveIfValid(i, col, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving up the board
    for (int i = row - 1; i >= 0; i--) {
      if (addMoveIfValid(i, col, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving right
    for (int i = col + 1; i < 8; i++) {
      if (addMoveIfValid(row, i, lineOfSight) != SquareState.EMPTY) break;
    }
    // Moving left
    for (int i = col - 1; i >= 0; i--) {
      if (addMoveIfValid(row, i, lineOfSight) != SquareState.EMPTY) break;
    }
  }

  protected SquareState addMoveIfValid(int row, int col, List<Move> moves) {
    SquareState state = getSquareState(row, col);
    if (state == SquareState.OUT_OF_BOUNDS) return state;
    if (state == SquareState.OWN_COLOR) return state;
    addMove(row, col, moves);
    return state;
  }

  @Override
  public Game getGame() {
    return game;
  }

}
