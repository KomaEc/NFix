package org.jf.dexlib2.base;

import com.google.common.primitives.Ints;
import java.util.Comparator;
import org.jf.dexlib2.iface.Annotation;
import org.jf.util.CollectionUtils;

public abstract class BaseAnnotation implements Annotation {
   public static final Comparator<? super Annotation> BY_TYPE = new Comparator<Annotation>() {
      public int compare(Annotation annotation1, Annotation annotation2) {
         return annotation1.getType().compareTo(annotation2.getType());
      }
   };

   public int hashCode() {
      int hashCode = this.getVisibility();
      hashCode = hashCode * 31 + this.getType().hashCode();
      return hashCode * 31 + this.getElements().hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof Annotation)) {
         return false;
      } else {
         Annotation other = (Annotation)o;
         return this.getVisibility() == other.getVisibility() && this.getType().equals(other.getType()) && this.getElements().equals(other.getElements());
      }
   }

   public int compareTo(Annotation o) {
      int res = Ints.compare(this.getVisibility(), o.getVisibility());
      if (res != 0) {
         return res;
      } else {
         res = this.getType().compareTo(o.getType());
         return res != 0 ? res : CollectionUtils.compareAsSet(this.getElements(), o.getElements());
      }
   }
}
