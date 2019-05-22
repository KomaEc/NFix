package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.HasDependencies;
import com.google.inject.spi.LinkedKeyBinding;
import java.util.Set;

public final class LinkedBindingImpl<T> extends BindingImpl<T> implements LinkedKeyBinding<T>, HasDependencies {
   final Key<? extends T> targetKey;

   public LinkedBindingImpl(InjectorImpl injector, Key<T> key, Object source, InternalFactory<? extends T> internalFactory, Scoping scoping, Key<? extends T> targetKey) {
      super(injector, key, source, internalFactory, scoping);
      this.targetKey = targetKey;
   }

   public LinkedBindingImpl(Object source, Key<T> key, Scoping scoping, Key<? extends T> targetKey) {
      super(source, key, scoping);
      this.targetKey = targetKey;
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      return visitor.visit((LinkedKeyBinding)this);
   }

   public Key<? extends T> getLinkedKey() {
      return this.targetKey;
   }

   public Set<Dependency<?>> getDependencies() {
      return ImmutableSet.of(Dependency.get(this.targetKey));
   }

   public BindingImpl<T> withScoping(Scoping scoping) {
      return new LinkedBindingImpl(this.getSource(), this.getKey(), scoping, this.targetKey);
   }

   public BindingImpl<T> withKey(Key<T> key) {
      return new LinkedBindingImpl(this.getSource(), key, this.getScoping(), this.targetKey);
   }

   public void applyTo(Binder binder) {
      this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()).to(this.getLinkedKey()));
   }

   public String toString() {
      return Objects.toStringHelper(LinkedKeyBinding.class).add("key", this.getKey()).add("source", this.getSource()).add("scope", this.getScoping()).add("target", this.targetKey).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof LinkedBindingImpl)) {
         return false;
      } else {
         LinkedBindingImpl<?> o = (LinkedBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping()) && Objects.equal(this.targetKey, o.targetKey);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping(), this.targetKey);
   }
}
