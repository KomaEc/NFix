package org.codehaus.plexus.component.repository;

public final class ComponentRequirement {
   private String role;
   private String roleHint;
   private String fieldName;
   private String fieldMappingType;

   public String getFieldName() {
      return this.fieldName;
   }

   public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public String getRoleHint() {
      return this.roleHint;
   }

   public void setRoleHint(String roleHint) {
      this.roleHint = roleHint;
   }

   public String getRequirementKey() {
      return this.getRoleHint() != null ? this.getRole() + this.getRoleHint() : this.getRole();
   }

   public String toString() {
      return "ComponentRequirement{role='" + this.role + "'" + ", roleHint='" + this.roleHint + "'" + ", fieldName='" + this.fieldName + "'" + "}";
   }

   public String getHumanReadableKey() {
      StringBuffer key = new StringBuffer();
      key.append("role: '");
      key.append(this.getRole());
      key.append("'");
      if (this.getRoleHint() != null) {
         key.append(", role-hint: '");
         key.append(this.getRoleHint());
         key.append("'. ");
      }

      if (this.getFieldName() != null) {
         key.append(", field name: '");
         key.append(this.getFieldName());
         key.append("' ");
      }

      String retValue = key.toString();
      return retValue;
   }

   public String getFieldMappingType() {
      return this.fieldMappingType;
   }

   public void setFieldMappingType(String fieldType) {
      this.fieldMappingType = fieldType;
   }

   public boolean equals(Object other) {
      if (other instanceof ComponentRequirement) {
         String myId = this.role + ":" + this.roleHint;
         ComponentRequirement req = (ComponentRequirement)other;
         String otherId = req.role + ":" + req.roleHint;
         return myId.equals(otherId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (this.role + ":" + this.roleHint).hashCode();
   }
}
