package org.jf.dexlib2.base;

import java.util.Comparator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.AnnotationElement;

public abstract class BaseAnnotationElement implements AnnotationElement {
   public static final Comparator<AnnotationElement> BY_NAME = new Comparator<AnnotationElement>() {
      public int compare(@Nonnull AnnotationElement element1, @Nonnull AnnotationElement element2) {
         return element1.getName().compareTo(element2.getName());
      }
   };

   public int hashCode() {
      int hashCode = this.getName().hashCode();
      return hashCode * 31 + this.getValue().hashCode();
   }

   public boolean equals(Object o) {
      if (o != null && o instanceof AnnotationElement) {
         AnnotationElement other = (AnnotationElement)o;
         return this.getName().equals(other.getName()) && this.getValue().equals(other.getValue());
      } else {
         return false;
      }
   }

   public int compareTo(AnnotationElement o) {
      int res = this.getName().compareTo(o.getName());
      return res != 0 ? res : this.getValue().compareTo(o.getValue());
   }
}
