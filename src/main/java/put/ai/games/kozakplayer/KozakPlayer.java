/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.kozakplayer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.pentago.impl.PentagoBoard;
import put.ai.games.pentago.impl.PentagoMove;

public class KozakPlayer extends Player {

    private Random random = new Random(0xdeadbeef);
    public int info = 0;
     
    public Color opponentColor;

    public static void main(String[] args) {}

    @Override
    public String getName() {
        return "Marcin Jaskulski 136560 Pablo Pytello 136786";
    }


    @Override
    public Move nextMove(Board b0) {
    	opponentColor = getOpponent(this.getColor());
    	PentagoBoard b = (PentagoBoard)b0;
    	
    	
    	
    	Move m;
    	m = canWinNow(b);
    	if(m != null)
    		return m;
    	
    	m =canWinOpponent(b);
    	if(m != null)
    		return m;
    	
    	
//    	//Jeśli mogę wygrać to zrobię to!
//    	m = canWin(b);
//    	if(m != null)
//    		return m;
//    	
//    	m = winBetween(b, getColor(), opponentColor);
//    	if(m != null)
//    		return m;
//    	
//    	//Jeśli przeciwnik może wygrać to przeszkodzę mu!
//    	m = opponentCanWin(b);
//    	if(m != null)
//    		return m;
//    	m = winBetween(b, opponentColor, getColor());
//    	if(m != null)
//    		return m;
//    	
//    	
//    	
    	//Startegia ruchu
    	m = centralSquare(b);
    	if(m != null)
    		return m;
    	
    	
    	
    	
    	
        List<Move> moves = b.getMovesFor(this.getColor());        
        
        //Move a = moves.get(random.nextInt(moves.size()));
        //System.out.println(a);
        
        PentagoMove a = (PentagoMove) moves.get(random.nextInt(moves.size())%10);
        return a;
		
    }
    
    
    private Move canWin(PentagoBoard b) {
		int poz = 0;
		int pion = 0;
		
		for(int i=0; i<b.getSize(); i++) {
			poz=0;
			pion=0;
			
			for(int j=0; j<b.getSize(); j++) {
				if(b.getState(j, i) == getColor())
					poz++;
				else 
					poz = 0;
				if(b.getState(i, j) == getColor())
					pion++;
				else
					pion=0;
				
				if(poz == 4) {// x=j, y=i					
					// czy moge na kocu polozyc, jesli nie to na poczatku
					if(j+1!=b.getSize() && b.getState(j+1, i) == Color.EMPTY) 
						return new PentagoMove(j+1,i,diagonalRotate(j+1,i)[0],
													 diagonalRotate(j+1,i)[1],
													 diagonalRotate(j+1,i)[2],
													 diagonalRotate(j+1,i)[3],
													 getColor());
					else if(j-4>=0 && b.getState(j-4, i) == Color.EMPTY) 
						return new PentagoMove(j-4,i,diagonalRotate(j-4,i)[0],
													 diagonalRotate(j-4,i)[1],
													 diagonalRotate(j-4,i)[2],
													 diagonalRotate(j-4,i)[3],
													 getColor());
				}
				
				if(pion == 4) { // odwrócone ośki x=i, y=j
					// czy moge na dole, jeslinie to na górze
					if(j+1!=b.getSize() && b.getState(i, j+1) == Color.EMPTY) 
						return new PentagoMove(i,j+1,diagonalRotate(i,j+1)[0],
													 diagonalRotate(i,j+1)[1],
													 diagonalRotate(i,j+1)[2],
													 diagonalRotate(i,j+1)[3],
													 getColor());				
					else if(j-4>=0 && b.getState(i, j-4) == Color.EMPTY) 
						return new PentagoMove(i,j-4,diagonalRotate(i,j-4)[0],
													 diagonalRotate(i,j-4)[1],
													 diagonalRotate(i,j-4)[2],
													 diagonalRotate(i,j-4)[3],
													 getColor());
				}
					
			}
		}
		return null;
    }
    
