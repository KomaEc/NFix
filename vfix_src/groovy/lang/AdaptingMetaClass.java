package groovy.lang;

public interface AdaptingMetaClass extends MetaClass {
   MetaClass getAdaptee();

   void setAdaptee(MetaClass var1);
}
