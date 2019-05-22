package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.inject.Key;
import com.google.inject.spi.Dependency;

final class FactoryProxy<T> implements InternalFactory<T>, CreationListener {
   private final InjectorImpl injector;
   private final Key<T> key;
   private final Key<? extends T> targetKey;
   private final Object source;
   private InternalFactory<? extends T> targetFactory;

   FactoryProxy(InjectorImpl injector, Key<T> key, Key<? extends T> targetKey, Object source) {
      this.injector = injector;
      this.key = key;
      this.targetKey = targetKey;
      this.source = source;
   }

   public void notify(Errors errors) {
      try {
         this.targetFactory = this.injector.getInternalFactory(this.targetKey, errors.withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
      } catch (ErrorsException var3) {
         errors.merge(var3.getErrors());
      }

   }

   public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
      context.pushState(this.targetKey, this.source);

      Object var5;
      try {
         var5 = this.targetFactory.get(errors.withSource(this.targetKey), context, dependency, true);
      } finally {
         context.popState();
      }

      return var5;
   }

   public String toString() {
      return Objects.toStringHelper(FactoryProxy.class).add("key", this.key).add("provider", this.targetFactory).toString();
   }
}
