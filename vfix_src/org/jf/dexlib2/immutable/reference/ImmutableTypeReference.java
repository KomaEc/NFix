package org.jf.dexlib2.immutable.reference;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ImmutableConverter;

public class ImmutableTypeReference extends BaseTypeReference implements ImmutableReference {
   @Nonnull
   protected final String type;
   private static final ImmutableConverter<ImmutableTypeReference, TypeReference> CONVERTER = new ImmutableConverter<ImmutableTypeReference, TypeReference>() {
      protected boolean isImmutable(@Nonnull TypeReference item) {
         return item instanceof ImmutableTypeReference;
      }

      @Nonnull
      protected ImmutableTypeReference makeImmutable(@Nonnull TypeReference item) {
         return ImmutableTypeReference.of(item);
      }
   };

   public ImmutableTypeReference(String type) {
      this.type = type;
   }

   @Nonnull
   public static ImmutableTypeReference of(@Nonnull TypeReference typeReference) {
      return typeReference instanceof ImmutableTypeReference ? (ImmutableTypeReference)typeReference : new ImmutableTypeReference(typeReference.getType());
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   @Nonnull
   public static ImmutableList<ImmutableTypeReference> immutableListOf(@Nullable List<? extends TypeReference> list) {
      return CONVERTER.toList(list);
   }
}
