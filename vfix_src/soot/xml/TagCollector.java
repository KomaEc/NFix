package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import soot.Body;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.KeyTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.Tag;

public class TagCollector {
   private final ArrayList<Attribute> attributes = new ArrayList();
   private final ArrayList<Key> keys = new ArrayList();

   public boolean isEmpty() {
      return this.attributes.isEmpty() && this.keys.isEmpty();
   }

   public void collectTags(SootClass sc) {
      this.collectTags(sc, true);
   }

   public void collectTags(SootClass sc, boolean includeBodies) {
      this.collectClassTags(sc);
      Iterator var3 = sc.getFields().iterator();

      while(var3.hasNext()) {
         SootField sf = (SootField)var3.next();
         this.collectFieldTags(sf);
      }

      var3 = sc.getMethods().iterator();

      while(var3.hasNext()) {
         SootMethod sm = (SootMethod)var3.next();
         this.collectMethodTags(sm);
         if (includeBodies && sm.hasActiveBody()) {
            Body b = sm.getActiveBody();
            this.collectBodyTags(b);
         }
      }

   }

   public void collectKeyTags(SootClass sc) {
      Iterator var2 = sc.getTags().iterator();

      while(var2.hasNext()) {
         Tag next = (Tag)var2.next();
         if (next instanceof KeyTag) {
            KeyTag kt = (KeyTag)next;
            Key k = new Key(kt.red(), kt.green(), kt.blue(), kt.key());
            k.aType(kt.analysisType());
            this.keys.add(k);
         }
      }

   }

   public void printKeys(PrintWriter writerOut) {
      Iterator it = this.keys.iterator();

      while(it.hasNext()) {
         Key k = (Key)it.next();
         k.print(writerOut);
      }

   }

   private void addAttribute(Attribute a) {
      if (!a.isEmpty()) {
         this.attributes.add(a);
      }

   }

   private void collectHostTags(Host h) {
      Predicate<Tag> p = Predicate.truePred();
      this.collectHostTags(h, p);
   }

   private void collectHostTags(Host h, Predicate<Tag> include) {
      if (!h.getTags().isEmpty()) {
         Attribute a = new Attribute();
         Iterator var4 = h.getTags().iterator();

         while(var4.hasNext()) {
            Tag t = (Tag)var4.next();
            if (include.test(t)) {
               a.addTag(t);
            }
         }

         this.addAttribute(a);
      }

   }

   public void collectClassTags(SootClass sc) {
      Predicate<Tag> noSFTags = new Predicate<Tag>() {
         public boolean test(Tag t) {
            return !(t instanceof SourceFileTag);
         }
      };
      this.collectHostTags(sc, noSFTags);
   }

   public void collectFieldTags(SootField sf) {
      this.collectHostTags(sf);
   }

   public void collectMethodTags(SootMethod sm) {
      if (sm.hasActiveBody()) {
         this.collectHostTags(sm);
      }

   }

   public synchronized void collectBodyTags(Body b) {
      Iterator var2 = b.getUnits().iterator();

      label48:
      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         Attribute ua = new Attribute();
         JimpleLineNumberTag jlnt = null;
         Iterator var6 = u.getTags().iterator();

         while(var6.hasNext()) {
            Tag t = (Tag)var6.next();
            ua.addTag(t);
            if (t instanceof JimpleLineNumberTag) {
               jlnt = (JimpleLineNumberTag)t;
            }
         }

         this.addAttribute(ua);
         var6 = u.getUseAndDefBoxes().iterator();

         while(true) {
            ValueBox vb;
            do {
               if (!var6.hasNext()) {
                  continue label48;
               }

               vb = (ValueBox)var6.next();
            } while(vb.getTags().isEmpty());

            Attribute va = new Attribute();
            Iterator var9 = vb.getTags().iterator();

            while(var9.hasNext()) {
               Tag t = (Tag)var9.next();
               va.addTag(t);
               if (jlnt != null) {
                  va.addTag(jlnt);
               }
            }

            this.addAttribute(va);
         }
      }

   }

   public void printTags(PrintWriter writerOut) {
      Iterator it = this.attributes.iterator();

      while(it.hasNext()) {
         Attribute a = (Attribute)it.next();
         a.print(writerOut);
      }

   }
}
