package org.testng.internal;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.testng.collections.Lists;

public class PackageUtils {
   private static String[] s_testClassPaths;
   private static final List<ClassLoader> m_classLoaders = new Vector();

   public static void addClassLoader(ClassLoader loader) {
      m_classLoaders.add(loader);
   }

   public static String[] findClassesInPackage(String packageName, List<String> included, List<String> excluded) throws IOException {
      String packageOnly = packageName;
      boolean recursive = false;
      if (packageName.endsWith(".*")) {
         packageOnly = packageName.substring(0, packageName.lastIndexOf(".*"));
         recursive = true;
      }

      List<String> vResult = Lists.newArrayList();
      String packageDirName = packageOnly.replace('.', '/') + (packageOnly.length() > 0 ? "/" : "");
      Vector<URL> dirs = new Vector();
      Vector<ClassLoader> allClassLoaders = new Vector();
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      if (contextClassLoader != null) {
         allClassLoaders.add(contextClassLoader);
      }

      if (m_classLoaders != null) {
         allClassLoaders.addAll(m_classLoaders);
      }

      int count = 0;
      Iterator dirIterator = allClassLoaders.iterator();

      while(true) {
         ClassLoader classLoader;
         do {
            if (!dirIterator.hasNext()) {
               dirIterator = dirs.iterator();

               while(true) {
                  label91:
                  while(true) {
                     URL url;
                     String protocol;
                     do {
                        if (!dirIterator.hasNext()) {
                           String[] result = (String[])vResult.toArray(new String[vResult.size()]);
                           return result;
                        }

                        url = (URL)dirIterator.next();
                        protocol = url.getProtocol();
                     } while(!matchTestClasspath(url, packageDirName, recursive));

                     if ("file".equals(protocol)) {
                        findClassesInDirPackage(packageOnly, included, excluded, URLDecoder.decode(url.getFile(), "UTF-8"), recursive, vResult);
                     } else if (!"jar".equals(protocol)) {
                        if ("bundleresource".equals(protocol)) {
                           try {
                              Class[] params = new Class[0];
                              URLConnection connection = url.openConnection();
                              Method thisMethod = url.openConnection().getClass().getDeclaredMethod("getFileURL", params);
                              Object[] paramsObj = new Object[0];
                              URL fileUrl = (URL)thisMethod.invoke(connection, paramsObj);
                              findClassesInDirPackage(packageOnly, included, excluded, URLDecoder.decode(fileUrl.getFile(), "UTF-8"), recursive, vResult);
                           } catch (Exception var20) {
                           }
                        }
                     } else {
                        JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                        Enumeration entries = jar.entries();

                        while(true) {
                           JarEntry entry;
                           String name;
                           do {
                              do {
                                 if (!entries.hasMoreElements()) {
                                    continue label91;
                                 }

                                 entry = (JarEntry)entries.nextElement();
                                 name = entry.getName();
                                 if (name.charAt(0) == '/') {
                                    name = name.substring(1);
                                 }
                              } while(!name.startsWith(packageDirName));

                              int idx = name.lastIndexOf(47);
                              if (idx != -1) {
                                 packageName = name.substring(0, idx).replace('/', '.');
                              }
                           } while(!recursive && !packageName.equals(packageOnly));

                           Utils.log("PackageUtils", 4, "Package name is " + packageName);
                           if (name.endsWith(".class") && !entry.isDirectory()) {
                              String className = name.substring(packageName.length() + 1, name.length() - 6);
                              Utils.log("PackageUtils", 4, "Found class " + className + ", seeing it if it's included or excluded");
                              includeOrExcludeClass(packageName, className, included, excluded, vResult);
                           }
                        }
                     }
                  }
               }
            }

            classLoader = (ClassLoader)dirIterator.next();
            ++count;
         } while(null == classLoader);

         Enumeration dirEnumeration = classLoader.getResources(packageDirName);

         while(dirEnumeration.hasMoreElements()) {
            URL dir = (URL)dirEnumeration.nextElement();
            dirs.add(dir);
         }
      }
   }

