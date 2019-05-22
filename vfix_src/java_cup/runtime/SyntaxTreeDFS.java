package java_cup.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SyntaxTreeDFS {
   public static void dfs(XMLElement element, SyntaxTreeDFS.Visitor visitor) {
      visitor.preVisit(element);
      Iterator var3 = element.getChildren().iterator();

      while(var3.hasNext()) {
         XMLElement el = (XMLElement)var3.next();
         dfs(el, visitor);
      }

      visitor.postVisit(element);
   }

   public abstract static class AbstractVisitor implements SyntaxTreeDFS.Visitor {
      private HashMap<String, SyntaxTreeDFS.ElementHandler> preMap = new HashMap();
      private HashMap<String, SyntaxTreeDFS.ElementHandler> postMap = new HashMap();

      public abstract void defaultPre(XMLElement var1, List<XMLElement> var2);

      public abstract void defaultPost(XMLElement var1, List<XMLElement> var2);

      public void preVisit(XMLElement element) {
         SyntaxTreeDFS.ElementHandler handler = (SyntaxTreeDFS.ElementHandler)this.preMap.get(element.tagname);
         if (handler == null) {
            this.defaultPre(element, element.getChildren());
         } else {
            handler.handle(element, element.getChildren());
         }

      }

      public void postVisit(XMLElement element) {
         SyntaxTreeDFS.ElementHandler handler = (SyntaxTreeDFS.ElementHandler)this.postMap.get(element.tagname);
         if (handler == null) {
            this.defaultPost(element, element.getChildren());
         } else {
            handler.handle(element, element.getChildren());
         }

      }

      public void registerPreVisit(String s, SyntaxTreeDFS.ElementHandler h) {
         this.preMap.put(s, h);
      }

      public void registerPostVisit(String s, SyntaxTreeDFS.ElementHandler h) {
         this.postMap.put(s, h);
      }
   }

   public interface ElementHandler {
      void handle(XMLElement var1, List<XMLElement> var2);
   }

   public interface Visitor {
      void preVisit(XMLElement var1);

      void postVisit(XMLElement var1);
   }
}
