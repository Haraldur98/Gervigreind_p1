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

    // evaluate the state
    public int evaluate(String role) {
        int playerDistance = 0;
        int opponentDistance = 0;
        int playerMobility = 0;
        int opponentMobility = 0;
    
        char playerPiece = role.equals("white") ? WHITE : BLACK;
        char opponentPiece = role.equals("white") ? BLACK : WHITE;
    
        // Evaluate distance and mobility
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] == playerPiece) { // Player's piece
                    playerDistance += (role.equals("white") ? height - j : j);
                    playerMobility += countLegalMoves(i, j);
                } else if (board[i][j] == opponentPiece) { // Opponent's piece
                    opponentDistance += (role.equals("white") ? height - j : j);
                    opponentMobility += countLegalMoves(i, j);
                }
            }
        }
    
        // Evaluate positional advantage (example weights)
        int playerPositionalAdvantage = 0;
        int opponentPositionalAdvantage = 0;
        playerPositionalAdvantage += (role.equals("white") ? board[width / 2][height - 1] == playerPiece ? 2 : 0 : board[width / 2][0] == playerPiece ? 2 : 0);
        opponentPositionalAdvantage += (role.equals("white") ? board[width / 2][height - 1] == opponentPiece ? 2 : 0 : board[width / 2][0] == opponentPiece ? 2 : 0);
    
        // Combine the evaluation components with appropriate weights
        int playerEvaluation = playerDistance * 2 + 2 * playerMobility + playerPositionalAdvantage;
        int opponentEvaluation = opponentDistance * 2  + 2 * opponentMobility + opponentPositionalAdvantage;
    
        return playerEvaluation - opponentEvaluation;
    }

    // count the legal moves for a piece at (x, y)
    public int countLegalMoves(int x, int y) {
        int count = 0;

        return count;
    }

    public int countDangers(int x, int y) {
        int count = 0;


        return count;
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
