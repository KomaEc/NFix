package org.apache.tools.ant.util;

public class DeweyDecimal {
   private int[] components;

   public DeweyDecimal(int[] components) {
      this.components = new int[components.length];

      for(int i = 0; i < components.length; ++i) {
         this.components[i] = components[i];
      }

   }

   public DeweyDecimal(String string) throws NumberFormatException {
      java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(string, ".", true);
      int size = tokenizer.countTokens();
      this.components = new int[(size + 1) / 2];

      for(int i = 0; i < this.components.length; ++i) {
         String component = tokenizer.nextToken();
         if (component.equals("")) {
            throw new NumberFormatException("Empty component in string");
         }

         this.components[i] = Integer.parseInt(component);
         if (tokenizer.hasMoreTokens()) {
            tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens()) {
               throw new NumberFormatException("DeweyDecimal ended in a '.'");
            }
         }
      }

   }

   public int getSize() {
      return this.components.length;
   }

   public int get(int index) {
      return this.components[index];
   }

   public boolean isEqual(DeweyDecimal other) {
      int max = Math.max(other.components.length, this.components.length);

      for(int i = 0; i < max; ++i) {
         int component1 = i < this.components.length ? this.components[i] : 0;
         int component2 = i < other.components.length ? other.components[i] : 0;
         if (component2 != component1) {
            return false;
         }
      }

      return true;
   }

   public boolean isLessThan(DeweyDecimal other) {
      return !this.isGreaterThanOrEqual(other);
   }

   public boolean isLessThanOrEqual(DeweyDecimal other) {
      return !this.isGreaterThan(other);
   }

   public boolean isGreaterThan(DeweyDecimal other) {
      int max = Math.max(other.components.length, this.components.length);

      for(int i = 0; i < max; ++i) {
         int component1 = i < this.components.length ? this.components[i] : 0;
         int component2 = i < other.components.length ? other.components[i] : 0;
         if (component2 > component1) {
            return false;
         }

         if (component2 < component1) {
            return true;
         }
      }

      return false;
   }

   public boolean isGreaterThanOrEqual(DeweyDecimal other) {
      int max = Math.max(other.components.length, this.components.length);

      for(int i = 0; i < max; ++i) {
         int component1 = i < this.components.length ? this.components[i] : 0;
         int component2 = i < other.components.length ? other.components[i] : 0;
         if (component2 > component1) {
            return false;
         }

         if (component2 < component1) {
            return true;
         }
      }

      return true;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < this.components.length; ++i) {
         if (i != 0) {
            sb.append('.');
         }

         sb.append(this.components[i]);
      }

      return sb.toString();
   }
}
