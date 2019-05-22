package corg.vfix.parser.jimple;

import java.util.Iterator;
import java.util.List;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.SubExpr;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;

public class ExprParser {
   public static void main(Expr expr) {
      if (expr instanceof BinopExpr) {
         parseBinopExpr((BinopExpr)expr);
      } else if (expr instanceof CastExpr) {
         parseCastExpr((CastExpr)expr);
      } else if (expr instanceof InstanceOfExpr) {
         parseInstanceOfExpr((InstanceOfExpr)expr);
      } else if (expr instanceof InvokeExpr) {
         parseInvokeExpr((InvokeExpr)expr);
      } else if (expr instanceof NewArrayExpr) {
         parseNewArrayExpr((NewArrayExpr)expr);
      } else if (expr instanceof NewExpr) {
         parseNewExpr((NewExpr)expr);
      } else if (expr instanceof NewMultiArrayExpr) {
         parseNewMultiArrayExpr((NewMultiArrayExpr)expr);
      } else if (expr instanceof UnopExpr) {
         parseUnopExpr((UnopExpr)expr);
      }

   }

   private static void parseUnopExpr(UnopExpr expr) {
      if (expr instanceof LengthExpr) {
         parseLengthExpr((LengthExpr)expr);
      } else if (expr instanceof NegExpr) {
         parseNegExpr((NegExpr)expr);
      }

   }

   private static void parseNegExpr(NegExpr expr) {
      System.out.println("NegExpr:");
      System.out.println(expr);
   }

   private static void parseLengthExpr(LengthExpr expr) {
      System.out.println("LengthExpr:");
      System.out.println(expr);
   }

   private static void parseNewMultiArrayExpr(NewMultiArrayExpr expr) {
      System.out.println("NewMultiArrayExpr:");
      System.out.println(expr);
   }

   private static void parseNewExpr(NewExpr expr) {
      System.out.println("NewExpr:");
      System.out.println(expr);
   }

   private static void parseNewArrayExpr(NewArrayExpr expr) {
      System.out.println("NewArrayExpr:");
      System.out.println(expr);
   }

   private static void parseInvokeExpr(InvokeExpr expr) {
      if (expr instanceof DynamicInvokeExpr) {
         parseDynamicInvokeExpr((DynamicInvokeExpr)expr);
      } else if (expr instanceof InstanceInvokeExpr) {
         parseInstanceInvokeExpr((InstanceInvokeExpr)expr);
      } else if (expr instanceof StaticInvokeExpr) {
         parseStaticInvokeExpr((StaticInvokeExpr)expr);
      }

   }

   private static void parseStaticInvokeExpr(StaticInvokeExpr expr) {
   }

   private static void parseInstanceInvokeExpr(InstanceInvokeExpr expr) {
      if (expr instanceof InterfaceInvokeExpr) {
         parseInterfaceInvokeExpr((InterfaceInvokeExpr)expr);
      } else if (expr instanceof SpecialInvokeExpr) {
         parseSpecialInvokeExpr((SpecialInvokeExpr)expr);
      } else if (expr instanceof VirtualInvokeExpr) {
         parseVirtualInvokeExpr((VirtualInvokeExpr)expr);
      }

   }

