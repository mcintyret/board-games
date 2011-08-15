package logger;

import game.Game;
import game.Move;
import game.Observer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import player.Player;

/**
 * Logs all the events that take place in a {@link Game} when registered as an
 * {@link Observer} in that <code>Game</code>.
 * 
 * <p>
 * The name of the log file is generated automatically based on the time and
 * date of creation.
 * 
 * @author Tom McIntyre
 * 
 */
public class Logger implements Observer {

  private final static DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
  private final static String logDirName = "logs";
  private final static File logDir = new File(logDirName);

  private final Game game;

  private final File logFile;

  private static boolean createLogDir() {
    return (logDir.exists() || logDir.mkdir());
  }

  public Logger(Game game) {
    this.game = game;
    if (!createLogDir()) throw new RuntimeException("Error creating log directories");
    Date date = new Date();
    logFile = new File(logDirName + "/" + dateFormat.format(date));
  }

  @Override
  public void notifyOnMove(Move move) {
    write(move + "\n");
  }

  @Override
  public void notifyOnPromotion(Move lastMove) {
    // This is taken care of by logging the actual promotion move
  }

  @Override
  public void notifyOnStalemate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void notifyOnStart() {
    write("Game started\n");
  }

  @Override
  public void notifyOnUndo() {
    write("Previous move undone\n");
  }

  @Override
  public void notifyOnWin(Player winner) {
    write(winner.getName() + " won the game\n");
  }

  private void write(String string) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void notifyOnCurrentPlayerChanged() {
    write(game.getCurrentPlayer() + "'s turn\n");
  }

}
