package org.apache.commons.digester;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanPropertySetterRule extends Rule {
   protected String propertyName;
   protected String bodyText;

   /** @deprecated */
   public BeanPropertySetterRule(Digester digester, String propertyName) {
      this(propertyName);
   }

   /** @deprecated */
   public BeanPropertySetterRule(Digester digester) {
      this();
   }

   public BeanPropertySetterRule(String propertyName) {
      this.propertyName = null;
      this.bodyText = null;
      this.propertyName = propertyName;
   }

   public BeanPropertySetterRule() {
      this((String)null);
   }

   public void body(String namespace, String name, String text) throws Exception {
      if (this.digester.log.isDebugEnabled()) {
         this.digester.log.debug("[BeanPropertySetterRule]{" + this.digester.match + "} Called with text '" + text + "'");
      }

      this.bodyText = text.trim();
   }

   public void end(String namespace, String name) throws Exception {
      String property = this.propertyName;
      if (property == null) {
         property = name;
      }

      Object top = this.digester.peek();
      if (this.digester.log.isDebugEnabled()) {
         this.digester.log.debug("[BeanPropertySetterRule]{" + this.digester.match + "} Set " + top.getClass().getName() + " property " + property + " with text " + this.bodyText);
      }

      if (top instanceof DynaBean) {
         DynaProperty desc = ((DynaBean)top).getDynaClass().getDynaProperty(property);
         if (desc == null) {
            throw new NoSuchMethodException("Bean has no property named " + property);
         }
      } else {
         PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(top, property);
         if (desc == null) {
            throw new NoSuchMethodException("Bean has no property named " + property);
         }
      }

      BeanUtils.setProperty(top, property, this.bodyText);
   }

   public void finish() throws Exception {
      this.bodyText = null;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("BeanPropertySetterRule[");
      sb.append("propertyName=");
      sb.append(this.propertyName);
      sb.append("]");
      return sb.toString();
   }
}
