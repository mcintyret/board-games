package pieces.checkers;

import game.Move;
import game.promotiongames.CheckersGame;
import pieces.PieceType;
import player.Player;

public final class CrownedChecker extends AbstractChecker {

  private static final PieceType type = PieceType.CROWNED_CHECKER;

  public CrownedChecker(CheckersGame game, Player player) {
    super(game, player);
  }

  private void addFlyingKingMove(int rowDif, int colDif, boolean takingOnly) {
    int localRow = row + rowDif;
    int localCol = col + colDif;
    while (true) {
      SquareState state = getSquareState(localRow, localCol);
      if (state == SquareState.EMPTY) {
        if (!takingOnly) legalMoves.add(new Move(this, localRow, localCol, null));
        localRow += rowDif;
        localCol += colDif;
      } else {
        if (state != SquareState.DIFF_COLOR) return;
        SquareState nextState = getSquareState(localRow + rowDif, localCol + colDif);
        if (nextState != SquareState.EMPTY) return;
        break;
      }
    }
    legalMoves.add(new Move(this, localRow + rowDif, localCol + colDif, game.getPieceAt(localRow,
        localCol)));
  }

  private void addFlyingKingMoves(boolean takingOnly) {
    addFlyingKingMove(-1, -1, takingOnly);
    addFlyingKingMove(-1, +1, takingOnly);
    addFlyingKingMove(+1, -1, takingOnly);
    addFlyingKingMove(+1, +1, takingOnly);
  }

  @Override
  protected void addAllMoves() {
    if (game.getRules().isFlyingKings()) {
      addFlyingKingMoves(false);
    } else {
      addForwardsNonCapturingMoves();
      addForwardsCapturingMoves();
      addBackwardsNonCapturingMoves();
      addBackwardsCapturingMoves();
    }
  }

  @Override
  protected void addCapturingMoves() {
    if (game.getRules().isFlyingKings()) {
      addFlyingKingMoves(true);
    } else {
      addForwardsCapturingMoves();
      addBackwardsCapturingMoves();
    }
  }

  @Override
  public PieceType getType() {
    return type;
  }

}
