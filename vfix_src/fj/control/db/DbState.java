package fj.control.db;

import fj.Unit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbState {
   private final Connector pc;
   private final DB<Unit> terminal;
   private static final DB<Unit> rollback = new DB<Unit>() {
      public Unit run(Connection c) throws SQLException {
         c.rollback();
         return Unit.unit();
      }
   };
   private static final DB<Unit> commit = new DB<Unit>() {
      public Unit run(Connection c) throws SQLException {
         c.commit();
         return Unit.unit();
      }
   };

   private DbState(Connector pc, DB<Unit> terminal) {
      this.pc = pc;
      this.terminal = terminal;
   }

   public static Connector driverManager(final String url) {
      return new Connector() {
         public Connection connect() throws SQLException {
            return DriverManager.getConnection(url);
         }
      };
   }

   public static DbState reader(String url) {
      return new DbState(driverManager(url), rollback);
   }

   public static DbState writer(String url) {
      return new DbState(driverManager(url), commit);
   }

   public static DbState reader(Connector pc) {
      return new DbState(pc, rollback);
   }

   public static DbState writer(Connector pc) {
      return new DbState(pc, commit);
   }

   public <A> A run(DB<A> dba) throws SQLException {
      Connection c = this.pc.connect();
      c.setAutoCommit(false);

      Object var4;
      try {
         A a = dba.run(c);
         this.terminal.run(c);
         var4 = a;
      } catch (SQLException var8) {
         c.rollback();
         throw var8;
      } finally {
         c.close();
      }

      return var4;
   }
}
