package org.apache.tools.ant.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;

public class DispatchUtils {
   public static final void execute(Object task) throws BuildException {
      String methodName = "execute";
      Dispatchable dispatchable = null;

      try {
         UnknownElement mName;
         if (task instanceof Dispatchable) {
            dispatchable = (Dispatchable)task;
         } else if (task instanceof UnknownElement) {
            mName = (UnknownElement)task;
            Object realThing = mName.getRealThing();
            if (realThing != null && realThing instanceof Dispatchable && realThing instanceof Task) {
               dispatchable = (Dispatchable)realThing;
            }
         }

         if (dispatchable != null) {
            mName = null;

            try {
               String name = dispatchable.getActionParameterName();
               if (name != null && name.trim().length() > 0) {
                  String mName = "get" + name.trim().substring(0, 1).toUpperCase();
                  if (name.length() > 1) {
                     mName = mName + name.substring(1);
                  }

                  Class c = dispatchable.getClass();
                  Method actionM = c.getMethod(mName);
                  if (actionM == null) {
                     return;
                  }

                  Object o = actionM.invoke(dispatchable, (Object[])null);
                  if (o == null) {
                     throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
                  }

                  String s = o.toString();
                  if (s != null && s.trim().length() > 0) {
                     methodName = s.trim();
                     Method executeM = null;
                     executeM = dispatchable.getClass().getMethod(methodName);
                     if (executeM == null) {
                        throw new BuildException("No public " + methodName + "() in " + dispatchable.getClass());
                     }

                     executeM.invoke(dispatchable, (Object[])null);
                     if (task instanceof UnknownElement) {
                        ((UnknownElement)task).setRealThing((Object)null);
                     }

                     return;
                  }

                  throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
               }

               throw new BuildException("Action Parameter Name must not be empty for Dispatchable Task.");
            } catch (NoSuchMethodException var10) {
               throw new BuildException("No public " + mName + "() in " + task.getClass());
            }
         } else {
            mName = null;
            Method executeM = task.getClass().getMethod(methodName);
            if (executeM == null) {
               throw new BuildException("No public " + methodName + "() in " + task.getClass());
            }

            executeM.invoke(task, (Object[])null);
            if (task instanceof UnknownElement) {
               ((UnknownElement)task).setRealThing((Object)null);
            }
         }

      } catch (InvocationTargetException var11) {
         Throwable t = var11.getTargetException();
         if (t instanceof BuildException) {
            throw (BuildException)t;
         } else {
            throw new BuildException(t);
         }
      } catch (NoSuchMethodException var12) {
         throw new BuildException(var12);
      } catch (IllegalAccessException var13) {
         throw new BuildException(var13);
      }
   }
}
