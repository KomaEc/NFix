package com.github.javaparser.symbolsolver.resolution.typesolvers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class JavaParserTypeSolver implements TypeSolver {
   private final Path srcDir;
   private final JavaParser javaParser;
   private TypeSolver parent;
   private final Cache<Path, Optional<CompilationUnit>> parsedFiles;
   private final Cache<Path, List<CompilationUnit>> parsedDirectories;
   private final Cache<String, SymbolReference<ResolvedReferenceTypeDeclaration>> foundTypes;

   public JavaParserTypeSolver(File srcDir) {
      this(srcDir.toPath());
   }

   public JavaParserTypeSolver(String srcDir) {
      this(new File(srcDir));
   }

   public JavaParserTypeSolver(File srcDir, ParserConfiguration parserConfiguration) {
      this(srcDir.toPath(), parserConfiguration);
   }

   public JavaParserTypeSolver(String srcDir, ParserConfiguration parserConfiguration) {
      this(new File(srcDir), parserConfiguration);
   }

   public JavaParserTypeSolver(Path srcDir, ParserConfiguration parserConfiguration) {
      this.parsedFiles = CacheBuilder.newBuilder().softValues().build();
      this.parsedDirectories = CacheBuilder.newBuilder().softValues().build();
      this.foundTypes = CacheBuilder.newBuilder().softValues().build();
      if (Files.exists(srcDir, new LinkOption[0]) && Files.isDirectory(srcDir, new LinkOption[0])) {
         this.srcDir = srcDir;
         this.javaParser = new JavaParser(parserConfiguration);
      } else {
         throw new IllegalStateException("SrcDir does not exist or is not a directory: " + srcDir);
      }
   }

   public JavaParserTypeSolver(Path srcDir) {
      this(srcDir, (new ParserConfiguration()).setLanguageLevel(ParserConfiguration.LanguageLevel.BLEEDING_EDGE));
   }

   public String toString() {
      return "JavaParserTypeSolver{srcDir=" + this.srcDir + ", parent=" + this.parent + '}';
   }

   public TypeSolver getParent() {
      return this.parent;
   }

   public void setParent(TypeSolver parent) {
      this.parent = parent;
   }

   private Optional<CompilationUnit> parse(Path srcFile) {
      try {
         return (Optional)this.parsedFiles.get(srcFile.toAbsolutePath(), () -> {
            try {
               return !Files.exists(srcFile, new LinkOption[0]) ? Optional.empty() : this.javaParser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(srcFile)).getResult().map((cu) -> {
                  return cu.setStorage(srcFile);
               });
            } catch (FileNotFoundException var3) {
               throw new RuntimeException("Issue while parsing while type solving: " + srcFile.toAbsolutePath(), var3);
            }
         });
      } catch (ExecutionException var3) {
         throw new RuntimeException(var3);
      }
   }

   private List<CompilationUnit> parseDirectory(Path srcDirectory) {
      return this.parseDirectory(srcDirectory, false);
   }

   private List<CompilationUnit> parseDirectoryRecursively(Path srcDirectory) {
      return this.parseDirectory(srcDirectory, true);
   }

   private List<CompilationUnit> parseDirectory(Path srcDirectory, boolean recursively) {
      try {
         return (List)this.parsedDirectories.get(srcDirectory.toAbsolutePath(), () -> {
            List<CompilationUnit> units = new ArrayList();
            if (Files.exists(srcDirectory, new LinkOption[0])) {
               DirectoryStream<Path> srcDirectoryStream = Files.newDirectoryStream(srcDirectory);
               Throwable var5 = null;

               try {
                  srcDirectoryStream.forEach((file) -> {
                     if (file.getFileName().toString().toLowerCase().endsWith(".java")) {
                        this.parse(file).ifPresent(units::add);
                     } else if (recursively && file.toFile().isDirectory()) {
                        units.addAll(this.parseDirectoryRecursively(file));
                     }

                  });
               } catch (Throwable var14) {
                  var5 = var14;
                  throw var14;
               } finally {
                  if (srcDirectoryStream != null) {
                     if (var5 != null) {
                        try {
                           srcDirectoryStream.close();
                        } catch (Throwable var13) {
                           var5.addSuppressed(var13);
                        }
                     } else {
                        srcDirectoryStream.close();
                     }
                  }

               }
            }

            return units;
         });
      } catch (ExecutionException var4) {
         throw new RuntimeException(var4);
      }
   }

   public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
      try {
         return (SymbolReference)this.foundTypes.get(name, () -> {
            SymbolReference<ResolvedReferenceTypeDeclaration> result = this.tryToSolveTypeUncached(name);
            return result.isSolved() ? SymbolReference.solved(result.getCorrespondingDeclaration()) : result;
         });
      } catch (ExecutionException var3) {
         throw new RuntimeException(var3);
      }
   }

   private SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveTypeUncached(String name) {
      String[] nameElements = name.split("\\.");

      for(int i = nameElements.length; i > 0; --i) {
         StringBuilder filePath = new StringBuilder(this.srcDir.toAbsolutePath().toString());

         for(int j = 0; j < i; ++j) {
            filePath.append("/").append(nameElements[j]);
         }

         filePath.append(".java");
         StringBuilder typeName = new StringBuilder();

         for(int j = i - 1; j < nameElements.length; ++j) {
            if (j != i - 1) {
               typeName.append(".");
            }

            typeName.append(nameElements[j]);
         }

         Path srcFile = Paths.get(filePath.toString());
         Optional<CompilationUnit> compilationUnit = this.parse(srcFile);
         if (compilationUnit.isPresent()) {
            Optional<TypeDeclaration<?>> astTypeDeclaration = Navigator.findType((CompilationUnit)compilationUnit.get(), typeName.toString());
            if (astTypeDeclaration.isPresent()) {
               return SymbolReference.solved(JavaParserFacade.get(this).getTypeDeclaration((TypeDeclaration)astTypeDeclaration.get()));
            }
         }

         List<CompilationUnit> compilationUnits = this.parseDirectory(srcFile.getParent());
         Iterator var14 = compilationUnits.iterator();

         while(var14.hasNext()) {
            CompilationUnit compilationUnit = (CompilationUnit)var14.next();
            Optional<TypeDeclaration<?>> astTypeDeclaration = Navigator.findType(compilationUnit, typeName.toString());
            if (astTypeDeclaration.isPresent()) {
               return SymbolReference.solved(JavaParserFacade.get(this).getTypeDeclaration((TypeDeclaration)astTypeDeclaration.get()));
            }
         }
      }

      return SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
   }
}
