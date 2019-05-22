package groovy.text;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport;

public abstract class TemplateEngine {
   public abstract Template createTemplate(Reader var1) throws CompilationFailedException, ClassNotFoundException, IOException;

   public Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException {
      return this.createTemplate((Reader)(new StringReader(templateText)));
   }

   public Template createTemplate(File file) throws CompilationFailedException, ClassNotFoundException, IOException {
      FileReader reader = new FileReader(file);

      Template var3;
      try {
         var3 = this.createTemplate((Reader)reader);
      } finally {
         DefaultGroovyMethodsSupport.closeWithWarning(reader);
      }

      return var3;
   }

   public Template createTemplate(URL url) throws CompilationFailedException, ClassNotFoundException, IOException {
      InputStreamReader reader = new InputStreamReader(url.openStream());

      Template var3;
      try {
         var3 = this.createTemplate((Reader)reader);
      } finally {
         DefaultGroovyMethodsSupport.closeWithWarning(reader);
      }

      return var3;
   }
}
