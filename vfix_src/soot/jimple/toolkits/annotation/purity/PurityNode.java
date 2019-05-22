package soot.jimple.toolkits.annotation.purity;

public interface PurityNode {
   boolean isInside();

   boolean isLoad();

   boolean isParam();
}
