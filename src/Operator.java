/*
 * Operator.java
 * Rupak Lamsal - rlamsal
 * John Nelson - jpnelson
 */

public class Operator {
	
	int column;		//Column to make a move on
	int move;		//Move to use: 1 - drop, 0 - pop
	
	/*
	 * Default Operator constructor
	 */
	public Operator(int _column, int _move) {
		column = _column;
		move = _move;
	}
	
	/*
	 * getColumn
	 * 
	 * @return column number
	 */
	public int getColumn() {
		return column;
	}
	
	/*
	 * getMove
	 * 
	 * @return move to be made: 1 - drop, 0 - pop
	 */
	public int getMove() {
		return move;
	}
}
