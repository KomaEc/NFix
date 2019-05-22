package org.testng.reporters.jq;

import java.util.List;
import org.testng.ISuite;

public abstract class BasePanel implements IPanel {
   public static final String C = "class";
   public static final String D = "div";
   public static final String S = "span";
   private Model m_model;

   public BasePanel(Model model) {
      this.m_model = model;
   }

   protected Model getModel() {
      return this.m_model;
   }

   protected List<ISuite> getSuites() {
      return this.getModel().getSuites();
   }

   protected static String pluralize(int count, String singular) {
      return Integer.toString(count) + " " + (count != 0 && count <= 1 ? singular : (singular.endsWith("s") ? singular + "es" : singular + "s"));
   }

   protected static String suiteToTag(ISuite suite) {
      return suite.getName().replace(" ", "_");
   }
}
