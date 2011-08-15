package tmcintyre.boardgame.pieces.chess;

import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.promotiongames.ChessGame;
import tmcintyre.boardgame.pieces.PieceType;
import tmcintyre.boardgame.player.Player;

public final class Rook extends AbstractChessPiece {

  private static final PieceType type = PieceType.ROOK;

  private int moveCount = 0;

  public Rook(ChessGame game, Player player) {
    super(game, player);
  }

  @Override
  public void doMove(Move move) {
    super.doMove(move);
    moveCount++;
  }

  public int getMoveCount() {
    return moveCount;
  }

  @Override
  public void undoMove(Move move) {
    super.undoMove(move);
    moveCount--;
  }

  @Override
  public void updateLineOfSight() {
    lineOfSight.clear();
    addLinearMoves();
  }

  @Override
  public PieceType getType() {
    return type;
  }

}
