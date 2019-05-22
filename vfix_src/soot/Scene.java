package soot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;
import pxb.android.axml.NodeVisitor;
import soot.dexpler.DalvikThrowAnalysis;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.PublicAndProtectedAccessibility;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ContextSensitiveCallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.options.CGOptions;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.util.ArrayNumberer;
import soot.util.Chain;
import soot.util.HashChain;
import soot.util.MapNumberer;
import soot.util.Numberer;
import soot.util.StringNumberer;

public class Scene {
   private static final Logger logger = LoggerFactory.getLogger(Scene.class);
   private final int defaultSdkVersion = 15;
   private final Map<String, Integer> maxAPIs = new HashMap();
   Chain<SootClass> classes = new HashChain();
   Chain<SootClass> applicationClasses = new HashChain();
   Chain<SootClass> libraryClasses = new HashChain();
   Chain<SootClass> phantomClasses = new HashChain();
   private final Map<String, RefType> nameToClass = new HashMap();
   protected final ArrayNumberer<Kind> kindNumberer;
   protected ArrayNumberer<Type> typeNumberer = new ArrayNumberer();
   protected ArrayNumberer<SootMethod> methodNumberer = new ArrayNumberer();
   protected Numberer<Unit> unitNumberer = new MapNumberer();
   protected Numberer<Context> contextNumberer = null;
   protected Numberer<SparkField> fieldNumberer = new ArrayNumberer();
   protected ArrayNumberer<SootClass> classNumberer = new ArrayNumberer();
   protected StringNumberer subSigNumberer = new StringNumberer();
   protected ArrayNumberer<Local> localNumberer = new ArrayNumberer();
   protected Hierarchy activeHierarchy;
   protected FastHierarchy activeFastHierarchy;
   protected CallGraph activeCallGraph;
   protected ReachableMethods reachableMethods;
   protected PointsToAnalysis activePointsToAnalysis;
   protected SideEffectAnalysis activeSideEffectAnalysis;
   protected List<SootMethod> entryPoints;
   protected ClientAccessibilityOracle accessibilityOracle;
   boolean allowsPhantomRefs = false;
   SootClass mainClass;
   String sootClassPath = null;
   private ThrowAnalysis defaultThrowAnalysis = null;
   private int androidAPIVersion = -1;
   Set<String> reservedNames = new HashSet();
   private int stateCount;
   private ContextSensitiveCallGraph cscg = null;
   private final Set<String>[] basicclasses = new Set[4];
   private List<SootClass> dynamicClasses = null;
   List<String> pkgList;
   private boolean doneResolving = false;
   private boolean incrementalBuild;
   protected LinkedList<String> excludedPackages;

   public Scene(Singletons.Global g) {
      this.setReservedNames();
      String scp = System.getProperty("soot.class.path");
      if (scp != null) {
         this.setSootClassPath(scp);
      }

      this.kindNumberer = new ArrayNumberer(new Kind[]{Kind.INVALID, Kind.STATIC, Kind.VIRTUAL, Kind.INTERFACE, Kind.SPECIAL, Kind.CLINIT, Kind.THREAD, Kind.EXECUTOR, Kind.ASYNCTASK, Kind.FINALIZE, Kind.INVOKE_FINALIZE, Kind.PRIVILEGED, Kind.NEWINSTANCE});
      this.addSootBasicClasses();
      this.determineExcludedPackages();
   }

   private void determineExcludedPackages() {
      this.excludedPackages = new LinkedList();
      if (Options.v().exclude() != null) {
         this.excludedPackages.addAll(Options.v().exclude());
      }

      if (!Options.v().include_all() && Options.v().output_format() != 10 && Options.v().output_format() != 11) {
         this.excludedPackages.add("java.*");
         this.excludedPackages.add("sun.*");
         this.excludedPackages.add("javax.*");
         this.excludedPackages.add("com.sun.*");
         this.excludedPackages.add("com.ibm.*");
         this.excludedPackages.add("org.xml.*");
         this.excludedPackages.add("org.w3c.*");
         this.excludedPackages.add("apple.awt.*");
         this.excludedPackages.add("com.apple.*");
      }

   }

   public static Scene v() {
      return G.v().soot_Scene();
   }

   public void setMainClass(SootClass m) {
      this.mainClass = m;
      if (!m.declaresMethod(this.getSubSigNumberer().findOrAdd("void main(java.lang.String[])"))) {
         throw new RuntimeException("Main-class has no main method!");
      }
   }

   public Set<String> getReservedNames() {
      return this.reservedNames;
   }

