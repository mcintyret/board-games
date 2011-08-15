package tools;

import java.awt.Color;
import java.util.Random;

public class Tools {
  private static final Random rng = new Random();

  /**
   * Returns a randomly generated <code>Color</code>.
   * 
   * <p>
   * More precisely, this method produces a <code>Color</code> with randomly
   * generated red, green and blue components from the default sRGB
   * <code>ColorSpace</code> (ie between 0 and 255). The alpha component is not
   * provided and so is 255 by default - the <code>Color</code> returned will be
   * completely opaque.
   * 
   * @return a randomly generated, completely opaque <code>Color</code>
   */
  public static Color getRandomColor() {
    int r = rng.nextInt(256);
    int g = rng.nextInt(256);
    int b = rng.nextInt(256);

    return new Color(r, g, b);
  }
}
