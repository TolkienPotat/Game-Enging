package rendering;

import org.lwjgl.opengl.GL30;

public class VertexArray {

	private final int id;
	
	
	public VertexArray() {
		id = GL30.glGenVertexArrays();
	}
	
	public void bind() {
		GL30.glBindVertexArray(id);
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(id);
	}
	
	public int getID() {
		return id;
	}
}
