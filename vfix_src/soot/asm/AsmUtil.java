package soot.asm;

import java.util.ArrayList;
import java.util.List;
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
import soot.SootClass;
import soot.Type;
import soot.VoidType;

class AsmUtil {
   public static boolean isDWord(Type type) {
      return type instanceof LongType || type instanceof DoubleType;
   }

   public static Type toBaseType(String internal) {
      if (internal.charAt(0) == '[') {
         internal = internal.substring(internal.lastIndexOf(91) + 1, internal.length());
      }

      if (internal.charAt(internal.length() - 1) == ';') {
         internal = internal.substring(0, internal.length() - 1);
         if (internal.charAt(0) == 'L') {
            internal = internal.substring(1, internal.length());
         }

         internal = toQualifiedName(internal);
         return RefType.v(internal);
      } else {
         switch(internal.charAt(0)) {
         case 'B':
            return ByteType.v();
         case 'C':
            return CharType.v();
         case 'D':
            return DoubleType.v();
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         default:
            internal = toQualifiedName(internal);
            return RefType.v(internal);
         case 'F':
            return FloatType.v();
         case 'I':
            return IntType.v();
         case 'J':
            return LongType.v();
         case 'S':
            return ShortType.v();
         case 'Z':
            return BooleanType.v();
         }
      }
   }

   public static String toQualifiedName(String internal) {
      return internal.replace('/', '.');
   }

   public static String toInternalName(String qual) {
      return qual.replace('.', '/');
   }

   public static String toInternalName(SootClass cls) {
      return toInternalName(cls.getName());
   }

   public static Type toJimpleRefType(String desc) {
      return (Type)(desc.charAt(0) == '[' ? toJimpleType(desc) : RefType.v(toQualifiedName(desc)));
   }

   public static Type toJimpleType(String desc) {
      int idx = desc.lastIndexOf(91);
      int nrDims = idx + 1;
      if (nrDims > 0) {
         if (desc.charAt(0) != '[') {
            throw new AssertionError("Invalid array descriptor: " + desc);
         }

         desc = desc.substring(idx + 1);
      }

      Object baseType;
      switch(desc.charAt(0)) {
      case 'B':
         baseType = ByteType.v();
         break;
      case 'C':
         baseType = CharType.v();
         break;
      case 'D':
         baseType = DoubleType.v();
         break;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      default:
         throw new AssertionError("Unknown descriptor: " + desc);
      case 'F':
         baseType = FloatType.v();
         break;
      case 'I':
         baseType = IntType.v();
         break;
      case 'J':
         baseType = LongType.v();
         break;
      case 'L':
         if (desc.charAt(desc.length() - 1) != ';') {
            throw new AssertionError("Invalid reference descriptor: " + desc);
         }

         String name = desc.substring(1, desc.length() - 1);
         name = toQualifiedName(name);
         baseType = RefType.v(name);
         break;
      case 'S':
         baseType = ShortType.v();
         break;
      case 'Z':
         baseType = BooleanType.v();
      }

      if (!(baseType instanceof RefLikeType) && desc.length() > 1) {
         throw new AssertionError("Invalid primitive type descriptor: " + desc);
      } else {
         return (Type)(nrDims > 0 ? ArrayType.v((Type)baseType, nrDims) : baseType);
      }
   }

   public static List<Type> toJimpleDesc(String desc) {
      ArrayList<Type> types = new ArrayList(2);
      int len = desc.length();
      int idx = 0;

      label53:
      while(idx != len) {
         int nrDims = 0;
         Object baseType = null;

         label50:
         while(idx != len) {
            char c = desc.charAt(idx++);
            switch(c) {
            case '(':
            case ')':
               continue label53;
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
               throw new AssertionError("Unknown type: " + c);
            case 'B':
               baseType = ByteType.v();
               break label50;
            case 'C':
               baseType = CharType.v();
               break label50;
            case 'D':
               baseType = DoubleType.v();
               break label50;
            case 'F':
               baseType = FloatType.v();
               break label50;
            case 'I':
               baseType = IntType.v();
               break label50;
            case 'J':
               baseType = LongType.v();
               break label50;
            case 'L':
               int begin = idx;

               do {
                  ++idx;
               } while(desc.charAt(idx) != ';');

               String cls = desc.substring(begin, idx++);
               baseType = RefType.v(toQualifiedName(cls));
               break label50;
            case 'S':
               baseType = ShortType.v();
               break label50;
            case 'V':
               baseType = VoidType.v();
               break label50;
            case 'Z':
               baseType = BooleanType.v();
               break label50;
            case '[':
               ++nrDims;
            }
         }

         if (baseType != null && nrDims > 0) {
            types.add(ArrayType.v((Type)baseType, nrDims));
         } else {
            types.add(baseType);
         }
      }

      return types;
   }

   public static String baseTypeName(String s) {
      int index = s.indexOf("[");
      return index < 0 ? s : s.substring(0, index);
   }

   private AsmUtil() {
   }
}
