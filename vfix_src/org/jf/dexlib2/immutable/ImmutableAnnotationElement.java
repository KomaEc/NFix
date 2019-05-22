package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import org.jf.util.ImmutableConverter;

public class ImmutableAnnotationElement extends BaseAnnotationElement {
   @Nonnull
   protected final String name;
   @Nonnull
   protected final ImmutableEncodedValue value;
   private static final ImmutableConverter<ImmutableAnnotationElement, AnnotationElement> CONVERTER = new ImmutableConverter<ImmutableAnnotationElement, AnnotationElement>() {
      protected boolean isImmutable(@Nonnull AnnotationElement item) {
         return item instanceof ImmutableAnnotationElement;
      }

      @Nonnull
      protected ImmutableAnnotationElement makeImmutable(@Nonnull AnnotationElement item) {
         return ImmutableAnnotationElement.of(item);
      }
   };

   public ImmutableAnnotationElement(@Nonnull String name, @Nonnull EncodedValue value) {
      this.name = name;
      this.value = ImmutableEncodedValueFactory.of(value);
   }

   public ImmutableAnnotationElement(@Nonnull String name, @Nonnull ImmutableEncodedValue value) {
      this.name = name;
      this.value = value;
   }

   public static ImmutableAnnotationElement of(AnnotationElement annotationElement) {
      return annotationElement instanceof ImmutableAnnotationElement ? (ImmutableAnnotationElement)annotationElement : new ImmutableAnnotationElement(annotationElement.getName(), annotationElement.getValue());
   }

   @Nonnull
   public String getName() {
      return this.name;
   }

   @Nonnull
   public EncodedValue getValue() {
      return this.value;
   }

   @Nonnull
   public static ImmutableSet<ImmutableAnnotationElement> immutableSetOf(@Nullable Iterable<? extends AnnotationElement> list) {
      return CONVERTER.toSet(list);
   }
}
