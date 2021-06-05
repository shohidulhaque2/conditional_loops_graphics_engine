package com.conditionalloops.graphics_engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

  private static KeyListener instance;
  private static Object Lock = new Object();

  private boolean keyPressed [] = new boolean [350];

  private KeyListener() {
  }

  public static KeyListener get(){
    if(instance == null){
      synchronized (Lock){
        if(instance == null){
          instance = new KeyListener();
        }
      }
    }
    return instance;
  }

  public static void keyCallback(long window, int key, int scancode, int action, int modifier){
    if(action == GLFW_PRESS && key < get().keyPressed.length){
      get().keyPressed[key] = true;
    } else if(action == GLFW_RELEASE && key < get().keyPressed.length){
      get().keyPressed[key] = false;
    }
  }

  public static boolean isKeyPressed(int keyCode) {
    return keyCode < get().keyPressed.length &&
        get().keyPressed[keyCode];
  }
}
