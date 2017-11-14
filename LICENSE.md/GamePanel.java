package uk.ac.cam.dak48.oop.tick5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel { //has built in GamePanel() constructor
    
    private World mWorld = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
  
        //mWorld = new ArrayWorld(new Pattern("a:b:20:20:1:1:111 1 111"));
       // mWorld = null;
        
        if (mWorld == null){
        	//do nothing except the white background
        	//Except tell them to select a world
        	g.setColor(Color.BLACK);
        	g.drawString("Please select a World to simulate", this.getWidth()/2-100, this.getHeight()/2);
        	
        }
        
        else{
        	g.setColor(Color.BLACK);
        	g.drawString("Generation: "+ mWorld.getGenerationCount(), 35, 35);
        	
        	int w = mWorld.getWidth();
        	int h = mWorld.getHeight();
        	
        	int sidelength = Math.min((this.getWidth()-100)/w, (this.getHeight()-100)/h); //calculates sidelength of each square so it fits
        	
        	
        	
        	g.setColor(Color.BLACK);
        	for(int r = 0; r<h; r++){ //loop through cells of mWorld and paint black rectangles accordingly
        		for(int c = 0; c<w; c++){
        			if (mWorld.getCell(r, c) == true){
        				g.fillRect(50+c*sidelength, 50+r*sidelength, sidelength, sidelength);
        			}
        		}
        	}
        	
        	g.setColor(Color.LIGHT_GRAY);
        	for (int i = 0; i<=w; i++){ //paint light grey column lines
        		g.drawLine(50+sidelength*i, 50, 50+sidelength*i, 50+sidelength*h);
        	}
        	for (int i = 0; i<=h; i++){ //paint light grey row lines
        		g.drawLine(50, 50+sidelength*i, 50+sidelength*w, 50+sidelength*i);
        	}
        	
        	
        	//watches out if window is resized to maybe resize the gamePanel
   	      	addComponentListener(new ComponentAdapter() {
 	            @Override
 	            public void componentResized(ComponentEvent e) {
 	                resizePreview();
 	            }
 	        });
        	
        }
    }

    private void resizePreview() { //method needed to handle resizing
        int w = this.getWidth();
        int h = this.getHeight();
        
        double panelratio = w/h;
        double worldratio = mWorld.getWidth()/mWorld.getHeight();
        
        if (panelratio > worldratio){ //panel is too wide, world is too tall
        	setPreferredSize(new Dimension((int) Math.floor(worldratio * h) , h )); //set worldheight to panelheight, calculate width
        	revalidate();
        }
        else{ //panel is too narrow, world is too short
        	setPreferredSize(new Dimension(w , (int) Math.floor(w/worldratio) )); //set worldwidth to panelwidth, calculate height
        	revalidate();
        }
        
    }
    
    public void display(World w) {
        mWorld = w;
        repaint();
    }
}
