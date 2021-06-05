package com.conditionalloops.graphics_engine.scene;

public enum SceneType {
  LevelEditorScene(SceneTypeId.LevelEditorSceneTypeId);
  public static class SceneTypeId{
    public final static int LevelEditorSceneTypeId = 0;
    public final static int LevelSceneTypeId = 1;
  }
  SceneType(int sceneType) {
    this.sceneType = sceneType;
  }
  public int getSceneType(){
    return this.sceneType;
  }

  public final int sceneType;
}
