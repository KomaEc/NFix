package com.google.inject.internal;

import com.google.inject.spi.Dependency;

final class SingleParameterInjector<T> {
   private static final Object[] NO_ARGUMENTS = new Object[0];
   private final Dependency<T> dependency;
   private final BindingImpl<? extends T> binding;

   SingleParameterInjector(Dependency<T> dependency, BindingImpl<? extends T> binding) {
      this.dependency = dependency;
      this.binding = binding;
   }

   private T inject(Errors errors, InternalContext context) throws ErrorsException {
      Dependency previous = context.pushDependency(this.dependency, this.binding.getSource());

      Object var4;
      try {
         var4 = this.binding.getInternalFactory().get(errors.withSource(this.dependency), context, this.dependency, false);
      } finally {
         context.popStateAndSetDependency(previous);
      }

      return var4;
   }

   static Object[] getAll(Errors errors, InternalContext context, SingleParameterInjector<?>[] parameterInjectors) throws ErrorsException {
      if (parameterInjectors == null) {
         return NO_ARGUMENTS;
      } else {
         int numErrorsBefore = errors.size();
         int size = parameterInjectors.length;
         Object[] parameters = new Object[size];

         for(int i = 0; i < size; ++i) {
            SingleParameterInjector parameterInjector = parameterInjectors[i];

            try {
               parameters[i] = parameterInjector.inject(errors, context);
            } catch (ErrorsException var9) {
               errors.merge(var9.getErrors());
            }
         }

         errors.throwIfNewErrors(numErrorsBefore);
         return parameters;
      }
   }
}
