package org.codehaus.plexus.component.factory.java;

import java.lang.reflect.Modifier;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.factory.AbstractComponentFactory;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;

public class JavaComponentFactory extends AbstractComponentFactory {
   public Object newInstance(ComponentDescriptor componentDescriptor, ClassRealm classRealm, PlexusContainer container) throws ComponentInstantiationException {
      Class implementationClass = null;

      try {
         String implementation = componentDescriptor.getImplementation();
         implementationClass = classRealm.loadClass(implementation);
         int modifiers = implementationClass.getModifiers();
         if (Modifier.isInterface(modifiers)) {
            throw new ComponentInstantiationException("Cannot instanciate implementation '" + implementation + "' because the class is a interface.");
         } else if (Modifier.isAbstract(modifiers)) {
            throw new ComponentInstantiationException("Cannot instanciate implementation '" + implementation + "' because the class is abstract.");
         } else {
            Object instance = implementationClass.newInstance();
            return instance;
         }
      } catch (InstantiationException var8) {
         throw this.makeException(classRealm, componentDescriptor, implementationClass, var8);
      } catch (ClassNotFoundException var9) {
         throw this.makeException(classRealm, componentDescriptor, implementationClass, var9);
      } catch (IllegalAccessException var10) {
         throw this.makeException(classRealm, componentDescriptor, implementationClass, var10);
      } catch (LinkageError var11) {
         throw this.makeException(classRealm, componentDescriptor, implementationClass, var11);
      }
   }

   private ComponentInstantiationException makeException(ClassRealm componentClassRealm, ComponentDescriptor componentDescriptor, Class implementationClass, Throwable e) {
      componentClassRealm.display();
      String msg = "Could not instanciate component: " + componentDescriptor.getHumanReadableKey();
      return new ComponentInstantiationException(msg, e);
   }
}
