package com.github.javaparser.ast.visitor;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
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
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import java.util.Iterator;

public abstract class GenericVisitorAdapter<R, A> implements GenericVisitor<R, A> {
   public R visit(final AnnotationDeclaration n, final A arg) {
      R result = n.getMembers().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getAnnotations().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final AnnotationMemberDeclaration n, final A arg) {
      Object result;
      if (n.getDefaultValue().isPresent()) {
         result = ((Expression)n.getDefaultValue().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getType().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getAnnotations().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final ArrayAccessExpr n, final A arg) {
      R result = n.getIndex().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ArrayCreationExpr n, final A arg) {
      R result = n.getElementType().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getInitializer().isPresent()) {
            result = ((ArrayInitializerExpr)n.getInitializer().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getLevels().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ArrayInitializerExpr n, final A arg) {
      R result = n.getValues().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final AssertStmt n, final A arg) {
      R result = n.getCheck().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getMessage().isPresent()) {
            result = ((Expression)n.getMessage().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final AssignExpr n, final A arg) {
      R result = n.getTarget().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getValue().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final BinaryExpr n, final A arg) {
      R result = n.getLeft().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getRight().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final BlockStmt n, final A arg) {
      R result = n.getStatements().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final BooleanLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final BreakStmt n, final A arg) {
      Object result;
      if (n.getLabel().isPresent()) {
         result = ((SimpleName)n.getLabel().get()).accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getComment().isPresent()) {
         result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final CastExpr n, final A arg) {
      R result = n.getExpression().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getType().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final CatchClause n, final A arg) {
      R result = n.getBody().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getParameter().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final CharLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final ClassExpr n, final A arg) {
      R result = n.getType().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final ClassOrInterfaceDeclaration n, final A arg) {
      R result = n.getExtendedTypes().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getImplementedTypes().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getTypeParameters().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               result = n.getMembers().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  result = n.getName().accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  } else {
                     result = n.getAnnotations().accept((GenericVisitor)this, arg);
                     if (result != null) {
                        return result;
                     } else {
                        if (n.getComment().isPresent()) {
                           result = ((Comment)n.getComment().get()).accept(this, arg);
                           if (result != null) {
                              return result;
                           }
                        }

                        return null;
                     }
                  }
               }
            }
         }
      }
   }

