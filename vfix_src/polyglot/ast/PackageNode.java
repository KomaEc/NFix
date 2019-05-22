package polyglot.ast;

import polyglot.types.Package;

public interface PackageNode extends Node, Prefix, QualifierNode {
   Package package_();

   PackageNode package_(Package var1);
}
