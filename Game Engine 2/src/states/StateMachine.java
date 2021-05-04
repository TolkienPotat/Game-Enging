package states;

import java.util.HashMap;
import java.util.Map;

import rendering.Renderer;
import window.Window;

public class StateMachine implements State{

	private Map<String, State> states;
	
	private State currentState;
	
	
	public StateMachine() {
		
	states = new HashMap<>();
	currentState = new EmptyState();
	states.put(null, currentState);
		
	}
	
	public void add(String name, State state) {
		states.put(name, state);
	}
	
	public void change(String name) {
		currentState.exit();
		currentState = states.get(name);
		currentState.init();
	}

	
	@Override
	public void tick() {
		currentState.tick();
	}

	@Override
	public void render(Renderer r) {
		currentState.render(r);
	}

	@Override
	public void init() {
		currentState.init();
	}

	@Override
	public void exit() {
		currentState.exit();
	}

	@Override
	public void input(Window window) {
		currentState.input(window);
	}

	

	
	
}
