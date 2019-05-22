package org.jf.dexlib2.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.StringUtils;

public final class ReferenceUtil {
   public static String getMethodDescriptor(MethodReference methodReference) {
      return getMethodDescriptor(methodReference, false);
   }

   public static String getMethodDescriptor(MethodReference methodReference, boolean useImplicitReference) {
      StringBuilder sb = new StringBuilder();
      if (!useImplicitReference) {
         sb.append(methodReference.getDefiningClass());
         sb.append("->");
      }

      sb.append(methodReference.getName());
      sb.append('(');
      Iterator var3 = methodReference.getParameterTypes().iterator();

      while(var3.hasNext()) {
         CharSequence paramType = (CharSequence)var3.next();
         sb.append(paramType);
      }

      sb.append(')');
      sb.append(methodReference.getReturnType());
      return sb.toString();
   }

   public static String getMethodProtoDescriptor(MethodProtoReference methodProtoReference) {
      StringBuilder sb = new StringBuilder();
      sb.append('(');
      Iterator var2 = methodProtoReference.getParameterTypes().iterator();

      while(var2.hasNext()) {
         CharSequence paramType = (CharSequence)var2.next();
         sb.append(paramType);
      }

      sb.append(')');
      sb.append(methodProtoReference.getReturnType());
      return sb.toString();
   }

   public static void writeMethodDescriptor(Writer writer, MethodReference methodReference) throws IOException {
      writeMethodDescriptor(writer, methodReference, false);
   }

   public static void writeMethodDescriptor(Writer writer, MethodReference methodReference, boolean useImplicitReference) throws IOException {
      if (!useImplicitReference) {
         writer.write(methodReference.getDefiningClass());
         writer.write("->");
      }

      writer.write(methodReference.getName());
      writer.write(40);
      Iterator var3 = methodReference.getParameterTypes().iterator();

      while(var3.hasNext()) {
         CharSequence paramType = (CharSequence)var3.next();
         writer.write(paramType.toString());
      }

      writer.write(41);
      writer.write(methodReference.getReturnType());
   }

   public static String getFieldDescriptor(FieldReference fieldReference) {
      return getFieldDescriptor(fieldReference, false);
   }

   public static String getFieldDescriptor(FieldReference fieldReference, boolean useImplicitReference) {
      StringBuilder sb = new StringBuilder();
      if (!useImplicitReference) {
         sb.append(fieldReference.getDefiningClass());
         sb.append("->");
      }

      sb.append(fieldReference.getName());
      sb.append(':');
      sb.append(fieldReference.getType());
      return sb.toString();
   }

   public static String getShortFieldDescriptor(FieldReference fieldReference) {
      StringBuilder sb = new StringBuilder();
      sb.append(fieldReference.getName());
      sb.append(':');
      sb.append(fieldReference.getType());
      return sb.toString();
   }

   public static void writeFieldDescriptor(Writer writer, FieldReference fieldReference) throws IOException {
      writeFieldDescriptor(writer, fieldReference, false);
   }

   public static void writeFieldDescriptor(Writer writer, FieldReference fieldReference, boolean implicitReference) throws IOException {
      if (!implicitReference) {
         writer.write(fieldReference.getDefiningClass());
         writer.write("->");
      }

      writer.write(fieldReference.getName());
      writer.write(58);
      writer.write(fieldReference.getType());
   }

   @Nullable
   public static String getReferenceString(@Nonnull Reference reference) {
      return getReferenceString(reference, (String)null);
   }

   @Nullable
   public static String getReferenceString(@Nonnull Reference reference, @Nullable String containingClass) {
      if (reference instanceof StringReference) {
         return String.format("\"%s\"", StringUtils.escapeString(((StringReference)reference).getString()));
      } else if (reference instanceof TypeReference) {
         return ((TypeReference)reference).getType();
      } else {
         boolean useImplicitReference;
         if (reference instanceof FieldReference) {
            FieldReference fieldReference = (FieldReference)reference;
            useImplicitReference = fieldReference.getDefiningClass().equals(containingClass);
            return getFieldDescriptor(fieldReference, useImplicitReference);
         } else if (reference instanceof MethodReference) {
            MethodReference methodReference = (MethodReference)reference;
            useImplicitReference = methodReference.getDefiningClass().equals(containingClass);
            return getMethodDescriptor(methodReference, useImplicitReference);
         } else if (reference instanceof MethodProtoReference) {
            MethodProtoReference methodProtoReference = (MethodProtoReference)reference;
            return getMethodProtoDescriptor(methodProtoReference);
         } else {
            return null;
         }
      }
   }

   private ReferenceUtil() {
   }
}
