package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTAggregatedCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DStaticFieldRef;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.util.Chain;

public class DeInliningFinalFields extends DepthFirstAdapter {
   SootClass sootClass = null;
   SootMethod sootMethod = null;
   DavaBody davaBody = null;
   HashMap<Comparable, SootField> finalFields;

   public DeInliningFinalFields() {
   }

   public DeInliningFinalFields(boolean verbose) {
      super(verbose);
   }

   public void inASTMethodNode(ASTMethodNode node) {
      DavaBody davaBody = node.getDavaBody();
      this.sootMethod = davaBody.getMethod();
      this.sootClass = this.sootMethod.getDeclaringClass();
      this.finalFields = new HashMap();
      ArrayList fieldChain = new ArrayList();
      Chain appClasses = Scene.v().getApplicationClasses();
      Iterator it = appClasses.iterator();

      while(it.hasNext()) {
         SootClass tempClass = (SootClass)it.next();
         Chain tempChain = tempClass.getFields();
         Iterator tempIt = tempChain.iterator();

         while(tempIt.hasNext()) {
            fieldChain.add(tempIt.next());
         }
      }

      Iterator fieldIt = fieldChain.iterator();

      while(true) {
         while(true) {
            SootField f;
            do {
               if (!fieldIt.hasNext()) {
                  return;
               }

               f = (SootField)fieldIt.next();
            } while(!f.isFinal());

            Type fieldType = f.getType();
            if (fieldType instanceof DoubleType && f.hasTag("DoubleConstantValueTag")) {
               double val = ((DoubleConstantValueTag)f.getTag("DoubleConstantValueTag")).getDoubleValue();
               this.finalFields.put(new Double(val), f);
            } else if (fieldType instanceof FloatType && f.hasTag("FloatConstantValueTag")) {
               float val = ((FloatConstantValueTag)f.getTag("FloatConstantValueTag")).getFloatValue();
               this.finalFields.put(new Float(val), f);
            } else if (fieldType instanceof LongType && f.hasTag("LongConstantValueTag")) {
               long val = ((LongConstantValueTag)f.getTag("LongConstantValueTag")).getLongValue();
               this.finalFields.put(new Long(val), f);
            } else {
               int val;
               if (fieldType instanceof CharType && f.hasTag("IntegerConstantValueTag")) {
                  val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                  this.finalFields.put(new Integer(val), f);
               } else if (fieldType instanceof BooleanType && f.hasTag("IntegerConstantValueTag")) {
                  val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                  if (val == 0) {
                     this.finalFields.put(new Boolean(false), f);
                  } else {
                     this.finalFields.put(new Boolean(true), f);
                  }
               } else if ((fieldType instanceof IntType || fieldType instanceof ByteType || fieldType instanceof ShortType) && f.hasTag("IntegerConstantValueTag")) {
                  val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                  this.finalFields.put(new Integer(val), f);
               } else if (f.hasTag("StringConstantValueTag")) {
                  String val = ((StringConstantValueTag)f.getTag("StringConstantValueTag")).getStringValue();
                  this.finalFields.put(val, f);
               }
            }
         }
      }
   }

   private boolean isConstant(Value val) {
      return val instanceof StringConstant || val instanceof DoubleConstant || val instanceof FloatConstant || val instanceof IntConstant || val instanceof LongConstant;
   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
   }

   public void checkAndSwitch(ValueBox valBox) {
      Value val = valBox.getValue();
      Object finalField = this.check(val);
      if (finalField != null) {
         SootField field = (SootField)finalField;
         if (this.sootClass.declaresField(field.getName(), field.getType())) {
            if (valBox.canContainValue(new DStaticFieldRef(field.makeRef(), true))) {
               valBox.setValue(new DStaticFieldRef(field.makeRef(), true));
            }
         } else if (valBox.canContainValue(new DStaticFieldRef(field.makeRef(), true))) {
            valBox.setValue(new DStaticFieldRef(field.makeRef(), false));
         }
      }

   }