   public R visit(final ClassOrInterfaceType n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getScope().isPresent()) {
            result = ((ClassOrInterfaceType)n.getScope().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getTypeArguments().isPresent()) {
            result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final CompilationUnit n, final A arg) {
      R result = n.getImports().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getModule().isPresent()) {
            result = ((ModuleDeclaration)n.getModule().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getPackageDeclaration().isPresent()) {
            result = ((PackageDeclaration)n.getPackageDeclaration().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getTypes().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ConditionalExpr n, final A arg) {
      R result = n.getCondition().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getElseExpr().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getThenExpr().accept(this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final ConstructorDeclaration n, final A arg) {
      R result = n.getBody().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getParameters().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getReceiverParameter().isPresent()) {
                  result = ((ReceiverParameter)n.getReceiverParameter().get()).accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               result = n.getThrownExceptions().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  result = n.getTypeParameters().accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  } else {
                     result = n.getAnnotations().accept((GenericVisitor)this, arg);
                     if (result != null) {
                        return result;
                     } else {
                        if (n.getComment().isPresent()) {
                           result = ((Comment)n.getComment().get()).accept(this, arg);
                           if (result != null) {
                              return result;
                           }
                        }

                        return null;
                     }
                  }
               }
            }
         }
      }
   }

   public R visit(final ContinueStmt n, final A arg) {
      Object result;
      if (n.getLabel().isPresent()) {
         result = ((SimpleName)n.getLabel().get()).accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getComment().isPresent()) {
         result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final DoStmt n, final A arg) {
      R result = n.getBody().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getCondition().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final DoubleLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final EmptyStmt n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final EnclosedExpr n, final A arg) {
      R result = n.getInner().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final EnumConstantDeclaration n, final A arg) {
      R result = n.getArguments().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getClassBody().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getName().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               result = n.getAnnotations().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  if (n.getComment().isPresent()) {
                     result = ((Comment)n.getComment().get()).accept(this, arg);
                     if (result != null) {
                        return result;
                     }
                  }

                  return null;
               }
            }
         }
      }
   }

   public R visit(final EnumDeclaration n, final A arg) {
      R result = n.getEntries().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getImplementedTypes().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getMembers().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               result = n.getName().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  result = n.getAnnotations().accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  } else {
                     if (n.getComment().isPresent()) {
                        result = ((Comment)n.getComment().get()).accept(this, arg);
                        if (result != null) {
                           return result;
                        }
                     }

                     return null;
                  }
               }
            }
         }
      }
   }

   public R visit(final ExplicitConstructorInvocationStmt n, final A arg) {
      R result = n.getArguments().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getExpression().isPresent()) {
            result = ((Expression)n.getExpression().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getTypeArguments().isPresent()) {
            result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final ExpressionStmt n, final A arg) {
      R result = n.getExpression().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final FieldAccessExpr n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getScope().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getTypeArguments().isPresent()) {
               result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               }
            }

            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final FieldDeclaration n, final A arg) {
      R result = n.getVariables().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ForeachStmt n, final A arg) {
      R result = n.getBody().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getIterable().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getVariable().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final ForStmt n, final A arg) {
      R result = n.getBody().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getCompare().isPresent()) {
            result = ((Expression)n.getCompare().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getInitialization().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getUpdate().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final IfStmt n, final A arg) {
      R result = n.getCondition().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getElseStmt().isPresent()) {
            result = ((Statement)n.getElseStmt().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getThenStmt().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final InitializerDeclaration n, final A arg) {
      R result = n.getBody().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final InstanceOfExpr n, final A arg) {
      R result = n.getExpression().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getType().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final IntegerLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final JavadocComment n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final LabeledStmt n, final A arg) {
      R result = n.getLabel().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getStatement().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final LongLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final MarkerAnnotationExpr n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final MemberValuePair n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getValue().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final MethodCallExpr n, final A arg) {
      R result = n.getArguments().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getScope().isPresent()) {
               result = ((Expression)n.getScope().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            if (n.getTypeArguments().isPresent()) {
               result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               }
            }

            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final MethodDeclaration n, final A arg) {
      Object result;
      if (n.getBody().isPresent()) {
         result = ((BlockStmt)n.getBody().get()).accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getType().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getParameters().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getReceiverParameter().isPresent()) {
                  result = ((ReceiverParameter)n.getReceiverParameter().get()).accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               result = n.getThrownExceptions().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  result = n.getTypeParameters().accept((GenericVisitor)this, arg);
                  if (result != null) {
                     return result;
                  } else {
                     result = n.getAnnotations().accept((GenericVisitor)this, arg);
                     if (result != null) {
                        return result;
                     } else {
                        if (n.getComment().isPresent()) {
                           result = ((Comment)n.getComment().get()).accept(this, arg);
                           if (result != null) {
                              return result;
                           }
                        }

                        return null;
                     }
                  }
               }
            }
         }
      }
   }

   public R visit(final NameExpr n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final NormalAnnotationExpr n, final A arg) {
      R result = n.getPairs().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final NullLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final ObjectCreationExpr n, final A arg) {
      Object result;
      if (n.getAnonymousClassBody().isPresent()) {
         result = ((NodeList)n.getAnonymousClassBody().get()).accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getArguments().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getScope().isPresent()) {
            result = ((Expression)n.getScope().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getType().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getTypeArguments().isPresent()) {
               result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               }
            }

            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final PackageDeclaration n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final Parameter n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getType().accept(this, arg);
            if (result != null) {
               return result;
            } else {
               result = n.getVarArgsAnnotations().accept((GenericVisitor)this, arg);
               if (result != null) {
                  return result;
               } else {
                  if (n.getComment().isPresent()) {
                     result = ((Comment)n.getComment().get()).accept(this, arg);
                     if (result != null) {
                        return result;
                     }
                  }

                  return null;
               }
            }
         }
      }
   }

   public R visit(final PrimitiveType n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final Name n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getQualifier().isPresent()) {
            result = ((Name)n.getQualifier().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final SimpleName n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final ArrayType n, final A arg) {
      R result = n.getComponentType().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ArrayCreationLevel n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getDimension().isPresent()) {
            result = ((Expression)n.getDimension().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final IntersectionType n, final A arg) {
      R result = n.getElements().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final UnionType n, final A arg) {
      R result = n.getElements().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getAnnotations().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ReturnStmt n, final A arg) {
      Object result;
      if (n.getExpression().isPresent()) {
         result = ((Expression)n.getExpression().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getComment().isPresent()) {
         result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final SingleMemberAnnotationExpr n, final A arg) {
      R result = n.getMemberValue().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final StringLiteralExpr n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final SuperExpr n, final A arg) {
      Object result;
      if (n.getClassExpr().isPresent()) {
         result = ((Expression)n.getClassExpr().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getComment().isPresent()) {
         result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final SwitchEntryStmt n, final A arg) {
      Object result;
      if (n.getLabel().isPresent()) {
         result = ((Expression)n.getLabel().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getStatements().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final SwitchStmt n, final A arg) {
      R result = n.getEntries().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getSelector().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final SynchronizedStmt n, final A arg) {
      R result = n.getBody().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getExpression().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ThisExpr n, final A arg) {
      Object result;
      if (n.getClassExpr().isPresent()) {
         result = ((Expression)n.getClassExpr().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getComment().isPresent()) {
         result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final ThrowStmt n, final A arg) {
      R result = n.getExpression().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final TryStmt n, final A arg) {
      R result = n.getCatchClauses().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getFinallyBlock().isPresent()) {
            result = ((BlockStmt)n.getFinallyBlock().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         result = n.getResources().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getTryBlock().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final LocalClassDeclarationStmt n, final A arg) {
      R result = n.getClassDeclaration().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final TypeParameter n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getTypeBound().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getAnnotations().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final UnaryExpr n, final A arg) {
      R result = n.getExpression().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final UnknownType n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final VariableDeclarationExpr n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getVariables().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final VariableDeclarator n, final A arg) {
      Object result;
      if (n.getInitializer().isPresent()) {
         result = ((Expression)n.getInitializer().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getType().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final VoidType n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final WhileStmt n, final A arg) {
      R result = n.getBody().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getCondition().accept(this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final WildcardType n, final A arg) {
      Object result;
      if (n.getExtendedType().isPresent()) {
         result = ((ReferenceType)n.getExtendedType().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      if (n.getSuperType().isPresent()) {
         result = ((ReferenceType)n.getSuperType().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final LambdaExpr n, final A arg) {
      R result = n.getBody().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getParameters().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final MethodReferenceExpr n, final A arg) {
      R result = n.getScope().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getTypeArguments().isPresent()) {
            result = ((NodeList)n.getTypeArguments().get()).accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            }
         }

         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final TypeExpr n, final A arg) {
      R result = n.getType().accept(this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final ImportDeclaration n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final BlockComment n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final LineComment n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(NodeList n, A arg) {
      Iterator var3 = n.iterator();

      Object result;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         Object v = var3.next();
         result = ((Node)v).accept(this, arg);
      } while(result == null);

      return result;
   }

   public R visit(final ModuleDeclaration n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getModuleStmts().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getName().accept((GenericVisitor)this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final ModuleRequiresStmt n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final ModuleExportsStmt n, final A arg) {
      R result = n.getModuleNames().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ModuleProvidesStmt n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getWith().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final ModuleUsesStmt n, final A arg) {
      R result = n.getName().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }

   public R visit(final ModuleOpensStmt n, final A arg) {
      R result = n.getModuleNames().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            if (n.getComment().isPresent()) {
               result = ((Comment)n.getComment().get()).accept(this, arg);
               if (result != null) {
                  return result;
               }
            }

            return null;
         }
      }
   }

   public R visit(final UnparsableStmt n, final A arg) {
      if (n.getComment().isPresent()) {
         R result = ((Comment)n.getComment().get()).accept(this, arg);
         if (result != null) {
            return result;
         }
      }

      return null;
   }

   public R visit(final ReceiverParameter n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         result = n.getName().accept((GenericVisitor)this, arg);
         if (result != null) {
            return result;
         } else {
            result = n.getType().accept(this, arg);
            if (result != null) {
               return result;
            } else {
               if (n.getComment().isPresent()) {
                  result = ((Comment)n.getComment().get()).accept(this, arg);
                  if (result != null) {
                     return result;
                  }
               }

               return null;
            }
         }
      }
   }

   public R visit(final VarType n, final A arg) {
      R result = n.getAnnotations().accept((GenericVisitor)this, arg);
      if (result != null) {
         return result;
      } else {
         if (n.getComment().isPresent()) {
            result = ((Comment)n.getComment().get()).accept(this, arg);
            if (result != null) {
               return result;
            }
         }

         return null;
      }
   }
}
