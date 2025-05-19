import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Simple, self-contained network client for the instructor’s Python game-server.
 *
 * <p>It relies exclusively on already-existing engine classes
 * (Board, BitBoardUtils, Move, …) – no changes to the server code required.</p>
 */
public class Client {

    /* ————————————————————————————————————  configuration  ———————————————————————————————————— */

    private static final String SERVER_HOST = "localhost";
    private static final int    SERVER_PORT = 8000;
    private static final int    BUFFER_SIZE = 4_096;     // matches server-side recv-buffer

    /* ————————————————————————————————————  network fields  ———————————————————————————————————— */

    private Socket         socket;
    private InputStream    in;
    private OutputStream   out;
    private final Gson     gson = new Gson();

    /* ————————————————————————————————————  game/engine fields  ———————————————————————————————————— */

    private int            playerId;         // 0 = red, 1 = blue  (as defined by the server)
    private char           myTurnToken;      // 'r' or 'b'
    private final BitBoardUtils engine = new BitBoardUtils();

    /* =================================================================================================================
                                              │ public bootstrap │
       ===============================================================================================================*/

    public static void main(String[] args) {
        try {
            new Client().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, InterruptedException {
        connect();
        gameLoop();
        close();
    }

    /* =================================================================================================================
                                              │ network helpers │
       ===============================================================================================================*/

    private void connect() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);
        in     = socket.getInputStream();
        out    = socket.getOutputStream();

        // The server sends a single byte: '0' or '1'
        int firstByte = in.read();
        if (firstByte == -1) throw new IOException("Server closed connection before sending player ID");
        playerId     = firstByte - '0';
        myTurnToken  = (playerId == 0) ? 'r' : 'b';

        System.out.printf("Connected – I am player %d (%s)%n",
                playerId, (myTurnToken == 'r' ? "RED" : "BLUE"));
    }

    private void close() {
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}
    }

    /**
     * Sends a payload (already a <em>single JSON value</em>) and waits for the server’s JSON reply.
     */
    private String sendAndReceive(String jsonValue) throws IOException {
        // encode and flush
        byte[] payload = jsonValue.getBytes(StandardCharsets.UTF_8);
        out.write(payload);
        out.flush();

        // read reply
        byte[] buf = new byte[BUFFER_SIZE];
        int len = in.read(buf);
        if (len == -1) throw new IOException("Server closed connection");
        return new String(buf, 0, len, StandardCharsets.UTF_8);
    }

    /* =================================================================================================================
                                              │ main game loop │
       ===============================================================================================================*/

    private void gameLoop() throws IOException, InterruptedException {

        GameState state = requestGameState();           // initial state
        while (!state.bothConnected) {                  // wait for opponent
            Thread.sleep(200);
            state = requestGameState();
        }

        while (!state.end) {

            boolean myTurn = (myTurnToken == state.turn.charAt(0));
            if (myTurn) {
                String moveStr = chooseMove(state.board);

                if (moveStr == null) {                  // no legal move – concede
                    System.err.println("No legal moves! Terminating.");
                    break;
                }
                FenUtils.printBoard(state.board);
                System.out.println("→ " + moveStr);
                state = sendMove(moveStr);              // server responds with updated state

            } else {
                // poll politely while the opponent thinks
                Thread.sleep(100);
                state = requestGameState();
            }
        }

        System.out.println("Game finished – server reported ‘end=true’. Closing connection.");
    }

    /* =================================================================================================================
                                              │ high-level helpers │
       ===============================================================================================================*/

    /**
     * Performs a single `"get"` round-trip.
     */
    private GameState requestGameState() throws IOException {
        String reply = sendAndReceive(gson.toJson("get"));
        return gson.fromJson(reply, GameState.class);
    }

    /**
     * Sends a move string (already validated by our engine) to the server and returns the resulting state.
     */
    private GameState sendMove(String move) throws IOException {
        String reply = sendAndReceive(gson.toJson(move));
        return gson.fromJson(reply, GameState.class);
    }

    private int evaluate(Board board){
        return (-1) * board.numPieces(board.getCurrentPlayer()) + 1000 * (BitBoardUtils.checkplayerWon(board) ? 1:0);
    }

    /**
     * Builds a legal move for the current FEN and converts it into the server’s “A7-B7-1” format.
     */
    private String chooseMove(String fen) {
        try {
            Board board = new Board(fen);                       // parses “<diagram> <turn>”
            List<BitBoardUtils.MovePair> moves =
                    engine.generateAllLegalMoves(board);

            if (moves.isEmpty()) return null;                   // no legal moves

            BitBoardUtils.MovePair choice = engine.pickMove(moves, board);
            Move m = choice.toMove();
            return m.toAlgebraic();

        } catch (Exception e) {                                 // any parsing / engine failure → no move
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts an internal {@link Move} into the server protocol “A7-B7-1”.
     */
    private static String moveToWireFormat(Move m) {
        char fromFile = (char) ('A' + m.fromCol);
        int  fromRank = 7 - m.fromRow;
        char toFile   = (char) ('A' + m.toCol);
        int  toRank   = 7 - m.toRow;
        return "" + fromFile + fromRank + '-' + toFile + toRank + '-' + m.moveHeight;
    }

    /* =================================================================================================================
                                               │ helper record │
       ===============================================================================================================*/

    /**
     * Mirror of the JSON object the server sends after each request.
     * (Field names match exactly, so default GSON mapping works.)
     */
    private static class GameState {
        String board;                // FEN + side-to-move
        String turn;                 // "r" / "b"
        boolean bothConnected;       // both players ready
        @SerializedName("time")
        long timeMs;                 // remaining time in ms (server’s clock)
        boolean end;                 // true when the game is finished
    }
}
