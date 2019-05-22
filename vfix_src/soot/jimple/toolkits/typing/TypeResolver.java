package soot.jimple.toolkits.typing;

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
import soot.ArrayType;
import soot.Body;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalDefs;

public class TypeResolver {
   private static final Logger logger = LoggerFactory.getLogger(TypeResolver.class);
   private final ClassHierarchy hierarchy;
   private final List<TypeVariable> typeVariableList = new ArrayList();
   private final Map<Object, TypeVariable> typeVariableMap = new HashMap();
   private final JimpleBody stmtBody;
   final TypeNode NULL;
   private final TypeNode OBJECT;
   private static final boolean DEBUG = false;
   private static final boolean IMPERFORMANT_TYPE_CHECK = false;
   private Collection<TypeVariable> unsolved;
   private Collection<TypeVariable> solved;
   private List<TypeVariable> single_soft_parent;
   private List<TypeVariable> single_hard_parent;
   private List<TypeVariable> multiple_parents;
   private List<TypeVariable> single_child_not_null;
   private List<TypeVariable> single_null_child;
   private List<TypeVariable> multiple_children;

   public ClassHierarchy hierarchy() {
      return this.hierarchy;
   }

   public TypeNode typeNode(Type type) {
      return this.hierarchy.typeNode(type);
   }

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

   public TypeVariable typeVariable(SootClass sootClass) {
      return this.typeVariable(this.hierarchy.typeNode(sootClass.getType()));
   }

   public TypeVariable typeVariable(Type type) {
      return this.typeVariable(this.hierarchy.typeNode(type));
   }

   public TypeVariable typeVariable() {
      int id = this.typeVariableList.size();
      this.typeVariableList.add((Object)null);
      TypeVariable result = new TypeVariable(id, this);
      this.typeVariableList.set(id, result);
      return result;
   }

   private TypeResolver(JimpleBody stmtBody, Scene scene) {
      this.stmtBody = stmtBody;
      this.hierarchy = ClassHierarchy.classHierarchy(scene);
      this.OBJECT = this.hierarchy.OBJECT;
      this.NULL = this.hierarchy.NULL;
      this.typeVariable(this.OBJECT);
      this.typeVariable(this.NULL);
      if (!Options.v().j2me()) {
         this.typeVariable(this.hierarchy.CLONEABLE);
         this.typeVariable(this.hierarchy.SERIALIZABLE);
      }

   }

   public static void resolve(JimpleBody stmtBody, Scene scene) {
      try {
         TypeResolver resolver = new TypeResolver(stmtBody, scene);
         resolver.resolve_step_1();
      } catch (TypeException var9) {
         try {
            TypeResolver resolver = new TypeResolver(stmtBody, scene);
            resolver.resolve_step_2();
         } catch (TypeException var8) {
            try {
               TypeResolver resolver = new TypeResolver(stmtBody, scene);
               resolver.resolve_step_3();
            } catch (TypeException var7) {
               StringWriter st = new StringWriter();
               PrintWriter pw = new PrintWriter(st);
               logger.error((String)var7.getMessage(), (Throwable)var7);
               pw.close();
               throw new RuntimeException(st.toString());
            }
         }
      }

      soot.jimple.toolkits.typing.integer.TypeResolver.resolve(stmtBody);
   }

   private void debug_vars(String message) {
   }

   private void debug_body() {
   }

   private void resolve_step_1() throws TypeException {
      this.collect_constraints_1_2();
      this.debug_vars("constraints");
      this.compute_array_depth();
      this.propagate_array_constraints();
      this.debug_vars("arrays");
      this.merge_primitive_types();
      this.debug_vars("primitive");
      this.merge_connected_components();
      this.debug_vars("components");
      this.remove_transitive_constraints();
      this.debug_vars("transitive");
      this.merge_single_constraints();
      this.debug_vars("single");
      this.assign_types_1_2();
      this.debug_vars("assign");
      this.check_constraints();
   }

   private void resolve_step_2() throws TypeException {
      this.debug_body();
      this.split_new();
      this.debug_body();
      this.collect_constraints_1_2();
      this.debug_vars("constraints");
      this.compute_array_depth();
      this.propagate_array_constraints();
      this.debug_vars("arrays");
      this.merge_primitive_types();
      this.debug_vars("primitive");
      this.merge_connected_components();
      this.debug_vars("components");
      this.remove_transitive_constraints();
      this.debug_vars("transitive");
      this.merge_single_constraints();
      this.debug_vars("single");
      this.assign_types_1_2();
      this.debug_vars("assign");
      this.check_constraints();
   }

