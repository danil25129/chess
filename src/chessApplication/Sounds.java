package chessApplication;

import javafx.scene.media.AudioClip;

public class Sounds {

   private static final AudioClip MOVE = new AudioClip(Sounds.class.getResource("../resources/sounds/move.mp3").toString());
   private static final AudioClip DRAW  = new AudioClip(Sounds.class.getResource("../resources/sounds/draw.mp3").toString());
   private static final AudioClip CHECKMATE= new AudioClip(Sounds.class.getResource("../resources/sounds/checkmate.mp3").toString());
   private static final AudioClip STALEMATE = new AudioClip(Sounds.class.getResource("../resources/sounds/stalemate.mp3").toString());
   private static final AudioClip RESIGN = new AudioClip(Sounds.class.getResource("../resources/sounds/resign.mp3").toString());
   private static final AudioClip CAPTURE_MATERIAL = new AudioClip(Sounds.class.getResource("../resources/sounds/capture.mp3").toString());
   
  
   private Sounds() {}

   public static void lagWorkAround() {
      MOVE.play(0.0);
   }

   public static void move() {
      MOVE.play();
   }

   public static void draw() {
      DRAW.play();
   }

   public static void checkmate() {
      CHECKMATE.play();
   }

   public static void stalemate() {
      STALEMATE.play();
   }

   public static void capture() {
	      CAPTURE_MATERIAL.play();
   }
   
   public static void captureStop() {
	      CAPTURE_MATERIAL.stop();
   }

   public static void resign() {
      RESIGN.play();
   }
}
