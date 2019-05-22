package groovy.servlet;

import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractHttpServlet extends HttpServlet implements ResourceConnector {
   public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
   public static final String INC_PATH_INFO = "javax.servlet.include.path_info";
   public static final String INC_REQUEST_URI = "javax.servlet.include.request_uri";
   public static final String INC_SERVLET_PATH = "javax.servlet.include.servlet_path";
   protected ServletContext servletContext = null;
   protected Matcher resourceNameMatcher = null;
   protected String resourceNameReplacement = null;
   protected boolean resourceNameReplaceAll = true;
   protected boolean verbose = false;
   protected String encoding = "UTF-8";
   protected boolean reflection = false;
   private boolean logGROOVY861 = false;

   public URLConnection getResourceConnection(String name) throws ResourceException {
      String basePath = this.servletContext.getRealPath("/");
      if (name.startsWith(basePath)) {
         name = name.substring(basePath.length());
      }

      name = name.replaceAll("\\\\", "/");
      if (name.startsWith("/")) {
         name = name.substring(1);
      }

      try {
         String tryScriptName = "/" + name;
         URL url = this.servletContext.getResource(tryScriptName);
         if (url == null) {
            tryScriptName = "/WEB-INF/groovy/" + name;
            url = this.servletContext.getResource("/WEB-INF/groovy/" + name);
         }

         if (url == null) {
            throw new ResourceException("Resource \"" + name + "\" not found!");
         } else {
            url = new URL("file", "", this.servletContext.getRealPath(tryScriptName));
            return url.openConnection();
         }
      } catch (IOException var5) {
         throw new ResourceException("Problems getting resource named \"" + name + "\"!", var5);
      }
   }

   private boolean isFile(URL ret) {
      return ret != null && ret.getProtocol().equals("file");
   }

   protected String getScriptUri(HttpServletRequest request) {
      if (this.logGROOVY861) {
         this.log("Logging request class and its class loader:");
         this.log(" c = request.getClass() :\"" + request.getClass() + "\"");
         this.log(" l = c.getClassLoader() :\"" + request.getClass().getClassLoader() + "\"");
         this.log(" l.getClass()           :\"" + request.getClass().getClassLoader().getClass() + "\"");
         this.logGROOVY861 = this.verbose;
      }

      String uri = null;
      String info = null;
      uri = (String)request.getAttribute("javax.servlet.include.servlet_path");
      if (uri != null) {
         info = (String)request.getAttribute("javax.servlet.include.path_info");
         if (info != null) {
            uri = uri + info;
         }

         return this.applyResourceNameMatcher(uri);
      } else {
         uri = request.getServletPath();
         info = request.getPathInfo();
         if (info != null) {
            uri = uri + info;
         }

         return this.applyResourceNameMatcher(uri);
      }
   }

   private String applyResourceNameMatcher(String aUri) {
      String uri = aUri;
      Matcher matcher = this.resourceNameMatcher;
      if (matcher != null) {
         matcher.reset(aUri);
         String replaced;
         if (this.resourceNameReplaceAll) {
            replaced = matcher.replaceAll(this.resourceNameReplacement);
         } else {
            replaced = matcher.replaceFirst(this.resourceNameReplacement);
         }

         if (!aUri.equals(replaced)) {
            if (this.verbose) {
               this.log("Replaced resource name \"" + aUri + "\" with \"" + replaced + "\".");
            }

            uri = replaced;
         }
      }

      return uri;
   }

   protected File getScriptUriAsFile(HttpServletRequest request) {
      String uri = this.getScriptUri(request);
      String real = this.servletContext.getRealPath(uri);
      return (new File(real)).getAbsoluteFile();
   }

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      this.servletContext = config.getServletContext();
      String value = config.getInitParameter("verbose");
      if (value != null) {
         this.verbose = Boolean.valueOf(value);
      }

      value = config.getInitParameter("encoding");
      if (value != null) {
         this.encoding = value;
      }

      if (this.verbose) {
         this.log("Parsing init parameters...");
      }

      String regex = config.getInitParameter("resource.name.regex");
      if (regex != null) {
         String replacement = config.getInitParameter("resource.name.replacement");
         String all;
         if (replacement == null) {
            Exception npex = new NullPointerException("resource.name.replacement");
            all = "Init-param 'resource.name.replacement' not specified!";
            this.log(all, npex);
            throw new ServletException(all, npex);
         }

         int flags = 0;
         this.resourceNameMatcher = Pattern.compile(regex, flags).matcher("");
         this.resourceNameReplacement = replacement;
         all = config.getInitParameter("resource.name.replace.all");
         if (all != null) {
            this.resourceNameReplaceAll = Boolean.valueOf(all);
         }
      }

      value = config.getInitParameter("logGROOVY861");
      if (value != null) {
         this.logGROOVY861 = Boolean.valueOf(value);
      }

      if (this.verbose) {
         this.log("(Abstract) init done. Listing some parameter name/value pairs:");
         this.log("verbose = " + this.verbose);
         this.log("reflection = " + this.reflection);
         this.log("logGROOVY861 = " + this.logGROOVY861);
         if (this.resourceNameMatcher != null) {
            this.log("resource.name.regex = " + this.resourceNameMatcher.pattern().pattern());
         } else {
            this.log("resource.name.regex = null");
         }

         this.log("resource.name.replacement = " + this.resourceNameReplacement);
      }

   }

   protected void setVariables(ServletBinding binding) {
   }
}
