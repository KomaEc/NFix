package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.writer.AnnotationSetSection;

public class AnnotationSetPool extends BaseNullableOffsetPool<Set<? extends Annotation>> implements AnnotationSetSection<Annotation, Set<? extends Annotation>> {
   public AnnotationSetPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull Set<? extends Annotation> annotationSet) {
      if (annotationSet.size() > 0) {
         Integer prev = (Integer)this.internedItems.put(annotationSet, 0);
         if (prev == null) {
            Iterator var3 = annotationSet.iterator();

            while(var3.hasNext()) {
               Annotation annotation = (Annotation)var3.next();
               ((AnnotationPool)this.dexPool.annotationSection).intern(annotation);
            }
         }
      }

   }

   @Nonnull
   public Collection<? extends Annotation> getAnnotations(@Nonnull Set<? extends Annotation> annotations) {
      return annotations;
   }
}