   public String quotedNameOf(String s) {
      boolean found = s.contains("-");
      Iterator var3 = this.reservedNames.iterator();

      while(var3.hasNext()) {
         String token = (String)var3.next();
         if (s.contains(token)) {
            found = true;
            break;
         }
      }

      if (!found) {
         return s;
      } else {
         StringBuilder res = new StringBuilder(s.length());
         String[] var9 = s.split("\\.");
         int var5 = var9.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String part = var9[var6];
            if (res.length() > 0) {
               res.append('.');
            }

            if (!part.startsWith("-") && !this.reservedNames.contains(part)) {
               res.append(part);
            } else {
               res.append('\'');
               res.append(part);
               res.append('\'');
            }
         }

         return res.toString();
      }
   }

   public String unescapeName(String s) {
      if (!s.contains("'")) {
         return s;
      } else {
         StringBuilder res = new StringBuilder(s.length());
         String[] var3 = s.split("\\.");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String part = var3[var5];
            if (res.length() > 0) {
               res.append('.');
            }

            if (part.startsWith("'") && part.endsWith("'")) {
               res.append(part.substring(1, part.length() - 1));
            } else {
               res.append(part);
            }
         }

         return res.toString();
      }
   }

   public boolean hasMainClass() {
      if (this.mainClass == null) {
         this.setMainClassFromOptions();
      }

      return this.mainClass != null;
   }

   public SootClass getMainClass() {
      if (!this.hasMainClass()) {
         throw new RuntimeException("There is no main class set!");
      } else {
         return this.mainClass;
      }
   }

   public SootMethod getMainMethod() {
      if (!this.hasMainClass()) {
         throw new RuntimeException("There is no main class set!");
      } else {
         SootMethod mainMethod = this.mainClass.getMethodUnsafe("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
         if (mainMethod == null) {
            throw new RuntimeException("Main class declares no main method!");
         } else {
            return mainMethod;
         }
      }
   }

   public void setSootClassPath(String p) {
      this.sootClassPath = p;
      SourceLocator.v().invalidateClassPath();
   }

   public void extendSootClassPath(String newPathElement) {
      this.sootClassPath = this.sootClassPath + File.pathSeparator + newPathElement;
      SourceLocator.v().extendClassPath(newPathElement);
   }

   public String getSootClassPath() {
      if (this.sootClassPath == null) {
         String optionscp = Options.v().soot_classpath();
         if (optionscp != null && optionscp.length() > 0) {
            this.sootClassPath = optionscp;
         }

         if (this.sootClassPath != null && !this.sootClassPath.isEmpty()) {
            if (Options.v().prepend_classpath()) {
               this.sootClassPath = this.sootClassPath + File.pathSeparator + this.defaultClassPath();
            }
         } else {
            this.sootClassPath = this.defaultClassPath();
         }

         List<String> process_dir = Options.v().process_dir();
         StringBuffer pds = new StringBuffer();
         Iterator var4 = process_dir.iterator();

         while(var4.hasNext()) {
            String path = (String)var4.next();
            if (!this.sootClassPath.contains(path)) {
               pds.append(path);
               pds.append(File.pathSeparator);
            }
         }

         this.sootClassPath = pds + this.sootClassPath;
      }

      return this.sootClassPath;
   }

   private int getMaxAPIAvailable(String dir) {
      Integer mapi = (Integer)this.maxAPIs.get(dir);
      if (mapi != null) {
         return mapi;
      } else {
         File d = new File(dir);
         if (!d.exists()) {
            throw new AndroidPlatformException(String.format("The Android platform directory you have specified (%s) does not exist. Please check.", dir));
         } else {
            File[] files = d.listFiles();
            if (files == null) {
               return -1;
            } else {
               int maxApi = -1;
               File[] var6 = files;
               int var7 = files.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  File f = var6[var8];
                  String name = f.getName();
                  if (f.isDirectory() && name.startsWith("android-")) {
                     try {
                        int v = Integer.decode(name.split("android-")[1]);
                        if (v > maxApi) {
                           maxApi = v;
                        }
                     } catch (NumberFormatException var12) {
                     }
                  }
               }

               this.maxAPIs.put(dir, maxApi);
               return maxApi;
            }
         }
      }
   }

   public String getAndroidJarPath(String jars, String apk) {
      int APIVersion = this.getAndroidAPIVersion(jars, apk);
      String jarPath = jars + File.separator + "android-" + APIVersion + File.separator + "android.jar";
      File f = new File(jarPath);
      if (!f.isFile()) {
         throw new AndroidPlatformException(String.format("error: target android.jar %s does not exist.", jarPath));
      } else {
         return jarPath;
      }
   }

   public int getAndroidAPIVersion() {
      return this.androidAPIVersion > 0 ? this.androidAPIVersion : (Options.v().android_api_version() > 0 ? Options.v().android_api_version() : 15);
   }

   private int getAndroidAPIVersion(String jars, String apk) {
      if (this.androidAPIVersion > 0) {
         return this.androidAPIVersion;
      } else {
         File jarsF = new File(jars);
         File apkF = apk == null ? null : new File(apk);
         if (!jarsF.exists()) {
            throw new AndroidPlatformException(String.format("Android platform directory '%s' does not exist!", jarsF.getAbsolutePath()));
         } else if (apkF != null && !apkF.exists()) {
            throw new RuntimeException("file '" + apk + "' does not exist!");
         } else {
            this.androidAPIVersion = 15;
            if (Options.v().android_api_version() > 0) {
               this.androidAPIVersion = Options.v().android_api_version();
            } else if (apk != null && apk.toLowerCase().endsWith(".apk")) {
               this.androidAPIVersion = this.getTargetSDKVersion(apk, jars);
            }

            int maxAPI = this.getMaxAPIAvailable(jars);
            if (maxAPI > 0 && this.androidAPIVersion > maxAPI) {
               this.androidAPIVersion = maxAPI;
            }

            while(this.androidAPIVersion < maxAPI) {
               String jarPath = jars + File.separator + "android-" + this.androidAPIVersion + File.separator + "android.jar";
               if ((new File(jarPath)).exists()) {
                  break;
               }

               ++this.androidAPIVersion;
            }

            return this.androidAPIVersion;
         }
      }
   }

   private int getTargetSDKVersion(String apkFile, String platformJARs) {
      InputStream manifestIS = null;
      ZipFile archive = null;

      byte var22;
      try {
         try {
            archive = new ZipFile(apkFile);
            Enumeration entries = archive.entries();

            while(entries.hasMoreElements()) {
               ZipEntry entry = (ZipEntry)entries.nextElement();
               String entryName = entry.getName();
               if (entryName.equals("AndroidManifest.xml")) {
                  manifestIS = archive.getInputStream(entry);
                  break;
               }
            }
         } catch (Exception var20) {
            throw new RuntimeException("Error when looking for manifest in apk: " + var20);
         }

         if (manifestIS != null) {
            int maxAPI = this.getMaxAPIAvailable(platformJARs);
            final Scene.AndroidVersionInfo versionInfo = new Scene.AndroidVersionInfo();

            try {
               AxmlReader xmlReader = new AxmlReader(IOUtils.toByteArray(manifestIS));
               xmlReader.accept(new AxmlVisitor() {
                  private String nodeName = null;

                  public void attr(String ns, String name, int resourceId, int type, Object obj) {
                     super.attr(ns, name, resourceId, type, obj);
                     if (this.nodeName != null && name != null) {
                        if (this.nodeName.equals("manifest")) {
                           if (name.equals("platformBuildVersionCode")) {
                              versionInfo.platformBuildVersionCode = Integer.valueOf("" + obj);
                           }
                        } else if (this.nodeName.equals("uses-sdk")) {
                           if (name.equals("targetSdkVersion")) {
                              versionInfo.sdkTargetVersion = Integer.valueOf("" + obj);
                           } else if (name.equals("minSdkVersion")) {
                              versionInfo.minSdkVersion = Integer.valueOf("" + obj);
                           }
                        }
                     }

                  }

                  public NodeVisitor child(String ns, String name) {
                     this.nodeName = name;
                     return this;
                  }
               });
            } catch (Exception var19) {
               logger.error((String)var19.getMessage(), (Throwable)var19);
            }

            int APIVersion = true;
            int APIVersion;
            if (versionInfo.sdkTargetVersion != -1) {
               if (versionInfo.sdkTargetVersion > maxAPI && versionInfo.minSdkVersion != -1 && versionInfo.minSdkVersion <= maxAPI) {
                  logger.warn("Android API version '" + versionInfo.sdkTargetVersion + "' not available, using minApkVersion '" + versionInfo.minSdkVersion + "' instead");
                  APIVersion = versionInfo.minSdkVersion;
               } else {
                  APIVersion = versionInfo.sdkTargetVersion;
               }
            } else if (versionInfo.platformBuildVersionCode != -1) {
               if (versionInfo.platformBuildVersionCode > maxAPI && versionInfo.minSdkVersion != -1 && versionInfo.minSdkVersion <= maxAPI) {
                  logger.warn("Android API version '" + versionInfo.platformBuildVersionCode + "' not available, using minApkVersion '" + versionInfo.minSdkVersion + "' instead");
                  APIVersion = versionInfo.minSdkVersion;
               } else {
                  APIVersion = versionInfo.platformBuildVersionCode;
               }
            } else if (versionInfo.minSdkVersion != -1) {
               APIVersion = versionInfo.minSdkVersion;
            } else {
               logger.debug("Could not find sdk version in Android manifest! Using default: 15");
               APIVersion = 15;
            }

            if (APIVersion <= 2) {
               APIVersion = 3;
            }

            int var8 = APIVersion;
            return var8;
         }

         logger.debug("Could not find sdk version in Android manifest! Using default: 15");
         var22 = 15;
      } finally {
         if (archive != null) {
            try {
               archive.close();
            } catch (IOException var18) {
               throw new RuntimeException("Error when looking for manifest in apk: " + var18);
            }
         }

      }

      return var22;
   }

   public String defaultClassPath() {
      if (Options.v().src_prec() != 5) {
         Iterator var1 = Options.v().process_dir().iterator();

         while(var1.hasNext()) {
            String entry = (String)var1.next();
            if (entry.toLowerCase().endsWith(".apk")) {
               System.err.println("APK file on process dir, but chosen src-prec does not support loading APKs");
               break;
            }
         }
      }

      return Options.v().src_prec() == 5 ? this.defaultAndroidClassPath() : this.defaultJavaClassPath();
   }

   private String defaultAndroidClassPath() {
      String androidJars = Options.v().android_jars();
      String forceAndroidJar = Options.v().force_android_jar();
      if (androidJars != null && !androidJars.equals("") || forceAndroidJar != null && !forceAndroidJar.equals("")) {
         String jarPath = "";
         if (forceAndroidJar != null && !forceAndroidJar.isEmpty()) {
            jarPath = forceAndroidJar;
            if (Options.v().android_api_version() > 0) {
               this.androidAPIVersion = Options.v().android_api_version();
            } else if (forceAndroidJar.contains("android-")) {
               Pattern pt = Pattern.compile("\\" + File.separatorChar + "android-(\\d+)\\" + File.separatorChar);
               Matcher m = pt.matcher(forceAndroidJar);
               if (m.find()) {
                  this.androidAPIVersion = Integer.valueOf(m.group(1));
               }
            } else {
               this.androidAPIVersion = 15;
            }
         } else if (androidJars != null && !androidJars.isEmpty()) {
            List<String> classPathEntries = new LinkedList(Arrays.asList(Options.v().soot_classpath().split(File.pathSeparator)));
            classPathEntries.addAll(Options.v().process_dir());
            String targetApk = "";
            Set<String> targetDexs = new HashSet();
            Iterator var7 = classPathEntries.iterator();

            while(var7.hasNext()) {
               String entry = (String)var7.next();
               if (entry.toLowerCase().endsWith(".apk")) {
                  if (targetApk != null && !targetApk.isEmpty()) {
                     throw new RuntimeException("only one Android application can be analyzed when using option -android-jars.");
                  }

                  targetApk = entry;
               }

               if (entry.toLowerCase().endsWith(".dex")) {
                  targetDexs.add(entry);
               }
            }

            if (targetApk != null && !targetApk.isEmpty()) {
               jarPath = this.getAndroidJarPath(androidJars, targetApk);
            } else {
               if (targetDexs.isEmpty()) {
                  throw new RuntimeException("no apk file given");
               }

               jarPath = this.getAndroidJarPath(androidJars, (String)null);
            }
         }

         if (jarPath.equals("")) {
            throw new RuntimeException("android.jar not found.");
         } else {
            File f = new File(jarPath);
            if (!f.exists()) {
               throw new RuntimeException("file '" + jarPath + "' does not exist!");
            } else {
               logger.debug("Using '" + jarPath + "' as android.jar");
               return jarPath;
            }
         }
      } else {
         throw new RuntimeException("You are analyzing an Android application but did not define android.jar. Options -android-jars or -force-android-jar should be used.");
      }
   }

   private String defaultJavaClassPath() {
      StringBuilder sb = new StringBuilder();
      if (System.getProperty("os.name").equals("Mac OS X")) {
         String prefix = System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator;
         File classesJar = new File(prefix + "classes.jar");
         if (classesJar.exists()) {
            sb.append(classesJar.getAbsolutePath() + File.pathSeparator);
         }

         File uiJar = new File(prefix + "ui.jar");
         if (uiJar.exists()) {
            sb.append(uiJar.getAbsolutePath() + File.pathSeparator);
         }
      }

      File rtJar = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar");
      if (rtJar.exists() && rtJar.isFile()) {
         sb.append(rtJar.getAbsolutePath());
      } else {
         rtJar = new File(System.getProperty("java.home") + File.separator + "jre" + File.separator + "lib" + File.separator + "rt.jar");
         if (!rtJar.exists() || !rtJar.isFile()) {
            throw new RuntimeException("Error: cannot find rt.jar.");
         }

         sb.append(rtJar.getAbsolutePath());
      }

      if (Options.v().whole_program() || Options.v().output_format() == 15) {
         sb.append(File.pathSeparator + System.getProperty("java.home") + File.separator + "lib" + File.separator + "jce.jar");
      }

      return sb.toString();
   }

   public int getState() {
      return this.stateCount;
   }

   protected void modifyHierarchy() {
      ++this.stateCount;
      this.activeHierarchy = null;
      this.activeFastHierarchy = null;
      this.activeSideEffectAnalysis = null;
      this.activePointsToAnalysis = null;
   }

   public void addClass(SootClass c) {
      this.addClassSilent(c);
      c.setLibraryClass();
      this.modifyHierarchy();
   }

   protected void addClassSilent(SootClass c) {
      if (c.isInScene()) {
         throw new RuntimeException("already managed: " + c.getName());
      } else if (this.containsClass(c.getName())) {
         throw new RuntimeException("duplicate class: " + c.getName());
      } else {
         this.classes.add(c);
         this.nameToClass.put(c.getName(), c.getType());
         c.getType().setSootClass(c);
         c.setInScene(true);
         if (!c.isPhantom) {
            this.modifyHierarchy();
         }

      }
   }

   public void removeClass(SootClass c) {
      if (!c.isInScene()) {
         throw new RuntimeException();
      } else {
         this.classes.remove(c);
         if (c.isLibraryClass()) {
            this.libraryClasses.remove(c);
         } else if (c.isPhantomClass()) {
            this.phantomClasses.remove(c);
         } else if (c.isApplicationClass()) {
            this.applicationClasses.remove(c);
         }

         c.getType().setSootClass((SootClass)null);
         c.setInScene(false);
         this.modifyHierarchy();
      }
   }

   public boolean containsClass(String className) {
      RefType type = (RefType)this.nameToClass.get(className);
      if (type == null) {
         return false;
      } else if (!type.hasSootClass()) {
         return false;
      } else {
         SootClass c = type.getSootClass();
         return c.isInScene();
      }
   }

   public boolean containsType(String className) {
      return this.nameToClass.containsKey(className);
   }

   public String signatureToClass(String sig) {
      if (sig.charAt(0) != '<') {
         throw new RuntimeException("oops " + sig);
      } else if (sig.charAt(sig.length() - 1) != '>') {
         throw new RuntimeException("oops " + sig);
      } else {
         int index = sig.indexOf(":");
         if (index < 0) {
            throw new RuntimeException("oops " + sig);
         } else {
            return sig.substring(1, index);
         }
      }
   }

   public String signatureToSubsignature(String sig) {
      if (sig.charAt(0) != '<') {
         throw new RuntimeException("oops " + sig);
      } else if (sig.charAt(sig.length() - 1) != '>') {
         throw new RuntimeException("oops " + sig);
      } else {
         int index = sig.indexOf(":");
         if (index < 0) {
            throw new RuntimeException("oops " + sig);
         } else {
            return sig.substring(index + 2, sig.length() - 1);
         }
      }
   }

   public SootField grabField(String fieldSignature) {
      String cname = this.signatureToClass(fieldSignature);
      String fname = this.signatureToSubsignature(fieldSignature);
      if (!this.containsClass(cname)) {
         return null;
      } else {
         SootClass c = this.getSootClass(cname);
         return c.getFieldUnsafe(fname);
      }
   }

   public boolean containsField(String fieldSignature) {
      return this.grabField(fieldSignature) != null;
   }

   public SootMethod grabMethod(String methodSignature) {
      String cname = this.signatureToClass(methodSignature);
      String mname = this.signatureToSubsignature(methodSignature);
      if (!this.containsClass(cname)) {
         return null;
      } else {
         SootClass c = this.getSootClass(cname);
         return c.getMethodUnsafe(mname);
      }
   }

   public boolean containsMethod(String methodSignature) {
      return this.grabMethod(methodSignature) != null;
   }

   public SootField getField(String fieldSignature) {
      SootField f = this.grabField(fieldSignature);
      if (f != null) {
         return f;
      } else {
         throw new RuntimeException("tried to get nonexistent field " + fieldSignature);
      }
   }

   public SootMethod getMethod(String methodSignature) {
      SootMethod m = this.grabMethod(methodSignature);
      if (m != null) {
         return m;
      } else {
         throw new RuntimeException("tried to get nonexistent method " + methodSignature);
      }
   }

   public SootClass tryLoadClass(String className, int desiredLevel) {
      this.setPhantomRefs(true);
      ClassSource source = SourceLocator.v().getClassSource(className);

      SootResolver resolver;
      label63: {
         try {
            if (this.getPhantomRefs() || source != null) {
               break label63;
            }

            this.setPhantomRefs(false);
            resolver = null;
         } finally {
            if (source != null) {
               source.close();
            }

         }

         return resolver;
      }

      resolver = SootResolver.v();
      SootClass toReturn = resolver.resolveClass(className, desiredLevel);
      this.setPhantomRefs(false);
      return toReturn;
   }

   public SootClass loadClassAndSupport(String className) {
      SootClass ret = this.loadClass(className, 2);
      if (!ret.isPhantom()) {
         ret = this.loadClass(className, 3);
      }

      return ret;
   }

   public SootClass loadClass(String className, int desiredLevel) {
      this.setPhantomRefs(true);
      SootResolver resolver = SootResolver.v();
      SootClass toReturn = resolver.resolveClass(className, desiredLevel);
      this.setPhantomRefs(false);
      return toReturn;
   }

   public Type getType(String arg) {
      String type = arg.replaceAll("([^\\[\\]]*)(.*)", "$1");
      int arrayCount = arg.contains("[") ? arg.replaceAll("([^\\[\\]]*)(.*)", "$2").length() / 2 : 0;
      Type result = this.getRefTypeUnsafe(type);
      if (result == null) {
         if (type.equals("long")) {
            result = LongType.v();
         } else if (type.equals("short")) {
            result = ShortType.v();
         } else if (type.equals("double")) {
            result = DoubleType.v();
         } else if (type.equals("int")) {
            result = IntType.v();
         } else if (type.equals("float")) {
            result = FloatType.v();
         } else if (type.equals("byte")) {
            result = ByteType.v();
         } else if (type.equals("char")) {
            result = CharType.v();
         } else if (type.equals("void")) {
            result = VoidType.v();
         } else {
            if (!type.equals("boolean")) {
               throw new RuntimeException("unknown type: '" + type + "'");
            }

            result = BooleanType.v();
         }
      }

      if (arrayCount != 0) {
         result = ArrayType.v((Type)result, arrayCount);
      }

      return (Type)result;
   }

   public RefType getRefType(String className) {
      RefType refType = this.getRefTypeUnsafe(className);
      if (refType == null) {
         throw new IllegalStateException("RefType " + className + " not loaded. If you tried to get the RefType of a library class, did you call loadNecessaryClasses()? Otherwise please check Soot's classpath.");
      } else {
         return refType;
      }
   }

   public RefType getRefTypeUnsafe(String className) {
      RefType refType = (RefType)this.nameToClass.get(className);
      return refType;
   }

   public RefType getObjectType() {
      return this.getRefType("java.lang.Object");
   }

   public void addRefType(RefType type) {
      this.nameToClass.put(type.getClassName(), type);
   }

   public SootClass getSootClassUnsafe(String className) {
      RefType type = (RefType)this.nameToClass.get(className);
      SootClass c;
      if (type != null) {
         c = type.getSootClass();
         if (c != null) {
            return c;
         }
      }

      if (!this.allowsPhantomRefs() && !className.equals("soot.dummy.InvokeDynamic")) {
         return null;
      } else {
         c = new SootClass(className);
         c.isPhantom = true;
         this.addClassSilent(c);
         c.setPhantomClass();
         return c;
      }
   }

   public SootClass getSootClass(String className) {
      SootClass sc = this.getSootClassUnsafe(className);
      if (sc != null) {
         return sc;
      } else {
         throw new RuntimeException(System.getProperty("line.separator") + "Aborting: can't find classfile " + className);
      }
   }

   public Chain<SootClass> getClasses() {
      return this.classes;
   }

   public Chain<SootClass> getApplicationClasses() {
      return this.applicationClasses;
   }

   public Chain<SootClass> getLibraryClasses() {
      return this.libraryClasses;
   }

   public Chain<SootClass> getPhantomClasses() {
      return this.phantomClasses;
   }

   Chain<SootClass> getContainingChain(SootClass c) {
      if (c.isApplicationClass()) {
         return this.getApplicationClasses();
      } else if (c.isLibraryClass()) {
         return this.getLibraryClasses();
      } else {
         return c.isPhantomClass() ? this.getPhantomClasses() : null;
      }
   }

   public SideEffectAnalysis getSideEffectAnalysis() {
      if (!this.hasSideEffectAnalysis()) {
         this.setSideEffectAnalysis(new SideEffectAnalysis(this.getPointsToAnalysis(), this.getCallGraph()));
      }

      return this.activeSideEffectAnalysis;
   }

   public void setSideEffectAnalysis(SideEffectAnalysis sea) {
      this.activeSideEffectAnalysis = sea;
   }

   public boolean hasSideEffectAnalysis() {
      return this.activeSideEffectAnalysis != null;
   }

   public void releaseSideEffectAnalysis() {
      this.activeSideEffectAnalysis = null;
   }

   public PointsToAnalysis getPointsToAnalysis() {
      return (PointsToAnalysis)(!this.hasPointsToAnalysis() ? DumbPointerAnalysis.v() : this.activePointsToAnalysis);
   }

   public void setPointsToAnalysis(PointsToAnalysis pa) {
      this.activePointsToAnalysis = pa;
   }

   public boolean hasPointsToAnalysis() {
      return this.activePointsToAnalysis != null;
   }

   public void releasePointsToAnalysis() {
      this.activePointsToAnalysis = null;
   }

   public ClientAccessibilityOracle getClientAccessibilityOracle() {
      return (ClientAccessibilityOracle)(!this.hasClientAccessibilityOracle() ? PublicAndProtectedAccessibility.v() : this.accessibilityOracle);
   }

   public boolean hasClientAccessibilityOracle() {
      return this.accessibilityOracle != null;
   }

   public void setClientAccessibilityOracle(ClientAccessibilityOracle oracle) {
      this.accessibilityOracle = oracle;
   }

   public void releaseClientAccessibilityOracle() {
      this.accessibilityOracle = null;
   }

   public FastHierarchy getOrMakeFastHierarchy() {
      if (!this.hasFastHierarchy()) {
         this.setFastHierarchy(new FastHierarchy());
      }

      return this.getFastHierarchy();
   }

   public FastHierarchy getFastHierarchy() {
      if (!this.hasFastHierarchy()) {
         throw new RuntimeException("no active FastHierarchy present for scene");
      } else {
         return this.activeFastHierarchy;
      }
   }

   public void setFastHierarchy(FastHierarchy hierarchy) {
      this.activeFastHierarchy = hierarchy;
   }

   public boolean hasFastHierarchy() {
      return this.activeFastHierarchy != null;
   }

   public void releaseFastHierarchy() {
      this.activeFastHierarchy = null;
   }

   public Hierarchy getActiveHierarchy() {
      if (!this.hasActiveHierarchy()) {
         this.setActiveHierarchy(new Hierarchy());
      }

      return this.activeHierarchy;
   }

   public void setActiveHierarchy(Hierarchy hierarchy) {
      this.activeHierarchy = hierarchy;
   }

   public boolean hasActiveHierarchy() {
      return this.activeHierarchy != null;
   }

   public void releaseActiveHierarchy() {
      this.activeHierarchy = null;
   }

   public boolean hasCustomEntryPoints() {
      return this.entryPoints != null;
   }

   public List<SootMethod> getEntryPoints() {
      if (this.entryPoints == null) {
         this.entryPoints = EntryPoints.v().all();
      }

      return this.entryPoints;
   }

   public void setEntryPoints(List<SootMethod> entryPoints) {
      this.entryPoints = entryPoints;
   }

   public ContextSensitiveCallGraph getContextSensitiveCallGraph() {
      if (this.cscg == null) {
         throw new RuntimeException("No context-sensitive call graph present in Scene. You can bulid one with Paddle.");
      } else {
         return this.cscg;
      }
   }

   public void setContextSensitiveCallGraph(ContextSensitiveCallGraph cscg) {
      this.cscg = cscg;
   }

   public CallGraph getCallGraph() {
      if (!this.hasCallGraph()) {
         throw new RuntimeException("No call graph present in Scene. Maybe you want Whole Program mode (-w).");
      } else {
         return this.activeCallGraph;
      }
   }

   public void setCallGraph(CallGraph cg) {
      this.reachableMethods = null;
      this.activeCallGraph = cg;
   }

   public boolean hasCallGraph() {
      return this.activeCallGraph != null;
   }

   public void releaseCallGraph() {
      this.activeCallGraph = null;
      this.reachableMethods = null;
   }

   public ReachableMethods getReachableMethods() {
      if (this.reachableMethods == null) {
         this.reachableMethods = new ReachableMethods(this.getCallGraph(), new ArrayList(this.getEntryPoints()));
      }

      this.reachableMethods.update();
      return this.reachableMethods;
   }

   public void setReachableMethods(ReachableMethods rm) {
      this.reachableMethods = rm;
   }

   public boolean hasReachableMethods() {
      return this.reachableMethods != null;
   }

   public void releaseReachableMethods() {
      this.reachableMethods = null;
   }

   public boolean getPhantomRefs() {
      return Options.v().allow_phantom_refs();
   }

   public void setPhantomRefs(boolean value) {
      this.allowsPhantomRefs = value;
   }

   public boolean allowsPhantomRefs() {
      return this.getPhantomRefs();
   }

   public Numberer<Kind> kindNumberer() {
      return this.kindNumberer;
   }

   public ArrayNumberer<Type> getTypeNumberer() {
      return this.typeNumberer;
   }

   public ArrayNumberer<SootMethod> getMethodNumberer() {
      return this.methodNumberer;
   }

   public Numberer<Context> getContextNumberer() {
      return this.contextNumberer;
   }

   public Numberer<Unit> getUnitNumberer() {
      return this.unitNumberer;
   }

   public Numberer<SparkField> getFieldNumberer() {
      return this.fieldNumberer;
   }

   public ArrayNumberer<SootClass> getClassNumberer() {
      return this.classNumberer;
   }

   public StringNumberer getSubSigNumberer() {
      return this.subSigNumberer;
   }

   public ArrayNumberer<Local> getLocalNumberer() {
      return this.localNumberer;
   }

   public void setContextNumberer(Numberer<Context> n) {
      if (this.contextNumberer != null) {
         throw new RuntimeException("Attempt to set context numberer when it is already set.");
      } else {
         this.contextNumberer = n;
      }
   }

   public ThrowAnalysis getDefaultThrowAnalysis() {
      if (this.defaultThrowAnalysis == null) {
         int optionsThrowAnalysis = Options.v().throw_analysis();
         switch(optionsThrowAnalysis) {
         case 1:
            this.defaultThrowAnalysis = PedanticThrowAnalysis.v();
            break;
         case 2:
            this.defaultThrowAnalysis = UnitThrowAnalysis.v();
            break;
         case 3:
            this.defaultThrowAnalysis = DalvikThrowAnalysis.v();
            break;
         default:
            throw new IllegalStateException("Options.v().throw_analysi() == " + Options.v().throw_analysis());
         }
      }

      return this.defaultThrowAnalysis;
   }

   public void setDefaultThrowAnalysis(ThrowAnalysis ta) {
      this.defaultThrowAnalysis = ta;
   }

   private void setReservedNames() {
      Set<String> rn = this.getReservedNames();
      rn.add("newarray");
      rn.add("newmultiarray");
      rn.add("nop");
      rn.add("ret");
      rn.add("specialinvoke");
      rn.add("staticinvoke");
      rn.add("tableswitch");
      rn.add("virtualinvoke");
      rn.add("null_type");
      rn.add("unknown");
      rn.add("cmp");
      rn.add("cmpg");
      rn.add("cmpl");
      rn.add("entermonitor");
      rn.add("exitmonitor");
      rn.add("interfaceinvoke");
      rn.add("lengthof");
      rn.add("lookupswitch");
      rn.add("neg");
      rn.add("if");
      rn.add("abstract");
      rn.add("annotation");
      rn.add("boolean");
      rn.add("break");
      rn.add("byte");
      rn.add("case");
      rn.add("catch");
      rn.add("char");
      rn.add("class");
      rn.add("enum");
      rn.add("final");
      rn.add("native");
      rn.add("public");
      rn.add("protected");
      rn.add("private");
      rn.add("static");
      rn.add("synchronized");
      rn.add("transient");
      rn.add("volatile");
      rn.add("interface");
      rn.add("void");
      rn.add("short");
      rn.add("int");
      rn.add("long");
      rn.add("float");
      rn.add("double");
      rn.add("extends");
      rn.add("implements");
      rn.add("breakpoint");
      rn.add("default");
      rn.add("goto");
      rn.add("instanceof");
      rn.add("new");
      rn.add("return");
      rn.add("throw");
      rn.add("throws");
      rn.add("null");
      rn.add("from");
      rn.add("to");
      rn.add("with");
   }

   private void addSootBasicClasses() {
      this.basicclasses[1] = new HashSet();
      this.basicclasses[2] = new HashSet();
      this.basicclasses[3] = new HashSet();
      this.addBasicClass("java.lang.Object");
      this.addBasicClass("java.lang.Class", 2);
      this.addBasicClass("java.lang.Void", 2);
      this.addBasicClass("java.lang.Boolean", 2);
      this.addBasicClass("java.lang.Byte", 2);
      this.addBasicClass("java.lang.Character", 2);
      this.addBasicClass("java.lang.Short", 2);
      this.addBasicClass("java.lang.Integer", 2);
      this.addBasicClass("java.lang.Long", 2);
      this.addBasicClass("java.lang.Float", 2);
      this.addBasicClass("java.lang.Double", 2);
      this.addBasicClass("java.lang.String");
      this.addBasicClass("java.lang.StringBuffer", 2);
      this.addBasicClass("java.lang.Error");
      this.addBasicClass("java.lang.AssertionError", 2);
      this.addBasicClass("java.lang.Throwable", 2);
      this.addBasicClass("java.lang.Exception", 2);
      this.addBasicClass("java.lang.NoClassDefFoundError", 2);
      this.addBasicClass("java.lang.ExceptionInInitializerError");
      this.addBasicClass("java.lang.RuntimeException");
      this.addBasicClass("java.lang.ClassNotFoundException");
      this.addBasicClass("java.lang.ArithmeticException");
      this.addBasicClass("java.lang.ArrayStoreException");
      this.addBasicClass("java.lang.ClassCastException");
      this.addBasicClass("java.lang.IllegalMonitorStateException");
      this.addBasicClass("java.lang.IndexOutOfBoundsException");
      this.addBasicClass("java.lang.ArrayIndexOutOfBoundsException");
      this.addBasicClass("java.lang.NegativeArraySizeException");
      this.addBasicClass("java.lang.NullPointerException", 2);
      this.addBasicClass("java.lang.InstantiationError");
      this.addBasicClass("java.lang.InternalError");
      this.addBasicClass("java.lang.OutOfMemoryError");
      this.addBasicClass("java.lang.StackOverflowError");
      this.addBasicClass("java.lang.UnknownError");
      this.addBasicClass("java.lang.ThreadDeath");
      this.addBasicClass("java.lang.ClassCircularityError");
      this.addBasicClass("java.lang.ClassFormatError");
      this.addBasicClass("java.lang.IllegalAccessError");
      this.addBasicClass("java.lang.IncompatibleClassChangeError");
      this.addBasicClass("java.lang.LinkageError");
      this.addBasicClass("java.lang.VerifyError");
      this.addBasicClass("java.lang.NoSuchFieldError");
      this.addBasicClass("java.lang.AbstractMethodError");
      this.addBasicClass("java.lang.NoSuchMethodError");
      this.addBasicClass("java.lang.UnsatisfiedLinkError");
      this.addBasicClass("java.lang.Thread");
      this.addBasicClass("java.lang.Runnable");
      this.addBasicClass("java.lang.Cloneable");
      this.addBasicClass("java.io.Serializable");
      this.addBasicClass("java.lang.ref.Finalizer");
      this.addBasicClass("java.lang.invoke.LambdaMetafactory");
   }

   public void addBasicClass(String name) {
      this.addBasicClass(name, 1);
   }

   public void addBasicClass(String name, int level) {
      this.basicclasses[level].add(name);
   }

   public void loadBasicClasses() {
      this.addReflectionTraceClasses();

      for(int i = 3; i >= 1; --i) {
         Iterator var2 = this.basicclasses[i].iterator();

         while(var2.hasNext()) {
            String name = (String)var2.next();
            this.tryLoadClass(name, i);
         }
      }

   }

   public Set<String> getBasicClasses() {
      Set<String> all = new HashSet();

      for(int i = 0; i < this.basicclasses.length; ++i) {
         Set<String> classes = this.basicclasses[i];
         if (classes != null) {
            all.addAll(classes);
         }
      }

      return all;
   }

   private void addReflectionTraceClasses() {
      CGOptions options = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
      String log = options.reflection_log();
      Set<String> classNames = new HashSet();
      String line;
      if (log != null && log.length() > 0) {
         BufferedReader reader = null;
         line = "";

         try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(log)));

            label144:
            while(true) {
               while(true) {
                  do {
                     if ((line = reader.readLine()) == null) {
                        break label144;
                     }
                  } while(line.length() == 0);

                  String[] portions = line.split(";", -1);
                  String kind = portions[0];
                  String target = portions[1];
                  String source = portions[2];
                  String sourceClassName = source.substring(0, source.lastIndexOf("."));
                  classNames.add(sourceClassName);
                  if (kind.equals("Class.forName")) {
                     classNames.add(target);
                  } else if (kind.equals("Class.newInstance")) {
                     classNames.add(target);
                  } else if (!kind.equals("Method.invoke") && !kind.equals("Constructor.newInstance")) {
                     if (!kind.equals("Field.set*") && !kind.equals("Field.get*")) {
                        throw new RuntimeException("Unknown entry kind: " + kind);
                     }

                     classNames.add(this.signatureToClass(target));
                  } else {
                     classNames.add(this.signatureToClass(target));
                  }
               }
            }
         } catch (Exception var18) {
            throw new RuntimeException("Line: '" + line + "'", var18);
         } finally {
            if (reader != null) {
               try {
                  reader.close();
               } catch (IOException var17) {
                  throw new RuntimeException(var17);
               }
            }

         }
      }

      Iterator var20 = classNames.iterator();

      while(var20.hasNext()) {
         line = (String)var20.next();
         this.addBasicClass(line, 3);
      }

   }

   public Collection<SootClass> dynamicClasses() {
      if (this.dynamicClasses == null) {
         throw new IllegalStateException("Have to call loadDynamicClasses() first!");
      } else {
         return this.dynamicClasses;
      }
   }

   private void loadNecessaryClass(String name) {
      SootClass c = this.loadClassAndSupport(name);
      c.setApplicationClass();
   }

   public void loadNecessaryClasses() {
      this.loadBasicClasses();
      Iterator var1 = Options.v().classes().iterator();

      String path;
      while(var1.hasNext()) {
         path = (String)var1.next();
         this.loadNecessaryClass(path);
      }

      this.loadDynamicClasses();
      if (Options.v().oaat()) {
         if (Options.v().process_dir().isEmpty()) {
            throw new IllegalArgumentException("If switch -oaat is used, then also -process-dir must be given.");
         }
      } else {
         var1 = Options.v().process_dir().iterator();

         while(var1.hasNext()) {
            path = (String)var1.next();
            Iterator var3 = SourceLocator.v().getClassesUnder(path).iterator();

            while(var3.hasNext()) {
               String cl = (String)var3.next();
               SootClass theClass = this.loadClassAndSupport(cl);
               if (!theClass.isPhantom) {
                  theClass.setApplicationClass();
               }
            }
         }
      }

      this.prepareClasses();
      this.setDoneResolving();
   }

   public void loadDynamicClasses() {
      this.dynamicClasses = new ArrayList();
      HashSet<String> dynClasses = new HashSet();
      dynClasses.addAll(Options.v().dynamic_class());
      Iterator iterator = Options.v().dynamic_dir().iterator();

      String className;
      while(iterator.hasNext()) {
         className = (String)iterator.next();
         dynClasses.addAll(SourceLocator.v().getClassesUnder(className));
      }

      iterator = Options.v().dynamic_package().iterator();

      while(iterator.hasNext()) {
         className = (String)iterator.next();
         dynClasses.addAll(SourceLocator.v().classesInDynamicPackage(className));
      }

      iterator = dynClasses.iterator();

      while(iterator.hasNext()) {
         className = (String)iterator.next();
         this.dynamicClasses.add(this.loadClassAndSupport(className));
      }

      iterator = this.dynamicClasses.iterator();

      while(iterator.hasNext()) {
         SootClass c = (SootClass)iterator.next();
         if (!c.isConcrete()) {
            if (Options.v().verbose()) {
               logger.warn("dynamic class " + c.getName() + " is abstract or an interface, and it will not be considered.");
            }

            iterator.remove();
         }
      }

   }

   private void prepareClasses() {
      HashChain processedClasses = new HashChain();

      while(true) {
         Chain<SootClass> unprocessedClasses = new HashChain(this.getClasses());
         unprocessedClasses.removeAll(processedClasses);
         if (unprocessedClasses.isEmpty()) {
            return;
         }

         processedClasses.addAll(unprocessedClasses);
         Iterator var3 = unprocessedClasses.iterator();

         while(var3.hasNext()) {
            SootClass s = (SootClass)var3.next();
            if (!s.isPhantom()) {
               if (Options.v().app()) {
                  s.setApplicationClass();
               }

               if (Options.v().classes().contains(s.getName())) {
                  s.setApplicationClass();
               } else {
                  if (s.isApplicationClass() && this.isExcluded(s)) {
                     s.setLibraryClass();
                  }

                  if (this.isIncluded(s)) {
                     s.setApplicationClass();
                  }

                  if (s.isApplicationClass()) {
                     this.loadClassAndSupport(s.getName());
                  }
               }
            }
         }
      }
   }

   public boolean isExcluded(SootClass sc) {
      String name = sc.getName();
      Iterator var3 = this.excludedPackages.iterator();

      String pkg;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         pkg = (String)var3.next();
      } while(!name.equals(pkg) && (!pkg.endsWith(".*") && !pkg.endsWith("$*") || !name.startsWith(pkg.substring(0, pkg.length() - 1))));

      return !this.isIncluded(sc);
   }

   public boolean isIncluded(SootClass sc) {
      String name = sc.getName();
      Iterator var3 = Options.v().include().iterator();

      String inc;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         inc = (String)var3.next();
      } while(!name.equals(inc) && (!inc.endsWith(".*") && !inc.endsWith("$*") || !name.startsWith(inc.substring(0, inc.length() - 1))));

      return true;
   }

   public void setPkgList(List<String> list) {
      this.pkgList = list;
   }

   public List<String> getPkgList() {
      return this.pkgList;
   }

   public SootMethodRef makeMethodRef(SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean isStatic) {
      return new SootMethodRefImpl(declaringClass, name, parameterTypes, returnType, isStatic);
   }

   public SootMethodRef makeConstructorRef(SootClass declaringClass, List<Type> parameterTypes) {
      return this.makeMethodRef(declaringClass, "<init>", parameterTypes, VoidType.v(), false);
   }

   public SootFieldRef makeFieldRef(SootClass declaringClass, String name, Type type, boolean isStatic) {
      return new AbstractSootFieldRef(declaringClass, name, type, isStatic);
   }

   public List<SootClass> getClasses(int desiredLevel) {
      List<SootClass> ret = new ArrayList();
      Iterator clIt = this.getClasses().iterator();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         if (cl.resolvingLevel() >= desiredLevel) {
            ret.add(cl);
         }
      }

      return ret;
   }

   public boolean doneResolving() {
      return this.doneResolving;
   }

   public void setDoneResolving() {
      this.doneResolving = true;
   }

   public void setMainClassFromOptions() {
      if (this.mainClass == null) {
         if (Options.v().main_class() != null && Options.v().main_class().length() > 0) {
            this.setMainClass(this.getSootClass(Options.v().main_class()));
         } else {
            Iterator classIter = Options.v().classes().iterator();

            SootClass c;
            while(classIter.hasNext()) {
               c = this.getSootClass((String)classIter.next());
               if (c.declaresMethod("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v())) {
                  logger.debug("No main class given. Inferred '" + c.getName() + "' as main class.");
                  this.setMainClass(c);
                  return;
               }
            }

            classIter = this.getApplicationClasses().iterator();

            while(classIter.hasNext()) {
               c = (SootClass)classIter.next();
               if (c.declaresMethod("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v())) {
                  logger.debug("No main class given. Inferred '" + c.getName() + "' as main class.");
                  this.setMainClass(c);
                  return;
               }
            }
         }

      }
   }

   public boolean isIncrementalBuild() {
      return this.incrementalBuild;
   }

   public void initiateIncrementalBuild() {
      this.incrementalBuild = true;
   }

   public void incrementalBuildFinished() {
      this.incrementalBuild = false;
   }

   public SootClass forceResolve(String className, int level) {
      boolean tmp = this.doneResolving;
      this.doneResolving = false;

      SootClass c;
      try {
         c = SootResolver.v().resolveClass(className, level);
      } finally {
         this.doneResolving = tmp;
      }

      return c;
   }

   public SootClass makeSootClass(String name) {
      return new SootClass(name);
   }

   public SootClass makeSootClass(String name, int modifiers) {
      return new SootClass(name, modifiers);
   }

   public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType) {
      return new SootMethod(name, parameterTypes, returnType);
   }

   public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers) {
      return new SootMethod(name, parameterTypes, returnType, modifiers);
   }

   public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers, List<SootClass> thrownExceptions) {
      return new SootMethod(name, parameterTypes, returnType, modifiers, thrownExceptions);
   }

   public SootField makeSootField(String name, Type type, int modifiers) {
      return new SootField(name, type, modifiers);
   }

   public SootField makeSootField(String name, Type type) {
      return new SootField(name, type);
   }

   public RefType getOrAddRefType(RefType tp) {
      RefType existing = (RefType)this.nameToClass.get(tp.getClassName());
      if (existing != null) {
         return existing;
      } else {
         this.nameToClass.put(tp.getClassName(), tp);
         return tp;
      }
   }

   public CallGraph internalMakeCallGraph() {
      return new CallGraph();
   }

   private static class AndroidVersionInfo {
      public int sdkTargetVersion;
      public int minSdkVersion;
      public int platformBuildVersionCode;

      private AndroidVersionInfo() {
         this.sdkTargetVersion = -1;
         this.minSdkVersion = -1;
         this.platformBuildVersionCode = -1;
      }

      // $FF: synthetic method
      AndroidVersionInfo(Object x0) {
         this();
      }
   }
}
