package org.codehaus.groovy.jsr223;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import groovy.lang.Tuple;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.syntax.SyntaxException;

public class GroovyScriptEngineImpl extends AbstractScriptEngine implements Compilable, Invocable {
   private static boolean debug = false;
   private Map<String, Class> classMap = Collections.synchronizedMap(new HashMap());
   private Map<String, Closure> globalClosures = Collections.synchronizedMap(new HashMap());
   private GroovyClassLoader loader = new GroovyClassLoader(this.getParentLoader(), new CompilerConfiguration());
   private volatile GroovyScriptEngineFactory factory;
   private static int counter = 0;

   public Object eval(Reader reader, ScriptContext ctx) throws ScriptException {
      return this.eval(this.readFully(reader), ctx);
   }

   public Object eval(String script, ScriptContext ctx) throws ScriptException {
      try {
         Class clazz = this.getScriptClass(script);
         if (clazz == null) {
            throw new ScriptException("Script class is null");
         } else {
            return this.eval(clazz, ctx);
         }
      } catch (SyntaxException var4) {
         throw new ScriptException(var4.getMessage(), var4.getSourceLocator(), var4.getLine());
      } catch (Exception var5) {
         if (debug) {
            var5.printStackTrace();
         }

         throw new ScriptException(var5);
      }
   }

   public Bindings createBindings() {
      return new SimpleBindings();
   }

   public ScriptEngineFactory getFactory() {
      if (this.factory == null) {
         synchronized(this) {
            if (this.factory == null) {
               this.factory = new GroovyScriptEngineFactory();
            }
         }
      }

      return this.factory;
   }

   public CompiledScript compile(String scriptSource) throws ScriptException {
      try {
         return new GroovyCompiledScript(this, this.getScriptClass(scriptSource));
      } catch (SyntaxException var3) {
         throw new ScriptException(var3.getMessage(), var3.getSourceLocator(), var3.getLine());
      } catch (IOException var4) {
         throw new ScriptException(var4);
      } catch (CompilationFailedException var5) {
         throw new ScriptException(var5);
      }
   }

   public CompiledScript compile(Reader reader) throws ScriptException {
      return this.compile(this.readFully(reader));
   }

