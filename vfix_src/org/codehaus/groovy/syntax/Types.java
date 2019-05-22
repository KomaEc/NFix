package org.codehaus.groovy.syntax;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.codehaus.groovy.GroovyBugError;

public class Types {
   public static final int EOF = -1;
   public static final int UNKNOWN = 0;
   public static final int NEWLINE = 5;
   public static final int LEFT_CURLY_BRACE = 10;
   public static final int RIGHT_CURLY_BRACE = 20;
   public static final int LEFT_SQUARE_BRACKET = 30;
   public static final int RIGHT_SQUARE_BRACKET = 40;
   public static final int LEFT_PARENTHESIS = 50;
   public static final int RIGHT_PARENTHESIS = 60;
   public static final int DOT = 70;
   public static final int DOT_DOT = 75;
   public static final int DOT_DOT_DOT = 77;
   public static final int NAVIGATE = 80;
   public static final int FIND_REGEX = 90;
   public static final int MATCH_REGEX = 94;
   public static final int REGEX_PATTERN = 97;
   public static final int EQUAL = 100;
   public static final int EQUALS = 100;
   public static final int ASSIGN = 100;
   public static final int COMPARE_NOT_EQUAL = 120;
   public static final int COMPARE_IDENTICAL = 121;
   public static final int COMPARE_NOT_IDENTICAL = 122;
   public static final int COMPARE_EQUAL = 123;
   public static final int COMPARE_LESS_THAN = 124;
   public static final int COMPARE_LESS_THAN_EQUAL = 125;
   public static final int COMPARE_GREATER_THAN = 126;
   public static final int COMPARE_GREATER_THAN_EQUAL = 127;
   public static final int COMPARE_TO = 128;
   public static final int NOT = 160;
   public static final int LOGICAL_OR = 162;
   public static final int LOGICAL_AND = 164;
   public static final int LOGICAL_OR_EQUAL = 166;
   public static final int LOGICAL_AND_EQUAL = 168;
   public static final int PLUS = 200;
   public static final int MINUS = 201;
   public static final int MULTIPLY = 202;
   public static final int DIVIDE = 203;
   public static final int INTDIV = 204;
   public static final int MOD = 205;
   public static final int STAR_STAR = 206;
   public static final int POWER = 206;
   public static final int PLUS_EQUAL = 210;
   public static final int MINUS_EQUAL = 211;
   public static final int MULTIPLY_EQUAL = 212;
   public static final int DIVIDE_EQUAL = 213;
   public static final int INTDIV_EQUAL = 214;
   public static final int MOD_EQUAL = 215;
   public static final int POWER_EQUAL = 216;
   public static final int PLUS_PLUS = 250;
   public static final int PREFIX_PLUS_PLUS = 251;
   public static final int POSTFIX_PLUS_PLUS = 252;
   public static final int PREFIX_PLUS = 253;
   public static final int MINUS_MINUS = 260;
   public static final int PREFIX_MINUS_MINUS = 261;
   public static final int POSTFIX_MINUS_MINUS = 262;
   public static final int PREFIX_MINUS = 263;
   public static final int LEFT_SHIFT = 280;
   public static final int RIGHT_SHIFT = 281;
   public static final int RIGHT_SHIFT_UNSIGNED = 282;
   public static final int LEFT_SHIFT_EQUAL = 285;
   public static final int RIGHT_SHIFT_EQUAL = 286;
   public static final int RIGHT_SHIFT_UNSIGNED_EQUAL = 287;
   public static final int STAR = 202;
   public static final int COMMA = 300;
   public static final int COLON = 310;
   public static final int SEMICOLON = 320;
   public static final int QUESTION = 330;
   public static final int PIPE = 340;
   public static final int DOUBLE_PIPE = 162;
   public static final int BITWISE_OR = 340;
   public static final int BITWISE_AND = 341;
   public static final int BITWISE_XOR = 342;
   public static final int BITWISE_OR_EQUAL = 350;
   public static final int BITWISE_AND_EQUAL = 351;
   public static final int BITWISE_XOR_EQUAL = 352;
   public static final int BITWISE_NEGATION = 97;
   public static final int STRING = 400;
   public static final int IDENTIFIER = 440;
   public static final int INTEGER_NUMBER = 450;
   public static final int DECIMAL_NUMBER = 451;
   public static final int KEYWORD_PRIVATE = 500;
   public static final int KEYWORD_PROTECTED = 501;
   public static final int KEYWORD_PUBLIC = 502;
   public static final int KEYWORD_ABSTRACT = 510;
   public static final int KEYWORD_FINAL = 511;
   public static final int KEYWORD_NATIVE = 512;
   public static final int KEYWORD_TRANSIENT = 513;
   public static final int KEYWORD_VOLATILE = 514;
   public static final int KEYWORD_SYNCHRONIZED = 520;
   public static final int KEYWORD_STATIC = 521;
   public static final int KEYWORD_DEF = 530;
   public static final int KEYWORD_DEFMACRO = 539;
   public static final int KEYWORD_CLASS = 531;
   public static final int KEYWORD_INTERFACE = 532;
   public static final int KEYWORD_MIXIN = 533;
   public static final int KEYWORD_IMPLEMENTS = 540;
   public static final int KEYWORD_EXTENDS = 541;
   public static final int KEYWORD_THIS = 542;
   public static final int KEYWORD_SUPER = 543;
   public static final int KEYWORD_INSTANCEOF = 544;
   public static final int KEYWORD_PROPERTY = 545;
   public static final int KEYWORD_NEW = 546;
   public static final int KEYWORD_PACKAGE = 550;
   public static final int KEYWORD_IMPORT = 551;
   public static final int KEYWORD_AS = 552;
   public static final int KEYWORD_RETURN = 560;
   public static final int KEYWORD_IF = 561;
   public static final int KEYWORD_ELSE = 562;
   public static final int KEYWORD_DO = 570;
   public static final int KEYWORD_WHILE = 571;
   public static final int KEYWORD_FOR = 572;
   public static final int KEYWORD_IN = 573;
   public static final int KEYWORD_BREAK = 574;
   public static final int KEYWORD_CONTINUE = 575;
   public static final int KEYWORD_SWITCH = 576;
   public static final int KEYWORD_CASE = 577;
   public static final int KEYWORD_DEFAULT = 578;
   public static final int KEYWORD_TRY = 580;
   public static final int KEYWORD_CATCH = 581;
   public static final int KEYWORD_FINALLY = 582;
   public static final int KEYWORD_THROW = 583;
   public static final int KEYWORD_THROWS = 584;
   public static final int KEYWORD_ASSERT = 585;
   public static final int KEYWORD_VOID = 600;
   public static final int KEYWORD_BOOLEAN = 601;
   public static final int KEYWORD_BYTE = 602;
   public static final int KEYWORD_SHORT = 603;
   public static final int KEYWORD_INT = 604;
   public static final int KEYWORD_LONG = 605;
   public static final int KEYWORD_FLOAT = 606;
   public static final int KEYWORD_DOUBLE = 607;
   public static final int KEYWORD_CHAR = 608;
   public static final int KEYWORD_TRUE = 610;
   public static final int KEYWORD_FALSE = 611;
   public static final int KEYWORD_NULL = 612;
   public static final int KEYWORD_CONST = 700;
   public static final int KEYWORD_GOTO = 701;
   public static final int SYNTH_COMPILATION_UNIT = 800;
   public static final int SYNTH_CLASS = 801;
   public static final int SYNTH_INTERFACE = 802;
   public static final int SYNTH_MIXIN = 803;
   public static final int SYNTH_METHOD = 804;
   public static final int SYNTH_PROPERTY = 805;
   public static final int SYNTH_PARAMETER_DECLARATION = 806;
   public static final int SYNTH_LIST = 810;
   public static final int SYNTH_MAP = 811;
   public static final int SYNTH_GSTRING = 812;
   public static final int SYNTH_METHOD_CALL = 814;
   public static final int SYNTH_CAST = 815;
   public static final int SYNTH_BLOCK = 816;
   public static final int SYNTH_CLOSURE = 817;
   public static final int SYNTH_LABEL = 818;
   public static final int SYNTH_TERNARY = 819;
   public static final int SYNTH_TUPLE = 820;
   public static final int SYNTH_VARIABLE_DECLARATION = 830;
   public static final int GSTRING_START = 901;
   public static final int GSTRING_END = 902;
   public static final int GSTRING_EXPRESSION_START = 903;
   public static final int GSTRING_EXPRESSION_END = 904;
   public static final int ANY = 1000;
   public static final int NOT_EOF = 1001;
   public static final int GENERAL_END_OF_STATEMENT = 1002;
   public static final int ANY_END_OF_STATEMENT = 1003;
   public static final int ASSIGNMENT_OPERATOR = 1100;
   public static final int COMPARISON_OPERATOR = 1101;
   public static final int MATH_OPERATOR = 1102;
   public static final int LOGICAL_OPERATOR = 1103;
   public static final int RANGE_OPERATOR = 1104;
   public static final int REGEX_COMPARISON_OPERATOR = 1105;
   public static final int DEREFERENCE_OPERATOR = 1106;
   public static final int BITWISE_OPERATOR = 1107;
   public static final int PREFIX_OPERATOR = 1200;
   public static final int POSTFIX_OPERATOR = 1210;
   public static final int INFIX_OPERATOR = 1220;
   public static final int PREFIX_OR_INFIX_OPERATOR = 1230;
   public static final int PURE_PREFIX_OPERATOR = 1235;
   public static final int KEYWORD = 1300;
   public static final int SYMBOL = 1301;
   public static final int LITERAL = 1310;
   public static final int NUMBER = 1320;
   public static final int SIGN = 1325;
   public static final int NAMED_VALUE = 1330;
   public static final int TRUTH_VALUE = 1331;
   public static final int PRIMITIVE_TYPE = 1340;
   public static final int CREATABLE_PRIMITIVE_TYPE = 1341;
   public static final int LOOP = 1350;
   public static final int RESERVED_KEYWORD = 1360;
   public static final int KEYWORD_IDENTIFIER = 1361;
   public static final int SYNTHETIC = 1370;
   public static final int TYPE_DECLARATION = 1400;
   public static final int DECLARATION_MODIFIER = 1410;
   public static final int TYPE_NAME = 1420;
   public static final int CREATABLE_TYPE_NAME = 1430;
   public static final int MATCHED_CONTAINER = 1500;
   public static final int LEFT_OF_MATCHED_CONTAINER = 1501;
   public static final int RIGHT_OF_MATCHED_CONTAINER = 1502;
   public static final int EXPRESSION = 1900;
   public static final int OPERATOR_EXPRESSION = 1901;
   public static final int SYNTH_EXPRESSION = 1902;
   public static final int KEYWORD_EXPRESSION = 1903;
   public static final int LITERAL_EXPRESSION = 1904;
   public static final int ARRAY_EXPRESSION = 1905;
   public static final int SIMPLE_EXPRESSION = 1910;
   public static final int COMPLEX_EXPRESSION = 1911;
   public static final int PARAMETER_TERMINATORS = 2000;
   public static final int ARRAY_ITEM_TERMINATORS = 2001;
   public static final int TYPE_LIST_TERMINATORS = 2002;
   public static final int OPTIONAL_DATATYPE_FOLLOWERS = 2003;
   public static final int SWITCH_BLOCK_TERMINATORS = 2004;
   public static final int SWITCH_ENTRIES = 2005;
   public static final int METHOD_CALL_STARTERS = 2006;
   public static final int UNSAFE_OVER_NEWLINES = 2007;
   public static final int PRECLUDES_CAST_OPERATOR = 2008;
   private static final Map<Integer, String> TEXTS = new HashMap();
   private static final Map<String, Integer> LOOKUP = new HashMap();
   private static final Set<String> KEYWORDS = new HashSet();
   private static final Map<Integer, String> DESCRIPTIONS;

