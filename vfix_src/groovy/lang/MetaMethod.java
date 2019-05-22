package groovy.lang;

import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ParameterTypes;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;

public abstract class MetaMethod extends ParameterTypes implements Cloneable {
   private String signature;
   private String mopName;

   public MetaMethod() {
   }

   public MetaMethod(Class[] pt) {
      super(pt);
   }

   public abstract int getModifiers();

   public abstract String getName();

   public abstract Class getReturnType();

   public abstract CachedClass getDeclaringClass();

   public abstract Object invoke(Object var1, Object[] var2);

   public void checkParameters(Class[] arguments) {
      if (!this.isValidMethod(arguments)) {
         throw new IllegalArgumentException("Parameters to method: " + this.getName() + " do not match types: " + InvokerHelper.toString(this.getParameterTypes()) + " for arguments: " + InvokerHelper.toString(arguments));
      }
   }

   public boolean isMethod(MetaMethod method) {
      return this.getName().equals(method.getName()) && this.getModifiers() == method.getModifiers() && this.getReturnType().equals(method.getReturnType()) && equal(this.getParameterTypes(), method.getParameterTypes());
   }

   protected static boolean equal(CachedClass[] a, Class[] b) {
      if (a.length == b.length) {
         int i = 0;

         for(int size = a.length; i < size; ++i) {
            if (!a[i].getTheClass().equals(b[i])) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected static boolean equal(CachedClass[] a, CachedClass[] b) {
      if (a.length == b.length) {
         int i = 0;

         for(int size = a.length; i < size; ++i) {
            if (a[i] != b[i]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public String toString() {
      return super.toString() + "[name: " + this.getName() + " params: " + InvokerHelper.toString(this.getParameterTypes()) + " returns: " + this.getReturnType() + " owner: " + this.getDeclaringClass() + "]";
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new GroovyRuntimeException("This should never happen", var2);
      }
   }

   public boolean isStatic() {
      return (this.getModifiers() & 8) != 0;
   }

   public boolean isAbstract() {
      return (this.getModifiers() & 1024) != 0;
   }

   public final boolean isPrivate() {
      return (this.getModifiers() & 2) != 0;
   }

   public final boolean isProtected() {
      return (this.getModifiers() & 4) != 0;
   }

   public final boolean isPublic() {
      return (this.getModifiers() & 1) != 0;
   }

   public final boolean isSame(MetaMethod method) {
      return this.getName().equals(method.getName()) && compatibleModifiers(this.getModifiers(), method.getModifiers()) && this.getReturnType().equals(method.getReturnType()) && equal(this.getParameterTypes(), method.getParameterTypes());
   }

   private static boolean compatibleModifiers(int modifiersA, int modifiersB) {
      int mask = 15;
      return (modifiersA & mask) == (modifiersB & mask);
   }

   public boolean isCacheable() {
      return true;
   }

   public String getDescriptor() {
      return BytecodeHelper.getMethodDescriptor(this.getReturnType(), this.getNativeParameterTypes());
   }

   public synchronized String getSignature() {
      if (this.signature == null) {
         CachedClass[] parameters = this.getParameterTypes();
         String name = this.getName();
         StringBuffer buf = new StringBuffer(name.length() + parameters.length * 10);
         buf.append(this.getReturnType().getName());
         buf.append(' ');
         buf.append(name);
         buf.append('(');

         for(int i = 0; i < parameters.length; ++i) {
            if (i > 0) {
               buf.append(", ");
            }

            buf.append(parameters[i].getName());
         }

         buf.append(')');
         this.signature = buf.toString();
      }

      return this.signature;
   }

   public String getMopName() {
      if (this.mopName == null) {
         String name = this.getName();
         CachedClass declaringClass = this.getDeclaringClass();
         if ((this.getModifiers() & 5) == 0) {
            this.mopName = "this$" + declaringClass.getSuperClassDistance() + "$" + name;
         } else {
            this.mopName = "super$" + declaringClass.getSuperClassDistance() + "$" + name;
         }
      }

      return this.mopName;
   }

   public final RuntimeException processDoMethodInvokeException(Exception e, Object object, Object[] argumentArray) {
      return (RuntimeException)(e instanceof RuntimeException ? (RuntimeException)e : MetaClassHelper.createExceptionText("failed to invoke method: ", this, object, argumentArray, e, true));
   }

   public Object doMethodInvoke(Object object, Object[] argumentArray) {
      argumentArray = this.coerceArgumentsToClasses(argumentArray);

      try {
         return this.invoke(object, argumentArray);
      } catch (Exception var4) {
         throw this.processDoMethodInvokeException(var4, object, argumentArray);
      }
   }
}
