package gui;

import game.dicegames.Dice;

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

public class DiceFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  private static final Image[] diceImages = new BufferedImage[6];
  static {
    for (int i = 0; i < diceImages.length; i++) {
      try {
        diceImages[i] = ImageIO.read(new File("./files/dice_" + (i + 1) + ".png"));
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
    initialise();
    launch();
  }

  public void updateOnDiceRoll() {
    int[] diceScores = dice.getScores();
    for (int i = 0; i < diceScores.length; i++) {
      dicePanels[i].score = diceScores[i];
    }
    repaint();
  }

  private void initialise() {
    setBounds(100, 100, 100 * dice.getNumDice(), 150);
    getContentPane().setLayout(new GridLayout(1, dice.getNumDice()));
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    for (int i = 0; i < dicePanels.length; i++) {
      dicePanels[i] = new DicePanel();
      getContentPane().add(dicePanels[i]);
    }
    addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          guiBoard.diceRollRequested();
          updateOnDiceRoll();
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

  private static class DicePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private int score = 1;

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      g2.drawImage(diceImages[score - 1], 0, 0, getWidth(), getHeight(), null);
    }

  }
}
