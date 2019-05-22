package org.apache.velocity.runtime.parser;

import java.io.IOException;
import java.io.PrintStream;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;

public class ParserTokenManager implements ParserConstants {
   private int fileDepth;
   private int lparen;
   private int rparen;
   Stack stateStack;
   public boolean debugPrint;
   private boolean inReference;
   public boolean inDirective;
   private boolean inComment;
   public boolean inSet;
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{-2L, -1L, -1L, -1L};
   static final long[] jjbitVec2 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[]{82, 84, 85, 86, 91, 92, 82, 85, 52, 91, 22, 23, 26, 11, 12, 13, 1, 2, 4, 11, 16, 12, 13, 19, 20, 24, 25, 61, 62, 64, 65, 66, 67, 78, 80, 75, 76, 72, 73, 14, 15, 17, 19, 20, 55, 56, 68, 69, 89, 90, 93, 94, 5, 6, 7, 8, 9, 10, 78, 80, 81, 82, 87, 88, 78, 81, 10, 87, 31, 32, 34, 42, 43, 45, 50, 32, 51, 66, 43, 67, 54, 57, 64, 71, 76, 22, 23, 24, 25, 35, 40, 47, 13, 14, 26, 27, 85, 86, 6, 11, 29, 3, 4, 19, 20, 21, 22, 14, 15, 23, 24, 8, 9, 10, 11, 12, 13, 9, 10, 11, 12, 33, 35, 36, 37, 47, 48, 33, 36, 42, 47, 17, 18, 21, 6, 7, 8, 6, 11, 7, 8, 25, 26, 27, 28, 9, 10, 12, 14, 15, 29, 30, 40, 41, 45, 46, 49, 50};
   public static final String[] jjstrLiteralImages = new String[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
   public static final String[] lexStateNames = new String[]{"DIRECTIVE", "REFMOD2", "REFMODIFIER", "DEFAULT", "PRE_DIRECTIVE", "REFERENCE", "IN_MULTI_LINE_COMMENT", "IN_FORMAL_COMMENT", "IN_SINGLE_LINE_COMMENT"};
   public static final int[] jjnewLexState = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
   static final long[] jjtoToken = new long[]{-4163577855537831937L, 3L};
   static final long[] jjtoSkip = new long[]{33554432L, 12L};
   static final long[] jjtoSpecial = new long[]{0L, 12L};
   static final long[] jjtoMore = new long[]{253952L, 0L};
   protected CharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   StringBuffer image;
   int jjimageLen;
   int lengthOfMatch;
   protected char curChar;
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public boolean stateStackPop() {
      Hashtable h;
      try {
         h = (Hashtable)this.stateStack.pop();
      } catch (EmptyStackException var3) {
         this.lparen = 0;
         this.SwitchTo(3);
         return false;
      }

      if (this.debugPrint) {
         System.out.println(" stack pop (" + this.stateStack.size() + ") : lparen=" + (Integer)h.get("lparen") + " newstate=" + (Integer)h.get("lexstate"));
      }

      this.lparen = (Integer)h.get("lparen");
      this.rparen = (Integer)h.get("rparen");
      this.SwitchTo((Integer)h.get("lexstate"));
      return true;
   }

   public boolean stateStackPush() {
      if (this.debugPrint) {
         System.out.println(" (" + this.stateStack.size() + ") pushing cur state : " + this.curLexState);
      }

      Hashtable h = new Hashtable();
      h.put("lexstate", new Integer(this.curLexState));
      h.put("lparen", new Integer(this.lparen));
      h.put("rparen", new Integer(this.rparen));
      this.lparen = 0;
      this.stateStack.push(h);
      return true;
   }

   public void clearStateVars() {
      this.stateStack.clear();
      this.lparen = 0;
      this.rparen = 0;
      this.inReference = false;
      this.inDirective = false;
      this.inComment = false;
      this.inSet = false;
   }

   private void RPARENHandler() {
      boolean closed = false;
      if (this.inComment) {
         closed = true;
      }

      while(!closed) {
         if (this.lparen > 0) {
            if (this.lparen == this.rparen + 1) {
               this.stateStackPop();
            } else {
               ++this.rparen;
            }

            closed = true;
         } else if (!this.stateStackPop()) {
            break;
         }
      }

   }

   public void setDebugStream(PrintStream ds) {
      this.debugStream = ds;
   }

   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 16L) != 0L) {
            return 53;
         } else if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 57;
            return 58;
         } else if ((active0 & 35184372088832L) != 0L) {
            return 45;
         } else if ((active0 & 64L) != 0L) {
            return 60;
         } else if ((active0 & 2147483648L) != 0L) {
            return 96;
         } else {
            if ((active0 & 458752L) != 0L) {
               return 7;
            }

            return -1;
         }
      case 1:
         if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 57;
            this.jjmatchedPos = 1;
            return 58;
         } else {
            if ((active0 & 65536L) != 0L) {
               return 5;
            }

            return -1;
         }
      case 2:
         if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 57;
            this.jjmatchedPos = 2;
            return 58;
         }

         return -1;
      case 3:
         if ((active0 & 268435456L) != 0L) {
            return 58;
         } else {
            if ((active0 & 536870912L) != 0L) {
               this.jjmatchedKind = 57;
               this.jjmatchedPos = 3;
               return 58;
            }

            return -1;
         }
      default:
         return -1;
      }
   }

   private final int jjStartNfa_0(int pos, long active0) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0), pos + 1);
   }

   private final int jjStopAtPos(int pos, int kind) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;
      return pos + 1;
   }

   private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_0(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_0() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_0(327680L);
      case '%':
         return this.jjStopAtPos(0, 35);
      case '(':
         return this.jjStopAtPos(0, 8);
      case '*':
         return this.jjStopAtPos(0, 33);
      case '+':
         return this.jjStopAtPos(0, 32);
      case ',':
         return this.jjStopAtPos(0, 3);
      case '-':
         return this.jjStartNfaWithStates_0(0, 31, 96);
      case '.':
         return this.jjMoveStringLiteralDfa1_0(16L);
      case '/':
         return this.jjStopAtPos(0, 34);
      case ':':
         return this.jjStopAtPos(0, 5);
      case '=':
         return this.jjStartNfaWithStates_0(0, 45, 45);
      case '[':
         return this.jjStopAtPos(0, 1);
      case ']':
         return this.jjStopAtPos(0, 2);
      case 'f':
         return this.jjMoveStringLiteralDfa1_0(536870912L);
      case 't':
         return this.jjMoveStringLiteralDfa1_0(268435456L);
      case '{':
         return this.jjStartNfaWithStates_0(0, 6, 60);
      case '}':
         return this.jjStopAtPos(0, 7);
      default:
         return this.jjMoveNfa_0(0, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_0(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_0(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_0(1, 16, 5);
         }
         break;
      case '.':
         if ((active0 & 16L) != 0L) {
            return this.jjStopAtPos(1, 4);
         }
         break;
      case 'a':
         return this.jjMoveStringLiteralDfa2_0(active0, 536870912L);
      case 'r':
         return this.jjMoveStringLiteralDfa2_0(active0, 268435456L);
      }

      return this.jjStartNfa_0(0, active0);
   }

   private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_0(0, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(1, active0);
            return 2;
         }

         switch(this.curChar) {
         case 'l':
            return this.jjMoveStringLiteralDfa3_0(active0, 536870912L);
         case 'u':
            return this.jjMoveStringLiteralDfa3_0(active0, 268435456L);
         default:
            return this.jjStartNfa_0(1, active0);
         }
      }
   }

   private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_0(1, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(2, active0);
            return 3;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 28, 58);
            }
         default:
            return this.jjStartNfa_0(2, active0);
         case 's':
            return this.jjMoveStringLiteralDfa4_0(active0, 536870912L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_0(2, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(3, active0);
            return 4;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 536870912L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 29, 58);
            }
         default:
            return this.jjStartNfa_0(3, active0);
         }
      }
   }

   private final void jjCheckNAdd(int state) {
      if (this.jjrounds[state] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = state;
         this.jjrounds[state] = this.jjround;
      }

   }

   private final void jjAddStates(int start, int end) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
      } while(start++ != end);

   }

   private final void jjCheckNAddTwoStates(int state1, int state2) {
      this.jjCheckNAdd(state1);
      this.jjCheckNAdd(state2);
   }

   private final void jjCheckNAddStates(int start, int end) {
      do {
         this.jjCheckNAdd(jjnextStates[start]);
      } while(start++ != end);

   }

   private final void jjCheckNAddStates(int start) {
      this.jjCheckNAdd(jjnextStates[start]);
      this.jjCheckNAdd(jjnextStates[start + 1]);
   }

   private final int jjMoveNfa_0(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 96;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(0, 5);
                  } else if ((4294977024L & l) != 0L) {
                     if (kind > 26) {
                        kind = 26;
                     }

                     this.jjCheckNAdd(9);
                  } else if (this.curChar == '-') {
                     this.jjCheckNAddStates(6, 9);
                  } else if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(68, 69);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAdd(53);
                  } else if (this.curChar == '!') {
                     if (kind > 44) {
                        kind = 44;
                     }
                  } else if (this.curChar == '=') {
                     this.jjstateSet[this.jjnewStateCnt++] = 45;
                  } else if (this.curChar == '>') {
                     this.jjstateSet[this.jjnewStateCnt++] = 43;
                  } else if (this.curChar == '<') {
                     this.jjstateSet[this.jjnewStateCnt++] = 40;
                  } else if (this.curChar == '&') {
                     this.jjstateSet[this.jjnewStateCnt++] = 30;
                  } else if (this.curChar == '\'') {
                     this.jjCheckNAddStates(10, 12);
                  } else if (this.curChar == '"') {
                     this.jjCheckNAddStates(13, 15);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  } else if (this.curChar == ')') {
                     if (kind > 9) {
                        kind = 9;
                     }

                     this.jjCheckNAddStates(16, 18);
                  }

                  if ((9216L & l) != 0L) {
                     if (kind > 30) {
                        kind = 30;
                     }
                  } else if (this.curChar == '!') {
                     this.jjstateSet[this.jjnewStateCnt++] = 49;
                  } else if (this.curChar == '>') {
                     if (kind > 40) {
                        kind = 40;
                     }
                  } else if (this.curChar == '<' && kind > 38) {
                     kind = 38;
                  }

                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 28;
                  }
                  break;
               case 1:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddStates(16, 18);
                  }
                  break;
               case 2:
                  if ((9216L & l) != 0L && kind > 9) {
                     kind = 9;
                  }
                  break;
               case 3:
                  if (this.curChar == '\n' && kind > 9) {
                     kind = 9;
                  }
                  break;
               case 4:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 3;
                  }
                  break;
               case 5:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 6;
                  }
                  break;
               case 6:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 7:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 8:
                  if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 9:
                  if ((4294977024L & l) != 0L) {
                     if (kind > 26) {
                        kind = 26;
                     }

                     this.jjCheckNAdd(9);
                  }
                  break;
               case 10:
                  if (this.curChar == '"') {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 11:
                  if ((-17179869185L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 12:
                  if (this.curChar == '"' && kind > 27) {
                     kind = 27;
                  }
               case 13:
               case 23:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 47:
               case 48:
               case 54:
               case 57:
               case 59:
               case 60:
               case 62:
               case 63:
               case 64:
               case 66:
               case 68:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 80:
               case 88:
               case 92:
               default:
                  break;
               case 14:
                  if ((566935683072L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 15:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(19, 22);
                  }
                  break;
               case 16:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 17:
                  if ((4222124650659840L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 18;
                  }
                  break;
               case 18:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(16);
                  }
                  break;
               case 19:
                  if (this.curChar == ' ') {
                     this.jjAddStates(23, 24);
                  }
                  break;
               case 20:
                  if (this.curChar == '\n') {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 21:
                  if (this.curChar == '\'') {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 22:
                  if ((-549755813889L & l) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 24:
                  if (this.curChar == ' ') {
                     this.jjAddStates(25, 26);
                  }
                  break;
               case 25:
                  if (this.curChar == '\n') {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 26:
                  if (this.curChar == '\'' && kind > 27) {
                     kind = 27;
                  }
                  break;
               case 27:
                  if ((9216L & l) != 0L && kind > 30) {
                     kind = 30;
                  }
                  break;
               case 28:
                  if (this.curChar == '\n' && kind > 30) {
                     kind = 30;
                  }
                  break;
               case 29:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 28;
                  }
                  break;
               case 30:
                  if (this.curChar == '&' && kind > 36) {
                     kind = 36;
                  }
                  break;
               case 31:
                  if (this.curChar == '&') {
                     this.jjstateSet[this.jjnewStateCnt++] = 30;
                  }
                  break;
               case 39:
                  if (this.curChar == '<' && kind > 38) {
                     kind = 38;
                  }
                  break;
               case 40:
                  if (this.curChar == '=' && kind > 39) {
                     kind = 39;
                  }
                  break;
               case 41:
                  if (this.curChar == '<') {
                     this.jjstateSet[this.jjnewStateCnt++] = 40;
                  }
                  break;
               case 42:
                  if (this.curChar == '>' && kind > 40) {
                     kind = 40;
                  }
                  break;
               case 43:
                  if (this.curChar == '=' && kind > 41) {
                     kind = 41;
                  }
                  break;
               case 44:
                  if (this.curChar == '>') {
                     this.jjstateSet[this.jjnewStateCnt++] = 43;
                  }
                  break;
               case 45:
                  if (this.curChar == '=' && kind > 42) {
                     kind = 42;
                  }
                  break;
               case 46:
                  if (this.curChar == '=') {
                     this.jjstateSet[this.jjnewStateCnt++] = 45;
                  }
                  break;
               case 49:
                  if (this.curChar == '=' && kind > 43) {
                     kind = 43;
                  }
                  break;
               case 50:
                  if (this.curChar == '!') {
                     this.jjstateSet[this.jjnewStateCnt++] = 49;
                  }
                  break;
               case 51:
                  if (this.curChar == '!' && kind > 44) {
                     kind = 44;
                  }
                  break;
               case 52:
                  if (this.curChar == '.') {
                     this.jjCheckNAdd(53);
                  }
                  break;
               case 53:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(53, 54);
                  }
                  break;
               case 55:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(56);
                  }
                  break;
               case 56:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(56);
                  }
                  break;
               case 58:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 58;
                  }
                  break;
               case 61:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjAddStates(27, 28);
                  }
                  break;
               case 65:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 67:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 69:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 70:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 81:
                  if (this.curChar == '-') {
                     this.jjCheckNAddStates(6, 9);
                  }
                  break;
               case 82:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddTwoStates(82, 84);
                  }
                  break;
               case 83:
                  if (this.curChar == '.' && kind > 52) {
                     kind = 52;
                  }
                  break;
               case 84:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 83;
                  }
                  break;
               case 85:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(85, 86);
                  }
                  break;
               case 86:
                  if (this.curChar == '.') {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(87, 88);
                  }
                  break;
               case 87:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(87, 88);
                  }
                  break;
               case 89:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(90);
                  }
                  break;
               case 90:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(90);
                  }
                  break;
               case 91:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(91, 92);
                  }
                  break;
               case 93:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(94);
                  }
                  break;
               case 94:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(94);
                  }
                  break;
               case 95:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(0, 5);
                  }
                  break;
               case 96:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(91, 92);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAdd(53);
                  }

                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(85, 86);
                  }

                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddTwoStates(82, 84);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjCheckNAdd(58);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(29, 32);
                  } else if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 60;
                  } else if (this.curChar == '|') {
                     this.jjstateSet[this.jjnewStateCnt++] = 35;
                  }

                  if (this.curChar == 'n') {
                     this.jjAddStates(33, 34);
                  } else if (this.curChar == 'g') {
                     this.jjAddStates(35, 36);
                  } else if (this.curChar == 'l') {
                     this.jjAddStates(37, 38);
                  } else if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 47;
                  } else if (this.curChar == 'o') {
                     this.jjstateSet[this.jjnewStateCnt++] = 37;
                  } else if (this.curChar == 'a') {
                     this.jjstateSet[this.jjnewStateCnt++] = 33;
                  }
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
               case 7:
               case 8:
               case 9:
               case 10:
               case 12:
               case 15:
               case 16:
               case 17:
               case 18:
               case 19:
               case 20:
               case 21:
               case 24:
               case 25:
               case 26:
               case 27:
               case 28:
               case 29:
               case 30:
               case 31:
               case 39:
               case 40:
               case 41:
               case 42:
               case 43:
               case 44:
               case 45:
               case 46:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 55:
               case 56:
               case 65:
               case 67:
               case 69:
               case 70:
               case 81:
               case 82:
               case 83:
               case 84:
               case 85:
               case 86:
               case 87:
               case 89:
               case 90:
               case 91:
               default:
                  break;
               case 6:
                  if (kind > 15) {
                     kind = 15;
                  }
                  break;
               case 11:
                  if ((-268435457L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 13:
                  if (this.curChar == '\\') {
                     this.jjAddStates(39, 43);
                  }
                  break;
               case 14:
                  if ((5700160604602368L & l) != 0L) {
                     this.jjCheckNAddStates(13, 15);
                  }
                  break;
               case 22:
                  this.jjAddStates(10, 12);
                  break;
               case 23:
                  if (this.curChar == '\\') {
                     this.jjAddStates(25, 26);
                  }
                  break;
               case 32:
                  if (this.curChar == 'd' && kind > 36) {
                     kind = 36;
                  }
                  break;
               case 33:
                  if (this.curChar == 'n') {
                     this.jjstateSet[this.jjnewStateCnt++] = 32;
                  }
                  break;
               case 34:
                  if (this.curChar == 'a') {
                     this.jjstateSet[this.jjnewStateCnt++] = 33;
                  }
                  break;
               case 35:
                  if (this.curChar == '|' && kind > 37) {
                     kind = 37;
                  }
                  break;
               case 36:
                  if (this.curChar == '|') {
                     this.jjstateSet[this.jjnewStateCnt++] = 35;
                  }
                  break;
               case 37:
                  if (this.curChar == 'r' && kind > 37) {
                     kind = 37;
                  }
                  break;
               case 38:
                  if (this.curChar == 'o') {
                     this.jjstateSet[this.jjnewStateCnt++] = 37;
                  }
                  break;
               case 47:
                  if (this.curChar == 'q' && kind > 42) {
                     kind = 42;
                  }
                  break;
               case 48:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 47;
                  }
                  break;
               case 54:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(44, 45);
                  }
                  break;
               case 57:
               case 58:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjCheckNAdd(58);
                  }
                  break;
               case 59:
                  if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 60;
                  }
                  break;
               case 60:
               case 61:
                  if ((576460745995190270L & l) != 0L) {
                     this.jjCheckNAddTwoStates(61, 62);
                  }
                  break;
               case 62:
                  if (this.curChar == '}' && kind > 58) {
                     kind = 58;
                  }
                  break;
               case 63:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(29, 32);
                  }
                  break;
               case 64:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(64, 65);
                  }
                  break;
               case 66:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(66, 67);
                  }
                  break;
               case 68:
                  if (this.curChar == '\\') {
                     this.jjAddStates(46, 47);
                  }
                  break;
               case 71:
                  if (this.curChar == 'l') {
                     this.jjAddStates(37, 38);
                  }
                  break;
               case 72:
                  if (this.curChar == 't' && kind > 38) {
                     kind = 38;
                  }
                  break;
               case 73:
                  if (this.curChar == 'e' && kind > 39) {
                     kind = 39;
                  }
                  break;
               case 74:
                  if (this.curChar == 'g') {
                     this.jjAddStates(35, 36);
                  }
                  break;
               case 75:
                  if (this.curChar == 't' && kind > 40) {
                     kind = 40;
                  }
                  break;
               case 76:
                  if (this.curChar == 'e' && kind > 41) {
                     kind = 41;
                  }
                  break;
               case 77:
                  if (this.curChar == 'n') {
                     this.jjAddStates(33, 34);
                  }
                  break;
               case 78:
                  if (this.curChar == 'e' && kind > 43) {
                     kind = 43;
                  }
                  break;
               case 79:
                  if (this.curChar == 't' && kind > 44) {
                     kind = 44;
                  }
                  break;
               case 80:
                  if (this.curChar == 'o') {
                     this.jjstateSet[this.jjnewStateCnt++] = 79;
                  }
                  break;
               case 88:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(48, 49);
                  }
                  break;
               case 92:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(50, 51);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 6:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 11:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                     this.jjAddStates(13, 15);
                  }
                  break;
               case 22:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                     this.jjAddStates(10, 12);
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 96 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_6(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_6(int pos, long active0) {
      return this.jjMoveNfa_6(this.jjStopStringLiteralDfa_6(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_6(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_6(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_6() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_6(327680L);
      case '*':
         return this.jjMoveStringLiteralDfa1_6(16777216L);
      default:
         return this.jjMoveNfa_6(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_6(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_6(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }

         if ((active0 & 16777216L) != 0L) {
            return this.jjStopAtPos(1, 24);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_6(1, 16, 0);
         }
      }

      return this.jjStartNfa_6(0, active0);
   }

   private final int jjMoveNfa_6(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 12;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(9, 10);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
               case 4:
               case 5:
               case 7:
               case 9:
               default:
                  break;
               case 6:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 8:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(9, 10);
                  }
                  break;
               case 10:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 11:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(9, 10);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 4:
               case 6:
               case 8:
               default:
                  break;
               case 3:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(52, 55);
                  }
                  break;
               case 5:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(5, 6);
                  }
                  break;
               case 7:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(7, 8);
                  }
                  break;
               case 9:
                  if (this.curChar == '\\') {
                     this.jjAddStates(56, 57);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 12 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_4(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_4(int pos, long active0) {
      return this.jjMoveNfa_4(this.jjStopStringLiteralDfa_4(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_4(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_4(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_4() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_4(327680L);
      default:
         return this.jjMoveNfa_4(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_4(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_4(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_4(1, 16, 0);
         }
      }

      return this.jjStartNfa_4(0, active0);
   }

   private final int jjMoveNfa_4(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 92;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(58, 63);
                  } else if (this.curChar == '-') {
                     this.jjCheckNAddStates(64, 67);
                  } else if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(26, 27);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAdd(11);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
               case 4:
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 12:
               case 15:
               case 17:
               case 18:
               case 20:
               case 21:
               case 22:
               case 24:
               case 26:
               case 29:
               case 30:
               case 35:
               case 36:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 46:
               case 47:
               case 48:
               case 49:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 65:
               case 68:
               case 69:
               case 70:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 84:
               case 88:
               default:
                  break;
               case 10:
                  if (this.curChar == '.') {
                     this.jjCheckNAdd(11);
                  }
                  break;
               case 11:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(11, 12);
                  }
                  break;
               case 13:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 14:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(14);
                  }
                  break;
               case 16:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 16;
                  }
                  break;
               case 19:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjAddStates(23, 24);
                  }
                  break;
               case 23:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 25:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(26, 27);
                  }
                  break;
               case 27:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 28:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(26, 27);
                  }
                  break;
               case 31:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddStates(68, 70);
                  }
                  break;
               case 32:
                  if ((9216L & l) != 0L && kind > 46) {
                     kind = 46;
                  }
                  break;
               case 33:
                  if (this.curChar == '\n' && kind > 46) {
                     kind = 46;
                  }
                  break;
               case 34:
               case 51:
                  if (this.curChar == '\r') {
                     this.jjCheckNAdd(33);
                  }
                  break;
               case 42:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddStates(71, 73);
                  }
                  break;
               case 43:
                  if ((9216L & l) != 0L && kind > 49) {
                     kind = 49;
                  }
                  break;
               case 44:
                  if (this.curChar == '\n' && kind > 49) {
                     kind = 49;
                  }
                  break;
               case 45:
               case 67:
                  if (this.curChar == '\r') {
                     this.jjCheckNAdd(44);
                  }
                  break;
               case 50:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddStates(74, 76);
                  }
                  break;
               case 66:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddStates(77, 79);
                  }
                  break;
               case 77:
                  if (this.curChar == '-') {
                     this.jjCheckNAddStates(64, 67);
                  }
                  break;
               case 78:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddTwoStates(78, 80);
                  }
                  break;
               case 79:
                  if (this.curChar == '.' && kind > 52) {
                     kind = 52;
                  }
                  break;
               case 80:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 79;
                  }
                  break;
               case 81:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(81, 82);
                  }
                  break;
               case 82:
                  if (this.curChar == '.') {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(83, 84);
                  }
                  break;
               case 83:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(83, 84);
                  }
                  break;
               case 85:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(86);
                  }
                  break;
               case 86:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(86);
                  }
                  break;
               case 87:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(87, 88);
                  }
                  break;
               case 89:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(90);
                  }
                  break;
               case 90:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(90);
                  }
                  break;
               case 91:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(58, 63);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 10:
               case 11:
               case 13:
               case 14:
               case 23:
               case 25:
               case 27:
               case 28:
               case 31:
               case 32:
               case 33:
               case 34:
               case 42:
               case 43:
               case 44:
               case 45:
               case 50:
               case 51:
               case 66:
               case 67:
               case 77:
               case 78:
               case 79:
               case 80:
               case 81:
               case 82:
               case 83:
               case 85:
               case 86:
               case 87:
               default:
                  break;
               case 3:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjCheckNAdd(16);
                  } else if (this.curChar == '{') {
                     this.jjAddStates(80, 84);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(85, 88);
                  }

                  if (this.curChar == 'e') {
                     this.jjAddStates(89, 91);
                  } else if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 18;
                  } else if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 8;
                  } else if (this.curChar == 'i') {
                     this.jjstateSet[this.jjnewStateCnt++] = 4;
                  }
                  break;
               case 4:
                  if (this.curChar == 'f' && kind > 47) {
                     kind = 47;
                  }
                  break;
               case 5:
                  if (this.curChar == 'i') {
                     this.jjstateSet[this.jjnewStateCnt++] = 4;
                  }
                  break;
               case 6:
                  if (this.curChar == 'p' && kind > 50) {
                     kind = 50;
                  }
                  break;
               case 7:
                  if (this.curChar == 'o') {
                     this.jjstateSet[this.jjnewStateCnt++] = 6;
                  }
                  break;
               case 8:
                  if (this.curChar == 't') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 9:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 8;
                  }
                  break;
               case 12:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(92, 93);
                  }
                  break;
               case 15:
               case 16:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 57) {
                        kind = 57;
                     }

                     this.jjCheckNAdd(16);
                  }
                  break;
               case 17:
                  if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 18;
                  }
                  break;
               case 18:
               case 19:
                  if ((576460745995190270L & l) != 0L) {
                     this.jjCheckNAddTwoStates(19, 20);
                  }
                  break;
               case 20:
                  if (this.curChar == '}' && kind > 58) {
                     kind = 58;
                  }
                  break;
               case 21:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(85, 88);
                  }
                  break;
               case 22:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(22, 23);
                  }
                  break;
               case 24:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(24, 25);
                  }
                  break;
               case 26:
                  if (this.curChar == '\\') {
                     this.jjAddStates(94, 95);
                  }
                  break;
               case 29:
                  if (this.curChar == 'e') {
                     this.jjAddStates(89, 91);
                  }
                  break;
               case 30:
                  if (this.curChar == 'd') {
                     if (kind > 46) {
                        kind = 46;
                     }

                     this.jjCheckNAddStates(68, 70);
                  }
                  break;
               case 35:
                  if (this.curChar == 'n') {
                     this.jjstateSet[this.jjnewStateCnt++] = 30;
                  }
                  break;
               case 36:
                  if (this.curChar == 'f' && kind > 48) {
                     kind = 48;
                  }
                  break;
               case 37:
                  if (this.curChar == 'i') {
                     this.jjstateSet[this.jjnewStateCnt++] = 36;
                  }
                  break;
               case 38:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 37;
                  }
                  break;
               case 39:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 38;
                  }
                  break;
               case 40:
                  if (this.curChar == 'l') {
                     this.jjstateSet[this.jjnewStateCnt++] = 39;
                  }
                  break;
               case 41:
                  if (this.curChar == 'e') {
                     if (kind > 49) {
                        kind = 49;
                     }

                     this.jjCheckNAddStates(71, 73);
                  }
                  break;
               case 46:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 41;
                  }
                  break;
               case 47:
                  if (this.curChar == 'l') {
                     this.jjstateSet[this.jjnewStateCnt++] = 46;
                  }
                  break;
               case 48:
                  if (this.curChar == '{') {
                     this.jjAddStates(80, 84);
                  }
                  break;
               case 49:
                  if (this.curChar == '}') {
                     if (kind > 46) {
                        kind = 46;
                     }

                     this.jjCheckNAddStates(74, 76);
                  }
                  break;
               case 52:
                  if (this.curChar == 'd') {
                     this.jjstateSet[this.jjnewStateCnt++] = 49;
                  }
                  break;
               case 53:
                  if (this.curChar == 'n') {
                     this.jjstateSet[this.jjnewStateCnt++] = 52;
                  }
                  break;
               case 54:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 53;
                  }
                  break;
               case 55:
                  if (this.curChar == '}' && kind > 47) {
                     kind = 47;
                  }
                  break;
               case 56:
                  if (this.curChar == 'f') {
                     this.jjstateSet[this.jjnewStateCnt++] = 55;
                  }
                  break;
               case 57:
                  if (this.curChar == 'i') {
                     this.jjstateSet[this.jjnewStateCnt++] = 56;
                  }
                  break;
               case 58:
                  if (this.curChar == '}' && kind > 48) {
                     kind = 48;
                  }
                  break;
               case 59:
                  if (this.curChar == 'f') {
                     this.jjstateSet[this.jjnewStateCnt++] = 58;
                  }
                  break;
               case 60:
                  if (this.curChar == 'i') {
                     this.jjstateSet[this.jjnewStateCnt++] = 59;
                  }
                  break;
               case 61:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 60;
                  }
                  break;
               case 62:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 61;
                  }
                  break;
               case 63:
                  if (this.curChar == 'l') {
                     this.jjstateSet[this.jjnewStateCnt++] = 62;
                  }
                  break;
               case 64:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 63;
                  }
                  break;
               case 65:
                  if (this.curChar == '}') {
                     if (kind > 49) {
                        kind = 49;
                     }

                     this.jjCheckNAddStates(77, 79);
                  }
                  break;
               case 68:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 65;
                  }
                  break;
               case 69:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 68;
                  }
                  break;
               case 70:
                  if (this.curChar == 'l') {
                     this.jjstateSet[this.jjnewStateCnt++] = 69;
                  }
                  break;
               case 71:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 70;
                  }
                  break;
               case 72:
                  if (this.curChar == '}' && kind > 50) {
                     kind = 50;
                  }
                  break;
               case 73:
                  if (this.curChar == 'p') {
                     this.jjstateSet[this.jjnewStateCnt++] = 72;
                  }
                  break;
               case 74:
                  if (this.curChar == 'o') {
                     this.jjstateSet[this.jjnewStateCnt++] = 73;
                  }
                  break;
               case 75:
                  if (this.curChar == 't') {
                     this.jjstateSet[this.jjnewStateCnt++] = 74;
                  }
                  break;
               case 76:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 75;
                  }
                  break;
               case 84:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(96, 97);
                  }
                  break;
               case 88:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(48, 49);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 92 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_3(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 1572864L) != 0L) {
            return 14;
         } else {
            if ((active0 & 458752L) != 0L) {
               return 29;
            }

            return -1;
         }
      default:
         return -1;
      }
   }

   private final int jjStartNfa_3(int pos, long active0) {
      return this.jjMoveNfa_3(this.jjStopStringLiteralDfa_3(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_3(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_3(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_3() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_3(327680L);
      case '\\':
         this.jjmatchedKind = 20;
         return this.jjMoveStringLiteralDfa1_3(524288L);
      default:
         return this.jjMoveNfa_3(18, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_3(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_3(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_3(1, 16, 27);
         }
         break;
      case '\\':
         if ((active0 & 524288L) != 0L) {
            return this.jjStartNfaWithStates_3(1, 19, 30);
         }
      }

      return this.jjStartNfa_3(0, active0);
   }

   private final int jjMoveNfa_3(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 30;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddTwoStates(0, 1);
                  }
                  break;
               case 1:
                  if (this.curChar == '#') {
                     this.jjCheckNAddTwoStates(6, 11);
                  }
               case 2:
               case 5:
               case 6:
               case 7:
               case 8:
               case 9:
               case 10:
               case 11:
               case 13:
               case 16:
               case 19:
               case 21:
               case 23:
               default:
                  break;
               case 3:
                  if (this.curChar == ' ') {
                     this.jjAddStates(101, 102);
                  }
                  break;
               case 4:
                  if (this.curChar == '(' && kind > 12) {
                     kind = 12;
                  }
                  break;
               case 12:
                  if ((-103079215105L & l) != 0L) {
                     if (kind > 21) {
                        kind = 21;
                     }

                     this.jjCheckNAdd(12);
                  }
                  break;
               case 14:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(23, 24);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 16;
                  }

                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 15:
                  if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 16;
                  }
                  break;
               case 17:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 11) {
                        kind = 11;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 17;
                  }
                  break;
               case 18:
                  if ((-103079215105L & l) != 0L) {
                     if (kind > 21) {
                        kind = 21;
                     }

                     this.jjCheckNAdd(12);
                  } else if (this.curChar == '#') {
                     this.jjCheckNAddStates(98, 100);
                  } else if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(23, 24);
                  }

                  if ((4294967808L & l) != 0L) {
                     this.jjCheckNAddTwoStates(0, 1);
                  }
                  break;
               case 20:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 22:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(23, 24);
                  }
                  break;
               case 24:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 25:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(23, 24);
                  }
                  break;
               case 26:
                  if (this.curChar == '#') {
                     this.jjCheckNAddStates(98, 100);
                  }
                  break;
               case 27:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 28;
                  }
                  break;
               case 28:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 29:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 27;
                  }
                  break;
               case 30:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(23, 24);
                  }

                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 2:
                  if (this.curChar == 't') {
                     this.jjCheckNAddTwoStates(3, 4);
                  }
               case 3:
               case 4:
               case 15:
               case 20:
               case 22:
               case 24:
               case 25:
               case 26:
               case 27:
               default:
                  break;
               case 5:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
                  break;
               case 6:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 7:
                  if (this.curChar == '}') {
                     this.jjCheckNAddTwoStates(3, 4);
                  }
                  break;
               case 8:
                  if (this.curChar == 't') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 9:
                  if (this.curChar == 'e') {
                     this.jjstateSet[this.jjnewStateCnt++] = 8;
                  }
                  break;
               case 10:
                  if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 9;
                  }
                  break;
               case 11:
                  if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 10;
                  }
                  break;
               case 12:
                  if ((-268435457L & l) != 0L) {
                     if (kind > 21) {
                        kind = 21;
                     }

                     this.jjCheckNAdd(12);
                  }
                  break;
               case 13:
                  if (this.curChar == '\\') {
                     this.jjAddStates(107, 108);
                  }
                  break;
               case 14:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(21, 22);
                  }

                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(19, 20);
                  }

                  if (this.curChar == '\\') {
                     this.jjstateSet[this.jjnewStateCnt++] = 13;
                  }
                  break;
               case 16:
               case 17:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 11) {
                        kind = 11;
                     }

                     this.jjCheckNAdd(17);
                  }
                  break;
               case 18:
                  if ((-268435457L & l) != 0L) {
                     if (kind > 21) {
                        kind = 21;
                     }

                     this.jjCheckNAdd(12);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(103, 106);
                  }

                  if (this.curChar == '\\') {
                     this.jjAddStates(107, 108);
                  }
                  break;
               case 19:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(19, 20);
                  }
                  break;
               case 21:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(21, 22);
                  }
                  break;
               case 23:
                  if (this.curChar == '\\') {
                     this.jjAddStates(109, 110);
                  }
                  break;
               case 28:
                  if (kind > 15) {
                     kind = 15;
                  }
                  break;
               case 29:
                  if (this.curChar == '{') {
                     this.jjstateSet[this.jjnewStateCnt++] = 10;
                  } else if (this.curChar == 's') {
                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 30:
                  if (this.curChar == '\\') {
                     this.jjAddStates(107, 108);
                  }

                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(21, 22);
                  }

                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(19, 20);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 12:
               case 18:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                     if (kind > 21) {
                        kind = 21;
                     }

                     this.jjCheckNAdd(12);
                  }
                  break;
               case 28:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 30 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_7(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_7(int pos, long active0) {
      return this.jjMoveNfa_7(this.jjStopStringLiteralDfa_7(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_7(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_7(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_7() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_7(327680L);
      case '*':
         return this.jjMoveStringLiteralDfa1_7(8388608L);
      default:
         return this.jjMoveNfa_7(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_7(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_7(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }

         if ((active0 & 8388608L) != 0L) {
            return this.jjStopAtPos(1, 23);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_7(1, 16, 0);
         }
      }

      return this.jjStartNfa_7(0, active0);
   }

   private final int jjMoveNfa_7(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 12;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(9, 10);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
               case 4:
               case 5:
               case 7:
               case 9:
               default:
                  break;
               case 6:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 8:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(9, 10);
                  }
                  break;
               case 10:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 11:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(9, 10);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 4:
               case 6:
               case 8:
               default:
                  break;
               case 3:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(52, 55);
                  }
                  break;
               case 5:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(5, 6);
                  }
                  break;
               case 7:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(7, 8);
                  }
                  break;
               case 9:
                  if (this.curChar == '\\') {
                     this.jjAddStates(56, 57);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 12 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_8(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_8(int pos, long active0) {
      return this.jjMoveNfa_8(this.jjStopStringLiteralDfa_8(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_8(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_8(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_8() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_8(327680L);
      default:
         return this.jjMoveNfa_8(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_8(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_8(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_8(1, 16, 0);
         }
      }

      return this.jjStartNfa_8(0, active0);
   }

   private final int jjMoveNfa_8(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 15;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if ((9216L & l) != 0L) {
                     if (kind > 22) {
                        kind = 22;
                     }
                  } else if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(12, 13);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }

                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 4:
                  if ((9216L & l) != 0L && kind > 22) {
                     kind = 22;
                  }
                  break;
               case 5:
                  if (this.curChar == '\n' && kind > 22) {
                     kind = 22;
                  }
                  break;
               case 6:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
               case 7:
               case 8:
               case 10:
               case 12:
               default:
                  break;
               case 9:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 11:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(12, 13);
                  }
                  break;
               case 13:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 14:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(12, 13);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 4:
               case 5:
               case 6:
               case 7:
               case 9:
               case 11:
               default:
                  break;
               case 3:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(111, 114);
                  }
                  break;
               case 8:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(8, 9);
                  }
                  break;
               case 10:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(10, 11);
                  }
                  break;
               case 12:
                  if (this.curChar == '\\') {
                     this.jjAddStates(115, 116);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 15 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_5(int pos, long active0, long active1) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               return 5;
            }

            return -1;
         }
      case 1:
         if ((active0 & 65536L) != 0L) {
            return 0;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               this.jjmatchedPos = 1;
               return 5;
            }

            return -1;
         }
      case 2:
         if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 62;
            this.jjmatchedPos = 2;
            return 5;
         }

         return -1;
      case 3:
         if ((active0 & 536870912L) != 0L) {
            this.jjmatchedKind = 62;
            this.jjmatchedPos = 3;
            return 5;
         } else {
            if ((active0 & 268435456L) != 0L) {
               return 5;
            }

            return -1;
         }
      default:
         return -1;
      }
   }

   private final int jjStartNfa_5(int pos, long active0, long active1) {
      return this.jjMoveNfa_5(this.jjStopStringLiteralDfa_5(pos, active0, active1), pos + 1);
   }

   private final int jjStartNfaWithStates_5(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_5(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_5() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_5(327680L);
      case 'f':
         return this.jjMoveStringLiteralDfa1_5(536870912L);
      case 't':
         return this.jjMoveStringLiteralDfa1_5(268435456L);
      case '{':
         return this.jjStopAtPos(0, 64);
      case '}':
         return this.jjStopAtPos(0, 65);
      default:
         return this.jjMoveNfa_5(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_5(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_5(0, active0, 0L);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_5(1, 16, 0);
         }
         break;
      case 'a':
         return this.jjMoveStringLiteralDfa2_5(active0, 536870912L);
      case 'r':
         return this.jjMoveStringLiteralDfa2_5(active0, 268435456L);
      }

      return this.jjStartNfa_5(0, active0, 0L);
   }

   private final int jjMoveStringLiteralDfa2_5(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_5(0, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_5(1, active0, 0L);
            return 2;
         }

         switch(this.curChar) {
         case 'l':
            return this.jjMoveStringLiteralDfa3_5(active0, 536870912L);
         case 'u':
            return this.jjMoveStringLiteralDfa3_5(active0, 268435456L);
         default:
            return this.jjStartNfa_5(1, active0, 0L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa3_5(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_5(1, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_5(2, active0, 0L);
            return 3;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_5(3, 28, 5);
            }
         default:
            return this.jjStartNfa_5(2, active0, 0L);
         case 's':
            return this.jjMoveStringLiteralDfa4_5(active0, 536870912L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa4_5(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_5(2, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_5(3, active0, 0L);
            return 4;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 536870912L) != 0L) {
               return this.jjStartNfaWithStates_5(4, 29, 5);
            }
         default:
            return this.jjStartNfa_5(3, active0, 0L);
         }
      }
   }

   private final int jjMoveNfa_5(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 16;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(13, 14);
                  } else if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
               case 4:
               case 7:
               case 8:
               case 9:
               case 11:
               case 13:
               default:
                  break;
               case 5:
                  if ((287984085547089920L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 6:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 10:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 12:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(13, 14);
                  }
                  break;
               case 14:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 15:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(13, 14);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 6:
               case 10:
               case 12:
               default:
                  break;
               case 3:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(5);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(117, 120);
                  }
                  break;
               case 4:
               case 5:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(5);
                  }
                  break;
               case 7:
                  if ((576460743847706622L & l) != 0L && kind > 63) {
                     kind = 63;
                  }
                  break;
               case 8:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(117, 120);
                  }
                  break;
               case 9:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(9, 10);
                  }
                  break;
               case 11:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(11, 12);
                  }
                  break;
               case 13:
                  if (this.curChar == '\\') {
                     this.jjAddStates(92, 93);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 16 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_1(int pos, long active0) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         } else if ((active0 & 16L) != 0L) {
            return 53;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               return 23;
            }

            return -1;
         }
      case 1:
         if ((active0 & 65536L) != 0L) {
            return 0;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               this.jjmatchedPos = 1;
               return 23;
            }

            return -1;
         }
      case 2:
         if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 62;
            this.jjmatchedPos = 2;
            return 23;
         }

         return -1;
      case 3:
         if ((active0 & 268435456L) != 0L) {
            return 23;
         } else {
            if ((active0 & 536870912L) != 0L) {
               this.jjmatchedKind = 62;
               this.jjmatchedPos = 3;
               return 23;
            }

            return -1;
         }
      default:
         return -1;
      }
   }

   private final int jjStartNfa_1(int pos, long active0) {
      return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(pos, active0), pos + 1);
   }

   private final int jjStartNfaWithStates_1(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_1(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_1() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_1(327680L);
      case ')':
         return this.jjStopAtPos(0, 10);
      case ',':
         return this.jjStopAtPos(0, 3);
      case '.':
         return this.jjMoveStringLiteralDfa1_1(16L);
      case ':':
         return this.jjStopAtPos(0, 5);
      case '[':
         return this.jjStopAtPos(0, 1);
      case ']':
         return this.jjStopAtPos(0, 2);
      case 'f':
         return this.jjMoveStringLiteralDfa1_1(536870912L);
      case 't':
         return this.jjMoveStringLiteralDfa1_1(268435456L);
      case '{':
         return this.jjStopAtPos(0, 6);
      case '}':
         return this.jjStopAtPos(0, 7);
      default:
         return this.jjMoveNfa_1(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_1(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_1(0, active0);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_1(1, 16, 0);
         }
         break;
      case '.':
         if ((active0 & 16L) != 0L) {
            return this.jjStopAtPos(1, 4);
         }
         break;
      case 'a':
         return this.jjMoveStringLiteralDfa2_1(active0, 536870912L);
      case 'r':
         return this.jjMoveStringLiteralDfa2_1(active0, 268435456L);
      }

      return this.jjStartNfa_1(0, active0);
   }

   private final int jjMoveStringLiteralDfa2_1(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_1(0, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_1(1, active0);
            return 2;
         }

         switch(this.curChar) {
         case 'l':
            return this.jjMoveStringLiteralDfa3_1(active0, 536870912L);
         case 'u':
            return this.jjMoveStringLiteralDfa3_1(active0, 268435456L);
         default:
            return this.jjStartNfa_1(1, active0);
         }
      }
   }

   private final int jjMoveStringLiteralDfa3_1(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_1(1, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_1(2, active0);
            return 3;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_1(3, 28, 23);
            }
         default:
            return this.jjStartNfa_1(2, active0);
         case 's':
            return this.jjMoveStringLiteralDfa4_1(active0, 536870912L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa4_1(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_1(2, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_1(3, active0);
            return 4;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 536870912L) != 0L) {
               return this.jjStartNfaWithStates_1(4, 29, 23);
            }
         default:
            return this.jjStartNfa_1(3, active0);
         }
      }
   }

   private final int jjMoveNfa_1(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 54;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(121, 126);
                  } else if ((4294977024L & l) != 0L) {
                     if (kind > 26) {
                        kind = 26;
                     }

                     this.jjCheckNAdd(4);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAddTwoStates(43, 53);
                  } else if (this.curChar == '-') {
                     this.jjCheckNAddStates(127, 130);
                  } else if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(29, 30);
                  } else if (this.curChar == '\'') {
                     this.jjCheckNAddStates(131, 133);
                  } else if (this.curChar == '"') {
                     this.jjCheckNAddStates(134, 136);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
                  break;
               case 4:
                  if ((4294977024L & l) != 0L) {
                     if (kind > 26) {
                        kind = 26;
                     }

                     this.jjCheckNAdd(4);
                  }
                  break;
               case 5:
                  if (this.curChar == '"') {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 6:
                  if ((-17179869185L & l) != 0L) {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 7:
                  if (this.curChar == '"' && kind > 27) {
                     kind = 27;
                  }
               case 8:
               case 18:
               case 22:
               case 24:
               case 25:
               case 27:
               case 29:
               case 39:
               case 44:
               case 48:
               default:
                  break;
               case 9:
                  if ((566935683072L & l) != 0L) {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 10:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(137, 140);
                  }
                  break;
               case 11:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 12:
                  if ((4222124650659840L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 13;
                  }
                  break;
               case 13:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(11);
                  }
                  break;
               case 14:
                  if (this.curChar == ' ') {
                     this.jjAddStates(107, 108);
                  }
                  break;
               case 15:
                  if (this.curChar == '\n') {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 16:
                  if (this.curChar == '\'') {
                     this.jjCheckNAddStates(131, 133);
                  }
                  break;
               case 17:
                  if ((-549755813889L & l) != 0L) {
                     this.jjCheckNAddStates(131, 133);
                  }
                  break;
               case 19:
                  if (this.curChar == ' ') {
                     this.jjAddStates(23, 24);
                  }
                  break;
               case 20:
                  if (this.curChar == '\n') {
                     this.jjCheckNAddStates(131, 133);
                  }
                  break;
               case 21:
                  if (this.curChar == '\'' && kind > 27) {
                     kind = 27;
                  }
                  break;
               case 23:
                  if ((287984085547089920L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 23;
                  }
                  break;
               case 26:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 28:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(29, 30);
                  }
                  break;
               case 30:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 31:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(29, 30);
                  }
                  break;
               case 32:
                  if (this.curChar == '-') {
                     this.jjCheckNAddStates(127, 130);
                  }
                  break;
               case 33:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddTwoStates(33, 35);
                  }
                  break;
               case 34:
                  if (this.curChar == '.' && kind > 52) {
                     kind = 52;
                  }
                  break;
               case 35:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 34;
                  }
                  break;
               case 36:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(36, 37);
                  }
                  break;
               case 37:
                  if (this.curChar == '.') {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(38, 39);
                  }
                  break;
               case 38:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(38, 39);
                  }
                  break;
               case 40:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(41);
                  }
                  break;
               case 41:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(41);
                  }
                  break;
               case 42:
                  if (this.curChar == '.') {
                     this.jjCheckNAdd(43);
                  }
                  break;
               case 43:
               case 53:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAddTwoStates(43, 44);
                  }
                  break;
               case 45:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(46);
                  }
                  break;
               case 46:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(46);
                  }
                  break;
               case 47:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(47, 48);
                  }
                  break;
               case 49:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(50);
                  }
                  break;
               case 50:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 53) {
                        kind = 53;
                     }

                     this.jjCheckNAdd(50);
                  }
                  break;
               case 51:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 52) {
                        kind = 52;
                     }

                     this.jjCheckNAddStates(121, 126);
                  }
                  break;
               case 52:
                  if (this.curChar == '.') {
                     this.jjCheckNAddTwoStates(43, 53);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 4:
               case 5:
               case 7:
               case 10:
               case 11:
               case 12:
               case 13:
               case 14:
               case 15:
               case 16:
               case 19:
               case 20:
               case 21:
               case 26:
               case 28:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 35:
               case 36:
               case 37:
               case 38:
               case 40:
               case 41:
               case 42:
               case 43:
               case 45:
               case 46:
               case 47:
               case 49:
               case 50:
               case 51:
               case 52:
               default:
                  break;
               case 3:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(23);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(141, 144);
                  }
                  break;
               case 6:
                  if ((-268435457L & l) != 0L) {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 8:
                  if (this.curChar == '\\') {
                     this.jjAddStates(145, 149);
                  }
                  break;
               case 9:
                  if ((5700160604602368L & l) != 0L) {
                     this.jjCheckNAddStates(134, 136);
                  }
                  break;
               case 17:
                  this.jjAddStates(131, 133);
                  break;
               case 18:
                  if (this.curChar == '\\') {
                     this.jjAddStates(23, 24);
                  }
                  break;
               case 22:
               case 23:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(23);
                  }
                  break;
               case 24:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(141, 144);
                  }
                  break;
               case 25:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(25, 26);
                  }
                  break;
               case 27:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(27, 28);
                  }
                  break;
               case 29:
                  if (this.curChar == '\\') {
                     this.jjAddStates(150, 151);
                  }
                  break;
               case 39:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(152, 153);
                  }
                  break;
               case 44:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(154, 155);
                  }
                  break;
               case 48:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(156, 157);
                  }
                  break;
               case 53:
                  if ((576460743847706622L & l) != 0L && kind > 63) {
                     kind = 63;
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 6:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                     this.jjAddStates(134, 136);
                  }
                  break;
               case 17:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
                     this.jjAddStates(131, 133);
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 54 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private final int jjStopStringLiteralDfa_2(int pos, long active0, long active1) {
      switch(pos) {
      case 0:
         if ((active0 & 458752L) != 0L) {
            return 2;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               return 5;
            }

            return -1;
         }
      case 1:
         if ((active0 & 65536L) != 0L) {
            return 0;
         } else {
            if ((active0 & 805306368L) != 0L) {
               this.jjmatchedKind = 62;
               this.jjmatchedPos = 1;
               return 5;
            }

            return -1;
         }
      case 2:
         if ((active0 & 805306368L) != 0L) {
            this.jjmatchedKind = 62;
            this.jjmatchedPos = 2;
            return 5;
         }

         return -1;
      case 3:
         if ((active0 & 536870912L) != 0L) {
            this.jjmatchedKind = 62;
            this.jjmatchedPos = 3;
            return 5;
         } else {
            if ((active0 & 268435456L) != 0L) {
               return 5;
            }

            return -1;
         }
      default:
         return -1;
      }
   }

   private final int jjStartNfa_2(int pos, long active0, long active1) {
      return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(pos, active0, active1), pos + 1);
   }

   private final int jjStartNfaWithStates_2(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_2(state, pos + 1);
   }

   private final int jjMoveStringLiteralDfa0_2() {
      switch(this.curChar) {
      case '#':
         this.jjmatchedKind = 17;
         return this.jjMoveStringLiteralDfa1_2(327680L);
      case '(':
         return this.jjStopAtPos(0, 8);
      case 'f':
         return this.jjMoveStringLiteralDfa1_2(536870912L);
      case 't':
         return this.jjMoveStringLiteralDfa1_2(268435456L);
      case '{':
         return this.jjStopAtPos(0, 64);
      case '}':
         return this.jjStopAtPos(0, 65);
      default:
         return this.jjMoveNfa_2(3, 0);
      }
   }

   private final int jjMoveStringLiteralDfa1_2(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_2(0, active0, 0L);
         return 1;
      }

      switch(this.curChar) {
      case '#':
         if ((active0 & 262144L) != 0L) {
            return this.jjStopAtPos(1, 18);
         }
         break;
      case '*':
         if ((active0 & 65536L) != 0L) {
            return this.jjStartNfaWithStates_2(1, 16, 0);
         }
         break;
      case 'a':
         return this.jjMoveStringLiteralDfa2_2(active0, 536870912L);
      case 'r':
         return this.jjMoveStringLiteralDfa2_2(active0, 268435456L);
      }

      return this.jjStartNfa_2(0, active0, 0L);
   }

   private final int jjMoveStringLiteralDfa2_2(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_2(0, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_2(1, active0, 0L);
            return 2;
         }

         switch(this.curChar) {
         case 'l':
            return this.jjMoveStringLiteralDfa3_2(active0, 536870912L);
         case 'u':
            return this.jjMoveStringLiteralDfa3_2(active0, 268435456L);
         default:
            return this.jjStartNfa_2(1, active0, 0L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa3_2(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_2(1, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_2(2, active0, 0L);
            return 3;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_2(3, 28, 5);
            }
         default:
            return this.jjStartNfa_2(2, active0, 0L);
         case 's':
            return this.jjMoveStringLiteralDfa4_2(active0, 536870912L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa4_2(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_2(2, old0, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_2(3, active0, 0L);
            return 4;
         }

         switch(this.curChar) {
         case 'e':
            if ((active0 & 536870912L) != 0L) {
               return this.jjStartNfaWithStates_2(4, 29, 5);
            }
         default:
            return this.jjStartNfa_2(3, active0, 0L);
         }
      }
   }

   private final int jjMoveNfa_2(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 16;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < '@') {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }
                  break;
               case 1:
                  if ((-34359738369L & l) != 0L && kind > 15) {
                     kind = 15;
                  }
                  break;
               case 2:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 0;
                  }
                  break;
               case 3:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(13, 14);
                  } else if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
               case 4:
               case 7:
               case 8:
               case 9:
               case 11:
               case 13:
               default:
                  break;
               case 5:
                  if ((287984085547089920L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjstateSet[this.jjnewStateCnt++] = 5;
                  }
                  break;
               case 6:
                  if (this.curChar == '.') {
                     this.jjstateSet[this.jjnewStateCnt++] = 7;
                  }
                  break;
               case 10:
                  if (this.curChar == '$' && kind > 13) {
                     kind = 13;
                  }
                  break;
               case 12:
                  if (this.curChar == '$') {
                     this.jjCheckNAddTwoStates(13, 14);
                  }
                  break;
               case 14:
                  if (this.curChar == '!' && kind > 14) {
                     kind = 14;
                  }
                  break;
               case 15:
                  if (this.curChar == '$') {
                     if (kind > 13) {
                        kind = 13;
                     }

                     this.jjCheckNAddTwoStates(13, 14);
                  }
               }
            } while(i != startsAt);
         } else if (this.curChar < 128) {
            l = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (kind > 15) {
                     kind = 15;
                  }
               case 2:
               case 6:
               case 10:
               case 12:
               default:
                  break;
               case 3:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(5);
                  } else if (this.curChar == '\\') {
                     this.jjCheckNAddStates(117, 120);
                  }
                  break;
               case 4:
               case 5:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 62) {
                        kind = 62;
                     }

                     this.jjCheckNAdd(5);
                  }
                  break;
               case 7:
                  if ((576460743847706622L & l) != 0L && kind > 63) {
                     kind = 63;
                  }
                  break;
               case 8:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddStates(117, 120);
                  }
                  break;
               case 9:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(9, 10);
                  }
                  break;
               case 11:
                  if (this.curChar == '\\') {
                     this.jjCheckNAddTwoStates(11, 12);
                  }
                  break;
               case 13:
                  if (this.curChar == '\\') {
                     this.jjAddStates(92, 93);
                  }
               }
            } while(i != startsAt);
         } else {
            int hiByte = this.curChar >> 8;
            int i1 = hiByte >> 6;
            long l1 = 1L << (hiByte & 63);
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & 63);

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 15) {
                     kind = 15;
                  }
               }
            } while(i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         ++curPos;
         if ((i = this.jjnewStateCnt) == (startsAt = 16 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return curPos;
         }
      }
   }

   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
      switch(hiByte) {
      case 0:
         return (jjbitVec2[i2] & l2) != 0L;
      default:
         return (jjbitVec0[i1] & l1) != 0L;
      }
   }

   public ParserTokenManager(CharStream stream) {
      this.fileDepth = 0;
      this.lparen = 0;
      this.rparen = 0;
      this.stateStack = new Stack();
      this.debugPrint = false;
      this.debugStream = System.out;
      this.jjrounds = new int[96];
      this.jjstateSet = new int[192];
      this.curLexState = 3;
      this.defaultLexState = 3;
      this.input_stream = stream;
   }

   public ParserTokenManager(CharStream stream, int lexState) {
      this(stream);
      this.SwitchTo(lexState);
   }

   public void ReInit(CharStream stream) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = stream;
      this.ReInitRounds();
   }

   private final void ReInitRounds() {
      this.jjround = -2147483647;

      for(int i = 96; i-- > 0; this.jjrounds[i] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(CharStream stream, int lexState) {
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void SwitchTo(int lexState) {
      if (lexState < 9 && lexState >= 0) {
         this.curLexState = lexState;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      Token t = Token.newToken(this.jjmatchedKind);
      t.kind = this.jjmatchedKind;
      String im = jjstrLiteralImages[this.jjmatchedKind];
      t.image = im == null ? this.input_stream.GetImage() : im;
      t.beginLine = this.input_stream.getBeginLine();
      t.beginColumn = this.input_stream.getBeginColumn();
      t.endLine = this.input_stream.getEndLine();
      t.endColumn = this.input_stream.getEndColumn();
      return t;
   }

   public Token getNextToken() {
      Token specialToken = null;
      int curPos = 0;

      label132:
      while(true) {
         Token matchedToken;
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (IOException var9) {
            this.jjmatchedKind = 0;
            matchedToken = this.jjFillToken();
            matchedToken.specialToken = specialToken;
            return matchedToken;
         }

         this.image = null;
         this.jjimageLen = 0;

         while(true) {
            switch(this.curLexState) {
            case 0:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_0();
               break;
            case 1:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_1();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 66) {
                  this.jjmatchedKind = 66;
               }
               break;
            case 2:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_2();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 66) {
                  this.jjmatchedKind = 66;
               }
               break;
            case 3:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_3();
               break;
            case 4:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_4();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 67) {
                  this.jjmatchedKind = 67;
               }
               break;
            case 5:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_5();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 66) {
                  this.jjmatchedKind = 66;
               }
               break;
            case 6:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_6();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 25) {
                  this.jjmatchedKind = 25;
               }
               break;
            case 7:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_7();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 25) {
                  this.jjmatchedKind = 25;
               }
               break;
            case 8:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_8();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 25) {
                  this.jjmatchedKind = 25;
               }
            }

            if (this.jjmatchedKind == Integer.MAX_VALUE) {
               break label132;
            }

            if (this.jjmatchedPos + 1 < curPos) {
               this.input_stream.backup(curPos - this.jjmatchedPos - 1);
            }

            if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               matchedToken = this.jjFillToken();
               matchedToken.specialToken = specialToken;
               this.TokenLexicalActions(matchedToken);
               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }

               return matchedToken;
            }

            if ((jjtoSkip[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
               if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
                  matchedToken = this.jjFillToken();
                  if (specialToken == null) {
                     specialToken = matchedToken;
                  } else {
                     matchedToken.specialToken = specialToken;
                     specialToken = specialToken.next = matchedToken;
                  }

                  this.SkipLexicalActions(matchedToken);
               } else {
                  this.SkipLexicalActions((Token)null);
               }

               if (jjnewLexState[this.jjmatchedKind] != -1) {
                  this.curLexState = jjnewLexState[this.jjmatchedKind];
               }
               break;
            }

            this.MoreLexicalActions();
            if (jjnewLexState[this.jjmatchedKind] != -1) {
               this.curLexState = jjnewLexState[this.jjmatchedKind];
            }

            curPos = 0;
            this.jjmatchedKind = Integer.MAX_VALUE;

            try {
               this.curChar = this.input_stream.readChar();
            } catch (IOException var11) {
               break label132;
            }
         }
      }

      int error_line = this.input_stream.getEndLine();
      int error_column = this.input_stream.getEndColumn();
      String error_after = null;
      boolean EOFSeen = false;

      try {
         this.input_stream.readChar();
         this.input_stream.backup(1);
      } catch (IOException var10) {
         EOFSeen = true;
         error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
         if (this.curChar != '\n' && this.curChar != '\r') {
            ++error_column;
         } else {
            ++error_line;
            error_column = 0;
         }
      }

      if (!EOFSeen) {
         this.input_stream.backup(1);
         error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
      }

      throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
   }

   void SkipLexicalActions(Token matchedToken) {
      switch(this.jjmatchedKind) {
      case 66:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.input_stream.backup(1);
         this.inReference = false;
         if (this.debugPrint) {
            System.out.print("REF_TERM :");
         }

         this.stateStackPop();
         break;
      case 67:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (this.debugPrint) {
            System.out.print("DIRECTIVE_TERM :");
         }

         this.input_stream.backup(1);
         this.inDirective = false;
         this.stateStackPop();
      }

   }

   void MoreLexicalActions() {
      this.jjimageLen += this.lengthOfMatch = this.jjmatchedPos + 1;
      switch(this.jjmatchedKind) {
      case 13:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         if (!this.inComment) {
            if (this.curLexState == 5) {
               this.inReference = false;
               this.stateStackPop();
            }

            this.inReference = true;
            if (this.debugPrint) {
               System.out.print("$  : going to 5");
            }

            this.stateStackPush();
            this.SwitchTo(5);
         }
         break;
      case 14:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         if (!this.inComment) {
            if (this.curLexState == 5) {
               this.inReference = false;
               this.stateStackPop();
            }

            this.inReference = true;
            if (this.debugPrint) {
               System.out.print("$!  : going to 5");
            }

            this.stateStackPush();
            this.SwitchTo(5);
         }
         break;
      case 15:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         this.input_stream.backup(1);
         this.inComment = true;
         this.stateStackPush();
         this.SwitchTo(7);
         break;
      case 16:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         this.inComment = true;
         this.stateStackPush();
         this.SwitchTo(6);
         break;
      case 17:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         if (!this.inComment) {
            if (this.curLexState == 5 || this.curLexState == 2) {
               this.inReference = false;
               this.stateStackPop();
            }

            this.inDirective = true;
            if (this.debugPrint) {
               System.out.print("# :  going to 0");
            }

            this.stateStackPush();
            this.SwitchTo(4);
         }
      }

   }

   void TokenLexicalActions(Token matchedToken) {
      switch(this.jjmatchedKind) {
      case 8:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (!this.inComment) {
            ++this.lparen;
         }

         if (this.curLexState == 2) {
            this.SwitchTo(1);
         }
         break;
      case 9:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.RPARENHandler();
         break;
      case 10:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.SwitchTo(5);
      case 11:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 19:
      case 20:
      case 21:
      case 25:
      case 26:
      case 28:
      case 29:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 51:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 64:
      default:
         break;
      case 12:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (!this.inComment) {
            this.inDirective = true;
            if (this.debugPrint) {
               System.out.print("#set :  going to 0");
            }

            this.stateStackPush();
            this.inSet = true;
            this.SwitchTo(0);
         }

         if (!this.inComment) {
            ++this.lparen;
            if (this.curLexState == 2) {
               this.SwitchTo(1);
            }
         }
         break;
      case 18:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (!this.inComment) {
            if (this.curLexState == 5) {
               this.inReference = false;
               this.stateStackPop();
            }

            this.inComment = true;
            this.stateStackPush();
            this.SwitchTo(8);
         }
         break;
      case 22:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inComment = false;
         this.stateStackPop();
         break;
      case 23:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inComment = false;
         this.stateStackPop();
         break;
      case 24:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inComment = false;
         this.stateStackPop();
         break;
      case 27:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (this.curLexState == 0 && !this.inSet && this.lparen == 0) {
            this.stateStackPop();
         }
         break;
      case 30:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (this.debugPrint) {
            System.out.println(" NEWLINE :");
         }

         this.stateStackPop();
         if (this.inSet) {
            this.inSet = false;
         }

         if (this.inDirective) {
            this.inDirective = false;
         }
         break;
      case 46:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inDirective = false;
         this.stateStackPop();
         break;
      case 47:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.SwitchTo(0);
         break;
      case 48:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.SwitchTo(0);
         break;
      case 49:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inDirective = false;
         this.stateStackPop();
         break;
      case 50:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.inDirective = false;
         this.stateStackPop();
         break;
      case 52:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (matchedToken.image.endsWith("..")) {
            this.input_stream.backup(2);
            matchedToken.image = matchedToken.image.substring(0, matchedToken.image.length() - 2);
         }

         if (this.lparen == 0 && !this.inSet && this.curLexState != 1) {
            this.stateStackPop();
         }
         break;
      case 53:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         if (this.lparen == 0 && !this.inSet && this.curLexState != 1) {
            this.stateStackPop();
         }
         break;
      case 63:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.input_stream.backup(1);
         matchedToken.image = ".";
         if (this.debugPrint) {
            System.out.print("DOT : switching to 2");
         }

         this.SwitchTo(2);
         break;
      case 65:
         if (this.image == null) {
            this.image = new StringBuffer();
         }

         this.image.append(this.input_stream.GetSuffix(this.jjimageLen + (this.lengthOfMatch = this.jjmatchedPos + 1)));
         this.stateStackPop();
      }

   }
}
