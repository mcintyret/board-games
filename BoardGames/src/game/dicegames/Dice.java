package game.dicegames;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents any number of dice up to and including <tt>Integer.MAX_VALUE</tt>.
 * 
 * <p>
 * The dice can have any number of faces up to and including
 * <tt>Integer.MAX_VALUE</tt>. However, note that all the dice represented will
 * have the same number of faces.
 * 
 * 
 * @author Tom McIntyre
 * 
 */
public class Dice {

  private static final int DEFAULT_FACES = 6;

  private final int faces;
  private final int numDice;
  private final Random rng = new Random();
  private final int[] scores;

  private int totalScore;

  public Dice() {
    this(1);
  }

  public Dice(int numDice) {
    this(numDice, DEFAULT_FACES);
  }

  public Dice(int numDice, int faces) {
    this.numDice = numDice;
    this.faces = faces;
    scores = new int[numDice];

  }

  /**
   * Returns the number of dice represented by this <code>Dice</code> object.
   * 
   * @return the number of dice represented by this <code>Dice</code> object
   */
  public int getNumDice() {
    return numDice;
  }

  /**
   * Returns a copy of the <code>scores</code> array which holds the score of
   * each individual die represented by this <code>Dice</code> object.
   * 
   * <p>
   * A copy is returned to prevent the underlying array being changed - a
   * possible method for cheating!
   * 
   * @return the individual scores of the dice
   */
  public int[] getScores() {
    return Arrays.copyOf(scores, scores.length);
  }

  /**
   * Returns the sum of the scores of all the dice represented by this
   * <code>Dice</code> object.
   * 
   * @return the sum of the scores of all the dice represented by this
   *         <code>Dice</code> object
   */
  public int getTotalScore() {
    return totalScore;
  }

  /**
   * Generates a new random score for each die represented by this
   * <code>Dice</code> object. The score is between 1 and <code>faces</code>
   * inclusive.
   * 
   * <p>
   * The sum of the scores of all the dice is cached for efficiency of the
   * <code>getTotalScore</code> method.
   */
  public void rollDice() {
    totalScore = 0;
    for (int i = 0; i < numDice; i++) {
      scores[i] = rng.nextInt(faces) + 1;
      totalScore += scores[i];
    }
  }

  /**
   * Returns the number of faces of the dice represented by this
   * <code>Dice</code> object.
   * 
   * @return the number of faces of the dice represented by this
   *         <code>Dice</code> object
   */
  public int getFaces() {
    return faces;
  }

}
