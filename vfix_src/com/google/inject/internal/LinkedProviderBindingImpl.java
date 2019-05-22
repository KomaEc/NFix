package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.ProviderKeyBinding;
import java.util.Set;
import javax.inject.Provider;

final class LinkedProviderBindingImpl<T> extends BindingImpl<T> implements ProviderKeyBinding<T>, HasDependencies, DelayedInitialize {
   final Key<? extends Provider<? extends T>> providerKey;
   final DelayedInitialize delayedInitializer;

   private LinkedProviderBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey, DelayedInitialize delayedInitializer) {
      super(injector, key, source, internalFactory, scoping);
      this.providerKey = providerKey;
      this.delayedInitializer = delayedInitializer;
   }

   public LinkedProviderBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey) {
      this(injector, key, source, internalFactory, scoping, providerKey, (DelayedInitialize)null);
   }

   LinkedProviderBindingImpl(Object source, Key<T> key, Scoping scoping, Key<? extends Provider<? extends T>> providerKey) {
      super(source, key, scoping);
      this.providerKey = providerKey;
      this.delayedInitializer = null;
   }

   static <T> LinkedProviderBindingImpl<T> createWithInitializer(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends Provider<? extends T>> providerKey, DelayedInitialize delayedInitializer) {
      return new LinkedProviderBindingImpl(injector, key, source, internalFactory, scoping, providerKey, delayedInitializer);
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      return visitor.visit((ProviderKeyBinding)this);
   }

   public Key<? extends Provider<? extends T>> getProviderKey() {
      return this.providerKey;
   }

   public void initialize(InjectorImpl injector, Errors errors) throws ErrorsException {
      if (this.delayedInitializer != null) {
         this.delayedInitializer.initialize(injector, errors);
      }

   }

   public Set<Dependency<?>> getDependencies() {
      return ImmutableSet.of(Dependency.get(this.providerKey));
   }

   public BindingImpl<T> withScoping(Scoping scoping) {
      return new LinkedProviderBindingImpl(this.getSource(), this.getKey(), scoping, this.providerKey);
   }

   public BindingImpl<T> withKey(Key<T> key) {
      return new LinkedProviderBindingImpl(this.getSource(), key, this.getScoping(), this.providerKey);
   }

   public void applyTo(Binder binder) {
      this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).toProvider(this.getProviderKey()));
   }

   public String toString() {
      return Objects.toStringHelper(ProviderKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("provider", this.providerKey).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof LinkedProviderBindingImpl)) {
         return false;
      } else {
         LinkedProviderBindingImpl<?> o = (LinkedProviderBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.providerKey, o.providerKey);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping(), this.providerKey);
   }
}
