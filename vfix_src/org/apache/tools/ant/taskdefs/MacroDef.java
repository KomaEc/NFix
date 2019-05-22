package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;

public class MacroDef extends AntlibDefinition {
   private MacroDef.NestedSequential nestedSequential;
   private String name;
   private boolean backTrace = true;
   private List attributes = new ArrayList();
   private Map elements = new HashMap();
   private String textName = null;
   private MacroDef.Text text = null;
   private boolean hasImplicitElement = false;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$MacroInstance;

   public void setName(String name) {
      this.name = name;
   }

   public void addConfiguredText(MacroDef.Text text) {
      if (this.text != null) {
         throw new BuildException("Only one nested text element allowed");
      } else if (text.getName() == null) {
         throw new BuildException("the text nested element needed a \"name\" attribute");
      } else {
         Iterator i = this.attributes.iterator();

         MacroDef.Attribute attribute;
         do {
            if (!i.hasNext()) {
               this.text = text;
               this.textName = text.getName();
               return;
            }

            attribute = (MacroDef.Attribute)i.next();
         } while(!text.getName().equals(attribute.getName()));

         throw new BuildException("the name \"" + text.getName() + "\" is already used as an attribute");
      }
   }

   public MacroDef.Text getText() {
      return this.text;
   }

   public void setBackTrace(boolean backTrace) {
      this.backTrace = backTrace;
   }

   public boolean getBackTrace() {
      return this.backTrace;
   }

   public MacroDef.NestedSequential createSequential() {
      if (this.nestedSequential != null) {
         throw new BuildException("Only one sequential allowed");
      } else {
         this.nestedSequential = new MacroDef.NestedSequential();
         return this.nestedSequential;
      }
   }

   public UnknownElement getNestedTask() {
      UnknownElement ret = new UnknownElement("sequential");
      ret.setTaskName("sequential");
      ret.setNamespace("");
      ret.setQName("sequential");
      new RuntimeConfigurable(ret, "sequential");

      for(int i = 0; i < this.nestedSequential.getNested().size(); ++i) {
         UnknownElement e = (UnknownElement)this.nestedSequential.getNested().get(i);
         ret.addChild(e);
         ret.getWrapper().addChild(e.getWrapper());
      }

      return ret;
   }

   public List getAttributes() {
      return this.attributes;
   }

   public Map getElements() {
      return this.elements;
   }

   public static boolean isValidNameCharacter(char c) {
      return Character.isLetterOrDigit(c) || c == '.' || c == '-';
   }

