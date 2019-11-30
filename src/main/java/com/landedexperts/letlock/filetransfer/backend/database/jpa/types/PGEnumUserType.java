package com.landedexperts.letlock.filetransfer.backend.database.jpa.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PGEnumUserType implements UserType, ParameterizedType {

    private Class<Enum> enumClass;

    public PGEnumUserType() {
        super();
    }

    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClassName");
        try {
            enumClass = (Class<Enum>) Class.forName(enumClassName);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Enum class not found ", e);
        }

    }

    private static final int[] SQL_TYPES = new int[] { Types.OTHER };

    public int[] sqlTypes() {
        // return new int[] { Types.VARCHAR };
        return SQL_TYPES;
    }

    public Class<?> returnedClass() {
        return enumClass;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String name = rs.getString(names[0]);
        return rs.wasNull() ? null : Enum.valueOf(enumClass, name);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, ((Enum<?>) value), Types.OTHER);
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false; // To change body of implemented methods use File | Settings | File Templates.
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Enum) value;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Object fromXMLString(String xmlValue) {
        return Enum.valueOf(enumClass, xmlValue);
    }

    public String objectToSQLString(Object value) {
        return '\'' + ((Enum<?>) value).name() + '\'';
    }

    public String toXMLString(Object value) {
        return ((Enum) value).name();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {

            if ("com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordEncodingAlgoType"
                    .equals(returnedClass().getCanonicalName())) {
                return PasswordEncodingAlgoType.valueOf(cellContent);
            } else if ("com.landedexperts.letlock.filetransfer.backend.database.jpa.types.PasswordHashAlgoType"
                    .equals(returnedClass().getCanonicalName())) {
                return PasswordHashAlgoType.valueOf(cellContent);
            } else if ("com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType"
                    .equals(returnedClass().getCanonicalName())) {
                return UserStatusType.valueOf(cellContent);
            }
            return null;
            // return mapper.readValue(cellContent.getBytes("UTF-8"), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setObject(index, ((Enum) value), Types.OTHER);
        }

    }
}