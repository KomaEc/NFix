package org.apache.commons.digester;

import org.apache.commons.collections.ArrayStack;
import org.xml.sax.Attributes;

public class FactoryCreateRule extends Rule {
   private boolean ignoreCreateExceptions;
   private ArrayStack exceptionIgnoredStack;
   protected String attributeName;
   protected String className;
   protected ObjectCreationFactory creationFactory;

   /** @deprecated */
   public FactoryCreateRule(Digester digester, String className) {
      this(className);
   }

   /** @deprecated */
   public FactoryCreateRule(Digester digester, Class clazz) {
      this(clazz);
   }

   /** @deprecated */
   public FactoryCreateRule(Digester digester, String className, String attributeName) {
      this(className, attributeName);
   }

   /** @deprecated */
   public FactoryCreateRule(Digester digester, Class clazz, String attributeName) {
      this(clazz, attributeName);
   }

   /** @deprecated */
   public FactoryCreateRule(Digester digester, ObjectCreationFactory creationFactory) {
      this(creationFactory);
   }

   public FactoryCreateRule(String className) {
      this(className, false);
   }

   public FactoryCreateRule(Class clazz) {
      this(clazz, false);
   }

   public FactoryCreateRule(String className, String attributeName) {
      this(className, attributeName, false);
   }

   public FactoryCreateRule(Class clazz, String attributeName) {
      this(clazz, attributeName, false);
   }

   public FactoryCreateRule(ObjectCreationFactory creationFactory) {
      this(creationFactory, false);
   }

   public FactoryCreateRule(String className, boolean ignoreCreateExceptions) {
      this((String)className, (String)null, ignoreCreateExceptions);
   }

   public FactoryCreateRule(Class clazz, boolean ignoreCreateExceptions) {
      this((Class)clazz, (String)null, ignoreCreateExceptions);
   }

   public FactoryCreateRule(String className, String attributeName, boolean ignoreCreateExceptions) {
      this.attributeName = null;
      this.className = null;
      this.creationFactory = null;
      this.className = className;
      this.attributeName = attributeName;
      this.ignoreCreateExceptions = ignoreCreateExceptions;
   }

   public FactoryCreateRule(Class clazz, String attributeName, boolean ignoreCreateExceptions) {
      this(clazz.getName(), attributeName, ignoreCreateExceptions);
   }

   public FactoryCreateRule(ObjectCreationFactory creationFactory, boolean ignoreCreateExceptions) {
      this.attributeName = null;
      this.className = null;
      this.creationFactory = null;
      this.creationFactory = creationFactory;
      this.ignoreCreateExceptions = ignoreCreateExceptions;
   }

   public void begin(String namespace, String name, Attributes attributes) throws Exception {
      Object instance;
      if (this.ignoreCreateExceptions) {
         if (this.exceptionIgnoredStack == null) {
            this.exceptionIgnoredStack = new ArrayStack();
         }

         try {
            instance = this.getFactory(attributes).createObject(attributes);
            if (this.digester.log.isDebugEnabled()) {
               this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} New " + instance.getClass().getName());
            }

            this.digester.push(instance);
            this.exceptionIgnoredStack.push(Boolean.FALSE);
         } catch (Exception var5) {
            if (this.digester.log.isInfoEnabled()) {
               this.digester.log.info("[FactoryCreateRule] Create exception ignored: " + (var5.getMessage() == null ? var5.getClass().getName() : var5.getMessage()));
               if (this.digester.log.isDebugEnabled()) {
                  this.digester.log.debug("[FactoryCreateRule] Ignored exception:", var5);
               }
            }

            this.exceptionIgnoredStack.push(Boolean.TRUE);
         }
      } else {
         instance = this.getFactory(attributes).createObject(attributes);
         if (this.digester.log.isDebugEnabled()) {
            this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} New " + instance.getClass().getName());
         }

         this.digester.push(instance);
      }

   }

   public void end(String namespace, String name) throws Exception {
      if (this.ignoreCreateExceptions && this.exceptionIgnoredStack != null && !this.exceptionIgnoredStack.empty() && (Boolean)this.exceptionIgnoredStack.pop()) {
         if (this.digester.log.isTraceEnabled()) {
            this.digester.log.trace("[FactoryCreateRule] No creation so no push so no pop");
         }

      } else {
         Object top = this.digester.pop();
         if (this.digester.log.isDebugEnabled()) {
            this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} Pop " + top.getClass().getName());
         }

      }
   }

   public void finish() throws Exception {
      if (this.attributeName != null) {
         this.creationFactory = null;
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("FactoryCreateRule[");
      sb.append("className=");
      sb.append(this.className);
      sb.append(", attributeName=");
      sb.append(this.attributeName);
      if (this.creationFactory != null) {
         sb.append(", creationFactory=");
         sb.append(this.creationFactory);
      }

      sb.append("]");
      return sb.toString();
   }

   protected ObjectCreationFactory getFactory(Attributes attributes) throws Exception {
      if (this.creationFactory == null) {
         String realClassName = this.className;
         if (this.attributeName != null) {
            String value = attributes.getValue(this.attributeName);
            if (value != null) {
               realClassName = value;
            }
         }

         if (this.digester.log.isDebugEnabled()) {
            this.digester.log.debug("[FactoryCreateRule]{" + this.digester.match + "} New factory " + realClassName);
         }

         Class clazz = this.digester.getClassLoader().loadClass(realClassName);
         this.creationFactory = (ObjectCreationFactory)clazz.newInstance();
         this.creationFactory.setDigester(this.digester);
      }

      return this.creationFactory;
   }
}
