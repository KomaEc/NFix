package org.apache.commons.validator;

import java.io.Serializable;

public class Msg implements Cloneable, Serializable {
   protected String bundle = null;
   protected String key = null;
   protected String name = null;
   protected boolean resource = true;

   public String getBundle() {
      return this.bundle;
   }

   public void setBundle(String bundle) {
      this.bundle = bundle;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public boolean isResource() {
      return this.resource;
   }

   public void setResource(boolean resource) {
      this.resource = resource;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public String toString() {
      StringBuffer results = new StringBuffer();
      results.append("Msg: name=");
      results.append(this.name);
      results.append("  key=");
      results.append(this.key);
      results.append("  resource=");
      results.append(this.resource);
      results.append("  bundle=");
      results.append(this.bundle);
      results.append("\n");
      return results.toString();
   }
}
