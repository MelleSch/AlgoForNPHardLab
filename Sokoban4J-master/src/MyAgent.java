import static java.lang.System.out;

import java.util.*;
import java.util.stream.Collectors;

import agents.ArtificialAgent;
import game.actions.EDirection;
import game.actions.compact.*;
import game.board.compact.BoardCompact;
import game.board.compact.CTile;

/**
 * The simplest Tree-DFS agent.
 * @author Jimmy
 */
public class MyAgent extends ArtificialAgent {
	protected BoardCompact board;
	protected int searchedNodes;
	protected HashSet<BoardCompact> visitedboards;
	protected boolean[][] deadsquares;
	protected int[][] targetDistances;
	
	@Override
	protected List<EDirection> think(BoardCompact board) {
		this.board = board;
		searchedNodes = 0;
		long searchStartMillis = System.currentTimeMillis();

		visitedboards = new HashSet<>();
		deadsquares = DeadSquareDetector.deadsquares(board);
		targetDistances = MinTargetDistancesCalculator.distances(board);

		List<EDirection> ret = aStar();

		long searchTime = System.currentTimeMillis() - searchStartMillis;
        
        if (verbose) {
            out.println("Nodes visited: " + searchedNodes);
            out.printf("Performance: %.1f nodes/sec\n",
                        ((double)searchedNodes / (double)searchTime * 1000));
        }
		
		return ret.isEmpty() ? null : ret;
	}

	private int heur(BoardCompact board) {
		int heurval = 0;
		for (int x = 0; x < board.width(); x++) {
			for (int y = 0; y < board.height(); y++) {
				int tile = board.tile(x, y);
				if (CTile.isSomeBox(tile)) {
					heurval += targetDistances[x][y];
				}
			}
		}
		return heurval;
	}


	// Runs the A* algorithm.
	private List<EDirection> aStar() {

		// Initialize the Priority Queue and list of actions.
		PriorityQueue<Node> pqueue = new PriorityQueue<>();
		List<CAction> actions = new ArrayList<>(4);


		// Loop through the possible moves from the initial board state, add possible actions to list of actions.
		for (CMove move : CMove.getActions()) {
			if (move.isPossible(board)) {
				actions.add(move);
			}
		}

		// Loop through the potential pushes from the initial board state, add possible actions to list of actions.
		// If push location is a dead square, ignore the square.
		for (CPush push : CPush.getActions()) {
			if (push.isPossible(board)) {
				int x = board.playerX + 2 * push.getDirection().dX;
				int y = board.playerY + 2 * push.getDirection().dY;
				if (deadsquares[x][y]) {
					continue;
				}
				actions.add(push);
			}
		}

		// Loop through the collected actions.
		// For each action, create a new board state and perform the action. Mark new board as visited.
		// Create a new Node from the newly created board state and add it to PQ.
		for (CAction action : actions) {
			BoardCompact boardcopy = board.clone();
			action.perform(boardcopy);
			visitedboards.add(boardcopy);
			ArrayList<CAction> moves = new ArrayList<>();
			moves.add(action);
			Node newNode = new Node(moves, boardcopy);
			pqueue.add(newNode);
		}

		// Run the main while loop when PQ is empty.
		while (!pqueue.isEmpty()) {
			Node node = pqueue.remove();
			BoardCompact currboard = node.state;
			searchedNodes++;

			// Check for victory.
			if (currboard.isVictory()) {
				// If solution found, map CAction list to EDirection list and return it.
				return node.path.stream().map(CAction::getDirection).collect(Collectors.toList());
			}

			// Initialize a list of next actions.
			List<CAction> nextActions = new ArrayList<>();

			// Loop through the possible moves from the initial board state, add possible actions to list of actions.
			for (CMove move : CMove.getActions()) {
				if (move.isPossible(currboard)) {
					nextActions.add(move);
				}
			}

			// Loop through the potential pushes from the initial board state, add possible actions to list of actions.
			// If push location is a dead square, ignore the square.
			for (CPush push : CPush.getActions()) {
				if (push.isPossible(currboard)) {
					int x = currboard.playerX + 2 * push.getDirection().dX;
					int y = currboard.playerY + 2 * push.getDirection().dY;
					if (deadsquares[x][y]) {
						continue;
					}
					nextActions.add(push);
				}
			}

			// Loop through the collected actions.
			for (CAction nextAction : nextActions) {
				BoardCompact newBoard = currboard.clone();

				// Perform the action.
				nextAction.perform(newBoard);

				if (visitedboards.contains(newBoard)) {
					continue;
				}

				// Initialize the list of actions we can do, add all past actions + next action.
				ArrayList<CAction> actionList = new ArrayList<>();
				actionList.addAll(node.path);
				actionList.add(nextAction);

				// If victory.
				if (newBoard.isVictory()) return node.path.stream().map(CAction::getDirection).collect(Collectors.toList());

				// Create a new node.
				Node newNode = new Node(actionList, newBoard);

				// Add to visited boards.
				visitedboards.add(newBoard);
				pqueue.add(newNode);
			}
		}
		System.out.println("Queue emptied out, no solution found");
		return new ArrayList<>();
	}

	class Node implements Comparable<Node> {
		private int heuristic;
		private ArrayList<CAction> path;
		private BoardCompact state;

		public Node(ArrayList<CAction> path, BoardCompact state) {
			this.path = path;
			this.state = state;
			this.heuristic = heur(state);
		}

		@Override
		public int compareTo(Node other) {
			return Integer.compare(this.path.size() + this.heuristic, other.path.size() + other.heuristic);
		}
	}
}


