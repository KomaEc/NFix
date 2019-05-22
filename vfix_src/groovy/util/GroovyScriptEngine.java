package groovy.util;

import groovy.lang.Binding;
import groovy.lang.DeprecationException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyResourceLoader;
import groovy.lang.Script;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.tools.gse.DependencyTracker;
import org.codehaus.groovy.tools.gse.StringSetMap;

public class GroovyScriptEngine implements ResourceConnector {
   private static final ClassLoader CL_STUB = new ClassLoader() {
   };
   private static WeakReference<ThreadLocal<StringSetMap>> dependencyCache = new WeakReference((Object)null);
   private static WeakReference<ThreadLocal<CompilationUnit>> localCu = new WeakReference((Object)null);
   private URL[] roots;
   private ResourceConnector rc;
   private final ClassLoader parentLoader;
   private final GroovyClassLoader groovyLoader;
   private final Map<String, GroovyScriptEngine.ScriptCacheEntry> scriptCache;
   private CompilerConfiguration config;

   private static synchronized ThreadLocal<StringSetMap> getDepCache() {
      ThreadLocal<StringSetMap> local = (ThreadLocal)dependencyCache.get();
      if (local != null) {
         return local;
      } else {
         local = new ThreadLocal<StringSetMap>() {
            protected StringSetMap initialValue() {
               return new StringSetMap();
            }
         };
         dependencyCache = new WeakReference(local);
         return local;
      }
   }

   private static synchronized ThreadLocal<CompilationUnit> getLocalCompilationUnit() {
      ThreadLocal<CompilationUnit> local = (ThreadLocal)localCu.get();
      if (local != null) {
         return local;
      } else {
         local = new ThreadLocal();
         localCu = new WeakReference(local);
         return local;
      }
   }

