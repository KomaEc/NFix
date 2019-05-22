package com.github.javaparser.printer;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithTraversableScope;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.utils.PositionUtils;
import com.github.javaparser.utils.Utils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class PrettyPrintVisitor implements VoidVisitor<Void> {
   protected final PrettyPrinterConfiguration configuration;
   protected final SourcePrinter printer;

   public PrettyPrintVisitor(PrettyPrinterConfiguration prettyPrinterConfiguration) {
      this.configuration = prettyPrinterConfiguration;
      this.printer = new SourcePrinter(this.configuration);
   }

   public String getSource() {
      return this.printer.getSource();
   }

   private void printModifiers(final EnumSet<Modifier> modifiers) {
      if (modifiers.size() > 0) {
         this.printer.print((String)modifiers.stream().map(Modifier::asString).collect(Collectors.joining(" ")) + " ");
      }

   }

   private void printMembers(final NodeList<BodyDeclaration<?>> members, final Void arg) {
      Iterator var3 = members.iterator();

      while(var3.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var3.next();
         this.printer.println();
         member.accept(this, arg);
         this.printer.println();
      }

   }

   private void printMemberAnnotations(final NodeList<AnnotationExpr> annotations, final Void arg) {
      if (!annotations.isEmpty()) {
         Iterator var3 = annotations.iterator();

         while(var3.hasNext()) {
            AnnotationExpr a = (AnnotationExpr)var3.next();
            a.accept(this, arg);
            this.printer.println();
         }

      }
   }

   private void printAnnotations(final NodeList<AnnotationExpr> annotations, boolean prefixWithASpace, final Void arg) {
      if (!annotations.isEmpty()) {
         if (prefixWithASpace) {
            this.printer.print(" ");
         }

         Iterator var4 = annotations.iterator();

         while(var4.hasNext()) {
            AnnotationExpr annotation = (AnnotationExpr)var4.next();
            annotation.accept(this, arg);
            this.printer.print(" ");
         }

      }
   }

   private void printTypeArgs(final NodeWithTypeArguments<?> nodeWithTypeArguments, final Void arg) {
      NodeList<Type> typeArguments = (NodeList)nodeWithTypeArguments.getTypeArguments().orElse((Object)null);
      if (!Utils.isNullOrEmpty(typeArguments)) {
         this.printer.print("<");
         Iterator i = typeArguments.iterator();

         while(i.hasNext()) {
            Type t = (Type)i.next();
            t.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }

         this.printer.print(">");
      }

   }

   private void printTypeParameters(final NodeList<TypeParameter> args, final Void arg) {
      if (!Utils.isNullOrEmpty(args)) {
         this.printer.print("<");
         Iterator i = args.iterator();

         while(i.hasNext()) {
            TypeParameter t = (TypeParameter)i.next();
            t.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }

         this.printer.print(">");
      }

   }

   private void printArguments(final NodeList<Expression> args, final Void arg) {
      this.printer.print("(");
      if (!Utils.isNullOrEmpty(args)) {
         boolean columnAlignParameters = args.size() > 1 && this.configuration.isColumnAlignParameters();
         if (columnAlignParameters) {
            this.printer.indentWithAlignTo(this.printer.getCursor().column);
         }

         Iterator i = args.iterator();

         while(i.hasNext()) {
            Expression e = (Expression)i.next();
            e.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(",");
               if (columnAlignParameters) {
                  this.printer.println();
               } else {
                  this.printer.print(" ");
               }
            }
         }

         if (columnAlignParameters) {
            this.printer.unindent();
         }
      }

      this.printer.print(")");
   }

   private void printPrePostFixOptionalList(final NodeList<? extends Visitable> args, final Void arg, String prefix, String separator, String postfix) {
      if (!args.isEmpty()) {
         this.printer.print(prefix);
         Iterator i = args.iterator();

         while(i.hasNext()) {
            Visitable v = (Visitable)i.next();
            v.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(separator);
            }
         }

         this.printer.print(postfix);
      }

   }

   private void printPrePostFixRequiredList(final NodeList<? extends Visitable> args, final Void arg, String prefix, String separator, String postfix) {
      this.printer.print(prefix);
      if (!args.isEmpty()) {
         Iterator i = args.iterator();

         while(i.hasNext()) {
            Visitable v = (Visitable)i.next();
            v.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(separator);
            }
         }
      }

      this.printer.print(postfix);
   }

   private void printComment(final Optional<Comment> comment, final Void arg) {
      comment.ifPresent((c) -> {
         c.accept(this, arg);
      });
   }

   public void visit(final CompilationUnit n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getParsed() == Node.Parsedness.UNPARSABLE) {
         this.printer.println("???");
      } else {
         if (n.getPackageDeclaration().isPresent()) {
            ((PackageDeclaration)n.getPackageDeclaration().get()).accept((VoidVisitor)this, arg);
         }

         n.getImports().accept((VoidVisitor)this, arg);
         if (!n.getImports().isEmpty()) {
            this.printer.println();
         }

         Iterator i = n.getTypes().iterator();

         while(i.hasNext()) {
            ((TypeDeclaration)i.next()).accept(this, arg);
            this.printer.println();
            if (i.hasNext()) {
               this.printer.println();
            }
         }

         n.getModule().ifPresent((m) -> {
            m.accept((VoidVisitor)this, arg);
         });
         this.printOrphanCommentsEnding(n);
      }
   }

   public void visit(final PackageDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printer.print("package ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.println(";");
      this.printer.println();
      this.printOrphanCommentsEnding(n);
   }

   public void visit(final NameExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getName().accept((VoidVisitor)this, arg);
      this.printOrphanCommentsEnding(n);
   }

   public void visit(final Name n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getQualifier().isPresent()) {
         ((Name)n.getQualifier().get()).accept((VoidVisitor)this, arg);
         this.printer.print(".");
      }

      this.printAnnotations(n.getAnnotations(), false, arg);
      this.printer.print(n.getIdentifier());
      this.printOrphanCommentsEnding(n);
   }

   public void visit(SimpleName n, Void arg) {
      this.printer.print(n.getIdentifier());
   }

   public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      if (n.isInterface()) {
         this.printer.print("interface ");
      } else {
         this.printer.print("class ");
      }

      n.getName().accept((VoidVisitor)this, arg);
      this.printTypeParameters(n.getTypeParameters(), arg);
      Iterator i;
      ClassOrInterfaceType c;
      if (!n.getExtendedTypes().isEmpty()) {
         this.printer.print(" extends ");
         i = n.getExtendedTypes().iterator();

         while(i.hasNext()) {
            c = (ClassOrInterfaceType)i.next();
            c.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      if (!n.getImplementedTypes().isEmpty()) {
         this.printer.print(" implements ");
         i = n.getImplementedTypes().iterator();

         while(i.hasNext()) {
            c = (ClassOrInterfaceType)i.next();
            c.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.println(" {");
      this.printer.indent();
      if (!Utils.isNullOrEmpty(n.getMembers())) {
         this.printMembers(n.getMembers(), arg);
      }

      this.printOrphanCommentsEnding(n);
      this.printer.unindent();
      this.printer.print("}");
   }

   public void visit(final JavadocComment n, final Void arg) {
      if (this.configuration.isPrintComments() && this.configuration.isPrintJavadoc()) {
         this.printer.println("/**");
         String commentContent = Utils.normalizeEolInTextBlock(n.getContent(), this.configuration.getEndOfLineCharacter());
         String[] lines = commentContent.split("\\R");
         boolean skippingLeadingEmptyLines = true;
         boolean prependEmptyLine = false;
         boolean prependSpace = Arrays.stream(lines).anyMatch((linex) -> {
            return !linex.isEmpty() && !linex.startsWith(" ");
         });
         String[] var8 = lines;
         int var9 = lines.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String line = var8[var10];
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("*")) {
               line = trimmedLine.substring(1);
            }

            line = Utils.trimTrailingSpaces(line);
            if (line.isEmpty()) {
               if (!skippingLeadingEmptyLines) {
                  prependEmptyLine = true;
               }
            } else {
               skippingLeadingEmptyLines = false;
               if (prependEmptyLine) {
                  this.printer.println(" *");
                  prependEmptyLine = false;
               }

               this.printer.print(" *");
               if (prependSpace) {
                  this.printer.print(" ");
               }

               this.printer.println(line);
            }
         }

         this.printer.println(" */");
      }

   }

   public void visit(final ClassOrInterfaceType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getScope().isPresent()) {
         ((ClassOrInterfaceType)n.getScope().get()).accept((VoidVisitor)this, arg);
         this.printer.print(".");
      }

      this.printAnnotations(n.getAnnotations(), false, arg);
      n.getName().accept((VoidVisitor)this, arg);
      if (n.isUsingDiamondOperator()) {
         this.printer.print("<>");
      } else {
         this.printTypeArgs(n, arg);
      }

   }

   public void visit(final TypeParameter n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      n.getName().accept((VoidVisitor)this, arg);
      if (!Utils.isNullOrEmpty(n.getTypeBound())) {
         this.printer.print(" extends ");
         Iterator i = n.getTypeBound().iterator();

         while(i.hasNext()) {
            ClassOrInterfaceType c = (ClassOrInterfaceType)i.next();
            c.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(" & ");
            }
         }
      }

   }

   public void visit(final PrimitiveType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), true, arg);
      this.printer.print(n.getType().asString());
   }

   public void visit(final ArrayType n, final Void arg) {
      List<ArrayType> arrayTypeBuffer = new LinkedList();

      Object type;
      ArrayType arrayType;
      for(type = n; type instanceof ArrayType; type = arrayType.getComponentType()) {
         arrayType = (ArrayType)type;
         arrayTypeBuffer.add(arrayType);
      }

      ((Type)type).accept(this, arg);
      Iterator var7 = arrayTypeBuffer.iterator();

      while(var7.hasNext()) {
         ArrayType arrayType = (ArrayType)var7.next();
         this.printAnnotations(arrayType.getAnnotations(), true, arg);
         this.printer.print("[]");
      }

   }

   public void visit(final ArrayCreationLevel n, final Void arg) {
      this.printAnnotations(n.getAnnotations(), true, arg);
      this.printer.print("[");
      if (n.getDimension().isPresent()) {
         ((Expression)n.getDimension().get()).accept(this, arg);
      }

      this.printer.print("]");
   }

   public void visit(final IntersectionType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      boolean isFirst = true;

      ReferenceType element;
      for(Iterator var4 = n.getElements().iterator(); var4.hasNext(); element.accept(this, arg)) {
         element = (ReferenceType)var4.next();
         if (isFirst) {
            isFirst = false;
         } else {
            this.printer.print(" & ");
         }
      }

   }

   public void visit(final UnionType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), true, arg);
      boolean isFirst = true;

      ReferenceType element;
      for(Iterator var4 = n.getElements().iterator(); var4.hasNext(); element.accept(this, arg)) {
         element = (ReferenceType)var4.next();
         if (isFirst) {
            isFirst = false;
         } else {
            this.printer.print(" | ");
         }
      }

   }

   public void visit(final WildcardType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      this.printer.print("?");
      if (n.getExtendedType().isPresent()) {
         this.printer.print(" extends ");
         ((ReferenceType)n.getExtendedType().get()).accept(this, arg);
      }

      if (n.getSuperType().isPresent()) {
         this.printer.print(" super ");
         ((ReferenceType)n.getSuperType().get()).accept(this, arg);
      }

   }

   public void visit(final UnknownType n, final Void arg) {
   }

   public void visit(final FieldDeclaration n, final Void arg) {
      this.printOrphanCommentsBeforeThisChildNode(n);
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      if (!n.getVariables().isEmpty()) {
         Optional<Type> maximumCommonType = n.getMaximumCommonType();
         maximumCommonType.ifPresent((t) -> {
            t.accept(this, arg);
         });
         if (!maximumCommonType.isPresent()) {
            this.printer.print("???");
         }
      }

      this.printer.print(" ");
      Iterator i = n.getVariables().iterator();

      while(i.hasNext()) {
         VariableDeclarator var = (VariableDeclarator)i.next();
         var.accept((VoidVisitor)this, arg);
         if (i.hasNext()) {
            this.printer.print(", ");
         }
      }

      this.printer.print(";");
   }

   public void visit(final VariableDeclarator n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getName().accept((VoidVisitor)this, arg);
      n.findAncestor(NodeWithVariables.class).ifPresent((ancestor) -> {
         ancestor.getMaximumCommonType().ifPresent((commonType) -> {
            Type type = n.getType();
            ArrayType arrayType = null;

            for(int i = commonType.getArrayLevel(); i < type.getArrayLevel(); ++i) {
               if (arrayType == null) {
                  arrayType = (ArrayType)type;
               } else {
                  arrayType = (ArrayType)arrayType.getComponentType();
               }

               this.printAnnotations(arrayType.getAnnotations(), true, arg);
               this.printer.print("[]");
            }

         });
      });
      if (n.getInitializer().isPresent()) {
         this.printer.print(" = ");
         ((Expression)n.getInitializer().get()).accept(this, arg);
      }

   }

   public void visit(final ArrayInitializerExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("{");
      if (!Utils.isNullOrEmpty(n.getValues())) {
         this.printer.print(" ");
         Iterator i = n.getValues().iterator();

         while(i.hasNext()) {
            Expression expr = (Expression)i.next();
            expr.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }

         this.printer.print(" ");
      }

      this.printOrphanCommentsEnding(n);
      this.printer.print("}");
   }

   public void visit(final VoidType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      this.printer.print("void");
   }

   public void visit(final VarType n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      this.printer.print("var");
   }

   public void visit(final ArrayAccessExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getName().accept(this, arg);
      this.printer.print("[");
      n.getIndex().accept(this, arg);
      this.printer.print("]");
   }

   public void visit(final ArrayCreationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("new ");
      n.getElementType().accept(this, arg);
      Iterator var3 = n.getLevels().iterator();

      while(var3.hasNext()) {
         ArrayCreationLevel level = (ArrayCreationLevel)var3.next();
         level.accept((VoidVisitor)this, arg);
      }

      if (n.getInitializer().isPresent()) {
         this.printer.print(" ");
         ((ArrayInitializerExpr)n.getInitializer().get()).accept((VoidVisitor)this, arg);
      }

   }

   public void visit(final AssignExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getTarget().accept(this, arg);
      this.printer.print(" ");
      this.printer.print(n.getOperator().asString());
      this.printer.print(" ");
      n.getValue().accept(this, arg);
   }

   public void visit(final BinaryExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getLeft().accept(this, arg);
      this.printer.print(" ");
      this.printer.print(n.getOperator().asString());
      this.printer.print(" ");
      n.getRight().accept(this, arg);
   }

   public void visit(final CastExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("(");
      n.getType().accept(this, arg);
      this.printer.print(") ");
      n.getExpression().accept(this, arg);
   }

   public void visit(final ClassExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getType().accept(this, arg);
      this.printer.print(".class");
   }

   public void visit(final ConditionalExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getCondition().accept(this, arg);
      this.printer.print(" ? ");
      n.getThenExpr().accept(this, arg);
      this.printer.print(" : ");
      n.getElseExpr().accept(this, arg);
   }

   public void visit(final EnclosedExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("(");
      n.getInner().accept(this, arg);
      this.printer.print(")");
   }

   public void visit(final FieldAccessExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getScope().accept(this, arg);
      this.printer.print(".");
      n.getName().accept((VoidVisitor)this, arg);
   }

   public void visit(final InstanceOfExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getExpression().accept(this, arg);
      this.printer.print(" instanceof ");
      n.getType().accept(this, arg);
   }

   public void visit(final CharLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("'");
      this.printer.print(n.getValue());
      this.printer.print("'");
   }

   public void visit(final DoubleLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(n.getValue());
   }

   public void visit(final IntegerLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(n.getValue());
   }

   public void visit(final LongLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(n.getValue());
   }

   public void visit(final StringLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("\"");
      this.printer.print(n.getValue());
      this.printer.print("\"");
   }

   public void visit(final BooleanLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(String.valueOf(n.getValue()));
   }

   public void visit(final NullLiteralExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("null");
   }

   public void visit(final ThisExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getClassExpr().isPresent()) {
         ((Expression)n.getClassExpr().get()).accept(this, arg);
         this.printer.print(".");
      }

      this.printer.print("this");
   }

   public void visit(final SuperExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getClassExpr().isPresent()) {
         ((Expression)n.getClassExpr().get()).accept(this, arg);
         this.printer.print(".");
      }

      this.printer.print("super");
   }

   public void visit(final MethodCallExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      AtomicBoolean columnAlignFirstMethodChain = new AtomicBoolean();
      Optional var10000;
      if (this.configuration.isColumnAlignFirstMethodChain() && (Boolean)n.findAncestor(Statement.class).map((px) -> {
         return px.isReturnStmt() || px.isThrowStmt() || px.isAssertStmt() || px.isExpressionStmt();
      }).orElse(false)) {
         Node c = n;

         Optional p;
         for(p = n.getParentNode(); p.isPresent(); p = ((Node)c).getParentNode()) {
            NodeWithTraversableScope.class.getClass();
            var10000 = p.filter(NodeWithTraversableScope.class::isInstance);
            NodeWithTraversableScope.class.getClass();
            var10000 = var10000.map(NodeWithTraversableScope.class::cast).flatMap(NodeWithTraversableScope::traverseScope);
            c.getClass();
            if (!(Boolean)var10000.map(c::equals).orElse(false)) {
               break;
            }

            c = (Node)p.get();
         }

         MethodCallExpr.class.getClass();
         columnAlignFirstMethodChain.set(!p.filter(MethodCallExpr.class::isInstance).isPresent());
      }

      AtomicBoolean lastMethodInCallChain = new AtomicBoolean(true);
      if (columnAlignFirstMethodChain.get()) {
         Object node = n;

         while(true) {
            var10000 = ((Node)node).getParentNode();
            NodeWithTraversableScope.class.getClass();
            var10000 = var10000.filter(NodeWithTraversableScope.class::isInstance);
            NodeWithTraversableScope.class.getClass();
            var10000 = var10000.map(NodeWithTraversableScope.class::cast).flatMap(NodeWithTraversableScope::traverseScope);
            node.getClass();
            if (!(Boolean)var10000.map(node::equals).orElse(false)) {
               break;
            }

            node = (Node)((Node)node).getParentNode().orElseThrow(AssertionError::new);
            if (node instanceof MethodCallExpr) {
               lastMethodInCallChain.set(false);
               break;
            }
         }
      }

      AtomicBoolean methodCallWithScopeInScope = new AtomicBoolean();
      if (columnAlignFirstMethodChain.get()) {
         Optional s = n.getScope();

         while(true) {
            NodeWithTraversableScope.class.getClass();
            if (!s.filter(NodeWithTraversableScope.class::isInstance).isPresent()) {
               break;
            }

            NodeWithTraversableScope.class.getClass();
            Optional<Expression> parentScope = s.map(NodeWithTraversableScope.class::cast).flatMap(NodeWithTraversableScope::traverseScope);
            MethodCallExpr.class.getClass();
            if (s.filter(MethodCallExpr.class::isInstance).isPresent() && parentScope.isPresent()) {
               methodCallWithScopeInScope.set(true);
               break;
            }

            s = parentScope;
         }
      }

      n.getScope().ifPresent((scope) -> {
         scope.accept(this, arg);
         if (columnAlignFirstMethodChain.get()) {
            if (methodCallWithScopeInScope.get()) {
               this.printer.println();
            } else if (!lastMethodInCallChain.get()) {
               this.printer.reindentWithAlignToCursor();
            }
         }

         this.printer.print(".");
      });
      this.printTypeArgs(n, arg);
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.duplicateIndent();
      this.printArguments(n.getArguments(), arg);
      this.printer.unindent();
      if (columnAlignFirstMethodChain.get() && methodCallWithScopeInScope.get() && lastMethodInCallChain.get()) {
         this.printer.reindentToPreviousLevel();
      }

   }

   public void visit(final ObjectCreationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getScope().isPresent()) {
         ((Expression)n.getScope().get()).accept(this, arg);
         this.printer.print(".");
      }

      this.printer.print("new ");
      this.printTypeArgs(n, arg);
      if (!Utils.isNullOrEmpty((Collection)n.getTypeArguments().orElse((Object)null))) {
         this.printer.print(" ");
      }

      n.getType().accept((VoidVisitor)this, arg);
      this.printArguments(n.getArguments(), arg);
      if (n.getAnonymousClassBody().isPresent()) {
         this.printer.println(" {");
         this.printer.indent();
         this.printMembers((NodeList)n.getAnonymousClassBody().get(), arg);
         this.printer.unindent();
         this.printer.print("}");
      }

   }

   public void visit(final UnaryExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getOperator().isPrefix()) {
         this.printer.print(n.getOperator().asString());
      }

      n.getExpression().accept(this, arg);
      if (n.getOperator().isPostfix()) {
         this.printer.print(n.getOperator().asString());
      }

   }

   public void visit(final ConstructorDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      this.printTypeParameters(n.getTypeParameters(), arg);
      if (n.isGeneric()) {
         this.printer.print(" ");
      }

      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print("(");
      Iterator i;
      if (!n.getParameters().isEmpty()) {
         i = n.getParameters().iterator();

         while(i.hasNext()) {
            Parameter p = (Parameter)i.next();
            p.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print(")");
      if (!Utils.isNullOrEmpty(n.getThrownExceptions())) {
         this.printer.print(" throws ");
         i = n.getThrownExceptions().iterator();

         while(i.hasNext()) {
            ReferenceType name = (ReferenceType)i.next();
            name.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print(" ");
      n.getBody().accept((VoidVisitor)this, arg);
   }

   public void visit(final MethodDeclaration n, final Void arg) {
      this.printOrphanCommentsBeforeThisChildNode(n);
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      this.printTypeParameters(n.getTypeParameters(), arg);
      if (!Utils.isNullOrEmpty(n.getTypeParameters())) {
         this.printer.print(" ");
      }

      n.getType().accept(this, arg);
      this.printer.print(" ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print("(");
      n.getReceiverParameter().ifPresent((rp) -> {
         rp.accept((VoidVisitor)this, arg);
         if (!Utils.isNullOrEmpty(n.getParameters())) {
            this.printer.print(", ");
         }

      });
      Iterator i;
      if (!Utils.isNullOrEmpty(n.getParameters())) {
         i = n.getParameters().iterator();

         while(i.hasNext()) {
            Parameter p = (Parameter)i.next();
            p.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print(")");
      if (!Utils.isNullOrEmpty(n.getThrownExceptions())) {
         this.printer.print(" throws ");
         i = n.getThrownExceptions().iterator();

         while(i.hasNext()) {
            ReferenceType name = (ReferenceType)i.next();
            name.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      if (!n.getBody().isPresent()) {
         this.printer.print(";");
      } else {
         this.printer.print(" ");
         ((BlockStmt)n.getBody().get()).accept((VoidVisitor)this, arg);
      }

   }

   public void visit(final Parameter n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      this.printModifiers(n.getModifiers());
      n.getType().accept(this, arg);
      if (n.isVarArgs()) {
         this.printAnnotations(n.getVarArgsAnnotations(), false, arg);
         this.printer.print("...");
      }

      if (!(n.getType() instanceof UnknownType)) {
         this.printer.print(" ");
      }

      n.getName().accept((VoidVisitor)this, arg);
   }

   public void visit(final ReceiverParameter n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printAnnotations(n.getAnnotations(), false, arg);
      n.getType().accept(this, arg);
      this.printer.print(" ");
      n.getName().accept((VoidVisitor)this, arg);
   }

   public void visit(final ExplicitConstructorInvocationStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.isThis()) {
         this.printTypeArgs(n, arg);
         this.printer.print("this");
      } else {
         if (n.getExpression().isPresent()) {
            ((Expression)n.getExpression().get()).accept(this, arg);
            this.printer.print(".");
         }

         this.printTypeArgs(n, arg);
         this.printer.print("super");
      }

      this.printArguments(n.getArguments(), arg);
      this.printer.print(";");
   }

   public void visit(final VariableDeclarationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      Optional var10000 = n.getParentNode();
      ExpressionStmt.class.getClass();
      if ((Boolean)var10000.map(ExpressionStmt.class::isInstance).orElse(false)) {
         this.printMemberAnnotations(n.getAnnotations(), arg);
      } else {
         this.printAnnotations(n.getAnnotations(), false, arg);
      }

      this.printModifiers(n.getModifiers());
      if (!n.getVariables().isEmpty()) {
         n.getMaximumCommonType().ifPresent((t) -> {
            t.accept(this, arg);
         });
      }

      this.printer.print(" ");
      Iterator i = n.getVariables().iterator();

      while(i.hasNext()) {
         VariableDeclarator v = (VariableDeclarator)i.next();
         v.accept((VoidVisitor)this, arg);
         if (i.hasNext()) {
            this.printer.print(", ");
         }
      }

   }

   public void visit(final LocalClassDeclarationStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getClassDeclaration().accept((VoidVisitor)this, arg);
   }

   public void visit(final AssertStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("assert ");
      n.getCheck().accept(this, arg);
      if (n.getMessage().isPresent()) {
         this.printer.print(" : ");
         ((Expression)n.getMessage().get()).accept(this, arg);
      }

      this.printer.print(";");
   }

   public void visit(final BlockStmt n, final Void arg) {
      this.printOrphanCommentsBeforeThisChildNode(n);
      this.printComment(n.getComment(), arg);
      this.printer.println("{");
      if (n.getStatements() != null) {
         this.printer.indent();
         Iterator var3 = n.getStatements().iterator();

         while(var3.hasNext()) {
            Statement s = (Statement)var3.next();
            s.accept(this, arg);
            this.printer.println();
         }

         this.printer.unindent();
      }

      this.printOrphanCommentsEnding(n);
      this.printer.print("}");
   }

   public void visit(final LabeledStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getLabel().accept((VoidVisitor)this, arg);
      this.printer.print(": ");
      n.getStatement().accept(this, arg);
   }

   public void visit(final EmptyStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(";");
   }

   public void visit(final ExpressionStmt n, final Void arg) {
      this.printOrphanCommentsBeforeThisChildNode(n);
      this.printComment(n.getComment(), arg);
      n.getExpression().accept(this, arg);
      this.printer.print(";");
   }

   public void visit(final SwitchStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("switch(");
      n.getSelector().accept(this, arg);
      this.printer.println(") {");
      if (n.getEntries() != null) {
         this.printer.indent();
         Iterator var3 = n.getEntries().iterator();

         while(var3.hasNext()) {
            SwitchEntryStmt e = (SwitchEntryStmt)var3.next();
            e.accept((VoidVisitor)this, arg);
         }

         this.printer.unindent();
      }

      this.printer.print("}");
   }

   public void visit(final SwitchEntryStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getLabel().isPresent()) {
         this.printer.print("case ");
         ((Expression)n.getLabel().get()).accept(this, arg);
         this.printer.print(":");
      } else {
         this.printer.print("default:");
      }

      this.printer.println();
      this.printer.indent();
      if (n.getStatements() != null) {
         Iterator var3 = n.getStatements().iterator();

         while(var3.hasNext()) {
            Statement s = (Statement)var3.next();
            s.accept(this, arg);
            this.printer.println();
         }
      }

      this.printer.unindent();
   }

   public void visit(final BreakStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("break");
      n.getLabel().ifPresent((l) -> {
         this.printer.print(" ").print(l.getIdentifier());
      });
      this.printer.print(";");
   }

   public void visit(final ReturnStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("return");
      if (n.getExpression().isPresent()) {
         this.printer.print(" ");
         ((Expression)n.getExpression().get()).accept(this, arg);
      }

      this.printer.print(";");
   }

   public void visit(final EnumDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      this.printer.print("enum ");
      n.getName().accept((VoidVisitor)this, arg);
      if (!n.getImplementedTypes().isEmpty()) {
         this.printer.print(" implements ");
         Iterator i = n.getImplementedTypes().iterator();

         while(i.hasNext()) {
            ClassOrInterfaceType c = (ClassOrInterfaceType)i.next();
            c.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.println(" {");
      this.printer.indent();
      if (n.getEntries().isNonEmpty()) {
         boolean alignVertically = n.getEntries().size() > this.configuration.getMaxEnumConstantsToAlignHorizontally() || n.getEntries().stream().anyMatch((ex) -> {
            return ex.getComment().isPresent();
         });
         this.printer.println();
         Iterator i = n.getEntries().iterator();

         while(i.hasNext()) {
            EnumConstantDeclaration e = (EnumConstantDeclaration)i.next();
            e.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               if (alignVertically) {
                  this.printer.println(",");
               } else {
                  this.printer.print(", ");
               }
            }
         }
      }

      if (!n.getMembers().isEmpty()) {
         this.printer.println(";");
         this.printMembers(n.getMembers(), arg);
      } else if (!n.getEntries().isEmpty()) {
         this.printer.println();
      }

      this.printer.unindent();
      this.printer.print("}");
   }

   public void visit(final EnumConstantDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      n.getName().accept((VoidVisitor)this, arg);
      if (!n.getArguments().isEmpty()) {
         this.printArguments(n.getArguments(), arg);
      }

      if (!n.getClassBody().isEmpty()) {
         this.printer.println(" {");
         this.printer.indent();
         this.printMembers(n.getClassBody(), arg);
         this.printer.unindent();
         this.printer.println("}");
      }

   }

   public void visit(final InitializerDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.isStatic()) {
         this.printer.print("static ");
      }

      n.getBody().accept((VoidVisitor)this, arg);
   }

   public void visit(final IfStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("if (");
      n.getCondition().accept(this, arg);
      boolean thenBlock = n.getThenStmt() instanceof BlockStmt;
      if (thenBlock) {
         this.printer.print(") ");
      } else {
         this.printer.println(")");
         this.printer.indent();
      }

      n.getThenStmt().accept(this, arg);
      if (!thenBlock) {
         this.printer.unindent();
      }

      if (n.getElseStmt().isPresent()) {
         if (thenBlock) {
            this.printer.print(" ");
         } else {
            this.printer.println();
         }

         boolean elseIf = n.getElseStmt().orElse((Object)null) instanceof IfStmt;
         boolean elseBlock = n.getElseStmt().orElse((Object)null) instanceof BlockStmt;
         if (!elseIf && !elseBlock) {
            this.printer.println("else");
            this.printer.indent();
         } else {
            this.printer.print("else ");
         }

         if (n.getElseStmt().isPresent()) {
            ((Statement)n.getElseStmt().get()).accept(this, arg);
         }

         if (!elseIf && !elseBlock) {
            this.printer.unindent();
         }
      }

   }

   public void visit(final WhileStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("while (");
      n.getCondition().accept(this, arg);
      this.printer.print(") ");
      n.getBody().accept(this, arg);
   }

   public void visit(final ContinueStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("continue");
      n.getLabel().ifPresent((l) -> {
         this.printer.print(" ").print(l.getIdentifier());
      });
      this.printer.print(";");
   }

   public void visit(final DoStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("do ");
      n.getBody().accept(this, arg);
      this.printer.print(" while (");
      n.getCondition().accept(this, arg);
      this.printer.print(");");
   }

   public void visit(final ForeachStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("for (");
      n.getVariable().accept((VoidVisitor)this, arg);
      this.printer.print(" : ");
      n.getIterable().accept(this, arg);
      this.printer.print(") ");
      n.getBody().accept(this, arg);
   }

   public void visit(final ForStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("for (");
      Iterator i;
      Expression e;
      if (n.getInitialization() != null) {
         i = n.getInitialization().iterator();

         while(i.hasNext()) {
            e = (Expression)i.next();
            e.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print("; ");
      if (n.getCompare().isPresent()) {
         ((Expression)n.getCompare().get()).accept(this, arg);
      }

      this.printer.print("; ");
      if (n.getUpdate() != null) {
         i = n.getUpdate().iterator();

         while(i.hasNext()) {
            e = (Expression)i.next();
            e.accept(this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print(") ");
      n.getBody().accept(this, arg);
   }

   public void visit(final ThrowStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("throw ");
      n.getExpression().accept(this, arg);
      this.printer.print(";");
   }

   public void visit(final SynchronizedStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("synchronized (");
      n.getExpression().accept(this, arg);
      this.printer.print(") ");
      n.getBody().accept((VoidVisitor)this, arg);
   }

   public void visit(final TryStmt n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("try ");
      Iterator resources;
      if (!n.getResources().isEmpty()) {
         this.printer.print("(");
         resources = n.getResources().iterator();

         for(boolean first = true; resources.hasNext(); first = false) {
            ((Expression)resources.next()).accept(this, arg);
            if (resources.hasNext()) {
               this.printer.print(";");
               this.printer.println();
               if (first) {
                  this.printer.indent();
               }
            }
         }

         if (n.getResources().size() > 1) {
            this.printer.unindent();
         }

         this.printer.print(") ");
      }

      n.getTryBlock().accept((VoidVisitor)this, arg);
      resources = n.getCatchClauses().iterator();

      while(resources.hasNext()) {
         CatchClause c = (CatchClause)resources.next();
         c.accept((VoidVisitor)this, arg);
      }

      if (n.getFinallyBlock().isPresent()) {
         this.printer.print(" finally ");
         ((BlockStmt)n.getFinallyBlock().get()).accept((VoidVisitor)this, arg);
      }

   }

   public void visit(final CatchClause n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print(" catch (");
      n.getParameter().accept((VoidVisitor)this, arg);
      this.printer.print(") ");
      n.getBody().accept((VoidVisitor)this, arg);
   }

   public void visit(final AnnotationDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      this.printer.print("@interface ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.println(" {");
      this.printer.indent();
      if (n.getMembers() != null) {
         this.printMembers(n.getMembers(), arg);
      }

      this.printer.unindent();
      this.printer.print("}");
   }

   public void visit(final AnnotationMemberDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printMemberAnnotations(n.getAnnotations(), arg);
      this.printModifiers(n.getModifiers());
      n.getType().accept(this, arg);
      this.printer.print(" ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print("()");
      if (n.getDefaultValue().isPresent()) {
         this.printer.print(" default ");
         ((Expression)n.getDefaultValue().get()).accept(this, arg);
      }

      this.printer.print(";");
   }

   public void visit(final MarkerAnnotationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("@");
      n.getName().accept((VoidVisitor)this, arg);
   }

   public void visit(final SingleMemberAnnotationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("@");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print("(");
      n.getMemberValue().accept(this, arg);
      this.printer.print(")");
   }

   public void visit(final NormalAnnotationExpr n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("@");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print("(");
      if (n.getPairs() != null) {
         Iterator i = n.getPairs().iterator();

         while(i.hasNext()) {
            MemberValuePair m = (MemberValuePair)i.next();
            m.accept((VoidVisitor)this, arg);
            if (i.hasNext()) {
               this.printer.print(", ");
            }
         }
      }

      this.printer.print(")");
   }

   public void visit(final MemberValuePair n, final Void arg) {
      this.printComment(n.getComment(), arg);
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.print(" = ");
      n.getValue().accept(this, arg);
   }

   public void visit(final LineComment n, final Void arg) {
      if (!this.configuration.isIgnoreComments()) {
         this.printer.print("// ").println(Utils.normalizeEolInTextBlock(n.getContent(), "").trim());
      }
   }

   public void visit(final BlockComment n, final Void arg) {
      if (!this.configuration.isIgnoreComments()) {
         String commentContent = Utils.normalizeEolInTextBlock(n.getContent(), this.configuration.getEndOfLineCharacter());
         String[] lines = commentContent.split("\\R", -1);
         this.printer.print("/*");

         for(int i = 0; i < lines.length - 1; ++i) {
            this.printer.print(lines[i]);
            this.printer.print(this.configuration.getEndOfLineCharacter());
         }

         this.printer.print(lines[lines.length - 1]);
         this.printer.println("*/");
      }
   }

   public void visit(LambdaExpr n, Void arg) {
      this.printComment(n.getComment(), arg);
      NodeList<Parameter> parameters = n.getParameters();
      boolean printPar = n.isEnclosingParameters();
      if (printPar) {
         this.printer.print("(");
      }

      Iterator i = parameters.iterator();

      while(i.hasNext()) {
         Parameter p = (Parameter)i.next();
         p.accept((VoidVisitor)this, arg);
         if (i.hasNext()) {
            this.printer.print(", ");
         }
      }

      if (printPar) {
         this.printer.print(")");
      }

      this.printer.print(" -> ");
      Statement body = n.getBody();
      if (body instanceof ExpressionStmt) {
         ((ExpressionStmt)body).getExpression().accept(this, arg);
      } else {
         body.accept(this, arg);
      }

   }

   public void visit(MethodReferenceExpr n, Void arg) {
      this.printComment(n.getComment(), arg);
      Expression scope = n.getScope();
      String identifier = n.getIdentifier();
      if (scope != null) {
         n.getScope().accept(this, arg);
      }

      this.printer.print("::");
      this.printTypeArgs(n, arg);
      if (identifier != null) {
         this.printer.print(identifier);
      }

   }

   public void visit(TypeExpr n, Void arg) {
      this.printComment(n.getComment(), arg);
      if (n.getType() != null) {
         n.getType().accept(this, arg);
      }

   }

   public void visit(NodeList n, Void arg) {
      if (this.configuration.isOrderImports() && n.size() > 0 && n.get(0) instanceof ImportDeclaration) {
         NodeList<ImportDeclaration> modifiableList = new NodeList(n);
         modifiableList.sort(Comparator.comparingInt((i) -> {
            return i.isStatic() ? 0 : 1;
         }).thenComparing(NodeWithName::getNameAsString));
         Iterator var7 = modifiableList.iterator();

         while(var7.hasNext()) {
            Object node = var7.next();
            ((Node)node).accept(this, arg);
         }
      } else {
         Iterator var3 = n.iterator();

         while(var3.hasNext()) {
            Object node = var3.next();
            ((Node)node).accept(this, arg);
         }
      }

   }

   public void visit(final ImportDeclaration n, final Void arg) {
      this.printComment(n.getComment(), arg);
      this.printer.print("import ");
      if (n.isStatic()) {
         this.printer.print("static ");
      }

      n.getName().accept((VoidVisitor)this, arg);
      if (n.isAsterisk()) {
         this.printer.print(".*");
      }

      this.printer.println(";");
      this.printOrphanCommentsEnding(n);
   }

   public void visit(ModuleDeclaration n, Void arg) {
      this.printMemberAnnotations(n.getAnnotations(), arg);
      if (n.isOpen()) {
         this.printer.print("open ");
      }

      this.printer.print("module ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.println(" {").indent();
      n.getModuleStmts().accept((VoidVisitor)this, arg);
      this.printer.unindent().println("}");
   }

   public void visit(ModuleRequiresStmt n, Void arg) {
      this.printer.print("requires ");
      this.printModifiers(n.getModifiers());
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.println(";");
   }

   public void visit(ModuleExportsStmt n, Void arg) {
      this.printer.print("exports ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printPrePostFixOptionalList(n.getModuleNames(), arg, " to ", ", ", "");
      this.printer.println(";");
   }

   public void visit(ModuleProvidesStmt n, Void arg) {
      this.printer.print("provides ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printPrePostFixRequiredList(n.getWith(), arg, " with ", ", ", "");
      this.printer.println(";");
   }

   public void visit(ModuleUsesStmt n, Void arg) {
      this.printer.print("uses ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printer.println(";");
   }

   public void visit(ModuleOpensStmt n, Void arg) {
      this.printer.print("opens ");
      n.getName().accept((VoidVisitor)this, arg);
      this.printPrePostFixOptionalList(n.getModuleNames(), arg, " to ", ", ", "");
      this.printer.println(";");
   }

   public void visit(UnparsableStmt n, Void arg) {
      this.printer.print("???;");
   }

   private void printOrphanCommentsBeforeThisChildNode(final Node node) {
      if (!this.configuration.isIgnoreComments()) {
         if (!(node instanceof Comment)) {
            Node parent = (Node)node.getParentNode().orElse((Object)null);
            if (parent != null) {
               List<Node> everything = new LinkedList();
               everything.addAll(parent.getChildNodes());
               PositionUtils.sortByBeginPosition((List)everything);
               int positionOfTheChild = -1;

               int positionOfPreviousChild;
               for(positionOfPreviousChild = 0; positionOfPreviousChild < everything.size(); ++positionOfPreviousChild) {
                  if (everything.get(positionOfPreviousChild) == node) {
                     positionOfTheChild = positionOfPreviousChild;
                  }
               }

               if (positionOfTheChild == -1) {
                  throw new AssertionError("I am not a child of my parent.");
               } else {
                  positionOfPreviousChild = -1;

                  int i;
                  for(i = positionOfTheChild - 1; i >= 0 && positionOfPreviousChild == -1; --i) {
                     if (!(everything.get(i) instanceof Comment)) {
                        positionOfPreviousChild = i;
                     }
                  }

                  for(i = positionOfPreviousChild + 1; i < positionOfTheChild; ++i) {
                     Node nodeToPrint = (Node)everything.get(i);
                     if (!(nodeToPrint instanceof Comment)) {
                        throw new RuntimeException("Expected comment, instead " + nodeToPrint.getClass() + ". Position of previous child: " + positionOfPreviousChild + ", position of child " + positionOfTheChild);
                     }

                     nodeToPrint.accept(this, (Object)null);
                  }

               }
            }
         }
      }
   }

   private void printOrphanCommentsEnding(final Node node) {
      if (!this.configuration.isIgnoreComments()) {
         List<Node> everything = new LinkedList();
         everything.addAll(node.getChildNodes());
         PositionUtils.sortByBeginPosition((List)everything);
         if (!everything.isEmpty()) {
            int commentsAtEnd = 0;
            boolean findingComments = true;

            while(findingComments && commentsAtEnd < everything.size()) {
               Node last = (Node)everything.get(everything.size() - 1 - commentsAtEnd);
               findingComments = last instanceof Comment;
               if (findingComments) {
                  ++commentsAtEnd;
               }
            }

            for(int i = 0; i < commentsAtEnd; ++i) {
               ((Node)everything.get(everything.size() - commentsAtEnd + i)).accept(this, (Object)null);
            }

         }
      }
   }
}
