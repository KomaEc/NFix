package fj.control.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Connector {
   public abstract Connection connect() throws SQLException;
}
