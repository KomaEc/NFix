package polyglot.frontend;

public class EmptyPass extends AbstractPass {
   public EmptyPass(Pass.ID id) {
      super(id);
   }

   public boolean run() {
      return true;
   }
}
