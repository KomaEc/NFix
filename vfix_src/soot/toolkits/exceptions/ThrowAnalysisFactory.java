package soot.toolkits.exceptions;

import soot.dexpler.DalvikThrowAnalysis;
import soot.options.Options;

public class ThrowAnalysisFactory {
   public static ThrowAnalysis checkInitThrowAnalysis() {
      switch(Options.v().check_init_throw_analysis()) {
      case 1:
         if (Options.v().android_jars().equals("") && Options.v().force_android_jar().equals("")) {
            return PedanticThrowAnalysis.v();
         }

         return DalvikThrowAnalysis.v();
      case 2:
         return PedanticThrowAnalysis.v();
      case 3:
         return UnitThrowAnalysis.v();
      case 4:
         return DalvikThrowAnalysis.v();
      default:
         assert false;

         return PedanticThrowAnalysis.v();
      }
   }
}
