package groovy.servlet;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

public class GroovyServlet extends AbstractHttpServlet {
   private GroovyScriptEngine gse;

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      this.gse = this.createGroovyScriptEngine();
      this.servletContext.log("Groovy servlet initialized on " + this.gse + ".");
   }

   public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
      final String scriptUri = this.getScriptUri(request);
      response.setContentType("text/html; charset=" + this.encoding);
      final ServletBinding binding = new ServletBinding(request, response, this.servletContext);
      this.setVariables(binding);

      try {
         Closure closure = new Closure(this.gse) {
            public Object call() {
               try {
                  return ((GroovyScriptEngine)this.getDelegate()).run(scriptUri, (Binding)binding);
               } catch (ResourceException var2) {
                  throw new RuntimeException(var2);
               } catch (ScriptException var3) {
                  throw new RuntimeException(var3);
               }
            }
         };
         GroovyCategorySupport.use(ServletCategory.class, closure);
      } catch (RuntimeException var8) {
         StringBuffer error = new StringBuffer("GroovyServlet Error: ");
         error.append(" script: '");
         error.append(scriptUri);
         error.append("': ");
         Throwable e = var8.getCause();
         if (e == null) {
            error.append(" Script processing failed.");
            error.append(var8.getMessage());
            if (var8.getStackTrace().length > 0) {
               error.append(var8.getStackTrace()[0].toString());
            }

            this.servletContext.log(error.toString());
            System.err.println(error.toString());
            var8.printStackTrace(System.err);
            response.sendError(500, error.toString());
            return;
         }

         if (e instanceof ResourceException) {
            error.append(" Script not found, sending 404.");
            this.servletContext.log(error.toString());
            System.err.println(error.toString());
            response.sendError(404);
            return;
         }

         this.servletContext.log("An error occurred processing the request", var8);
         error.append(e.getMessage());
         if (e.getStackTrace().length > 0) {
            error.append(e.getStackTrace()[0].toString());
         }

         this.servletContext.log(e.toString());
         System.err.println(e.toString());
         var8.printStackTrace(System.err);
         response.sendError(500, e.toString());
      }

   }

   protected GroovyScriptEngine createGroovyScriptEngine() {
      return new GroovyScriptEngine(this);
   }
}
