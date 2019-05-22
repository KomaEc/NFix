package org.codehaus.groovy.tools.groovydoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;

public class SimpleGroovyPackageDoc extends SimpleGroovyDoc implements GroovyPackageDoc {
   private static final char FS = '/';
   final Map<String, GroovyClassDoc> classDocs = new TreeMap();
   private String description = "";
   private String summary = "";

   public SimpleGroovyPackageDoc(String name) {
      super(name);
   }

   public GroovyClassDoc[] allClasses() {
      return (GroovyClassDoc[])this.classDocs.values().toArray(new GroovyClassDoc[this.classDocs.values().size()]);
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public void putAll(Map<String, GroovyClassDoc> classes) {
      Iterator i$ = classes.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, GroovyClassDoc> docEntry = (Entry)i$.next();
         GroovyClassDoc classDoc = (GroovyClassDoc)docEntry.getValue();
         this.classDocs.put(docEntry.getKey(), classDoc);
         SimpleGroovyProgramElementDoc programElement = (SimpleGroovyProgramElementDoc)classDoc;
         programElement.setContainingPackage(this);
      }

   }

   public String nameWithDots() {
      return this.name().replace('/', '.');
   }

   public GroovyClassDoc[] allClasses(boolean arg0) {
      List<GroovyClassDoc> classDocValues = new ArrayList(this.classDocs.values());
      return (GroovyClassDoc[])classDocValues.toArray(new GroovyClassDoc[classDocValues.size()]);
   }

   public GroovyClassDoc[] enums() {
      List<GroovyClassDoc> result = new ArrayList(this.classDocs.values().size());
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc doc = (GroovyClassDoc)i$.next();
         if (doc.isEnum()) {
            result.add(doc);
         }
      }

      return (GroovyClassDoc[])result.toArray(new GroovyClassDoc[result.size()]);
   }

   public GroovyClassDoc[] errors() {
      List<GroovyClassDoc> result = new ArrayList(this.classDocs.values().size());
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc doc = (GroovyClassDoc)i$.next();
         if (doc.isError()) {
            result.add(doc);
         }
      }

      return (GroovyClassDoc[])result.toArray(new GroovyClassDoc[result.size()]);
   }

   public GroovyClassDoc[] exceptions() {
      List<GroovyClassDoc> result = new ArrayList(this.classDocs.values().size());
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc doc = (GroovyClassDoc)i$.next();
         if (doc.isException()) {
            result.add(doc);
         }
      }

      return (GroovyClassDoc[])result.toArray(new GroovyClassDoc[result.size()]);
   }

   public GroovyClassDoc findClass(String arg0) {
      return null;
   }

   public GroovyClassDoc[] interfaces() {
      List<GroovyClassDoc> result = new ArrayList(this.classDocs.values().size());
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc doc = (GroovyClassDoc)i$.next();
         if (doc.isInterface()) {
            result.add(doc);
         }
      }

      return (GroovyClassDoc[])result.toArray(new GroovyClassDoc[result.size()]);
   }

   public GroovyClassDoc[] ordinaryClasses() {
      List<GroovyClassDoc> result = new ArrayList(this.classDocs.values().size());
      Iterator i$ = this.classDocs.values().iterator();

      while(i$.hasNext()) {
         GroovyClassDoc doc = (GroovyClassDoc)i$.next();
         if (doc.isOrdinaryClass()) {
            result.add(doc);
         }
      }

      return (GroovyClassDoc[])result.toArray(new GroovyClassDoc[result.size()]);
   }

   public String description() {
      return this.description;
   }

   public String summary() {
      return this.summary;
   }

   public String getRelativeRootPath() {
      StringTokenizer tokenizer = new StringTokenizer(this.name(), "/");
      StringBuffer sb = new StringBuffer();

      while(tokenizer.hasMoreTokens()) {
         tokenizer.nextToken();
         sb.append("../");
      }

      return sb.toString();
   }
}
