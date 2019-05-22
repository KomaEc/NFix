package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;

public final class JavaParser {
   private final ParserConfiguration configuration;
   private GeneratedJavaParser astParser;
   private static ParserConfiguration staticConfiguration = new ParserConfiguration();

   public JavaParser() {
      this(new ParserConfiguration());
   }

   public JavaParser(ParserConfiguration configuration) {
      this.astParser = null;
      this.configuration = configuration;
   }

   public static ParserConfiguration getStaticConfiguration() {
      return staticConfiguration;
   }

   public static void setStaticConfiguration(ParserConfiguration staticConfiguration) {
      JavaParser.staticConfiguration = staticConfiguration;
   }

   public ParserConfiguration getParserConfiguration() {
      return this.configuration;
   }

   private GeneratedJavaParser getParserForProvider(Provider provider) {
      if (this.astParser == null) {
         this.astParser = new GeneratedJavaParser(provider);
      } else {
         this.astParser.reset(provider);
      }

      this.astParser.setTabSize(this.configuration.getTabSize());
      this.astParser.setStoreTokens(this.configuration.isStoreTokens());
      return this.astParser;
   }

   public <N extends Node> ParseResult<N> parse(ParseStart<N> start, Provider provider) {
      Utils.assertNotNull(start);
      Utils.assertNotNull(provider);
      GeneratedJavaParser parser = this.getParserForProvider(provider);

      ParseResult var6;
      try {
         N resultNode = (Node)start.parse(parser);
         ParseResult<N> result = new ParseResult(resultNode, parser.problems, parser.getTokens(), parser.getCommentsCollection());
         this.configuration.getPostProcessors().forEach((postProcessor) -> {
            postProcessor.process(result, this.configuration);
         });
         result.getProblems().sort(Problem.PROBLEM_BY_BEGIN_POSITION);
         var6 = result;
         return var6;
      } catch (Exception var16) {
         String message = var16.getMessage() == null ? "Unknown error" : var16.getMessage();
         parser.problems.add(new Problem(message, (TokenRange)null, var16));
         var6 = new ParseResult((Object)null, parser.problems, parser.getTokens(), parser.getCommentsCollection());
      } finally {
         try {
            provider.close();
         } catch (IOException var15) {
         }

      }

      return var6;
   }

