package soot.jimple.toolkits.typing.integer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.BooleanType;
import soot.ByteType;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;

public class TypeResolver {
   private static final Logger logger = LoggerFactory.getLogger(TypeResolver.class);
   private final List<TypeVariable> typeVariableList = new ArrayList();
   private final Map<Object, TypeVariable> typeVariableMap = new HashMap();
   private final JimpleBody stmtBody;
   final TypeVariable BOOLEAN;
   final TypeVariable BYTE;
   final TypeVariable SHORT;
   final TypeVariable CHAR;
   final TypeVariable INT;
   final TypeVariable TOP;
   final TypeVariable R0_1;
   final TypeVariable R0_127;
   final TypeVariable R0_32767;
   private static final boolean DEBUG = false;
   private static final boolean IMPERFORMANT_TYPE_CHECK = false;
   private Collection<TypeVariable> unsolved;
   private Collection<TypeVariable> solved;

   TypeVariable typeVariable(Local local) {
      TypeVariable result = (TypeVariable)this.typeVariableMap.get(local);
      if (result == null) {
         int id = this.typeVariableList.size();
         this.typeVariableList.add((Object)null);
         result = new TypeVariable(id, this);
         this.typeVariableList.set(id, result);
         this.typeVariableMap.put(local, result);
      }

      return result;
   }

   public TypeVariable typeVariable(TypeNode typeNode) {
      TypeVariable result = (TypeVariable)this.typeVariableMap.get(typeNode);
      if (result == null) {
         int id = this.typeVariableList.size();
         this.typeVariableList.add((Object)null);
         result = new TypeVariable(id, this, typeNode);
         this.typeVariableList.set(id, result);
         this.typeVariableMap.put(typeNode, result);
      }

      return result;
   }

   public TypeVariable typeVariable(Type type) {
      return this.typeVariable(ClassHierarchy.v().typeNode(type));
   }

   public TypeVariable typeVariable() {
      int id = this.typeVariableList.size();
      this.typeVariableList.add((Object)null);
      TypeVariable result = new TypeVariable(id, this);
      this.typeVariableList.set(id, result);
      return result;
   }

   private TypeResolver(JimpleBody stmtBody) {
      this.BOOLEAN = this.typeVariable(ClassHierarchy.v().BOOLEAN);
      this.BYTE = this.typeVariable(ClassHierarchy.v().BYTE);
      this.SHORT = this.typeVariable(ClassHierarchy.v().SHORT);
      this.CHAR = this.typeVariable(ClassHierarchy.v().CHAR);
      this.INT = this.typeVariable(ClassHierarchy.v().INT);
      this.TOP = this.typeVariable(ClassHierarchy.v().TOP);
      this.R0_1 = this.typeVariable(ClassHierarchy.v().R0_1);
      this.R0_127 = this.typeVariable(ClassHierarchy.v().R0_127);
      this.R0_32767 = this.typeVariable(ClassHierarchy.v().R0_32767);
      this.stmtBody = stmtBody;
   }

   public static void resolve(JimpleBody stmtBody) {
      try {
         TypeResolver resolver = new TypeResolver(stmtBody);
         resolver.resolve_step_1();
      } catch (TypeException var6) {
         try {
            TypeResolver resolver = new TypeResolver(stmtBody);
            resolver.resolve_step_2();
         } catch (TypeException var5) {
            StringWriter st = new StringWriter();
            PrintWriter pw = new PrintWriter(st);
            logger.error((String)var5.getMessage(), (Throwable)var5);
            pw.close();
            throw new RuntimeException(st.toString());
         }
      }

   }

   private void debug_vars(String message) {
   }

   private void resolve_step_1() throws TypeException {
      this.collect_constraints_1();
      this.debug_vars("constraints");
      this.compute_approximate_types();
      this.merge_connected_components();
      this.debug_vars("components");
      this.merge_single_constraints();
      this.debug_vars("single");
      this.assign_types_1();
      this.debug_vars("assign");
      this.check_and_fix_constraints();
   }

   private void resolve_step_2() throws TypeException {
      this.collect_constraints_2();
      this.compute_approximate_types();
      this.assign_types_2();
      this.check_and_fix_constraints();
   }

