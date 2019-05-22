package soot.coffi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.VoidType;
import soot.jbco.Main;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;
import soot.options.Options;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.util.ArraySet;
import soot.util.Chain;

public class CFG {
   private static final Logger logger = LoggerFactory.getLogger(CFG.class);
   private method_info method;
   BasicBlock cfg;
   Chain<Unit> units;
   JimpleBody listBody;
   Map<Instruction, Stmt> instructionToFirstStmt;
   Map<Instruction, Stmt> instructionToLastStmt;
   SootMethod jmethod;
   Scene cm;
   Instruction firstInstruction;
   Instruction lastInstruction;
   private Instruction sentinel;
   private Hashtable<Instruction, BasicBlock> h2bb;
   private Hashtable<Instruction, BasicBlock> t2bb;
   public static HashMap<SootMethod, int[]> methodsToVEM = new HashMap();
   Map<Instruction, Instruction> jsr2astore = new HashMap();
   Map<Instruction, Instruction> astore2ret = new HashMap();
   LinkedList<Instruction> jsrorder = new LinkedList();
   private final Hashtable<Instruction, Instruction_Goto> replacedInsns = new Hashtable();
   private BootstrapMethods_attribute bootstrap_methods_attribute;

   public CFG(method_info m) {
      this.method = m;
      this.sentinel = new Instruction_Nop();
      this.sentinel.next = m.instructions;
      m.instructions.prev = this.sentinel;
      this.eliminateJsrRets();
      this.buildBBCFG();
      m.cfg = this;
      if (this.cfg != null) {
         this.cfg.beginCode = true;
         this.firstInstruction = this.cfg.head;
      } else {
         this.firstInstruction = null;
      }

      if (Main.metrics) {
         this.complexity();
      }

   }

