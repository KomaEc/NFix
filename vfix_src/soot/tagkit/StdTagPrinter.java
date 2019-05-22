package soot.tagkit;

public class StdTagPrinter implements TagPrinter {
   public String print(String aClassName, String aFieldOrMtdSignature, Tag aTag) {
      return aTag.toString();
   }
}
