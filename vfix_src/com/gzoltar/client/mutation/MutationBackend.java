package com.gzoltar.client.mutation;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public abstract class MutationBackend {
   protected boolean compileMutant(Mutant var1) {
      JavaCompiler var2 = ToolProvider.getSystemJavaCompiler();

      assert var2 != null;

      try {
         DiagnosticCollector var3 = new DiagnosticCollector();
         StandardJavaFileManager var4 = var2.getStandardFileManager((DiagnosticListener)null, (Locale)null, (Charset)null);
         ArrayList var5;
         (var5 = new ArrayList()).add("-cp");
         var5.add(System.getProperty("java.class.path"));
         boolean var7 = var2.getTask((Writer)null, var4, var3, var5, (Iterable)null, var4.getJavaFileObjects(new File(var1.getDirectory() + SystemProperties.FILE_SEPARATOR + var1.getJavaFile()))).call();
         var4.close();
         if (var7) {
            Logger.getInstance().debug("  >>> SUCCESS");
            return true;
         } else {
            Logger.getInstance().debug("  <<< FAIL");
            Iterator var8 = var3.getDiagnostics().iterator();

            while(var8.hasNext()) {
               Diagnostic var9 = (Diagnostic)var8.next();
               Logger.getInstance().debug("Code: " + var9.getCode());
               Logger.getInstance().debug("Kind: " + var9.getKind());
               Logger.getInstance().debug("Position: " + var9.getPosition());
               Logger.getInstance().debug("Start Position: " + var9.getStartPosition());
               Logger.getInstance().debug("End Position: " + var9.getEndPosition());
               Logger.getInstance().debug("Source: " + var9.getSource());
               Logger.getInstance().debug("Message: " + var9.getMessage(Locale.getDefault()));
            }

            return false;
         }
      } catch (IOException | RuntimeException var6) {
         Logger.getInstance().debug("  <<< FAIL", var6);
         return false;
      }
   }

   public abstract int createMutants(List<String> var1);
}