   public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
      return this.invokeImpl((Object)null, name, args);
   }

   public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
      if (thiz == null) {
         throw new IllegalArgumentException("script object is null");
      } else {
         return this.invokeImpl(thiz, name, args);
      }
   }

   public <T> T getInterface(Class<T> clasz) {
      return this.makeInterface((Object)null, clasz);
   }

   public <T> T getInterface(Object thiz, Class<T> clasz) {
      if (thiz == null) {
         throw new IllegalArgumentException("script object is null");
      } else {
         return this.makeInterface(thiz, clasz);
      }
   }

   Object eval(Class scriptClass, final ScriptContext ctx) throws ScriptException {
      if (null == ctx.getAttribute("context", 100)) {
         ctx.setAttribute("context", ctx, 100);
         Writer writer = ctx.getWriter();
         ctx.setAttribute("out", writer instanceof PrintWriter ? writer : new PrintWriter(writer, true), 100);
      }

      if (ctx.getWriter() != null) {
         ctx.setAttribute("out", new PrintWriter(ctx.getWriter(), true), 100);
      }

      Binding binding = new Binding(ctx.getBindings(100)) {
         public Object getVariable(String name) {
            synchronized(ctx) {
               int scope = ctx.getAttributesScope(name);
               if (scope != -1) {
                  return ctx.getAttribute(name, scope);
               }
            }

            throw new MissingPropertyException(name, this.getClass());
         }

         public void setVariable(String name, Object value) {
            synchronized(ctx) {
               int scope = ctx.getAttributesScope(name);
               if (scope == -1) {
                  scope = 100;
               }

               ctx.setAttribute(name, value, scope);
            }
         }
      };

      try {
         if (!Script.class.isAssignableFrom(scriptClass)) {
            Class var18 = scriptClass;
            return var18;
         } else {
            Script scriptObject = (Script)scriptClass.newInstance();
            scriptObject.setBinding(binding);
            Method[] methods = scriptClass.getMethods();
            Map<String, Closure> closures = new HashMap();
            Method[] arr$ = methods;
            int len$ = methods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Method m = arr$[i$];
               String name = m.getName();
               closures.put(name, new MethodClosure(scriptObject, name));
            }

            this.globalClosures.putAll(closures);
            MetaClass oldMetaClass = scriptObject.getMetaClass();
            scriptObject.setMetaClass(new DelegatingMetaClass(oldMetaClass) {
               public Object invokeMethod(Object object, String name, Object args) {
                  if (args == null) {
                     return this.invokeMethod(object, name, MetaClassHelper.EMPTY_ARRAY);
                  } else if (args instanceof Tuple) {
                     return this.invokeMethod(object, name, ((Tuple)args).toArray());
                  } else {
                     return args instanceof Object[] ? this.invokeMethod(object, name, (Object[])((Object[])args)) : this.invokeMethod(object, name, new Object[]{args});
                  }
               }

               public Object invokeMethod(Object object, String name, Object[] args) {
                  try {
                     return super.invokeMethod(object, name, args);
                  } catch (MissingMethodException var5) {
                     return GroovyScriptEngineImpl.this.callGlobal(name, args, ctx);
                  }
               }

               public Object invokeStaticMethod(Object object, String name, Object[] args) {
                  try {
                     return super.invokeStaticMethod(object, name, args);
                  } catch (MissingMethodException var5) {
                     return GroovyScriptEngineImpl.this.callGlobal(name, args, ctx);
                  }
               }
            });
            Object var20 = scriptObject.run();
            return var20;
         }
      } catch (Exception var15) {
         throw new ScriptException(var15);
      } finally {
         ctx.removeAttribute("context", 100);
         ctx.removeAttribute("out", 100);
      }
   }

   Class getScriptClass(String script) throws SyntaxException, CompilationFailedException, IOException {
      Class clazz = (Class)this.classMap.get(script);
      if (clazz != null) {
         return clazz;
      } else {
         clazz = this.loader.parseClass(script, this.generateScriptName());
         this.classMap.put(script, clazz);
         return clazz;
      }
   }

   public void setClassLoader(GroovyClassLoader classLoader) {
      this.loader = classLoader;
   }

   public GroovyClassLoader getClassLoader() {
      return this.loader;
   }

   private Object invokeImpl(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
      if (name == null) {
         throw new NullPointerException("method name is null");
      } else {
         try {
            return thiz != null ? InvokerHelper.invokeMethod(thiz, name, args) : this.callGlobal(name, args);
         } catch (MissingMethodException var5) {
            throw new NoSuchMethodException(var5.getMessage());
         } catch (Exception var6) {
            throw new ScriptException(var6);
         }
      }
   }

   private Object callGlobal(String name, Object[] args) {
      return this.callGlobal(name, args, this.context);
   }

   private Object callGlobal(String name, Object[] args, ScriptContext ctx) {
      Closure closure = (Closure)this.globalClosures.get(name);
      if (closure != null) {
         return closure.call(args);
      } else {
         Object value = ctx.getAttribute(name);
         if (value instanceof Closure) {
            return ((Closure)value).call(args);
         } else {
            throw new MissingMethodException(name, this.getClass(), args);
         }
      }
   }

   private synchronized String generateScriptName() {
      return "Script" + ++counter + ".groovy";
   }

   private <T> T makeInterface(final Object obj, Class<T> clazz) {
      if (clazz != null && clazz.isInterface()) {
         return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
               return GroovyScriptEngineImpl.this.invokeImpl(obj, m.getName(), args);
            }
         });
      } else {
         throw new IllegalArgumentException("interface Class expected");
      }
   }

   private ClassLoader getParentLoader() {
      ClassLoader ctxtLoader = Thread.currentThread().getContextClassLoader();

      try {
         Class c = ctxtLoader.loadClass(Script.class.getName());
         if (c == Script.class) {
            return ctxtLoader;
         }
      } catch (ClassNotFoundException var3) {
      }

      return Script.class.getClassLoader();
   }

   private String readFully(Reader reader) throws ScriptException {
      char[] arr = new char[8192];
      StringBuilder buf = new StringBuilder();

      int numChars;
      try {
         while((numChars = reader.read(arr, 0, arr.length)) > 0) {
            buf.append(arr, 0, numChars);
         }
      } catch (IOException var6) {
         throw new ScriptException(var6);
      }

      return buf.toString();
   }
}
