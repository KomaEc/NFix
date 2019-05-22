package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValueFactory;
import org.jf.util.ImmutableConverter;
import org.jf.util.ImmutableUtils;

public class ImmutableField extends BaseFieldReference implements Field {
   @Nonnull
   protected final String definingClass;
   @Nonnull
   protected final String name;
   @Nonnull
   protected final String type;
   protected final int accessFlags;
   @Nullable
   protected final ImmutableEncodedValue initialValue;
   @Nonnull
   protected final ImmutableSet<? extends ImmutableAnnotation> annotations;
   private static final ImmutableConverter<ImmutableField, Field> CONVERTER = new ImmutableConverter<ImmutableField, Field>() {
      protected boolean isImmutable(@Nonnull Field item) {
         return item instanceof ImmutableField;
      }

      @Nonnull
      protected ImmutableField makeImmutable(@Nonnull Field item) {
         return ImmutableField.of(item);
      }
   };

   public ImmutableField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable EncodedValue initialValue, @Nullable Collection<? extends Annotation> annotations) {
      this.definingClass = definingClass;
      this.name = name;
      this.type = type;
      this.accessFlags = accessFlags;
      this.initialValue = ImmutableEncodedValueFactory.ofNullable(initialValue);
      this.annotations = ImmutableAnnotation.immutableSetOf(annotations);
   }

   public ImmutableField(@Nonnull String definingClass, @Nonnull String name, @Nonnull String type, int accessFlags, @Nullable ImmutableEncodedValue initialValue, @Nullable ImmutableSet<? extends ImmutableAnnotation> annotations) {
      this.definingClass = definingClass;
      this.name = name;
      this.type = type;
      this.accessFlags = accessFlags;
      this.initialValue = initialValue;
      this.annotations = ImmutableUtils.nullToEmptySet(annotations);
   }

   public static ImmutableField of(Field field) {
      return field instanceof ImmutableField ? (ImmutableField)field : new ImmutableField(field.getDefiningClass(), field.getName(), field.getType(), field.getAccessFlags(), field.getInitialValue(), field.getAnnotations());
   }

   @Nonnull
   public String getDefiningClass() {
      return this.definingClass;
   }

   @Nonnull
   public String getName() {
      return this.name;
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   public int getAccessFlags() {
      return this.accessFlags;
   }

   public EncodedValue getInitialValue() {
      return this.initialValue;
   }

   @Nonnull
   public ImmutableSet<? extends ImmutableAnnotation> getAnnotations() {
      return this.annotations;
   }

   @Nonnull
   public static ImmutableSortedSet<ImmutableField> immutableSetOf(@Nullable Iterable<? extends Field> list) {
      return CONVERTER.toSortedSet(Ordering.natural(), (Iterable)list);
   }
}
