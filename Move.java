/* Name: Thang Tran
   CS 442 - Homework 1
*/
class Move {
    Square toSquare;                              //to square
	Square fromSquare;                            //from square
	
	//Initialize a move
	Move(Square fromSquare, Square toSquare){
	    this.toSquare = toSquare;
		this.fromSquare = fromSquare;
	}
	
	//get to square
	public Square getToSquare(){
	    return toSquare;
	}
	
	//get from square
	public Square getFromSquare(){
	    return fromSquare;
	}
	
	//check if a move is equal to another move
	public boolean equalTo(Move b){
	    Square bfrom = b.getFromSquare();
		Square bto = b.getToSquare();
		if(toSquare.equalTo(bto) && fromSquare.equalTo(bfrom))
		    return true;
		else
		    return false;
	}
	
	//set the current Move to a given Move
	public void setTo(Move a){
	    Square afrom = a.getFromSquare();
		Square ato = a.getToSquare();
		fromSquare.setTo(afrom);
		toSquare.setTo(ato);
	}
}