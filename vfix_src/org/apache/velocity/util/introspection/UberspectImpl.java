package org.apache.velocity.util.introspection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import org.apache.velocity.runtime.RuntimeLogger;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.log.RuntimeLoggerLog;
import org.apache.velocity.runtime.parser.node.AbstractExecutor;
import org.apache.velocity.runtime.parser.node.BooleanPropertyExecutor;
import org.apache.velocity.runtime.parser.node.GetExecutor;
import org.apache.velocity.runtime.parser.node.MapGetExecutor;
import org.apache.velocity.runtime.parser.node.MapSetExecutor;
import org.apache.velocity.runtime.parser.node.PropertyExecutor;
import org.apache.velocity.runtime.parser.node.PutExecutor;
import org.apache.velocity.runtime.parser.node.SetExecutor;
import org.apache.velocity.runtime.parser.node.SetPropertyExecutor;
import org.apache.velocity.util.ArrayIterator;
import org.apache.velocity.util.EnumerationIterator;

public class UberspectImpl implements Uberspect, UberspectLoggable {
   protected Log log;
   protected Introspector introspector;

   public void init() {
      this.introspector = new Introspector(this.log);
   }

   public void setLog(Log log) {
      this.log = log;
   }

   /** @deprecated */
   public void setRuntimeLogger(RuntimeLogger runtimeLogger) {
      this.setLog(new RuntimeLoggerLog(runtimeLogger));
   }

   public Iterator getIterator(Object obj, Info i) throws Exception {
      if (obj.getClass().isArray()) {
         return new ArrayIterator(obj);
      } else if (obj instanceof Collection) {
         return ((Collection)obj).iterator();
      } else if (obj instanceof Map) {
         return ((Map)obj).values().iterator();
      } else if (obj instanceof Iterator) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("The iterative object in the #foreach() loop at " + i + " is of type java.util.Iterator.  Because " + "it is not resettable, if used in more than once it " + "may lead to unexpected results.");
         }

         return (Iterator)obj;
      } else if (obj instanceof Enumeration) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("The iterative object in the #foreach() loop at " + i + " is of type java.util.Enumeration.  Because " + "it is not resettable, if used in more than once it " + "may lead to unexpected results.");
         }

         return new EnumerationIterator((Enumeration)obj);
      } else {
         this.log.info("Could not determine type of iterator in #foreach loop at " + i);
         return null;
      }
   }

   public VelMethod getMethod(Object obj, String methodName, Object[] args, Info i) throws Exception {
      if (obj == null) {
         return null;
      } else {
         Method m = this.introspector.getMethod(obj.getClass(), methodName, args);
         return m != null ? new UberspectImpl.VelMethodImpl(m) : null;
      }
   }

   public VelPropertyGet getPropertyGet(Object obj, String identifier, Info i) throws Exception {
      if (obj == null) {
         return null;
      } else {
         Class claz = obj.getClass();
         AbstractExecutor executor = new PropertyExecutor(this.log, this.introspector, claz, identifier);
         if (!((AbstractExecutor)executor).isAlive()) {
            executor = new MapGetExecutor(this.log, claz, identifier);
         }

         if (!((AbstractExecutor)executor).isAlive()) {
            executor = new GetExecutor(this.log, this.introspector, claz, identifier);
         }

         if (!((AbstractExecutor)executor).isAlive()) {
            executor = new BooleanPropertyExecutor(this.log, this.introspector, claz, identifier);
         }

         return ((AbstractExecutor)executor).isAlive() ? new UberspectImpl.VelGetterImpl((AbstractExecutor)executor) : null;
      }
   }

   public VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info i) throws Exception {
      if (obj == null) {
         return null;
      } else {
         Class claz = obj.getClass();
         SetExecutor executor = new SetPropertyExecutor(this.log, this.introspector, claz, identifier, arg);
         if (!((SetExecutor)executor).isAlive()) {
            executor = new MapSetExecutor(this.log, claz, identifier);
         }

         if (!((SetExecutor)executor).isAlive()) {
            executor = new PutExecutor(this.log, this.introspector, claz, arg, identifier);
         }

         return ((SetExecutor)executor).isAlive() ? new UberspectImpl.VelSetterImpl((SetExecutor)executor) : null;
      }
   }

   public static class VelSetterImpl implements VelPropertySet {
      private final SetExecutor setExecutor;

      public VelSetterImpl(SetExecutor setExecutor) {
         this.setExecutor = setExecutor;
      }

      private VelSetterImpl() {
         this.setExecutor = null;
      }

      public Object invoke(Object o, Object value) throws Exception {
         return this.setExecutor.execute(o, value);
      }

      public boolean isCacheable() {
         return true;
      }

      public String getMethodName() {
         return this.setExecutor.isAlive() ? this.setExecutor.getMethod().getName() : null;
      }
   }

   public static class VelGetterImpl implements VelPropertyGet {
      final AbstractExecutor getExecutor;

      public VelGetterImpl(AbstractExecutor exec) {
         this.getExecutor = exec;
      }

      private VelGetterImpl() {
         this.getExecutor = null;
      }

      public Object invoke(Object o) throws Exception {
         return this.getExecutor.execute(o);
      }

      public boolean isCacheable() {
         return true;
      }

      public String getMethodName() {
         return this.getExecutor.isAlive() ? this.getExecutor.getMethod().getName() : null;
      }
   }

   public static class VelMethodImpl implements VelMethod {
      final Method method;

      public VelMethodImpl(Method m) {
         this.method = m;
      }

      private VelMethodImpl() {
         this.method = null;
      }

      public Object invoke(Object o, Object[] params) throws Exception {
         return this.method.invoke(o, params);
      }

      public boolean isCacheable() {
         return true;
      }

      public String getMethodName() {
         return this.method.getName();
      }

      public Class getReturnType() {
         return this.method.getReturnType();
      }
   }
}
