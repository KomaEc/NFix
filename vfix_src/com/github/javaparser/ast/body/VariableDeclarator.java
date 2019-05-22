package com.github.javaparser.ast.body;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.observer.AstObserverAdapter;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.NonEmptyProperty;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.metamodel.VariableDeclaratorMetaModel;
import com.github.javaparser.resolution.Resolvable;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.utils.Utils;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class VariableDeclarator extends Node implements NodeWithType<VariableDeclarator, Type>, NodeWithSimpleName<VariableDeclarator>, Resolvable<ResolvedValueDeclaration> {
   private SimpleName name;
   @OptionalProperty
   @NonEmptyProperty
   private Expression initializer;
   private Type type;

   public VariableDeclarator() {
      this((TokenRange)null, new ClassOrInterfaceType(), new SimpleName(), (Expression)null);
   }

   public VariableDeclarator(Type type, String variableName) {
      this((TokenRange)null, type, new SimpleName(variableName), (Expression)null);
   }

   public VariableDeclarator(Type type, SimpleName name) {
      this((TokenRange)null, type, name, (Expression)null);
   }

   public VariableDeclarator(Type type, String variableName, Expression initializer) {
      this((TokenRange)null, type, new SimpleName(variableName), initializer);
   }

   @AllFieldsConstructor
   public VariableDeclarator(Type type, SimpleName name, Expression initializer) {
      this((TokenRange)null, type, name, initializer);
   }

   public VariableDeclarator(TokenRange tokenRange, Type type, SimpleName name, Expression initializer) {
      super(tokenRange);
      this.setType(type);
      this.setName(name);
      this.setInitializer(initializer);
      this.customInitialization();
   }

   protected void customInitialization() {
      this.register(new AstObserverAdapter() {
         public void propertyChange(Node observedNode, ObservableProperty property, Object oldValue, Object newValue) {
            if (property == ObservableProperty.TYPE) {
               VariableDeclarator vd = VariableDeclarator.this;
               if (vd.getParentNode().isPresent() && vd.getParentNode().get() instanceof NodeWithVariables) {
                  NodeWithVariables<?> nodeWithVariables = (NodeWithVariables)vd.getParentNode().get();
                  Optional<Type> currentMaxCommonType = nodeWithVariables.getMaximumCommonType();
                  List<Type> types = new LinkedList();
                  int index = nodeWithVariables.getVariables().indexOf(vd);

                  for(int i = 0; i < nodeWithVariables.getVariables().size(); ++i) {
                     if (i == index) {
                        types.add((Type)newValue);
                     } else {
                        types.add(nodeWithVariables.getVariable(i).getType());
                     }
                  }

                  Optional<Type> newMaxCommonType = NodeWithVariables.calculateMaximumCommonType(types);
                  ((Node)nodeWithVariables).notifyPropertyChange(ObservableProperty.MAXIMUM_COMMON_TYPE, currentMaxCommonType.orElse((Object)null), newMaxCommonType.orElse((Object)null));
               }
            }

         }
      });
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Optional<Expression> getInitializer() {
      return Optional.ofNullable(this.initializer);
   }

   public SimpleName getName() {
      return this.name;
   }

   public VariableDeclarator setName(final SimpleName name) {
      Utils.assertNotNull(name);
      if (name == this.name) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.NAME, this.name, name);
         if (this.name != null) {
            this.name.setParentNode((Node)null);
         }

         this.name = name;
         this.setAsParentNodeOf(name);
         return this;
      }
   }

   public VariableDeclarator setInitializer(final Expression initializer) {
      if (initializer == this.initializer) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INITIALIZER, this.initializer, initializer);
         if (this.initializer != null) {
            this.initializer.setParentNode((Node)null);
         }

         this.initializer = initializer;
         this.setAsParentNodeOf(initializer);
         return this;
      }
   }

   public VariableDeclarator setInitializer(String init) {
      return this.setInitializer((Expression)(new NameExpr(Utils.assertNonEmpty(init))));
   }

   public Type getType() {
      return this.type;
   }

   public VariableDeclarator setType(final Type type) {
      Utils.assertNotNull(type);
      if (type == this.type) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.TYPE, this.type, type);
         if (this.type != null) {
            this.type.setParentNode((Node)null);
         }

         this.type = type;
         this.setAsParentNodeOf(type);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.initializer != null && node == this.initializer) {
         this.removeInitializer();
         return true;
      } else {
         return super.remove(node);
      }
   }

   public VariableDeclarator removeInitializer() {
      return this.setInitializer((Expression)null);
   }

   public VariableDeclarator clone() {
      return (VariableDeclarator)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public VariableDeclaratorMetaModel getMetaModel() {
      return JavaParserMetaModel.variableDeclaratorMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (this.initializer != null && node == this.initializer) {
         this.setInitializer((Expression)replacementNode);
         return true;
      } else if (node == this.name) {
         this.setName((SimpleName)replacementNode);
         return true;
      } else if (node == this.type) {
         this.setType((Type)replacementNode);
         return true;
      } else {
         return super.replace(node, replacementNode);
      }
   }

   public ResolvedValueDeclaration resolve() {
      return (ResolvedValueDeclaration)this.getSymbolResolver().resolveDeclaration(this, ResolvedValueDeclaration.class);
   }
}
