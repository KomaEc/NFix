package polyglot.ext.jl.parse;

import polyglot.ast.Expr;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.parse.BaseParser;
import polyglot.types.Package;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public class Name {
   public final Name prefix;
   public final String name;
   public final Position pos;
   NodeFactory nf;
   TypeSystem ts;

   public Name(BaseParser parser, Position pos, String name) {
      this(parser, pos, (Name)null, name);
   }

   public Name(BaseParser parser, Position pos, Name prefix, String name) {
      this.nf = parser.nf;
      this.ts = parser.ts;
      this.pos = pos;
      this.prefix = prefix;
      this.name = name;
   }

   public Expr toExpr() {
      return (Expr)(this.prefix == null ? this.nf.AmbExpr(this.pos, this.name) : this.nf.Field(this.pos, this.prefix.toReceiver(), this.name));
   }

   public Receiver toReceiver() {
      return this.prefix == null ? this.nf.AmbReceiver(this.pos, this.name) : this.nf.AmbReceiver(this.pos, this.prefix.toPrefix(), this.name);
   }

   public Prefix toPrefix() {
      return this.prefix == null ? this.nf.AmbPrefix(this.pos, this.name) : this.nf.AmbPrefix(this.pos, this.prefix.toPrefix(), this.name);
   }

   public QualifierNode toQualifier() {
      return this.prefix == null ? this.nf.AmbQualifierNode(this.pos, this.name) : this.nf.AmbQualifierNode(this.pos, this.prefix.toQualifier(), this.name);
   }

   public PackageNode toPackage() {
      return this.prefix == null ? this.nf.PackageNode(this.pos, this.ts.createPackage((Package)null, this.name)) : this.nf.PackageNode(this.pos, this.ts.createPackage(this.prefix.toPackage().package_(), this.name));
   }

   public TypeNode toType() {
      return this.prefix == null ? this.nf.AmbTypeNode(this.pos, this.name) : this.nf.AmbTypeNode(this.pos, this.prefix.toQualifier(), this.name);
   }

   public String toString() {
      return this.prefix == null ? this.name : this.prefix.toString() + "." + this.name;
   }
}
