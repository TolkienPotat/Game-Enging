package rendering;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import matrixes.Matrix4f;

public class Renderer {

	private static VertexArray vao;
	private static VertexBufferObject vbo;
	private static ShaderProgram program;

	private static FloatBuffer vertices;
	private static int numVertices;
	private static boolean drawing;

	public Renderer() {

	}

	public void init(String fragLocation, String vertexLocation) {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		setupShaderProgram(fragLocation, vertexLocation);

	}

	public void begin() {
		if (drawing) {
			throw new IllegalStateException("Renderer is already drawing!");
		}
		drawing = true;
		numVertices = 0;
	}

	public void end() {
		if (!drawing) {
			throw new IllegalStateException("Renderer isn't drawing!");
		}
		drawing = false;
		flush();
	}

	public static void flush() {
		if (numVertices > 0) {
			vertices.flip();

			if (vao != null) {
				vao.bind();
			} else {
				vbo.bind(GL_ARRAY_BUFFER);
				specifyVertexAttributes();
			}
			program.use();

			/* Upload the new vertex data */
			vbo.bind(GL_ARRAY_BUFFER);
			vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

			/* Draw batch */
			glDrawArrays(GL_TRIANGLES, 0, numVertices);

			/* Clear vertex data for next batch */
			vertices.clear();
			numVertices = 0;
		}
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void dispose() {
		MemoryUtil.memFree(vertices);

		if (vao != null) {
			vao.delete();
		}
		vbo.delete();
		program.delete();

	}

	public void drawTexture(Texture t, float x, float y) {
		float x2 = x + t.getWidth();
		float y2 = t.getHeight();
		completeDrawTexture(x, y, x, y2, x2, y2, x2, y, 0f, 0f, 1f, 1f, new Color(1, 1, 1), -1f, -1f);
	}

	public void drawTextureC(Texture t, float x, float y, Color c) {
		
		//Draw coloured texture
		
	}
	
	public void drawTextureLS(Texture t, float x, float y, float scaleX, float scaleY) {
		
		//Draw a linearly scaled texture
		float x2 = x + t.getWidth()*scaleX;
		float y2 = t.getHeight()*scaleY;
		completeDrawTexture(x, y, x, y2, x2, y2, x2, y, 0f, 0f, 1f, 1f, new Color(1, 1, 1), -1f, -1f);
	}
	
	public void completeDrawTexture(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4,
			float tx1, float ty1, float tx2, float ty2, Color c, float xIG, float yIG) {
		
		//XY Pos for each vertex, starts bottom left, clockwise
		
		if (vertices.remaining() < 7 * 8) {
			/* We need more space in the buffer, so flush it */
			flush();
		}

		float r = c.getRed();
		float g = c.getGreen();
		float b = c.getBlue();
		float a = c.getAlpha();
		
		vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(tx1).put(ty1).put(xIG).put(yIG);
		vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(tx1).put(tx2).put(xIG).put(yIG);
		vertices.put(x3).put(y3).put(r).put(g).put(b).put(a).put(tx2).put(ty2).put(xIG).put(yIG);
		
		vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(tx1).put(ty1).put(xIG).put(yIG);
		vertices.put(x4).put(y4).put(r).put(g).put(b).put(a).put(tx2).put(ty1).put(xIG).put(yIG);
		vertices.put(x3).put(y3).put(r).put(g).put(b).put(a).put(tx2).put(ty2).put(xIG).put(yIG);
		
		numVertices += 6;
	}

	private static void setupShaderProgram(String fragLocation, String vertexLocation) {

		vao = new VertexArray();
		vao.bind();

		/* Generate Vertex Buffer Object */
		vbo = new VertexBufferObject();
		vbo.bind(GL_ARRAY_BUFFER);

		/* Create FloatBuffer */
		vertices = MemoryUtil.memAllocFloat(4096);

		/* Upload null data to allocate storage for the VBO */
		long size = vertices.capacity() * Float.BYTES;
		vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

		/* Initialize variables */
		numVertices = 0;
		drawing = false;

		/* Load shaders */
		Shader vertexShader, fragmentShader;

		vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertexLocation);
		fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragLocation);

		/* Create shader program */
		program = new ShaderProgram();
		program.attachShader(vertexShader);
		program.attachShader(fragmentShader);

		program.bindFragmentDataLocation(0, "fragColor");

		program.link();
		program.use();

		/* Delete linked shaders */
		vertexShader.delete();
		fragmentShader.delete();

		/* Get width and height of framebuffer */
		long window = GLFW.glfwGetCurrentContext();
		int width, height;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
			width = widthBuffer.get();
			height = heightBuffer.get();
		}

		/* Specify Vertex Pointers */
		specifyVertexAttributes();

		/* Set texture uniform */
		int uniTex = program.getUniformLocation("texImage");
		program.setUniform(uniTex, 0);

		/* Set model matrix to identity matrix */
		Matrix4f model = new Matrix4f();
		int uniModel = program.getUniformLocation("model");
		program.setUniform(uniModel, model);

		/* Set view matrix to identity matrix */
		Matrix4f view = new Matrix4f();
		int uniView = program.getUniformLocation("view");
		program.setUniform(uniView, view);

		/* Set projection matrix to an orthographic projection */
//		Matrix4f projection = Matrix4f.orthographic(-width / 2, width / 2, -height / 2, height / 2, -1f, 1f);
		Matrix4f projection = Matrix4f.orthographic(0, width, 0, height, -1f, 1f);
		int uniProjection = program.getUniformLocation("projection");
		program.setUniform(uniProjection, projection);

	}

	private static void specifyVertexAttributes() {
		/* Specify Vertex Pointer */
		int posAttrib = program.getAttributeLocation("position");
		program.enableVertexAttribute(posAttrib);
		program.pointVertexAttribute(posAttrib, 2, 10 * Float.BYTES, 0);

		/* Specify Color Pointer */
		int colAttrib = program.getAttributeLocation("color");
		program.enableVertexAttribute(colAttrib);
		program.pointVertexAttribute(colAttrib, 4, 10 * Float.BYTES, 2 * Float.BYTES);

		/* Specify Texture Pointer */
		int texAttrib = program.getAttributeLocation("texcoord");
		program.enableVertexAttribute(texAttrib);
		program.pointVertexAttribute(texAttrib, 2, 10 * Float.BYTES, 6 * Float.BYTES);

		int posigAttrib = program.getAttributeLocation("posInGame");
		program.enableVertexAttribute(posigAttrib);
		program.pointVertexAttribute(posigAttrib, 2, 10 * Float.BYTES, 8 * Float.BYTES);

	}

}
