package soot;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.asm.AsmClassProvider;
import soot.dexpler.DexFileProvider;
import soot.options.Options;

public class SourceLocator {
   private static final Logger logger = LoggerFactory.getLogger(SourceLocator.class);
   protected Set<ClassLoader> additionalClassLoaders = new HashSet();
   protected List<ClassProvider> classProviders;
   protected List<String> classPath;
   private List<String> sourcePath;
   private LoadingCache<String, SourceLocator.ClassSourceType> pathToSourceType = CacheBuilder.newBuilder().initialCapacity(60).maximumSize(500L).softValues().concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(new CacheLoader<String, SourceLocator.ClassSourceType>() {
      public SourceLocator.ClassSourceType load(String path) throws Exception {
         File f = new File(path);
         if (!f.exists() && !Options.v().ignore_classpath_errors()) {
            throw new Exception("Error: The path '" + path + "' does not exist.");
         } else if (!f.canRead() && !Options.v().ignore_classpath_errors()) {
            throw new Exception("Error: The path '" + path + "' exists but is not readable.");
         } else if (f.isFile()) {
            if (path.endsWith(".zip")) {
               return SourceLocator.ClassSourceType.zip;
            } else if (path.endsWith(".jar")) {
               return SourceLocator.ClassSourceType.jar;
            } else if (path.endsWith(".apk")) {
               return SourceLocator.ClassSourceType.apk;
            } else {
               return path.endsWith(".dex") ? SourceLocator.ClassSourceType.dex : SourceLocator.ClassSourceType.unknown;
            }
         } else {
            return SourceLocator.ClassSourceType.directory;
         }
      }
   });
   private LoadingCache<String, Set<String>> archivePathsToEntriesCache = CacheBuilder.newBuilder().initialCapacity(60).maximumSize(500L).softValues().concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(new CacheLoader<String, Set<String>>() {
      public Set<String> load(String archivePath) throws Exception {
         ZipFile archive = null;

         try {
            archive = new ZipFile(archivePath);
            Set<String> ret = new HashSet();
            Enumeration it = archive.entries();

            while(it.hasMoreElements()) {
               ret.add(((ZipEntry)it.nextElement()).getName());
            }

            HashSet var5 = ret;
            return var5;
         } finally {
            if (archive != null) {
               archive.close();
            }

         }
      }
   });
   private Set<String> dexClassPathExtensions;
   private Map<String, File> dexClassIndex;

   public SourceLocator(Singletons.Global g) {
   }

   public static SourceLocator v() {
      return G.v().soot_SourceLocator();
   }

   public static void ensureDirectoryExists(File dir) {
      if (dir != null && !dir.exists()) {
         try {
            dir.mkdirs();
         } catch (SecurityException var2) {
            logger.debug("Unable to create " + dir);
            throw new CompilationDeathException(0);
         }
      }

   }

   public static List<String> explodeClassPath(String classPath) {
      List<String> ret = new ArrayList();
      String regex = "(?<!\\\\)" + Pattern.quote(File.pathSeparator);
      String[] var3 = classPath.split(regex);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String originalDir = var3[var5];

         try {
            String canonicalDir = (new File(originalDir)).getCanonicalPath();
            ret.add(canonicalDir);
         } catch (IOException var8) {
            throw new CompilationDeathException("Couldn't resolve classpath entry " + originalDir + ": " + var8);
         }
      }

