package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableAnnotation extends BaseAnnotation {
   protected final int visibility;
   @Nonnull
   protected final String type;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotationElement> elements;
   private static final ImmutableConverter<ImmutableAnnotation, Annotation> CONVERTER = new ImmutableConverter<ImmutableAnnotation, Annotation>() {
      protected boolean isImmutable(@Nonnull Annotation item) {
         return item instanceof ImmutableAnnotation;
      }

      @Nonnull
      protected ImmutableAnnotation makeImmutable(@Nonnull Annotation item) {
         return ImmutableAnnotation.of(item);
      }
   };

   public ImmutableAnnotation(int visibility, @Nonnull String type, @Nullable Collection<? extends AnnotationElement> elements) {
      this.visibility = visibility;
      this.type = type;
      this.elements = ImmutableAnnotationElement.immutableSetOf(elements);
   }

   public ImmutableAnnotation(int visibility, @Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotationElement> elements) {
      this.visibility = visibility;
      this.type = type;
      this.elements = ImmutableUtils.nullToEmptySet(elements);
   }

   public static ImmutableAnnotation of(Annotation annotation) {
      return annotation instanceof ImmutableAnnotation ? (ImmutableAnnotation)annotation : new ImmutableAnnotation(annotation.getVisibility(), annotation.getType(), annotation.getElements());
   }

   public int getVisibility() {
      return this.visibility;
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableAnnotationElement> getElements() {
      return this.elements;
   }

   @Nonnull
   public static ImmutableSet<ImmutableAnnotation> immutableSetOf(@Nullable Iterable<? extends Annotation> list) {
      return CONVERTER.toSet(list);
   }
}
