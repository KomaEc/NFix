package org.jf.dexlib2.util;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.util.CharSequenceUtils;

public final class MethodUtil {
   private static int directMask;
   public static Predicate<Method> METHOD_IS_DIRECT;
   public static Predicate<Method> METHOD_IS_VIRTUAL;

   public static boolean isDirect(@Nonnull Method method) {
      return (method.getAccessFlags() & directMask) != 0;
   }

   public static boolean isStatic(@Nonnull Method method) {
      return AccessFlags.STATIC.isSet(method.getAccessFlags());
   }

   public static boolean isConstructor(@Nonnull MethodReference methodReference) {
      return methodReference.getName().equals("<init>");
   }

   public static boolean isPackagePrivate(@Nonnull Method method) {
      return (method.getAccessFlags() & (AccessFlags.PRIVATE.getValue() | AccessFlags.PROTECTED.getValue() | AccessFlags.PUBLIC.getValue())) == 0;
   }

   public static int getParameterRegisterCount(@Nonnull Method method) {
      return getParameterRegisterCount((MethodReference)method, isStatic(method));
   }

   public static int getParameterRegisterCount(@Nonnull MethodReference methodRef, boolean isStatic) {
      return getParameterRegisterCount((Collection)methodRef.getParameterTypes(), isStatic);
   }

   public static int getParameterRegisterCount(@Nonnull Collection<? extends CharSequence> parameterTypes, boolean isStatic) {
      int regCount = 0;
      Iterator var3 = parameterTypes.iterator();

      while(true) {
         while(var3.hasNext()) {
            CharSequence paramType = (CharSequence)var3.next();
            int firstChar = paramType.charAt(0);
            if (firstChar != 'J' && firstChar != 'D') {
               ++regCount;
            } else {
               regCount += 2;
            }
         }

         if (!isStatic) {
            ++regCount;
         }

         return regCount;
      }
   }

   private static char getShortyType(CharSequence type) {
      return type.length() > 1 ? 'L' : type.charAt(0);
   }

   public static String getShorty(Collection<? extends CharSequence> params, String returnType) {
      StringBuilder sb = new StringBuilder(params.size() + 1);
      sb.append(getShortyType(returnType));
      Iterator var3 = params.iterator();

      while(var3.hasNext()) {
         CharSequence typeRef = (CharSequence)var3.next();
         sb.append(getShortyType(typeRef));
      }

      return sb.toString();
   }

   public static boolean methodSignaturesMatch(@Nonnull MethodReference a, @Nonnull MethodReference b) {
      return a.getName().equals(b.getName()) && a.getReturnType().equals(b.getReturnType()) && CharSequenceUtils.listEquals(a.getParameterTypes(), b.getParameterTypes());
   }

   private MethodUtil() {
   }

   static {
      directMask = AccessFlags.STATIC.getValue() | AccessFlags.PRIVATE.getValue() | AccessFlags.CONSTRUCTOR.getValue();
      METHOD_IS_DIRECT = new Predicate<Method>() {
         public boolean apply(@Nullable Method input) {
            return input != null && MethodUtil.isDirect(input);
         }
      };
      METHOD_IS_VIRTUAL = new Predicate<Method>() {
         public boolean apply(@Nullable Method input) {
            return input != null && !MethodUtil.isDirect(input);
         }
      };
   }
}
