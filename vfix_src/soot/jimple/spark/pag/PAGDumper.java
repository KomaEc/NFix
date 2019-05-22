package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.solver.TopoSorter;

public class PAGDumper {
   private static final Logger logger = LoggerFactory.getLogger(PAGDumper.class);
   protected PAG pag;
   protected String output_dir;
   protected int fieldNum = 0;
   protected HashMap<SparkField, Integer> fieldMap = new HashMap();
   protected PAGDumper.ObjectNumberer root = new PAGDumper.ObjectNumberer((Object)null, 0);

   public PAGDumper(PAG pag, String output_dir) {
      this.pag = pag;
      this.output_dir = output_dir;
   }

   public void dumpPointsToSets() {
      try {
         final PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, "solution")));
         file.println("Solution:");
         Iterator vnIt = this.pag.getVarNodeNumberer().iterator();

         while(vnIt.hasNext()) {
            final VarNode vn = (VarNode)vnIt.next();
            if (vn.getReplacement() == vn) {
               PointsToSetInternal p2set = vn.getP2Set();
               if (p2set != null) {
                  p2set.forall(new P2SetVisitor() {
                     public final void visit(Node n) {
                        try {
                           PAGDumper.this.dumpNode(vn, file);
                           file.print(" ");
                           PAGDumper.this.dumpNode(n, file);
                           file.println("");
                        } catch (IOException var3) {
                           throw new RuntimeException("Couldn't dump solution." + var3);
                        }
                     }
                  });
               }
            }
         }

         file.close();
      } catch (IOException var5) {
         throw new RuntimeException("Couldn't dump solution." + var5);
      }
   }

   public void dump() {
      try {
         PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, "pag")));
         if (this.pag.getOpts().topo_sort()) {
            (new TopoSorter(this.pag, false)).sort();
         }

         file.println("Allocations:");
         Iterator var2 = this.pag.allocSources().iterator();

         while(true) {
            AllocNode n;
            Node[] succs;
            Node[] var6;
            int var7;
            int var8;
            Node element0;
            do {
               Object object;
               if (!var2.hasNext()) {
                  file.println("Assignments:");
                  var2 = this.pag.simpleSources().iterator();

                  while(true) {
                     VarNode n;
                     do {
                        if (!var2.hasNext()) {
                           file.println("Loads:");
                           var2 = this.pag.loadSources().iterator();

                           while(var2.hasNext()) {
                              object = var2.next();
                              FieldRefNode n = (FieldRefNode)object;
                              succs = this.pag.loadLookup(n);
                              var6 = succs;
                              var7 = succs.length;

                              for(var8 = 0; var8 < var7; ++var8) {
                                 element0 = var6[var8];
                                 this.dumpNode(n, file);
                                 file.print(" ");
                                 this.dumpNode(element0, file);
                                 file.println("");
                              }
                           }

                           file.println("Stores:");
                           var2 = this.pag.storeSources().iterator();

                           while(true) {
                              do {
                                 if (!var2.hasNext()) {
                                    if (this.pag.getOpts().dump_types()) {
                                       this.dumpTypes(file);
                                    }

                                    file.close();
                                    return;
                                 }

                                 object = var2.next();
                                 n = (VarNode)object;
                              } while(n.getReplacement() != n);

                              succs = this.pag.storeLookup(n);
                              var6 = succs;
                              var7 = succs.length;

                              for(var8 = 0; var8 < var7; ++var8) {
                                 element0 = var6[var8];
                                 this.dumpNode(n, file);
                                 file.print(" ");
                                 this.dumpNode(element0, file);
                                 file.println("");
                              }
                           }
                        }

                        object = var2.next();
                        n = (VarNode)object;
                     } while(n.getReplacement() != n);

                     succs = this.pag.simpleLookup(n);
                     var6 = succs;
                     var7 = succs.length;

                     for(var8 = 0; var8 < var7; ++var8) {
                        element0 = var6[var8];
                        this.dumpNode(n, file);
                        file.print(" ");
                        this.dumpNode(element0, file);
                        file.println("");
                     }
                  }
               }

               object = var2.next();
               n = (AllocNode)object;
            } while(n.getReplacement() != n);

            succs = this.pag.allocLookup(n);
            var6 = succs;
            var7 = succs.length;

            for(var8 = 0; var8 < var7; ++var8) {
               element0 = var6[var8];
               this.dumpNode(n, file);
               file.print(" ");
               this.dumpNode(element0, file);
               file.println("");
            }
         }
      } catch (IOException var10) {
         throw new RuntimeException("Couldn't dump PAG." + var10);
      }
   }

   protected void dumpTypes(PrintWriter file) throws IOException {
      HashSet<Type> declaredTypes = new HashSet();
      HashSet<Type> actualTypes = new HashSet();
      HashSet<SparkField> allFields = new HashSet();
      Iterator nIt = this.pag.getVarNodeNumberer().iterator();

      while(nIt.hasNext()) {
         Node n = (Node)nIt.next();
         Type t = n.getType();
         if (t != null) {
            declaredTypes.add(t);
         }
      }

      nIt = this.pag.loadSources().iterator();

      Type declType;
      Object object;
      Node n;
      while(nIt.hasNext()) {
         object = nIt.next();
         n = (Node)object;
         if (n.getReplacement() == n) {
            declType = n.getType();
            if (declType != null) {
               declaredTypes.add(declType);
            }

            allFields.add(((FieldRefNode)n).getField());
         }
      }

      nIt = this.pag.storeInvSources().iterator();

      while(nIt.hasNext()) {
         object = nIt.next();
         n = (Node)object;
         if (n.getReplacement() == n) {
            declType = n.getType();
            if (declType != null) {
               declaredTypes.add(declType);
            }

            allFields.add(((FieldRefNode)n).getField());
         }
      }

      nIt = this.pag.allocSources().iterator();

      while(nIt.hasNext()) {
         object = nIt.next();
         n = (Node)object;
         if (n.getReplacement() == n) {
            declType = n.getType();
            if (declType != null) {
               actualTypes.add(declType);
            }
         }
      }

      HashMap<Type, Integer> typeToInt = new HashMap();
      int nextint = 1;
      Iterator nIt = declaredTypes.iterator();

      while(nIt.hasNext()) {
         declType = (Type)nIt.next();
         typeToInt.put(declType, new Integer(nextint++));
      }

      nIt = actualTypes.iterator();

      while(nIt.hasNext()) {
         declType = (Type)nIt.next();
         if (!typeToInt.containsKey(declType)) {
            typeToInt.put(declType, new Integer(nextint++));
         }
      }

      file.println("Declared Types:");
      nIt = declaredTypes.iterator();

      Type t;
      while(nIt.hasNext()) {
         declType = (Type)nIt.next();
         Iterator var9 = actualTypes.iterator();

         while(var9.hasNext()) {
            t = (Type)var9.next();
            if (this.pag.getTypeManager().castNeverFails(t, declType)) {
               file.println("" + typeToInt.get(declType) + " " + typeToInt.get(t));
            }
         }
      }

      file.println("Allocation Types:");
      nIt = this.pag.allocSources().iterator();

      while(nIt.hasNext()) {
         Object object = nIt.next();
         Node n = (Node)object;
         if (n.getReplacement() == n) {
            t = n.getType();
            this.dumpNode(n, file);
            if (t == null) {
               throw new RuntimeException("allocnode with null type");
            }

            file.println(" " + typeToInt.get(t));
         }
      }

      file.println("Variable Types:");
      nIt = this.pag.getVarNodeNumberer().iterator();

      while(nIt.hasNext()) {
         Node n = (Node)nIt.next();
         if (n.getReplacement() == n) {
            Type t = n.getType();
            this.dumpNode(n, file);
            if (t == null) {
               file.println(" 0");
            } else {
               file.println(" " + typeToInt.get(t));
            }
         }
      }

   }

   protected int fieldToNum(SparkField f) {
      Integer ret = (Integer)this.fieldMap.get(f);
      if (ret == null) {
         ret = new Integer(++this.fieldNum);
         this.fieldMap.put(f, ret);
      }

      return ret;
   }

   protected void dumpNode(Node n, PrintWriter out) throws IOException {
      if (n.getReplacement() != n) {
         throw new RuntimeException("Attempt to dump collapsed node.");
      } else {
         if (n instanceof FieldRefNode) {
            FieldRefNode fn = (FieldRefNode)n;
            this.dumpNode(fn.getBase(), out);
            out.print(" " + this.fieldToNum(fn.getField()));
         } else if (this.pag.getOpts().class_method_var() && n instanceof VarNode) {
            VarNode vn = (VarNode)n;
            SootMethod m = null;
            if (vn instanceof LocalVarNode) {
               m = ((LocalVarNode)vn).getMethod();
            }

            SootClass c = null;
            if (m != null) {
               c = m.getDeclaringClass();
            }

            PAGDumper.ObjectNumberer cl = this.root.findOrAdd(c);
            PAGDumper.ObjectNumberer me = cl.findOrAdd(m);
            PAGDumper.ObjectNumberer vr = me.findOrAdd(vn);
            out.print("" + cl.num + " " + me.num + " " + vr.num);
         } else if (this.pag.getOpts().topo_sort() && n instanceof VarNode) {
            out.print("" + ((VarNode)n).finishingNumber);
         } else {
            out.print("" + n.getNumber());
         }

      }
   }

   class ObjectNumberer {
      Object o = null;
      int num = 0;
      int nextChildNum = 1;
      HashMap<Object, PAGDumper.ObjectNumberer> children = null;

      ObjectNumberer(Object o, int num) {
         this.o = o;
         this.num = num;
      }

      PAGDumper.ObjectNumberer findOrAdd(Object child) {
         if (this.children == null) {
            this.children = new HashMap();
         }

         PAGDumper.ObjectNumberer ret = (PAGDumper.ObjectNumberer)this.children.get(child);
         if (ret == null) {
            ret = PAGDumper.this.new ObjectNumberer(child, this.nextChildNum++);
            this.children.put(child, ret);
         }

         return ret;
      }
   }
}
