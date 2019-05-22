package bsh.org.objectweb.asm;

final class Item {
   short index;
   int type;
   int intVal;
   long longVal;
   float floatVal;
   double doubleVal;
   String strVal1;
   String strVal2;
   String strVal3;
   int hashCode;
   Item next;

   Item() {
   }

   Item(short var1, Item var2) {
      this.index = var1;
      this.type = var2.type;
      this.intVal = var2.intVal;
      this.longVal = var2.longVal;
      this.floatVal = var2.floatVal;
      this.doubleVal = var2.doubleVal;
      this.strVal1 = var2.strVal1;
      this.strVal2 = var2.strVal2;
      this.strVal3 = var2.strVal3;
      this.hashCode = var2.hashCode;
   }

   void set(int var1) {
      this.type = 3;
      this.intVal = var1;
      this.hashCode = this.type + var1;
   }

   void set(long var1) {
      this.type = 5;
      this.longVal = var1;
      this.hashCode = this.type + (int)var1;
   }

   void set(float var1) {
      this.type = 4;
      this.floatVal = var1;
      this.hashCode = this.type + (int)var1;
   }

   void set(double var1) {
      this.type = 6;
      this.doubleVal = var1;
      this.hashCode = this.type + (int)var1;
   }

   void set(int var1, String var2, String var3, String var4) {
      this.type = var1;
      this.strVal1 = var2;
      this.strVal2 = var3;
      this.strVal3 = var4;
      switch(var1) {
      case 1:
      case 7:
      case 8:
         this.hashCode = var1 + var2.hashCode();
         return;
      case 12:
         this.hashCode = var1 + var2.hashCode() * var3.hashCode();
         return;
      default:
         this.hashCode = var1 + var2.hashCode() * var3.hashCode() * var4.hashCode();
      }
   }

   boolean isEqualTo(Item var1) {
      if (var1.type != this.type) {
         return false;
      } else {
         switch(this.type) {
         case 1:
         case 7:
         case 8:
            return var1.strVal1.equals(this.strVal1);
         case 2:
         case 9:
         case 10:
         case 11:
         default:
            return var1.strVal1.equals(this.strVal1) && var1.strVal2.equals(this.strVal2) && var1.strVal3.equals(this.strVal3);
         case 3:
            return var1.intVal == this.intVal;
         case 4:
            return var1.floatVal == this.floatVal;
         case 5:
            return var1.longVal == this.longVal;
         case 6:
            return var1.doubleVal == this.doubleVal;
         case 12:
            return var1.strVal1.equals(this.strVal1) && var1.strVal2.equals(this.strVal2);
         }
      }
   }
}
