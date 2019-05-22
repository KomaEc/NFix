package com.github.javaparser.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.CommentsCollection;
import com.github.javaparser.printer.PrettyPrinter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SourceRoot {
   private final Path root;
   private final Map<Path, ParseResult<CompilationUnit>> cache;
   private ParserConfiguration parserConfiguration;
   private Function<CompilationUnit, String> printer;
   private static final Pattern JAVA_IDENTIFIER = Pattern.compile("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*");

   public SourceRoot(Path root) {
      this.cache = new ConcurrentHashMap();
      this.parserConfiguration = new ParserConfiguration();
      PrettyPrinter var10001 = new PrettyPrinter();
      this.printer = var10001::print;
      Utils.assertNotNull(root);
      if (!Files.isDirectory(root, new LinkOption[0])) {
         throw new IllegalArgumentException("Only directories are allowed as root path: " + root);
      } else {
         this.root = root.normalize();
         Log.info("New source root at \"%s\"", this.root);
      }
   }

   public SourceRoot(Path root, ParserConfiguration parserConfiguration) {
      this(root);
      this.setParserConfiguration(parserConfiguration);
   }

   /** @deprecated */
   @Deprecated
   public ParseResult<CompilationUnit> tryToParse(String startPackage, String filename, JavaParser javaParser) throws IOException {
      return this.tryToParse(startPackage, filename, javaParser.getParserConfiguration());
   }

   public ParseResult<CompilationUnit> tryToParse(String startPackage, String filename, ParserConfiguration configuration) throws IOException {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(filename);
      Path relativePath = CodeGenerationUtils.fileInPackageRelativePath(startPackage, filename);
      if (this.cache.containsKey(relativePath)) {
         Log.trace("Retrieving cached %s", relativePath);
         return (ParseResult)this.cache.get(relativePath);
      } else {
         Path path = this.root.resolve(relativePath);
         Log.trace("Parsing %s", path);
         ParseResult<CompilationUnit> result = (new JavaParser(configuration)).parse(ParseStart.COMPILATION_UNIT, Providers.provider(path));
         result.getResult().ifPresent((cu) -> {
            cu.setStorage(path);
         });
         this.cache.put(relativePath, result);
         return result;
      }
   }

   public ParseResult<CompilationUnit> tryToParse(String startPackage, String filename) throws IOException {
      return this.tryToParse(startPackage, filename, this.parserConfiguration);
   }

   public List<ParseResult<CompilationUnit>> tryToParse(String startPackage) throws IOException {
      Utils.assertNotNull(startPackage);
      this.logPackage(startPackage);
      Path path = CodeGenerationUtils.packageAbsolutePath(this.root, startPackage);
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isDirectory() && file.toString().endsWith(".java")) {
               Path relative = SourceRoot.this.root.relativize(file.getParent());
               SourceRoot.this.tryToParse(relative.toString(), file.getFileName().toString());
            }

            return FileVisitResult.CONTINUE;
         }

         public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return SourceRoot.isSensibleDirectoryToEnter(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
         }
      });
      return this.getCache();
   }

   private static boolean isSensibleDirectoryToEnter(Path dir) throws IOException {
      String dirToEnter = dir.getFileName().toString();
      boolean directoryIsAValidJavaIdentifier = JAVA_IDENTIFIER.matcher(dirToEnter).matches();
      if (!Files.isHidden(dir) && directoryIsAValidJavaIdentifier) {
         return true;
      } else {
         Log.trace("Not processing directory \"%s\"", dirToEnter);
         return false;
      }
   }

   public List<ParseResult<CompilationUnit>> tryToParse() throws IOException {
      return this.tryToParse("");
   }

   public List<ParseResult<CompilationUnit>> tryToParseParallelized(String startPackage) {
      Utils.assertNotNull(startPackage);
      this.logPackage(startPackage);
      Path path = CodeGenerationUtils.packageAbsolutePath(this.root, startPackage);
      SourceRoot.ParallelParse parse = new SourceRoot.ParallelParse(path, (file, attrs) -> {
         if (!attrs.isDirectory() && file.toString().endsWith(".java")) {
            Path relative = this.root.relativize(file.getParent());

            try {
               this.tryToParse(relative.toString(), file.getFileName().toString(), this.parserConfiguration);
            } catch (IOException var5) {
               Log.error(var5);
            }
         }

         return FileVisitResult.CONTINUE;
      });
      ForkJoinPool pool = new ForkJoinPool();
      pool.invoke(parse);
      return this.getCache();
   }

   public List<ParseResult<CompilationUnit>> tryToParseParallelized() {
      return this.tryToParseParallelized("");
   }

   public CompilationUnit parse(String startPackage, String filename) {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(filename);

      try {
         ParseResult<CompilationUnit> result = this.tryToParse(startPackage, filename);
         if (result.isSuccessful()) {
            return (CompilationUnit)result.getResult().get();
         } else {
            throw new ParseProblemException(result.getProblems());
         }
      } catch (IOException var4) {
         throw new ParseProblemException(var4);
      }
   }

   private FileVisitResult callback(Path absolutePath, ParserConfiguration configuration, SourceRoot.Callback callback) throws IOException {
      Path localPath = this.root.relativize(absolutePath);
      Log.trace("Parsing %s", localPath);
      ParseResult<CompilationUnit> result = (new JavaParser(configuration)).parse(ParseStart.COMPILATION_UNIT, Providers.provider(absolutePath));
      result.getResult().ifPresent((cu) -> {
         cu.setStorage(absolutePath);
      });
      switch(callback.process(localPath, absolutePath, result)) {
      case SAVE:
         result.getResult().ifPresent((cu) -> {
            this.save(cu, absolutePath);
         });
      case DONT_SAVE:
         return FileVisitResult.CONTINUE;
      case TERMINATE:
         return FileVisitResult.TERMINATE;
      default:
         throw new AssertionError("Return an enum defined in SourceRoot.Callback.Result");
      }
   }

   public SourceRoot parse(String startPackage, String filename, ParserConfiguration configuration, SourceRoot.Callback callback) throws IOException {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(filename);
      Utils.assertNotNull(configuration);
      Utils.assertNotNull(callback);
      this.callback(CodeGenerationUtils.fileInPackageAbsolutePath(this.root, startPackage, filename), configuration, callback);
      return this;
   }

   public SourceRoot parse(String startPackage, String filename, SourceRoot.Callback callback) throws IOException {
      this.parse(startPackage, filename, this.parserConfiguration, callback);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public SourceRoot parse(String startPackage, JavaParser javaParser, SourceRoot.Callback callback) throws IOException {
      return this.parse(startPackage, javaParser.getParserConfiguration(), callback);
   }

   public SourceRoot parse(String startPackage, ParserConfiguration configuration, SourceRoot.Callback callback) throws IOException {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(configuration);
      Utils.assertNotNull(callback);
      this.logPackage(startPackage);
      Path path = CodeGenerationUtils.packageAbsolutePath(this.root, startPackage);
      if (Files.exists(path, new LinkOption[0])) {
         Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path absolutePath, BasicFileAttributes attrs) throws IOException {
               return !attrs.isDirectory() && absolutePath.toString().endsWith(".java") ? SourceRoot.this.callback(absolutePath, configuration, callback) : FileVisitResult.CONTINUE;
            }

            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
               return SourceRoot.isSensibleDirectoryToEnter(dir) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
            }
         });
      }

      return this;
   }

   public SourceRoot parse(String startPackage, SourceRoot.Callback callback) throws IOException {
      this.parse(startPackage, this.parserConfiguration, callback);
      return this;
   }

   private void logPackage(String startPackage) {
      if (!startPackage.isEmpty()) {
         Log.info("Parsing package \"%s\"", startPackage);
      }
   }

   public SourceRoot parseParallelized(String startPackage, ParserConfiguration configuration, SourceRoot.Callback callback) {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(configuration);
      Utils.assertNotNull(callback);
      this.logPackage(startPackage);
      Path path = CodeGenerationUtils.packageAbsolutePath(this.root, startPackage);
      if (Files.exists(path, new LinkOption[0])) {
         SourceRoot.ParallelParse parse = new SourceRoot.ParallelParse(path, (absolutePath, attrs) -> {
            if (!attrs.isDirectory() && absolutePath.toString().endsWith(".java")) {
               try {
                  return this.callback(absolutePath, configuration, callback);
               } catch (IOException var6) {
                  Log.error(var6);
               }
            }

            return FileVisitResult.CONTINUE;
         });
         ForkJoinPool pool = new ForkJoinPool();
         pool.invoke(parse);
      }

      return this;
   }

   public SourceRoot parseParallelized(String startPackage, SourceRoot.Callback callback) throws IOException {
      return this.parseParallelized(startPackage, new ParserConfiguration(), callback);
   }

   public SourceRoot parseParallelized(SourceRoot.Callback callback) throws IOException {
      return this.parseParallelized("", new ParserConfiguration(), callback);
   }

   public SourceRoot add(String startPackage, String filename, CompilationUnit compilationUnit) {
      Utils.assertNotNull(startPackage);
      Utils.assertNotNull(filename);
      Utils.assertNotNull(compilationUnit);
      Log.trace("Adding new file %s.%s", startPackage, filename);
      Path path = CodeGenerationUtils.fileInPackageRelativePath(startPackage, filename);
      ParseResult<CompilationUnit> parseResult = new ParseResult(compilationUnit, new ArrayList(), (List)null, (CommentsCollection)null);
      this.cache.put(path, parseResult);
      return this;
   }

   public SourceRoot add(CompilationUnit compilationUnit) {
      Utils.assertNotNull(compilationUnit);
      if (compilationUnit.getStorage().isPresent()) {
         Path path = ((CompilationUnit.Storage)compilationUnit.getStorage().get()).getPath();
         Log.trace("Adding new file %s", path);
         ParseResult<CompilationUnit> parseResult = new ParseResult(compilationUnit, new ArrayList(), (List)null, (CommentsCollection)null);
         this.cache.put(path, parseResult);
         return this;
      } else {
         throw new AssertionError("Files added with this method should have their path set.");
      }
   }

   private SourceRoot save(CompilationUnit cu, Path path) {
      Utils.assertNotNull(cu);
      Utils.assertNotNull(path);
      cu.setStorage(path);
      ((CompilationUnit.Storage)cu.getStorage().get()).save(this.printer);
      return this;
   }

   public SourceRoot saveAll(Path root) {
      Utils.assertNotNull(root);
      Log.info("Saving all files (%s) to %s", this.cache.size(), root);
      Iterator var2 = this.cache.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<Path, ParseResult<CompilationUnit>> cu = (Entry)var2.next();
         Path path = root.resolve((Path)cu.getKey());
         if (((ParseResult)cu.getValue()).getResult().isPresent()) {
            Log.trace("Saving %s", path);
            this.save((CompilationUnit)((ParseResult)cu.getValue()).getResult().get(), path);
         }
      }

      return this;
   }

   public SourceRoot saveAll() {
      return this.saveAll(this.root);
   }

   public List<ParseResult<CompilationUnit>> getCache() {
      return new ArrayList(this.cache.values());
   }

   public List<CompilationUnit> getCompilationUnits() {
      return (List)this.cache.values().stream().filter(ParseResult::isSuccessful).map((p) -> {
         return (CompilationUnit)p.getResult().get();
      }).collect(Collectors.toList());
   }

   public Path getRoot() {
      return this.root;
   }

   /** @deprecated */
   @Deprecated
   public JavaParser getJavaParser() {
      return new JavaParser(this.parserConfiguration);
   }

   /** @deprecated */
   @Deprecated
   public SourceRoot setJavaParser(JavaParser javaParser) {
      Utils.assertNotNull(javaParser);
      this.parserConfiguration = javaParser.getParserConfiguration();
      return this;
   }

   public ParserConfiguration getParserConfiguration() {
      return this.parserConfiguration;
   }

   public SourceRoot setParserConfiguration(ParserConfiguration parserConfiguration) {
      Utils.assertNotNull(parserConfiguration);
      this.parserConfiguration = parserConfiguration;
      return this;
   }

   public SourceRoot setPrinter(Function<CompilationUnit, String> printer) {
      Utils.assertNotNull(printer);
      this.printer = printer;
      return this;
   }

   public Function<CompilationUnit, String> getPrinter() {
      return this.printer;
   }

   public String toString() {
      return "SourceRoot at " + this.root;
   }

   private static class ParallelParse extends RecursiveAction {
      private static final long serialVersionUID = 1L;
      private final Path path;
      private final SourceRoot.ParallelParse.VisitFileCallback callback;

      ParallelParse(Path path, SourceRoot.ParallelParse.VisitFileCallback callback) {
         this.path = path;
         this.callback = callback;
      }

      protected void compute() {
         final ArrayList walks = new ArrayList();

         try {
            Files.walkFileTree(this.path, new SimpleFileVisitor<Path>() {
               public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                  if (!SourceRoot.isSensibleDirectoryToEnter(dir)) {
                     return FileVisitResult.SKIP_SUBTREE;
                  } else if (!dir.equals(ParallelParse.this.path)) {
                     SourceRoot.ParallelParse w = new SourceRoot.ParallelParse(dir, ParallelParse.this.callback);
                     w.fork();
                     walks.add(w);
                     return FileVisitResult.SKIP_SUBTREE;
                  } else {
                     return FileVisitResult.CONTINUE;
                  }
               }

               public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                  return ParallelParse.this.callback.process(file, attrs);
               }
            });
         } catch (IOException var4) {
            Log.error(var4);
         }

         Iterator var2 = walks.iterator();

         while(var2.hasNext()) {
            SourceRoot.ParallelParse w = (SourceRoot.ParallelParse)var2.next();
            w.join();
         }

      }

      interface VisitFileCallback {
         FileVisitResult process(Path file, BasicFileAttributes attrs);
      }
   }

   @FunctionalInterface
   public interface Callback {
      SourceRoot.Callback.Result process(Path localPath, Path absolutePath, ParseResult<CompilationUnit> result);

      public static enum Result {
         SAVE,
         DONT_SAVE,
         TERMINATE;
      }
   }
}
