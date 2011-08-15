package game;

import game.dicegames.ChutesAndLaddersGame;
import game.promotiongames.CheckersGame;
import game.promotiongames.ChessGame;

public enum GameType {
  CHECKERS(2, 2, CheckersGame.class),
  CHESS(2, 2, ChessGame.class),
  CHUTES_AND_LADDERS(2, 6, ChutesAndLaddersGame.class);

  private int minPlayers;
  private int maxPlayers;
  private Class<? extends Game> classObj;

  private GameType(int minPlayers, int maxPlayers, Class<? extends Game> classObj) {
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
    this.classObj = classObj;
  }

  /**
   * Returns the maximum number of players that can play in this
   * <code>GameType</code>.
   * 
   * @return the maximum number of players that can play in this
   *         <code>GameType</code>
   */
  public int getMaxPlayers() {
    return maxPlayers;
  }

  /**
   * Returns the minimum number of players that can play in this
   * <code>GameType</code>.
   * 
   * @return the minimum number of players that can play in this
   *         <code>GameType</code>
   */
  public int getMinPlayers() {
    return minPlayers;
  }

  /**
   * Returns a new instance of the <code>Game</code> type corresponding to this
   * <code>GameType</code>.
   * 
   * @return a new instance of the <code>Game</code> type corresponding to this
   *         <code>GameType</code>
   */
  public Game getNewInstance() {
    try {
      return classObj.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Error generating " + this + " game");
    }
  }

}