   private static boolean isValidName(String name) {
      if (name.length() == 0) {
         return false;
      } else {
         for(int i = 0; i < name.length(); ++i) {
            if (!isValidNameCharacter(name.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public void addConfiguredAttribute(MacroDef.Attribute attribute) {
      if (attribute.getName() == null) {
         throw new BuildException("the attribute nested element needed a \"name\" attribute");
      } else if (attribute.getName().equals(this.textName)) {
         throw new BuildException("the name \"" + attribute.getName() + "\" has already been used by the text element");
      } else {
         for(int i = 0; i < this.attributes.size(); ++i) {
            MacroDef.Attribute att = (MacroDef.Attribute)this.attributes.get(i);
            if (att.getName().equals(attribute.getName())) {
               throw new BuildException("the name \"" + attribute.getName() + "\" has already been used in " + "another attribute element");
            }
         }

         this.attributes.add(attribute);
      }
   }

   public void addConfiguredElement(MacroDef.TemplateElement element) {
      if (element.getName() == null) {
         throw new BuildException("the element nested element needed a \"name\" attribute");
      } else if (this.elements.get(element.getName()) != null) {
         throw new BuildException("the element " + element.getName() + " has already been specified");
      } else if (!this.hasImplicitElement && (!element.isImplicit() || this.elements.size() == 0)) {
         this.hasImplicitElement = element.isImplicit();
         this.elements.put(element.getName(), element);
      } else {
         throw new BuildException("Only one element allowed when using implicit elements");
      }
   }

   public void execute() {
      if (this.nestedSequential == null) {
         throw new BuildException("Missing sequential element");
      } else if (this.name == null) {
         throw new BuildException("Name not specified");
      } else {
         this.name = ProjectHelper.genComponentName(this.getURI(), this.name);
         MacroDef.MyAntTypeDefinition def = new MacroDef.MyAntTypeDefinition(this);
         def.setName(this.name);
         def.setClass(class$org$apache$tools$ant$taskdefs$MacroInstance == null ? (class$org$apache$tools$ant$taskdefs$MacroInstance = class$("org.apache.tools.ant.taskdefs.MacroInstance")) : class$org$apache$tools$ant$taskdefs$MacroInstance);
         ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
         helper.addDataTypeDefinition(def);
         this.log("creating macro  " + this.name, 3);
      }
   }

   private boolean sameOrSimilar(Object obj, boolean same) {
      if (obj == this) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!obj.getClass().equals(this.getClass())) {
         return false;
      } else {
         MacroDef other = (MacroDef)obj;
         if (this.name == null) {
            return other.name == null;
         } else if (!this.name.equals(other.name)) {
            return false;
         } else if (other.getLocation() != null && other.getLocation().equals(this.getLocation()) && !same) {
            return true;
         } else {
            if (this.text == null) {
               if (other.text != null) {
                  return false;
               }
            } else if (!this.text.equals(other.text)) {
               return false;
            }

            if (this.getURI() != null && !this.getURI().equals("") && !this.getURI().equals("antlib:org.apache.tools.ant")) {
               if (!this.getURI().equals(other.getURI())) {
                  return false;
               }
            } else if (other.getURI() != null && !other.getURI().equals("") && !other.getURI().equals("antlib:org.apache.tools.ant")) {
               return false;
            }

            if (!this.nestedSequential.similar(other.nestedSequential)) {
               return false;
            } else if (!this.attributes.equals(other.attributes)) {
               return false;
            } else {
               return this.elements.equals(other.elements);
            }
         }
      }
   }

   public boolean similar(Object obj) {
      return this.sameOrSimilar(obj, false);
   }

   public boolean sameDefinition(Object obj) {
      return this.sameOrSimilar(obj, true);
   }

   private static int objectHashCode(Object o) {
      return o == null ? 0 : o.hashCode();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static class MyAntTypeDefinition extends AntTypeDefinition {
      private MacroDef macroDef;

      public MyAntTypeDefinition(MacroDef macroDef) {
         this.macroDef = macroDef;
      }

      public Object create(Project project) {
         Object o = super.create(project);
         if (o == null) {
            return null;
         } else {
            ((MacroInstance)o).setMacroDef(this.macroDef);
            return o;
         }
      }

      public boolean sameDefinition(AntTypeDefinition other, Project project) {
         if (!super.sameDefinition(other, project)) {
            return false;
         } else {
            MacroDef.MyAntTypeDefinition otherDef = (MacroDef.MyAntTypeDefinition)other;
            return this.macroDef.sameDefinition(otherDef.macroDef);
         }
      }

      public boolean similarDefinition(AntTypeDefinition other, Project project) {
         if (!super.similarDefinition(other, project)) {
            return false;
         } else {
            MacroDef.MyAntTypeDefinition otherDef = (MacroDef.MyAntTypeDefinition)other;
            return this.macroDef.similar(otherDef.macroDef);
         }
      }
   }

   public static class TemplateElement {
      private String name;
      private String description;
      private boolean optional = false;
      private boolean implicit = false;

      public void setName(String name) {
         if (!MacroDef.isValidName(name)) {
            throw new BuildException("Illegal name [" + name + "] for macro element");
         } else {
            this.name = name.toLowerCase(Locale.US);
         }
      }

      public String getName() {
         return this.name;
      }

      public void setDescription(String desc) {
         this.description = desc;
      }

      public String getDescription() {
         return this.description;
      }

      public void setOptional(boolean optional) {
         this.optional = optional;
      }

      public boolean isOptional() {
         return this.optional;
      }

      public void setImplicit(boolean implicit) {
         this.implicit = implicit;
      }

      public boolean isImplicit() {
         return this.implicit;
      }

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (obj != null && obj.getClass().equals(this.getClass())) {
            boolean var10000;
            label26: {
               MacroDef.TemplateElement t = (MacroDef.TemplateElement)obj;
               if (this.name == null) {
                  if (t.name != null) {
                     break label26;
                  }
               } else if (!this.name.equals(t.name)) {
                  break label26;
               }

               if (this.optional == t.optional && this.implicit == t.implicit) {
                  var10000 = true;
                  return var10000;
               }
            }

            var10000 = false;
            return var10000;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return MacroDef.objectHashCode(this.name) + (this.optional ? 1 : 0) + (this.implicit ? 1 : 0);
      }
   }

   public static class Text {
      private String name;
      private boolean optional;
      private boolean trim;
      private String description;

      public void setName(String name) {
         if (!MacroDef.isValidName(name)) {
            throw new BuildException("Illegal name [" + name + "] for attribute");
         } else {
            this.name = name.toLowerCase(Locale.US);
         }
      }

      public String getName() {
         return this.name;
      }

      public void setOptional(boolean optional) {
         this.optional = optional;
      }

      public boolean getOptional() {
         return this.optional;
      }

      public void setTrim(boolean trim) {
         this.trim = trim;
      }

      public boolean getTrim() {
         return this.trim;
      }

      public void setDescription(String desc) {
         this.description = desc;
      }

      public String getDescription() {
         return this.description;
      }

      public boolean equals(Object obj) {
         if (obj == null) {
            return false;
         } else if (obj.getClass() != this.getClass()) {
            return false;
         } else {
            MacroDef.Text other = (MacroDef.Text)obj;
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.optional != other.optional) {
               return false;
            } else {
               return this.trim == other.trim;
            }
         }
      }

      public int hashCode() {
         return MacroDef.objectHashCode(this.name);
      }
   }

   public static class Attribute {
      private String name;
      private String defaultValue;
      private String description;

      public void setName(String name) {
         if (!MacroDef.isValidName(name)) {
            throw new BuildException("Illegal name [" + name + "] for attribute");
         } else {
            this.name = name.toLowerCase(Locale.US);
         }
      }

      public String getName() {
         return this.name;
      }

      public void setDefault(String defaultValue) {
         this.defaultValue = defaultValue;
      }

      public String getDefault() {
         return this.defaultValue;
      }

      public void setDescription(String desc) {
         this.description = desc;
      }

      public String getDescription() {
         return this.description;
      }

      public boolean equals(Object obj) {
         if (obj == null) {
            return false;
         } else if (obj.getClass() != this.getClass()) {
            return false;
         } else {
            MacroDef.Attribute other = (MacroDef.Attribute)obj;
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            if (this.defaultValue == null) {
               if (other.defaultValue != null) {
                  return false;
               }
            } else if (!this.defaultValue.equals(other.defaultValue)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         return MacroDef.objectHashCode(this.defaultValue) + MacroDef.objectHashCode(this.name);
      }
   }

   public static class NestedSequential implements TaskContainer {
      private List nested = new ArrayList();

      public void addTask(Task task) {
         this.nested.add(task);
      }

      public List getNested() {
         return this.nested;
      }

      public boolean similar(MacroDef.NestedSequential other) {
         if (this.nested.size() != other.nested.size()) {
            return false;
         } else {
            for(int i = 0; i < this.nested.size(); ++i) {
               UnknownElement me = (UnknownElement)this.nested.get(i);
               UnknownElement o = (UnknownElement)other.nested.get(i);
               if (!me.similar(o)) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
