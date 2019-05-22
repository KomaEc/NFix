package corg.vfix.parser.jimple;

import soot.dexpler.typing.UntypedConstant;
import soot.jimple.ArithmeticConstant;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MethodHandle;
import soot.jimple.NullConstant;
import soot.jimple.NumericConstant;
import soot.jimple.RealConstant;
import soot.jimple.StringConstant;

public class ConstantParser {
   public static void main(Constant constant) {
      if (constant instanceof ClassConstant) {
         parseClassConstant((ClassConstant)constant);
      } else if (constant instanceof MethodHandle) {
         parseMethodHandle((MethodHandle)constant);
      } else if (constant instanceof NullConstant) {
         parseNullConstant((NullConstant)constant);
      } else if (constant instanceof NumericConstant) {
         parseNumericConstant((NumericConstant)constant);
      } else if (constant instanceof StringConstant) {
         parseStringConstant((StringConstant)constant);
      } else if (constant instanceof UntypedConstant) {
         parseUntypedConstant((UntypedConstant)constant);
      }

   }

   private static void parseNumericConstant(NumericConstant constant) {
      if (constant instanceof ArithmeticConstant) {
         parseArithmeticConstant((ArithmeticConstant)constant);
      } else if (constant instanceof RealConstant) {
         parseRealConstant((RealConstant)constant);
      }

   }

   private static void parseRealConstant(RealConstant constant) {
      if (constant instanceof DoubleConstant) {
         parseDoubleConstant((DoubleConstant)constant);
      } else if (constant instanceof FloatConstant) {
         parseFloatConstant((FloatConstant)constant);
      }

   }

   private static void parseFloatConstant(FloatConstant constant) {
      System.out.println("FloatConstant:");
      System.out.println(constant);
   }

   private static void parseDoubleConstant(DoubleConstant constant) {
      System.out.println("DoubleConstant:");
      System.out.println(constant);
   }

   private static void parseArithmeticConstant(ArithmeticConstant constant) {
      if (constant instanceof IntConstant) {
         parseIntConstant((IntConstant)constant);
      } else if (constant instanceof LongConstant) {
         parseLongConstant((LongConstant)constant);
      }

   }

   private static void parseLongConstant(LongConstant constant) {
      System.out.println("LongConstant:");
      System.out.println(constant);
   }

   private static void parseIntConstant(IntConstant constant) {
      System.out.println("IntConstant:");
      System.out.println(constant);
   }

   private static void parseStringConstant(StringConstant constant) {
      System.out.println("StringConstant:");
      System.out.println(constant);
   }

   private static void parseUntypedConstant(UntypedConstant constant) {
      System.out.println("UntypedConstant:");
      System.out.println(constant);
   }

   private static void parseNullConstant(NullConstant constant) {
      System.out.println("NullConstant:");
      System.out.println(constant);
   }

   private static void parseMethodHandle(MethodHandle constant) {
      System.out.println("MethodHandle:");
      System.out.println(constant);
   }

   private static void parseClassConstant(ClassConstant constant) {
      System.out.println("ClassConstant:");
      System.out.println(constant);
   }

   public static void main(String[] args) {
      printIf("Double");
      printElseIf("Float");
      printElseIf("Method");
      printElseIf("Null");
      printElseIf("Numeric");
      printElseIf("String");
      printElseIf("Untyped");
   }

   private static void printIf(String str) {
      System.out.println("if(constant instanceof " + str + "Constant)");
      System.out.println("\tparse" + str + "Constant((" + str + "Constant)constant);");
   }

   private static void printElseIf(String str) {
      System.out.println("else if(constant instanceof " + str + "Constant)");
      System.out.println("\tparse" + str + "Constant((" + str + "Constant)constant);");
   }
}
