package uk.ac.cam.dak48.oop.tick5;

import java.util.Arrays;


public class ArrayWorld extends World {

	private boolean[][] mWorld;
	public final boolean[] mDeadRow; //couldn't this be final, because it isnt changed once we initialised the width of the world
	
	public ArrayWorld(Pattern pat)  {
		
	    super(pat);
	    mWorld = new boolean[getHeight()][getWidth()];
	    mDeadRow = new boolean[getWidth()];

	    getPattern().initialise(this);
	    
	    boolean livecell = false;
	    //have all dead rows point to mDeadRow
	    for (int r = 0; r<getHeight(); r++){
	    	for (int c = 0; c<getWidth(); c++){
	    		
	    		if (this.getCell(r, c) == true){
	    		
	    			livecell = true;
	    			break;
	    		}

	    	}
	    	
	    	if(livecell == false){//if all cells in row are dead
	    		this.mWorld[r] = mDeadRow; //have this point to mDeadRow
	    	}
	    	livecell = false; //reset counter 
	    }
	    
	   
	    
	}
	
	public ArrayWorld(ArrayWorld aw){ //copy constructor
		 
		 
		
		super(aw);
		mWorld = new boolean [aw.getHeight()][aw.getWidth()];
	    mDeadRow = aw.mDeadRow; //makes the new Deadrow point to original one
	   
		for (int r = 0; r<aw.getHeight(); r++){
			if (aw.getRow(r) == mDeadRow) mWorld[r] = mDeadRow; //if Row is pointing to deadrow 
			else{ //if there is some live cell 
				for(int c = 0; c < aw.getWidth(); c++){
					mWorld[r][c] = aw.getCell(r, c);
				}
			}
			
		}
		
	}
	
	public ArrayWorld(String s){
		super(s);
	    mWorld = new boolean[getHeight()][getWidth()];
	    mDeadRow = new boolean[getWidth()];
	  
	 	    
	    
	    System.out.println(mDeadRow);

	    getPattern().initialise(this);
	    
	    boolean livecell = false;
	    //have all dead rows point to mDeadRow
	    for (int r = 0; r<getHeight(); r++){
	    	for (int c = 0; c<getWidth(); c++){
	    		
	    		if (this.getCell(r, c) == true){
	    		
	    			livecell = true;
	    			break;
	    		}

	    	}
	    	
	    	if(livecell == false){//if all cells in row are dead
	    		this.mWorld[r] = mDeadRow; //have this point to mDeadRow
	    	}
	    	livecell = false; //reset counter 
	    }
	    
	    
	}

	@Override
	public boolean getCell(int r, int c) {
		if (r < 0 || r > getHeight()-1) return false;
		   if (c < 0 || c > getWidth()-1) return false;

		   return mWorld[r][c];
	}

	@Override
	public boolean[] getRow(int r){ //need this to efficiently do the copy constructor
		if (r<0 || r> getHeight()-1) return new boolean[getWidth()]; //returns default => false row
		
		return mWorld[r];
	}
	
	
	@Override
	public void setCell(int r, int c, boolean val) {
		if (mWorld[r] != mDeadRow) mWorld[r][c] = val;
		else {
			mWorld[r] = new boolean[getWidth()];
			mWorld[r][c] = val;
		}
	}

	@Override
	public void nextGenerationImpl() {
		 boolean[][] nextGeneration = new boolean[getHeight()][];
	        
	        for (int r = 0; r < getHeight(); ++r) {
	          
	        	nextGeneration[r] = new boolean[getWidth()];
	            
	        	for (int c = 0; c < getWidth(); ++c) {
	                boolean nextCell = computeCell(r, c);
	                nextGeneration[r][c]=nextCell;
	            }
	        
	        }
	        mWorld = nextGeneration; //update mWorld
	     
	        //have all dead rows point to mDeadRow; because I create a new World for a short time this is necessary
		    for (int r = 0; r<getHeight(); r++){
		    	int counter = 1; //counts whether I went through all columns without finding a live cell
		    	for (int c = 0; c<getWidth(); c++){
		    		
		    		if (getCell(r, c) == true){
		    			break;
		    		}
		    		counter++;
		    	}
		    	
		    	if(counter == getWidth()){//if all cells in row are dead
		    		mWorld[r] = mDeadRow; //have this point to mDeadRow
		    	}
		    	counter = 1; //reset counter 
		    }
	
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		ArrayWorld copied = (ArrayWorld) super.clone();
		
		//I can't clone the mWorld array b/c the copied mWorld is private
	    copied.mWorld = this.mWorld.clone();
		for (int i = 0; i<getPattern().getHeight(); i++){ 
            copied.mWorld[i] = copied.mWorld[i].clone();
		}
		
		return copied;
	}
	
}






