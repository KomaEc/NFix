package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.util.Providers;
import java.util.Collection;
import java.util.Set;

final class InstanceBindingImpl<T> extends BindingImpl<T> implements InstanceBinding<T> {
   final T instance;
   final Provider<T> provider;
   final ImmutableSet<InjectionPoint> injectionPoints;

   public InstanceBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Set<InjectionPoint> injectionPoints, T instance) {
      super(injector, key, source, internalFactory, Scoping.EAGER_SINGLETON);
      this.injectionPoints = ImmutableSet.copyOf((Collection)injectionPoints);
      this.instance = instance;
      this.provider = Providers.of(instance);
   }

   public InstanceBindingImpl(Object source, Key<T> key, Scoping scoping, Set<InjectionPoint> injectionPoints, T instance) {
      super(source, key, scoping);
      this.injectionPoints = ImmutableSet.copyOf((Collection)injectionPoints);
      this.instance = instance;
      this.provider = Providers.of(instance);
   }

   public Provider<T> getProvider() {
      return this.provider;
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      return visitor.visit((InstanceBinding)this);
   }

   public T getInstance() {
      return this.instance;
   }

   public Set<InjectionPoint> getInjectionPoints() {
      return this.injectionPoints;
   }

   public Set<Dependency<?>> getDependencies() {
      return (Set)(this.instance instanceof HasDependencies ? ImmutableSet.copyOf((Collection)((HasDependencies)this.instance).getDependencies()) : Dependency.forInjectionPoints(this.injectionPoints));
   }

   public BindingImpl<T> withScoping(Scoping scoping) {
      return new InstanceBindingImpl(this.getSource(), this.getKey(), scoping, this.injectionPoints, this.instance);
   }

   public BindingImpl<T> withKey(Key<T> key) {
      return new InstanceBindingImpl(this.getSource(), key, this.getScoping(), this.injectionPoints, this.instance);
   }

   public void applyTo(Binder binder) {
      binder.withSource(this.getSource()).bind(this.getKey()).toInstance(this.instance);
   }

   public String toString() {
      return Objects.toStringHelper(InstanceBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("instance", this.instance).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof InstanceBindingImpl)) {
         return false;
      } else {
         InstanceBindingImpl<?> o = (InstanceBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.instance, o.instance);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping());
   }
}
