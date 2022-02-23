import static java.lang.System.out;

import java.util.*;

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
		
		List<EDirection> result = new ArrayList<EDirection>();
//		dfs(5, result); // the number marks how deep we will search (the longest plan we will consider)
		visitedboards = new HashSet<>();
		deadsquares = DeadSquareDetector.detect(board);

		List<EDirection> ret = aStar(1);

		long searchTime = System.currentTimeMillis() - searchStartMillis;
        
        if (verbose) {
            out.println("Nodes visited: " + searchedNodes);
            out.printf("Performance: %.1f nodes/sec\n",
                        ((double)searchedNodes / (double)searchTime * 1000));
        }
		
		return ret.isEmpty() ? null : ret;
	}

	private int heur(List<CAction> path) {
//		for (int i = 0; i < path.size(); i++) {
//			path.get(i).perform(board);
//		}
		// If the board is a victory then it is highest priority possible
		if (board.isVictory()) {
			return Integer.MIN_VALUE/2;
		}
		// Else return a value calculated by a multitude of factors such as:
		// Bad Factors:
		// The state has been visited (use hashmap)
		// The state has a box on a dead square
		// The character is far away from a box (look for the smallest manhattan distance to a box)
		// Good Factors:
		// Boxes are on the goal/close to the goal (look for the smallest manhattan distance between boxes and goals)

		// If the board has been visited before return a big number to make sure the path is low priority
		boolean dead = false;
		for(int x = 0; x < board.width(); x++) {
			for (int y = 0; y < board.height(); y++) {
				if (deadsquares[x][y]) {
					if (CTile.isSomeBox(board.tile(x, y))) {
						dead = true;
						break;
					}
				}
			}
			if (dead) {
				break;
			}
		}
		int heurval = visitedboards.contains(board) || dead ? Integer.MAX_VALUE/2 : 0;

//		for (int i = path.size() - 1; i >= 0; i--) {
//			path.get(i).reverse(board);
//		}
		return heurval;
	}

	private List<EDirection> aStar(int level) {
		++searchedNodes;

		List<EDirection> result = new ArrayList<>();
		PriorityQueue<List<CAction>> pqueue = new PriorityQueue<>(4, new PathComparator());
		List<CAction> actions = new ArrayList<>(4);
		Map<List<CAction>, Double> costs = new HashMap<>();

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

		for (CAction action : actions) {
			ArrayList<CAction> moves = new ArrayList<>();
			moves.add(action);
			pqueue.add(moves);
			costs.put(moves, 1.0 + heur(moves)); // TODO: What should this value be?
		}

		/*
			XXXXX
			X   X
			X P X
			X $.X
			XXXXX
		 */

		while (!pqueue.isEmpty()) {

			// clean current results list
			result.clear();

			List<CAction> qActions = pqueue.remove();

			for (CAction a : qActions) {
				result.add(a.getDirection());
				a.perform(board);
			}
			visitedboards.add(board);
			// CHECK VICTORY
			if (board.isVictory()) {
				// SOLUTION FOUND!
				return result;
			}

			double currentCost = costs.get(qActions);
			double newCost;
			List<CAction> nextActions = new ArrayList<>();


			// Add all potential moves
			for (CMove move : CMove.getActions()) {
				if (move.isPossible(board)) {
					nextActions.add(move);
				}
			}

			// Add all potential pushes
			for (CPush push : CPush.getActions()) {
				if (push.isPossible(board)) {
					nextActions.add(push);
				}
			}

			// Loop through the next possible actions, add them to PQ if they are any good.
			for (CAction nextAction : nextActions) {
				List<CAction> actionList = new ArrayList<>();
				actionList.addAll(qActions);
				actionList.add(nextAction);
				newCost = currentCost + 1;// + heur(actionList);

				if (!costs.containsKey(actionList)) {
					costs.put(actionList, newCost);
					pqueue.add(actionList);
				}
				else if (costs.get(actionList) > newCost) {
					costs.replace(actionList, newCost);
					pqueue.add(actionList);
				}
			}
			for (int i = qActions.size() - 1; i >= 0; i--) {
				qActions.get(i).reverse(board);
			}
		}
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

	class PathComparator implements Comparator<List<CAction>> {
		@Override
		public int compare(List<CAction> p1, List<CAction> p2) {
			// Add heuristic function to compare here
			return Integer.compare(p1.size() + heur(p1), p2.size() + heur(p2));
		}
	}
}


