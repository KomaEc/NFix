package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

final class BoundProviderFactory<T> extends ProviderInternalFactory<T> implements CreationListener {
   private final ProvisionListenerStackCallback<T> provisionCallback;
   private final InjectorImpl injector;
   final Key<? extends Provider<? extends T>> providerKey;
   private InternalFactory<? extends Provider<? extends T>> providerFactory;

   BoundProviderFactory(InjectorImpl injector, Key<? extends Provider<? extends T>> providerKey, Object source, ProvisionListenerStackCallback<T> provisionCallback) {
      super(source);
      this.provisionCallback = (ProvisionListenerStackCallback)Preconditions.checkNotNull(provisionCallback, "provisionCallback");
      this.injector = injector;
      this.providerKey = providerKey;
   }

   public void notify(Errors errors) {
      try {
         this.providerFactory = this.injector.getInternalFactory(this.providerKey, errors.withSource(this.source), InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
      } catch (ErrorsException var3) {
         errors.merge(var3.getErrors());
      }

   }

   public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) throws ErrorsException {
      context.pushState(this.providerKey, this.source);

      Object var6;
      try {
         errors = errors.withSource(this.providerKey);
         Provider<? extends T> provider = (Provider)this.providerFactory.get(errors, context, dependency, true);
         var6 = this.circularGet(provider, errors, context, dependency, this.provisionCallback);
      } finally {
         context.popState();
      }

      return var6;
   }

   protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
      try {
         return super.provision(provider, errors, dependency, constructionContext);
      } catch (RuntimeException var6) {
         throw errors.errorInProvider(var6).toException();
      }
   }

   public String toString() {
      return this.providerKey.toString();
   }
}
