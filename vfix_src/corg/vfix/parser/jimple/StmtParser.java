package corg.vfix.parser.jimple;

import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Expr;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.SwitchStmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;

public class StmtParser {
   public static void main(Stmt stmt) {
      System.out.println();
      if (stmt instanceof BreakpointStmt) {
         parseBreakpointStmt((BreakpointStmt)stmt);
      } else if (stmt instanceof DefinitionStmt) {
         parseDefinitionStmt((DefinitionStmt)stmt);
      } else if (stmt instanceof GotoStmt) {
         parseGotoStmt((GotoStmt)stmt);
      } else if (stmt instanceof IfStmt) {
         parseIfStmt((IfStmt)stmt);
      } else if (stmt instanceof InvokeStmt) {
         parseInvokeStmt((InvokeStmt)stmt);
      } else if (stmt instanceof MonitorStmt) {
         parseMonitorStmt((MonitorStmt)stmt);
      } else if (stmt instanceof NopStmt) {
         parseNopStmt((NopStmt)stmt);
      } else if (stmt instanceof RetStmt) {
         parseRetStmt((RetStmt)stmt);
      } else if (stmt instanceof ReturnStmt) {
         parseReturnStmt((ReturnStmt)stmt);
      } else if (stmt instanceof ReturnVoidStmt) {
         parseReturnVoidStmt((ReturnVoidStmt)stmt);
      } else if (stmt instanceof SwitchStmt) {
         parseSwitchStmt((SwitchStmt)stmt);
      } else if (stmt instanceof ThrowStmt) {
         parseThrowStmt((ThrowStmt)stmt);
      }

   }

   private static void parseThrowStmt(ThrowStmt stmt) {
      System.out.println("ThrowStmt:");
      System.out.println(stmt);
   }

   private static void parseSwitchStmt(SwitchStmt stmt) {
      if (stmt instanceof LookupSwitchStmt) {
         parseLookupSwitchStmt((LookupSwitchStmt)stmt);
      } else if (stmt instanceof TableSwitchStmt) {
         parseTableSwitchStmt((TableSwitchStmt)stmt);
      }

   }

   private static void parseTableSwitchStmt(TableSwitchStmt stmt) {
      System.out.println("TableSwitchStmt:");
      System.out.println(stmt);
   }

   private static void parseLookupSwitchStmt(LookupSwitchStmt stmt) {
      System.out.println("LookupSwitchStmt:");
      System.out.println(stmt);
   }

   private static void parseReturnVoidStmt(ReturnVoidStmt stmt) {
      System.out.println("ReturnVoidStmt:");
      System.out.println(stmt);
   }

   private static void parseReturnStmt(ReturnStmt stmt) {
      System.out.println("ReturnStmt:");
      System.out.println(stmt);
      System.out.println("Op:");
      ValueParser.main(stmt.getOp());
   }

   private static void parseRetStmt(RetStmt stmt) {
      System.out.println("RetStmt:");
      System.out.println(stmt);
   }

   private static void parseNopStmt(NopStmt stmt) {
      System.out.println("NopStmt:");
      System.out.println(stmt);
   }

   private static void parseMonitorStmt(MonitorStmt stmt) {
      if (stmt instanceof EnterMonitorStmt) {
         parseEnterMonitorStmt((EnterMonitorStmt)stmt);
      } else if (stmt instanceof ExitMonitorStmt) {
         parseExitMonitorStmt((ExitMonitorStmt)stmt);
      }

   }

   private static void parseEnterMonitorStmt(EnterMonitorStmt stmt) {
      System.out.println("EnterMonitorStmt:");
      System.out.println(stmt);
   }

   private static void parseExitMonitorStmt(ExitMonitorStmt stmt) {
      System.out.println("ExitMonitorStmt:");
      System.out.println(stmt);
   }

   private static void parseInvokeStmt(InvokeStmt stmt) {
      System.out.println("InvokeStmt:");
      System.out.println(stmt);
      System.out.println("InvokeExpr:");
      ExprParser.main((Expr)stmt.getInvokeExpr());
   }

   private static void parseIfStmt(IfStmt stmt) {
      System.out.println("IfStmt:");
      System.out.println(stmt);
      System.out.println("Condition:");
      ValueParser.main(stmt.getCondition());
   }

   private static void parseGotoStmt(GotoStmt stmt) {
      System.out.println("GotoStmt:");
      System.out.println(stmt);
   }

   private static void parseDefinitionStmt(DefinitionStmt stmt) {
      if (stmt instanceof AssignStmt) {
         parseAssignStmt((AssignStmt)stmt);
      } else if (stmt instanceof IdentityStmt) {
         parseIdentityStmt((IdentityStmt)stmt);
      }

   }

   private static void parseIdentityStmt(IdentityStmt stmt) {
      System.out.println("IdentityStmt:");
      System.out.println(stmt);
   }

   private static void parseAssignStmt(AssignStmt stmt) {
      System.out.println("AssignStmt:");
      System.out.println(stmt);
      System.out.println("LeftOp:");
      ValueParser.main(stmt.getLeftOp());
      System.out.println("RightOp:");
      ValueParser.main(stmt.getRightOp());
   }

   private static void parseBreakpointStmt(BreakpointStmt stmt) {
      System.out.println("BreakpointStmt:");
      System.out.println(stmt);
   }

   public static void main(String[] args) {
      String Type = "LookupSwitchStmt";
      System.out.println("if(stmt instanceof " + Type + ")");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "TableSwitchStmt";
      System.out.println("else if(stmt instanceof " + Type + ")");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ExitStmt";
      System.out.println("else if(stmt instanceof " + Type + ")");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
      Type = "ReturnVoidStmt";
      System.out.println("else if(stmt instanceof " + Type + ")");
      System.out.println("\tparse" + Type + "((" + Type + ")stmt);");
   }
}
