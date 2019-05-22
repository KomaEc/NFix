package bsh.classpath;

import bsh.ClassPathException;
import bsh.NameSource;
import bsh.StringUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BshClassPath implements ClassPathListener, NameSource {
   String name;
   private List path;
   private List compPaths;
   private Map packageMap;
   private Map classSource;
   private boolean mapsInitialized;
   private BshClassPath.UnqualifiedNameTable unqNameTable;
   private boolean nameCompletionIncludesUnqNames;
   Vector listeners;
   static URL[] userClassPathComp;
   static BshClassPath userClassPath;
   static BshClassPath bootClassPath;
   List nameSourceListeners;
   static BshClassPath.MappingFeedback mappingFeedbackListener;
   // $FF: synthetic field
   static Class class$java$lang$Class;

   public BshClassPath(String var1) {
      this.nameCompletionIncludesUnqNames = true;
      this.listeners = new Vector();
      this.name = var1;
      this.reset();
   }

   public BshClassPath(String var1, URL[] var2) {
      this(var1);
      this.add(var2);
   }

   public void setPath(URL[] var1) {
      this.reset();
      this.add(var1);
   }

   public void addComponent(BshClassPath var1) {
      if (this.compPaths == null) {
         this.compPaths = new ArrayList();
      }

      this.compPaths.add(var1);
      var1.addListener(this);
   }

   public void add(URL[] var1) {
      this.path.addAll(Arrays.asList(var1));
      if (this.mapsInitialized) {
         this.map(var1);
      }

   }

   public void add(URL var1) throws IOException {
      this.path.add(var1);
      if (this.mapsInitialized) {
         this.map(var1);
      }

   }

   public URL[] getPathComponents() {
      return (URL[])this.getFullPath().toArray(new URL[0]);
   }

   public synchronized Set getClassesForPackage(String var1) {
      this.insureInitialized();
      HashSet var2 = new HashSet();
      Collection var3 = (Collection)this.packageMap.get(var1);
      if (var3 != null) {
         var2.addAll(var3);
      }

      if (this.compPaths != null) {
         for(int var4 = 0; var4 < this.compPaths.size(); ++var4) {
            Set var5 = ((BshClassPath)this.compPaths.get(var4)).getClassesForPackage(var1);
            if (var5 != null) {
               var2.addAll(var5);
            }
         }
      }

      return var2;
   }

   public synchronized BshClassPath.ClassSource getClassSource(String var1) {
      BshClassPath.ClassSource var2 = (BshClassPath.ClassSource)this.classSource.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         this.insureInitialized();
         var2 = (BshClassPath.ClassSource)this.classSource.get(var1);
         if (var2 == null && this.compPaths != null) {
            for(int var3 = 0; var3 < this.compPaths.size() && var2 == null; ++var3) {
               var2 = ((BshClassPath)this.compPaths.get(var3)).getClassSource(var1);
            }
         }

         return var2;
      }
   }

   public synchronized void setClassSource(String var1, BshClassPath.ClassSource var2) {
      this.classSource.put(var1, var2);
   }

   public void insureInitialized() {
      this.insureInitialized(true);
   }

   protected synchronized void insureInitialized(boolean var1) {
      if (var1 && !this.mapsInitialized) {
         this.startClassMapping();
      }

      if (this.compPaths != null) {
         for(int var2 = 0; var2 < this.compPaths.size(); ++var2) {
            ((BshClassPath)this.compPaths.get(var2)).insureInitialized(false);
         }
      }

      if (!this.mapsInitialized) {
         this.map((URL[])this.path.toArray(new URL[0]));
      }

      if (var1 && !this.mapsInitialized) {
         this.endClassMapping();
      }

      this.mapsInitialized = true;
   }

   protected List getFullPath() {
      ArrayList var1 = new ArrayList();
      if (this.compPaths != null) {
         for(int var2 = 0; var2 < this.compPaths.size(); ++var2) {
            List var3 = ((BshClassPath)this.compPaths.get(var2)).getFullPath();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (!var1.contains(var5)) {
                  var1.add(var5);
               }
            }
         }
      }

      var1.addAll(this.path);
      return var1;
   }

   public String getClassNameByUnqName(String var1) throws ClassPathException {
      this.insureInitialized();
      BshClassPath.UnqualifiedNameTable var2 = this.getUnqualifiedNameTable();
      Object var3 = var2.get(var1);
      if (var3 instanceof BshClassPath.AmbiguousName) {
         throw new ClassPathException("Ambigous class names: " + ((BshClassPath.AmbiguousName)var3).get());
      } else {
         return (String)var3;
      }
   }

   private BshClassPath.UnqualifiedNameTable getUnqualifiedNameTable() {
      if (this.unqNameTable == null) {
         this.unqNameTable = this.buildUnqualifiedNameTable();
      }

      return this.unqNameTable;
   }

   private BshClassPath.UnqualifiedNameTable buildUnqualifiedNameTable() {
      BshClassPath.UnqualifiedNameTable var1 = new BshClassPath.UnqualifiedNameTable();
      if (this.compPaths != null) {
         for(int var2 = 0; var2 < this.compPaths.size(); ++var2) {
            Set var3 = ((BshClassPath)this.compPaths.get(var2)).classSource.keySet();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               var1.add((String)var4.next());
            }
         }
      }

      Iterator var5 = this.classSource.keySet().iterator();

      while(var5.hasNext()) {
         var1.add((String)var5.next());
      }

      return var1;
   }

   public String[] getAllNames() {
      this.insureInitialized();
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getPackagesSet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.addAll(removeInnerClassNames(this.getClassesForPackage(var3)));
      }

      if (this.nameCompletionIncludesUnqNames) {
         var1.addAll(this.getUnqualifiedNameTable().keySet());
      }

      return (String[])var1.toArray(new String[0]);
   }

   synchronized void map(URL[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         try {
            this.map(var1[var2]);
         } catch (IOException var5) {
            String var4 = "Error constructing classpath: " + var1[var2] + ": " + var5;
            this.errorWhileMapping(var4);
         }
      }

   }

   synchronized void map(URL var1) throws IOException {
      String var2 = var1.getFile();
      File var3 = new File(var2);
      if (var3.isDirectory()) {
         this.classMapping("Directory " + var3.toString());
         this.map(traverseDirForClasses(var3), new BshClassPath.DirClassSource(var3));
      } else if (isArchiveFileName(var2)) {
         this.classMapping("Archive: " + var1);
         this.map(searchJarForClasses(var1), new BshClassPath.JarClassSource(var1));
      } else {
         String var4 = "Not a classpath component: " + var2;
         this.errorWhileMapping(var4);
      }

   }

   private void map(String[] var1, Object var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.mapClass(var1[var3], var2);
      }

   }

   private void mapClass(String var1, Object var2) {
      String[] var3 = splitClassname(var1);
      String var4 = var3[0];
      String var5 = var3[1];
      Object var6 = (Set)this.packageMap.get(var4);
      if (var6 == null) {
         var6 = new HashSet();
         this.packageMap.put(var4, var6);
      }

      ((Set)var6).add(var1);
      Object var7 = this.classSource.get(var1);
      if (var7 == null) {
         this.classSource.put(var1, var2);
      }

   }

   private synchronized void reset() {
      this.path = new ArrayList();
      this.compPaths = null;
      this.clearCachedStructures();
   }

   private synchronized void clearCachedStructures() {
      this.mapsInitialized = false;
      this.packageMap = new HashMap();
      this.classSource = new HashMap();
      this.unqNameTable = null;
      this.nameSpaceChanged();
   }

   public void classPathChanged() {
      this.clearCachedStructures();
      this.notifyListeners();
   }

   static String[] traverseDirForClasses(File var0) throws IOException {
      List var1 = traverseDirForClassesAux(var0, var0);
      return (String[])var1.toArray(new String[0]);
   }

   static List traverseDirForClassesAux(File var0, File var1) throws IOException {
      ArrayList var2 = new ArrayList();
      String var3 = var0.getAbsolutePath();
      File[] var4 = var1.listFiles();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         File var6 = var4[var5];
         if (var6.isDirectory()) {
            var2.addAll(traverseDirForClassesAux(var0, var6));
         } else {
            String var7 = var6.getAbsolutePath();
            if (isClassFileName(var7)) {
               if (!var7.startsWith(var3)) {
                  throw new IOException("problem parsing paths");
               }

               var7 = var7.substring(var3.length() + 1);
               var7 = canonicalizeClassName(var7);
               var2.add(var7);
            }
         }
      }

      return var2;
   }

   static String[] searchJarForClasses(URL var0) throws IOException {
      Vector var1 = new Vector();
      InputStream var2 = var0.openStream();
      ZipInputStream var3 = new ZipInputStream(var2);

      ZipEntry var4;
      while((var4 = var3.getNextEntry()) != null) {
         String var5 = var4.getName();
         if (isClassFileName(var5)) {
            var1.addElement(canonicalizeClassName(var5));
         }
      }

      var3.close();
      String[] var6 = new String[var1.size()];
      var1.copyInto(var6);
      return var6;
   }

   public static boolean isClassFileName(String var0) {
      return var0.toLowerCase().endsWith(".class");
   }

   public static boolean isArchiveFileName(String var0) {
      var0 = var0.toLowerCase();
      return var0.endsWith(".jar") || var0.endsWith(".zip");
   }

   public static String canonicalizeClassName(String var0) {
      String var1 = var0.replace('/', '.');
      var1 = var1.replace('\\', '.');
      if (var1.startsWith("class ")) {
         var1 = var1.substring(6);
      }

      if (var1.endsWith(".class")) {
         var1 = var1.substring(0, var1.length() - 6);
      }

      return var1;
   }

   public static String[] splitClassname(String var0) {
      var0 = canonicalizeClassName(var0);
      int var1 = var0.lastIndexOf(".");
      String var2;
      String var3;
      if (var1 == -1) {
         var2 = var0;
         var3 = "<unpackaged>";
      } else {
         var3 = var0.substring(0, var1);
         var2 = var0.substring(var1 + 1);
      }

      return new String[]{var3, var2};
   }

   public static Collection removeInnerClassNames(Collection var0) {
      ArrayList var1 = new ArrayList();
      var1.addAll(var0);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (var3.indexOf("$") != -1) {
            var2.remove();
         }
      }

      return var1;
   }

   public static URL[] getUserClassPathComponents() throws ClassPathException {
      if (userClassPathComp != null) {
         return userClassPathComp;
      } else {
         String var0 = System.getProperty("java.class.path");
         String[] var1 = StringUtil.split(var0, File.pathSeparator);
         URL[] var2 = new URL[var1.length];

         try {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               var2[var3] = (new File((new File(var1[var3])).getCanonicalPath())).toURL();
            }
         } catch (IOException var4) {
            throw new ClassPathException("can't parse class path: " + var4);
         }

         userClassPathComp = var2;
         return var2;
      }
   }

   public Set getPackagesSet() {
      this.insureInitialized();
      HashSet var1 = new HashSet();
      var1.addAll(this.packageMap.keySet());
      if (this.compPaths != null) {
         for(int var2 = 0; var2 < this.compPaths.size(); ++var2) {
            var1.addAll(((BshClassPath)this.compPaths.get(var2)).packageMap.keySet());
         }
      }

      return var1;
   }

   public void addListener(ClassPathListener var1) {
      this.listeners.addElement(new WeakReference(var1));
   }

   public void removeListener(ClassPathListener var1) {
      this.listeners.removeElement(var1);
   }

   void notifyListeners() {
      Enumeration var1 = this.listeners.elements();

      while(var1.hasMoreElements()) {
         WeakReference var2 = (WeakReference)var1.nextElement();
         ClassPathListener var3 = (ClassPathListener)var2.get();
         if (var3 == null) {
            this.listeners.removeElement(var2);
         } else {
            var3.classPathChanged();
         }
      }

   }

   public static BshClassPath getUserClassPath() throws ClassPathException {
      if (userClassPath == null) {
         userClassPath = new BshClassPath("User Class Path", getUserClassPathComponents());
      }

      return userClassPath;
   }

   public static BshClassPath getBootClassPath() throws ClassPathException {
      if (bootClassPath == null) {
         try {
            String var0 = getRTJarPath();
            URL var1 = (new File(var0)).toURL();
            bootClassPath = new BshClassPath("Boot Class Path", new URL[]{var1});
         } catch (MalformedURLException var2) {
            throw new ClassPathException(" can't find boot jar: " + var2);
         }
      }

      return bootClassPath;
   }

   private static String getRTJarPath() {
      String var0 = (class$java$lang$Class == null ? (class$java$lang$Class = class$("java.lang.Class")) : class$java$lang$Class).getResource("/java/lang/String.class").toExternalForm();
      if (!var0.startsWith("jar:file:")) {
         return null;
      } else {
         int var1 = var0.indexOf("!");
         return var1 == -1 ? null : var0.substring("jar:file:".length(), var1);
      }
   }

   public static void main(String[] var0) throws Exception {
      URL[] var1 = new URL[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = (new File(var0[var2])).toURL();
      }

      new BshClassPath("Test", var1);
   }

   public String toString() {
      return "BshClassPath " + this.name + "(" + super.toString() + ") path= " + this.path + "\n" + "compPaths = {" + this.compPaths + " }";
   }

   void nameSpaceChanged() {
      if (this.nameSourceListeners != null) {
         for(int var1 = 0; var1 < this.nameSourceListeners.size(); ++var1) {
            ((NameSource.Listener)this.nameSourceListeners.get(var1)).nameSourceChanged(this);
         }

      }
   }

   public void addNameSourceListener(NameSource.Listener var1) {
      if (this.nameSourceListeners == null) {
         this.nameSourceListeners = new ArrayList();
      }

      this.nameSourceListeners.add(var1);
   }

   public static void addMappingFeedback(BshClassPath.MappingFeedback var0) {
      if (mappingFeedbackListener != null) {
         throw new RuntimeException("Unimplemented: already a listener");
      } else {
         mappingFeedbackListener = var0;
      }
   }

   void startClassMapping() {
      if (mappingFeedbackListener != null) {
         mappingFeedbackListener.startClassMapping();
      } else {
         System.err.println("Start ClassPath Mapping");
      }

   }

   void classMapping(String var1) {
      if (mappingFeedbackListener != null) {
         mappingFeedbackListener.classMapping(var1);
      } else {
         System.err.println("Mapping: " + var1);
      }

   }

   void errorWhileMapping(String var1) {
      if (mappingFeedbackListener != null) {
         mappingFeedbackListener.errorWhileMapping(var1);
      } else {
         System.err.println(var1);
      }

   }

   void endClassMapping() {
      if (mappingFeedbackListener != null) {
         mappingFeedbackListener.endClassMapping();
      } else {
         System.err.println("End ClassPath Mapping");
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public interface MappingFeedback {
      void startClassMapping();

      void classMapping(String var1);

      void errorWhileMapping(String var1);

      void endClassMapping();
   }

   public static class AmbiguousName {
      List list = new ArrayList();

      public void add(String var1) {
         this.list.add(var1);
      }

      public List get() {
         return this.list;
      }
   }

   static class UnqualifiedNameTable extends HashMap {
      void add(String var1) {
         String var2 = BshClassPath.splitClassname(var1)[1];
         Object var3 = super.get(var2);
         if (var3 == null) {
            super.put(var2, var1);
         } else if (var3 instanceof BshClassPath.AmbiguousName) {
            ((BshClassPath.AmbiguousName)var3).add(var1);
         } else {
            BshClassPath.AmbiguousName var4 = new BshClassPath.AmbiguousName();
            var4.add((String)var3);
            var4.add(var1);
            super.put(var2, var4);
         }

      }
   }

   public static class GeneratedClassSource extends BshClassPath.ClassSource {
      GeneratedClassSource(byte[] var1) {
         this.source = var1;
      }

      public byte[] getCode(String var1) {
         return (byte[])this.source;
      }
   }

   public static class DirClassSource extends BshClassPath.ClassSource {
      DirClassSource(File var1) {
         this.source = var1;
      }

      public File getDir() {
         return (File)this.source;
      }

      public String toString() {
         return "Dir: " + this.source;
      }

      public byte[] getCode(String var1) {
         return readBytesFromFile(this.getDir(), var1);
      }

      public static byte[] readBytesFromFile(File var0, String var1) {
         String var2 = var1.replace('.', File.separatorChar) + ".class";
         File var3 = new File(var0, var2);
         if (var3 != null && var3.exists()) {
            try {
               FileInputStream var4 = new FileInputStream(var3);
               DataInputStream var5 = new DataInputStream(var4);
               byte[] var6 = new byte[(int)var3.length()];
               var5.readFully(var6);
               var5.close();
               return var6;
            } catch (IOException var7) {
               throw new RuntimeException("Couldn't load file: " + var3);
            }
         } else {
            return null;
         }
      }
   }

   public static class JarClassSource extends BshClassPath.ClassSource {
      JarClassSource(URL var1) {
         this.source = var1;
      }

      public URL getURL() {
         return (URL)this.source;
      }

      public byte[] getCode(String var1) {
         throw new Error("Unimplemented");
      }

      public String toString() {
         return "Jar: " + this.source;
      }
   }

   public abstract static class ClassSource {
      Object source;

      abstract byte[] getCode(String var1);
   }
}
