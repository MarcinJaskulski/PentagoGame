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
    	//Jeśli mogę wygrać to zrobię to!
    	m = canWin(b);
    	if(m != null) {
    		info++; 
    		return m;
    	}
    	
    	//Jeśli przeciwnik może wygrać to przeszkodzę mu!
    	m = opponentCanWin(b);
    	if(m != null) {
    		info++; 
    		return m;
    	}
    	
    	
    	
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
					// co obrocic -> po przekatnej
					int[] diagonalRotate = diagonalRotate(j,i);
					int[] rotateFrom = {diagonalRotate[0], diagonalRotate[1]}; //x,y
					int[] roatteTo  = {diagonalRotate[2], diagonalRotate[3]}; //x,y   						
					
					// czy moge na kocu polozyc, jesli nie to na poczatku
					if(j+1!=b.getSize() && b.getState(j+1, i) == Color.EMPTY) 
						return new PentagoMove(j+1,i,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
					else if(j-4>=0 && b.getState(j-4, i) == Color.EMPTY) 
						return new PentagoMove(j-4,i,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
				}
				
				if(pion == 4) { // odwrócone ośki x=i, y=j
					// co obrocic -> po przekatnej
					int[] diagonalRotate = diagonalRotate(i,j);
					int[] rotateFrom = {diagonalRotate[0], diagonalRotate[1]}; //x,y
					int[] roatteTo  = {diagonalRotate[2], diagonalRotate[3]}; //x,y   						
					
					// czy moge na dole, jeslinie to na górze
					if(j+1!=b.getSize() && b.getState(i, j+1) == Color.EMPTY) 
						return new PentagoMove(i,j+1,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());				
					else if(j-4>=0 && b.getState(i, j-4) == Color.EMPTY) 
						return new PentagoMove(i,j-4,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
					}
					
			}
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
				
				if(poz == 4) {
					//co obrocic -> ta gdzie jest czasc zwycieskiej lini
					int[] diagonalRotate = thisSquareRotate(j,i);
					int[] rotateFrom = {diagonalRotate[0], diagonalRotate[1]}; //x,y
					int[] roatteTo  = {diagonalRotate[2], diagonalRotate[3]}; //x,y   						
					
					// czy moge ustwaic na koncu, jesli nie to  sprobuje na poczatku
					if(j+1!=b.getSize() && b.getState(j+1, i) == Color.EMPTY) 
						return new PentagoMove(j+1,i,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
					else if(j-4>=0 && b.getState(j-4, i) == Color.EMPTY) 
						return new PentagoMove(j-4,i,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
				}
				if(pion == 4) {
					//co obrocic -> ta gdzie jest czasc zwycieskiej lini
					int[] diagonalRotate = thisSquareRotate(i,j);
					int[] rotateFrom = {diagonalRotate[0], diagonalRotate[1]}; //x,y
					int[] roatteTo  = {diagonalRotate[2], diagonalRotate[3]}; //x,y   						
					
					// czy moge ustawic na dole, jesli nie to sprobuje na gorze
					if(j+1!=b.getSize() && b.getState(i, j+1) == Color.EMPTY) 
						return new PentagoMove(i,j+1,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
					else if(j-4>=0 && b.getState(i, j-4) == Color.EMPTY) 
						return new PentagoMove(i,i-4,rotateFrom[0],rotateFrom[1],roatteTo[0],roatteTo[1],getColor());
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
    
}
