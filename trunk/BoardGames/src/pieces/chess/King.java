package pieces.chess;

import game.Move;
import game.promotiongames.ChessGame;
import pieces.Piece;
import pieces.PieceType;
import player.Player;

public final class King extends AbstractChessPiece {

  private static final PieceType type = PieceType.KING;

  private int moveCount = 0;

  public King(ChessGame game, Player player) {
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

    for (int i = col - 1; i <= col + 1; i++) {
      for (int j = row - 1; j <= row + 1; j++) {
        // don't need to test if this puts the king in check; this
        // will get tested for all pieces when they have done updating
        addMoveIfValid(j, i, lineOfSight);
      }
    }

    if (moveCount > 0) return;
    castleLeft();
    castleRight();
  }

  private void castleLeft() {
    if (!checkRookCanCastle(0)) return;
    for (int i = 1; i < col; i++) {
      if (getSquareState(row, i) != SquareState.EMPTY) return;
    }
    if (putsInCheck(row, col - 1)) return;
    if (putsInCheck(row, col - 2)) return;

    Move castleLeftMove = new Move(this, row, col - 2, null, color + " castled to the left");
    castleLeftMove.setNextMove(new Move(game.getPieceAt(row, 0), row, 3, null, null));
    lineOfSight.add(castleLeftMove);
  }

  private void castleRight() {
    if (!checkRookCanCastle(game.getBoardWidth() - 1)) return;
    for (int i = game.getBoardWidth() - 2; i > col; i--) {
      if (getSquareState(row, i) != SquareState.EMPTY) return;
    }
    if (putsInCheck(row, col + 1)) return;
    if (putsInCheck(row, col + 2)) return;

    Move castleRightMove = new Move(this, row, col + 2, null, color + " castled to the right");
    castleRightMove.setNextMove(new Move(game.getPieceAt(row, game.getBoardWidth() - 1), row, 5,
        null, null));
    lineOfSight.add(castleRightMove);
  }

  private boolean checkRookCanCastle(int col) {
    Piece rook = game.getPieceAt(row, col);
    if (rook == null) return false;
    if (rook.getType() != PieceType.ROOK) return false;
    return (((Rook) rook).getMoveCount() == 0);

  }

  private boolean putsInCheck(int row, int col) {
    for (Player player : game.getPlayers()) {
      if (player == this.player) continue;
      for (Piece p : player.getPieces()) {
        for (Move m : p.getLegalMoves()) {
          if (m.destRow() == row && m.destCol() == col) return true;
        }
      }
    }
    return false;
  }

  @Override
  public PieceType getType() {
    return type;
  }

}
