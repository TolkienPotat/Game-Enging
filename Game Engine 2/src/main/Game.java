package main;

import rendering.Renderer;
import states.State;
import states.StateMachine;
import window.Window;

import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

	public StateMachine s;

	public Renderer defaultRenderer;

	public Window mainWindow;

	

	public int rightMouse;

	public int leftMouse;

	public int vSync = 1;
	
	public boolean shouldExit = false;

	public Game() {

	}

	public void initiate() {

		s = new StateMachine();
		
		mainWindow = new Window();
		mainWindow.initialize(1920, 1080, 1, vSync);

		defaultRenderer = new Renderer();
		defaultRenderer.init("/Files/fragshad.frag", "/Files/vertexshader.vert");

		
		

	}

	public void run() {
		
		tick();

		double secsPerUpdate = 1000000000 / 60.0d;
		double previous = System.nanoTime();
		double steps = 0.0;
		
		while (!shouldExit && !glfwWindowShouldClose(mainWindow.getID())) {
			
			
			double loopStartTime = System.nanoTime();
			double elapsed = loopStartTime - previous;
			previous = loopStartTime;
			steps += elapsed;
			
			input();
			
			if (steps >= secsPerUpdate) {

				tick();
				steps -= secsPerUpdate;
				
			}
			
			render();
			

			glfwSwapBuffers(mainWindow.getID());
			glfwPollEvents();
			
			if (mainWindow.resized) {
	            GL30.glViewport(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
	            mainWindow.resized = false;
	        }
			if (vSync == 0) {
				sync(loopStartTime);
			}
			

		}
		

	}

	public void input() {
		s.input(mainWindow);
	}

	public void tick() {
//		System.out.println(keyMap.get(GLFW_KEY_ESCAPE));
//		System.out.println(glfwWindowShouldClose(mainWindow.getID()));
		s.tick();
	}

	public void render() {
		defaultRenderer.clear();
		s.render(defaultRenderer);
	}
	
	public void exit() {
		glfwDestroyWindow(mainWindow.getID());
		Renderer.dispose();
		mainWindow.input.free();
		mainWindow.minput.free();
		glfwTerminate();
		mainWindow.errorCallback.free();
		System.out.println("Successfully Closed Window.");
	}

	public void addStates(State state, String name) {
		s.add(name, state);
	}

	public void changeStates(String name) {
		s.change(name);
	}

	public void destroy() {

	}

	private void sync(double loopStartTime) {
		float loopSlot = 1f / 50;
		double endTime = loopStartTime + loopSlot;
		while (System.nanoTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	
}
