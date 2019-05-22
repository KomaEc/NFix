package groovy.lang;

import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.ClassWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class GroovyClassLoader extends URLClassLoader {
   protected final Map<String, Class> classCache;
   protected final Map<String, Class> sourceCache;
   private final CompilerConfiguration config;
   private Boolean recompile;
   private static int scriptNameCounter = 1000000;
   private GroovyResourceLoader resourceLoader;

   public GroovyClassLoader() {
      this(Thread.currentThread().getContextClassLoader());
   }

   public GroovyClassLoader(ClassLoader loader) {
      this(loader, (CompilerConfiguration)null);
   }

   public GroovyClassLoader(GroovyClassLoader parent) {
      this(parent, parent.config, false);
   }

   public GroovyClassLoader(ClassLoader parent, CompilerConfiguration config, boolean useConfigurationClasspath) {
      super(new URL[0], parent);
      this.classCache = new HashMap();
      this.sourceCache = new HashMap();
      this.resourceLoader = new GroovyResourceLoader() {
         public URL loadGroovySource(final String filename) throws MalformedURLException {
            return (URL)AccessController.doPrivileged(new PrivilegedAction<URL>() {
               public URL run() {
                  Iterator i$ = GroovyClassLoader.this.config.getScriptExtensions().iterator();

                  while(i$.hasNext()) {
                     String extension = (String)i$.next();

                     try {
                        URL ret = GroovyClassLoader.this.getSourceFile(filename, extension);
                        if (ret != null) {
                           return ret;
                        }
                     } catch (Throwable var4) {
                     }
                  }

                  return null;
               }
            });
         }
      };
      if (config == null) {
         config = CompilerConfiguration.DEFAULT;
      }

      this.config = config;
      if (useConfigurationClasspath) {
         Iterator i$ = config.getClasspath().iterator();

         while(i$.hasNext()) {
            String path = (String)i$.next();
            this.addClasspath(path);
         }
      }

   }

   public GroovyClassLoader(ClassLoader loader, CompilerConfiguration config) {
      this(loader, config, true);
   }

   public void setResourceLoader(GroovyResourceLoader resourceLoader) {
      if (resourceLoader == null) {
         throw new IllegalArgumentException("Resource loader must not be null!");
      } else {
         this.resourceLoader = resourceLoader;
      }
   }

   public GroovyResourceLoader getResourceLoader() {
      return this.resourceLoader;
   }

   /** @deprecated */
   public Class defineClass(ClassNode classNode, String file) {
      throw new DeprecationException("the method GroovyClassLoader#defineClass(ClassNode, String) is no longer used and removed");
   }

   public Class defineClass(ClassNode classNode, String file, String newCodeBase) {
      CodeSource codeSource = null;

      try {
         codeSource = new CodeSource(new URL("file", "", newCodeBase), (Certificate[])null);
      } catch (MalformedURLException var9) {
      }

      CompilationUnit unit = this.createCompilationUnit(this.config, codeSource);
      GroovyClassLoader.ClassCollector collector = this.createCollector(unit, classNode.getModule().getContext());

      try {
         unit.addClassNode(classNode);
         unit.setClassgenCallback(collector);
         unit.compile(7);
         this.definePackage(collector.generatedClass.getName());
         return collector.generatedClass;
      } catch (CompilationFailedException var8) {
         throw new RuntimeException(var8);
      }
   }

   public Class parseClass(File file) throws CompilationFailedException, IOException {
      return this.parseClass(new GroovyCodeSource(file, this.config.getSourceEncoding()));
   }

   public Class parseClass(final String text, final String fileName) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            return new GroovyCodeSource(text, fileName, "/groovy/script");
         }
      });
      gcs.setCachable(false);
      return this.parseClass(gcs);
   }

   public Class parseClass(String text) throws CompilationFailedException {
      return this.parseClass(text, "script" + System.currentTimeMillis() + Math.abs(text.hashCode()) + ".groovy");
   }

   /** @deprecated */
   public Class parseClass(InputStream in) throws CompilationFailedException {
      return this.parseClass(in, this.generateScriptName());
   }

   public synchronized String generateScriptName() {
      ++scriptNameCounter;
      return "script" + scriptNameCounter + ".groovy";
   }

   /** @deprecated */
   public Class parseClass(final InputStream in, final String fileName) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            try {
               String scriptText = GroovyClassLoader.this.config.getSourceEncoding() != null ? DefaultGroovyMethods.getText(in, GroovyClassLoader.this.config.getSourceEncoding()) : DefaultGroovyMethods.getText(in);
               return new GroovyCodeSource(scriptText, fileName, "/groovy/script");
            } catch (IOException var2) {
               throw new RuntimeException("Impossible to read the content of the input stream for file named: " + fileName, var2);
            }
         }
      });
      return this.parseClass(gcs);
   }

   public Class parseClass(GroovyCodeSource codeSource) throws CompilationFailedException {
      return this.parseClass(codeSource, codeSource.isCachable());
   }

   public Class parseClass(GroovyCodeSource codeSource, boolean shouldCacheSource) throws CompilationFailedException {
      Class answer;
      synchronized(this.sourceCache) {
         answer = (Class)this.sourceCache.get(codeSource.getName());
         if (answer != null) {
            return answer;
         }

         if (shouldCacheSource) {
            answer = this.doParseClass(codeSource);
            this.sourceCache.put(codeSource.getName(), answer);
         }
      }

      if (!shouldCacheSource) {
         answer = this.doParseClass(codeSource);
      }

      return answer;
   }

   private Class doParseClass(GroovyCodeSource codeSource) {
      this.validate(codeSource);
      CompilationUnit unit = this.createCompilationUnit(this.config, codeSource.getCodeSource());
      SourceUnit su = null;
      if (codeSource.getFile() == null) {
         su = unit.addSource(codeSource.getName(), codeSource.getScriptText());
      } else {
         su = unit.addSource(codeSource.getFile());
      }

      GroovyClassLoader.ClassCollector collector = this.createCollector(unit, su);
      unit.setClassgenCallback(collector);
      int goalPhase = 7;
      if (this.config != null && this.config.getTargetDirectory() != null) {
         goalPhase = 8;
      }

      unit.compile(goalPhase);
      Class answer = collector.generatedClass;
      String mainClass = su.getAST().getMainClassName();
      Iterator i$ = collector.getLoadedClasses().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Class clazz = (Class)o;
         String clazzName = clazz.getName();
         this.definePackage(clazzName);
         this.setClassCacheEntry(clazz);
         if (clazzName.equals(mainClass)) {
            answer = clazz;
         }
      }

      return answer;
   }

   private void validate(GroovyCodeSource codeSource) {
      if (codeSource.getFile() == null && codeSource.getScriptText() == null) {
         throw new IllegalArgumentException("Script text to compile cannot be null!");
      }
   }

   private void definePackage(String className) {
      int i = className.lastIndexOf(46);
      if (i != -1) {
         String pkgName = className.substring(0, i);
         Package pkg = this.getPackage(pkgName);
         if (pkg == null) {
            this.definePackage(pkgName, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
         }
      }

   }

   protected String[] getClassPath() {
      URL[] urls = this.getURLs();
      String[] ret = new String[urls.length];

      for(int i = 0; i < ret.length; ++i) {
         ret[i] = urls[i].getFile();
      }

      return ret;
   }

   /** @deprecated */
   protected void expandClassPath(List pathList, String base, String classpath, boolean isManifestClasspath) {
      throw new DeprecationException("the method groovy.lang.GroovyClassLoader#expandClassPath(List,String,String,boolean) is no longer used internally and removed");
   }

   /** @deprecated */
   protected Class defineClass(String name, byte[] bytecode, ProtectionDomain domain) {
      throw new DeprecationException("the method groovy.lang.GroovyClassLoader#defineClass(String,byte[],ProtectionDomain) is no longer used internally and removed");
   }

   protected PermissionCollection getPermissions(CodeSource codeSource) {
      Object perms;
      try {
         perms = super.getPermissions(codeSource);
      } catch (SecurityException var6) {
         perms = new Permissions();
      }

      ProtectionDomain myDomain = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>() {
         public ProtectionDomain run() {
            return this.getClass().getProtectionDomain();
         }
      });
      PermissionCollection myPerms = myDomain.getPermissions();
      if (myPerms != null) {
         Enumeration elements = myPerms.elements();

         while(elements.hasMoreElements()) {
            ((PermissionCollection)perms).add((Permission)elements.nextElement());
         }
      }

      ((PermissionCollection)perms).setReadOnly();
      return (PermissionCollection)perms;
   }

   protected CompilationUnit createCompilationUnit(CompilerConfiguration config, CodeSource source) {
      return new CompilationUnit(config, source, this);
   }

   protected GroovyClassLoader.ClassCollector createCollector(CompilationUnit unit, SourceUnit su) {
      GroovyClassLoader.InnerLoader loader = (GroovyClassLoader.InnerLoader)AccessController.doPrivileged(new PrivilegedAction<GroovyClassLoader.InnerLoader>() {
         public GroovyClassLoader.InnerLoader run() {
            return new GroovyClassLoader.InnerLoader(GroovyClassLoader.this);
         }
      });
      return new GroovyClassLoader.ClassCollector(loader, unit, su);
   }

   public Class defineClass(String name, byte[] b) {
      return super.defineClass(name, b, 0, b.length);
   }

   public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript) throws ClassNotFoundException, CompilationFailedException {
      return this.loadClass(name, lookupScriptFiles, preferClassOverScript, false);
   }

   protected Class getClassCacheEntry(String name) {
      if (name == null) {
         return null;
      } else {
         synchronized(this.classCache) {
            return (Class)this.classCache.get(name);
         }
      }
   }

   protected void setClassCacheEntry(Class cls) {
      synchronized(this.classCache) {
         this.classCache.put(cls.getName(), cls);
      }
   }

   protected void removeClassCacheEntry(String name) {
      synchronized(this.classCache) {
         this.classCache.remove(name);
      }
   }

   public void addURL(URL url) {
      super.addURL(url);
   }

   protected boolean isRecompilable(Class cls) {
      if (cls == null) {
         return true;
      } else if (cls.getClassLoader() == this) {
         return false;
      } else if (this.recompile == null && !this.config.getRecompileGroovySource()) {
         return false;
      } else if (this.recompile != null && !this.recompile) {
         return false;
      } else if (!GroovyObject.class.isAssignableFrom(cls)) {
         return false;
      } else {
         long timestamp = this.getTimeStamp(cls);
         return timestamp != Long.MAX_VALUE;
      }
   }

   public void setShouldRecompile(Boolean mode) {
      this.recompile = mode;
   }

   public Boolean isShouldRecompile() {
      return this.recompile;
   }

   public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve) throws ClassNotFoundException, CompilationFailedException {
      Class cls = this.getClassCacheEntry(name);
      boolean recompile = this.isRecompilable(cls);
      if (!recompile) {
         return cls;
      } else {
         ClassNotFoundException last = null;

         try {
            Class parentClassLoaderClass = super.loadClass(name, resolve);
            if (cls != parentClassLoaderClass) {
               return parentClassLoaderClass;
            }
         } catch (ClassNotFoundException var19) {
            last = var19;
         } catch (NoClassDefFoundError var20) {
            if (var20.getMessage().indexOf("wrong name") <= 0) {
               throw var20;
            }

            last = new ClassNotFoundException(name);
         }

         SecurityManager sm = System.getSecurityManager();
         if (sm != null) {
            String className = name.replace('/', '.');
            int i = className.lastIndexOf(46);
            if (i != -1 && !className.startsWith("sun.reflect.")) {
               sm.checkPackageAccess(className.substring(0, i));
            }
         }

         if (cls != null && preferClassOverScript) {
            return cls;
         } else {
            if (lookupScriptFiles) {
               try {
                  Class classCacheEntry = this.getClassCacheEntry(name);
                  if (classCacheEntry != cls) {
                     Class var24 = classCacheEntry;
                     return var24;
                  }

                  URL source = this.resourceLoader.loadGroovySource(name);
                  Class oldClass = cls;
                  cls = null;
                  cls = this.recompile(source, name, oldClass);
               } catch (IOException var17) {
                  last = new ClassNotFoundException("IOException while opening groovy source: " + name, var17);
               } finally {
                  if (cls == null) {
                     this.removeClassCacheEntry(name);
                  } else {
                     this.setClassCacheEntry(cls);
                  }

               }
            }

            if (cls == null) {
               if (last == null) {
                  throw new AssertionError(true);
               } else {
                  throw last;
               }
            } else {
               return cls;
            }
         }
      }
   }

   protected Class recompile(URL source, String className, Class oldClass) throws CompilationFailedException, IOException {
      if (source == null || (oldClass == null || !this.isSourceNewer(source, oldClass)) && oldClass != null) {
         return oldClass;
      } else {
         synchronized(this.sourceCache) {
            this.sourceCache.remove(className);
            return this.parseClass(source.openStream(), className);
         }
      }
   }

   protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      return this.loadClass(name, true, true, resolve);
   }

   protected long getTimeStamp(Class cls) {
      return Verifier.getTimestamp(cls);
   }

   private String decodeFileName(String fileName) {
      String decodedFile = fileName;

      try {
         decodedFile = URLDecoder.decode(fileName, "UTF-8");
      } catch (UnsupportedEncodingException var4) {
         System.err.println("Encountered an invalid encoding scheme when trying to use URLDecoder.decode() inside of the GroovyClassLoader.decodeFileName() method.  Returning the unencoded URL.");
         System.err.println("Please note that if you encounter this error and you have spaces in your directory you will run into issues.  Refer to GROOVY-1787 for description of this bug.");
      }

      return decodedFile;
   }

   private boolean isFile(URL ret) {
      return ret != null && ret.getProtocol().equals("file");
   }

   private File getFileForUrl(URL ret, String filename) {
      String fileWithoutPackage = filename;
      if (filename.indexOf(47) != -1) {
         int index = filename.lastIndexOf(47);
         fileWithoutPackage = filename.substring(index + 1);
      }

      return this.fileReallyExists(ret, fileWithoutPackage);
   }

   private File fileReallyExists(URL ret, String fileWithoutPackage) {
      File path = (new File(this.decodeFileName(ret.getFile()))).getParentFile();
      if (path.exists() && path.isDirectory()) {
         File file = new File(path, fileWithoutPackage);
         if (file.exists()) {
            File parent = file.getParentFile();
            String[] arr$ = parent.list();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String child = arr$[i$];
               if (child.equals(fileWithoutPackage)) {
                  return file;
               }
            }
         }
      }

      return null;
   }

   private URL getSourceFile(String name, String extension) {
      String filename = name.replace('.', '/') + "." + extension;
      URL ret = this.getResource(filename);
      return this.isFile(ret) && this.getFileForUrl(ret, filename) == null ? null : ret;
   }

   private URL getSourceFile(String name) {
      return this.getSourceFile(name, this.config.getDefaultScriptExtension());
   }

   protected boolean isSourceNewer(URL source, Class cls) throws IOException {
      long lastMod;
      if (this.isFile(source)) {
         String path = source.getPath().replace('/', File.separatorChar).replace('|', ':');
         File file = new File(path);
         lastMod = file.lastModified();
      } else {
         URLConnection conn = source.openConnection();
         lastMod = conn.getLastModified();
         conn.getInputStream().close();
      }

      long classTime = this.getTimeStamp(cls);
      return classTime + (long)this.config.getMinimumRecompilationInterval() < lastMod;
   }

   public void addClasspath(final String path) {
      AccessController.doPrivileged(new PrivilegedAction<Void>() {
         public Void run() {
            try {
               File f = new File(path);
               URL newURL = f.toURI().toURL();
               URL[] urls = GroovyClassLoader.this.getURLs();
               URL[] arr$ = urls;
               int len$ = urls.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  URL url = arr$[i$];
                  if (url.equals(newURL)) {
                     return null;
                  }
               }

               GroovyClassLoader.this.addURL(newURL);
            } catch (MalformedURLException var8) {
            }

            return null;
         }
      });
   }

   public Class[] getLoadedClasses() {
      synchronized(this.classCache) {
         Collection<Class> values = this.classCache.values();
         return (Class[])values.toArray(new Class[values.size()]);
      }
   }

   public void clearCache() {
      synchronized(this.classCache) {
         this.classCache.clear();
      }

      synchronized(this.sourceCache) {
         this.sourceCache.clear();
      }
   }

   public static class ClassCollector extends CompilationUnit.ClassgenCallback {
      private Class generatedClass;
      private final GroovyClassLoader cl;
      private final SourceUnit su;
      private final CompilationUnit unit;
      private final Collection<Class> loadedClasses;

      protected ClassCollector(GroovyClassLoader.InnerLoader cl, CompilationUnit unit, SourceUnit su) {
         this.cl = cl;
         this.unit = unit;
         this.loadedClasses = new ArrayList();
         this.su = su;
      }

      public GroovyClassLoader getDefiningClassLoader() {
         return this.cl;
      }

      protected Class createClass(byte[] code, ClassNode classNode) {
         GroovyClassLoader cl = this.getDefiningClassLoader();
         Class theClass = cl.defineClass(classNode.getName(), code, 0, code.length, this.unit.getAST().getCodeSource());
         this.loadedClasses.add(theClass);
         if (this.generatedClass == null) {
            ModuleNode mn = classNode.getModule();
            SourceUnit msu = null;
            if (mn != null) {
               msu = mn.getContext();
            }

            ClassNode main = null;
            if (mn != null) {
               main = (ClassNode)mn.getClasses().get(0);
            }

            if (msu == this.su && main == classNode) {
               this.generatedClass = theClass;
            }
         }

         return theClass;
      }

      protected Class onClassNode(ClassWriter classWriter, ClassNode classNode) {
         byte[] code = classWriter.toByteArray();
         return this.createClass(code, classNode);
      }

      public void call(ClassVisitor classWriter, ClassNode classNode) {
         this.onClassNode((ClassWriter)classWriter, classNode);
      }

      public Collection getLoadedClasses() {
         return this.loadedClasses;
      }
   }

   public static class InnerLoader extends GroovyClassLoader {
      private final GroovyClassLoader delegate;
      private final long timeStamp;

      public InnerLoader(GroovyClassLoader delegate) {
         super(delegate);
         this.delegate = delegate;
         this.timeStamp = System.currentTimeMillis();
      }

      public void addClasspath(String path) {
         this.delegate.addClasspath(path);
      }

      public void clearCache() {
         this.delegate.clearCache();
      }

      public URL findResource(String name) {
         return this.delegate.findResource(name);
      }

      public Enumeration findResources(String name) throws IOException {
         return this.delegate.findResources(name);
      }

      public Class[] getLoadedClasses() {
         return this.delegate.getLoadedClasses();
      }

      public URL getResource(String name) {
         return this.delegate.getResource(name);
      }

      public InputStream getResourceAsStream(String name) {
         return this.delegate.getResourceAsStream(name);
      }

      public GroovyResourceLoader getResourceLoader() {
         return this.delegate.getResourceLoader();
      }

      public URL[] getURLs() {
         return this.delegate.getURLs();
      }

      public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve) throws ClassNotFoundException, CompilationFailedException {
         Class c = this.findLoadedClass(name);
         return c != null ? c : this.delegate.loadClass(name, lookupScriptFiles, preferClassOverScript, resolve);
      }

      public Class parseClass(GroovyCodeSource codeSource, boolean shouldCache) throws CompilationFailedException {
         return this.delegate.parseClass(codeSource, shouldCache);
      }

      public void setResourceLoader(GroovyResourceLoader resourceLoader) {
         this.delegate.setResourceLoader(resourceLoader);
      }

      public void addURL(URL url) {
         this.delegate.addURL(url);
      }

      public long getTimeStamp() {
         return this.timeStamp;
      }
   }
}
