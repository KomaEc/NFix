package org.jf.dexlib2.writer.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.writer.AnnotationSetSection;

class BuilderAnnotationSetPool extends BaseBuilderPool implements AnnotationSetSection<BuilderAnnotation, BuilderAnnotationSet> {
   @Nonnull
   private final ConcurrentMap<Set<? extends Annotation>, BuilderAnnotationSet> internedItems = Maps.newConcurrentMap();

   public BuilderAnnotationSetPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderAnnotationSet internAnnotationSet(@Nullable Set<? extends Annotation> annotations) {
      if (annotations == null) {
         return BuilderAnnotationSet.EMPTY;
      } else {
         BuilderAnnotationSet ret = (BuilderAnnotationSet)this.internedItems.get(annotations);
         if (ret != null) {
            return ret;
         } else {
            BuilderAnnotationSet annotationSet = new BuilderAnnotationSet(ImmutableSet.copyOf(Iterators.transform(annotations.iterator(), new Function<Annotation, BuilderAnnotation>() {
               @Nullable
               public BuilderAnnotation apply(Annotation input) {
                  return ((BuilderAnnotationPool)BuilderAnnotationSetPool.this.dexBuilder.annotationSection).internAnnotation(input);
               }
            })));
            ret = (BuilderAnnotationSet)this.internedItems.putIfAbsent(annotationSet, annotationSet);
            return ret == null ? annotationSet : ret;
         }
      }
   }

   @Nonnull
   public Collection<? extends BuilderAnnotation> getAnnotations(@Nonnull BuilderAnnotationSet key) {
      return key.annotations;
   }

   public int getNullableItemOffset(@Nullable BuilderAnnotationSet key) {
      return key == null ? 0 : key.offset;
   }

   public int getItemOffset(@Nonnull BuilderAnnotationSet key) {
      return key.offset;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderAnnotationSet, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderAnnotationSet>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderAnnotationSet key) {
            return key.offset;
         }

         protected int setValue(@Nonnull BuilderAnnotationSet key, int value) {
            int prev = key.offset;
            key.offset = value;
            return prev;
         }
      };
   }
}
