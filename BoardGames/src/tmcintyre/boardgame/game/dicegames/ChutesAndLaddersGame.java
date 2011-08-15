package tmcintyre.boardgame.game.dicegames;


import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.pieces.ChutesAndLaddersPiece;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.player.Player;
import tmcintyre.boardgame.tools.Tools;

/**
 * A Chutes and Ladders game.
 * 
 * <p>
 * Internally the chutes and ladders are represented as a <code>Map</code> of
 * <code>Point</code>s to
 * <code>Point<point>s. When a <code>ChutesAndLaddersPiece</code> lands on a
 * square it checks to see whether that square is the start of a chute or ladder
 * (corresponds to a <code>Point</code> that is a key in the map) and, if so,
 * adds a new <code>Move</code> to the destination of that chute or ladder (the
 * value associated with that key).
 * 
 * <p>
 * To simplify the graphical representation of the chutes and ladders they are
 * represented as a pair of squares of the same color. Note that this means that
 * it is currently impossible to tell which end of a chute or ladder is the
 * start and which is the destination. Think of this as part of the fun!
 * 
 * @author Tom McIntyre
 * 
 */
public class ChutesAndLaddersGame extends AbstractDiceGame {

  private static final Map<String, String[]> options = new LinkedHashMap<String, String[]>();
  static {
    options.put("board height", null);
    options.put("board width", null);
  }
  private static final int DEFAULT_BOARD_SIZE = 8;

  public int sixCount = 0;

  private final Random rng = new Random();

  // A map of points representing the destinations of any snakes or ladders
  // rooted at that point.
  private final Map<Point, Point> chutesAndLadders = new HashMap<Point, Point>();

  public ChutesAndLaddersGame() {
    super(GameType.CHUTES_AND_LADDERS);
  }

  /**
   * Returns true if the current player has won the game.
   * 
   * <p>
   * A piece has won if it has reached the end position of the game board. The
   * end row is always 0. However, the end column depends on whether there are
   * an even or odd number of rows, in which case it is the leftmost or
   * rightmost column respectively.
   * 
   */
  @Override
  public boolean checkWinConditions() {
    int winCol = boardHeight % 2 == 0 ? 0 : boardWidth - 1;
    Piece piece = currentPlayer.getPieces().get(0);
    if (piece.getRow() != 0) return false;
    return (piece.getCol() == winCol);
  }

  @Override
  public void doMove(Move move, boolean isDummy) {
    super.doMove(move, isDummy);
    if (isTurnOver(move)) sixCount = 0;
  }

  public Point getChuteOrLadderDest(int destRow, int destCol) {
    Point start = new Point(destRow, destCol);
    return chutesAndLadders.get(start);
  }

  @Override
  protected void initialiseBoardColors() {
    initializeCheckerboard();
    initializeChutesAndLadders();
  }

  @Override
  public boolean isTurnOver(Move move) {
    if (move.getNextMove() != null) return false;
    if (dice.getTotalScore() != 6) return true;
    if (sixCount == 3) return true;
    return false;
  }

  @Override
  public void rollDice() {
    dice.rollDice();
    currentPlayerHasRolled = true;
    if (dice.getTotalScore() == 6) {
      sixCount++;
    }
    currentPlayer.updateLegalMoves();
  }

  private Point getUnusedPoint() {
    // Randomly generates new Points until one is made that has not been used
    // before.
    while (true) {
      Point p = new Point(rng.nextInt(boardHeight), rng.nextInt(boardWidth));
      if (!chutesAndLadders.containsKey(p)) return p;
    }
  }

  private void initializeChutesAndLadders() {
    // Prevents a snake or ladder appearing on the start square:
    chutesAndLadders.put(new Point(boardHeight - 1, 0), null);

    // A reasonable number of chutes and ladders given the board dimensions:
    int numChutesAndLadders = (boardHeight + boardWidth) / 2;

    for (int i = 0; i < numChutesAndLadders; i++) {
      Point start = getUnusedPoint();
      Point end = getUnusedPoint();
      chutesAndLadders.put(start, end);

      // To ensure there are not multiple chutes or ladders ending at the same
      // square.
      // To make the snakes and ladders 2-way, change 'null' to 'start'
      chutesAndLadders.put(end, null);

      // Generating a random color to associate with this chute or ladder
      Color color = Tools.getRandomColor();
      boardColors[start.x][start.y] = color;
      boardColors[end.x][end.y] = color;
    }
  }

  @Override
  protected void addInitialPieces(Player player) {
    // All pieces start at the same square in the bottom left corner
    addPieceAt(boardHeight - 1, 0, new ChutesAndLaddersPiece(this, player));
  }

  @Override
  public Map<String, String[]> getGameSpecificOptions() {
    return options;
  }

  @Override
  public void implementSelectedOptions(Map<String, String> selectedOptions) {
    Integer height = getNumericOption("board height", selectedOptions);
    int boardHeight = height == null ? DEFAULT_BOARD_SIZE : height;

    Integer width = getNumericOption("board width", selectedOptions);
    int boardWidth = width == null ? DEFAULT_BOARD_SIZE : width;

    setBoardDimensions(boardHeight, boardWidth);
  }

}
