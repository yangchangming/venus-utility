/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package venus.frames.jdbc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.sql.*;

/**
 * Generic utility methods for working with JDBC. Mainly for internal use
 * within the framework, but also useful for custom JDBC access code.
 * 
 * 由于spring2.0.6中去除了countParameterPlaceholders该方法，所以迁移的时候抽取spring1.1的该类的方法。
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 */
public abstract class UdpJdbcUtils {

	private static final Log logger = LogFactory.getLog(UdpJdbcUtils.class);

	/**
	 * Count the occurrences of the character <code>placeholder</code> in an SQL string
	 * <code>str</code>. The character <code>placeholder</code> is not counted if it
	 * appears within a literal as determined by the <code>delimiters</code> that are passed in.
	 * <p>Examples: If one of the delimiters is the single quote, and the character to count the
	 * occurrences of is the question mark, then:
	 * <p><code>The big ? 'bad wolf?'</code> gives a count of one.<br>
	 * <code>The big ?? bad wolf</code> gives a count of two.<br>
	 * <code>The big  'ba''ad?' ? wolf</code> gives a count of one.
	 * <p>The grammar of the string passed in should obey the rules of the JDBC spec
	 * which is close to no rules at all: one placeholder per parameter, and it should
	 * be valid SQL for the target database.
	 * @param str string to search in. Returns 0 if this is null
	 * @param placeholder the character to search for and count.
	 * @param delimiters the delimiters for character literals.
	 */
	public static int countParameterPlaceholders(String str, char placeholder, String delimiters) {
		int count = 0;
		boolean insideLiteral = false;
		int activeLiteral = -1;
		for (int i = 0; str != null && i < str.length(); i++) {
			if (str.charAt(i) == placeholder) {
				if (!insideLiteral)
					count++;
			}
 			else {
				if (delimiters.indexOf(str.charAt(i)) > -1) {
					if (!insideLiteral) {
						insideLiteral = true;
						activeLiteral = delimiters.indexOf(str.charAt(i));
					}
					else {
						if (activeLiteral == delimiters.indexOf(str.charAt(i))) {
							insideLiteral = false;
							activeLiteral = -1;
						}
					}
				}
			}
		}
		return count;
	}

    /**
     * Determine the column name to use. The column name is determined based on a
     * lookup using ResultSetMetaData.
     * <p>This method implementation takes into account recent clarifications
     * expressed in the JDBC 4.0 specification:
     * <p><i>columnLabel - the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the column</i>.
     * @return the column name to use
     * @param resultSetMetaData the current meta data to use
     * @param columnIndex the index of the column for the look up
     * @throws SQLException in case of lookup failure
     */
    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (name == null || name.length() < 1) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }


    /**
     * Retrieve a JDBC column value from a ResultSet, using the specified value type.
     * <p>Uses the specifically typed ResultSet accessor methods, falling back to
     * {@link #getResultSetValue(ResultSet, int)} for unknown types.
     * <p>Note that the returned value may not be assignable to the specified
     * required type, in case of an unknown type. Calling code needs to deal
     * with this case appropriately, e.g. throwing a corresponding exception.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @param requiredType the required value type (may be <code>null</code>)
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     */
    public static Object getResultSetValue(ResultSet rs, int index, Class requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }

        Object value = null;
        boolean wasNullCheck = false;

        // Explicitly extract typed value, as far as possible.
        if (String.class.equals(requiredType)) {
            value = rs.getString(index);
        }
        else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
            value = Boolean.valueOf(rs.getBoolean(index));
            wasNullCheck = true;
        }
        else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
            value = new Byte(rs.getByte(index));
            wasNullCheck = true;
        }
        else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
            value = new Short(rs.getShort(index));
            wasNullCheck = true;
        }
        else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            value = new Integer(rs.getInt(index));
            wasNullCheck = true;
        }
        else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            value = new Long(rs.getLong(index));
            wasNullCheck = true;
        }
        else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            value = new Float(rs.getFloat(index));
            wasNullCheck = true;
        }
        else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
                Number.class.equals(requiredType)) {
            value = new Double(rs.getDouble(index));
            wasNullCheck = true;
        }
        else if (byte[].class.equals(requiredType)) {
            value = rs.getBytes(index);
        }
        else if (Date.class.equals(requiredType)) {
            value = rs.getDate(index);
        }
        else if (Time.class.equals(requiredType)) {
            value = rs.getTime(index);
        }
        else if (Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
            value = rs.getTimestamp(index);
        }
        else if (BigDecimal.class.equals(requiredType)) {
            value = rs.getBigDecimal(index);
        }
        else if (Blob.class.equals(requiredType)) {
            value = rs.getBlob(index);
        }
        else if (Clob.class.equals(requiredType)) {
            value = rs.getClob(index);
        }
        else {
            // Some unknown type desired -> rely on getObject.
            value = getResultSetValue(rs, index);
        }

        // Perform was-null check if demanded (for results that the
        // JDBC driver returns as primitives).
        if (wasNullCheck && value != null && rs.wasNull()) {
            value = null;
        }
        return value;
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the most appropriate
     * value type. The returned value should be a detached value object, not having
     * any ties to the active ResultSet: in particular, it should not be a Blob or
     * Clob object but rather a byte array respectively String representation.
     * <p>Uses the <code>getObject(index)</code> method, but includes additional "hacks"
     * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
     * datatype and a <code>java.sql.Date</code> for DATE columns leaving out the
     * time portion: These columns will explicitly be extracted as standard
     * <code>java.sql.Timestamp</code> object.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     * @see Blob
     * @see Clob
     * @see Timestamp
     */
    private static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            obj = rs.getBytes(index);
        }
        else if (obj instanceof Clob) {
            obj = rs.getString(index);
        }
        else if (className != null &&
                ("oracle.sql.TIMESTAMP".equals(className) ||
                "oracle.sql.TIMESTAMPTZ".equals(className))) {
            obj = rs.getTimestamp(index);
        }
        else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) ||
                    "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            }
            else {
                obj = rs.getDate(index);
            }
        }
        else if (obj != null && obj instanceof Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }
}
