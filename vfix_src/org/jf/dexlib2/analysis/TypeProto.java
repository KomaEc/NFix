package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public interface TypeProto {
   @Nonnull
   ClassPath getClassPath();

   @Nonnull
   String getType();

   boolean isInterface();

   boolean implementsInterface(@Nonnull String var1);

   @Nullable
   String getSuperclass();

   @Nonnull
   TypeProto getCommonSuperclass(@Nonnull TypeProto var1);

   @Nullable
   FieldReference getFieldByOffset(int var1);

   @Nullable
   Method getMethodByVtableIndex(int var1);

   int findMethodIndexInVtable(@Nonnull MethodReference var1);
}
