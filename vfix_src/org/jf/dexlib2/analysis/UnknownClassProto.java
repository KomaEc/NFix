package org.jf.dexlib2.analysis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public class UnknownClassProto implements TypeProto {
   @Nonnull
   protected final ClassPath classPath;

   public UnknownClassProto(@Nonnull ClassPath classPath) {
      this.classPath = classPath;
   }

   public String toString() {
      return "Ujava/lang/Object;";
   }

   @Nonnull
   public ClassPath getClassPath() {
      return this.classPath;
   }

   @Nullable
   public String getSuperclass() {
      return null;
   }

   public boolean isInterface() {
      return false;
   }

   public boolean implementsInterface(@Nonnull String iface) {
      return false;
   }

   @Nonnull
   public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
      if (other.getType().equals("Ljava/lang/Object;")) {
         return other;
      } else {
         return (TypeProto)(other instanceof ArrayProto ? this.classPath.getClass("Ljava/lang/Object;") : this);
      }
   }

   @Nonnull
   public String getType() {
      return "Ujava/lang/Object;";
   }

   @Nullable
   public FieldReference getFieldByOffset(int fieldOffset) {
      return this.classPath.getClass("Ljava/lang/Object;").getFieldByOffset(fieldOffset);
   }

   @Nullable
   public Method getMethodByVtableIndex(int vtableIndex) {
      return this.classPath.getClass("Ljava/lang/Object;").getMethodByVtableIndex(vtableIndex);
   }

   public int findMethodIndexInVtable(@Nonnull MethodReference method) {
      return this.classPath.getClass("Ljava/lang/Object;").findMethodIndexInVtable(method);
   }
}
