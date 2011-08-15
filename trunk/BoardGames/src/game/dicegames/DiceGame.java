package game.dicegames;

import game.Game;

/**
 * A game that uses dice.
 * 
 * @author Tom McIntyre
 * 
 */
public interface DiceGame extends Game {

  /**
   * Returns <tt>true</tt> if the current player has rolled the dice,
   * <tt>false</tt> otherwise.
   * 
   * @return <tt>true</tt> if the current player has rolled the dice,
   *         <tt>false</tt> otherwise
   */
  public boolean currentPlayerHasRolled();

  /**
   * Returns the <code>Dice</code> object associated with this game.
   * 
   * @return the <code>Dice</code> object associated with this game
   */
  public Dice getDice();

  /**
   * Returns the sum of the scores of each die in the game.
   * 
   * @return the sum of the scores of each die in the game
   * @see Dice.getTotalScore
   */
  public int getDiceScore();

  /**
   * Rolls the dice. Calls <code>rollDice</code> on the <code>Dice</code> object
   * associated with this game as well as any game-specific actions.
   */
  public void rollDice();
}
