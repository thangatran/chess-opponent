/* Name: Thang Tran
   CS 442 - Homework 1
*/
class Square {
    private int x;                     //x coordinate
	private int y;                     //y coordinate
	
	//Initialize a square
	Square (int x, int y){     
	    this.x = x;
		this.y = y;
	}
	
	//get x
	public int getx(){
	    return x;
	}
	
	//get y
	public int gety(){
	    return y;
	}
	
	//true if two squares are equal. Otherwise false
	public boolean equalTo(Square b){
	    if ((x == b.getx()) && (y == b.gety()))
		    return true;
		else
		    return false;
	}
	
	//set the current Square to a given Square
	public void setTo(Square a){
	    x = a.getx();
		y = a.gety();
	}
}