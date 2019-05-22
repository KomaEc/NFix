package groovy.lang;

import java.lang.reflect.Method;
import java.util.List;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.runtime.InvokerHelper;

public class DelegatingMetaClass implements MetaClass, MutableMetaClass, GroovyObject {
   protected MetaClass delegate;

   public DelegatingMetaClass(MetaClass delegate) {
      this.delegate = delegate;
   }

   public DelegatingMetaClass(Class theClass) {
      this(GroovySystem.getMetaClassRegistry().getMetaClass(theClass));
   }

   public boolean isModified() {
      return this.delegate instanceof MutableMetaClass && ((MutableMetaClass)this.delegate).isModified();
   }

   public void addNewInstanceMethod(Method method) {
      if (this.delegate instanceof MutableMetaClass) {
         ((MutableMetaClass)this.delegate).addNewInstanceMethod(method);
      }

   }

   public void addNewStaticMethod(Method method) {
      if (this.delegate instanceof MutableMetaClass) {
         ((MutableMetaClass)this.delegate).addNewStaticMethod(method);
      }

   }

   public void addMetaMethod(MetaMethod metaMethod) {
      if (this.delegate instanceof MutableMetaClass) {
         ((MutableMetaClass)this.delegate).addMetaMethod(metaMethod);
      }

   }

   public void addMetaBeanProperty(MetaBeanProperty metaBeanProperty) {
      if (this.delegate instanceof MutableMetaClass) {
         ((MutableMetaClass)this.delegate).addMetaBeanProperty(metaBeanProperty);
      }

   }

   public void initialize() {
      this.delegate.initialize();
   }

   public Object getAttribute(Object object, String attribute) {
      return this.delegate.getAttribute(object, attribute);
   }

   public ClassNode getClassNode() {
      return this.delegate.getClassNode();
   }

   public List<MetaMethod> getMetaMethods() {
      return this.delegate.getMetaMethods();
   }

   public List<MetaMethod> getMethods() {
      return this.delegate.getMethods();
   }

   public List<MetaMethod> respondsTo(Object obj, String name, Object[] argTypes) {
      return this.delegate.respondsTo(obj, name, argTypes);
   }

   public List<MetaMethod> respondsTo(Object obj, String name) {
      return this.delegate.respondsTo(obj, name);
   }

   public MetaProperty hasProperty(Object obj, String name) {
      return this.delegate.hasProperty(obj, name);
   }

   public List<MetaProperty> getProperties() {
      return this.delegate.getProperties();
   }

   public Object getProperty(Object object, String property) {
      return this.delegate.getProperty(object, property);
   }

   public Object invokeConstructor(Object[] arguments) {
      return this.delegate.invokeConstructor(arguments);
   }

   public Object invokeMethod(Object object, String methodName, Object arguments) {
      return this.delegate.invokeMethod(object, methodName, arguments);
   }

   public Object invokeMethod(Object object, String methodName, Object[] arguments) {
      return this.delegate.invokeMethod(object, methodName, arguments);
   }

   public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
      return this.delegate.invokeStaticMethod(object, methodName, arguments);
   }

   public void setAttribute(Object object, String attribute, Object newValue) {
      this.delegate.setAttribute(object, attribute, newValue);
   }

   public void setProperty(Object object, String property, Object newValue) {
      this.delegate.setProperty(object, property, newValue);
   }

   public boolean equals(Object obj) {
      return this.delegate.equals(obj);
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public String toString() {
      return super.toString() + "[" + this.delegate.toString() + "]";
   }

   /** @deprecated */
   public MetaMethod pickMethod(String methodName, Class[] arguments) {
      return this.delegate.pickMethod(methodName, arguments);
   }

   public Object getAttribute(Class sender, Object receiver, String messageName, boolean useSuper) {
      return this.delegate.getAttribute(sender, receiver, messageName, useSuper);
   }

   public Object getProperty(Class sender, Object receiver, String messageName, boolean useSuper, boolean fromInsideClass) {
      return this.delegate.getProperty(sender, receiver, messageName, useSuper, fromInsideClass);
   }

   public MetaProperty getMetaProperty(String name) {
      return this.delegate.getMetaProperty(name);
   }

   public MetaMethod getStaticMetaMethod(String name, Object[] args) {
      return this.delegate.getStaticMetaMethod(name, args);
   }

   public MetaMethod getStaticMetaMethod(String name, Class[] argTypes) {
      return this.delegate.getStaticMetaMethod(name, argTypes);
   }

   public MetaMethod getMetaMethod(String name, Object[] args) {
      return this.delegate.getMetaMethod(name, args);
   }

   public Class getTheClass() {
      return this.delegate.getTheClass();
   }

   public Object invokeMethod(Class sender, Object receiver, String methodName, Object[] arguments, boolean isCallToSuper, boolean fromInsideClass) {
      return this.delegate.invokeMethod(sender, receiver, methodName, arguments, isCallToSuper, fromInsideClass);
   }

   public Object invokeMissingMethod(Object instance, String methodName, Object[] arguments) {
      return this.delegate.invokeMissingMethod(instance, methodName, arguments);
   }

   public Object invokeMissingProperty(Object instance, String propertyName, Object optionalValue, boolean isGetter) {
      return this.delegate.invokeMissingProperty(instance, propertyName, optionalValue, isGetter);
   }

   public boolean isGroovyObject() {
      return GroovyObject.class.isAssignableFrom(this.delegate.getTheClass());
   }

   public void setAttribute(Class sender, Object receiver, String messageName, Object messageValue, boolean useSuper, boolean fromInsideClass) {
      this.delegate.setAttribute(sender, receiver, messageName, messageValue, useSuper, fromInsideClass);
   }

   public void setProperty(Class sender, Object receiver, String messageName, Object messageValue, boolean useSuper, boolean fromInsideClass) {
      this.delegate.setProperty(sender, receiver, messageName, messageValue, useSuper, fromInsideClass);
   }

   public int selectConstructorAndTransformArguments(int numberOfConstructors, Object[] arguments) {
      return this.delegate.selectConstructorAndTransformArguments(numberOfConstructors, arguments);
   }

   public void setAdaptee(MetaClass adaptee) {
      this.delegate = adaptee;
   }

   public MetaClass getAdaptee() {
      return this.delegate;
   }

   public Object invokeMethod(String name, Object args) {
      try {
         return this.getMetaClass().invokeMethod(this, name, args);
      } catch (MissingMethodException var4) {
         if (this.delegate instanceof GroovyObject) {
            return ((GroovyObject)this.delegate).invokeMethod(name, args);
         } else {
            throw var4;
         }
      }
   }

   public Object getProperty(String property) {
      try {
         return this.getMetaClass().getProperty(this, property);
      } catch (MissingPropertyException var3) {
         if (this.delegate instanceof GroovyObject) {
            return ((GroovyObject)this.delegate).getProperty(property);
         } else {
            throw var3;
         }
      }
   }

   public void setProperty(String property, Object newValue) {
      try {
         this.getMetaClass().setProperty(this, property, newValue);
      } catch (MissingPropertyException var4) {
         if (!(this.delegate instanceof GroovyObject)) {
            throw var4;
         }

         ((GroovyObject)this.delegate).setProperty(property, newValue);
      }

   }

   public MetaClass getMetaClass() {
      return InvokerHelper.getMetaClass(this.getClass());
   }

   public void setMetaClass(MetaClass metaClass) {
      throw new UnsupportedOperationException();
   }
}
