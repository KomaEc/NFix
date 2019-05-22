package org.codehaus.groovy.tools.groovydoc;

import groovy.text.GStringTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class GroovyDocTemplateEngine {
   private TemplateEngine engine;
   private GroovyDocTool tool;
   private ResourceManager resourceManager;
   private Properties properties;
   private Map<String, Template> docTemplates;
   private List<String> docTemplatePaths;
   private Map<String, Template> packageTemplates;
   private List<String> packageTemplatePaths;
   private Map<String, Template> classTemplates;
   private List<String> classTemplatePaths;

   public GroovyDocTemplateEngine(GroovyDocTool tool, ResourceManager resourceManager, String classTemplate) {
      this(tool, resourceManager, new String[0], new String[0], new String[]{classTemplate}, new Properties());
   }

   public GroovyDocTemplateEngine(GroovyDocTool tool, ResourceManager resourceManager, String[] docTemplates, String[] packageTemplates, String[] classTemplates, Properties properties) {
      this.tool = tool;
      this.resourceManager = resourceManager;
      this.properties = properties;
      this.docTemplatePaths = Arrays.asList(docTemplates);
      this.packageTemplatePaths = Arrays.asList(packageTemplates);
      this.classTemplatePaths = Arrays.asList(classTemplates);
      this.docTemplates = new HashMap();
      this.packageTemplates = new HashMap();
      this.classTemplates = new HashMap();
      this.engine = new GStringTemplateEngine();
   }

   String applyClassTemplates(GroovyClassDoc classDoc) {
      String templatePath = (String)this.classTemplatePaths.get(0);
      String templateWithBindingApplied = "";

      try {
         Template t = (Template)this.classTemplates.get(templatePath);
         if (t == null) {
            t = this.engine.createTemplate(this.resourceManager.getReader(templatePath));
            this.classTemplates.put(templatePath, t);
         }

         Map<String, Object> binding = new HashMap();
         binding.put("classDoc", classDoc);
         binding.put("props", this.properties);
         templateWithBindingApplied = t.make(binding).toString();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return templateWithBindingApplied;
   }

   String applyPackageTemplate(String template, GroovyPackageDoc packageDoc) {
      String templateWithBindingApplied = "";

      try {
         Template t = (Template)this.packageTemplates.get(template);
         if (t == null) {
            t = this.engine.createTemplate(this.resourceManager.getReader(template));
            this.packageTemplates.put(template, t);
         }

         Map<String, Object> binding = new HashMap();
         binding.put("packageDoc", packageDoc);
         binding.put("props", this.properties);
         templateWithBindingApplied = t.make(binding).toString();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return templateWithBindingApplied;
   }

   String applyRootDocTemplate(String template, GroovyRootDoc rootDoc) {
      String templateWithBindingApplied = "";

      try {
         Template t = (Template)this.docTemplates.get(template);
         if (t == null) {
            t = this.engine.createTemplate(this.resourceManager.getReader(template));
            this.docTemplates.put(template, t);
         }

         Map<String, Object> binding = new HashMap();
         binding.put("rootDoc", rootDoc);
         binding.put("props", this.properties);
         templateWithBindingApplied = t.make(binding).toString();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return templateWithBindingApplied;
   }

   Iterator<String> classTemplatesIterator() {
      return this.classTemplatePaths.iterator();
   }

   Iterator<String> packageTemplatesIterator() {
      return this.packageTemplatePaths.iterator();
   }

   Iterator<String> docTemplatesIterator() {
      return this.docTemplatePaths.iterator();
   }

   public void copyBinaryResource(String template, String destFileName) {
      if (this.resourceManager instanceof ClasspathResourceManager) {
         FileOutputStream outputStream = null;

         try {
            InputStream inputStream = ((ClasspathResourceManager)this.resourceManager).getInputStream(template);
            outputStream = new FileOutputStream(destFileName);
            DefaultGroovyMethods.leftShift((OutputStream)outputStream, (InputStream)inputStream);
         } catch (IOException var9) {
            System.err.println("Resource " + template + " skipped due to: " + var9.getMessage());
         } catch (NullPointerException var10) {
            System.err.println("Resource " + template + " not found so skipped");
         } finally {
            DefaultGroovyMethods.closeQuietly(outputStream);
         }
      }

   }
}
