package org.apache.tools.ant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Attributes.Name;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.ManifestException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.CollectionUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.LoaderUtils;

public class AntClassLoader extends ClassLoader implements SubBuildListener {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final int BUFFER_SIZE = 8192;
   private static final int NUMBER_OF_STRINGS = 256;
   private Vector pathComponents;
   private Project project;
   private boolean parentFirst;
   private Vector systemPackages;
   private Vector loaderPackages;
   private boolean ignoreBase;
   private ClassLoader parent;
   private Hashtable zipFiles;
   private static Map pathMap = Collections.synchronizedMap(new HashMap());
   private ClassLoader savedContextLoader;
   private boolean isContextLoaderSaved;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$AntClassLoader;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;

   public AntClassLoader(ClassLoader parent, Project project, Path classpath) {
      this.pathComponents = new Vector();
      this.parentFirst = true;
      this.systemPackages = new Vector();
      this.loaderPackages = new Vector();
      this.ignoreBase = false;
      this.parent = null;
      this.zipFiles = new Hashtable();
      this.savedContextLoader = null;
      this.isContextLoaderSaved = false;
      this.setParent(parent);
      this.setClassPath(classpath);
      this.setProject(project);
   }

   public AntClassLoader() {
      this.pathComponents = new Vector();
      this.parentFirst = true;
      this.systemPackages = new Vector();
      this.loaderPackages = new Vector();
      this.ignoreBase = false;
      this.parent = null;
      this.zipFiles = new Hashtable();
      this.savedContextLoader = null;
      this.isContextLoaderSaved = false;
      this.setParent((ClassLoader)null);
   }

   public AntClassLoader(Project project, Path classpath) {
      this.pathComponents = new Vector();
      this.parentFirst = true;
      this.systemPackages = new Vector();
      this.loaderPackages = new Vector();
      this.ignoreBase = false;
      this.parent = null;
      this.zipFiles = new Hashtable();
      this.savedContextLoader = null;
      this.isContextLoaderSaved = false;
      this.setParent((ClassLoader)null);
      this.setProject(project);
      this.setClassPath(classpath);
   }

   public AntClassLoader(ClassLoader parent, Project project, Path classpath, boolean parentFirst) {
      this(project, classpath);
      if (parent != null) {
         this.setParent(parent);
      }

      this.setParentFirst(parentFirst);
      this.addJavaLibraries();
   }

   public AntClassLoader(Project project, Path classpath, boolean parentFirst) {
      this((ClassLoader)null, project, classpath, parentFirst);
   }

   public AntClassLoader(ClassLoader parent, boolean parentFirst) {
      this.pathComponents = new Vector();
      this.parentFirst = true;
      this.systemPackages = new Vector();
      this.loaderPackages = new Vector();
      this.ignoreBase = false;
      this.parent = null;
      this.zipFiles = new Hashtable();
      this.savedContextLoader = null;
      this.isContextLoaderSaved = false;
      this.setParent(parent);
      this.project = null;
      this.parentFirst = parentFirst;
   }

   public void setProject(Project project) {
      this.project = project;
      if (project != null) {
         project.addBuildListener(this);
      }

   }

   public void setClassPath(Path classpath) {
      this.pathComponents.removeAllElements();
      if (classpath != null) {
         Path actualClasspath = classpath.concatSystemClasspath("ignore");
         String[] pathElements = actualClasspath.list();

         for(int i = 0; i < pathElements.length; ++i) {
            try {
               this.addPathElement(pathElements[i]);
            } catch (BuildException var6) {
            }
         }
      }

   }

   public void setParent(ClassLoader parent) {
      if (parent == null) {
         this.parent = (class$org$apache$tools$ant$AntClassLoader == null ? (class$org$apache$tools$ant$AntClassLoader = class$("org.apache.tools.ant.AntClassLoader")) : class$org$apache$tools$ant$AntClassLoader).getClassLoader();
      } else {
         this.parent = parent;
      }

   }

