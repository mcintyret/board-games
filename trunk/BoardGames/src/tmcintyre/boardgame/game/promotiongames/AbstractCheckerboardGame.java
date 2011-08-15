package tmcintyre.boardgame.game.promotiongames;


import java.awt.Color;
import java.util.List;

import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.pieces.Piece;

/**
 * A convenience class that contains several methods common to games that use a
 * Checkerboard.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractCheckerboardGame extends AbstractPromotionGame {

  protected Color blackColor;
  protected Color whiteColor;

  protected AbstractCheckerboardGame(GameType gameType) {
    super(gameType);
  }

  @Override
  public void start() {
    whiteColor = players.get(0).getColor();
    blackColor = players.get(1).getColor();
    super.start();
  }

  public Color getBlackColor() {
    return blackColor;
  }

  public Color getWhiteColor() {
    return whiteColor;
  }

  @Override
  public void initialiseBoardColors() {
    initializeCheckerboard();
  }

  protected boolean moveReachesEndOfBoard(Move move) {
    Color color = move.getMovingPiece().getColor();
    if (color == whiteColor) return (move.destRow() == 0);
    if (color == blackColor) return (move.destRow() == boardHeight - 1);
    throw new AssertionError("Illegal Color");
  }

  @Override
  public void changeCurrentPlayerColor(Color newColor) {
    Color oldColor = currentPlayer.getColor();
    super.changeCurrentPlayerColor(newColor);
    if (whiteColor == oldColor) {
      whiteColor = newColor;
    } else if (blackColor == oldColor) {
      blackColor = newColor;
    }
  }

  @Override
  public void doMove(Move move, boolean isDummy) {
    super.doMove(move, isDummy);
    if (!isDummy) currentPlayer.updateLegalMoves();
  }

  @Override
  public void undoMove(boolean isDummy) {
    super.undoMove(isDummy);
    if (!isDummy) currentPlayer.updateLegalMoves();
  }

  @Override
  public Piece getPieceAt(int row, int col) {
    List<Piece> pieces = getPiecesAt(row, col);
    if (pieces == null || pieces.size() == 0) return null;
    return pieces.get(0);
  }

}
