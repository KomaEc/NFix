package soot.util;

public final class NumberedString implements Numberable {
   private final String s;
   private volatile int number;

   public NumberedString(String s) {
      this.s = s;
   }

   public final String toString() {
      return this.getString();
   }

   public final String getString() {
      if (this.number == 0) {
         throw new RuntimeException("oops");
      } else {
         return this.s;
      }
   }

   public final void setNumber(int number) {
      this.number = number;
   }

   public final int getNumber() {
      return this.number;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.number;
      result = 31 * result + (this.s == null ? 0 : this.s.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NumberedString other = (NumberedString)obj;
         if (this.number != other.number) {
            return false;
         } else {
            if (this.s == null) {
               if (other.s != null) {
                  return false;
               }
            } else if (!this.s.equals(other.s)) {
               return false;
            }

            return true;
         }
      }
   }
}
