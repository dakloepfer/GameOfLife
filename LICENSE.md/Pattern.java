package uk.ac.cam.dak48.oop.tick5;
 

public class Pattern implements Comparable<Pattern>{

	private String mName;
    private String mAuthor;
    private int mWidth;
    private int mHeight;
    private int mStartCol;
    private int mStartRow;
    private String mCells;
    
    
    public Pattern(String format) throws NumberFormatException {
		// Input is of format NAME:AUTHOR:WIDTH:HEIGHT:STARTUPPERCOL:STARTUPPERROW:CELLS
    	String[] mInputs = format.split(":");
 
    	//testing for wrong input begins
    	if (mInputs.length != 7){ //check for exception
    		System.out.println("Invalid pattern format: Incorrect number of fields in pattern (found " + mInputs.length + ", should be 7)");
    	}
    	
   	
    	boolean misdigit = true;
    	int errorplace = 0;
    	for (int i = 2; i<=5; i++){
    		char [] chars = mInputs[i].toCharArray(); 
    		for (int j = 0; j<= chars.length -1; j++){
    			
    			if (Character.isDigit(chars[j]) == false){
    				
    				misdigit = false;
    				errorplace = i+1;
    				break;
    				
    			}
    		}
    		
    	if (misdigit == false){
    		System.out.println("Invalid pattern format: input number " + errorplace + " is not a valid number");
    	}
    //testing for wrong input ends
    		
    	}
    	
    	mName = mInputs[0];
    	mAuthor = mInputs[1];
    	mWidth = Integer.parseInt(mInputs[2]);
    	mHeight = Integer.parseInt(mInputs[3]);
    	mStartCol = Integer.parseInt(mInputs[4]);
    	mStartRow = Integer.parseInt(mInputs[5]);
    	mCells = mInputs[6];

    }
    
    // public 'get' methods for ALL of the fields above;
    public String getName() {
       return mName;
    }
    public String getAuthor() {
    	return mAuthor;
    }
    public int getWidth() {
    	return mWidth;
    }
    public int getHeight() {
    	return mHeight;
    }
    public int getStartCol() {
    	return mStartCol;
    }
    public int getStartRow() {
    	return mStartRow;
    }
    public String getCells() {
    	return mCells;
    }
    

    public void initialise(World world)  {
    	//	 update the values in world as expressed by the contents of the field 'mCells'.

    	String[] relCells = mCells.split(" ");
    	
    	for (int r = mStartRow; r<= relCells.length + mStartRow-1; r++){ //loop through relevant rows
    		for (int c = mStartCol; c <= relCells[r-mStartCol].toCharArray().length + mStartCol-1; c++){ //loop through relevant cols
    			
    			if (relCells[r-mStartRow].toCharArray()[c-mStartCol] == '1'){
    				world.setCell(r, c, true);
    			}
    			
    		}
    	}

    	
    }
	
    @Override
    public int compareTo(Pattern o) {
    	return mName.compareToIgnoreCase(o.getName());
    }
    
    @Override
    public String toString(){
    	return (mName + " " + "(" + mAuthor + ")");
    }
    
    
    public static void main (String[] args) throws PatternFormatException{
    	
    	try {
    	Pattern p = new Pattern(args[0]); //eg new Pattern("Glider:Richard Guy:20:20:1:1:010 001 111");
    	
    	System.out.println("Name: " + p.mName);
    	System.out.println("Author: " + p.mAuthor);
    	System.out.println("Width: " + p.mWidth);
    	System.out.println("Height: " + p.mHeight);
    	System.out.println("StartCol: " + p.mStartCol);
    	System.out.println("StartRow: " + p.mStartRow);
    	System.out.println("Pattern: " + p.mCells);

    	}
    	catch (NumberFormatException a) {
    		throw new PatternFormatException("Invalid Pattern Format: Could not interpret some of the input as numbers.");
    	}
    	
    	
    	

    	
    	
    }
}
