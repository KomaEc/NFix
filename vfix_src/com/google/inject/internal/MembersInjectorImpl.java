package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.InjectionPoint;
import java.util.Iterator;

final class MembersInjectorImpl<T> implements MembersInjector<T> {
   private final TypeLiteral<T> typeLiteral;
   private final InjectorImpl injector;
   private final ImmutableList<SingleMemberInjector> memberInjectors;
   private final ImmutableSet<MembersInjector<? super T>> userMembersInjectors;
   private final ImmutableSet<InjectionListener<? super T>> injectionListeners;

   MembersInjectorImpl(InjectorImpl injector, TypeLiteral<T> typeLiteral, EncounterImpl<T> encounter, ImmutableList<SingleMemberInjector> memberInjectors) {
      this.injector = injector;
      this.typeLiteral = typeLiteral;
      this.memberInjectors = memberInjectors;
      this.userMembersInjectors = encounter.getMembersInjectors();
      this.injectionListeners = encounter.getInjectionListeners();
   }

   public ImmutableList<SingleMemberInjector> getMemberInjectors() {
      return this.memberInjectors;
   }

   public void injectMembers(T instance) {
      Errors errors = new Errors(this.typeLiteral);

      try {
         this.injectAndNotify(instance, errors, (Key)null, (ProvisionListenerStackCallback)null, this.typeLiteral, false);
      } catch (ErrorsException var4) {
         errors.merge(var4.getErrors());
      }

      errors.throwProvisionExceptionIfErrorsExist();
   }

   void injectAndNotify(final T instance, final Errors errors, final Key<T> key, final ProvisionListenerStackCallback<T> provisionCallback, final Object source, final boolean toolableOnly) throws ErrorsException {
      if (instance != null) {
         this.injector.callInContext(new ContextualCallable<Void>() {
            public Void call(final InternalContext context) throws ErrorsException {
               context.pushState(key, source);

               try {
                  if (provisionCallback != null && provisionCallback.hasListeners()) {
                     provisionCallback.provision(errors, context, new ProvisionListenerStackCallback.ProvisionCallback<T>() {
                        public T call() {
                           MembersInjectorImpl.this.injectMembers(instance, errors, context, toolableOnly);
                           return instance;
                        }
                     });
                  } else {
                     MembersInjectorImpl.this.injectMembers(instance, errors, context, toolableOnly);
                  }
               } finally {
                  context.popState();
               }

               return null;
            }
         });
         if (!toolableOnly) {
            this.notifyListeners(instance, errors);
         }

      }
   }

   void notifyListeners(T instance, Errors errors) throws ErrorsException {
      int numErrorsBefore = errors.size();
      Iterator i$ = this.injectionListeners.iterator();

      while(i$.hasNext()) {
         InjectionListener injectionListener = (InjectionListener)i$.next();

         try {
            injectionListener.afterInjection(instance);
         } catch (RuntimeException var7) {
            errors.errorNotifyingInjectionListener(injectionListener, this.typeLiteral, var7);
         }
      }

      errors.throwIfNewErrors(numErrorsBefore);
   }

   void injectMembers(T t, Errors errors, InternalContext context, boolean toolableOnly) {
      int i = 0;

      for(int size = this.memberInjectors.size(); i < size; ++i) {
         SingleMemberInjector injector = (SingleMemberInjector)this.memberInjectors.get(i);
         if (!toolableOnly || injector.getInjectionPoint().isToolable()) {
            injector.inject(errors, context, t);
         }
      }

      if (!toolableOnly) {
         Iterator i$ = this.userMembersInjectors.iterator();

         while(i$.hasNext()) {
            MembersInjector userMembersInjector = (MembersInjector)i$.next();

            try {
               userMembersInjector.injectMembers(t);
            } catch (RuntimeException var8) {
               errors.errorInUserInjector(userMembersInjector, this.typeLiteral, var8);
            }
         }
      }

   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(this.typeLiteral));
      return (new StringBuilder(17 + var1.length())).append("MembersInjector<").append(var1).append(">").toString();
   }

   public ImmutableSet<InjectionPoint> getInjectionPoints() {
      ImmutableSet.Builder<InjectionPoint> builder = ImmutableSet.builder();
      Iterator i$ = this.memberInjectors.iterator();

      while(i$.hasNext()) {
         SingleMemberInjector memberInjector = (SingleMemberInjector)i$.next();
         builder.add((Object)memberInjector.getInjectionPoint());
      }

      return builder.build();
   }
}
