package com.github.javaparser.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SourceZip {
   private final Path zipPath;
   private ParserConfiguration parserConfiguration;

   public SourceZip(Path zipPath) {
      this(zipPath, new ParserConfiguration());
   }

   public SourceZip(Path zipPath, ParserConfiguration configuration) {
      Utils.assertNotNull(zipPath);
      Utils.assertNotNull(configuration);
      this.zipPath = zipPath.normalize();
      this.parserConfiguration = configuration;
      Log.info("New source zip at \"%s\"", this.zipPath);
   }

   public List<Pair<Path, ParseResult<CompilationUnit>>> parse() throws IOException {
      Log.info("Parsing zip at \"%s\"", this.zipPath);
      List<Pair<Path, ParseResult<CompilationUnit>>> results = new ArrayList();
      this.parse((path, result) -> {
         results.add(new Pair(path, result));
      });
      return results;
   }

   public SourceZip parse(SourceZip.Callback callback) throws IOException {
      Log.info("Parsing zip at \"%s\"", this.zipPath);
      JavaParser javaParser = new JavaParser(this.parserConfiguration);
      ZipFile zipFile = new ZipFile(this.zipPath.toFile());
      Throwable var4 = null;

      try {
         Iterator var5 = Collections.list(zipFile.entries()).iterator();

         while(var5.hasNext()) {
            ZipEntry entry = (ZipEntry)var5.next();
            if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
               Log.info("Parsing zip entry \"%s\"", entry.getName());
               ParseResult<CompilationUnit> result = javaParser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(zipFile.getInputStream(entry)));
               callback.process(Paths.get(entry.getName()), result);
            }
         }
      } catch (Throwable var15) {
         var4 = var15;
         throw var15;
      } finally {
         if (zipFile != null) {
            if (var4 != null) {
               try {
                  zipFile.close();
               } catch (Throwable var14) {
                  var4.addSuppressed(var14);
               }
            } else {
               zipFile.close();
            }
         }

      }

      return this;
   }

   public Path getZipPath() {
      return this.zipPath;
   }

   /** @deprecated */
   @Deprecated
   public JavaParser getJavaParser() {
      return new JavaParser(this.parserConfiguration);
   }

   /** @deprecated */
   @Deprecated
   public SourceZip setJavaParser(JavaParser javaParser) {
      Utils.assertNotNull(javaParser);
      this.parserConfiguration = javaParser.getParserConfiguration();
      return this;
   }

   public ParserConfiguration getParserConfiguration() {
      return this.parserConfiguration;
   }

   public SourceZip setParserConfiguration(ParserConfiguration parserConfiguration) {
      Utils.assertNotNull(parserConfiguration);
      this.parserConfiguration = parserConfiguration;
      return this;
   }

   @FunctionalInterface
   public interface Callback {
      void process(Path relativeZipEntryPath, ParseResult<CompilationUnit> result);
   }
}
