package ppg.parse;

import java.util.Stack;
import java.util.Vector;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import ppg.atoms.Nonterminal;
import ppg.atoms.Precedence;
import ppg.atoms.PrecedenceModifier;
import ppg.atoms.Production;
import ppg.atoms.SemanticAction;
import ppg.atoms.SymbolList;
import ppg.cmds.Command;
import ppg.cmds.DropCmd;
import ppg.cmds.ExtendCmd;
import ppg.cmds.NewProdCmd;
import ppg.cmds.OverrideCmd;
import ppg.cmds.TransferCmd;
import ppg.code.ActionCode;
import ppg.code.InitCode;
import ppg.code.ParserCode;
import ppg.code.ScanCode;
import ppg.lex.Token;
import ppg.spec.CUPSpec;
import ppg.spec.PPGSpec;
import ppg.spec.Spec;

class CUP$Parser$actions {
   private final Parser parser;

   CUP$Parser$actions(Parser parser) {
      this.parser = parser;
   }

   public final Symbol CUP$Parser$do_action(int CUP$Parser$act_num, lr_parser CUP$Parser$parser, Stack CUP$Parser$stack, int CUP$Parser$top) throws Exception {
      Symbol CUP$Parser$result;
      String RESULT;
      int cleft;
      int cright;
      Token c;
      int r_idleft;
      int r_idright;
      String r_id;
      int user_codeleft;
      int user_coderight;
      int sym_listleft;
      int sym_listright;
      Vector sym_list;
      int prec_listleft;
      int prec_listright;
      Vector prec_list;
      int startleft;
      int startright;
      int prod_listleft;
      int prod_listright;
      Vector prod_list;
      String id;
      Vector t_list;
      Object p;
      Vector v;
      Vector non_term;
      Precedence RESULT;
      Vector t_list;
      Object mod;
      Vector code;
      Vector RESULT;
      Production p;
      SymbolList RESULT;
      ParserCode RESULT;
      Production p;
      Spec s;
      DropCmd RESULT;
      switch(CUP$Parser$act_num) {
      case 0:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         s = (Spec)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(0, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, s);
         CUP$Parser$parser.done_parsing();
         return CUP$Parser$result;
      case 1:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         s = (Spec)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Parser.setProgramNode(s);
         CUP$Parser$result = new Symbol(12, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 2:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         s = (Spec)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Parser.setProgramNode(s);
         CUP$Parser$result = new Symbol(12, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 3:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).value;
         user_codeleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
         user_coderight = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
         code = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
         sym_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left;
         sym_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).right;
         sym_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).value;
         prec_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         prec_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         prec_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         startleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         startright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         Vector prec_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         prod_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         prod_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         prod_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         int cmd_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         int cmd_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         Vector cmd_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Spec RESULT = new PPGSpec(id, r_id, code, sym_list, prec_list, prec_list, prod_list, cmd_list);
         CUP$Parser$result = new Symbol(13, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 7)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 4:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         c = (Token)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = (String)c.getValue();
         CUP$Parser$result = new Symbol(16, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 5:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(50, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 6:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(50, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 7:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(23, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 8:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(23, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 9:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         t_list = new Vector();
         t_list.addElement(id);
         CUP$Parser$result = new Symbol(24, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 10:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(24, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 11:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addAll(non_term);
         CUP$Parser$result = new Symbol(26, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 12:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(26, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 13:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         v = new Vector();
         v.addElement(id);
         v.addElement(r_id);
         CUP$Parser$result = new Symbol(25, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, v);
         return CUP$Parser$result;
      case 14:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         Command a = (Command)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(a);
         CUP$Parser$result = new Symbol(19, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 15:
         RESULT = null;
         RESULT = new Vector();
         CUP$Parser$result = new Symbol(19, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 16:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = (Production)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Object RESULT;
         if (mod == null) {
            RESULT = new NewProdCmd(p);
         } else if (mod.equals("extend")) {
            RESULT = new ExtendCmd(p);
         } else {
            RESULT = new OverrideCmd(p);
         }

         CUP$Parser$result = new Symbol(28, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 17:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         p = (Production)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new DropCmd(p);
         CUP$Parser$result = new Symbol(28, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 18:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new DropCmd(t_list);
         CUP$Parser$result = new Symbol(28, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 19:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Command RESULT = new TransferCmd(id, non_term);
         CUP$Parser$result = new Symbol(28, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 20:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(r_id);
         CUP$Parser$result = new Symbol(27, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 21:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(id);
         CUP$Parser$result = new Symbol(27, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 22:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         c = (Token)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = (String)c.getValue();
         CUP$Parser$result = new Symbol(1, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 23:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         c = (Token)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = (String)c.getValue();
         CUP$Parser$result = new Symbol(1, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 24:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(1, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 25:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         user_codeleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         user_coderight = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         code = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         Production p = new Production(new Nonterminal(r_id), code);
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(22, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 26:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         v = new Vector();
         Production p = new Production(new Nonterminal(id), non_term);
         v.addElement(p);
         CUP$Parser$result = new Symbol(22, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, v);
         return CUP$Parser$result;
      case 27:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = (Production)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(30, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 28:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = (Production)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(30, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 29:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         Production RESULT = new Production(new Nonterminal(id), non_term);
         CUP$Parser$result = new Symbol(29, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 30:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(31, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 31:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(32, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 32:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(21, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 33:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(mod);
         CUP$Parser$result = new Symbol(21, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 34:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.add(new PrecedenceModifier(r_id));
         CUP$Parser$result = new Symbol(3, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 35:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(3, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 36:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(20, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 37:
         RESULT = null;
         RESULT = new Vector();
         CUP$Parser$result = new Symbol(20, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 38:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Object RESULT = new Nonterminal(id, r_id);
         CUP$Parser$result = new Symbol(2, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 39:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Object RESULT = new SemanticAction(id);
         CUP$Parser$result = new Symbol(2, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 40:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(34, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 41:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(34, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 42:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(33, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 43:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(37, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 44:
         RESULT = null;
         CUP$Parser$result = new Symbol(7, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 45:
         RESULT = null;
         CUP$Parser$result = new Symbol(7, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 46:
         RESULT = null;
         CUP$Parser$result = new Symbol(4, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 47:
         RESULT = null;
         CUP$Parser$result = new Symbol(4, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 48:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 5)).value;
         user_codeleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left;
         user_coderight = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).right;
         code = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).value;
         sym_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         sym_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         sym_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         prec_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         prec_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         prec_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         startleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         startright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         String start = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         prod_listleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         prod_listright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         prod_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         Spec RESULT = new CUPSpec(id, non_term, code, sym_list, prec_list, start, prod_list);
         CUP$Parser$result = new Symbol(14, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 6)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 49:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(38, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 50:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(38, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 51:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(r_id);
         CUP$Parser$result = new Symbol(47, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 52:
         RESULT = null;
         RESULT = new Vector();
         CUP$Parser$result = new Symbol(47, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 53:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(40, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 54:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(51, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 55:
         RESULT = null;
         RESULT = new Vector();
         CUP$Parser$result = new Symbol(51, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 56:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(6, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, mod);
         return CUP$Parser$result;
      case 57:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(6, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, mod);
         return CUP$Parser$result;
      case 58:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(6, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, mod);
         return CUP$Parser$result;
      case 59:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(6, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, mod);
         return CUP$Parser$result;
      case 60:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         Object RESULT = new ActionCode(id);
         CUP$Parser$result = new Symbol(5, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 61:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new ParserCode((String)null, "", id);
         CUP$Parser$result = new Symbol(8, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 62:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         user_codeleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         user_coderight = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         String user_code = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new ParserCode(id, r_id, user_code);
         CUP$Parser$result = new Symbol(8, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 4)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 63:
         RESULT = null;
         RESULT = "";
         CUP$Parser$result = new Symbol(17, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 64:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = id + " extends " + r_id;
         CUP$Parser$result = new Symbol(17, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 65:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = id + " implements " + r_id;
         CUP$Parser$result = new Symbol(17, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 66:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         Object RESULT = new InitCode(id);
         CUP$Parser$result = new Symbol(9, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 67:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         Object RESULT = new ScanCode(id);
         CUP$Parser$result = new Symbol(10, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 68:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         SymbolList s = (SymbolList)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(s);
         CUP$Parser$result = new Symbol(49, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 69:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         SymbolList s = (SymbolList)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(s);
         CUP$Parser$result = new Symbol(49, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 70:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = new SymbolList(0, id, non_term);
         CUP$Parser$result = new Symbol(57, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 71:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = new SymbolList(0, (String)null, t_list);
         CUP$Parser$result = new Symbol(57, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 72:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         non_term = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = new SymbolList(1, id, non_term);
         CUP$Parser$result = new Symbol(57, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 73:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = new SymbolList(1, (String)null, t_list);
         CUP$Parser$result = new Symbol(57, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 74:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(55, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 75:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(56, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 76:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(r_id);
         CUP$Parser$result = new Symbol(48, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 77:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(id);
         CUP$Parser$result = new Symbol(48, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 78:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(r_id);
         CUP$Parser$result = new Symbol(46, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 79:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(id);
         CUP$Parser$result = new Symbol(46, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 80:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(54, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 81:
         RESULT = null;
         RESULT = new Vector();
         CUP$Parser$result = new Symbol(54, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 82:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         p = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(p);
         CUP$Parser$result = new Symbol(53, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 83:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         mod = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(mod);
         CUP$Parser$result = new Symbol(53, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 84:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new Precedence(0, t_list);
         CUP$Parser$result = new Symbol(11, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 85:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new Precedence(1, t_list);
         CUP$Parser$result = new Symbol(11, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 86:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         RESULT = new Precedence(2, t_list);
         CUP$Parser$result = new Symbol(11, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 87:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         t_list = (Vector)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list.addElement(r_id);
         CUP$Parser$result = new Symbol(52, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 88:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         t_list = new Vector();
         t_list.addElement(id);
         CUP$Parser$result = new Symbol(52, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, t_list);
         return CUP$Parser$result;
      case 89:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(35, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 90:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(36, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 91:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 1)).value;
         CUP$Parser$result = new Symbol(42, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 3)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 92:
         RESULT = null;
         RESULT = null;
         CUP$Parser$result = new Symbol(42, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 93:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         r_idleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         r_idright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         r_id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = id + "." + r_id;
         CUP$Parser$result = new Symbol(41, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 94:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(41, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 95:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         RESULT = id + ".*";
         CUP$Parser$result = new Symbol(39, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 96:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(39, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 97:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(43, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 98:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).value;
         RESULT = id + "[]";
         CUP$Parser$result = new Symbol(43, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 2)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 99:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(45, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 100:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         id = (String)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         CUP$Parser$result = new Symbol(44, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, id);
         return CUP$Parser$result;
      case 101:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         c = (Token)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = (String)c.getValue();
         CUP$Parser$result = new Symbol(15, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      case 102:
         RESULT = null;
         cleft = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left;
         cright = ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right;
         c = (Token)((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).value;
         RESULT = (String)c.getValue();
         CUP$Parser$result = new Symbol(18, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).left, ((Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top - 0)).right, RESULT);
         return CUP$Parser$result;
      default:
         throw new Exception("Invalid action number found in internal parse table");
      }
   }
}
