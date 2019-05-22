package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingMethodException;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.runtime.InvokerHelper;

public abstract class BuilderSupport extends GroovyObjectSupport {
   private Object current;
   private Closure nameMappingClosure;
   private final BuilderSupport proxyBuilder;

   public BuilderSupport() {
      this.proxyBuilder = this;
   }

   public BuilderSupport(BuilderSupport proxyBuilder) {
      this((Closure)null, proxyBuilder);
   }

   public BuilderSupport(Closure nameMappingClosure, BuilderSupport proxyBuilder) {
      this.nameMappingClosure = nameMappingClosure;
      this.proxyBuilder = proxyBuilder;
   }

   public Object invokeMethod(String methodName) {
      return this.invokeMethod(methodName, (Object)null);
   }

   public Object invokeMethod(String methodName, Object args) {
      Object name = this.getName(methodName);
      return this.doInvokeMethod(methodName, name, args);
   }

   protected Object doInvokeMethod(String methodName, Object name, Object args) {
      Object node = null;
      Closure closure = null;
      List list = InvokerHelper.asList(args);
      Object object1;
      Object object2;
      switch(list.size()) {
      case 0:
         node = this.proxyBuilder.createNode(name);
         break;
      case 1:
         object1 = list.get(0);
         if (object1 instanceof Map) {
            node = this.proxyBuilder.createNode(name, (Map)object1);
         } else if (object1 instanceof Closure) {
            closure = (Closure)object1;
            node = this.proxyBuilder.createNode(name);
         } else {
            node = this.proxyBuilder.createNode(name, object1);
         }
         break;
      case 2:
         object1 = list.get(0);
         object2 = list.get(1);
         if (object1 instanceof Map) {
            if (object2 instanceof Closure) {
               closure = (Closure)object2;
               node = this.proxyBuilder.createNode(name, (Map)object1);
            } else {
               node = this.proxyBuilder.createNode(name, (Map)object1, object2);
            }
         } else if (object2 instanceof Closure) {
            closure = (Closure)object2;
            node = this.proxyBuilder.createNode(name, object1);
         } else {
            if (!(object2 instanceof Map)) {
               throw new MissingMethodException(name.toString(), this.getClass(), list.toArray(), false);
            }

            node = this.proxyBuilder.createNode(name, (Map)object2, object1);
         }
         break;
      case 3:
         object1 = list.get(0);
         object2 = list.get(1);
         Object arg2 = list.get(2);
         if (object1 instanceof Map && arg2 instanceof Closure) {
            closure = (Closure)arg2;
            node = this.proxyBuilder.createNode(name, (Map)object1, object2);
            break;
         } else {
            if (object2 instanceof Map && arg2 instanceof Closure) {
               closure = (Closure)arg2;
               node = this.proxyBuilder.createNode(name, (Map)object2, object1);
               break;
            }

            throw new MissingMethodException(name.toString(), this.getClass(), list.toArray(), false);
         }
      default:
         throw new MissingMethodException(name.toString(), this.getClass(), list.toArray(), false);
      }

      if (this.current != null) {
         this.proxyBuilder.setParent(this.current, node);
      }

      if (closure != null) {
         object1 = this.getCurrent();
         this.setCurrent(node);
         this.setClosureDelegate(closure, node);
         closure.call();
         this.setCurrent(object1);
      }

      this.proxyBuilder.nodeCompleted(this.current, node);
      return this.proxyBuilder.postNodeCompletion(this.current, node);
   }

   protected void setClosureDelegate(Closure closure, Object node) {
      closure.setDelegate(this);
   }

   protected abstract void setParent(Object var1, Object var2);

   protected abstract Object createNode(Object var1);

   protected abstract Object createNode(Object var1, Object var2);

   protected abstract Object createNode(Object var1, Map var2);

   protected abstract Object createNode(Object var1, Map var2, Object var3);

   protected Object getName(String methodName) {
      return this.nameMappingClosure != null ? this.nameMappingClosure.call((Object)methodName) : methodName;
   }

   protected void nodeCompleted(Object parent, Object node) {
   }

   protected Object postNodeCompletion(Object parent, Object node) {
      return node;
   }

   protected Object getCurrent() {
      return this.current;
   }

   protected void setCurrent(Object current) {
      this.current = current;
   }
}
