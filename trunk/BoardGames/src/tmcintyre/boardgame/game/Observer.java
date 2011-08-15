package tmcintyre.boardgame.game;

import tmcintyre.boardgame.player.Player;

/**
 * An <code>Observer</code> that can be registered with a {@link Game} using the
 * <code>Game.addObserver</code> method. Observers are notified by the game when
 * various events take place.
 * 
 * @author Tom McIntyre
 */
public interface Observer {
  /**
   * Notifies this <code>Observer</code> that the provided move has taken place.
   * 
   * @param move
   *          the move that has taken place.
   */
  public void notifyOnMove(Move move);

  /**
   * Notifies this <code>Observer</code> that the moving <code>Piece</code> in
   * the provided move has been promoted.
   * 
   * @param move
   *          the move resulting in a promotion.
   */
  public void notifyOnPromotion(Move move);

  /**
   * Notifies this <code>Observer</code> that the game has ended in a stalemate.
   */
  public void notifyOnStalemate();

  /**
   * Notifies this <code>Observer</code> that the game has started.
   */
  public void notifyOnStart();

  /**
   * Notifies this <code>Observer</code> that the current player has changed.
   */
  public void notifyOnCurrentPlayerChanged();

  /**
   * Notifies this <code>Observer</code> that the most recent <code>Move</code>
   * has been undone.
   */
  public void notifyOnUndo();

  /**
   * Notifies this <code>Observer</code> that the provided <code>Player</code>
   * has won the game.
   * 
   * @param winner
   *          the <code>Player</code> that has won the game.
   */
  public void notifyOnWin(Player winner);

}
