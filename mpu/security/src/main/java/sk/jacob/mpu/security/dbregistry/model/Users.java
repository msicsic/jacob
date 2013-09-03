package sk.jacob.mpu.security.dbregistry.model;

import sk.jacob.sql.Metadata;
import sk.jacob.sql.ddl.Column;
import sk.jacob.sql.ddl.Table;

import static sk.jacob.sql.ddl.Column.options;
import static sk.jacob.sql.ddl.TYPE.Boolean;
import static sk.jacob.sql.ddl.TYPE.String;

/**
 * Enum of usernames.
 */
public class Users extends Table {
    public static final String NAME = "USERS";
    public Users(Metadata metadata) {
        super(NAME, metadata);
    }

    /**
     * Login name of user.
     */
    public final Column login = new Column(this, "login",
                                           String(50), options().primaryKey());

    /**
     * Meaningful name of user.
     */
    public final Column username = new Column(this, "username",
                                              String(100), options().nullable(false));

    /**
     * Md5 hashed password.
     */
    public final Column md5pwd = new Column(this, "md5pwd",
                                            String(32), options().nullable(false));

    /**
     * Token string used for request authenticating.
     */
    public final Column token = new Column(this, "token",
                                           String(256), options().nullable(true).unique(true));

    /**
     * Mark if user is admin. Only one admin is permitted.
     */
    public final Column admin = new Column(this, "admin",
                                           Boolean(), options().nullable(false));
}
