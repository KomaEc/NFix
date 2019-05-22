package groovy.text;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.lang.Writable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;

public class SimpleTemplateEngine extends TemplateEngine {
   private boolean verbose;
   private static int counter = 1;
   private GroovyShell groovyShell;

   public SimpleTemplateEngine() {
      this(GroovyShell.class.getClassLoader());
   }

   public SimpleTemplateEngine(boolean verbose) {
      this(GroovyShell.class.getClassLoader());
      this.setVerbose(verbose);
   }

   public SimpleTemplateEngine(ClassLoader parentLoader) {
      this(new GroovyShell(parentLoader));
   }

   public SimpleTemplateEngine(GroovyShell groovyShell) {
      this.groovyShell = groovyShell;
   }

   public Template createTemplate(Reader reader) throws CompilationFailedException, IOException {
      SimpleTemplateEngine.SimpleTemplate template = new SimpleTemplateEngine.SimpleTemplate();
      String script = template.parse(reader);
      if (this.verbose) {
         System.out.println("\n-- script source --");
         System.out.print(script);
         System.out.println("\n-- script end --\n");
      }

      try {
         template.script = this.groovyShell.parse(script, "SimpleTemplateScript" + counter++ + ".groovy");
         return template;
      } catch (Exception var5) {
         throw new GroovyRuntimeException("Failed to parse template script (your template may contain an error or be trying to use expressions not currently supported): " + var5.getMessage());
      }
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   private static class SimpleTemplate implements Template {
      protected Script script;

      private SimpleTemplate() {
      }

      public Writable make() {
         return this.make((Map)null);
      }

      public Writable make(final Map map) {
         return new Writable() {
            public Writer writeTo(Writer writer) {
               Binding binding;
               if (map == null) {
                  binding = new Binding();
               } else {
                  binding = new Binding(map);
               }

               Script scriptObject = InvokerHelper.createScript(SimpleTemplate.this.script.getClass(), binding);
               PrintWriter pw = new PrintWriter(writer);
               scriptObject.setProperty("out", pw);
               scriptObject.run();
               pw.flush();
               return writer;
            }

            public String toString() {
               StringWriter sw = new StringWriter();
               this.writeTo(sw);
               return sw.toString();
            }
         };
      }

      protected String parse(Reader reader) throws IOException {
         if (!((Reader)reader).markSupported()) {
            reader = new BufferedReader((Reader)reader);
         }

         StringWriter sw = new StringWriter();
         this.startScript(sw);

         while(true) {
            int c;
            while((c = ((Reader)reader).read()) != -1) {
               if (c == 60) {
                  ((Reader)reader).mark(1);
                  c = ((Reader)reader).read();
                  if (c != 37) {
                     sw.write(60);
                     ((Reader)reader).reset();
                  } else {
                     ((Reader)reader).mark(1);
                     c = ((Reader)reader).read();
                     if (c == 61) {
                        this.groovyExpression((Reader)reader, sw);
                     } else {
                        ((Reader)reader).reset();
                        this.groovySection((Reader)reader, sw);
                     }
                  }
               } else if (c == 36) {
                  ((Reader)reader).mark(1);
                  c = ((Reader)reader).read();
                  if (c != 123) {
                     sw.write(36);
                     ((Reader)reader).reset();
                  } else {
                     ((Reader)reader).mark(1);
                     sw.write("${");
                     this.processGSstring((Reader)reader, sw);
                  }
               } else {
                  if (c == 34) {
                     sw.write(92);
                  }

                  if (c != 10 && c != 13) {
                     sw.write(c);
                  } else {
                     if (c == 13) {
                        ((Reader)reader).mark(1);
                        c = ((Reader)reader).read();
                        if (c != 10) {
                           ((Reader)reader).reset();
                        }
                     }

                     sw.write("\n");
                  }
               }
            }

            this.endScript(sw);
            return sw.toString();
         }
      }

      private void startScript(StringWriter sw) {
         sw.write("/* Generated by SimpleTemplateEngine */\n");
         sw.write("out.print(\"\"\"");
      }

      private void endScript(StringWriter sw) {
         sw.write("\"\"\");\n");
      }

      private void processGSstring(Reader reader, StringWriter sw) throws IOException {
         while(true) {
            int c;
            if ((c = reader.read()) != -1) {
               if (c != 10 && c != 13) {
                  sw.write(c);
               }

               if (c != 125) {
                  continue;
               }
            }

            return;
         }
      }

      private void groovyExpression(Reader reader, StringWriter sw) throws IOException {
         sw.write("${");

         int c;
         while((c = reader.read()) != -1) {
            if (c == 37) {
               c = reader.read();
               if (c == 62) {
                  break;
               }

               sw.write(37);
            }

            if (c != 10 && c != 13) {
               sw.write(c);
            }
         }

         sw.write("}");
      }

      private void groovySection(Reader reader, StringWriter sw) throws IOException {
         sw.write("\"\"\");");

         int c;
         for(; (c = reader.read()) != -1; sw.write(c)) {
            if (c == 37) {
               c = reader.read();
               if (c == 62) {
                  break;
               }

               sw.write(37);
            }
         }

         sw.write(";\nout.print(\"\"\"");
      }

      // $FF: synthetic method
      SimpleTemplate(Object x0) {
         this();
      }
   }
}