    // Musi zaczynać i kończyć się na swój znak + jeden biały znak pomiędzy
    private Move winBetween(PentagoBoard b, Color lookingForColor, Color badColor) {
		int poz = 0;
		int pozEmpty=0;
		int pion = 0;
		int pionEmpty=0;
		
		for(int i=0; i<b.getSize(); i++) {
			poz=0;
			pozEmpty=0;
			pion=0;
			pionEmpty=0;
			
			for(int j=0; j<b.getSize(); j++) {
				if(b.getState(j, i) == lookingForColor) // zaczynajacy
					poz++;
				else if(b.getState(j, i) == Color.EMPTY && poz>0) // jesli jest pusty, ale cos juz zalapalismy
					if(pozEmpty==0)
						pozEmpty=1;
					else {
						poz=0;
						pozEmpty=0;
					}
				else if(b.getState(j, i) == badColor){ 
					poz = 0;
					pozEmpty=0;
				}
				
				if(b.getState(i, j) == lookingForColor)
					pion++;
				else if(b.getState(i, j) == Color.EMPTY && pion>0)
					if(pionEmpty==0)
						pionEmpty=1;
					else {
						pion=0;
						pionEmpty=0;
					}
				else if(b.getState(i, j) == badColor) {
					pion=0;
					pionEmpty=0;
				}
				
				//poszukiwanie wolnego miejsca
				if(poz + pozEmpty > 4) {
					for(int k=j-4; k<=j; k++) {
						if(b.getState(k, i) == Color.EMPTY)
							return new PentagoMove(k,i,diagonalRotate(k,i)[0],
													   diagonalRotate(k,i)[1],
													   diagonalRotate(k,i)[2],
													   diagonalRotate(k,i)[3],
													   getColor());
					}
				}
				if(pion + pionEmpty > 4) {
					for(int k=j-4; k<=j; k++) {
						if(b.getState(i, k) == Color.EMPTY)
							return new PentagoMove(i,k,diagonalRotate(i,k)[0],
													   diagonalRotate(i,k)[1],
													   diagonalRotate(i,k)[2],
													   diagonalRotate(i,k)[3],
													   getColor());
					}
				}
				
				
			}
		}
		return null;
    }
    
    private Move canWinNow(PentagoBoard b) {
    	List<Move> moves = b.getMovesFor(getColor());
    	for(Move m: moves) {
    		b.doMove(m);
    		if(b.getWinner(getColor()) == getColor())
    			return m;
    		else
    			b.undoMove(m);
    	}
    	return null;
    }
    
    private Move canWinOpponent(PentagoBoard b) {
    	List<Move> moves = b.getMovesFor(opponentColor);
    	for(Move m: moves) {
    		b.doMove(m);
    		if(b.getWinner(getColor()) == opponentColor) {
    			PentagoMove m1 = (PentagoMove)m;
    			return new PentagoMove(m1.getPlaceX(), m1.getPlaceY(), m1.getRotateSrcX(), 
    																   m1.getRotateSrcY(),
    																   m1.getRotateDstX(),
    																   m1.getRotateDstY(),
    																	getColor());
    		}
    		else
    			b.undoMove(m);
    	}
    	return null;
    }
    
    private Move opponentCanWin(PentagoBoard b) {
		int poz = 0;
		int pion = 0;
		
		for(int i=0; i<b.getSize(); i++) {
			poz=0;
			pion=0;
			
			for(int j=0; j<b.getSize(); j++) {
				if(b.getState(j, i) == opponentColor)
					poz++;
				else 
					poz = 0;
				if(b.getState(i, j) == opponentColor)
					pion++;
				else
					pion=0;
				
				if(poz == 4) { //dziala, ale uzaleznic jakos rotacje sensowniej!!!!									
					// czy moge ustwaic na koncu, jesli nie to  sprobuje na poczatku
					if(j+1!=b.getSize() && b.getState(j+1, i) == Color.EMPTY) 
						return new PentagoMove(j+1,i, thisSquareRotate(j+1,1)[0],
													 thisSquareRotate(j+1,1)[1],
													 thisSquareRotate(j+1,1)[2],
													 thisSquareRotate(j+1,1)[3],
													 getColor());
					else if(j-4>=0 && b.getState(j-4, i) == Color.EMPTY) 
						return new PentagoMove(j-4,i,thisSquareRotate(j-4,1)[0],
													 thisSquareRotate(j-4,1)[1],
													 thisSquareRotate(j-4,1)[2],
													 thisSquareRotate(j-4,1)[3],
													 getColor());
				}
				if(pion == 4) {
					// czy moge ustawic na dole, jesli nie to sprobuje na gorze
					if(j+1!=b.getSize() && b.getState(i, j+1) == Color.EMPTY) 
						return new PentagoMove(i,j+1,thisSquareRotate(i,j+1)[0],
													 thisSquareRotate(i,j+1)[1],
													 thisSquareRotate(i,j+1)[2],
													 thisSquareRotate(i,j+1)[3],
													 getColor());
					else if(j-4>=0 && b.getState(i, j-4) == Color.EMPTY) 
						return new PentagoMove(i,j-4,thisSquareRotate(j,j-4)[0],
													 thisSquareRotate(j,j-4)[1],
													 thisSquareRotate(j,j-4)[2],
													 thisSquareRotate(j,j-4)[3],
													 getColor());
				}	
			}
		}
		return null;
    }
    