   private void resolve_step_3() throws TypeException {
      this.collect_constraints_3();
      this.compute_approximate_types();
      this.assign_types_3();
      this.check_and_fix_constraints();
   }

   private void collect_constraints_1_2() {
      ConstraintCollector collector = new ConstraintCollector(this, true);
      Iterator stmtIt = this.stmtBody.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void collect_constraints_3() {
      ConstraintCollector collector = new ConstraintCollector(this, false);
      Iterator stmtIt = this.stmtBody.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void compute_array_depth() throws TypeException {
      this.compute_approximate_types();
      TypeVariable[] vars = new TypeVariable[this.typeVariableList.size()];
      vars = (TypeVariable[])this.typeVariableList.toArray(vars);
      TypeVariable[] var2 = vars;
      int var3 = vars.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TypeVariable element = var2[var4];
         element.fixDepth();
      }

   }

   private void propagate_array_constraints() {
      int max = 0;
      Iterator var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         TypeVariable var = (TypeVariable)var2.next();
         int depth = var.depth();
         if (depth > max) {
            max = depth;
         }
      }

      if (max > 1 && !Options.v().j2me()) {
         this.typeVariable((Type)ArrayType.v(RefType.v("java.lang.Cloneable"), max - 1));
         this.typeVariable((Type)ArrayType.v(RefType.v("java.io.Serializable"), max - 1));
      }

      for(int i = max; i >= 0; --i) {
         Iterator var6 = this.typeVariableList.iterator();

         while(var6.hasNext()) {
            TypeVariable var = (TypeVariable)var6.next();
            var.propagate();
         }
      }

   }

   private void merge_primitive_types() throws TypeException {
      this.compute_solved();
      Iterator varIt = this.solved.iterator();

      while(true) {
         TypeVariable var;
         do {
            if (!varIt.hasNext()) {
               return;
            }

            var = (TypeVariable)varIt.next();
         } while(!(var.type().type() instanceof IntType) && !(var.type().type() instanceof LongType) && !(var.type().type() instanceof FloatType) && !(var.type().type() instanceof DoubleType));

         while(true) {
            boolean finished = true;
            List<TypeVariable> parents = var.parents();
            Iterator var6;
            TypeVariable child;
            if (parents.size() != 0) {
               finished = false;

               for(var6 = parents.iterator(); var6.hasNext(); var = var.union(child)) {
                  child = (TypeVariable)var6.next();
               }
            }

            List<TypeVariable> children = var.children();
            if (children.size() != 0) {
               finished = false;

               for(var6 = children.iterator(); var6.hasNext(); var = var.union(child)) {
                  child = (TypeVariable)var6.next();
               }
            }

            if (finished) {
               break;
            }
         }
      }
   }

   private void merge_connected_components() throws TypeException {
      this.refresh_solved();
   }

   private void remove_transitive_constraints() throws TypeException {
      this.refresh_solved();
      Iterator var1 = this.solved.iterator();

      TypeVariable var;
      while(var1.hasNext()) {
         var = (TypeVariable)var1.next();
         var.removeIndirectRelations();
      }

      var1 = this.unsolved.iterator();

      while(var1.hasNext()) {
         var = (TypeVariable)var1.next();
         var.removeIndirectRelations();
      }

   }

