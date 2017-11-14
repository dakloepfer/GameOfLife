package uk.ac.cam.dak48.oop.tick5;



import java.io.*;
import java.net.*;
import java.util.*;



public class PatternStore {

	private List<Pattern> mPatterns = new LinkedList<>(); //List of all the patterns
	private Map<String,List<Pattern>> mMapAuths = new HashMap<>(); //maps Author name to pattern
	private Map<String,Pattern> mMapName = new HashMap<>(); //maps Patternname to Pattern
	
   public PatternStore(String source) throws IOException, NumberFormatException {
       if (source.startsWith("http://")) {
          loadFromURL(source);
       }
       else {
          loadFromDisk(source);}
    }
   
    
   public PatternStore(Reader source) throws IOException, NumberFormatException {
      load(source);
   }
    
   private void load(Reader r) throws IOException, NumberFormatException {
      //  read each line from the reader and print it to the screen    
	   BufferedReader b = new BufferedReader(r);
	   String line = b.readLine();
	   
	   while ( line != null) {
	     
		  System.out.println(line);// Print out line
  
	      Pattern p = new Pattern(line);
	      
	      if (mMapName.get(p.getName()) == null){ //If pattern doesn't exist yet; otherwise do nothing 
		      mPatterns.add(p);
		      
		      if(mMapAuths.get(p.getAuthor()) == null){ //if Author doesn't exist in Map yet
		    	  List<Pattern> l = new LinkedList<Pattern>();
		    	  l.add(p);
		    	  mMapAuths.put(p.getAuthor(), l);
		      }
		      else{
		    	  mMapAuths.get(p.getAuthor()).add(p); //add p to the linked list with key Author
		      }
		      
		      mMapName.put(p.getName(), p);
		      
	      }
	      line=b.readLine();

	   } 
	  
   }
    
    
   private void loadFromURL(String url) throws IOException, NumberFormatException {
    //  Create a Reader for the URL and then call load on it
	   URL destination = new URL(url);
	   URLConnection conn = destination.openConnection();
	   Reader r = new java.io.InputStreamReader(conn.getInputStream()); 
	   load(r);
   }

   private void loadFromDisk(String filename) throws IOException, NumberFormatException {
    // Create a Reader for the file and then call load on it
	 // File f = new File(filename); // => not necessary as FileReader also takes a String as argument
	   Reader r = new java.io.FileReader(filename);
	   load(r);
   }

   
  
   public List<Pattern> getPatternsNameSorted() {
	   // Get a list of all patterns sorted by name
	   List<Pattern> copy = new LinkedList<Pattern>(mPatterns);
	   Collections.sort(copy);   //leaves mPatterns unchanged
		return copy;
	}

	public List<Pattern> getPatternsAuthorSorted() {
	   // TODO: Get a list of all patterns sorted by author then name
		
		List<Pattern> copy = new LinkedList<Pattern>(mPatterns);
		
		Collections.sort(copy, new Comparator<Pattern>() {
			   public int compare(Pattern p1, Pattern p2) {
				   if ((p1.getAuthor()).compareTo(p2.getAuthor()) == 0){ //if Authors are equal compare by Name
					   return (p1.getName()).compareToIgnoreCase(p2.getName());
				   }
				   else{
					   return (p1.getAuthor()).compareToIgnoreCase(p2.getAuthor()); //if not equal compare by Author
				   }
			   }
				   
			 });
		return copy;

	}

	public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
	   // return a list of patterns from a particular author sorted by name
		   List<Pattern> copy = new LinkedList<Pattern>(mMapAuths.get(author));
		   Collections.sort(copy);
		   return copy;
		
	}

	public Pattern getPatternByName(String name) throws PatternNotFound {
	   // Get a particular pattern by name
		if (mMapName.containsKey(name)){
		
			Pattern copy = mMapName.get(name); //this returns the reference to the pattern but the way I understood it that's ok
			return copy;
		}
		else{
			throw new PatternNotFound("The pattern you asked for doesn't exist");
		}
		
	}

	public List<String> getPatternAuthors() {
	   // Get a sorted list of all pattern authors in the store
		Set<String> Authset = mMapAuths.keySet();
		List<String> Authlist = new LinkedList<String>(Authset);
		 Collections.sort(Authlist); //sort Authors
		return Authlist;
	}

	public List<String> getPatternNames() {
	   // Get a list of all pattern names in the store,
	   // sorted by name
		Set<String> Nameset = mMapName.keySet();
		List<String> Namelist = new LinkedList<String>(Nameset);
		 Collections.sort(Namelist); //sort Names
		return Namelist;
	}
	
	
   
   public static void main(String args[]) {
      try {
		PatternStore p =
		   new PatternStore("/Users/DominikKloepfer/Documents/Cambridge/Computer Science/ticks/PatternStore.txt");
		System.out.println("Done!");
	} catch (IOException e) {
		// Auto-generated catch block
		e.printStackTrace();
	
	} catch (NumberFormatException e) {
		// Auto-generated catch block
		e.printStackTrace();
	
	} 
      
   }
}