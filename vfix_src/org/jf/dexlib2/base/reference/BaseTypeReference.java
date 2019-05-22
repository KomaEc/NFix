package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.TypeReference;

public abstract class BaseTypeReference implements TypeReference {
   public int hashCode() {
      return this.getType().hashCode();
   }

   public boolean equals(Object o) {
      if (o != null) {
         if (o instanceof TypeReference) {
            return this.getType().equals(((TypeReference)o).getType());
         }

         if (o instanceof CharSequence) {
            return this.getType().equals(o.toString());
         }
      }

      return false;
   }

   public int compareTo(@Nonnull CharSequence o) {
      return this.getType().compareTo(o.toString());
   }

   public int length() {
      return this.getType().length();
   }

   public char charAt(int index) {
      return this.getType().charAt(index);
   }

   public CharSequence subSequence(int start, int end) {
      return this.getType().subSequence(start, end);
   }

   @Nonnull
   public String toString() {
      return this.getType();
   }
}
