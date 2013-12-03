import java.util.*;
import java.util.concurrent.TimeUnit;

public class State {
	public char [][] board = new char [6][5];                             //our main board 
	public boolean turn;                                                  //convention: false is White, true is black
	public int moveNumberW;                                               //move number of White
	public int moveNumberB;                                               //move number of Black

	public Move m0;                                                       //best move of negamax method
	public long hash;                                                     //hash number
	public ZobristTable zotable;                                          //zobrist table
	public TTable ttable;                                                 //ttable 
	 
	public int bonusN, bonusn, bonusB, bonusb, bonusR, bonusr;            //bonus points
	
    //create new game	
	State(){
	    turn = false; 
		moveNumberW = 0;
		moveNumberB = 0;
        
        Square temp1 = new Square (-1,-1);
		Square temp2 = new Square (-1,-1);
        m0 = new Move(temp1,temp2);
		
		hash = 0;
		zotable = new ZobristTable();
		ttable = null;
		
		board[0][0] = 'k';
		board[0][1] = 'q';
		board[0][2] = 'b';
		board[0][3] = 'n';
		board[0][4] = 'r';
		for(int i=0; i<5; ++i){
		    board[1][i]= 'p';
			board[4][i]= 'P';
			board[2][i]='.';
			board[3][i]='.';
		}
		int j = 4;
		for(int i=0; i<5; ++i)
		    board[5][i]= Character.toUpperCase(board[0][j--]);
		
		bonusN = 0;
		bonusn = 0;
        bonusB = 0;
        bonusb = 0;
        bonusR = 0;
        bonusr = 0;
	}
	
