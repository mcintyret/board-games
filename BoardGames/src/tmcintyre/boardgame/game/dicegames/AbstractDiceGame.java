package tmcintyre.boardgame.game.dicegames;

import tmcintyre.boardgame.game.AbstractGame;
import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;

/**
 * A skeletal implementation of a <code>DiceGame</code>.
 * 
 * <p>
 * Provides simple implementations of methods common to most
 * <code>DiceGame</code>s.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractDiceGame extends AbstractGame implements DiceGame {

  protected boolean currentPlayerHasRolled = false;
  protected final Dice dice = new Dice();

  protected AbstractDiceGame(GameType gameType) {
    super(gameType);
  }

  @Override
  public Dice getDice() {
    return dice;
  }

  @Override
  public int getDiceScore() {
    return dice.getTotalScore();
  }

  @Override
  public void doMove(Move move, boolean isDummy) {
    super.doMove(move, isDummy);
    currentPlayerHasRolled = false;
  }

  @Override
  public boolean currentPlayerHasRolled() {
    return currentPlayerHasRolled;
  }

  @Override
  public void undoMove(boolean dummy) {
    super.undoMove(dummy);
    currentPlayerHasRolled = false;
  }

}
