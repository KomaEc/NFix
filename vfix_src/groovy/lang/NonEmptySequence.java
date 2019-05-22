package groovy.lang;

import java.util.List;

public class NonEmptySequence extends Sequence {
   public NonEmptySequence() {
      super((Class)null);
   }

   public NonEmptySequence(Class type) {
      super(type);
   }

   public NonEmptySequence(Class type, List content) {
      super(type, content);
   }

   public int minimumSize() {
      return 1;
   }
}