   public void setParentFirst(boolean parentFirst) {
      this.parentFirst = parentFirst;
   }

   protected void log(String message, int priority) {
      if (this.project != null) {
         this.project.log(message, priority);
      }

   }

   public void setThreadContextLoader() {
      if (this.isContextLoaderSaved) {
         throw new BuildException("Context loader has not been reset");
      } else {
         if (LoaderUtils.isContextLoaderAvailable()) {
            this.savedContextLoader = LoaderUtils.getContextClassLoader();
            ClassLoader loader = this;
            if (this.project != null && "only".equals(this.project.getProperty("build.sysclasspath"))) {
               loader = this.getClass().getClassLoader();
            }

            LoaderUtils.setContextClassLoader((ClassLoader)loader);
            this.isContextLoaderSaved = true;
         }

      }
   }

   public void resetThreadContextLoader() {
      if (LoaderUtils.isContextLoaderAvailable() && this.isContextLoaderSaved) {
         LoaderUtils.setContextClassLoader(this.savedContextLoader);
         this.savedContextLoader = null;
         this.isContextLoaderSaved = false;
      }

   }

   public void addPathElement(String pathElement) throws BuildException {
      File pathComponent = this.project != null ? this.project.resolveFile(pathElement) : new File(pathElement);

      try {
         this.addPathFile(pathComponent);
      } catch (IOException var4) {
         throw new BuildException(var4);
      }
   }

   protected void addPathFile(File pathComponent) throws IOException {
      this.pathComponents.addElement(pathComponent);
      if (!pathComponent.isDirectory()) {
         String absPathPlusTimeAndLength = pathComponent.getAbsolutePath() + pathComponent.lastModified() + "-" + pathComponent.length();
         String classpath = (String)pathMap.get(absPathPlusTimeAndLength);
         if (classpath == null) {
            ZipFile jarFile = null;
            InputStream manifestStream = null;

            label162: {
               try {
                  jarFile = new ZipFile(pathComponent);
                  manifestStream = jarFile.getInputStream(new ZipEntry("META-INF/MANIFEST.MF"));
                  if (manifestStream != null) {
                     Reader manifestReader = new InputStreamReader(manifestStream, "UTF-8");
                     Manifest manifest = new Manifest(manifestReader);
                     classpath = manifest.getMainSection().getAttributeValue("Class-Path");
                     break label162;
                  }
               } catch (ManifestException var12) {
                  break label162;
               } finally {
                  if (manifestStream != null) {
                     manifestStream.close();
                  }

                  if (jarFile != null) {
                     jarFile.close();
                  }

               }

               return;
            }

            if (classpath == null) {
               classpath = "";
            }

            pathMap.put(absPathPlusTimeAndLength, classpath);
         }

         if (!"".equals(classpath)) {
            URL baseURL = FILE_UTILS.getFileURL(pathComponent);
            StringTokenizer st = new StringTokenizer(classpath);

            while(st.hasMoreTokens()) {
               String classpathElement = st.nextToken();
               URL libraryURL = new URL(baseURL, classpathElement);
               if (!libraryURL.getProtocol().equals("file")) {
                  this.log("Skipping jar library " + classpathElement + " since only relative URLs are supported by this" + " loader", 3);
               } else {
                  String decodedPath = Locator.decodeUri(libraryURL.getFile());
                  File libraryFile = new File(decodedPath);
                  if (libraryFile.exists() && !this.isInPath(libraryFile)) {
                     this.addPathFile(libraryFile);
                  }
               }
            }
         }

      }
   }

   public String getClasspath() {
      StringBuffer sb = new StringBuffer();
      boolean firstPass = true;

      for(Enumeration componentEnum = this.pathComponents.elements(); componentEnum.hasMoreElements(); sb.append(((File)componentEnum.nextElement()).getAbsolutePath())) {
         if (!firstPass) {
            sb.append(System.getProperty("path.separator"));
         } else {
            firstPass = false;
         }
      }

      return sb.toString();
   }

