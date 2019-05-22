package org.jf.dexlib2.writer.builder;

import com.google.common.collect.ImmutableSet;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;

public class BuilderAnnotationSet extends AbstractSet<BuilderAnnotation> {
   public static final BuilderAnnotationSet EMPTY = new BuilderAnnotationSet(ImmutableSet.of());
   @Nonnull
   final Set<BuilderAnnotation> annotations;
   int offset = 0;

   public BuilderAnnotationSet(@Nonnull Set<BuilderAnnotation> annotations) {
      this.annotations = annotations;
   }

   @Nonnull
   public Iterator<BuilderAnnotation> iterator() {
      return this.annotations.iterator();
   }

   public int size() {
      return this.annotations.size();
   }
}
