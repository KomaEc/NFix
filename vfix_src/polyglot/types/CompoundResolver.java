package polyglot.types;

public class CompoundResolver implements TopLevelResolver {
   TopLevelResolver head;
   TopLevelResolver tail;

   public CompoundResolver(TopLevelResolver head, TopLevelResolver tail) {
      this.head = head;
      this.tail = tail;
   }

   public String toString() {
      return "(compound " + this.head + " " + this.tail + ")";
   }

   public boolean packageExists(String name) {
      return this.head.packageExists(name) || this.tail.packageExists(name);
   }

   public Named find(String name) throws SemanticException {
      try {
         return this.head.find(name);
      } catch (NoClassException var3) {
         return this.tail.find(name);
      }
   }
}
