package soot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Util;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.tagkit.SourceFileTag;

public class CoffiClassSource extends ClassSource {
   private static final Logger logger = LoggerFactory.getLogger(CoffiClassSource.class);
   private FoundFile foundFile;
   private InputStream classFile;
   private final String fileName;
   private final String zipFileName;

   public CoffiClassSource(String className, FoundFile foundFile) {
      super(className);
      if (foundFile == null) {
         throw new IllegalStateException("Error: The FoundFile must not be null.");
      } else {
         this.foundFile = foundFile;
         this.classFile = foundFile.inputStream();
         this.fileName = foundFile.getFile().getAbsolutePath();
         this.zipFileName = !foundFile.isZipFile() ? null : foundFile.getFilePath();
      }
   }

   public CoffiClassSource(String className, InputStream classFile, String fileName) {
      super(className);
      if (classFile != null && fileName != null) {
         this.classFile = classFile;
         this.fileName = fileName;
         this.zipFileName = null;
         this.foundFile = null;
      } else {
         throw new IllegalStateException("Error: The class file input strean and file name must not be null.");
      }
   }

   public IInitialResolver.Dependencies resolve(SootClass sc) {
      if (Options.v().verbose()) {
         logger.debug("resolving [from .class]: " + this.className);
      }

      ArrayList references = new ArrayList();

      try {
         Util.v().resolveFromClassFile(sc, this.classFile, this.fileName, references);
      } finally {
         this.close();
      }

      this.addSourceFileTag(sc);
      IInitialResolver.Dependencies deps = new IInitialResolver.Dependencies();
      deps.typesToSignature.addAll(references);
      return deps;
   }

   private void addSourceFileTag(SootClass sc) {
      if (this.fileName != null || this.zipFileName != null) {
         SourceFileTag tag = null;
         if (sc.hasTag("SourceFileTag")) {
            tag = (SourceFileTag)sc.getTag("SourceFileTag");
         } else {
            tag = new SourceFileTag();
            sc.addTag(tag);
         }

         if (tag.getSourceFile() == null) {
            String name = this.zipFileName == null ? (new File(this.fileName)).getName() : (new File(this.zipFileName)).getName();
            tag.setSourceFile(name);
         }

      }
   }

   public void close() {
      try {
         if (this.classFile != null) {
            this.classFile.close();
            this.classFile = null;
         }
      } catch (IOException var5) {
         throw new RuntimeException("Error: Failed to close source input stream.", var5);
      } finally {
         if (this.foundFile != null) {
            this.foundFile.close();
            this.foundFile = null;
         }

      }

   }
}
