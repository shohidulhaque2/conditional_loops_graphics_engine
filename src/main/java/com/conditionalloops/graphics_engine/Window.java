package com.conditionalloops.graphics_engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.Objects;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Window {

  private final static org.slf4j.Logger Logger = LoggerFactory
      .getLogger(com.conditionalloops.graphics_engine.Window.class);
  private static final Object Lock = new Object();

  private static Window Window;
  private final int width;
  private final int height;

  private final String title;

  private long glfwWindow;

  private Window() {
    this.width = 1920;
    this.height = 1080;
    this.title = "Conditional Loops Game Engine";
  }

  /**
   * @return
   */
  public static Window getWindow() {

    if (Objects.isNull(Window)) {
      synchronized (Lock) {
        if (Objects.isNull(Window)) {
          Window = new Window();
        }
      }
    }
    return Window;
  }

  /**
   *
   */
  public void run() {
    Logger.debug("An attempt has been made to start LWJGL. Version running is {}",
        Version.getVersion());
    init();
    loop();
    clean();
  }

  private void clean() {
    Logger.debug("cleaning LWJGL resources.");
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(glfwWindow);
    glfwDestroyWindow(glfwWindow);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private void init() {
    GLFWErrorCallback.createPrint(System.err).set();
    if (!glfwInit()) {
      Logger.error("Unable to initialize GLFW.");
      throw new IllegalStateException("Unable to initialize GLFW.");
    }
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

    this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
    if (this.glfwWindow == NULL) {
      Logger.error("Unable to create GLFW window.");
      throw new IllegalStateException("Failed to create GLFW window.");
    }

    glfwMakeContextCurrent(this.glfwWindow);
    glfwSwapInterval(1);

    glfwShowWindow(glfwWindow);

    GL.createCapabilities();

  }

  private void loop() {
    Logger.debug("attempting to start LWJGL event loop.");
    while (!glfwWindowShouldClose(this.glfwWindow)) {
      glfwPollEvents();
      glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);
      glfwSwapBuffers(glfwWindow);
    }
  }

}
