package org.jboss.util.property;

import java.util.Properties;

public class PropertyContainer extends PropertyMap {
   private static final long serialVersionUID = -3347198703863412326L;
   protected String groupname;

   public PropertyContainer(Properties props) {
      super(props);
      this.groupname = "<unknown>";
   }

   public PropertyContainer(String groupname) {
      this((Properties)Property.getGroup(groupname));
      this.groupname = groupname;
   }

   public PropertyContainer(Class<?> type) {
      this(type.getName());
   }

   protected void bindField(String name, String propertyName) {
      if (name != null && !name.equals("")) {
         if (propertyName != null && !propertyName.equals("")) {
            this.addPropertyListener(new FieldBoundPropertyListener(this, name, propertyName));
         } else {
            throw new IllegalArgumentException("propertyName");
         }
      } else {
         throw new IllegalArgumentException("name");
      }
   }

   protected void bindField(String name) {
      this.bindField(name, name);
   }

   protected void bindMethod(String name, String propertyName) {
      if (name != null && !name.equals("")) {
         if (propertyName != null && !propertyName.equals("")) {
            this.addPropertyListener(new MethodBoundPropertyListener(this, propertyName, name));
         } else {
            throw new IllegalArgumentException("propertyName");
         }
      } else {
         throw new IllegalArgumentException("name");
      }
   }

   protected void bindMethod(String name) {
      this.bindMethod(name, name);
   }

   private String makeName(String name) {
      return this.groupname + "." + name;
   }

   protected void throwException(String name) throws PropertyException {
      throw new PropertyException(this.makeName(name));
   }

   protected void throwException(String name, String msg) throws PropertyException {
      throw new PropertyException(this.makeName(name) + ": " + msg);
   }

   protected void throwException(String name, String msg, Throwable nested) throws PropertyException {
      throw new PropertyException(this.makeName(name) + ": " + msg, nested);
   }

   protected void throwException(String name, Throwable nested) throws PropertyException {
      throw new PropertyException(this.makeName(name), nested);
   }
}
