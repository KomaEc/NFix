package com.google.inject.internal;

import com.google.common.base.Objects;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.UntargettedBinding;

final class UntargettedBindingImpl<T> extends BindingImpl<T> implements UntargettedBinding<T> {
   UntargettedBindingImpl(InjectorImpl injector, Key<T> key, Object source) {
      super(injector, key, source, new InternalFactory<T>() {
         public T get(Errors errors, InternalContext context, Dependency<?> dependency, boolean linked) {
            throw new AssertionError();
         }
      }, Scoping.UNSCOPED);
   }

   public UntargettedBindingImpl(Object source, Key<T> key, Scoping scoping) {
      super(source, key, scoping);
   }

   public <V> V acceptTargetVisitor(BindingTargetVisitor<? super T, V> visitor) {
      return visitor.visit((UntargettedBinding)this);
   }

   public BindingImpl<T> withScoping(Scoping scoping) {
      return new UntargettedBindingImpl(this.getSource(), this.getKey(), scoping);
   }

   public BindingImpl<T> withKey(Key<T> key) {
      return new UntargettedBindingImpl(this.getSource(), key, this.getScoping());
   }

   public void applyTo(Binder binder) {
      this.getScoping().applyTo(binder.withSource(this.getSource()).bind(this.getKey()));
   }

   public String toString() {
      return Objects.toStringHelper(UntargettedBinding.class).add("key", this.getKey()).add("source", this.getSource()).toString();
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof UntargettedBindingImpl)) {
         return false;
      } else {
         UntargettedBindingImpl<?> o = (UntargettedBindingImpl)obj;
         return this.getKey().equals(o.getKey()) && this.getScoping().equals(o.getScoping());
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.getKey(), this.getScoping());
   }
}
