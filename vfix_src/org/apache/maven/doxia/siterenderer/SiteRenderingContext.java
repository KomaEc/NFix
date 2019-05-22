package org.apache.maven.doxia.siterenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.maven.doxia.site.decoration.DecorationModel;

public class SiteRenderingContext {
   private static final String DEFAULT_INPUT_ENCODING = "UTF-8";
   private static final String DEFAULT_OUTPUT_ENCODING = "UTF-8";
   private String inputEncoding = "UTF-8";
   private String outputEncoding = "UTF-8";
   private String templateName;
   private ClassLoader templateClassLoader;
   private Map templateProperties;
   private Locale locale = Locale.getDefault();
   private DecorationModel decoration;
   private String defaultWindowTitle;
   private File skinJarFile;
   private boolean usingDefaultTemplate;
   private List siteDirectories = new ArrayList();
   private Map moduleExcludes;
   private List modules = new ArrayList();

   public String getTemplateName() {
      return this.templateName;
   }

   public ClassLoader getTemplateClassLoader() {
      return this.templateClassLoader;
   }

   public void setTemplateClassLoader(ClassLoader templateClassLoader) {
      this.templateClassLoader = templateClassLoader;
   }

   public Map getTemplateProperties() {
      return this.templateProperties;
   }

   public void setTemplateProperties(Map templateProperties) {
      this.templateProperties = Collections.unmodifiableMap(templateProperties);
   }

   public Locale getLocale() {
      return this.locale;
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   public DecorationModel getDecoration() {
      return this.decoration;
   }

   public void setDecoration(DecorationModel decoration) {
      this.decoration = decoration;
   }

   public void setDefaultWindowTitle(String defaultWindowTitle) {
      this.defaultWindowTitle = defaultWindowTitle;
   }

   public String getDefaultWindowTitle() {
      return this.defaultWindowTitle;
   }

   public File getSkinJarFile() {
      return this.skinJarFile;
   }

   public void setSkinJarFile(File skinJarFile) {
      this.skinJarFile = skinJarFile;
   }

   public void setTemplateName(String templateName) {
      this.templateName = templateName;
   }

   public void setUsingDefaultTemplate(boolean usingDefaultTemplate) {
      this.usingDefaultTemplate = usingDefaultTemplate;
   }

   public boolean isUsingDefaultTemplate() {
      return this.usingDefaultTemplate;
   }

   public void addSiteDirectory(File file) {
      this.siteDirectories.add(file);
   }

   public void addModuleDirectory(File file, String moduleParserId) {
      this.modules.add(new ModuleReference(moduleParserId, file));
   }

   public List getSiteDirectories() {
      return this.siteDirectories;
   }

   public List getModules() {
      return this.modules;
   }

   public Map getModuleExcludes() {
      return this.moduleExcludes;
   }

   public void setModuleExcludes(Map moduleExcludes) {
      this.moduleExcludes = moduleExcludes;
   }

   public String getInputEncoding() {
      return this.inputEncoding;
   }

   public void setInputEncoding(String inputEncoding) {
      this.inputEncoding = inputEncoding;
   }

   public String getOutputEncoding() {
      return this.outputEncoding;
   }

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }
}
