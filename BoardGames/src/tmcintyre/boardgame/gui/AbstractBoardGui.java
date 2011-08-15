package tmcintyre.boardgame.gui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.Observer;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.player.Player;

/**
 * A graphical representation of a game board.
 * 
 * <p>
 * The board is represented as a 2D grid of {@link Square} objects directly
 * corresponding to the squares of the game board.
 * 
 * <p>
 * Implements the {@link Observer} interface and so is notified when the game
 * state changes. This allows the representation to update itself as needed.
 * 
 * <p>
 * Contains several methods to update the game state in response to
 * user-generated events
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractBoardGui extends JPanel implements Observer {
  private static final long serialVersionUID = 1L;

  private static final Border moveBorder = BorderFactory.createLineBorder(Color.RED, 5);
  private static final Border selectedBorder = BorderFactory.createLineBorder(Color.YELLOW, 5);
  private static final ImageIcon winIcon = new ImageIcon("files/win.png");

  private final Map<Square, Move> legalMoves = new HashMap<Square, Move>();
  private JFrame frame;

  private boolean showAvailableMoves = true;
  private Piece selectedPiece;

  private Square[] squares;

  protected void initialise() {
    setLayout(new GridLayout(getGame().getBoardHeight(), getGame().getBoardWidth()));
    setOpaque(true);
    initialiseSquares();
  }

  public abstract Game getGame();

  @Override
  public void notifyOnMove(Move move) {
    updateOnBoardChanged();
    repaint();
  }

  @Override
  public void notifyOnStalemate() {

  }

  public void setFrame(JFrame frame) {
    this.frame = frame;
  }

  private void setFrameTitle() {
    frame.setTitle(getGame().getCurrentPlayer().getName() + "'s turn");
  }

  @Override
  public void notifyOnCurrentPlayerChanged() {
    setFrameTitle();
  }

  @Override
  public void notifyOnStart() {
    initialise();
    updateOnBoardChanged();
    repaint();
    setFrameTitle();
  }

  @Override
  public void notifyOnUndo() {
    updateOnBoardChanged();
    repaint();
  }

  @Override
  public void notifyOnWin(Player winner) {
    JOptionPane.showMessageDialog(this, "Congratulations " + winner.getName() + ", you won!!",
        "Winner!", JOptionPane.INFORMATION_MESSAGE, winIcon);
  }

  public void start() {
    setGameOptions();
    getGame().addObserver(this);
    getGame().start();
  }

  private void setGameOptions() {
    Map<String, String[]> options = getGame().getGameSpecificOptions();
    if (options == null) return;

    Map<String, String> selectedOptions = new HashMap<String, String>();
    for (Map.Entry<String, String[]> entry: options.entrySet()) {
      String key = entry.getKey();
      String[] choices = entry.getValue();
      String response;
      if (choices == null) {
        response = (String) JOptionPane.showInputDialog(frame, "Select " + key, null,
            JOptionPane.PLAIN_MESSAGE, null, null, null);
      } else {
        response = (String) JOptionPane.showInputDialog(frame, "Select " + key, null,
            JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
      }
      selectedOptions.put(key, response);
    }
    getGame().implementSelectedOptions(selectedOptions);
  }

  public void toggleShowAvailableMoves() {
    showAvailableMoves = !showAvailableMoves;
  }

  public void undoMove() {
    Move lastMove = getGame().getLastMove();
    if (lastMove == null) {
      JOptionPane.showMessageDialog(this, "No moves to undo", "Error", JOptionPane.ERROR_MESSAGE,
          null);
    } else {
      getGame().undoMove(false);
    }
  }

  protected void updateOnBoardChanged() {
    for (Square square : squares) {
      square.updatePiecesHeld();
    }
    legalMoves.clear();
  }

  private void updateLegalMoveSquaresForSelectedPiece() {
    legalMoves.clear();
    for (Move move : selectedPiece.getLegalMoves()) {
      int squareIndex = getGame().getBoardHeight() * move.destRow() + move.destCol();
      legalMoves.put(squares[squareIndex], move);
    }
  }

  private void initialiseSquares() {
    squares = new Square[getGame().getBoardHeight() * getGame().getBoardWidth()];
    for (int i = 0; i < squares.length; i++) {
      squares[i] = new Square(i);
      add(squares[i]);
    }
  }

  private class BoardMouseListener extends MouseAdapter {

    private final Square square;

    public BoardMouseListener(Square square) {
      this.square = square;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
      int clicks = event.getClickCount();
      if (clicks == 1) {
        doSelection();
      } else if (clicks == 2) {
        doSelectedMoveIfValid();
      }
    }

    private void doSelectedMoveIfValid() {
      if (selectedPiece == null) {
        JOptionPane.showMessageDialog(AbstractBoardGui.this, "Select a piece first", "Woops!",
            JOptionPane.WARNING_MESSAGE);
      } else if (!legalMoves.containsKey(square)) {
        JOptionPane.showMessageDialog(AbstractBoardGui.this, "Invalid move!", "Error",
            JOptionPane.ERROR_MESSAGE);
      } else {
        getGame().doMove(legalMoves.get(square), false);
      }
    }

    private void doSelection() {
      if (square.pieces == null) return;
      // note that this assumes that pieces belonging to the same player will
      // not be on the same position on the board
      for (Piece piece : square.pieces) {
        if (piece.getPlayer() == getGame().getCurrentPlayer()) {
          selectedPiece = piece;
          updateLegalMoveSquaresForSelectedPiece();
          repaint();
          return;
        }
      }
    }
  }

  /**
   * Each <code>Square</code> object represents a corresponding square on the
   * game board.
   * 
   * <p>
   * Whenever the Gui is updated the square updates its list of {@link Piece}s
   * to match those of its game board square.
   * 
   * @author Tom McIntyre
   * 
   */
  private class Square extends JPanel {
    private static final long serialVersionUID = 1L;

    private final List<Piece> pieces = new LinkedList<Piece>();
    private final int index;

    public Square(int index) {
      this.index = index;
      addMouseListener(new BoardMouseListener(this));
    }

    private void updatePiecesHeld() {
      pieces.clear();
      pieces.addAll(getPiecesFromBoard());
    }

    private Point getPointFromSquareIndex() {
      Point p = new Point();
      p.x = index / getGame().getBoardWidth();
      p.y = index % getGame().getBoardWidth();
      return p;
    }

    private List<Piece> getPiecesFromBoard() {
      Point p = getPointFromSquareIndex();
      return getGame().getPiecesAt(p.x, p.y);
    }

    private Color getColorFromBoard() {
      Point p = getPointFromSquareIndex();
      return getGame().getBoardColorAt(p.x, p.y);
    }

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      setBackground(getColorFromBoard());
      super.paintComponent(g2);

      boolean hasSelectedPiece = false;
      for (Piece piece : pieces) {
        if (piece == selectedPiece) {
          hasSelectedPiece = true;
          break;
        }
      }

      // Setting the appropriate border for this square.
      if (hasSelectedPiece) {
        setBorder(selectedBorder);
      } else if (showAvailableMoves && legalMoves.containsKey(this)) {
        setBorder(moveBorder);
      } else {
        setBorder(null);
      }

      if (pieces.size() == 0) return;

      Piece drawnPiece;
      if (hasSelectedPiece) {
        // If this square has the selected piece it makes sense to draw it
        drawnPiece = selectedPiece;
      } else {
        // Otherwise simply take the first piece.
        // TODO: a way of showing multiple pieces occupying the same square?
        drawnPiece = pieces.get(0);
      }

      g2.setPaint(drawnPiece.getColor());

      char symbol = drawnPiece.getSymbol();
      String string = new String(new char[] { symbol });

      int size = (int) (getHeight() * 1.2f);
      // Quivira is a Unicode font that can handle the more unusual Piece
      // characters.
      g2.setFont(new Font("Quivira", Font.PLAIN, size));
      g2.drawString(string, 0, getHeight());
    }
  }

  public void close() {
    // Do nothing in most cases
  }

}
