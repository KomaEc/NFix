package org.jf.dexlib2.util;

import com.google.common.base.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.iface.Field;

public final class FieldUtil {
   public static Predicate<Field> FIELD_IS_STATIC = new Predicate<Field>() {
      public boolean apply(@Nullable Field input) {
         return input != null && FieldUtil.isStatic(input);
      }
   };
   public static Predicate<Field> FIELD_IS_INSTANCE = new Predicate<Field>() {
      public boolean apply(@Nullable Field input) {
         return input != null && !FieldUtil.isStatic(input);
      }
   };

   public static boolean isStatic(@Nonnull Field field) {
      return AccessFlags.STATIC.isSet(field.getAccessFlags());
   }

   private FieldUtil() {
   }
}