   public synchronized void setIsolated(boolean isolated) {
      this.ignoreBase = isolated;
   }

   /** @deprecated */
   public static void initializeClass(Class theClass) {
      Constructor[] cons = theClass.getDeclaredConstructors();
      if (cons != null && cons.length > 0 && cons[0] != null) {
         String[] strs = new String[256];

         try {
            cons[0].newInstance((Object[])strs);
         } catch (Exception var4) {
         }
      }

   }

   public void addSystemPackageRoot(String packageRoot) {
      this.systemPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
   }

   public void addLoaderPackageRoot(String packageRoot) {
      this.loaderPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
   }

   public Class forceLoadClass(String classname) throws ClassNotFoundException {
      this.log("force loading " + classname, 4);
      Class theClass = this.findLoadedClass(classname);
      if (theClass == null) {
         theClass = this.findClass(classname);
      }

      return theClass;
   }

   public Class forceLoadSystemClass(String classname) throws ClassNotFoundException {
      this.log("force system loading " + classname, 4);
      Class theClass = this.findLoadedClass(classname);
      if (theClass == null) {
         theClass = this.findBaseClass(classname);
      }

      return theClass;
   }

   public InputStream getResourceAsStream(String name) {
      InputStream resourceStream = null;
      if (this.isParentFirst(name)) {
         resourceStream = this.loadBaseResource(name);
         if (resourceStream != null) {
            this.log("ResourceStream for " + name + " loaded from parent loader", 4);
         } else {
            resourceStream = this.loadResource(name);
            if (resourceStream != null) {
               this.log("ResourceStream for " + name + " loaded from ant loader", 4);
            }
         }
      } else {
         resourceStream = this.loadResource(name);
         if (resourceStream != null) {
            this.log("ResourceStream for " + name + " loaded from ant loader", 4);
         } else {
            resourceStream = this.loadBaseResource(name);
            if (resourceStream != null) {
               this.log("ResourceStream for " + name + " loaded from parent loader", 4);
            }
         }
      }

      if (resourceStream == null) {
         this.log("Couldn't load ResourceStream for " + name, 4);
      }

      return resourceStream;
   }

   private InputStream loadResource(String name) {
      InputStream stream = null;

      File pathComponent;
      for(Enumeration e = this.pathComponents.elements(); e.hasMoreElements() && stream == null; stream = this.getResourceStream(pathComponent, name)) {
         pathComponent = (File)e.nextElement();
      }

      return stream;
   }

   private InputStream loadBaseResource(String name) {
      return this.parent == null ? getSystemResourceAsStream(name) : this.parent.getResourceAsStream(name);
   }

   private InputStream getResourceStream(File file, String resourceName) {
      try {
         if (!file.exists()) {
            return null;
         }

         if (file.isDirectory()) {
            File resource = new File(file, resourceName);
            if (resource.exists()) {
               return new FileInputStream(resource);
            }
         } else {
            ZipFile zipFile = (ZipFile)this.zipFiles.get(file);
            if (zipFile == null) {
               zipFile = new ZipFile(file);
               this.zipFiles.put(file, zipFile);
            }

            ZipEntry entry = zipFile.getEntry(resourceName);
            if (entry != null) {
               return zipFile.getInputStream(entry);
            }
         }
      } catch (Exception var5) {
         this.log("Ignoring Exception " + var5.getClass().getName() + ": " + var5.getMessage() + " reading resource " + resourceName + " from " + file, 3);
      }

      return null;
   }

   private boolean isParentFirst(String resourceName) {
      boolean useParentFirst = this.parentFirst;
      Enumeration e = this.systemPackages.elements();

      String packageName;
      while(e.hasMoreElements()) {
         packageName = (String)e.nextElement();
         if (resourceName.startsWith(packageName)) {
            useParentFirst = true;
            break;
         }
      }

      e = this.loaderPackages.elements();

      while(e.hasMoreElements()) {
         packageName = (String)e.nextElement();
         if (resourceName.startsWith(packageName)) {
            useParentFirst = false;
            break;
         }
      }

      return useParentFirst;
   }

