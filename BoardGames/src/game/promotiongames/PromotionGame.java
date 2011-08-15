package game.promotiongames;

import game.Game;
import game.Move;
import pieces.PieceType;

/**
 * A game in which pieces can be promoted.
 * 
 * <p>
 * Typically a promotion occurs under certain conditions (eg a pawn reaching the
 * end of the board in chess) and the promoted piece is replaced with another
 * piece of greater or equal strength.
 * 
 * <p>
 * The new piece belongs to the same player as the old piece, and is the same
 * color.
 * 
 * @author Tom McIntyre
 * 
 */
public interface PromotionGame extends Game {

  /**
   * Returns <tt>true</tt> if the provided <code>Move</code> results in the
   * moving piece being promoted.
   * 
   * @param move
   * @return <tt>true</tt> if the piece is to be promoted, <tt>false</tt>
   *         otherwise.
   */
  public boolean checkPromotion(Move move);

  /**
   * Performs the promotion of the moving piece in the provided
   * <code>Move</code> to the specified <code>PieceType</code>.
   * 
   * @param move
   * @param type
   *          the <code>PieceType</code> to which the piece is being promoted.
   */
  public void doPromotion(Move move, PieceType type);

  /**
   * Returns an array of <code>PieceType</code> constants. These are the
   * <code>PieceType</code>s to which the piece being promoted may be promoted.
   * 
   * @return the <code>PieceType</code>s to which the piece being promoted may
   *         be promoted.
   */
  public PieceType[] getPromotionOptions();
}
