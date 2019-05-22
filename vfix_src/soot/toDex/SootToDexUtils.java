package soot.toDex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jf.dexlib2.Opcode;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefLikeType;
import soot.RefType;
import soot.ShortType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

public class SootToDexUtils {
   private static final Map<Class<? extends Type>, String> sootToDexTypeDescriptor = new HashMap();

   public static String getDexTypeDescriptor(Type sootType) {
      if (sootType == null) {
         throw new NullPointerException("Soot type was null");
      } else {
         String typeDesc;
         if (sootType instanceof RefType) {
            typeDesc = getDexClassName(((RefType)sootType).getClassName());
         } else if (sootType instanceof ArrayType) {
            typeDesc = getDexArrayTypeDescriptor((ArrayType)sootType);
         } else {
            typeDesc = (String)sootToDexTypeDescriptor.get(sootType.getClass());
         }

         if (typeDesc != null && !typeDesc.isEmpty()) {
            return typeDesc;
         } else {
            throw new RuntimeException("Could not create type descriptor for class " + sootType);
         }
      }
   }

   public static String getDexClassName(String dottedClassName) {
      if (dottedClassName != null && !dottedClassName.isEmpty()) {
         String slashedName = dottedClassName.replace('.', '/');
         return slashedName.startsWith("L") && slashedName.endsWith(";") ? slashedName : "L" + slashedName + ";";
      } else {
         throw new RuntimeException("Empty class name detected");
      }
   }

   public static int getDexAccessFlags(SootMethod m) {
      int dexAccessFlags = m.getModifiers();
      if (m.isConstructor() || m.getName().equals("<clinit>")) {
         dexAccessFlags |= 65536;
      }

      if (m.isSynchronized()) {
         dexAccessFlags |= 131072;
         if (!m.isNative()) {
            dexAccessFlags &= -33;
         }
      }

      return dexAccessFlags;
   }

   public static String getArrayTypeDescriptor(ArrayType type) {
      Object baseType;
      if (type.numDimensions > 1) {
         baseType = ArrayType.v(type.baseType, 1);
      } else {
         baseType = type.baseType;
      }

      return getDexTypeDescriptor((Type)baseType);
   }

   private static String getDexArrayTypeDescriptor(ArrayType sootArray) {
      if (sootArray.numDimensions > 255) {
         throw new RuntimeException("dex does not support more than 255 dimensions! " + sootArray + " has " + sootArray.numDimensions);
      } else {
         String baseTypeDescriptor = getDexTypeDescriptor(sootArray.baseType);
         StringBuilder sb = new StringBuilder(sootArray.numDimensions + baseTypeDescriptor.length());

         for(int i = 0; i < sootArray.numDimensions; ++i) {
            sb.append('[');
         }

         sb.append(baseTypeDescriptor);
         return sb.toString();
      }
   }

   public static boolean isObject(String typeDescriptor) {
      if (typeDescriptor.isEmpty()) {
         return false;
      } else {
         char first = typeDescriptor.charAt(0);
         return first == 'L' || first == '[';
      }
   }

   public static boolean isObject(Type sootType) {
      return sootType instanceof RefLikeType;
   }

   public static boolean isWide(String typeDescriptor) {
      return typeDescriptor.equals("J") || typeDescriptor.equals("D");
   }

   public static boolean isWide(Type sootType) {
      return sootType instanceof LongType || sootType instanceof DoubleType;
   }

   public static int getRealRegCount(List<Register> regs) {
      int regCount = 0;

      Type regType;
      for(Iterator var2 = regs.iterator(); var2.hasNext(); regCount += getDexWords(regType)) {
         Register r = (Register)var2.next();
         regType = r.getType();
      }

      return regCount;
   }

   public static int getDexWords(Type sootType) {
      return isWide(sootType) ? 2 : 1;
   }

   public static int getDexWords(List<Type> sootTypes) {
      int dexWords = 0;

      Type t;
      for(Iterator var2 = sootTypes.iterator(); var2.hasNext(); dexWords += getDexWords(t)) {
         t = (Type)var2.next();
      }

      return dexWords;
   }

   public static int getOutWordCount(Collection<Unit> units) {
      int outWords = 0;
      Iterator var2 = units.iterator();

      while(true) {
         Stmt stmt;
         do {
            if (!var2.hasNext()) {
               return outWords;
            }

            Unit u = (Unit)var2.next();
            stmt = (Stmt)u;
         } while(!stmt.containsInvokeExpr());

         int wordsForParameters = 0;
         InvokeExpr invocation = stmt.getInvokeExpr();
         List<Value> args = invocation.getArgs();

         Value arg;
         for(Iterator var8 = args.iterator(); var8.hasNext(); wordsForParameters += getDexWords(arg.getType())) {
            arg = (Value)var8.next();
         }

         if (!invocation.getMethod().isStatic()) {
            ++wordsForParameters;
         }

         if (wordsForParameters > outWords) {
            outWords = wordsForParameters;
         }
      }
   }

   public static boolean fitsSigned4(long literal) {
      return literal >= -8L && literal <= 7L;
   }

   public static boolean fitsSigned8(long literal) {
      return literal >= -128L && literal <= 127L;
   }

   public static boolean fitsSigned16(long literal) {
      return literal >= -32768L && literal <= 32767L;
   }

   public static boolean fitsSigned32(long literal) {
      return literal >= -2147483648L && literal <= 2147483647L;
   }

   public static boolean isNormalMove(Opcode opc) {
      return opc.name.startsWith("move") && !opc.name.startsWith("move-result");
   }

   public static List<String> splitSignature(String sig) {
      List<String> split = new ArrayList();
      int len = sig.length();
      int i = 0;

      int j;
      for(boolean var4 = false; i < len; i = j) {
         char c = sig.charAt(i);
         if (c == 'L') {
            for(j = i + 1; j < len; ++j) {
               c = sig.charAt(j);
               if (c == ';') {
                  ++j;
                  break;
               }

               if (c == '<') {
                  break;
               }
            }
         } else {
            for(j = i + 1; j < len && sig.charAt(j) != 'L'; ++j) {
            }
         }

         split.add(sig.substring(i, j));
      }

      return split;
   }

   static {
      sootToDexTypeDescriptor.put(BooleanType.class, "Z");
      sootToDexTypeDescriptor.put(ByteType.class, "B");
      sootToDexTypeDescriptor.put(CharType.class, "C");
      sootToDexTypeDescriptor.put(DoubleType.class, "D");
      sootToDexTypeDescriptor.put(FloatType.class, "F");
      sootToDexTypeDescriptor.put(IntType.class, "I");
      sootToDexTypeDescriptor.put(LongType.class, "J");
      sootToDexTypeDescriptor.put(ShortType.class, "S");
      sootToDexTypeDescriptor.put(VoidType.class, "V");
   }
}
