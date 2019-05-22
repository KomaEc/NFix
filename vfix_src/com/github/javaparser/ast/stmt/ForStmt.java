package com.github.javaparser.ast.stmt;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.AllFieldsConstructor;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithBody;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.ast.visitor.CloneVisitor;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.metamodel.ForStmtMetaModel;
import com.github.javaparser.metamodel.JavaParserMetaModel;
import com.github.javaparser.metamodel.OptionalProperty;
import com.github.javaparser.utils.Utils;
import java.util.Optional;
import java.util.function.Consumer;

public final class ForStmt extends Statement implements NodeWithBody<ForStmt> {
   private NodeList<Expression> initialization;
   @OptionalProperty
   private Expression compare;
   private NodeList<Expression> update;
   private Statement body;

   public ForStmt() {
      this((TokenRange)null, new NodeList(), new BooleanLiteralExpr(), new NodeList(), new ReturnStmt());
   }

   @AllFieldsConstructor
   public ForStmt(final NodeList<Expression> initialization, final Expression compare, final NodeList<Expression> update, final Statement body) {
      this((TokenRange)null, initialization, compare, update, body);
   }

   public ForStmt(TokenRange tokenRange, NodeList<Expression> initialization, Expression compare, NodeList<Expression> update, Statement body) {
      super(tokenRange);
      this.setInitialization(initialization);
      this.setCompare(compare);
      this.setUpdate(update);
      this.setBody(body);
      this.customInitialization();
   }

   public <R, A> R accept(final GenericVisitor<R, A> v, final A arg) {
      return v.visit(this, arg);
   }

   public <A> void accept(final VoidVisitor<A> v, final A arg) {
      v.visit(this, arg);
   }

   public Statement getBody() {
      return this.body;
   }

   public Optional<Expression> getCompare() {
      return Optional.ofNullable(this.compare);
   }

   public NodeList<Expression> getInitialization() {
      return this.initialization;
   }

   public NodeList<Expression> getUpdate() {
      return this.update;
   }

   public ForStmt setBody(final Statement body) {
      Utils.assertNotNull(body);
      if (body == this.body) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.BODY, this.body, body);
         if (this.body != null) {
            this.body.setParentNode((Node)null);
         }

         this.body = body;
         this.setAsParentNodeOf(body);
         return this;
      }
   }

   public ForStmt setCompare(final Expression compare) {
      if (compare == this.compare) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.COMPARE, this.compare, compare);
         if (this.compare != null) {
            this.compare.setParentNode((Node)null);
         }

         this.compare = compare;
         this.setAsParentNodeOf(compare);
         return this;
      }
   }

   public ForStmt setInitialization(final NodeList<Expression> initialization) {
      Utils.assertNotNull(initialization);
      if (initialization == this.initialization) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.INITIALIZATION, this.initialization, initialization);
         if (this.initialization != null) {
            this.initialization.setParentNode((Node)null);
         }

         this.initialization = initialization;
         this.setAsParentNodeOf(initialization);
         return this;
      }
   }

   public ForStmt setUpdate(final NodeList<Expression> update) {
      Utils.assertNotNull(update);
      if (update == this.update) {
         return this;
      } else {
         this.notifyPropertyChange(ObservableProperty.UPDATE, this.update, update);
         if (this.update != null) {
            this.update.setParentNode((Node)null);
         }

         this.update = update;
         this.setAsParentNodeOf(update);
         return this;
      }
   }

   public boolean remove(Node node) {
      if (node == null) {
         return false;
      } else if (this.compare != null && node == this.compare) {
         this.removeCompare();
         return true;
      } else {
         int i;
         for(i = 0; i < this.initialization.size(); ++i) {
            if (this.initialization.get(i) == node) {
               this.initialization.remove(i);
               return true;
            }
         }

         for(i = 0; i < this.update.size(); ++i) {
            if (this.update.get(i) == node) {
               this.update.remove(i);
               return true;
            }
         }

         return super.remove(node);
      }
   }

   public ForStmt removeCompare() {
      return this.setCompare((Expression)null);
   }

   public ForStmt clone() {
      return (ForStmt)this.accept((GenericVisitor)(new CloneVisitor()), (Object)null);
   }

   public ForStmtMetaModel getMetaModel() {
      return JavaParserMetaModel.forStmtMetaModel;
   }

   public boolean replace(Node node, Node replacementNode) {
      if (node == null) {
         return false;
      } else if (node == this.body) {
         this.setBody((Statement)replacementNode);
         return true;
      } else if (this.compare != null && node == this.compare) {
         this.setCompare((Expression)replacementNode);
         return true;
      } else {
         int i;
         for(i = 0; i < this.initialization.size(); ++i) {
            if (this.initialization.get(i) == node) {
               this.initialization.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         for(i = 0; i < this.update.size(); ++i) {
            if (this.update.get(i) == node) {
               this.update.set(i, (Node)((Expression)replacementNode));
               return true;
            }
         }

         return super.replace(node, replacementNode);
      }
   }

   public boolean isForStmt() {
      return true;
   }

   public ForStmt asForStmt() {
      return this;
   }

   public void ifForStmt(Consumer<ForStmt> action) {
      action.accept(this);
   }

   public Optional<ForStmt> toForStmt() {
      return Optional.of(this);
   }
}
