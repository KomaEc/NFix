package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.FieldDeclarationMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

public final class FieldDeclaration extends BodyDeclaration<FieldDeclaration> implements NodeWithJavadoc<FieldDeclaration>, NodeWithVariables<FieldDeclaration>, NodeWithAccessModifiers<FieldDeclaration>, NodeWithStaticModifier<FieldDeclaration>, NodeWithFinalModifier<FieldDeclaration>, Resolvable<ResolvedFieldDeclaration> {
   private EnumSet<Modifier> modifiers;
   @NonEmptyProperty
   private NodeList<VariableDeclarator> variables;

   public FieldDeclaration() {
      this((TokenRange)null, EnumSet.noneOf(Modifier.class), new NodeList(), new NodeList());
   }

   public FieldDeclaration(EnumSet<Modifier> modifiers, VariableDeclarator variable) {
      this((TokenRange)null, modifiers, new NodeList(), NodeList.nodeList((Node[])(variable)));
   }

   public FieldDeclaration(EnumSet<Modifier> modifiers, NodeList<VariableDeclarator> variables) {
      this((TokenRange)null, modifiers, new NodeList(), variables);
   }

   @AllFieldsConstructor
   public FieldDeclaration(EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<VariableDeclarator> variables) {
      this((TokenRange)null, modifiers, annotations, variables);
   }

   public FieldDeclaration(TokenRange tokenRange, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations, NodeList<VariableDeclarator> variables) {
      super(tokenRange, annotations);
      this.setModifiers(modifiers);
      this.setVariables(variables);
      this.customInitialization();
   }

   public FieldDeclaration(EnumSet<Modifier> modifiers, Type type, String name) {
      this((EnumSet)Utils.assertNotNull(modifiers), new VariableDeclarator(type, (String)Utils.assertNotNull(name)));
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public EnumSet<Modifier> getModifiers() {
      return this.modifiers;
   }

   public NodeList<VariableDeclarator> getVariables() {
      return this.variables;
   }

   public FieldDeclaration setModifiers(final EnumSet<Modifier> modifiers) {
      Utils.assertNotNull(modifiers);
      if (modifiers == this.modifiers) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.MODIFIERS, this.modifiers, modifiers);
         this.modifiers = modifiers;
         return this;
      }
   }

   public FieldDeclaration setVariables(final NodeList<VariableDeclarator> variables) {
      Utils.assertNotNull(variables);
      if (variables == this.variables) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.VARIABLES, this.variables, variables);
         if (this.variables != null) {
            this.variables.setParentNode((Node)null);
         }

         this.variables = variables;
         this.setAsParentNodeOf(variables);
         return this;
      }
   }

   public MethodDeclaration createGetter() {
      if (this.getVariables().size() != 1) {
         throw new IllegalStateException("You can use this only when the field declares only 1 variable name");
      } else {
         Optional<ClassOrInterfaceDeclaration> parentClass = this.findAncestor(ClassOrInterfaceDeclaration.class);
         Optional<EnumDeclaration> parentEnum = this.findAncestor(EnumDeclaration.class);
         if ((parentClass.isPresent() || parentEnum.isPresent()) && (!parentClass.isPresent() || !((ClassOrInterfaceDeclaration)parentClass.get()).isInterface())) {
            VariableDeclarator variable = this.getVariable(0);
            String fieldName = variable.getNameAsString();
            String fieldNameUpper = fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1, fieldName.length());
            MethodDeclaration getter = (MethodDeclaration)parentClass.map((clazz) -> {
               return clazz.addMethod("get" + fieldNameUpper, new Modifier[]{Modifier.PUBLIC});
            }).orElseGet(() -> {
               return ((EnumDeclaration)parentEnum.get()).addMethod("get" + fieldNameUpper, new Modifier[]{Modifier.PUBLIC});
            });
            getter.setType(variable.getType());
            BlockStmt blockStmt = new BlockStmt();
            getter.setBody(blockStmt);
            blockStmt.addStatement(new ReturnStmt(fieldName));
            return getter;
         } else {
            throw new IllegalStateException("You can use this only when the field is attached to a class or an enum");
         }
      }
   }

   public MethodDeclaration createSetter() {
      if (this.getVariables().size() != 1) {
         throw new IllegalStateException("You can use this only when the field declares only 1 variable name");
      } else {
         Optional<ClassOrInterfaceDeclaration> parentClass = this.findAncestor(ClassOrInterfaceDeclaration.class);
         Optional<EnumDeclaration> parentEnum = this.findAncestor(EnumDeclaration.class);
         if ((parentClass.isPresent() || parentEnum.isPresent()) && (!parentClass.isPresent() || !((ClassOrInterfaceDeclaration)parentClass.get()).isInterface())) {
            VariableDeclarator variable = this.getVariable(0);
            String fieldName = variable.getNameAsString();
            String fieldNameUpper = fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1, fieldName.length());
            MethodDeclaration setter = (MethodDeclaration)parentClass.map((clazz) -> {
               return clazz.addMethod("set" + fieldNameUpper, new Modifier[]{Modifier.PUBLIC});
            }).orElseGet(() -> {
               return ((EnumDeclaration)parentEnum.get()).addMethod("set" + fieldNameUpper, new Modifier[]{Modifier.PUBLIC});
            });
            setter.setType(new VoidType());
            setter.getParameters().add((Node)(new Parameter(variable.getType(), fieldName)));
            BlockStmt blockStmt2 = new BlockStmt();
            setter.setBody(blockStmt2);
            blockStmt2.addStatement(new AssignExpr(new NameExpr("this." + fieldName), new NameExpr(fieldName), AssignExpr.Operator.ASSIGN));
            return setter;
         } else {
            throw new IllegalStateException("You can use this only when the field is attached to a class or an enum");
         }
      }
   }

   public boolean isTransient() {
      return this.getModifiers().contains(Modifier.TRANSIENT);
   }

   public boolean isVolatile() {
      return this.getModifiers().contains(Modifier.VOLATILE);
   }

   public FieldDeclaration setTransient(boolean set) {
      return (FieldDeclaration)this.setModifier(Modifier.TRANSIENT, set);
   }

   public FieldDeclaration setVolatile(boolean set) {
      return (FieldDeclaration)this.setModifier(Modifier.VOLATILE, set);
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.variables.size(); ++i) {
            if (this.variables.get(i) == node) {
               this.variables.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public FieldDeclaration clone() {
      return (FieldDeclaration)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public FieldDeclarationMetaModel getMetaModel() {
      return JavaParserMetaModel.fieldDeclarationMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else {
         for(int i = 0; i < this.variables.size(); ++i) {
            if (this.variables.get(i) == node) {
               this.variables.set(i, (Node)((VariableDeclarator)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isFieldDeclaration() {
      return true;
   }

   public FieldDeclaration asFieldDeclaration() {
      return this;
   }

   public void ifFieldDeclaration(Consumer<FieldDeclaration> action) {
      action.accept(this);
   }

   public ResolvedFieldDeclaration resolve() {
      return (ResolvedFieldDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedFieldDeclaration.class);
   }

   public Optional<FieldDeclaration> toFieldDeclaration() {
      return Optional.of(this);
   }
}
