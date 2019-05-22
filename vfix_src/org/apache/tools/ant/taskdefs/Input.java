package org.apache.tools.ant.taskdefs;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.GreedyInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.input.PropertyFileInputHandler;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.StringUtils;

public class Input extends Task {
   private String validargs = null;
   private String message = "";
   private String addproperty = null;
   private String defaultvalue = null;
   private Input.Handler handler = null;
   private boolean messageAttribute;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$input$InputHandler;

   public void setValidargs(String validargs) {
      this.validargs = validargs;
   }

   public void setAddproperty(String addproperty) {
      this.addproperty = addproperty;
   }

   public void setMessage(String message) {
      this.message = message;
      this.messageAttribute = true;
   }

   public void setDefaultvalue(String defaultvalue) {
      this.defaultvalue = defaultvalue;
   }

   public void addText(String msg) {
      if (!this.messageAttribute || !"".equals(msg.trim())) {
         this.message = this.message + this.getProject().replaceProperties(msg);
      }
   }

   public void execute() throws BuildException {
      if (this.addproperty != null && this.getProject().getProperty(this.addproperty) != null) {
         this.log("skipping " + this.getTaskName() + " as property " + this.addproperty + " has already been set.");
      } else {
         InputRequest request = null;
         if (this.validargs != null) {
            Vector accept = StringUtils.split(this.validargs, 44);
            request = new MultipleChoiceInputRequest(this.message, accept);
         } else {
            request = new InputRequest(this.message);
         }

         ((InputRequest)request).setDefaultValue(this.defaultvalue);
         InputHandler h = this.handler == null ? this.getProject().getInputHandler() : this.handler.getInputHandler();
         h.handleInput((InputRequest)request);
         String value = ((InputRequest)request).getInput();
         if ((value == null || value.trim().length() == 0) && this.defaultvalue != null) {
            value = this.defaultvalue;
         }

         if (this.addproperty != null && value != null) {
            this.getProject().setNewProperty(this.addproperty, value);
         }

      }
   }

   public Input.Handler createHandler() {
      if (this.handler != null) {
         throw new BuildException("Cannot define > 1 nested input handler");
      } else {
         this.handler = new Input.Handler();
         return this.handler;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class HandlerType extends EnumeratedAttribute {
      private static final String[] VALUES = new String[]{"default", "propertyfile", "greedy"};
      private static final InputHandler[] HANDLERS = new InputHandler[]{new DefaultInputHandler(), new PropertyFileInputHandler(), new GreedyInputHandler()};

      public String[] getValues() {
         return VALUES;
      }

      private InputHandler getInputHandler() {
         return HANDLERS[this.getIndex()];
      }
   }

   public class Handler extends DefBase {
      private String refid = null;
      private Input.HandlerType type = null;
      private String classname = null;

      public void setRefid(String refid) {
         this.refid = refid;
      }

      public String getRefid() {
         return this.refid;
      }

      public void setClassname(String classname) {
         this.classname = classname;
      }

      public String getClassname() {
         return this.classname;
      }

      public void setType(Input.HandlerType type) {
         this.type = type;
      }

      public Input.HandlerType getType() {
         return this.type;
      }

      private InputHandler getInputHandler() {
         if (this.type != null) {
            return this.type.getInputHandler();
         } else if (this.refid != null) {
            try {
               return (InputHandler)((InputHandler)this.getProject().getReference(this.refid));
            } catch (ClassCastException var2) {
               throw new BuildException(this.refid + " does not denote an InputHandler", var2);
            }
         } else if (this.classname != null) {
            return (InputHandler)((InputHandler)ClasspathUtils.newInstance(this.classname, this.createLoader(), Input.class$org$apache$tools$ant$input$InputHandler == null ? (Input.class$org$apache$tools$ant$input$InputHandler = Input.class$("org.apache.tools.ant.input.InputHandler")) : Input.class$org$apache$tools$ant$input$InputHandler));
         } else {
            throw new BuildException("Must specify refid, classname or type");
         }
      }
   }
}
