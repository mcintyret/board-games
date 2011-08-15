package tmcintyre.boardgame.game.promotiongames;

import tmcintyre.boardgame.game.GameType;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.pieces.PieceType;
import tmcintyre.boardgame.pieces.chess.Bishop;
import tmcintyre.boardgame.pieces.chess.King;
import tmcintyre.boardgame.pieces.chess.Knight;
import tmcintyre.boardgame.pieces.chess.Pawn;
import tmcintyre.boardgame.pieces.chess.Queen;
import tmcintyre.boardgame.pieces.chess.Rook;
import tmcintyre.boardgame.player.Player;

/**
 * A chess game.
 * 
 * @author Tom McIntyre
 */
public class ChessGame extends AbstractCheckerboardGame {

  private static final int CHESS_BOARD_SIZE = 8;
  private static final PieceType[] promotionOptions = new PieceType[] { PieceType.PAWN,
      PieceType.KNIGHT, PieceType.BISHOP, PieceType.ROOK, PieceType.QUEEN };

  public ChessGame() {
    super(GameType.CHESS);
    setBoardDimensions(CHESS_BOARD_SIZE, CHESS_BOARD_SIZE);
  }

  @Override
  public boolean checkPromotion(Move move) {
    if (move.getMovingPiece().getType() != PieceType.PAWN) return false;
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
    // TODO: implement stalemate
    updateCurrentPlayerToNext();
    currentPlayer.updateLegalMoves();
    boolean canMove = currentPlayer.getAllLegalMoves().size() != 0;
    updateCurrentPlayerToPrev();
    return !canMove;
  }

  @Override
  public PieceType[] getPromotionOptions() {
    return promotionOptions;
  }

  @Override
  public boolean isTurnOver(Move move) {
    return move.getNextMove() == null;
  }

  @Override
  protected void addInitialPieces(Player player) {
    int mainRow;
    int pawnRow;

    if (player.getColor() == blackColor) {
      mainRow = 0;
      pawnRow = 1;
    } else if (player.getColor() == whiteColor) {
      mainRow = boardHeight - 1;
      pawnRow = boardHeight - 2;
    } else {
      throw new AssertionError("Illegal Color");
    }

    addPieceAt(mainRow, 0, new Rook(this, player));
    addPieceAt(mainRow, 1, new Knight(this, player));
    addPieceAt(mainRow, 2, new Bishop(this, player));
    addPieceAt(mainRow, 3, new Queen(this, player));
    addPieceAt(mainRow, 4, new King(this, player));
    addPieceAt(mainRow, 5, new Bishop(this, player));
    addPieceAt(mainRow, 6, new Knight(this, player));
    addPieceAt(mainRow, 7, new Rook(this, player));

    for (int col = 0; col < boardWidth; col++) {
      addPieceAt(pawnRow, col, new Pawn(this, player));
    }
  }

}