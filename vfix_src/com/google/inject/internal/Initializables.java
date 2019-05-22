package com.google.inject.internal;

final class Initializables {
   static <T> Initializable<T> of(final T instance) {
      return new Initializable<T>() {
         public T get(Errors errors) throws ErrorsException {
            return instance;
         }

         public String toString() {
            return String.valueOf(instance);
         }
      };
   }
}
