package org.codehaus.groovy.bsf;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.bsf.BSFDeclaredBean;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.BSFFunctions;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;

public class CachingGroovyEngine extends GroovyEngine {
   private static final Logger LOG = Logger.getLogger(CachingGroovyEngine.class.getName());
   private static final Object[] EMPTY_ARGS = new Object[]{new String[0]};
   private Map evalScripts;
   private Map execScripts;
   private Binding context;
   private GroovyClassLoader loader;

   public Object eval(String source, int lineNo, int columnNo, Object script) throws BSFException {
      try {
         Class scriptClass = (Class)this.evalScripts.get(script);
         if (scriptClass == null) {
            scriptClass = this.loader.parseClass(script.toString(), source);
            this.evalScripts.put(script, scriptClass);
         } else {
            LOG.fine("eval() - Using cached script...");
         }

         Script s = InvokerHelper.createScript(scriptClass, this.context);
         return s.run();
      } catch (Exception var7) {
         throw new BSFException(100, "exception from Groovy: " + var7, var7);
      }
   }

   public void exec(String source, int lineNo, int columnNo, Object script) throws BSFException {
      try {
         Class scriptClass = (Class)this.execScripts.get(script);
         if (scriptClass == null) {
            scriptClass = this.loader.parseClass(script.toString(), source);
            this.execScripts.put(script, scriptClass);
         } else {
            LOG.fine("exec() - Using cached version of class...");
         }

         InvokerHelper.invokeMethod(scriptClass, "main", EMPTY_ARGS);
      } catch (Exception var6) {
         LOG.log(Level.WARNING, "BSF trace", var6);
         throw new BSFException(100, "exception from Groovy: " + var6, var6);
      }
   }

   public void initialize(final BSFManager mgr, String lang, Vector declaredBeans) throws BSFException {
      super.initialize(mgr, lang, declaredBeans);
      final ClassLoader parent = mgr.getClassLoader();
      if (parent == null) {
         parent = GroovyShell.class.getClassLoader();
      }

      this.loader = (GroovyClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            CompilerConfiguration configuration = new CompilerConfiguration();
            configuration.setClasspath(mgr.getClassPath());
            return new GroovyClassLoader(parent, configuration);
         }
      });
      this.execScripts = new HashMap();
      this.evalScripts = new HashMap();
      this.context = this.shell.getContext();
      this.context.setVariable("bsf", new BSFFunctions(mgr, this));
      int size = declaredBeans.size();

      for(int i = 0; i < size; ++i) {
         this.declareBean((BSFDeclaredBean)declaredBeans.elementAt(i));
      }

   }
}