	//print the current state
	public void PrintState(){
	    int h = 6;
		if(!turn){
		    System.out.println(" W");
			System.out.print(moveNumberW);
		}
		else{
		    System.out.println(" B");
			System.out.print(moveNumberB);
		}
		System.out.println("  abcde");
		System.out.println("-------");
	    for(int i=0; i<6; ++i){
		    System.out.print(h-- + "|");
		    for(int j=0; j<5; ++j){
			    System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
	//generate all possible moves for Black
	public Vector<Move> moveGenB(){
		Vector<Move> moveB = new Vector<Move> ();
		for (int i=0; i<=5; ++i){
		    for(int j=0; j<=4; ++j){
				if(board[i][j]!='.' && Character.isLowerCase(board[i][j])){
				    moveB.addAll(moveList(i,j));
				}
			}
		}
		return moveB;
	}
	
	//generate all possible moves for White
	public Vector<Move> moveGenW(){
		Vector<Move> moveW = new Vector<Move> ();
		for (int i=0; i<=5; ++i){
		    for(int j=0; j<=4; ++j){
				if(board[i][j]!='.' && Character.isUpperCase(board[i][j])){
				    moveW.addAll(moveList(i,j));
				}
			}
		}
		return moveW;
	}
	
	//All possible moves for a piece 
	private Vector<Move> moveList(int x, int y) {
	    char piece = board[x][y];
        Vector<Move> moves = new Vector<Move> ();
		int dx, dy;
		int temp;
        switch (piece){
		    case 'q': case 'Q': case 'k': case 'K':
			    for (dx=-1; dx<=1; ++dx){
				    for (dy=-1; dy<=1; ++dy){
					    if(dx==0 && dy==0)
						    continue;
						if(piece =='q' || piece =='Q'){
						    moves.addAll(moveScan(x,y,dx,dy,false,true));
						}
						else{
						    moves.addAll(moveScan(x,y,dx,dy,true,true));
						}
					}
				}	    
				break;
			case 'r': case 'R': case 'b': case 'B':
			    dx=1;
			    dy=0;
				boolean cap;
				boolean stop;
				if(piece =='r' || piece =='R'){
				    cap = true;
					stop = false;
				}
				else{
				    cap = false;
					stop = true;
				}
				for(int i=1; i<=4; ++i){
				    moves.addAll(moveScan(x,y,dx,dy,stop,cap));
                    temp = dx;
                    dx = dy;
                    dy = temp;
					dy = dy * (-1);
				}
				if(piece =='b' || piece =='B'){
				    dx=1;
			        dy=1;
					for(int i=1; i<=4; ++i){
				        moves.addAll(moveScan(x,y,dx,dy,false,true));
                        temp = dx;
                        dx = dy;
                        dy = temp;
					    dy = dy * (-1);
				    }
				}
				break;
			case 'n': case 'N':
			    dx=2;
			    dy=1;
				for(int i=1; i<=4; ++i){
				    moves.addAll(moveScan(x,y,dx,dy,true,true));
					temp = dx;
                    dx = dy;
                    dy = temp;
					dy = dy * (-1);
				}
				dx=-2;
			    dy=1;
				for(int i=1; i<=4; ++i){
				    moves.addAll(moveScan(x,y,dx,dy,true,true));
					temp = dx;
                    dx = dy;
                    dy = temp;
					dy = dy * (-1);
				}
				break;
			case 'p': case 'P':
			    int dir = 1;
				Vector<Move> m = new Vector<Move> ();
				if (Character.isUpperCase(piece))
				    dir = -1;
				m.addAll(moveScan(x,y,dir,-1,true,true));
				if (m.size() == 1 && board[x+dir][y-1] != '.'){
				    moves.addAll(m);   
				}
				m.removeAllElements();
				m.addAll(moveScan(x,y,dir,1,true,true));
				if (m.size() == 1 && board[x+dir][y+1] != '.'){
				    moves.addAll(m);   
				}
				moves.addAll(moveScan(x,y,dir,0,true,false));
				break;
			default: 
			    break;
        }
        return moves;		
	}
	
	//All possible moves for a piece in a given direction
	private Vector<Move> moveScan(int x0, int y0, int dx, int dy, boolean stopShort, boolean capture) {
	    int x = x0;
		int y = y0;
		boolean isBlack = Character.isLowerCase(board[x0][y0]);
		Vector<Move> moves = new Vector<Move> ();
		do{
		    x = x + dx;
            y = y + dy;
			if ((x<0 || x >=6) || (y<0 || y>=5)){
			    break;
            }
            if(board[x][y] != '.'){
			    if(isBlack == Character.isLowerCase(board[x][y])){
				    break;
				}
				if (!capture){
				    break;
				}
				stopShort = true;
            }
            Square from = new Square(x0,y0);
            Square to = new Square(x,y);
            Move fromto = new Move(from, to);
            moves.addElement(fromto);			
		}
		while(!stopShort);
		return moves;
	}
	
	//move a Move
	public char move (Move movepiece){
	    Square from = movepiece.getFromSquare();
		Square to = movepiece.getToSquare();	
		char capturedpiece = '0';
		if (board[to.getx()][to.gety()]!= '.'){
			capturedpiece = board[to.getx()][to.gety()];
			board[to.getx()][to.gety()] = board[from.getx()][from.gety()];
            board[from.getx()][from.gety()]	= '.';		
		}
		else{
			board[to.getx()][to.gety()] = board[from.getx()][from.gety()];
            board[from.getx()][from.gety()]	= '.';		
		}
		
		if(turn == false)
			++moveNumberW;
		else 
			++moveNumberB;
		turn = !turn;
		return capturedpiece;
	}
	
	//unmove a Move with captured piece
	private void unmove(Move m, char pieCap, boolean checkPcomeQ){
        Square from = m.getFromSquare();
		Square to = m.getToSquare();
        if (checkPcomeQ == true){
		    if (board[to.getx()][to.gety()] == 'Q')
		        board[to.getx()][to.gety()] = 'P';
			else 
			    board[to.getx()][to.gety()] = 'p';
		}
		board[from.getx()][from.gety()] = board[to.getx()][to.gety()];
        if (pieCap == '0')
            board[to.getx()][to.gety()] = '.';
        else
            board[to.getx()][to.gety()] = pieCap;
        turn = !turn;
        if(turn == false)
			--moveNumberW;
		else
			--moveNumberB;
	}
	
	//Check P become Q
	public boolean isPbecomeQ(){
	    for (int i =0; i<5; ++i){
		    if(board[0][i] == 'P'){
			    board[0][i] = 'Q';
			    return true;
			}
		}
		for (int i =0; i<5; ++i){
		    if(board[5][i] == 'p'){
			    board[5][i] = 'q';
				return true;
			}
		}
		return false;
	}
	
	//map human command to array form
	private int maphumantoComputerx(char x){
	    switch(x){
		    case '1': return 5;
			case '2': return 4;
			case '3': return 3;
			case '4': return 2;
			case '5': return 1;
			case '6': return 0;
			default: return -1;
		}
	}
	
	//map human command to array form
	private int maphumantoComputery(char y){
	    switch(y){
		    case 'a': return 0; 
			case 'b': return 1;
			case 'c': return 2;
			case 'd': return 3;
			case 'e': return 4;
			default: return -1;
		}
	}
	
	//convert human move to computer move
	public Move convertHumanToStateMove (String moveCommand){
	    int yfrom = maphumantoComputery(moveCommand.charAt(0));
		int xfrom = maphumantoComputerx(moveCommand.charAt(1));
		int yto = maphumantoComputery(moveCommand.charAt(3));
		int xto = maphumantoComputerx(moveCommand.charAt(4));
		
		Square from = new Square(xfrom,yfrom);
        Square to = new Square(xto,yto);
        Move fromto = new Move(from, to);
        return fromto;
	}
	
	//check game ove
	public boolean gameover () {
	    if (moveNumberB > 40 || moveNumberW > 40){
			return true;
		}
		boolean checkKingB = false ;
		boolean checkKingW = false;
	    for(int i=0; i<6; ++i){
		    for(int j=0; j<5; ++j){
			    if(board[i][j] == 'K')
				    checkKingW = true;
				if(board[i][j] == 'k')
				    checkKingB = true;
			}
		}
        if(!checkKingW || !checkKingB){
			return true;
        }
        Vector<Move> possiblemoves = new Vector<Move>();
		possiblemoves.addAll(moveGenB());
		possiblemoves.addAll(moveGenW());
		if(possiblemoves.isEmpty()){
			return true;
		}
	    return false;
	}
	
	//map array form to human form
	private String maptoHumanx(int x){
	    switch(x){
		    case 0: return "6";
			case 1: return "5";
			case 2: return "4";
			case 3: return "3";
			case 4: return "2";
			case 5: return "1";
			default: return "0";
		}
	}
	
	//map array form to human form
	private String maptoHumany(int y){
	    switch(y){
		    case 0: return "a"; 
			case 1: return "b";
			case 2: return "c";
			case 3: return "d";
			case 4: return "e";
			default: return "0";
		}
	}
	
	//convert array form to human form and print out
	public String ConvertToHumanMove(Move amove){
	    Square to = amove.getToSquare();
		Square from = amove.getFromSquare();
		String result;
		result = maptoHumany(from.gety());
        result = result.concat(maptoHumanx(from.getx()));
        result = result.concat("-");		
		result = result.concat(maptoHumany(to.gety()));
        result = result.concat(maptoHumanx(to.getx()));
		return result;
	}
	
	//value of each piece
	private int valuepiece(char pie){
	    switch(pie){
		    case 'p': 
				return -100;
			case 'P': 
				return 100;
			case 'b': case 'n':
				return -300;
			case 'B': case 'N':
				return 300;
			case 'r': 
			    return -500;
			case 'R': 
			    return 500;
			case 'q': 
			    return -900;
			case 'Q':
       			return 900;
			case 'k': 
			    return -900000;
			case 'K':
        		return 900000;
			default:
			    return 0;
		}
	}
	
    //calculate the score of the state	
	public int evaluateState(){
	    int result = 0;
		for(int i=0; i<6; ++i){
		    for(int j=0; j<5; ++j){
			    result += valuepiece(board[i][j]);
			}
		}
		return result;
	}
	
	//generate all possible move and sort them by value
	private Vector<Move> sortMovesByValues (Vector<Move> sortmoves, Vector<Integer> values){
		int i, j, key;
		Square temp1 = new Square (-1,-1);
		Square temp2 = new Square (-1,-1);
		Move tempmove = new Move(temp1,temp2);
		if(turn == false){
			for(j=1; j<values.size(); ++j){
				key = values.elementAt(j);
				tempmove.setTo(sortmoves.elementAt(j));
				for(i=j-1; (i>=0)&&(values.elementAt(i)<key); i--){
					values.set(i+1,values.elementAt(i));
					sortmoves.elementAt(i+1).setTo(sortmoves.elementAt(i));
				}
				values.set(i+1, key);
				sortmoves.elementAt(i+1).setTo(tempmove);
			}
		}
		else{
			for(j=1; j<values.size(); ++j){
				key = values.elementAt(j);
				tempmove.setTo(sortmoves.elementAt(j));
				for(i=j-1; (i>=0)&&(values.elementAt(i)>key); i--){
					values.set(i+1,values.elementAt(i));
					sortmoves.elementAt(i+1).setTo(sortmoves.elementAt(i));
				}
				values.set(i+1, key);
				sortmoves.elementAt(i+1).setTo(tempmove);
			}
		}
		return sortmoves;
	}
	
	//Sort the possible move of s with given value
	public Vector<Move> sortedMovesFrom(State s, int val){
	    Vector<Move> possiblemoves = new Vector<Move> (); 
		Vector<Integer> values = new Vector<Integer>();
		Vector<Move> unsortedmoves = new Vector<Move>();
		char p = '0';
		int v0 = 0;
		if(s.turn == false)
			possiblemoves.addAll(s.moveGenW());
		else
			possiblemoves.addAll(s.moveGenB());
		for(int i=0; i<possiblemoves.size(); ++i){
		    p = s.move(possiblemoves.elementAt(i));
			boolean checkPprom = s.isPbecomeQ();
			v0 = val - valuepiece(p);
            values.addElement(v0);
			unsortedmoves.addElement(possiblemoves.elementAt(i));
            s.unmove(possiblemoves.elementAt(i), p, checkPprom);			
		}
		return sortMovesByValues(unsortedmoves, values);
	}
	
	//map pieces into a number
	private int mappiecenum(char pi){
		switch(pi){
			case 'k':
				return 0;
			case 'q':
				return 1;
			case 'b':
				return 2;
			case 'n':
				return 3;
			case 'r':
				return 4;
			case 'p':
				return 5;
			case 'K':
				return 6;
			case 'Q':
				return 7;
			case 'B':
				return 8;
			case 'N':
				return 9;
			case 'R':
				return 10;
			case 'P':
				return 11;
			default:
				return 12;
		}
	}
	
	//compute the hash number for the current state
	public long computeHash(){
	    int squareNum = 0;
		long [] arrayzoentries = new long [30]; 
		for(int i=0; i<6; ++i){
			for(int j=0; j<5; ++j){
				arrayzoentries[squareNum] = zotable.lookupentry(squareNum,mappiecenum(board[i][j]));
				++squareNum;
			}
		}
		hash = arrayzoentries[0] ^ arrayzoentries[1];
		for (int i=2; i<30; ++i){
			hash = hash ^ arrayzoentries[i];
		}
		if (turn == false)
			hash = hash ^ zotable.WNum();
		else
			hash = hash ^ zotable.BNum();
		return hash;
	}
	
	//update the hash with give move and captured piece if any.
	private long updateHash(Move m, char capturedpiece, boolean checkPtoQ){
		Square from = m.getFromSquare();
		Square to = m.getToSquare();
		int fromsquarenum = (5*from.getx())+from.gety();
		int tosquarenum =(5*to.getx())+to.gety();
		int frompiecenum =  mappiecenum(board[from.getx()][from.gety()]);
		int topiecenum = mappiecenum (board[to.getx()][to.gety()]);
		int topiecenumBefore = topiecenum;
		if(checkPtoQ == true){
			if(board[to.getx()][to.gety()] == 'Q')
				topiecenumBefore = mappiecenum ('P');
			else
				topiecenumBefore = mappiecenum ('p');
		}
		
		if(capturedpiece!='0'){
		    if(turn == false)
				hash = hash ^ zotable.lookupentry(fromsquarenum,topiecenumBefore) ^ zotable.lookupentry(fromsquarenum,frompiecenum) ^ zotable.lookupentry(tosquarenum,mappiecenum(capturedpiece)) ^ zotable.lookupentry(tosquarenum,topiecenum) ^ zotable.BNum() ^ zotable.WNum();
			else
				hash = hash ^ zotable.lookupentry(fromsquarenum,topiecenumBefore) ^ zotable.lookupentry(fromsquarenum,frompiecenum) ^ zotable.lookupentry(tosquarenum,mappiecenum(capturedpiece)) ^ zotable.lookupentry(tosquarenum,topiecenum) ^ zotable.WNum() ^ zotable.BNum();
		}
		else{
			if(turn == false)
				hash = hash ^ zotable.lookupentry(fromsquarenum,topiecenumBefore) ^ zotable.lookupentry(fromsquarenum,frompiecenum) ^ zotable.lookupentry(tosquarenum,mappiecenum('.')) ^ zotable.lookupentry(tosquarenum,topiecenum) ^ zotable.BNum() ^ zotable.WNum();
			else
				hash = hash ^ zotable.lookupentry(fromsquarenum,topiecenumBefore) ^ zotable.lookupentry(fromsquarenum,frompiecenum) ^ zotable.lookupentry(tosquarenum,mappiecenum('.')) ^ zotable.lookupentry(tosquarenum,topiecenum) ^ zotable.WNum() ^ zotable.BNum();
		}
		return hash;
	}

	//negamax with ttable
	private int negamax_ttable(State s, int d, int a, int b, int val){
		if(s.gameover()==true || d == 0)
		    return val;
		TTableEntry t = ttable.getEntry(s.hash); 
		if((t!= null) && ((t.a<t.value && t.value<t.b) || (t.a<=a && b<=t.b)) && (t.depth>=d)){
			return t.value;
		}
		Vector<Move> M = new Vector<Move>();
		M.addAll(sortedMovesFrom(s,val));
		int v = -1000000000;
		int a0 = a;
		char p;
		int v0;
		long oldhash = s.hash;
		
		for(int i=0; i<M.size(); ++i){
			p = s.move(M.elementAt(i));
			boolean checkPprom = s.isPbecomeQ();
			s.updateHash(M.elementAt(i), p, checkPprom);
			v0 = val - valuepiece(p);
			v = Math.max(v, -(negamax_ttable(s,d-1,-b,-a0,v0)));
			a0 = Math.max(a0, v);
			s.unmove(M.elementAt(i),p,checkPprom);
			s.hash = oldhash;
			if(v>=b){
				v = b;
			    break;
			}
		}
		TTableEntry t1 = new TTableEntry();
		t1.hash = s.hash;
		t1.depth = d;
		t1.a = a;
		t1.b = b;
		t1.value = v;
		ttable.store(t1);
		return v;
	}
	
	//Negamax ttable implementation with deep limit time
	public Move ttable_bestmove(State s, int time){
	    TTable ttabletemp = new TTable();
		ttable = ttabletemp;
		int d0 = 1;
		Vector<Move> M = new Vector<Move> ();
		int valueofstate = s.evaluateState();
		M.addAll(sortedMovesFrom(s,valueofstate));
		long oldhash = s.computeHash();
	
		for (long stop = System.nanoTime()+TimeUnit.SECONDS.toNanos(time); stop>=System.nanoTime();){
		    int v = -1000000000;
			int a0 = -1000000000;
			char p = '0';
			for(int i=0; i<M.size(); ++i){
			    p = s.move(M.elementAt(i));
				boolean checkPprom = s.isPbecomeQ();
				s.updateHash(M.elementAt(i), p, checkPprom);
				int tempvalue = valueofstate - valuepiece(p);
				int v0 = Math.max(v, -(negamax_ttable(s,d0,-1000000000,-a0,tempvalue)));
				a0 = Math.max(a0,v0);
				if(v0>v)
					m0.setTo(M.elementAt(i));
				v = Math.max(v,v0);
				s.unmove(M.elementAt(i),p,checkPprom);
				s.hash = oldhash;
				if(p=='k' || p=='K')
				    return M.elementAt(i);
				if(p=='q' || p=='Q') 
				    return M.elementAt(i);
			}
			d0 += 1;
		}
		return m0;
	}	
	
	//do a bonus score
	public int doBonus(Move amove){
        int bonuspoint = 0;
		Square from = amove.getFromSquare();
	    Square to = amove.getToSquare();
        char pieceonMove = board[from.getx()][from.gety()];
        char piececapturing = board[to.getx()][to.gety()];    
				
		board[to.getx()][to.gety()] = board[from.getx()][from.gety()];
		board[from.getx()][from.gety()] = '.';
		Vector<Move> temp = moveList(to.getx(),to.gety());
		bonuspoint = temp.size() * 30;
		board[from.getx()][from.gety()] = pieceonMove;
		board[to.getx()][to.gety()] = piececapturing;
		
		if((to.getx()==2&&to.gety()==1) || (to.getx()==2&&to.gety()==2) || (to.getx()==2&&to.gety()==3) || (to.getx()==3&&to.gety()==1) || (to.getx()==3&&to.gety()==2) || (to.getx()==3&&to.gety()==3))
			bonuspoint += 50;
	
		if(Character.isLowerCase(pieceonMove))
		    bonuspoint = bonuspoint * (-1); 
        if((piececapturing!='.') && (Character.isLowerCase(pieceonMove)!= Character.isLowerCase(piececapturing)))
			bonuspoint += valuepiece(piececapturing);
			
        switch(pieceonMove){	
            case 'b':
                if (to.getx()==0 && to.gety()==2){
                    bonusb = 0;
                }
                else{
					bonusb -= 50;
                }
				return bonuspoint + bonusb;
			case 'B':
                if (to.getx()==5 && to.gety()==2){
                    bonusB = 0;
                }
                else{
		    	    bonusB += 50;
                }
				return bonuspoint + bonusB;
            case 'n':
		        if (to.getx()==0 && to.gety()==3){
                    bonusn = 0;
                }
				else{
					bonusn -= 50;
				}
				return bonuspoint + bonusn;
		    case 'N':
		        if (to.getx()==5 && to.gety()==1){
                    bonusN = 0;
                }
                else{
		    	    bonusN += 50;
                }
				return bonuspoint + bonusN;
            case 'r':
		        if (to.getx()==0 && to.gety()==4){
                    bonusr = 0;
                }
                else{
		    	    bonusr -= 50;
                }
				return bonuspoint + bonusr;
		    case 'R':
		        if (to.getx()==5 && to.gety()==0){
                    bonusR = 0;
                }
                else{
		    	    bonusR += 50;
                }
				return bonuspoint + bonusR;
		    default:
		        return bonuspoint;
	    }
    }
}