package tmcintyre.boardgame.pieces.chess;

import tmcintyre.boardgame.game.promotiongames.ChessGame;
import tmcintyre.boardgame.pieces.PieceType;
import tmcintyre.boardgame.player.Player;

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
