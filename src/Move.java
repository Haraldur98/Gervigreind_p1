public class Move {
    
    public int x1, y1, x2, y2;
    
    // constructor
    public Move(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // string representation of the move
    public String to_string() {
        return "(move " + this.x1 + " " + this.y1 + " " + this.x2 + " " + this.y2 + ")";
    }

    // add one to the coordinates of the move since the game is 1-indexed 
    public void add_one() {
        this.x1 += 1;
        this.y1 += 1;
        this.x2 += 1;
        this.y2 += 1;
    }

    // subtract one from the coordinates of the move since the game is 1-indexed
    public void subtract_one() {
        this.x1 -= 1;
        this.y1 -= 1;
        this.x2 -= 1;
        this.y2 -= 1;
    }

    // is the move diagonal
    public boolean is_diagonal() {
        return Math.abs(this.x2 - this.x1) == Math.abs(this.y2 - this.y1);
    }

}
