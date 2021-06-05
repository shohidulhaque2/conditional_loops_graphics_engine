package com.conditionalloops.graphics_engine.time;

public class Time {

  public static float timeStarted = -1;
  public static float getTime() {
    if(timeStarted == -1){
     timeStarted = System.nanoTime();
     return 0;
    } else {
      float deltaTime = System.nanoTime() - timeStarted;
      return toSeconds(deltaTime);
    }
  }

  private static float toSeconds(float deltaTime){
    return (float)(deltaTime * 1E-9);
  }

}
