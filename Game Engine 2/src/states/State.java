package states;

import rendering.Renderer;
import window.Window;

public interface State {

	
	
	public void render(Renderer r);
	
	public void init();
	
	public void exit();

	public void input(Window window);

	

	

	void tick();
	
	
	
}
