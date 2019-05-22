package soot.jimple.toolkits.typing.integer;

import soot.ArrayType;
import soot.IntegerType;
import soot.Local;
import soot.NullType;
import soot.SootMethodRef;
import soot.Type;
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

class ConstraintCollector extends AbstractStmtSwitch {
   private TypeResolver resolver;
   private boolean uses;
   private JimpleBody stmtBody;

   public ConstraintCollector(TypeResolver resolver, boolean uses) {
      this.resolver = resolver;
      this.uses = uses;
   }

   public void collect(Stmt stmt, JimpleBody stmtBody) {
      this.stmtBody = stmtBody;
      stmt.apply(this);
   }

   private void handleInvokeExpr(InvokeExpr ie) {
      if (this.uses) {
         SootMethodRef method = ie.getMethodRef();

         for(int i = 0; i < ie.getArgCount(); ++i) {
            if (ie.getArg(i) instanceof Local) {
               Local local = (Local)ie.getArg(i);
               if (local.getType() instanceof IntegerType) {
                  TypeVariable localType = this.resolver.typeVariable(local);
                  localType.addParent(this.resolver.typeVariable(method.parameterType(i)));
               }
            }
         }

         if (ie instanceof DynamicInvokeExpr) {
            DynamicInvokeExpr die = (DynamicInvokeExpr)ie;
            SootMethodRef bootstrapMethod = die.getBootstrapMethodRef();

            for(int i = 0; i < die.getBootstrapArgCount(); ++i) {
               if (die.getBootstrapArg(i) instanceof Local) {
                  Local local = (Local)die.getBootstrapArg(i);
                  if (local.getType() instanceof IntegerType) {
                     TypeVariable localType = this.resolver.typeVariable(local);
                     localType.addParent(this.resolver.typeVariable(bootstrapMethod.parameterType(i)));
                  }
               }
            }
         }

      }
   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.handleInvokeExpr(stmt.getInvokeExpr());
   }

