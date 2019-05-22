package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.writer.AnnotationSection;

public class AnnotationPool extends BaseOffsetPool<Annotation> implements AnnotationSection<CharSequence, CharSequence, Annotation, AnnotationElement, EncodedValue> {
   public AnnotationPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull Annotation annotation) {
      Integer prev = (Integer)this.internedItems.put(annotation, 0);
      if (prev == null) {
         ((TypePool)this.dexPool.typeSection).intern(annotation.getType());
         Iterator var3 = annotation.getElements().iterator();

         while(var3.hasNext()) {
            AnnotationElement element = (AnnotationElement)var3.next();
            ((StringPool)this.dexPool.stringSection).intern(element.getName());
            this.dexPool.internEncodedValue(element.getValue());
         }
      }

   }

   public int getVisibility(@Nonnull Annotation annotation) {
      return annotation.getVisibility();
   }

   @Nonnull
   public CharSequence getType(@Nonnull Annotation annotation) {
      return annotation.getType();
   }

   @Nonnull
   public Collection<? extends AnnotationElement> getElements(@Nonnull Annotation annotation) {
      return annotation.getElements();
   }

   @Nonnull
   public CharSequence getElementName(@Nonnull AnnotationElement annotationElement) {
      return annotationElement.getName();
   }

   @Nonnull
   public EncodedValue getElementValue(@Nonnull AnnotationElement annotationElement) {
      return annotationElement.getValue();
   }
}
