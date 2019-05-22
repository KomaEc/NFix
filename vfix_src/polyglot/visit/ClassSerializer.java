package polyglot.visit;

import java.io.IOException;
import java.util.Date;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.FieldDecl;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.main.Version;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
import polyglot.util.TypeEncoder;

public class ClassSerializer extends NodeVisitor {
   protected TypeEncoder te;
   protected ErrorQueue eq;
   protected Date date;
   protected TypeSystem ts;
   protected NodeFactory nf;
   protected Version ver;

   public ClassSerializer(TypeSystem ts, NodeFactory nf, Date date, ErrorQueue eq, Version ver) {
      this.ts = ts;
      this.nf = nf;
      this.te = new TypeEncoder(ts);
      this.eq = eq;
      this.date = date;
      this.ver = ver;
   }

   public Node override(Node n) {
      return n instanceof ClassMember && !(n instanceof ClassDecl) ? n : null;
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      if (!(n instanceof ClassDecl)) {
         return n;
      } else {
         try {
            ClassDecl cn = (ClassDecl)n;
            ClassBody body = cn.body();
            ParsedClassType ct = cn.type();
            ct.superType();
            ct.interfaces();
            ct.memberClasses();
            ct.constructors();
            ct.methods();
            ct.fields();
            if (!ct.isTopLevel() && !ct.isMember()) {
               return n;
            } else {
               String suffix = this.ver.name();
               if (ct.fieldNamed("jlc$CompilerVersion$" + suffix) == null && ct.fieldNamed("jlc$SourceLastModified$" + suffix) == null && ct.fieldNamed("jlc$ClassType$" + suffix) == null) {
                  Flags flags = Flags.PUBLIC.set(Flags.STATIC).set(Flags.FINAL);
                  String version = this.ver.major() + "." + this.ver.minor() + "." + this.ver.patch_level();
                  Position pos = Position.COMPILER_GENERATED;
                  FieldInstance fi = this.ts.fieldInstance(pos, ct, flags, this.ts.String(), "jlc$CompilerVersion$" + suffix);
                  InitializerInstance ii = this.ts.initializerInstance(pos, ct, Flags.STATIC);
                  FieldDecl f = this.nf.FieldDecl(fi.position(), fi.flags(), this.nf.CanonicalTypeNode(fi.position(), fi.type()), fi.name(), this.nf.StringLit(pos, version).type(this.ts.String()));
                  f = f.fieldInstance(fi);
                  f = f.initializerInstance(ii);
                  body = body.addMember(f);
                  long time = this.date.getTime();
                  fi = this.ts.fieldInstance(pos, ct, flags, this.ts.Long(), "jlc$SourceLastModified$" + suffix);
                  ii = this.ts.initializerInstance(pos, ct, Flags.STATIC);
                  f = this.nf.FieldDecl(fi.position(), fi.flags(), this.nf.CanonicalTypeNode(fi.position(), fi.type()), fi.name(), this.nf.IntLit(pos, IntLit.LONG, time).type(this.ts.Long()));
                  f = f.fieldInstance(fi);
                  f = f.initializerInstance(ii);
                  body = body.addMember(f);
                  fi = this.ts.fieldInstance(pos, ct, flags, this.ts.String(), "jlc$ClassType$" + suffix);
                  ii = this.ts.initializerInstance(pos, ct, Flags.STATIC);
                  f = this.nf.FieldDecl(fi.position(), fi.flags(), this.nf.CanonicalTypeNode(fi.position(), fi.type()), fi.name(), this.nf.StringLit(pos, this.te.encode(ct)).type(this.ts.String()));
                  f = f.fieldInstance(fi);
                  f = f.initializerInstance(ii);
                  body = body.addMember(f);
                  return cn.body(body);
               } else {
                  this.eq.enqueue(5, "Cannot encode Polyglot type information more than once.");
                  return n;
               }
            }
         } catch (IOException var17) {
            this.eq.enqueue(2, "Unable to encode Polyglot type information.");
            return n;
         }
      }
   }
}
