package org.apache.tools.ant.taskdefs.rmic;

import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;

public final class RmicAdapterFactory {
   public static final String ERROR_UNKNOWN_COMPILER = "Class not found: ";
   public static final String ERROR_NOT_RMIC_ADAPTER = "Class of unexpected Type: ";
   public static final String DEFAULT_COMPILER = "default";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$rmic$RmicAdapterFactory;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$rmic$RmicAdapter;

   private RmicAdapterFactory() {
   }

   public static RmicAdapter getRmic(String rmicType, Task task) throws BuildException {
      String compiler = rmicType.toLowerCase(Locale.ENGLISH);
      if ("default".equals(compiler) || compiler.length() == 0) {
         compiler = KaffeRmic.isAvailable() ? "kaffe" : "sun";
      }

      if ("sun".equals(compiler)) {
         return new SunRmic();
      } else if ("kaffe".equals(compiler)) {
         return new KaffeRmic();
      } else if ("weblogic".equals(compiler)) {
         return new WLRmic();
      } else if ("forking".equals(compiler)) {
         return new ForkingSunRmic();
      } else {
         return (RmicAdapter)("xnew".equals(compiler) ? new XNewRmic() : resolveClassName(rmicType));
      }
   }

   private static RmicAdapter resolveClassName(String className) throws BuildException {
      return (RmicAdapter)ClasspathUtils.newInstance(className, (class$org$apache$tools$ant$taskdefs$rmic$RmicAdapterFactory == null ? (class$org$apache$tools$ant$taskdefs$rmic$RmicAdapterFactory = class$("org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory")) : class$org$apache$tools$ant$taskdefs$rmic$RmicAdapterFactory).getClassLoader(), class$org$apache$tools$ant$taskdefs$rmic$RmicAdapter == null ? (class$org$apache$tools$ant$taskdefs$rmic$RmicAdapter = class$("org.apache.tools.ant.taskdefs.rmic.RmicAdapter")) : class$org$apache$tools$ant$taskdefs$rmic$RmicAdapter);
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