   public void caseAssignStmt(AssignStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      TypeVariable left = null;
      TypeVariable right = null;
      ArrayRef ref;
      Type baset;
      InstanceFieldRef ref;
      StaticFieldRef ref;
      if (l instanceof ArrayRef) {
         ref = (ArrayRef)l;
         baset = ((Local)ref.getBase()).getType();
         if (baset instanceof ArrayType) {
            ArrayType base = (ArrayType)baset;
            Value index = ref.getIndex();
            if (this.uses) {
               if (base.numDimensions == 1 && base.baseType instanceof IntegerType) {
                  left = this.resolver.typeVariable(base.baseType);
               }

               if (index instanceof Local) {
                  this.resolver.typeVariable((Local)index).addParent(this.resolver.INT);
               }
            }
         }
      } else if (l instanceof Local) {
         if (((Local)l).getType() instanceof IntegerType) {
            left = this.resolver.typeVariable((Local)l);
         }
      } else if (l instanceof InstanceFieldRef) {
         if (this.uses) {
            ref = (InstanceFieldRef)l;
            baset = ref.getFieldRef().type();
            if (baset instanceof IntegerType) {
               left = this.resolver.typeVariable(ref.getFieldRef().type());
            }
         }
      } else {
         if (!(l instanceof StaticFieldRef)) {
            throw new RuntimeException("Unhandled assignment left hand side type: " + l.getClass());
         }

         if (this.uses) {
            ref = (StaticFieldRef)l;
            baset = ref.getFieldRef().type();
            if (baset instanceof IntegerType) {
               left = this.resolver.typeVariable(ref.getFieldRef().type());
            }
         }
      }

      Value size;
      if (r instanceof ArrayRef) {
         ref = (ArrayRef)r;
         baset = ((Local)ref.getBase()).getType();
         if (!(baset instanceof NullType)) {
            size = ref.getIndex();
            if (baset instanceof ArrayType) {
               ArrayType base = (ArrayType)baset;
               if (base.numDimensions == 1 && base.baseType instanceof IntegerType) {
                  right = this.resolver.typeVariable(base.baseType);
               }
            } else if (baset instanceof IntegerType) {
               right = this.resolver.typeVariable(baset);
            }

            if (this.uses && size instanceof Local) {
               this.resolver.typeVariable((Local)size).addParent(this.resolver.INT);
            }
         }
      } else if (!(r instanceof DoubleConstant) && !(r instanceof FloatConstant)) {
         if (r instanceof IntConstant) {
            int value = ((IntConstant)r).value;
            if (value < -32768) {
               right = this.resolver.INT;
            } else if (value < -128) {
               right = this.resolver.SHORT;
            } else if (value < 0) {
               right = this.resolver.BYTE;
            } else if (value < 2) {
               right = this.resolver.R0_1;
            } else if (value < 128) {
               right = this.resolver.R0_127;
            } else if (value < 32768) {
               right = this.resolver.R0_32767;
            } else if (value < 65536) {
               right = this.resolver.CHAR;
            } else {
               right = this.resolver.INT;
            }
         } else if (!(r instanceof LongConstant) && !(r instanceof NullConstant) && !(r instanceof StringConstant) && !(r instanceof ClassConstant)) {
            Value size;
            TypeVariable lop;
            if (r instanceof BinopExpr) {
               BinopExpr be = (BinopExpr)r;
               size = be.getOp1();
               size = be.getOp2();
               lop = null;
               TypeVariable rop = null;
               int value;
               if (size instanceof Local) {
                  if (((Local)size).getType() instanceof IntegerType) {
                     lop = this.resolver.typeVariable((Local)size);
                  }
               } else if (!(size instanceof DoubleConstant) && !(size instanceof FloatConstant)) {
                  if (size instanceof IntConstant) {
                     value = ((IntConstant)size).value;
                     if (value < -32768) {
                        lop = this.resolver.INT;
                     } else if (value < -128) {
                        lop = this.resolver.SHORT;
                     } else if (value < 0) {
                        lop = this.resolver.BYTE;
                     } else if (value < 2) {
                        lop = this.resolver.R0_1;
                     } else if (value < 128) {
                        lop = this.resolver.R0_127;
                     } else if (value < 32768) {
                        lop = this.resolver.R0_32767;
                     } else if (value < 65536) {
                        lop = this.resolver.CHAR;
                     } else {
                        lop = this.resolver.INT;
                     }
                  } else if (!(size instanceof LongConstant) && !(size instanceof NullConstant) && !(size instanceof StringConstant) && !(size instanceof ClassConstant)) {
                     throw new RuntimeException("Unhandled binary expression left operand type: " + size.getClass());
                  }
               }

               if (size instanceof Local) {
                  if (((Local)size).getType() instanceof IntegerType) {
                     rop = this.resolver.typeVariable((Local)size);
                  }
               } else if (!(size instanceof DoubleConstant) && !(size instanceof FloatConstant)) {
                  if (size instanceof IntConstant) {
                     value = ((IntConstant)size).value;
                     if (value < -32768) {
                        rop = this.resolver.INT;
                     } else if (value < -128) {
                        rop = this.resolver.SHORT;
                     } else if (value < 0) {
                        rop = this.resolver.BYTE;
                     } else if (value < 2) {
                        rop = this.resolver.R0_1;
                     } else if (value < 128) {
                        rop = this.resolver.R0_127;
                     } else if (value < 32768) {
                        rop = this.resolver.R0_32767;
                     } else if (value < 65536) {
                        rop = this.resolver.CHAR;
                     } else {
                        rop = this.resolver.INT;
                     }
                  } else if (!(size instanceof LongConstant) && !(size instanceof NullConstant) && !(size instanceof StringConstant) && !(size instanceof ClassConstant)) {
                     throw new RuntimeException("Unhandled binary expression right operand type: " + size.getClass());
                  }
               }

               if (!(be instanceof AddExpr) && !(be instanceof SubExpr) && !(be instanceof DivExpr) && !(be instanceof RemExpr) && !(be instanceof MulExpr)) {
                  TypeVariable common;
                  if (!(be instanceof AndExpr) && !(be instanceof OrExpr) && !(be instanceof XorExpr)) {
                     if (be instanceof ShlExpr) {
                        if (this.uses) {
                           if (lop != null && lop.type() == null) {
                              lop.addParent(this.resolver.INT);
                           }

                           if (rop.type() == null) {
                              rop.addParent(this.resolver.INT);
                           }
                        }

                        right = lop == null ? null : this.resolver.INT;
                     } else if (!(be instanceof ShrExpr) && !(be instanceof UshrExpr)) {
                        if (!(be instanceof CmpExpr) && !(be instanceof CmpgExpr) && !(be instanceof CmplExpr)) {
                           if (!(be instanceof EqExpr) && !(be instanceof GeExpr) && !(be instanceof GtExpr) && !(be instanceof LeExpr) && !(be instanceof LtExpr) && !(be instanceof NeExpr)) {
                              throw new RuntimeException("Unhandled binary expression type: " + be.getClass());
                           }

                           if (this.uses) {
                              common = this.resolver.typeVariable();
                              if (rop != null) {
                                 rop.addParent(common);
                              }

                              if (lop != null) {
                                 lop.addParent(common);
                              }
                           }

                           right = this.resolver.BOOLEAN;
                        } else {
                           right = this.resolver.BYTE;
                        }
                     } else {
                        if (this.uses) {
                           if (lop != null && lop.type() == null) {
                              lop.addParent(this.resolver.INT);
                           }

                           if (rop.type() == null) {
                              rop.addParent(this.resolver.INT);
                           }
                        }

                        right = lop;
                     }
                  } else if (lop != null && rop != null) {
                     common = this.resolver.typeVariable();
                     if (rop != null) {
                        rop.addParent(common);
                     }

                     if (lop != null) {
                        lop.addParent(common);
                     }

                     right = common;
                  }
               } else if (lop != null && rop != null) {
                  if (this.uses) {
                     if (lop.type() == null) {
                        lop.addParent(this.resolver.INT);
                     }

                     if (rop.type() == null) {
                        rop.addParent(this.resolver.INT);
                     }
                  }

                  right = this.resolver.INT;
               }
            } else if (r instanceof CastExpr) {
               CastExpr ce = (CastExpr)r;
               if (ce.getCastType() instanceof IntegerType) {
                  right = this.resolver.typeVariable(ce.getCastType());
               }
            } else if (r instanceof InstanceOfExpr) {
               right = this.resolver.BOOLEAN;
            } else if (r instanceof InvokeExpr) {
               InvokeExpr ie = (InvokeExpr)r;
               this.handleInvokeExpr(ie);
               if (ie.getMethodRef().returnType() instanceof IntegerType) {
                  right = this.resolver.typeVariable(ie.getMethodRef().returnType());
               }
            } else {
               TypeVariable v;
               if (r instanceof NewArrayExpr) {
                  NewArrayExpr nae = (NewArrayExpr)r;
                  if (this.uses) {
                     size = nae.getSize();
                     if (size instanceof Local) {
                        v = this.resolver.typeVariable((Local)size);
                        v.addParent(this.resolver.INT);
                     }
                  }
               } else if (!(r instanceof NewExpr)) {
                  int value;
                  if (r instanceof NewMultiArrayExpr) {
                     NewMultiArrayExpr nmae = (NewMultiArrayExpr)r;
                     if (this.uses) {
                        for(value = 0; value < nmae.getSizeCount(); ++value) {
                           size = nmae.getSize(value);
                           if (size instanceof Local) {
                              lop = this.resolver.typeVariable((Local)size);
                              lop.addParent(this.resolver.INT);
                           }
                        }
                     }
                  } else if (r instanceof LengthExpr) {
                     right = this.resolver.INT;
                  } else if (r instanceof NegExpr) {
                     NegExpr ne = (NegExpr)r;
                     if (ne.getOp() instanceof Local) {
                        Local local = (Local)ne.getOp();
                        if (local.getType() instanceof IntegerType) {
                           if (this.uses) {
                              this.resolver.typeVariable(local).addParent(this.resolver.INT);
                           }

                           v = this.resolver.typeVariable();
                           v.addChild(this.resolver.BYTE);
                           v.addChild(this.resolver.typeVariable(local));
                           right = v;
                        }
                     } else if (!(ne.getOp() instanceof DoubleConstant) && !(ne.getOp() instanceof FloatConstant)) {
                        if (ne.getOp() instanceof IntConstant) {
                           value = ((IntConstant)ne.getOp()).value;
                           if (value < -32768) {
                              right = this.resolver.INT;
                           } else if (value < -128) {
                              right = this.resolver.SHORT;
                           } else if (value < 0) {
                              right = this.resolver.BYTE;
                           } else if (value < 2) {
                              right = this.resolver.BYTE;
                           } else if (value < 128) {
                              right = this.resolver.BYTE;
                           } else if (value < 32768) {
                              right = this.resolver.SHORT;
                           } else if (value < 65536) {
                              right = this.resolver.INT;
                           } else {
                              right = this.resolver.INT;
                           }
                        } else if (!(ne.getOp() instanceof LongConstant)) {
                           throw new RuntimeException("Unhandled neg expression operand type: " + ne.getOp().getClass());
                        }
                     }
                  } else if (r instanceof Local) {
                     Local local = (Local)r;
                     if (local.getType() instanceof IntegerType) {
                        right = this.resolver.typeVariable(local);
                     }
                  } else if (r instanceof InstanceFieldRef) {
                     ref = (InstanceFieldRef)r;
                     if (ref.getFieldRef().type() instanceof IntegerType) {
                        right = this.resolver.typeVariable(ref.getFieldRef().type());
                     }
                  } else {
                     if (!(r instanceof StaticFieldRef)) {
                        throw new RuntimeException("Unhandled assignment right hand side type: " + r.getClass());
                     }

                     ref = (StaticFieldRef)r;
                     if (ref.getFieldRef().type() instanceof IntegerType) {
                        right = this.resolver.typeVariable(ref.getFieldRef().type());
                     }
                  }
               }
            }
         }
      }

      if (left != null && right != null && (left.type() == null || right.type() == null)) {
         right.addParent(left);
      }

   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      Value l = stmt.getLeftOp();
      Value r = stmt.getRightOp();
      if (l instanceof Local && ((Local)l).getType() instanceof IntegerType) {
         TypeVariable left = this.resolver.typeVariable((Local)l);
         TypeVariable right = this.resolver.typeVariable(r.getType());
         right.addParent(left);
      }

   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
   }

