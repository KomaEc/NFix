package com.google.inject.internal;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.spi.Dependency;

final class ProviderToInternalFactoryAdapter<T> implements Provider<T> {
   private final InjectorImpl injector;
   private final InternalFactory<? extends T> internalFactory;

   public ProviderToInternalFactoryAdapter(InjectorImpl injector, InternalFactory<? extends T> internalFactory) {
      this.injector = injector;
      this.internalFactory = internalFactory;
   }

   public T get() {
      final Errors errors = new Errors();

      try {
         T t = this.injector.callInContext(new ContextualCallable<T>() {
            public T call(InternalContext context) throws ErrorsException {
               Dependency dependency = context.getDependency();
               return ProviderToInternalFactoryAdapter.this.internalFactory.get(errors, context, dependency, true);
            }
         });
         errors.throwIfNewErrors(0);
         return t;
      } catch (ErrorsException var3) {
         throw new ProvisionException(errors.merge(var3.getErrors()).getMessages());
      }
   }

   public String toString() {
      return this.internalFactory.toString();
   }
}