   private static void parseVirtualInvokeExpr(VirtualInvokeExpr expr) {
      System.out.println("VirtualInvokeExpr:");
      System.out.println(expr);
      System.out.println("Base:");
      ValueParser.main(expr.getBase());
      System.out.println("Args:");
      List<Value> args = expr.getArgs();
      Iterator var3 = args.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         ValueParser.main(value);
      }

   }

   private static void parseSpecialInvokeExpr(SpecialInvokeExpr expr) {
      System.out.println("SpecialInvokeExpr:");
      System.out.println(expr);
      System.out.println("Base:");
      ValueParser.main(expr.getBase());
      System.out.println("Args:");
      List<Value> args = expr.getArgs();
      Iterator var3 = args.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         ValueParser.main(value);
      }

   }

   private static void parseInterfaceInvokeExpr(InterfaceInvokeExpr expr) {
      System.out.println("InterfaceInvokeExpr:");
      System.out.println(expr);
      System.out.println("Base:");
      ValueParser.main(expr.getBase());
      System.out.println("Args:");
      List<Value> args = expr.getArgs();
      Iterator var3 = args.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         ValueParser.main(value);
      }

   }

   private static void parseDynamicInvokeExpr(DynamicInvokeExpr expr) {
      System.out.println("DynamicInvokeExpr:");
      System.out.println(expr);
   }

   private static void parseInstanceOfExpr(InstanceOfExpr expr) {
      System.out.println("InstanceExpr:");
      System.out.println(expr);
   }

   private static void parseCastExpr(CastExpr expr) {
      System.out.println("CastExpr:");
      System.out.println(expr);
      System.out.println("Cast Type:");
      System.out.println(expr.getCastType());
      System.out.println("Op:");
      ValueParser.main(expr.getOp());
   }

   private static void parseBinopExpr(BinopExpr expr) {
      if (expr instanceof AddExpr) {
         parseAddExpr((AddExpr)expr);
      } else if (expr instanceof AndExpr) {
         parseAndExpr((AndExpr)expr);
      } else if (expr instanceof CmpExpr) {
         parseCmpExpr((CmpExpr)expr);
      } else if (expr instanceof CmpgExpr) {
         parseCmpgExpr((CmpgExpr)expr);
      } else if (expr instanceof CmplExpr) {
         parseCmplExpr((CmplExpr)expr);
      } else if (expr instanceof ConditionExpr) {
         parseConditionExpr((ConditionExpr)expr);
      } else if (expr instanceof DivExpr) {
         parseDivExpr((DivExpr)expr);
      } else if (expr instanceof MulExpr) {
         parseMulExpr((MulExpr)expr);
      } else if (expr instanceof OrExpr) {
         parseOrExpr((OrExpr)expr);
      } else if (expr instanceof RemExpr) {
         parseRemExpr((RemExpr)expr);
      } else if (expr instanceof ShlExpr) {
         parseShlExpr((ShlExpr)expr);
      } else if (expr instanceof ShrExpr) {
         parseShrExpr((ShrExpr)expr);
      } else if (expr instanceof SubExpr) {
         parseSubExpr((SubExpr)expr);
      } else if (expr instanceof UshrExpr) {
         parseUshrExpr((UshrExpr)expr);
      } else if (expr instanceof XorExpr) {
         parseXorExpr((XorExpr)expr);
      }

   }

   private static void parseXorExpr(XorExpr expr) {
      System.out.println("XorExpr:");
      System.out.println(expr);
   }

   private static void parseUshrExpr(UshrExpr expr) {
      System.out.println("UshrExpr:");
      System.out.println(expr);
   }

   private static void parseSubExpr(SubExpr expr) {
      System.out.println("SubExpr:");
      System.out.println(expr);
   }

   private static void parseShrExpr(ShrExpr expr) {
      System.out.println("ShrExpr:");
      System.out.println(expr);
   }

   private static void parseShlExpr(ShlExpr expr) {
      System.out.println("ShlExpr:");
      System.out.println(expr);
   }

   private static void parseRemExpr(RemExpr expr) {
      System.out.println("RemExpr:");
      System.out.println(expr);
   }

   private static void parseOrExpr(OrExpr expr) {
      System.out.println("OrExpr:");
      System.out.println(expr);
   }

   private static void parseMulExpr(MulExpr expr) {
      System.out.println("MulExpr:");
      System.out.println(expr);
   }

   private static void parseDivExpr(DivExpr expr) {
      System.out.println("DivExpr:");
      System.out.println(expr);
   }

   private static void parseConditionExpr(ConditionExpr expr) {
      System.out.println("ConditionExpr:");
      System.out.println(expr);
      if (expr instanceof EqExpr) {
         parseEqExpr((EqExpr)expr);
      } else if (expr instanceof GeExpr) {
         parseGeExpr((GeExpr)expr);
      } else if (expr instanceof GtExpr) {
         parseGtExpr((GtExpr)expr);
      } else if (expr instanceof LeExpr) {
         parseLeExpr((LeExpr)expr);
      } else if (expr instanceof LtExpr) {
         parseLtExpr((LtExpr)expr);
      } else if (expr instanceof NeExpr) {
         parseNeExpr((NeExpr)expr);
      }

   }

   private static void parseNeExpr(NeExpr expr) {
      System.out.println("NeExpr:");
      System.out.println(expr);
   }

   private static void parseLtExpr(LtExpr expr) {
      System.out.println("LtExpr:");
      System.out.println(expr);
   }

   private static void parseLeExpr(LeExpr expr) {
      System.out.println("LeExpr:");
      System.out.println(expr);
   }

   private static void parseGtExpr(GtExpr expr) {
      System.out.println("GtExpr:");
      System.out.println(expr);
   }

   private static void parseGeExpr(GeExpr expr) {
      System.out.println("GeExpr:");
      System.out.println(expr);
   }

   private static void parseEqExpr(EqExpr expr) {
      System.out.println("EqExpr:");
      System.out.println(expr);
   }

   private static void parseCmplExpr(CmplExpr expr) {
      System.out.println("CmplExpr:");
      System.out.println(expr);
   }

   private static void parseCmpgExpr(CmpgExpr expr) {
      System.out.println("CmpgExpr:");
      System.out.println(expr);
   }

   private static void parseCmpExpr(CmpExpr expr) {
      System.out.println("CmpExpr:");
      System.out.println(expr);
   }

   private static void parseAndExpr(AndExpr expr) {
      System.out.println("AndExpr:");
      System.out.println(expr);
   }

   private static void parseAddExpr(AddExpr expr) {
      System.out.println("AddExpr:");
      System.out.println(expr);
   }

   public static void main(String[] args) {
      printIf("Eq");
      printElseIf("Ge");
      printElseIf("Gt");
      printElseIf("Le");
      printElseIf("Lt");
      printElseIf("Ne");
   }

   private static void printIf(String str) {
      System.out.println("if(expr instanceof " + str + "Expr)");
      System.out.println("\tparse" + str + "Expr((" + str + "Expr)expr);");
   }

   private static void printElseIf(String str) {
      System.out.println("else if(expr instanceof " + str + "Expr)");
      System.out.println("\tparse" + str + "Expr((" + str + "Expr)expr);");
   }
}
