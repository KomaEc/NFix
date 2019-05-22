package org.jf.dexlib2.immutable.value;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.value.BaseAnnotationEncodedValue;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.immutable.ImmutableAnnotationElement;
import org.jf.util.ImmutableUtils;

public class ImmutableAnnotationEncodedValue extends BaseAnnotationEncodedValue implements ImmutableEncodedValue {
   @Nonnull
   protected final String type;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotationElement> elements;

   public ImmutableAnnotationEncodedValue(@Nonnull String type, @Nullable Collection<? extends AnnotationElement> elements) {
      this.type = type;
      this.elements = ImmutableAnnotationElement.immutableSetOf(elements);
   }

   public ImmutableAnnotationEncodedValue(@Nonnull String type, @Nullable ImmutableSet<? extends ImmutableAnnotationElement> elements) {
      this.type = type;
      this.elements = ImmutableUtils.nullToEmptySet(elements);
   }

   public static ImmutableAnnotationEncodedValue of(AnnotationEncodedValue annotationEncodedValue) {
      return annotationEncodedValue instanceof ImmutableAnnotationEncodedValue ? (ImmutableAnnotationEncodedValue)annotationEncodedValue : new ImmutableAnnotationEncodedValue(annotationEncodedValue.getType(), annotationEncodedValue.getElements());
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableAnnotationElement> getElements() {
      return this.elements;
   }
}
