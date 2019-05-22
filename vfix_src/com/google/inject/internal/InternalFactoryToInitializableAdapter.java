package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

final class InternalFactoryToInitializableAdapter<T> extends ProviderInternalFactory<T> {
   private final ProvisionListenerStackCallback<T> provisionCallback;
   private final Initializable<? extends Provider<? extends T>> initializable;

   public InternalFactoryToInitializableAdapter(Initializable<? extends Provider<? extends T>> initializable, Object source, ProvisionListenerStackCallback<T> provisionCallback) {
      super(source);
      this.provisionCallback = (ProvisionListenerStackCallback)Preconditions.checkNotNull(provisionCallback, "provisionCallback");
      this.initializable = (Initializable)Preconditions.checkNotNull(initializable, "provider");
   }

   public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
      return this.circularGet((Provider)this.initializable.get(errors), errors, context, dependency, this.provisionCallback);
   }

   protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
      try {
         return super.provision(provider, errors, dependency, constructionContext);
      } catch (RuntimeException var6) {
         throw errors.withSource(this.source).errorInProvider(var6).toException();
      }
   }

   public String toString() {
      return this.initializable.toString();
   }
}