   private void merge_single_constraints() throws TypeException {
      boolean finished = false;
      boolean modified = false;

      do {
         label144:
         while(true) {
            this.categorize();
            Iterator i;
            TypeVariable var;
            TypeVariable child;
            if (this.single_child_not_null.size() != 0) {
               finished = false;
               modified = true;
               i = this.single_child_not_null.iterator();

               while(i.hasNext()) {
                  var = (TypeVariable)i.next();
                  if (this.single_child_not_null.contains(var)) {
                     child = (TypeVariable)var.children().get(0);
                     var.union(child);
                  }
               }
            }

            if (finished) {
               if (this.single_soft_parent.size() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_soft_parent.iterator();

                  while(i.hasNext()) {
                     var = (TypeVariable)i.next();
                     if (this.single_soft_parent.contains(var)) {
                        child = (TypeVariable)var.parents().get(0);
                        var.union(child);
                     }
                  }
               }

               if (this.single_hard_parent.size() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_hard_parent.iterator();

                  while(i.hasNext()) {
                     var = (TypeVariable)i.next();
                     if (this.single_hard_parent.contains(var)) {
                        child = (TypeVariable)var.parents().get(0);
                        this.debug_vars("union single parent\n " + var + "\n " + child);
                        var.union(child);
                     }
                  }
               }

               if (this.single_null_child.size() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_null_child.iterator();

                  while(i.hasNext()) {
                     var = (TypeVariable)i.next();
                     if (this.single_null_child.contains(var)) {
                        child = (TypeVariable)var.children().get(0);
                        var.union(child);
                     }
                  }
               }
               break;
            }

            if (modified) {
               modified = false;
            } else {
               finished = true;
               i = this.multiple_children.iterator();

               while(true) {
                  LinkedList children_to_remove;
                  Iterator var7;
                  TypeVariable child;
                  TypeNode lca;
                  label134:
                  do {
                     label130:
                     while(i.hasNext()) {
                        var = (TypeVariable)i.next();
                        lca = null;
                        children_to_remove = new LinkedList();
                        var.fixChildren();
                        var7 = var.children().iterator();

                        while(true) {
                           while(true) {
                              if (!var7.hasNext()) {
                                 continue label134;
                              }

                              child = (TypeVariable)var7.next();
                              TypeNode type = child.type();
                              if (type != null && type.isNull()) {
                                 var.removeChild(child);
                              } else if (type != null && type.isClass()) {
                                 children_to_remove.add(child);
                                 if (lca == null) {
                                    lca = type;
                                 } else {
                                    lca = lca.lcaIfUnique(type);
                                    if (lca == null) {
                                       continue label130;
                                    }
                                 }
                              }
                           }
                        }
                     }

                     i = this.multiple_parents.iterator();

                     label111:
                     while(true) {
                        if (!i.hasNext()) {
                           continue label144;
                        }

                        var = (TypeVariable)i.next();
                        List<TypeVariable> hp = new ArrayList();
                        var.fixParents();
                        Iterator var14 = var.parents().iterator();

                        while(true) {
                           TypeVariable parent;
                           TypeNode type;
                           do {
                              if (!var14.hasNext()) {
                                 continue label111;
                              }

                              parent = (TypeVariable)var14.next();
                              type = parent.type();
                           } while(type == null);

                           Iterator k = hp.iterator();

                           while(k.hasNext()) {
                              TypeVariable otherparent = (TypeVariable)k.next();
                              TypeNode othertype = otherparent.type();
                              if (type.hasDescendant(othertype)) {
                                 var.removeParent(parent);
                                 type = null;
                                 break;
                              }

                              if (type.hasAncestor(othertype)) {
                                 var.removeParent(otherparent);
                                 k.remove();
                              }
                           }

                           if (type != null) {
                              hp.add(parent);
                           }
                        }
                     }
                  } while(lca == null);

                  var7 = children_to_remove.iterator();

                  while(var7.hasNext()) {
                     child = (TypeVariable)var7.next();
                     var.removeChild(child);
                  }

                  var.addChild(this.typeVariable(lca));
               }
            }
         }
      } while(!finished);

   }

   private void assign_types_1_2() throws TypeException {
      Iterator localIt = this.stmtBody.getLocals().iterator();

      while(true) {
         while(localIt.hasNext()) {
            Local local = (Local)localIt.next();
            TypeVariable var = this.typeVariable(local);
            if (var == null) {
               local.setType(RefType.v("java.lang.Object"));
            } else if (var.depth() == 0) {
               if (var.type() == null) {
                  TypeVariable.error("Type Error(5):  Variable without type");
               } else {
                  local.setType(var.type().type());
               }
            } else {
               TypeVariable element = var.element();

               for(int j = 1; j < var.depth(); ++j) {
                  element = element.element();
               }

               if (element.type() == null) {
                  TypeVariable.error("Type Error(6):  Array variable without base type");
               } else if (element.type().type() instanceof NullType) {
                  local.setType(NullType.v());
               } else {
                  Type t = element.type().type();
                  if (t instanceof IntType) {
                     local.setType(var.approx().type());
                  } else {
                     local.setType(ArrayType.v(t, var.depth()));
                  }
               }
            }
         }

         return;
      }
   }

