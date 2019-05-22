package org.apache.maven.plugin.descriptor;

public class Parameter {
   private String alias;
   private String name;
   private String type;
   private boolean required;
   private boolean editable = true;
   private String description;
   private String expression;
   private String deprecated;
   private String defaultValue;
   private String implementation;
   private Requirement requirement;
   private String since;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean required) {
      this.required = required;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getExpression() {
      return this.expression;
   }

   public void setExpression(String expression) {
      this.expression = expression;
   }

   public String getDeprecated() {
      return this.deprecated;
   }

   public void setDeprecated(String deprecated) {
      this.deprecated = deprecated;
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object other) {
      return other instanceof Parameter && this.getName().equals(((Parameter)other).getName());
   }

   public String getAlias() {
      return this.alias;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public boolean isEditable() {
      return this.editable;
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }

   public void setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public String toString() {
      return "Mojo parameter [name: '" + this.getName() + "'; alias: '" + this.getAlias() + "']";
   }

   public Requirement getRequirement() {
      return this.requirement;
   }

   public void setRequirement(Requirement requirement) {
      this.requirement = requirement;
   }

   public String getImplementation() {
      return this.implementation;
   }

   public void setImplementation(String implementation) {
      this.implementation = implementation;
   }

   public String getSince() {
      return this.since;
   }

   public void setSince(String since) {
      this.since = since;
   }
}
