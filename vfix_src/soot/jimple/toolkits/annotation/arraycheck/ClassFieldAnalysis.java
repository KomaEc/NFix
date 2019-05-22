package soot.jimple.toolkits.annotation.arraycheck;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalDefs;

public class ClassFieldAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(ClassFieldAnalysis.class);
   private final boolean final_in = true;
   private final boolean private_in = true;
   private final Map<SootClass, Hashtable<SootField, IntValueContainer>> classToFieldInfoMap = new HashMap();

   public ClassFieldAnalysis(Singletons.Global g) {
   }

   public static ClassFieldAnalysis v() {
      return G.v().soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis();
   }

   protected void internalTransform(SootClass c) {
      if (!this.classToFieldInfoMap.containsKey(c)) {
         Date start = new Date();
         if (Options.v().verbose()) {
            logger.debug("[] ClassFieldAnalysis started on : " + start + " for " + c.getPackageName() + c.getName());
         }

         Hashtable<SootField, IntValueContainer> fieldInfoTable = new Hashtable();
         this.classToFieldInfoMap.put(c, fieldInfoTable);
         HashSet<SootField> candidSet = new HashSet();
         int arrayTypeFieldNum = 0;
         Iterator fieldIt = c.getFields().iterator();

         while(true) {
            SootField field;
            int modifiers;
            Type type;
            do {
               do {
                  if (!fieldIt.hasNext()) {
                     if (arrayTypeFieldNum == 0) {
                        if (Options.v().verbose()) {
                           logger.debug("[] ClassFieldAnalysis finished with nothing");
                        }

                        return;
                     }

                     Iterator methodIt = c.methodIterator();

                     while(methodIt.hasNext()) {
                        this.ScanMethod((SootMethod)methodIt.next(), candidSet, fieldInfoTable);
                     }

                     Date finish = new Date();
                     if (Options.v().verbose()) {
                        long runtime = finish.getTime() - start.getTime();
                        long mins = runtime / 60000L;
                        long secs = runtime % 60000L / 1000L;
                        logger.debug("[] ClassFieldAnalysis finished normally. It took " + mins + " mins and " + secs + " secs.");
                     }

                     return;
                  }

                  field = (SootField)fieldIt.next();
                  modifiers = field.getModifiers();
                  type = field.getType();
               } while(!(type instanceof ArrayType));
            } while((modifiers & 16) == 0 && (modifiers & 2) == 0);

            candidSet.add(field);
            ++arrayTypeFieldNum;
         }
      }
   }

   public Object getFieldInfo(SootField field) {
      SootClass c = field.getDeclaringClass();
      Map<SootField, IntValueContainer> fieldInfoTable = (Map)this.classToFieldInfoMap.get(c);
      if (fieldInfoTable == null) {
         this.internalTransform(c);
         fieldInfoTable = (Map)this.classToFieldInfoMap.get(c);
      }

      return fieldInfoTable.get(field);
   }

   public void ScanMethod(SootMethod method, Set<SootField> candidates, Hashtable<SootField, IntValueContainer> fieldinfo) {
      if (method.isConcrete()) {
         Body body = method.retrieveActiveBody();
         if (body != null) {
            boolean hasArrayLocal = false;
            Collection<Local> locals = body.getLocals();
            Iterator localIt = locals.iterator();

            while(localIt.hasNext()) {
               Local local = (Local)localIt.next();
               Type type = local.getType();
               if (type instanceof ArrayType) {
                  hasArrayLocal = true;
                  break;
               }
            }

            if (hasArrayLocal) {
               HashMap<Stmt, SootField> stmtfield = new HashMap();
               Iterator unitIt = body.getUnits().iterator();

               while(unitIt.hasNext()) {
                  Stmt stmt = (Stmt)unitIt.next();
                  if (stmt.containsFieldRef()) {
                     Value leftOp = ((AssignStmt)stmt).getLeftOp();
                     if (leftOp instanceof FieldRef) {
                        FieldRef fref = (FieldRef)leftOp;
                        SootField field = fref.getField();
                        if (candidates.contains(field)) {
                           stmtfield.put(stmt, field);
                        }
                     }
                  }
               }

               if (stmtfield.size() != 0) {
                  if (Options.v().verbose()) {
                     logger.debug("[] ScanMethod for field started.");
                  }

                  LocalDefs localDefs = LocalDefs.Factory.newLocalDefs(body);
                  Set<Entry<Stmt, SootField>> entries = stmtfield.entrySet();
                  Iterator entryIt = entries.iterator();

                  label121:
                  while(true) {
                     SootField which;
                     IntValueContainer length;
                     Value rightOp;
                     Stmt where;
                     do {
                        if (!entryIt.hasNext()) {
                           if (Options.v().verbose()) {
                              logger.debug("[] ScanMethod finished.");
                           }

                           return;
                        }

                        Entry<Stmt, SootField> entry = (Entry)entryIt.next();
                        where = (Stmt)entry.getKey();
                        which = (SootField)entry.getValue();
                        length = new IntValueContainer();
                        rightOp = ((AssignStmt)where).getRightOp();
                     } while(!(rightOp instanceof Local));

                     Local local = (Local)rightOp;
                     DefinitionStmt usestmt = (DefinitionStmt)where;

                     while(true) {
                        while(true) {
                           while(length.isBottom()) {
                              List<Unit> defs = localDefs.getDefsOfAt(local, usestmt);
                              if (defs.size() == 1) {
                                 usestmt = (DefinitionStmt)defs.get(0);
                                 if (Options.v().debug()) {
                                    logger.debug("        " + usestmt);
                                 }

                                 Value tmp_rhs = usestmt.getRightOp();
                                 if (!(tmp_rhs instanceof NewArrayExpr) && !(tmp_rhs instanceof NewMultiArrayExpr)) {
                                    if (tmp_rhs instanceof IntConstant) {
                                       length.setValue(((IntConstant)tmp_rhs).value);
                                    } else if (tmp_rhs instanceof Local) {
                                       local = (Local)tmp_rhs;
                                    } else {
                                       length.setTop();
                                    }
                                 } else {
                                    Value size;
                                    if (tmp_rhs instanceof NewArrayExpr) {
                                       size = ((NewArrayExpr)tmp_rhs).getSize();
                                    } else {
                                       size = ((NewMultiArrayExpr)tmp_rhs).getSize(0);
                                    }

                                    if (size instanceof IntConstant) {
                                       length.setValue(((IntConstant)size).value);
                                    } else if (size instanceof Local) {
                                       local = (Local)size;
                                    } else {
                                       length.setTop();
                                    }
                                 }
                              } else {
                                 length.setTop();
                              }
                           }

                           IntValueContainer oldv = (IntValueContainer)fieldinfo.get(which);
                           if (length.isTop()) {
                              if (oldv == null) {
                                 fieldinfo.put(which, length.dup());
                              } else {
                                 oldv.setTop();
                              }

                              candidates.remove(which);
                           } else if (length.isInteger()) {
                              if (oldv == null) {
                                 fieldinfo.put(which, length.dup());
                              } else if (oldv.isInteger() && oldv.getValue() != length.getValue()) {
                                 oldv.setTop();
                                 candidates.remove(which);
                              }
                           }
                           continue label121;
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
