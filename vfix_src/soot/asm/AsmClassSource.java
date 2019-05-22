package soot.asm;

import java.io.IOException;
import java.io.InputStream;
import org.objectweb.asm.ClassReader;
import soot.ClassSource;
import soot.FoundFile;
import soot.SootClass;
import soot.javaToJimple.IInitialResolver;

class AsmClassSource extends ClassSource {
   private FoundFile foundFile;

   AsmClassSource(String cls, FoundFile foundFile) {
      super(cls);
      if (foundFile == null) {
         throw new IllegalStateException("Error: The FoundFile must not be null.");
      } else {
         this.foundFile = foundFile;
      }
   }

   public IInitialResolver.Dependencies resolve(SootClass sc) {
      InputStream d = null;

      IInitialResolver.Dependencies var6;
      try {
         d = this.foundFile.inputStream();
         ClassReader clsr = new ClassReader(d);
         SootClassBuilder scb = new SootClassBuilder(sc);
         clsr.accept(scb, 4);
         IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
         deps.typesToSignature.addAll(scb.deps);
         var6 = deps;
      } catch (IOException var30) {
         throw new RuntimeException("Error: Failed to create class reader from class source.", var30);
      } finally {
         try {
            if (d != null) {
               d.close();
               d = null;
            }
         } catch (IOException var31) {
            throw new RuntimeException("Error: Failed to close source input stream.", var31);
         } finally {
            this.close();
         }

      }

      return var6;
   }

   public void close() {
      if (this.foundFile != null) {
         this.foundFile.close();
         this.foundFile = null;
      }

   }
}
