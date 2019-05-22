package hidden.org.codehaus.plexus.interpolation;

import hidden.org.codehaus.plexus.interpolation.reflection.ReflectionValueExtractor;
import java.util.ArrayList;
import java.util.List;

public class ObjectBasedValueSource implements FeedbackEnabledValueSource {
   private final Object root;
   private List feedback = new ArrayList();

   public ObjectBasedValueSource(Object root) {
      this.root = root;
   }

   public Object getValue(String expression) {
      try {
         return ReflectionValueExtractor.evaluate(expression, this.root, false);
      } catch (Exception var3) {
         this.feedback.add("Failed to extract '" + expression + "' from: " + this.root);
         this.feedback.add(var3);
         return null;
      }
   }

   public List getFeedback() {
      return this.feedback;
   }

   public void clearFeedback() {
      this.feedback.clear();
   }
}