   public static boolean ofType(int specific, int general) {
      if (general == specific) {
         return true;
      } else {
         switch(general) {
         case 1000:
            return true;
         case 1001:
            return specific >= 0 && specific <= 830;
         case 1002:
            switch(specific) {
            case -1:
            case 5:
            case 320:
               return true;
            default:
               return false;
            }
         case 1003:
            switch(specific) {
            case -1:
            case 5:
            case 20:
            case 320:
               return true;
            default:
               return false;
            }
         case 1100:
            return specific == 100 || specific >= 210 && specific <= 216 || specific >= 166 && specific <= 168 || specific >= 285 && specific <= 287 || specific >= 350 && specific <= 352;
         case 1101:
            return specific >= 120 && specific <= 128;
         case 1102:
            return specific >= 200 && specific <= 282 || specific >= 160 && specific <= 164 || specific >= 340 && specific <= 342;
         case 1103:
            return specific >= 160 && specific <= 164;
         case 1104:
            return specific == 75 || specific == 77;
         case 1105:
            return specific == 90 || specific == 94;
         case 1106:
            return specific == 70 || specific == 80;
         case 1107:
            return specific >= 340 && specific <= 342 || specific == 97;
         case 1200:
            switch(specific) {
            case 201:
            case 250:
            case 260:
               return true;
            }
         case 1235:
            switch(specific) {
            case 97:
            case 160:
            case 251:
            case 253:
            case 261:
            case 263:
            case 815:
               return true;
            default:
               return false;
            }
         case 1210:
            switch(specific) {
            case 250:
            case 252:
            case 260:
            case 262:
               return true;
            default:
               return false;
            }
         case 1220:
            switch(specific) {
            case 70:
            case 75:
            case 77:
            case 80:
            case 90:
            case 94:
            case 162:
            case 164:
            case 280:
            case 281:
            case 282:
            case 340:
            case 341:
            case 342:
            case 544:
               return true;
            default:
               return specific >= 120 && specific <= 128 || specific >= 200 && specific <= 215 || specific == 100 || specific >= 210 && specific <= 216 || specific >= 166 && specific <= 168 || specific >= 285 && specific <= 287 || specific >= 350 && specific <= 352;
            }
         case 1230:
            switch(specific) {
            case 200:
            case 201:
            case 206:
            case 253:
            case 263:
               return true;
            default:
               return false;
            }
         case 1300:
            return specific >= 500 && specific <= 701;
         case 1301:
            return specific >= 5 && specific <= 340;
         case 1310:
            return specific >= 400 && specific <= 451;
         case 1320:
            return specific == 450 || specific == 451;
         case 1325:
            switch(specific) {
            case 200:
            case 201:
               return true;
            default:
               return false;
            }
         case 1330:
            return specific >= 610 && specific <= 612;
         case 1331:
            return specific == 610 || specific == 611;
         case 1350:
            switch(specific) {
            case 570:
            case 571:
            case 572:
               return true;
            default:
               return false;
            }
         case 1360:
            return specific >= 700 && specific <= 701;
         case 1361:
            switch(specific) {
            case 530:
            case 531:
            case 532:
            case 533:
            case 539:
            case 545:
            case 573:
               return true;
            default:
               return false;
            }
         case 1370:
            return specific >= 800 && specific <= 830;
         case 1400:
            return specific >= 531 && specific <= 533;
         case 1410:
            return specific >= 500 && specific <= 521;
         case 1420:
            if (specific == 440) {
               return true;
            }
         case 1340:
            return specific >= 600 && specific <= 608;
         case 1430:
            if (specific == 440) {
               return true;
            }
         case 1341:
            return specific >= 601 && specific <= 608;
         case 1500:
            switch(specific) {
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
            case 60:
               return true;
            default:
               return false;
            }
         case 1501:
            switch(specific) {
            case 10:
            case 30:
            case 50:
               return true;
            default:
               return false;
            }
         case 1502:
            switch(specific) {
            case 20:
            case 40:
            case 60:
               return true;
            default:
               return false;
            }
         case 1900:
            if (specific >= 70 && specific <= 282) {
               return true;
            } else if (specific >= 400 && specific <= 451) {
               return true;
            } else {
               switch(specific) {
               case 30:
               case 542:
               case 543:
               case 544:
               case 546:
               case 610:
               case 611:
               case 612:
               case 812:
               case 815:
               case 817:
               case 819:
                  return true;
               default:
                  return false;
               }
            }
         case 1901:
            return specific >= 70 && specific <= 282;
         case 1902:
            switch(specific) {
            case 815:
            case 817:
            case 819:
               return true;
            case 816:
            case 818:
            default:
               return false;
            }
         case 1903:
            switch(specific) {
            case 542:
            case 543:
            case 544:
            case 546:
            case 610:
            case 611:
            case 612:
               return true;
            default:
               return false;
            }
         case 1904:
            return specific >= 400 && specific <= 451;
         case 1905:
            return specific == 30;
         case 1911:
            switch(specific) {
            case 546:
            case 810:
            case 811:
            case 812:
            case 814:
            case 817:
            case 819:
            case 830:
               return true;
            }
         case 1910:
            if (specific >= 400 && specific <= 451) {
               return true;
            } else {
               switch(specific) {
               case 542:
               case 543:
               case 610:
               case 611:
               case 612:
                  return true;
               default:
                  return false;
               }
            }
         case 2000:
            return specific == 60 || specific == 300;
         case 2001:
            return specific == 40 || specific == 300;
         case 2002:
            switch(specific) {
            case 10:
            case 300:
            case 540:
            case 584:
               return true;
            default:
               return false;
            }
         case 2003:
            switch(specific) {
            case 30:
            case 70:
            case 440:
               return true;
            default:
               return false;
            }
         case 2004:
            if (specific == 20) {
               return true;
            }
         case 2005:
            return specific == 577 || specific == 578;
         case 2006:
            if (specific >= 400 && specific <= 451) {
               return true;
            } else {
               switch(specific) {
               case 50:
               case 546:
               case 812:
               case 901:
                  return true;
               default:
                  return false;
               }
            }
         case 2007:
            if (ofType(specific, 1301)) {
               switch(specific) {
               case 10:
               case 30:
               case 50:
               case 97:
               case 160:
               case 200:
               case 201:
               case 250:
               case 260:
                  return true;
               default:
                  return false;
               }
            } else {
               switch(specific) {
               case 544:
               case 902:
               case 903:
               case 904:
                  return false;
               default:
                  return true;
               }
            }
         case 2008:
            switch(specific) {
            case 50:
            case 200:
            case 201:
            case 251:
            case 253:
            case 261:
            case 263:
               return false;
            default:
               return !ofType(specific, 1911);
            }
         }
      }
   }

