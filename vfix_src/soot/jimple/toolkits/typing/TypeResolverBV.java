package soot.jimple.toolkits.typing;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import soot.util.BitSetIterator;
import soot.util.BitVector;

/** @deprecated */
@Deprecated
public class TypeResolverBV {
   private static final Logger logger = LoggerFactory.getLogger(TypeResolverBV.class);
   private final ClassHierarchy hierarchy;
   private final List<TypeVariableBV> typeVariableList = new ArrayList();
   private final BitVector invalidIds = new BitVector();
   private final Map<Object, TypeVariableBV> typeVariableMap = new HashMap();
   private final JimpleBody stmtBody;
   final TypeNode NULL;
   private final TypeNode OBJECT;
   private static final boolean DEBUG = false;
   private BitVector unsolved;
   private BitVector solved;
   private BitVector single_soft_parent;
   private BitVector single_hard_parent;
   private BitVector multiple_parents;
   private BitVector single_child_not_null;
   private BitVector single_null_child;
   private BitVector multiple_children;

   public ClassHierarchy hierarchy() {
      return this.hierarchy;
   }

   public TypeNode typeNode(Type type) {
      return this.hierarchy.typeNode(type);
   }

   TypeVariableBV typeVariable(Local local) {
      TypeVariableBV result = (TypeVariableBV)this.typeVariableMap.get(local);
      if (result == null) {
         int id = this.typeVariableList.size();
         this.typeVariableList.add((Object)null);
         result = new TypeVariableBV(id, this);
         this.typeVariableList.set(id, result);
         this.typeVariableMap.put(local, result);
      }

      return result;
   }

   public TypeVariableBV typeVariable(TypeNode typeNode) {
      TypeVariableBV result = (TypeVariableBV)this.typeVariableMap.get(typeNode);
      if (result == null) {
         int id = this.typeVariableList.size();
         this.typeVariableList.add((Object)null);
         result = new TypeVariableBV(id, this, typeNode);
         this.typeVariableList.set(id, result);
         this.typeVariableMap.put(typeNode, result);
      }

      return result;
   }

   public TypeVariableBV typeVariable(SootClass sootClass) {
      return this.typeVariable(this.hierarchy.typeNode(sootClass.getType()));
   }

   public TypeVariableBV typeVariable(Type type) {
      return this.typeVariable(this.hierarchy.typeNode(type));
   }

   public TypeVariableBV typeVariable() {
      int id = this.typeVariableList.size();
      this.typeVariableList.add((Object)null);
      TypeVariableBV result = new TypeVariableBV(id, this);
      this.typeVariableList.set(id, result);
      return result;
   }

   private TypeResolverBV(JimpleBody stmtBody, Scene scene) {
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
         TypeResolverBV resolver = new TypeResolverBV(stmtBody, scene);
         resolver.resolve_step_1();
      } catch (TypeException var9) {
         try {
            TypeResolverBV resolver = new TypeResolverBV(stmtBody, scene);
            resolver.resolve_step_2();
         } catch (TypeException var8) {
            try {
               TypeResolverBV resolver = new TypeResolverBV(stmtBody, scene);
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
      ConstraintCollectorBV collector = new ConstraintCollectorBV(this, true);
      Iterator stmtIt = this.stmtBody.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void collect_constraints_3() {
      ConstraintCollectorBV collector = new ConstraintCollectorBV(this, false);
      Iterator stmtIt = this.stmtBody.getUnits().iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         collector.collect(stmt, this.stmtBody);
      }

   }

   private void compute_array_depth() throws TypeException {
      this.compute_approximate_types();
      TypeVariableBV[] vars = new TypeVariableBV[this.typeVariableList.size()];
      vars = (TypeVariableBV[])this.typeVariableList.toArray(vars);
      TypeVariableBV[] var2 = vars;
      int var3 = vars.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         TypeVariableBV element = var2[var4];
         element.fixDepth();
      }

   }

   private void propagate_array_constraints() {
      int max = 0;
      Iterator var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         TypeVariableBV var = (TypeVariableBV)var2.next();
         int depth = var.depth();
         if (depth > max) {
            max = depth;
         }
      }

      if (max > 1 && !Options.v().j2me()) {
         this.typeVariable((Type)ArrayType.v(RefType.v("java.lang.Cloneable"), max - 1));
         this.typeVariable((Type)ArrayType.v(RefType.v("java.io.Serializable"), max - 1));
      }

      LinkedList<TypeVariableBV>[] lists = new LinkedList[max + 1];

      int i;
      for(i = 0; i <= max; ++i) {
         lists[i] = new LinkedList();
      }

      Iterator var8 = this.typeVariableList.iterator();

      while(var8.hasNext()) {
         TypeVariableBV var = (TypeVariableBV)var8.next();
         int depth = var.depth();
         lists[depth].add(var);
      }

      for(i = max; i >= 0; --i) {
         Iterator var10 = this.typeVariableList.iterator();

         while(var10.hasNext()) {
            TypeVariableBV var = (TypeVariableBV)var10.next();
            var.propagate();
         }
      }

   }

