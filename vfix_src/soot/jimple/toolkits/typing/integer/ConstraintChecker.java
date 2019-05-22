package soot.jimple.toolkits.typing.integer;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.NullType;
import soot.ShortType;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.BreakpointStmt;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.toolkits.typing.Util;

class ConstraintChecker extends AbstractStmtSwitch {
   private static final Logger logger = LoggerFactory.getLogger(ConstraintChecker.class);
   private final TypeResolver resolver;
   private final boolean fix;
   private JimpleBody stmtBody;

   public ConstraintChecker(TypeResolver resolver, boolean fix) {
      this.resolver = resolver;
      this.fix = fix;
   }

   public void check(Stmt stmt, JimpleBody stmtBody) throws TypeException {
      try {
         this.stmtBody = stmtBody;
         stmt.apply(this);
      } catch (ConstraintChecker.RuntimeTypeException var6) {
         StringWriter st = new StringWriter();
         PrintWriter pw = new PrintWriter(st);
         logger.error((String)var6.getMessage(), (Throwable)var6);
         pw.close();
         throw new TypeException(st.toString());
      }
   }

   static void error(String message) {
      throw new ConstraintChecker.RuntimeTypeException(message);
   }

   private void handleInvokeExpr(InvokeExpr ie, Stmt invokestmt) {
      SootMethodRef method = ie.getMethodRef();

      for(int i = 0; i < ie.getArgCount(); ++i) {
         if (ie.getArg(i) instanceof Local) {
            Local local = (Local)ie.getArg(i);
            if (local.getType() instanceof IntegerType && !ClassHierarchy.v().typeNode(local.getType()).hasAncestor_1(ClassHierarchy.v().typeNode(method.parameterType(i)))) {
               if (this.fix) {
                  ie.setArg(i, this.insertCast(local, method.parameterType(i), invokestmt));
               } else {
                  error("Type Error");
               }
            }
         }
      }

      if (ie instanceof DynamicInvokeExpr) {
         DynamicInvokeExpr die = (DynamicInvokeExpr)ie;
         SootMethodRef bootstrapMethod = die.getBootstrapMethodRef();

         for(int i = 0; i < die.getBootstrapArgCount(); ++i) {
            if (die.getBootstrapArg(i) instanceof Local) {
               Local local = (Local)die.getBootstrapArg(i);
               if (local.getType() instanceof IntegerType && !ClassHierarchy.v().typeNode(local.getType()).hasAncestor_1(ClassHierarchy.v().typeNode(bootstrapMethod.parameterType(i)))) {
                  if (this.fix) {
                     die.setArg(i, this.insertCast(local, bootstrapMethod.parameterType(i), invokestmt));
                  } else {
                     error("Type Error");
                  }
               }
            }
         }
      }

   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.handleInvokeExpr(stmt.getInvokeExpr(), stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      TypeNode left = null;
      TypeNode right = null;
      ArrayRef ref;
      Type baset;
      ArrayType base;
      Value index;
      InstanceFieldRef ref;
      StaticFieldRef ref;
      if (l instanceof ArrayRef) {
         ref = (ArrayRef)l;
         baset = ((Local)ref.getBase()).getType();
         if (baset instanceof ArrayType) {
            base = (ArrayType)baset;
            index = ref.getIndex();
            if (base.numDimensions == 1 && base.baseType instanceof IntegerType) {
               left = ClassHierarchy.v().typeNode(base.baseType);
            }

            if (index instanceof Local && !ClassHierarchy.v().typeNode(((Local)index).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
               if (this.fix) {
                  ref.setIndex(this.insertCast((Local)index, IntType.v(), stmt));
               } else {
                  error("Type Error(5)");
               }
            }
         }
      } else if (l instanceof Local) {
         if (((Local)l).getType() instanceof IntegerType) {
            left = ClassHierarchy.v().typeNode(((Local)l).getType());
         }
      } else if (l instanceof InstanceFieldRef) {
         ref = (InstanceFieldRef)l;
         if (ref.getFieldRef().type() instanceof IntegerType) {
            left = ClassHierarchy.v().typeNode(ref.getFieldRef().type());
         }
      } else {
         if (!(l instanceof StaticFieldRef)) {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
         }

         ref = (StaticFieldRef)l;
         if (ref.getFieldRef().type() instanceof IntegerType) {
            left = ClassHierarchy.v().typeNode(ref.getFieldRef().type());
         }
      }

      if (r instanceof ArrayRef) {
         ref = (ArrayRef)r;
         baset = ((Local)ref.getBase()).getType();
         if (!(baset instanceof NullType)) {
            base = (ArrayType)baset;
            index = ref.getIndex();
            if (base.numDimensions == 1 && base.baseType instanceof IntegerType) {
               right = ClassHierarchy.v().typeNode(base.baseType);
            }

            if (index instanceof Local && !ClassHierarchy.v().typeNode(((Local)index).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
               if (this.fix) {
                  ref.setIndex(this.insertCast((Local)index, IntType.v(), stmt));
               } else {
                  error("Type Error(6)");
               }
            }
         }
      } else if (!(r instanceof DoubleConstant) && !(r instanceof FloatConstant)) {
         if (r instanceof IntConstant) {
            int value = ((IntConstant)r).value;
            if (value < -32768) {
               right = ClassHierarchy.v().INT;
            } else if (value < -128) {
               right = ClassHierarchy.v().SHORT;
            } else if (value < 0) {
               right = ClassHierarchy.v().BYTE;
            } else if (value < 2) {
               right = ClassHierarchy.v().R0_1;
            } else if (value < 128) {
               right = ClassHierarchy.v().R0_127;
            } else if (value < 32768) {
               right = ClassHierarchy.v().R0_32767;
            } else if (value < 65536) {
               right = ClassHierarchy.v().CHAR;
            } else {
               right = ClassHierarchy.v().INT;
            }
         } else if (!(r instanceof LongConstant) && !(r instanceof NullConstant) && !(r instanceof StringConstant) && !(r instanceof ClassConstant)) {
            Value size;
            Value size;
            if (r instanceof BinopExpr) {
               BinopExpr be = (BinopExpr)r;
               size = be.getOp1();
               size = be.getOp2();
               TypeNode lop = null;
               TypeNode rop = null;
               int value;
               if (size instanceof Local) {
                  if (((Local)size).getType() instanceof IntegerType) {
                     lop = ClassHierarchy.v().typeNode(((Local)size).getType());
                  }
               } else if (!(size instanceof DoubleConstant) && !(size instanceof FloatConstant)) {
                  if (size instanceof IntConstant) {
                     value = ((IntConstant)size).value;
                     if (value < -32768) {
                        lop = ClassHierarchy.v().INT;
                     } else if (value < -128) {
                        lop = ClassHierarchy.v().SHORT;
                     } else if (value < 0) {
                        lop = ClassHierarchy.v().BYTE;
                     } else if (value < 2) {
                        lop = ClassHierarchy.v().R0_1;
                     } else if (value < 128) {
                        lop = ClassHierarchy.v().R0_127;
                     } else if (value < 32768) {
                        lop = ClassHierarchy.v().R0_32767;
                     } else if (value < 65536) {
                        lop = ClassHierarchy.v().CHAR;
                     } else {
                        lop = ClassHierarchy.v().INT;
                     }
                  } else if (!(size instanceof LongConstant) && !(size instanceof NullConstant) && !(size instanceof StringConstant) && !(size instanceof ClassConstant)) {
                     throw new RuntimeException("Unhandled binary expression left operand type: " + size.getClass());
                  }
               }

               if (size instanceof Local) {
                  if (((Local)size).getType() instanceof IntegerType) {
                     rop = ClassHierarchy.v().typeNode(((Local)size).getType());
                  }
               } else if (!(size instanceof DoubleConstant) && !(size instanceof FloatConstant)) {
                  if (size instanceof IntConstant) {
                     value = ((IntConstant)size).value;
                     if (value < -32768) {
                        rop = ClassHierarchy.v().INT;
                     } else if (value < -128) {
                        rop = ClassHierarchy.v().SHORT;
                     } else if (value < 0) {
                        rop = ClassHierarchy.v().BYTE;
                     } else if (value < 2) {
                        rop = ClassHierarchy.v().R0_1;
                     } else if (value < 128) {
                        rop = ClassHierarchy.v().R0_127;
                     } else if (value < 32768) {
                        rop = ClassHierarchy.v().R0_32767;
                     } else if (value < 65536) {
                        rop = ClassHierarchy.v().CHAR;
                     } else {
                        rop = ClassHierarchy.v().INT;
                     }
                  } else if (!(size instanceof LongConstant) && !(size instanceof NullConstant) && !(size instanceof StringConstant) && !(size instanceof ClassConstant)) {
                     throw new RuntimeException("Unhandled binary expression right operand type: " + size.getClass());
                  }
               }

               if (!(be instanceof AddExpr) && !(be instanceof SubExpr) && !(be instanceof MulExpr) && !(be instanceof DivExpr) && !(be instanceof RemExpr)) {
                  TypeNode lca;
                  if (!(be instanceof AndExpr) && !(be instanceof OrExpr) && !(be instanceof XorExpr)) {
                     if (be instanceof ShlExpr) {
                        if (lop != null && !lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                           if (this.fix) {
                              be.setOp1(this.insertCast(be.getOp1(), getTypeForCast(lop), IntType.v(), stmt));
                           } else {
                              error("Type Error(9)");
                           }
                        }

                        if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                           if (this.fix) {
                              be.setOp2(this.insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                           } else {
                              error("Type Error(10)");
                           }
                        }

                        right = lop == null ? null : ClassHierarchy.v().INT;
                     } else if (!(be instanceof ShrExpr) && !(be instanceof UshrExpr)) {
                        if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr)) {
                           if (!(be instanceof EqExpr) && !(be instanceof GeExpr) && !(be instanceof GtExpr) && !(be instanceof LeExpr) && !(be instanceof LtExpr) && !(be instanceof NeExpr)) {
                              throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
                           }

                           if (rop != null) {
                              lca = lop.lca_1(rop);
                              if (lca == ClassHierarchy.v().TOP) {
                                 if (this.fix) {
                                    if (!lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                                       be.setOp1(this.insertCast(be.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
                                    }

                                    if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                                       be.setOp2(this.insertCast(be.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
                                    }
                                 } else {
                                    error("Type Error(11)");
                                 }
                              }
                           }

                           right = ClassHierarchy.v().BOOLEAN;
                        } else {
                           right = ClassHierarchy.v().BYTE;
                        }
                     } else {
                        if (lop != null && !lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                           if (this.fix) {
                              be.setOp1(this.insertCast(be.getOp1(), getTypeForCast(lop), ByteType.v(), stmt));
                              lop = ClassHierarchy.v().BYTE;
                           } else {
                              error("Type Error(9)");
                           }
                        }

                        if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                           if (this.fix) {
                              be.setOp2(this.insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                           } else {
                              error("Type Error(10)");
                           }
                        }

                        right = lop;
                     }
                  } else if (lop != null && rop != null) {
                     lca = lop.lca_1(rop);
                     if (lca == ClassHierarchy.v().TOP) {
                        if (this.fix) {
                           if (!lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                              be.setOp1(this.insertCast(be.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
                              lca = rop;
                           }

                           if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                              be.setOp2(this.insertCast(be.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
                              lca = lop;
                           }
                        } else {
                           error("Type Error(11)");
                        }
                     }

                     right = lca;
                  }
               } else {
                  if (lop != null && rop != null) {
                     if (!lop.hasAncestor_1(ClassHierarchy.v().INT)) {
                        if (this.fix) {
                           be.setOp1(this.insertCast(be.getOp1(), getTypeForCast(lop), IntType.v(), stmt));
                        } else {
                           error("Type Error(7)");
                        }
                     }

                     if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
                        if (this.fix) {
                           be.setOp2(this.insertCast(be.getOp2(), getTypeForCast(rop), IntType.v(), stmt));
                        } else {
                           error("Type Error(8)");
                        }
                     }
                  }

                  right = ClassHierarchy.v().INT;
               }
            } else if (r instanceof CastExpr) {
               CastExpr ce = (CastExpr)r;
               if (ce.getCastType() instanceof IntegerType) {
                  right = ClassHierarchy.v().typeNode(ce.getCastType());
               }
            } else if (r instanceof InstanceOfExpr) {
               right = ClassHierarchy.v().BOOLEAN;
            } else if (r instanceof InvokeExpr) {
               InvokeExpr ie = (InvokeExpr)r;
               this.handleInvokeExpr(ie, stmt);
               if (ie.getMethodRef().returnType() instanceof IntegerType) {
                  right = ClassHierarchy.v().typeNode(ie.getMethodRef().returnType());
               }
            } else if (r instanceof NewArrayExpr) {
               NewArrayExpr nae = (NewArrayExpr)r;
               size = nae.getSize();
               if (size instanceof Local && !ClassHierarchy.v().typeNode(((Local)size).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
                  if (this.fix) {
                     nae.setSize(this.insertCast((Local)size, IntType.v(), stmt));
                  } else {
                     error("Type Error(12)");
                  }
               }
            } else if (!(r instanceof NewExpr)) {
               if (r instanceof NewMultiArrayExpr) {
                  NewMultiArrayExpr nmae = (NewMultiArrayExpr)r;

                  for(int i = 0; i < nmae.getSizeCount(); ++i) {
                     size = nmae.getSize(i);
                     if (size instanceof Local && !ClassHierarchy.v().typeNode(((Local)size).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
                        if (this.fix) {
                           nmae.setSize(i, this.insertCast((Local)size, IntType.v(), stmt));
                        } else {
                           error("Type Error(13)");
                        }
                     }
                  }
               } else if (r instanceof LengthExpr) {
                  right = ClassHierarchy.v().INT;
               } else if (r instanceof NegExpr) {
                  NegExpr ne = (NegExpr)r;
                  if (ne.getOp() instanceof Local) {
                     Local local = (Local)ne.getOp();
                     if (local.getType() instanceof IntegerType) {
                        TypeNode ltype = ClassHierarchy.v().typeNode(local.getType());
                        if (!ltype.hasAncestor_1(ClassHierarchy.v().INT)) {
                           if (this.fix) {
                              ne.setOp(this.insertCast(local, IntType.v(), stmt));
                              ltype = ClassHierarchy.v().BYTE;
                           } else {
                              error("Type Error(14)");
                           }
                        }

                        right = ltype == ClassHierarchy.v().CHAR ? ClassHierarchy.v().INT : ltype;
                     }
                  } else if (!(ne.getOp() instanceof DoubleConstant) && !(ne.getOp() instanceof FloatConstant)) {
                     if (ne.getOp() instanceof IntConstant) {
                        right = ClassHierarchy.v().INT;
                     } else if (!(ne.getOp() instanceof LongConstant)) {
                        throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
                     }
                  }
               } else if (r instanceof Local) {
                  Local local = (Local)r;
                  if (local.getType() instanceof IntegerType) {
                     right = ClassHierarchy.v().typeNode(local.getType());
                  }
               } else if (r instanceof InstanceFieldRef) {
                  ref = (InstanceFieldRef)r;
                  if (ref.getFieldRef().type() instanceof IntegerType) {
                     right = ClassHierarchy.v().typeNode(ref.getFieldRef().type());
                  }
               } else {
                  if (!(r instanceof StaticFieldRef)) {
                     throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
                  }

                  ref = (StaticFieldRef)r;
                  if (ref.getFieldRef().type() instanceof IntegerType) {
                     right = ClassHierarchy.v().typeNode(ref.getFieldRef().type());
                  }
               }
            }
         }
      }

      if (left != null && right != null && !right.hasAncestor_1(left)) {
         if (this.fix) {
            stmt.setRightOp(this.insertCast(stmt.getRightOp(), getTypeForCast(right), getTypeForCast(left), stmt));
         } else {
            error("Type Error(15)");
         }
      }

   }

   static Type getTypeForCast(TypeNode node) {
      if (node.type() == null) {
         if (node == ClassHierarchy.v().R0_1) {
            return BooleanType.v();
         }

         if (node == ClassHierarchy.v().R0_127) {
            return ByteType.v();
         }

         if (node == ClassHierarchy.v().R0_32767) {
            return ShortType.v();
         }
      }

      return node.type();
   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      if (l instanceof Local && ((Local)l).getType() instanceof IntegerType) {
         TypeNode left = ClassHierarchy.v().typeNode(((Local)l).getType());
         TypeNode right = ClassHierarchy.v().typeNode(r.getType());
         if (!right.hasAncestor_1(left)) {
            if (this.fix) {
               ((JIdentityStmt)stmt).setLeftOp(this.insertCastAfter((Local)l, getTypeForCast(left), getTypeForCast(right), stmt));
            } else {
               error("Type Error(16)");
            }
         }
      }

   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
   }

   public void caseGotoStmt(GotoStmt stmt) {
   }

   public void caseIfStmt(IfStmt stmt) {
      ConditionExpr cond = (ConditionExpr)stmt.getCondition();
      Value lv = cond.getOp1();
      Value rv = cond.getOp2();
      TypeNode lop = null;
      TypeNode rop = null;
      int value;
      if (lv instanceof Local) {
         if (((Local)lv).getType() instanceof IntegerType) {
            lop = ClassHierarchy.v().typeNode(((Local)lv).getType());
         }
      } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
         if (lv instanceof IntConstant) {
            value = ((IntConstant)lv).value;
            if (value < -32768) {
               lop = ClassHierarchy.v().INT;
            } else if (value < -128) {
               lop = ClassHierarchy.v().SHORT;
            } else if (value < 0) {
               lop = ClassHierarchy.v().BYTE;
            } else if (value < 2) {
               lop = ClassHierarchy.v().R0_1;
            } else if (value < 128) {
               lop = ClassHierarchy.v().R0_127;
            } else if (value < 32768) {
               lop = ClassHierarchy.v().R0_32767;
            } else if (value < 65536) {
               lop = ClassHierarchy.v().CHAR;
            } else {
               lop = ClassHierarchy.v().INT;
            }
         } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
            throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
         }
      }

      if (rv instanceof Local) {
         if (((Local)rv).getType() instanceof IntegerType) {
            rop = ClassHierarchy.v().typeNode(((Local)rv).getType());
         }
      } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
         if (rv instanceof IntConstant) {
            value = ((IntConstant)rv).value;
            if (value < -32768) {
               rop = ClassHierarchy.v().INT;
            } else if (value < -128) {
               rop = ClassHierarchy.v().SHORT;
            } else if (value < 0) {
               rop = ClassHierarchy.v().BYTE;
            } else if (value < 2) {
               rop = ClassHierarchy.v().R0_1;
            } else if (value < 128) {
               rop = ClassHierarchy.v().R0_127;
            } else if (value < 32768) {
               rop = ClassHierarchy.v().R0_32767;
            } else if (value < 65536) {
               rop = ClassHierarchy.v().CHAR;
            } else {
               rop = ClassHierarchy.v().INT;
            }
         } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
            throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
         }
      }

      if (lop != null && rop != null && lop.lca_1(rop) == ClassHierarchy.v().TOP) {
         if (this.fix) {
            if (!lop.hasAncestor_1(ClassHierarchy.v().INT)) {
               cond.setOp1(this.insertCast(cond.getOp1(), getTypeForCast(lop), getTypeForCast(rop), stmt));
            }

            if (!rop.hasAncestor_1(ClassHierarchy.v().INT)) {
               cond.setOp2(this.insertCast(cond.getOp2(), getTypeForCast(rop), getTypeForCast(lop), stmt));
            }
         } else {
            error("Type Error(17)");
         }
      }

   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      Value key = stmt.getKey();
      if (key instanceof Local && !ClassHierarchy.v().typeNode(((Local)key).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
         if (this.fix) {
            stmt.setKey(this.insertCast((Local)key, IntType.v(), stmt));
         } else {
            error("Type Error(18)");
         }
      }

   }

   public void caseNopStmt(NopStmt stmt) {
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      if (stmt.getOp() instanceof Local && ((Local)stmt.getOp()).getType() instanceof IntegerType && !ClassHierarchy.v().typeNode(((Local)stmt.getOp()).getType()).hasAncestor_1(ClassHierarchy.v().typeNode(this.stmtBody.getMethod().getReturnType()))) {
         if (this.fix) {
            stmt.setOp(this.insertCast((Local)stmt.getOp(), this.stmtBody.getMethod().getReturnType(), stmt));
         } else {
            error("Type Error(19)");
         }
      }

   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      Value key = stmt.getKey();
      if (key instanceof Local) {
         if (!ClassHierarchy.v().typeNode(((Local)key).getType()).hasAncestor_1(ClassHierarchy.v().INT)) {
            if (this.fix) {
               stmt.setKey(this.insertCast((Local)key, IntType.v(), stmt));
            } else {
               error("Type Error(20)");
            }
         }

         this.resolver.typeVariable((Local)key).addParent(this.resolver.INT);
      }

   }

   public void caseThrowStmt(ThrowStmt stmt) {
   }

   public void defaultCase(Stmt stmt) {
      throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
   }

   private Local insertCast(Local oldlocal, Type type, Stmt stmt) {
      Local newlocal = Jimple.v().newLocal("tmp", type);
      this.stmtBody.getLocals().add(newlocal);
      Unit u = Util.findFirstNonIdentityUnit(this.stmtBody, stmt);
      this.stmtBody.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(newlocal, Jimple.v().newCastExpr(oldlocal, type)), (Unit)u);
      return newlocal;
   }

   private Local insertCastAfter(Local leftlocal, Type lefttype, Type righttype, Stmt stmt) {
      Local newlocal = Jimple.v().newLocal("tmp", righttype);
      this.stmtBody.getLocals().add(newlocal);
      Unit u = Util.findLastIdentityUnit(this.stmtBody, stmt);
      this.stmtBody.getUnits().insertAfter((Unit)Jimple.v().newAssignStmt(leftlocal, Jimple.v().newCastExpr(newlocal, lefttype)), (Unit)u);
      return newlocal;
   }

   private Local insertCast(Value oldvalue, Type oldtype, Type type, Stmt stmt) {
      Local newlocal1 = Jimple.v().newLocal("tmp", oldtype);
      Local newlocal2 = Jimple.v().newLocal("tmp", type);
      this.stmtBody.getLocals().add(newlocal1);
      this.stmtBody.getLocals().add(newlocal2);
      Unit u = Util.findFirstNonIdentityUnit(this.stmtBody, stmt);
      this.stmtBody.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(newlocal1, oldvalue), (Unit)u);
      this.stmtBody.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(newlocal2, Jimple.v().newCastExpr(newlocal1, type)), (Unit)u);
      return newlocal2;
   }

   private static class RuntimeTypeException extends RuntimeException {
      RuntimeTypeException(String message) {
         super(message);
      }
   }
}
