import java.util.*;
class ZobristTable {
    private long [][] arrayIndexSquarePiece ;                          //array of entries of Zobrist Table
	private long randomW;                                              // random number for White turn
	private long randomB;                                              //random number for Black turn 
	
	//Initialize all the array entries to random long integers and set random long integers for White turn and Black turn  
	ZobristTable(){
	    arrayIndexSquarePiece = new long [30][13];
	    Random randomNum = new Random();
	    for(int i=0; i<30; ++i)
		   	for(int j=0; j<13; ++j)
				arrayIndexSquarePiece[i][j] = randomNum.nextLong();
		randomW = randomNum.nextLong();
		randomB = randomNum.nextLong(); 
	}
	
	//return an entry with given square number and piece number
	public long lookupentry(int squarenum, int pi){
		return arrayIndexSquarePiece[squarenum][pi];
	}
	
	//return random number of Black turn.
	public long BNum(){
		return randomB;
	}
	
	//return random number of White turn.
	public long WNum(){
		return randomW;
	}
	
	
}