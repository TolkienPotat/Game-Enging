package testing;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import java.awt.Color;

import main.Launcher;
import rendering.Renderer;
import rendering.Texture;
import states.State;
import window.Window;

public class Test implements State{

	Texture t;
	
	@Override
	public void render(Renderer r) {
		System.out.println("rendering");
		
		
//		t.bind();
		r.begin();
		glBindTexture(GL_TEXTURE_2D, 1);
//		r.completeDrawTexture(0, 0, 10, 100, 350, 100, 330, 10, 0, 0, 1, 1, new Color(1, 1, 1), 1, 1);
		r.drawTextureLS(t, 10, 10, 50, 50);
		
		r.end();
		
	}

	@Override
	public void init() {

		t = Texture.loadTexture("/Files/test.png");
		
	}

	@Override
	public void exit() {
	}

	@Override
	public void input(Window window) {
		
		if (window.isKeyPressed(GLFW_KEY_ESCAPE)) Launcher.g.shouldExit = true;
		
	}

	@Override
	public void tick() {
	}

}