   private void complexity() {
      if (this.method.jmethod.getDeclaringClass().isApplicationClass()) {
         BasicBlock b = this.cfg;
         HashMap<BasicBlock, Integer> block2exc = new HashMap();
         int nodes = 0;
         int edges = 0;

         int tmp;
         int highest;
         for(highest = 0; b != null; b = b.next) {
            tmp = 0;
            exception_table_entry[] var7 = this.method.code_attr.exception_table;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               exception_table_entry element = var7[var9];
               Instruction start = element.start_inst;
               Instruction end = element.start_inst;
               if (start.label >= b.head.label && start.label <= b.tail.label || end.label > b.head.label && (b.tail.next == null || end.label <= b.tail.next.label)) {
                  ++tmp;
               }
            }

            block2exc.put(b, new Integer(tmp));
         }

         for(b = this.cfg; b != null; b = b.next) {
            ++nodes;
            tmp = b.succ.size() + (Integer)block2exc.get(b);
            int deg = b.pred.size() + tmp + (b.beginException ? 1 : 0);
            if (deg > highest) {
               highest = deg;
            }

            edges += tmp;
         }

         methodsToVEM.put(this.method.jmethod, new int[]{nodes, edges, highest});
      }
   }

   private void buildBBCFG() {
      Code_attribute ca = this.method.locate_code_attribute();
      this.h2bb = new Hashtable(100, 25.0F);
      this.t2bb = new Hashtable(100, 25.0F);
      Instruction insn = this.sentinel.next;
      BasicBlock blast = null;
      Instruction tail;
      if (insn != null) {
         tail = buildBasicBlock(insn);
         this.cfg = new BasicBlock(insn, tail);
         this.h2bb.put(insn, this.cfg);
         this.t2bb.put(tail, this.cfg);
         insn = tail.next;
         blast = this.cfg;
      }

      while(insn != null) {
         tail = buildBasicBlock(insn);
         BasicBlock block = new BasicBlock(insn, tail);
         blast.next = block;
         blast = block;
         this.h2bb.put(insn, block);
         this.t2bb.put(tail, block);
         insn = tail.next;
      }

      for(BasicBlock block = this.cfg; block != null; block = block.next) {
         Instruction insn = block.tail;
         if (!insn.branches) {
            if (block.next != null) {
               block.succ.addElement(block.next);
               block.next.pred.addElement(block);
            }
         } else {
            Object branches;
            int i;
            if (insn instanceof Instruction_Athrow) {
               HashSet<Instruction> ethandlers = new HashSet();

               for(i = 0; i < ca.exception_table_length; ++i) {
                  exception_table_entry etentry = ca.exception_table[i];
                  if (insn.label >= etentry.start_inst.label && (etentry.end_inst == null || insn.label < etentry.end_inst.label)) {
                     ethandlers.add(etentry.handler_inst);
                  }
               }

               branches = ethandlers.toArray();
            } else {
               branches = insn.branchpoints(insn.next);
            }

            if (branches != null) {
               block.succ.ensureCapacity(block.succ.size() + ((Object[])branches).length);
               Object var14 = branches;
               i = ((Object[])branches).length;

               for(int var17 = 0; var17 < i; ++var17) {
                  Object element = ((Object[])var14)[var17];
                  if (element != null) {
                     BasicBlock bb = (BasicBlock)this.h2bb.get(element);
                     if (bb == null) {
                        logger.warn("target of a branch is null");
                        logger.debug("" + insn);
                     } else {
                        block.succ.addElement(bb);
                        bb.pred.addElement(block);
                     }
                  }
               }
            }
         }
      }

      for(int i = 0; i < ca.exception_table_length; ++i) {
         BasicBlock bb = (BasicBlock)this.h2bb.get(ca.exception_table[i].handler_inst);
         if (bb == null) {
            logger.warn("No basic block found for start of exception handler code.");
         } else {
            bb.beginException = true;
            ca.exception_table[i].b = bb;
         }
      }

   }

   private static Instruction buildBasicBlock(Instruction head) {
      Instruction insn = head;
      Instruction next = head.next;
      if (next == null) {
         return head;
      } else {
         while(!insn.branches && !next.labelled) {
            insn = next;
            next = next.next;
            if (next == null) {
               break;
            }
         }

         return insn;
      }
   }

   private boolean eliminateJsrRets() {
      Instruction insn;
      for(insn = this.sentinel; insn.next != null; insn = insn.next) {
      }

      this.lastInstruction = insn;
      HashMap<Instruction, Instruction> todoBlocks = new HashMap();
      todoBlocks.put(this.sentinel.next, this.lastInstruction);
      LinkedList<Instruction> todoList = new LinkedList();
      todoList.add(this.sentinel.next);

      while(!todoList.isEmpty()) {
         Instruction firstInsn = (Instruction)todoList.removeFirst();
         Instruction lastInsn = (Instruction)todoBlocks.get(firstInsn);
         this.jsrorder.clear();
         this.jsr2astore.clear();
         this.astore2ret.clear();
         if (this.findOutmostJsrs(firstInsn, lastInsn)) {
            HashMap<Instruction, Instruction> newblocks = this.inliningJsrTargets();
            todoBlocks.putAll(newblocks);
            todoList.addAll(newblocks.keySet());
         }
      }

      this.method.instructions = this.sentinel.next;
      this.adjustExceptionTable();
      this.adjustLineNumberTable();
      this.adjustBranchTargets();
      return true;
   }

   private boolean findOutmostJsrs(Instruction start, Instruction end) {
      HashSet<Instruction> innerJsrs = new HashSet();
      boolean unusual = false;
      Instruction insn = start;

      do {
         if (insn instanceof Instruction_Jsr || insn instanceof Instruction_Jsr_w) {
            if (innerJsrs.contains(insn)) {
               insn = insn.next;
               continue;
            }

            Instruction astore = ((Instruction_branch)insn).target;
            if (!(astore instanceof Interface_Astore)) {
               unusual = true;
               break;
            }

            Instruction ret = this.findMatchingRet(astore, insn, innerJsrs);
            this.jsrorder.addLast(insn);
            this.jsr2astore.put(insn, astore);
            this.astore2ret.put(astore, ret);
         }

         insn = insn.next;
      } while(insn != end.next);

      if (unusual) {
         logger.debug("Sorry, I cannot handle this method.");
         return false;
      } else {
         return true;
      }
   }

   private Instruction findMatchingRet(Instruction astore, Instruction jsr, HashSet<Instruction> innerJsrs) {
      int astorenum = ((Interface_Astore)astore).getLocalNumber();

      for(Instruction insn = astore.next; insn != null; insn = insn.next) {
         if (!(insn instanceof Instruction_Ret) && !(insn instanceof Instruction_Ret_w)) {
            if (insn instanceof Instruction_Jsr || insn instanceof Instruction_Jsr_w) {
               innerJsrs.add(insn);
            }
         } else {
            int retnum = ((Interface_OneIntArg)insn).getIntArg();
            if (astorenum == retnum) {
               return insn;
            }
         }
      }

      return null;
   }

   private HashMap<Instruction, Instruction> inliningJsrTargets() {
      HashMap newblocks = new HashMap();

      while(!this.jsrorder.isEmpty()) {
         Instruction jsr = (Instruction)this.jsrorder.removeFirst();
         Instruction astore = (Instruction)this.jsr2astore.get(jsr);
         Instruction ret = (Instruction)this.astore2ret.get(astore);
         Instruction newhead = this.makeCopyOf(astore, ret, jsr.next);
         Instruction_Goto togo = new Instruction_Goto();
         togo.target = newhead;
         newhead.labelled = true;
         togo.label = jsr.label;
         togo.labelled = jsr.labelled;
         togo.prev = jsr.prev;
         togo.next = jsr.next;
         togo.prev.next = togo;
         togo.next.prev = togo;
         this.replacedInsns.put(jsr, togo);
         if (ret != null) {
            newblocks.put(newhead, this.lastInstruction);
         }
      }

      return newblocks;
   }

   private Instruction makeCopyOf(Instruction astore, Instruction ret, Instruction target) {
      if (ret == null) {
         return astore.next;
      } else {
         Instruction last = this.lastInstruction;
         Instruction headbefore = last;
         int curlabel = this.lastInstruction.label;
         HashMap<Instruction, Instruction> insnmap = new HashMap();

         Instruction insn;
         for(insn = astore.next; insn != ret && insn != null; insn = insn.next) {
            try {
               Instruction newone = (Instruction)insn.clone();
               ++curlabel;
               newone.label = curlabel;
               newone.prev = last;
               last.next = newone;
               last = newone;
               insnmap.put(insn, newone);
            } catch (CloneNotSupportedException var20) {
               logger.debug("Error !");
            }
         }

         Instruction_Goto togo = new Instruction_Goto();
         togo.target = target;
         target.labelled = true;
         ++curlabel;
         togo.label = curlabel;
         last.next = togo;
         togo.prev = last;
         Instruction last = togo;
         this.lastInstruction = togo;
         insnmap.put(astore, headbefore.next);
         insnmap.put(ret, togo);

         int i;
         for(insn = headbefore.next; insn != last; insn = insn.next) {
            Instruction newdefault;
            if (insn instanceof Instruction_branch) {
               Instruction oldtgt = ((Instruction_branch)insn).target;
               newdefault = (Instruction)insnmap.get(oldtgt);
               if (newdefault != null) {
                  ((Instruction_branch)insn).target = newdefault;
                  newdefault.labelled = true;
               }
            } else {
               Instruction newtgt;
               if (insn instanceof Instruction_Lookupswitch) {
                  Instruction_Lookupswitch switchinsn = (Instruction_Lookupswitch)insn;
                  newdefault = (Instruction)insnmap.get(switchinsn.default_inst);
                  if (newdefault != null) {
                     switchinsn.default_inst = newdefault;
                     newdefault.labelled = true;
                  }

                  for(i = 0; i < switchinsn.match_insts.length; ++i) {
                     newtgt = (Instruction)insnmap.get(switchinsn.match_insts[i]);
                     if (newtgt != null) {
                        switchinsn.match_insts[i] = newtgt;
                        newtgt.labelled = true;
                     }
                  }
               } else if (insn instanceof Instruction_Tableswitch) {
                  Instruction_Tableswitch switchinsn = (Instruction_Tableswitch)insn;
                  newdefault = (Instruction)insnmap.get(switchinsn.default_inst);
                  if (newdefault != null) {
                     switchinsn.default_inst = newdefault;
                     newdefault.labelled = true;
                  }

                  for(i = 0; i < switchinsn.jump_insts.length; ++i) {
                     newtgt = (Instruction)insnmap.get(switchinsn.jump_insts[i]);
                     if (newtgt != null) {
                        switchinsn.jump_insts[i] = newtgt;
                        newtgt.labelled = true;
                     }
                  }
               }
            }
         }

         Code_attribute ca = this.method.locate_code_attribute();
         LinkedList<exception_table_entry> newentries = new LinkedList();
         i = astore.next.originalIndex;
         int orig_end_of_subr = ret.originalIndex;

         int j;
         for(int i = 0; i < ca.exception_table_length; ++i) {
            exception_table_entry etentry = ca.exception_table[i];
            j = etentry.start_pc;
            int orig_end_of_trap = etentry.end_pc;
            if (j < orig_end_of_subr && orig_end_of_trap > i) {
               exception_table_entry newone = new exception_table_entry();
               if (j <= i) {
                  newone.start_inst = headbefore.next;
               } else {
                  Instruction ins = (Instruction)insnmap.get(etentry.start_inst);
                  if (ins != null) {
                     newone.start_inst = (Instruction)insnmap.get(etentry.start_inst);
                  } else {
                     newone.start_inst = etentry.start_inst;
                  }
               }

               if (orig_end_of_trap > orig_end_of_subr) {
                  newone.end_inst = null;
               } else {
                  newone.end_inst = (Instruction)insnmap.get(etentry.end_inst);
               }

               newone.handler_inst = (Instruction)insnmap.get(etentry.handler_inst);
               if (newone.handler_inst == null) {
                  newone.handler_inst = etentry.handler_inst;
               }

               newentries.add(newone);
            }

            if (etentry.end_inst == null) {
               etentry.end_inst = headbefore.next;
            }
         }

         if (newentries.size() > 0) {
            ca.exception_table_length += newentries.size();
            exception_table_entry[] newtable = new exception_table_entry[ca.exception_table_length];
            System.arraycopy(ca.exception_table, 0, newtable, 0, ca.exception_table.length);
            int i = 0;

            for(j = ca.exception_table.length; i < newentries.size(); ++j) {
               newtable[j] = (exception_table_entry)newentries.get(i);
               ++i;
            }

            ca.exception_table = newtable;
         }

         return headbefore.next;
      }
   }

   private void adjustBranchTargets() {
      for(Instruction insn = this.sentinel.next; insn != null; insn = insn.next) {
         Instruction newdefault;
         if (insn instanceof Instruction_branch) {
            Instruction_branch binsn = (Instruction_branch)insn;
            newdefault = (Instruction)this.replacedInsns.get(binsn.target);
            if (newdefault != null) {
               binsn.target = newdefault;
               newdefault.labelled = true;
            }
         } else {
            int i;
            Instruction newtgt;
            if (insn instanceof Instruction_Lookupswitch) {
               Instruction_Lookupswitch switchinsn = (Instruction_Lookupswitch)insn;
               newdefault = (Instruction)this.replacedInsns.get(switchinsn.default_inst);
               if (newdefault != null) {
                  switchinsn.default_inst = newdefault;
                  newdefault.labelled = true;
               }

               for(i = 0; i < switchinsn.npairs; ++i) {
                  newtgt = (Instruction)this.replacedInsns.get(switchinsn.match_insts[i]);
                  if (newtgt != null) {
                     switchinsn.match_insts[i] = newtgt;
                     newtgt.labelled = true;
                  }
               }
            } else if (insn instanceof Instruction_Tableswitch) {
               Instruction_Tableswitch switchinsn = (Instruction_Tableswitch)insn;
               newdefault = (Instruction)this.replacedInsns.get(switchinsn.default_inst);
               if (newdefault != null) {
                  switchinsn.default_inst = newdefault;
                  newdefault.labelled = true;
               }

               for(i = 0; i <= switchinsn.high - switchinsn.low; ++i) {
                  newtgt = (Instruction)this.replacedInsns.get(switchinsn.jump_insts[i]);
                  if (newtgt != null) {
                     switchinsn.jump_insts[i] = newtgt;
                     newtgt.labelled = true;
                  }
               }
            }
         }
      }

   }

   private void adjustExceptionTable() {
      Code_attribute codeAttribute = this.method.locate_code_attribute();

      for(int i = 0; i < codeAttribute.exception_table_length; ++i) {
         exception_table_entry entry = codeAttribute.exception_table[i];
         Instruction oldinsn = entry.start_inst;
         Instruction newinsn = (Instruction)this.replacedInsns.get(oldinsn);
         if (newinsn != null) {
            entry.start_inst = newinsn;
         }

         oldinsn = entry.end_inst;
         if (entry.end_inst != null) {
            newinsn = (Instruction)this.replacedInsns.get(oldinsn);
            if (newinsn != null) {
               entry.end_inst = newinsn;
            }
         }

         oldinsn = entry.handler_inst;
         newinsn = (Instruction)this.replacedInsns.get(oldinsn);
         if (newinsn != null) {
            entry.handler_inst = newinsn;
         }
      }

   }

   private void adjustLineNumberTable() {
      if (Options.v().keep_line_number()) {
         if (this.method.code_attr != null) {
            attribute_info[] attributes = this.method.code_attr.attributes;
            attribute_info[] var2 = attributes;
            int var3 = attributes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               attribute_info element = var2[var4];
               if (element instanceof LineNumberTable_attribute) {
                  LineNumberTable_attribute lntattr = (LineNumberTable_attribute)element;
                  line_number_table_entry[] var7 = lntattr.line_number_table;
                  int var8 = var7.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     line_number_table_entry element0 = var7[var9];
                     Instruction oldinst = element0.start_inst;
                     Instruction newinst = (Instruction)this.replacedInsns.get(oldinst);
                     if (newinst != null) {
                        element0.start_inst = newinst;
                     }
                  }
               }
            }

         }
      }
   }

   public Instruction reconstructInstructions() {
      return this.cfg != null ? this.cfg.head : null;
   }

   public boolean jimplify(cp_info[] constant_pool, int this_class, BootstrapMethods_attribute bootstrap_methods_attribute, JimpleBody listBody) {
      this.bootstrap_methods_attribute = bootstrap_methods_attribute;
      Chain<Unit> units = listBody.getUnits();
      this.listBody = listBody;
      this.units = units;
      this.instructionToFirstStmt = new HashMap();
      this.instructionToLastStmt = new HashMap();
      this.jmethod = listBody.getMethod();
      this.cm = Scene.v();
      Set<Local> initialLocals = new ArraySet();
      List<Type> parameterTypes = this.jmethod.getParameterTypes();
      Code_attribute ca = this.method.locate_code_attribute();
      LocalVariableTable_attribute la = ca.findLocalVariableTable();
      LocalVariableTypeTable_attribute lt = ca.findLocalVariableTypeTable();
      Util.v().bodySetup(la, lt, constant_pool);
      boolean isStatic = Modifier.isStatic(this.jmethod.getModifiers());
      int currentLocalIndex = 0;
      if (!isStatic) {
         Local local = Util.v().getLocalForParameter(listBody, currentLocalIndex);
         ++currentLocalIndex;
         units.add(Jimple.v().newIdentityStmt(local, Jimple.v().newThisRef(this.jmethod.getDeclaringClass().getType())));
      }

      Iterator<Type> typeIt = parameterTypes.iterator();

      for(int argCount = 0; typeIt.hasNext(); ++argCount) {
         Local local = Util.v().getLocalForParameter(listBody, currentLocalIndex);
         Type type = (Type)typeIt.next();
         initialLocals.add(local);
         units.add(Jimple.v().newIdentityStmt(local, Jimple.v().newParameterRef(type, argCount)));
         if (!type.equals(DoubleType.v()) && !type.equals(LongType.v())) {
            ++currentLocalIndex;
         } else {
            currentLocalIndex += 2;
         }
      }

      Util.v().resetEasyNames();
      this.jimplify(constant_pool, this_class);
      return true;
   }

   private void buildInsnCFGfromBBCFG() {
      for(BasicBlock block = this.cfg; block != null; block = block.next) {
         Instruction insn;
         for(insn = block.head; insn != block.tail; insn = insn.next) {
            Instruction[] succs = new Instruction[]{insn.next};
            insn.succs = succs;
         }

         Vector<BasicBlock> bsucc = block.succ;
         int size = bsucc.size();
         Instruction[] succs = new Instruction[size];

         for(int i = 0; i < size; ++i) {
            succs[i] = ((BasicBlock)bsucc.elementAt(i)).head;
         }

         insn.succs = succs;
      }

   }

   void jimplify(cp_info[] constant_pool, int this_class) {
      Code_attribute codeAttribute = this.method.locate_code_attribute();
      Set<Instruction> handlerInstructions = new ArraySet();
      Map<Instruction, SootClass> handlerInstructionToException = new HashMap();
      this.buildInsnCFGfromBBCFG();

      Instruction ins;
      Instruction startIns;
      int i;
      Instruction endIns;
      Instruction[] newsuccs;
      for(int i = 0; i < codeAttribute.exception_table_length; ++i) {
         Instruction startIns = codeAttribute.exception_table[i].start_inst;
         ins = codeAttribute.exception_table[i].end_inst;
         startIns = codeAttribute.exception_table[i].handler_inst;
         handlerInstructions.add(startIns);
         i = codeAttribute.exception_table[i].catch_type;
         SootClass exception;
         if (i != 0) {
            CONSTANT_Class_info classinfo = (CONSTANT_Class_info)constant_pool[i];
            String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[classinfo.name_index])).convert();
            name = name.replace('/', '.');
            exception = this.cm.getSootClass(name);
         } else {
            exception = this.cm.getSootClass("java.lang.Throwable");
         }

         handlerInstructionToException.put(startIns, exception);
         if (startIns == ins) {
            throw new RuntimeException("Empty catch range for exception handler");
         }

         endIns = startIns;

         do {
            Instruction[] succs = endIns.succs;
            newsuccs = new Instruction[succs.length + 1];
            System.arraycopy(succs, 0, newsuccs, 0, succs.length);
            newsuccs[succs.length] = startIns;
            endIns.succs = newsuccs;
            endIns = endIns.next;
         } while(endIns != ins && endIns != null);
      }

      Set<Instruction> reachableInstructions = new HashSet();
      LinkedList<Instruction> instructionsToVisit = new LinkedList();
      reachableInstructions.add(this.firstInstruction);
      instructionsToVisit.addLast(this.firstInstruction);

      int i;
      int var43;
      while(!instructionsToVisit.isEmpty()) {
         ins = (Instruction)instructionsToVisit.removeFirst();
         Instruction[] succs = ins.succs;
         Instruction[] var37 = succs;
         i = succs.length;

         for(var43 = 0; var43 < i; ++var43) {
            Instruction succ = var37[var43];
            if (!reachableInstructions.contains(succ)) {
               reachableInstructions.add(succ);
               instructionsToVisit.addLast(succ);
            }
         }
      }

      Map<Instruction, TypeStack> instructionToTypeStack = new HashMap();
      Map<Instruction, TypeStack> instructionToPostTypeStack = new HashMap();
      Set<Instruction> visitedInstructions = new HashSet();
      List<Instruction> changedInstructions = new ArrayList();
      TypeStack initialTypeStack = TypeStack.v();
      instructionToTypeStack.put(this.firstInstruction, initialTypeStack);
      visitedInstructions.add(this.firstInstruction);
      changedInstructions.add(this.firstInstruction);

      while(!changedInstructions.isEmpty()) {
         endIns = (Instruction)changedInstructions.get(0);
         changedInstructions.remove(0);
         OutFlow ret = this.processFlow(endIns, (TypeStack)instructionToTypeStack.get(endIns), constant_pool);
         instructionToPostTypeStack.put(endIns, ret.typeStack);
         newsuccs = endIns.succs;
         Instruction[] var49 = newsuccs;
         int var16 = newsuccs.length;

         for(int var17 = 0; var17 < var16; ++var17) {
            Instruction s = var49[var17];
            TypeStack newTypeStack;
            if (!visitedInstructions.contains(s)) {
               if (handlerInstructions.contains(s)) {
                  newTypeStack = TypeStack.v().push(RefType.v(((SootClass)handlerInstructionToException.get(s)).getName()));
                  instructionToTypeStack.put(s, newTypeStack);
               } else {
                  instructionToTypeStack.put(s, ret.typeStack);
               }

               visitedInstructions.add(s);
               changedInstructions.add(s);
            } else {
               TypeStack oldTypeStack = (TypeStack)instructionToTypeStack.get(s);
               if (handlerInstructions.contains(s)) {
                  TypeStack exceptionTypeStack = TypeStack.v().push(RefType.v(((SootClass)handlerInstructionToException.get(s)).getName()));
                  newTypeStack = exceptionTypeStack;
               } else {
                  try {
                     newTypeStack = ret.typeStack.merge(oldTypeStack);
                  } catch (RuntimeException var23) {
                     logger.debug("Considering " + s);
                     throw var23;
                  }
               }

               if (!newTypeStack.equals(oldTypeStack)) {
                  changedInstructions.add(s);
               }

               instructionToTypeStack.put(s, newTypeStack);
            }
         }
      }

      for(BasicBlock b = this.cfg; b != null; b = b.next) {
         ins = b.head;
         b.statements = new ArrayList();
         List blockStatements = b.statements;

         while(true) {
            List<Stmt> statementsForIns = new ArrayList();
            if (reachableInstructions.contains(ins)) {
               this.generateJimple(ins, (TypeStack)instructionToTypeStack.get(ins), (TypeStack)instructionToPostTypeStack.get(ins), constant_pool, statementsForIns, b);
            } else {
               statementsForIns.add(Jimple.v().newNopStmt());
            }

            if (!statementsForIns.isEmpty()) {
               for(i = 0; i < statementsForIns.size(); ++i) {
                  this.units.add(statementsForIns.get(i));
                  blockStatements.add(statementsForIns.get(i));
               }

               this.instructionToFirstStmt.put(ins, statementsForIns.get(0));
               this.instructionToLastStmt.put(ins, statementsForIns.get(statementsForIns.size() - 1));
            }

            if (ins == b.tail) {
               break;
            }

            ins = ins.next;
         }
      }

      this.jimpleTargetFixup();
      Map<Stmt, Stmt> stmtstags = new HashMap();

      for(int i = 0; i < codeAttribute.exception_table_length; ++i) {
         startIns = codeAttribute.exception_table[i].start_inst;
         endIns = codeAttribute.exception_table[i].end_inst;
         Instruction targetIns = codeAttribute.exception_table[i].handler_inst;
         if (!this.instructionToFirstStmt.containsKey(startIns) || endIns != null && !this.instructionToLastStmt.containsKey(endIns)) {
            throw new RuntimeException("Exception range does not coincide with jimple instructions");
         }

         if (!this.instructionToFirstStmt.containsKey(targetIns)) {
            throw new RuntimeException("Exception handler does not coincide with jimple instruction");
         }

         int catchType = codeAttribute.exception_table[i].catch_type;
         SootClass exception;
         if (catchType != 0) {
            CONSTANT_Class_info classinfo = (CONSTANT_Class_info)constant_pool[catchType];
            String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[classinfo.name_index])).convert();
            name = name.replace('/', '.');
            exception = this.cm.getSootClass(name);
         } else {
            exception = this.cm.getSootClass("java.lang.Throwable");
         }

         Stmt firstStmt = (Stmt)this.instructionToFirstStmt.get(targetIns);
         Object newTarget;
         if (stmtstags.containsKey(firstStmt)) {
            newTarget = (Stmt)stmtstags.get(firstStmt);
         } else {
            Local local = Util.v().getLocalCreatingIfNecessary(this.listBody, "$stack0", UnknownType.v());
            newTarget = Jimple.v().newIdentityStmt(local, Jimple.v().newCaughtExceptionRef());
            ((PatchingChain)this.units).insertBeforeNoRedirect((Unit)newTarget, firstStmt);
            stmtstags.put(firstStmt, newTarget);
            if (this.units.getFirst() != newTarget) {
               Unit prev = (Unit)this.units.getPredOf(newTarget);
               if (prev != null && prev.fallsThrough()) {
                  this.units.insertAfter((Object)Jimple.v().newGotoStmt((Unit)firstStmt), prev);
               }
            }
         }

         firstStmt = (Stmt)this.instructionToFirstStmt.get(startIns);
         Object afterEndStmt;
         if (endIns == null) {
            afterEndStmt = (Stmt)this.units.getLast();
         } else {
            afterEndStmt = (Stmt)this.instructionToLastStmt.get(endIns);
            IdentityStmt catchStart = (IdentityStmt)stmtstags.get(afterEndStmt);
            if (catchStart != null) {
               if (catchStart != this.units.getPredOf(afterEndStmt)) {
                  throw new IllegalStateException("Assertion failure: catchStart != pred of afterEndStmt");
               }

               afterEndStmt = catchStart;
            }
         }

         Trap trap = Jimple.v().newTrap(exception, (Unit)firstStmt, (Unit)afterEndStmt, (Unit)newTarget);
         this.listBody.getTraps().add(trap);
      }

      if (Options.v().keep_line_number()) {
         stmtstags = new HashMap();
         LinkedList<Stmt> startstmts = new LinkedList();
         attribute_info[] attrs = codeAttribute.attributes;
         attribute_info[] var42 = attrs;
         i = attrs.length;

         for(var43 = 0; var43 < i; ++var43) {
            attribute_info element = var42[var43];
            if (element instanceof LineNumberTable_attribute) {
               LineNumberTable_attribute lntattr = (LineNumberTable_attribute)element;
               line_number_table_entry[] var67 = lntattr.line_number_table;
               int var66 = var67.length;

               for(int var68 = 0; var68 < var66; ++var68) {
                  line_number_table_entry element0 = var67[var68];
                  Stmt start_stmt = (Stmt)this.instructionToFirstStmt.get(element0.start_inst);
                  if (start_stmt != null) {
                     LineNumberTag lntag = new LineNumberTag(element0.line_number);
                     stmtstags.put(start_stmt, lntag);
                     startstmts.add(start_stmt);
                  }
               }
            }
         }

         Iterator stmtIt = (new ArrayList(stmtstags.keySet())).iterator();

         Stmt stmt;
         while(stmtIt.hasNext()) {
            stmt = (Stmt)stmtIt.next();
            Stmt pred = stmt;
            Tag tag = (Tag)stmtstags.get(stmt);

            while(true) {
               pred = (Stmt)this.units.getPredOf(pred);
               if (pred == null || !(pred instanceof IdentityStmt)) {
                  break;
               }

               stmtstags.put(pred, tag);
               pred.addTag(tag);
            }
         }

         for(i = 0; i < startstmts.size(); ++i) {
            stmt = (Stmt)startstmts.get(i);
            Tag tag = (Tag)stmtstags.get(stmt);
            stmt.addTag(tag);

            for(stmt = (Stmt)this.units.getSuccOf(stmt); stmt != null && !stmtstags.containsKey(stmt); stmt = (Stmt)this.units.getSuccOf(stmt)) {
               stmt.addTag(tag);
            }
         }
      }

   }

   private Type byteCodeTypeOf(Type type) {
      return (Type)(!type.equals(ShortType.v()) && !type.equals(CharType.v()) && !type.equals(ByteType.v()) && !type.equals(BooleanType.v()) ? type : IntType.v());
   }

   OutFlow processFlow(Instruction ins, TypeStack typeStack, cp_info[] constant_pool) {
      int x = ins.code & 255;
      int j;
      Type type;
      int args;
      Type returnType;
      Type returnType;
      int j;
      Type secondType;
      switch(x) {
      case 0:
      case 116:
      case 117:
      case 118:
      case 119:
      case 132:
      case 145:
      case 146:
      case 147:
      case 167:
      case 169:
      case 177:
      case 191:
      case 200:
      case 202:
      case 209:
         break;
      case 1:
         typeStack = typeStack.push(RefType.v("java.lang.Object"));
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         typeStack = typeStack.push(IntType.v());
         break;
      case 9:
      case 10:
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 11:
      case 12:
      case 13:
         typeStack = typeStack.push(FloatType.v());
         break;
      case 14:
      case 15:
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 16:
         typeStack = typeStack.push(IntType.v());
         break;
      case 17:
         typeStack = typeStack.push(IntType.v());
         break;
      case 18:
         return this.processCPEntry(constant_pool, ((Instruction_Ldc1)ins).arg_b, typeStack, this.jmethod);
      case 19:
      case 20:
         return this.processCPEntry(constant_pool, ((Instruction_intindex)ins).arg_i, typeStack, this.jmethod);
      case 21:
         typeStack = typeStack.push(IntType.v());
         break;
      case 22:
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 23:
         typeStack = typeStack.push(FloatType.v());
         break;
      case 24:
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 25:
         typeStack = typeStack.push(RefType.v("java.lang.Object"));
         break;
      case 26:
      case 27:
      case 28:
      case 29:
         typeStack = typeStack.push(IntType.v());
         break;
      case 30:
      case 31:
      case 32:
      case 33:
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 34:
      case 35:
      case 36:
      case 37:
         typeStack = typeStack.push(FloatType.v());
         break;
      case 38:
      case 39:
      case 40:
      case 41:
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 42:
      case 43:
      case 44:
      case 45:
         typeStack = typeStack.push(RefType.v("java.lang.Object"));
         break;
      case 46:
      case 51:
      case 52:
      case 53:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(IntType.v());
         break;
      case 47:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 48:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(FloatType.v());
         break;
      case 49:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 50:
         typeStack = this.popSafe(typeStack, IntType.v());
         if (typeStack.top() instanceof ArrayType) {
            ArrayType arrayType = (ArrayType)typeStack.top();
            typeStack = this.popSafeRefType(typeStack);
            if (arrayType.numDimensions == 1) {
               typeStack = typeStack.push(arrayType.baseType);
            } else {
               typeStack = typeStack.push(ArrayType.v(arrayType.baseType, arrayType.numDimensions - 1));
            }
         } else {
            typeStack = this.popSafeRefType(typeStack);
            typeStack = typeStack.push(RefType.v("java.lang.Object"));
         }
         break;
      case 54:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 55:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         break;
      case 56:
         typeStack = this.popSafe(typeStack, FloatType.v());
         break;
      case 57:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         break;
      case 58:
         typeStack = typeStack.pop();
         break;
      case 59:
      case 60:
      case 61:
      case 62:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 63:
      case 64:
      case 65:
      case 66:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         break;
      case 67:
      case 68:
      case 69:
      case 70:
         typeStack = this.popSafe(typeStack, FloatType.v());
         break;
      case 71:
      case 72:
      case 73:
      case 74:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         break;
      case 75:
      case 76:
      case 77:
      case 78:
         if (!(typeStack.top() instanceof StmtAddressType) && !(typeStack.top() instanceof RefType) && !(typeStack.top() instanceof ArrayType)) {
            throw new RuntimeException("Astore failed, invalid stack type: " + typeStack.top());
         }

         typeStack = typeStack.pop();
         break;
      case 79:
      case 84:
      case 85:
      case 86:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 80:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 81:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 82:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 83:
         typeStack = this.popSafeRefType(typeStack);
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 87:
         typeStack = typeStack.pop();
         break;
      case 88:
         typeStack = typeStack.pop();
         typeStack = typeStack.pop();
         break;
      case 89:
         typeStack = typeStack.push(typeStack.top());
         break;
      case 90:
         type = typeStack.get(typeStack.topIndex());
         secondType = typeStack.get(typeStack.topIndex() - 1);
         typeStack = typeStack.pop().pop();
         typeStack = typeStack.push(type).push(secondType).push(type);
         break;
      case 91:
         type = typeStack.get(typeStack.topIndex());
         secondType = typeStack.get(typeStack.topIndex() - 1);
         returnType = typeStack.get(typeStack.topIndex() - 2);
         typeStack = typeStack.pop().pop().pop();
         typeStack = typeStack.push(type).push(returnType).push(secondType).push(type);
         break;
      case 92:
         type = typeStack.get(typeStack.topIndex());
         secondType = typeStack.get(typeStack.topIndex() - 1);
         typeStack = typeStack.push(secondType).push(type);
         break;
      case 93:
         type = typeStack.get(typeStack.topIndex());
         secondType = typeStack.get(typeStack.topIndex() - 1);
         returnType = typeStack.get(typeStack.topIndex() - 2);
         typeStack = typeStack.pop().pop().pop();
         typeStack = typeStack.push(secondType).push(type).push(returnType).push(secondType).push(type);
         break;
      case 94:
         type = typeStack.get(typeStack.topIndex());
         secondType = typeStack.get(typeStack.topIndex() - 1);
         returnType = typeStack.get(typeStack.topIndex() - 2);
         returnType = typeStack.get(typeStack.topIndex() - 3);
         typeStack = typeStack.pop().pop().pop().pop();
         typeStack = typeStack.push(secondType).push(type).push(returnType).push(returnType).push(secondType).push(type);
         break;
      case 95:
         type = typeStack.top();
         typeStack = typeStack.pop();
         secondType = typeStack.top();
         typeStack = typeStack.pop();
         typeStack = typeStack.push(type);
         typeStack = typeStack.push(secondType);
         break;
      case 96:
      case 100:
      case 104:
      case 108:
      case 112:
      case 120:
      case 122:
      case 124:
      case 126:
      case 128:
      case 130:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 97:
      case 101:
      case 105:
      case 109:
      case 113:
      case 127:
      case 129:
      case 131:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 98:
      case 102:
      case 106:
      case 110:
      case 114:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = typeStack.push(FloatType.v());
         break;
      case 99:
      case 103:
      case 107:
      case 111:
      case 115:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 121:
      case 123:
      case 125:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 133:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 134:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = typeStack.push(FloatType.v());
         break;
      case 135:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 136:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 137:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(FloatType.v());
         break;
      case 138:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 139:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 140:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 141:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
         break;
      case 142:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 143:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
         break;
      case 144:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = typeStack.push(FloatType.v());
         break;
      case 148:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 149:
      case 150:
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = this.popSafe(typeStack, FloatType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 151:
      case 152:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         typeStack = typeStack.push(IntType.v());
         break;
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 165:
      case 166:
         typeStack = this.popSafeRefType(typeStack);
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 168:
      case 201:
         typeStack = typeStack.push(StmtAddressType.v());
         break;
      case 170:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 171:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 172:
         typeStack = this.popSafe(typeStack, IntType.v());
         break;
      case 173:
         typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
         typeStack = this.popSafe(typeStack, LongType.v());
         break;
      case 174:
         typeStack = this.popSafe(typeStack, FloatType.v());
         break;
      case 175:
         typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
         typeStack = this.popSafe(typeStack, DoubleType.v());
         break;
      case 176:
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 178:
         type = this.byteCodeTypeOf(this.jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Getstatic)ins).arg_i));
         if (type.equals(DoubleType.v())) {
            typeStack = typeStack.push(DoubleType.v());
            typeStack = typeStack.push(Double2ndHalfType.v());
         } else if (type.equals(LongType.v())) {
            typeStack = typeStack.push(LongType.v());
            typeStack = typeStack.push(Long2ndHalfType.v());
         } else {
            typeStack = typeStack.push(type);
         }
         break;
      case 179:
         type = this.byteCodeTypeOf(this.jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Putstatic)ins).arg_i));
         if (type.equals(DoubleType.v())) {
            typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
            typeStack = this.popSafe(typeStack, DoubleType.v());
         } else if (type.equals(LongType.v())) {
            typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
            typeStack = this.popSafe(typeStack, LongType.v());
         } else if (type instanceof RefType) {
            typeStack = this.popSafeRefType(typeStack);
         } else {
            typeStack = this.popSafe(typeStack, type);
         }
         break;
      case 180:
         type = this.byteCodeTypeOf(this.jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Getfield)ins).arg_i));
         typeStack = this.popSafeRefType(typeStack);
         if (type.equals(DoubleType.v())) {
            typeStack = typeStack.push(DoubleType.v());
            typeStack = typeStack.push(Double2ndHalfType.v());
         } else if (type.equals(LongType.v())) {
            typeStack = typeStack.push(LongType.v());
            typeStack = typeStack.push(Long2ndHalfType.v());
         } else {
            typeStack = typeStack.push(type);
         }
         break;
      case 181:
         type = this.byteCodeTypeOf(this.jimpleTypeOfFieldInFieldRef(this.cm, constant_pool, ((Instruction_Putfield)ins).arg_i));
         if (type.equals(DoubleType.v())) {
            typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
            typeStack = this.popSafe(typeStack, DoubleType.v());
         } else if (type.equals(LongType.v())) {
            typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
            typeStack = this.popSafe(typeStack, LongType.v());
         } else if (type instanceof RefType) {
            typeStack = this.popSafeRefType(typeStack);
         } else {
            typeStack = this.popSafe(typeStack, type);
         }

         typeStack = this.popSafeRefType(typeStack);
         break;
      case 182:
         Instruction_Invokevirtual iv = (Instruction_Invokevirtual)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         returnType = this.byteCodeTypeOf(this.jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv.arg_i));

         for(j = args - 1; j >= 0; --j) {
            if (typeStack.top().equals(Long2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
               typeStack = this.popSafe(typeStack, LongType.v());
            } else if (typeStack.top().equals(Double2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
               typeStack = this.popSafe(typeStack, DoubleType.v());
            } else {
               typeStack = this.popSafe(typeStack, typeStack.top());
            }
         }

         typeStack = this.popSafeRefType(typeStack);
         if (!returnType.equals(VoidType.v())) {
            typeStack = this.smartPush(typeStack, returnType);
         }
         break;
      case 183:
         Instruction_Invokenonvirtual iv = (Instruction_Invokenonvirtual)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         returnType = this.byteCodeTypeOf(this.jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv.arg_i));

         for(j = args - 1; j >= 0; --j) {
            if (typeStack.top().equals(Long2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
               typeStack = this.popSafe(typeStack, LongType.v());
            } else if (typeStack.top().equals(Double2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
               typeStack = this.popSafe(typeStack, DoubleType.v());
            } else {
               typeStack = this.popSafe(typeStack, typeStack.top());
            }
         }

         typeStack = this.popSafeRefType(typeStack);
         if (!returnType.equals(VoidType.v())) {
            typeStack = this.smartPush(typeStack, returnType);
         }
         break;
      case 184:
         Instruction_Invokestatic iv = (Instruction_Invokestatic)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         returnType = this.byteCodeTypeOf(this.jimpleReturnTypeOfMethodRef(this.cm, constant_pool, iv.arg_i));

         for(j = args - 1; j >= 0; --j) {
            if (typeStack.top().equals(Long2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
               typeStack = this.popSafe(typeStack, LongType.v());
            } else if (typeStack.top().equals(Double2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
               typeStack = this.popSafe(typeStack, DoubleType.v());
            } else {
               typeStack = this.popSafe(typeStack, typeStack.top());
            }
         }

         if (!returnType.equals(VoidType.v())) {
            typeStack = this.smartPush(typeStack, returnType);
         }
         break;
      case 185:
         Instruction_Invokeinterface iv = (Instruction_Invokeinterface)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         returnType = this.byteCodeTypeOf(this.jimpleReturnTypeOfInterfaceMethodRef(this.cm, constant_pool, iv.arg_i));

         for(j = args - 1; j >= 0; --j) {
            if (typeStack.top().equals(Long2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
               typeStack = this.popSafe(typeStack, LongType.v());
            } else if (typeStack.top().equals(Double2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
               typeStack = this.popSafe(typeStack, DoubleType.v());
            } else {
               typeStack = this.popSafe(typeStack, typeStack.top());
            }
         }

         typeStack = this.popSafeRefType(typeStack);
         if (!returnType.equals(VoidType.v())) {
            typeStack = this.smartPush(typeStack, returnType);
         }
         break;
      case 186:
         Instruction_Invokedynamic iv = (Instruction_Invokedynamic)ins;
         CONSTANT_InvokeDynamic_info iv_info = (CONSTANT_InvokeDynamic_info)constant_pool[iv.invoke_dynamic_index];
         int args = cp_info.countParams(constant_pool, iv_info.name_and_type_index);
         returnType = this.byteCodeTypeOf(this.jimpleReturnTypeOfNameAndType(this.cm, constant_pool, iv_info.name_and_type_index));

         for(j = args - 1; j >= 0; --j) {
            if (typeStack.top().equals(Long2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Long2ndHalfType.v());
               typeStack = this.popSafe(typeStack, LongType.v());
            } else if (typeStack.top().equals(Double2ndHalfType.v())) {
               typeStack = this.popSafe(typeStack, Double2ndHalfType.v());
               typeStack = this.popSafe(typeStack, DoubleType.v());
            } else {
               typeStack = this.popSafe(typeStack, typeStack.top());
            }
         }

         if (!returnType.equals(VoidType.v())) {
            typeStack = this.smartPush(typeStack, returnType);
         }
         break;
      case 187:
         Type type = RefType.v(this.getClassName(constant_pool, ((Instruction_New)ins).arg_i));
         typeStack = typeStack.push(type);
         break;
      case 188:
         typeStack = this.popSafe(typeStack, IntType.v());
         type = this.jimpleTypeOfAtype(((Instruction_Newarray)ins).atype);
         typeStack = typeStack.push(ArrayType.v(type, 1));
         break;
      case 189:
         CONSTANT_Class_info c = (CONSTANT_Class_info)constant_pool[((Instruction_Anewarray)ins).arg_i];
         String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         name = name.replace('/', '.');
         Object baseType;
         if (name.startsWith("[")) {
            String baseName = this.getClassName(constant_pool, ((Instruction_Anewarray)ins).arg_i);
            baseType = Util.v().jimpleTypeOfFieldDescriptor(baseName);
         } else {
            baseType = RefType.v(name);
         }

         typeStack = this.popSafe(typeStack, IntType.v());
         typeStack = typeStack.push(((Type)baseType).makeArrayType());
         break;
      case 190:
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(IntType.v());
         break;
      case 192:
         String className = this.getClassName(constant_pool, ((Instruction_Checkcast)ins).arg_i);
         Object castType;
         if (className.startsWith("[")) {
            castType = Util.v().jimpleTypeOfFieldDescriptor(this.getClassName(constant_pool, ((Instruction_Checkcast)ins).arg_i));
         } else {
            castType = RefType.v(className);
         }

         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push((Type)castType);
         break;
      case 193:
         typeStack = this.popSafeRefType(typeStack);
         typeStack = typeStack.push(IntType.v());
         break;
      case 194:
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 195:
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 196:
         throw new RuntimeException("Wide instruction should not be encountered");
      case 197:
         int bdims = ((Instruction_Multianewarray)ins).dims;
         CONSTANT_Class_info c = (CONSTANT_Class_info)constant_pool[((Instruction_Multianewarray)ins).arg_i];
         String arrayDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         ArrayType arrayType = (ArrayType)Util.v().jimpleTypeOfFieldDescriptor(arrayDescriptor);

         for(j = 0; j < bdims; ++j) {
            typeStack = this.popSafe(typeStack, IntType.v());
         }

         typeStack = typeStack.push(arrayType);
         break;
      case 198:
      case 199:
         typeStack = this.popSafeRefType(typeStack);
         break;
      case 203:
      case 204:
      case 205:
      case 206:
      case 207:
      case 208:
      default:
         throw new RuntimeException("processFlow failed: Unknown bytecode instruction: " + x);
      }

      return new OutFlow(typeStack);
   }

   private Type jimpleTypeOfFieldInFieldRef(Scene cm, cp_info[] constant_pool, int index) {
      CONSTANT_Fieldref_info fr = (CONSTANT_Fieldref_info)((CONSTANT_Fieldref_info)constant_pool[index]);
      CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[fr.name_and_type_index]);
      String fieldDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nat.descriptor_index])).convert();
      return Util.v().jimpleTypeOfFieldDescriptor(fieldDescriptor);
   }

   private Type jimpleReturnTypeOfNameAndType(Scene cm, cp_info[] constant_pool, int index) {
      CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[index]);
      String methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nat.descriptor_index])).convert();
      return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
   }

   private Type jimpleReturnTypeOfMethodRef(Scene cm, cp_info[] constant_pool, int index) {
      CONSTANT_Methodref_info mr = (CONSTANT_Methodref_info)((CONSTANT_Methodref_info)constant_pool[index]);
      CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[mr.name_and_type_index]);
      String methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nat.descriptor_index])).convert();
      return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
   }

   private Type jimpleReturnTypeOfInterfaceMethodRef(Scene cm, cp_info[] constant_pool, int index) {
      CONSTANT_InterfaceMethodref_info mr = (CONSTANT_InterfaceMethodref_info)((CONSTANT_InterfaceMethodref_info)constant_pool[index]);
      CONSTANT_NameAndType_info nat = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[mr.name_and_type_index]);
      String methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nat.descriptor_index])).convert();
      return Util.v().jimpleReturnTypeOfMethodDescriptor(methodDescriptor);
   }

   private OutFlow processCPEntry(cp_info[] constant_pool, int i, TypeStack typeStack, SootMethod jmethod) {
      cp_info c = constant_pool[i];
      if (c instanceof CONSTANT_Integer_info) {
         typeStack = typeStack.push(IntType.v());
      } else if (c instanceof CONSTANT_Float_info) {
         typeStack = typeStack.push(FloatType.v());
      } else if (c instanceof CONSTANT_Long_info) {
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
      } else if (c instanceof CONSTANT_Double_info) {
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
      } else if (c instanceof CONSTANT_String_info) {
         typeStack = typeStack.push(RefType.v("java.lang.String"));
      } else if (c instanceof CONSTANT_Utf8_info) {
         typeStack = typeStack.push(RefType.v("java.lang.String"));
      } else {
         if (!(c instanceof CONSTANT_Class_info)) {
            throw new RuntimeException("Attempting to push a non-constant cp entry" + c.getClass());
         }

         CONSTANT_Class_info info = (CONSTANT_Class_info)c;
         String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[info.name_index])).convert();
         name = name.replace('/', '.');
         if (name.charAt(0) == '[') {
            int dim;
            for(dim = 0; name.charAt(dim) == '['; ++dim) {
            }

            Type baseType = null;
            char typeIndicator = name.charAt(dim);
            switch(typeIndicator) {
            case 'B':
               baseType = ByteType.v();
               break;
            case 'C':
               baseType = CharType.v();
               break;
            case 'D':
               baseType = DoubleType.v();
               break;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
               throw new RuntimeException("Unknown Array Base Type in Class Constant");
            case 'F':
               baseType = FloatType.v();
               break;
            case 'I':
               baseType = IntType.v();
               break;
            case 'J':
               baseType = LongType.v();
               break;
            case 'L':
               baseType = RefType.v(name.substring(dim + 1, name.length() - 1));
               break;
            case 'S':
               baseType = ShortType.v();
               break;
            case 'Z':
               baseType = BooleanType.v();
            }

            typeStack = typeStack.push(ArrayType.v((Type)baseType, dim));
         } else {
            typeStack = typeStack.push(RefType.v(name));
         }
      }

      return new OutFlow(typeStack);
   }

   TypeStack smartPush(TypeStack typeStack, Type type) {
      if (type.equals(LongType.v())) {
         typeStack = typeStack.push(LongType.v());
         typeStack = typeStack.push(Long2ndHalfType.v());
      } else if (type.equals(DoubleType.v())) {
         typeStack = typeStack.push(DoubleType.v());
         typeStack = typeStack.push(Double2ndHalfType.v());
      } else {
         typeStack = typeStack.push(type);
      }

      return typeStack;
   }

   TypeStack popSafeRefType(TypeStack typeStack) {
      return typeStack.pop();
   }

   TypeStack popSafeArrayType(TypeStack typeStack) {
      return typeStack.pop();
   }

   TypeStack popSafe(TypeStack typeStack, Type requiredType) {
      return typeStack.pop();
   }

   void confirmType(Type actualType, Type requiredType) {
   }

   String getClassName(cp_info[] constant_pool, int index) {
      CONSTANT_Class_info c = (CONSTANT_Class_info)constant_pool[index];
      String name = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
      return name.replace('/', '.');
   }

   void confirmRefType(Type actualType) {
   }

   private void processTargetFixup(BBQ bbq) {
      label85:
      while(true) {
         if (!bbq.isEmpty()) {
            BasicBlock b;
            try {
               b = bbq.pull();
            } catch (NoSuchElementException var9) {
               return;
            }

            Stmt s = b.getTailJStmt();
            int count;
            if (s instanceof GotoStmt) {
               if (b.succ.size() != 1) {
                  logger.debug("Error :");

                  for(count = 0; count < b.statements.size(); ++count) {
                     logger.debug("" + b.statements.get(count));
                  }

                  throw new RuntimeException(b + " has " + b.succ.size() + " successors.");
               }

               ((GotoStmt)s).setTarget(((BasicBlock)b.succ.firstElement()).getHeadJStmt());
            } else if (s instanceof IfStmt) {
               if (b.succ.size() != 2) {
                  logger.debug("How can an if not have 2 successors?");
               }

               if (b.succ.firstElement() == b.next) {
                  ((IfStmt)s).setTarget(((BasicBlock)b.succ.elementAt(1)).getHeadJStmt());
               } else {
                  ((IfStmt)s).setTarget(((BasicBlock)b.succ.firstElement()).getHeadJStmt());
               }
            } else {
               Iterator var7;
               BasicBlock basicBlock;
               if (s instanceof TableSwitchStmt) {
                  count = 0;
                  TableSwitchStmt sts = (TableSwitchStmt)s;

                  for(var7 = b.succ.iterator(); var7.hasNext(); ++count) {
                     basicBlock = (BasicBlock)var7.next();
                     if (count == 0) {
                        sts.setDefaultTarget(basicBlock.getHeadJStmt());
                     } else {
                        sts.setTarget(count - 1, basicBlock.getHeadJStmt());
                     }
                  }
               } else if (s instanceof LookupSwitchStmt) {
                  count = 0;
                  LookupSwitchStmt sls = (LookupSwitchStmt)s;

                  for(var7 = b.succ.iterator(); var7.hasNext(); ++count) {
                     basicBlock = (BasicBlock)var7.next();
                     if (count == 0) {
                        sls.setDefaultTarget(basicBlock.getHeadJStmt());
                     } else {
                        sls.setTarget(count - 1, basicBlock.getHeadJStmt());
                     }
                  }
               }
            }

            b.done = false;
            Iterator var12 = b.succ.iterator();

            while(true) {
               if (!var12.hasNext()) {
                  continue label85;
               }

               BasicBlock basicBlock = (BasicBlock)var12.next();
               if (basicBlock.done) {
                  bbq.push(basicBlock);
               }
            }
         }

         return;
      }
   }

   void jimpleTargetFixup() {
      BBQ bbq = new BBQ();
      Code_attribute c = this.method.locate_code_attribute();
      if (c != null) {
         for(BasicBlock bb = this.cfg; bb != null; bb = bb.next) {
            bb.done = true;
         }

         bbq.push(this.cfg);
         this.processTargetFixup(bbq);
         if (bbq.isEmpty()) {
            for(int i = 0; i < c.exception_table_length; ++i) {
               BasicBlock b = c.exception_table[i].b;
               if (b != null && b.done) {
                  bbq.push(b);
                  this.processTargetFixup(bbq);
                  if (!bbq.isEmpty()) {
                     logger.debug("Error 2nd processing exception block.");
                     break;
                  }
               }
            }
         }

      }
   }

   private void generateJimpleForCPEntry(cp_info[] constant_pool, int i, TypeStack typeStack, TypeStack postTypeStack, SootMethod jmethod, List<Stmt> statements) {
      cp_info c = constant_pool[i];
      AssignStmt stmt;
      if (c instanceof CONSTANT_Integer_info) {
         CONSTANT_Integer_info ci = (CONSTANT_Integer_info)c;
         Value rvalue = IntConstant.v((int)ci.bytes);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
      } else if (c instanceof CONSTANT_Float_info) {
         CONSTANT_Float_info cf = (CONSTANT_Float_info)c;
         Value rvalue = FloatConstant.v(cf.convert());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
      } else if (c instanceof CONSTANT_Long_info) {
         CONSTANT_Long_info cl = (CONSTANT_Long_info)c;
         Value rvalue = LongConstant.v(cl.convert());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
      } else if (c instanceof CONSTANT_Double_info) {
         CONSTANT_Double_info cd = (CONSTANT_Double_info)c;
         Value rvalue = DoubleConstant.v(cd.convert());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
      } else {
         String constant;
         StringConstant rvalue;
         if (c instanceof CONSTANT_String_info) {
            CONSTANT_String_info cs = (CONSTANT_String_info)c;
            constant = cs.toString(constant_pool);
            if (constant.startsWith("\"") && constant.endsWith("\"")) {
               constant = constant.substring(1, constant.length() - 1);
            }

            rvalue = StringConstant.v(constant);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else if (c instanceof CONSTANT_Utf8_info) {
            CONSTANT_Utf8_info cu = (CONSTANT_Utf8_info)c;
            constant = cu.convert();
            if (constant.startsWith("\"") && constant.endsWith("\"")) {
               constant = constant.substring(1, constant.length() - 1);
            }

            rvalue = StringConstant.v(constant);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            if (!(c instanceof CONSTANT_Class_info)) {
               throw new RuntimeException("Attempting to push a non-constant cp entry" + c);
            }

            String className = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[((CONSTANT_Class_info)c).name_index])).convert();
            Value rvalue = ClassConstant.v(className);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         }
      }

      statements.add(stmt);
   }

   void generateJimple(Instruction ins, TypeStack typeStack, TypeStack postTypeStack, cp_info[] constant_pool, List<Stmt> statements, BasicBlock basicBlock) {
      Local l1 = null;
      Local l2 = null;
      Local l3 = null;
      Local l4 = null;
      Expr rhs = null;
      ConditionExpr co = null;
      ArrayRef a = null;
      Stmt stmt = null;
      int x = ins.code & 255;
      Value[] params;
      int args;
      Local local;
      int j;
      CastExpr rhs;
      EqExpr co;
      NewArrayExpr rhs;
      CmpgExpr rhs;
      CmplExpr rhs;
      LeExpr co;
      GtExpr co;
      GeExpr co;
      LtExpr co;
      IntConstant rvalue;
      AddExpr rhs;
      XorExpr rhs;
      OrExpr rhs;
      AndExpr rhs;
      UshrExpr rhs;
      String className;
      Object castType;
      ShrExpr rhs;
      String mstype;
      ShlExpr rhs;
      SootMethodRef bootstrapMethodRef;
      Type returnType;
      RemExpr rhs;
      CONSTANT_Methodref_info methodInfo;
      DivExpr rhs;
      int j;
      MulExpr rhs;
      CONSTANT_Class_info c;
      String className;
      CONSTANT_Fieldref_info fieldInfo;
      String fieldName;
      SubExpr rhs;
      StaticFieldRef fr;
      String methodName;
      int npairs;
      SootClass bclass;
      CONSTANT_NameAndType_info i;
      Type fieldType;
      SootClass bclass;
      SootFieldRef fieldRef;
      AssignStmt stmt;
      switch(x) {
      case 0:
         stmt = Jimple.v().newNopStmt();
         break;
      case 1:
         Value rvalue = NullConstant.v();
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         rvalue = IntConstant.v(x - 3);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 9:
      case 10:
         Value rvalue = LongConstant.v((long)(x - 9));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 11:
      case 12:
      case 13:
         Value rvalue = FloatConstant.v((float)(x - 11));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 14:
      case 15:
         Value rvalue = DoubleConstant.v((double)(x - 14));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 16:
         rvalue = IntConstant.v(((Instruction_Bipush)ins).arg_b);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 17:
         rvalue = IntConstant.v(((Instruction_Sipush)ins).arg_i);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         break;
      case 18:
         this.generateJimpleForCPEntry(constant_pool, ((Instruction_Ldc1)ins).arg_b, typeStack, postTypeStack, this.jmethod, statements);
         break;
      case 19:
      case 20:
         this.generateJimpleForCPEntry(constant_pool, ((Instruction_intindex)ins).arg_i, typeStack, postTypeStack, this.jmethod, statements);
         break;
      case 21:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 22:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 23:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 24:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 25:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 26:
      case 27:
      case 28:
      case 29:
         local = Util.v().getLocalForIndex(this.listBody, x - 26, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 30:
      case 31:
      case 32:
      case 33:
         local = Util.v().getLocalForIndex(this.listBody, x - 30, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 34:
      case 35:
      case 36:
      case 37:
         local = Util.v().getLocalForIndex(this.listBody, x - 34, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 38:
      case 39:
      case 40:
      case 41:
         local = Util.v().getLocalForIndex(this.listBody, x - 38, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 42:
      case 43:
      case 44:
      case 45:
         local = Util.v().getLocalForIndex(this.listBody, x - 42, ins);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), local);
         break;
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
         a = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), a);
         break;
      case 54:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 55:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 56:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 57:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 58:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_bytevar)ins).arg_b, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 59:
      case 60:
      case 61:
      case 62:
         local = Util.v().getLocalForIndex(this.listBody, x - 59, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 63:
      case 64:
      case 65:
      case 66:
         local = Util.v().getLocalForIndex(this.listBody, x - 63, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 67:
      case 68:
      case 69:
      case 70:
         local = Util.v().getLocalForIndex(this.listBody, x - 67, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 71:
      case 72:
      case 73:
      case 74:
         local = Util.v().getLocalForIndex(this.listBody, x - 71, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 75:
      case 76:
      case 77:
      case 78:
         local = Util.v().getLocalForIndex(this.listBody, x - 75, ins);
         stmt = Jimple.v().newAssignStmt(local, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 79:
      case 81:
      case 83:
      case 84:
      case 85:
      case 86:
         a = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(a, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 80:
      case 82:
         a = Jimple.v().newArrayRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2));
         stmt = Jimple.v().newAssignStmt(a, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 87:
      case 88:
         stmt = Jimple.v().newNopStmt();
         break;
      case 89:
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 90:
         l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
         l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1);
         statements.add(stmt);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
         statements.add(stmt);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()));
         statements.add(stmt);
         stmt = null;
         break;
      case 91:
         if (this.typeSize(typeStack.get(typeStack.topIndex() - 2)) == 2) {
            l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
            l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l1);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1);
            statements.add(stmt);
            stmt = null;
         } else {
            l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
            l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
            l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()));
            statements.add(stmt);
            stmt = null;
         }
         break;
      case 92:
         if (this.typeSize(typeStack.top()) == 2) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         } else {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
            statements.add(stmt);
            stmt = null;
         }
         break;
      case 93:
         if (this.typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
            l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
            l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1));
            statements.add(stmt);
            stmt = null;
         } else {
            l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
            l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
            l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()));
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1));
            statements.add(stmt);
            stmt = null;
         }
         break;
      case 94:
         if (this.typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
            l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
            statements.add(stmt);
         } else {
            l1 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            l2 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1), l2);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), l1);
            statements.add(stmt);
         }

         if (this.typeSize(typeStack.get(typeStack.topIndex() - 3)) == 2) {
            l4 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l4);
            statements.add(stmt);
         } else {
            l4 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3);
            l3 = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 3), l4);
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 2), l3);
            statements.add(stmt);
         }

         if (this.typeSize(typeStack.get(typeStack.topIndex() - 1)) == 2) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 5), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1));
            statements.add(stmt);
         } else {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 5), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1));
            statements.add(stmt);
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 4), Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()));
            statements.add(stmt);
         }

         stmt = null;
         break;
      case 95:
         typeStack = typeStack.push(typeStack.top());
         local = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
         typeStack = typeStack.pop();
         Local second = Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex());
         Local third = Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex() - 1);
         stmt = Jimple.v().newAssignStmt(local, second);
         statements.add(stmt);
         stmt = Jimple.v().newAssignStmt(second, third);
         statements.add(stmt);
         stmt = Jimple.v().newAssignStmt(third, local);
         statements.add(stmt);
         stmt = null;
         break;
      case 96:
      case 98:
         rhs = Jimple.v().newAddExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 97:
      case 99:
         rhs = Jimple.v().newAddExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 100:
      case 102:
         rhs = Jimple.v().newSubExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 101:
      case 103:
         rhs = Jimple.v().newSubExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 104:
      case 106:
         rhs = Jimple.v().newMulExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 105:
      case 107:
         rhs = Jimple.v().newMulExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 108:
      case 110:
         rhs = Jimple.v().newDivExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 109:
      case 111:
         rhs = Jimple.v().newDivExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 112:
      case 114:
         rhs = Jimple.v().newRemExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 113:
      case 115:
         rhs = Jimple.v().newRemExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 116:
      case 117:
      case 118:
      case 119:
         Expr rhs = Jimple.v().newNegExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 120:
         rhs = Jimple.v().newShlExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 121:
         rhs = Jimple.v().newShlExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 122:
         rhs = Jimple.v().newShrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 123:
         rhs = Jimple.v().newShrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 124:
         rhs = Jimple.v().newUshrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 125:
         rhs = Jimple.v().newUshrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 2), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 126:
         rhs = Jimple.v().newAndExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 127:
         rhs = Jimple.v().newAndExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 128:
         rhs = Jimple.v().newOrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 129:
         rhs = Jimple.v().newOrExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 130:
         rhs = Jimple.v().newXorExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 131:
         rhs = Jimple.v().newXorExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 132:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_Iinc)ins).arg_b, ins);
         npairs = ((Instruction_Iinc)ins).arg_c;
         rhs = Jimple.v().newAddExpr(local, IntConstant.v(npairs));
         stmt = Jimple.v().newAssignStmt(local, rhs);
         break;
      case 133:
      case 140:
      case 143:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), LongType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 134:
      case 137:
      case 144:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), FloatType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 135:
      case 138:
      case 141:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), DoubleType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 136:
      case 139:
      case 142:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 145:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), ByteType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 146:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), CharType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 147:
         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), ShortType.v());
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 148:
         Expr rhs = Jimple.v().newCmpExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 149:
         rhs = Jimple.v().newCmplExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 150:
         rhs = Jimple.v().newCmpgExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 151:
         rhs = Jimple.v().newCmplExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 152:
         rhs = Jimple.v().newCmpgExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 3), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 153:
         co = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 154:
         co = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 155:
         co = Jimple.v().newLtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 156:
         co = Jimple.v().newGeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 157:
         co = Jimple.v().newGtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 158:
         co = Jimple.v().newLeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), IntConstant.v(0));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 159:
         co = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 160:
         co = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 161:
         co = Jimple.v().newLtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 162:
         co = Jimple.v().newGeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 163:
         co = Jimple.v().newGtExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 164:
         co = Jimple.v().newLeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 165:
         co = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 166:
         co = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - 1), Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 167:
         stmt = Jimple.v().newGotoStmt((Unit)(new FutureStmt()));
         break;
      case 168:
      case 201:
      case 203:
      case 204:
      case 205:
      case 206:
      case 207:
      case 208:
      default:
         throw new RuntimeException("Unrecognized bytecode instruction: " + x);
      case 169:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_Ret)ins).arg_b, ins);
         stmt = Jimple.v().newRetStmt(local);
         break;
      case 170:
         int lowIndex = ((Instruction_Tableswitch)ins).low;
         npairs = ((Instruction_Tableswitch)ins).high;
         j = npairs - lowIndex + 1;
         stmt = Jimple.v().newTableSwitchStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), lowIndex, npairs, Arrays.asList(), (Unit)(new FutureStmt()));
         break;
      case 171:
         List<IntConstant> matches = new ArrayList();
         npairs = ((Instruction_Lookupswitch)ins).npairs;

         for(j = 0; j < npairs; ++j) {
            matches.add(IntConstant.v(((Instruction_Lookupswitch)ins).match_offsets[j * 2]));
         }

         stmt = Jimple.v().newLookupSwitchStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), matches, Arrays.asList(), (Unit)(new FutureStmt()));
         break;
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
         stmt = Jimple.v().newReturnStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 177:
         stmt = Jimple.v().newReturnVoidStmt();
         break;
      case 178:
         local = null;
         fieldInfo = (CONSTANT_Fieldref_info)constant_pool[((Instruction_Getstatic)ins).arg_i];
         c = (CONSTANT_Class_info)constant_pool[fieldInfo.class_index];
         className = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         className = className.replace('/', '.');
         i = (CONSTANT_NameAndType_info)constant_pool[fieldInfo.name_and_type_index];
         fieldName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.name_index])).convert();
         methodName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.descriptor_index])).convert();
         fieldType = Util.v().jimpleTypeOfFieldDescriptor(methodName);
         bclass = this.cm.getSootClass(className);
         fieldRef = Scene.v().makeFieldRef(bclass, fieldName, fieldType, true);
         fr = Jimple.v().newStaticFieldRef(fieldRef);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), fr);
         break;
      case 179:
         local = null;
         fieldInfo = (CONSTANT_Fieldref_info)constant_pool[((Instruction_Putstatic)ins).arg_i];
         c = (CONSTANT_Class_info)constant_pool[fieldInfo.class_index];
         className = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         className = className.replace('/', '.');
         i = (CONSTANT_NameAndType_info)constant_pool[fieldInfo.name_and_type_index];
         fieldName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.name_index])).convert();
         methodName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.descriptor_index])).convert();
         fieldType = Util.v().jimpleTypeOfFieldDescriptor(methodName);
         bclass = this.cm.getSootClass(className);
         fieldRef = Scene.v().makeFieldRef(bclass, fieldName, fieldType, true);
         fr = Jimple.v().newStaticFieldRef(fieldRef);
         stmt = Jimple.v().newAssignStmt(fr, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 180:
         local = null;
         fieldInfo = (CONSTANT_Fieldref_info)constant_pool[((Instruction_Getfield)ins).arg_i];
         c = (CONSTANT_Class_info)constant_pool[fieldInfo.class_index];
         className = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         className = className.replace('/', '.');
         i = (CONSTANT_NameAndType_info)constant_pool[fieldInfo.name_and_type_index];
         fieldName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.name_index])).convert();
         methodName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.descriptor_index])).convert();
         if (className.charAt(0) == '[') {
            className = "java.lang.Object";
         }

         bclass = this.cm.getSootClass(className);
         Type fieldType = Util.v().jimpleTypeOfFieldDescriptor(methodName);
         fieldRef = Scene.v().makeFieldRef(bclass, fieldName, fieldType, false);
         InstanceFieldRef fr = Jimple.v().newInstanceFieldRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), fieldRef);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), fr);
         break;
      case 181:
         CONSTANT_Fieldref_info fieldInfo = (CONSTANT_Fieldref_info)constant_pool[((Instruction_Putfield)ins).arg_i];
         CONSTANT_Class_info c = (CONSTANT_Class_info)constant_pool[fieldInfo.class_index];
         mstype = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
         mstype = mstype.replace('/', '.');
         CONSTANT_NameAndType_info i = (CONSTANT_NameAndType_info)constant_pool[fieldInfo.name_and_type_index];
         String fieldName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.name_index])).convert();
         fieldName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.descriptor_index])).convert();
         Type fieldType = Util.v().jimpleTypeOfFieldDescriptor(fieldName);
         bclass = this.cm.getSootClass(mstype);
         SootFieldRef fieldRef = Scene.v().makeFieldRef(bclass, fieldName, fieldType, false);
         InstanceFieldRef fr = Jimple.v().newInstanceFieldRef(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - this.typeSize(typeStack.top())), fieldRef);
         Value rvalue = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
         stmt = Jimple.v().newAssignStmt(fr, rvalue);
         break;
      case 182:
         Instruction_Invokevirtual iv = (Instruction_Invokevirtual)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         methodInfo = (CONSTANT_Methodref_info)constant_pool[iv.arg_i];
         bootstrapMethodRef = this.createMethodRef(constant_pool, methodInfo, false);
         returnType = bootstrapMethodRef.returnType();
         params = new Value[args];

         for(j = args - 1; j >= 0; --j) {
            params[j] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            if (this.typeSize(typeStack.top()) == 2) {
               typeStack = typeStack.pop();
               typeStack = typeStack.pop();
            } else {
               typeStack = typeStack.pop();
            }
         }

         Value rvalue = Jimple.v().newVirtualInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), bootstrapMethodRef, Arrays.asList(params));
         if (!returnType.equals(VoidType.v())) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            stmt = Jimple.v().newInvokeStmt(rvalue);
         }
         break;
      case 183:
         Instruction_Invokenonvirtual iv = (Instruction_Invokenonvirtual)ins;
         args = cp_info.countParams(constant_pool, iv.arg_i);
         methodInfo = (CONSTANT_Methodref_info)constant_pool[iv.arg_i];
         bootstrapMethodRef = this.createMethodRef(constant_pool, methodInfo, false);
         returnType = bootstrapMethodRef.returnType();
         params = new Value[args];

         for(j = args - 1; j >= 0; --j) {
            params[j] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            if (this.typeSize(typeStack.top()) == 2) {
               typeStack = typeStack.pop();
               typeStack = typeStack.pop();
            } else {
               typeStack = typeStack.pop();
            }
         }

         Value rvalue = Jimple.v().newSpecialInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), bootstrapMethodRef, Arrays.asList(params));
         if (!returnType.equals(VoidType.v())) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            stmt = Jimple.v().newInvokeStmt(rvalue);
         }
         break;
      case 184:
         Instruction_Invokestatic is = (Instruction_Invokestatic)ins;
         args = cp_info.countParams(constant_pool, is.arg_i);
         methodInfo = (CONSTANT_Methodref_info)constant_pool[is.arg_i];
         bootstrapMethodRef = this.createMethodRef(constant_pool, methodInfo, true);
         returnType = bootstrapMethodRef.returnType();
         params = new Value[args];

         for(j = args - 1; j >= 0; --j) {
            params[j] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            if (this.typeSize(typeStack.top()) == 2) {
               typeStack = typeStack.pop();
               typeStack = typeStack.pop();
            } else {
               typeStack = typeStack.pop();
            }
         }

         Value rvalue = Jimple.v().newStaticInvokeExpr(bootstrapMethodRef, Arrays.asList(params));
         if (!returnType.equals(VoidType.v())) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            stmt = Jimple.v().newInvokeStmt(rvalue);
         }
         break;
      case 185:
         Instruction_Invokeinterface ii = (Instruction_Invokeinterface)ins;
         args = cp_info.countParams(constant_pool, ii.arg_i);
         CONSTANT_InterfaceMethodref_info methodInfo = (CONSTANT_InterfaceMethodref_info)constant_pool[ii.arg_i];
         bootstrapMethodRef = this.createMethodRef(constant_pool, methodInfo, false);
         returnType = bootstrapMethodRef.returnType();
         params = new Value[args];

         for(j = args - 1; j >= 0; --j) {
            params[j] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            if (this.typeSize(typeStack.top()) == 2) {
               typeStack = typeStack.pop();
               typeStack = typeStack.pop();
            } else {
               typeStack = typeStack.pop();
            }
         }

         Value rvalue = Jimple.v().newInterfaceInvokeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), bootstrapMethodRef, Arrays.asList(params));
         if (!returnType.equals(VoidType.v())) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            stmt = Jimple.v().newInvokeStmt(rvalue);
         }
         break;
      case 186:
         Instruction_Invokedynamic iv = (Instruction_Invokedynamic)ins;
         CONSTANT_InvokeDynamic_info iv_info = (CONSTANT_InvokeDynamic_info)constant_pool[iv.invoke_dynamic_index];
         args = cp_info.countParams(constant_pool, iv_info.name_and_type_index);
         List<Value> bootstrapArgs = new LinkedList();
         short[] bootstrapMethodTable = this.bootstrap_methods_attribute.method_handles;
         short methodSigIndex = bootstrapMethodTable[iv_info.bootstrap_method_index];
         CONSTANT_MethodHandle_info mhInfo = (CONSTANT_MethodHandle_info)constant_pool[methodSigIndex];
         CONSTANT_Methodref_info bsmInfo = (CONSTANT_Methodref_info)constant_pool[mhInfo.target_index];
         bootstrapMethodRef = this.createMethodRef(constant_pool, bsmInfo, false);
         short[] bsmArgIndices = this.bootstrap_methods_attribute.arg_indices[iv_info.bootstrap_method_index];
         int j;
         if (bsmArgIndices.length > 0) {
            short[] var28 = bsmArgIndices;
            int var29 = bsmArgIndices.length;

            for(j = 0; j < var29; ++j) {
               short bsmArgIndex = var28[j];
               cp_info cpEntry = constant_pool[bsmArgIndex];
               Value val = cpEntry.createJimpleConstantValue(constant_pool);
               bootstrapArgs.add(val);
            }
         }

         SootMethodRef methodRef = null;
         CONSTANT_NameAndType_info nameAndTypeInfo = (CONSTANT_NameAndType_info)constant_pool[iv_info.name_and_type_index];
         methodName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nameAndTypeInfo.name_index])).convert();
         String methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[nameAndTypeInfo.descriptor_index])).convert();
         bclass = this.cm.getSootClass("soot.dummy.InvokeDynamic");
         Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
         List<Type> parameterTypes = new ArrayList();

         for(int k = 0; k < types.length - 1; ++k) {
            parameterTypes.add(types[k]);
         }

         Type returnType = types[types.length - 1];
         methodRef = Scene.v().makeMethodRef(bclass, methodName, parameterTypes, returnType, true);
         params = new Value[args];

         for(j = args - 1; j >= 0; --j) {
            params[j] = Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex());
            if (this.typeSize(typeStack.top()) == 2) {
               typeStack = typeStack.pop();
               typeStack = typeStack.pop();
            } else {
               typeStack = typeStack.pop();
            }
         }

         Value rvalue = Jimple.v().newDynamicInvokeExpr(bootstrapMethodRef, bootstrapArgs, methodRef, Arrays.asList(params));
         if (!returnType.equals(VoidType.v())) {
            stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rvalue);
         } else {
            stmt = Jimple.v().newInvokeStmt(rvalue);
         }
         break;
      case 187:
         SootClass bclass = this.cm.getSootClass(this.getClassName(constant_pool, ((Instruction_New)ins).arg_i));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), Jimple.v().newNewExpr(RefType.v(bclass.getName())));
         break;
      case 188:
         Type baseType = this.jimpleTypeOfAtype(((Instruction_Newarray)ins).atype);
         rhs = Jimple.v().newNewArrayExpr(baseType, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 189:
         className = this.getClassName(constant_pool, ((Instruction_Anewarray)ins).arg_i);
         if (className.startsWith("[")) {
            castType = Util.v().jimpleTypeOfFieldDescriptor(this.getClassName(constant_pool, ((Instruction_Anewarray)ins).arg_i));
         } else {
            castType = RefType.v(className);
         }

         rhs = Jimple.v().newNewArrayExpr((Type)castType, Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 190:
         Expr rhs = Jimple.v().newLengthExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 191:
         stmt = Jimple.v().newThrowStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 192:
         className = this.getClassName(constant_pool, ((Instruction_Checkcast)ins).arg_i);
         if (className.startsWith("[")) {
            castType = Util.v().jimpleTypeOfFieldDescriptor(this.getClassName(constant_pool, ((Instruction_Checkcast)ins).arg_i));
         } else {
            castType = RefType.v(className);
         }

         rhs = Jimple.v().newCastExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), (Type)castType);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 193:
         String className = this.getClassName(constant_pool, ((Instruction_Instanceof)ins).arg_i);
         Object checkType;
         if (className.startsWith("[")) {
            checkType = Util.v().jimpleTypeOfFieldDescriptor(this.getClassName(constant_pool, ((Instruction_Instanceof)ins).arg_i));
         } else {
            checkType = RefType.v(className);
         }

         Expr rhs = Jimple.v().newInstanceOfExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), (Type)checkType);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 194:
         stmt = Jimple.v().newEnterMonitorStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 195:
         stmt = Jimple.v().newExitMonitorStmt(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()));
         break;
      case 196:
         throw new RuntimeException("WIDE instruction should not be encountered anymore");
      case 197:
         int bdims = ((Instruction_Multianewarray)ins).dims;
         List<Value> dims = new ArrayList();

         for(j = 0; j < bdims; ++j) {
            dims.add(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex() - bdims + j + 1));
         }

         mstype = constant_pool[((Instruction_Multianewarray)ins).arg_i].toString(constant_pool);
         ArrayType jimpleType = (ArrayType)Util.v().jimpleTypeOfFieldDescriptor(mstype);
         rhs = Jimple.v().newNewMultiArrayExpr(jimpleType, dims);
         stmt = Jimple.v().newAssignStmt(Util.v().getLocalForStackOp(this.listBody, postTypeStack, postTypeStack.topIndex()), rhs);
         break;
      case 198:
         co = Jimple.v().newEqExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), NullConstant.v());
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 199:
         co = Jimple.v().newNeExpr(Util.v().getLocalForStackOp(this.listBody, typeStack, typeStack.topIndex()), NullConstant.v());
         stmt = Jimple.v().newIfStmt(co, (Unit)(new FutureStmt()));
         break;
      case 200:
         stmt = Jimple.v().newGotoStmt((Unit)(new FutureStmt()));
         break;
      case 202:
         stmt = Jimple.v().newBreakpointStmt();
         break;
      case 209:
         local = Util.v().getLocalForIndex(this.listBody, ((Instruction_Ret_w)ins).arg_i, ins);
         stmt = Jimple.v().newRetStmt(local);
      }

      if (stmt != null) {
         if (Options.v().keep_offset()) {
            ((Stmt)stmt).addTag(new BytecodeOffsetTag(ins.label));
         }

         statements.add(stmt);
      }

   }

   private SootMethodRef createMethodRef(cp_info[] constant_pool, ICONSTANT_Methodref_info methodInfo, boolean isStatic) {
      CONSTANT_Class_info c = (CONSTANT_Class_info)constant_pool[methodInfo.getClassIndex()];
      String className = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[c.name_index])).convert();
      className = className.replace('/', '.');
      CONSTANT_NameAndType_info i = (CONSTANT_NameAndType_info)constant_pool[methodInfo.getNameAndTypeIndex()];
      String methodName = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.name_index])).convert();
      String methodDescriptor = ((CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[i.descriptor_index])).convert();
      if (className.charAt(0) == '[') {
         className = "java.lang.Object";
      }

      SootClass bclass = this.cm.getSootClass(className);
      Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(methodDescriptor);
      List<Type> parameterTypes = new ArrayList();

      for(int k = 0; k < types.length - 1; ++k) {
         parameterTypes.add(types[k]);
      }

      Type returnType = types[types.length - 1];
      SootMethodRef methodRef = Scene.v().makeMethodRef(bclass, methodName, parameterTypes, returnType, isStatic);
      return methodRef;
   }

   Type jimpleTypeOfAtype(int atype) {
      switch(atype) {
      case 4:
         return BooleanType.v();
      case 5:
         return CharType.v();
      case 6:
         return FloatType.v();
      case 7:
         return DoubleType.v();
      case 8:
         return ByteType.v();
      case 9:
         return ShortType.v();
      case 10:
         return IntType.v();
      case 11:
         return LongType.v();
      default:
         throw new RuntimeException("Undefined 'atype' in NEWARRAY byte instruction");
      }
   }

   int typeSize(Type type) {
      return !type.equals(LongType.v()) && !type.equals(DoubleType.v()) && !type.equals(Long2ndHalfType.v()) && !type.equals(Double2ndHalfType.v()) ? 1 : 2;
   }
}
