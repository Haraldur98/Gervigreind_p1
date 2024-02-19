import java.util.ArrayList;

public class Enviorment {
    
    public State current_state; 
    public int width;
    public int height;

    static final char EMPTY = ' ';
    static final char BLACK = 'B';
    static final char WHITE = 'W';

    public Enviorment(int width, int height) {
        this.width = width;
        this.height = height;
        this.current_state = new State(width, height);
    }

    public boolean can_move_n_steps_forward(State state, int y, int max_height_black, int max_height_white) {
        if (state.is_white_turn && y <= max_height_white) return true;
        if (!state.is_white_turn && y >= max_height_black) return true;
        return false;
    }

    // 
    private void get_moves(State state, ArrayList<Move> moves, int x, int y) {
        char opponent = state.is_white_turn ? BLACK : WHITE; 
        int one_step = state.is_white_turn ? 1 : -1;
        int two_step = state.is_white_turn ? 2 : -2;
    
        // Two steps forward and one step to the left or right
        if (can_move_n_steps_forward(state, y, 2, this.height - 3)) {
            if (x > 0 && state.board[x - 1][y + two_step] == EMPTY) {
                moves.add(new Move(x, y, x - 1, y + two_step));
            }
            if (x < this.width - 1 && state.board[x + 1][y + two_step] == EMPTY) {
                moves.add(new Move(x, y, x + 1, y + two_step));
            }
        }
    
        // One step forward and two steps to the left or right
        if (can_move_n_steps_forward(state, y, 1, this.height - 4)) {
            if (x > 1 && state.board[x - 2][y + one_step] == EMPTY) {
                moves.add(new Move(x, y, x - 2, y + one_step));
            }
            if (x < this.width - 2 && state.board[x + 2][y + one_step] == EMPTY) {
                moves.add(new Move(x, y, x + 2, y + one_step));
            }
        }
    
        // Diagonal moves
        if (can_move_n_steps_forward(state, y, 1, this.height - 3)) {
            if (x > 0 && state.board[x - 1][y + one_step] == opponent) {
                moves.add(new Move(x, y, x - 1, y + one_step));
            }
            if (x < this.width - 1 && state.board[x + 1][y + one_step] == opponent) {
                moves.add(new Move(x, y, x + 1, y + one_step));
            }
        }
    }
    

    // get all legal moves for the current state of the game for the given role (white or black)
    public ArrayList<Move> get_legal_moves(State state) {
        ArrayList<Move> moves = new ArrayList<Move>();
        char friendly = current_state.is_white_turn ? WHITE : BLACK;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (state.board[x][y] == friendly) {
                    get_moves(state, moves, x, y);
               }
            }
        }
        return moves;
    }

    // move a piece from (x1, y1) to (x2, y2)
    public void move(State state, Move move) {
        state.board[move.x2][move.y2] = state.board[move.x1][move.y1];
        state.board[move.x1][move.y1] = EMPTY;
        state.is_white_turn = !state.is_white_turn;

    }

    public void undo_move(State state, Move move) {
        if (move.is_diagonal()) {
            state.board[move.x1][move.y1] = state.board[move.x2][move.y2];
            state.board[move.x2][move.y2] = state.is_white_turn ? WHITE : BLACK;
        } else {
            state.board[move.x1][move.y1] = state.board[move.x2][move.y2];  /// DOSE THIS NEED TO BE TEMP (as in video)
            state.board[move.x2][move.y2] = EMPTY;
        }
        state.is_white_turn = !state.is_white_turn;
    }

}
