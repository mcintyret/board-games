package pieces.chess;

import game.promotiongames.ChessGame;
import pieces.PieceType;
import player.Player;

public final class Knight extends AbstractChessPiece {

  private static final PieceType type = PieceType.KNIGHT;
  
  public Knight( ChessGame game, Player player) {
    super(game, player);
  }

  @Override
  public void updateLineOfSight() {
    lineOfSight.clear();
    addMoveIfValid(row + 2, col + 1, lineOfSight);
    addMoveIfValid(row + 2, col - 1, lineOfSight);
    addMoveIfValid(row + 1, col + 2, lineOfSight);
    addMoveIfValid(row + 1, col - 2, lineOfSight);
    addMoveIfValid(row - 1, col + 2, lineOfSight);
    addMoveIfValid(row - 1, col - 2, lineOfSight);
    addMoveIfValid(row - 2, col + 1, lineOfSight);
    addMoveIfValid(row - 2, col - 1, lineOfSight);
  }

  @Override
  public PieceType getType() {
    return type;
  }
  
  

}
