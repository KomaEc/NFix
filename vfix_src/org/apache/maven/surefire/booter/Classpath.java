package org.apache.maven.surefire.booter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.maven.surefire.util.UrlUtils;

public class Classpath implements Iterable<String> {
   private final List<String> unmodifiableElements;

   public static Classpath join(Classpath firstClasspath, Classpath secondClasspath) {
      LinkedHashSet<String> accumulated = new LinkedHashSet();
      if (firstClasspath != null) {
         firstClasspath.addTo(accumulated);
      }

      if (secondClasspath != null) {
         secondClasspath.addTo(accumulated);
      }

      return new Classpath(accumulated);
   }

   private void addTo(Collection<String> c) {
      c.addAll(this.unmodifiableElements);
   }

   private Classpath() {
      this.unmodifiableElements = Collections.emptyList();
   }

   public Classpath(Classpath other, String additionalElement) {
      ArrayList<String> elems = new ArrayList(other.unmodifiableElements);
      elems.add(additionalElement);
      this.unmodifiableElements = Collections.unmodifiableList(elems);
   }

   public Classpath(Iterable<String> elements) {
      List<String> newCp = new ArrayList();
      Iterator i$ = elements.iterator();

      while(i$.hasNext()) {
         String element = (String)i$.next();
         newCp.add(element);
      }

      this.unmodifiableElements = Collections.unmodifiableList(newCp);
   }

   public static Classpath emptyClasspath() {
      return new Classpath();
   }

   public Classpath addClassPathElementUrl(String path) {
      if (path == null) {
         throw new IllegalArgumentException("Null is not a valid class path element url.");
      } else {
         return !this.unmodifiableElements.contains(path) ? new Classpath(this, path) : this;
      }
   }

   public List<String> getClassPath() {
      return this.unmodifiableElements;
   }

   public List<URL> getAsUrlList() throws MalformedURLException {
      List<URL> urls = new ArrayList();
      Iterator i$ = this.unmodifiableElements.iterator();

      while(i$.hasNext()) {
         String url = (String)i$.next();
         File f = new File(url);
         urls.add(UrlUtils.getURL(f));
      }

      return urls;
   }

   public void writeToSystemProperty(String propertyName) {
      StringBuilder sb = new StringBuilder();
      Iterator i$ = this.unmodifiableElements.iterator();

      while(i$.hasNext()) {
         String element = (String)i$.next();
         sb.append(element).append(File.pathSeparatorChar);
      }

      System.setProperty(propertyName, sb.toString());
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         boolean var10000;
         label35: {
            Classpath classpath = (Classpath)o;
            if (this.unmodifiableElements != null) {
               if (this.unmodifiableElements.equals(classpath.unmodifiableElements)) {
                  break label35;
               }
            } else if (classpath.unmodifiableElements == null) {
               break label35;
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      } else {
         return false;
      }
   }

   public ClassLoader createClassLoader(ClassLoader parent, boolean childDelegation, boolean enableAssertions, String roleName) throws SurefireExecutionException {
      try {
         List urls = this.getAsUrlList();
         IsolatedClassLoader classLoader = new IsolatedClassLoader(parent, childDelegation, roleName);
         Iterator i$ = urls.iterator();

         while(i$.hasNext()) {
            Object url1 = i$.next();
            URL url = (URL)url1;
            classLoader.addURL(url);
         }

         if (parent != null) {
            parent.setDefaultAssertionStatus(enableAssertions);
         }

         classLoader.setDefaultAssertionStatus(enableAssertions);
         return classLoader;
      } catch (MalformedURLException var10) {
         throw new SurefireExecutionException("When creating classloader", var10);
      }
   }

   public int hashCode() {
      return this.unmodifiableElements != null ? this.unmodifiableElements.hashCode() : 0;
   }

   public String getLogMessage(String descriptor) {
      StringBuilder result = new StringBuilder();
      result.append(descriptor).append(" classpath:");
      Iterator i$ = this.unmodifiableElements.iterator();

      while(i$.hasNext()) {
         String element = (String)i$.next();
         result.append("  ").append(element);
      }

      return result.toString();
   }

   public String getCompactLogMessage(String descriptor) {
      StringBuilder result = new StringBuilder();
      result.append(descriptor).append(" classpath:");
      Iterator i$ = this.unmodifiableElements.iterator();

      while(i$.hasNext()) {
         String element = (String)i$.next();
         result.append("  ");
         if (element != null) {
            int pos = element.lastIndexOf(File.separatorChar);
            if (pos >= 0) {
               result.append(element.substring(pos + 1));
            } else {
               result.append(element);
            }
         } else {
            result.append(element);
         }
      }

      return result.toString();
   }

   public Iterator<String> iterator() {
      return this.unmodifiableElements.iterator();
   }
}
