package org.apache.tools.ant.taskdefs;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;

public class MacroInstance extends Task implements DynamicAttribute, TaskContainer {
   private MacroDef macroDef;
   private Map map = new HashMap();
   private Map nsElements = null;
   private Map presentElements;
   private Hashtable localAttributes;
   private String text = null;
   private String implicitTag = null;
   private List unknownElements = new ArrayList();
   private static final int STATE_NORMAL = 0;
   private static final int STATE_EXPECT_BRACKET = 1;
   private static final int STATE_EXPECT_NAME = 2;

   public void setMacroDef(MacroDef macroDef) {
      this.macroDef = macroDef;
   }

   public MacroDef getMacroDef() {
      return this.macroDef;
   }

   public void setDynamicAttribute(String name, String value) {
      this.map.put(name, value);
   }

   /** @deprecated */
   public Object createDynamicElement(String name) throws BuildException {
      throw new BuildException("Not implemented any more");
   }

   private Map getNsElements() {
      if (this.nsElements == null) {
         this.nsElements = new HashMap();
         Iterator i = this.macroDef.getElements().entrySet().iterator();

         while(i.hasNext()) {
            Entry entry = (Entry)i.next();
            this.nsElements.put((String)entry.getKey(), entry.getValue());
            MacroDef.TemplateElement te = (MacroDef.TemplateElement)entry.getValue();
            if (te.isImplicit()) {
               this.implicitTag = te.getName();
            }
         }
      }

      return this.nsElements;
   }

   public void addTask(Task nestedTask) {
      this.unknownElements.add(nestedTask);
   }

   private void processTasks() {
      if (this.implicitTag == null) {
         Iterator i = this.unknownElements.iterator();

         while(i.hasNext()) {
            UnknownElement ue = (UnknownElement)i.next();
            String name = ProjectHelper.extractNameFromComponentName(ue.getTag()).toLowerCase(Locale.US);
            if (this.getNsElements().get(name) == null) {
               throw new BuildException("unsupported element " + name);
            }

            if (this.presentElements.get(name) != null) {
               throw new BuildException("Element " + name + " already present");
            }

            this.presentElements.put(name, ue);
         }

      }
   }

   private String macroSubs(String s, Map macroMapping) {
      if (s == null) {
         return null;
      } else {
         StringBuffer ret = new StringBuffer();
         StringBuffer macroName = null;
         int state = 0;

         for(int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            switch(state) {
            case 0:
               if (ch == '@') {
                  state = 1;
               } else {
                  ret.append(ch);
               }
               break;
            case 1:
               if (ch == '{') {
                  state = 2;
                  macroName = new StringBuffer();
               } else if (ch == '@') {
                  state = 0;
                  ret.append('@');
               } else {
                  state = 0;
                  ret.append('@');
                  ret.append(ch);
               }
               break;
            case 2:
               if (ch == '}') {
                  state = 0;
                  String name = macroName.toString().toLowerCase(Locale.US);
                  String value = (String)macroMapping.get(name);
                  if (value == null) {
                     ret.append("@{");
                     ret.append(name);
                     ret.append("}");
                  } else {
                     ret.append(value);
                  }

                  macroName = null;
               } else {
                  macroName.append(ch);
               }
            }
         }

         switch(state) {
         case 0:
         default:
            break;
         case 1:
            ret.append('@');
            break;
         case 2:
            ret.append("@{");
            ret.append(macroName.toString());
         }

         return ret.toString();
      }
   }

   public void addText(String text) {
      this.text = text;
   }

