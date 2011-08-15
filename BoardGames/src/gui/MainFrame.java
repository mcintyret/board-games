package gui;

import game.Game;
import game.GameType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import player.Player;
import tools.Tools;

public class MainFrame {
  private static AbstractBoardGui board;

  private static final JFrame frame = new JFrame();

  private static final GameType[] gameOptions = GameType.values();

  private static final JMenuBar menuBar = new JMenuBar();
  private static final JMenu newGame = new JMenu("New Game");
  private static final JMenuItem toggleShowAvailableMoves = new ShowAvailableMenuItem();

  private static final JToolBar toolbar = new JToolBar();
  private static final JMenuItem undoButton = new JMenuItem("Undo");
  private static final JMenuItem changeColorButton = new JMenuItem("Change Color");

  private static final List<Player> playersAddedThisSession = new LinkedList<Player>();
  private static final Set<Color> chosenColors = new HashSet<Color>();
  private static final Set<String> chosenNames = new HashSet<String>();

  public static void start() {
    initialiseFrame();
    initialiseToolbar();
    launch();
    getInitialGame();
  }

  private static void getInitialGame() {
    GameType gameType = (GameType) JOptionPane.showInputDialog(frame, "Select Game", "Welcome!",
        JOptionPane.PLAIN_MESSAGE, null, gameOptions, null);
    if (gameType == null) gameType = GameType.CHESS;
    startGameFromGameType(gameType);
  }

  private static int getNumPlayers(GameType gameType) {
    int minPlayers = gameType.getMinPlayers();
    int maxPlayers = gameType.getMaxPlayers();
    if (minPlayers == maxPlayers) return minPlayers;
    Integer[] options = new Integer[1 + maxPlayers - minPlayers];
    for (int i = minPlayers; i <= maxPlayers; i++) {
      options[i - minPlayers] = i;
    }
    return (Integer) JOptionPane.showInputDialog(frame, "How many players?", "Setup",
        JOptionPane.PLAIN_MESSAGE, null, options, null);
  }

  private static void addNewPlayer() {
    int playerNum = playersAddedThisSession.size() + 1;

    String name;
    do {
      name = (String) JOptionPane.showInputDialog(frame, "Enter name for Player " + playerNum, "",
          JOptionPane.PLAIN_MESSAGE, null, null, "Player " + playerNum);
    } while (!chosenNames.add(name));
    if (name == null) name = "Player " + playerNum;

    Color color = getPlayerColorFromUser();

    playersAddedThisSession.add(new Player(color, name));
  }

  private static void initialiseFrame() {
    frame.setBounds(100, 100, 1000, 900);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(toolbar, BorderLayout.NORTH);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  private static void initialiseToolbar() {
    for (GameType gameType : gameOptions) {
      newGame.add(new NewGameMenuItem(gameType));
    }
    toolbar.add(menuBar);
    menuBar.add(newGame);

    undoButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        board.undoMove();
      }
    });
    toolbar.add(undoButton);

    toolbar.add(toggleShowAvailableMoves);

    changeColorButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        Color newColor = getPlayerColorFromUser();
        board.getGame().changeCurrentPlayerColor(newColor);
        board.repaint();
      }
    });
    toolbar.add(changeColorButton);

  }

  private static Color getPlayerColorFromUser() {
    Color defaultColor;
    do {
      defaultColor = Tools.getRandomColor();
    } while (chosenColors.contains(defaultColor));

    Color color;
    do {
      color = JColorChooser.showDialog(frame, "Choose Color", defaultColor);
    } while (!chosenColors.add(color));

    if (color == null) {
      color = defaultColor;
      chosenColors.add(defaultColor);
    }
    return color;
  }

  private static void launch() {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private static void startGameFromGameType(GameType gameType) {
    if (board != null) {
      board.close();
      frame.getContentPane().remove(board);
    }

    int players = getNumPlayers(gameType);
    while (playersAddedThisSession.size() < players)
      addNewPlayer();
    List<Player> playersThisGame = playersAddedThisSession.subList(0, players);
    for (Player player : playersThisGame) {
      player.getPieces().clear();
    }

    board = getBoardFromGameType(gameType);

    board.setFrame(frame);
    board.getGame().addPlayers(playersThisGame);
    board.start();
    frame.getContentPane().add(board, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  private static AbstractBoardGui getBoardFromGameType(GameType gameType) {
    Game game = gameType.getNewInstance();
    switch (gameType) {
    case CHESS:
    case CHECKERS:
      return new PromotionGameBoardGui(game);
    case CHUTES_AND_LADDERS:
      return new DiceGameBoardGui(game);
    }
    throw new AssertionError();
  }

  private static class GameTypeSelectionListener implements ActionListener {

    private final GameType gameType;

    public GameTypeSelectionListener(GameType gameType) {
      this.gameType = gameType;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
      startGameFromGameType(gameType);
    }
  }

  private static class NewGameMenuItem extends JMenuItem {
    private static final long serialVersionUID = 1L;

    public NewGameMenuItem(GameType gameType) {
      super(gameType.name());
      addActionListener(new GameTypeSelectionListener(gameType));
    }
  }

  private static class ShowAvailableMenuItem extends JMenuItem {
    private static final String off = "Available Moves: Off";

    private static final String on = "Available Moves: On";
    private static final long serialVersionUID = 1L;
    private String current = on;

    public ShowAvailableMenuItem() {
      super(on);
      addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          board.toggleShowAvailableMoves();

          current = current == on ? off : on;
          setText(current);
        }
      });
    }
  }
}
