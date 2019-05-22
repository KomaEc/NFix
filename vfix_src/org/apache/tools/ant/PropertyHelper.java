package org.apache.tools.ant;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class PropertyHelper {
   private Project project;
   private PropertyHelper next;
   private Hashtable properties = new Hashtable();
   private Hashtable userProperties = new Hashtable();
   private Hashtable inheritedProperties = new Hashtable();

   protected PropertyHelper() {
   }

   public void setProject(Project p) {
      this.project = p;
   }

   public void setNext(PropertyHelper next) {
      this.next = next;
   }

   public PropertyHelper getNext() {
      return this.next;
   }

   public static synchronized PropertyHelper getPropertyHelper(Project project) {
      PropertyHelper helper = (PropertyHelper)project.getReference("ant.PropertyHelper");
      if (helper != null) {
         return helper;
      } else {
         helper = new PropertyHelper();
         helper.setProject(project);
         project.addReference("ant.PropertyHelper", helper);
         return helper;
      }
   }

   public boolean setPropertyHook(String ns, String name, Object value, boolean inherited, boolean user, boolean isNew) {
      if (this.getNext() != null) {
         boolean subst = this.getNext().setPropertyHook(ns, name, value, inherited, user, isNew);
         if (subst) {
            return true;
         }
      }

      return false;
   }

   public Object getPropertyHook(String ns, String name, boolean user) {
      Object v;
      if (this.getNext() != null) {
         v = this.getNext().getPropertyHook(ns, name, user);
         if (v != null) {
            return v;
         }
      }

      if (name.startsWith("toString:")) {
         name = name.substring("toString:".length());
         v = this.project.getReference(name);
         return v == null ? null : v.toString();
      } else {
         return null;
      }
   }

   public void parsePropertyString(String value, Vector fragments, Vector propertyRefs) throws BuildException {
      parsePropertyStringDefault(value, fragments, propertyRefs);
   }

   public String replaceProperties(String ns, String value, Hashtable keys) throws BuildException {
      if (value != null && value.indexOf(36) != -1) {
         Vector fragments = new Vector();
         Vector propertyRefs = new Vector();
         this.parsePropertyString(value, fragments, propertyRefs);
         StringBuffer sb = new StringBuffer();
         Enumeration i = fragments.elements();

         String fragment;
         for(Enumeration j = propertyRefs.elements(); i.hasMoreElements(); sb.append(fragment)) {
            fragment = (String)i.nextElement();
            if (fragment == null) {
               String propertyName = (String)j.nextElement();
               Object replacement = null;
               if (keys != null) {
                  replacement = keys.get(propertyName);
               }

               if (replacement == null) {
                  replacement = this.getProperty(ns, propertyName);
               }

               if (replacement == null) {
                  this.project.log("Property \"" + propertyName + "\" has not been set", 3);
               }

               fragment = replacement != null ? replacement.toString() : "${" + propertyName + "}";
            }
         }

         return sb.toString();
      } else {
         return value;
      }
   }

   public synchronized boolean setProperty(String ns, String name, Object value, boolean verbose) {
      if (null != this.userProperties.get(name)) {
         if (verbose) {
            this.project.log("Override ignored for user property \"" + name + "\"", 3);
         }

         return false;
      } else {
         boolean done = this.setPropertyHook(ns, name, value, false, false, false);
         if (done) {
            return true;
         } else {
            if (null != this.properties.get(name) && verbose) {
               this.project.log("Overriding previous definition of property \"" + name + "\"", 3);
            }

            if (verbose) {
               this.project.log("Setting project property: " + name + " -> " + value, 4);
            }

            this.properties.put(name, value);
            return true;
         }
      }
   }

   public synchronized void setNewProperty(String ns, String name, Object value) {
      if (null != this.properties.get(name)) {
         this.project.log("Override ignored for property \"" + name + "\"", 3);
      } else {
         boolean done = this.setPropertyHook(ns, name, value, false, false, true);
         if (!done) {
            this.project.log("Setting project property: " + name + " -> " + value, 4);
            if (name != null && value != null) {
               this.properties.put(name, value);
            }

         }
      }
   }

   public synchronized void setUserProperty(String ns, String name, Object value) {
      this.project.log("Setting ro project property: " + name + " -> " + value, 4);
      this.userProperties.put(name, value);
      boolean done = this.setPropertyHook(ns, name, value, false, true, false);
      if (!done) {
         this.properties.put(name, value);
      }
   }

   public synchronized void setInheritedProperty(String ns, String name, Object value) {
      this.inheritedProperties.put(name, value);
      this.project.log("Setting ro project property: " + name + " -> " + value, 4);
      this.userProperties.put(name, value);
      boolean done = this.setPropertyHook(ns, name, value, true, false, false);
      if (!done) {
         this.properties.put(name, value);
      }
   }

   public synchronized Object getProperty(String ns, String name) {
      if (name == null) {
         return null;
      } else {
         Object o = this.getPropertyHook(ns, name, false);
         return o != null ? o : this.properties.get(name);
      }
   }

   public synchronized Object getUserProperty(String ns, String name) {
      if (name == null) {
         return null;
      } else {
         Object o = this.getPropertyHook(ns, name, true);
         return o != null ? o : this.userProperties.get(name);
      }
   }

   public Hashtable getProperties() {
      return new Hashtable(this.properties);
   }

   public Hashtable getUserProperties() {
      return new Hashtable(this.userProperties);
   }

   protected Hashtable getInternalProperties() {
      return this.properties;
   }

   protected Hashtable getInternalUserProperties() {
      return this.userProperties;
   }

   protected Hashtable getInternalInheritedProperties() {
      return this.inheritedProperties;
   }

   public void copyInheritedProperties(Project other) {
      Enumeration e = this.inheritedProperties.keys();

      while(e.hasMoreElements()) {
         String arg = e.nextElement().toString();
         if (other.getUserProperty(arg) == null) {
            Object value = this.inheritedProperties.get(arg);
            other.setInheritedProperty(arg, value.toString());
         }
      }

   }

   public void copyUserProperties(Project other) {
      Enumeration e = this.userProperties.keys();

      while(e.hasMoreElements()) {
         Object arg = e.nextElement();
         if (!this.inheritedProperties.containsKey(arg)) {
            Object value = this.userProperties.get(arg);
            other.setUserProperty(arg.toString(), value.toString());
         }
      }

   }

   static void parsePropertyStringDefault(String value, Vector fragments, Vector propertyRefs) throws BuildException {
      int prev = 0;

      int pos;
      while((pos = value.indexOf("$", prev)) >= 0) {
         if (pos > 0) {
            fragments.addElement(value.substring(prev, pos));
         }

         if (pos == value.length() - 1) {
            fragments.addElement("$");
            prev = pos + 1;
         } else if (value.charAt(pos + 1) != '{') {
            if (value.charAt(pos + 1) == '$') {
               fragments.addElement("$");
               prev = pos + 2;
            } else {
               fragments.addElement(value.substring(pos, pos + 2));
               prev = pos + 2;
            }
         } else {
            int endName = value.indexOf(125, pos);
            if (endName < 0) {
               throw new BuildException("Syntax error in property: " + value);
            }

            String propertyName = value.substring(pos + 2, endName);
            fragments.addElement((Object)null);
            propertyRefs.addElement(propertyName);
            prev = endName + 1;
         }
      }

      if (prev < value.length()) {
         fragments.addElement(value.substring(prev));
      }

   }
}
