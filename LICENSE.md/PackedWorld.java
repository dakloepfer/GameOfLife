package uk.ac.cam.dak48.oop.tick5;

public class PackedWorld extends World{

	private long mWorld;
	
	public PackedWorld(Pattern pat) {
		super(pat);
		mWorld = 0L;
		getPattern().initialise(this);
		
		//testing for bad input
		if (getHeight() * getWidth() > 64){ //test whether size of the world is too big
			System.out.println("You are trying to create a world whose size is not compatible with the storage method! Please only use 8x8 worlds");
		}
		
		String[] relCells =  getPattern().getCells().split(" ");
		int nAssignedCells = 0;
		
		for (int i = 0; i < relCells.length; i++){	 //count how many cells were assigned
			nAssignedCells = nAssignedCells + relCells[i].toCharArray().length;
		}
		
		if (nAssignedCells > getWidth()*getHeight()){ //test whether too many cells were assigned
			System.out.println("Watch out! You are assigning more cells than exist in the world you created! Please limit the number of assigned Cells to 64" );
		}
		//testing ends
		
		
	}
	
	public PackedWorld(PackedWorld pw){ //copy constructor
			
			super(pw);
			mWorld = 0L;
			if(pw.getHeight() * pw.getWidth() >64) System.out.println("Please only pass a PackedWorld to create a PackedWorld"); //sanitizing inputs
			for (int i = 0; i<pw.getHeight(); i++){ //loop through cells and set each individually; here one could in principle do it all in one go but only if one violates the principle that mWorld is private
				for(int j = 0; j < pw.getWidth(); j++){
					setCell(i, j, pw.getCell(i, j));
				}
			}
			
		}
	
	public PackedWorld(String s){//required by tick4 tester
		  
	    super(s);
		mWorld = 0L;
		getPattern().initialise(this);
		
		//testing for bad input
		if (getHeight() * getWidth() > 64){ //test whether size of the world is too big
			System.out.println("You are trying to create a world whose size is not compatible with the storage method! Please only use 8x8 worlds");
		}
		
		String[] relCells =  getPattern().getCells().split(" ");
		int nAssignedCells = 0;
		
		for (int i = 0; i < relCells.length; i++){	 //count how many cells were assigned
			nAssignedCells = nAssignedCells + relCells[i].toCharArray().length;
		}
		
		if (nAssignedCells > getWidth()*getHeight()){ //test whether too many cells were assigned
			System.out.println("Watch out! You are assigning more cells than exist in the world you created! Please limit the number of assigned Cells to 64" );
		}
		//testing ends
	}	

	@Override
	public boolean getCell(int r, int c) {
		int position = (r-1)*getWidth() + c -1 ; //translate r and c into a position index 
		
		long check = (mWorld >>> position) & 1;  
		    
	    return (check == 1);
	}

	@Override
	public void setCell(int r, int c, boolean val) {
		int position = (r-1)*getWidth() + c - 1; //translate r and c into a position index 

		if (val) {
			 long h = 1; 
			 h = h << position;
			 mWorld = mWorld | h;
			             // update the value "packed" with the bit at "position" set to 1
		}
		else {
			long h = 1; 
			h = h << position;
			mWorld = mWorld & ~h;
			// update the value "packed" with the bit a "position" set to 0 
		}			       
	}

	@Override
	public void nextGenerationImpl() {
   		long nextWorld = 0L; //I need to first build up the next world before I can update mWorld, otherwise it uses the partially updated mWorld for further calculations
   	
		for (int row = 0; row < getHeight(); row++){ //loop through rows 
			for (int col = 0; col < getWidth(); col++){ //loop through columns
				
				boolean val = computeCell(row, col); //get the supposed next value
				
				//This here is pretty much the setCell method adapted to work on nextWorld
				int position = (row-1)*getWidth() + col - 1; //translate r and c into a position index 

				if (val) {
					 long h = 1; 
					 h = h << position;
					 nextWorld = nextWorld | h;
					             // update the value "packed" with the bit at "position" set to 1
				}
				else {
					long h = 1; 
					h = h << position;
					nextWorld = nextWorld & ~h;
					// update the value "packed" with the bit a "position" set to 0 
				}
				
				}
		}	
		mWorld = nextWorld;

	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		PackedWorld copied = (PackedWorld) super.clone();
		for (int i = 0; i<getPattern().getHeight(); i++){ //loop through cells and set each individually; here one could in principle do it all in one go but only if one violates the principle that mWorld is private
			for(int j = 0; j < getPattern().getWidth(); j++){
				setCell(i, j, getCell(i, j));
			}
		}
		
		return copied;
	}

	
	
}
