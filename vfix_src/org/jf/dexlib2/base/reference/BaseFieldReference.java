package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.util.ReferenceUtil;

public abstract class BaseFieldReference implements FieldReference {
   public int hashCode() {
      int hashCode = this.getDefiningClass().hashCode();
      hashCode = hashCode * 31 + this.getName().hashCode();
      return hashCode * 31 + this.getType().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      if (!(o instanceof FieldReference)) {
         return false;
      } else {
         FieldReference other = (FieldReference)o;
         return this.getDefiningClass().equals(other.getDefiningClass()) && this.getName().equals(other.getName()) && this.getType().equals(other.getType());
      }
   }

   public int compareTo(@Nonnull FieldReference o) {
      int res = this.getDefiningClass().compareTo(o.getDefiningClass());
      if (res != 0) {
         return res;
      } else {
         res = this.getName().compareTo(o.getName());
         return res != 0 ? res : this.getType().compareTo(o.getType());
      }
   }

   public String toString() {
      return ReferenceUtil.getFieldDescriptor(this);
   }
}
