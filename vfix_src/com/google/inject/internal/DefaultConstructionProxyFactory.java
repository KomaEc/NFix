package com.google.inject.internal;

import com.google.inject.spi.InjectionPoint;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

final class DefaultConstructionProxyFactory<T> implements ConstructionProxyFactory<T> {
   private final InjectionPoint injectionPoint;

   DefaultConstructionProxyFactory(InjectionPoint injectionPoint) {
      this.injectionPoint = injectionPoint;
   }

   public ConstructionProxy<T> create() {
      final Constructor<T> constructor = (Constructor)this.injectionPoint.getMember();
      if (Modifier.isPublic(constructor.getModifiers())) {
         Class<T> classToConstruct = constructor.getDeclaringClass();
         if (!Modifier.isPublic(classToConstruct.getModifiers())) {
            constructor.setAccessible(true);
         }
      } else {
         constructor.setAccessible(true);
      }

      return new ConstructionProxy<T>() {
         public T newInstance(Object... arguments) throws InvocationTargetException {
            try {
               return constructor.newInstance(arguments);
            } catch (InstantiationException var3) {
               throw new AssertionError(var3);
            } catch (IllegalAccessException var4) {
               throw new AssertionError(var4);
            }
         }

         public InjectionPoint getInjectionPoint() {
            return DefaultConstructionProxyFactory.this.injectionPoint;
         }

         public Constructor<T> getConstructor() {
            return constructor;
         }
      };
   }
}
