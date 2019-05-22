package jasmin;

import java.util.Hashtable;
import java_cup.runtime.Symbol;

abstract class ReservedWords {
   static Hashtable reserved_words = new Hashtable();

   public static Symbol get(String name) {
      Symbol template = (Symbol)reserved_words.get(name);
      return template != null ? new Symbol(template.sym) : null;
   }

   public static boolean contains(String name) {
      return reserved_words.get(name) != null;
   }

   static {
      reserved_words.put(".catch", new Symbol(2));
      reserved_words.put(".class", new Symbol(3));
      reserved_words.put(".end", new Symbol(4));
      reserved_words.put(".field", new Symbol(5));
      reserved_words.put(".implements", new Symbol(15));
      reserved_words.put(".interface", new Symbol(16));
      reserved_words.put(".limit", new Symbol(6));
      reserved_words.put(".line", new Symbol(7));
      reserved_words.put(".method", new Symbol(8));
      reserved_words.put(".set", new Symbol(9));
      reserved_words.put(".source", new Symbol(12));
      reserved_words.put(".super", new Symbol(10));
      reserved_words.put(".no_super", new Symbol(11));
      reserved_words.put(".throws", new Symbol(13));
      reserved_words.put(".var", new Symbol(14));
      reserved_words.put(".class_attribute", new Symbol(18));
      reserved_words.put(".field_attribute", new Symbol(19));
      reserved_words.put(".method_attribute", new Symbol(20));
      reserved_words.put(".code_attribute", new Symbol(17));
      reserved_words.put(".inner_class_attr", new Symbol(21));
      reserved_words.put(".inner_class_spec_attr", new Symbol(22));
      reserved_words.put(".synthetic", new Symbol(23));
      reserved_words.put(".enclosing_method_attr", new Symbol(24));
      reserved_words.put(".deprecated", new Symbol(25));
      reserved_words.put(".signature_attr", new Symbol(26));
      reserved_words.put(".runtime_visible_annotation", new Symbol(27));
      reserved_words.put(".runtime_invisible_annotation", new Symbol(28));
      reserved_words.put(".runtime_param_visible_annotation", new Symbol(29));
      reserved_words.put(".runtime_param_invisible_annotation", new Symbol(30));
      reserved_words.put(".annotation_attr", new Symbol(31));
      reserved_words.put(".param", new Symbol(50));
      reserved_words.put(".annotation", new Symbol(32));
      reserved_words.put(".int_kind", new Symbol(33));
      reserved_words.put(".byte_kind", new Symbol(34));
      reserved_words.put(".char_kind", new Symbol(35));
      reserved_words.put(".short_kind", new Symbol(36));
      reserved_words.put(".bool_kind", new Symbol(37));
      reserved_words.put(".str_kind", new Symbol(45));
      reserved_words.put(".long_kind", new Symbol(38));
      reserved_words.put(".doub_kind", new Symbol(39));
      reserved_words.put(".float_kind", new Symbol(40));
      reserved_words.put(".enum_kind", new Symbol(43));
      reserved_words.put(".ann_kind", new Symbol(42));
      reserved_words.put(".arr_kind", new Symbol(41));
      reserved_words.put(".cls_kind", new Symbol(44));
      reserved_words.put(".arr_elem", new Symbol(46));
      reserved_words.put(".annot_elem", new Symbol(47));
      reserved_words.put(".elem", new Symbol(48));
      reserved_words.put(".annotation_default", new Symbol(49));
      reserved_words.put("is", new Symbol(53));
      reserved_words.put("from", new Symbol(54));
      reserved_words.put("method", new Symbol(55));
      reserved_words.put("to", new Symbol(51));
      reserved_words.put("using", new Symbol(52));
      reserved_words.put("tableswitch", new Symbol(71));
      reserved_words.put("lookupswitch", new Symbol(70));
      reserved_words.put("default", new Symbol(72));
      reserved_words.put("public", new Symbol(62));
      reserved_words.put("private", new Symbol(60));
      reserved_words.put("protected", new Symbol(61));
      reserved_words.put("static", new Symbol(63));
      reserved_words.put("final", new Symbol(57));
      reserved_words.put("synchronized", new Symbol(64));
      reserved_words.put("volatile", new Symbol(66));
      reserved_words.put("transient", new Symbol(65));
      reserved_words.put("native", new Symbol(59));
      reserved_words.put("interface", new Symbol(58));
      reserved_words.put("abstract", new Symbol(56));
      reserved_words.put("strictfp", new Symbol(67));
      reserved_words.put("annotation", new Symbol(68));
      reserved_words.put("enum", new Symbol(69));
   }
}
