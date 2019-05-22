package org.jf.dexlib2.analysis;

import com.google.common.base.Strings;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.util.TypeUtils;
import org.jf.util.ExceptionWithContext;

public class ArrayProto implements TypeProto {
   protected final ClassPath classPath;
   protected final int dimensions;
   protected final String elementType;
   private static final String BRACKETS = Strings.repeat("[", 256);

   public ArrayProto(@Nonnull ClassPath classPath, @Nonnull String type) {
      this.classPath = classPath;
      int i = 0;

      do {
         if (type.charAt(i) != '[') {
            if (i == 0) {
               throw new ExceptionWithContext("Invalid array type: %s", new Object[]{type});
            }

            this.dimensions = i;
            this.elementType = type.substring(i);
            return;
         }

         ++i;
      } while(i != type.length());

      throw new ExceptionWithContext("Invalid array type: %s", new Object[]{type});
   }

   public String toString() {
      return this.getType();
   }

   @Nonnull
   public ClassPath getClassPath() {
      return this.classPath;
   }

   @Nonnull
   public String getType() {
      return makeArrayType(this.elementType, this.dimensions);
   }

   public int getDimensions() {
      return this.dimensions;
   }

   public boolean isInterface() {
      return false;
   }

   @Nonnull
   public String getElementType() {
      return this.elementType;
   }

   @Nonnull
   public String getImmediateElementType() {
      return this.dimensions > 1 ? makeArrayType(this.elementType, this.dimensions - 1) : this.elementType;
   }

   public boolean implementsInterface(@Nonnull String iface) {
      return iface.equals("Ljava/lang/Cloneable;") || iface.equals("Ljava/io/Serializable;");
   }

   @Nullable
   public String getSuperclass() {
      return "Ljava/lang/Object;";
   }

   @Nonnull
   public TypeProto getCommonSuperclass(@Nonnull TypeProto other) {
      if (other instanceof ArrayProto) {
         if (!TypeUtils.isPrimitiveType(this.getElementType()) && !TypeUtils.isPrimitiveType(((ArrayProto)other).getElementType())) {
            if (this.dimensions == ((ArrayProto)other).dimensions) {
               TypeProto thisClass = this.classPath.getClass(this.elementType);
               TypeProto otherClass = this.classPath.getClass(((ArrayProto)other).elementType);
               TypeProto mergedClass = thisClass.getCommonSuperclass(otherClass);
               if (thisClass == mergedClass) {
                  return this;
               } else {
                  return otherClass == mergedClass ? other : this.classPath.getClass(makeArrayType(mergedClass.getType(), this.dimensions));
               }
            } else {
               int dimensions = Math.min(this.dimensions, ((ArrayProto)other).dimensions);
               return this.classPath.getClass(makeArrayType("Ljava/lang/Object;", dimensions));
            }
         } else {
            return (TypeProto)(this.dimensions == ((ArrayProto)other).dimensions && this.getElementType().equals(((ArrayProto)other).getElementType()) ? this : this.classPath.getClass("Ljava/lang/Object;"));
         }
      } else if (other instanceof ClassProto) {
         try {
            if (other.isInterface() && this.implementsInterface(other.getType())) {
               return other;
            }
         } catch (UnresolvedClassException var5) {
         }

         return this.classPath.getClass("Ljava/lang/Object;");
      } else {
         return other.getCommonSuperclass(this);
      }
   }

   @Nonnull
   private static String makeArrayType(@Nonnull String elementType, int dimensions) {
      return BRACKETS.substring(0, dimensions) + elementType;
   }

   @Nullable
   public FieldReference getFieldByOffset(int fieldOffset) {
      return fieldOffset == 8 ? new ImmutableFieldReference(this.getType(), "length", "int") : null;
   }

   @Nullable
   public Method getMethodByVtableIndex(int vtableIndex) {
      return this.classPath.getClass("Ljava/lang/Object;").getMethodByVtableIndex(vtableIndex);
   }

   public int findMethodIndexInVtable(@Nonnull MethodReference method) {
      return this.classPath.getClass("Ljava/lang/Object;").findMethodIndexInVtable(method);
   }
}
