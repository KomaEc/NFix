package com.gzoltar.shaded.org.jacoco.core.analysis;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.ExecutionDataStore;
import com.gzoltar.shaded.org.jacoco.core.internal.ContentTypeDetector;
import com.gzoltar.shaded.org.jacoco.core.internal.Java9Support;
import com.gzoltar.shaded.org.jacoco.core.internal.Pack200Streams;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.ClassAnalyzer;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.ClassCoverageImpl;
import com.gzoltar.shaded.org.jacoco.core.internal.analysis.StringPool;
import com.gzoltar.shaded.org.jacoco.core.internal.data.CRC64;
import com.gzoltar.shaded.org.jacoco.core.internal.flow.ClassProbesAdapter;
import com.gzoltar.shaded.org.objectweb.asm.ClassReader;
import com.gzoltar.shaded.org.objectweb.asm.ClassVisitor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Analyzer {
   private final ExecutionDataStore executionData;
   private final ICoverageVisitor coverageVisitor;
   private final StringPool stringPool;

   public Analyzer(ExecutionDataStore executionData, ICoverageVisitor coverageVisitor) {
      this.executionData = executionData;
      this.coverageVisitor = coverageVisitor;
      this.stringPool = new StringPool();
   }

   private ClassVisitor createAnalyzingVisitor(long classid, String className) {
      ExecutionData data = this.executionData.get(classid);
      boolean[] probes;
      boolean noMatch;
      if (data == null) {
         probes = null;
         noMatch = this.executionData.contains(className);
      } else {
         probes = data.getProbes();
         noMatch = false;
      }

      final ClassCoverageImpl coverage = new ClassCoverageImpl(className, classid, noMatch);
      ClassAnalyzer analyzer = new ClassAnalyzer(coverage, probes, this.stringPool) {
         public void visitEnd() {
            super.visitEnd();
            Analyzer.this.coverageVisitor.visitCoverage(coverage);
         }
      };
      return new ClassProbesAdapter(analyzer, false);
   }

   public void analyzeClass(ClassReader reader) {
      ClassVisitor visitor = this.createAnalyzingVisitor(CRC64.checksum(reader.b), reader.getClassName());
      reader.accept(visitor, 0);
   }

   public void analyzeClass(byte[] buffer, String location) throws IOException {
      try {
         this.analyzeClass(new ClassReader(Java9Support.downgradeIfRequired(buffer)));
      } catch (RuntimeException var4) {
         throw this.analyzerError(location, var4);
      }
   }

   public void analyzeClass(InputStream input, String location) throws IOException {
      try {
         this.analyzeClass(Java9Support.readFully(input), location);
      } catch (RuntimeException var4) {
         throw this.analyzerError(location, var4);
      }
   }

   private IOException analyzerError(String location, Exception cause) {
      IOException ex = new IOException(String.format("Error while analyzing %s.", location));
      ex.initCause(cause);
      return ex;
   }

   public int analyzeAll(InputStream input, String location) throws IOException {
      ContentTypeDetector detector;
      try {
         detector = new ContentTypeDetector(input);
      } catch (IOException var5) {
         throw this.analyzerError(location, var5);
      }

      switch(detector.getType()) {
      case -889275714:
         this.analyzeClass(detector.getInputStream(), location);
         return 1;
      case -889270259:
         return this.analyzePack200(detector.getInputStream(), location);
      case 529203200:
         return this.analyzeGzip(detector.getInputStream(), location);
      case 1347093252:
         return this.analyzeZip(detector.getInputStream(), location);
      default:
         return 0;
      }
   }

   public int analyzeAll(File file) throws IOException {
      int count = 0;
      if (file.isDirectory()) {
         File[] arr$ = file.listFiles();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File f = arr$[i$];
            count += this.analyzeAll(f);
         }
      } else {
         FileInputStream in = new FileInputStream(file);

         try {
            count += this.analyzeAll((InputStream)in, (String)file.getPath());
         } finally {
            in.close();
         }
      }

      return count;
   }

   public int analyzeAll(String path, File basedir) throws IOException {
      int count = 0;

      for(StringTokenizer st = new StringTokenizer(path, File.pathSeparator); st.hasMoreTokens(); count += this.analyzeAll(new File(basedir, st.nextToken()))) {
      }

      return count;
   }

   private int analyzeZip(InputStream input, String location) throws IOException {
      ZipInputStream zip = new ZipInputStream(input);

      int count;
      ZipEntry entry;
      for(count = 0; (entry = this.nextEntry(zip, location)) != null; count += this.analyzeAll((InputStream)zip, (String)(location + "@" + entry.getName()))) {
      }

      return count;
   }

   private ZipEntry nextEntry(ZipInputStream input, String location) throws IOException {
      try {
         return input.getNextEntry();
      } catch (IOException var4) {
         throw this.analyzerError(location, var4);
      }
   }

   private int analyzeGzip(InputStream input, String location) throws IOException {
      GZIPInputStream gzipInputStream;
      try {
         gzipInputStream = new GZIPInputStream(input);
      } catch (IOException var5) {
         throw this.analyzerError(location, var5);
      }

      return this.analyzeAll((InputStream)gzipInputStream, (String)location);
   }

   private int analyzePack200(InputStream input, String location) throws IOException {
      InputStream unpackedInput;
      try {
         unpackedInput = Pack200Streams.unpack(input);
      } catch (IOException var5) {
         throw this.analyzerError(location, var5);
      }

      return this.analyzeAll(unpackedInput, location);
   }
}
