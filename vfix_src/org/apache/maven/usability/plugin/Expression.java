package org.apache.maven.usability.plugin;

import java.io.Serializable;
import java.util.Properties;

public class Expression implements Serializable {
   private String syntax;
   private String description;
   private String configuration;
   private Properties cliOptions;
   private Properties apiMethods;
   private String deprecation;
   private String ban;
   private boolean editable = true;
   private String modelEncoding = "UTF-8";

   public void addApiMethod(String key, String value) {
      this.getApiMethods().put(key, value);
   }

   public void addCliOption(String key, String value) {
      this.getCliOptions().put(key, value);
   }

   public Properties getApiMethods() {
      if (this.apiMethods == null) {
         this.apiMethods = new Properties();
      }

      return this.apiMethods;
   }

   public String getBan() {
      return this.ban;
   }

   public Properties getCliOptions() {
      if (this.cliOptions == null) {
         this.cliOptions = new Properties();
      }

      return this.cliOptions;
   }

   public String getConfiguration() {
      return this.configuration;
   }

   public String getDeprecation() {
      return this.deprecation;
   }

   public String getDescription() {
      return this.description;
   }

   public String getSyntax() {
      return this.syntax;
   }

   public boolean isEditable() {
      return this.editable;
   }

   public void setApiMethods(Properties apiMethods) {
      this.apiMethods = apiMethods;
   }

   public void setBan(String ban) {
      this.ban = ban;
   }

   public void setCliOptions(Properties cliOptions) {
      this.cliOptions = cliOptions;
   }

   public void setConfiguration(String configuration) {
      this.configuration = configuration;
   }

   public void setDeprecation(String deprecation) {
      this.deprecation = deprecation;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }

   public void setSyntax(String syntax) {
      this.syntax = syntax;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
