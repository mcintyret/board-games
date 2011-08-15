package tmcintyre.boardgame.pieces;


import java.awt.Point;

import tmcintyre.boardgame.game.Game;
import tmcintyre.boardgame.game.Move;
import tmcintyre.boardgame.game.dicegames.ChutesAndLaddersGame;
import tmcintyre.boardgame.player.Player;

public final class ChutesAndLaddersPiece extends AbstractPiece {

  private static final PieceType type = PieceType.CHUTES_AND_LADDERS_PIECE;

  private final ChutesAndLaddersGame game;

  public ChutesAndLaddersPiece(ChutesAndLaddersGame game, Player player) {
    super(player);
    this.game = game;
  }

  @Override
  public void updateLegalMoves() {
    legalMoves.clear();

    if (game.sixCount == 3) {
      game.sixCount = 0;
      legalMoves.add(new Move(this, game.getBoardHeight() - 1, 0, null, player.getName()
          + " rolled 3 sixes - return to start!"));
    } else {
      int diceScore = game.getDiceScore();

      boolean headingEast = game.getBoardHeight() % 2 == 0 ^ row % 2 == 0;
      // The number of squares remaining in this row
      // - this depends on the direction the piece is moving
      int colsRemaining = headingEast ? game.getBoardWidth() - (col + 1) : col;
      int newCol = col;
      int newRow = row;
      if (colsRemaining >= diceScore) {
        // The piece doesn't have to change rows
        newCol = headingEast ? col + diceScore : col - diceScore;
      } else if (row != 0) {
        // If there are no rows left the piece has overshot the finish line
        // - it stays where it is!
        // note that in this case a move is added, but with the same desination
        // as start point.
        newCol = headingEast ? game.getBoardWidth() - (diceScore - colsRemaining) : diceScore
            - colsRemaining - 1;
        newRow--;
      }
      Move move = new Move(this, newRow, newCol, null);

      Point p = game.getChuteOrLadderDest(move.destRow(), move.destCol());
      if (p != null) {
        move.setNextMove(new Move(this, p.x, p.y, null, player.getName()
            + " took a snake/ladder from " + move.destRow() + ", " + move.destCol() + " to " + p.x
            + ", " + p.y));
      }
      legalMoves.add(move);

    }
  }

  @Override
  public Game getGame() {
    return game;
  }

  @Override
  public PieceType getType() {
    return type;
  }

}
