package tmcintyre.boardgame.gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import tmcintyre.boardgame.game.dicegames.Dice;

/**
 * A graphical representation of a {@link Dice} object.
 * 
 * <p>
 * The score for each die represented by the underlying <code>Dice</code> object
 * is shown on a separate <code>DicePanel</code>.
 * 
 * @author Tom McIntyre
 * 
 */
public class DiceFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  private static final Image[] diceImages = new BufferedImage[7];
  static {
    for (int i = 0; i < diceImages.length; i++) {
      try {
        diceImages[i] = ImageIO.read(new File("./files/dice_" + i  + ".png"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  private final Dice dice;

  private final DicePanel[] dicePanels;

  private final DiceGameBoardGui guiBoard;

  public DiceFrame(Dice dice, DiceGameBoardGui guiBoard) {
    this.dice = dice;
    this.guiBoard = guiBoard;
    dicePanels = new DicePanel[dice.getNumDice()];
    initialize();
    launch();
  }

  private void initialize() {
    setBounds(100, 100, 100 * dice.getNumDice(), 150);
    getContentPane().setLayout(new GridLayout(1, dice.getNumDice()));
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    // Initializing the dice panels
    for (int i = 0; i < dicePanels.length; i++) {
      getContentPane().add(new DicePanel(i));
    }

    // Double-clicking on the DiceFrame tells the DiceGameBoardGui object that
    // the user requested to roll the dice.
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          guiBoard.diceRollRequested();
          repaint();
        }
      }
    });
    setAlwaysOnTop(true);
  }

  private void launch() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Displays an image of a die face corresponding to the score of the die.
   * 
   * @author Tom McIntyre
   * 
   */
  private class DicePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final int index;

    public DicePanel(int index) {
      this.index = index;
    }

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      int score = dice.getScores()[index];

      g2.drawImage(diceImages[score], 0, 0, getWidth(), getHeight(), null);
    }

  }
}
