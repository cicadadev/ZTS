package gcp.external.kainess.gvia_util;

/**
 * @Class        : ERRORCODE
 * @Date         : 2015. 3. 1. 
 * @Author       : 박민호
 * @Description  :
 */
public class ErrorCode
{
    public static final int ERROR_SUCCESS = -1;
    
    public static final int ERROR_DEFAULT = 0;
    
    public static final int ERROR_SOCKET_CONNECT = 1;
    public static final int ERROR_EXECUTE_FAIL = 2;    
    
    public static final int ERROR_PACKET_HEADER_UNKNOWN = 10;
    public static final int ERROR_PACKET_SIZE_MAX_LIMIT = 11;
    public static final int ERROR_PACKET_SIZE_MIN_LIMIT = 12;
    
    public static final int ERROR_ADDR_COUNT_MAX_LIMIT = 20;
    
    public static final int ERROR_UNKNOWN = 99;
}
