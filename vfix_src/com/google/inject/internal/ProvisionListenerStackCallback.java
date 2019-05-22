package com.google.inject.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.inject.Binding;
import com.google.inject.ProvisionException;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.ProvisionListener;
import java.util.List;
import java.util.Set;

final class ProvisionListenerStackCallback<T> {
   private static final ProvisionListener[] EMPTY_LISTENER = new ProvisionListener[0];
   private static final ProvisionListenerStackCallback<?> EMPTY_CALLBACK = new ProvisionListenerStackCallback((Binding)null, ImmutableList.of());
   private final ProvisionListener[] listeners;
   private final Binding<T> binding;

   public static <T> ProvisionListenerStackCallback<T> emptyListener() {
      return EMPTY_CALLBACK;
   }

   public ProvisionListenerStackCallback(Binding<T> binding, List<ProvisionListener> listeners) {
      this.binding = binding;
      if (listeners.isEmpty()) {
         this.listeners = EMPTY_LISTENER;
      } else {
         Set<ProvisionListener> deDuplicated = Sets.newLinkedHashSet(listeners);
         this.listeners = (ProvisionListener[])deDuplicated.toArray(new ProvisionListener[deDuplicated.size()]);
      }

   }

   public boolean hasListeners() {
      return this.listeners.length > 0;
   }

   public T provision(Errors errors, InternalContext context, ProvisionListenerStackCallback.ProvisionCallback<T> callable) throws ErrorsException {
      ProvisionListenerStackCallback<T>.Provision provision = new ProvisionListenerStackCallback.Provision(errors, context, callable);
      RuntimeException caught = null;

      try {
         provision.provision();
      } catch (RuntimeException var7) {
         caught = var7;
      }

      if (provision.exceptionDuringProvision != null) {
         throw provision.exceptionDuringProvision;
      } else if (caught != null) {
         Object listener = provision.erredListener != null ? provision.erredListener.getClass() : "(unknown)";
         throw errors.errorInUserCode(caught, "Error notifying ProvisionListener %s of %s.%n Reason: %s", listener, this.binding.getKey(), caught).toException();
      } else {
         return provision.result;
      }
   }

   private class Provision extends ProvisionListener.ProvisionInvocation<T> {
      final Errors errors;
      final int numErrorsBefore;
      final InternalContext context;
      final ProvisionListenerStackCallback.ProvisionCallback<T> callable;
      int index = -1;
      T result;
      ErrorsException exceptionDuringProvision;
      ProvisionListener erredListener;

      public Provision(Errors errors, InternalContext context, ProvisionListenerStackCallback.ProvisionCallback<T> callable) {
         this.callable = callable;
         this.context = context;
         this.errors = errors;
         this.numErrorsBefore = errors.size();
      }

      public T provision() {
         ++this.index;
         if (this.index == ProvisionListenerStackCallback.this.listeners.length) {
            try {
               this.result = this.callable.call();
               this.errors.throwIfNewErrors(this.numErrorsBefore);
            } catch (ErrorsException var4) {
               this.exceptionDuringProvision = var4;
               throw new ProvisionException(this.errors.merge(var4.getErrors()).getMessages());
            }
         } else {
            if (this.index >= ProvisionListenerStackCallback.this.listeners.length) {
               throw new IllegalStateException("Already provisioned in this listener.");
            }

            int currentIdx = this.index;

            try {
               ProvisionListenerStackCallback.this.listeners[this.index].onProvision(this);
            } catch (RuntimeException var3) {
               this.erredListener = ProvisionListenerStackCallback.this.listeners[currentIdx];
               throw var3;
            }

            if (currentIdx == this.index) {
               this.provision();
            }
         }

         return this.result;
      }

      public Binding<T> getBinding() {
         return ProvisionListenerStackCallback.this.binding;
      }

      public List<DependencyAndSource> getDependencyChain() {
         return this.context.getDependencyChain();
      }
   }

   public interface ProvisionCallback<T> {
      T call() throws ErrorsException;
   }
}