   private void assign_types_3() throws TypeException {
      Iterator localIt = this.stmtBody.getLocals().iterator();

      while(true) {
         while(localIt.hasNext()) {
            Local local = (Local)localIt.next();
            TypeVariable var = this.typeVariable(local);
            if (var != null && var.approx() != null && var.approx().type() != null) {
               local.setType(var.approx().type());
            } else {
               local.setType(RefType.v("java.lang.Object"));
            }
         }

         return;
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
      var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         var = (TypeVariable)var2.next();
         if (var.approx() == this.NULL) {
            var.union(this.typeVariable(this.NULL));
         } else if (var.approx() == null) {
            var.union(this.typeVariable(this.NULL));
         }
      }

   }

   private void compute_solved() {
      Set<TypeVariable> unsolved_set = new TreeSet();
      Set<TypeVariable> solved_set = new TreeSet();
      Iterator var3 = this.typeVariableList.iterator();

      while(var3.hasNext()) {
         TypeVariable var = (TypeVariable)var3.next();
         if (var.depth() == 0) {
            if (var.type() == null) {
               unsolved_set.add(var);
            } else {
               solved_set.add(var);
            }
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
         if (var.depth() == 0) {
            if (var.type() == null) {
               unsolved_set.add(var);
            } else {
               solved_set.add(var);
            }
         }
      }

      this.solved = solved_set;
      this.unsolved = unsolved_set;
   }

   private void categorize() throws TypeException {
      this.refresh_solved();
      this.single_soft_parent = new LinkedList();
      this.single_hard_parent = new LinkedList();
      this.multiple_parents = new LinkedList();
      this.single_child_not_null = new LinkedList();
      this.single_null_child = new LinkedList();
      this.multiple_children = new LinkedList();
      Iterator var1 = this.unsolved.iterator();

      while(var1.hasNext()) {
         TypeVariable var = (TypeVariable)var1.next();
         List<TypeVariable> children = var.parents();
         int size = children.size();
         TypeVariable child;
         if (size == 0) {
            var.addParent(this.typeVariable(this.OBJECT));
            this.single_soft_parent.add(var);
         } else if (size == 1) {
            child = (TypeVariable)children.get(0);
            if (child.type() == null) {
               this.single_soft_parent.add(var);
            } else {
               this.single_hard_parent.add(var);
            }
         } else {
            this.multiple_parents.add(var);
         }

         children = var.children();
         size = children.size();
         if (size == 0) {
            var.addChild(this.typeVariable(this.NULL));
            this.single_null_child.add(var);
         } else if (size == 1) {
            child = (TypeVariable)children.get(0);
            if (child.type() == this.NULL) {
               this.single_null_child.add(var);
            } else {
               this.single_child_not_null.add(var);
            }
         } else {
            this.multiple_children.add(var);
         }
      }

   }

   private void split_new() {
      LocalDefs defs = LocalDefs.Factory.newLocalDefs((Body)this.stmtBody);
      PatchingChain<Unit> units = this.stmtBody.getUnits();
      Stmt[] stmts = new Stmt[units.size()];
      units.toArray(stmts);
      Stmt[] var4 = stmts;
      int var5 = stmts.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Stmt stmt = var4[var6];
         if (stmt instanceof InvokeStmt) {
            InvokeStmt invoke = (InvokeStmt)stmt;
            if (invoke.getInvokeExpr() instanceof SpecialInvokeExpr) {
               SpecialInvokeExpr special = (SpecialInvokeExpr)invoke.getInvokeExpr();
               AssignStmt assign;
               if ("<init>".equals(special.getMethodRef().name())) {
                  for(List deflist = defs.getDefsOfAt((Local)special.getBase(), invoke); deflist.size() == 1; deflist = defs.getDefsOfAt((Local)assign.getRightOp(), assign)) {
                     Stmt stmt2 = (Stmt)deflist.get(0);
                     if (!(stmt2 instanceof AssignStmt)) {
                        break;
                     }

                     assign = (AssignStmt)stmt2;
                     if (!(assign.getRightOp() instanceof Local)) {
                        if (assign.getRightOp() instanceof NewExpr) {
                           Local newlocal = Jimple.v().newLocal("tmp", (Type)null);
                           this.stmtBody.getLocals().add(newlocal);
                           special.setBase(newlocal);
                           units.insertAfter((Unit)Jimple.v().newAssignStmt(assign.getLeftOp(), newlocal), (Unit)assign);
                           assign.setLeftOp(newlocal);
                        }
                        break;
                     }
                  }
               }
            }
         }
      }

   }
}
