package com.google.inject.internal;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.spi.Element;
import java.lang.annotation.Annotation;
import java.util.List;

public final class ConstantBindingBuilderImpl<T> extends AbstractBindingBuilder<T> implements AnnotatedConstantBindingBuilder, ConstantBindingBuilder {
   public ConstantBindingBuilderImpl(Binder binder, List<Element> elements, Object source) {
      super(binder, elements, source, NULL_KEY);
   }

   public ConstantBindingBuilder annotatedWith(Class<? extends Annotation> annotationType) {
      this.annotatedWithInternal(annotationType);
      return this;
   }

   public ConstantBindingBuilder annotatedWith(Annotation annotation) {
      this.annotatedWithInternal(annotation);
      return this;
   }

   public void to(String value) {
      this.toConstant(String.class, value);
   }

   public void to(int value) {
      this.toConstant(Integer.class, value);
   }

   public void to(long value) {
      this.toConstant(Long.class, value);
   }

   public void to(boolean value) {
      this.toConstant(Boolean.class, value);
   }

   public void to(double value) {
      this.toConstant(Double.class, value);
   }

   public void to(float value) {
      this.toConstant(Float.class, value);
   }

   public void to(short value) {
      this.toConstant(Short.class, value);
   }

   public void to(char value) {
      this.toConstant(Character.class, value);
   }

   public void to(byte value) {
      this.toConstant(Byte.class, value);
   }

   public void to(Class<?> value) {
      this.toConstant(Class.class, value);
   }

   public <E extends Enum<E>> void to(E value) {
      this.toConstant(value.getDeclaringClass(), value);
   }

   private void toConstant(Class<?> type, Object instance) {
      if (this.keyTypeIsSet()) {
         this.binder.addError("Constant value is set more than once.");
      } else {
         BindingImpl<T> base = this.getBinding();
         Key key;
         if (base.getKey().getAnnotation() != null) {
            key = Key.get(type, base.getKey().getAnnotation());
         } else if (base.getKey().getAnnotationType() != null) {
            key = Key.get(type, base.getKey().getAnnotationType());
         } else {
            key = Key.get(type);
         }

         if (instance == null) {
            this.binder.addError("Binding to null instances is not allowed. Use toProvider(Providers.of(null)) if this is your intended behaviour.");
         }

         this.setBinding(new InstanceBindingImpl(base.getSource(), key, base.getScoping(), ImmutableSet.of(), instance));
      }
   }

   public String toString() {
      return "ConstantBindingBuilder";
   }
}
