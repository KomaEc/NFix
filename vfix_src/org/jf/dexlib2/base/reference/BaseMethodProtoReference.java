package org.jf.dexlib2.base.reference;

import com.google.common.collect.Ordering;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.util.ReferenceUtil;
import org.jf.util.CharSequenceUtils;
import org.jf.util.CollectionUtils;

public abstract class BaseMethodProtoReference implements MethodProtoReference {
   public int hashCode() {
      int hashCode = this.getReturnType().hashCode();
      return hashCode * 31 + this.getParameterTypes().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      if (!(o instanceof MethodProtoReference)) {
         return false;
      } else {
         MethodProtoReference other = (MethodProtoReference)o;
         return this.getReturnType().equals(other.getReturnType()) && CharSequenceUtils.listEquals(this.getParameterTypes(), other.getParameterTypes());
      }
   }

   public int compareTo(@Nonnull MethodProtoReference o) {
      int res = this.getReturnType().compareTo(o.getReturnType());
      return res != 0 ? res : CollectionUtils.compareAsIterable(Ordering.usingToString(), this.getParameterTypes(), o.getParameterTypes());
   }

   public String toString() {
      return ReferenceUtil.getMethodProtoDescriptor(this);
   }
}
