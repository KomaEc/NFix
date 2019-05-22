package corg.vfix.parser.jimple;

import soot.Immediate;
import soot.Local;
import soot.jimple.Constant;

public class ImmediateParser {
   public static void main(Immediate immediate) {
      if (immediate instanceof Local) {
         LocalParser.main((Local)immediate);
      } else if (immediate instanceof Constant) {
         ConstantParser.main((Constant)immediate);
      }

   }
}
