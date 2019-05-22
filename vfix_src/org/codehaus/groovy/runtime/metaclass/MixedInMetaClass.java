package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import java.lang.ref.WeakReference;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;

public class MixedInMetaClass extends OwnedMetaClass {
   private final WeakReference owner;

   public MixedInMetaClass(Object instance, Object owner) {
      super(GroovySystem.getMetaClassRegistry().getMetaClass(instance.getClass()));
      this.owner = new WeakReference(owner);
      DefaultGroovyMethods.setMetaClass((Object)instance, this);
   }

   protected Object getOwner() {
      return this.owner.get();
   }

   protected MetaClass getOwnerMetaClass(Object owner) {
      return InvokerHelper.getMetaClass(owner);
   }

   public Object invokeMethod(Class sender, Object receiver, String methodName, Object[] arguments, boolean isCallToSuper, boolean fromInsideClass) {
      return isCallToSuper ? this.delegate.invokeMethod(sender, receiver, methodName, arguments, true, fromInsideClass) : super.invokeMethod(sender, receiver, methodName, arguments, false, fromInsideClass);
   }
}
