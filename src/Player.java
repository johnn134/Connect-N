/*
 * Player.java
 * Rupak Lamsal - rlamsal
 * John Nelson - jpnelson
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
//import java.util.Iterator;
import java.util.List;


public class Player {

	String playerName = "Lynyrd";
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	boolean first_move = true;
	int playerNumber;
	Board board;
	boolean canPop = true;
	
	int height, width, numWin, turnTime;
	
	/*
	 * main
	 */
	public static void main(String[] args) throws IOException {
		Player rp = new Player();
		
		//Print out playerName
		System.out.println(rp.playerName);
		while (true){
			rp.processInput();
		}
	}
	
	/*
	 * processInput
	 */
	public void processInput() throws IOException {	
    	String s = input.readLine();	
		List<String> gameinfo = Arrays.asList(s.split(" "));
		
		//React to input
		if(gameinfo.size()==2){		//Opponent's move
			if(first_move) {
				first_move = false;
		
				//Parse opponent's move
				Operator opponentMove = new Operator(Integer.parseInt(gameinfo.get(0)),
						Integer.parseInt(gameinfo.get(1)));
				
				//Create empty board
				board = new Board(height, width, numWin);
				
				//Apply opponent's move to board
				updateBoard(opponentMove, 2);
				
				//Choose next move
				Operator myMove = processGame();
				
				//Print move
				System.out.println("" + myMove.getColumn() + " " + myMove.getMove() + "");
			}
			else {
				//Parse opponent's move
				Operator opponentMove = new Operator(Integer.parseInt(gameinfo.get(0)),
						Integer.parseInt(gameinfo.get(1)));
				
				//Apply opponent's move to board
				updateBoard(opponentMove, 2);
				
				//Choose next move
				Operator myMove = processGame();
				
				//Print move
				System.out.println("" + myMove.getColumn() + " " + myMove.getMove() + "");
			}
		}
		else if(gameinfo.size() == 1){	//Game over
			System.out.println("game over!!!");
			System.exit(0);
		}
		else if(gameinfo.size() == 5){	//Game Beginning Information
			/* Input is read as V W X Y Z
			 * V - board height
			 * W - board width
			 * X - number of pieces to win
			 * Y - turn of the player (1 - 1st player, 2 - 2nd player
			 * Z - time limit in seconds
			 */
			
			height = Integer.parseInt(gameinfo.get(0));
			width = Integer.parseInt(gameinfo.get(1));
			numWin = Integer.parseInt(gameinfo.get(2));
			turnTime = Integer.parseInt(gameinfo.get(4));
			
			if(Integer.parseInt(gameinfo.get(3)) == playerNumber) {
				first_move = false;
				//Create empty board
				board = new Board(height, width, numWin);
				
				//Choose next move
				Operator myMove = processGame();
				
				//Print move
				System.out.println("" + myMove.getColumn() + " " + myMove.getMove() + "");
			}
		}
		//player1: aa player2: bb
		else if(gameinfo.size() == 4){		//Player order	
			//read which player number this is
			if(gameinfo.get(1).equals(playerName))
				playerNumber = 1;
			else
				playerNumber = 2;
		}
		else
			System.out.println("not what I want");
	}
	
	/*
	 * processGame
	 * 
	 * @return the next move to be made
	 */
	public Operator processGame() {
		boolean hasTime = true;
		Operator nextMove = null;
		
		//Get the current time
		long startTime = System.currentTimeMillis();
		int counter = 1;
		
		//While loop for creating tree depths and running minimax
		while(hasTime) {
			//Build a depth of the memory tree
			Node root = new Node(board, null, 2, canPop, counter);
			
			//Run minimax (or alpha beta)
			int alpha = (int) Double.NEGATIVE_INFINITY;	//Get -infinity
			int beta = (int) Double.POSITIVE_INFINITY;	//Get +infinity
			root.maxValue(alpha, beta);
			
			nextMove = root.getBestMove();

			counter++;	//Increase depth for next iteration
			
			//Check time left (if there is time, loop; if not stop loop)
			long curTime = System.currentTimeMillis();
			
			if(curTime - startTime + (long)Math.pow(3.2, counter + 1) > (long)(turnTime * 1000)) {
				hasTime = false;
			}
		}
		
		//Apply my move to board
		updateBoard(nextMove, 1);
		
		//If pop, update hasPopped
		if(nextMove.getMove() == 0)
			canPop = false;
		
		//return best option
		return nextMove;
	}

	/*
	 * udpateBoard
	 * 
	 * @param move - move to be made on board
	 * @param player - player making that move
	 */
	public void updateBoard(Operator move, int player) {
		//Apply move to board
		if(move.getMove() == 1) {
			board.dropADiscFromTop(move.getColumn(), player);
		}
		else if(move.getMove() == 0) {
			board.removeADiscFromBottom(move.getColumn());
		}
	}
}


