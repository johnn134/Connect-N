/*
 * Node.java
 * Rupak Lamsal - rlamsal
 * John Nelson - jpnelson
 */
import java.util.ArrayList;

public class Node {
	
	Board state;			//The state of the game this Node holds
	Operator move;			//The operator used to reach this game state
	int value;				//The node's heuristic value
	ArrayList<Node> children;		//This node's children Nodes
	boolean finalState;		//Is the node a final state
	int player;				//The player that made this game state's last move
	Operator bestMove;		//The best move to be made at this game state
	boolean canPop;			//Can the player still pop
	int depth;
	
	/*
	 * Node
	 * 
	 * creates a root node
	 * 
	 * @param: state - node's game state
	 * @param: n - board columns
	 */
	public Node(Board _state, Operator _move, int _player, boolean _canPop, int _depth) {
		state = new Board(_state);
		move = _move;
		player = _player;
		canPop = _canPop;
		depth = _depth;
		
		children = new ArrayList<Node>();
		bestMove = null;
		finalState = false;
		
		//Check if the state is a final game state
		if(state.isConnectN() != -1 || state.isFull())
			finalState = true;
		
		//Find the heuristic value of this game state
		Evaluator ev = new Evaluator();
		value = ev.heuristicEvaluation(state);
		
		//Create children to given depth
		this.createChildren();
	}
	
	/*
	 * getValue
	 * 
	 * @return value
	 */
	public int getValue() {
		return value;
	}
	
	/*
	 * getState
	 * 
	 * @return state
	 */
	public Board getState() {
		return state;
	}
	
	/*
	 * getMove
	 * 
	 * @return move
	 */
	public Operator getMove() {
		return move;
	}
	
	/*
	 * getBestMove
	 * 
	 * @return bestMove
	 */
	public Operator getBestMove() {
		return bestMove;
	}
	
	/*
	 * createChildren
	 * 
	 * creates leaf nodes for all possible moves from this state
	 */
	public void createChildren() {
		//Only create children for this node if it is not a final game state
		if(!finalState && depth > 0) {
			/* Create children nodes for each possible game operator */
			for(int i = 0; i < this.state.width; i++) {
				Board temp = new Board(this.state);

				//Attempt to apply operators to state
				//Operator: place disk
				if(temp.canDropADiscFromTop(i, nextPlayer())) {
					temp.dropADiscFromTop(i, nextPlayer());
					this.children.add(new Node(temp, new Operator(i,1), nextPlayer(), canPop, depth - 1));
				}
				
				if(canPop) {
					//Operator: pop disk
					temp = this.state;

					//Attempt to apply operator to state
					if(temp.canRemoveADiscFromBottom(i, nextPlayer())) {
						temp.removeADiscFromBottom(i);
						this.children.add(new Node(temp, new Operator(i,0), nextPlayer(), false, depth - 1));
					}
				}
			}
		}
	}
	
	/*
	 * nextPlayer
	 * 
	 * @return the player that goes next (1 or 2)
	 */
	private int nextPlayer() {
		if(player == 1)
			return 2;
		else
			return 1;
	}

	/*
	 * maxValue
	 * 
	 * @param: current state of the game-> node?.
	 * @return
	 */
	public int maxValue(int _alpha, int _beta){
		//If this node is a leaf, return its value
		if (children.isEmpty()) 
			return value;
		
		//Otherwise, recurse maxValue
		int maxValue = (int) Double.NEGATIVE_INFINITY;	//Get -infinity
		int alpha = _alpha;
		int beta = _beta;
		
		//run minValue on each child
		for (Node child : children){
			//Check the maximum value between maxValue and the value of child
			int maxCheck = Math.max(maxValue, child.minValue(alpha, beta));
			
			//If the child has a better value, update this node's bestMove and value
			if(maxCheck != maxValue) {
				maxValue = maxCheck;			//update MaxValue
				bestMove = child.getMove();		//update bestMove to child's bestMove
				value = child.value;			//update value to child's value
			}
			
			//Apply alpha-beta pruning if necessary
			alpha = Math.max(alpha, maxValue);
			if(alpha >= beta)
				return value;
			
		}		
		return value;
	}
	
	/*
	 * minValue
	 * 
	 * @param: current state of the game-> node?.
	 * @return
	 */
	public int minValue(int _alpha, int _beta){
		//If this node is a leaf, return its value
		if (children.isEmpty()) 
			return value;

		//Otherwise, recurse minValue
		int minValue = (int) Double.POSITIVE_INFINITY;	//Get +infinity
		int alpha = _alpha;
		int beta = _beta;
		
		//If this node is a parent, run maxValue on each child
		for (Node child : children){
			//Check the maximum value between maxValue and the value of child
			int minCheck = Math.min(minValue, child.maxValue(alpha, beta));
			
			//If the child has a better value, update this node's bestMove and value
			if(minCheck != minValue) {
				minValue = minCheck;
				bestMove = child.getMove();
				value = minValue;
			}
			
			//Apply alpha-beta pruning if necessary
			beta = Math.min(beta, minValue);
			if(beta <= alpha)
				return value;
		}		
		return value;
	}
}


