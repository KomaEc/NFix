package com.github.javaparser.ast;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Providers;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.CompilationUnitMetaModel;
import com.github.javaparser.metamodel.InternalProperty;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.utils.ClassUtils;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CompilationUnit extends Node {
   @OptionalProperty
   private PackageDeclaration packageDeclaration;
   private NodeList<ImportDeclaration> imports;
   private NodeList<TypeDeclaration<?>> types;
   @OptionalProperty
   private ModuleDeclaration module;
   @InternalProperty
   private CompilationUnit.Storage storage;

   public CompilationUnit() {
      this((TokenRange)null, (PackageDeclaration)null, new NodeList(), new NodeList(), (ModuleDeclaration)null);
   }

   public CompilationUnit(String packageDeclaration) {
      this((TokenRange)null, new PackageDeclaration(new Name(packageDeclaration)), new NodeList(), new NodeList(), (ModuleDeclaration)null);
   }

   @AllFieldsConstructor
   public CompilationUnit(PackageDeclaration packageDeclaration, NodeList<ImportDeclaration> imports, NodeList<TypeDeclaration<?>> types, ModuleDeclaration module) {
      this((TokenRange)null, packageDeclaration, imports, types, module);
   }

   public CompilationUnit(TokenRange tokenRange, PackageDeclaration packageDeclaration, NodeList<ImportDeclaration> imports, NodeList<TypeDeclaration<?>> types, ModuleDeclaration module) {
      super(tokenRange);
      this.setPackageDeclaration(packageDeclaration);
      this.setImports(imports);
      this.setTypes(types);
      this.setModule(module);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public List<Comment> getComments() {
      return this.getAllContainedComments();
   }

   public NodeList<ImportDeclaration> getImports() {
      return this.imports;
   }

   public ImportDeclaration getImport(int i) {
      return (ImportDeclaration)this.getImports().get(i);
   }

   public Optional<PackageDeclaration> getPackageDeclaration() {
      return Optional.ofNullable(this.packageDeclaration);
   }

   public NodeList<TypeDeclaration<?>> getTypes() {
      return this.types;
   }

   public TypeDeclaration<?> getType(int i) {
      return (TypeDeclaration)this.getTypes().get(i);
   }

   public CompilationUnit setImports(final NodeList<ImportDeclaration> imports) {
      Utils.assertNotNull(imports);
      if (imports == this.imports) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.IMPORTS, this.imports, imports);
         if (this.imports != null) {
            this.imports.setParentNode((Node)null);
         }

         this.imports = imports;
         this.setAsParentNodeOf(imports);
         return this;
      }
   }

   public CompilationUnit setImport(int i, ImportDeclaration imports) {
      this.getImports().set(i, (Node)imports);
      return this;
   }

   public CompilationUnit addImport(ImportDeclaration imports) {
      this.getImports().add((Node)imports);
      return this;
   }

   public CompilationUnit setPackageDeclaration(final PackageDeclaration packageDeclaration) {
      if (packageDeclaration == this.packageDeclaration) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.PACKAGE_DECLARATION, this.packageDeclaration, packageDeclaration);
         if (this.packageDeclaration != null) {
            this.packageDeclaration.setParentNode((Node)null);
         }

         this.packageDeclaration = packageDeclaration;
         this.setAsParentNodeOf(packageDeclaration);
         return this;
      }
   }

   public CompilationUnit setTypes(final NodeList<TypeDeclaration<?>> types) {
      Utils.assertNotNull(types);
      if (types == this.types) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPES, this.types, types);
         if (this.types != null) {
            this.types.setParentNode((Node)null);
         }

         this.types = types;
         this.setAsParentNodeOf(types);
         return this;
      }
   }

   public CompilationUnit setType(int i, TypeDeclaration<?> type) {
      NodeList<TypeDeclaration<?>> copy = new NodeList();
      copy.addAll(this.getTypes());
      this.getTypes().set(i, (Node)type);
      this.notifyPropertyChange(ObservableProperty.TYPES, copy, this.types);
      return this;
   }

   public CompilationUnit addType(TypeDeclaration<?> type) {
      NodeList<TypeDeclaration<?>> copy = new NodeList();
      copy.addAll(this.getTypes());
      this.getTypes().add((Node)type);
      this.notifyPropertyChange(ObservableProperty.TYPES, copy, this.types);
      return this;
   }

   public CompilationUnit setPackageDeclaration(String name) {
      this.setPackageDeclaration(new PackageDeclaration(JavaParser.parseName(name)));
      return this;
   }

   public CompilationUnit addImport(String name) {
      return this.addImport(name, false, false);
   }

   public CompilationUnit addImport(Class<?> clazz) {
      if (clazz.isArray()) {
         return this.addImport(clazz.getComponentType());
      } else if (!ClassUtils.isPrimitiveOrWrapper(clazz) && !clazz.getName().startsWith("java.lang")) {
         if (clazz.isMemberClass()) {
            return this.addImport(clazz.getName().replace("$", "."));
         } else if (!clazz.isAnonymousClass() && !clazz.isLocalClass()) {
            return this.addImport(clazz.getName());
         } else {
            throw new RuntimeException(clazz.getName() + " is an anonymous or local class therefore it can't be added with addImport");
         }
      } else {
         return this;
      }
   }

   public CompilationUnit addImport(String name, boolean isStatic, boolean isAsterisk) {
      StringBuilder i = new StringBuilder("import ");
      if (isStatic) {
         i.append("static ");
      }

      i.append(name);
      if (isAsterisk) {
         i.append(".*");
      }

      i.append(";");
      ImportDeclaration importDeclaration = JavaParser.parseImport(i.toString());
      if (this.getImports().stream().anyMatch((im) -> {
         return im.toString().equals(importDeclaration.toString());
      })) {
         return this;
      } else {
         this.getImports().add((Node)importDeclaration);
         return this;
      }
   }

   public ClassOrInterfaceDeclaration addClass(String name) {
      return this.addClass(name, Modifier.PUBLIC);
   }

   public ClassOrInterfaceDeclaration addClass(String name, Modifier... modifiers) {
      ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), false, name);
      this.getTypes().add((Node)classOrInterfaceDeclaration);
      return classOrInterfaceDeclaration;
   }

   public ClassOrInterfaceDeclaration addInterface(String name) {
      return this.addInterface(name, Modifier.PUBLIC);
   }

   public ClassOrInterfaceDeclaration addInterface(String name, Modifier... modifiers) {
      ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), true, name);
      this.getTypes().add((Node)classOrInterfaceDeclaration);
      return classOrInterfaceDeclaration;
   }

   public EnumDeclaration addEnum(String name) {
      return this.addEnum(name, Modifier.PUBLIC);
   }

   public EnumDeclaration addEnum(String name, Modifier... modifiers) {
      EnumDeclaration enumDeclaration = new EnumDeclaration((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), name);
      this.getTypes().add((Node)enumDeclaration);
      return enumDeclaration;
   }

   public AnnotationDeclaration addAnnotationDeclaration(String name) {
      return this.addAnnotationDeclaration(name, Modifier.PUBLIC);
   }

   public AnnotationDeclaration addAnnotationDeclaration(String name, Modifier... modifiers) {
      AnnotationDeclaration annotationDeclaration = new AnnotationDeclaration((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })), name);
      this.getTypes().add((Node)annotationDeclaration);
      return annotationDeclaration;
   }

   public Optional<ClassOrInterfaceDeclaration> getClassByName(String className) {
      return this.getTypes().stream().filter((type) -> {
         return type.getNameAsString().equals(className) && type instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)type).isInterface();
      }).findFirst().map((t) -> {
         return (ClassOrInterfaceDeclaration)t;
      });
   }

   public Optional<ClassOrInterfaceDeclaration> getInterfaceByName(String interfaceName) {
      return this.getTypes().stream().filter((type) -> {
         return type.getNameAsString().equals(interfaceName) && type instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration)type).isInterface();
      }).findFirst().map((t) -> {
         return (ClassOrInterfaceDeclaration)t;
      });
   }

   public Optional<EnumDeclaration> getEnumByName(String enumName) {
      return this.getTypes().stream().filter((type) -> {
         return type.getNameAsString().equals(enumName) && type instanceof EnumDeclaration;
      }).findFirst().map((t) -> {
         return (EnumDeclaration)t;
      });
   }

   public Optional<String> getPrimaryTypeName() {
      return this.getStorage().map(CompilationUnit.Storage::getFileName).map(Utils::removeFileExtension);
   }

   public Optional<TypeDeclaration<?>> getPrimaryType() {
      return this.getPrimaryTypeName().flatMap((name) -> {
         return this.getTypes().stream().filter((t) -> {
            return t.getNameAsString().equals(name);
         }).findFirst();
      });
   }

   public Optional<AnnotationDeclaration> getAnnotationDeclarationByName(String annotationName) {
      return this.getTypes().stream().filter((type) -> {
         return type.getNameAsString().equals(annotationName) && type instanceof AnnotationDeclaration;
      }).findFirst().map((t) -> {
         return (AnnotationDeclaration)t;
      });
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.imports.size(); ++i) {
            if (this.imports.get(i) == node) {
               this.imports.remove(i);
               return true;
            }
         }

         if (this.module != null && node == this.module) {
            this.removeModule();
            return true;
         } else if (this.packageDeclaration != null && node == this.packageDeclaration) {
            this.removePackageDeclaration();
            return true;
         } else {
            for(i = 0; i < this.types.size(); ++i) {
               if (this.types.get(i) == node) {
                  this.types.remove(i);
                  return true;
               }
            }

            return super.remove(node);
         }
      }
   }

   public CompilationUnit removePackageDeclaration() {
      return this.setPackageDeclaration((PackageDeclaration)null);
   }

   public Optional<ModuleDeclaration> getModule() {
      return Optional.ofNullable(this.module);
   }

   public CompilationUnit setModule(final ModuleDeclaration module) {
      if (module == this.module) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODULE, this.module, module);
         if (this.module != null) {
            this.module.setParentNode((Node)null);
         }

         this.module = module;
         this.setAsParentNodeOf(module);
         return this;
      }
   }

   public CompilationUnit removeModule() {
      return this.setModule((ModuleDeclaration)null);
   }

   public Optional<CompilationUnit.Storage> getStorage() {
      return Optional.ofNullable(this.storage);
   }

   public CompilationUnit setStorage(Path path) {
      this.storage = new CompilationUnit.Storage(this, path);
      return this;
   }

   public ModuleDeclaration setModule(String name) {
      ModuleDeclaration module = new ModuleDeclaration(JavaParser.parseName(name), false);
      this.setModule(module);
      return module;
   }

   public CompilationUnit clone() {
      return (CompilationUnit)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public CompilationUnitMetaModel getMetaModel() {
      return JavaParserMetaModel.compilationUnitMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         int i;
         for(i = 0; i < this.imports.size(); ++i) {
            if (this.imports.get(i) == node) {
               this.imports.set(i, (Node)((ImportDeclaration)replacementNode));
               return true;
            }
         }

         if (this.module != null && node == this.module) {
            this.setModule((ModuleDeclaration)replacementNode);
            return true;
         } else if (this.packageDeclaration != null && node == this.packageDeclaration) {
            this.setPackageDeclaration((PackageDeclaration)replacementNode);
            return true;
         } else {
            for(i = 0; i < this.types.size(); ++i) {
               if (this.types.get(i) == node) {
                  this.types.set(i, (Node)((TypeDeclaration)replacementNode));
                  return true;
               }
            }

            return super.replace(node, replacementNode);
         }
      }
   }

   public static class Storage {
      private final CompilationUnit compilationUnit;
      private final Path path;

      private Storage(CompilationUnit compilationUnit, Path path) {
         this.compilationUnit = compilationUnit;
         this.path = path.toAbsolutePath();
      }

      public Path getPath() {
         return this.path;
      }

      public CompilationUnit getCompilationUnit() {
         return this.compilationUnit;
      }

      public Path getSourceRoot() {
         Optional<String> pkgAsString = this.compilationUnit.getPackageDeclaration().map(NodeWithName::getNameAsString);
         return (Path)pkgAsString.map((p) -> {
            return Paths.get(CodeGenerationUtils.packageToPath(p));
         }).map((pkg) -> {
            return CodeGenerationUtils.subtractPaths(this.getDirectory(), pkg);
         }).orElse(this.getDirectory());
      }

      public String getFileName() {
         return this.path.getFileName().toString();
      }

      public Path getDirectory() {
         return this.path.getParent();
      }

      public void save() {
         this.save((cu) -> {
            return (new PrettyPrinter()).print(cu);
         });
      }

      public void save(Function<CompilationUnit, String> makeOutput) {
         try {
            Files.createDirectories(this.path.getParent());
            String code = (String)makeOutput.apply(this.getCompilationUnit());
            Files.write(this.path, code.getBytes(Providers.UTF8), new OpenOption[0]);
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      public ParseResult<CompilationUnit> reparse(JavaParser javaParser) {
         try {
            return javaParser.parse(ParseStart.COMPILATION_UNIT, Providers.provider(this.getPath()));
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      // $FF: synthetic method
      Storage(CompilationUnit x0, Path x1, Object x2) {
         this(x0, x1);
      }
   }
}
