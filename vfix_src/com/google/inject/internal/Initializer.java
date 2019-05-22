package com.google.inject.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionPoint;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

final class Initializer {
   private final Thread creatingThread = Thread.currentThread();
   private final CountDownLatch ready = new CountDownLatch(1);
   private final Map<Object, MembersInjectorImpl<?>> pendingMembersInjectors = Maps.newIdentityHashMap();
   private final Map<Object, Initializer.InjectableReference<?>> pendingInjection = Maps.newIdentityHashMap();

   <T> Initializable<T> requestInjection(InjectorImpl injector, T instance, Binding<T> binding, Object source, Set<InjectionPoint> injectionPoints) {
      Preconditions.checkNotNull(source);
      ProvisionListenerStackCallback<T> provisionCallback = binding == null ? null : injector.provisionListenerStore.get(binding);
      if (instance == null || injectionPoints.isEmpty() && !injector.membersInjectorStore.hasTypeListeners() && (provisionCallback == null || !provisionCallback.hasListeners())) {
         return Initializables.of(instance);
      } else {
         Initializer.InjectableReference<T> initializable = new Initializer.InjectableReference(injector, instance, binding == null ? null : binding.getKey(), provisionCallback, source);
         this.pendingInjection.put(instance, initializable);
         return initializable;
      }
   }

   void validateOustandingInjections(Errors errors) {
      Iterator i$ = this.pendingInjection.values().iterator();

      while(i$.hasNext()) {
         Initializer.InjectableReference reference = (Initializer.InjectableReference)i$.next();

         try {
            this.pendingMembersInjectors.put(reference.instance, reference.validate(errors));
         } catch (ErrorsException var5) {
            errors.merge(var5.getErrors());
         }
      }

   }

   void injectAll(Errors errors) {
      Iterator i$ = Lists.newArrayList((Iterable)this.pendingInjection.values()).iterator();

      while(i$.hasNext()) {
         Initializer.InjectableReference reference = (Initializer.InjectableReference)i$.next();

         try {
            reference.get(errors);
         } catch (ErrorsException var5) {
            errors.merge(var5.getErrors());
         }
      }

      if (!this.pendingInjection.isEmpty()) {
         String var6 = String.valueOf(String.valueOf(this.pendingInjection));
         throw new AssertionError((new StringBuilder(18 + var6.length())).append("Failed to satisfy ").append(var6).toString());
      } else {
         this.ready.countDown();
      }
   }

   private class InjectableReference<T> implements Initializable<T> {
      private final InjectorImpl injector;
      private final T instance;
      private final Object source;
      private final Key<T> key;
      private final ProvisionListenerStackCallback<T> provisionCallback;

      public InjectableReference(InjectorImpl injector, T instance, Key<T> key, ProvisionListenerStackCallback<T> provisionCallback, Object source) {
         this.injector = injector;
         this.key = key;
         this.provisionCallback = provisionCallback;
         this.instance = Preconditions.checkNotNull(instance, "instance");
         this.source = Preconditions.checkNotNull(source, "source");
      }

      public MembersInjectorImpl<T> validate(Errors errors) throws ErrorsException {
         TypeLiteral<T> type = TypeLiteral.get(this.instance.getClass());
         return this.injector.membersInjectorStore.get(type, errors.withSource(this.source));
      }

      public T get(Errors errors) throws ErrorsException {
         if (Initializer.this.ready.getCount() == 0L) {
            return this.instance;
         } else if (Thread.currentThread() != Initializer.this.creatingThread) {
            try {
               Initializer.this.ready.await();
               return this.instance;
            } catch (InterruptedException var3) {
               throw new RuntimeException(var3);
            }
         } else {
            if (Initializer.this.pendingInjection.remove(this.instance) != null) {
               MembersInjectorImpl<T> membersInjector = (MembersInjectorImpl)Initializer.this.pendingMembersInjectors.remove(this.instance);
               Preconditions.checkState(membersInjector != null, "No membersInjector available for instance: %s, from key: %s", this.instance, this.key);
               membersInjector.injectAndNotify(this.instance, errors.withSource(this.source), this.key, this.provisionCallback, this.source, this.injector.options.stage == Stage.TOOL);
            }

            return this.instance;
         }
      }

      public String toString() {
         return this.instance.toString();
      }
   }
}
