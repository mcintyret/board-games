package tmcintyre.boardgame.game.promotiongames;


import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.pieces.Piece;
import tmcintyre.boardgame.pieces.PieceType;
import tmcintyre.boardgame.pieces.checkers.BaseChecker;
import tmcintyre.boardgame.player.Player;

/**
 * A checkers game.
 * 
 * <p>
 * Each game is associated with a {@link CheckersRules} object that describes
 * the variant of checkers being played. The CheckersRules object is selected
 * and implemented using the <code>getGameSpecificOptions</code> and
 * <code>implementSelectedOptions</code> methods.
 * 
 * @author Tom McIntyre
 * 
 */
public class CheckersGame extends AbstractCheckerboardGame {

  private static final Map<String, String[]> gameSpecificOptions = new HashMap<String, String[]>();
  static {
    String[] rulesOptions = CheckersRules.rulesMap.keySet().toArray(
        new String[CheckersRules.rulesMap.size()]);
    gameSpecificOptions.put("Checkers Rules", rulesOptions);
  }

  private static final PieceType[] promotionOptions = new PieceType[] { PieceType.CROWNED_CHECKER };
  private CheckersRules rules;

  public CheckersGame() {
    super(GameType.CHECKERS);
  }

  @Override
  public boolean checkPromotion(Move move) {
    if (move.getMovingPiece().getType() != PieceType.BASE_CHECKER) return false;
    return moveReachesEndOfBoard(move);
  }

  /**
   * Returns true if the current player has won the game. Currently no support
   * for draws.
   * 
   * <p>
   * The current player has won if the opponent player has no legal moves.
   * 
   */
  @Override
  public boolean checkWinConditions() {
    updateCurrentPlayerToNext();
    currentPlayer.updateLegalMoves();
    boolean canMove = currentPlayer.getAllLegalMoves().size() != 0;
    updateCurrentPlayerToPrev();
    return !canMove;
  }

  @Override
  public Piece getPieceAt(int row, int col) {
    List<Piece> pieces = getPiecesAt(row, col);
    if (pieces == null) return null;
    return pieces.get(0);
  }

  @Override
  public PieceType[] getPromotionOptions() {
    return promotionOptions;
  }

  @Override
  public boolean isTurnOver(Move move) {
    // If this move has a next move linked to it then the turn IS NOT over
    if (move.getNextMove() != null) return false;

    // If no piece was captured the move IS over
    if (move.getCapturedPiece() == null) return true;

    // If this move is a promotion then the turn IS over
    if (move.getCapturedPiece().getPlayer() == move.getMovingPiece().getPlayer()) return true;

    // Otherwise this piece can keep going as long
    // as there are pieces that can be captured
    move.getMovingPiece().updateLegalMoves();
    return move.getMovingPiece().getLegalMoves().size() == 0;
  }

  @Override
  protected void addInitialPieces(Player player) {
    int rowsFilled = boardHeight / 3 + 1;
    int start;
    int end;
    Color color = player.getColor();

    if (color == whiteColor) {
      start = boardHeight - rowsFilled;
      end = boardHeight - 1;
    } else if (color == blackColor) {
      start = 0;
      end = rowsFilled - 1;
    } else {
      throw new AssertionError("Illegal Color");
    }

    for (int row = start; row <= end; row++) {
      for (int col = 0; col < boardWidth; col++) {
        if (getBoardColorAt(row, col) == Color.WHITE) continue;
        addPieceAt(row, col, new BaseChecker(row, col, this, player));
      }
    }
  }

  public CheckersRules getRules() {
    return rules;
  }

  @Override
  public Map<String, String[]> getGameSpecificOptions() {
    return gameSpecificOptions;
  }

  @Override
  public void implementSelectedOptions(Map<String, String> options) {
    String rule = options.get("Checkers Rules");
    rules = CheckersRules.rulesMap.get(rule);
    setBoardDimensions(rules.getBoardSize(), rules.getBoardSize());
  }
}
