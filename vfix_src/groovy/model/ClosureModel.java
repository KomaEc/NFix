package groovy.model;

import groovy.lang.Closure;

public class ClosureModel implements ValueModel, NestedValueModel {
   private final ValueModel sourceModel;
   private final Closure readClosure;
   private final Closure writeClosure;
   private final Class type;

   public ClosureModel(ValueModel sourceModel, Closure readClosure) {
      this(sourceModel, readClosure, (Closure)null);
   }

   public ClosureModel(ValueModel sourceModel, Closure readClosure, Closure writeClosure) {
      this(sourceModel, readClosure, writeClosure, Object.class);
   }

   public ClosureModel(ValueModel sourceModel, Closure readClosure, Closure writeClosure, Class type) {
      this.sourceModel = sourceModel;
      this.readClosure = readClosure;
      this.writeClosure = writeClosure;
      this.type = type;
   }

   public ValueModel getSourceModel() {
      return this.sourceModel;
   }

   public Object getValue() {
      Object source = this.sourceModel.getValue();
      return source != null ? this.readClosure.call(source) : null;
   }

   public void setValue(Object value) {
      if (this.writeClosure != null) {
         Object source = this.sourceModel.getValue();
         if (source != null) {
            this.writeClosure.call(new Object[]{source, value});
         }
      }

   }

   public Class getType() {
      return this.type;
   }

   public boolean isEditable() {
      return this.writeClosure != null;
   }
}
