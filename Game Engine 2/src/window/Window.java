package window;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;

public class Window {

	public int width;
	public int height;

	public int vSync;

	public GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

	public long id;

	public Window window;

	public GLFWKeyCallback input;

	public GLFWMouseButtonCallback minput;

	public GLFWFramebufferSizeCallback resizer;

	public boolean resized = false;

	public Window() {

	}

	public void initialize(int width, int height, int resizable, int vSync) {

		this.width = width;
		this.height = height;

		this.vSync = vSync;

		glfwSetErrorCallback(errorCallback);
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, resizable);

		id = glfwCreateWindow(width, height, "Game", glfwGetPrimaryMonitor(), NULL);
		if (id == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		setKCBs();
		setFBCs();
		glfwMakeContextCurrent(id);
		GL.createCapabilities();
		glfwSwapInterval(vSync);
	}

	public long getID() {
		return id;
	}

	public int getVsync() {
		return vSync;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setFBCs() {
		// Setup resize callback
		glfwSetFramebufferSizeCallback(id, resizer = new GLFWFramebufferSizeCallback() {

			@Override
			public void invoke(long window, int width, int height) {
				
				Window.this.width = width;
				Window.this.height = height;
				Window.this.resized = true;
			}
		});


	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(id, keyCode) == GLFW_PRESS;
	}
	
	public int isMouseDown(int keyCode) {
		return glfwGetMouseButton(id, keyCode);
	}
	
	public void setKCBs() {

		input = new GLFWKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {

			}
		};
		GLFW.glfwSetKeyCallback(id, input);

		GLFW.glfwSetMouseButtonCallback(id, minput = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {


			}
		});

	}

}
