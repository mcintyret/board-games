package gui;

import game.Game;
import game.Move;
import game.promotiongames.PromotionGame;

import javax.swing.JOptionPane;

import pieces.PieceType;

public class PromotionGameBoardGui extends AbstractBoardGui {
  private static final long serialVersionUID = 1L;

  private final PromotionGame game;

  public PromotionGameBoardGui(Game game) {
    this.game = (PromotionGame) game;
  }

  @Override
  public void notifyOnPromotion(Move move) {
    PromotionGame pg = (PromotionGame) game;
    PieceType[] options = pg.getPromotionOptions();
    if (options.length == 1) {
      pg.doPromotion(move, options[0]);
      return;
    }
    PieceType choice = (PieceType) JOptionPane.showInputDialog(this, "Select Promotion",
        "You got promoted!", JOptionPane.PLAIN_MESSAGE, null, options, null);
    pg.doPromotion(move, choice);
    updateOnBoardChanged();
    repaint();
  }

  @Override
  public Game getGame() {
    return game;
  }

}
