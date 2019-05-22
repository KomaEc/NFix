package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.inject.spi.Dependency;

final class ConstantFactory<T> implements InternalFactory<T> {
   private final Initializable<T> initializable;

   public ConstantFactory(Initializable<T> initializable) {
      this.initializable = initializable;
   }

   public T get(Errors errors, InternalContext context, Dependency dependency, boolean linked) throws ErrorsException {
      return this.initializable.get(errors);
   }

   public String toString() {
      return Objects.toStringHelper(ConstantFactory.class).add("value", this.initializable).toString();
   }
}
