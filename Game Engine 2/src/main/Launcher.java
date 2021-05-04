package main;

import testing.Test;

public class Launcher {

	public static Game g;
	
	public static void main(String[]args) {
		
		g = new Game();
		g.initiate();
		g.addStates(new Test(), "test");
		g.changeStates("test");
		g.run();
		g.exit();
		
	}
	
	
}