   private void merge_primitive_types() throws TypeException {
      this.compute_solved();
      BitSetIterator varIt = this.solved.iterator();

      while(true) {
         TypeVariableBV var;
         do {
            if (!varIt.hasNext()) {
               return;
            }

            var = this.typeVariableForId(varIt.next());
         } while(!(var.type().type() instanceof IntType) && !(var.type().type() instanceof LongType) && !(var.type().type() instanceof FloatType) && !(var.type().type() instanceof DoubleType));

         while(true) {
            boolean finished = true;
            BitVector parents = var.parents();
            BitSetIterator j;
            TypeVariableBV child;
            if (parents.length() != 0) {
               finished = false;

               for(j = parents.iterator(); j.hasNext(); var = var.union(child)) {
                  child = this.typeVariableForId(j.next());
               }
            }

            BitVector children = var.children();
            if (children.length() != 0) {
               finished = false;

               for(j = children.iterator(); j.hasNext(); var = var.union(child)) {
                  child = this.typeVariableForId(j.next());
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
      BitVector list = new BitVector();
      list.or(this.solved);
      list.or(this.unsolved);
      new StronglyConnectedComponentsBV(list, this);
   }

   private void remove_transitive_constraints() throws TypeException {
      this.refresh_solved();
      BitVector list = new BitVector();
      list.or(this.solved);
      list.or(this.unsolved);
      BitSetIterator varIt = list.iterator();

      while(varIt.hasNext()) {
         TypeVariableBV var = this.typeVariableForId(varIt.next());
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
            BitSetIterator i;
            TypeVariableBV var;
            TypeVariableBV child;
            if (this.single_child_not_null.length() != 0) {
               finished = false;
               modified = true;
               i = this.single_child_not_null.iterator();

               while(i.hasNext()) {
                  var = this.typeVariableForId(i.next());
                  if (this.single_child_not_null.get(var.id())) {
                     child = this.typeVariableForId(var.children().iterator().next());
                     var.union(child);
                  }
               }
            }

            if (finished) {
               if (this.single_soft_parent.length() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_soft_parent.iterator();

                  while(i.hasNext()) {
                     var = this.typeVariableForId(i.next());
                     if (this.single_soft_parent.get(var.id())) {
                        child = this.typeVariableForId(var.parents().iterator().next());
                        var.union(child);
                     }
                  }
               }

               if (this.single_hard_parent.length() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_hard_parent.iterator();

                  while(i.hasNext()) {
                     var = this.typeVariableForId(i.next());
                     if (this.single_hard_parent.get(var.id())) {
                        child = this.typeVariableForId(var.parents().iterator().next());
                        this.debug_vars("union single parent\n " + var + "\n " + child);
                        var.union(child);
                     }
                  }
               }

               if (this.single_null_child.length() != 0) {
                  finished = false;
                  modified = true;
                  i = this.single_null_child.iterator();

                  while(i.hasNext()) {
                     var = this.typeVariableForId(i.next());
                     if (this.single_null_child.get(var.id())) {
                        child = this.typeVariableForId(var.children().iterator().next());
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
                  BitVector children_to_remove;
                  BitSetIterator childIt;
                  TypeVariableBV child;
                  TypeNode lca;
                  label134:
                  do {
                     label130:
                     while(i.hasNext()) {
                        var = this.typeVariableForId(i.next());
                        lca = null;
                        children_to_remove = new BitVector();
                        childIt = var.children().iterator();

                        while(true) {
                           while(true) {
                              if (!childIt.hasNext()) {
                                 continue label134;
                              }

                              child = this.typeVariableForId(childIt.next());
                              TypeNode type = child.type();
                              if (type != null && type.isNull()) {
                                 var.removeChild(child);
                              } else if (type != null && type.isClass()) {
                                 children_to_remove.set(child.id());
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

                        var = this.typeVariableForId(i.next());
                        LinkedList<TypeVariableBV> hp = new LinkedList();
                        BitSetIterator parentIt = var.parents().iterator();

                        while(true) {
                           TypeVariableBV parent;
                           TypeNode type;
                           do {
                              if (!parentIt.hasNext()) {
                                 continue label111;
                              }

                              parent = this.typeVariableForId(parentIt.next());
                              type = parent.type();
                           } while(type == null);

                           Iterator k = hp.iterator();

                           while(k.hasNext()) {
                              TypeVariableBV otherparent = (TypeVariableBV)k.next();
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

                  childIt = children_to_remove.iterator();

                  while(childIt.hasNext()) {
                     child = this.typeVariableForId(childIt.next());
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
            TypeVariableBV var = this.typeVariable(local);
            if (var == null) {
               local.setType(RefType.v("java.lang.Object"));
            } else if (var.depth() == 0) {
               if (var.type() == null) {
                  TypeVariableBV.error("Type Error(5):  Variable without type");
               } else {
                  local.setType(var.type().type());
               }
            } else {
               TypeVariableBV element = var.element();

               for(int j = 1; j < var.depth(); ++j) {
                  element = element.element();
               }

               if (element.type() == null) {
                  TypeVariableBV.error("Type Error(6):  Array variable without base type");
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
            TypeVariableBV var = this.typeVariable(local);
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
      ConstraintCheckerBV checker = new ConstraintCheckerBV(this, false);
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
      ConstraintCheckerBV checker = new ConstraintCheckerBV(this, true);
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
      TreeSet<TypeVariableBV> workList = new TreeSet();
      Iterator var2 = this.typeVariableList.iterator();

      TypeVariableBV var;
      while(var2.hasNext()) {
         var = (TypeVariableBV)var2.next();
         if (var.type() != null) {
            workList.add(var);
         }
      }

      TypeVariableBV.computeApprox(workList);
      var2 = this.typeVariableList.iterator();

      while(var2.hasNext()) {
         var = (TypeVariableBV)var2.next();
         if (var.approx() == this.NULL) {
            var.union(this.typeVariable(this.NULL));
         } else if (var.approx() == null) {
            var.union(this.typeVariable(this.NULL));
         }
      }

   }

   private void compute_solved() {
      this.unsolved = new BitVector();
      this.solved = new BitVector();
      Iterator var1 = this.typeVariableList.iterator();

      while(var1.hasNext()) {
         TypeVariableBV var = (TypeVariableBV)var1.next();
         if (var.depth() == 0) {
            if (var.type() == null) {
               this.unsolved.set(var.id());
            } else {
               this.solved.set(var.id());
            }
         }
      }

   }

   private void refresh_solved() throws TypeException {
      this.unsolved = new BitVector();
      BitSetIterator varIt = this.unsolved.iterator();

      while(varIt.hasNext()) {
         TypeVariableBV var = this.typeVariableForId(varIt.next());
         if (var.depth() == 0) {
            if (var.type() == null) {
               this.unsolved.set(var.id());
            } else {
               this.solved.set(var.id());
            }
         }
      }

   }

   private void categorize() throws TypeException {
      this.refresh_solved();
      this.single_soft_parent = new BitVector();
      this.single_hard_parent = new BitVector();
      this.multiple_parents = new BitVector();
      this.single_child_not_null = new BitVector();
      this.single_null_child = new BitVector();
      this.multiple_children = new BitVector();
      BitSetIterator i = this.unsolved.iterator();

      while(i.hasNext()) {
         TypeVariableBV var = this.typeVariableForId(i.next());
         BitVector children = var.parents();
         int size = children.length();
         TypeVariableBV child;
         if (size == 0) {
            var.addParent(this.typeVariable(this.OBJECT));
            this.single_soft_parent.set(var.id());
         } else if (size == 1) {
            child = this.typeVariableForId(children.iterator().next());
            if (child.type() == null) {
               this.single_soft_parent.set(var.id());
            } else {
               this.single_hard_parent.set(var.id());
            }
         } else {
            this.multiple_parents.set(var.id());
         }

         children = var.children();
         size = children.size();
         if (size == 0) {
            var.addChild(this.typeVariable(this.NULL));
            this.single_null_child.set(var.id());
         } else if (size == 1) {
            child = this.typeVariableForId(children.iterator().next());
            if (child.type() == this.NULL) {
               this.single_null_child.set(var.id());
            } else {
               this.single_child_not_null.set(var.id());
            }
         } else {
            this.multiple_children.set(var.id());
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
               if (special.getMethodRef().name().equals("<init>")) {
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

   public TypeVariableBV typeVariableForId(int idx) {
      return (TypeVariableBV)this.typeVariableList.get(idx);
   }

   public BitVector invalidIds() {
      return this.invalidIds;
   }

   public void invalidateId(int id) {
      this.invalidIds.set(id);
   }
}
