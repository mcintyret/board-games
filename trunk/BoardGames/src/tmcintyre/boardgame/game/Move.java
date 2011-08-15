package tmcintyre.boardgame.game;

import tmcintyre.boardgame.pieces.Piece;

/**
 * Represents the movement of a piece from one position on the game board to
 * another.
 * 
 * <p>
 * A move may optionally capture another piece; typically this piece will be
 * removed from the game when the move is passed to <code>Game.doMove</code>.
 * 
 * <p>
 * In some cases a single move allowed by a game will in fact consist of
 * multiple related movements of pieces (for example castling in chess). In
 * these cases it is possible to chain related moves together using the
 * <code>setNextMove</code> method. Accessor methods are also provided.
 * 
 * 
 * @author Tom McIntyre
 * 
 */
public class Move {
  private final int destCol;
  private final int destRow;
  private boolean destroyMovingPieceOnUndo = false;
  private final Piece movingPiece;
  private final String name;
  private Move nextMove;
  private Move prevMove;

  private final int startCol;

  private final int startRow;
  private final Piece capturedPiece;

  public Move(Piece movingPiece, int destRow, int destCol) {
    this(movingPiece, destRow, destCol, null);
  }

  public Move(Piece movingPiece, int destRow, int destCol, Piece takenPiece) {
    this(movingPiece, destRow, destCol, takenPiece, null);
    generateName();
  }

  public Move(Piece movingPiece, int destRow, int destCol, Piece capturedPiece, String name) {
    this.movingPiece = movingPiece;
    startRow = movingPiece.getRow();
    startCol = movingPiece.getCol();
    this.destRow = destRow;
    this.destCol = destCol;
    this.capturedPiece = capturedPiece;
    this.name = name != null ? name : generateName();
  }

  /**
   * Returns the column to which the moving <code>Piece</code> is moving.
   * 
   * @return the column to which the moving <code>Piece</code> is moving
   */
  public int destCol() {
    return destCol;
  }

  /**
   * Returns the row to which the moving <code>Piece</code> is moving.
   * 
   * @return the row to which the moving <code>Piece</code> is moving
   */
  public int destRow() {
    return destRow;
  }

  /**
   * Returns <tt>true</tt> if the piece being moved should be destroyed if the
   * <code>Move</code> is undone by being passed to <code>Game.undoMove</code>.
   * Typically this will be the case where the piece was created specifically
   * for this move. An example is in promotions in chess and checkers, where a
   * new <code>Piece</code> object of the promoted type takes the place of the
   * <code>Piece</code> that was promoted.
   * 
   * @return <tt>true</tt> if the piece being moved should be destroyed if the
   *         <code>Move</code> is undone, false otherwise.
   */
  public boolean destroyMovingPieceOnUndo() {
    return destroyMovingPieceOnUndo;
  }

  /**
   * Returns the <code>Piece</code> that is moving in this <code>Move</code>.
   * 
   * @return the <code>Piece</code> that is moving in this <code>Move</code>
   */
  public Piece getMovingPiece() {
    return movingPiece;
  }

  /**
   * Returns the name of this <code>Move</code>
   * 
   * @return the name of this <code>Move</code>
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Returns the next <code>Move</code> linked to this <code>Move</code>, if
   * any.
   * 
   * @return the next <code>Move</code> linked to this <code>Move</code>, if any
   */
  public Move getNextMove() {
    return nextMove;
  }

  /**
   * Returns the previous <code>Move</code> linked to this <code>Move</code>, if
   * any.
   * 
   * @return the previous <code>Move</code> linked to this <code>Move</code>, if
   *         any
   */
  public Move getPrevMove() {
    return prevMove;
  }

  /**
   * Returns the <code>Piece</code> captured by this <code>Move</code>, if any.
   * 
   * @return the <code>Piece</code> captured by this <code>Move</code>, if any
   */
  public Piece getCapturedPiece() {
    return capturedPiece;
  }

  /**
   * Sets whether the moving piece should be destroyed if this move is undone.
   * 
   * @see destroyMovingPieceOnUndo
   * @param destroyMovingPieceOnUndo
   */
  public void setDestroyMovingPieceOnUndo(boolean destroyMovingPieceOnUndo) {
    this.destroyMovingPieceOnUndo = destroyMovingPieceOnUndo;
  }

  /**
   * Sets the provided <code>Move</code> as the next <code>Move</code> linked to
   * this move. Automatically sets this <code>Move</code> as the previous
   * <code>Move</code> of the new next move.
   * 
   * @param nextMove
   *          the <code>Move</code> to be linked to this <code>Move</code> as
   *          the next move.
   */
  public void setNextMove(Move nextMove) {
    this.nextMove = nextMove;
    nextMove.prevMove = this;
  }

  /**
   * Returns the column from which the moving <code>Piece</code> is moving.
   * 
   * @return the column from which the moving <code>Piece</code> is moving
   */
  public int startCol() {
    return startCol;
  }

  /**
   * Returns the row from which the moving <code>Piece</code> is moving.
   * 
   * @return the row from which the moving <code>Piece</code> is moving
   */
  public int startRow() {
    return startRow;
  }

  /**
   * Generates a String describing this <code>Move</code> to be used as a
   * default name if none is provided. The name is based on the moving piece,
   * the start and destination positions, and the captured piece (if any).
   * 
   * @return a String describing this <code>Move</code>
   */
  private String generateName() {
    StringBuilder sb = new StringBuilder();
    sb.append(movingPiece.getPlayer().getName() + " ");
    sb.append(movingPiece.getType());
    sb.append(" moved from ");
    sb.append(startRow + ", " + startCol);
    sb.append(" to ");
    sb.append(destRow + ", " + destCol);
    if (capturedPiece != null) {
      sb.append(", capturing ");
      sb.append(capturedPiece.getPlayer().getName() + "'s ");
      sb.append(capturedPiece.getType());
    }
    return sb.toString();
  }
}
