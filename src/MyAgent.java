import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MyAgent implements Agent {
	// private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private double playclock; // this is how much time (in seconds) we have before nextAction needs to return
								// a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private Enviorment enviorment;

	private static final int MAX_VALUE = Integer.MAX_VALUE;
	private static final int MIN_VALUE = Integer.MIN_VALUE;

	// perfomance Measure
	private int totalExpansions;
	private double totalTime;
	private int totalDepthLimit;

	// init(String role, int playclock) is called once before you have to select the
	// first action. Use it to initialize the agent.
	// role is either "white" or "black" and playclock is the number of seconds
	// after which nextAction must return.
	public void init(String role, int width, int height, int playclock) {
		this.width = width;
		this.height = height;
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		enviorment = new Enviorment(width, height);
	}

	// lastMove is null the first time nextAction gets called (in the initial state)
	// otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last
	// player did
	public String nextAction(int[] lastMove) {

		if (lastMove != null) {

			Move last_move = new Move(lastMove[0], lastMove[1], lastMove[2], lastMove[3]);
			String roleOfLastPlayer = (myTurn && role.equals("white")) || (!myTurn && role.equals("black")) ? "white"
					: "black";
			System.out.println(roleOfLastPlayer + " moved from " + last_move.x1 + "," + last_move.y1 + " to "
					+ last_move.x2 + "," + last_move.y2);

			// update the board with the last move
			last_move.subtract_one();
			this.enviorment.move(this.enviorment.current_state, last_move);
			System.out.println(this.enviorment.current_state.to_string_move(last_move));
		}

		// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {

			// preforme alpha-beta search to find the best move
			long startTime = System.nanoTime();
			ArrayList<Move> next_moves = this.enviorment.get_legal_moves(this.enviorment.current_state);
			int bestValue = MIN_VALUE;
			Move bestMove = null;
			int depth = 1;

			while (true) {

				Move currentBestMove = null;
				for (Move move : next_moves) {

					this.enviorment.move(this.enviorment.current_state, move);
					int newValue = alphaBetaSearch(this.enviorment.current_state, depth, MIN_VALUE, MAX_VALUE, false);
					this.enviorment.undo_move(this.enviorment.current_state, move);

					if (newValue > bestValue) {
						bestValue = newValue;
						currentBestMove = move;
					}
				}

				long endTime = System.nanoTime();
				double duration = (endTime - startTime) / 1e9;
				// if the time is up or the depth limit is reached
				if (duration >= playclock - 1 || currentBestMove == null) {
					break;
				}

				bestMove = currentBestMove;
				depth++;
			}

			// Output performance metrics
			long endTime = System.nanoTime();
			double duration = (endTime - startTime) / 1e9;
			totalTime += duration;
			totalDepthLimit = Math.max(totalDepthLimit, depth);
			System.out.println("Turn Time: " + duration + " seconds");

			if (bestMove != null) {

				bestMove.add_one(); // add one to the coordinates of the move since the game is 1-indexed
				return bestMove.to_string(); // return the move in the required format
			} else {
				return "noop";
			}
		} else {
			return "noop";
		}
	}

	private int alphaBetaSearch(State state, int depth, int alpha, int beta, boolean maximizingPlayer) {
		totalDepthLimit = Math.max(totalDepthLimit, depth);
		totalExpansions++;

		// end of the game or depth limit reached
		if (depth == 0 || state.isGameOver()) {
			return evaluateState();
		}

		return maximizingPlayer ? maximize(state, depth, alpha, beta) : minimize(state, depth, alpha, beta);
	}

	private int maximize(State state, int depth, int alpha, int beta) {
		int value = MIN_VALUE;
		ArrayList<Move> legalMoves = enviorment.get_legal_moves(state);
		for (Move move : legalMoves) {
			enviorment.move(state, move);
			int newValue = alphaBetaSearch(state, depth - 1, alpha, beta, false);
			value = Math.max(value, newValue);
			alpha = Math.max(alpha, value);
			enviorment.undo_move(state, move);
			if (beta <= alpha) {
				break; // Beta cut-off
			}
		}
		return value;
	}

	private int minimize(State state, int depth, int alpha, int beta) {
		int value = MAX_VALUE;
		ArrayList<Move> legalMoves = enviorment.get_legal_moves(state);
		for (Move move : legalMoves) {
			enviorment.move(state, move);
			int newValue = alphaBetaSearch(state, depth - 1, alpha, beta, true);
			value = Math.min(value, newValue);
			beta = Math.min(beta, value);
			enviorment.undo_move(state, move);
			if (beta <= alpha) {
				break; // Alpha cut-off
			}
		}
		return value;
	}

	// Evaluate the current state of the game
	public int evaluateState() {

		int distance = this.enviorment.evaluateDistance(this.role);
		int mobility = this.enviorment.evaluateMobility(this.role);
		int danger = this.enviorment.evaluateDangers(this.role);
		int isGameOver = this.enviorment.current_state.isGameOver() ? 1000 : 0;

		// wheights for the evaluation function 
		int distanceWeight = 10;
		int mobilityWeight = 1;
		int dangerWeight = 2;

		int value = distanceWeight * distance + mobilityWeight * mobility + dangerWeight * danger + isGameOver;

		return value;
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		try {
			FileWriter file = new FileWriter("log.txt", true);
			file.append("~~~~~~~~~~~GAME OUTPUT~~~~~~~~~~~\n");
			file.append("height: " + this.enviorment.height + " width: " + this.enviorment.width +  "\n");
			file.append("Final Total Expansions: " + totalExpansions + "\n");
			file.append("Final Total Time: " + totalTime + " seconds" + "\n");
			file.append("Final Total Depth Limit: " + totalDepthLimit + "\n");
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Final Total Expansions: " + totalExpansions);
		System.out.println("Final Total Time: " + totalTime + " seconds");
		System.out.println("Final Total Depth Limit: " + totalDepthLimit);
		totalTime = 0;
		totalExpansions = 0;
		totalDepthLimit = 0;
		this.enviorment = null;
	}

}
