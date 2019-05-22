package com.google.common.util.concurrent;

import com.google.common.base.Function;
import java.lang.reflect.Constructor;
import java.util.Arrays;

final class Futures$7 implements Function<Constructor<?>, Boolean> {
   public Boolean apply(Constructor<?> input) {
      return Arrays.asList(input.getParameterTypes()).contains(String.class);
   }
}
