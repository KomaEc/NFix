package soot;

public interface IdentityUnit extends Unit {
   Value getLeftOp();

   Value getRightOp();

   ValueBox getLeftOpBox();

   ValueBox getRightOpBox();
}
