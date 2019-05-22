package polyglot.frontend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;
import polyglot.util.UnicodeWriter;

public class TargetFactory {
   File outputDirectory;
   String outputExtension;
   boolean outputStdout;

   public TargetFactory(File outDir, String outExt, boolean so) {
      this.outputDirectory = outDir;
      this.outputExtension = outExt;
      this.outputStdout = so;
   }

   public File getOutputDirectory() {
      return this.outputDirectory;
   }

   public Writer outputWriter(String packageName, String className, Source source) throws IOException {
      return this.outputWriter(this.outputFile(packageName, className, source));
   }

   public Writer outputWriter(File outputFile) throws IOException {
      if (Report.should_report((String)"frontend", 2)) {
         Report.report(2, "Opening " + outputFile + " for output.");
      }

      if (this.outputStdout) {
         return new UnicodeWriter(new PrintWriter(System.out));
      } else {
         if (!outputFile.getParentFile().exists()) {
            File parent = outputFile.getParentFile();
            parent.mkdirs();
         }

         return new UnicodeWriter(new FileWriter(outputFile));
      }
   }

   public File outputFile(String packageName, Source source) {
      String name = (new File(source.name())).getName();
      name = name.substring(0, name.lastIndexOf(46));
      return this.outputFile(packageName, name, source);
   }

   public File outputFile(String packageName, String className, Source source) {
      if (this.outputDirectory == null) {
         throw new InternalCompilerError("Output directory not set.");
      } else {
         if (packageName == null) {
            packageName = "";
         }

         File outputFile = new File(this.outputDirectory, packageName.replace('.', File.separatorChar) + File.separatorChar + className + "." + this.outputExtension);
         if (source != null && outputFile.getPath().equals(source.path())) {
            throw new InternalCompilerError("The output file is the same as the source file");
         } else {
            return outputFile;
         }
      }
   }

   public String headerNameForFileName(String filename) {
      String s = null;
      int dotIdx = filename.lastIndexOf(".");
      if (dotIdx < 0) {
         s = filename + ".h";
      } else {
         s = filename.substring(0, dotIdx + 1) + "h";
      }

      return s;
   }
}
