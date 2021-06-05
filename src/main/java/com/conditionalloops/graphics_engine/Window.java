package com.conditionalloops.graphics_engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
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
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
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

import com.conditionalloops.graphics_engine.scene.LevelEditorScene;
import com.conditionalloops.graphics_engine.scene.LevelScene;
import com.conditionalloops.graphics_engine.scene.Scene;
import com.conditionalloops.graphics_engine.scene.SceneType;
import com.conditionalloops.graphics_engine.time.Time;
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

  private static Window instance;
  private final int width;
  private final int height;

  private final String title;

  private long glfwWindow;

  public float r, g, b, a;


  private static Scene currentScene;

  public static void changeScene(int newSceneId){

    switch (newSceneId){

      case SceneType.SceneTypeId.LevelEditorSceneTypeId:
        currentScene = new LevelEditorScene();
        break;
      case  SceneType.SceneTypeId.LevelSceneTypeId:
        currentScene = new LevelScene();
        break;
      default:
        Logger.error("Unknown scene id : {}", newSceneId );
        assert false : "Unknown scene id : " + newSceneId;
    }

  }

  private Window() {
    this.width = 1920;
    this.height = 1080;
    this.title = "Conditional Loops Game Engine";
    this.r = 1;
    this.g = 1;
    this.b = 1;
    this.a = 1;
  }

  /**
   * @return
   */
  public static Window get() {

    if (Objects.isNull(instance)) {
      synchronized (Lock) {
        if (Objects.isNull(instance)) {
          instance = new Window();
        }
      }
    }
    return instance;
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

    glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
    glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
    glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallBack);

    glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
    glfwMakeContextCurrent(this.glfwWindow);
    glfwSwapInterval(1);

    glfwShowWindow(glfwWindow);

    GL.createCapabilities();

    Window.changeScene(SceneType.SceneTypeId.LevelEditorSceneTypeId);

  }

  private void loop() {
    Logger.debug("attempting to start LWJGL event loop.");
    float beginTime = Time.getTime();
    float dTime = 0;
    while (!glfwWindowShouldClose(this.glfwWindow)) {
      glfwPollEvents();

      glClearColor(this.r, this.g, this.b, this.a);
      glClear(GL_COLOR_BUFFER_BIT);

      if(dTime > 0) {
        currentScene.update(dTime);
      }

      glfwSwapBuffers(glfwWindow);

      final float endTime = Time.getTime();
      dTime = endTime - beginTime;
      beginTime = endTime;
    }
  }

}