   private void collect_constraints_1() {
      ConstraintCollector collector = new ConstraintCollector(this, true);
      Iterator var2 = this.stmtBody.getUnits().iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         Stmt stmt = (Stmt)u;
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void collect_constraints_2() {
      ConstraintCollector collector = new ConstraintCollector(this, false);
      Iterator var2 = this.stmtBody.getUnits().iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         Stmt stmt = (Stmt)u;
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void merge_connected_components() throws TypeException {
      this.compute_solved();
   }

   private void merge_single_constraints() throws TypeException {
      boolean modified = true;

      while(true) {
         Iterator var2;
         TypeVariable var;
         label202:
         do {
            if (!modified) {
               return;
            }

            modified = false;
            this.refresh_solved();
            var2 = this.unsolved.iterator();

            while(true) {
               TypeVariable parent;
               TypeNode type;
               do {
                  do {
                     LinkedList parents_to_remove;
                     TypeNode gcd;
                     Iterator var6;
                     TypeVariable parent;
                     TypeNode type;
                     if (!var2.hasNext()) {
                        if (!modified) {
                           var2 = this.unsolved.iterator();

                           label165:
                           while(true) {
                              do {
                                 do {
                                    if (!var2.hasNext()) {
                                       break label165;
                                    }

                                    var = (TypeVariable)var2.next();
                                    parents_to_remove = new LinkedList();
                                    gcd = null;
                                    var.fixParents();
                                    var6 = var.parents().iterator();

                                    while(var6.hasNext()) {
                                       parent = (TypeVariable)var6.next();
                                       type = parent.type();
                                       if (type != null) {
                                          parents_to_remove.add(parent);
                                          if (gcd == null) {
                                             gcd = type;
                                          } else {
                                             gcd = gcd.gcd_1(type);
                                          }
                                       }
                                    }

                                    if (gcd != null) {
                                       var6 = parents_to_remove.iterator();

                                       while(var6.hasNext()) {
                                          parent = (TypeVariable)var6.next();
                                          var.removeParent(parent);
                                       }

                                       var.addParent(this.typeVariable(gcd));
                                    }
                                 } while(var.parents().size() != 1);

                                 parent = (TypeVariable)var.parents().get(0);
                                 type = parent.type();
                              } while(type != null && type.type() == null);

                              var.union(parent);
                              modified = true;
                           }
                        }

                        if (!modified) {
                           var2 = this.unsolved.iterator();

                           while(var2.hasNext()) {
                              var = (TypeVariable)var2.next();
                              if (var.type() == null && var.inv_approx() != null && var.inv_approx().type() != null) {
                                 var.union(this.typeVariable(var.inv_approx()));
                                 modified = true;
                              }
                           }
                        }

                        if (!modified) {
                           var2 = this.unsolved.iterator();

                           while(var2.hasNext()) {
                              var = (TypeVariable)var2.next();
                              if (var.type() == null && var.approx() != null && var.approx().type() != null) {
                                 var.union(this.typeVariable(var.approx()));
                                 modified = true;
                              }
                           }
                        }

                        if (!modified) {
                           var2 = this.unsolved.iterator();

                           while(var2.hasNext()) {
                              var = (TypeVariable)var2.next();
                              if (var.type() == null && var.approx() == ClassHierarchy.v().R0_32767) {
                                 var.union(this.SHORT);
                                 modified = true;
                              }
                           }
                        }

                        if (!modified) {
                           var2 = this.unsolved.iterator();

                           while(var2.hasNext()) {
                              var = (TypeVariable)var2.next();
                              if (var.type() == null && var.approx() == ClassHierarchy.v().R0_127) {
                                 var.union(this.BYTE);
                                 modified = true;
                              }
                           }
                        }
                        continue label202;
                     }

                     var = (TypeVariable)var2.next();
                     parents_to_remove = new LinkedList();
                     gcd = null;
                     var.fixChildren();
                     var6 = var.children().iterator();

                     while(var6.hasNext()) {
                        parent = (TypeVariable)var6.next();
                        type = parent.type();
                        if (type != null) {
                           parents_to_remove.add(parent);
                           if (gcd == null) {
                              gcd = type;
                           } else {
                              gcd = gcd.lca_1(type);
                           }
                        }
                     }

                     if (gcd != null) {
                        var6 = parents_to_remove.iterator();

                        while(var6.hasNext()) {
                           parent = (TypeVariable)var6.next();
                           var.removeChild(parent);
                        }

                        var.addChild(this.typeVariable(gcd));
                     }
                  } while(var.children().size() != 1);

                  parent = (TypeVariable)var.children().get(0);
                  type = parent.type();
               } while(type != null && type.type() == null);

               var.union(parent);
               modified = true;
            }
         } while(modified);

         var2 = this.R0_1.parents().iterator();

         while(var2.hasNext()) {
            var = (TypeVariable)var2.next();
            if (var.type() == null && var.approx() == ClassHierarchy.v().R0_1) {
               var.union(this.BOOLEAN);
               modified = true;
            }
         }
      }
   }

   private void assign_types_1() throws TypeException {
      Iterator localIt = this.stmtBody.getLocals().iterator();

      while(true) {
         while(true) {
            Local local;
            do {
               if (!localIt.hasNext()) {
                  return;
               }

               local = (Local)localIt.next();
            } while(!(local.getType() instanceof IntegerType));

            TypeVariable var = this.typeVariable(local);
            if (var.type() != null && var.type().type() != null) {
               local.setType(var.type().type());
            } else {
               TypeVariable.error("Type Error(21):  Variable without type");
            }
         }
      }
   }

   private void assign_types_2() throws TypeException {
      Iterator localIt = this.stmtBody.getLocals().iterator();

      while(true) {
         while(true) {
            Local local;
            do {
               if (!localIt.hasNext()) {
                  return;
               }

               local = (Local)localIt.next();
            } while(!(local.getType() instanceof IntegerType));

            TypeVariable var = this.typeVariable(local);
            if (var.inv_approx() != null && var.inv_approx().type() != null) {
               local.setType(var.inv_approx().type());
            } else if (var.approx().type() != null) {
               local.setType(var.approx().type());
            } else if (var.approx() == ClassHierarchy.v().R0_1) {
               local.setType(BooleanType.v());
            } else if (var.approx() == ClassHierarchy.v().R0_127) {
               local.setType(ByteType.v());
            } else {
               local.setType(ShortType.v());
            }
         }
      }
   }

   private void check_constraints() throws TypeException {
      ConstraintChecker checker = new ConstraintChecker(this, false);
      StringBuffer s = null;
      Iterator stmtIt = this.stmtBody.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();

         try {
            checker.check(stmt, this.stmtBody);
         } catch (TypeException var6) {
            throw var6;
         }
      }

   }

   private void check_and_fix_constraints() throws TypeException {
      ConstraintChecker checker = new ConstraintChecker(this, true);
      StringBuffer s = null;
      PatchingChain<Unit> units = this.stmtBody.getUnits();
      Stmt[] stmts = new Stmt[units.size()];
      units.toArray(stmts);
      Stmt[] var5 = stmts;
      int var6 = stmts.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Stmt stmt = var5[var7];

         try {
            checker.check(stmt, this.stmtBody);
         } catch (TypeException var10) {
            throw var10;
         }
      }

   }

