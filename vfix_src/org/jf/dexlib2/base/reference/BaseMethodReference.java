package org.jf.dexlib2.base.reference;

import com.google.common.collect.Ordering;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.util.CharSequenceUtils;
import org.jf.util.CollectionUtils;

public abstract class BaseMethodReference implements MethodReference {
   public int hashCode() {
      int hashCode = this.getDefiningClass().hashCode();
      hashCode = hashCode * 31 + this.getName().hashCode();
      hashCode = hashCode * 31 + this.getReturnType().hashCode();
      return hashCode * 31 + this.getParameterTypes().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      if (o != null && o instanceof MethodReference) {
         MethodReference other = (MethodReference)o;
         return this.getDefiningClass().equals(other.getDefiningClass()) && this.getName().equals(other.getName()) && this.getReturnType().equals(other.getReturnType()) && CharSequenceUtils.listEquals(this.getParameterTypes(), other.getParameterTypes());
      } else {
         return false;
      }
   }

   public int compareTo(@Nonnull MethodReference o) {
      int res = this.getDefiningClass().compareTo(o.getDefiningClass());
      if (res != 0) {
         return res;
      } else {
         res = this.getName().compareTo(o.getName());
         if (res != 0) {
            return res;
         } else {
            res = this.getReturnType().compareTo(o.getReturnType());
            return res != 0 ? res : CollectionUtils.compareAsIterable(Ordering.usingToString(), this.getParameterTypes(), o.getParameterTypes());
         }
      }
   }

   public String toString() {
      return ReferenceUtil.getMethodDescriptor(this);
   }
}
