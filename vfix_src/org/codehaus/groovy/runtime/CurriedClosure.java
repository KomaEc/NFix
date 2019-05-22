package org.codehaus.groovy.runtime;

import groovy.lang.Closure;

public final class CurriedClosure extends Closure {
   private Object[] curriedParams;
   private int index;
   private int numTrailingArgs;
   private Class varargType;

   public CurriedClosure(int index, Closure uncurriedClosure, Object[] arguments) {
      super(uncurriedClosure.clone());
      this.numTrailingArgs = 0;
      this.varargType = null;
      this.curriedParams = arguments;
      this.index = index;
      int origMaxLen = uncurriedClosure.getMaximumNumberOfParameters();
      this.maximumNumberOfParameters = origMaxLen - arguments.length;
      Class[] classes = uncurriedClosure.getParameterTypes();
      Class lastType = classes.length == 0 ? null : classes[classes.length - 1];
      if (lastType != null && lastType.isArray()) {
         this.varargType = lastType;
      }

      if (this.isVararg()) {
         if (index < 0) {
            this.numTrailingArgs = -index - arguments.length;
         }
      } else {
         if (index < 0) {
            this.index += origMaxLen;
         }

         if (this.maximumNumberOfParameters < 0) {
            throw new IllegalArgumentException("Can't curry " + arguments.length + " arguments for a closure with " + origMaxLen + " parameters.");
         }

         if (index < 0) {
            if (index < -origMaxLen || index > -arguments.length) {
               throw new IllegalArgumentException("To curry " + arguments.length + " argument(s) expect index range " + -origMaxLen + ".." + -arguments.length + " but found " + index);
            }
         } else if (index > this.maximumNumberOfParameters) {
            throw new IllegalArgumentException("To curry " + arguments.length + " argument(s) expect index range 0.." + this.maximumNumberOfParameters + " but found " + index);
         }
      }

   }

   public CurriedClosure(Closure uncurriedClosure, Object[] arguments) {
      this(0, uncurriedClosure, arguments);
   }

   /** @deprecated */
   @Deprecated
   public CurriedClosure(Closure uncurriedClosure, int i) {
      this(uncurriedClosure, new Object[]{i});
   }

   public Object[] getUncurriedArguments(Object[] arguments) {
      if (this.isVararg()) {
         int normalizedIndex = this.index < 0 ? this.index + arguments.length + this.curriedParams.length : this.index;
         if (normalizedIndex >= 0 && normalizedIndex <= arguments.length) {
            Object[] newCurriedParams = new Object[this.curriedParams.length + arguments.length];
            System.arraycopy(arguments, 0, newCurriedParams, 0, normalizedIndex);
            System.arraycopy(this.curriedParams, 0, newCurriedParams, normalizedIndex, this.curriedParams.length);
            if (arguments.length - normalizedIndex > 0) {
               System.arraycopy(arguments, normalizedIndex, newCurriedParams, this.curriedParams.length + normalizedIndex, arguments.length - normalizedIndex);
            }

            return newCurriedParams;
         } else {
            throw new IllegalArgumentException("When currying expected index range between " + (-arguments.length - this.curriedParams.length) + ".." + (arguments.length + this.curriedParams.length) + " but found " + this.index);
         }
      } else {
         Object[] newCurriedParams = new Object[this.curriedParams.length + arguments.length];
         System.arraycopy(arguments, 0, newCurriedParams, 0, this.index);
         System.arraycopy(this.curriedParams, 0, newCurriedParams, this.index, this.curriedParams.length);
         if (arguments.length - this.index > 0) {
            System.arraycopy(arguments, this.index, newCurriedParams, this.curriedParams.length + this.index, arguments.length - this.index);
         }

         return newCurriedParams;
      }
   }

   public void setDelegate(Object delegate) {
      ((Closure)this.getOwner()).setDelegate(delegate);
   }

   public Object getDelegate() {
      return ((Closure)this.getOwner()).getDelegate();
   }

   public void setResolveStrategy(int resolveStrategy) {
      ((Closure)this.getOwner()).setResolveStrategy(resolveStrategy);
   }

   public int getResolveStrategy() {
      return ((Closure)this.getOwner()).getResolveStrategy();
   }

   public Object clone() {
      Closure uncurriedClosure = (Closure)((Closure)this.getOwner()).clone();
      return new CurriedClosure(this.index, uncurriedClosure, this.curriedParams);
   }

   public Class[] getParameterTypes() {
      Class[] oldParams = ((Closure)this.getOwner()).getParameterTypes();
      int extraParams = 0;
      int gobbledParams = this.curriedParams.length;
      if (!this.isVararg()) {
         Class[] newParams = new Class[oldParams.length - gobbledParams + extraParams];
         System.arraycopy(oldParams, 0, newParams, 0, this.index);
         if (newParams.length - this.index > 0) {
            System.arraycopy(oldParams, this.curriedParams.length + this.index, newParams, this.index, newParams.length - this.index);
         }

         return newParams;
      } else {
         int numNonVarargs = oldParams.length - 1;
         int leadingKept;
         int newNumNonVarargs;
         if (this.index < 0) {
            leadingKept = this.index < 0 ? -this.index : this.index;
            if (leadingKept > numNonVarargs) {
               gobbledParams = numNonVarargs;
            }

            newNumNonVarargs = numNonVarargs - gobbledParams;
            if (leadingKept - this.curriedParams.length > newNumNonVarargs) {
               extraParams = leadingKept - this.curriedParams.length - newNumNonVarargs;
            }

            int keptParams = Math.max(numNonVarargs - leadingKept, 0);
            Class[] newParams = new Class[keptParams + newNumNonVarargs + extraParams + 1];
            System.arraycopy(oldParams, 0, newParams, 0, keptParams);

            int i;
            for(i = 0; i < newNumNonVarargs; ++i) {
               newParams[keptParams + i] = Object.class;
            }

            for(i = 0; i < extraParams; ++i) {
               newParams[keptParams + newNumNonVarargs + i] = this.varargType.getComponentType();
            }

            newParams[newParams.length - 1] = this.varargType;
            return newParams;
         } else {
            leadingKept = Math.min(this.index, numNonVarargs);
            newNumNonVarargs = Math.max(numNonVarargs - leadingKept - this.curriedParams.length, 0);
            if (this.index > leadingKept) {
               extraParams = this.index - leadingKept;
            }

            Class[] newParams = new Class[leadingKept + newNumNonVarargs + extraParams + 1];
            System.arraycopy(oldParams, 0, newParams, 0, leadingKept);
            if (newNumNonVarargs > 0) {
               System.arraycopy(oldParams, leadingKept + this.curriedParams.length, newParams, leadingKept, newNumNonVarargs);
            }

            for(int i = 0; i < extraParams; ++i) {
               newParams[leadingKept + newNumNonVarargs + i] = this.varargType.getComponentType();
            }

            newParams[newParams.length - 1] = this.varargType;
            return newParams;
         }
      }
   }

   private boolean isVararg() {
      return this.varargType != null;
   }
}
