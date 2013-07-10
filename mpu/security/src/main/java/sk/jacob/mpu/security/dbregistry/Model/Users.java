package sk.jacob.mpu.security.dbregistry.Model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.*;

public class Users extends Table {
    public static final String NAME = "USERS";
    public Users(Metadata metadata) {
        super(NAME, metadata);
    }

    /* Enum of usernames
     *
     * login - login name of user
     * username - meaningful name of user
     * md5pwd - md5 hashed password
     * token - token string used for request authenticating
     * admin - mark if user is admin. Only one admin is permitted.
     */
    public final Column login = new Column(this, "login", String(50), options().primaryKey());
    public final Column username = new Column(this, "username", String(100), options().nullable(false));
    public final Column md5pwd = new Column(this, "md5pwd", String(32), options().nullable(false));
    public final Column token = new Column(this, "token", String(256), options().nullable(false).unique(true));
    public final Column admin = new Column(this, "admin", Boolean(), options().nullable(false));
}