      return ret;
   }

   public ClassSource getClassSource(String className) {
      if (this.classPath == null) {
         this.classPath = explodeClassPath(Scene.v().getSootClassPath());
      }

      if (this.classProviders == null) {
         this.setupClassProviders();
      }

      JavaClassProvider.JarException ex = null;
      Iterator var3 = this.classProviders.iterator();

      ClassSource ret;
      while(var3.hasNext()) {
         ClassProvider cp = (ClassProvider)var3.next();

         try {
            ret = cp.find(className);
            if (ret != null) {
               return ret;
            }
         } catch (JavaClassProvider.JarException var7) {
            ex = var7;
         }
      }

      if (ex != null) {
         throw ex;
      } else {
         var3 = this.additionalClassLoaders.iterator();

         final ClassLoader cl;
         while(var3.hasNext()) {
            cl = (ClassLoader)var3.next();

            try {
               ret = (new ClassProvider() {
                  public ClassSource find(String className) {
                     String fileName = className.replace('.', '/') + ".class";
                     InputStream stream = cl.getResourceAsStream(fileName);
                     return stream == null ? null : new CoffiClassSource(className, stream, fileName);
                  }
               }).find(className);
               if (ret != null) {
                  return ret;
               }
            } catch (JavaClassProvider.JarException var6) {
               ex = var6;
            }
         }

         if (ex != null) {
            throw ex;
         } else {
            if (className.startsWith("soot.rtlib.tamiflex.")) {
               String fileName = className.replace('.', '/') + ".class";
               cl = this.getClass().getClassLoader();
               if (cl == null) {
                  return null;
               }

               InputStream stream = cl.getResourceAsStream(fileName);
               if (stream != null) {
                  return new CoffiClassSource(className, stream, fileName);
               }
            }

            return null;
         }
      }
   }

   public void additionalClassLoader(ClassLoader c) {
      this.additionalClassLoaders.add(c);
   }

   protected void setupClassProviders() {
      this.classProviders = new LinkedList();
      ClassProvider classFileClassProvider = Options.v().coffi() ? new CoffiClassProvider() : new AsmClassProvider();
      switch(Options.v().src_prec()) {
      case 1:
         this.classProviders.add(classFileClassProvider);
         this.classProviders.add(new JimpleClassProvider());
         this.classProviders.add(new JavaClassProvider());
         break;
      case 2:
         this.classProviders.add(classFileClassProvider);
         break;
      case 3:
         this.classProviders.add(new JimpleClassProvider());
         this.classProviders.add(classFileClassProvider);
         this.classProviders.add(new JavaClassProvider());
         break;
      case 4:
         this.classProviders.add(new JavaClassProvider());
         this.classProviders.add(classFileClassProvider);
         this.classProviders.add(new JimpleClassProvider());
         break;
      case 5:
         this.classProviders.add(new DexClassProvider());
         this.classProviders.add(classFileClassProvider);
         this.classProviders.add(new JavaClassProvider());
         this.classProviders.add(new JimpleClassProvider());
         break;
      case 6:
         this.classProviders.add(new DexClassProvider());
         this.classProviders.add(classFileClassProvider);
         this.classProviders.add(new JimpleClassProvider());
         break;
      default:
         throw new RuntimeException("Other source precedences are not currently supported.");
      }

   }

   public void setClassProviders(List<ClassProvider> classProviders) {
      this.classProviders = classProviders;
   }

   public List<String> classPath() {
      return this.classPath;
   }

   public void invalidateClassPath() {
      this.classPath = null;
      this.dexClassIndex = null;
   }

   public List<String> sourcePath() {
      if (this.sourcePath == null) {
         this.sourcePath = new ArrayList();
         Iterator var1 = this.classPath.iterator();

         while(var1.hasNext()) {
            String dir = (String)var1.next();
            SourceLocator.ClassSourceType cst = this.getClassSourceType(dir);
            if (cst != SourceLocator.ClassSourceType.apk && cst != SourceLocator.ClassSourceType.jar && cst != SourceLocator.ClassSourceType.zip) {
               this.sourcePath.add(dir);
            }
         }
      }

      return this.sourcePath;
   }

   private SourceLocator.ClassSourceType getClassSourceType(String path) {
      try {
         return (SourceLocator.ClassSourceType)this.pathToSourceType.get(path);
      } catch (Exception var3) {
         throw new RuntimeException(var3);
      }
   }

   public List<String> getClassesUnder(String aPath) {
      return this.getClassesUnder(aPath, "");
   }

   private List<String> getClassesUnder(String aPath, String prefix) {
      List<String> classes = new ArrayList();
      SourceLocator.ClassSourceType cst = this.getClassSourceType(aPath);
      if (cst != SourceLocator.ClassSourceType.apk && cst != SourceLocator.ClassSourceType.dex) {
         int extensionIndex;
         if (cst != SourceLocator.ClassSourceType.jar && cst != SourceLocator.ClassSourceType.zip) {
            if (cst != SourceLocator.ClassSourceType.directory) {
               throw new RuntimeException("Invalid class source type");
            }

            File file = new File(aPath);
            File[] files = file.listFiles();
            if (files == null) {
               files = new File[]{file};
            }

            File[] var35 = files;
            int var36 = files.length;

            for(extensionIndex = 0; extensionIndex < var36; ++extensionIndex) {
               File element = var35[extensionIndex];
               if (element.isDirectory()) {
                  classes.addAll(this.getClassesUnder(aPath + File.separatorChar + element.getName(), prefix + element.getName() + "."));
               } else {
                  String fileName = element.getName();
                  int index;
                  if (fileName.endsWith(".class")) {
                     index = fileName.lastIndexOf(".class");
                     classes.add(prefix + fileName.substring(0, index));
                  } else if (fileName.endsWith(".jimple")) {
                     index = fileName.lastIndexOf(".jimple");
                     classes.add(prefix + fileName.substring(0, index));
                  } else if (fileName.endsWith(".java")) {
                     index = fileName.lastIndexOf(".java");
                     classes.add(prefix + fileName.substring(0, index));
                  } else if (fileName.endsWith(".dex")) {
                     try {
                        Iterator var12 = DexFileProvider.v().getDexFromSource(element).iterator();

                        while(var12.hasNext()) {
                           DexFileProvider.DexContainer container = (DexFileProvider.DexContainer)var12.next();
                           classes.addAll(DexClassProvider.classesOfDex(container.getBase()));
                        }
                     } catch (IOException var23) {
                        logger.debug("" + var23.getMessage());
                     }
                  }
               }
            }
         } else {
            ZipFile archive = null;

            try {
               archive = new ZipFile(aPath);
               Enumeration entries = archive.entries();

               label240:
               while(true) {
                  String entryName;
                  do {
                     if (!entries.hasMoreElements()) {
                        break label240;
                     }

                     ZipEntry entry = (ZipEntry)entries.nextElement();
                     entryName = entry.getName();
                  } while(!entryName.endsWith(".class") && !entryName.endsWith(".jimple"));

                  extensionIndex = entryName.lastIndexOf(46);
                  entryName = entryName.substring(0, extensionIndex);
                  entryName = entryName.replace('/', '.');
                  classes.add(prefix + entryName);
               }
            } catch (Throwable var26) {
               throw new CompilationDeathException("Error reading archive '" + aPath + "'", var26);
            } finally {
               try {
                  if (archive != null) {
                     archive.close();
                  }
               } catch (Throwable var22) {
                  logger.debug("" + var22.getMessage());
               }

            }

            try {
               Iterator var32 = DexFileProvider.v().getDexFromSource(new File(aPath)).iterator();

               while(var32.hasNext()) {
                  DexFileProvider.DexContainer container = (DexFileProvider.DexContainer)var32.next();
                  classes.addAll(DexClassProvider.classesOfDex(container.getBase()));
               }
            } catch (CompilationDeathException var24) {
            } catch (IOException var25) {
               logger.debug("" + var25.getMessage());
            }
         }
      } else {
         try {
            Iterator var5 = DexFileProvider.v().getDexFromSource(new File(aPath)).iterator();

            while(var5.hasNext()) {
               DexFileProvider.DexContainer dex = (DexFileProvider.DexContainer)var5.next();
               classes.addAll(DexClassProvider.classesOfDex(dex.getBase()));
            }
         } catch (IOException var28) {
            throw new CompilationDeathException("Error reading dex source", var28);
         }
      }

      return classes;
   }

   public String getFileNameFor(SootClass c, int rep) {
      if (rep == 12) {
         return null;
      } else {
         StringBuffer b = new StringBuffer();
         if (!Options.v().output_jar()) {
            b.append(this.getOutputDir());
         }

         if (b.length() > 0 && b.charAt(b.length() - 1) != File.separatorChar) {
            b.append(File.separatorChar);
         }

         if (rep != 15) {
            if (rep == 14) {
               b.append(c.getName().replace('.', File.separatorChar));
            } else if (rep == 16) {
               b.append(c.getName().replace('.', '_'));
               b.append("_Maker");
            } else {
               b.append(c.getName());
            }

            b.append(this.getExtensionFor(rep));
            return b.toString();
         } else {
            return this.getDavaFilenameFor(c, b);
         }
      }
   }

   private String getDavaFilenameFor(SootClass c, StringBuffer b) {
      b.append("dava");
      b.append(File.separatorChar);
      ensureDirectoryExists(new File(b.toString() + "classes"));
      b.append("src");
      b.append(File.separatorChar);
      String fixedPackageName = c.getJavaPackageName();
      if (!fixedPackageName.equals("")) {
         b.append(fixedPackageName.replace('.', File.separatorChar));
         b.append(File.separatorChar);
      }

      ensureDirectoryExists(new File(b.toString()));
      b.append(c.getShortJavaStyleName());
      b.append(".java");
      return b.toString();
   }

   public Set<String> classesInDynamicPackage(String str) {
      HashSet<String> set = new HashSet(0);
      StringTokenizer strtok = new StringTokenizer(Scene.v().getSootClassPath(), String.valueOf(File.pathSeparatorChar));

      while(true) {
         String path;
         do {
            if (!strtok.hasMoreTokens()) {
               return set;
            }

            path = strtok.nextToken();
         } while(this.getClassSourceType(path) != SourceLocator.ClassSourceType.directory);

         List<String> l = this.getClassesUnder(path);
         Iterator var6 = l.iterator();

         while(var6.hasNext()) {
            String filename = (String)var6.next();
            if (filename.startsWith(str)) {
               set.add(filename);
            }
         }

         path = path + File.separatorChar;
         StringTokenizer tokenizer = new StringTokenizer(str, ".");

         while(tokenizer.hasMoreTokens()) {
            path = path + tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
               path = path + File.separatorChar;
            }
         }

         l = this.getClassesUnder(path);
         Iterator var10 = l.iterator();

         while(var10.hasNext()) {
            String string = (String)var10.next();
            set.add(str + "." + string);
         }
      }
   }

   public String getExtensionFor(int rep) {
      switch(rep) {
      case 1:
         return ".jimple";
      case 2:
         return ".jimp";
      case 3:
         return ".shimple";
      case 4:
         return ".shimp";
      case 5:
         return ".baf";
      case 6:
         return ".b";
      case 7:
         return ".grimple";
      case 8:
         return ".grimp";
      case 9:
         return ".xml";
      case 10:
      case 11:
      case 12:
      default:
         throw new RuntimeException();
      case 13:
         return ".jasmin";
      case 14:
         return ".class";
      case 15:
         return ".java";
      case 16:
         return ".java";
      case 17:
         return ".asm";
      }
   }

   public String getOutputDir() {
      File dir;
      if (Options.v().output_dir().length() == 0) {
         dir = new File("sootOutput");
      } else {
         dir = new File(Options.v().output_dir());
         if (dir.getPath().endsWith(".jar")) {
            dir = dir.getParentFile();
            if (dir == null) {
               dir = new File("");
            }
         }
      }

      ensureDirectoryExists(dir);
      return dir.getPath();
   }

   public String getOutputJarName() {
      if (!Options.v().output_jar()) {
         return "";
      } else {
         File dir;
         if (Options.v().output_dir().length() == 0) {
            dir = new File("sootOutput/out.jar");
         } else {
            dir = new File(Options.v().output_dir());
            if (!dir.getPath().endsWith(".jar")) {
               dir = new File(dir.getPath(), "out.jar");
            }
         }

         ensureDirectoryExists(dir.getParentFile());
         return dir.getPath();
      }
   }

   public FoundFile lookupInClassPath(String fileName) {
      Iterator var2 = this.classPath.iterator();

      FoundFile ret;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         String dir = (String)var2.next();
         ret = null;
         SourceLocator.ClassSourceType cst = this.getClassSourceType(dir);
         if (cst != SourceLocator.ClassSourceType.zip && cst != SourceLocator.ClassSourceType.jar) {
            if (cst == SourceLocator.ClassSourceType.directory) {
               ret = this.lookupInDir(dir, fileName);
            }
         } else {
            ret = this.lookupInArchive(dir, fileName);
         }
      } while(ret == null);

      return ret;
   }

   private FoundFile lookupInDir(String dir, String fileName) {
      File f = new File(dir, fileName);
      return f.exists() && f.canRead() ? new FoundFile(f) : null;
   }

   protected FoundFile lookupInArchive(String archivePath, String fileName) {
      Set entryNames = null;

      try {
         entryNames = (Set)this.archivePathsToEntriesCache.get(archivePath);
      } catch (Exception var5) {
         throw new RuntimeException("Error: Failed to retrieve the archive entries list for the archive at path '" + archivePath + "'.", var5);
      }

      return entryNames.contains(fileName) ? new FoundFile(archivePath, fileName) : null;
   }

   public String getSourceForClass(String className) {
      String javaClassName = className;
      int i = className.indexOf("$");
      if (i > -1) {
         javaClassName = className.substring(0, i);
      }

      return javaClassName;
   }

   public Map<String, File> dexClassIndex() {
      return this.dexClassIndex;
   }

   public void setDexClassIndex(Map<String, File> index) {
      this.dexClassIndex = index;
   }

   public void extendClassPath(String newPathElement) {
      this.classPath = null;
      if (newPathElement.endsWith(".dex") || newPathElement.endsWith(".apk")) {
         if (this.dexClassPathExtensions == null) {
            this.dexClassPathExtensions = new HashSet();
         }

         this.dexClassPathExtensions.add(newPathElement);
      }

   }

   public Set<String> getDexClassPathExtensions() {
      return this.dexClassPathExtensions;
   }

   public void clearDexClassPathExtensions() {
      this.dexClassPathExtensions = null;
   }

   private static enum ClassSourceType {
      jar,
      zip,
      apk,
      dex,
      directory,
      unknown;
   }
}
