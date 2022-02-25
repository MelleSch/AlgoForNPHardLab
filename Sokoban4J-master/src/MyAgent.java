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
	
	@Override
	protected List<EDirection> think(BoardCompact board) {
		this.board = board;
		searchedNodes = 0;
		long searchStartMillis = System.currentTimeMillis();

//		dfs(5, result); // the number marks how deep we will search (the longest plan we will consider)
		visitedboards = new HashSet<>();
		deadsquares = DeadSquareDetector.detect(board);

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

		// If the board is a victory then it is highest priority possible
		// Else return a value calculated by a multitude of factors such as:
		// Bad Factors:
		// The state has been visited (use hashmap)
		// The state has a box on a dead square
		// The character is far away from a box (look for the smallest manhattan distance to a box)
		// Good Factors:
		// Boxes are on the goal/close to the goal (look for the smallest manhattan distance between boxes and goals)

		// If the board has been visited before return a big number to make sure the path is low priority
		int heurval =  0;

		return heurval;
	}

	private List<EDirection> aStar() {
		PriorityQueue<Node> pqueue = new PriorityQueue<>();
		List<CAction> actions = new ArrayList<>(4);

		for (CMove move : CMove.getActions()) {
			if (move.isPossible(board)) {
				actions.add(move);
			}
		}
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

		for (CAction action : actions) {
			BoardCompact boardcopy = board.clone();
			action.perform(boardcopy);
			visitedboards.add(boardcopy);
			ArrayList<CAction> moves = new ArrayList<>();
			moves.add(action);
			Node newNode = new Node(moves, boardcopy);
			pqueue.add(newNode);
		}

		/*
			XXXXX
			X   X
			X P X
			X $.X
			XXXXX
		 */

		while (!pqueue.isEmpty()) {
			Node node = pqueue.remove();
			BoardCompact currboard = node.state;
			searchedNodes++;

			// CHECK VICTORY
			if (currboard.isVictory()) {
				// SOLUTION FOUND!
				return node.path.stream().map(CAction::getDirection).collect(Collectors.toList());
			}
			List<CAction> nextActions = new ArrayList<>();

			// Add all potential moves
			for (CMove move : CMove.getActions()) {
				if (move.isPossible(currboard)) {
					nextActions.add(move);
				}
			}

			// Add all potential pushes
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

			// Loop through the next possible actions, add them to PQ if they are any good.
			for (CAction nextAction : nextActions) {
				BoardCompact newBoard = currboard.clone();
				nextAction.perform(newBoard);

				if (visitedboards.contains(newBoard)) {
					continue;
				}

				ArrayList<CAction> actionList = new ArrayList<>();
				actionList.addAll(node.path);
				actionList.add(nextAction);
				if (newBoard.isVictory()) return node.path.stream().map(CAction::getDirection).collect(Collectors.toList());
				Node newNode = new Node(actionList, newBoard);
				visitedboards.add(newBoard);
				if (newNode.heuristic != Integer.MAX_VALUE/2) {
					pqueue.add(newNode);
				}
			}
		}
		System.out.println("Queue emptied out, no solution found");
		return new ArrayList<>();
	}


	private boolean dfs(int level, List<EDirection> result) {
		if (level <= 0) return false; // DEPTH-LIMITED
		
		++searchedNodes;
		
		// COLLECT POSSIBLE ACTIONS
		
		List<CAction> actions = new ArrayList<CAction>(4);
		
		for (CMove move : CMove.getActions()) {
			if (move.isPossible(board)) {
				actions.add(move);
			}
		}
		for (CPush push : CPush.getActions()) {
			if (push.isPossible(board)) {
				actions.add(push);
			}
		}
		
		// TRY ACTIONS
		for (CAction action : actions) {
			// PERFORM THE ACTION
			result.add(action.getDirection());
			action.perform(board);
			
			// CHECK VICTORY
			if (board.isVictory()) {
				// SOLUTION FOUND!
				return true;
			}
			
			// CONTINUE THE SEARCH
			if (dfs(level - 1, result)) {
				// SOLUTION FOUND!
				return true;
			}
			
			// REVERSE ACTION
			result.remove(result.size()-1);
			action.reverse(board);
		}
		
		return false;
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
			return Integer.compare(this.path.size() + this.heuristic, this.path.size() + other.heuristic);
		}
	}
}


