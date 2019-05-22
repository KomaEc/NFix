package org.jf.dexlib2.iface;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodReference;

public interface Method extends MethodReference, Member {
   @Nonnull
   String getDefiningClass();

   @Nonnull
   String getName();

   @Nonnull
   List<? extends MethodParameter> getParameters();

   @Nonnull
   String getReturnType();

   int getAccessFlags();

   @Nonnull
   Set<? extends Annotation> getAnnotations();

   @Nullable
   MethodImplementation getImplementation();
}
