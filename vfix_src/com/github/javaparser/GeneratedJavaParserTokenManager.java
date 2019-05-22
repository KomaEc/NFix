package com.github.javaparser;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsCollection;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneratedJavaParserTokenManager implements GeneratedJavaParserConstants {
   private List<JavaToken> tokens = new ArrayList();
   private CommentsCollection commentsCollection = new CommentsCollection();
   private JavaToken homeToken;
   private Stack<Token> tokenWorkStack = new Stack();
   private boolean storeTokens;
   public PrintStream debugStream;
   static final long[] jjbitVec0 = new long[]{0L, 0L, 4294967328L, 0L};
   static final long[] jjbitVec1 = new long[]{0L, 0L, 1L, 0L};
   static final long[] jjbitVec2 = new long[]{16384L, 0L, 0L, 0L};
   static final long[] jjbitVec3 = new long[]{144036023255039L, 6442450944L, 0L, 0L};
   static final long[] jjbitVec4 = new long[]{1L, 0L, 0L, 0L};
   static final long[] jjbitVec5 = new long[]{0L, 0L, 0L, Long.MIN_VALUE};
   static final long[] jjbitVec6 = new long[]{-2L, -1L, -1L, -1L};
   static final long[] jjbitVec8 = new long[]{0L, 0L, -1L, -1L};
   static final long[] jjbitVec9 = new long[]{-4503598551400446L, -8193L, -17388175097857L, 1297036696969281535L};
   static final long[] jjbitVec10 = new long[]{0L, 0L, 297242231151001600L, -36028797027352577L};
   static final long[] jjbitVec11 = new long[]{-1L, -1L, -1L, 88094074470339L};
   static final long[] jjbitVec12 = new long[]{0L, -4837147474772623360L, -17179879616L, -18014398509481985L};
   static final long[] jjbitVec13 = new long[]{-1L, -1L, -1021L, -1L};
   static final long[] jjbitVec14 = new long[]{-281474976710657L, -8547991553L, 33023L, 1979120929931264L};
   static final long[] jjbitVec15 = new long[]{-4294965248L, -351843720886273L, -1L, -7205547885240254465L};
   static final long[] jjbitVec16 = new long[]{281474976514048L, -8192L, 563224831328255L, 301749971126844416L};
   static final long[] jjbitVec17 = new long[]{1168302407679L, 8791831609343L, 4602678814877679616L, 0L};
   static final long[] jjbitVec18 = new long[]{2594073385365405680L, -562932790263808L, 2577745637692514273L, 1733604397398638592L};
   static final long[] jjbitVec19 = new long[]{247132830528276448L, 7881300924956672L, 2589004636761079776L, 144678150914244608L};
   static final long[] jjbitVec20 = new long[]{2589004636760940512L, 562965791113216L, 288167810662516712L, 144115188075921408L};
   static final long[] jjbitVec21 = new long[]{2594071186342010848L, 13002342400L, 2589567586714640353L, 1688863818907648L};
   static final long[] jjbitVec22 = new long[]{2882303761516978144L, -288230361111969792L, 3457638613854978016L, 127L};
   static final long[] jjbitVec23 = new long[]{-9219431387180826626L, 127L, 2309762420256548246L, 4026531935L};
   static final long[] jjbitVec24 = new long[]{1L, 35184372088575L, 7936L, 0L};
   static final long[] jjbitVec25 = new long[]{-9223363240761753601L, -8514196127940608L, -4294950909L, -576460752303480641L};
   static final long[] jjbitVec26 = new long[]{-1L, -3263218177L, 9168765891372858879L, -8388803L};
   static final long[] jjbitVec27 = new long[]{-12713985L, 134217727L, -4294901761L, 4557642822898941951L};
   static final long[] jjbitVec28 = new long[]{-1L, -105553116266497L, -4160749570L, 144053615424700415L};
   static final long[] jjbitVec29 = new long[]{1125895612129279L, 527761286627327L, 4503599627370495L, 411041792L};
   static final long[] jjbitVec30 = new long[]{-4294967296L, 72057594037927935L, -274877906944097L, 18014398509481983L};
   static final long[] jjbitVec31 = new long[]{2147483647L, 8796093022142464L, -263882790666241L, 1023L};
   static final long[] jjbitVec32 = new long[]{-4286578689L, 2097151L, 549755813888L, 0L};
   static final long[] jjbitVec33 = new long[]{4503599627370464L, 4064L, -288019261329244168L, 274877906943L};
   static final long[] jjbitVec34 = new long[]{68719476735L, 4611686018360336384L, 511L, 28110114275721216L};
   static final long[] jjbitVec35 = new long[]{-1L, -1L, -1L, 0L};
   static final long[] jjbitVec36 = new long[]{-3233808385L, 4611686017001275199L, 6908521828386340863L, 2295745090394464220L};
   static final long[] jjbitVec37 = new long[]{Long.MIN_VALUE, -9222809086900305919L, -3758161920L, 0L};
   static final long[] jjbitVec38 = new long[]{-864764451093480316L, -4294949920L, 511L, 0L};
   static final long[] jjbitVec39 = new long[]{-140737488355329L, -2147483649L, -1L, 3509778554814463L};
   static final long[] jjbitVec40 = new long[]{-245465970900993L, 141836999983103L, 9187201948305063935L, 2139062143L};
   static final long[] jjbitVec41 = new long[]{140737488355328L, 0L, 0L, 0L};
   static final long[] jjbitVec42 = new long[]{2251241253188403424L, -2L, -4823449601L, -576460752303423489L};
   static final long[] jjbitVec43 = new long[]{-422212465066016L, -1L, 576460748008488959L, -281474976710656L};
   static final long[] jjbitVec44 = new long[]{-1L, -1L, 18014398509481983L, 0L};
   static final long[] jjbitVec45 = new long[]{-1L, -1L, -1L, 8796093022207L};
   static final long[] jjbitVec46 = new long[]{-1L, -1L, 8191L, 4611686018427322368L};
   static final long[] jjbitVec47 = new long[]{13198434443263L, -9223231299366420481L, -3221225473L, 281474976710655L};
   static final long[] jjbitVec48 = new long[]{-12893290496L, -1L, 71916856549571071L, -36028797018963968L};
   static final long[] jjbitVec49 = new long[]{72057628397664187L, 4503599627370495L, 4503599627370492L, 2953235455648202752L};
   static final long[] jjbitVec50 = new long[]{-281200098804736L, 2305843004918726783L, 2251799813685232L, 8935422993945886720L};
   static final long[] jjbitVec51 = new long[]{2199023255551L, -4287426849551675401L, 4495436853045886975L, 7890092085477381L};
   static final long[] jjbitVec52 = new long[]{-141291530846594L, -281200233021441L, -1L, 34359738367L};
   static final long[] jjbitVec53 = new long[]{-1L, -1L, -281406257233921L, 1152921504606845055L};
   static final long[] jjbitVec54 = new long[]{-1L, -211106232532993L, -1L, 67108863L};
   static final long[] jjbitVec55 = new long[]{6881498030004502655L, -37L, 1125899906842623L, -524288L};
   static final long[] jjbitVec56 = new long[]{4611686018427387903L, -65536L, -196609L, 2305561534236983551L};
   static final long[] jjbitVec57 = new long[]{6755399441055744L, -9286475208138752L, -1L, 2305843009213693951L};
   static final long[] jjbitVec58 = new long[]{-8646911293141286896L, -274743689218L, Long.MAX_VALUE, 425688104188L};
   static final long[] jjbitVec59 = new long[]{0L, 0L, 297277419818057727L, -36028797027352577L};
   static final long[] jjbitVec60 = new long[]{-1L, -4836865999795912705L, -17179879616L, -18014398509481985L};
   static final long[] jjbitVec61 = new long[]{-1L, -1L, -773L, -1L};
   static final long[] jjbitVec62 = new long[]{-281474976710657L, -8547991553L, -4611686018427485953L, 1979120929931446L};
   static final long[] jjbitVec63 = new long[]{-3892377537L, -65970697666561L, -1L, -6917531227739127809L};
   static final long[] jjbitVec64 = new long[]{-32768L, -6145L, 1125899906842623L, 306244774661193727L};
   static final long[] jjbitVec65 = new long[]{70368744177663L, 8792066490367L, 4602678814877679616L, -1048576L};
   static final long[] jjbitVec66 = new long[]{-1L, -281681135140865L, -881018876128026641L, 1733885649045453215L};
   static final long[] jjbitVec67 = new long[]{-3211631683292264466L, 18014125208779143L, -869759877059461138L, -143270973599040577L};
   static final long[] jjbitVec68 = new long[]{-869759877059600402L, 844217442122143L, -4323518207764871188L, 144396388183129543L};
   static final long[] jjbitVec69 = new long[]{-2017614832085377041L, 281264647060959L, -869196927105900561L, 1970115463626207L};
   static final long[] jjbitVec70 = new long[]{-139281L, -287949109465154081L, 3457638613854978028L, 3658904103781503L};
   static final long[] jjbitVec71 = new long[]{-8646911284551352322L, 67076095L, 4323434403644581270L, 4093591391L};
   static final long[] jjbitVec72 = new long[]{-4422530440275951615L, -527765581332737L, 2305843009196916703L, 64L};
   static final long[] jjbitVec73 = new long[]{-1L, -64513L, -3221225473L, -576460752303480641L};
   static final long[] jjbitVec74 = new long[]{-12713985L, 3892314111L, -4294901761L, 4557642822898941951L};
   static final long[] jjbitVec75 = new long[]{9007194961862655L, 3905461007941631L, -1L, 4394700505087L};
   static final long[] jjbitVec76 = new long[]{-4227893248L, 72057594037927935L, -272678883688449L, 18014398509481983L};
   static final long[] jjbitVec77 = new long[]{1152657619668697087L, 8796093022207936L, -263882790666241L, 67044351L};
   static final long[] jjbitVec78 = new long[]{-4026531841L, -6917529029788565505L, 4611405093273535487L, 0L};
   static final long[] jjbitVec79 = new long[]{-1L, 4494803601395711L, -1L, 4503599627370495L};
   static final long[] jjbitVec80 = new long[]{72057594037927935L, 4611686018427380735L, 511L, 288230376151121920L};
   static final long[] jjbitVec81 = new long[]{-1L, -1L, -1L, -288230376151711745L};
   static final long[] jjbitVec82 = new long[]{-9223235697412868096L, -9222527753657516031L, -3758161920L, 562821641207808L};
   static final long[] jjbitVec83 = new long[]{-140737488355329L, -2147483649L, -1L, 4494940973301759L};
   static final long[] jjbitVec84 = new long[]{-245465970900993L, -9223230199854792705L, 9187201948305063935L, -2155905153L};
   static final long[] jjbitVec85 = new long[]{2251518330118602976L, -2L, -4722786305L, -576460752303423489L};
   static final long[] jjbitVec86 = new long[]{17592185987071L, -4615908143078047745L, -1L, 1125899906842623L};
   static final long[] jjbitVec87 = new long[]{72058693549555711L, 4503599627370495L, -1L, 2954361351327121471L};
   static final long[] jjbitVec88 = new long[]{-211106232532993L, 2305843004919775231L, -1L, 9223372032626884609L};
   static final long[] jjbitVec89 = new long[]{36028797018963967L, -252201583360655361L, -1L, 35184368733388807L};
   static final long[] jjbitVec90 = new long[]{-141291530846594L, -281200233021441L, -1L, 288010473826156543L};
   static final long[] jjbitVec91 = new long[]{6881498031078244479L, -37L, 1125899906842623L, -524288L};
   static final long[] jjbitVec92 = new long[]{7036870122864639L, -9286475208138752L, -1L, -6917529027641081857L};
   static final long[] jjbitVec93 = new long[]{-8646911293074243568L, -274743689218L, Long.MAX_VALUE, 1008806742219095292L};
   public static final String[] jjstrLiteralImages = new String[]{"", null, null, null, null, null, null, null, null, null, null, "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while", "requires", "to", "with", "open", "opens", "uses", "module", "exports", "provides", "transitive", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "(", ")", "{", "}", "[", "]", ";", ",", ".", "@", "=", "<", "!", "~", "?", ":", "==", "<=", ">=", "!=", "||", "&&", "++", "--", "+", "-", "*", "/", "&", "|", "^", "%", "<<", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=", ">>=", ">>>=", "...", "->", "::", ">>>", ">>", ">", "\u001a"};
   static final int[] jjnextStates = new int[]{57, 58, 60, 65, 66, 67, 68, 69, 70, 71, 72, 10, 78, 79, 80, 86, 87, 88, 56, 63, 28, 29, 36, 37, 13, 15, 26, 99, 103, 106, 110, 114, 117, 121, 134, 3, 4, 5, 10, 8, 10, 11, 7, 8, 10, 11, 28, 29, 39, 36, 37, 75, 10, 77, 74, 75, 10, 77, 83, 10, 85, 82, 83, 10, 85, 89, 92, 10, 90, 91, 92, 10, 95, 10, 97, 94, 95, 10, 97, 101, 102, 67, 104, 105, 67, 108, 109, 67, 123, 124, 125, 127, 128, 129, 132, 133, 10, 136, 137, 138, 139, 142, 143, 10, 3, 4, 6, 7, 8, 9, 16, 17, 19, 30, 38, 40, 65, 66, 68, 69, 70, 71, 73, 74, 75, 76, 78, 79, 81, 82, 83, 84, 86, 87, 90, 91, 93, 94, 95, 96, 104, 105, 108, 109, 115, 116, 119, 120, 130, 131, 132, 133, 140, 141, 142, 143};
   int curLexState;
   int defaultLexState;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;
   public static final String[] lexStateNames = new String[]{"DEFAULT", "IN_JAVADOC_COMMENT", "IN_MULTI_LINE_COMMENT"};
   public static final int[] jjnewLexState = new int[]{-1, -1, -1, -1, -1, -1, 1, 2, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
   static final long[] jjtoToken = new long[]{-2047L, -209645569L, 32767L};
   static final long[] jjtoSkip = new long[]{830L, 0L, 0L};
   static final long[] jjtoSpecial = new long[]{830L, 0L, 0L};
   static final long[] jjtoMore = new long[]{1216L, 0L, 0L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds;
   private final int[] jjstateSet;
   private final StringBuilder jjimage;
   private StringBuilder image;
   private int jjimageLen;
   private int lengthOfMatch;
   protected int curChar;

   void reset() {
      this.tokens = new ArrayList();
      this.commentsCollection = new CommentsCollection();
      this.homeToken = null;
   }

   List<JavaToken> getTokens() {
      return this.storeTokens ? this.tokens : null;
   }

   CommentsCollection getCommentsCollection() {
      return this.commentsCollection;
   }

   JavaToken getHomeToken() {
      return this.homeToken;
   }

   public void setStoreTokens(boolean storeTokens) {
      this.storeTokens = storeTokens;
   }

   private void CommonTokenAction(Token token) {
      do {
         this.tokenWorkStack.push(token);
         token = token.specialToken;
      } while(token != null);

      while(!this.tokenWorkStack.empty()) {
         token = (Token)this.tokenWorkStack.pop();
         token.javaToken = new JavaToken(token, this.tokens);
         if (this.storeTokens) {
            this.tokens.add(token.javaToken);
         }

         if (this.homeToken == null) {
            this.homeToken = token.javaToken;
         }

         if (TokenTypes.isComment(token.kind)) {
            Comment comment = GeneratedJavaParserTokenManagerBase.createCommentFromToken(token);
            this.commentsCollection.addComment(comment);
         }
      }

   }

   public void setDebugStream(PrintStream ds) {
      this.debugStream = ds;
   }

   private final int jjStopStringLiteralDfa_0(int pos, long active0, long active1, long active2) {
      switch(pos) {
      case 0:
         if ((active1 & 68719476736L) == 0L && (active2 & 256L) == 0L) {
            if ((active0 & -2048L) == 0L && (active1 & 1023L) == 0L) {
               if ((active0 & 128L) == 0L && (active1 & 36028797018963968L) == 0L && (active2 & 1L) == 0L) {
                  return -1;
               }

               return 56;
            }

            this.jjmatchedKind = 89;
            return 144;
         }

         return 2;
      case 1:
         if ((active0 & 128L) != 0L) {
            return 61;
         } else {
            if ((active0 & -17205037056L) == 0L && (active1 & 1021L) == 0L) {
               if ((active0 & 17205035008L) == 0L && (active1 & 2L) == 0L) {
                  return -1;
               }

               return 144;
            }

            if (this.jjmatchedPos != 1) {
               this.jjmatchedKind = 89;
               this.jjmatchedPos = 1;
            }

            return 144;
         }
      case 2:
         if ((active0 & 1152926731582046208L) != 0L) {
            return 144;
         } else {
            if ((active0 & -1152926748770306048L) == 0L && (active1 & 1021L) == 0L) {
               return -1;
            }

            if (this.jjmatchedPos != 2) {
               this.jjmatchedKind = 89;
               this.jjmatchedPos = 2;
            }

            return 144;
         }
      case 3:
         if ((active0 & 2918342462831689728L) == 0L && (active1 & 60L) == 0L) {
            if ((active0 & -4071268661846181888L) == 0L && (active1 & 961L) == 0L) {
               return -1;
            }

            if (this.jjmatchedPos != 3) {
               this.jjmatchedKind = 89;
               this.jjmatchedPos = 3;
            }

            return 144;
         }

         return 144;
      case 4:
         if ((active0 & 4930864039285766144L) == 0L && (active1 & 961L) == 0L) {
            if ((active0 & -9002132701131948032L) == 0L && (active1 & 16L) == 0L) {
               return -1;
            }

            return 144;
         }

         if (this.jjmatchedPos != 4) {
            this.jjmatchedKind = 89;
            this.jjmatchedPos = 4;
         }

         return 144;
      case 5:
         if ((active0 & 154672767462019072L) == 0L && (active1 & 64L) == 0L) {
            if ((active0 & 4920306460973344768L) == 0L && (active1 & 897L) == 0L) {
               return -1;
            }

            this.jjmatchedKind = 89;
            this.jjmatchedPos = 5;
            return 144;
         }

         return 144;
      case 6:
         if ((active0 & 52777770295296L) == 0L && (active1 & 128L) == 0L) {
            if ((active0 & 4920253683203049472L) == 0L && (active1 & 769L) == 0L) {
               return -1;
            }

            this.jjmatchedKind = 89;
            this.jjmatchedPos = 6;
            return 144;
         }

         return 144;
      case 7:
         if ((active0 & 306315864959877120L) == 0L && (active1 & 512L) == 0L) {
            if ((active0 & 4613937818243172352L) == 0L && (active1 & 257L) == 0L) {
               return -1;
            }

            return 144;
         }

         this.jjmatchedKind = 89;
         this.jjmatchedPos = 7;
         return 144;
      case 8:
         if ((active0 & 288301294651703296L) != 0L) {
            return 144;
         } else {
            if ((active0 & 18014570308173824L) == 0L && (active1 & 512L) == 0L) {
               return -1;
            }

            this.jjmatchedKind = 89;
            this.jjmatchedPos = 8;
            return 144;
         }
      case 9:
         if ((active0 & 171798691840L) == 0L && (active1 & 512L) == 0L) {
            if ((active0 & 18014398509481984L) != 0L) {
               this.jjmatchedKind = 89;
               this.jjmatchedPos = 9;
               return 144;
            }

            return -1;
         }

         return 144;
      case 10:
         if ((active0 & 18014398509481984L) != 0L) {
            this.jjmatchedKind = 89;
            this.jjmatchedPos = 10;
            return 144;
         }

         return -1;
      default:
         return -1;
      }
   }

   private final int jjStartNfa_0(int pos, long active0, long active1, long active2) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0, active1, active2), pos + 1);
   }

   private int jjStopAtPos(int pos, int kind) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;
      return pos + 1;
   }

   private int jjMoveStringLiteralDfa0_0() {
      switch(this.curChar) {
      case 10:
         return this.jjStopAtPos(0, 3);
      case 11:
      case 12:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 34:
      case 35:
      case 36:
      case 39:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
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
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 92:
      case 95:
      case 96:
      case 104:
      case 106:
      case 107:
      case 113:
      case 120:
      case 121:
      case 122:
      default:
         return this.jjMoveNfa_0(0, 0);
      case 13:
         this.jjmatchedKind = 4;
         return this.jjMoveStringLiteralDfa1_0(4L, 0L, 0L);
      case 26:
         return this.jjStopAtPos(0, 142);
      case 33:
         this.jjmatchedKind = 104;
         return this.jjMoveStringLiteralDfa1_0(0L, 140737488355328L, 0L);
      case 37:
         this.jjmatchedKind = 123;
         return this.jjMoveStringLiteralDfa1_0(0L, 0L, 16L);
      case 38:
         this.jjmatchedKind = 120;
         return this.jjMoveStringLiteralDfa1_0(0L, 562949953421312L, 2L);
      case 40:
         return this.jjStopAtPos(0, 92);
      case 41:
         return this.jjStopAtPos(0, 93);
      case 42:
         this.jjmatchedKind = 118;
         return this.jjMoveStringLiteralDfa1_0(0L, Long.MIN_VALUE, 0L);
      case 43:
         this.jjmatchedKind = 116;
         return this.jjMoveStringLiteralDfa1_0(0L, 2306968909120536576L, 0L);
      case 44:
         return this.jjStopAtPos(0, 99);
      case 45:
         this.jjmatchedKind = 117;
         return this.jjMoveStringLiteralDfa1_0(0L, 4613937818241073152L, 512L);
      case 46:
         this.jjmatchedKind = 100;
         return this.jjMoveStringLiteralDfa1_0(0L, 0L, 256L);
      case 47:
         this.jjmatchedKind = 119;
         return this.jjMoveStringLiteralDfa1_0(128L, 0L, 1L);
      case 58:
         this.jjmatchedKind = 107;
         return this.jjMoveStringLiteralDfa1_0(0L, 0L, 1024L);
      case 59:
         return this.jjStopAtPos(0, 98);
      case 60:
         this.jjmatchedKind = 103;
         return this.jjMoveStringLiteralDfa1_0(0L, 1152956688978935808L, 32L);
      case 61:
         this.jjmatchedKind = 102;
         return this.jjMoveStringLiteralDfa1_0(0L, 17592186044416L, 0L);
      case 62:
         this.jjmatchedKind = 141;
         return this.jjMoveStringLiteralDfa1_0(0L, 70368744177664L, 6336L);
      case 63:
         return this.jjStopAtPos(0, 106);
      case 64:
         return this.jjStopAtPos(0, 101);
      case 91:
         return this.jjStopAtPos(0, 96);
      case 93:
         return this.jjStopAtPos(0, 97);
      case 94:
         this.jjmatchedKind = 122;
         return this.jjMoveStringLiteralDfa1_0(0L, 0L, 8L);
      case 97:
         return this.jjMoveStringLiteralDfa1_0(6144L, 0L, 0L);
      case 98:
         return this.jjMoveStringLiteralDfa1_0(57344L, 0L, 0L);
      case 99:
         return this.jjMoveStringLiteralDfa1_0(4128768L, 0L, 0L);
      case 100:
         return this.jjMoveStringLiteralDfa1_0(29360128L, 0L, 0L);
      case 101:
         return this.jjMoveStringLiteralDfa1_0(234881024L, 128L, 0L);
      case 102:
         return this.jjMoveStringLiteralDfa1_0(8321499136L, 0L, 0L);
      case 103:
         return this.jjMoveStringLiteralDfa1_0(8589934592L, 0L, 0L);
      case 105:
         return this.jjMoveStringLiteralDfa1_0(1082331758592L, 0L, 0L);
      case 108:
         return this.jjMoveStringLiteralDfa1_0(1099511627776L, 0L, 0L);
      case 109:
         return this.jjMoveStringLiteralDfa1_0(0L, 64L, 0L);
      case 110:
         return this.jjMoveStringLiteralDfa1_0(15393162788864L, 0L, 0L);
      case 111:
         return this.jjMoveStringLiteralDfa1_0(0L, 24L, 0L);
      case 112:
         return this.jjMoveStringLiteralDfa1_0(263882790666240L, 256L, 0L);
      case 114:
         return this.jjMoveStringLiteralDfa1_0(281474976710656L, 1L, 0L);
      case 115:
         return this.jjMoveStringLiteralDfa1_0(35465847065542656L, 0L, 0L);
      case 116:
         return this.jjMoveStringLiteralDfa1_0(2269814212194729984L, 514L, 0L);
      case 117:
         return this.jjMoveStringLiteralDfa1_0(0L, 32L, 0L);
      case 118:
         return this.jjMoveStringLiteralDfa1_0(6917529027641081856L, 0L, 0L);
      case 119:
         return this.jjMoveStringLiteralDfa1_0(Long.MIN_VALUE, 4L, 0L);
      case 123:
         return this.jjStopAtPos(0, 94);
      case 124:
         this.jjmatchedKind = 121;
         return this.jjMoveStringLiteralDfa1_0(0L, 281474976710656L, 4L);
      case 125:
         return this.jjStopAtPos(0, 95);
      case 126:
         return this.jjStopAtPos(0, 105);
      }
   }

   private int jjMoveStringLiteralDfa1_0(long active0, long active1, long active2) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var8) {
         this.jjStopStringLiteralDfa_0(0, active0, active1, active2);
         return 1;
      }

      switch(this.curChar) {
      case 10:
         if ((active0 & 4L) != 0L) {
            return this.jjStopAtPos(1, 2);
         }
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 39:
      case 40:
      case 41:
      case 44:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 59:
      case 63:
      case 64:
      case 65:
      case 66:
      case 67:
      case 68:
      case 69:
      case 70:
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
      case 81:
      case 82:
      case 83:
      case 84:
      case 85:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 99:
      case 100:
      case 103:
      case 106:
      case 107:
      case 113:
      case 118:
      case 122:
      case 123:
      default:
         break;
      case 38:
         if ((active1 & 562949953421312L) != 0L) {
            return this.jjStopAtPos(1, 113);
         }
         break;
      case 42:
         if ((active0 & 128L) != 0L) {
            return this.jjStartNfaWithStates_0(1, 7, 61);
         }
         break;
      case 43:
         if ((active1 & 1125899906842624L) != 0L) {
            return this.jjStopAtPos(1, 114);
         }
         break;
      case 45:
         if ((active1 & 2251799813685248L) != 0L) {
            return this.jjStopAtPos(1, 115);
         }
         break;
      case 46:
         return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 256L);
      case 58:
         if ((active2 & 1024L) != 0L) {
            return this.jjStopAtPos(1, 138);
         }
         break;
      case 60:
         if ((active1 & 1152921504606846976L) != 0L) {
            this.jjmatchedKind = 124;
            this.jjmatchedPos = 1;
         }

         return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 32L);
      case 61:
         if ((active1 & 17592186044416L) != 0L) {
            return this.jjStopAtPos(1, 108);
         }

         if ((active1 & 35184372088832L) != 0L) {
            return this.jjStopAtPos(1, 109);
         }

         if ((active1 & 70368744177664L) != 0L) {
            return this.jjStopAtPos(1, 110);
         }

         if ((active1 & 140737488355328L) != 0L) {
            return this.jjStopAtPos(1, 111);
         }

         if ((active1 & 2305843009213693952L) != 0L) {
            return this.jjStopAtPos(1, 125);
         }

         if ((active1 & 4611686018427387904L) != 0L) {
            return this.jjStopAtPos(1, 126);
         }

         if ((active1 & Long.MIN_VALUE) != 0L) {
            return this.jjStopAtPos(1, 127);
         }

         if ((active2 & 1L) != 0L) {
            return this.jjStopAtPos(1, 128);
         }

         if ((active2 & 2L) != 0L) {
            return this.jjStopAtPos(1, 129);
         }

         if ((active2 & 4L) != 0L) {
            return this.jjStopAtPos(1, 130);
         }

         if ((active2 & 8L) != 0L) {
            return this.jjStopAtPos(1, 131);
         }

         if ((active2 & 16L) != 0L) {
            return this.jjStopAtPos(1, 132);
         }
         break;
      case 62:
         if ((active2 & 512L) != 0L) {
            return this.jjStopAtPos(1, 137);
         }

         if ((active2 & 4096L) != 0L) {
            this.jjmatchedKind = 140;
            this.jjmatchedPos = 1;
         }

         return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 0L, active2, 2240L);
      case 97:
         return this.jjMoveStringLiteralDfa2_0(active0, 19791477932032L, active1, 0L, active2, 0L);
      case 98:
         return this.jjMoveStringLiteralDfa2_0(active0, 2048L, active1, 0L, active2, 0L);
      case 101:
         return this.jjMoveStringLiteralDfa2_0(active0, 285873027416064L, active1, 1L, active2, 0L);
      case 102:
         if ((active0 & 17179869184L) != 0L) {
            return this.jjStartNfaWithStates_0(1, 34, 144);
         }
         break;
      case 104:
         return this.jjMoveStringLiteralDfa2_0(active0, -8970607507768344576L, active1, 0L, active2, 0L);
      case 105:
         return this.jjMoveStringLiteralDfa2_0(active0, 1610612736L, active1, 4L, active2, 0L);
      case 108:
         return this.jjMoveStringLiteralDfa2_0(active0, 2181562368L, active1, 0L, active2, 0L);
      case 109:
         return this.jjMoveStringLiteralDfa2_0(active0, 103079215104L, active1, 0L, active2, 0L);
      case 110:
         return this.jjMoveStringLiteralDfa2_0(active0, 962139783168L, active1, 0L, active2, 0L);
      case 111:
         if ((active0 & 8388608L) != 0L) {
            this.jjmatchedKind = 23;
            this.jjmatchedPos = 1;
         } else if ((active1 & 2L) != 0L) {
            return this.jjStartNfaWithStates_0(1, 65, 144);
         }

         return this.jjMoveStringLiteralDfa2_0(active0, 6917530140057542656L, active1, 64L, active2, 0L);
      case 112:
         return this.jjMoveStringLiteralDfa2_0(active0, 0L, active1, 24L, active2, 0L);
      case 114:
         return this.jjMoveStringLiteralDfa2_0(active0, 2017718186178265088L, active1, 768L, active2, 0L);
      case 115:
         return this.jjMoveStringLiteralDfa2_0(active0, 4096L, active1, 32L, active2, 0L);
      case 116:
         return this.jjMoveStringLiteralDfa2_0(active0, 3377699720527872L, active1, 0L, active2, 0L);
      case 117:
         return this.jjMoveStringLiteralDfa2_0(active0, 4653133208748032L, active1, 0L, active2, 0L);
      case 119:
         return this.jjMoveStringLiteralDfa2_0(active0, 9007199254740992L, active1, 0L, active2, 0L);
      case 120:
         return this.jjMoveStringLiteralDfa2_0(active0, 134217728L, active1, 128L, active2, 0L);
      case 121:
         return this.jjMoveStringLiteralDfa2_0(active0, 18014398509514752L, active1, 0L, active2, 0L);
      case 124:
         if ((active1 & 281474976710656L) != 0L) {
            return this.jjStopAtPos(1, 112);
         }
      }

      return this.jjStartNfa_0(0, active0, active1, active2);
   }

   private int jjMoveStringLiteralDfa2_0(long old0, long active0, long old1, long active1, long old2, long active2) {
      if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L) {
         return this.jjStartNfa_0(0, old0, old1, old2);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(1, active0, active1, active2);
            return 2;
         }

         switch(this.curChar) {
         case 46:
            if ((active2 & 256L) != 0L) {
               return this.jjStopAtPos(2, 136);
            }
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
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
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 103:
         case 104:
         case 106:
         case 107:
         case 109:
         case 118:
         case 120:
         default:
            break;
         case 61:
            if ((active2 & 32L) != 0L) {
               return this.jjStopAtPos(2, 133);
            }

            if ((active2 & 64L) != 0L) {
               return this.jjStopAtPos(2, 134);
            }
            break;
         case 62:
            if ((active2 & 2048L) != 0L) {
               this.jjmatchedKind = 139;
               this.jjmatchedPos = 2;
            }

            return this.jjMoveStringLiteralDfa3_0(active0, 0L, active1, 0L, active2, 128L);
         case 97:
            return this.jjMoveStringLiteralDfa3_0(active0, 289356276059340800L, active1, 512L, active2, 0L);
         case 98:
            return this.jjMoveStringLiteralDfa3_0(active0, 140737488355328L, active1, 0L, active2, 0L);
         case 99:
            return this.jjMoveStringLiteralDfa3_0(active0, 17592186044416L, active1, 0L, active2, 0L);
         case 100:
            return this.jjMoveStringLiteralDfa3_0(active0, 0L, active1, 64L, active2, 0L);
         case 101:
            return this.jjMoveStringLiteralDfa3_0(active0, 16384L, active1, 56L, active2, 0L);
         case 102:
            return this.jjMoveStringLiteralDfa3_0(active0, 4194304L, active1, 0L, active2, 0L);
         case 105:
            return this.jjMoveStringLiteralDfa3_0(active0, -6872457846995288064L, active1, 0L, active2, 0L);
         case 108:
            return this.jjMoveStringLiteralDfa3_0(active0, 4611694814788845568L, active1, 0L, active2, 0L);
         case 110:
            return this.jjMoveStringLiteralDfa3_0(active0, 18015499634868224L, active1, 0L, active2, 0L);
         case 111:
            return this.jjMoveStringLiteralDfa3_0(active0, 633320845090816L, active1, 256L, active2, 0L);
         case 112:
            return this.jjMoveStringLiteralDfa3_0(active0, 4503702706585600L, active1, 128L, active2, 0L);
         case 113:
            return this.jjMoveStringLiteralDfa3_0(active0, 0L, active1, 1L, active2, 0L);
         case 114:
            if ((active0 & 4294967296L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 32, 144);
            }

            return this.jjMoveStringLiteralDfa3_0(active0, 218424581927469056L, active1, 0L, active2, 0L);
         case 115:
            return this.jjMoveStringLiteralDfa3_0(active0, 137472579584L, active1, 0L, active2, 0L);
         case 116:
            if ((active0 & 274877906944L) != 0L) {
               this.jjmatchedKind = 38;
               this.jjmatchedPos = 2;
            }

            return this.jjMoveStringLiteralDfa3_0(active0, 284232480096256L, active1, 4L, active2, 0L);
         case 117:
            return this.jjMoveStringLiteralDfa3_0(active0, 576460752387309568L, active1, 0L, active2, 0L);
         case 119:
            if ((active0 & 4398046511104L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 42, 144);
            }
            break;
         case 121:
            if ((active0 & 1152921504606846976L) != 0L) {
               return this.jjStartNfaWithStates_0(2, 60, 144);
            }
         }

         return this.jjStartNfa_0(1, active0, active1, active2);
      }
   }

   private int jjMoveStringLiteralDfa3_0(long old0, long active0, long old1, long active1, long old2, long active2) {
      if (((active0 &= old0) | (active1 &= old1) | (active2 &= old2)) == 0L) {
         return this.jjStartNfa_0(1, old0, old1, old2);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(2, active0, active1, active2);
            return 3;
         }

         switch(this.curChar) {
         case 61:
            if ((active2 & 128L) != 0L) {
               return this.jjStopAtPos(3, 135);
            }
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
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
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 102:
         case 106:
         case 112:
         case 113:
         default:
            break;
         case 97:
            return this.jjMoveStringLiteralDfa4_0(active0, 4611686022189694976L, active1, 0L, active2, 0L);
         case 98:
            return this.jjMoveStringLiteralDfa4_0(active0, 16777216L, active1, 0L, active2, 0L);
         case 99:
            return this.jjMoveStringLiteralDfa4_0(active0, 18014398509613056L, active1, 0L, active2, 0L);
         case 100:
            if ((active0 & 2305843009213693952L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 61, 144);
            }
            break;
         case 101:
            if ((active0 & 32768L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 15, 144);
            }

            if ((active0 & 65536L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 16, 144);
            }

            if ((active0 & 33554432L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 25, 144);
            }

            if ((active0 & 576460752303423488L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 59, 144);
            }

            return this.jjMoveStringLiteralDfa4_0(active0, 4504149517406208L, active1, 0L, active2, 0L);
         case 103:
            if ((active0 & 1099511627776L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 40, 144);
            }
            break;
         case 104:
            if ((active1 & 4L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 66, 144);
            }
            break;
         case 105:
            return this.jjMoveStringLiteralDfa4_0(active0, 2253998836940800L, active1, 0L, active2, 0L);
         case 107:
            return this.jjMoveStringLiteralDfa4_0(active0, 17592186044416L, active1, 0L, active2, 0L);
         case 108:
            if ((active0 & 8796093022208L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 43, 144);
            }

            return this.jjMoveStringLiteralDfa4_0(active0, -9223231265006673920L, active1, 0L, active2, 0L);
         case 109:
            if ((active0 & 67108864L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 26, 144);
            }
            break;
         case 110:
            if ((active1 & 8L) != 0L) {
               this.jjmatchedKind = 67;
               this.jjmatchedPos = 3;
            }

            return this.jjMoveStringLiteralDfa4_0(active0, 288230376151711744L, active1, 528L, active2, 0L);
         case 111:
            if ((active0 & 8589934592L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 33, 144);
            }

            return this.jjMoveStringLiteralDfa4_0(active0, 216172850833260544L, active1, 128L, active2, 0L);
         case 114:
            if ((active0 & 262144L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 18, 144);
            }

            return this.jjMoveStringLiteralDfa4_0(active0, 562949953421312L, active1, 0L, active2, 0L);
         case 115:
            if ((active0 & 36028797018963968L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 55, 144);
            }

            if ((active1 & 32L) != 0L) {
               return this.jjStartNfaWithStates_0(3, 69, 144);
            }

            return this.jjMoveStringLiteralDfa4_0(active0, 270008320L, active1, 0L, active2, 0L);
         case 116:
            return this.jjMoveStringLiteralDfa4_0(active0, 10203605346813952L, active1, 0L, active2, 0L);
         case 117:
            return this.jjMoveStringLiteralDfa4_0(active0, 281474976710656L, active1, 65L, active2, 0L);
         case 118:
            return this.jjMoveStringLiteralDfa4_0(active0, 35184372088832L, active1, 256L, active2, 0L);
         }

         return this.jjStartNfa_0(2, active0, active1, active2);
      }
   }

   private int jjMoveStringLiteralDfa4_0(long old0, long active0, long old1, long active1, long old2, long active2) {
      if (((active0 &= old0) | (active1 &= old1) | active2 & old2) == 0L) {
         return this.jjStartNfa_0(2, old0, old1, old2);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var14) {
            this.jjStopStringLiteralDfa_0(3, active0, active1, 0L);
            return 4;
         }

         switch(this.curChar) {
         case 97:
            return this.jjMoveStringLiteralDfa5_0(active0, 52913997086720L, active1, 0L);
         case 99:
            return this.jjMoveStringLiteralDfa5_0(active0, 11258999068426240L, active1, 0L);
         case 101:
            if ((active0 & 268435456L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 28, 144);
            } else {
               if ((active0 & Long.MIN_VALUE) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 63, 144);
               }

               return this.jjMoveStringLiteralDfa5_0(active0, 70403103924224L, active1, 0L);
            }
         case 104:
            if ((active0 & 131072L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 17, 144);
            }

            return this.jjMoveStringLiteralDfa5_0(active0, 18014398509481984L, active1, 0L);
         case 105:
            return this.jjMoveStringLiteralDfa5_0(active0, 1266637397295104L, active1, 257L);
         case 107:
            if ((active0 & 16384L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 14, 144);
            }
         case 98:
         case 100:
         case 102:
         case 103:
         case 106:
         case 109:
         case 111:
         case 112:
         case 113:
         default:
            return this.jjStartNfa_0(3, active0, active1, 0L);
         case 108:
            if ((active0 & 536870912L) != 0L) {
               this.jjmatchedKind = 29;
               this.jjmatchedPos = 4;
            }

            return this.jjMoveStringLiteralDfa5_0(active0, 1090519040L, active1, 64L);
         case 110:
            return this.jjMoveStringLiteralDfa5_0(active0, 134217728L, active1, 0L);
         case 114:
            if ((active0 & 4503599627370496L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 52, 144);
            }

            return this.jjMoveStringLiteralDfa5_0(active0, 282093452007424L, active1, 128L);
         case 115:
            if ((active0 & 524288L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 19, 144);
            } else {
               if ((active1 & 16L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 68, 144);
               }

               return this.jjMoveStringLiteralDfa5_0(active0, 288230376151711744L, active1, 512L);
            }
         case 116:
            if ((active0 & 1048576L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 20, 144);
            } else if ((active0 & 2147483648L) != 0L) {
               return this.jjStartNfaWithStates_0(4, 31, 144);
            } else {
               if ((active0 & 562949953421312L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 49, 144);
               }

               return this.jjMoveStringLiteralDfa5_0(active0, 4611686018427387904L, active1, 0L);
            }
         case 117:
            return this.jjMoveStringLiteralDfa5_0(active0, 4194304L, active1, 0L);
         case 118:
            return this.jjMoveStringLiteralDfa5_0(active0, 2199023255552L, active1, 0L);
         case 119:
            if ((active0 & 72057594037927936L) != 0L) {
               this.jjmatchedKind = 56;
               this.jjmatchedPos = 4;
            }

            return this.jjMoveStringLiteralDfa5_0(active0, 144115188075855872L, active1, 0L);
         }
      }
   }

   private int jjMoveStringLiteralDfa5_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | (active1 &= old1)) == 0L) {
         return this.jjStartNfa_0(3, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(4, active0, active1, 0L);
            return 5;
         }

         switch(this.curChar) {
         case 97:
            return this.jjMoveStringLiteralDfa6_0(active0, 10240L, active1, 0L);
         case 98:
         case 106:
         case 107:
         case 111:
         case 112:
         case 113:
         default:
            break;
         case 99:
            if ((active0 & 140737488355328L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 47, 144);
            }

            if ((active0 & 1125899906842624L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 50, 144);
            }

            return this.jjMoveStringLiteralDfa6_0(active0, 70368744177664L, active1, 0L);
         case 100:
            return this.jjMoveStringLiteralDfa6_0(active0, 134217728L, active1, 256L);
         case 101:
            if ((active0 & 16777216L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 24, 144);
            }

            if ((active0 & 2199023255552L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 41, 144);
            }

            if ((active1 & 64L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 70, 144);
            }
            break;
         case 102:
            return this.jjMoveStringLiteralDfa6_0(active0, 549755813888L, active1, 0L);
         case 103:
            return this.jjMoveStringLiteralDfa6_0(active0, 17592186044416L, active1, 0L);
         case 104:
            if ((active0 & 9007199254740992L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 53, 144);
            }
            break;
         case 105:
            return this.jjMoveStringLiteralDfa6_0(active0, 4899916394579099648L, active1, 512L);
         case 108:
            return this.jjMoveStringLiteralDfa6_0(active0, 1077936128L, active1, 0L);
         case 109:
            return this.jjMoveStringLiteralDfa6_0(active0, 34359738368L, active1, 0L);
         case 110:
            if ((active0 & 281474976710656L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 48, 144);
            }

            return this.jjMoveStringLiteralDfa6_0(active0, 137441050624L, active1, 0L);
         case 114:
            return this.jjMoveStringLiteralDfa6_0(active0, 18014398509481984L, active1, 1L);
         case 115:
            if ((active0 & 144115188075855872L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 57, 144);
            }
            break;
         case 116:
            if ((active0 & 4096L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 12, 144);
            }

            if ((active0 & 68719476736L) != 0L) {
               return this.jjStartNfaWithStates_0(5, 36, 144);
            }

            return this.jjMoveStringLiteralDfa6_0(active0, 2286984185774080L, active1, 128L);
         }

         return this.jjStartNfa_0(4, active0, active1, 0L);
      }
   }

   private int jjMoveStringLiteralDfa6_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | (active1 &= old1)) == 0L) {
         return this.jjStartNfa_0(4, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(5, active0, active1, 0L);
            return 6;
         }

         switch(this.curChar) {
         case 97:
            return this.jjMoveStringLiteralDfa7_0(active0, 549755813888L, active1, 0L);
         case 98:
         case 100:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 109:
         case 112:
         case 113:
         case 114:
         case 118:
         case 119:
         case 120:
         default:
            break;
         case 99:
            return this.jjMoveStringLiteralDfa7_0(active0, 137438955520L, active1, 0L);
         case 101:
            if ((active0 & 17592186044416L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 44, 144);
            }

            if ((active0 & 35184372088832L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 45, 144);
            }

            return this.jjMoveStringLiteralDfa7_0(active0, 288230410511450112L, active1, 257L);
         case 102:
            return this.jjMoveStringLiteralDfa7_0(active0, 2251799813685248L, active1, 0L);
         case 108:
            return this.jjMoveStringLiteralDfa7_0(active0, 4611686018427387904L, active1, 0L);
         case 110:
            if ((active0 & 8192L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 13, 144);
            }
            break;
         case 111:
            return this.jjMoveStringLiteralDfa7_0(active0, 18014398509481984L, active1, 0L);
         case 115:
            if ((active0 & 134217728L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 27, 144);
            }

            if ((active1 & 128L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 71, 144);
            }
            break;
         case 116:
            if ((active0 & 4194304L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 22, 144);
            }

            return this.jjMoveStringLiteralDfa7_0(active0, 70368744177664L, active1, 512L);
         case 117:
            return this.jjMoveStringLiteralDfa7_0(active0, 2097152L, active1, 0L);
         case 121:
            if ((active0 & 1073741824L) != 0L) {
               return this.jjStartNfaWithStates_0(6, 30, 144);
            }
         }

         return this.jjStartNfa_0(5, active0, active1, 0L);
      }
   }

   private int jjMoveStringLiteralDfa7_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | (active1 &= old1)) == 0L) {
         return this.jjStartNfa_0(5, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(6, active0, active1, 0L);
            return 7;
         }

         switch(this.curChar) {
         case 99:
            return this.jjMoveStringLiteralDfa8_0(active0, 549755813888L, active1, 0L);
         case 100:
         case 102:
         case 103:
         case 104:
         case 106:
         case 107:
         case 108:
         case 109:
         case 111:
         case 113:
         case 114:
         default:
            break;
         case 101:
            if ((active0 & 2097152L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 21, 144);
            }

            if ((active0 & 4611686018427387904L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 62, 144);
            }

            return this.jjMoveStringLiteralDfa8_0(active0, 70506183131136L, active1, 0L);
         case 105:
            return this.jjMoveStringLiteralDfa8_0(active0, 0L, active1, 512L);
         case 110:
            return this.jjMoveStringLiteralDfa8_0(active0, 306244809020932096L, active1, 0L);
         case 112:
            if ((active0 & 2251799813685248L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 51, 144);
            }
            break;
         case 115:
            if ((active1 & 1L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 64, 144);
            }

            if ((active1 & 256L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 72, 144);
            }
            break;
         case 116:
            if ((active0 & 2048L) != 0L) {
               return this.jjStartNfaWithStates_0(7, 11, 144);
            }
         }

         return this.jjStartNfa_0(6, active0, active1, 0L);
      }
   }

   private int jjMoveStringLiteralDfa8_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | (active1 &= old1)) == 0L) {
         return this.jjStartNfa_0(6, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(7, active0, active1, 0L);
            return 8;
         }

         switch(this.curChar) {
         case 100:
            if ((active0 & 70368744177664L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 46, 144);
            }
            break;
         case 101:
            if ((active0 & 549755813888L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 39, 144);
            }
         case 102:
         case 103:
         case 104:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 112:
         case 113:
         case 114:
         case 115:
         case 117:
         default:
            break;
         case 105:
            return this.jjMoveStringLiteralDfa9_0(active0, 18014398509481984L, active1, 0L);
         case 111:
            return this.jjMoveStringLiteralDfa9_0(active0, 137438953472L, active1, 0L);
         case 116:
            if ((active0 & 288230376151711744L) != 0L) {
               return this.jjStartNfaWithStates_0(8, 58, 144);
            }

            return this.jjMoveStringLiteralDfa9_0(active0, 34359738368L, active1, 0L);
         case 118:
            return this.jjMoveStringLiteralDfa9_0(active0, 0L, active1, 512L);
         }

         return this.jjStartNfa_0(7, active0, active1, 0L);
      }
   }

   private int jjMoveStringLiteralDfa9_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | (active1 &= old1)) == 0L) {
         return this.jjStartNfa_0(7, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(8, active0, active1, 0L);
            return 9;
         }

         switch(this.curChar) {
         case 101:
            if ((active1 & 512L) != 0L) {
               return this.jjStartNfaWithStates_0(9, 73, 144);
            }
            break;
         case 102:
            if ((active0 & 137438953472L) != 0L) {
               return this.jjStartNfaWithStates_0(9, 37, 144);
            }
            break;
         case 115:
            if ((active0 & 34359738368L) != 0L) {
               return this.jjStartNfaWithStates_0(9, 35, 144);
            }
            break;
         case 122:
            return this.jjMoveStringLiteralDfa10_0(active0, 18014398509481984L, active1, 0L);
         }

         return this.jjStartNfa_0(8, active0, active1, 0L);
      }
   }

   private int jjMoveStringLiteralDfa10_0(long old0, long active0, long old1, long active1) {
      if (((active0 &= old0) | active1 & old1) == 0L) {
         return this.jjStartNfa_0(8, old0, old1, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var10) {
            this.jjStopStringLiteralDfa_0(9, active0, 0L, 0L);
            return 10;
         }

         switch(this.curChar) {
         case 101:
            return this.jjMoveStringLiteralDfa11_0(active0, 18014398509481984L);
         default:
            return this.jjStartNfa_0(9, active0, 0L, 0L);
         }
      }
   }

   private int jjMoveStringLiteralDfa11_0(long old0, long active0) {
      if ((active0 &= old0) == 0L) {
         return this.jjStartNfa_0(9, old0, 0L, 0L);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(10, active0, 0L, 0L);
            return 11;
         }

         switch(this.curChar) {
         case 100:
            if ((active0 & 18014398509481984L) != 0L) {
               return this.jjStartNfaWithStates_0(11, 54, 144);
            }
         default:
            return this.jjStartNfa_0(10, active0, 0L, 0L);
         }
      }
   }

   private int jjStartNfaWithStates_0(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_0(state, pos + 1);
   }

   private int jjMoveNfa_0(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 144;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while(true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         long l;
         if (this.curChar < 64) {
            l = 1L << this.curChar;

            do {
               --i;
               switch(this.jjstateSet[i]) {
               case 0:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddStates(3, 17);
                  } else if ((4294971904L & l) != 0L) {
                     if (kind > 1) {
                        kind = 1;
                     }
                  } else if (this.curChar == 47) {
                     this.jjAddStates(18, 19);
                  } else if (this.curChar == 36) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  } else if (this.curChar == 34) {
                     this.jjCheckNAddStates(20, 23);
                  } else if (this.curChar == 39) {
                     this.jjAddStates(24, 26);
                  } else if (this.curChar == 46) {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }

                  if (this.curChar == 48) {
                     this.jjAddStates(27, 34);
                  }
                  break;
               case 1:
                  if (this.curChar == 46) {
                     this.jjstateSet[this.jjnewStateCnt++] = 2;
                  }
                  break;
               case 2:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(35, 38);
                  }
                  break;
               case 3:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(3, 4);
                  }
                  break;
               case 4:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddTwoStates(5, 10);
                  }
               case 5:
               case 10:
               case 15:
               case 21:
               case 26:
               case 29:
               case 31:
               case 36:
               case 44:
               case 49:
               case 50:
               case 54:
               case 67:
               case 72:
               case 80:
               case 92:
               case 99:
               case 106:
               case 110:
               case 117:
               case 121:
               case 129:
               case 134:
               case 139:
               default:
                  break;
               case 6:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(7);
                  }
                  break;
               case 7:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(39, 41);
                  }
                  break;
               case 8:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(8, 9);
                  }
                  break;
               case 9:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddTwoStates(7, 10);
                  }
                  break;
               case 11:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(42, 45);
                  }
                  break;
               case 12:
                  if (this.curChar == 39) {
                     this.jjAddStates(24, 26);
                  }
                  break;
               case 13:
                  if ((-549755823105L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 14:
                  if (this.curChar == 39 && kind > 87) {
                     kind = 87;
                  }
                  break;
               case 16:
                  if ((566935683072L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 17:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddTwoStates(18, 14);
                  }
                  break;
               case 18:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 19:
                  if ((4222124650659840L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 20;
                  }
                  break;
               case 20:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(18);
                  }
                  break;
               case 22:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 23;
                  }
                  break;
               case 23:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 24;
                  }
                  break;
               case 24:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 25;
                  }
                  break;
               case 25:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 27:
                  if (this.curChar == 34) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 28:
                  if ((-17179878401L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 30:
                  if ((566935683072L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 32:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 33;
                  }
                  break;
               case 33:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 34;
                  }
                  break;
               case 34:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 35;
                  }
                  break;
               case 35:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 37:
                  if (this.curChar == 34 && kind > 88) {
                     kind = 88;
                  }
                  break;
               case 38:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(46, 50);
                  }
                  break;
               case 39:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 40:
                  if ((4222124650659840L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 41;
                  }
                  break;
               case 41:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(39);
                  }
                  break;
               case 42:
                  if (this.curChar == 36) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 43:
               case 144:
                  if ((287948970162897407L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 45:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 46;
                  }
                  break;
               case 46:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 47;
                  }
                  break;
               case 47:
               case 53:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(48);
                  }
                  break;
               case 48:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 51:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 52;
                  }
                  break;
               case 52:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 53;
                  }
                  break;
               case 55:
                  if (this.curChar == 47) {
                     this.jjAddStates(18, 19);
                  }
                  break;
               case 56:
                  if (this.curChar == 42) {
                     this.jjstateSet[this.jjnewStateCnt++] = 61;
                  } else if (this.curChar == 47) {
                     if (kind > 5) {
                        kind = 5;
                     }

                     this.jjCheckNAddStates(0, 2);
                  }
                  break;
               case 57:
                  if ((-9217L & l) != 0L) {
                     if (kind > 5) {
                        kind = 5;
                     }

                     this.jjCheckNAddStates(0, 2);
                  }
                  break;
               case 58:
                  if ((9216L & l) != 0L && kind > 5) {
                     kind = 5;
                  }
                  break;
               case 59:
                  if (this.curChar == 10 && kind > 5) {
                     kind = 5;
                  }
                  break;
               case 60:
                  if (this.curChar == 13) {
                     this.jjstateSet[this.jjnewStateCnt++] = 59;
                  }
                  break;
               case 61:
                  if (this.curChar == 42) {
                     this.jjstateSet[this.jjnewStateCnt++] = 62;
                  }
                  break;
               case 62:
                  if ((-140737488355329L & l) != 0L && kind > 6) {
                     kind = 6;
                  }
                  break;
               case 63:
                  if (this.curChar == 42) {
                     this.jjstateSet[this.jjnewStateCnt++] = 61;
                  }
                  break;
               case 64:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddStates(3, 17);
                  }
                  break;
               case 65:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(65, 66);
                  }
                  break;
               case 66:
               case 102:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(67);
                  }
                  break;
               case 68:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(68, 69);
                  }
                  break;
               case 69:
               case 113:
                  if ((287948901175001088L & l) != 0L && kind > 75) {
                     kind = 75;
                  }
                  break;
               case 70:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(70, 71);
                  }
                  break;
               case 71:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(72, 10);
                  }
                  break;
               case 73:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(74);
                  }
                  break;
               case 74:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(51, 53);
                  }
                  break;
               case 75:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(75, 76);
                  }
                  break;
               case 76:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(74, 10);
                  }
                  break;
               case 77:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(54, 57);
                  }
                  break;
               case 78:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(78, 79);
                  }
                  break;
               case 79:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(80);
                  }
                  break;
               case 81:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(82);
                  }
                  break;
               case 82:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(58, 60);
                  }
                  break;
               case 83:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(83, 84);
                  }
                  break;
               case 84:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddTwoStates(82, 10);
                  }
                  break;
               case 85:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(61, 64);
                  }
                  break;
               case 86:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(86, 87);
                  }
                  break;
               case 87:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(88);
                  }
                  break;
               case 88:
                  if (this.curChar == 46) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(65, 67);
                  }
                  break;
               case 89:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(68, 71);
                  }
                  break;
               case 90:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(90, 91);
                  }
                  break;
               case 91:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddTwoStates(92, 10);
                  }
                  break;
               case 93:
                  if ((43980465111040L & l) != 0L) {
                     this.jjCheckNAdd(94);
                  }
                  break;
               case 94:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(72, 74);
                  }
                  break;
               case 95:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(95, 96);
                  }
                  break;
               case 96:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddTwoStates(94, 10);
                  }
                  break;
               case 97:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(75, 78);
                  }
                  break;
               case 98:
                  if (this.curChar == 48) {
                     this.jjAddStates(27, 34);
                  }
                  break;
               case 100:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(79, 81);
                  }
                  break;
               case 101:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(101, 102);
                  }
                  break;
               case 103:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddStates(82, 84);
                  }
                  break;
               case 104:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddTwoStates(104, 105);
                  }
                  break;
               case 105:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAdd(67);
                  }
                  break;
               case 107:
                  if ((844424930131968L & l) != 0L) {
                     this.jjCheckNAddStates(85, 87);
                  }
                  break;
               case 108:
                  if ((844424930131968L & l) != 0L) {
                     this.jjCheckNAddTwoStates(108, 109);
                  }
                  break;
               case 109:
                  if ((844424930131968L & l) != 0L) {
                     this.jjCheckNAdd(67);
                  }
                  break;
               case 111:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddTwoStates(112, 113);
                  }
                  break;
               case 112:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(112, 113);
                  }
                  break;
               case 114:
                  if ((71776119061217280L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddTwoStates(115, 116);
                  }
                  break;
               case 115:
                  if ((71776119061217280L & l) != 0L) {
                     this.jjCheckNAddTwoStates(115, 116);
                  }
                  break;
               case 116:
                  if ((71776119061217280L & l) != 0L && kind > 75) {
                     kind = 75;
                  }
                  break;
               case 118:
                  if ((844424930131968L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddTwoStates(119, 120);
                  }
                  break;
               case 119:
                  if ((844424930131968L & l) != 0L) {
                     this.jjCheckNAddTwoStates(119, 120);
                  }
                  break;
               case 120:
                  if ((844424930131968L & l) != 0L && kind > 75) {
                     kind = 75;
                  }
                  break;
               case 122:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(88, 90);
                  }
                  break;
               case 123:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(123, 124);
                  }
                  break;
               case 124:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(125);
                  }
                  break;
               case 125:
                  if (this.curChar == 46) {
                     this.jjstateSet[this.jjnewStateCnt++] = 126;
                  }
                  break;
               case 126:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(91, 93);
                  }
                  break;
               case 127:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(127, 128);
                  }
                  break;
               case 128:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAdd(129);
                  }
                  break;
               case 130:
                  if ((43980465111040L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 131;
                  }
                  break;
               case 131:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(94, 96);
                  }
                  break;
               case 132:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(132, 133);
                  }
                  break;
               case 133:
               case 143:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAdd(10);
                  }
                  break;
               case 135:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddStates(97, 100);
                  }
                  break;
               case 136:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(136, 137);
                  }
                  break;
               case 137:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(138, 139);
                  }
                  break;
               case 138:
                  if (this.curChar == 46) {
                     this.jjCheckNAdd(139);
                  }
                  break;
               case 140:
                  if ((43980465111040L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 141;
                  }
                  break;
               case 141:
                  if ((287948901175001088L & l) != 0L) {
                     if (kind > 80) {
                        kind = 80;
                     }

                     this.jjCheckNAddStates(101, 103);
                  }
                  break;
               case 142:
                  if ((287948901175001088L & l) != 0L) {
                     this.jjCheckNAddTwoStates(142, 143);
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
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  } else if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 50;
                  }
               case 1:
               case 2:
               case 4:
               case 6:
               case 7:
               case 9:
               case 11:
               case 12:
               case 14:
               case 17:
               case 18:
               case 19:
               case 20:
               case 27:
               case 37:
               case 38:
               case 39:
               case 40:
               case 41:
               case 55:
               case 56:
               case 58:
               case 59:
               case 60:
               case 61:
               case 63:
               case 64:
               case 66:
               case 69:
               case 71:
               case 73:
               case 74:
               case 76:
               case 77:
               case 79:
               case 81:
               case 82:
               case 84:
               case 85:
               case 87:
               case 88:
               case 89:
               case 91:
               case 93:
               case 94:
               case 96:
               case 97:
               case 98:
               case 103:
               case 105:
               case 107:
               case 109:
               case 114:
               case 116:
               case 118:
               case 120:
               case 125:
               case 130:
               case 131:
               case 133:
               case 138:
               case 140:
               case 141:
               case 143:
               default:
                  break;
               case 3:
                  if (this.curChar == 95) {
                     this.jjAddStates(104, 105);
                  }
                  break;
               case 5:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(106, 107);
                  }
                  break;
               case 8:
                  if (this.curChar == 95) {
                     this.jjAddStates(108, 109);
                  }
                  break;
               case 10:
                  if ((343597383760L & l) != 0L && kind > 80) {
                     kind = 80;
                  }
                  break;
               case 13:
                  if ((-268435457L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 15:
                  if (this.curChar == 92) {
                     this.jjAddStates(110, 112);
                  }
                  break;
               case 16:
                  if ((5700160604602368L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 21:
                  if (this.curChar == 117) {
                     this.jjstateSet[this.jjnewStateCnt++] = 22;
                  }
                  break;
               case 22:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 23;
                  }
                  break;
               case 23:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 24;
                  }
                  break;
               case 24:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 25;
                  }
                  break;
               case 25:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAdd(14);
                  }
                  break;
               case 26:
                  if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 21;
                  }
                  break;
               case 28:
                  if ((-268435457L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 29:
                  if (this.curChar == 92) {
                     this.jjAddStates(113, 115);
                  }
                  break;
               case 30:
                  if ((5700160604602368L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 31:
                  if (this.curChar == 117) {
                     this.jjstateSet[this.jjnewStateCnt++] = 32;
                  }
                  break;
               case 32:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 33;
                  }
                  break;
               case 33:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 34;
                  }
                  break;
               case 34:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 35;
                  }
                  break;
               case 35:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddStates(20, 23);
                  }
                  break;
               case 36:
                  if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 31;
                  }
                  break;
               case 42:
                  if ((576460745995190270L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 43:
                  if ((-8646911290859585538L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 44:
                  if (this.curChar == 117) {
                     this.jjstateSet[this.jjnewStateCnt++] = 45;
                  }
                  break;
               case 45:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 46;
                  }
                  break;
               case 46:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 47;
                  }
                  break;
               case 47:
               case 53:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAdd(48);
                  }
                  break;
               case 48:
                  if ((541165879422L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 49:
                  if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 44;
                  }
                  break;
               case 50:
                  if (this.curChar == 117) {
                     this.jjstateSet[this.jjnewStateCnt++] = 51;
                  }
                  break;
               case 51:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 52;
                  }
                  break;
               case 52:
                  if ((541165879422L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 53;
                  }
                  break;
               case 54:
                  if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 50;
                  }
                  break;
               case 57:
                  if (kind > 5) {
                     kind = 5;
                  }

                  this.jjAddStates(0, 2);
                  break;
               case 62:
                  if (kind > 6) {
                     kind = 6;
                  }
                  break;
               case 65:
                  if (this.curChar == 95) {
                     this.jjAddStates(116, 117);
                  }
                  break;
               case 67:
                  if ((17592186048512L & l) != 0L && kind > 74) {
                     kind = 74;
                  }
                  break;
               case 68:
                  if (this.curChar == 95) {
                     this.jjAddStates(118, 119);
                  }
                  break;
               case 70:
                  if (this.curChar == 95) {
                     this.jjAddStates(120, 121);
                  }
                  break;
               case 72:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(122, 123);
                  }
                  break;
               case 75:
                  if (this.curChar == 95) {
                     this.jjAddStates(124, 125);
                  }
                  break;
               case 78:
                  if (this.curChar == 95) {
                     this.jjAddStates(126, 127);
                  }
                  break;
               case 80:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(128, 129);
                  }
                  break;
               case 83:
                  if (this.curChar == 95) {
                     this.jjAddStates(130, 131);
                  }
                  break;
               case 86:
                  if (this.curChar == 95) {
                     this.jjAddStates(132, 133);
                  }
                  break;
               case 90:
                  if (this.curChar == 95) {
                     this.jjAddStates(134, 135);
                  }
                  break;
               case 92:
                  if ((137438953504L & l) != 0L) {
                     this.jjAddStates(136, 137);
                  }
                  break;
               case 95:
                  if (this.curChar == 95) {
                     this.jjAddStates(138, 139);
                  }
                  break;
               case 99:
                  if ((72057594054705152L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 100;
                  }
                  break;
               case 100:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddStates(79, 81);
                  }
                  break;
               case 101:
                  if ((543313363070L & l) != 0L) {
                     this.jjCheckNAddTwoStates(101, 102);
                  }
                  break;
               case 102:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAdd(67);
                  }
                  break;
               case 104:
                  if (this.curChar == 95) {
                     this.jjAddStates(140, 141);
                  }
                  break;
               case 106:
                  if ((17179869188L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 107;
                  }
                  break;
               case 108:
                  if (this.curChar == 95) {
                     this.jjAddStates(142, 143);
                  }
                  break;
               case 110:
                  if ((72057594054705152L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 111;
                  }
                  break;
               case 111:
                  if ((541165879422L & l) != 0L) {
                     if (kind > 75) {
                        kind = 75;
                     }

                     this.jjCheckNAddTwoStates(112, 113);
                  }
                  break;
               case 112:
                  if ((543313363070L & l) != 0L) {
                     this.jjCheckNAddTwoStates(112, 113);
                  }
                  break;
               case 113:
                  if ((541165879422L & l) != 0L && kind > 75) {
                     kind = 75;
                  }
                  break;
               case 115:
                  if (this.curChar == 95) {
                     this.jjAddStates(144, 145);
                  }
                  break;
               case 117:
                  if ((17179869188L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 118;
                  }
                  break;
               case 119:
                  if (this.curChar == 95) {
                     this.jjAddStates(146, 147);
                  }
                  break;
               case 121:
                  if ((72057594054705152L & l) != 0L) {
                     this.jjCheckNAddTwoStates(122, 125);
                  }
                  break;
               case 122:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddStates(88, 90);
                  }
                  break;
               case 123:
                  if ((543313363070L & l) != 0L) {
                     this.jjCheckNAddTwoStates(123, 124);
                  }
                  break;
               case 124:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAdd(125);
                  }
                  break;
               case 126:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddStates(91, 93);
                  }
                  break;
               case 127:
                  if ((543313363070L & l) != 0L) {
                     this.jjCheckNAddTwoStates(127, 128);
                  }
                  break;
               case 128:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAdd(129);
                  }
                  break;
               case 129:
                  if ((281474976776192L & l) != 0L) {
                     this.jjAddStates(148, 149);
                  }
                  break;
               case 132:
                  if (this.curChar == 95) {
                     this.jjAddStates(150, 151);
                  }
                  break;
               case 134:
                  if ((72057594054705152L & l) != 0L) {
                     this.jjstateSet[this.jjnewStateCnt++] = 135;
                  }
                  break;
               case 135:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddStates(97, 100);
                  }
                  break;
               case 136:
                  if ((543313363070L & l) != 0L) {
                     this.jjCheckNAddTwoStates(136, 137);
                  }
                  break;
               case 137:
                  if ((541165879422L & l) != 0L) {
                     this.jjCheckNAddTwoStates(138, 139);
                  }
                  break;
               case 139:
                  if ((281474976776192L & l) != 0L) {
                     this.jjAddStates(152, 153);
                  }
                  break;
               case 142:
                  if (this.curChar == 95) {
                     this.jjAddStates(154, 155);
                  }
                  break;
               case 144:
                  if ((-8646911290859585538L & l) != 0L) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  } else if (this.curChar == 92) {
                     this.jjstateSet[this.jjnewStateCnt++] = 44;
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
               case 0:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2) && kind > 1) {
                     kind = 1;
                  }

                  if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 13:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
                     this.jjstateSet[this.jjnewStateCnt++] = 14;
                  }
                  break;
               case 28:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
                     this.jjAddStates(20, 23);
                  }
                  break;
               case 42:
                  if (jjCanMove_2(hiByte, i1, i2, l1, l2)) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 43:
               case 144:
                  if (jjCanMove_3(hiByte, i1, i2, l1, l2)) {
                     if (kind > 89) {
                        kind = 89;
                     }

                     this.jjCheckNAddTwoStates(43, 49);
                  }
                  break;
               case 57:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2)) {
                     if (kind > 5) {
                        kind = 5;
                     }

                     this.jjAddStates(0, 2);
                  }
                  break;
               case 62:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2) && kind > 6) {
                     kind = 6;
                  }
                  break;
               default:
                  if (i1 != 0 && l1 != 0L && i2 != 0 && l2 == 0L) {
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
         if ((i = this.jjnewStateCnt) == (startsAt = 144 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var13) {
            return curPos;
         }
      }
   }

   private int jjMoveStringLiteralDfa0_2() {
      switch(this.curChar) {
      case 42:
         return this.jjMoveStringLiteralDfa1_2(512L);
      default:
         return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_2(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch(this.curChar) {
      case 47:
         if ((active0 & 512L) != 0L) {
            return this.jjStopAtPos(1, 9);
         }

         return 2;
      default:
         return 2;
      }
   }

   private int jjMoveStringLiteralDfa0_1() {
      switch(this.curChar) {
      case 42:
         return this.jjMoveStringLiteralDfa1_1(256L);
      default:
         return 1;
      }
   }

   private int jjMoveStringLiteralDfa1_1(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         return 1;
      }

      switch(this.curChar) {
      case 47:
         if ((active0 & 256L) != 0L) {
            return this.jjStopAtPos(1, 8);
         }

         return 2;
      default:
         return 2;
      }
   }

   protected Token jjFillToken() {
      String im = jjstrLiteralImages[this.jjmatchedKind];
      String curTokenImage = im == null ? this.input_stream.GetImage() : im;
      int beginLine = this.input_stream.getBeginLine();
      int beginColumn = this.input_stream.getBeginColumn();
      int endLine = this.input_stream.getEndLine();
      int endColumn = this.input_stream.getEndColumn();
      Token t = Token.newToken(this.jjmatchedKind);
      t.kind = this.jjmatchedKind;
      t.image = curTokenImage;
      t.beginLine = beginLine;
      t.endLine = endLine;
      t.beginColumn = beginColumn;
      t.endColumn = endColumn;
      return t;
   }

   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2) {
      switch(hiByte) {
      case 0:
         return (jjbitVec0[i2] & l2) != 0L;
      case 22:
         return (jjbitVec1[i2] & l2) != 0L;
      case 24:
         return (jjbitVec2[i2] & l2) != 0L;
      case 32:
         return (jjbitVec3[i2] & l2) != 0L;
      case 48:
         return (jjbitVec4[i2] & l2) != 0L;
      case 254:
         return (jjbitVec5[i2] & l2) != 0L;
      default:
         return false;
      }
   }

   private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2) {
      switch(hiByte) {
      case 0:
         return (jjbitVec8[i2] & l2) != 0L;
      default:
         return (jjbitVec6[i1] & l1) != 0L;
      }
   }

   private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2) {
      switch(hiByte) {
      case 0:
         return (jjbitVec10[i2] & l2) != 0L;
      case 2:
         return (jjbitVec11[i2] & l2) != 0L;
      case 3:
         return (jjbitVec12[i2] & l2) != 0L;
      case 4:
         return (jjbitVec13[i2] & l2) != 0L;
      case 5:
         return (jjbitVec14[i2] & l2) != 0L;
      case 6:
         return (jjbitVec15[i2] & l2) != 0L;
      case 7:
         return (jjbitVec16[i2] & l2) != 0L;
      case 8:
         return (jjbitVec17[i2] & l2) != 0L;
      case 9:
         return (jjbitVec18[i2] & l2) != 0L;
      case 10:
         return (jjbitVec19[i2] & l2) != 0L;
      case 11:
         return (jjbitVec20[i2] & l2) != 0L;
      case 12:
         return (jjbitVec21[i2] & l2) != 0L;
      case 13:
         return (jjbitVec22[i2] & l2) != 0L;
      case 14:
         return (jjbitVec23[i2] & l2) != 0L;
      case 15:
         return (jjbitVec24[i2] & l2) != 0L;
      case 16:
         return (jjbitVec25[i2] & l2) != 0L;
      case 18:
         return (jjbitVec26[i2] & l2) != 0L;
      case 19:
         return (jjbitVec27[i2] & l2) != 0L;
      case 20:
         return (jjbitVec6[i2] & l2) != 0L;
      case 22:
         return (jjbitVec28[i2] & l2) != 0L;
      case 23:
         return (jjbitVec29[i2] & l2) != 0L;
      case 24:
         return (jjbitVec30[i2] & l2) != 0L;
      case 25:
         return (jjbitVec31[i2] & l2) != 0L;
      case 26:
         return (jjbitVec32[i2] & l2) != 0L;
      case 27:
         return (jjbitVec33[i2] & l2) != 0L;
      case 28:
         return (jjbitVec34[i2] & l2) != 0L;
      case 29:
         return (jjbitVec35[i2] & l2) != 0L;
      case 31:
         return (jjbitVec36[i2] & l2) != 0L;
      case 32:
         return (jjbitVec37[i2] & l2) != 0L;
      case 33:
         return (jjbitVec38[i2] & l2) != 0L;
      case 44:
         return (jjbitVec39[i2] & l2) != 0L;
      case 45:
         return (jjbitVec40[i2] & l2) != 0L;
      case 46:
         return (jjbitVec41[i2] & l2) != 0L;
      case 48:
         return (jjbitVec42[i2] & l2) != 0L;
      case 49:
         return (jjbitVec43[i2] & l2) != 0L;
      case 77:
         return (jjbitVec44[i2] & l2) != 0L;
      case 159:
         return (jjbitVec45[i2] & l2) != 0L;
      case 164:
         return (jjbitVec46[i2] & l2) != 0L;
      case 166:
         return (jjbitVec47[i2] & l2) != 0L;
      case 167:
         return (jjbitVec48[i2] & l2) != 0L;
      case 168:
         return (jjbitVec49[i2] & l2) != 0L;
      case 169:
         return (jjbitVec50[i2] & l2) != 0L;
      case 170:
         return (jjbitVec51[i2] & l2) != 0L;
      case 171:
         return (jjbitVec52[i2] & l2) != 0L;
      case 215:
         return (jjbitVec53[i2] & l2) != 0L;
      case 250:
         return (jjbitVec54[i2] & l2) != 0L;
      case 251:
         return (jjbitVec55[i2] & l2) != 0L;
      case 253:
         return (jjbitVec56[i2] & l2) != 0L;
      case 254:
         return (jjbitVec57[i2] & l2) != 0L;
      case 255:
         return (jjbitVec58[i2] & l2) != 0L;
      default:
         return (jjbitVec9[i1] & l1) != 0L;
      }
   }

   private static final boolean jjCanMove_3(int hiByte, int i1, int i2, long l1, long l2) {
      switch(hiByte) {
      case 0:
         return (jjbitVec59[i2] & l2) != 0L;
      case 2:
         return (jjbitVec11[i2] & l2) != 0L;
      case 3:
         return (jjbitVec60[i2] & l2) != 0L;
      case 4:
         return (jjbitVec61[i2] & l2) != 0L;
      case 5:
         return (jjbitVec62[i2] & l2) != 0L;
      case 6:
         return (jjbitVec63[i2] & l2) != 0L;
      case 7:
         return (jjbitVec64[i2] & l2) != 0L;
      case 8:
         return (jjbitVec65[i2] & l2) != 0L;
      case 9:
         return (jjbitVec66[i2] & l2) != 0L;
      case 10:
         return (jjbitVec67[i2] & l2) != 0L;
      case 11:
         return (jjbitVec68[i2] & l2) != 0L;
      case 12:
         return (jjbitVec69[i2] & l2) != 0L;
      case 13:
         return (jjbitVec70[i2] & l2) != 0L;
      case 14:
         return (jjbitVec71[i2] & l2) != 0L;
      case 15:
         return (jjbitVec72[i2] & l2) != 0L;
      case 16:
         return (jjbitVec73[i2] & l2) != 0L;
      case 18:
         return (jjbitVec26[i2] & l2) != 0L;
      case 19:
         return (jjbitVec74[i2] & l2) != 0L;
      case 20:
         return (jjbitVec6[i2] & l2) != 0L;
      case 22:
         return (jjbitVec28[i2] & l2) != 0L;
      case 23:
         return (jjbitVec75[i2] & l2) != 0L;
      case 24:
         return (jjbitVec76[i2] & l2) != 0L;
      case 25:
         return (jjbitVec77[i2] & l2) != 0L;
      case 26:
         return (jjbitVec78[i2] & l2) != 0L;
      case 27:
         return (jjbitVec79[i2] & l2) != 0L;
      case 28:
         return (jjbitVec80[i2] & l2) != 0L;
      case 29:
         return (jjbitVec81[i2] & l2) != 0L;
      case 31:
         return (jjbitVec36[i2] & l2) != 0L;
      case 32:
         return (jjbitVec82[i2] & l2) != 0L;
      case 33:
         return (jjbitVec38[i2] & l2) != 0L;
      case 44:
         return (jjbitVec83[i2] & l2) != 0L;
      case 45:
         return (jjbitVec84[i2] & l2) != 0L;
      case 46:
         return (jjbitVec41[i2] & l2) != 0L;
      case 48:
         return (jjbitVec85[i2] & l2) != 0L;
      case 49:
         return (jjbitVec43[i2] & l2) != 0L;
      case 77:
         return (jjbitVec44[i2] & l2) != 0L;
      case 159:
         return (jjbitVec45[i2] & l2) != 0L;
      case 164:
         return (jjbitVec46[i2] & l2) != 0L;
      case 166:
         return (jjbitVec86[i2] & l2) != 0L;
      case 167:
         return (jjbitVec48[i2] & l2) != 0L;
      case 168:
         return (jjbitVec87[i2] & l2) != 0L;
      case 169:
         return (jjbitVec88[i2] & l2) != 0L;
      case 170:
         return (jjbitVec89[i2] & l2) != 0L;
      case 171:
         return (jjbitVec90[i2] & l2) != 0L;
      case 215:
         return (jjbitVec53[i2] & l2) != 0L;
      case 250:
         return (jjbitVec54[i2] & l2) != 0L;
      case 251:
         return (jjbitVec91[i2] & l2) != 0L;
      case 253:
         return (jjbitVec56[i2] & l2) != 0L;
      case 254:
         return (jjbitVec92[i2] & l2) != 0L;
      case 255:
         return (jjbitVec93[i2] & l2) != 0L;
      default:
         return (jjbitVec9[i1] & l1) != 0L;
      }
   }

   public Token getNextToken() {
      Token specialToken = null;
      int curPos = 0;

      label106:
      while(true) {
         Token matchedToken;
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (Exception var9) {
            this.jjmatchedKind = 0;
            this.jjmatchedPos = -1;
            matchedToken = this.jjFillToken();
            matchedToken.specialToken = specialToken;
            this.CommonTokenAction(matchedToken);
            return matchedToken;
         }

         this.image = this.jjimage;
         this.image.setLength(0);
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
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 10) {
                  this.jjmatchedKind = 10;
               }
               break;
            case 2:
               this.jjmatchedKind = Integer.MAX_VALUE;
               this.jjmatchedPos = 0;
               curPos = this.jjMoveStringLiteralDfa0_2();
               if (this.jjmatchedPos == 0 && this.jjmatchedKind > 10) {
                  this.jjmatchedKind = 10;
               }
            }

            if (this.jjmatchedKind == Integer.MAX_VALUE) {
               break label106;
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

               this.CommonTokenAction(matchedToken);
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
               break label106;
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
         if (this.curChar != 10 && this.curChar != 13) {
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

      throw new TokenMgrException(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
   }

   void SkipLexicalActions(Token matchedToken) {
      switch(this.jjmatchedKind) {
      default:
      }
   }

   void MoreLexicalActions() {
      this.jjimageLen += this.lengthOfMatch = this.jjmatchedPos + 1;
      switch(this.jjmatchedKind) {
      case 6:
         this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
         this.jjimageLen = 0;
         this.input_stream.backup(1);
      default:
      }
   }

   void TokenLexicalActions(Token matchedToken) {
      switch(this.jjmatchedKind) {
      case 139:
         this.image.append(jjstrLiteralImages[139]);
         this.lengthOfMatch = jjstrLiteralImages[139].length();
         matchedToken.kind = 141;
         matchedToken.realKind = 139;
         this.input_stream.backup(2);
         break;
      case 140:
         this.image.append(jjstrLiteralImages[140]);
         this.lengthOfMatch = jjstrLiteralImages[140].length();
         matchedToken.kind = 141;
         matchedToken.realKind = 140;
         this.input_stream.backup(1);
      }

   }

   private void jjCheckNAdd(int state) {
      if (this.jjrounds[state] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = state;
         this.jjrounds[state] = this.jjround;
      }

   }

   private void jjAddStates(int start, int end) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
      } while(start++ != end);

   }

   private void jjCheckNAddTwoStates(int state1, int state2) {
      this.jjCheckNAdd(state1);
      this.jjCheckNAdd(state2);
   }

   private void jjCheckNAddStates(int start, int end) {
      do {
         this.jjCheckNAdd(jjnextStates[start]);
      } while(start++ != end);

   }

   public GeneratedJavaParserTokenManager(SimpleCharStream stream) {
      this.debugStream = System.out;
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.jjrounds = new int[144];
      this.jjstateSet = new int[288];
      this.jjimage = new StringBuilder();
      this.image = this.jjimage;
      this.input_stream = stream;
   }

   public GeneratedJavaParserTokenManager(SimpleCharStream stream, int lexState) {
      this.debugStream = System.out;
      this.curLexState = 0;
      this.defaultLexState = 0;
      this.jjrounds = new int[144];
      this.jjstateSet = new int[288];
      this.jjimage = new StringBuilder();
      this.image = this.jjimage;
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void ReInit(SimpleCharStream stream) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = stream;
      this.ReInitRounds();
   }

   private void ReInitRounds() {
      this.jjround = -2147483647;

      for(int i = 144; i-- > 0; this.jjrounds[i] = Integer.MIN_VALUE) {
      }

   }

   public void ReInit(SimpleCharStream stream, int lexState) {
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void SwitchTo(int lexState) {
      if (lexState < 3 && lexState >= 0) {
         this.curLexState = lexState;
      } else {
         throw new TokenMgrException("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
      }
   }
}
