package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Contributor implements Serializable {
   private String name;
   private String email;
   private String url;
   private String organization;
   private String organizationUrl;
   private List<String> roles;
   private String timezone;
   private Properties properties;

   public void addProperty(String key, String value) {
      this.getProperties().put(key, value);
   }

   public void addRole(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Contributor.addRoles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getRoles().add(string);
      }
   }

   public String getEmail() {
      return this.email;
   }

   public String getName() {
      return this.name;
   }

   public String getOrganization() {
      return this.organization;
   }

   public String getOrganizationUrl() {
      return this.organizationUrl;
   }

   public Properties getProperties() {
      if (this.properties == null) {
         this.properties = new Properties();
      }

      return this.properties;
   }

   public List<String> getRoles() {
      if (this.roles == null) {
         this.roles = new ArrayList();
      }

      return this.roles;
   }

   public String getTimezone() {
      return this.timezone;
   }

   public String getUrl() {
      return this.url;
   }

   public void removeRole(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Contributor.removeRoles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getRoles().remove(string);
      }
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setOrganization(String organization) {
      this.organization = organization;
   }

   public void setOrganizationUrl(String organizationUrl) {
      this.organizationUrl = organizationUrl;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public void setRoles(List<String> roles) {
      this.roles = roles;
   }

   public void setTimezone(String timezone) {
      this.timezone = timezone;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
