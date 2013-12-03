import java.util.*;
import java.io.*;
class Main {
    public static void main(String[] args) throws IOException{
		/*//accept
		Scanner scanIn = new Scanner(System.in);
		Client myplayer = new Client("imcs.svcs.cs.pdx.edu","3589","thang","1234");
		System.out.print("Enter Game ID: ");
		String playId = scanIn.nextLine();
		myplayer.accept(playId,'B');
		State game = new State();
	    Move opponentMove;
		Move bestmove;
		boolean checkwin = false;
		
		while(game.gameover() == false){
			if (game.turn == false){
				opponentMove = game.convertHumanToStateMove(myplayer.getMove());
				game.move(opponentMove);
				game.isPbecomeQ();
				checkwin = false;
			}
			else{
			    Vector <Move> possiblemoves = game.moveGenB();
				bestmove = game.ttable_bestmove(game, 1);
				
				boolean temp = false;
				for(int i=0; i<possiblemoves.size(); ++i){
				    if(bestmove.equalTo(possiblemoves.elementAt(i))){
					    temp = true;
                        break;						
					}
				}
				if(temp ==false){
				    Random generator = new Random();
					bestmove = possiblemoves.elementAt(generator.nextInt(possiblemoves.size()));
					System.out.println("Random best move");
				}
				   
				game.move(bestmove);
				game.isPbecomeQ();
				myplayer.sendMove(game.ConvertToHumanMove(bestmove));
				checkwin = true;
			}
		}
		if(checkwin == false)
			System.out.println("White won");
		else
			System.out.println("Black won");
		*/
		//offer
		Client myplayer = new Client("imcs.svcs.cs.pdx.edu","3589","thang1","1234");
		myplayer.offer('W');
		State game = new State();
	    Move opponentMove;
		Move bestmove;
		boolean checkwin = false;
		
		while(game.gameover() == false){
			if (game.turn == false){
			    Vector <Move> possiblemoves = game.moveGenW();
				bestmove = game.ttable_bestmove(game, 1);
				
				boolean temp = false;
				for(int i=0; i<possiblemoves.size(); ++i){
				    if(bestmove.equalTo(possiblemoves.elementAt(i))){
					    temp = true;
                        break;						
					}
				}
				if(temp ==false){
				    Random generator = new Random();
					bestmove = possiblemoves.elementAt(generator.nextInt(possiblemoves.size()));
					System.out.println("Random best move");
				}
				
				game.move(bestmove);
				game.isPbecomeQ();
				myplayer.sendMove(game.ConvertToHumanMove(bestmove));
				checkwin = false;
			}
			else{
				opponentMove = game.convertHumanToStateMove(myplayer.getMove());
				game.move(opponentMove);
				game.isPbecomeQ();
				checkwin = true;
			}
		}
		if(checkwin == false)
			System.out.println("White won");
		else
			System.out.println("Black won");
			
	}
}