   private void compute_approximate_types() throws TypeException {
      TreeSet<TypeVariable> workList = new TreeSet();
      Iterator var2 = this.typeVariableList.iterator();

      TypeVariable var;
      while(var2.hasNext()) {
         var = (TypeVariable)var2.next();
         if (var.type() != null) {
            workList.add(var);
         }
      }

      TypeVariable.computeApprox(workList);
      workList = new TreeSet();
      var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         var = (TypeVariable)var2.next();
         if (var.type() != null) {
            workList.add(var);
         }
      }

      TypeVariable.computeInvApprox(workList);
      var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         var = (TypeVariable)var2.next();
         if (var.approx() == null) {
            var.union(this.INT);
         }
      }

   }

   private void compute_solved() {
      Set<TypeVariable> unsolved_set = new TreeSet();
      Set<TypeVariable> solved_set = new TreeSet();
      Iterator var3 = this.typeVariableList.iterator();

      while(var3.hasNext()) {
         TypeVariable var = (TypeVariable)var3.next();
         if (var.type() == null) {
            unsolved_set.add(var);
         } else {
            solved_set.add(var);
         }
      }

      this.solved = solved_set;
      this.unsolved = unsolved_set;
   }

   private void refresh_solved() throws TypeException {
      Set<TypeVariable> unsolved_set = new TreeSet();
      Set<TypeVariable> solved_set = new TreeSet(this.solved);
      Iterator var3 = this.unsolved.iterator();

      while(var3.hasNext()) {
         TypeVariable var = (TypeVariable)var3.next();
         if (var.type() == null) {
            unsolved_set.add(var);
         } else {
            solved_set.add(var);
         }
      }

      this.solved = solved_set;
      this.unsolved = unsolved_set;
   }
}
