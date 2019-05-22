package org.codehaus.groovy.binding;

import groovy.lang.Closure;

public abstract class AbstractFullBinding implements FullBinding {
   protected SourceBinding sourceBinding;
   protected TargetBinding targetBinding;
   protected Closure validator;
   protected Closure converter;
   protected Closure reverseConverter;

   private void fireBinding() {
      if (this.sourceBinding != null && this.targetBinding != null) {
         Object result = this.sourceBinding.getSourceValue();
         if (this.getValidator() != null) {
            Object validation = this.getValidator().call(result);
            if (validation == null || validation instanceof Boolean && !(Boolean)validation) {
               return;
            }
         }

         if (this.getConverter() != null) {
            result = this.getConverter().call(result);
         }

         this.targetBinding.updateTargetValue(result);
      }
   }

   public void update() {
      this.fireBinding();
   }

   private void fireReverseBinding() {
      if (this.sourceBinding instanceof TargetBinding && this.targetBinding instanceof SourceBinding) {
         Object result = ((SourceBinding)this.targetBinding).getSourceValue();
         if (this.getReverseConverter() != null) {
            result = this.getReverseConverter().call(result);
         }

         ((TargetBinding)this.sourceBinding).updateTargetValue(result);
      } else {
         throw new RuntimeException("Binding Instance is not reversable");
      }
   }

   public void reverseUpdate() {
      this.fireReverseBinding();
   }

   public SourceBinding getSourceBinding() {
      return this.sourceBinding;
   }

   public void setSourceBinding(SourceBinding sourceBinding) {
      this.sourceBinding = sourceBinding;
   }

   public TargetBinding getTargetBinding() {
      return this.targetBinding;
   }

   public void setTargetBinding(TargetBinding targetBinding) {
      this.targetBinding = targetBinding;
   }

   public Closure getValidator() {
      return this.validator;
   }

   public void setValidator(Closure validator) {
      this.validator = validator;
   }

   public Closure getConverter() {
      return this.converter;
   }

   public void setConverter(Closure converter) {
      this.converter = converter;
   }

   public Closure getReverseConverter() {
      return this.reverseConverter;
   }

   public void setReverseConverter(Closure reverseConverter) {
      this.reverseConverter = reverseConverter;
   }
}
