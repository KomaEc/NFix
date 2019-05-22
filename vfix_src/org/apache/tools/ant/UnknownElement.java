package org.apache.tools.ant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.tools.ant.taskdefs.PreSetDef;

public class UnknownElement extends Task {
   private String elementName;
   private String namespace = "";
   private String qname;
   private Object realThing;
   private List children = null;
   private boolean presetDefed = false;

   public UnknownElement(String elementName) {
      this.elementName = elementName;
   }

   public List getChildren() {
      return this.children;
   }

   public String getTag() {
      return this.elementName;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void setNamespace(String namespace) {
      if (namespace.equals("ant:current")) {
         ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
         namespace = helper.getCurrentAntlibUri();
      }

      this.namespace = namespace == null ? "" : namespace;
   }

   public String getQName() {
      return this.qname;
   }

   public void setQName(String qname) {
      this.qname = qname;
   }

   public RuntimeConfigurable getWrapper() {
      return super.getWrapper();
   }

   public void maybeConfigure() throws BuildException {
      if (this.realThing == null) {
         this.configure(this.makeObject(this, this.getWrapper()));
      }
   }

   public void configure(Object realObject) {
      this.realThing = realObject;
      this.getWrapper().setProxy(this.realThing);
      Task task = null;
      if (this.realThing instanceof Task) {
         task = (Task)this.realThing;
         task.setRuntimeConfigurableWrapper(this.getWrapper());
         if (this.getWrapper().getId() != null) {
            this.getOwningTarget().replaceChild(this, (Task)((Task)this.realThing));
         }
      }

      if (task != null) {
         task.maybeConfigure();
      } else {
         this.getWrapper().maybeConfigure(this.getProject());
      }

      this.handleChildren(this.realThing, this.getWrapper());
   }

   protected void handleOutput(String output) {
      if (this.realThing instanceof Task) {
         ((Task)this.realThing).handleOutput(output);
      } else {
         super.handleOutput(output);
      }

   }

   protected int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.realThing instanceof Task ? ((Task)this.realThing).handleInput(buffer, offset, length) : super.handleInput(buffer, offset, length);
   }

   protected void handleFlush(String output) {
      if (this.realThing instanceof Task) {
         ((Task)this.realThing).handleFlush(output);
      } else {
         super.handleFlush(output);
      }

   }

   protected void handleErrorOutput(String output) {
      if (this.realThing instanceof Task) {
         ((Task)this.realThing).handleErrorOutput(output);
      } else {
         super.handleErrorOutput(output);
      }

   }

   protected void handleErrorFlush(String output) {
      if (this.realThing instanceof Task) {
         ((Task)this.realThing).handleErrorOutput(output);
      } else {
         super.handleErrorOutput(output);
      }

   }

   public void execute() {
      if (this.realThing == null) {
         throw new BuildException("Could not create task of type: " + this.elementName, this.getLocation());
      } else {
         if (this.realThing instanceof Task) {
            ((Task)this.realThing).execute();
         }

         this.realThing = null;
         this.getWrapper().setProxy((Object)null);
      }
   }

   public void addChild(UnknownElement child) {
      if (this.children == null) {
         this.children = new ArrayList();
      }

      this.children.add(child);
   }

   protected void handleChildren(Object parent, RuntimeConfigurable parentWrapper) throws BuildException {
      if (parent instanceof TypeAdapter) {
         parent = ((TypeAdapter)parent).getProxy();
      }

      String parentUri = this.getNamespace();
      Class parentClass = parent.getClass();
      IntrospectionHelper ih = IntrospectionHelper.getHelper(this.getProject(), parentClass);
      if (this.children != null) {
         Iterator it = this.children.iterator();

         for(int i = 0; it.hasNext(); ++i) {
            RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
            UnknownElement child = (UnknownElement)it.next();

            try {
               if (!this.handleChild(parentUri, ih, parent, child, childWrapper)) {
                  if (!(parent instanceof TaskContainer)) {
                     ih.throwNotSupported(this.getProject(), parent, child.getTag());
                  } else {
                     TaskContainer container = (TaskContainer)parent;
                     container.addTask(child);
                  }
               }
            } catch (UnsupportedElementException var11) {
               throw new BuildException(parentWrapper.getElementTag() + " doesn't support the nested \"" + var11.getElement() + "\" element.", var11);
            }
         }
      }

   }

   protected String getComponentName() {
      return ProjectHelper.genComponentName(this.getNamespace(), this.getTag());
   }

   public void applyPreSet(UnknownElement u) {
      if (!this.presetDefed) {
         this.getWrapper().applyPreSet(u.getWrapper());
         if (u.children != null) {
            List newChildren = new ArrayList();
            newChildren.addAll(u.children);
            if (this.children != null) {
               newChildren.addAll(this.children);
            }

            this.children = newChildren;
         }

         this.presetDefed = true;
      }
   }

