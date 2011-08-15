package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import logger.Logger;
import pieces.Piece;
import player.Player;

/**
 * A skeletal implementation of the {@link Game} interface.
 * 
 * <p>
 * Provides default implementation of many of the methods in the
 * <code>Game</code> interface.
 * 
 * <p>
 * Provides several additional methods for updating the game state. These
 * methods may be overridden in subclasses to provide game-specific
 * implementations. However, they do not form part of the core <code>Game</code>
 * API.
 * 
 * @author Tom McIntyre
 * 
 */
public abstract class AbstractGame implements Game {

  private int playerIndex = 0;

  protected List<Piece>[][] boardPieces;
  protected Color[][] boardColors;
  protected int boardHeight;
  protected int boardWidth;

  protected Player currentPlayer;

  protected final GameType gameType;
  protected final Stack<Move> moveHistory = new Stack<Move>();
  protected final List<Observer> observers = new LinkedList<Observer>();
  protected final List<Player> players = new ArrayList<Player>();

  protected AbstractGame(GameType gameType) {
    this.gameType = gameType;
  }

  @Override
  public void addObserver(Observer o) {
    observers.add(o);
  }

  @Override
  public void addPieceAt(int row, int col, Piece piece) {
    if (piece == null) return;
    boardPieces[piece.getRow()][piece.getCol()].remove(piece);
    boardPieces[row][col].add(piece);

    piece.setRow(row);
    piece.setCol(col);
  }

  @Override
  public void addPlayers(List<Player> players) {
    this.players.addAll(players);
  }

  @Override
  public void changeCurrentPlayerColor(Color newColor) {
    if (newColor == null) return;
    currentPlayer.setColor(newColor);
  }

  @Override
  public void doMove(Move move, boolean isDummy) {
    moveHistory.push(move);
    boolean turnOver;

    do {
      Piece moving = move.getMovingPiece();
      Piece captured = move.getCapturedPiece();

      // Removing the captured piece (if any) from the game
      removePiece(captured);

      // Updating the piece's location
      addPieceAt(move.destRow(), move.destCol(), moving);

      // Notifying the piece that it has moved
      moving.doMove(move);

      if (!isDummy) {
        for (Observer o : observers) {
          o.notifyOnMove(move);
        }

        if (checkWinConditions()) {
          for (Observer o : observers) {
            o.notifyOnWin(currentPlayer);
          }
        }
      }

      turnOver = isTurnOver(move);
      if (turnOver) updateCurrentPlayerToNext();
      move = move.getNextMove();
    } while (move != null);

    if (turnOver) {
      for (Observer o : observers) {
        o.notifyOnCurrentPlayerChanged();
      }
    }
  }

  @Override
  public Color getBoardColorAt(int row, int col) {
    return boardColors[row][col];
  }

  @Override
  public int getBoardHeight() {
    return boardHeight;
  }

  @Override
  public int getBoardWidth() {
    return boardWidth;
  }

  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public GameType getGameType() {
    return gameType;
  }

  @Override
  public Move getLastMove() {
    if (moveHistory.isEmpty()) return null;
    return moveHistory.peek();
  }

  @Override
  public Map<String, String[]> getGameSpecificOptions() {
    // The default case when there are no game-specific options
    return null;
  }

  @Override
  public Piece getPieceAt(int row, int col) {
    throw new UnsupportedOperationException(
        "Only games that guarantee that only one pieces occupies a square at a time support this operation");
  }

  @Override
  public List<Piece> getPiecesAt(int row, int col) {
    if (row < 0 || row >= boardHeight) return null;
    if (col < 0 || col >= boardWidth) return null;
    return boardPieces[row][col];
  }

  @Override
  public List<Player> getPlayers() {
    return players;
  }

  protected void initializeBoardPieces() {
    for (Player player : players) {
      addInitialPieces(player);
    }
  }

  @Override
  public void removePiece(Piece piece) {
    if (piece == null) return;
    piece.getPlayer().removePiece(piece);
    boardPieces[piece.getRow()][piece.getCol()].remove(piece);
  }

