package soot.dexpler;

import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;

public class DexType {
   protected String name;
   protected TypeReference type;

   public DexType(TypeReference type) {
      if (type == null) {
         throw new RuntimeException("error: type ref is null!");
      } else {
         this.type = type;
         this.name = type.getType();
      }
   }

   public DexType(String type) {
      if (type == null) {
         throw new RuntimeException("error: type is null!");
      } else {
         this.type = new ImmutableTypeReference(type);
         this.name = type;
      }
   }

   public String getName() {
      return this.name;
   }

   public boolean overwriteEquivalent(DexType field) {
      return this.name.equals(field.getName());
   }

   public TypeReference getType() {
      return this.type;
   }

   public Type toSoot() {
      return toSoot(this.type.getType(), 0);
   }

   public static Type toSoot(TypeReference type) {
      return toSoot(type.getType(), 0);
   }

   public static Type toSoot(String type) {
      return toSoot(type, 0);
   }

   public static boolean isWide(TypeReference typeReference) {
      String t = typeReference.getType();
      return isWide(t);
   }

   public static boolean isWide(String type) {
      return type.startsWith("J") || type.startsWith("D");
   }

   private static Type toSoot(String typeDescriptor, int pos) {
      char typeDesignator = typeDescriptor.charAt(pos);
      Object type;
      switch(typeDesignator) {
      case 'B':
         type = ByteType.v();
         break;
      case 'C':
         type = CharType.v();
         break;
      case 'D':
         type = DoubleType.v();
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
      case 'W':
      case 'X':
      case 'Y':
      default:
         type = UnknownType.v();
         break;
      case 'F':
         type = FloatType.v();
         break;
      case 'I':
         type = IntType.v();
         break;
      case 'J':
         type = LongType.v();
         break;
      case 'L':
         type = RefType.v(Util.dottedClassName(typeDescriptor));
         break;
      case 'S':
         type = ShortType.v();
         break;
      case 'V':
         type = VoidType.v();
         break;
      case 'Z':
         type = BooleanType.v();
         break;
      case '[':
         type = toSoot(typeDescriptor, pos + 1).makeArrayType();
      }

      return (Type)type;
   }

   public static String toSootICAT(String type) {
      type = type.replace(".", "/");
      String r = "";
      String[] split1 = type.split(";");
      String[] var3 = split1;
      int var4 = split1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String s = var3[var5];
         if (s.startsWith("L")) {
            s = s.replaceFirst("L", "");
         }

         if (s.startsWith("<L")) {
            s = s.replaceFirst("<L", "<");
         }

         r = r + s;
      }

      return r;
   }

   public static String toDalvikICAT(String type) {
      type = type.replaceAll("<", "<L");
      type = type.replaceAll(">", ">;");
      type = "L" + type;
      type = type.replaceAll("L\\*;", "*");
      if (!type.endsWith(";")) {
         type = type + ";";
      }

      return type;
   }

   public static String toSootAT(String type) {
      return type;
   }

   public String toString() {
      return this.name;
   }
}
