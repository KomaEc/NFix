package corg.vfix.parser.jimple;

import soot.SootField;
import soot.SootFieldRef;

public class FieldParser {
   public static void main(SootField field) {
      System.out.println("Name:" + field.getName());
   }

   public static void main(SootFieldRef fieldRef) {
      System.out.println("Name:" + fieldRef.name());
      System.out.println("Type:" + fieldRef.type());
   }
}