   private UnknownElement copy(UnknownElement ue) {
      UnknownElement ret = new UnknownElement(ue.getTag());
      ret.setNamespace(ue.getNamespace());
      ret.setProject(this.getProject());
      ret.setQName(ue.getQName());
      ret.setTaskType(ue.getTaskType());
      ret.setTaskName(ue.getTaskName());
      ret.setLocation(this.macroDef.getBackTrace() ? ue.getLocation() : this.getLocation());
      if (this.getOwningTarget() == null) {
         Target t = new Target();
         t.setProject(this.getProject());
         ret.setOwningTarget(t);
      } else {
         ret.setOwningTarget(this.getOwningTarget());
      }

      RuntimeConfigurable rc = new RuntimeConfigurable(ret, ue.getTaskName());
      rc.setPolyType(ue.getWrapper().getPolyType());
      Map m = ue.getWrapper().getAttributeMap();
      Iterator i = m.entrySet().iterator();

      while(i.hasNext()) {
         Entry entry = (Entry)i.next();
         rc.setAttribute((String)entry.getKey(), this.macroSubs((String)entry.getValue(), this.localAttributes));
      }

      rc.addText(this.macroSubs(ue.getWrapper().getText().toString(), this.localAttributes));
      Enumeration e = ue.getWrapper().getChildren();

      while(true) {
         while(e.hasMoreElements()) {
            RuntimeConfigurable r = (RuntimeConfigurable)e.nextElement();
            UnknownElement unknownElement = (UnknownElement)r.getProxy();
            String tag = unknownElement.getTaskType();
            if (tag != null) {
               tag = tag.toLowerCase(Locale.US);
            }

            MacroDef.TemplateElement templateElement = (MacroDef.TemplateElement)this.getNsElements().get(tag);
            UnknownElement presentElement;
            if (templateElement == null) {
               presentElement = this.copy(unknownElement);
               rc.addChild(presentElement.getWrapper());
               ret.addChild(presentElement);
            } else if (templateElement.isImplicit()) {
               if (this.unknownElements.size() == 0 && !templateElement.isOptional()) {
                  throw new BuildException("Missing nested elements for implicit element " + templateElement.getName());
               }

               Iterator i = this.unknownElements.iterator();

               while(i.hasNext()) {
                  UnknownElement child = this.copy((UnknownElement)i.next());
                  rc.addChild(child.getWrapper());
                  ret.addChild(child);
               }
            } else {
               presentElement = (UnknownElement)this.presentElements.get(tag);
               if (presentElement == null) {
                  if (!templateElement.isOptional()) {
                     throw new BuildException("Required nested element " + templateElement.getName() + " missing");
                  }
               } else {
                  String presentText = presentElement.getWrapper().getText().toString();
                  if (!"".equals(presentText)) {
                     rc.addText(this.macroSubs(presentText, this.localAttributes));
                  }

                  List list = presentElement.getChildren();
                  if (list != null) {
                     Iterator i = list.iterator();

                     while(i.hasNext()) {
                        UnknownElement child = this.copy((UnknownElement)i.next());
                        rc.addChild(child.getWrapper());
                        ret.addChild(child);
                     }
                  }
               }
            }
         }

         return ret;
      }
   }

   public void execute() {
      this.presentElements = new HashMap();
      this.getNsElements();
      this.processTasks();
      this.localAttributes = new Hashtable();
      Set copyKeys = new HashSet(this.map.keySet());
      Iterator i = this.macroDef.getAttributes().iterator();

      while(i.hasNext()) {
         MacroDef.Attribute attribute = (MacroDef.Attribute)i.next();
         String value = (String)this.map.get(attribute.getName());
         if (value == null && "description".equals(attribute.getName())) {
            value = this.getDescription();
         }

         if (value == null) {
            value = attribute.getDefault();
            value = this.macroSubs(value, this.localAttributes);
         }

         if (value == null) {
            throw new BuildException("required attribute " + attribute.getName() + " not set");
         }

         this.localAttributes.put(attribute.getName(), value);
         copyKeys.remove(attribute.getName());
      }

      if (copyKeys.contains("id")) {
         copyKeys.remove("id");
      }

      if (this.macroDef.getText() != null) {
         if (this.text == null) {
            if (!this.macroDef.getText().getOptional()) {
               throw new BuildException("required text missing");
            }

            this.text = "";
         }

         if (this.macroDef.getText().getTrim()) {
            this.text = this.text.trim();
         }

         this.localAttributes.put(this.macroDef.getText().getName(), this.text);
      } else if (this.text != null && !this.text.trim().equals("")) {
         throw new BuildException("The \"" + this.getTaskName() + "\" macro does not support" + " nested text data.");
      }

      if (copyKeys.size() != 0) {
         throw new BuildException("Unknown attribute" + (copyKeys.size() > 1 ? "s " : " ") + copyKeys);
      } else {
         UnknownElement c = this.copy(this.macroDef.getNestedTask());
         c.init();

         try {
            c.perform();
         } catch (BuildException var8) {
            if (this.macroDef.getBackTrace()) {
               throw ProjectHelper.addLocationToBuildException(var8, this.getLocation());
            }

            var8.setLocation(this.getLocation());
            throw var8;
         } finally {
            this.presentElements = null;
            this.localAttributes = null;
         }

      }
   }

   public static class Element implements TaskContainer {
      private List unknownElements = new ArrayList();

      public void addTask(Task nestedTask) {
         this.unknownElements.add(nestedTask);
      }

      public List getUnknownElements() {
         return this.unknownElements;
      }
   }
}
