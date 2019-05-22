package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;

public class SimpleGroovyRootDoc extends SimpleGroovyDoc implements GroovyRootDoc {
   private Map<String, GroovyPackageDoc> packageDocs = new HashMap();
   private List<GroovyPackageDoc> packageDocValues = null;
   private Map<String, GroovyClassDoc> classDocs = new HashMap();
   private List<GroovyClassDoc> classDocValues = null;
   private String description = "";

   public SimpleGroovyRootDoc(String name) {
      super(name);
   }

   public GroovyClassDoc classNamed(String name) {
      Iterator i$ = this.classDocs.keySet().iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         if (key.equals(name)) {
            return (GroovyClassDoc)this.classDocs.get(key);
         }

         int lastSlashIdx = key.lastIndexOf(47);
         if (lastSlashIdx > 0) {
            String shortKey = key.substring(lastSlashIdx + 1);
            if (shortKey.equals(name)) {
               return (GroovyClassDoc)this.classDocs.get(key);
            }
         }
      }

      return null;
   }

   public GroovyClassDoc classNamedExact(String name) {
      Iterator i$ = this.classDocs.keySet().iterator();

      String key;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         key = (String)i$.next();
      } while(!key.equals(name));

      return (GroovyClassDoc)this.classDocs.get(key);
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String description() {
      return this.description;
   }

   public String summary() {
      return SimpleGroovyDoc.calculateFirstSentence(this.description);
   }

   public GroovyClassDoc[] classes() {
      if (this.classDocValues == null) {
         this.classDocValues = new ArrayList(this.classDocs.values());
         Collections.sort(this.classDocValues);
      }

      return (GroovyClassDoc[])this.classDocValues.toArray(new GroovyClassDoc[this.classDocValues.size()]);
   }

   public String[][] options() {
      return (String[][])null;
   }

   public GroovyPackageDoc packageNamed(String packageName) {
      return (GroovyPackageDoc)this.packageDocs.get(packageName);
   }

   public void putAllClasses(Map<String, GroovyClassDoc> classes) {
      this.classDocs.putAll(classes);
      this.classDocValues = null;
   }

   public void put(String packageName, GroovyPackageDoc packageDoc) {
      this.packageDocs.put(packageName, packageDoc);
      this.packageDocValues = null;
   }

   public GroovyClassDoc[] specifiedClasses() {
      return null;
   }

   public GroovyPackageDoc[] specifiedPackages() {
      if (this.packageDocValues == null) {
         this.packageDocValues = new ArrayList(this.packageDocs.values());
         Collections.sort(this.packageDocValues);
      }

      return (GroovyPackageDoc[])this.packageDocValues.toArray(new GroovyPackageDoc[this.packageDocValues.size()]);
   }

   public Map<String, GroovyClassDoc> getVisibleClasses(List importedClassesAndPackages) {
      Map<String, GroovyClassDoc> visibleClasses = new HashMap();
      Iterator i$ = this.classDocs.keySet().iterator();

      while(true) {
         String fullClassName;
         String equivalentPackageImport;
         do {
            if (!i$.hasNext()) {
               return visibleClasses;
            }

            fullClassName = (String)i$.next();
            equivalentPackageImport = fullClassName.replaceAll("[^/]+$", "*");
         } while(!importedClassesAndPackages.contains(fullClassName) && !importedClassesAndPackages.contains(equivalentPackageImport));

         GroovyClassDoc classDoc = (GroovyClassDoc)this.classDocs.get(fullClassName);
         visibleClasses.put(classDoc.name(), classDoc);
      }
   }

   public void printError(String arg0) {
   }

   public void printNotice(String arg0) {
   }

   public void printWarning(String arg0) {
   }

   public void resolve() {
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc groovyClassDoc = (GroovyClassDoc)i$.next();
         SimpleGroovyClassDoc classDoc = (SimpleGroovyClassDoc)groovyClassDoc;
         classDoc.resolve(this);
      }

   }
}
