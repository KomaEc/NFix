package org.codehaus.groovy.classgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;

public class ReturnAdder {
   public static void addReturnIfNeeded(MethodNode node) {
      Statement statement = node.getCode();
      if (!node.isVoidMethod()) {
         if (statement != null) {
            node.setCode(addReturnsIfNeeded(statement, node.getVariableScope()));
         }
      } else if (!node.isAbstract() && !(statement instanceof BytecodeSequence)) {
         BlockStatement newBlock = new BlockStatement();
         Statement code = node.getCode();
         if (code instanceof BlockStatement) {
            newBlock.setVariableScope(((BlockStatement)code).getVariableScope());
         }

         if (statement instanceof BlockStatement) {
            newBlock.addStatements(((BlockStatement)statement).getStatements());
         } else {
            newBlock.addStatement(statement);
         }

         newBlock.addStatement(ReturnStatement.RETURN_NULL_OR_VOID);
         newBlock.setSourcePosition(statement);
         node.setCode(newBlock);
      }

   }

   private static Statement addReturnsIfNeeded(Statement statement, VariableScope scope) {
      if (!(statement instanceof ReturnStatement) && !(statement instanceof BytecodeSequence) && !(statement instanceof ThrowStatement)) {
         if (statement instanceof EmptyStatement) {
            return new ReturnStatement(ConstantExpression.NULL);
         } else {
            ReturnStatement ret;
            if (statement instanceof ExpressionStatement) {
               ExpressionStatement expStmt = (ExpressionStatement)statement;
               Expression expr = expStmt.getExpression();
               ret = new ReturnStatement(expr);
               ret.setSourcePosition(expr);
               ret.setStatementLabel(statement.getStatementLabel());
               return ret;
            } else if (statement instanceof SynchronizedStatement) {
               SynchronizedStatement sync = (SynchronizedStatement)statement;
               sync.setCode(addReturnsIfNeeded(sync.getCode(), scope));
               return sync;
            } else if (statement instanceof IfStatement) {
               IfStatement ifs = (IfStatement)statement;
               ifs.setIfBlock(addReturnsIfNeeded(ifs.getIfBlock(), scope));
               ifs.setElseBlock(addReturnsIfNeeded(ifs.getElseBlock(), scope));
               return ifs;
            } else if (statement instanceof SwitchStatement) {
               SwitchStatement swi = (SwitchStatement)statement;
               Iterator i$ = swi.getCaseStatements().iterator();

               while(i$.hasNext()) {
                  CaseStatement caseStatement = (CaseStatement)i$.next();
                  caseStatement.setCode(adjustSwitchCaseCode(caseStatement.getCode(), scope, false));
               }

               swi.setDefaultStatement(adjustSwitchCaseCode(swi.getDefaultStatement(), scope, true));
               return swi;
            } else {
               int idx;
               if (!(statement instanceof TryCatchStatement)) {
                  if (statement instanceof BlockStatement) {
                     BlockStatement block = (BlockStatement)statement;
                     List list = block.getStatements();
                     if (!list.isEmpty()) {
                        idx = list.size() - 1;
                        Statement last = addReturnsIfNeeded((Statement)list.get(idx), block.getVariableScope());
                        list.set(idx, last);
                        if (!statementReturns(last)) {
                           list.add(new ReturnStatement(ConstantExpression.NULL));
                        }

                        BlockStatement newBlock = new BlockStatement(list, block.getVariableScope());
                        newBlock.setSourcePosition(block);
                        return newBlock;
                     } else {
                        ret = new ReturnStatement(ConstantExpression.NULL);
                        ret.setSourcePosition(block);
                        return ret;
                     }
                  } else if (statement == null) {
                     return new ReturnStatement(ConstantExpression.NULL);
                  } else {
                     List list = new ArrayList();
                     list.add(statement);
                     list.add(new ReturnStatement(ConstantExpression.NULL));
                     BlockStatement newBlock = new BlockStatement(list, new VariableScope(scope));
                     newBlock.setSourcePosition(statement);
                     return newBlock;
                  }
               } else {
                  TryCatchStatement trys = (TryCatchStatement)statement;
                  trys.setTryStatement(addReturnsIfNeeded(trys.getTryStatement(), scope));
                  int len = trys.getCatchStatements().size();

                  for(idx = 0; idx != len; ++idx) {
                     CatchStatement catchStatement = trys.getCatchStatement(idx);
                     catchStatement.setCode(addReturnsIfNeeded(catchStatement.getCode(), scope));
                  }

                  return trys;
               }
            }
         }
      } else {
         return statement;
      }
   }

   private static Statement adjustSwitchCaseCode(Statement statement, VariableScope scope, boolean defaultCase) {
      if (statement instanceof BlockStatement) {
         List list = ((BlockStatement)statement).getStatements();
         if (!list.isEmpty()) {
            int idx = list.size() - 1;
            Statement last = (Statement)list.get(idx);
            if (last instanceof BreakStatement) {
               list.remove(idx);
               return addReturnsIfNeeded(statement, scope);
            }

            if (defaultCase) {
               return addReturnsIfNeeded(statement, scope);
            }
         }
      }

      return statement;
   }

   private static boolean statementReturns(Statement last) {
      return last instanceof ReturnStatement || last instanceof BlockStatement || last instanceof IfStatement || last instanceof ExpressionStatement || last instanceof EmptyStatement || last instanceof TryCatchStatement || last instanceof BytecodeSequence || last instanceof ThrowStatement || last instanceof SynchronizedStatement;
   }
}