   public static void main(String[] urls) throws Exception {
      GroovyScriptEngine gse = new GroovyScriptEngine(urls);
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      while(true) {
         System.out.print("groovy> ");
         String line;
         if ((line = br.readLine()) == null || line.equals("quit")) {
            return;
         }

         try {
            System.out.println(gse.run(line, new Binding()));
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }
   }

   private GroovyClassLoader initGroovyLoader() {
      return (GroovyClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return GroovyScriptEngine.this.parentLoader instanceof GroovyClassLoader ? GroovyScriptEngine.this.new ScriptClassLoader((GroovyClassLoader)GroovyScriptEngine.this.parentLoader) : GroovyScriptEngine.this.new ScriptClassLoader(GroovyScriptEngine.this.parentLoader);
         }
      });
   }

   public URLConnection getResourceConnection(String resourceName) throws ResourceException {
      URLConnection groovyScriptConn = null;
      ResourceException se = null;
      URL[] arr$ = this.roots;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         URL root = arr$[i$];
         URL scriptURL = null;

         String message;
         try {
            scriptURL = new URL(root, resourceName);
            groovyScriptConn = scriptURL.openConnection();
            groovyScriptConn.getInputStream();
            break;
         } catch (MalformedURLException var11) {
            message = "Malformed URL: " + root + ", " + resourceName;
            if (se == null) {
               se = new ResourceException(message);
            } else {
               se = new ResourceException(message, se);
            }
         } catch (IOException var12) {
            groovyScriptConn = null;
            message = "Cannot open URL: " + scriptURL;
            groovyScriptConn = null;
            if (se == null) {
               se = new ResourceException(message);
            } else {
               se = new ResourceException(message, se);
            }
         }
      }

      if (se == null) {
         se = new ResourceException("No resource for " + resourceName + " was found");
      }

      if (groovyScriptConn == null) {
         throw se;
      } else {
         return groovyScriptConn;
      }
   }

   private void forceClose(URLConnection urlConnection) {
      if (urlConnection != null) {
         InputStream in = null;

         try {
            in = urlConnection.getInputStream();
         } catch (Exception var12) {
         } finally {
            if (in != null) {
               try {
                  in.close();
               } catch (IOException var11) {
               }
            }

         }
      }

   }

   private GroovyScriptEngine(URL[] roots, ClassLoader parent, ResourceConnector rc) {
      this.scriptCache = new ConcurrentHashMap();
      this.config = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
      if (roots == null) {
         roots = new URL[0];
      }

      this.roots = roots;
      if (rc == null) {
         rc = this;
      }

      this.rc = (ResourceConnector)rc;
      if (parent == CL_STUB) {
         parent = this.getClass().getClassLoader();
      }

      this.parentLoader = parent;
      this.groovyLoader = this.initGroovyLoader();
      URL[] arr$ = roots;
      int len$ = roots.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         URL root = arr$[i$];
         this.groovyLoader.addURL(root);
      }

   }

   public GroovyScriptEngine(URL[] roots) {
      this(roots, CL_STUB, (ResourceConnector)null);
   }

   public GroovyScriptEngine(URL[] roots, ClassLoader parentClassLoader) {
      this(roots, parentClassLoader, (ResourceConnector)null);
   }

   public GroovyScriptEngine(String[] urls) throws IOException {
      this(createRoots(urls), CL_STUB, (ResourceConnector)null);
   }

   private static URL[] createRoots(String[] urls) throws MalformedURLException {
      if (urls == null) {
         return null;
      } else {
         URL[] roots = new URL[urls.length];

         for(int i = 0; i < roots.length; ++i) {
            if (urls[i].indexOf("://") != -1) {
               roots[i] = new URL(urls[i]);
            } else {
               roots[i] = (new File(urls[i])).toURI().toURL();
            }
         }

         return roots;
      }
   }

   public GroovyScriptEngine(String[] urls, ClassLoader parentClassLoader) throws IOException {
      this(createRoots(urls), parentClassLoader, (ResourceConnector)null);
   }

   public GroovyScriptEngine(String url) throws IOException {
      this(new String[]{url});
   }

   public GroovyScriptEngine(String url, ClassLoader parentClassLoader) throws IOException {
      this(new String[]{url}, parentClassLoader);
   }

   public GroovyScriptEngine(ResourceConnector rc) {
      this((URL[])null, CL_STUB, rc);
   }

   public GroovyScriptEngine(ResourceConnector rc, ClassLoader parentClassLoader) {
      this((URL[])null, parentClassLoader, rc);
   }

   public ClassLoader getParentClassLoader() {
      return this.parentLoader;
   }

   /** @deprecated */
   public void setParentClassLoader(ClassLoader parentClassLoader) {
      throw new DeprecationException("The method GroovyScriptEngine#setParentClassLoader(ClassLoader) is no longer supported. Specify a parentLoader in the constructor instead.");
   }

   public Class loadScriptByName(String scriptName) throws ResourceException, ScriptException {
      URLConnection conn = this.rc.getResourceConnection(scriptName);
      String path = conn.getURL().getPath();
      GroovyScriptEngine.ScriptCacheEntry entry = (GroovyScriptEngine.ScriptCacheEntry)this.scriptCache.get(path);
      Class clazz = null;
      if (entry != null) {
         clazz = entry.scriptClass;
      }

      try {
         if (this.isSourceNewer(entry)) {
            try {
               String encoding = conn.getContentEncoding() != null ? conn.getContentEncoding() : "UTF-8";
               clazz = this.groovyLoader.parseClass(DefaultGroovyMethods.getText(conn.getInputStream(), encoding), path);
            } catch (IOException var10) {
               throw new ResourceException(var10);
            }
         }
      } finally {
         this.forceClose(conn);
      }

      return clazz;
   }

   /** @deprecated */
   public Class loadScriptByName(String scriptName, ClassLoader parentClassLoader) throws ResourceException, ScriptException {
      throw new DeprecationException("The method GroovyScriptEngine#loadScriptByName(String,ClassLoader) is no longer supported. Use GroovyScriptEngine#loadScriptByName(String) instead.");
   }

   public String run(String scriptName, String argument) throws ResourceException, ScriptException {
      Binding binding = new Binding();
      binding.setVariable("arg", argument);
      Object result = this.run(scriptName, binding);
      return result == null ? "" : result.toString();
   }

   public Object run(String scriptName, Binding binding) throws ResourceException, ScriptException {
      return this.createScript(scriptName, binding).run();
   }

   public Script createScript(String scriptName, Binding binding) throws ResourceException, ScriptException {
      return InvokerHelper.createScript(this.loadScriptByName(scriptName), binding);
   }

   protected boolean isSourceNewer(GroovyScriptEngine.ScriptCacheEntry entry) throws ResourceException {
      if (entry == null) {
         return true;
      } else {
         long now = System.currentTimeMillis();
         Iterator i$ = entry.dependencies.iterator();

         while(i$.hasNext()) {
            String scriptName = (String)i$.next();
            GroovyScriptEngine.ScriptCacheEntry depEntry = (GroovyScriptEngine.ScriptCacheEntry)this.scriptCache.get(scriptName);
            long nextPossibleRecompilationTime = depEntry.lastModified + (long)this.config.getMinimumRecompilationInterval();
            if (nextPossibleRecompilationTime <= now) {
               URLConnection conn = this.rc.getResourceConnection(scriptName);
               long lastMod = (conn.getLastModified() / 1000L + 1L) * 1000L - 1L;
               this.forceClose(conn);
               if (depEntry.lastModified < lastMod) {
                  GroovyScriptEngine.ScriptCacheEntry newEntry = new GroovyScriptEngine.ScriptCacheEntry(depEntry.scriptClass, lastMod, depEntry.dependencies);
                  this.scriptCache.put(scriptName, newEntry);
                  return true;
               }
            }
         }

         return false;
      }
   }

   public GroovyClassLoader getGroovyClassLoader() {
      return this.groovyLoader;
   }

   public CompilerConfiguration getConfig() {
      return this.config;
   }

   public void setConfig(CompilerConfiguration config) {
      if (config == null) {
         throw new NullPointerException("configuration cannot be null");
      } else {
         this.config = config;
      }
   }

   private class ScriptClassLoader extends GroovyClassLoader {
      public ScriptClassLoader(GroovyClassLoader loader) {
         super(loader);
         this.setResLoader();
      }

      public ScriptClassLoader(ClassLoader loader) {
         super(loader);
         this.setResLoader();
      }

      private void setResLoader() {
         final GroovyResourceLoader rl = this.getResourceLoader();
         this.setResourceLoader(new GroovyResourceLoader() {
            public URL loadGroovySource(String className) throws MalformedURLException {
               Iterator i$ = GroovyScriptEngine.this.getConfig().getScriptExtensions().iterator();

               while(i$.hasNext()) {
                  String extension = (String)i$.next();
                  String filename = className.replace('.', File.separatorChar) + "." + extension;

                  try {
                     URLConnection dependentScriptConn = GroovyScriptEngine.this.rc.getResourceConnection(filename);
                     return dependentScriptConn.getURL();
                  } catch (ResourceException var6) {
                  }
               }

               return rl.loadGroovySource(className);
            }
         });
      }

      protected CompilationUnit createCompilationUnit(CompilerConfiguration config, CodeSource source) {
         CompilationUnit cu = super.createCompilationUnit(config, source);
         GroovyScriptEngine.getLocalCompilationUnit().set(cu);
         final StringSetMap cache = (StringSetMap)GroovyScriptEngine.getDepCache().get();
         Iterator i$ = cache.get(".").iterator();

         while(i$.hasNext()) {
            String depSourcePath = (String)i$.next();

            try {
               cu.addSource(GroovyScriptEngine.this.getResourceConnection(depSourcePath).getURL());
            } catch (ResourceException var8) {
            }
         }

         cache.clear();
         cu.addPhaseOperation((CompilationUnit.PrimaryClassNodeOperation)(new CompilationUnit.PrimaryClassNodeOperation() {
            public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
               if (!(classNode instanceof InnerClassNode)) {
                  DependencyTracker dt = new DependencyTracker(source, cache);
                  dt.visitClass(classNode);
               }
            }
         }), 7);
         return cu;
      }

      public Class parseClass(GroovyCodeSource codeSource, boolean shouldCacheSource) throws CompilationFailedException {
         ThreadLocal<CompilationUnit> localCu = GroovyScriptEngine.getLocalCompilationUnit();
         ThreadLocal<StringSetMap> localCache = GroovyScriptEngine.getDepCache();
         GroovyScriptEngine.ScriptCacheEntry origEntry = (GroovyScriptEngine.ScriptCacheEntry)GroovyScriptEngine.this.scriptCache.get(codeSource.getName());
         Set<String> origDep = null;
         if (origEntry != null) {
            origDep = origEntry.dependencies;
         }

         if (origDep != null) {
            ((StringSetMap)localCache.get()).put(".", origDep);
         }

         Class answer = super.parseClass(codeSource, false);
         StringSetMap cache = (StringSetMap)localCache.get();
         cache.makeTransitiveHull();
         long now = System.currentTimeMillis();
         Set<String> entryNames = new HashSet();
         Iterator i$ = cache.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Set<String>> entry = (Entry)i$.next();
            String className = (String)entry.getKey();
            Class clazz = this.getClassCacheEntry(className);
            if (clazz != null) {
               String entryName = this.getPath(clazz);
               if (!entryNames.contains(entryName)) {
                  entryNames.add(entryName);
                  Set<String> value = this.convertToPaths((Set)entry.getValue());
                  GroovyScriptEngine.ScriptCacheEntry cacheEntry = new GroovyScriptEngine.ScriptCacheEntry(clazz, now, value);
                  GroovyScriptEngine.this.scriptCache.put(entryName, cacheEntry);
               }
            }
         }

         cache.clear();
         localCu.set((Object)null);
         return answer;
      }

      private String getPath(Class clazz) {
         ThreadLocal<CompilationUnit> localCu = GroovyScriptEngine.getLocalCompilationUnit();
         ClassNode classNode = ((CompilationUnit)localCu.get()).getClassNode(clazz.getCanonicalName());
         return classNode.getModule().getContext().getName();
      }

      private Set<String> convertToPaths(Set<String> orig) {
         Set<String> ret = new HashSet();
         Iterator i$ = orig.iterator();

         while(i$.hasNext()) {
            String className = (String)i$.next();
            Class clazz = this.getClassCacheEntry(className);
            if (clazz != null) {
               ret.add(this.getPath(clazz));
            }
         }

         return ret;
      }
   }

   private static class ScriptCacheEntry {
      private final Class scriptClass;
      private final long lastModified;
      private final Set<String> dependencies;

      public ScriptCacheEntry(Class clazz, long modified, Set<String> depend) {
         this.scriptClass = clazz;
         this.lastModified = modified;
         this.dependencies = depend;
      }
   }
}