   private ClassLoader getRootLoader() {
      ClassLoader ret;
      for(ret = this.getClass().getClassLoader(); ret != null && ret.getParent() != null; ret = ret.getParent()) {
      }

      return ret;
   }

   public URL getResource(String name) {
      URL url = null;
      if (this.isParentFirst(name)) {
         url = this.parent == null ? super.getResource(name) : this.parent.getResource(name);
      }

      if (url != null) {
         this.log("Resource " + name + " loaded from parent loader", 4);
      } else {
         Enumeration e = this.pathComponents.elements();

         while(e.hasMoreElements() && url == null) {
            File pathComponent = (File)e.nextElement();
            url = this.getResourceURL(pathComponent, name);
            if (url != null) {
               this.log("Resource " + name + " loaded from ant loader", 4);
            }
         }
      }

      if (url == null && !this.isParentFirst(name)) {
         if (this.ignoreBase) {
            url = this.getRootLoader() == null ? null : this.getRootLoader().getResource(name);
         } else {
            url = this.parent == null ? super.getResource(name) : this.parent.getResource(name);
         }

         if (url != null) {
            this.log("Resource " + name + " loaded from parent loader", 4);
         }
      }

      if (url == null) {
         this.log("Couldn't load Resource " + name, 4);
      }

      return url;
   }

   protected Enumeration findResources(String name) throws IOException {
      Enumeration mine = new AntClassLoader.ResourceEnumeration(name);
      Object base;
      if (this.parent != null && this.parent != this.getParent()) {
         base = this.parent.getResources(name);
      } else {
         base = new CollectionUtils.EmptyEnumeration();
      }

      if (this.isParentFirst(name)) {
         return CollectionUtils.append((Enumeration)base, mine);
      } else if (this.ignoreBase) {
         return (Enumeration)(this.getRootLoader() == null ? mine : CollectionUtils.append(mine, this.getRootLoader().getResources(name)));
      } else {
         return CollectionUtils.append(mine, (Enumeration)base);
      }
   }

