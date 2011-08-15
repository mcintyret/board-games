package pieces.chess;

import game.promotiongames.ChessGame;
import pieces.PieceType;
import player.Player;

public final class Queen extends AbstractChessPiece {

  private static final PieceType type =  PieceType.QUEEN;
  
  public Queen(ChessGame game, Player player) {
    super( game, player);
  }

  @Override
  public void updateLineOfSight() {
    lineOfSight.clear();
    addDiagonalMoves();
    addLinearMoves();
  }

  @Override
  public PieceType getType() {
    return type;
  }

}