   public static boolean canMean(int actual, int preferred) {
      if (actual == preferred) {
         return true;
      } else {
         switch(preferred) {
         case 440:
         case 806:
            switch(actual) {
            case 440:
            case 530:
            case 531:
            case 532:
            case 533:
            case 539:
               return true;
            }
         default:
            return false;
         case 801:
         case 802:
         case 803:
         case 804:
         case 805:
            return actual == 440;
         case 810:
         case 811:
            return actual == 30;
         case 815:
            return actual == 50;
         case 816:
         case 817:
            return actual == 10;
         case 818:
            return actual == 310;
         case 830:
            return actual == 440;
         }
      }
   }

   public static void makePrefix(CSTNode node, boolean throwIfInvalid) {
      switch(node.getMeaning()) {
      case 200:
         node.setMeaning(253);
         break;
      case 201:
         node.setMeaning(263);
         break;
      case 250:
         node.setMeaning(251);
         break;
      case 260:
         node.setMeaning(261);
         break;
      default:
         if (throwIfInvalid) {
            throw new GroovyBugError("cannot convert to prefix for type [" + node.getMeaning() + "]");
         }
      }

   }

   public static void makePostfix(CSTNode node, boolean throwIfInvalid) {
      switch(node.getMeaning()) {
      case 250:
         node.setMeaning(252);
         break;
      case 260:
         node.setMeaning(262);
         break;
      default:
         if (throwIfInvalid) {
            throw new GroovyBugError("cannot convert to postfix for type [" + node.getMeaning() + "]");
         }
      }

   }

