package org.codehaus.classworlds;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

public class DefaultClassRealm implements ClassRealm {
   private ClassWorld world;
   private String id;
   private TreeSet imports;
   private ClassLoader foreignClassLoader;
   private RealmClassLoader classLoader;
   private ClassRealm parent;

   public DefaultClassRealm(ClassWorld world, String id) {
      this(world, id, (ClassLoader)null);
   }

   public DefaultClassRealm(ClassWorld world, String id, ClassLoader foreignClassLoader) {
      this.world = world;
      this.id = id;
      this.imports = new TreeSet();
      if (foreignClassLoader != null) {
         this.foreignClassLoader = foreignClassLoader;
      }

      if ("true".equals(System.getProperty("classworlds.bootstrapped"))) {
         this.classLoader = new UberJarRealmClassLoader(this);
      } else {
         this.classLoader = new RealmClassLoader(this);
      }

   }

   public URL[] getConstituents() {
      return this.classLoader.getURLs();
   }

   public ClassRealm getParent() {
      return this.parent;
   }

   public void setParent(ClassRealm parent) {
      this.parent = parent;
   }

   public String getId() {
      return this.id;
   }

   public ClassWorld getWorld() {
      return this.world;
   }

   public void importFrom(String realmId, String packageName) throws NoSuchRealmException {
      this.imports.add(new Entry(this.getWorld().getRealm(realmId), packageName));
      this.imports.add(new Entry(this.getWorld().getRealm(realmId), packageName.replace('.', '/')));
   }

   public void addConstituent(URL constituent) {
      this.classLoader.addConstituent(constituent);
   }

   public void addConstituent(String constituent, byte[] b) throws ClassNotFoundException {
      try {
         File path;
         File file;
         if (constituent.lastIndexOf(46) != -1) {
            path = new File("byteclass/" + constituent.substring(0, constituent.lastIndexOf(46) + 1).replace('.', File.separatorChar));
            file = new File(path, constituent.substring(constituent.lastIndexOf(46) + 1) + ".class");
         } else {
            path = new File("byteclass/");
            file = new File(path, constituent + ".class");
         }

         this.addConstituent(new URL((URL)null, file.toURL().toExternalForm(), new BytesURLStreamHandler(b)));
      } catch (IOException var5) {
         throw new ClassNotFoundException("Couldn't load byte stream.", var5);
      }
   }

   public ClassRealm locateSourceRealm(String classname) {
      Iterator iterator = this.imports.iterator();

      Entry entry;
      do {
         if (!iterator.hasNext()) {
            return this;
         }

         entry = (Entry)iterator.next();
      } while(!entry.matches(classname));

      return entry.getRealm();
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public ClassRealm createChildRealm(String id) throws DuplicateRealmException {
      ClassRealm childRealm = this.getWorld().newRealm(id);
      childRealm.setParent(this);
      return childRealm;
   }

   public Class loadClass(String name) throws ClassNotFoundException {
      if (name.startsWith("org.codehaus.classworlds.")) {
         return this.getWorld().loadClass(name);
      } else {
         try {
            if (this.foreignClassLoader != null) {
               try {
                  return this.foreignClassLoader.loadClass(name);
               } catch (ClassNotFoundException var5) {
               }
            }

            ClassRealm sourceRealm = this.locateSourceRealm(name);
            if (sourceRealm == this) {
               return this.classLoader.loadClassDirect(name);
            } else {
               try {
                  return sourceRealm.loadClass(name);
               } catch (ClassNotFoundException var4) {
                  return this.classLoader.loadClassDirect(name);
               }
            }
         } catch (ClassNotFoundException var6) {
            if (this.getParent() != null) {
               return this.getParent().loadClass(name);
            } else {
               throw var6;
            }
         }
      }
   }

   public URL getResource(String name) {
      URL resource = null;
      name = UrlUtils.normalizeUrlPath(name);
      if (this.foreignClassLoader != null) {
         resource = this.foreignClassLoader.getResource(name);
         if (resource != null) {
            return resource;
         }
      }

      ClassRealm sourceRealm = this.locateSourceRealm(name);
      if (sourceRealm == this) {
         resource = this.classLoader.getResourceDirect(name);
      } else {
         resource = sourceRealm.getResource(name);
         if (resource == null) {
            resource = this.classLoader.getResourceDirect(name);
         }
      }

      if (resource == null && this.getParent() != null) {
         resource = this.getParent().getResource(name);
      }

      return resource;
   }

   public InputStream getResourceAsStream(String name) {
      URL url = this.getResource(name);
      InputStream is = null;
      if (url != null) {
         try {
            is = url.openStream();
         } catch (IOException var5) {
         }
      }

      return is;
   }

   public Enumeration findResources(String name) throws IOException {
      name = UrlUtils.normalizeUrlPath(name);
      Vector resources = new Vector();
      if (this.foreignClassLoader != null) {
         Enumeration res = this.foreignClassLoader.getResources(name);

         while(res.hasMoreElements()) {
            resources.addElement(res.nextElement());
         }
      }

      ClassRealm sourceRealm = this.locateSourceRealm(name);
      Enumeration parent;
      if (sourceRealm != this) {
         parent = sourceRealm.findResources(name);

         while(parent.hasMoreElements()) {
            resources.addElement(parent.nextElement());
         }
      }

      parent = this.classLoader.findResourcesDirect(name);

      while(parent.hasMoreElements()) {
         resources.addElement(parent.nextElement());
      }

      if (this.parent != null) {
         parent = this.getParent().findResources(name);

         while(parent.hasMoreElements()) {
            resources.addElement(parent.nextElement());
         }
      }

      return resources.elements();
   }

   public void display() {
      ClassRealm cr = this;
      System.out.println("-----------------------------------------------------");
      this.showUrls(this);

      while(((ClassRealm)cr).getParent() != null) {
         System.out.println("\n");
         cr = ((ClassRealm)cr).getParent();
         this.showUrls((ClassRealm)cr);
      }

      System.out.println("-----------------------------------------------------");
   }

   private void showUrls(ClassRealm classRealm) {
      System.out.println("this realm = " + classRealm.getId());
      URL[] urls = classRealm.getConstituents();

      for(int i = 0; i < urls.length; ++i) {
         System.out.println("urls[" + i + "] = " + urls[i]);
      }

      System.out.println("Number of imports: " + this.imports.size());
      Iterator i = this.imports.iterator();

      while(i.hasNext()) {
         System.out.println("import: " + i.next());
      }

   }
}
