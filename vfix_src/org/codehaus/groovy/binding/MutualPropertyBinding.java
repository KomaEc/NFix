package org.codehaus.groovy.binding;

import groovy.lang.Closure;

public class MutualPropertyBinding implements FullBinding {
   boolean bound;
   PropertyBinding sourceBinding;
   PropertyBinding targetBinding;
   Closure validator;
   Closure converter;
   Closure reverseConverter;
   Closure triggerFactory;
   TriggerBinding forwardTriggerBinding;
   FullBinding forwardBinding;
   TriggerBinding reverseTriggerBinding;
   FullBinding reverseBinding;

   MutualPropertyBinding(TriggerBinding forwardTrigger, PropertyBinding source, PropertyBinding target, Closure triggerFactory) {
      this.triggerFactory = triggerFactory;
      this.sourceBinding = source;
      this.forwardTriggerBinding = forwardTrigger;
      this.setTargetBinding(target);
      this.rebuildBindings();
   }

   public SourceBinding getSourceBinding() {
      return this.sourceBinding;
   }

   public TargetBinding getTargetBinding() {
      return this.targetBinding;
   }

   public void setSourceBinding(SourceBinding sourceBinding) {
      try {
         if (sourceBinding == null) {
            this.forwardTriggerBinding = null;
         } else {
            this.forwardTriggerBinding = (TriggerBinding)this.triggerFactory.call((Object)sourceBinding);
         }

         this.sourceBinding = (PropertyBinding)sourceBinding;
      } catch (RuntimeException var3) {
         throw new UnsupportedOperationException("Mutual Bindings may only change source bindings to other PropertyBindings");
      }

      this.rebuildBindings();
   }

   public void setTargetBinding(TargetBinding targetBinding) {
      try {
         if (targetBinding == null) {
            this.reverseTriggerBinding = null;
         } else {
            this.reverseTriggerBinding = (TriggerBinding)this.triggerFactory.call((Object)targetBinding);
         }

         this.targetBinding = (PropertyBinding)targetBinding;
      } catch (RuntimeException var3) {
         throw new UnsupportedOperationException("Mutual Bindings may only change target bindings to other PropertyBindings");
      }

      this.rebuildBindings();
   }

   public void setValidator(Closure validator) {
      this.validator = validator;
      this.rebuildBindings();
   }

   public Closure getValidator() {
      return this.validator;
   }

   public void setConverter(Closure converter) {
      this.converter = converter;
      this.rebuildBindings();
   }

   public Closure getConverter() {
      return this.converter;
   }

   public void setReverseConverter(Closure reverseConverter) {
      this.reverseConverter = reverseConverter;
      this.rebuildBindings();
   }

   public Closure getReverseConverter() {
      return this.reverseConverter;
   }

   protected void rebuildBindings() {
      if (this.bound) {
         if (this.forwardBinding != null) {
            this.forwardBinding.unbind();
         }

         if (this.reverseBinding != null) {
            this.reverseBinding.unbind();
         }
      }

      if (this.forwardTriggerBinding != null && this.sourceBinding != null && this.reverseTriggerBinding != null && this.targetBinding != null) {
         this.forwardBinding = this.forwardTriggerBinding.createBinding(this.sourceBinding, this.targetBinding);
         this.reverseBinding = this.reverseTriggerBinding.createBinding(this.targetBinding, this.sourceBinding);
         if (this.converter != null && this.reverseConverter != null) {
            this.forwardBinding.setConverter(this.converter);
            this.reverseBinding.setConverter(this.reverseConverter);
         }

         if (this.validator != null) {
            this.forwardBinding.setValidator(this.validator);
         }

         if (this.bound) {
            this.forwardBinding.bind();
            this.reverseBinding.bind();
         }

      }
   }

   public void bind() {
      if (!this.bound) {
         this.bound = true;
         if (this.converter == null != (this.reverseConverter == null)) {
            throw new RuntimeException("Both converter or reverseConverter must be set or unset to bind.  Only " + (this.converter != null ? "converter" : "reverseConverter") + " is set.");
         }

         if (this.forwardBinding == null || this.reverseBinding == null) {
            return;
         }

         this.forwardBinding.bind();
         this.reverseBinding.bind();
      }

   }

   public void unbind() {
      if (this.bound) {
         this.forwardBinding.unbind();
         this.reverseBinding.unbind();
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
      this.forwardBinding.update();
   }

   public void reverseUpdate() {
      this.reverseBinding.update();
   }
}
