package bsh;

import java.io.IOException;
import java.io.PrintStream;

public class ParserTokenManager implements ParserConstants {
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final long[] jjbitVec1 = new long[]{-2L, -1L, -1L, -1L};
   static final long[] jjbitVec3 = new long[]{2301339413881290750L, -16384L, 4294967295L, 432345564227567616L};
   static final long[] jjbitVec4 = new long[]{0L, 0L, 0L, -36028797027352577L};
   static final long[] jjbitVec5 = new long[]{0L, -1L, -1L, -1L};
   static final long[] jjbitVec6 = new long[]{-1L, -1L, 65535L, 0L};
   static final long[] jjbitVec7 = new long[]{-1L, -1L, 0L, 0L};
   static final long[] jjbitVec8 = new long[]{70368744177663L, 0L, 0L, 0L};
   static final int[] jjnextStates = new int[]{37, 38, 43, 44, 47, 48, 15, 56, 61, 73, 26, 27, 29, 17, 19, 52, 54, 9, 57, 58, 60, 2, 3, 5, 11, 12, 15, 26, 27, 31, 29, 39, 40, 15, 47, 48, 15, 63, 64, 66, 69, 70, 72, 13, 14, 20, 21, 23, 28, 30, 32, 41, 42, 45, 46, 49, 50};
   public static final String[] jjstrLiteralImages = new String[]{"", null, null, null, null, null, null, null, null, null, "abstract", "boolean", "break", "class", "byte", "case", "catch", "char", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "switch", "synchronized", "transient", "throw", "throws", "true", "try", "void", "volatile", "while", null, null, null, null, null, null, null, null, null, null, null, null, "(", ")", "{", "}", "[", "]", ";", ",", ".", "=", ">", "@gt", "<", "@lt", "!", "~", "?", ":", "==", "<=", "@lteq", ">=", "@gteq", "!=", "||", "@or", "&&", "@and", "++", "--", "+", "-", "*", "/", "&", "@bitwise_and", "|", "@bitwise_or", "^", "%", "<<", "@left_shift", ">>", "@right_shift", ">>>", "@right_unsigned_shift", "+=", "-=", "*=", "/=", "&=", "@and_assign", "|=", "@or_assign", "^=", "%=", "<<=", "@left_shift_assign", ">>=", "@right_shift_assign", ">>>=", "@right_unsigned_shift_assign"};
   public static final String[] lexStateNames = new String[]{"DEFAULT"};
   static final long[] jjtoToken = new long[]{2305843009213692929L, -195L, 63L};
   static final long[] jjtoSkip = new long[]{1022L, 0L, 0L};
   static final long[] jjtoSpecial = new long[]{896L, 0L, 0L};
   protected JavaCharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   protected char curChar;
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream var1) {
      this.debugStream = var1;
   }

   private final int jjStopStringLiteralDfa_0(int var1, long var2, long var4, long var6) {
      switch(var1) {
      case 0:
         if ((var4 & 144117387099111424L) != 0L) {
            return 56;
         } else if ((var2 & 62L) != 0L) {
            return 0;
         } else if ((var4 & 65536L) != 0L) {
            return 11;
         } else {
            if ((var2 & 1152921504606845952L) != 0L) {
               this.jjmatchedKind = 69;
               return 35;
            }

            return -1;
         }
      case 1:
         if ((var2 & 4301258752L) != 0L) {
            return 35;
         } else {
            if ((var2 & 1152921500305587200L) != 0L) {
               if (this.jjmatchedPos != 1) {
                  this.jjmatchedKind = 69;
                  this.jjmatchedPos = 1;
               }

               return 35;
            }

            return -1;
         }
      case 2:
         if ((var2 & 1080862599528053760L) != 0L) {
            if (this.jjmatchedPos != 2) {
               this.jjmatchedKind = 69;
               this.jjmatchedPos = 2;
            }

            return 35;
         } else {
            if ((var2 & 72058900781727744L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 3:
         if ((var2 & 900716275798195200L) != 0L) {
            if (this.jjmatchedPos != 3) {
               this.jjmatchedKind = 69;
               this.jjmatchedPos = 3;
            }

            return 35;
         } else {
            if ((var2 & 180146461168812032L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 4:
         if ((var2 & 603623088562974720L) != 0L) {
            return 35;
         } else {
            if ((var2 & 297093187235220480L) != 0L) {
               if (this.jjmatchedPos != 4) {
                  this.jjmatchedKind = 69;
                  this.jjmatchedPos = 4;
               }

               return 35;
            }

            return -1;
         }
      case 5:
         if ((var2 & 295579692563958784L) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 5;
            return 35;
         } else {
            if ((var2 & 19527893449179136L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 6:
         if ((var2 & 295566498121384960L) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 6;
            return 35;
         } else {
            if ((var2 & 13194442573824L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 7:
         if ((var2 & 288793326105658368L) != 0L) {
            return 35;
         } else {
            if ((var2 & 6773172015726592L) != 0L) {
               this.jjmatchedKind = 69;
               this.jjmatchedPos = 7;
               return 35;
            }

            return -1;
         }
      case 8:
         if ((var2 & 2251842763358208L) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 8;
            return 35;
         } else {
            if ((var2 & 4521329252368384L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 9:
         if ((var2 & 2251799813685248L) != 0L) {
            this.jjmatchedKind = 69;
            this.jjmatchedPos = 9;
            return 35;
         } else {
            if ((var2 & 42949672960L) != 0L) {
               return 35;
            }

            return -1;
         }
      case 10:
         if ((var2 & 2251799813685248L) != 0L) {
            if (this.jjmatchedPos != 10) {
               this.jjmatchedKind = 69;
               this.jjmatchedPos = 10;
            }

            return 35;
         }

         return -1;
      case 11:
         if ((var2 & 2251799813685248L) != 0L) {
            return 35;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_0(int var1, long var2, long var4, long var6) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(var1, var2, var4, var6), var1 + 1);
   }

   private final int jjStopAtPos(int var1, int var2) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;
      return var1 + 1;
   }

   private final int jjStartNfaWithStates_0(int var1, int var2, int var3) {
      this.jjmatchedKind = var2;
      this.jjmatchedPos = var1;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return var1 + 1;
      }

      return this.jjMoveNfa_0(var3, var1 + 1);
   }

   private final int jjMoveStringLiteralDfa0_0() {
      switch(this.curChar) {
      case '\t':
         return this.jjStartNfaWithStates_0(0, 2, 0);
      case '\n':
         return this.jjStartNfaWithStates_0(0, 5, 0);
      case '\u000b':
      case '\u000e':
      case '\u000f':
      case '\u0010':
      case '\u0011':
      case '\u0012':
      case '\u0013':
      case '\u0014':
      case '\u0015':
      case '\u0016':
      case '\u0017':
      case '\u0018':
      case '\u0019':
      case '\u001a':
      case '\u001b':
      case '\u001c':
      case '\u001d':
      case '\u001e':
      case '\u001f':
      case '"':
      case '#':
      case '$':
      case '\'':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '\\':
      case '_':
      case '`':
      case 'h':
      case 'j':
      case 'k':
      case 'm':
      case 'o':
      case 'q':
      case 'u':
      case 'x':
      case 'y':
      case 'z':
      default:
         return this.jjMoveNfa_0(6, 0);
      case '\f':
         return this.jjStartNfaWithStates_0(0, 4, 0);
      case '\r':
         return this.jjStartNfaWithStates_0(0, 3, 0);
      case ' ':
         return this.jjStartNfaWithStates_0(0, 1, 0);
      case '!':
         this.jjmatchedKind = 86;
         return this.jjMoveStringLiteralDfa1_0(0L, 2147483648L, 0L);
      case '%':
         this.jjmatchedKind = 111;
         return this.jjMoveStringLiteralDfa1_0(0L, Long.MIN_VALUE, 0L);
      case '&':
         this.jjmatchedKind = 106;
         return this.jjMoveStringLiteralDfa1_0(0L, 288230393331580928L, 0L);
      case '(':
         return this.jjStopAtPos(0, 72);
      case ')':
         return this.jjStopAtPos(0, 73);
      case '*':
         this.jjmatchedKind = 104;
         return this.jjMoveStringLiteralDfa1_0(0L, 72057594037927936L, 0L);
      case '+':
         this.jjmatchedKind = 102;
         return this.jjMoveStringLiteralDfa1_0(0L, 18014467228958720L, 0L);
      case ',':
         return this.jjStopAtPos(0, 79);
      case '-':
         this.jjmatchedKind = 103;
         return this.jjMoveStringLiteralDfa1_0(0L, 36028934457917440L, 0L);
      case '.':
         return this.jjStartNfaWithStates_0(0, 80, 11);
      case '/':
         this.jjmatchedKind = 105;
         return this.jjMoveStringLiteralDfa1_0(0L, 144115188075855872L, 0L);
      case ':':
         return this.jjStopAtPos(0, 89);
      case ';':
         return this.jjStopAtPos(0, 78);
      case '<':
         this.jjmatchedKind = 84;
         return this.jjMoveStringLiteralDfa1_0(0L, 281475110928384L, 1L);
      case '=':
         this.jjmatchedKind = 81;
         return this.jjMoveStringLiteralDfa1_0(0L, 67108864L, 0L);
      case '>':
         this.jjmatchedKind = 82;
         return this.jjMoveStringLiteralDfa1_0(0L, 5629500071084032L, 20L);
      case '?':
         return this.jjStopAtPos(0, 88);
      case '@':
         return this.jjMoveStringLiteralDfa1_0(0L, 2894169735298547712L, 42L);
      case '[':
         return this.jjStopAtPos(0, 76);
      case ']':
         return this.jjStopAtPos(0, 77);
      case '^':
         this.jjmatchedKind = 110;
         return this.jjMoveStringLiteralDfa1_0(0L, 4611686018427387904L, 0L);
      case 'a':
         return this.jjMoveStringLiteralDfa1_0(1024L, 0L, 0L);
      case 'b':
         return this.jjMoveStringLiteralDfa1_0(22528L, 0L, 0L);
      case 'c':
         return this.jjMoveStringLiteralDfa1_0(1024000L, 0L, 0L);
      case 'd':
         return this.jjMoveStringLiteralDfa1_0(7340032L, 0L, 0L);
      case 'e':
         return this.jjMoveStringLiteralDfa1_0(58720256L, 0L, 0L);
      case 'f':
         return this.jjMoveStringLiteralDfa1_0(2080374784L, 0L, 0L);
      case 'g':
         return this.jjMoveStringLiteralDfa1_0(2147483648L, 0L, 0L);
      case 'i':
         return this.jjMoveStringLiteralDfa1_0(270582939648L, 0L, 0L);
      case 'l':
         return this.jjMoveStringLiteralDfa1_0(274877906944L, 0L, 0L);
      case 'n':
         return this.jjMoveStringLiteralDfa1_0(3848290697216L, 0L, 0L);
      case 'p':
         return this.jjMoveStringLiteralDfa1_0(65970697666560L, 0L, 0L);
      case 'r':
         return this.jjMoveStringLiteralDfa1_0(70368744177664L, 0L, 0L);
      case 's':
         return this.jjMoveStringLiteralDfa1_0(4362862139015168L, 0L, 0L);
      case 't':
         return this.jjMoveStringLiteralDfa1_0(139611588448485376L, 0L, 0L);
      case 'v':
         return this.jjMoveStringLiteralDfa1_0(432345564227567616L, 0L, 0L);
      case 'w':
         return this.jjMoveStringLiteralDfa1_0(576460752303423488L, 0L, 0L);
      case '{':
         return this.jjStopAtPos(0, 74);
      case '|':
         this.jjmatchedKind = 108;
         return this.jjMoveStringLiteralDfa1_0(0L, 1152921508901814272L, 0L);
      case '}':
         return this.jjStopAtPos(0, 75);
      case '~':
         return this.jjStopAtPos(0, 87);
      }
   }

   private final int jjMoveStringLiteralDfa1_0(long var1, long var3, long var5) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var8) {
         this.jjStopStringLiteralDfa_0(0, var1, var3, var5);
         return 1;
      }

      switch(this.curChar) {
      case '&':
         if ((var3 & 17179869184L) != 0L) {
            return this.jjStopAtPos(1, 98);
         }
      case '\'':
      case '(':
      case ')':
      case '*':
      case ',':
      case '.':
      case '/':
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
      case ':':
      case ';':
      case '?':
      case '@':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '[':
      case '\\':
      case ']':
      case '^':
      case '_':
      case '`':
      case 'c':
      case 'd':
      case 'j':
      case 'k':
      case 'p':
      case 'q':
      case 's':
      case 'v':
      case 'z':
      case '{':
      default:
         break;
      case '+':
         if ((var3 & 68719476736L) != 0L) {
            return this.jjStopAtPos(1, 100);
         }
         break;
      case '-':
         if ((var3 & 137438953472L) != 0L) {
            return this.jjStopAtPos(1, 101);
         }
         break;
      case '<':
         if ((var3 & 281474976710656L) != 0L) {
            this.jjmatchedKind = 112;
            this.jjmatchedPos = 1;
         }

         return this.jjMoveStringLiteralDfa2_0(var1, 0L, var3, 0L, var5, 1L);
      case '=':
         if ((var3 & 67108864L) != 0L) {
            return this.jjStopAtPos(1, 90);
         }

         if ((var3 & 134217728L) != 0L) {
            return this.jjStopAtPos(1, 91);
         }

         if ((var3 & 536870912L) != 0L) {
            return this.jjStopAtPos(1, 93);
         }

         if ((var3 & 2147483648L) != 0L) {
            return this.jjStopAtPos(1, 95);
         }

         if ((var3 & 18014398509481984L) != 0L) {
            return this.jjStopAtPos(1, 118);
         }

         if ((var3 & 36028797018963968L) != 0L) {
            return this.jjStopAtPos(1, 119);
         }

         if ((var3 & 72057594037927936L) != 0L) {
            return this.jjStopAtPos(1, 120);
         }

         if ((var3 & 144115188075855872L) != 0L) {
            return this.jjStopAtPos(1, 121);
         }

         if ((var3 & 288230376151711744L) != 0L) {
            return this.jjStopAtPos(1, 122);
         }

         if ((var3 & 1152921504606846976L) != 0L) {
            return this.jjStopAtPos(1, 124);
         }

         if ((var3 & 4611686018427387904L) != 0L) {
            return this.jjStopAtPos(1, 126);
         }

         if ((var3 & Long.MIN_VALUE) != 0L) {
            return this.jjStopAtPos(1, 127);
         }
         break;
      case '>':
         if ((var3 & 1125899906842624L) != 0L) {
            this.jjmatchedKind = 114;
            this.jjmatchedPos = 1;
         }

         return this.jjMoveStringLiteralDfa2_0(var1, 0L, var3, 4503599627370496L, var5, 20L);
      case 'a':
         return this.jjMoveStringLiteralDfa2_0(var1, 4947869532160L, var3, 576460786663161856L, var5, 0L);
      case 'b':
         return this.jjMoveStringLiteralDfa2_0(var1, 1024L, var3, 43980465111040L, var5, 0L);
      case 'e':
         return this.jjMoveStringLiteralDfa2_0(var1, 71468256854016L, var3, 0L, var5, 0L);
      case 'f':
         if ((var1 & 4294967296L) != 0L) {
            return this.jjStartNfaWithStates_0(1, 32, 35);
         }
         break;
      case 'g':
         return this.jjMoveStringLiteralDfa2_0(var1, 0L, var3, 1074266112L, var5, 0L);
      case 'h':
         return this.jjMoveStringLiteralDfa2_0(var1, 603623087556132864L, var3, 0L, var5, 0L);
      case 'i':
         return this.jjMoveStringLiteralDfa2_0(var1, 402653184L, var3, 0L, var5, 0L);
      case 'l':
         return this.jjMoveStringLiteralDfa2_0(var1, 545267712L, var3, 562950223953920L, var5, 2L);
      case 'm':
         return this.jjMoveStringLiteralDfa2_0(var1, 25769803776L, var3, 0L, var5, 0L);
      case 'n':
         return this.jjMoveStringLiteralDfa2_0(var1, 240534945792L, var3, 0L, var5, 0L);
      case 'o':
         if ((var1 & 2097152L) != 0L) {
            this.jjmatchedKind = 21;
            this.jjmatchedPos = 1;
         }

         return this.jjMoveStringLiteralDfa2_0(var1, 432345842331682816L, var3, 2305843017803628544L, var5, 0L);
      case 'r':
         return this.jjMoveStringLiteralDfa2_0(var1, 112616378963333120L, var3, 11258999068426240L, var5, 40L);
      case 't':
         return this.jjMoveStringLiteralDfa2_0(var1, 844424930131968L, var3, 0L, var5, 0L);
      case 'u':
         return this.jjMoveStringLiteralDfa2_0(var1, 37383395344384L, var3, 0L, var5, 0L);
      case 'w':
         return this.jjMoveStringLiteralDfa2_0(var1, 1125899906842624L, var3, 0L, var5, 0L);
      case 'x':
         return this.jjMoveStringLiteralDfa2_0(var1, 33554432L, var3, 0L, var5, 0L);
      case 'y':
         return this.jjMoveStringLiteralDfa2_0(var1, 2251799813701632L, var3, 0L, var5, 0L);
      case '|':
         if ((var3 & 4294967296L) != 0L) {
            return this.jjStopAtPos(1, 96);
         }
      }

      return this.jjStartNfa_0(0, var1, var3, var5);
   }

   private final int jjMoveStringLiteralDfa2_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(0, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(1, var3, var7, var11);
            return 2;
         }

         switch(this.curChar) {
         case '=':
            if ((var11 & 1L) != 0L) {
               return this.jjStopAtPos(2, 128);
            }

            if ((var11 & 4L) != 0L) {
               return this.jjStopAtPos(2, 130);
            }
            break;
         case '>':
            if ((var7 & 4503599627370496L) != 0L) {
               this.jjmatchedKind = 116;
               this.jjmatchedPos = 2;
            }

            return this.jjMoveStringLiteralDfa3_0(var3, 0L, var7, 0L, var11, 16L);
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '_':
         case '`':
         case 'd':
         case 'g':
         case 'h':
         case 'j':
         case 'k':
         case 'm':
         case 'q':
         case 'v':
         case 'x':
         default:
            break;
         case 'a':
            return this.jjMoveStringLiteralDfa3_0(var3, 4785074604220416L, var7, 0L, var11, 0L);
         case 'b':
            return this.jjMoveStringLiteralDfa3_0(var3, 35184372088832L, var7, 0L, var11, 0L);
         case 'c':
            return this.jjMoveStringLiteralDfa3_0(var3, 4398046511104L, var7, 0L, var11, 0L);
         case 'e':
            return this.jjMoveStringLiteralDfa3_0(var3, 4096L, var7, 562949953421312L, var11, 2L);
         case 'f':
            return this.jjMoveStringLiteralDfa3_0(var3, 1048576L, var7, 0L, var11, 0L);
         case 'i':
            return this.jjMoveStringLiteralDfa3_0(var3, 721710636379144192L, var7, 11302979533537280L, var11, 40L);
         case 'l':
            return this.jjMoveStringLiteralDfa3_0(var3, 288232575242076160L, var7, 0L, var11, 0L);
         case 'n':
            return this.jjMoveStringLiteralDfa3_0(var3, 2252075095031808L, var7, 576460786663161856L, var11, 0L);
         case 'o':
            return this.jjMoveStringLiteralDfa3_0(var3, 158330211272704L, var7, 0L, var11, 0L);
         case 'p':
            return this.jjMoveStringLiteralDfa3_0(var3, 25769803776L, var7, 0L, var11, 0L);
         case 'r':
            if ((var3 & 1073741824L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 30, 35);
            }

            if ((var7 & 8589934592L) != 0L) {
               this.jjmatchedKind = 97;
               this.jjmatchedPos = 2;
            }

            return this.jjMoveStringLiteralDfa3_0(var3, 27584547717644288L, var7, 2305843009213693952L, var11, 0L);
         case 's':
            return this.jjMoveStringLiteralDfa3_0(var3, 34368160768L, var7, 0L, var11, 0L);
         case 't':
            if ((var3 & 68719476736L) != 0L) {
               this.jjmatchedKind = 36;
               this.jjmatchedPos = 2;
            } else if ((var7 & 524288L) != 0L) {
               this.jjmatchedKind = 83;
               this.jjmatchedPos = 2;
            } else if ((var7 & 2097152L) != 0L) {
               this.jjmatchedKind = 85;
               this.jjmatchedPos = 2;
            }

            return this.jjMoveStringLiteralDfa3_0(var3, 71058120065024L, var7, 1342177280L, var11, 0L);
         case 'u':
            return this.jjMoveStringLiteralDfa3_0(var3, 36028797039935488L, var7, 0L, var11, 0L);
         case 'w':
            if ((var3 & 1099511627776L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 40, 35);
            }
            break;
         case 'y':
            if ((var3 & 72057594037927936L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 56, 35);
            }
         }

         return this.jjStartNfa_0(1, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa3_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(1, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(2, var3, var7, var11);
            return 3;
         }

         switch(this.curChar) {
         case '=':
            if ((var11 & 16L) != 0L) {
               return this.jjStopAtPos(3, 132);
            }
         case '>':
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '`':
         case 'h':
         case 'j':
         case 'p':
         case 'q':
         default:
            break;
         case '_':
            return this.jjMoveStringLiteralDfa4_0(var3, 0L, var7, 2305843009213693952L, var11, 0L);
         case 'a':
            return this.jjMoveStringLiteralDfa4_0(var3, 288230377092288512L, var7, 0L, var11, 0L);
         case 'b':
            return this.jjMoveStringLiteralDfa4_0(var3, 4194304L, var7, 0L, var11, 0L);
         case 'c':
            return this.jjMoveStringLiteralDfa4_0(var3, 2251799813750784L, var7, 0L, var11, 0L);
         case 'd':
            if ((var3 & 144115188075855872L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 57, 35);
            }

            if ((var7 & 34359738368L) != 0L) {
               this.jjmatchedKind = 99;
               this.jjmatchedPos = 3;
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 0L, var7, 576460752303423488L, var11, 0L);
         case 'e':
            if ((var3 & 16384L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 14, 35);
            }

            if ((var3 & 32768L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 15, 35);
            }

            if ((var3 & 8388608L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 23, 35);
            }

            if ((var3 & 36028797018963968L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 55, 35);
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 137472507904L, var7, 1342177280L, var11, 0L);
         case 'f':
            return this.jjMoveStringLiteralDfa4_0(var3, 0L, var7, 562949953421312L, var11, 2L);
         case 'g':
            if ((var3 & 274877906944L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 38, 35);
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 0L, var7, 11258999068426240L, var11, 40L);
         case 'i':
            return this.jjMoveStringLiteralDfa4_0(var3, 563499709235200L, var7, 0L, var11, 0L);
         case 'k':
            return this.jjMoveStringLiteralDfa4_0(var3, 4398046511104L, var7, 0L, var11, 0L);
         case 'l':
            if ((var3 & 2199023255552L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 41, 35);
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 576495945265448960L, var7, 0L, var11, 0L);
         case 'm':
            if ((var3 & 16777216L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 24, 35);
            }
            break;
         case 'n':
            return this.jjMoveStringLiteralDfa4_0(var3, 4503599627370496L, var7, 0L, var11, 0L);
         case 'o':
            if ((var3 & 2147483648L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 31, 35);
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 27021614944092160L, var7, 0L, var11, 0L);
         case 'r':
            if ((var3 & 131072L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 17, 35);
            }

            return this.jjMoveStringLiteralDfa4_0(var3, 140737488355328L, var7, 0L, var11, 0L);
         case 's':
            return this.jjMoveStringLiteralDfa4_0(var3, 67379200L, var7, 0L, var11, 0L);
         case 't':
            return this.jjMoveStringLiteralDfa4_0(var3, 1425001429861376L, var7, 43980465111040L, var11, 0L);
         case 'u':
            return this.jjMoveStringLiteralDfa4_0(var3, 70368744177664L, var7, 0L, var11, 0L);
         case 'v':
            return this.jjMoveStringLiteralDfa4_0(var3, 8796093022208L, var7, 0L, var11, 0L);
         }

         return this.jjStartNfa_0(2, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa4_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(2, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(3, var3, var7, var11);
            return 4;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa5_0(var3, 0L, var7, 576460752303423488L, var11, 0L);
         case '`':
         case 'b':
         case 'd':
         case 'f':
         case 'g':
         case 'j':
         case 'm':
         case 'o':
         case 'p':
         default:
            break;
         case 'a':
            return this.jjMoveStringLiteralDfa5_0(var3, 13228499271680L, var7, 2305843009213693952L, var11, 0L);
         case 'c':
            return this.jjMoveStringLiteralDfa5_0(var3, 1688849860263936L, var7, 0L, var11, 0L);
         case 'e':
            if ((var3 & 67108864L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 26, 35);
            }

            if ((var3 & 576460752303423488L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 59, 35);
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 17600775981056L, var7, 0L, var11, 0L);
         case 'h':
            if ((var3 & 65536L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 16, 35);
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 2251799813685248L, var7, 11258999068426240L, var11, 40L);
         case 'i':
            return this.jjMoveStringLiteralDfa5_0(var3, 316659349323776L, var7, 0L, var11, 0L);
         case 'k':
            if ((var3 & 4096L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 12, 35);
            }
            break;
         case 'l':
            if ((var3 & 134217728L) != 0L) {
               this.jjmatchedKind = 27;
               this.jjmatchedPos = 4;
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 272629760L, var7, 0L, var11, 0L);
         case 'n':
            return this.jjMoveStringLiteralDfa5_0(var3, 33554432L, var7, 0L, var11, 0L);
         case 'q':
            if ((var7 & 268435456L) != 0L) {
               return this.jjStopAtPos(4, 92);
            }

            if ((var7 & 1073741824L) != 0L) {
               return this.jjStopAtPos(4, 94);
            }
            break;
         case 'r':
            return this.jjMoveStringLiteralDfa5_0(var3, 70523363001344L, var7, 0L, var11, 0L);
         case 's':
            if ((var3 & 8192L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 13, 35);
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 4503599627370496L, var7, 0L, var11, 0L);
         case 't':
            if ((var3 & 262144L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 18, 35);
            }

            if ((var3 & 536870912L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 29, 35);
            }

            if ((var3 & 140737488355328L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 47, 35);
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 288230376151711744L, var7, 562949953421312L, var11, 2L);
         case 'u':
            return this.jjMoveStringLiteralDfa5_0(var3, 1048576L, var7, 0L, var11, 0L);
         case 'v':
            return this.jjMoveStringLiteralDfa5_0(var3, 549755813888L, var7, 0L, var11, 0L);
         case 'w':
            if ((var3 & 9007199254740992L) != 0L) {
               this.jjmatchedKind = 53;
               this.jjmatchedPos = 4;
            }

            return this.jjMoveStringLiteralDfa5_0(var3, 18014398509481984L, var7, 43980465111040L, var11, 0L);
         }

         return this.jjStartNfa_0(3, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa5_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(3, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(4, var3, var7, var11);
            return 5;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa6_0(var3, 0L, var7, 562949953421312L, var11, 2L);
         case '`':
         case 'b':
         case 'j':
         case 'k':
         case 'o':
         case 'p':
         case 'q':
         default:
            break;
         case 'a':
            return this.jjMoveStringLiteralDfa6_0(var3, 3072L, var7, 576460752303423488L, var11, 0L);
         case 'c':
            if ((var3 & 35184372088832L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 45, 35);
            }

            if ((var3 & 281474976710656L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 48, 35);
            }

            return this.jjMoveStringLiteralDfa6_0(var3, 17592186044416L, var7, 0L, var11, 0L);
         case 'd':
            return this.jjMoveStringLiteralDfa6_0(var3, 33554432L, var7, 0L, var11, 0L);
         case 'e':
            if ((var3 & 4194304L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 22, 35);
            }

            if ((var3 & 549755813888L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 39, 35);
            }
            break;
         case 'f':
            return this.jjMoveStringLiteralDfa6_0(var3, 137438953472L, var7, 0L, var11, 0L);
         case 'g':
            return this.jjMoveStringLiteralDfa6_0(var3, 4398046511104L, var7, 0L, var11, 0L);
         case 'h':
            if ((var3 & 1125899906842624L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 50, 35);
            }
            break;
         case 'i':
            return this.jjMoveStringLiteralDfa6_0(var3, 292733975779082240L, var7, 43980465111040L, var11, 0L);
         case 'l':
            return this.jjMoveStringLiteralDfa6_0(var3, 269484032L, var7, 0L, var11, 0L);
         case 'm':
            return this.jjMoveStringLiteralDfa6_0(var3, 8589934592L, var7, 0L, var11, 0L);
         case 'n':
            if ((var3 & 70368744177664L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 46, 35);
            }

            return this.jjMoveStringLiteralDfa6_0(var3, 34360262656L, var7, 0L, var11, 0L);
         case 'r':
            return this.jjMoveStringLiteralDfa6_0(var3, 2251799813685248L, var7, 0L, var11, 0L);
         case 's':
            if ((var3 & 18014398509481984L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 54, 35);
            }

            return this.jjMoveStringLiteralDfa6_0(var3, 0L, var7, 2305843009213693952L, var11, 0L);
         case 't':
            if ((var3 & 17179869184L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 34, 35);
            }

            return this.jjMoveStringLiteralDfa6_0(var3, 571746046443520L, var7, 11258999068426240L, var11, 40L);
         }

         return this.jjStartNfa_0(4, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa6_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(4, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(5, var3, var7, var11);
            return 6;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa7_0(var3, 0L, var7, 11258999068426240L, var11, 40L);
         case '`':
         case 'b':
         case 'd':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'm':
         case 'p':
         case 'q':
         case 'r':
         case 'v':
         case 'w':
         case 'x':
         default:
            break;
         case 'a':
            return this.jjMoveStringLiteralDfa7_0(var3, 137438953472L, var7, 0L, var11, 0L);
         case 'c':
            return this.jjMoveStringLiteralDfa7_0(var3, 34359739392L, var7, 0L, var11, 0L);
         case 'e':
            if ((var3 & 4398046511104L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 42, 35);
            }

            if ((var3 & 8796093022208L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 43, 35);
            }

            return this.jjMoveStringLiteralDfa7_0(var3, 4503608217305088L, var7, 0L, var11, 0L);
         case 'f':
            return this.jjMoveStringLiteralDfa7_0(var3, 562949953421312L, var7, 0L, var11, 0L);
         case 'l':
            return this.jjMoveStringLiteralDfa7_0(var3, 288230376151711744L, var7, 0L, var11, 0L);
         case 'n':
            if ((var3 & 2048L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 11, 35);
            }
            break;
         case 'o':
            return this.jjMoveStringLiteralDfa7_0(var3, 2251799813685248L, var7, 0L, var11, 0L);
         case 's':
            if ((var3 & 33554432L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 25, 35);
            }

            return this.jjMoveStringLiteralDfa7_0(var3, 0L, var7, 2882910691935649792L, var11, 2L);
         case 't':
            if ((var3 & 1048576L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 20, 35);
            }

            return this.jjMoveStringLiteralDfa7_0(var3, 17592186044416L, var7, 0L, var11, 0L);
         case 'u':
            return this.jjMoveStringLiteralDfa7_0(var3, 524288L, var7, 0L, var11, 0L);
         case 'y':
            if ((var3 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 28, 35);
            }
         }

         return this.jjStartNfa_0(5, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa7_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(5, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(6, var3, var7, var11);
            return 7;
         }

         switch(this.curChar) {
         case 'c':
            return this.jjMoveStringLiteralDfa8_0(var3, 137438953472L, var7, 0L, var11, 0L);
         case 'd':
         case 'f':
         case 'g':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'o':
         case 'q':
         case 'r':
         default:
            break;
         case 'e':
            if ((var3 & 524288L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 19, 35);
            }

            if ((var3 & 288230376151711744L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 58, 35);
            }

            return this.jjMoveStringLiteralDfa8_0(var3, 17626545782784L, var7, 43980465111040L, var11, 0L);
         case 'h':
            return this.jjMoveStringLiteralDfa8_0(var3, 0L, var7, 562949953421312L, var11, 2L);
         case 'i':
            return this.jjMoveStringLiteralDfa8_0(var3, 0L, var7, 2305843009213693952L, var11, 0L);
         case 'n':
            return this.jjMoveStringLiteralDfa8_0(var3, 6755408030990336L, var7, 0L, var11, 0L);
         case 'p':
            if ((var3 & 562949953421312L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 49, 35);
            }
            break;
         case 's':
            return this.jjMoveStringLiteralDfa8_0(var3, 0L, var7, 578712552117108736L, var11, 8L);
         case 't':
            if ((var3 & 1024L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 10, 35);
            }
            break;
         case 'u':
            return this.jjMoveStringLiteralDfa8_0(var3, 0L, var7, 9007199254740992L, var11, 32L);
         }

         return this.jjStartNfa_0(6, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa8_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(6, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(7, var3, var7, var11);
            return 8;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa9_0(var3, 0L, var7, 43980465111040L, var11, 0L);
         case '`':
         case 'a':
         case 'b':
         case 'c':
         case 'f':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         default:
            break;
         case 'd':
            if ((var3 & 17592186044416L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 44, 35);
            }
            break;
         case 'e':
            if ((var3 & 137438953472L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 37, 35);
            }
            break;
         case 'g':
            return this.jjMoveStringLiteralDfa9_0(var3, 0L, var7, 2305843009213693952L, var11, 0L);
         case 'h':
            return this.jjMoveStringLiteralDfa9_0(var3, 0L, var7, 2251799813685248L, var11, 8L);
         case 'i':
            return this.jjMoveStringLiteralDfa9_0(var3, 2251799813685248L, var7, 577023702256844800L, var11, 2L);
         case 'n':
            return this.jjMoveStringLiteralDfa9_0(var3, 0L, var7, 9007199254740992L, var11, 32L);
         case 'o':
            return this.jjMoveStringLiteralDfa9_0(var3, 34359738368L, var7, 0L, var11, 0L);
         case 't':
            if ((var3 & 4503599627370496L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 52, 35);
            }

            return this.jjMoveStringLiteralDfa9_0(var3, 8589934592L, var7, 0L, var11, 0L);
         }

         return this.jjStartNfa_0(7, var3, var7, var11);
      }
   }

   private final int jjMoveStringLiteralDfa9_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(7, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(8, var3, var7, var11);
            return 9;
         }

         switch(this.curChar) {
         case 'a':
            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 8796093022208L, var11, 0L);
         case 'f':
            if ((var3 & 34359738368L) != 0L) {
               return this.jjStartNfaWithStates_0(9, 35, 35);
            }

            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 562949953421312L, var11, 2L);
         case 'g':
            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 576460752303423488L, var11, 0L);
         case 'i':
            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 2251799813685248L, var11, 8L);
         case 'n':
            if ((var7 & 2305843009213693952L) != 0L) {
               return this.jjStopAtPos(9, 125);
            }
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'h':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'p':
         case 'q':
         case 'r':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         default:
            return this.jjStartNfa_0(8, var3, var7, var11);
         case 'o':
            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 35184372088832L, var11, 0L);
         case 's':
            if ((var3 & 8589934592L) != 0L) {
               return this.jjStartNfaWithStates_0(9, 33, 35);
            }

            return this.jjMoveStringLiteralDfa10_0(var3, 0L, var7, 9007199254740992L, var11, 32L);
         case 'z':
            return this.jjMoveStringLiteralDfa10_0(var3, 2251799813685248L, var7, 0L, var11, 0L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa10_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(8, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(9, var3, var7, var11);
            return 10;
         }

         switch(this.curChar) {
         case 'e':
            return this.jjMoveStringLiteralDfa11_0(var3, 2251799813685248L, var7, 0L, var11, 0L);
         case 'f':
            return this.jjMoveStringLiteralDfa11_0(var3, 0L, var7, 2251799813685248L, var11, 8L);
         case 'i':
            return this.jjMoveStringLiteralDfa11_0(var3, 0L, var7, 9007199254740992L, var11, 32L);
         case 'n':
            if ((var7 & 576460752303423488L) != 0L) {
               return this.jjStopAtPos(10, 123);
            }

            return this.jjMoveStringLiteralDfa11_0(var3, 0L, var7, 8796093022208L, var11, 0L);
         case 'r':
            if ((var7 & 35184372088832L) != 0L) {
               return this.jjStopAtPos(10, 109);
            }
         case 'g':
         case 'h':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'o':
         case 'p':
         case 'q':
         case 's':
         default:
            return this.jjStartNfa_0(9, var3, var7, var11);
         case 't':
            if ((var7 & 562949953421312L) != 0L) {
               this.jjmatchedKind = 113;
               this.jjmatchedPos = 10;
            }

            return this.jjMoveStringLiteralDfa11_0(var3, 0L, var7, 0L, var11, 2L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa11_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if (((var3 &= var1) | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(9, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(10, var3, var7, var11);
            return 11;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa12_0(var3, 0L, var7, 0L, var11, 2L);
         case 'd':
            if ((var3 & 2251799813685248L) != 0L) {
               return this.jjStartNfaWithStates_0(11, 51, 35);
            } else if ((var7 & 8796093022208L) != 0L) {
               return this.jjStopAtPos(11, 107);
            }
         default:
            return this.jjStartNfa_0(10, var3, var7, var11);
         case 'g':
            return this.jjMoveStringLiteralDfa12_0(var3, 0L, var7, 9007199254740992L, var11, 32L);
         case 't':
            if ((var7 & 2251799813685248L) != 0L) {
               this.jjmatchedKind = 115;
               this.jjmatchedPos = 11;
            }

            return this.jjMoveStringLiteralDfa12_0(var3, 0L, var7, 0L, var11, 8L);
         }
      }
   }

   private final int jjMoveStringLiteralDfa12_0(long var1, long var3, long var5, long var7, long var9, long var11) {
      if ((var3 & var1 | (var7 &= var5) | (var11 &= var9)) == 0L) {
         return this.jjStartNfa_0(10, var1, var5, var9);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(11, 0L, var7, var11);
            return 12;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa13_0(var7, 0L, var11, 8L);
         case 'a':
            return this.jjMoveStringLiteralDfa13_0(var7, 0L, var11, 2L);
         case 'n':
            return this.jjMoveStringLiteralDfa13_0(var7, 9007199254740992L, var11, 32L);
         default:
            return this.jjStartNfa_0(11, 0L, var7, var11);
         }
      }
   }

   private final int jjMoveStringLiteralDfa13_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(11, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(12, 0L, var3, var7);
            return 13;
         }

         switch(this.curChar) {
         case 'a':
            return this.jjMoveStringLiteralDfa14_0(var3, 0L, var7, 8L);
         case 'e':
            return this.jjMoveStringLiteralDfa14_0(var3, 9007199254740992L, var7, 32L);
         case 's':
            return this.jjMoveStringLiteralDfa14_0(var3, 0L, var7, 2L);
         default:
            return this.jjStartNfa_0(12, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa14_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(12, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(13, 0L, var3, var7);
            return 14;
         }

         switch(this.curChar) {
         case 'd':
            return this.jjMoveStringLiteralDfa15_0(var3, 9007199254740992L, var7, 32L);
         case 's':
            return this.jjMoveStringLiteralDfa15_0(var3, 0L, var7, 10L);
         default:
            return this.jjStartNfa_0(13, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa15_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(13, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(14, 0L, var3, var7);
            return 15;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa16_0(var3, 9007199254740992L, var7, 32L);
         case 'i':
            return this.jjMoveStringLiteralDfa16_0(var3, 0L, var7, 2L);
         case 's':
            return this.jjMoveStringLiteralDfa16_0(var3, 0L, var7, 8L);
         default:
            return this.jjStartNfa_0(14, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa16_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(14, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(15, 0L, var3, var7);
            return 16;
         }

         switch(this.curChar) {
         case 'g':
            return this.jjMoveStringLiteralDfa17_0(var3, 0L, var7, 2L);
         case 'i':
            return this.jjMoveStringLiteralDfa17_0(var3, 0L, var7, 8L);
         case 's':
            return this.jjMoveStringLiteralDfa17_0(var3, 9007199254740992L, var7, 32L);
         default:
            return this.jjStartNfa_0(15, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa17_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(15, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(16, 0L, var3, var7);
            return 17;
         }

         switch(this.curChar) {
         case 'g':
            return this.jjMoveStringLiteralDfa18_0(var3, 0L, var7, 8L);
         case 'h':
            return this.jjMoveStringLiteralDfa18_0(var3, 9007199254740992L, var7, 32L);
         case 'n':
            if ((var7 & 2L) != 0L) {
               return this.jjStopAtPos(17, 129);
            }
         default:
            return this.jjStartNfa_0(16, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa18_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(16, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(17, 0L, var3, var7);
            return 18;
         }

         switch(this.curChar) {
         case 'i':
            return this.jjMoveStringLiteralDfa19_0(var3, 9007199254740992L, var7, 32L);
         case 'n':
            if ((var7 & 8L) != 0L) {
               return this.jjStopAtPos(18, 131);
            }
         default:
            return this.jjStartNfa_0(17, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa19_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(17, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(18, 0L, var3, var7);
            return 19;
         }

         switch(this.curChar) {
         case 'f':
            return this.jjMoveStringLiteralDfa20_0(var3, 9007199254740992L, var7, 32L);
         default:
            return this.jjStartNfa_0(18, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa20_0(long var1, long var3, long var5, long var7) {
      if (((var3 &= var1) | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(18, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(19, 0L, var3, var7);
            return 20;
         }

         switch(this.curChar) {
         case 't':
            if ((var3 & 9007199254740992L) != 0L) {
               this.jjmatchedKind = 117;
               this.jjmatchedPos = 20;
            }

            return this.jjMoveStringLiteralDfa21_0(var3, 0L, var7, 32L);
         default:
            return this.jjStartNfa_0(19, 0L, var3, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa21_0(long var1, long var3, long var5, long var7) {
      if ((var3 & var1 | (var7 &= var5)) == 0L) {
         return this.jjStartNfa_0(19, 0L, var1, var5);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(20, 0L, 0L, var7);
            return 21;
         }

         switch(this.curChar) {
         case '_':
            return this.jjMoveStringLiteralDfa22_0(var7, 32L);
         default:
            return this.jjStartNfa_0(20, 0L, 0L, var7);
         }
      }
   }

   private final int jjMoveStringLiteralDfa22_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(20, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(21, 0L, 0L, var3);
            return 22;
         }

         switch(this.curChar) {
         case 'a':
            return this.jjMoveStringLiteralDfa23_0(var3, 32L);
         default:
            return this.jjStartNfa_0(21, 0L, 0L, var3);
         }
      }
   }

   private final int jjMoveStringLiteralDfa23_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(21, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(22, 0L, 0L, var3);
            return 23;
         }

         switch(this.curChar) {
         case 's':
            return this.jjMoveStringLiteralDfa24_0(var3, 32L);
         default:
            return this.jjStartNfa_0(22, 0L, 0L, var3);
         }
      }
   }

   private final int jjMoveStringLiteralDfa24_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(22, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(23, 0L, 0L, var3);
            return 24;
         }

         switch(this.curChar) {
         case 's':
            return this.jjMoveStringLiteralDfa25_0(var3, 32L);
         default:
            return this.jjStartNfa_0(23, 0L, 0L, var3);
         }
      }
   }

   private final int jjMoveStringLiteralDfa25_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(23, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(24, 0L, 0L, var3);
            return 25;
         }

         switch(this.curChar) {
         case 'i':
            return this.jjMoveStringLiteralDfa26_0(var3, 32L);
         default:
            return this.jjStartNfa_0(24, 0L, 0L, var3);
         }
      }
   }

   private final int jjMoveStringLiteralDfa26_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(24, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(25, 0L, 0L, var3);
            return 26;
         }

         switch(this.curChar) {
         case 'g':
            return this.jjMoveStringLiteralDfa27_0(var3, 32L);
         default:
            return this.jjStartNfa_0(25, 0L, 0L, var3);
         }
      }
   }

   private final int jjMoveStringLiteralDfa27_0(long var1, long var3) {
      if ((var3 &= var1) == 0L) {
         return this.jjStartNfa_0(25, 0L, 0L, var1);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(26, 0L, 0L, var3);
            return 27;
         }

         switch(this.curChar) {
         case 'n':
            if ((var3 & 32L) != 0L) {
               return this.jjStopAtPos(27, 133);
            }
         default:
            return this.jjStartNfa_0(26, 0L, 0L, var3);
         }
      }
   }

   private final void jjCheckNAdd(int var1) {
      if (this.jjrounds[var1] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = var1;
         this.jjrounds[var1] = this.jjround;
      }

   }

   private final void jjAddStates(int var1, int var2) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[var1];
      } while(var1++ != var2);

   }

   private final void jjCheckNAddTwoStates(int var1, int var2) {
      this.jjCheckNAdd(var1);
      this.jjCheckNAdd(var2);
   }

   private final void jjCheckNAddStates(int var1, int var2) {
      do {
         this.jjCheckNAdd(jjnextStates[var1]);
      } while(var1++ != var2);

   }

   private final void jjCheckNAddStates(int var1) {
      this.jjCheckNAdd(jjnextStates[var1]);
      this.jjCheckNAdd(jjnextStates[var1 + 1]);
   }

   private final int jjMoveNfa_0(int var1, int var2) {
      int var3 = 0;
      this.jjnewStateCnt = 74;
      int var4 = 1;
      this.jjstateSet[0] = var1;
      int var5 = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long var6;
         if (this.curChar < '@') {
            var6 = 1L << this.curChar;

            do {
               --var4;
               switch(this.jjstateSet[var4]) {
               case 0:
                  if ((8589934591L & var6) != 0L) {
                     if (var5 > 6) {
                        var5 = 6;
                     }

                     this.jjCheckNAdd(0);
                  }
                  break;
               case 1:
                  if (this.curChar == '!') {
                     this.jjCheckNAddStates(21, 23);
                  }
                  break;
               case 2:
                  if ((-9217L & var6) != 0L) {
                     this.jjCheckNAddStates(21, 23);
                  }
                  break;
               case 3:
                  if ((9216L & var6) != 0L && var5 > 8) {
                     var5 = 8;
                  }
                  break;
               case 4:
                  if (this.curChar == '\n' && var5 > 8) {
                     var5 = 8;
                  }
                  break;
               case 5:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 4;
                  }
                  break;
               case 6:
                  if ((8589934591L & var6) != 0L) {
                     if (var5 > 6) {
                        var5 = 6;
                     }

                     this.jjCheckNAdd(0);
                  } else if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddStates(0, 6);
                  } else if (this.curChar == '/') {
                     this.jjAddStates(7, 9);
                  } else if (this.curChar == '$') {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  } else if (this.curChar == '"') {
                     this.jjCheckNAddStates(10, 12);
                  } else if (this.curChar == '\'') {
                     this.jjAddStates(13, 14);
                  } else if (this.curChar == '.') {
                     this.jjCheckNAdd(11);
                  } else if (this.curChar == '#') {
                     this.jjstateSet[this.jjnewStateCnt++] = 1;
                  }

                  if ((287667426198290432L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(8, 9);
                  } else if (this.curChar == '0') {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddStates(15, 17);
                  }
                  break;
               case 7:
                  if ((287667426198290432L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(8, 9);
                  }
                  break;
               case 8:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(8, 9);
                  }
               case 9:
               case 12:
               case 15:
               case 19:
               case 27:
               case 40:
               case 44:
               case 48:
               case 52:
               default:
                  break;
               case 10:
                  if (this.curChar == '.') {
                     this.jjCheckNAdd(11);
                  }
                  break;
               case 11:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddStates(24, 26);
                  }
                  break;
               case 13:
                  if ((43980465111040L & var6) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 14:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddTwoStates(14, 15);
                  }
                  break;
               case 16:
                  if (this.curChar == '\'') {
                     this.jjAddStates(13, 14);
                  }
                  break;
               case 17:
                  if ((-549755823105L & var6) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 18:
                  if (this.curChar == '\'' && var5 > 66) {
                     var5 = 66;
                  }
                  break;
               case 20:
                  if ((566935683072L & var6) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 21:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(22, 18);
                  }
                  break;
               case 22:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 23:
                  if ((4222124650659840L & var6) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 24;
                  }
                  break;
               case 24:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAdd(22);
                  }
                  break;
               case 25:
                  if (this.curChar == '"') {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 26:
                  if ((-17179878401L & var6) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 28:
                  if ((566935683072L & var6) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 29:
                  if (this.curChar == '"' && var5 > 67) {
                     var5 = 67;
                  }
                  break;
               case 30:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAddStates(27, 30);
                  }
                  break;
               case 31:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 32:
                  if ((4222124650659840L & var6) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 33;
                  }
                  break;
               case 33:
                  if ((71776119061217280L & var6) != 0L) {
                     this.jjCheckNAdd(31);
                  }
                  break;
               case 34:
                  if (this.curChar == '$') {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  }
                  break;
               case 35:
                  if ((287948969894477824L & var6) != 0L) {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  }
                  break;
               case 36:
                  if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddStates(0, 6);
                  }
                  break;
               case 37:
                  if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(37, 38);
                  }
                  break;
               case 38:
                  if (this.curChar == '.') {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddStates(31, 33);
                  }
                  break;
               case 39:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddStates(31, 33);
                  }
                  break;
               case 41:
                  if ((43980465111040L & var6) != 0L) {
                     this.jjCheckNAdd(42);
                  }
                  break;
               case 42:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddTwoStates(42, 15);
                  }
                  break;
               case 43:
                  if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(43, 44);
                  }
                  break;
               case 45:
                  if ((43980465111040L & var6) != 0L) {
                     this.jjCheckNAdd(46);
                  }
                  break;
               case 46:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 64) {
                        var5 = 64;
                     }

                     this.jjCheckNAddTwoStates(46, 15);
                  }
                  break;
               case 47:
                  if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddStates(34, 36);
                  }
                  break;
               case 49:
                  if ((43980465111040L & var6) != 0L) {
                     this.jjCheckNAdd(50);
                  }
                  break;
               case 50:
                  if ((287948901175001088L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(50, 15);
                  }
                  break;
               case 51:
                  if (this.curChar == '0') {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddStates(15, 17);
                  }
                  break;
               case 53:
                  if ((287948901175001088L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(53, 9);
                  }
                  break;
               case 54:
                  if ((71776119061217280L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(54, 9);
                  }
                  break;
               case 55:
                  if (this.curChar == '/') {
                     this.jjAddStates(7, 9);
                  }
                  break;
               case 56:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 67;
                  } else if (this.curChar == '/') {
                     if (var5 > 7) {
                        var5 = 7;
                     }

                     this.jjCheckNAddStates(18, 20);
                  }

                  if (this.curChar == '*') {
                     this.jjCheckNAdd(62);
                  }
                  break;
               case 57:
                  if ((-9217L & var6) != 0L) {
                     if (var5 > 7) {
                        var5 = 7;
                     }

                     this.jjCheckNAddStates(18, 20);
                  }
                  break;
               case 58:
                  if ((9216L & var6) != 0L && var5 > 7) {
                     var5 = 7;
                  }
                  break;
               case 59:
                  if (this.curChar == '\n' && var5 > 7) {
                     var5 = 7;
                  }
                  break;
               case 60:
                  if (this.curChar == '\r') {
                     this.jjstateSet[this.jjnewStateCnt++] = 59;
                  }
                  break;
               case 61:
                  if (this.curChar == '*') {
                     this.jjCheckNAdd(62);
                  }
                  break;
               case 62:
                  if ((-4398046511105L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(62, 63);
                  }
                  break;
               case 63:
                  if (this.curChar == '*') {
                     this.jjCheckNAddStates(37, 39);
                  }
                  break;
               case 64:
                  if ((-145135534866433L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(65, 63);
                  }
                  break;
               case 65:
                  if ((-4398046511105L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(65, 63);
                  }
                  break;
               case 66:
                  if (this.curChar == '/' && var5 > 9) {
                     var5 = 9;
                  }
                  break;
               case 67:
                  if (this.curChar == '*') {
                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 68:
                  if ((-4398046511105L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 69:
                  if (this.curChar == '*') {
                     this.jjCheckNAddStates(40, 42);
                  }
                  break;
               case 70:
                  if ((-145135534866433L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(71, 69);
                  }
                  break;
               case 71:
                  if ((-4398046511105L & var6) != 0L) {
                     this.jjCheckNAddTwoStates(71, 69);
                  }
                  break;
               case 72:
                  if (this.curChar == '/' && var5 > 68) {
                     var5 = 68;
                  }
                  break;
               case 73:
                  if (this.curChar == '*') {
                     this.jjstateSet[this.jjnewStateCnt++] = 67;
                  }
               }
            } while(var4 != var3);
         } else if (this.curChar < 128) {
            var6 = 1L << (this.curChar & 63);

            do {
               --var4;
               switch(this.jjstateSet[var4]) {
               case 2:
                  this.jjAddStates(21, 23);
               case 3:
               case 4:
               case 5:
               case 7:
               case 8:
               case 10:
               case 11:
               case 13:
               case 14:
               case 16:
               case 18:
               case 21:
               case 22:
               case 23:
               case 24:
               case 25:
               case 29:
               case 30:
               case 31:
               case 32:
               case 33:
               case 34:
               case 36:
               case 37:
               case 38:
               case 39:
               case 41:
               case 42:
               case 43:
               case 45:
               case 46:
               case 47:
               case 49:
               case 50:
               case 51:
               case 54:
               case 55:
               case 56:
               case 58:
               case 59:
               case 60:
               case 61:
               case 63:
               case 66:
               case 67:
               case 69:
               default:
                  break;
               case 6:
               case 35:
                  if ((576460745995190270L & var6) != 0L) {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  }
                  break;
               case 9:
                  if ((17592186048512L & var6) != 0L && var5 > 60) {
                     var5 = 60;
                  }
                  break;
               case 12:
                  if ((137438953504L & var6) != 0L) {
                     this.jjAddStates(43, 44);
                  }
                  break;
               case 15:
                  if ((343597383760L & var6) != 0L && var5 > 64) {
                     var5 = 64;
                  }
                  break;
               case 17:
                  if ((-268435457L & var6) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 19:
                  if (this.curChar == '\\') {
                     this.jjAddStates(45, 47);
                  }
                  break;
               case 20:
                  if ((5700160604602368L & var6) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 26:
                  if ((-268435457L & var6) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 27:
                  if (this.curChar == '\\') {
                     this.jjAddStates(48, 50);
                  }
                  break;
               case 28:
                  if ((5700160604602368L & var6) != 0L) {
                     this.jjCheckNAddStates(10, 12);
                  }
                  break;
               case 40:
                  if ((137438953504L & var6) != 0L) {
                     this.jjAddStates(51, 52);
                  }
                  break;
               case 44:
                  if ((137438953504L & var6) != 0L) {
                     this.jjAddStates(53, 54);
                  }
                  break;
               case 48:
                  if ((137438953504L & var6) != 0L) {
                     this.jjAddStates(55, 56);
                  }
                  break;
               case 52:
                  if ((72057594054705152L & var6) != 0L) {
                     this.jjCheckNAdd(53);
                  }
                  break;
               case 53:
                  if ((541165879422L & var6) != 0L) {
                     if (var5 > 60) {
                        var5 = 60;
                     }

                     this.jjCheckNAddTwoStates(53, 9);
                  }
                  break;
               case 57:
                  if (var5 > 7) {
                     var5 = 7;
                  }

                  this.jjAddStates(18, 20);
                  break;
               case 62:
                  this.jjCheckNAddTwoStates(62, 63);
                  break;
               case 64:
               case 65:
                  this.jjCheckNAddTwoStates(65, 63);
                  break;
               case 68:
                  this.jjCheckNAddTwoStates(68, 69);
                  break;
               case 70:
               case 71:
                  this.jjCheckNAddTwoStates(71, 69);
               }
            } while(var4 != var3);
         } else {
            int var8 = this.curChar >> 8;
            int var9 = var8 >> 6;
            long var10 = 1L << (var8 & 63);
            int var12 = (this.curChar & 255) >> 6;
            long var13 = 1L << (this.curChar & 63);

            do {
               --var4;
               switch(this.jjstateSet[var4]) {
               case 0:
                  if (jjCanMove_0(var8, var9, var12, var10, var13)) {
                     if (var5 > 6) {
                        var5 = 6;
                     }

                     this.jjCheckNAdd(0);
                  }
                  break;
               case 2:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjAddStates(21, 23);
                  }
                  break;
               case 6:
                  if (jjCanMove_0(var8, var9, var12, var10, var13)) {
                     if (var5 > 6) {
                        var5 = 6;
                     }

                     this.jjCheckNAdd(0);
                  }

                  if (jjCanMove_2(var8, var9, var12, var10, var13)) {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  }
                  break;
               case 17:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjstateSet[this.jjnewStateCnt++] = 18;
                  }
                  break;
               case 26:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjAddStates(10, 12);
                  }
                  break;
               case 34:
               case 35:
                  if (jjCanMove_2(var8, var9, var12, var10, var13)) {
                     if (var5 > 69) {
                        var5 = 69;
                     }

                     this.jjCheckNAdd(35);
                  }
                  break;
               case 57:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     if (var5 > 7) {
                        var5 = 7;
                     }

                     this.jjAddStates(18, 20);
                  }
                  break;
               case 62:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjCheckNAddTwoStates(62, 63);
                  }
                  break;
               case 64:
               case 65:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjCheckNAddTwoStates(65, 63);
                  }
                  break;
               case 68:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 70:
               case 71:
                  if (jjCanMove_1(var8, var9, var12, var10, var13)) {
                     this.jjCheckNAddTwoStates(71, 69);
                  }
               }
            } while(var4 != var3);
         }

         if (var5 != Integer.MAX_VALUE) {
            this.jjmatchedKind = var5;
            this.jjmatchedPos = var2;
            var5 = Integer.MAX_VALUE;
         }

         ++var2;
         if ((var4 = this.jjnewStateCnt) == (var3 = 74 - (this.jjnewStateCnt = var3))) {
            return var2;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var15) {
            return var2;
         }
      }
   }

   private static final boolean jjCanMove_0(int var0, int var1, int var2, long var3, long var5) {
      switch(var0) {
      case 0:
         return (jjbitVec0[var2] & var5) != 0L;
      default:
         return false;
      }
   }

   private static final boolean jjCanMove_1(int var0, int var1, int var2, long var3, long var5) {
      switch(var0) {
      case 0:
         return (jjbitVec0[var2] & var5) != 0L;
      default:
         return (jjbitVec1[var1] & var3) != 0L;
      }
   }

   private static final boolean jjCanMove_2(int var0, int var1, int var2, long var3, long var5) {
      switch(var0) {
      case 0:
         return (jjbitVec4[var2] & var5) != 0L;
      case 48:
         return (jjbitVec5[var2] & var5) != 0L;
      case 49:
         return (jjbitVec6[var2] & var5) != 0L;
      case 51:
         return (jjbitVec7[var2] & var5) != 0L;
      case 61:
         return (jjbitVec8[var2] & var5) != 0L;
      default:
         return (jjbitVec3[var1] & var3) != 0L;
      }
   }

   public ParserTokenManager(JavaCharStream var1) {
      this.debugStream = System.out;
      this.jjrounds = new int[74];
      this.jjstateSet = new int[148];
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.input_stream = var1;
   }

   public ParserTokenManager(JavaCharStream var1, int var2) {
      this(var1);
      this.SwitchTo(var2);
   }

   public void ReInit(JavaCharStream var1) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = var1;
      this.ReInitRounds();
   }

   private final void ReInitRounds() {
      this.jjround = -2147483647;

      for(int var1 = 74; var1-- > 0; this.jjrounds[var1] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(JavaCharStream var1, int var2) {
      this.ReInit(var1);
      this.SwitchTo(var2);
   }

   public void SwitchTo(int var1) {
      if (var1 < 1 && var1 >= 0) {
         this.curLexState = var1;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + var1 + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      Token var1 = Token.newToken(this.jjmatchedKind);
      var1.kind = this.jjmatchedKind;
      String var2 = jjstrLiteralImages[this.jjmatchedKind];
      var1.image = var2 == null ? this.input_stream.GetImage() : var2;
      var1.beginLine = this.input_stream.getBeginLine();
      var1.beginColumn = this.input_stream.getBeginColumn();
      var1.endLine = this.input_stream.getEndLine();
      var1.endColumn = this.input_stream.getEndColumn();
      return var1;
   }

   public Token getNextToken() {
      Token var1 = null;
      boolean var2 = false;

      while(true) {
         Token var4;
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (IOException var9) {
            this.jjmatchedKind = 0;
            var4 = this.jjFillToken();
            var4.specialToken = var1;
            return var4;
         }

         this.jjmatchedKind = Integer.MAX_VALUE;
         this.jjmatchedPos = 0;
         int var11 = this.jjMoveStringLiteralDfa0_0();
         if (this.jjmatchedKind == Integer.MAX_VALUE) {
            int var3 = this.input_stream.getEndLine();
            int var5 = this.input_stream.getEndColumn();
            String var6 = null;
            boolean var7 = false;

            try {
               this.input_stream.readChar();
               this.input_stream.backup(1);
            } catch (IOException var10) {
               var7 = true;
               var6 = var11 <= 1 ? "" : this.input_stream.GetImage();
               if (this.curChar != '\n' && this.curChar != '\r') {
                  ++var5;
               } else {
                  ++var3;
                  var5 = 0;
               }
            }

            if (!var7) {
               this.input_stream.backup(1);
               var6 = var11 <= 1 ? "" : this.input_stream.GetImage();
            }

            throw new TokenMgrError(var7, this.curLexState, var3, var5, var6, this.curChar, 0);
         }

         if (this.jjmatchedPos + 1 < var11) {
            this.input_stream.backup(var11 - this.jjmatchedPos - 1);
         }

         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
            var4 = this.jjFillToken();
            var4.specialToken = var1;
            return var4;
         }

         if ((jjtoSpecial[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
            var4 = this.jjFillToken();
            if (var1 == null) {
               var1 = var4;
            } else {
               var4.specialToken = var1;
               var1 = var1.next = var4;
            }
         }
      }
   }
}
