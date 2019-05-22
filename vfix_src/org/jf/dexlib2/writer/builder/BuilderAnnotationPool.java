package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.writer.AnnotationSection;

class BuilderAnnotationPool extends BaseBuilderPool implements AnnotationSection<BuilderStringReference, BuilderTypeReference, BuilderAnnotation, BuilderAnnotationElement, BuilderEncodedValues.BuilderEncodedValue> {
   @Nonnull
   private final ConcurrentMap<Annotation, BuilderAnnotation> internedItems = Maps.newConcurrentMap();

   public BuilderAnnotationPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderAnnotation internAnnotation(@Nonnull Annotation annotation) {
      BuilderAnnotation ret = (BuilderAnnotation)this.internedItems.get(annotation);
      if (ret != null) {
         return ret;
      } else {
         BuilderAnnotation dexBuilderAnnotation = new BuilderAnnotation(annotation.getVisibility(), ((BuilderTypePool)this.dexBuilder.typeSection).internType(annotation.getType()), this.dexBuilder.internAnnotationElements(annotation.getElements()));
         ret = (BuilderAnnotation)this.internedItems.putIfAbsent(dexBuilderAnnotation, dexBuilderAnnotation);
         return ret == null ? dexBuilderAnnotation : ret;
      }
   }

   public int getVisibility(@Nonnull BuilderAnnotation key) {
      return key.visibility;
   }

   @Nonnull
   public BuilderTypeReference getType(@Nonnull BuilderAnnotation key) {
      return key.type;
   }

   @Nonnull
   public Collection<? extends BuilderAnnotationElement> getElements(@Nonnull BuilderAnnotation key) {
      return key.elements;
   }

   @Nonnull
   public BuilderStringReference getElementName(@Nonnull BuilderAnnotationElement element) {
      return element.name;
   }

   @Nonnull
   public BuilderEncodedValues.BuilderEncodedValue getElementValue(@Nonnull BuilderAnnotationElement element) {
      return element.value;
   }

   public int getItemOffset(@Nonnull BuilderAnnotation key) {
      return key.offset;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderAnnotation, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderAnnotation>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderAnnotation key) {
            return key.offset;
         }

         protected int setValue(@Nonnull BuilderAnnotation key, int value) {
            int prev = key.offset;
            key.offset = value;
            return prev;
         }
      };
   }
}
