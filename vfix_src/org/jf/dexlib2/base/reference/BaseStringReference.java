package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.StringReference;

public abstract class BaseStringReference implements StringReference {
   public int hashCode() {
      return this.getString().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      return o != null && o instanceof StringReference ? this.getString().equals(((StringReference)o).getString()) : false;
   }

   public int compareTo(@Nonnull CharSequence o) {
      return this.getString().compareTo(o.toString());
   }

   public int length() {
      return this.getString().length();
   }

   public char charAt(int index) {
      return this.getString().charAt(index);
   }

   public CharSequence subSequence(int start, int end) {
      return this.getString().subSequence(start, end);
   }

   @Nonnull
   public String toString() {
      return this.getString();
   }
}
