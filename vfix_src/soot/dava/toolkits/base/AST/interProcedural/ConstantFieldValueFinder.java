package soot.dava.toolkits.base.AST.interProcedural;

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
import soot.PrimType;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTNode;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NumericConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.util.Chain;

public class ConstantFieldValueFinder {
   public final boolean DEBUG = false;
   public static String combiner = "_$p$g_";
   HashMap<String, SootField> classNameFieldNameToSootFieldMapping = new HashMap();
   HashMap<String, ArrayList> fieldToValues = new HashMap();
   HashMap<String, Object> primTypeFieldValueToUse = new HashMap();
   Chain appClasses;

   public ConstantFieldValueFinder(Chain classes) {
      this.appClasses = classes;
      this.debug("ConstantFieldValueFinder -- applyAnalyses", "computing Method Summaries");
      this.computeFieldToValuesAssignedList();
      this.valuesForPrimTypeFields();
   }

   public HashMap<String, Object> getFieldsWithConstantValues() {
      return this.primTypeFieldValueToUse;
   }

   public HashMap<String, SootField> getClassNameFieldNameToSootFieldMapping() {
      return this.classNameFieldNameToSootFieldMapping;
   }

   private void valuesForPrimTypeFields() {
      Iterator classIt = this.appClasses.iterator();

      label163:
      while(classIt.hasNext()) {
         SootClass s = (SootClass)classIt.next();
         this.debug("\nvaluesforPrimTypeFields", "Processing class " + s.getName());
         String declaringClass = s.getName();
         Iterator fieldIt = s.getFields().iterator();

         while(true) {
            while(true) {
               SootField f;
               String fieldName;
               Type fieldType;
               do {
                  if (!fieldIt.hasNext()) {
                     continue label163;
                  }

                  f = (SootField)fieldIt.next();
                  fieldName = f.getName();
                  fieldType = f.getType();
               } while(!(fieldType instanceof PrimType));

               String combined = declaringClass + combiner + fieldName;
               this.classNameFieldNameToSootFieldMapping.put(combined, f);
               Object value = null;
               if (fieldType instanceof DoubleType && f.hasTag("DoubleConstantValueTag")) {
                  double val = ((DoubleConstantValueTag)f.getTag("DoubleConstantValueTag")).getDoubleValue();
                  value = new Double(val);
               } else if (fieldType instanceof FloatType && f.hasTag("FloatConstantValueTag")) {
                  float val = ((FloatConstantValueTag)f.getTag("FloatConstantValueTag")).getFloatValue();
                  value = new Float(val);
               } else if (fieldType instanceof LongType && f.hasTag("LongConstantValueTag")) {
                  long val = ((LongConstantValueTag)f.getTag("LongConstantValueTag")).getLongValue();
                  value = new Long(val);
               } else {
                  int val;
                  if (fieldType instanceof CharType && f.hasTag("IntegerConstantValueTag")) {
                     val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                     value = new Integer(val);
                  } else if (fieldType instanceof BooleanType && f.hasTag("IntegerConstantValueTag")) {
                     val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                     if (val == 0) {
                        value = new Boolean(false);
                     } else {
                        value = new Boolean(true);
                     }
                  } else if ((fieldType instanceof IntType || fieldType instanceof ByteType || fieldType instanceof ShortType) && f.hasTag("IntegerConstantValueTag")) {
                     val = ((IntegerConstantValueTag)f.getTag("IntegerConstantValueTag")).getIntValue();
                     value = new Integer(val);
                  }
               }

               if (value != null) {
                  this.debug("TAGGED value found for field: " + combined);
                  this.primTypeFieldValueToUse.put(combined, value);
               } else {
                  Object temp = this.fieldToValues.get(combined);
                  if (temp == null) {
                     if (fieldType instanceof DoubleType) {
                        value = new Double(0.0D);
                     } else if (fieldType instanceof FloatType) {
                        value = new Float(0.0F);
                     } else if (fieldType instanceof LongType) {
                        value = new Long(0L);
                     } else if (fieldType instanceof BooleanType) {
                        value = new Boolean(false);
                     } else {
                        if (!(fieldType instanceof IntType) && !(fieldType instanceof ByteType) && !(fieldType instanceof ShortType) && !(fieldType instanceof CharType)) {
                           throw new DecompilationException("Unknown primitive type...please report to developer");
                        }

                        value = new Integer(0);
                     }

                     this.primTypeFieldValueToUse.put(combined, value);
                     this.debug("DEFAULT value for field: " + combined);
                  } else {
                     this.debug("CHECKING USER ASSIGNED VALUES FOR: " + combined);
                     ArrayList values = (ArrayList)temp;
                     Iterator it = values.iterator();
                     NumericConstant tempConstant = null;

                     while(it.hasNext()) {
                        Value val = (Value)it.next();
                        if (!(val instanceof NumericConstant)) {
                           tempConstant = null;
                           this.debug("Not numeric constant hence giving up");
                           break;
                        }

                        if (tempConstant == null) {
                           tempConstant = (NumericConstant)val;
                        } else if (!tempConstant.equals(val)) {
                           tempConstant = null;
                           break;
                        }
                     }

                     if (tempConstant != null) {
                        if (tempConstant instanceof LongConstant) {
                           Long tempVal = new Long(((LongConstant)tempConstant).value);
                           if (tempVal.compareTo(new Long(0L)) == 0) {
                              this.primTypeFieldValueToUse.put(combined, tempVal);
                           } else {
                              this.debug("Not assigning the agreed value since that is not the default value for " + combined);
                           }
                        } else if (tempConstant instanceof DoubleConstant) {
                           Double tempVal = new Double(((DoubleConstant)tempConstant).value);
                           if (tempVal.compareTo(new Double(0.0D)) == 0) {
                              this.primTypeFieldValueToUse.put(combined, tempVal);
                           } else {
                              this.debug("Not assigning the agreed value since that is not the default value for " + combined);
                           }
                        } else if (tempConstant instanceof FloatConstant) {
                           Float tempVal = new Float(((FloatConstant)tempConstant).value);
                           if (tempVal.compareTo(new Float(0.0F)) == 0) {
                              this.primTypeFieldValueToUse.put(combined, tempVal);
                           } else {
                              this.debug("Not assigning the agreed value since that is not the default value for " + combined);
                           }
                        } else {
                           if (!(tempConstant instanceof IntConstant)) {
                              throw new DecompilationException("Un handled Numberic Constant....report to programmer");
                           }

                           Integer tempVal = new Integer(((IntConstant)tempConstant).value);
                           if (tempVal.compareTo(new Integer(0)) == 0) {
                              SootField tempField = (SootField)this.classNameFieldNameToSootFieldMapping.get(combined);
                              if (tempField.getType() instanceof BooleanType) {
                                 this.primTypeFieldValueToUse.put(combined, new Boolean(false));
                              } else {
                                 this.primTypeFieldValueToUse.put(combined, tempVal);
                              }
                           } else {
                              this.debug("Not assigning the agreed value since that is not the default value for " + combined);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private void computeFieldToValuesAssignedList() {
      Iterator classIt = this.appClasses.iterator();

      label42:
      while(classIt.hasNext()) {
         SootClass s = (SootClass)classIt.next();
         this.debug("\ncomputeMethodSummaries", "Processing class " + s.getName());
         Iterator methodIt = s.methodIterator();

         while(true) {
            SootMethod m;
            DavaBody body;
            do {
               if (!methodIt.hasNext()) {
                  continue label42;
               }

               m = (SootMethod)methodIt.next();
               body = null;
            } while(!m.hasActiveBody());

            body = (DavaBody)m.getActiveBody();
            ASTNode AST = (ASTNode)body.getUnits().getFirst();
            AllDefinitionsFinder defFinder = new AllDefinitionsFinder();
            AST.apply(defFinder);
            Iterator allDefIt = defFinder.getAllDefs().iterator();

            while(allDefIt.hasNext()) {
               DefinitionStmt stmt = (DefinitionStmt)allDefIt.next();
               Value left = stmt.getLeftOp();
               if (left instanceof FieldRef) {
                  this.debug("computeMethodSummaries method: " + m.getName(), "Field ref is: " + left);
                  FieldRef ref = (FieldRef)left;
                  SootField field = ref.getField();
                  if (field.getType() instanceof PrimType) {
                     String fieldName = field.getName();
                     String declaringClass = field.getDeclaringClass().getName();
                     this.debug("\tField Name: " + fieldName);
                     this.debug("\tField DeclaringClass: " + declaringClass);
                     String combined = declaringClass + combiner + fieldName;
                     Object temp = this.fieldToValues.get(combined);
                     ArrayList valueList;
                     if (temp == null) {
                        valueList = new ArrayList();
                        this.fieldToValues.put(combined, valueList);
                     } else {
                        valueList = (ArrayList)temp;
                     }

                     valueList.add(stmt.getRightOp());
                  }
               }
            }
         }
      }

   }

   public void printConstantValueFields() {
      System.out.println("\n\n Printing Constant Value Fields (method: printConstantValueFields)");
      Iterator it = this.primTypeFieldValueToUse.keySet().iterator();

      while(it.hasNext()) {
         String combined = (String)it.next();
         int temp = combined.indexOf(combiner, 0);
         if (temp > 0) {
            System.out.println("Class: " + combined.substring(0, temp) + " Field: " + combined.substring(temp + combiner.length()) + " Value: " + this.primTypeFieldValueToUse.get(combined));
         }
      }

   }

   public void debug(String methodName, String debug) {
   }

   public void debug(String debug) {
   }
}
