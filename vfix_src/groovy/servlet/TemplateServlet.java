package groovy.servlet;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TemplateServlet extends AbstractHttpServlet {
   private final Map cache = new WeakHashMap();
   private TemplateEngine engine = null;
   private boolean generateBy = true;
   private String fileEncodingParamVal = null;
   private static final String GROOVY_SOURCE_ENCODING = "groovy.source.encoding";

   protected Template getTemplate(File file) throws ServletException {
      String key = file.getAbsolutePath();
      Template template = null;
      if (this.verbose) {
         this.log("Looking for cached template by key \"" + key + "\"");
      }

      TemplateServlet.TemplateCacheEntry entry = (TemplateServlet.TemplateCacheEntry)this.cache.get(key);
      if (entry != null) {
         if (entry.validate(file)) {
            if (this.verbose) {
               this.log("Cache hit! " + entry);
            }

            template = entry.template;
         } else if (this.verbose) {
            this.log("Cached template needs recompiliation!");
         }
      } else if (this.verbose) {
         this.log("Cache miss.");
      }

      if (template == null) {
         if (this.verbose) {
            this.log("Creating new template from file " + file + "...");
         }

         String fileEncoding = this.fileEncodingParamVal != null ? this.fileEncodingParamVal : System.getProperty("groovy.source.encoding");
         Object reader = null;

         try {
            reader = fileEncoding == null ? new FileReader(file) : new InputStreamReader(new FileInputStream(file), fileEncoding);
            template = this.engine.createTemplate((Reader)reader);
         } catch (Exception var15) {
            throw new ServletException("Creation of template failed: " + var15, var15);
         } finally {
            if (reader != null) {
               try {
                  ((Reader)reader).close();
               } catch (IOException var14) {
               }
            }

         }

         this.cache.put(key, new TemplateServlet.TemplateCacheEntry(file, template, this.verbose));
         if (this.verbose) {
            this.log("Created and added template to cache. [key=" + key + "]");
         }
      }

      if (template == null) {
         throw new ServletException("Template is null? Should not happen here!");
      } else {
         return template;
      }
   }

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      this.engine = this.initTemplateEngine(config);
      if (this.engine == null) {
         throw new ServletException("Template engine not instantiated.");
      } else {
         String value = config.getInitParameter("generated.by");
         if (value != null) {
            this.generateBy = Boolean.valueOf(value);
         }

         value = config.getInitParameter("groovy.source.encoding");
         if (value != null) {
            this.fileEncodingParamVal = value;
         }

         this.log("Servlet " + this.getClass().getName() + " initialized on " + this.engine.getClass());
      }
   }

   protected TemplateEngine initTemplateEngine(ServletConfig config) {
      String name = config.getInitParameter("template.engine");
      if (name == null) {
         return new SimpleTemplateEngine();
      } else {
         try {
            return (TemplateEngine)Class.forName(name).newInstance();
         } catch (InstantiationException var4) {
            this.log("Could not instantiate template engine: " + name, var4);
         } catch (IllegalAccessException var5) {
            this.log("Could not access template engine class: " + name, var5);
         } catch (ClassNotFoundException var6) {
            this.log("Could not find template engine class: " + name, var6);
         }

         return null;
      }
   }

   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      if (this.verbose) {
         this.log("Creating/getting cached template...");
      }

      File file = super.getScriptUriAsFile(request);
      String name = file.getName();
      if (!file.exists()) {
         response.sendError(404);
      } else if (!file.canRead()) {
         response.sendError(403, "Can not read \"" + name + "\"!");
      } else {
         long getMillis = System.currentTimeMillis();
         Template template = this.getTemplate(file);
         getMillis = System.currentTimeMillis() - getMillis;
         ServletBinding binding = new ServletBinding(request, response, this.servletContext);
         this.setVariables(binding);
         response.setContentType("text/html; charset=" + this.encoding);
         response.setStatus(200);
         Writer out = (Writer)binding.getVariable("out");
         if (out == null) {
            out = response.getWriter();
         }

         if (this.verbose) {
            this.log("Making template \"" + name + "\"...");
         }

         long makeMillis = System.currentTimeMillis();
         template.make(binding.getVariables()).writeTo((Writer)out);
         makeMillis = System.currentTimeMillis() - makeMillis;
         if (this.generateBy) {
            StringBuffer sb = new StringBuffer(100);
            sb.append("\n<!-- Generated by Groovy TemplateServlet [create/get=");
            sb.append(Long.toString(getMillis));
            sb.append(" ms, make=");
            sb.append(Long.toString(makeMillis));
            sb.append(" ms] -->\n");
            ((Writer)out).write(sb.toString());
         }

         response.flushBuffer();
         if (this.verbose) {
            this.log("Template \"" + name + "\" request responded. [create/get=" + getMillis + " ms, make=" + makeMillis + " ms]");
         }

      }
   }

   private static class TemplateCacheEntry {
      Date date;
      long hit;
      long lastModified;
      long length;
      Template template;

      public TemplateCacheEntry(File file, Template template) {
         this(file, template, false);
      }

      public TemplateCacheEntry(File file, Template template, boolean timestamp) {
         if (file == null) {
            throw new NullPointerException("file");
         } else if (template == null) {
            throw new NullPointerException("template");
         } else {
            if (timestamp) {
               this.date = new Date(System.currentTimeMillis());
            } else {
               this.date = null;
            }

            this.hit = 0L;
            this.lastModified = file.lastModified();
            this.length = file.length();
            this.template = template;
         }
      }

      public boolean validate(File file) {
         if (file == null) {
            throw new NullPointerException("file");
         } else if (file.lastModified() != this.lastModified) {
            return false;
         } else if (file.length() != this.length) {
            return false;
         } else {
            ++this.hit;
            return true;
         }
      }

      public String toString() {
         return this.date == null ? "Hit #" + this.hit : "Hit #" + this.hit + " since " + this.date;
      }
   }
}