   protected URL getResourceURL(File file, String resourceName) {
      try {
         if (!file.exists()) {
            return null;
         }

         if (file.isDirectory()) {
            File resource = new File(file, resourceName);
            if (resource.exists()) {
               try {
                  return FILE_UTILS.getFileURL(resource);
               } catch (MalformedURLException var7) {
                  return null;
               }
            }
         } else {
            ZipFile zipFile = (ZipFile)this.zipFiles.get(file);
            if (zipFile == null) {
               zipFile = new ZipFile(file);
               this.zipFiles.put(file, zipFile);
            }

            ZipEntry entry = zipFile.getEntry(resourceName);
            if (entry != null) {
               try {
                  return new URL("jar:" + FILE_UTILS.getFileURL(file) + "!/" + entry);
               } catch (MalformedURLException var6) {
                  return null;
               }
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return null;
   }

   protected synchronized Class loadClass(String classname, boolean resolve) throws ClassNotFoundException {
      Class theClass = this.findLoadedClass(classname);
      if (theClass != null) {
         return theClass;
      } else {
         if (this.isParentFirst(classname)) {
            try {
               theClass = this.findBaseClass(classname);
               this.log("Class " + classname + " loaded from parent loader " + "(parentFirst)", 4);
            } catch (ClassNotFoundException var5) {
               theClass = this.findClass(classname);
               this.log("Class " + classname + " loaded from ant loader " + "(parentFirst)", 4);
            }
         } else {
            try {
               theClass = this.findClass(classname);
               this.log("Class " + classname + " loaded from ant loader", 4);
            } catch (ClassNotFoundException var6) {
               if (this.ignoreBase) {
                  throw var6;
               }

               theClass = this.findBaseClass(classname);
               this.log("Class " + classname + " loaded from parent loader", 4);
            }
         }

         if (resolve) {
            this.resolveClass(theClass);
         }

         return theClass;
      }
   }

   private String getClassFilename(String classname) {
      return classname.replace('.', '/') + ".class";
   }

   protected Class defineClassFromData(File container, byte[] classData, String classname) throws IOException {
      this.definePackage(container, classname);
      return this.defineClass(classname, classData, 0, classData.length, (class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project).getProtectionDomain());
   }

   protected void definePackage(File container, String className) throws IOException {
      int classIndex = className.lastIndexOf(46);
      if (classIndex != -1) {
         String packageName = className.substring(0, classIndex);
         if (this.getPackage(packageName) == null) {
            java.util.jar.Manifest manifest = this.getJarManifest(container);
            if (manifest == null) {
               this.definePackage(packageName, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
            } else {
               this.definePackage(container, packageName, manifest);
            }

         }
      }
   }

   private java.util.jar.Manifest getJarManifest(File container) throws IOException {
      if (container.isDirectory()) {
         return null;
      } else {
         JarFile jarFile = null;

         java.util.jar.Manifest var3;
         try {
            jarFile = new JarFile(container);
            var3 = jarFile.getManifest();
         } finally {
            if (jarFile != null) {
               jarFile.close();
            }

         }

         return var3;
      }
   }

   protected void definePackage(File container, String packageName, java.util.jar.Manifest manifest) {
      String sectionName = packageName.replace('.', '/') + "/";
      String specificationTitle = null;
      String specificationVendor = null;
      String specificationVersion = null;
      String implementationTitle = null;
      String implementationVendor = null;
      String implementationVersion = null;
      String sealedString = null;
      URL sealBase = null;
      Attributes sectionAttributes = manifest.getAttributes(sectionName);
      if (sectionAttributes != null) {
         specificationTitle = sectionAttributes.getValue(Name.SPECIFICATION_TITLE);
         specificationVendor = sectionAttributes.getValue(Name.SPECIFICATION_VENDOR);
         specificationVersion = sectionAttributes.getValue(Name.SPECIFICATION_VERSION);
         implementationTitle = sectionAttributes.getValue(Name.IMPLEMENTATION_TITLE);
         implementationVendor = sectionAttributes.getValue(Name.IMPLEMENTATION_VENDOR);
         implementationVersion = sectionAttributes.getValue(Name.IMPLEMENTATION_VERSION);
         sealedString = sectionAttributes.getValue(Name.SEALED);
      }

      Attributes mainAttributes = manifest.getMainAttributes();
      if (mainAttributes != null) {
         if (specificationTitle == null) {
            specificationTitle = mainAttributes.getValue(Name.SPECIFICATION_TITLE);
         }

         if (specificationVendor == null) {
            specificationVendor = mainAttributes.getValue(Name.SPECIFICATION_VENDOR);
         }

         if (specificationVersion == null) {
            specificationVersion = mainAttributes.getValue(Name.SPECIFICATION_VERSION);
         }

         if (implementationTitle == null) {
            implementationTitle = mainAttributes.getValue(Name.IMPLEMENTATION_TITLE);
         }

         if (implementationVendor == null) {
            implementationVendor = mainAttributes.getValue(Name.IMPLEMENTATION_VENDOR);
         }

         if (implementationVersion == null) {
            implementationVersion = mainAttributes.getValue(Name.IMPLEMENTATION_VERSION);
         }

         if (sealedString == null) {
            sealedString = mainAttributes.getValue(Name.SEALED);
         }
      }

      if (sealedString != null && sealedString.equalsIgnoreCase("true")) {
         try {
            sealBase = new URL(FileUtils.getFileUtils().toURI(container.getAbsolutePath()));
         } catch (MalformedURLException var16) {
         }
      }

      this.definePackage(packageName, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
   }

   private Class getClassFromStream(InputStream stream, String classname, File container) throws IOException, SecurityException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int bytesRead = true;
      byte[] buffer = new byte[8192];

      int bytesRead;
      while((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
         baos.write(buffer, 0, bytesRead);
      }

      byte[] classData = baos.toByteArray();
      return this.defineClassFromData(container, classData, classname);
   }

   public Class findClass(String name) throws ClassNotFoundException {
      this.log("Finding class " + name, 4);
      return this.findClassInComponents(name);
   }

   protected boolean isInPath(File component) {
      Enumeration e = this.pathComponents.elements();

      File pathComponent;
      do {
         if (!e.hasMoreElements()) {
            return false;
         }

         pathComponent = (File)e.nextElement();
      } while(!pathComponent.equals(component));

      return true;
   }

   private Class findClassInComponents(String name) throws ClassNotFoundException {
      InputStream stream = null;
      String classFilename = this.getClassFilename(name);

      try {
         Enumeration e = this.pathComponents.elements();

         while(e.hasMoreElements()) {
            File pathComponent = (File)e.nextElement();

            try {
               stream = this.getResourceStream(pathComponent, classFilename);
               if (stream != null) {
                  this.log("Loaded from " + pathComponent + " " + classFilename, 4);
                  Class var6 = this.getClassFromStream(stream, name, pathComponent);
                  return var6;
               }
            } catch (SecurityException var16) {
               throw var16;
            } catch (IOException var17) {
               this.log("Exception reading component " + pathComponent + " (reason: " + var17.getMessage() + ")", 3);
            }
         }

         throw new ClassNotFoundException(name);
      } finally {
         try {
            if (stream != null) {
               stream.close();
            }
         } catch (IOException var15) {
         }

      }
   }

   private Class findBaseClass(String name) throws ClassNotFoundException {
      return this.parent == null ? this.findSystemClass(name) : this.parent.loadClass(name);
   }

   public synchronized void cleanup() {
      Enumeration e = this.zipFiles.elements();

      while(e.hasMoreElements()) {
         ZipFile zipFile = (ZipFile)e.nextElement();

         try {
            zipFile.close();
         } catch (IOException var4) {
         }
      }

      this.zipFiles = new Hashtable();
      if (this.project != null) {
         this.project.removeBuildListener(this);
      }

      this.project = null;
   }

   public void buildStarted(BuildEvent event) {
   }

   public void buildFinished(BuildEvent event) {
      this.cleanup();
   }

   public void subBuildFinished(BuildEvent event) {
      if (event.getProject() == this.project) {
         this.cleanup();
      }

   }

   public void subBuildStarted(BuildEvent event) {
   }

   public void targetStarted(BuildEvent event) {
   }

   public void targetFinished(BuildEvent event) {
   }

   public void taskStarted(BuildEvent event) {
   }

   public void taskFinished(BuildEvent event) {
   }

   public void messageLogged(BuildEvent event) {
   }

   public void addJavaLibraries() {
      Vector packages = JavaEnvUtils.getJrePackages();
      Enumeration e = packages.elements();

      while(e.hasMoreElements()) {
         String packageName = (String)e.nextElement();
         this.addSystemPackageRoot(packageName);
      }

   }

   public String toString() {
      return "AntClassLoader[" + this.getClasspath() + "]";
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class ResourceEnumeration implements Enumeration {
      private String resourceName;
      private int pathElementsIndex;
      private URL nextResource;

      ResourceEnumeration(String name) {
         this.resourceName = name;
         this.pathElementsIndex = 0;
         this.findNextResource();
      }

      public boolean hasMoreElements() {
         return this.nextResource != null;
      }

      public Object nextElement() {
         URL ret = this.nextResource;
         this.findNextResource();
         return ret;
      }

      private void findNextResource() {
         URL url = null;

         while(this.pathElementsIndex < AntClassLoader.this.pathComponents.size() && url == null) {
            try {
               File pathComponent = (File)AntClassLoader.this.pathComponents.elementAt(this.pathElementsIndex);
               url = AntClassLoader.this.getResourceURL(pathComponent, this.resourceName);
               ++this.pathElementsIndex;
            } catch (BuildException var3) {
            }
         }

         this.nextResource = url;
      }
   }
}