   public static int getPrecedence(int type, boolean throwIfInvalid) {
      switch(type) {
      case 30:
      case 804:
         return 75;
      case 50:
         return 0;
      case 70:
      case 80:
         return 80;
      case 75:
      case 77:
         return 30;
      case 90:
      case 94:
      case 120:
      case 123:
      case 124:
      case 125:
      case 126:
      case 127:
      case 128:
      case 544:
         return 25;
      case 97:
      case 160:
         return 50;
      case 100:
      case 166:
      case 168:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
      case 216:
      case 285:
      case 286:
      case 287:
      case 350:
      case 351:
      case 352:
         return 5;
      case 121:
      case 122:
         return 24;
      case 162:
         return 15;
      case 164:
         return 20;
      case 200:
      case 201:
         return 40;
      case 202:
      case 203:
      case 204:
      case 205:
         return 45;
      case 206:
         return 72;
      case 250:
      case 251:
      case 252:
      case 260:
      case 261:
      case 262:
         return 65;
      case 253:
      case 263:
         return 70;
      case 280:
      case 281:
      case 282:
         return 35;
      case 330:
         return 10;
      case 340:
      case 341:
      case 342:
         return 22;
      case 546:
         return 85;
      case 815:
         return 55;
      default:
         if (throwIfInvalid) {
            throw new GroovyBugError("precedence requested for non-operator");
         } else {
            return -1;
         }
      }
   }

