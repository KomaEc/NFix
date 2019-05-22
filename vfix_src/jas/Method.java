package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Method {
   short acc;
   CP name;
   CP sig;
   CodeAttr code;
   ExceptAttr excepts;
   SyntheticAttr synthAttr = null;
   DeprecatedAttr deprAttr = null;
   SignatureAttr sigAttr = null;
   VisibilityAnnotationAttr vis_annot_attr = null;
   VisibilityAnnotationAttr invis_annot_attr = null;
   ParameterVisibilityAnnotationAttr param_vis_annot_attr = null;
   ParameterVisibilityAnnotationAttr param_invis_annot_attr = null;
   AnnotationDefaultAttr annotDef = null;
   Vector genericAttrs = new Vector();

   public Method(short macc, CP name, CP sig, CodeAttr cd, ExceptAttr ex) {
      this.acc = macc;
      this.name = name;
      this.sig = sig;
      this.code = cd;
      this.excepts = ex;
   }

   public Method(short macc, CP name, CP sig, CodeAttr cd, ExceptAttr ex, SyntheticAttr synth) {
      this.acc = macc;
      this.name = name;
      this.sig = sig;
      this.code = cd;
      this.excepts = ex;
      this.synthAttr = synth;
   }

   public void addGenericAttr(GenericAttr g) {
      this.genericAttrs.addElement(g);
   }

   public void addDeprecatedAttr(DeprecatedAttr d) {
      this.deprAttr = d;
   }

   public void addSignatureAttr(SignatureAttr s) {
      this.sigAttr = s;
   }

   public void addVisAnnotationAttr(VisibilityAnnotationAttr v) {
      this.vis_annot_attr = v;
   }

   public void addInvisAnnotationAttr(VisibilityAnnotationAttr v) {
      this.invis_annot_attr = v;
   }

   public void addVisParamAnnotationAttr(ParameterVisibilityAnnotationAttr v) {
      this.param_vis_annot_attr = v;
   }

   public void addInvisParamAnnotationAttr(ParameterVisibilityAnnotationAttr v) {
      this.param_invis_annot_attr = v;
   }

   public void addAnnotationDef(AnnotationDefaultAttr v) {
      this.annotDef = v;
   }

   void resolve(ClassEnv e) {
      e.addCPItem(this.name);
      e.addCPItem(this.sig);
      if (this.code != null) {
         this.code.resolve(e);
      }

      if (this.excepts != null) {
         this.excepts.resolve(e);
      }

      if (this.synthAttr != null) {
         this.synthAttr.resolve(e);
      }

      if (this.deprAttr != null) {
         this.deprAttr.resolve(e);
      }

      if (this.sigAttr != null) {
         this.sigAttr.resolve(e);
      }

      if (this.vis_annot_attr != null) {
         this.vis_annot_attr.resolve(e);
      }

      if (this.invis_annot_attr != null) {
         this.invis_annot_attr.resolve(e);
      }

      if (this.param_vis_annot_attr != null) {
         this.param_vis_annot_attr.resolve(e);
      }

      if (this.param_invis_annot_attr != null) {
         this.param_invis_annot_attr.resolve(e);
      }

      if (this.annotDef != null) {
         this.annotDef.resolve(e);
      }

   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      short cnt = 0;
      out.writeShort(this.acc);
      out.writeShort(e.getCPIndex(this.name));
      out.writeShort(e.getCPIndex(this.sig));
      if (this.code != null) {
         ++cnt;
      }

      if (this.excepts != null) {
         ++cnt;
      }

      cnt = (short)(cnt + this.genericAttrs.size());
      if (this.synthAttr != null) {
         ++cnt;
      }

      if (this.deprAttr != null) {
         ++cnt;
      }

      if (this.sigAttr != null) {
         ++cnt;
      }

      if (this.vis_annot_attr != null) {
         ++cnt;
      }

      if (this.invis_annot_attr != null) {
         ++cnt;
      }

      if (this.param_vis_annot_attr != null) {
         ++cnt;
      }

      if (this.param_invis_annot_attr != null) {
         ++cnt;
      }

      if (this.annotDef != null) {
         ++cnt;
      }

      out.writeShort(cnt);
      if (this.code != null) {
         this.code.write(e, out);
      }

      if (this.excepts != null) {
         this.excepts.write(e, out);
      }

      if (this.synthAttr != null) {
         this.synthAttr.write(e, out);
      }

      if (this.deprAttr != null) {
         this.deprAttr.write(e, out);
      }

      if (this.sigAttr != null) {
         this.sigAttr.write(e, out);
      }

      if (this.vis_annot_attr != null) {
         this.vis_annot_attr.write(e, out);
      }

      if (this.invis_annot_attr != null) {
         this.invis_annot_attr.write(e, out);
      }

      if (this.param_vis_annot_attr != null) {
         this.param_vis_annot_attr.write(e, out);
      }

      if (this.param_invis_annot_attr != null) {
         this.param_invis_annot_attr.write(e, out);
      }

      if (this.annotDef != null) {
         this.annotDef.write(e, out);
      }

      Enumeration enu = this.genericAttrs.elements();

      while(enu.hasMoreElements()) {
         ((GenericAttr)enu.nextElement()).write(e, out);
      }

   }
}
