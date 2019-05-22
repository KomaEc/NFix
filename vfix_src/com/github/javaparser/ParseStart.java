package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
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

@FunctionalInterface
public interface ParseStart<R> {
   ParseStart<CompilationUnit> COMPILATION_UNIT = GeneratedJavaParser::CompilationUnit;
   ParseStart<BlockStmt> BLOCK = GeneratedJavaParser::BlockParseStart;
   ParseStart<Statement> STATEMENT = GeneratedJavaParser::BlockStatementParseStart;
   ParseStart<ImportDeclaration> IMPORT_DECLARATION = GeneratedJavaParser::ImportDeclarationParseStart;
   ParseStart<Expression> EXPRESSION = GeneratedJavaParser::ExpressionParseStart;
   ParseStart<AnnotationExpr> ANNOTATION = GeneratedJavaParser::AnnotationParseStart;
   ParseStart<BodyDeclaration<?>> ANNOTATION_BODY = GeneratedJavaParser::AnnotationBodyDeclarationParseStart;
   ParseStart<BodyDeclaration<?>> CLASS_BODY = GeneratedJavaParser::ClassOrInterfaceBodyDeclarationParseStart;
   ParseStart<ClassOrInterfaceType> CLASS_OR_INTERFACE_TYPE = GeneratedJavaParser::ClassOrInterfaceTypeParseStart;
   ParseStart<Type> TYPE = GeneratedJavaParser::ResultTypeParseStart;
   ParseStart<TypeParameter> TYPE_PARAMETER = GeneratedJavaParser::TypeParameterParseStart;
   ParseStart<VariableDeclarationExpr> VARIABLE_DECLARATION_EXPR = GeneratedJavaParser::VariableDeclarationExpressionParseStart;
   ParseStart<ExplicitConstructorInvocationStmt> EXPLICIT_CONSTRUCTOR_INVOCATION_STMT = GeneratedJavaParser::ExplicitConstructorInvocationParseStart;
   ParseStart<Name> NAME = GeneratedJavaParser::NameParseStart;
   ParseStart<SimpleName> SIMPLE_NAME = GeneratedJavaParser::SimpleNameParseStart;
   ParseStart<Parameter> PARAMETER = GeneratedJavaParser::ParameterParseStart;
   ParseStart<PackageDeclaration> PACKAGE_DECLARATION = GeneratedJavaParser::PackageDeclarationParseStart;
   ParseStart<TypeDeclaration<?>> TYPE_DECLARATION = GeneratedJavaParser::TypeDeclarationParseStart;
   ParseStart<ModuleDeclaration> MODULE_DECLARATION = GeneratedJavaParser::ModuleDeclarationParseStart;
   ParseStart<ModuleStmt> MODULE_DIRECTIVE = GeneratedJavaParser::ModuleDirectiveParseStart;

   R parse(GeneratedJavaParser parser) throws ParseException;
}