   public Object check(Value val) {
      Object finalField = null;
      if (this.isConstant(val)) {
         String myString;
         if (val instanceof StringConstant) {
            myString = ((StringConstant)val).toString();
            myString = myString.substring(1, myString.length() - 1);
            finalField = this.finalFields.get(myString);
         } else if (val instanceof DoubleConstant) {
            myString = ((DoubleConstant)val).toString();
            finalField = this.finalFields.get(new Double(myString));
         } else if (val instanceof FloatConstant) {
            myString = ((FloatConstant)val).toString();
            finalField = this.finalFields.get(new Float(myString));
         } else if (val instanceof LongConstant) {
            myString = ((LongConstant)val).toString();
            finalField = this.finalFields.get(new Long(myString.substring(0, myString.length() - 1)));
         } else if (val instanceof IntConstant) {
            myString = ((IntConstant)val).toString();
            if (myString.length() == 0) {
               return null;
            }

            Type valType = ((IntConstant)val).getType();
            Integer myInt = null;

            try {
               if (myString.charAt(0) == '\'') {
                  if (myString.length() < 2) {
                     return null;
                  }

                  myInt = new Integer(myString.charAt(1));
               } else {
                  myInt = new Integer(myString);
               }
            } catch (Exception var7) {
               return finalField;
            }

            if (valType instanceof ByteType) {
               finalField = this.finalFields.get(myInt);
            } else if (valType instanceof IntType) {
               if (myString.equals("false")) {
                  finalField = this.finalFields.get(new Boolean(false));
               } else if (myString.equals("true")) {
                  finalField = this.finalFields.get(new Boolean(true));
               } else {
                  finalField = this.finalFields.get(myInt);
               }
            } else if (valType instanceof ShortType) {
               finalField = this.finalFields.get(myInt);
            }
         }
      }

      return finalField;
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      Value val = node.get_Key();
      if (this.isConstant(val)) {
         this.checkAndSwitch(node.getKeyBox());
      } else {
         Iterator it = val.getUseBoxes().iterator();

         while(it.hasNext()) {
            ValueBox tempBox = (ValueBox)it.next();
            this.checkAndSwitch(tempBox);
         }

      }
   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      Iterator var2 = node.getStatements().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         Iterator tempIt = s.getUseBoxes().iterator();

         while(tempIt.hasNext()) {
            ValueBox tempBox = (ValueBox)tempIt.next();
            this.checkAndSwitch(tempBox);
         }
      }

   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      Iterator var2 = node.getInit().iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Stmt s = as.get_Stmt();
         Iterator tempIt = s.getUseBoxes().iterator();

         while(tempIt.hasNext()) {
            ValueBox tempBox = (ValueBox)tempIt.next();
            this.checkAndSwitch(tempBox);
         }
      }

      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
      Iterator var9 = node.getUpdate().iterator();

      while(var9.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var9.next();
         Stmt s = as.get_Stmt();
         Iterator tempIt = s.getUseBoxes().iterator();

         while(tempIt.hasNext()) {
            ValueBox tempBox = (ValueBox)tempIt.next();
            this.checkAndSwitch(tempBox);
         }
      }

   }

   public void checkConditionalUses(Object cond, ASTNode node) {
      if (cond instanceof ASTAggregatedCondition) {
         this.checkConditionalUses(((ASTAggregatedCondition)cond).getLeftOp(), node);
         this.checkConditionalUses(((ASTAggregatedCondition)cond).getRightOp(), node);
      } else {
         if (cond instanceof ASTBinaryCondition) {
            Value val = ((ASTBinaryCondition)cond).getConditionExpr();
            Iterator tempIt = val.getUseBoxes().iterator();

            while(tempIt.hasNext()) {
               ValueBox tempBox = (ValueBox)tempIt.next();
               this.checkAndSwitch(tempBox);
            }
         }

      }
   }

   public void inASTIfNode(ASTIfNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTWhileNode(ASTWhileNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      ASTCondition cond = node.get_Condition();
      this.checkConditionalUses(cond, node);
   }
}
