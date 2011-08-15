package pieces.chess;

import game.promotiongames.ChessGame;
import pieces.PieceType;
import player.Player;

public final class Bishop extends AbstractChessPiece {

  private static final PieceType type = PieceType.BISHOP;

  public Bishop(ChessGame game, Player player) {
    super(game, player);
  }

  @Override
  public void updateLineOfSight() {
    lineOfSight.clear();
    addDiagonalMoves();
  }

  @Override
  public PieceType getType() {
    return type;
  }
}
