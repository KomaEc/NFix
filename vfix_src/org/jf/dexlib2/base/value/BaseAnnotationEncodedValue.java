package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.util.CollectionUtils;

public abstract class BaseAnnotationEncodedValue implements AnnotationEncodedValue {
   public int hashCode() {
      int hashCode = this.getType().hashCode();
      return hashCode * 31 + this.getElements().hashCode();
   }

   public boolean equals(@Nullable Object o) {
      if (!(o instanceof AnnotationEncodedValue)) {
         return false;
      } else {
         AnnotationEncodedValue other = (AnnotationEncodedValue)o;
         return this.getType().equals(other.getType()) && this.getElements().equals(other.getElements());
      }
   }

   public int compareTo(@Nonnull EncodedValue o) {
      int res = Ints.compare(this.getValueType(), o.getValueType());
      if (res != 0) {
         return res;
      } else {
         AnnotationEncodedValue other = (AnnotationEncodedValue)o;
         res = this.getType().compareTo(other.getType());
         return res != 0 ? res : CollectionUtils.compareAsSet(this.getElements(), other.getElements());
      }
   }

   public int getValueType() {
      return 29;
   }
}
