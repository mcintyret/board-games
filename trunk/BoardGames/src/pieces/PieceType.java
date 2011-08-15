package pieces;

import game.Game;

import java.lang.reflect.Constructor;

import pieces.checkers.BaseChecker;
import pieces.checkers.CrownedChecker;
import pieces.chess.Bishop;
import pieces.chess.King;
import pieces.chess.Knight;
import pieces.chess.Pawn;
import pieces.chess.Queen;
import pieces.chess.Rook;
import player.Player;

/**
 * A set of constants representing each type of {@link Piece} that has been
 * written.
 * 
 * This Enum is used to store static information that would otherwise be
 * declared in the corresponding class. This saves on boilerplate code, as the
 * latter system would require Overridden getters in each class. This is all
 * replaced with a single static field in each class, the <code>PieceType</code>
 * constant.
 * 
 * @author Tom McIntyre
 * 
 */

// TODO: This enum may get unwieldy as the number of pieces expands. A better
// solution may be to make a PieceType interface containing all the methods held
// here, then for each new game type put all the pieces in their own enum (eg
// ChessPieceType implements PieceType etc).
//
// Pros:
// ¥ Reducing clutter in this class, replacing it with a logical structure
// ¥ By making the static member type of each Piece class the concrete enum type
// rather than the interface type, errors where the wrong type of piece for a
// game is provided would be more difficult to make. Eg in AbstractChessPiece
// have an abstract method getChessPieceType(), implemented by every subclass.
// Also, AbstractChessPiece would implement the Piece.getType() method, simply
// returning the call to getChessPieceType(). This would force the PieceType
// held in each concrete class to be declared as a ChessPieceType, making it
// much more difficult to accidently put a checkers piece in a chess class.
//
// Cons:
// ¥ All the code would be repeated in each enum.


public enum PieceType {
  BASE_CHECKER('\u26c2', BaseChecker.class),
  BISHOP('\u265d', Bishop.class),
  CROWNED_CHECKER('\u26c3', CrownedChecker.class),
  KING('\u265a', King.class),
  KNIGHT('\u265e', Knight.class),
  PAWN('\u265f', Pawn.class),
  QUEEN('\u265b', Queen.class),
  ROOK('\u265c', Rook.class),
  CHUTES_AND_LADDERS_PIECE('¥', ChutesAndLaddersPiece.class);

  private char symbol;
  private Class<? extends Piece> classObj;

  private PieceType(char symbol, Class<? extends Piece> classObj) {
    this.symbol = symbol;
    this.classObj = classObj;
  }

  /**
   * Returns the character used to represent this <code>PieceType</code>.
   * 
   * @return the character used to represent this <code>PieceType</code>
   */
  public char symbol() {
    return symbol;
  }

  /**
   * Returns a new instance of the corresponding <code>Piece</code> class.
   * 
   * <p>
   * The instance is instantiated using the <code>Game</code> and
   * <code>Player</code> objects passed to the method.
   * 
   * <p>
   * Using reflection in this way is much neater than other methods of
   * instantiating a corresponding <code>Piece</code> class, which would rely on
   * a large and error-prone switch statement to produce the new
   * <code>Piece</code>.
   * 
   * <p>
   * A potential downside of this system is that it will not work for a
   * <code>Piece</code> whose constructor is different to the one assumed here.
   * 
   * @param game
   *          the <code>Game</code> to be associated with the new
   *          <code>Piece</code>
   * @param player
   *          the <code>Player</code> to whom the new <code>Piece</code> belongs
   * @return a new instance of the corresponding <code>Piece</code> class.
   */
  public Piece newInstance(Game game, Player player) {
    Constructor<?>[] constr = classObj.getConstructors();
    try {
      return classObj.cast(constr[0].newInstance(game, player));
    } catch (Exception e) {
      // Don't need separate catch blocks - same response for any error
      e.printStackTrace();
      throw new RuntimeException("Creating new " + classObj + " failed");
    }
  }

}
