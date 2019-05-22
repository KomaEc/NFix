package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CompoundClassPathRoot implements ClassPathRoot, Iterable<ClassPathRoot> {
   private final List<ClassPathRoot> roots = new ArrayList();

   public CompoundClassPathRoot(List<ClassPathRoot> roots) {
      this.roots.addAll(roots);
   }

   public InputStream getData(String name) throws IOException {
      Iterator i$ = this.roots.iterator();

      InputStream is;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         ClassPathRoot each = (ClassPathRoot)i$.next();
         is = each.getData(name);
      } while(is == null);

      return is;
   }

   public Collection<String> classNames() {
      List<String> arrayList = new ArrayList();
      Iterator i$ = this.roots.iterator();

      while(i$.hasNext()) {
         ClassPathRoot root = (ClassPathRoot)i$.next();
         arrayList.addAll(root.classNames());
      }

      return arrayList;
   }

   public URL getResource(String name) throws MalformedURLException {
      try {
         return this.findRootForResource(name);
      } catch (IOException var3) {
         return null;
      }
   }

   private URL findRootForResource(String name) throws IOException {
      Iterator i$ = this.roots.iterator();

      URL u;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         ClassPathRoot root = (ClassPathRoot)i$.next();
         u = root.getResource(name);
      } while(u == null);

      return u;
   }

   public Option<String> cacheLocation() {
      StringBuilder classpath = new StringBuilder();
      Iterator i$ = this.roots.iterator();

      while(i$.hasNext()) {
         ClassPathRoot each = (ClassPathRoot)i$.next();
         Option<String> additional = each.cacheLocation();

         String path;
         for(Iterator i$ = additional.iterator(); i$.hasNext(); classpath = classpath.append(File.pathSeparator + path)) {
            path = (String)i$.next();
         }
      }

      return Option.some(classpath.toString());
   }

   public Iterator<ClassPathRoot> iterator() {
      return this.roots.iterator();
   }
}
