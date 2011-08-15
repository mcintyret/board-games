package tmcintyre.boardgame.game.promotiongames;

import tmcintyre.boardgame.game.AbstractGame;
import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.Observer;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.pieces.PieceType;

/**
 * A skeletal implementation of a <code>PromotionGame</code>.
 * 
 * <p>
 * Provides an implementation of the <code>doPromotion</code> method, together
 * with a modified version of the <code>doMove</code> method, that can be used
 * by most <code>PromotionGame</code>s.
 * 
 * <p>
 * Other <code>PromotionGame</code> methods are more game-specific and therefore
 * not suitable for implementation here.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractPromotionGame extends AbstractGame implements PromotionGame {

  protected AbstractPromotionGame(GameType gameType) {
    super(gameType);
  }

  /**
   * Overrides <code>AbstractGame.doMove</code>. Checks to see whether the
   * <code>Move</code> being performed will lead to promotion before calling the
   * Overridden super method.
   * 
   * <p>
   * This way the promotion <code>Move</code> is added to the original
   * <code>Move</code> before the moves are processed by
   * <code>AbstractGame.doMove</code>. The result is that the promotion is
   * implemented within the call to <code>doMove</code>.
   */
  @Override
  public void doMove(Move move, boolean dummy) {
    if (!dummy && checkPromotion(move)) {
      for (Observer o : observers) {
        o.notifyOnPromotion(move);
      }
    }
    super.doMove(move, dummy);
  }

  /**
   * Performs the promotion of the moving piece in the provided Move to the
   * specified PieceType.
   * 
   * <p>
   * The promotion is implemented as an additional <code>Move</code> where the
   * new piece (the result of the promotion) captures the old piece (the piece
   * getting promoted). This <code>Move</code> is then linked to the original
   * <code>Move</code> - the one supplied as an argument - as its next move.
   * 
   * @see {@link Move} for more information
   */
  @Override
  public void doPromotion(Move move, PieceType newType) {
    Piece pieceBeforePromotion = move.getMovingPiece();
    int row = move.destRow();
    int col = move.destCol();

    String moveName = pieceBeforePromotion.getColor().toString() + " "
        + pieceBeforePromotion.getType() + " promoted to " + newType;
    Piece pieceAfterPromotion = newType.newInstance(this,pieceBeforePromotion.getPlayer());

    Move promotionMove = new Move(pieceAfterPromotion, row, col, pieceBeforePromotion, moveName);
    promotionMove.setDestroyMovingPieceOnUndo(true);
    move.setNextMove(promotionMove);
  }

}