   public static CompilationUnit parse(final InputStream in, Charset encoding) {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(in, encoding));
   }

   public static CompilationUnit parse(final InputStream in) {
      return parse(in, Providers.UTF8);
   }

   public static CompilationUnit parse(final File file, final Charset encoding) throws FileNotFoundException {
      return ((CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(file, encoding))).setStorage(file.toPath());
   }

   public static CompilationUnit parse(final File file) throws FileNotFoundException {
      return ((CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(file))).setStorage(file.toPath());
   }

   public static CompilationUnit parse(final Path path, final Charset encoding) throws IOException {
      return ((CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(path, encoding))).setStorage(path);
   }

   public static CompilationUnit parse(final Path path) throws IOException {
      return ((CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(path))).setStorage(path);
   }

   public static CompilationUnit parseResource(final String path) throws IOException {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.resourceProvider(path));
   }

   public static CompilationUnit parseResource(final String path, Charset encoding) throws IOException {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.resourceProvider(path, encoding));
   }

   public static CompilationUnit parseResource(final ClassLoader classLoader, final String path, Charset encoding) throws IOException {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.resourceProvider(classLoader, path, encoding));
   }

   public static CompilationUnit parse(final Reader reader) {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(reader));
   }

   public static CompilationUnit parse(String code) {
      return (CompilationUnit)simplifiedParse(ParseStart.COMPILATION_UNIT, Providers.provider(code));
   }

   public static BlockStmt parseBlock(final String blockStatement) {
      return (BlockStmt)simplifiedParse(ParseStart.BLOCK, Providers.provider(blockStatement));
   }

   public static Statement parseStatement(final String statement) {
      return (Statement)simplifiedParse(ParseStart.STATEMENT, Providers.provider(statement));
   }

   private static <T extends Node> T simplifiedParse(ParseStart<T> context, Provider provider) {
      ParseResult<T> result = (new JavaParser(staticConfiguration)).parse(context, provider);
      if (result.isSuccessful()) {
         return (Node)result.getResult().get();
      } else {
         throw new ParseProblemException(result.getProblems());
      }
   }

   public static ImportDeclaration parseImport(final String importDeclaration) {
      return (ImportDeclaration)simplifiedParse(ParseStart.IMPORT_DECLARATION, Providers.provider(importDeclaration));
   }

   public static <T extends Expression> T parseExpression(final String expression) {
      return (Expression)simplifiedParse(ParseStart.EXPRESSION, Providers.provider(expression));
   }

   public static AnnotationExpr parseAnnotation(final String annotation) {
      return (AnnotationExpr)simplifiedParse(ParseStart.ANNOTATION, Providers.provider(annotation));
   }

   public static BodyDeclaration<?> parseAnnotationBodyDeclaration(final String body) {
      return (BodyDeclaration)simplifiedParse(ParseStart.ANNOTATION_BODY, Providers.provider(body));
   }

   /** @deprecated */
   @Deprecated
   public static BodyDeclaration<?> parseClassBodyDeclaration(String body) {
      return parseBodyDeclaration(body);
   }

   /** @deprecated */
   @Deprecated
   public static BodyDeclaration<?> parseInterfaceBodyDeclaration(String body) {
      return parseBodyDeclaration(body);
   }

   public static BodyDeclaration<?> parseBodyDeclaration(String body) {
      return (BodyDeclaration)simplifiedParse(ParseStart.CLASS_BODY, Providers.provider(body));
   }

   public static ClassOrInterfaceType parseClassOrInterfaceType(String type) {
      return (ClassOrInterfaceType)simplifiedParse(ParseStart.CLASS_OR_INTERFACE_TYPE, Providers.provider(type));
   }

   public static Type parseType(String type) {
      return (Type)simplifiedParse(ParseStart.TYPE, Providers.provider(type));
   }

   public static VariableDeclarationExpr parseVariableDeclarationExpr(String declaration) {
      return (VariableDeclarationExpr)simplifiedParse(ParseStart.VARIABLE_DECLARATION_EXPR, Providers.provider(declaration));
   }

   public static Javadoc parseJavadoc(String content) {
      return JavadocParser.parse(content);
   }

   public static ExplicitConstructorInvocationStmt parseExplicitConstructorInvocationStmt(String statement) {
      return (ExplicitConstructorInvocationStmt)simplifiedParse(ParseStart.EXPLICIT_CONSTRUCTOR_INVOCATION_STMT, Providers.provider(statement));
   }

   public static Name parseName(String qualifiedName) {
      return (Name)simplifiedParse(ParseStart.NAME, Providers.provider(qualifiedName));
   }

   public static SimpleName parseSimpleName(String name) {
      return (SimpleName)simplifiedParse(ParseStart.SIMPLE_NAME, Providers.provider(name));
   }

   public static Parameter parseParameter(String parameter) {
      return (Parameter)simplifiedParse(ParseStart.PARAMETER, Providers.provider(parameter));
   }

   public static PackageDeclaration parsePackageDeclaration(String packageDeclaration) {
      return (PackageDeclaration)simplifiedParse(ParseStart.PACKAGE_DECLARATION, Providers.provider(packageDeclaration));
   }

   public static TypeDeclaration<?> parseTypeDeclaration(String typeDeclaration) {
      return (TypeDeclaration)simplifiedParse(ParseStart.TYPE_DECLARATION, Providers.provider(typeDeclaration));
   }

   public static ModuleDeclaration parseModuleDeclaration(String moduleDeclaration) {
      return (ModuleDeclaration)simplifiedParse(ParseStart.MODULE_DECLARATION, Providers.provider(moduleDeclaration));
   }

   public static ModuleStmt parseModuleDirective(String moduleDirective) {
      return (ModuleStmt)simplifiedParse(ParseStart.MODULE_DIRECTIVE, Providers.provider(moduleDirective));
   }

   public static TypeParameter parseTypeParameter(String typeParameter) {
      return (TypeParameter)simplifiedParse(ParseStart.TYPE_PARAMETER, Providers.provider(typeParameter));
   }
}
