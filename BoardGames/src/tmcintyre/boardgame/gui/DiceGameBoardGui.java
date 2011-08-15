package tmcintyre.boardgame.gui;


import java.util.List;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.dicegames.DiceGame;

/**
 * A graphical representation of a {@link DiceGame}
 * @author Tom McIntyre
 *
 */
public class DiceGameBoardGui extends AbstractBoardGui {

  private static final long serialVersionUID = 1L;

  private final DiceFrame diceFrame;
  private final DiceGame game;

  public DiceGameBoardGui(Game game) {
    this.game = (DiceGame) game;
    diceFrame = new DiceFrame(this.game.getDice(), this);
  }

  public void diceRollRequested() {
    if (!game.currentPlayerHasRolled()) game.rollDice();
    diceFrame.repaint();

    List<Move> legalMoves = game.getCurrentPlayer().getAllLegalMoves();
    if (legalMoves.size() == 1) {
      // In many dice-based games there will only be one move available,
      // corresponding to the score on the dice. In this case the move is
      // automatically made to save the player explicitly selecting that move.
      game.doMove(legalMoves.get(0), false);
    }
  }

  @Override
  public void notifyOnPromotion(Move move) {
    // TODO Auto-generated method stub

  }

  @Override
  public Game getGame() {
    return game;
  }

  @Override
  public void close() {
    diceFrame.setVisible(false);
  }
}
