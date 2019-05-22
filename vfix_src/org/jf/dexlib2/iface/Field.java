package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.value.EncodedValue;

public interface Field extends FieldReference, Member {
   @Nonnull
   String getDefiningClass();

   @Nonnull
   String getName();

   @Nonnull
   String getType();

   int getAccessFlags();

   @Nullable
   EncodedValue getInitialValue();

   @Nonnull
   Set<? extends Annotation> getAnnotations();
}
