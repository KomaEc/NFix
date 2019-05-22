package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.google.inject.spi.Dependency;

final class InternalFactoryToProviderAdapter<T> implements InternalFactory<T> {
   private final Provider<? extends T> provider;
   private final Object source;

   public InternalFactoryToProviderAdapter(Provider<? extends T> provider, Object source) {
      this.provider = (Provider)Preconditions.checkNotNull(provider, "provider");
      this.source = Preconditions.checkNotNull(source, "source");
   }

   public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
      try {
         return errors.checkForNull(this.provider.get(), this.source, dependency);
      } catch (RuntimeException var6) {
         throw errors.withSource(this.source).errorInProvider(var6).toException();
      }
   }

   public String toString() {
      return this.provider.toString();
   }
}
