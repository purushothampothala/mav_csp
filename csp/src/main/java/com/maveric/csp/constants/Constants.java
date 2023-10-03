package com.maveric.csp.constants;

public class Constants{

    public static final int MAXIMUM_DORMANT_DAYS =10;
    public static final String DEFAULT_PAGE_VALUE ="0";
    public static final int DEFAULT_SIZE=0;
    public static final String DEFAULT_PAGE_SIZE_VALUE="10";
    public static final char ACTIVE_STATUS='A';
    public static final char ARCHIVE_STATUS='X';
    public static final char DELETE_STATUS='D';
    public static final  char ARCHIVE_FLAG_NO='N';
    public static final char ARCHIVE_FLAG_YES='Y';
public static final String BASE_SESSION_URL="/session";
public static final String SESSION_URL_ID="/{id}";
public static final String SESSION_ARCHIVE_URL="/archived/{id}";
public static final String SESSION_STATUS_URL="/{status}";
    public static final String JOIN_COLUMN_CUSTOMER="customer_id";
    public static final String SESSION_SEQUENCE_NAME="session_seq";
    public static final String SESSION_SEQUENCE_STRATEGY="com.maveric.csp.config.StringPrefixedSequenceIdGenerator";
    public static final String SEQUENCE="session_seq";
    public static final String INCREMENT_PARAM_VALUE="1";
    public static final String PREFIX_PARAMETER="SESSION_";
    public static final String NUMBER_FORMAT_PARAMETER_VALUE="%01d";
    public static final String SEQUENCE_GENERATOR_VALUE_PREFIX="valuePrefix";
    public static final String SEQUENCE_GENERATOR_NUMBERFORMAT="numberFormat";
    public static final String SEQUENCE_GENERATOR_NUMBER_FORMAT_DEFAULT="%d";
    public static final  String DATE_PATTERN="yyyy-MM-dd HH:mm:ss.SSS";

    public static final  String SESSION_NAME_ERR= "SessionName is required";
    public static final  String CREATED_BY_ERR= "created by is required";
    public static final String REMARKS_ERR="Remarks is required";
    public static final String CUSTOMER_NAME_ERR="Customer name is required";
    public static final String SESSION_ARCHIVED="Session Archived successfully";
    public static final String SESSION_DELETED="session deleted successfully";

    public static final String CUSTOMER_NOT_FOUND="Customer Not found with the given Id";

    public static final String PAGE_SIZE_EXCEPTION="Page index must not be less than zero ";

    public static final String MANDATORY_FIELDS_EXCEPTION="Mandatory fields required to fill";

    public static final String SESSION_NOT_FOUND_EXCEPTION="Session Not Found with the given Id";

    public static final String ARCHIVE_SESSION_EXCEPTION="we can archive a session only after 10 days from the date when it has updated";

    public static final String INVALID_CREDENTIALS="Invalid credentials";
    public static final String STATUS_NOT_FOUND_EXCEPTION="Records not found with the given status";
    public static final String ALREADY_DELETED_EXCEPTION="Session already deleted with the given id";

    public static final String LOGGING_BEFORE="execution(* com.maveric.csp.serviceimpl.*.*(..))";
    public static final String LOGGING_AFTER_RETURNING="execution(* com.maveric.csp.serviceimpl.*.*(..))";

    private Constants(){

    }
}
