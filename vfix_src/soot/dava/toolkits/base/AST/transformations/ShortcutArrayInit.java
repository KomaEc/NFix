package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DArrayInitExpr;
import soot.dava.internal.javaRep.DArrayInitValueBox;
import soot.dava.internal.javaRep.DAssignStmt;
import soot.dava.internal.javaRep.DShortcutAssignStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.dava.toolkits.base.AST.traversals.InitializationDeclarationShortcut;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;

public class ShortcutArrayInit extends DepthFirstAdapter {
   public static boolean DEBUG = false;
   ASTMethodNode methodNode;

   public ShortcutArrayInit() {
   }

   public ShortcutArrayInit(boolean verbose) {
      super(verbose);
   }

   public void inASTMethodNode(ASTMethodNode node) {
      this.methodNode = node;
   }

   public void debug(String msg) {
      if (DEBUG) {
         System.out.println("[SHortcutArrayInit]  DEBUG" + msg);
      }

   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      this.debug("inASTStatementSequenceNode");
      boolean success = false;
      ArrayList<AugmentedStmt> toRemove = new ArrayList();
      Iterator var4 = node.getStatements().iterator();

      while(var4.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var4.next();
         success = false;
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt)s;
            ValueBox right = ds.getRightOpBox();
            Value rightValue = right.getValue();
            if (rightValue instanceof NewArrayExpr) {
               this.debug("Found a new ArrayExpr" + rightValue);
               this.debug("Type of array is:" + rightValue.getType());
               Type arrayType = rightValue.getType();
               Value size = ((NewArrayExpr)rightValue).getSize();
               if (size instanceof IntConstant) {
                  if (((IntConstant)size).value == 0) {
                     this.debug("Size of array is 0 dont do anything");
                  } else {
                     if (DEBUG) {
                        System.out.println("Size of array is: " + ((IntConstant)size).value);
                     }

                     Iterator tempIt = node.getStatements().iterator();

                     while(tempIt.hasNext()) {
                        AugmentedStmt tempAs = (AugmentedStmt)tempIt.next();
                        Stmt tempS = tempAs.get_Stmt();
                        if (tempS.equals(s)) {
                           break;
                        }
                     }

                     ValueBox[] array = new ValueBox[((IntConstant)size).value];
                     success = true;

                     for(int i = 0; i < ((IntConstant)size).value; ++i) {
                        if (!tempIt.hasNext()) {
                           if (DEBUG) {
                              System.out.println("returning");
                           }

                           return;
                        }

                        AugmentedStmt aug = (AugmentedStmt)tempIt.next();
                        Stmt augS = aug.get_Stmt();
                        if (!this.isInSequenceAssignment(augS, ds.getLeftOp(), i)) {
                           if (DEBUG) {
                              System.out.println("Out of order assignment aborting attempt");
                           }

                           success = false;
                           break;
                        }

                        if (DEBUG) {
                           System.out.println("Assignment stmt in order adding to array");
                        }

                        array[i] = ((DefinitionStmt)augS).getRightOpBox();
                        toRemove.add(aug);
                     }

                     if (success) {
                        DArrayInitExpr tempExpr = new DArrayInitExpr(array, arrayType);
                        DArrayInitValueBox tempValueBox = new DArrayInitValueBox(tempExpr);
                        DAssignStmt newStmt = new DAssignStmt(ds.getLeftOpBox(), tempValueBox);
                        if (DEBUG) {
                           System.out.println("Created new DAssignStmt and replacing it");
                        }

                        InitializationDeclarationShortcut shortcutChecker = new InitializationDeclarationShortcut(as);
                        this.methodNode.apply(shortcutChecker);
                        boolean possible = shortcutChecker.isShortcutPossible();
                        if (possible) {
                           if (DEBUG) {
                              System.out.println("Shortcut is possible");
                           }

                           DShortcutAssignStmt newShortcutStmt = new DShortcutAssignStmt(newStmt, arrayType);
                           as.set_Stmt(newShortcutStmt);
                           this.markLocal(ds.getLeftOp());
                        }
                        break;
                     }
                  }
               }
            }
         }
      }

      if (success) {
         List<AugmentedStmt> newStmtList = new ArrayList();
         Iterator var21 = node.getStatements().iterator();

         while(var21.hasNext()) {
            AugmentedStmt as = (AugmentedStmt)var21.next();
            if (toRemove.contains(as)) {
               toRemove.remove(as);
            } else {
               newStmtList.add(as);
            }
         }

         node.setStatements(newStmtList);
         this.inASTStatementSequenceNode(node);
         G.v().ASTTransformations_modified = true;
      }

      this.secondPattern(node);
   }

   public boolean isInSequenceAssignment(Stmt s, Value leftOp, int index) {
      if (!(s instanceof DefinitionStmt)) {
         return false;
      } else {
         DefinitionStmt ds = (DefinitionStmt)s;
         Value leftValue = ds.getLeftOp();
         if (!(leftValue instanceof ArrayRef)) {
            return false;
         } else {
            if (DEBUG) {
               System.out.println("Stmt number " + index + " is an array ref assignment" + leftValue);
               System.out.println("Array is" + leftOp);
            }

            ArrayRef leftRef = (ArrayRef)leftValue;
            if (!leftOp.equals(leftRef.getBase())) {
               if (DEBUG) {
                  System.out.println("Not assigning to same array");
               }

               return false;
            } else if (!(leftRef.getIndex() instanceof IntConstant)) {
               if (DEBUG) {
                  System.out.println("Cant determine index of assignment");
               }

               return false;
            } else {
               IntConstant leftIndex = (IntConstant)leftRef.getIndex();
               if (leftIndex.value != index) {
                  if (DEBUG) {
                     System.out.println("Out of order assignment");
                  }

                  return false;
               } else {
                  return true;
               }
            }
         }
      }
   }

   public void secondPattern(ASTStatementSequenceNode node) {
      boolean success = false;
      ArrayList<AugmentedStmt> toRemove = new ArrayList();
      Iterator var4 = node.getStatements().iterator();

      label115:
      while(var4.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var4.next();
         success = false;
         Stmt s = as.get_Stmt();
         if (s instanceof DefinitionStmt) {
            DefinitionStmt ds = (DefinitionStmt)s;
            ValueBox right = ds.getRightOpBox();
            Value rightValue = right.getValue();
            if (rightValue instanceof NewArrayExpr) {
               if (DEBUG) {
                  System.out.println("Found a new ArrayExpr" + rightValue);
                  System.out.println("Type of array is:" + rightValue.getType());
               }

               Type arrayType = rightValue.getType();
               Value size = ((NewArrayExpr)rightValue).getSize();
               if (size instanceof IntConstant) {
                  if (((IntConstant)size).value == 0) {
                     this.debug("Found value to be 0 doing nothing");
                  } else {
                     if (DEBUG) {
                        System.out.println("Size of array is: " + ((IntConstant)size).value);
                     }

                     Iterator tempIt = node.getStatements().iterator();

                     while(tempIt.hasNext()) {
                        AugmentedStmt tempAs = (AugmentedStmt)tempIt.next();
                        Stmt tempS = tempAs.get_Stmt();
                        if (tempS.equals(s)) {
                           break;
                        }
                     }

                     ValueBox[] array = new ValueBox[((IntConstant)size).value];
                     success = true;
                     int i = 0;

                     while(true) {
                        if (i < ((IntConstant)size).value) {
                           if (!tempIt.hasNext()) {
                              if (DEBUG) {
                                 System.out.println("returning");
                              }

                              return;
                           }

                           AugmentedStmt augOne = (AugmentedStmt)tempIt.next();
                           Stmt augSOne = augOne.get_Stmt();
                           if (!tempIt.hasNext()) {
                              if (DEBUG) {
                                 System.out.println("returning");
                              }

                              return;
                           }

                           AugmentedStmt augTwo = (AugmentedStmt)tempIt.next();
                           Stmt augSTwo = augTwo.get_Stmt();
                           if (this.isInSequenceAssignmentPatternTwo(augSOne, augSTwo, ds.getLeftOp(), i)) {
                              if (DEBUG) {
                                 System.out.println("Assignment stmt in order adding to array");
                              }

                              array[i] = ((DShortcutAssignStmt)augSOne).getRightOpBox();
                              toRemove.add(augOne);
                              toRemove.add(augTwo);
                              ++i;
                              continue;
                           }

                           if (DEBUG) {
                              System.out.println("Out of order assignment aborting attempt");
                           }

                           success = false;
                        }

                        if (!success) {
                           break;
                        }

                        DArrayInitExpr tempExpr = new DArrayInitExpr(array, arrayType);
                        DArrayInitValueBox tempValueBox = new DArrayInitValueBox(tempExpr);
                        DAssignStmt newStmt = new DAssignStmt(ds.getLeftOpBox(), tempValueBox);
                        if (DEBUG) {
                           System.out.println("Created new DAssignStmt and replacing it");
                        }

                        InitializationDeclarationShortcut shortcutChecker = new InitializationDeclarationShortcut(as);
                        this.methodNode.apply(shortcutChecker);
                        boolean possible = shortcutChecker.isShortcutPossible();
                        if (possible) {
                           if (DEBUG) {
                              System.out.println("Shortcut is possible");
                           }

                           DShortcutAssignStmt newShortcutStmt = new DShortcutAssignStmt(newStmt, arrayType);
                           as.set_Stmt(newShortcutStmt);
                           this.markLocal(ds.getLeftOp());
                        }
                        break label115;
                     }
                  }
               }
            }
         }
      }

      if (success) {
         List<AugmentedStmt> newStmtList = new ArrayList();
         Iterator var21 = node.getStatements().iterator();

         while(var21.hasNext()) {
            AugmentedStmt as = (AugmentedStmt)var21.next();
            if (toRemove.contains(as)) {
               toRemove.remove(as);
            } else {
               newStmtList.add(as);
            }
         }

         node.setStatements(newStmtList);
         this.inASTStatementSequenceNode(node);
         G.v().ASTTransformations_modified = true;
      }

   }

   public boolean isInSequenceAssignmentPatternTwo(Stmt one, Stmt two, Value leftOp, int index) {
      if (!(two instanceof DefinitionStmt)) {
         return false;
      } else {
         DefinitionStmt ds = (DefinitionStmt)two;
         Value leftValue = ds.getLeftOp();
         if (!(leftValue instanceof ArrayRef)) {
            return false;
         } else {
            ArrayRef leftRef = (ArrayRef)leftValue;
            if (!leftOp.equals(leftRef.getBase())) {
               if (DEBUG) {
                  System.out.println("Not assigning to same array");
               }

               return false;
            } else if (!(leftRef.getIndex() instanceof IntConstant)) {
               if (DEBUG) {
                  System.out.println("Cant determine index of assignment");
               }

               return false;
            } else {
               IntConstant leftIndex = (IntConstant)leftRef.getIndex();
               if (leftIndex.value != index) {
                  if (DEBUG) {
                     System.out.println("Out of order assignment");
                  }

                  return false;
               } else {
                  Value rightOp = ds.getRightOp();
                  if (!(one instanceof DShortcutAssignStmt)) {
                     return false;
                  } else {
                     DShortcutAssignStmt shortcut = (DShortcutAssignStmt)one;
                     Value shortcutVar = shortcut.getLeftOp();
                     return shortcutVar.equals(rightOp);
                  }
               }
            }
         }
      }
   }

   public void markLocal(Value shortcutLocal) {
      if (!(shortcutLocal instanceof Local)) {
         throw new DecompilationException("Found non local. report to developer.");
      } else {
         this.methodNode.addToDontPrintLocalsList((Local)shortcutLocal);
      }
   }
}
