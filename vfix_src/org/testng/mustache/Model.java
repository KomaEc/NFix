package org.testng.mustache;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class Model {
   private Map<String, Object> m_model;
   private Stack<Model.SubModel> m_subModels = new Stack();

   public Model(Map<String, Object> model) {
      this.m_model = model;
   }

   public void push(String variable, Object subModel) {
      Model.SubModel sl = new Model.SubModel();
      sl.variable = variable;
      sl.subModel = subModel;
      this.m_subModels.push(sl);
   }

   public Value resolveValue(String variable) {
      if (!this.m_subModels.isEmpty()) {
         Iterator i$ = this.m_subModels.iterator();

         while(i$.hasNext()) {
            Model.SubModel object = (Model.SubModel)i$.next();
            Value value = this.resolveOnClass(object.subModel, variable);
            if (value != null) {
               return value;
            }
         }
      }

      return new Value(this.m_model.get(variable));
   }

   private Value resolveOnClass(Object object, String variable) {
      Class cls = object.getClass();

      try {
         Field f = cls.getField(variable);
         return new Value(f.get(object));
      } catch (NoSuchFieldException | SecurityException | IllegalAccessException var5) {
         return null;
      }
   }

   public Object getTopSubModel() {
      return ((Model.SubModel)this.m_subModels.peek()).variable;
   }

   public void popSubModel() {
      this.m_subModels.pop();
   }

   public String resolveValueToString(String variable) {
      new StringBuilder();
      Value value = this.resolveValue(variable);
      return value.get() != null ? value.get().toString() : "";
   }

   public String toString() {
      return "[Model " + this.m_model + " subModel:" + this.m_subModels + "]";
   }

   private static class SubModel {
      String variable;
      Object subModel;

      private SubModel() {
      }

      // $FF: synthetic method
      SubModel(Object x0) {
         this();
      }
   }
}
