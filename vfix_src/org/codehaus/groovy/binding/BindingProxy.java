package org.codehaus.groovy.binding;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.ReadOnlyPropertyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BindingProxy extends GroovyObjectSupport implements BindingUpdatable {
   Object model;
   boolean bound;
   final Map<String, PropertyBinding> propertyBindings = new HashMap();
   final List<FullBinding> generatedBindings = new ArrayList();

   public BindingProxy(Object model) {
      this.model = model;
   }

   public Object getModel() {
      return this.model;
   }

   public synchronized void setModel(Object model) {
      boolean bindAgain = this.bound;
      this.model = model;
      this.unbind();
      Iterator i$ = this.propertyBindings.values().iterator();

      while(i$.hasNext()) {
         PropertyBinding propertyBinding = (PropertyBinding)i$.next();
         propertyBinding.setBean(model);
      }

      if (bindAgain) {
         this.bind();
         this.update();
      }

   }

   public Object getProperty(String property) {
      Object pb;
      synchronized(this.propertyBindings) {
         pb = (PropertyBinding)this.propertyBindings.get(property);
         if (pb == null) {
            pb = new BindingProxy.ModelBindingPropertyBinding(this.model, property);
            this.propertyBindings.put(property, pb);
         }
      }

      FullBinding fb = ((PropertyBinding)pb).createBinding((SourceBinding)pb, (TargetBinding)null);
      if (this.bound) {
         fb.bind();
      }

      return fb;
   }

   public void setProperty(String property, Object value) {
      throw new ReadOnlyPropertyException(property, this.model.getClass());
   }

   public void bind() {
      synchronized(this.generatedBindings) {
         if (!this.bound) {
            this.bound = true;
            Iterator i$ = this.generatedBindings.iterator();

            while(i$.hasNext()) {
               FullBinding generatedBinding = (FullBinding)i$.next();
               generatedBinding.bind();
            }
         }

      }
   }

   public void unbind() {
      synchronized(this.generatedBindings) {
         if (this.bound) {
            this.bound = false;
            Iterator i$ = this.generatedBindings.iterator();

            while(i$.hasNext()) {
               FullBinding generatedBinding = (FullBinding)i$.next();
               generatedBinding.unbind();
            }
         }

      }
   }

   public void rebind() {
      synchronized(this.generatedBindings) {
         if (this.bound) {
            Iterator i$ = this.generatedBindings.iterator();

            while(i$.hasNext()) {
               FullBinding generatedBinding = (FullBinding)i$.next();
               generatedBinding.rebind();
            }
         }

      }
   }

   public void update() {
      synchronized(this.generatedBindings) {
         Iterator i$ = this.generatedBindings.iterator();

         while(i$.hasNext()) {
            FullBinding generatedBinding = (FullBinding)i$.next();
            generatedBinding.update();
         }

      }
   }

   public void reverseUpdate() {
      synchronized(this.generatedBindings) {
         Iterator i$ = this.generatedBindings.iterator();

         while(i$.hasNext()) {
            FullBinding generatedBinding = (FullBinding)i$.next();
            generatedBinding.reverseUpdate();
         }

      }
   }

   class ModelBindingPropertyBinding extends PropertyBinding {
      public ModelBindingPropertyBinding(Object bean, String propertyName) {
         super(bean, propertyName);
      }

      public FullBinding createBinding(SourceBinding source, TargetBinding target) {
         FullBinding fb = super.createBinding(source, target);
         BindingProxy.this.generatedBindings.add(fb);
         return fb;
      }
   }
}