   protected Object makeObject(UnknownElement ue, RuntimeConfigurable w) {
      ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
      String name = ue.getComponentName();
      Object o = helper.createComponent(ue, ue.getNamespace(), name);
      if (o == null) {
         throw this.getNotFoundException("task or type", name);
      } else {
         if (o instanceof PreSetDef.PreSetDefinition) {
            PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)o;
            o = def.createObject(ue.getProject());
            if (o == null) {
               throw this.getNotFoundException("preset " + name, def.getPreSets().getComponentName());
            }

            ue.applyPreSet(def.getPreSets());
            if (o instanceof Task) {
               Task task = (Task)o;
               task.setTaskType(ue.getTaskType());
               task.setTaskName(ue.getTaskName());
               task.init();
            }
         }

         if (o instanceof UnknownElement) {
            o = ((UnknownElement)o).makeObject((UnknownElement)o, w);
         }

         if (o instanceof Task) {
            ((Task)o).setOwningTarget(this.getOwningTarget());
         }

         if (o instanceof ProjectComponent) {
            ((ProjectComponent)o).setLocation(this.getLocation());
         }

         return o;
      }
   }

   protected Task makeTask(UnknownElement ue, RuntimeConfigurable w) {
      Task task = this.getProject().createTask(ue.getTag());
      if (task != null) {
         task.setLocation(this.getLocation());
         task.setOwningTarget(this.getOwningTarget());
         task.init();
      }

      return task;
   }

   protected BuildException getNotFoundException(String what, String name) {
      ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
      String msg = helper.diagnoseCreationFailure(name, what);
      return new BuildException(msg, this.getLocation());
   }

   public String getTaskName() {
      return this.realThing != null && this.realThing instanceof Task ? ((Task)this.realThing).getTaskName() : super.getTaskName();
   }

   public Task getTask() {
      return this.realThing instanceof Task ? (Task)this.realThing : null;
   }

   public Object getRealThing() {
      return this.realThing;
   }

   public void setRealThing(Object realThing) {
      this.realThing = realThing;
   }

   private boolean handleChild(String parentUri, IntrospectionHelper ih, Object parent, UnknownElement child, RuntimeConfigurable childWrapper) {
      String childName = ProjectHelper.genComponentName(child.getNamespace(), child.getTag());
      if (ih.supportsNestedElement(parentUri, childName)) {
         IntrospectionHelper.Creator creator = ih.getElementCreator(this.getProject(), parentUri, parent, childName, child);
         creator.setPolyType(childWrapper.getPolyType());
         Object realChild = creator.create();
         if (realChild instanceof PreSetDef.PreSetDefinition) {
            PreSetDef.PreSetDefinition def = (PreSetDef.PreSetDefinition)realChild;
            realChild = creator.getRealObject();
            child.applyPreSet(def.getPreSets());
         }

         childWrapper.setCreator(creator);
         childWrapper.setProxy(realChild);
         if (realChild instanceof Task) {
            Task childTask = (Task)realChild;
            childTask.setRuntimeConfigurableWrapper(childWrapper);
            childTask.setTaskName(childName);
            childTask.setTaskType(childName);
         }

         if (realChild instanceof ProjectComponent) {
            ((ProjectComponent)realChild).setLocation(child.getLocation());
         }

         childWrapper.maybeConfigure(this.getProject());
         child.handleChildren(realChild, childWrapper);
         creator.store();
         return true;
      } else {
         return false;
      }
   }

   public boolean similar(Object obj) {
      if (obj == null) {
         return false;
      } else if (!this.getClass().getName().equals(obj.getClass().getName())) {
         return false;
      } else {
         UnknownElement other = (UnknownElement)obj;
         if (!equalsString(this.elementName, other.elementName)) {
            return false;
         } else if (!this.namespace.equals(other.namespace)) {
            return false;
         } else if (!this.qname.equals(other.qname)) {
            return false;
         } else if (!this.getWrapper().getAttributeMap().equals(other.getWrapper().getAttributeMap())) {
            return false;
         } else if (!this.getWrapper().getText().toString().equals(other.getWrapper().getText().toString())) {
            return false;
         } else if (this.children != null && this.children.size() != 0) {
            if (other.children == null) {
               return false;
            } else if (this.children.size() != other.children.size()) {
               return false;
            } else {
               for(int i = 0; i < this.children.size(); ++i) {
                  UnknownElement child = (UnknownElement)this.children.get(i);
                  if (!child.similar(other.children.get(i))) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return other.children == null || other.children.size() == 0;
         }
      }
   }

   private static boolean equalsString(String a, String b) {
      return a == null ? b == null : a.equals(b);
   }

   public UnknownElement copy(Project newProject) {
      UnknownElement ret = new UnknownElement(this.getTag());
      ret.setNamespace(this.getNamespace());
      ret.setProject(newProject);
      ret.setQName(this.getQName());
      ret.setTaskType(this.getTaskType());
      ret.setTaskName(this.getTaskName());
      ret.setLocation(this.getLocation());
      if (this.getOwningTarget() == null) {
         Target t = new Target();
         t.setProject(this.getProject());
         ret.setOwningTarget(t);
      } else {
         ret.setOwningTarget(this.getOwningTarget());
      }

      RuntimeConfigurable copyRC = new RuntimeConfigurable(ret, this.getTaskName());
      copyRC.setPolyType(this.getWrapper().getPolyType());
      Map m = this.getWrapper().getAttributeMap();
      Iterator i = m.entrySet().iterator();

      while(i.hasNext()) {
         Entry entry = (Entry)i.next();
         copyRC.setAttribute((String)entry.getKey(), (String)entry.getValue());
      }

      copyRC.addText(this.getWrapper().getText().toString());
      Enumeration e = this.getWrapper().getChildren();

      while(e.hasMoreElements()) {
         RuntimeConfigurable r = (RuntimeConfigurable)e.nextElement();
         UnknownElement ueChild = (UnknownElement)r.getProxy();
         UnknownElement copyChild = ueChild.copy(newProject);
         copyRC.addChild(copyChild.getWrapper());
         ret.addChild(copyChild);
      }

      return ret;
   }
}
