package com.google.inject.util;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderWithDependencies;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public final class Providers {
   private Providers() {
   }

   public static <T> Provider<T> of(T instance) {
      return new Providers.ConstantProvider(instance);
   }

   public static <T> Provider<T> guicify(javax.inject.Provider<T> provider) {
      if (provider instanceof Provider) {
         return (Provider)provider;
      } else {
         javax.inject.Provider<T> delegate = (javax.inject.Provider)Preconditions.checkNotNull(provider, "provider");
         Set<InjectionPoint> injectionPoints = InjectionPoint.forInstanceMethodsAndFields(provider.getClass());
         if (injectionPoints.isEmpty()) {
            return new Providers.GuicifiedProvider(delegate);
         } else {
            Set<Dependency<?>> mutableDeps = Sets.newHashSet();
            Iterator i$ = injectionPoints.iterator();

            while(i$.hasNext()) {
               InjectionPoint ip = (InjectionPoint)i$.next();
               mutableDeps.addAll(ip.getDependencies());
            }

            Set<Dependency<?>> dependencies = ImmutableSet.copyOf((Collection)mutableDeps);
            return new Providers.GuicifiedProviderWithDependencies(dependencies, delegate);
         }
      }
   }

   private static final class GuicifiedProviderWithDependencies<T> extends Providers.GuicifiedProvider<T> implements ProviderWithDependencies<T> {
      private final Set<Dependency<?>> dependencies;

      private GuicifiedProviderWithDependencies(Set<Dependency<?>> dependencies, javax.inject.Provider<T> delegate) {
         super(delegate, null);
         this.dependencies = dependencies;
      }

      @Inject
      void initialize(Injector injector) {
         injector.injectMembers(this.delegate);
      }

      public Set<Dependency<?>> getDependencies() {
         return this.dependencies;
      }

      // $FF: synthetic method
      GuicifiedProviderWithDependencies(Set x0, javax.inject.Provider x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class GuicifiedProvider<T> implements Provider<T> {
      protected final javax.inject.Provider<T> delegate;

      private GuicifiedProvider(javax.inject.Provider<T> delegate) {
         this.delegate = delegate;
      }

      public T get() {
         return this.delegate.get();
      }

      public String toString() {
         String var1 = String.valueOf(String.valueOf(this.delegate));
         return (new StringBuilder(11 + var1.length())).append("guicified(").append(var1).append(")").toString();
      }

      public boolean equals(Object obj) {
         return obj instanceof Providers.GuicifiedProvider && Objects.equal(this.delegate, ((Providers.GuicifiedProvider)obj).delegate);
      }

      public int hashCode() {
         return Objects.hashCode(this.delegate);
      }

      // $FF: synthetic method
      GuicifiedProvider(javax.inject.Provider x0, Object x1) {
         this(x0);
      }
   }

   private static final class ConstantProvider<T> implements Provider<T> {
      private final T instance;

      private ConstantProvider(T instance) {
         this.instance = instance;
      }

      public T get() {
         return this.instance;
      }

      public String toString() {
         String var1 = String.valueOf(String.valueOf(this.instance));
         return (new StringBuilder(4 + var1.length())).append("of(").append(var1).append(")").toString();
      }

      public boolean equals(Object obj) {
         return obj instanceof Providers.ConstantProvider && Objects.equal(this.instance, ((Providers.ConstantProvider)obj).instance);
      }

      public int hashCode() {
         return Objects.hashCode(this.instance);
      }

      // $FF: synthetic method
      ConstantProvider(Object x0, Object x1) {
         this(x0);
      }
   }
}
