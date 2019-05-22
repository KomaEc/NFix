package com.google.inject.internal;

import com.google.inject.spi.Dependency;
import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Field;

final class SingleFieldInjector implements SingleMemberInjector {
   final Field field;
   final InjectionPoint injectionPoint;
   final Dependency<?> dependency;
   final BindingImpl<?> binding;

   public SingleFieldInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
      this.injectionPoint = injectionPoint;
      this.field = (Field)injectionPoint.getMember();
      this.dependency = (Dependency)injectionPoint.getDependencies().get(0);
      this.field.setAccessible(true);
      this.binding = injector.getBindingOrThrow(this.dependency.getKey(), errors, InjectorImpl.JitLimitation.NO_JIT);
   }

   public InjectionPoint getInjectionPoint() {
      return this.injectionPoint;
   }

   public void inject(Errors errors, InternalContext context, Object o) {
      errors = errors.withSource(this.dependency);
      Dependency previous = context.pushDependency(this.dependency, this.binding.getSource());

      try {
         Object value = this.binding.getInternalFactory().get(errors, context, this.dependency, false);
         this.field.set(o, value);
      } catch (ErrorsException var10) {
         errors.withSource(this.injectionPoint).merge(var10.getErrors());
      } catch (IllegalAccessException var11) {
         throw new AssertionError(var11);
      } finally {
         context.popStateAndSetDependency(previous);
      }

   }
}
