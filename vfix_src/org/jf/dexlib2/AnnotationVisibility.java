package org.jf.dexlib2;

import org.jf.util.ExceptionWithContext;

public final class AnnotationVisibility {
   public static final int BUILD = 0;
   public static final int RUNTIME = 1;
   public static final int SYSTEM = 2;
   private static String[] NAMES = new String[]{"build", "runtime", "system"};

   public static String getVisibility(int visibility) {
      if (visibility >= 0 && visibility < NAMES.length) {
         return NAMES[visibility];
      } else {
         throw new ExceptionWithContext("Invalid annotation visibility %d", new Object[]{visibility});
      }
   }

   public static int getVisibility(String visibility) {
      visibility = visibility.toLowerCase();
      if (visibility.equals("build")) {
         return 0;
      } else if (visibility.equals("runtime")) {
         return 1;
      } else if (visibility.equals("system")) {
         return 2;
      } else {
         throw new ExceptionWithContext("Invalid annotation visibility: %s", new Object[]{visibility});
      }
   }

   private AnnotationVisibility() {
   }
}
