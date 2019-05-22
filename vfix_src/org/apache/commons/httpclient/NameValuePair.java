package org.apache.commons.httpclient;

import java.io.Serializable;

public class NameValuePair implements Serializable {
   private String name;
   private String value;

   public NameValuePair() {
      this((String)null, (String)null);
   }

   public NameValuePair(String name, String value) {
      this.name = null;
      this.value = null;
      this.name = name;
      this.value = value;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public String toString() {
      return "name=" + this.name + ", " + "value=" + this.value;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (this.getClass().equals(object.getClass())) {
         NameValuePair pair = (NameValuePair)object;
         return (null == this.name ? null == pair.name : this.name.equals(pair.name)) && (null == this.value ? null == pair.value : this.value.equals(pair.value));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.getClass().hashCode() ^ (null == this.name ? 0 : this.name.hashCode()) ^ (null == this.value ? 0 : this.value.hashCode());
   }
}
