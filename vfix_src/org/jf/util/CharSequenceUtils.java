package org.jf.util;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import java.util.List;

public class CharSequenceUtils {
   private static final Function<Object, String> TO_STRING = Functions.toStringFunction();

   public static int listHashCode(List<? extends CharSequence> list) {
      return Lists.transform(list, TO_STRING).hashCode();
   }

   public static boolean listEquals(List<? extends CharSequence> list1, List<? extends CharSequence> list2) {
      return Lists.transform(list1, TO_STRING).equals(Lists.transform(list2, TO_STRING));
   }
}
