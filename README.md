# GameOfLife
A Java implementation of Conway's Game of Life, with a selection of preloaded patterns. 

The main method is in the GUILife class, together with a few methods to facilitate using the GUI. 

Both the PackedWorld and ArrayWorld classes achieve the same goal (storing a game world of fixed size) but the PackedWorld class uses a
restrictive storage format (longs) that limits the size. Both ArrayWorld and PackedWorld inherit from the abstract World class. It is in 
these two classes that the next generation is computed. 

The GamePanel class is needed for the GUI to run nicely.

The Pattern class bundles the information for a starting pattern up.

This program has been written as part of my Computer Science module in first year at University. 
