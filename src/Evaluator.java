/*
 * Evaluator.java
 * Rupak Lamsal - rlamsal
 * John Nelson - jpnelson
 */

public class Evaluator {
	Board state;
	
	/*
	 * heuristicEvaluation
	 * 
	 * param: state - current game state
	 * param: numToWin - number of N to win
	 * return: Heuristic Evaluation of game state
	 */
	public int heuristicEvaluation(Board _state) {
		//h = w1f1 + w2f2 + ... + wnfn
		state = new Board(_state);
		double ourValue = 0;
		double oppValue = 0;
		
		for (int n = 1; n <= state.N; n++){
			ourValue = ourValue + howManyTogether(n) * Math.pow(n, n);
		}
		
		for (int n=1; n <= state.N; n++){
			oppValue = oppValue + howManyTogether_2(n) * Math.pow(n, n);
		}
		
		//value = state.hasTwo()*weight2 + state.hasThree()*weight3 + 
		return (int)(ourValue - oppValue);
	}
	
	/*
	 * howManyTogether
	 * 
	 * @param n - amount of disks
	 * @return value of board for this player
	 */
	public int howManyTogether(int n){
		int count = 0;
		
		count += checkHorizontally(n, 1);

		count += checkVertically(n, 1);

		count += checkDiagonally1(n, 1);

		count += checkDiagonally2(n, 1);

		return count;
		
	}
	
	/*
	 * howManyTogether_2
	 * 
	 * @param n - amount of disks
	 * @return value of board for this player
	 */
	public int howManyTogether_2(int n){
		int count = 0;
		
		count += checkHorizontally(n, 2);

		count += checkVertically(n, 2);

		count += checkDiagonally1(n, 2);

		count += checkDiagonally2(n, 2);

		return count;
	}
	
	/*
	 * checkHorizontally
	 * 
	 * @param n - amount of disks
	 * @param player
	 * @return number of combos horizontally
	 */
	public int checkHorizontally(int n, int player){
		int max = 0;
		int count = 0;

		//check each row, horizontally
		for(int i = 0; i < state.height; i++){
			max = 0;
			for(int j = 0;j < state.width; j++){

				if(state.board[i][j] == player){
					max++;

					if(max == n)
						count++;

					if(max > n)
						count--;
				}
				else
					max = 0;

			}
		} 

		return count;
	}
	
	/*
	 * checkVertically
	 * 
	 * @param n - amount of disks
	 * @param player
	 * @return number of combos vertically
	 */
	public int checkVertically(int n, int player){
		//check each column, vertically
		int max1=0;
		int count = 0;

		for(int i=0;i<state.width;i++){
			max1=0;

			for(int j = 0;j<state.height;j++){
				if(state.board[j][i] == player){
					max1++;

					if(max1 == n)
						count++;

					if(max1 > n)
						count--;
				}
				else
					max1 = 0;
			}
		} 

		return count;
	}

	/*
	 * checkDiagonally1
	 * 
	 * @param n - amount of disks
	 * @param player
	 * @return number of combos diagonally
	 */
	public int checkDiagonally1(int n, int player){
		//check diagonally y=-x+k
		int max1 = 0;
		int count = 0;

		int N = state.N;
		int upper_bound = state.height - 1 + 
						  state.width - 1 - (N - 1);

		for(int k = N - 1; k <= upper_bound; k++){			
			max1 = 0;

			int x, y;
			if(k < state.width) 
				x = k;
			else
				x=state.width-1;
			y = -x + k;

			while(x >= 0  && y < state.height){
				// System.out.println("k: "+k+", x: "+x+", y: "+y);
				if(state.board[state.height - y - 1][x] == player){
					max1++;

					if(max1 == n)
						count++;

					if(max1 > n)
						count--;
				}
				else
					max1 = 0;
				x--;
				y++;
			}	 

		}

		return count;
	}
	
	/*
	 * checkDiagonally1
	 * 
	 * @param n - amount of disks
	 * @param player
	 * @return number of combos diagonally
	 */
	public int checkDiagonally2(int n, int player){
		//check diagonally y=x-k
		int max1 = 0;
		int N = state.N;
		int height = state.height;
		int width = state.width;
		int count = 0;

		int upper_bound = width -(N - 1) - 1;
		int  lower_bound = -(height - (N - 1) - 1);
		// System.out.println("lower: "+lower_bound+", upper_bound: "+upper_bound);
		for(int k = lower_bound; k <= upper_bound; k++){			
			max1 = 0;

			int x, y;
			if(k >= 0) 
				x = k;
			else
				x = 0;
			y = x - k;
			while(x >= 0 && x < width && y < height){
				// System.out.println("k: "+k+", x: "+x+", y: "+y);
				if(state.board[height - y - 1][x] == player){
					max1++;

					if(max1 == n)
						count++;
					
					if(max1 > n)
						count--;
				}
				else
					max1 = 0;
				x++;
				y++;
			}	 

		}	 //end for y=x-k

		return count;
	}
}
