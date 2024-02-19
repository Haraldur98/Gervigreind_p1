public class State {

    public char[][] board;
    public int width;
    public int height;
    static final char EMPTY = ' ';
    static final char BLACK = 'B';
    static final char WHITE = 'W';
    public boolean is_white_turn = true; 


    public State(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new char[width][height];
        initializeBoard();
    }

    // check if the game is over
    public boolean isGameOver() {
        // checks if the board cpieces on winnign positions
        for (int i = 0; i < this.width; i++) {
            if (this.board[i][0] == BLACK) {
                return true;
            }
            if (this.board[i][height-1] == WHITE) {
                return true;
            }
        }
        return false;
    }


    //to string
    public String toString() {
        String s = "";
        for (int j = this.height - 1; j >= 0; j--) {
            for (int i = 0; i < this.width; i++) {
                s += this.board[i][j] + " ";
            }
            s += "\n";
        }
        return s;
    }

    // to string with move colored 
    public String to_string_move(Move move) {
        String s = "";
        for (int j = this.height - 1; j >= 0; j--) {
            for (int i = 0; i < this.width; i++) {
                if (i == move.x1 && j == move.y1) {
                    s += "\033[94m"+ "o" +"\033[0m" + " ";
                } else if (i == move.x2 && j == move.y2) {
                    s += "\033[91m"+ this.board[i][j] +"\033[0m" + " ";
                } else {
                    s += this.board[i][j] + " ";
                } 
            }
            s += "\n";
        }
        return s;
    }

    // fill the board (in start state)
    public void initializeBoard() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (j < 2 ) {
                    this.board[i][j] = WHITE;
                } else if (j > this.height - 3) {
                    this.board[i][j] = BLACK;
                } else {
                    this.board[i][j] = EMPTY;
                }
            }
        } 
    }


}
