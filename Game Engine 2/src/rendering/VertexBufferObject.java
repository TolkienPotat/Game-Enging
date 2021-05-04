package rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL30;

public class VertexBufferObject {

	private final int id;
	
	
	public VertexBufferObject() {
		id = GL30.glGenBuffers();
	}
	
    public void bind(int target) {
        GL30.glBindBuffer(target, id);
    }
	
    public void uploadData(int target, long size, int usage) {
        GL30.glBufferData(target, size, usage);
    }
    
    public void uploadSubData(int target, long offset, FloatBuffer data) {
        GL30.glBufferSubData(target, offset, data);
    }
    
    public void uploadData(int target, IntBuffer data, int usage) {
        GL30.glBufferData(target, data, usage);
    }
    
    public void delete() {
        GL30.glDeleteBuffers(id);
    }
    
    public int getID() {
        return id;
    }
    
}
