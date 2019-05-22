package com.google.inject.spi;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;

public final class MembersInjectorLookup<T> implements Element {
   private final Object source;
   private final TypeLiteral<T> type;
   private MembersInjector<T> delegate;

   public MembersInjectorLookup(Object source, TypeLiteral<T> type) {
      this.source = Preconditions.checkNotNull(source, "source");
      this.type = (TypeLiteral)Preconditions.checkNotNull(type, "type");
   }

   public Object getSource() {
      return this.source;
   }

   public TypeLiteral<T> getType() {
      return this.type;
   }

   public <T> T acceptVisitor(ElementVisitor<T> visitor) {
      return visitor.visit(this);
   }

   public void initializeDelegate(MembersInjector<T> delegate) {
      Preconditions.checkState(this.delegate == null, "delegate already initialized");
      this.delegate = (MembersInjector)Preconditions.checkNotNull(delegate, "delegate");
   }

   public void applyTo(Binder binder) {
      this.initializeDelegate(binder.withSource(this.getSource()).getMembersInjector(this.type));
   }

   public MembersInjector<T> getDelegate() {
      return this.delegate;
   }

   public MembersInjector<T> getMembersInjector() {
      return new MembersInjector<T>() {
         public void injectMembers(T instance) {
            Preconditions.checkState(MembersInjectorLookup.this.delegate != null, "This MembersInjector cannot be used until the Injector has been created.");
            MembersInjectorLookup.this.delegate.injectMembers(instance);
         }

         public String toString() {
            String var1 = String.valueOf(String.valueOf(MembersInjectorLookup.this.type));
            return (new StringBuilder(17 + var1.length())).append("MembersInjector<").append(var1).append(">").toString();
         }
      };
   }
}