   private static String[] getTestClasspath() {
      if (null != s_testClassPaths) {
         return s_testClassPaths;
      } else {
         String testClasspath = System.getProperty("testng.test.classpath");
         if (null == testClasspath) {
            return null;
         } else {
            String[] classpathFragments = Utils.split(testClasspath, File.pathSeparator);
            s_testClassPaths = new String[classpathFragments.length];

            for(int i = 0; i < classpathFragments.length; ++i) {
               String path = null;
               if (!classpathFragments[i].toLowerCase().endsWith(".jar") && !classpathFragments[i].toLowerCase().endsWith(".zip")) {
                  if (classpathFragments[i].endsWith(File.separator)) {
                     path = classpathFragments[i];
                  } else {
                     path = classpathFragments[i] + "/";
                  }
               } else {
                  path = classpathFragments[i] + "!/";
               }

               s_testClassPaths[i] = path.replace('\\', '/');
            }

            return s_testClassPaths;
         }
      }
   }

   private static boolean matchTestClasspath(URL url, String lastFragment, boolean recursive) {
      String[] classpathFragments = getTestClasspath();
      if (null == classpathFragments) {
         return true;
      } else {
         String fileName = null;

         try {
            fileName = URLDecoder.decode(url.getFile(), "UTF-8");
         } catch (UnsupportedEncodingException var11) {
         }

         String[] arr$ = classpathFragments;
         int len$ = classpathFragments.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String classpathFrag = arr$[i$];
            String path = classpathFrag + lastFragment;
            int idx = fileName.indexOf(path);
            if (idx != -1 && (idx <= 0 || fileName.charAt(idx - 1) == '/') && (fileName.endsWith(classpathFrag + lastFragment) || recursive && fileName.charAt(idx + path.length()) == '/')) {
               return true;
            }
         }

         return false;
      }
   }

   private static void findClassesInDirPackage(String packageName, List<String> included, List<String> excluded, String packagePath, final boolean recursive, List<String> classes) {
      File dir = new File(packagePath);
      if (dir.exists() && dir.isDirectory()) {
         File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
               return recursive && file.isDirectory() || file.getName().endsWith(".class") || file.getName().endsWith(".groovy");
            }
         });
         Utils.log("PackageUtils", 4, "Looking for test classes in the directory: " + dir);
         File[] arr$ = dirfiles;
         int len$ = dirfiles.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            if (file.isDirectory()) {
               findClassesInDirPackage(makeFullClassName(packageName, file.getName()), included, excluded, file.getAbsolutePath(), recursive, classes);
            } else {
               String className = file.getName().substring(0, file.getName().lastIndexOf("."));
               Utils.log("PackageUtils", 4, "Found class " + className + ", seeing it if it's included or excluded");
               includeOrExcludeClass(packageName, className, included, excluded, classes);
            }
         }

      }
   }

   private static String makeFullClassName(String pkg, String cls) {
      return pkg.length() > 0 ? pkg + "." + cls : cls;
   }

   private static void includeOrExcludeClass(String packageName, String className, List<String> included, List<String> excluded, List<String> classes) {
      if (isIncluded(packageName, included, excluded)) {
         Utils.log("PackageUtils", 4, "... Including class " + className);
         classes.add(makeFullClassName(packageName, className));
      } else {
         Utils.log("PackageUtils", 4, "... Excluding class " + className);
      }

   }

   private static boolean isIncluded(String name, List<String> included, List<String> excluded) {
      boolean result = false;
      if (included.size() == 0 && excluded.size() == 0) {
         result = true;
      } else {
         boolean isIncluded = find(name, included);
         boolean isExcluded = find(name, excluded);
         if (isIncluded && !isExcluded) {
            result = true;
         } else if (isExcluded) {
            result = false;
         } else {
            result = included.size() == 0;
         }
      }

      return result;
   }

   private static boolean find(String name, List<String> list) {
      Iterator i$ = list.iterator();

      String regexpStr;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         regexpStr = (String)i$.next();
      } while(!Pattern.matches(regexpStr, name));

      return true;
   }
}
