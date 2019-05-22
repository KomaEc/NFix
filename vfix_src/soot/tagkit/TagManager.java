package soot.tagkit;

import soot.G;
import soot.Singletons;

public class TagManager {
   private TagPrinter tagPrinter = new StdTagPrinter();

   public TagManager(Singletons.Global g) {
   }

   public static TagManager v() {
      return G.v().soot_tagkit_TagManager();
   }

   public Tag getTagFor(String tagName) {
      try {
         Class<?> cc = Class.forName("soot.tagkit." + tagName);
         return (Tag)cc.newInstance();
      } catch (ClassNotFoundException var3) {
         return null;
      } catch (IllegalAccessException var4) {
         throw new RuntimeException();
      } catch (InstantiationException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public void setTagPrinter(TagPrinter p) {
      this.tagPrinter = p;
   }

   public String print(String aClassName, String aFieldOrMtdSignature, Tag aTag) {
      return this.tagPrinter.print(aClassName, aFieldOrMtdSignature, aTag);
   }
}
