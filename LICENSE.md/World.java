package uk.ac.cam.dak48.oop.tick5;

public abstract class World implements Cloneable{
	
	private int mGeneration;
	private Pattern mPattern;
	
	public World(Pattern pat) {
		
		 mGeneration = 0;
		 mPattern = pat;
		
	}
	
	public World(World w){ //copy constructor
		
		mGeneration = w.getGenerationCount();
		mPattern = w.getPattern();//as pattern is immutable, this is ok
		
	}
	
	public World(String s){
		mGeneration = 0;
		mPattern = new Pattern(s);
	}
	
	public int getWidth(){
		return mPattern.getWidth();
	}
	
	public int getHeight(){
		return mPattern.getHeight();
	}
	
	public int getGenerationCount(){
		return mGeneration;
	}
	
	protected void incrementGenerationCount(){
		mGeneration++;
	}
	
	protected Pattern getPattern(){
		return mPattern;
	}
	
	
	protected int countNeighbours(int row, int col){
		
			int n = 0; //number count of live neighbours
			for (int r = row-1; r <= row+1; r++){ //loop through rows 
				for (int c = col-1; c <= col+1; c++){ //loop through columns
				
					if (getCell(r, c) == true) {
						n++; //update n if appropriate
					}
				}
			} 		
			
			if (getCell(row, col) == true) {
			 	n--; //if we counted cell (row, col) as live, subtract it
		    }
			
			return n; 
	}

		
	protected boolean computeCell(int row, int col){
		
			// liveCell is true if the cell at position (col,row) in world is live
	   		 boolean liveCell = getCell(row, col);
	    
	   		// neighbours is the number of live neighbours to cell (col,row)
	   		 int neighbours = countNeighbours(row, col);

	   		// we will return this value at the end of the method to indicate whether 
	   		// cell (col,row) should be live in the next generation
	   		 boolean nextCell = false;
	    
	   		//A live cell with less than two neighbours dies (underpopulation)
	   		if (neighbours < 2) {
	   		   nextCell = false;
	   		}
	 
	   		//A live cell with two or three neighbours lives (a balanced population)
	   		if ((neighbours == 2)  && (liveCell == true)) { //I check for three neighbours further down
	   		   nextCell = true;
	   		}

	   		//A live cell with with more than three neighbours dies (overcrowding)
			if (neighbours > 3) {
	   		   nextCell = false;
	   		}
	   		//A dead cell with exactly three live neighbours comes alive
			if (neighbours == 3) {
	   		   nextCell = true;
	   		}    
	   		
	   		return nextCell; 
	   	}

	public abstract boolean getCell(int r, int c);
	public abstract void setCell(int r, int c, boolean val);
	protected abstract void nextGenerationImpl();
	public boolean[] getRow(int r){//only need for ArrayWorld, it is overriden there so the stub body is ok
		return null;
	}


	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
		//no deep cloning required b/c Pattern is immutable
	
	}
	
	public void nextGeneration(){
		nextGenerationImpl();
		mGeneration++;
	}
	

}