   public static Collection<String> getKeywords() {
      return Collections.unmodifiableSet(KEYWORDS);
   }

   public static boolean isKeyword(String text) {
      return KEYWORDS.contains(text);
   }

   public static int lookup(String text, int filter) {
      int type = 0;
      if (LOOKUP.containsKey(text)) {
         type = (Integer)LOOKUP.get(text);
         if (filter != 0 && !ofType(type, filter)) {
            type = 0;
         }
      }

      return type;
   }

   public static int lookupKeyword(String text) {
      return lookup(text, 1300);
   }

   public static int lookupSymbol(String text) {
      return lookup(text, 1301);
   }

   public static String getText(int type) {
      String text = "";
      if (TEXTS.containsKey(type)) {
         text = (String)TEXTS.get(type);
      }

      return text;
   }

   private static void addTranslation(String text, int type) {
      TEXTS.put(type, text);
      LOOKUP.put(text, type);
   }

   private static void addKeyword(String text, int type) {
      KEYWORDS.add(text);
      addTranslation(text, type);
   }

   public static String getDescription(int type) {
      return DESCRIPTIONS.containsKey(type) ? (String)DESCRIPTIONS.get(type) : "<>";
   }

   private static void addDescription(int type, String description) {
      if (description.startsWith("<") && description.endsWith(">")) {
         DESCRIPTIONS.put(type, description);
      } else {
         DESCRIPTIONS.put(type, '"' + description + '"');
      }

   }