   public void caseGotoStmt(GotoStmt stmt) {
   }

   public void caseIfStmt(IfStmt stmt) {
      if (this.uses) {
         ConditionExpr cond = (ConditionExpr)stmt.getCondition();
         Value lv = cond.getOp1();
         Value rv = cond.getOp2();
         TypeVariable lop = null;
         TypeVariable rop = null;
         int value;
         if (lv instanceof Local) {
            if (((Local)lv).getType() instanceof IntegerType) {
               lop = this.resolver.typeVariable((Local)lv);
            }
         } else if (!(lv instanceof DoubleConstant) && !(lv instanceof FloatConstant)) {
            if (lv instanceof IntConstant) {
               value = ((IntConstant)lv).value;
               if (value < -32768) {
                  lop = this.resolver.INT;
               } else if (value < -128) {
                  lop = this.resolver.SHORT;
               } else if (value < 0) {
                  lop = this.resolver.BYTE;
               } else if (value < 2) {
                  lop = this.resolver.R0_1;
               } else if (value < 128) {
                  lop = this.resolver.R0_127;
               } else if (value < 32768) {
                  lop = this.resolver.R0_32767;
               } else if (value < 65536) {
                  lop = this.resolver.CHAR;
               } else {
                  lop = this.resolver.INT;
               }
            } else if (!(lv instanceof LongConstant) && !(lv instanceof NullConstant) && !(lv instanceof StringConstant) && !(lv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression left operand type: " + lv.getClass());
            }
         }

         if (rv instanceof Local) {
            if (((Local)rv).getType() instanceof IntegerType) {
               rop = this.resolver.typeVariable((Local)rv);
            }
         } else if (!(rv instanceof DoubleConstant) && !(rv instanceof FloatConstant)) {
            if (rv instanceof IntConstant) {
               value = ((IntConstant)rv).value;
               if (value < -32768) {
                  rop = this.resolver.INT;
               } else if (value < -128) {
                  rop = this.resolver.SHORT;
               } else if (value < 0) {
                  rop = this.resolver.BYTE;
               } else if (value < 2) {
                  rop = this.resolver.R0_1;
               } else if (value < 128) {
                  rop = this.resolver.R0_127;
               } else if (value < 32768) {
                  rop = this.resolver.R0_32767;
               } else if (value < 65536) {
                  rop = this.resolver.CHAR;
               } else {
                  rop = this.resolver.INT;
               }
            } else if (!(rv instanceof LongConstant) && !(rv instanceof NullConstant) && !(rv instanceof StringConstant) && !(rv instanceof ClassConstant)) {
               throw new RuntimeException("Unhandled binary expression right operand type: " + rv.getClass());
            }
         }

         if (rop != null && lop != null) {
            TypeVariable common = this.resolver.typeVariable();
            if (rop != null) {
               rop.addParent(common);
            }

            if (lop != null) {
               lop.addParent(common);
            }
         }
      }

   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      if (this.uses) {
         Value key = stmt.getKey();
         if (key instanceof Local) {
            this.resolver.typeVariable((Local)key).addParent(this.resolver.INT);
         }
      }

   }

   public void caseNopStmt(NopStmt stmt) {
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      if (this.uses && stmt.getOp() instanceof Local && ((Local)stmt.getOp()).getType() instanceof IntegerType) {
         this.resolver.typeVariable((Local)stmt.getOp()).addParent(this.resolver.typeVariable(this.stmtBody.getMethod().getReturnType()));
      }

   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      if (this.uses) {
         Value key = stmt.getKey();
         if (key instanceof Local) {
            this.resolver.typeVariable((Local)key).addParent(this.resolver.INT);
         }
      }

   }

   public void caseThrowStmt(ThrowStmt stmt) {
   }

   public void defaultCase(Stmt stmt) {
      throw new RuntimeException("Unhandled statement type: " + stmt.getClass());
   }
}
