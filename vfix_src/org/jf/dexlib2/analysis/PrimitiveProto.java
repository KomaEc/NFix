package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.util.ExceptionWithContext;

public class PrimitiveProto implements TypeProto {
   protected final ClassPath classPath;
   protected final String type;

   public PrimitiveProto(@Nonnull ClassPath classPath, @Nonnull String type) {
      this.classPath = classPath;
      this.type = type;
   }

   public String toString() {
      return this.type;
   }

   @Nonnull
   public ClassPath getClassPath() {
      return this.classPath;
   }

   @Nonnull
   public String getType() {
      return this.type;
   }

   public boolean isInterface() {
      return false;
   }

   public boolean implementsInterface(@Nonnull String iface) {
      return false;
   }

   @Nullable
   public String getSuperclass() {
      return null;
   }

   @Nonnull
   public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
      throw new ExceptionWithContext("Cannot call getCommonSuperclass on PrimitiveProto", new Object[0]);
   }

   @Nullable
   public FieldReference getFieldByOffset(int fieldOffset) {
      return null;
   }

   @Nullable
   public Method getMethodByVtableIndex(int vtableIndex) {
      return null;
   }

   public int findMethodIndexInVtable(@Nonnull MethodReference method) {
      return -1;
   }
}
