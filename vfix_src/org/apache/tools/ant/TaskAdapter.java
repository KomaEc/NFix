package org.apache.tools.ant;

import java.lang.reflect.Method;
import org.apache.tools.ant.dispatch.DispatchUtils;

public class TaskAdapter extends Task implements TypeAdapter {
   private Object proxy;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$dispatch$Dispatchable;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Location;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;

   public static void checkTaskClass(Class taskClass, Project project) {
      if (!(class$org$apache$tools$ant$dispatch$Dispatchable == null ? (class$org$apache$tools$ant$dispatch$Dispatchable = class$("org.apache.tools.ant.dispatch.Dispatchable")) : class$org$apache$tools$ant$dispatch$Dispatchable).isAssignableFrom(taskClass)) {
         String message;
         try {
            Method executeM = taskClass.getMethod("execute", (Class[])null);
            if (!Void.TYPE.equals(executeM.getReturnType())) {
               message = "return type of execute() should be void but was \"" + executeM.getReturnType() + "\" in " + taskClass;
               project.log(message, 1);
            }
         } catch (NoSuchMethodException var4) {
            message = "No public execute() in " + taskClass;
            project.log(message, 0);
            throw new BuildException(message);
         } catch (LinkageError var5) {
            message = "Could not load " + taskClass + ": " + var5;
            project.log(message, 0);
            throw new BuildException(message, var5);
         }
      }

   }

   public void checkProxyClass(Class proxyClass) {
      checkTaskClass(proxyClass, this.getProject());
   }

   public void execute() throws BuildException {
      Method setProjectM;
      try {
         setProjectM = this.proxy.getClass().getMethod("setLocation", class$org$apache$tools$ant$Location == null ? (class$org$apache$tools$ant$Location = class$("org.apache.tools.ant.Location")) : class$org$apache$tools$ant$Location);
         if (setProjectM != null) {
            setProjectM.invoke(this.proxy, this.getLocation());
         }
      } catch (NoSuchMethodException var6) {
      } catch (Exception var7) {
         this.log("Error setting location in " + this.proxy.getClass(), 0);
         throw new BuildException(var7);
      }

      try {
         setProjectM = this.proxy.getClass().getMethod("setProject", class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
         if (setProjectM != null) {
            setProjectM.invoke(this.proxy, this.getProject());
         }
      } catch (NoSuchMethodException var4) {
      } catch (Exception var5) {
         this.log("Error setting project in " + this.proxy.getClass(), 0);
         throw new BuildException(var5);
      }

      try {
         DispatchUtils.execute(this.proxy);
      } catch (BuildException var2) {
         throw var2;
      } catch (Exception var3) {
         this.log("Error in " + this.proxy.getClass(), 3);
         throw new BuildException(var3);
      }
   }

   public void setProxy(Object o) {
      this.proxy = o;
   }

   public Object getProxy() {
      return this.proxy;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
