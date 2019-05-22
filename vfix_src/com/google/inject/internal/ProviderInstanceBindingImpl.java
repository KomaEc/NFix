package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderWithExtensionVisitor;
import com.google.inject.util.Providers;
import java.util.Collection;
import java.util.Set;
import javax.inject.Provider;

final class ProviderInstanceBindingImpl<T> extends BindingImpl<T> implements ProviderInstanceBinding<T> {
   final Provider<? extends T> providerInstance;
   final ImmutableSet<InjectionPoint> injectionPoints;

   public ProviderInstanceBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Provider<? extends T> providerInstance, Set<InjectionPoint> injectionPoints) {
      super(injector, key, source, internalFactory, scoping);
      this.providerInstance = providerInstance;
      this.injectionPoints = ImmutableSet.copyOf((Collection)injectionPoints);
   }

   public ProviderInstanceBindingImpl(Object source, Key<T> key, Scoping scoping, Set<InjectionPoint> injectionPoints, Provider<? extends T> providerInstance) {
      super(source, key, scoping);
      this.injectionPoints = ImmutableSet.copyOf((Collection)injectionPoints);
      this.providerInstance = providerInstance;
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      return this.providerInstance instanceof ProviderWithExtensionVisitor ? ((ProviderWithExtensionVisitor)this.providerInstance).acceptExtensionVisitor(visitor, this) : visitor.visit((ProviderInstanceBinding)this);
   }

   public com.google.inject.Provider<? extends T> getProviderInstance() {
      return Providers.guicify(this.providerInstance);
   }

   public Provider<? extends T> getUserSuppliedProvider() {
      return this.providerInstance;
   }

   public Set<InjectionPoint> getInjectionPoints() {
      return this.injectionPoints;
   }

   public Set<Dependency<?>> getDependencies() {
      return (Set)(this.providerInstance instanceof HasDependencies ? ImmutableSet.copyOf((Collection)((HasDependencies)this.providerInstance).getDependencies()) : Dependency.forInjectionPoints(this.injectionPoints));
   }

   public BindingImpl<T> withScoping(Scoping scoping) {
      return new ProviderInstanceBindingImpl(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.providerInstance);
   }

   public BindingImpl<T> withKey(Key<T> key) {
      return new ProviderInstanceBindingImpl(this.getSource(), key, this.getScoping(), this.injectionPoints, this.providerInstance);
   }

   public void applyTo(Binder binder) {
      this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getUserSuppliedProvider()));
   }

   public String toString() {
      return Objects.toStringHelper(ProviderInstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerInstance).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ProviderInstanceBindingImpl)) {
         return false;
      } else {
         ProviderInstanceBindingImpl<?> o = (ProviderInstanceBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.providerInstance, o.providerInstance);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping());
   }
}
