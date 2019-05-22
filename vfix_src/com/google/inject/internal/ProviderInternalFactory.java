package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.inject.spi.Dependency;
import javax.inject.Provider;

abstract class ProviderInternalFactory<T> implements InternalFactory<T> {
   protected final Object source;

   ProviderInternalFactory(Object source) {
      this.source = Preconditions.checkNotNull(source, "source");
   }

   protected T circularGet(final Provider<? extends T> provider, final Errors errors, InternalContext context, final Dependency<?> dependency, ProvisionListenerStackCallback<T> provisionCallback) throws ErrorsException {
      final ConstructionContext<T> constructionContext = context.getConstructionContext(this);
      if (constructionContext.isConstructing()) {
         Class<?> expectedType = dependency.getKey().getTypeLiteral().getRawType();
         T proxyType = constructionContext.createProxy(errors, context.getInjectorOptions(), expectedType);
         return proxyType;
      } else {
         constructionContext.startConstruction();

         Object var7;
         try {
            if (provisionCallback.hasListeners()) {
               var7 = provisionCallback.provision(errors, context, new ProvisionListenerStackCallback.ProvisionCallback<T>() {
                  public T call() throws ErrorsException {
                     return ProviderInternalFactory.this.provision(provider, errors, dependency, constructionContext);
                  }
               });
               return var7;
            }

            var7 = this.provision(provider, errors, dependency, constructionContext);
         } finally {
            constructionContext.removeCurrentReference();
            constructionContext.finishConstruction();
         }

         return var7;
      }
   }

   protected T provision(Provider<? extends T> provider, Errors errors, Dependency<?> dependency, ConstructionContext<T> constructionContext) throws ErrorsException {
      T t = errors.checkForNull(provider.get(), this.source, dependency);
      constructionContext.setProxyDelegates(t);
      return t;
   }
}
