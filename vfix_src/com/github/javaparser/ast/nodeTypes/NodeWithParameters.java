package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface NodeWithParameters<N extends Node> {
   NodeList<Parameter> getParameters();

   default Parameter getParameter(int i) {
      return (Parameter)this.getParameters().get(i);
   }

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default N setParameter(int i, Parameter parameter) {
      this.getParameters().set(i, (Node)parameter);
      return (Node)this;
   }

   N setParameters(NodeList<Parameter> parameters);

   default N addParameter(Type type, String name) {
      return this.addParameter(new Parameter(type, name));
   }

   default N addParameter(Class<?> paramClass, String name) {
      this.tryAddImportToParentCompilationUnit(paramClass);
      return this.addParameter(JavaParser.parseType(paramClass.getSimpleName()), name);
   }

   default N addParameter(String className, String name) {
      return this.addParameter(JavaParser.parseType(className), name);
   }

   default N addParameter(Parameter parameter) {
      this.getParameters().add((Node)parameter);
      return (Node)this;
   }

   default Parameter addAndGetParameter(Type type, String name) {
      return this.addAndGetParameter(new Parameter(type, name));
   }

   default Parameter addAndGetParameter(Class<?> paramClass, String name) {
      this.tryAddImportToParentCompilationUnit(paramClass);
      return this.addAndGetParameter(JavaParser.parseType(paramClass.getSimpleName()), name);
   }

   default Parameter addAndGetParameter(String className, String name) {
      return this.addAndGetParameter(JavaParser.parseType(className), name);
   }

   default Parameter addAndGetParameter(Parameter parameter) {
      this.getParameters().add((Node)parameter);
      return parameter;
   }

   default Optional<Parameter> getParameterByName(String name) {
      return this.getParameters().stream().filter((p) -> {
         return p.getNameAsString().equals(name);
      }).findFirst();
   }

   default Optional<Parameter> getParameterByType(String type) {
      return this.getParameters().stream().filter((p) -> {
         return p.getType().toString().equals(type);
      }).findFirst();
   }

   default Optional<Parameter> getParameterByType(Class<?> type) {
      return this.getParameters().stream().filter((p) -> {
         return p.getType().toString().equals(type.getSimpleName());
      }).findFirst();
   }

   default boolean hasParametersOfType(String... paramTypes) {
      return ((Set)this.getParameters().stream().map((p) -> {
         return p.getType().toString();
      }).collect(Collectors.toSet())).equals(Stream.of(paramTypes).collect(Collectors.toSet()));
   }

   default boolean hasParametersOfType(Class<?>... paramTypes) {
      return ((Set)this.getParameters().stream().map((p) -> {
         return p.getType().toString();
      }).collect(Collectors.toSet())).equals(Stream.of(paramTypes).map(Class::getSimpleName).collect(Collectors.toSet()));
   }
}