    private int[] diagonalRotate(int x, int y) {
    	int[] rotateFrom = {-1,-1}; //x,y
		int[] roatteTo  = {-1,-1};
    	//co obrocic -> po przekatnej
		if(x<3) {
			if(y<3) { // 2 cwiartka na 4
				rotateFrom[0] = 3;
				rotateFrom[1] = 3;
				roatteTo[0] = 5;
				roatteTo[1] = 3;
			}
			else { // 3 cwiartka na 1
				rotateFrom[0] = 3;
				rotateFrom[1] = 0;
				roatteTo[0] = 5;
				roatteTo[1] = 0;
			}	
		}
		else{
			if(y<3) { // 1 cwiartka na 3
				rotateFrom[0] = 0;
				rotateFrom[1] = 3;
				roatteTo[0] = 2;
				roatteTo[1] = 3;
			}
			else { // 4 cwiartka na 2
				rotateFrom[0] = 0;
				rotateFrom[1] = 0;
				roatteTo[0] = 2;
				roatteTo[1] = 0;
			}	
		}
		int[] tab = {rotateFrom[0], rotateFrom[1], roatteTo[0], roatteTo[1]};
		return tab;
    }
    
    // obraza kwadrat, w ktorego skład wchodzi punkt
    private int[] thisSquareRotate(int x, int y) {
    	int[] rotateFrom = {-1,-1}; //x,y
		int[] roatteTo  = {-1,-1};
    	//co obrocic -> po przekatnej
		if(x<3) {
			if(y<3) { // 2 cwiartka 
				rotateFrom[0] = 0;
				rotateFrom[1] = 0;
				roatteTo[0] = 2;
				roatteTo[1] = 0;
			}
			else { // 3 cwiartka
				rotateFrom[0] = 0;
				rotateFrom[1] = 3;
				roatteTo[0] = 2;
				roatteTo[1] = 3;
			}	
		}
		else{
			if(y<3) { // 1 cwiartka
				rotateFrom[0] = 3;
				rotateFrom[1] = 0;
				roatteTo[0] = 5;
				roatteTo[1] = 0;
			}
			else { // 4 cwiartka
				rotateFrom[0] = 3;
				rotateFrom[1] = 3;
				roatteTo[0] = 5;
				roatteTo[1] = 3;
			}	
		}
		int[] tab = {rotateFrom[0], rotateFrom[1], roatteTo[0], roatteTo[1]};
		return tab;
    }
    
    private Move centralSquare(PentagoBoard b) {
    	if(b.getState(1, 1) == Color.EMPTY)
    		return new PentagoMove(1,1,0,0,2,0,getColor());
    	if(b.getState(1, 4) == Color.EMPTY)
    		return new PentagoMove(1,4,0,3,2,3,getColor());
    	if(b.getState(4, 1) == Color.EMPTY)
    		return new PentagoMove(4,1,3,0,5,0,getColor());
    	if(b.getState(4, 4) == Color.EMPTY)
    		return new PentagoMove(4,4,3,3,5,3,getColor());
    	
    	return null;
    }
    
    
}
