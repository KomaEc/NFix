package com.google.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

final class ConstructorInjector<T> {
   private final ImmutableSet<InjectionPoint> injectableMembers;
   private final SingleParameterInjector<?>[] parameterInjectors;
   private final ConstructionProxy<T> constructionProxy;
   private final MembersInjectorImpl<T> membersInjector;

   ConstructorInjector(Set<InjectionPoint> injectableMembers, ConstructionProxy<T> constructionProxy, SingleParameterInjector<?>[] parameterInjectors, MembersInjectorImpl<T> membersInjector) {
      this.injectableMembers = ImmutableSet.copyOf((Collection)injectableMembers);
      this.constructionProxy = constructionProxy;
      this.parameterInjectors = parameterInjectors;
      this.membersInjector = membersInjector;
   }

   public ImmutableSet<InjectionPoint> getInjectableMembers() {
      return this.injectableMembers;
   }

   ConstructionProxy<T> getConstructionProxy() {
      return this.constructionProxy;
   }

   Object construct(final Errors errors, final InternalContext context, Class<?> expectedType, ProvisionListenerStackCallback<T> provisionCallback) throws ErrorsException {
      final ConstructionContext<T> constructionContext = context.getConstructionContext(this);
      if (constructionContext.isConstructing()) {
         return constructionContext.createProxy(errors, context.getInjectorOptions(), expectedType);
      } else {
         T t = constructionContext.getCurrentReference();
         if (t != null) {
            return t;
         } else {
            constructionContext.startConstruction();

            Object var7;
            try {
               if (provisionCallback.hasListeners()) {
                  var7 = provisionCallback.provision(errors, context, new ProvisionListenerStackCallback.ProvisionCallback<T>() {
                     public T call() throws ErrorsException {
                        return ConstructorInjector.this.provision(errors, context, constructionContext);
                     }
                  });
                  return var7;
               }

               var7 = this.provision(errors, context, constructionContext);
            } finally {
               constructionContext.finishConstruction();
            }

            return var7;
         }
      }
   }

   private T provision(Errors errors, InternalContext context, ConstructionContext<T> constructionContext) throws ErrorsException {
      Object cause;
      try {
         Object t;
         try {
            Object[] parameters = SingleParameterInjector.getAll(errors, context, this.parameterInjectors);
            t = this.constructionProxy.newInstance(parameters);
            constructionContext.setProxyDelegates(t);
         } finally {
            constructionContext.finishConstruction();
         }

         constructionContext.setCurrentReference(t);
         this.membersInjector.injectMembers(t, errors, context, false);
         this.membersInjector.notifyListeners(t, errors);
         cause = t;
      } catch (InvocationTargetException var15) {
         cause = var15.getCause() != null ? var15.getCause() : var15;
         throw errors.withSource(this.constructionProxy.getInjectionPoint()).errorInjectingConstructor((Throwable)cause).toException();
      } finally {
         constructionContext.removeCurrentReference();
      }

      return cause;
   }
}
