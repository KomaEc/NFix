package soot.dava.toolkits.base.misc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.dava.Dava;
import soot.util.IterableSet;

public class PackageNamer {
   private static final Logger logger = LoggerFactory.getLogger(PackageNamer.class);
   private boolean fixed = false;
   private final ArrayList<PackageNamer.NameHolder> appRoots = new ArrayList();
   private final ArrayList<PackageNamer.NameHolder> otherRoots = new ArrayList();
   private final HashSet<String> keywords = new HashSet();
   private char fileSep;
   private String classPath;
   private String pathSep;

   public PackageNamer(Singletons.Global g) {
   }

   public static PackageNamer v() {
      return G.v().soot_dava_toolkits_base_misc_PackageNamer();
   }

   public boolean has_FixedNames() {
      return this.fixed;
   }

   public boolean use_ShortName(String fixedPackageName, String fixedShortClassName) {
      if (!this.fixed) {
         return false;
      } else if (fixedPackageName.equals(Dava.v().get_CurrentPackage())) {
         return true;
      } else {
         IterableSet packageContext = Dava.v().get_CurrentPackageContext();
         if (packageContext == null) {
            return true;
         } else {
            packageContext = this.patch_PackageContext(packageContext);
            int count = 0;
            StringTokenizer st = new StringTokenizer(this.classPath, this.pathSep);

            while(st.hasMoreTokens()) {
               String classpathDir = st.nextToken();
               Iterator packIt = packageContext.iterator();

               while(packIt.hasNext()) {
                  if (this.package_ContainsClass(classpathDir, (String)packIt.next(), fixedShortClassName)) {
                     ++count;
                     if (count > 1) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }
      }
   }

   public String get_FixedClassName(String originalFullClassName) {
      if (!this.fixed) {
         return originalFullClassName;
      } else {
         Iterator it = this.appRoots.iterator();

         PackageNamer.NameHolder h;
         do {
            if (!it.hasNext()) {
               return originalFullClassName.substring(originalFullClassName.lastIndexOf(".") + 1);
            }

            h = (PackageNamer.NameHolder)it.next();
         } while(!h.contains_OriginalName(new StringTokenizer(originalFullClassName, "."), true));

         return h.get_FixedName(new StringTokenizer(originalFullClassName, "."), true);
      }
   }

   public String get_FixedPackageName(String originalPackageName) {
      if (!this.fixed) {
         return originalPackageName;
      } else if (originalPackageName.equals("")) {
         return "";
      } else {
         Iterator it = this.appRoots.iterator();

         PackageNamer.NameHolder h;
         do {
            if (!it.hasNext()) {
               return originalPackageName;
            }

            h = (PackageNamer.NameHolder)it.next();
         } while(!h.contains_OriginalName(new StringTokenizer(originalPackageName, "."), false));

         return h.get_FixedName(new StringTokenizer(originalPackageName, "."), false);
      }
   }

   public void fixNames() {
      if (!this.fixed) {
         String[] keywordArray = new String[]{"abstract", "default", "if", "private", "this", "boolean", "do", "implements", "protected", "throw", "break", "double", "import", "public", "throws", "byte", "else", "instanceof", "return", "transient", "case", "extends", "int", "short", "try", "catch", "final", "interface", "static", "void", "char", "finally", "long", "strictfp", "volatile", "class", "float", "native", "super", "while", "const", "for", "new", "switch", "continue", "goto", "package", "synchronized", "true", "false", "null"};
         String[] var2 = keywordArray;
         int var3 = keywordArray.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String element = var2[var4];
            this.keywords.add(element);
         }

         Iterator classIt = Scene.v().getLibraryClasses().iterator();

         while(classIt.hasNext()) {
            this.add_ClassName(((SootClass)classIt.next()).getName(), this.otherRoots);
         }

         classIt = Scene.v().getApplicationClasses().iterator();

         while(classIt.hasNext()) {
            this.add_ClassName(((SootClass)classIt.next()).getName(), this.appRoots);
         }

         Iterator arit = this.appRoots.iterator();

         while(arit.hasNext()) {
            ((PackageNamer.NameHolder)arit.next()).fix_ClassNames("");
         }

         arit = this.appRoots.iterator();

         while(arit.hasNext()) {
            ((PackageNamer.NameHolder)arit.next()).fix_PackageNames();
         }

         this.fileSep = System.getProperty("file.separator").charAt(0);
         this.pathSep = System.getProperty("path.separator");
         this.classPath = System.getProperty("java.class.path");
         this.fixed = true;
      }
   }

   private void add_ClassName(String className, ArrayList<PackageNamer.NameHolder> roots) {
      ArrayList<PackageNamer.NameHolder> children = roots;
      PackageNamer.NameHolder curNode = null;

      PackageNamer.NameHolder child;
      for(StringTokenizer st = new StringTokenizer(className, "."); st.hasMoreTokens(); children = child.get_Children()) {
         String curName = st.nextToken();
         child = null;
         boolean found = false;
         Iterator lit = children.iterator();

         while(lit.hasNext()) {
            child = (PackageNamer.NameHolder)lit.next();
            if (child.get_OriginalName().equals(curName)) {
               if (!st.hasMoreTokens()) {
                  child.set_ClassAttr();
               }

               found = true;
               break;
            }
         }

         if (!found) {
            child = new PackageNamer.NameHolder(curName, curNode, !st.hasMoreTokens());
            children.add(child);
         }

         curNode = child;
      }

   }

   public boolean package_ContainsClass(String classpathDir, String packageName, String className) {
      File p = new File(classpathDir);
      if (!p.exists()) {
         return false;
      } else {
         packageName = packageName.replace('.', this.fileSep);
         if (packageName.length() > 0 && packageName.charAt(packageName.length() - 1) != this.fileSep) {
            packageName = packageName + this.fileSep;
         }

         String name = packageName + className + ".class";
         if (p.isDirectory()) {
            if (classpathDir.length() > 0 && classpathDir.charAt(classpathDir.length() - 1) != this.fileSep) {
               classpathDir = classpathDir + this.fileSep;
            }

            return (new File(classpathDir + name)).exists();
         } else {
            JarFile jf = null;

            try {
               jf = new JarFile(p);
            } catch (IOException var8) {
               return false;
            }

            return jf.getJarEntry(name) != null;
         }
      }
   }

   IterableSet patch_PackageContext(IterableSet currentContext) {
      IterableSet newContext = new IterableSet();
      Iterator it = currentContext.iterator();

      while(true) {
         while(it.hasNext()) {
            String currentPackage = (String)it.next();
            String newPackage = null;
            StringTokenizer st = new StringTokenizer(currentPackage, ".");
            if (!st.hasMoreTokens()) {
               newContext.add(currentPackage);
            } else {
               String firstToken = st.nextToken();
               Iterator arit = this.appRoots.iterator();

               while(arit.hasNext()) {
                  PackageNamer.NameHolder h = (PackageNamer.NameHolder)arit.next();
                  if (h.get_PackageName().equals(firstToken)) {
                     newPackage = h.get_OriginalPackageName(st);
                     break;
                  }
               }

               if (newPackage != null) {
                  newContext.add(newPackage);
               } else {
                  newContext.add(currentPackage);
               }
            }
         }

         return newContext;
      }
   }

   private class NameHolder {
      private final String originalName;
      private String packageName;
      private String className;
      private final ArrayList<PackageNamer.NameHolder> children;
      private PackageNamer.NameHolder parent;
      private boolean isClass;

      public NameHolder(String name, PackageNamer.NameHolder parent, boolean isClass) {
         this.originalName = name;
         this.className = name;
         this.packageName = name;
         this.parent = parent;
         this.isClass = isClass;
         this.children = new ArrayList();
      }

      public PackageNamer.NameHolder get_Parent() {
         return this.parent;
      }

      public void set_ClassAttr() {
         this.isClass = true;
      }

      public boolean is_Class() {
         return this.children.isEmpty() ? true : this.isClass;
      }

      public boolean is_Package() {
         return !this.children.isEmpty();
      }

      public String get_PackageName() {
         return this.packageName;
      }

      public String get_ClassName() {
         return this.className;
      }

      public void set_PackageName(String packageName) {
         this.packageName = packageName;
      }

      public void set_ClassName(String className) {
         this.className = className;
      }

      public String get_OriginalName() {
         return this.originalName;
      }

      public ArrayList<PackageNamer.NameHolder> get_Children() {
         return this.children;
      }

      public String get_FixedPackageName() {
         return this.parent == null ? "" : this.parent.retrieve_FixedPackageName();
      }

      public String retrieve_FixedPackageName() {
         return this.parent == null ? this.packageName : this.parent.get_FixedPackageName() + "." + this.packageName;
      }

      public String get_FixedName(StringTokenizer st, boolean forClass) {
         if (!st.nextToken().equals(this.originalName)) {
            throw new RuntimeException("Unable to resolve naming.");
         } else {
            return this.retrieve_FixedName(st, forClass);
         }
      }

      private String retrieve_FixedName(StringTokenizer st, boolean forClass) {
         if (!st.hasMoreTokens()) {
            return forClass ? this.className : this.packageName;
         } else {
            String subName = st.nextToken();
            Iterator cit = this.children.iterator();

            PackageNamer.NameHolder h;
            do {
               if (!cit.hasNext()) {
                  throw new RuntimeException("Unable to resolve naming.");
               }

               h = (PackageNamer.NameHolder)cit.next();
            } while(!h.get_OriginalName().equals(subName));

            if (forClass) {
               return h.retrieve_FixedName(st, forClass);
            } else {
               return this.packageName + "." + h.retrieve_FixedName(st, forClass);
            }
         }
      }

      public String get_OriginalPackageName(StringTokenizer st) {
         if (!st.hasMoreTokens()) {
            return this.get_OriginalName();
         } else {
            String subName = st.nextToken();
            Iterator cit = this.children.iterator();

            PackageNamer.NameHolder h;
            do {
               if (!cit.hasNext()) {
                  return null;
               }

               h = (PackageNamer.NameHolder)cit.next();
            } while(!h.get_PackageName().equals(subName));

            String originalSubPackageName = h.get_OriginalPackageName(st);
            if (originalSubPackageName == null) {
               return null;
            } else {
               return this.get_OriginalName() + "." + originalSubPackageName;
            }
         }
      }

      public boolean contains_OriginalName(StringTokenizer st, boolean forClass) {
         return !this.get_OriginalName().equals(st.nextToken()) ? false : this.finds_OriginalName(st, forClass);
      }

      private boolean finds_OriginalName(StringTokenizer st, boolean forClass) {
         if (st.hasMoreTokens()) {
            String subName = st.nextToken();
            Iterator cit = this.children.iterator();

            PackageNamer.NameHolder h;
            do {
               if (!cit.hasNext()) {
                  return false;
               }

               h = (PackageNamer.NameHolder)cit.next();
            } while(!h.get_OriginalName().equals(subName));

            return h.finds_OriginalName(st, forClass);
         } else {
            return forClass && this.is_Class() || !forClass && this.is_Package();
         }
      }

      public void fix_ClassNames(String curPackName) {
         if (this.is_Class() && PackageNamer.this.keywords.contains(this.className)) {
            String tClassName = this.className;
            if (Character.isLowerCase(this.className.charAt(0))) {
               tClassName = tClassName.substring(0, 1).toUpperCase() + tClassName.substring(1);
               this.className = tClassName;
            }

            for(int i = 0; PackageNamer.this.keywords.contains(this.className); ++i) {
               this.className = tClassName + "_c" + i;
            }
         }

         Iterator it = this.children.iterator();

         while(it.hasNext()) {
            ((PackageNamer.NameHolder)it.next()).fix_ClassNames(curPackName + "." + this.packageName);
         }

      }

      public void fix_PackageNames() {
         if (this.is_Package() && !this.verify_PackageName()) {
            String tPackageName = this.packageName;
            if (Character.isUpperCase(this.packageName.charAt(0))) {
               tPackageName = tPackageName.substring(0, 1).toLowerCase() + tPackageName.substring(1);
               this.packageName = tPackageName;
            }

            for(int i = 0; !this.verify_PackageName(); ++i) {
               this.packageName = tPackageName + "_p" + i;
            }
         }

         Iterator it = this.children.iterator();

         while(it.hasNext()) {
            ((PackageNamer.NameHolder)it.next()).fix_PackageNames();
         }

      }

      public boolean verify_PackageName() {
         return !PackageNamer.this.keywords.contains(this.packageName) && !this.siblingClashes(this.packageName) && (!this.is_Class() || !this.className.equals(this.packageName));
      }

      public boolean siblingClashes(String name) {
         Iterator<PackageNamer.NameHolder> it = null;
         if (this.parent == null) {
            if (!PackageNamer.this.appRoots.contains(this)) {
               throw new RuntimeException("Unable to find package siblings.");
            }

            it = PackageNamer.this.appRoots.iterator();
         } else {
            it = this.parent.get_Children().iterator();
         }

         PackageNamer.NameHolder sibling;
         do {
            do {
               if (!it.hasNext()) {
                  return false;
               }

               sibling = (PackageNamer.NameHolder)it.next();
            } while(sibling == this);
         } while((!sibling.is_Package() || !sibling.get_PackageName().equals(name)) && (!sibling.is_Class() || !sibling.get_ClassName().equals(name)));

         return true;
      }

      public void dump(String indentation) {
         PackageNamer.logger.debug("" + indentation + "\"" + this.originalName + "\", \"" + this.packageName + "\", \"" + this.className + "\" (");
         if (this.is_Class()) {
            PackageNamer.logger.debug("c");
         }

         if (this.is_Package()) {
            PackageNamer.logger.debug("p");
         }

         PackageNamer.logger.debug(")");
         Iterator it = this.children.iterator();

         while(it.hasNext()) {
            ((PackageNamer.NameHolder)it.next()).dump(indentation + "  ");
         }

      }
   }
}
