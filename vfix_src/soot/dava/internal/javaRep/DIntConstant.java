package soot.dava.internal.javaRep;

import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.Type;
import soot.jimple.IntConstant;

public class DIntConstant extends IntConstant {
   public Type type;

   private DIntConstant(int value, Type type) {
      super(value);
      this.type = type;
   }

   public static DIntConstant v(int value, Type type) {
      return new DIntConstant(value, type);
   }

   public String toString() {
      if (this.type != null) {
         if (this.type instanceof BooleanType) {
            if (this.value == 0) {
               return "false";
            }

            return "true";
         }

         if (this.type instanceof CharType) {
            String ch = "";
            switch(this.value) {
            case 8:
               ch = "\\b";
               break;
            case 9:
               ch = "\\t";
               break;
            case 10:
               ch = "\\n";
               break;
            case 12:
               ch = "\\f";
               break;
            case 13:
               ch = "\\r";
               break;
            case 34:
               ch = "\\\"";
               break;
            case 39:
               ch = "\\'";
               break;
            case 92:
               ch = "\\\\";
               break;
            default:
               if (this.value > 31 && this.value < 127) {
                  ch = (new Character((char)this.value)).toString();
               } else {
                  for(ch = Integer.toHexString(this.value); ch.length() < 4; ch = "0" + ch) {
                  }

                  if (ch.length() > 4) {
                     ch = ch.substring(ch.length() - 4);
                  }

                  ch = "\\u" + ch;
               }
            }

            return "'" + ch + "'";
         }

         if (this.type instanceof ByteType) {
            return "(byte) " + (new Integer(this.value)).toString();
         }
      }

      return (new Integer(this.value)).toString();
   }
}
