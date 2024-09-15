package chess.util;

import static chess.util.GameEvaluator.EvaluationResult.CHECK;
import static chess.util.GameEvaluator.EvaluationResult.CHECKMATE;

import static chess.util.GameEvaluator.EvaluationResult.NORMAL_GAME_SITUATION;
import static chess.util.GameEvaluator.EvaluationResult.STALEMATE;

import chess.Game;
import chess.Move;


public class GameEvaluator {

   public enum EvaluationResult{
      //используется в коде
      CHECK,
      CHECKMATE,
      STALEMATE,
      NORMAL_GAME_SITUATION,
      //Используется в самом интерфейсе
      OFFER_DRAW,
      RESIGN;

      @Override
      public String toString() {
         return name().substring(0, 1) + name().substring(1).replace('_', ' ').toLowerCase()+".";
      }
   }

   public static EvaluationResult checkmate(Game g, Move m) {
      return g.checkmate(m) ?
            CHECKMATE: NORMAL_GAME_SITUATION;
   }

   public static EvaluationResult stalemate(Game g, Move m) {
      return g.stalemate(m) ?
            STALEMATE: NORMAL_GAME_SITUATION;
   }

   public static EvaluationResult check(Game g, Move m) {
      return g.checkOnOpponent(m) ?
            CHECK: NORMAL_GAME_SITUATION;
   }



   public static EvaluationResult FinalEvaluation(Game g, Move m) {
         EvaluationResult result = checkmate(g, m);
         
         if (result == NORMAL_GAME_SITUATION) {
        	 result = check(g, m);
        	 if (result == NORMAL_GAME_SITUATION) {
        		 result = stalemate(g, m);
        		 return result;
        	 } else {
        		return result; 
        	 }
         } else {
        	 return result;
         }
   }
}