package org.apache.commons.validator;

import java.io.Serializable;

public class Arg implements Cloneable, Serializable {
   protected String bundle = null;
   protected String key = null;
   protected String name = null;
   protected int position = -1;
   protected boolean resource = true;

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2.toString());
      }
   }

   public String getBundle() {
      return this.bundle;
   }

   public String getKey() {
      return this.key;
   }

   public String getName() {
      return this.name;
   }

   public int getPosition() {
      return this.position;
   }

   public boolean isResource() {
      return this.resource;
   }

   public void setBundle(String bundle) {
      this.bundle = bundle;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPosition(int position) {
      this.position = position;
   }

   public void setResource(boolean resource) {
      this.resource = resource;
   }

   public String toString() {
      StringBuffer results = new StringBuffer();
      results.append("Arg: name=");
      results.append(this.name);
      results.append("  key=");
      results.append(this.key);
      results.append("  position=");
      results.append(this.position);
      results.append("  bundle=");
      results.append(this.bundle);
      results.append("  resource=");
      results.append(this.resource);
      results.append("\n");
      return results.toString();
   }
}
