package sk.jacob.sql.engine;

import sk.jacob.sql.ddl.Column;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class JacobResultSet implements ResultSet {
    private final ResultSet rs;

    public JacobResultSet(ResultSet rs) {
        this.rs = rs;
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public NClob getNClob(int i) throws SQLException {
        return rs.getNClob(i);
    }

    public void updateFloat(String s, float v) throws SQLException {
        rs.updateFloat(s, v);
    }

    public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {
        rs.updateAsciiStream(i, inputStream);
    }

    public Object getObject(String s, Map<String, Class<?>> stringClassMap) throws SQLException {
        return rs.getObject(s, stringClassMap);
    }

    public void refreshRow() throws SQLException {
        rs.refreshRow();
    }

    public void updateBlob(String s, InputStream inputStream) throws SQLException {
        rs.updateBlob(s, inputStream);
    }

    public void updateBlob(int i, Blob blob) throws SQLException {
        rs.updateBlob(i, blob);
    }

    public void updateCharacterStream(int i, Reader reader) throws SQLException {
        rs.updateCharacterStream(i, reader);
    }

    public Blob getBlob(int i) throws SQLException {
        return rs.getBlob(i);
    }

    public Object getObject(int i, Map<String, Class<?>> stringClassMap) throws SQLException {
        return rs.getObject(i, stringClassMap);
    }

    public void updateNull(int i) throws SQLException {
        rs.updateNull(i);
    }

    public Date getDate(String s, Calendar calendar) throws SQLException {
        return rs.getDate(s, calendar);
    }

    public String getNString(String s) throws SQLException {
        return rs.getNString(s);
    }

    public Array getArray(int i) throws SQLException {
        return rs.getArray(i);
    }

    public Reader getCharacterStream(int i) throws SQLException {
        return rs.getCharacterStream(i);
    }

    public SQLWarning getWarnings() throws SQLException {
        return rs.getWarnings();
    }

    public SQLXML getSQLXML(String s) throws SQLException {
        return rs.getSQLXML(s);
    }

    public void beforeFirst() throws SQLException {
        rs.beforeFirst();
    }

    public boolean first() throws SQLException {
        return rs.first();
    }

    public int getFetchSize() throws SQLException {
        return rs.getFetchSize();
    }

    public void updateBoolean(String s, boolean b) throws SQLException {
        rs.updateBoolean(s, b);
    }

    public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
        rs.updateBinaryStream(s, inputStream, i);
    }

    public void updateTime(String s, Time time) throws SQLException {
        rs.updateTime(s, time);
    }

    public Timestamp getTimestamp(int i) throws SQLException {
        return rs.getTimestamp(i);
    }

    public Reader getNCharacterStream(String s) throws SQLException {
        return rs.getNCharacterStream(s);
    }

    public short getShort(int i) throws SQLException {
        return rs.getShort(i);
    }

    public boolean absolute(int i) throws SQLException {
        return rs.absolute(i);
    }

    public void updateString(String s, String s2) throws SQLException {
        rs.updateString(s, s2);
    }

    public void updateByte(int i, byte b) throws SQLException {
        rs.updateByte(i, b);
    }

    public void updateBinaryStream(int i, InputStream inputStream, int i2) throws SQLException {
        rs.updateBinaryStream(i, inputStream, i2);
    }

    public InputStream getAsciiStream(int i) throws SQLException {
        return rs.getAsciiStream(i);
    }

    public void updateBlob(String s, Blob blob) throws SQLException {
        rs.updateBlob(s, blob);
    }

    public void updateCharacterStream(String s, Reader reader) throws SQLException {
        rs.updateCharacterStream(s, reader);
    }

    public void updateClob(String s, Reader reader, long l) throws SQLException {
        rs.updateClob(s, reader, l);
    }

    public short getShort(String s) throws SQLException {
        return rs.getShort(s);
    }

    public void deleteRow() throws SQLException {
        rs.deleteRow();
    }

    public void updateObject(String s, Object o) throws SQLException {
        rs.updateObject(s, o);
    }

    public int findColumn(String s) throws SQLException {
        return rs.findColumn(s);
    }

    public void updateRef(String s, Ref ref) throws SQLException {
        rs.updateRef(s, ref);
    }

    public void cancelRowUpdates() throws SQLException {
        rs.cancelRowUpdates();
    }

    public Time getTime(String s) throws SQLException {
        return rs.getTime(s);
    }

    public Ref getRef(int i) throws SQLException {
        return rs.getRef(i);
    }

    public String getCursorName() throws SQLException {
        return rs.getCursorName();
    }

    public void updateString(int i, String s) throws SQLException {
        rs.updateString(i, s);
    }

    public void moveToCurrentRow() throws SQLException {
        rs.moveToCurrentRow();
    }

    public Object getObject(String s) throws SQLException {
        return rs.getObject(s);
    }

    public void updateNString(int i, String s) throws SQLException {
        rs.updateNString(i, s);
    }

    public boolean rowDeleted() throws SQLException {
        return rs.rowDeleted();
    }

    public boolean wasNull() throws SQLException {
        return rs.wasNull();
    }

    public int getFetchDirection() throws SQLException {
        return rs.getFetchDirection();
    }

    public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {
        rs.updateNCharacterStream(s, reader, l);
    }

    public InputStream getUnicodeStream(int i) throws SQLException {
        return rs.getUnicodeStream(i);
    }

    public InputStream getBinaryStream(int i) throws SQLException {
        return rs.getBinaryStream(i);
    }

    public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {
        rs.updateBlob(s, inputStream, l);
    }

    public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {
        rs.updateBinaryStream(s, inputStream);
    }

    public void updateByte(String s, byte b) throws SQLException {
        rs.updateByte(s, b);
    }

    public void updateShort(String s, short i) throws SQLException {
        rs.updateShort(s, i);
    }

    public void updateArray(int i, Array array) throws SQLException {
        rs.updateArray(i, array);
    }

    public void updateClob(String s, Clob clob) throws SQLException {
        rs.updateClob(s, clob);
    }

    public int getInt(String s) throws SQLException {
        return rs.getInt(s);
    }

    public void updateNClob(String s, Reader reader, long l) throws SQLException {
        rs.updateNClob(s, reader, l);
    }

    public void updateBytes(int i, byte[] bytes) throws SQLException {
        rs.updateBytes(i, bytes);
    }

    public void updateClob(int i, Reader reader) throws SQLException {
        rs.updateClob(i, reader);
    }

    public void updateDate(String s, Date date) throws SQLException {
        rs.updateDate(s, date);
    }

    public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
        return rs.getTimestamp(s, calendar);
    }

    public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
        rs.updateTimestamp(s, timestamp);
    }

    public Reader getNCharacterStream(int i) throws SQLException {
        return rs.getNCharacterStream(i);
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return rs.getMetaData();
    }

    public String getString(String s) throws SQLException {
        return rs.getString(s);
    }

    public String getString(Column c) throws SQLException {
        return rs.getString(c.alias());
    }

    public void updateNull(String s) throws SQLException {
        rs.updateNull(s);
    }

    public Time getTime(int i, Calendar calendar) throws SQLException {
        return rs.getTime(i, calendar);
    }

    public boolean isAfterLast() throws SQLException {
        return rs.isAfterLast();
    }

    public void close() throws SQLException {
        rs.close();
    }

    public boolean relative(int i) throws SQLException {
        return rs.relative(i);
    }

    public void updateRow() throws SQLException {
        rs.updateRow();
    }

    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return rs.isWrapperFor(aClass);
    }

    public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {
        rs.updateSQLXML(s, sqlxml);
    }

    public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
        rs.updateAsciiStream(s, inputStream, i);
    }

    public boolean rowUpdated() throws SQLException {
        return rs.rowUpdated();
    }

    public InputStream getAsciiStream(String s) throws SQLException {
        return rs.getAsciiStream(s);
    }

    public boolean getBoolean(int i) throws SQLException {
        return rs.getBoolean(i);
    }

    public void updateNClob(String s, NClob nClob) throws SQLException {
        rs.updateNClob(s, nClob);
    }

    public void setFetchDirection(int i) throws SQLException {
        rs.setFetchDirection(i);
    }

    public int getInt(int i) throws SQLException {
        return rs.getInt(i);
    }

    public void updateNClob(int i, Reader reader) throws SQLException {
        rs.updateNClob(i, reader);
    }

    public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {
        rs.updateAsciiStream(s, inputStream);
    }

    public void updateNClob(int i, NClob nClob) throws SQLException {
        rs.updateNClob(i, nClob);
    }

    public void updateRowId(String s, RowId rowId) throws SQLException {
        rs.updateRowId(s, rowId);
    }

    public void updateFloat(int i, float v) throws SQLException {
        rs.updateFloat(i, v);
    }

    public byte getByte(String s) throws SQLException {
        return rs.getByte(s);
    }

    public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
        rs.updateBigDecimal(s, bigDecimal);
    }

    public void updateClob(int i, Reader reader, long l) throws SQLException {
        rs.updateClob(i, reader, l);
    }

    public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
        rs.updateCharacterStream(s, reader, i);
    }

    public Time getTime(int i) throws SQLException {
        return rs.getTime(i);
    }

    public int getType() throws SQLException {
        return rs.getType();
    }

    public Blob getBlob(String s) throws SQLException {
        return rs.getBlob(s);
    }

    public BigDecimal getBigDecimal(String s) throws SQLException {
        return rs.getBigDecimal(s);
    }

    public void updateObject(int i, Object o, int i2) throws SQLException {
        rs.updateObject(i, o, i2);
    }

    public void clearWarnings() throws SQLException {
        rs.clearWarnings();
    }

    public byte[] getBytes(String s) throws SQLException {
        return rs.getBytes(s);
    }

    public <T> T getObject(int i, Class<T> tClass) throws SQLException {
        return rs.getObject(i, tClass);
    }

    public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {
        rs.updateCharacterStream(s, reader, l);
    }

    public int getHoldability() throws SQLException {
        return rs.getHoldability();
    }

    public double getDouble(int i) throws SQLException {
        return rs.getDouble(i);
    }

    public void updateBoolean(int i, boolean b) throws SQLException {
        rs.updateBoolean(i, b);
    }

    public void updateNString(String s, String s2) throws SQLException {
        rs.updateNString(s, s2);
    }

    public double getDouble(String s) throws SQLException {
        return rs.getDouble(s);
    }

    public double getDouble(Column c) throws SQLException {
        return rs.getDouble(c.alias());
    }

    public boolean getBoolean(String s) throws SQLException {
        return rs.getBoolean(s);
    }

    public boolean getBoolean(Column c) throws SQLException {
        return rs.getBoolean(c.alias());
    }

    public int getConcurrency() throws SQLException {
        return rs.getConcurrency();
    }

    public boolean isLast() throws SQLException {
        return rs.isLast();
    }

    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
        rs.updateNCharacterStream(i, reader, l);
    }

    public void updateClob(int i, Clob clob) throws SQLException {
        rs.updateClob(i, clob);
    }

    public long getLong(int i) throws SQLException {
        return rs.getLong(i);
    }

    public float getFloat(String s) throws SQLException {
        return rs.getFloat(s);
    }

    public BigDecimal getBigDecimal(int i, int i2) throws SQLException {
        return rs.getBigDecimal(i, i2);
    }

    public void updateNCharacterStream(String s, Reader reader) throws SQLException {
        rs.updateNCharacterStream(s, reader);
    }

    public void updateArray(String s, Array array) throws SQLException {
        rs.updateArray(s, array);
    }

    public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
        rs.updateBigDecimal(i, bigDecimal);
    }

    public String getString(int i) throws SQLException {
        return rs.getString(i);
    }

    public boolean last() throws SQLException {
        return rs.last();
    }

    public Clob getClob(String s) throws SQLException {
        return rs.getClob(s);
    }

    public void updateCharacterStream(int i, Reader reader, int i2) throws SQLException {
        rs.updateCharacterStream(i, reader, i2);
    }

    public Clob getClob(int i) throws SQLException {
        return rs.getClob(i);
    }

    public NClob getNClob(String s) throws SQLException {
        return rs.getNClob(s);
    }

    public void setFetchSize(int i) throws SQLException {
        rs.setFetchSize(i);
    }

    public BigDecimal getBigDecimal(int i) throws SQLException {
        return rs.getBigDecimal(i);
    }

    public void updateTime(int i, Time time) throws SQLException {
        rs.updateTime(i, time);
    }

    public void updateClob(String s, Reader reader) throws SQLException {
        rs.updateClob(s, reader);
    }

    public void updateLong(int i, long l) throws SQLException {
        rs.updateLong(i, l);
    }

    public boolean previous() throws SQLException {
        return rs.previous();
    }

    public URL getURL(int i) throws SQLException {
        return rs.getURL(i);
    }

    public String getNString(int i) throws SQLException {
        return rs.getNString(i);
    }

    public Statement getStatement() throws SQLException {
        return rs.getStatement();
    }

    public Object getObject(int i) throws SQLException {
        return rs.getObject(i);
    }

    public InputStream getUnicodeStream(String s) throws SQLException {
        return rs.getUnicodeStream(s);
    }

    public SQLXML getSQLXML(int i) throws SQLException {
        return rs.getSQLXML(i);
    }

    public void updateObject(String s, Object o, int i) throws SQLException {
        rs.updateObject(s, o, i);
    }

    public void updateDate(int i, Date date) throws SQLException {
        rs.updateDate(i, date);
    }

    public Reader getCharacterStream(String s) throws SQLException {
        return rs.getCharacterStream(s);
    }

    public void updateShort(int i, short i2) throws SQLException {
        rs.updateShort(i, i2);
    }

    public boolean rowInserted() throws SQLException {
        return rs.rowInserted();
    }

    public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
        rs.updateAsciiStream(i, inputStream, l);
    }

    public int getRow() throws SQLException {
        return rs.getRow();
    }

    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
        rs.updateSQLXML(i, sqlxml);
    }

    public RowId getRowId(String s) throws SQLException {
        return rs.getRowId(s);
    }

    public void updateAsciiStream(int i, InputStream inputStream, int i2) throws SQLException {
        rs.updateAsciiStream(i, inputStream, i2);
    }

    public void updateRef(int i, Ref ref) throws SQLException {
        rs.updateRef(i, ref);
    }

    public Date getDate(String s) throws SQLException {
        return rs.getDate(s);
    }

    public <T> T getObject(String s, Class<T> tClass) throws SQLException {
        return rs.getObject(s, tClass);
    }

    public Array getArray(String s) throws SQLException {
        return rs.getArray(s);
    }

    public void updateBytes(String s, byte[] bytes) throws SQLException {
        rs.updateBytes(s, bytes);
    }

    public byte getByte(int i) throws SQLException {
        return rs.getByte(i);
    }

    public void updateLong(String s, long l) throws SQLException {
        rs.updateLong(s, l);
    }

    public Date getDate(int i, Calendar calendar) throws SQLException {
        return rs.getDate(i, calendar);
    }

    public void updateDouble(int i, double v) throws SQLException {
        rs.updateDouble(i, v);
    }

    public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
        return rs.getTimestamp(i, calendar);
    }

    public InputStream getBinaryStream(String s) throws SQLException {
        return rs.getBinaryStream(s);
    }

    public void updateRowId(int i, RowId rowId) throws SQLException {
        rs.updateRowId(i, rowId);
    }

    public URL getURL(String s) throws SQLException {
        return rs.getURL(s);
    }

    public boolean isFirst() throws SQLException {
        return rs.isFirst();
    }

    public void updateObject(int i, Object o) throws SQLException {
        rs.updateObject(i, o);
    }

    public byte[] getBytes(int i) throws SQLException {
        return rs.getBytes(i);
    }

    public float getFloat(int i) throws SQLException {
        return rs.getFloat(i);
    }

    public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {
        rs.updateBinaryStream(i, inputStream);
    }

    public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {
        rs.updateBlob(i, inputStream, l);
    }

    public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
        rs.updateBinaryStream(i, inputStream, l);
    }

    public void moveToInsertRow() throws SQLException {
        rs.moveToInsertRow();
    }

    public boolean isBeforeFirst() throws SQLException {
        return rs.isBeforeFirst();
    }

    public void updateInt(String s, int i) throws SQLException {
        rs.updateInt(s, i);
    }

    public void updateDouble(String s, double v) throws SQLException {
        rs.updateDouble(s, v);
    }

    public void updateBlob(int i, InputStream inputStream) throws SQLException {
        rs.updateBlob(i, inputStream);
    }

    public Date getDate(int i) throws SQLException {
        return rs.getDate(i);
    }

    public void updateNClob(String s, Reader reader) throws SQLException {
        rs.updateNClob(s, reader);
    }

    public void updateNCharacterStream(int i, Reader reader) throws SQLException {
        rs.updateNCharacterStream(i, reader);
    }

    public BigDecimal getBigDecimal(String s, int i) throws SQLException {
        return rs.getBigDecimal(s, i);
    }

    public Timestamp getTimestamp(String s) throws SQLException {
        return rs.getTimestamp(s);
    }

    public void updateNClob(int i, Reader reader, long l) throws SQLException {
        rs.updateNClob(i, reader, l);
    }

    public void afterLast() throws SQLException {
        rs.afterLast();
    }

    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return rs.unwrap(tClass);
    }

    public long getLong(String s) throws SQLException {
        return rs.getLong(s);
    }

    public long getLong(Column c) throws SQLException {
        return rs.getLong(c.alias());
    }

    public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
        rs.updateAsciiStream(s, inputStream, l);
    }

    public void insertRow() throws SQLException {
        rs.insertRow();
    }

    public void updateInt(int i, int i2) throws SQLException {
        rs.updateInt(i, i2);
    }

    public boolean isClosed() throws SQLException {
        return rs.isClosed();
    }

    public RowId getRowId(int i) throws SQLException {
        return rs.getRowId(i);
    }

    public Ref getRef(String s) throws SQLException {
        return rs.getRef(s);
    }

    public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
        rs.updateTimestamp(i, timestamp);
    }

    public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
        rs.updateBinaryStream(s, inputStream, l);
    }

    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
        rs.updateCharacterStream(i, reader, l);
    }

    public Time getTime(String s, Calendar calendar) throws SQLException {
        return rs.getTime(s, calendar);
    }
}
