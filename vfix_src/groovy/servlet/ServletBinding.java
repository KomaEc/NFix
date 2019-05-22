package groovy.servlet;

import groovy.lang.Binding;
import groovy.xml.MarkupBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.runtime.MethodClosure;

public class ServletBinding extends Binding {
   private boolean initialized;

   public ServletBinding(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
      super.setVariable("request", request);
      super.setVariable("response", response);
      super.setVariable("context", context);
      super.setVariable("application", context);
      super.setVariable("session", request.getSession(false));
      Map params = new LinkedHashMap();
      Enumeration names = request.getParameterNames();

      while(names.hasMoreElements()) {
         String name = (String)names.nextElement();
         if (!super.getVariables().containsKey(name)) {
            String[] values = request.getParameterValues(name);
            if (values.length == 1) {
               params.put(name, values[0]);
            } else {
               params.put(name, values);
            }
         }
      }

      super.setVariable("params", params);
      Map<String, String> headers = new LinkedHashMap();
      Enumeration names = request.getHeaderNames();

      while(names.hasMoreElements()) {
         String headerName = (String)names.nextElement();
         String headerValue = request.getHeader(headerName);
         headers.put(headerName, headerValue);
      }

      super.setVariable("headers", headers);
   }

   public void setVariable(String name, Object value) {
      this.lazyInit();
      this.validateArgs(name, "Can't bind variable to");
      this.excludeReservedName(name, "out");
      this.excludeReservedName(name, "sout");
      this.excludeReservedName(name, "html");
      this.excludeReservedName(name, "forward");
      this.excludeReservedName(name, "include");
      this.excludeReservedName(name, "redirect");
      super.setVariable(name, value);
   }

   public Map getVariables() {
      this.lazyInit();
      return super.getVariables();
   }

   public Object getVariable(String name) {
      this.lazyInit();
      this.validateArgs(name, "No variable with");
      return super.getVariable(name);
   }

   private void lazyInit() {
      if (!this.initialized) {
         this.initialized = true;
         HttpServletResponse response = (HttpServletResponse)super.getVariable("response");
         ServletContext context = (ServletContext)super.getVariable("context");
         ServletBinding.ServletOutput output = new ServletBinding.ServletOutput(response);
         super.setVariable("out", output.getWriter());
         super.setVariable("sout", output.getOutputStream());
         super.setVariable("html", new MarkupBuilder(output.getWriter()));
         MethodClosure c = new MethodClosure(this, "forward");
         super.setVariable("forward", c);
         c = new MethodClosure(this, "include");
         super.setVariable("include", c);
         c = new MethodClosure(this, "redirect");
         super.setVariable("redirect", c);
      }
   }

   private void validateArgs(String name, String message) {
      if (name == null) {
         throw new IllegalArgumentException(message + " null key.");
      } else if (name.length() == 0) {
         throw new IllegalArgumentException(message + " blank key name. [length=0]");
      }
   }

   private void excludeReservedName(String name, String reservedName) {
      if (reservedName.equals(name)) {
         throw new IllegalArgumentException("Can't bind variable to key named '" + name + "'.");
      }
   }

   public void forward(String path) throws ServletException, IOException {
      HttpServletRequest request = (HttpServletRequest)super.getVariable("request");
      HttpServletResponse response = (HttpServletResponse)super.getVariable("response");
      RequestDispatcher dispatcher = request.getRequestDispatcher(path);
      dispatcher.forward(request, response);
   }

   public void include(String path) throws ServletException, IOException {
      HttpServletRequest request = (HttpServletRequest)super.getVariable("request");
      HttpServletResponse response = (HttpServletResponse)super.getVariable("response");
      RequestDispatcher dispatcher = request.getRequestDispatcher(path);
      dispatcher.include(request, response);
   }

   public void redirect(String location) throws IOException {
      HttpServletResponse response = (HttpServletResponse)super.getVariable("response");
      response.sendRedirect(location);
   }

   private static class ServletOutput {
      private HttpServletResponse response;
      private ServletOutputStream outputStream;
      private PrintWriter writer;

      public ServletOutput(HttpServletResponse response) {
         this.response = response;
      }

      private ServletOutputStream getResponseStream() throws IOException {
         if (this.writer != null) {
            throw new IllegalStateException("The variable 'out' or 'html' have been used already. Use either out/html or sout, not both.");
         } else {
            if (this.outputStream == null) {
               this.outputStream = this.response.getOutputStream();
            }

            return this.outputStream;
         }
      }

      public ServletOutputStream getOutputStream() {
         return new ServletOutputStream() {
            public void write(int b) throws IOException {
               ServletOutput.this.getResponseStream().write(b);
            }

            public void close() throws IOException {
               ServletOutput.this.getResponseStream().close();
            }

            public void flush() throws IOException {
               ServletOutput.this.getResponseStream().flush();
            }

            public void write(byte[] b) throws IOException {
               ServletOutput.this.getResponseStream().write(b);
            }

            public void write(byte[] b, int off, int len) throws IOException {
               ServletOutput.this.getResponseStream().write(b, off, len);
            }
         };
      }

      private PrintWriter getResponseWriter() {
         if (this.outputStream != null) {
            throw new IllegalStateException("The variable 'sout' have been used already. Use either out/html or sout, not both.");
         } else {
            if (this.writer == null) {
               try {
                  this.writer = this.response.getWriter();
               } catch (IOException var2) {
                  this.writer = new PrintWriter(new ByteArrayOutputStream());
                  throw new IllegalStateException("unable to get response writer", var2);
               }
            }

            return this.writer;
         }
      }

      public PrintWriter getWriter() {
         return new PrintWriter(new ServletBinding.InvalidOutputStream()) {
            public boolean checkError() {
               return ServletOutput.this.getResponseWriter().checkError();
            }

            public void close() {
               ServletOutput.this.getResponseWriter().close();
            }

            public void flush() {
               ServletOutput.this.getResponseWriter().flush();
            }

            public void write(char[] buf) {
               ServletOutput.this.getResponseWriter().write(buf);
            }

            public void write(char[] buf, int off, int len) {
               ServletOutput.this.getResponseWriter().write(buf, off, len);
            }

            public void write(int c) {
               ServletOutput.this.getResponseWriter().write(c);
            }

            public void write(String s, int off, int len) {
               ServletOutput.this.getResponseWriter().write(s, off, len);
            }

            public void println() {
               ServletOutput.this.getResponseWriter().println();
            }

            public PrintWriter format(String format, Object... args) {
               ServletOutput.this.getResponseWriter().format(format, args);
               return this;
            }

            public PrintWriter format(Locale l, String format, Object... args) {
               ServletOutput.this.getResponseWriter().format(l, format, args);
               return this;
            }
         };
      }
   }

   private static class InvalidOutputStream extends OutputStream {
      private InvalidOutputStream() {
      }

      public void write(int b) {
         throw new GroovyBugError("Any write calls to this stream are invalid!");
      }

      // $FF: synthetic method
      InvalidOutputStream(Object x0) {
         this();
      }
   }
}
