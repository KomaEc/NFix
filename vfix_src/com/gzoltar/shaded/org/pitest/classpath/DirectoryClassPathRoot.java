package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DirectoryClassPathRoot implements ClassPathRoot {
   private final File root;

   public DirectoryClassPathRoot(File root) {
      this.root = root;
   }

   public InputStream getData(String classname) throws IOException {
      String filename = classname.replace('.', File.separatorChar).concat(".class");
      File file = new File(this.root, filename);
      return file.canRead() ? new FileInputStream(file) : null;
   }

   public URL getResource(String name) throws MalformedURLException {
      File f = new File(this.root, name);
      return f.canRead() ? f.toURI().toURL() : null;
   }

   public Collection<String> classNames() {
      return this.classNames(this.root);
   }

   private Collection<String> classNames(File file) {
      List<String> classNames = new LinkedList();
      File[] arr$ = file.listFiles();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         File f = arr$[i$];
         if (f.isDirectory()) {
            classNames.addAll(this.classNames(f));
         } else if (f.getName().endsWith(".class")) {
            classNames.add(this.fileToClassName(f));
         }
      }

      return classNames;
   }

   private String fileToClassName(File f) {
      return f.getAbsolutePath().substring(this.root.getAbsolutePath().length() + 1, f.getAbsolutePath().length() - ".class".length()).replace(File.separatorChar, '.');
   }

   public Option<String> cacheLocation() {
      return Option.some(this.root.getAbsolutePath());
   }
}
