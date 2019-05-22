package polyglot.types.reflect;

public class Constant {
   int tag;
   Object value;
   static final byte CLASS = 7;
   static final byte FIELD_REF = 9;
   static final byte METHOD_REF = 10;
   static final byte STRING = 8;
   static final byte INTEGER = 3;
   static final byte FLOAT = 4;
   static final byte LONG = 5;
   static final byte DOUBLE = 6;
   static final byte INTERFACE_METHOD_REF = 11;
   static final byte NAME_AND_TYPE = 12;
   static final byte UTF8 = 1;

   Constant(int tag, Object value) {
      this.tag = tag;
      this.value = value;
   }

   final int tag() {
      return this.tag;
   }

   public final Object value() {
      return this.value;
   }

   public int hashCode() {
      switch(this.tag) {
      case 1:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         return this.tag ^ this.value.hashCode();
      case 2:
      default:
         return this.tag;
      case 9:
      case 10:
      case 11:
      case 12:
         return this.tag ^ ((int[])this.value)[0] ^ ((int[])this.value)[1];
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof Constant)) {
         return false;
      } else {
         Constant c = (Constant)other;
         if (this.tag != c.tag) {
            return false;
         } else {
            switch(this.tag) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
               return this.value.equals(c.value);
            case 2:
            default:
               return false;
            case 9:
            case 10:
            case 11:
            case 12:
               return ((int[])this.value)[0] == ((int[])c.value)[0] && ((int[])this.value)[1] == ((int[])c.value)[1];
            }
         }
      }
   }
}
