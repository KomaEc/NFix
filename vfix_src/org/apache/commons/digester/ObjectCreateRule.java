package org.apache.commons.digester;

import org.xml.sax.Attributes;

public class ObjectCreateRule extends Rule {
   protected String attributeName;
   protected String className;

   /** @deprecated */
   public ObjectCreateRule(Digester digester, String className) {
      this(className);
   }

   /** @deprecated */
   public ObjectCreateRule(Digester digester, Class clazz) {
      this(clazz);
   }

   /** @deprecated */
   public ObjectCreateRule(Digester digester, String className, String attributeName) {
      this(className, attributeName);
   }

   /** @deprecated */
   public ObjectCreateRule(Digester digester, String attributeName, Class clazz) {
      this(attributeName, clazz);
   }

   public ObjectCreateRule(String className) {
      this(className, (String)null);
   }

   public ObjectCreateRule(Class clazz) {
      this(clazz.getName(), (String)null);
   }

   public ObjectCreateRule(String className, String attributeName) {
      this.attributeName = null;
      this.className = null;
      this.className = className;
      this.attributeName = attributeName;
   }

   public ObjectCreateRule(String attributeName, Class clazz) {
      this(clazz.getName(), attributeName);
   }

   public void begin(Attributes attributes) throws Exception {
      String realClassName = this.className;
      if (this.attributeName != null) {
         String value = attributes.getValue(this.attributeName);
         if (value != null) {
            realClassName = value;
         }
      }

      if (this.digester.log.isDebugEnabled()) {
         this.digester.log.debug("[ObjectCreateRule]{" + this.digester.match + "}New " + realClassName);
      }

      Class clazz = this.digester.getClassLoader().loadClass(realClassName);
      Object instance = clazz.newInstance();
      this.digester.push(instance);
   }

   public void end() throws Exception {
      Object top = this.digester.pop();
      if (this.digester.log.isDebugEnabled()) {
         this.digester.log.debug("[ObjectCreateRule]{" + this.digester.match + "} Pop " + top.getClass().getName());
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("ObjectCreateRule[");
      sb.append("className=");
      sb.append(this.className);
      sb.append(", attributeName=");
      sb.append(this.attributeName);
      sb.append("]");
      return sb.toString();
   }
}
