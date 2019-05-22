package corg.vfix.parser.jimple;

import soot.Immediate;
import soot.Value;
import soot.jimple.Expr;
import soot.jimple.Ref;

public class ValueParser {
   public static void main(Value value) {
      if (value instanceof Expr) {
         ExprParser.main((Expr)value);
      } else if (value instanceof Immediate) {
         ImmediateParser.main((Immediate)value);
      } else if (value instanceof Ref) {
         RefParser.main((Ref)value);
      }

   }

   public static void main(String[] args) {
      printIf("Expr");
      printElseIf("Immediate");
      printElseIf("Local");
      printElseIf("Ref");
   }

   private static void printIf(String str) {
      System.out.println("if(value instanceof " + str + ")");
      System.out.println("\t" + str + "Parser.main((" + str + ") value);");
   }

   private static void printElseIf(String str) {
      System.out.println("else if(value instanceof " + str + ")");
      System.out.println("\t" + str + "Parser.main((" + str + ") value);");
   }
}
