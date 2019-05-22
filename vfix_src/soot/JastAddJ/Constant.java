package soot.JastAddJ;

public class Constant {
   public boolean error = false;

   int intValue() {
      throw new UnsupportedOperationException();
   }

   long longValue() {
      throw new UnsupportedOperationException();
   }

   float floatValue() {
      throw new UnsupportedOperationException();
   }

   double doubleValue() {
      throw new UnsupportedOperationException();
   }

   boolean booleanValue() {
      throw new UnsupportedOperationException(this.getClass().getName());
   }

   String stringValue() {
      throw new UnsupportedOperationException();
   }

   protected Constant() {
   }

   static Constant create(int i) {
      return new Constant.ConstantInt(i);
   }

   static Constant create(long l) {
      return new Constant.ConstantLong(l);
   }

   static Constant create(float f) {
      return new Constant.ConstantFloat(f);
   }

   static Constant create(double d) {
      return new Constant.ConstantDouble(d);
   }

   static Constant create(boolean b) {
      return new Constant.ConstantBoolean(b);
   }

   static Constant create(char c) {
      return new Constant.ConstantChar(c);
   }

   static Constant create(String s) {
      return new Constant.ConstantString(s);
   }

   static class ConstantString extends Constant {
      private String value;

      public ConstantString(String s) {
         this.value = s;
      }

      String stringValue() {
         return this.value;
      }
   }

   static class ConstantBoolean extends Constant {
      private boolean value;

      public ConstantBoolean(boolean b) {
         this.value = b;
      }

      boolean booleanValue() {
         return this.value;
      }

      String stringValue() {
         return (new Boolean(this.value)).toString();
      }
   }

   static class ConstantChar extends Constant {
      private char value;

      public ConstantChar(char c) {
         this.value = c;
      }

      int intValue() {
         return this.value;
      }

      long longValue() {
         return (long)this.value;
      }

      float floatValue() {
         return (float)this.value;
      }

      double doubleValue() {
         return (double)this.value;
      }

      String stringValue() {
         return (new Character(this.value)).toString();
      }
   }

   static class ConstantDouble extends Constant {
      private double value;

      public ConstantDouble(double d) {
         this.value = d;
      }

      int intValue() {
         return (int)this.value;
      }

      long longValue() {
         return (long)this.value;
      }

      float floatValue() {
         return (float)this.value;
      }

      double doubleValue() {
         return this.value;
      }

      String stringValue() {
         return (new Double(this.value)).toString();
      }
   }

   static class ConstantFloat extends Constant {
      private float value;

      public ConstantFloat(float f) {
         this.value = f;
      }

      int intValue() {
         return (int)this.value;
      }

      long longValue() {
         return (long)this.value;
      }

      float floatValue() {
         return this.value;
      }

      double doubleValue() {
         return (double)this.value;
      }

      String stringValue() {
         return (new Float(this.value)).toString();
      }
   }

   static class ConstantLong extends Constant {
      private long value;

      public ConstantLong(long l) {
         this.value = l;
      }

      int intValue() {
         return (int)this.value;
      }

      long longValue() {
         return this.value;
      }

      float floatValue() {
         return (float)this.value;
      }

      double doubleValue() {
         return (double)this.value;
      }

      String stringValue() {
         return (new Long(this.value)).toString();
      }
   }

   static class ConstantInt extends Constant {
      private int value;

      public ConstantInt(int i) {
         this.value = i;
      }

      int intValue() {
         return this.value;
      }

      long longValue() {
         return (long)this.value;
      }

      float floatValue() {
         return (float)this.value;
      }

      double doubleValue() {
         return (double)this.value;
      }

      String stringValue() {
         return (new Integer(this.value)).toString();
      }
   }
}
