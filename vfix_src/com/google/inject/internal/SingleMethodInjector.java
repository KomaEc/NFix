package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

final class SingleMethodInjector implements SingleMemberInjector {
   private final InjectorImpl.MethodInvoker methodInvoker;
   private final SingleParameterInjector<?>[] parameterInjectors;
   private final InjectionPoint injectionPoint;

   SingleMethodInjector(InjectorImpl injector, InjectionPoint injectionPoint, Errors errors) throws ErrorsException {
      this.injectionPoint = injectionPoint;
      Method method = (Method)injectionPoint.getMember();
      this.methodInvoker = this.createMethodInvoker(method);
      this.parameterInjectors = injector.getParametersInjectors(injectionPoint.getDependencies(), errors);
   }

   private InjectorImpl.MethodInvoker createMethodInvoker(final Method method) {
      int modifiers = method.getModifiers();
      if (!Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers)) {
      }

      if (!Modifier.isPublic(modifiers) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
         method.setAccessible(true);
      }

      return new InjectorImpl.MethodInvoker() {
         public Object invoke(Object target, Object... parameters) throws IllegalAccessException, InvocationTargetException {
            return method.invoke(target, parameters);
         }
      };
   }

   public InjectionPoint getInjectionPoint() {
      return this.injectionPoint;
   }

   public void inject(Errors errors, InternalContext context, Object o) {
      Object[] parameters;
      try {
         parameters = SingleParameterInjector.getAll(errors, context, this.parameterInjectors);
      } catch (ErrorsException var7) {
         errors.merge(var7.getErrors());
         return;
      }

      try {
         this.methodInvoker.invoke(o, parameters);
      } catch (IllegalAccessException var8) {
         throw new AssertionError(var8);
      } catch (InvocationTargetException var9) {
         Throwable cause = var9.getCause() != null ? var9.getCause() : var9;
         errors.withSource(this.injectionPoint).errorInjectingMethod((Throwable)cause);
      }

   }
}
