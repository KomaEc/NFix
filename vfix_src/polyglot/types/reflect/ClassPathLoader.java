package polyglot.types.reflect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import polyglot.main.Report;

public class ClassPathLoader {
   List classpath;
   ClassFileLoader loader;
   static Collection verbose = new HashSet();

   public ClassPathLoader(List classpath, ClassFileLoader loader) {
      this.classpath = new ArrayList(classpath);
      this.loader = loader;
   }

   public ClassPathLoader(String classpath, ClassFileLoader loader) {
      this.classpath = new ArrayList();
      StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);

      while(st.hasMoreTokens()) {
         String s = st.nextToken();
         this.classpath.add(new File(s));
      }

      this.loader = loader;
   }

   public String classpath() {
      return this.classpath.toString();
   }

   public boolean packageExists(String name) {
      Iterator i = this.classpath.iterator();

      File dir;
      do {
         if (!i.hasNext()) {
            return false;
         }

         dir = (File)i.next();
      } while(!this.loader.packageExists(dir, name));

      return true;
   }

   public ClassFile loadClass(String name) {
      if (Report.should_report((Collection)verbose, 2)) {
         Report.report(2, "attempting to load class " + name);
         Report.report(2, "classpath = " + this.classpath);
      }

      Iterator i = this.classpath.iterator();

      ClassFile cf;
      do {
         if (!i.hasNext()) {
            return null;
         }

         File dir = (File)i.next();
         cf = this.loader.loadClass(dir, name);
      } while(cf == null);

      return cf;
   }

   static {
      verbose.add("loader");
   }
}
