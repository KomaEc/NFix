package org.jf.dexlib2.immutable.reference;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseMethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.immutable.util.CharSequenceConverter;

public class ImmutableMethodProtoReference extends BaseMethodProtoReference implements ImmutableReference {
   @Nonnull
   protected final ImmutableList<String> parameters;
   @Nonnull
   protected final String returnType;

   public ImmutableMethodProtoReference(@Nullable Iterable<? extends CharSequence> parameters, @Nonnull String returnType) {
      this.parameters = CharSequenceConverter.immutableStringList(parameters);
      this.returnType = returnType;
   }

   @Nonnull
   public static ImmutableMethodProtoReference of(@Nonnull MethodProtoReference methodProtoReference) {
      return methodProtoReference instanceof ImmutableMethodProtoReference ? (ImmutableMethodProtoReference)methodProtoReference : new ImmutableMethodProtoReference(methodProtoReference.getParameterTypes(), methodProtoReference.getReturnType());
   }

   public List<? extends CharSequence> getParameterTypes() {
      return this.parameters;
   }

   public String getReturnType() {
      return this.returnType;
   }
}
