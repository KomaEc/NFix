package heros;

public interface ItemPrinter<N, D, M> {
   ItemPrinter<Object, Object, Object> DEFAULT_PRINTER = new ItemPrinter<Object, Object, Object>() {
      public String printNode(Object node, Object parentMethod) {
         return node.toString();
      }

      public String printFact(Object fact) {
         return fact.toString();
      }

      public String printMethod(Object method) {
         return method.toString();
      }
   };

   String printNode(N var1, M var2);

   String printFact(D var1);

   String printMethod(M var1);
}
