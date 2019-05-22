package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NodeWithMembers<N extends Node> {
   NodeList<BodyDeclaration<?>> getMembers();

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default BodyDeclaration<?> getMember(int i) {
      return (BodyDeclaration)this.getMembers().get(i);
   }

   default N setMember(int i, BodyDeclaration<?> member) {
      this.getMembers().set(i, (Node)member);
      return (Node)this;
   }

   default N addMember(BodyDeclaration<?> member) {
      this.getMembers().add((Node)member);
      return (Node)this;
   }

   N setMembers(NodeList<BodyDeclaration<?>> members);

   default FieldDeclaration addField(Class<?> typeClass, String name, Modifier... modifiers) {
      this.tryAddImportToParentCompilationUnit(typeClass);
      return this.addField(typeClass.getSimpleName(), name, modifiers);
   }

   default FieldDeclaration addField(String type, String name, Modifier... modifiers) {
      return this.addField(JavaParser.parseType(type), name, modifiers);
   }

   default FieldDeclaration addField(Type type, String name, Modifier... modifiers) {
      FieldDeclaration fieldDeclaration = new FieldDeclaration();
      VariableDeclarator variable = new VariableDeclarator(type, name);
      fieldDeclaration.getVariables().add((Node)variable);
      fieldDeclaration.setModifiers((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })));
      this.getMembers().add((Node)fieldDeclaration);
      return fieldDeclaration;
   }

   default FieldDeclaration addFieldWithInitializer(Class<?> typeClass, String name, Expression initializer, Modifier... modifiers) {
      this.tryAddImportToParentCompilationUnit(typeClass);
      return this.addFieldWithInitializer(typeClass.getSimpleName(), name, initializer, modifiers);
   }

   default FieldDeclaration addFieldWithInitializer(String type, String name, Expression initializer, Modifier... modifiers) {
      return this.addFieldWithInitializer(JavaParser.parseType(type), name, initializer, modifiers);
   }

   default FieldDeclaration addFieldWithInitializer(Type type, String name, Expression initializer, Modifier... modifiers) {
      FieldDeclaration declaration = this.addField(type, name, modifiers);
      ((VariableDeclarator)declaration.getVariables().iterator().next()).setInitializer(initializer);
      return declaration;
   }

   default FieldDeclaration addPrivateField(Class<?> typeClass, String name) {
      return this.addField(typeClass, name, Modifier.PRIVATE);
   }

   default FieldDeclaration addPrivateField(String type, String name) {
      return this.addField(type, name, Modifier.PRIVATE);
   }

   default FieldDeclaration addPrivateField(Type type, String name) {
      return this.addField(type, name, Modifier.PRIVATE);
   }

   default FieldDeclaration addPublicField(Class<?> typeClass, String name) {
      return this.addField(typeClass, name, Modifier.PUBLIC);
   }

   default FieldDeclaration addPublicField(String type, String name) {
      return this.addField(type, name, Modifier.PUBLIC);
   }

   default FieldDeclaration addPublicField(Type type, String name) {
      return this.addField(type, name, Modifier.PUBLIC);
   }

   default FieldDeclaration addProtectedField(Class<?> typeClass, String name) {
      return this.addField(typeClass, name, Modifier.PROTECTED);
   }

   default FieldDeclaration addProtectedField(String type, String name) {
      return this.addField(type, name, Modifier.PROTECTED);
   }

   default FieldDeclaration addProtectedField(Type type, String name) {
      return this.addField(type, name, Modifier.PROTECTED);
   }

   default MethodDeclaration addMethod(String methodName, Modifier... modifiers) {
      MethodDeclaration methodDeclaration = new MethodDeclaration();
      methodDeclaration.setName((String)methodName);
      methodDeclaration.setType(new VoidType());
      methodDeclaration.setModifiers((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })));
      this.getMembers().add((Node)methodDeclaration);
      return methodDeclaration;
   }

   default BlockStmt addInitializer() {
      BlockStmt block = new BlockStmt();
      InitializerDeclaration initializerDeclaration = new InitializerDeclaration(false, block);
      this.getMembers().add((Node)initializerDeclaration);
      return block;
   }

   default BlockStmt addStaticInitializer() {
      BlockStmt block = new BlockStmt();
      InitializerDeclaration initializerDeclaration = new InitializerDeclaration(true, block);
      this.getMembers().add((Node)initializerDeclaration);
      return block;
   }

   default List<MethodDeclaration> getMethodsByName(String name) {
      return Collections.unmodifiableList((List)this.getMethods().stream().filter((m) -> {
         return m.getNameAsString().equals(name);
      }).collect(Collectors.toList()));
   }

   default List<MethodDeclaration> getMethods() {
      return Collections.unmodifiableList((List)this.getMembers().stream().filter((m) -> {
         return m instanceof MethodDeclaration;
      }).map((m) -> {
         return (MethodDeclaration)m;
      }).collect(Collectors.toList()));
   }

   default List<MethodDeclaration> getMethodsByParameterTypes(String... paramTypes) {
      return Collections.unmodifiableList((List)this.getMethods().stream().filter((m) -> {
         return m.hasParametersOfType(paramTypes);
      }).collect(Collectors.toList()));
   }

   default List<MethodDeclaration> getMethodsBySignature(String name, String... paramTypes) {
      return Collections.unmodifiableList((List)this.getMethodsByName(name).stream().filter((m) -> {
         return m.hasParametersOfType(paramTypes);
      }).collect(Collectors.toList()));
   }

   default List<MethodDeclaration> getMethodsByParameterTypes(Class<?>... paramTypes) {
      return Collections.unmodifiableList((List)this.getMethods().stream().filter((m) -> {
         return m.hasParametersOfType(paramTypes);
      }).collect(Collectors.toList()));
   }

   default Optional<FieldDeclaration> getFieldByName(String name) {
      return this.getMembers().stream().filter((m) -> {
         return m instanceof FieldDeclaration;
      }).map((f) -> {
         return (FieldDeclaration)f;
      }).filter((f) -> {
         return f.getVariables().stream().anyMatch((var) -> {
            return var.getNameAsString().equals(name);
         });
      }).findFirst();
   }

   default List<FieldDeclaration> getFields() {
      return Collections.unmodifiableList((List)this.getMembers().stream().filter((m) -> {
         return m instanceof FieldDeclaration;
      }).map((m) -> {
         return (FieldDeclaration)m;
      }).collect(Collectors.toList()));
   }
}