   static {
      addTranslation("\n", 5);
      addTranslation("{", 10);
      addTranslation("}", 20);
      addTranslation("[", 30);
      addTranslation("]", 40);
      addTranslation("(", 50);
      addTranslation(")", 60);
      addTranslation(".", 70);
      addTranslation("..", 75);
      addTranslation("...", 77);
      addTranslation("->", 80);
      addTranslation("=~", 90);
      addTranslation("==~", 94);
      addTranslation("~", 97);
      addTranslation("=", 100);
      addTranslation("!=", 120);
      addTranslation("===", 121);
      addTranslation("!==", 122);
      addTranslation("==", 123);
      addTranslation("<", 124);
      addTranslation("<=", 125);
      addTranslation(">", 126);
      addTranslation(">=", 127);
      addTranslation("<=>", 128);
      addTranslation("!", 160);
      addTranslation("||", 162);
      addTranslation("&&", 164);
      addTranslation("||=", 166);
      addTranslation("&&=", 168);
      addTranslation("+", 200);
      addTranslation("-", 201);
      addTranslation("*", 202);
      addTranslation("/", 203);
      addTranslation("\\", 204);
      addTranslation("%", 205);
      addTranslation("**", 206);
      addTranslation("+=", 210);
      addTranslation("-=", 211);
      addTranslation("*=", 212);
      addTranslation("/=", 213);
      addTranslation("\\=", 214);
      addTranslation("%=", 215);
      addTranslation("**=", 216);
      addTranslation("++", 250);
      addTranslation("--", 260);
      addTranslation("<<", 280);
      addTranslation(">>", 281);
      addTranslation(">>>", 282);
      addTranslation("<<=", 285);
      addTranslation(">>=", 286);
      addTranslation(">>>=", 287);
      addTranslation("&", 341);
      addTranslation("^", 342);
      addTranslation("|=", 350);
      addTranslation("&=", 351);
      addTranslation("^=", 352);
      addTranslation(",", 300);
      addTranslation(":", 310);
      addTranslation(";", 320);
      addTranslation("?", 330);
      addTranslation("|", 340);
      addTranslation("${}", 903);
      addKeyword("abstract", 510);
      addKeyword("as", 552);
      addKeyword("assert", 585);
      addKeyword("break", 574);
      addKeyword("case", 577);
      addKeyword("catch", 581);
      addKeyword("class", 531);
      addKeyword("const", 700);
      addKeyword("continue", 575);
      addKeyword("def", 530);
      addKeyword("defmacro", 530);
      addKeyword("default", 578);
      addKeyword("do", 570);
      addKeyword("else", 562);
      addKeyword("extends", 541);
      addKeyword("final", 511);
      addKeyword("finally", 582);
      addKeyword("for", 572);
      addKeyword("goto", 701);
      addKeyword("if", 561);
      addKeyword("in", 573);
      addKeyword("implements", 540);
      addKeyword("import", 551);
      addKeyword("instanceof", 544);
      addKeyword("interface", 532);
      addKeyword("mixin", 533);
      addKeyword("native", 512);
      addKeyword("new", 546);
      addKeyword("package", 550);
      addKeyword("private", 500);
      addKeyword("property", 545);
      addKeyword("protected", 501);
      addKeyword("public", 502);
      addKeyword("return", 560);
      addKeyword("static", 521);
      addKeyword("super", 543);
      addKeyword("switch", 576);
      addKeyword("synchronized", 520);
      addKeyword("this", 542);
      addKeyword("throw", 583);
      addKeyword("throws", 584);
      addKeyword("transient", 513);
      addKeyword("try", 580);
      addKeyword("volatile", 514);
      addKeyword("while", 571);
      addKeyword("true", 610);
      addKeyword("false", 611);
      addKeyword("null", 612);
      addKeyword("void", 600);
      addKeyword("boolean", 601);
      addKeyword("byte", 602);
      addKeyword("int", 604);
      addKeyword("short", 603);
      addKeyword("long", 605);
      addKeyword("float", 606);
      addKeyword("double", 607);
      addKeyword("char", 608);
      DESCRIPTIONS = new HashMap();
      Iterator iterator = LOOKUP.keySet().iterator();

      while(iterator.hasNext()) {
         String text = (String)iterator.next();
         int key = (Integer)LOOKUP.get(text);
         addDescription(key, text);
      }

      addDescription(5, "<newline>");
      addDescription(251, "<prefix ++>");
      addDescription(252, "<postfix ++>");
      addDescription(261, "<prefix -->");
      addDescription(262, "<postfix -->");
      addDescription(253, "<positive>");
      addDescription(263, "<negative>");
      addDescription(400, "<string literal>");
      addDescription(440, "<identifier>");
      addDescription(450, "<integer>");
      addDescription(451, "<decimal>");
      addDescription(800, "<compilation unit>");
      addDescription(801, "<class>");
      addDescription(802, "<interface>");
      addDescription(803, "<mixin>");
      addDescription(804, "<method>");
      addDescription(814, "<method call>");
      addDescription(805, "<property>");
      addDescription(806, "<parameter>");
      addDescription(810, "<list>");
      addDescription(811, "<map>");
      addDescription(820, "<tuple>");
      addDescription(812, "<gstring>");
      addDescription(815, "<cast>");
      addDescription(816, "<block>");
      addDescription(817, "<closure>");
      addDescription(819, "<ternary>");
      addDescription(818, "<label>");
      addDescription(830, "<variable declaration>");
      addDescription(901, "<start of gstring tokens>");
      addDescription(902, "<end of gstring tokens>");
      addDescription(903, "<start of gstring expression>");
      addDescription(904, "<end of gstring expression>");
      addDescription(1100, "<assignment operator>");
      addDescription(1101, "<comparison operator>");
      addDescription(1102, "<math operator>");
      addDescription(1103, "<logical operator>");
      addDescription(1107, "<bitwise operator>");
      addDescription(1104, "<range operator>");
      addDescription(1105, "<regex comparison operator>");
      addDescription(1106, "<dereference operator>");
      addDescription(1200, "<prefix operator>");
      addDescription(1210, "<postfix operator>");
      addDescription(1220, "<infix operator>");
      addDescription(1300, "<keyword>");
      addDescription(1310, "<literal>");
      addDescription(1320, "<number>");
      addDescription(1330, "<named value>");
      addDescription(1331, "<truth value>");
      addDescription(1340, "<primitive type>");
      addDescription(1341, "<creatable primitive type>");
      addDescription(1350, "<loop>");
      addDescription(1360, "<reserved keyword>");
      addDescription(1370, "<synthetic>");
      addDescription(1400, "<type declaration>");
      addDescription(1410, "<declaration modifier>");
      addDescription(1420, "<type name>");
      addDescription(1430, "<creatable type name>");
      addDescription(1500, "<matched container>");
      addDescription(1501, "<left of matched container>");
      addDescription(1502, "<right of matched container>");
      addDescription(2005, "<valid in a switch body>");
   }
}
