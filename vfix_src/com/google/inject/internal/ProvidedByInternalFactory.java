package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.spi.Dependency;

class ProvidedByInternalFactory<T> extends ProviderInternalFactory<T> implements DelayedInitialize {
   private final Class<?> rawType;
   private final Class<? extends Provider<?>> providerType;
   private final Key<? extends Provider<T>> providerKey;
   private BindingImpl<? extends Provider<T>> providerBinding;
   private ProvisionListenerStackCallback<T> provisionCallback;

   ProvidedByInternalFactory(Class<?> rawType, Class<? extends Provider<?>> providerType, Key<? extends Provider<T>> providerKey) {
      super(providerKey);
      this.rawType = rawType;
      this.providerType = providerType;
      this.providerKey = providerKey;
   }

   void setProvisionListenerCallback(ProvisionListenerStackCallback<T> listener) {
      this.provisionCallback = listener;
   }

   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
      this.providerBinding = injector.getBindingOrThrow(this.providerKey, errors, InjectorImpl.JitLimitation.NEW_OR_EXISTING_JIT);
   }

   public T get(Errors errors, InternalContext context, Dependency dependency, boolean linked) throws ErrorsException {
      Preconditions.checkState(this.providerBinding != null, "not initialized");
      context.pushState(this.providerKey, this.providerBinding.getSource());

      Object var6;
      try {
         errors = errors.withSource(this.providerKey);
         Provider<? extends T> provider = (Provider)this.providerBinding.getInternalFactory().get(errors, context, dependency, true);
         var6 = this.circularGet(provider, errors, context, dependency, this.provisionCallback);
      } finally {
         context.popState();
      }

      return var6;
   }

   protected T provision(javax.inject.Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
      try {
         Object o = super.provision(provider, errors, dependency, constructionContext);
         if (o != null && !this.rawType.isInstance(o)) {
            throw errors.subtypeNotProvided(this.providerType, this.rawType).toException();
         } else {
            return o;
         }
      } catch (RuntimeException var7) {
         throw errors.errorInProvider(var7).toException();
      }
   }
}
