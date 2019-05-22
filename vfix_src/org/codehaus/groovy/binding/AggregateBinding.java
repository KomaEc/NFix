package org.codehaus.groovy.binding;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class AggregateBinding implements BindingUpdatable {
   protected boolean bound;
   protected Set<BindingUpdatable> bindings = new LinkedHashSet();

   public void addBinding(BindingUpdatable binding) {
      if (this.bound) {
         binding.bind();
      }

      this.bindings.add(binding);
   }

   public void removeBinding(BindingUpdatable binding) {
      this.bindings.remove(binding);
   }

   public void bind() {
      if (!this.bound) {
         this.bound = true;
         Iterator i$ = this.bindings.iterator();

         while(i$.hasNext()) {
            BindingUpdatable binding = (BindingUpdatable)i$.next();
            binding.bind();
         }
      }

   }

   public void unbind() {
      if (this.bound) {
         Iterator i$ = this.bindings.iterator();

         while(i$.hasNext()) {
            BindingUpdatable binding = (BindingUpdatable)i$.next();
            binding.unbind();
         }

         this.bound = false;
      }

   }

   public void rebind() {
      if (this.bound) {
         this.unbind();
         this.bind();
      }

   }

   public void update() {
      Iterator i$ = this.bindings.iterator();

      while(i$.hasNext()) {
         BindingUpdatable binding = (BindingUpdatable)i$.next();
         binding.update();
      }

   }

   public void reverseUpdate() {
      Iterator i$ = this.bindings.iterator();

      while(i$.hasNext()) {
         BindingUpdatable binding = (BindingUpdatable)i$.next();
         binding.reverseUpdate();
      }

   }
}
