package org.codehaus.groovy.control.io;

import java.io.BufferedReader;
import java.io.IOException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Janitor;

public abstract class AbstractReaderSource implements ReaderSource {
   protected CompilerConfiguration configuration;
   private BufferedReader lineSource = null;
   private String line = null;
   private int number = 0;

   public AbstractReaderSource(CompilerConfiguration configuration) {
      if (configuration == null) {
         throw new IllegalArgumentException("Compiler configuration must not be null!");
      } else {
         this.configuration = configuration;
      }
   }

   public boolean canReopenSource() {
      return true;
   }

   public String getLine(int lineNumber, Janitor janitor) {
      if (this.lineSource != null && this.number > lineNumber) {
         this.cleanup();
      }

      if (this.lineSource == null) {
         try {
            this.lineSource = new BufferedReader(this.getReader());
         } catch (Exception var5) {
         }

         this.number = 0;
      }

      if (this.lineSource != null) {
         while(this.number < lineNumber) {
            try {
               this.line = this.lineSource.readLine();
               ++this.number;
            } catch (IOException var4) {
               this.cleanup();
            }
         }

         if (janitor == null) {
            String result = this.line;
            this.cleanup();
            return result;
         }

         janitor.register(this);
      }

      return this.line;
   }

   public void cleanup() {
      if (this.lineSource != null) {
         try {
            this.lineSource.close();
         } catch (Exception var2) {
         }
      }

      this.lineSource = null;
      this.line = null;
      this.number = 0;
   }
}