  @Override
  public void setBoardDimensions(int boardHeight, int boardWidth) {
    if (boardHeight <= 0 || boardWidth <= 0) {
      throw new IllegalArgumentException("Board Dimensions Must Be Positive");
    }
    this.boardHeight = boardHeight;
    this.boardWidth = boardWidth;
    boardColors = new Color[boardHeight][boardWidth];
    boardPieces = (LinkedList<Piece>[][]) new LinkedList[boardHeight][boardWidth];
  }

  @Override
  public void implementSelectedOptions(Map<String, String> selectedOptions) {
    // Default case where there are no options
  }

  protected abstract void initialiseBoardColors();

  @Override
  public void start() {
    initialiseBoardColors();
    initializeBoardPieceLists();
    initializeBoardPieces();
    initializePlayers();
    addObserver(new Logger(this));

    for (Observer o : observers) {
      o.notifyOnStart();
    }
  }

  @Override
  public void undoMove(boolean dummy) {
    if (moveHistory.isEmpty()) return;

    Move lastMove = moveHistory.peek();
    while (lastMove.getNextMove() != null)
      lastMove = lastMove.getNextMove();

    do {
      if (isTurnOver(lastMove)) updateCurrentPlayerToPrev();

      Piece movingBack = lastMove.getMovingPiece();

      if (!dummy && lastMove.destroyMovingPieceOnUndo()) {
        removePiece(movingBack);
      } else {
        addPieceAt(lastMove.startRow(), lastMove.startCol(), movingBack);
      }

      // Restoring the captured piece, if any
      Piece resurrected = lastMove.getCapturedPiece();
      if (resurrected != null) {
        resurrected.getPlayer().addPiece(resurrected);
        addPieceAt(resurrected.getRow(), resurrected.getCol(), resurrected);
      }

      // Notifying the piece
      movingBack.undoMove(lastMove);

      if (!dummy) {
        for (Observer o : observers) {
          o.notifyOnUndo();
        }
      }
      lastMove = lastMove.getPrevMove();
    } while (lastMove != null);

    for (Observer o : observers) {
      o.notifyOnCurrentPlayerChanged();
    }
    moveHistory.pop();
  }

  private void initializeBoardPieceLists() {
    for (int i = 0; i < boardPieces.length; i++) {
      for (int j = 0; j < boardPieces[i].length; j++) {
        boardPieces[i][j] = new LinkedList<Piece>();
      }
    }
  }

  private void initializePlayers() {
    for (int i = 0; i < players.size(); i++) {
      updateCurrentPlayerToNext();
      currentPlayer.updateLegalMoves();
    }
    updateCurrentPlayerToNext(); // So the game starts with the correct player
  }

  protected abstract void addInitialPieces(Player player);

  protected Integer getNumericOption(String optionKey, Map<String, String> selectedOptions) {
    // Returns an Integer if a valid integer was entered, null otherwise.
    String optionVal = selectedOptions.get(optionKey);
    try {
      return Integer.valueOf(optionVal);
    } catch (NullPointerException e) {
      return null;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * A convenience method that initializes boardColors to a black and white
   * checker board pattern.
   */
  protected void initializeCheckerboard() {
    Color color = Color.WHITE;
    for (int row = 0; row < boardHeight; row++) {
      for (int col = 0; col < boardWidth; col++) {
        boardColors[row][col] = color;
        if (col == boardWidth - 1 && boardWidth % 2 == 0) continue;
        color = color == Color.WHITE ? Color.BLACK : Color.WHITE;
      }
    }
  }

  protected void updateCurrentPlayerToNext() {
    currentPlayer = players.get(playerIndex);
    playerIndex++;
    if (playerIndex >= players.size()) playerIndex -= players.size();

  }

  protected void updateCurrentPlayerToPrev() {
    currentPlayer = players.get(playerIndex);
    playerIndex--;
    if (playerIndex < 0) playerIndex += players.size();
  }
}
