package corg.vfix.parser.jimple;

import soot.jimple.ArrayRef;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ConcreteRef;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.ThisRef;

public class RefParser {
   public static void main(Ref ref) {
      if (ref instanceof ConcreteRef) {
         parseConcreteRef((ConcreteRef)ref);
      } else if (ref instanceof IdentityRef) {
         parseIdentityRef((IdentityRef)ref);
      }

   }

   private static void parseConcreteRef(ConcreteRef ref) {
      if (ref instanceof ArrayRef) {
         parseArrayRef((ArrayRef)ref);
      } else if (ref instanceof FieldRef) {
         parseFieldRef((FieldRef)ref);
      }

   }

   private static void parseFieldRef(FieldRef ref) {
      if (ref instanceof StaticFieldRef) {
         parseStaticFieldRef((StaticFieldRef)ref);
      } else if (ref instanceof InstanceFieldRef) {
         parseInstanceFieldRef((InstanceFieldRef)ref);
      }

   }

   private static void parseInstanceFieldRef(InstanceFieldRef ref) {
      System.out.println("InstanceFieldRef:");
      System.out.println(ref);
      System.out.println("Base:");
      ValueParser.main(ref.getBase());
      System.out.println("Field:");
      FieldParser.main(ref.getField());
      System.out.println("FieldRef:");
      FieldParser.main(ref.getFieldRef());
   }

   private static void parseStaticFieldRef(StaticFieldRef ref) {
      System.out.println("StaticFieldRef:");
      System.out.println(ref);
      System.out.println("Field:");
      FieldParser.main(ref.getField());
      System.out.println("FieldRef:");
      FieldParser.main(ref.getFieldRef());
   }

   private static void parseArrayRef(ArrayRef ref) {
      System.out.println("ArrayRef:");
      System.out.println(ref);
   }

   private static void parseIdentityRef(IdentityRef ref) {
      if (ref instanceof ParameterRef) {
         parseParameterRef((ParameterRef)ref);
      } else if (ref instanceof ThisRef) {
         parseThisRef((ThisRef)ref);
      } else if (ref instanceof CaughtExceptionRef) {
         parseCaughtExceptionRef((CaughtExceptionRef)ref);
      }

   }

   private static void parseCaughtExceptionRef(CaughtExceptionRef ref) {
      System.out.println("CaughtExceptionRef:");
      System.out.println(ref);
   }

   private static void parseThisRef(ThisRef ref) {
      System.out.println("ThisRef:");
      System.out.println(ref);
   }

   private static void parseParameterRef(ParameterRef ref) {
      System.out.println("ParameterRef:");
      System.out.println(ref);
   }

   public static void main(String[] args) {
      printIf("Parameter");
      printElseIf("This");
      printElseIf("CaughtException");
      printElseIf("Cmpg");
   }

   private static void printIf(String str) {
      System.out.println("if(ref instanceof " + str + "Ref)");
      System.out.println("\tparse" + str + "Ref((" + str + "Ref)ref);");
   }

   private static void printElseIf(String str) {
      System.out.println("else if(ref instanceof " + str + "Ref)");
      System.out.println("\tparse" + str + "Ref((" + str + "Ref)ref);");
   }
}
