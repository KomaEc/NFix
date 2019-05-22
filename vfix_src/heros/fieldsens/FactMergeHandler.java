package heros.fieldsens;

public interface FactMergeHandler<Fact> {
   void merge(Fact var1, Fact var2);

   void restoreCallingContext(Fact var1, Fact var2);
}
