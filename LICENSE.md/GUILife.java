package uk.ac.cam.dak48.oop.tick5;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUILife extends JFrame implements ListSelectionListener {
    
	private World mWorld;
	private PatternStore mStore;
	private List<World> mCachedWorlds = new ArrayList<World>();

	private JPanel mGamePanel;
	
	private JButton mPlayButton;
	private java.util.Timer mTimer;
	private boolean mPlaying;

	
	public GUILife(PatternStore ps) {
	    super("Game of Life");
	    mStore=ps;
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1024,768);
	    
	    add(createPatternsPanel(),BorderLayout.WEST);
	    add(createControlPanel(),BorderLayout.SOUTH);
	    add(createGamePanel(),BorderLayout.CENTER);
	    
	    mPlaying = false;

	}

	private void addBorder(JComponent component, String title) {
	    Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	    Border tb = BorderFactory.createTitledBorder(etch,title);
	    component.setBorder(tb);
	}

	private JPanel createGamePanel() {
	    mGamePanel = new GamePanel();
	    addBorder(mGamePanel,"Game Panel");
	    return mGamePanel;
	}

	private JPanel createPatternsPanel() {
	    JPanel patt = new JPanel(new GridLayout(1,1));
	    addBorder(patt,"Patterns");
	   
	    List<Pattern> storedPatterns = mStore.getPatternsNameSorted(); //get all Patterns sorted in a list
	    Pattern[] stoPatts = (Pattern[]) storedPatterns.toArray(new Pattern[storedPatterns.size()]); //turn that into an array JList can use
	    
	    JList plist = new JList(stoPatts);
	    JScrollPane patpane = new JScrollPane(plist);
	    
	    
	    
	    patt.add(patpane);
 
	    plist.addListSelectionListener(this);
	    
	    return patt; 
	}

	private JPanel createControlPanel() {
	    JPanel ctrl =  new JPanel();
	   
	    addBorder(ctrl,"Controls");
	    
	    JButton b = new JButton("<< Back");
	    JButton f = new JButton("Forward >>");  
	    mPlayButton = new JButton("Play");
	    
	    b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (mPlaying) { //if the simulation is currently running
        	        mTimer.cancel(); //stop simulation
        	        mPlaying=false;
        	        mPlayButton.setText("Play");
        	    }
            	
            	moveBack();
                
            }
        });
	    
	    f.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	if (mPlaying) { //if the simulation is currently running
            	        mTimer.cancel(); //stop simulation
            	        mPlaying=false;
            	        mPlayButton.setText("Play");
            	    }
                	moveForward();
	               
				} catch (CloneNotSupportedException e1) {
					System.out.println("Something went wrong when trying to copy the world into the cache!");
				}
            }
        });
	    
	    mPlayButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e){
	    		
	    		runOrPause();
		
	    	}
	    });
	    
	    ctrl.setLayout(new GridLayout(1, 3, 0, 0));
	    ctrl.add(b);
	    ctrl.add(mPlayButton);
	    ctrl.add(f);
	    
	    return ctrl;
	}
	
	
	
    private World copyWorld(boolean useCloning) throws /*InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,*/ CloneNotSupportedException {
 	   if (useCloning == false){
 		 
 		 //ugly version
 		   if (mWorld instanceof ArrayWorld){
 			   
 			   return new ArrayWorld((ArrayWorld) mWorld);
 		   }
 		   else if (mWorld instanceof PackedWorld){
 			  return new PackedWorld((PackedWorld) mWorld);
 		   }
 		   
 		  /* //slightly less ugly version
 		   World [] parameters = {mWorld};
 		   Constructor test = mWorld.getClass().getConstructors()[1]; //hinges on always putting the copy constructor second
 		   World copied = (World) test.newInstance(parameters); //I cast this as World but due to dynamic polymorphism it should be ok
 		   return copied; */
 	   }
 	    //useCloning == true
 		   return (World) mWorld.clone();
 	   
 	}
    
    private void moveBack(){
    	if(mWorld == null) ((GamePanel) mGamePanel).display(mWorld);
    	
    	else if (mWorld.getGenerationCount() == 0) ((GamePanel) mGamePanel).display(mWorld); //If I already am at Generation 0, I need to only again display the current world
    	
    	else if (mWorld.getGenerationCount() > 0){ //I am at Generation 1 or higher
    	
    		mWorld = mCachedWorlds.get(mWorld.getGenerationCount() - 1);
    		((GamePanel) mGamePanel).display(mWorld);
    	}
    }
    
    private void moveForward() throws CloneNotSupportedException{
    	if (mWorld==null) ((GamePanel) mGamePanel).display(mWorld);
        
    	else if (mWorld.getGenerationCount() == mCachedWorlds.size()-1){ //If I don't have the next generation in the cache
                    
                    
                    mCachedWorlds.add(copyWorld(true)); //add a new world to the end of the cache; this is a new object that gets put into the cache
                    mWorld = mCachedWorlds.get(mCachedWorlds.size()-1); //move the pointer of mWorld so it points to the new element
                    mWorld.nextGeneration();   //update the new element (and mWorld)                         
                  //this is necessary to ensure everything gets updated correctly
                    ((GamePanel) mGamePanel).display(mWorld);            }
            
        else if (mWorld.getGenerationCount() <mCachedWorlds.size()-1){ //I have the next generation in the cache
            	mWorld = mCachedWorlds.get(mWorld.getGenerationCount() + 1);
            	
            	((GamePanel) mGamePanel).display(mWorld);            }
    }
    
  

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (mPlaying) { //if the simulation is currently running
	        mTimer.cancel(); //stop simulation
	        mPlaying=false;
	        mPlayButton.setText("Play");
	    }
		 
		JList<Pattern> list = (JList<Pattern>) e.getSource();
        Pattern p = list.getSelectedValue();

        // Based on size, create either a PackedWorld or ArrayWorld
        // from p. Clear the cache, set mWorld and put it into
        // the now-empty cache. Tell the game panel to display
        // the new mWorld. 
        if(p.getWidth()*p.getHeight()<=64){
        	mWorld = new PackedWorld(p);	
        }
        else {
        	mWorld = new ArrayWorld(p);
        }
        mCachedWorlds = new ArrayList<World>(); //clear cache
    	try {
			mCachedWorlds.add(copyWorld(true));//add the newly created thing to the cache
		} catch (CloneNotSupportedException e1) {
			System.out.println("Something went wrong when copying the new World into the cache!");
		} 
    	((GamePanel) mGamePanel).display(mWorld);

	}

	private void runOrPause() {
	    if (mPlaying) { //if the simulation is currently running
	        mTimer.cancel(); //stop simulation
	        mPlaying=false;
	        mPlayButton.setText("Play");
	    }
	    else {
	        mPlaying=true;
	        mPlayButton.setText("Stop");
	        mTimer = new java.util.Timer(true);
	        mTimer.scheduleAtFixedRate(new java.util.TimerTask() { //this bit moves forward every now and then
	            @Override
	            public void run() {
	                try {
						moveForward();
					} catch (CloneNotSupportedException e) {
						System.out.println("Something went wrong when trying to copy the world into the cache!");
					}
	            }
	        }, 0, 200); //the last number controls the time each generation is visible
	    }
	}

	public static void main(String[] args) {
		try {
			
			//other sources: "http://www.cl.cam.ac.uk/teaching/1617/OOProg/ticks/lifetest.txt"
			PatternStore ps = new PatternStore("/Users/DominikKloepfer/Documents/Cambridge/Computer Science/ticks/PatternStore.txt");
			GUILife gui = new GUILife(ps);
	        gui.setVisible(true);
		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	

	}

}

