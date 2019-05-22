package soot.util.backend;

import java.util.Iterator;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassWriter;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethodRef;
import soot.Type;
import soot.TypeSwitch;
import soot.VoidType;
import soot.baf.DoubleWordType;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;

public class ASMBackendUtils {
   public static String slashify(String s) {
      return s == null ? null : s.replace('.', '/');
   }

   public static String toTypeDesc(SootMethodRef m) {
      StringBuilder sb = new StringBuilder();
      sb.append('(');
      Iterator var2 = m.parameterTypes().iterator();

      while(var2.hasNext()) {
         Type t = (Type)var2.next();
         sb.append(toTypeDesc(t));
      }

      sb.append(')');
      sb.append(toTypeDesc(m.returnType()));
      return sb.toString();
   }

   public static String toTypeDesc(Type type) {
      final StringBuilder sb = new StringBuilder(1);
      type.apply(new TypeSwitch() {
         public void defaultCase(Type t) {
            throw new RuntimeException("Invalid type " + t.toString());
         }

         public void caseDoubleType(DoubleType t) {
            sb.append('D');
         }

         public void caseFloatType(FloatType t) {
            sb.append('F');
         }

         public void caseIntType(IntType t) {
            sb.append('I');
         }

         public void caseByteType(ByteType t) {
            sb.append('B');
         }

         public void caseShortType(ShortType t) {
            sb.append('S');
         }

         public void caseCharType(CharType t) {
            sb.append('C');
         }

         public void caseBooleanType(BooleanType t) {
            sb.append('Z');
         }

         public void caseLongType(LongType t) {
            sb.append('J');
         }

         public void caseArrayType(ArrayType t) {
            sb.append('[');
            t.getElementType().apply(this);
         }

         public void caseRefType(RefType t) {
            sb.append('L');
            sb.append(ASMBackendUtils.slashify(t.getClassName()));
            sb.append(';');
         }

         public void caseVoidType(VoidType t) {
            sb.append('V');
         }
      });
      return sb.toString();
   }

   public static Object getDefaultValue(SootField field) {
      if (field.hasTag("StringConstantValueTag")) {
         if (acceptsStringInitialValue(field)) {
            return ((StringConstantValueTag)field.getTag("StringConstantValueTag")).getStringValue();
         }
      } else {
         if (field.hasTag("IntegerConstantValueTag")) {
            return ((IntegerConstantValueTag)field.getTag("IntegerConstantValueTag")).getIntValue();
         }

         if (field.hasTag("LongConstantValueTag")) {
            return ((LongConstantValueTag)field.getTag("LongConstantValueTag")).getLongValue();
         }

         if (field.hasTag("FloatConstantValueTag")) {
            return ((FloatConstantValueTag)field.getTag("FloatConstantValueTag")).getFloatValue();
         }

         if (field.hasTag("DoubleConstantValueTag")) {
            return ((DoubleConstantValueTag)field.getTag("DoubleConstantValueTag")).getDoubleValue();
         }
      }

      return null;
   }

   public static boolean acceptsStringInitialValue(SootField field) {
      if (field.getType() instanceof RefType) {
         SootClass fieldClass = ((RefType)field.getType()).getSootClass();
         return fieldClass.getName().equals("java.lang.String");
      } else {
         return false;
      }
   }

   public static int sizeOfType(Type t) {
      if (!(t instanceof DoubleWordType) && !(t instanceof LongType) && !(t instanceof DoubleType)) {
         return t instanceof VoidType ? 0 : 1;
      } else {
         return 2;
      }
   }

   public static Attribute createASMAttribute(final soot.tagkit.Attribute attr) {
      return new Attribute(attr.getName()) {
         protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
            ByteVector result = new ByteVector();
            result.putByteArray(attr.getValue(), 0, attr.getValue().length);
            return result;
         }
      };
   }

   public static String translateJavaVersion(int javaVersion) {
      return javaVersion == 1 ? "1.0" : "1." + (javaVersion - 1);
   }
}
