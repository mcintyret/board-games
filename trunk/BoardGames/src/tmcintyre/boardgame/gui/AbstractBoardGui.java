package tmcintyre.boardgame.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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
 * <p>
 * Note that the <code>getGame</code> method is not abstract and that there is
 * no <code>Game</code> object field. This forces subclasses to implement their
 * own <code>getGame</code> method and provide their own <code>Game</code>
 * object. This allows subclasses to have as a member a more specific
 * implementation of <code>Game</code>, giving access to methods specific to
 * that game type.
 * 
 * @author Tom McIntyre
 * 
 */

// TODO: Change the structure of the BoardGui class tree to mirror that of the
// Game class tree.
// The key point is to make interfaces for BoardGui that are equivalent to those
// for Game. The Game system uses interfaces so that it is possible to have a
// Game that implements PromotionGame, a Game that implements DiceGame, and a
// Game that implements both. Because the BoardGui system so far just uses class
// inheritance, this mixing of properties is not possible at the moment.

public abstract class AbstractBoardGui extends JPanel implements Observer {
  private static final long serialVersionUID = 1L;

  private static final Border moveBorder = BorderFactory.createLineBorder(Color.RED, 5);
  private static final Border selectedBorder = BorderFactory.createLineBorder(Color.YELLOW, 5);
  private static final ImageIcon winIcon = new ImageIcon("files/win.png");

  private final Map<Square, Move> legalMoves = new HashMap<Square, Move>();
  private JFrame frame;

  private boolean showAvailableMoves = true;
  private Piece selectedPiece;

  private Square[][] squares;

  protected void initialise() {
    setLayout(new GridLayout(getGame().getBoardHeight(), getGame().getBoardWidth()));
    setOpaque(true);
    initialiseSquares();
  }

  /**
   * Returns the <code>Game</code> object associated with this
   * <code>BoardGUI</code>.
   * 
   * @return the <code>Game</code> object associated with this
   *         <code>BoardGUI</code>
   */
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
    for (Map.Entry<String, String[]> entry : options.entrySet()) {
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

  /**
   * Toggles the value of the boolean field <code>showAvailableMoves</code>.
   * 
   * <p>
   * If <code>showAvailableMoves</code> is <tt>true</tt>, the available moves
   * for the currently selected piece will be highlighted in red.
   */
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

  /**
   * Calls all the <code>Square</code> objects to update the list of
   * <code>Piece</code>s they hold.
   */
  // TODO: consider having each square do this as part of its repaint method.
  // This method could then be completely removed. This would make the squares
  // essentially autonomous. The downside is all the unnecessary calls to
  // Game.getPiecesAt() when no change to the board has taken place.
  protected void updateOnBoardChanged() {
    for (Square[] squareRow : squares) {
      for (Square square : squareRow) {
        square.updatePiecesHeld();
      }
    }
    legalMoves.clear();
  }

  private void updateLegalMoveSquaresForSelectedPiece() {
    legalMoves.clear();
    for (Move move : selectedPiece.getLegalMoves()) {
      legalMoves.put(squares[move.destRow()][move.destCol()], move);
    }
  }

  private void initialiseSquares() {
    squares = new Square[getGame().getBoardHeight()][getGame().getBoardWidth()];
    for (int row = 0; row < squares.length; row++) {
      for (int col = 0; col < squares[row].length; col++) {
        squares[row][col] = new Square(row, col);
        add(squares[row][col]);
      }
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
      // Note that this assumes that pieces belonging to the same player will
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
   * Whenever the GUI is updated the square updates its list of {@link Piece}s
   * to match those of its game board square.
   * 
   * @author Tom McIntyre
   * 
   */
  private class Square extends JPanel {
    private static final long serialVersionUID = 1L;

    private final List<Piece> pieces = new LinkedList<Piece>();
    private final int row;
    private final int col;

    public Square(int row, int col) {
      this.row = row;
      this.col = col;
      addMouseListener(new BoardMouseListener(this));
    }

    private void updatePiecesHeld() {
      pieces.clear();
      pieces.addAll(getGame().getPiecesAt(row, col));
    }

    @Override
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      setBackground(getGame().getBoardColorAt(row, col));
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

    @Override
    public int hashCode() {
      // Automatically generated hashcode function
      final int prime = 31;
      int result = 1;
      result = prime * result + col;
      result = prime * result + ((pieces == null) ? 0 : pieces.hashCode());
      result = prime * result + row;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      // Automatically generated equals function
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Square other = (Square) obj;
      if (col != other.col) return false;
      if (row != other.row) return false;
      if (pieces == null) {
        if (other.pieces != null) return false;
      } else if (!pieces.equals(other.pieces)) return false;
      return true;
    }
  }

  public void close() {
    // Do nothing in most cases
  }

}
