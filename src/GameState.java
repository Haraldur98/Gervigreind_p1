import java.util.ArrayList;

public class GameState {
    private int[][] board;
    private int width;
    private int height;

    public GameState(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new int[width][height];

        initializeBoard(); // initialize the board with the given state

        System.out.println(this.toString()); // print the initial state of the board

    }

    // get best move for the current state of the game (using alpha-beta search)
    // returns an array of 4 integers: x1, y1, x2, y2
    // it takes in acount for the role of the player and the time left to play
    public int[] getBestMove() {
        int[] bestMove = new int[4];
        



        return bestMove;

    }

    public boolean isTerminal() {
        // check if the game is over
        // checks if the board cpieces on winnign positions
        for (int i = 0; i < this.width; i++) {
            if (this.board[i][0] == 2) {
                return true;
            }
            if (this.board[i][height-1] == 1) {
                return true;
            }
        }
        return false;
    }

    public void clone(GameState state) {
        // clone the state of the game
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.board[i][j] = state.board[i][j];
            }
        }
    }


    // evaluate the heuristic of the current state of the game for the given role (white or black)
    // valuatte the distance of the pieces to the promotion zone for the given role
    public int evaluateHeuristic(String currentRole) {
        int playerDistance = 0;
        int opponentDistance = 0;
    
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] == 2) { // White piece
                    if (currentRole.equals("white")) {
                        playerDistance += height - j; // Distance to promotion zone for white piece
                    } else {
                        opponentDistance += j + 1; // Distance to promotion zone for black piece
                    }
                } else if (board[i][j] == 1) { // Black piece
                    if (currentRole.equals("black")) {
                        playerDistance += j + 1; // Distance to promotion zone for black piece
                    } else {
                        opponentDistance += height - j; // Distance to promotion zone for white piece
                    }
                }
            }
        }
    
        return playerDistance - opponentDistance;
    }

    
    // Generate all possible moves for the current state of the game for the given role (white or black)
    public ArrayList<Integer[]> generateMoves(String currentRole) {
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
    
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
    
                if (board[i][j] == 1 && currentRole.equals("black")) { // Black piece

                    if (j < height - 1) {                       
                        if (i > 0 && board[i-1][j+1] == 0) { // Diagonal left
                            Integer[] move = {i, j, i-1, j+1};
                            moves.add(move);
                        }
                        if (i < width - 1 && board[i+1][j+1] == 0) { // Diagonal right
                            Integer[] move = {i, j, i+1, j+1};
                            moves.add(move);
                        }
                    }
                } else if (board[i][j] == 2 && currentRole.equals("white")) { // White piece

                    if (j > 0) {
                        if (i > 0 && board[i-1][j-1] == 0) { // Diagonal left
                            Integer[] move = {i, j, i-1, j-1};
                            moves.add(move);
                        }
                        if (i < width - 1 && board[i+1][j-1] == 0) { // Diagonal right
                            Integer[] move = {i, j, i+1, j-1};
                            moves.add(move);
                        }
                    }
                }
            }
        }
    
        return moves;
    }


    // check if a move is valid
    public boolean isValidMove(int x1, int y1, int x2, int y2) {
        if (x1 < 1 || x1 > width || x2 < 1 || x2 > width || y1 < 1 || y1 > height || y2 < 1 || y2 > height) { // if the coordinates are out of bounds
            return false;
        }
         if (board[x1-1][y1-1] == 0 || board[x2-1][y2-1] != 0) { // if the starting cell is empty or the ending cell is not empty
            return false;
        }
        if (board[x1-1][y1-1] == 1 && y2 - y1 == 1 && Math.abs(x2 - x1) == 1) { // if the piece is black and the move is diagonal and forward
            return true;
        }
        if (board[x1-1][y1-1] == 2 && y1 - y2 == 1 && Math.abs(x2 - x1) == 1) { // if the piece is white and the move is diagonal and forward
            return true;
        }
        return false;
    }


    // move a piece from (x1, y1) to (x2, y2)
    public void move(int x1, int y1, int x2, int y2) {
        board[x2-1][y2-1] = board[x1-1][y1-1];
        board[x1-1][y1-1] = 0;
    }


    // initilize the board with the given state
    private void initializeBoard() {
        // Initialize the board and fill in the pieces
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (j < 2 ) {
                    this.board[i][j] = 1; // 1 represents a white piece
                } else if (j > this.height - 3) {
                    this.board[i][j] = 2; // 2 represents a black piece
                } else {
                this.board[i][j] = 0; // 0 represents an empty cell
               }
            }
        } 
    }


    // string representation of the board fliped
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
}
