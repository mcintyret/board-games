package game.promotiongames;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encapsulates various options and parameters that distinguish variants of the
 * game Checkers.
 * 
 * <p>
 * Fields are immutable and are accessed through getters. Several common
 * combinations of rules are provided for convenience.
 * 
 * @author Tom McIntyre
 * @see CheckersGame
 * 
 */
public final class CheckersRules {

  public static final Map<String, CheckersRules> rulesMap = new LinkedHashMap<String, CheckersRules>();
  
  private static final CheckersRules americanCheckers = new CheckersRules(8, false, false,
      CaptureRules.MUST_CAPTURE);
  private static final CheckersRules brazilianDraughts = new CheckersRules(8, true, true,
      CaptureRules.MUST_MAXIMISE_CAPTURE);
  private static final CheckersRules canadianDraughts = new CheckersRules(12, true, true,
      CaptureRules.MUST_MAXIMISE_CAPTURE);
  private static final CheckersRules internationalDraughts = new CheckersRules(10, true, true,
      CaptureRules.MUST_MAXIMISE_CAPTURE);
  private static final CheckersRules poolCheckers = new CheckersRules(8, false, false,
      CaptureRules.MUST_CAPTURE);
  static {
    rulesMap.put("American Checkers", americanCheckers);
    rulesMap.put("Brazilian Draughts", brazilianDraughts);
    rulesMap.put("Canadian Draughts", canadianDraughts);
    rulesMap.put("International Draughts", internationalDraughts);
    rulesMap.put("Pool Checkers", poolCheckers);
  }

  public static CheckersRules getAmericanCheckers() {
    return americanCheckers;
  }

  public static CheckersRules getBrazilianDraughts() {
    return brazilianDraughts;
  }

  public static CheckersRules getCanadianDraughts() {
    return canadianDraughts;
  }

  public static CheckersRules getInternationalDraughts() {
    return internationalDraughts;
  }

  public static CheckersRules getPoolCheckers() {
    return poolCheckers;
  }

  private final int boardSize;

  private final CaptureRules captureRules;

  private final boolean flyingKings;

  private final boolean menCaptureBackwards;

  private CheckersRules(int boardSize, boolean menCaptureBackwards, boolean flyingKings,
      CaptureRules captureRules) {
    this.boardSize = boardSize;
    this.menCaptureBackwards = menCaptureBackwards;
    this.flyingKings = flyingKings;
    this.captureRules = captureRules;
  }

  /**
   * Returns the board size for this set of rules.
   * 
   * <p>
   * Note that there is only one number - boards are assumed to be square.
   * 
   * @return the size of the board for this set of rules
   */
  public int getBoardSize() {
    return boardSize;
  }

  // TODO: implement and document this!
  public CaptureRules getCaptureRules() {
    return captureRules;
  }

  /**
   * Returns <tt>true</tt> if these rules include the 'Flying Kings' rule.
   * 
   * <p>
   * If the Flying Kings rule is being played kings can move as far as they want
   * along unblocked diagonals. This move can (but needn't) end by a capture in
   * the usual way, jumping over an opposing piece to an adjacent unoccupied
   * square.
   * 
   * @return
   */
  public boolean isFlyingKings() {
    return flyingKings;
  }

  /**
   * Returns <tt>true</tt> if these rules allow men to capture backwards.
   * Otherwise men can only capture pieces ahead of them on the board.
   * 
   * @return <tt>true</tt> if these rules allow men to capture backwards, false
   *         otherwise.
   */

  public boolean isMenCaptureBackwards() {
    return menCaptureBackwards;
  }

  private enum CaptureRules {
    /**
     * If a move exists where an opponent's piece is captured, such a move must
     * be played. If multiple such moves exist the player is free to choose
     * among them.
     */
    MUST_CAPTURE,
    /**
     * The player must select the sequence of moves that maximizes the number of
     * opponent's pieces captured. If multiple sequences of moves result in this
     * maximum the player is free to choose among them.
     */
    MUST_MAXIMISE_CAPTURE,
    /**
     * There are no constraints on a player's choice of move (although the set
     * of available moves is limited by the constrains on how the pieces
     * themselves move).
     */
    NO_CONSTRAINTS;
  }

}
