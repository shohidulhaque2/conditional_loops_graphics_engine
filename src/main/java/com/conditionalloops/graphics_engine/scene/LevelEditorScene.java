package com.conditionalloops.graphics_engine.scene;

import static com.conditionalloops.graphics_engine.scene.SceneType.SceneTypeId.LevelEditorSceneTypeId;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import com.conditionalloops.graphics_engine.KeyListener;
import com.conditionalloops.graphics_engine.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelEditorScene extends Scene {

 Logger Logger = LoggerFactory.getLogger(LevelEditorScene.class);

  private boolean changingScene = false;
  private float timeToChangeScene = 2.0f;
  private float dTimeAccumulator = 0;


  public LevelEditorScene() {
  }

  @Override
  public void update(float dTime) {

    dTimeAccumulator += dTime;
    System.out.println(dTimeAccumulator);

    if( !changingScene && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
      Logger.debug("Space has been pressed. Changing scene.");
      changingScene = true;
    }

    if(changingScene && dTimeAccumulator > 10 && timeToChangeScene > 0){
      timeToChangeScene -= dTime;
      Window.get().r -= dTime * 5.0f;
      Window.get().g -= dTime * 5.0f;
      Window.get().b -= dTime * 5.0f;
      Window.get().a -= dTime * 5.0f;
    }

  }

}
