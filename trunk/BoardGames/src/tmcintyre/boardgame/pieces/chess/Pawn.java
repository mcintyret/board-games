package tmcintyre.boardgame.pieces.chess;


import java.util.List;

import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.promotiongames.ChessGame;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.pieces.PieceType;
import tmcintyre.boardgame.player.Player;

public final class Pawn extends AbstractChessPiece {

  private static final PieceType type = PieceType.PAWN;

  private int moveCount = 0;

  public Pawn(ChessGame game, Player player) {
    super(game, player);
  }

  @Override
  public void doMove(Move move) {
    super.doMove(move);
    moveCount++;
  }

  @Override
  public void undoMove(Move move) {
    super.undoMove(move);
    moveCount--;
  }

  @Override
  public void updateLineOfSight() {
    lineOfSight.clear();

    boolean isBlack;

    if (color == game.getWhiteColor()) {
      isBlack = false;
    } else if (color == game.getBlackColor()) {
      isBlack = true;
    } else {
      throw new AssertionError("Illegal Color");
    }
    int nextRow = isBlack ? row + 1 : row - 1;

    addMoveIfCanNotTake(nextRow, col, lineOfSight);
    addMoveIfCanTake(nextRow, col - 1, lineOfSight);
    addMoveIfCanTake(nextRow, col + 1, lineOfSight);

    if (moveCount > 0) {
      // Check for en-passant
      int viableRow = isBlack ? 4 : 3;
      if (row != viableRow) return;

      if (getSquareState(nextRow, col - 1) == SquareState.EMPTY) {
        Piece left = game.getPieceAt(row, col - 1);
        if (testEnPassant(left)) lineOfSight.add(new Move(this, nextRow, col - 1, left));
      }
      if (getSquareState(nextRow, col + 1) == SquareState.EMPTY) {
        Piece right = game.getPieceAt(row, col + 1);
        if (testEnPassant(right)) lineOfSight.add(new Move(this, nextRow, col + 1, right));
      }

    } else {
      // Option of moving 2 squares since this is the first move.
      int nextNextRow = isBlack ? 3 : 4;
      addMoveIfCanNotTake(nextNextRow, col, lineOfSight);
    }
  }

  private void addMoveIfCanNotTake(int row, int col, List<Move> moves) {
    if (getSquareState(row, col) != SquareState.EMPTY) return;
    addMove(row, col, moves);
  }

  private void addMoveIfCanTake(int row, int col, List<Move> moves) {
    if (getSquareState(row, col) != SquareState.DIFF_COLOR) return;
    addMove(row, col, moves);
  }

  private boolean testEnPassant(Piece piece) {
    if (piece == null) return false;
    if (piece.getType() != PieceType.PAWN) return false;
    // En-passant only valid if the pawn moved 2 squares in its first turn.
    if (((Pawn) piece).moveCount != 1) return false;
    return true;
  }

  @Override
  public PieceType getType() {
    return type;
  }
}
