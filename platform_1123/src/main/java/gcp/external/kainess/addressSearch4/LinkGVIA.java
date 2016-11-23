package gcp.external.kainess.addressSearch4;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import gcp.external.kainess.gvia_util.ErrorCode;
import gcp.external.kainess.gvia_util.ProptInfo;
import gcp.external.kainess.vo.GviaAddressMultVo;
import gcp.external.kainess.vo.GviaAddressToSearchVo;
import gcp.external.kainess.vo.GviaAddressVo;
import gcp.external.kainess.vo.GviaDistrictMultVo;
import gcp.external.kainess.vo.GviaDistrictVo;
import gcp.external.kainess.vo.GviaPostSearchNewMultVo;
import gcp.external.kainess.vo.GviaPostSearchNewVo;
import gcp.external.kainess.vo.GviaPostSearchTotCountVo;

public class LinkGVIA extends ConfigGVIA
{
    private boolean logUse = false;
    private String API_VER = "1.0.3.8";
    private String API_USER_VER = API_USER;
    
	private String		SVRIP1			= "49.50.33.154";
    private int PORT1 = 7900;
    
	private String		SVRIP2			= "49.50.33.154";
    private int PORT2 = 7900;
    
    private int Linger_TIME = 0; // 연결 요청시간(기본:0-서버 디폴트)
    private int Connection_TIME = 5000; // 연결 후 대기시간(기본:5초, 배치:10초)
    
    private String GPoint_ver = "2";
    
    private Socket socket = null;
    
    static String strResult;
    static String sFail = "300200100|09900000000000000000000000000000000||||||||||||||||||||||||||||||||||||||||||||||||||000|;";
    static StringBuffer sbFail = new StringBuffer("300200100|09900000000000000000000000000000000||||||||||||||||||||||||||||||||||||||||||||||||||000|;");
    
    static ProptInfo proptInfo = null;
    
    /**
     * LinkGVIA 클래스 함수
     * 
     * @Description : 내부 통신 정보를 이용한다.
     */
    public LinkGVIA()
    {
    }
    
    /**
     * LinkGVIA 클래스 함수
     * 
     * @Description : 통신 정보를 설정한다.
     * @param strIp : 서버IP
     * @param nPort : 서버PORT
     */
    public LinkGVIA(String strIp, int nPort)
    {
        setSVRIP1(strIp);
        setPORT1(nPort);
    }
    
    /**
     * LinkGVIA 클래스 함수
     * 
     * @Description : 통신 정보를 설정한다.
     * @param strIp : 서버IP
     * @param nPort : 서버PORT
     * @param strIp2 : 서버IP2
     * @param nPort2 : 서버PORT2
     */
    public LinkGVIA(String strIp, int nPort, String strIp2, int nPort2)
    {
        setSVRIP1(strIp);
        setPORT1(nPort);
        setSVRIP2(strIp2);
        setPORT2(nPort2);
    }
    
    /**
     * LinkGVIA 클래스 함수
     * 
     * @Description : 프로퍼티를 이용하여 통신 및 버전 정보를 설정한다.
     * @param proptUrl : 프로퍼티 경로
     */
    public LinkGVIA(String proptUrl)
    {
        // properties 정보 입력
        setProptInfo(proptUrl);
        
        if (proptUrl != null && !proptUrl.equals(""))
        {
            setSVRIP1(proptInfo.getGviaIp1());
            setPORT1(proptInfo.getGviaPort1());
            setSVRIP2(proptInfo.getGviaIp2());
            setPORT2(proptInfo.getGviaPort2());
            setGPoint_ver(proptInfo.getGVersion());
        }
    }
    
    public String getApiVER()
    {
        return API_VER;
    }
    
    public String getApiUserVER()
    {
        return API_USER_VER;
    }
    
    public void setProptInfo(String proptUrl)
    {
        proptInfo = new ProptInfo(proptUrl);
    }
    
    public ProptInfo getProptInfo()
    {
        return proptInfo;
    }
    
    public String getSVRIP1()
    {
        return SVRIP1;
    }
    
    public void setSVRIP1(String sVRIP1)
    {
        SVRIP1 = sVRIP1;
    }
    
    public int getPORT1()
    {
        return PORT1;
    }
    
    public void setPORT1(int pORT1)
    {
        PORT1 = pORT1;
    }
    
    public String getSVRIP2()
    {
        return SVRIP2;
    }
    
    public void setSVRIP2(String sVRIP2)
    {
        SVRIP2 = sVRIP2;
    }
    
    public int getPORT2()
    {
        return PORT2;
    }
    
    public void setPORT2(int pORT2)
    {
        PORT2 = pORT2;
    }
    
    public int getConnection_TIME()
    {
        return Connection_TIME;
    }
    
    public void setConnection_TIME(int vConnection_TIME)
    {
        Connection_TIME = vConnection_TIME;
    }
    
    public String getGPoint_ver()
    {
        return GPoint_ver;
    }
    
    public void setGPoint_ver(String gPoint_ver)
    {
        GPoint_ver = gPoint_ver;
    }
    
    public boolean SocketConnect()
    {
        try
        {
            socket = new Socket();
            socket.setSoLinger(true, Linger_TIME);
            socket.setSoTimeout(Connection_TIME);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(SVRIP1, PORT1);
            socket.connect(inetSocketAddress, 500);
            
            // System.out.println("SocketConnect() SVRIP1:"+SVRIP1);
            // System.out.println("SocketConnect() PORT1:"+PORT1);
        }
        catch (IOException ex)
        {
            if (PORT2 != 0)
            {
                try
                {
                    socket = new Socket();
                    socket.setSoLinger(true, Linger_TIME);
                    socket.setSoTimeout(Connection_TIME);
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(SVRIP2, PORT2);
                    socket.connect(inetSocketAddress, 500);
                    
                    // System.out.println("SocketConnect() SVRIP2:"+SVRIP2);
                    // System.out.println("SocketConnect() PORT2:"+PORT2);
                }
                catch (IOException ex2)
                {
                    System.out.println("Connection Exception2 : " + ex2.toString());
                    return false;
                }
            }
            else
            {
                System.out.println("Connection Exception1 : " + ex.toString());
                return false;
            }
        }
        return true;
    }
    
    public boolean SocketClose()
    {
        try
        {
            if (socket != null)
            {
                socket.close();
                socket = null;
            }
        }
        catch (IOException ioe)
        {
            return false;
        }
        return true;
    }
    
    // ////////////////// gvia_util start ////////////////////
    /**
     * lpad 함수
     * 
     * @param str 대상문자열, len 길이, addStr 대체문자
     * @return 문자열
     */
    public static String lpad(String str, int size, String fStr)
    {
        
        try
        {
            int tmp = size - str.getBytes("EUC-KR").length;
            // System.out.println("tmp : " + tmp);
            for (int i = 0; i < tmp; i++)
            {
                str = fStr + str;
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return str;
    }
    
    /**
     * rpad 함수
     * 
     * @param str 대상문자열, len 길이, addStr 대체문자
     * @return 문자열
     */
    public static String rpad(String str, int size, String fStr)
    {
        
        try
        {
            int tmp = size - str.getBytes("EUC-KR").length;
            // System.out.println("tmp : " + tmp);
            for (int i = 0; i < tmp; i++)
            {
                str += fStr;
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return str;
    }
    
    /**
     * subString 함수
     * 바이트단위로 substring
     * 
     * @param str, startIndex, length
     * @return String
     */
    public static String subString(String strData, int iStartPos, int iByteLength)
    {
        byte[] bytTemp = null;
        int iRealStart = 0;
        int iRealEnd = 0;
        int iLength = 0;
        int iChar = 0;
        
        try
        {
            // UTF-8로 변환하는경우 한글 2Byte, 기타 1Byte로 떨어짐
            bytTemp = strData.getBytes("EUC-KR");
            iLength = bytTemp.length;
            
            for (int iIndex = 0; iIndex < iLength; iIndex++)
            {
                if (iStartPos <= iIndex)
                {
                    break;
                }
                iChar = (int) bytTemp[iIndex];
                if ((iChar > 127) || (iChar < 0))
                {
                    // 한글의 경우(2byte 통과처리)
                    // 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
                    iRealStart++;
                    iIndex++;
                }
                else
                {
                    // 기타 글씨(1Byte 통과처리)
                    iRealStart++;
                }
            }
            
            iRealEnd = iRealStart;
            int iEndLength = iRealStart + iByteLength;
            for (int iIndex = iRealStart; iIndex < iEndLength; iIndex++)
            {
                iChar = (int) bytTemp[iIndex];
                if ((iChar > 127) || (iChar < 0))
                {
                    // 한글의 경우(2byte 통과처리)
                    // 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
                    iRealEnd++;
                    iIndex++;
                }
                else
                {
                    // 기타 글씨(1Byte 통과처리)
                    iRealEnd++;
                }
            }
        }
        catch (Exception e)
        {
            //
        }
        
        if (iRealEnd > iLength)
        {
            // System.out.println("  ==> Integer.toString(iLength)1 : " + Integer.toString(iLength));
            // System.out.println("  ==> Integer.toString(iRealEnd)1 : " + Integer.toString(iRealEnd));
            
            return strData.substring(iRealStart, iLength);
        }
        
        // System.out.println("  ==> Integer.toString(iLength)2 : " + Integer.toString(iLength));
        // System.out.println("  ==> Integer.toString(iRealEnd)2 : " + Integer.toString(iRealEnd));
        
        return strData.substring(iRealStart, iRealEnd);
    }
    
    // ////////////////// gvia_util end ////////////////////
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        
    }
    
    // ////////////////// gvia start ////////////////////
    /**
     * @Method ExecCallGvia
     * @data 2015.03.11
     * @Description : Gvia엔진에서 InOut 처리하는 함수
     * @param txtType : InOutProtocol
     * @param mkAddrSearch : 데이터 길이가 포함되지 않은 패킷 정의가 끝난 검색용 데이터
     * @return ret : GVIA엔진 결과
     */
    public String ExecCallGvia(String txtType, String mkAddrSearch)
    {
        BufferedWriter wr = null;
        StringBuffer inTxt = new StringBuffer("");
        String ret = "";
        InputStream InStr = null;
        ByteArrayOutputStream bout = null;
        OutputStreamWriter OutStrWrit = null;
        byte[] buf = new byte[0];
        int bufSize = 0;
        int inSize = 0;
        int minLen = 0;
        int maxLen = 0;
        int mkASLen = 0;
        
        // txtType = cvtGviaHeadCode(txtType); // keep버전 코드 Gvia로 대체
        txtType = cvtGviaHeadCode(txtType, mkAddrSearch); // keep버전 코드 Gvia로 대체
        
        if (txtType == null || mkAddrSearch == null || txtType.equals("") || mkAddrSearch.equals("")) return "";
        
        mkAddrSearch = mkAddrSearch.trim();
        
        // 인젝션 검사(검색에서 sql인젝션 검사를 실시하며 검색어에 인젝션 유형이 포함되어 있다면 종료시킨다.)
        if (!chkGviaInjection(txtType, mkAddrSearch)) return "";
        
        mkASLen = mkAddrSearch.length();
        
        try
        {
            minLen = getGviaExecMinLen(txtType); // 2015.03.11 입력에 따라 검색가능 최소길이를 가져온다.
            maxLen = getGviaExecMaxLen(txtType); // 2015.04.16 입력에 따라 검색가능 최대길이를 가져온다.(100건 배치 용)
            bufSize = getGviaBufSize(txtType); // 2015.03.11 입력에 따라 버퍼 사이즈를 가져온다.
            
            // 필요시 검색용 데이터 생성 함수 MakeGviaInData 에서 처리할 것
            // if( (mkASLen >= minLen) && (mkAddrSearch.substring(mkASLen - minLen, mkASLen)).equals("null") )
            // {
            // mkAddrSearch = mkAddrSearch.substring(0, mkASLen - minLen);
            // mkAddrSearch = mkAddrSearch.trim();
            // mkASLen = mkAddrSearch.length();
            // }
            
            if (mkASLen < minLen)
            {
                return "";
            }
            
            // inSize = 1 + mkAddrSearch.toString().getBytes("EUC-KR").length; // 2014-10-16
            inSize = 1 + mkAddrSearch.toString().getBytes("MS949").length; // 2015-06-11
            
            if (inSize > maxLen)
            {
                return "";
            }
            
            inTxt.append(txtType); // 헤더
            
            for (int i = 0; i < (5 - Integer.toString(inSize).length()); i++)
            {
                inTxt.append("0");
            }
            inTxt.append(Integer.toString(inSize) + "|");
            inTxt.append(mkAddrSearch);
            
            outLog("  ==> 원본(ExecCallGvia) : " + inTxt.toString());
            OutStrWrit = new OutputStreamWriter(socket.getOutputStream(), "8859_1");
            
            wr = new BufferedWriter(OutStrWrit);
            // wr.write(new String(inTxt.toString().getBytes("KSC5601"), "8859_1")); //한글완성형에서 윗똠길 검색 안됨
            wr.write(new String(inTxt.toString().getBytes("MS949"), "8859_1"));
            wr.flush();
            
            InStr = socket.getInputStream();
            bout = new ByteArrayOutputStream();
            buf = new byte[bufSize];
            
            int chkCnt = 0;
            int chkLoopCnt = 0;
            int nReceiveSize = 0;
            byte[] byteArray = new byte[0];
            while (true)
            {
                // outLog("chkCnt : " + chkCnt + " / " + bufSize);
                int m = InStr.read(buf);
                if (m > 0)
                {
                    bout.write(buf, 0, m);
                    // outLog(chkCnt + " bout : " + bout.toString("MS949"));
                }
                else
                {
                    outLog("** Buffer Read Zero **");
                    // break;
                }
                
                if (m > 10 && m <= bufSize)
                {
                    chkCnt++;
                    // ret = bout.toString("EUC-KR"); //한글완성형에서 윗똠길 검색 안됨
                    // ret = bout.toString("MS949");
                    ret = bout.toString(); // 2015-08-25 일부 고객사에서 toString 정상 작동이 되지 않는관계로 binary, byte 검사 가능토록 변경
                    // System.out.println("  ==> ret : " + ret);
                    if (chkCnt == 1 && ret.length() > 10)
                    {
                        if (chkCnt == 1)
                        {
                            String[] addrLen1 = ret.split(";");
                            String[] addrLen2 = addrLen1[0].split("[\u007C]");
                            nReceiveSize = Integer.parseInt(addrLen2[0].substring(4, addrLen2[0].length()));
                        }
                    }
                    
                    // outLog(chkCnt + " nReceiveSize : " + nReceiveSize + " / " + m + " / " + (ret.length() - 10));
                    
                    // 2015-07-06 일부환경에서 바이트로 나눠서 가지고 오는 데이터를 정상적으로 substring 하지 못하는 문제로 바이트가 정상일 경우만 마무리 검사를 한다.
                    byteArray = bout.toByteArray();
                    
                    if (nReceiveSize != 0 && nReceiveSize <= ret.length() - 10) // 2015-07-02
                    {
                        outLog("** ReceiveSize No Zero Data Zero Complete break **");
                        break;
                    }
                    else if (nReceiveSize == 0)
                    {
                        if (byteArray[byteArray.length - 1] > -1 && byteArray[byteArray.length - 2] > -1 && byteArray[byteArray.length - 3] > -1 && byteArray[byteArray.length - 4] > -1)
                        {
                            if (ret.substring(ret.length() - 2, ret.length()).equals("|;") || ret.substring(ret.length() - 3, ret.length() - 1).equals("|;") || ret.substring(ret.length() - 4, ret.length() - 2).equals("|;"))
                            {
                                outLog("** ReceiveSize Zero break **");
                                break;
                            }
                        }
                        else
                        {
                            // outLog(chkCnt + " nReceiveSize0_0 : " + byteArray[byteArray.length]);
                            // outLog(chkCnt + " nReceiveSize0_1 : " + byteArray[byteArray.length - 1]);
                            // outLog(chkCnt + " nReceiveSize0_2 : " + byteArray[byteArray.length - 2]);
                            // outLog(chkCnt + " nReceiveSize0_3 : " + byteArray[byteArray.length - 3]);
                            // outLog(chkCnt + " nReceiveSize0_4 : " + byteArray[byteArray.length - 4]);
                        }
                    }
                    else
                    {
                        if (byteArray[byteArray.length - 2] > -1 && byteArray[byteArray.length - 3] > -1 && byteArray[byteArray.length - 4] > -1)
                        {
                            // outLog(chkCnt + " qualf1 : " + (ret.substring(ret.length() - 4, ret.length() - 2)));
                            // outLog(chkCnt + " qualf2 : " + (ret.charAt(ret.length() - 2)));
                            // 2015-07-15 온라인 b 이용 시 종료 구분문자는 ;$ 이므로 해당 조건 추가
                            if ((ret.substring(ret.length() - 4, ret.length() - 2).equals("|;") || ret.substring(ret.length() - 4, ret.length() - 2).equals(";$")) && ret.charAt(ret.length() - 2) == 0x0D)
                            {
                                // 바이트로 나눠서 가져올 경우 한글을 완전히 가져오지 못하는 문제로 인하여 일부 java1.4로우버전에서 charAt 오류가 발생(StringOutOfBoundsException)
                                // 해당 문제로 인하여 byte로 마지막이 온전한 데이터인지 확인 하는 조건 추가
                                // 2015-07-02
                                if (byteArray[byteArray.length - 1] > -1)
                                {
                                    // outLog(chkCnt + " qualf3 : " + (ret.charAt(ret.length() - 1)));
                                    if (ret.charAt(ret.length() - 1) == 0x0A)
                                    {
                                        // outLog(chkCnt + " qualf out");
                                        outLog("** End String OK break **");
                                        break;
                                    }
                                }
                            }
                            // else
                            // { // 2015-07-02 확인용
                            // if (byteArray[byteArray.length - 1] < 0)
                            // {
                            // outLog(chkCnt + " fileArray : " + (char) byteArray[byteArray.length - 1]);
                            // outLog(chkCnt + " fileArray : " + byteArray[byteArray.length - 1]);
                            // outLog(chkCnt + " qualf3 : " + (ret.charAt(ret.length() - 1)));
                            // outLog("==========");
                            // }
                            // }
                        }
                        // else
                        // { // 2015-07-06 확인용
                        // outLog(chkCnt + " qualf1 : " + (ret.substring(ret.length() - 4, ret.length() - 2)));
                        // outLog(chkCnt + " qualf2 : " + (ret.charAt(ret.length() - 2)));
                        // outLog(chkCnt + " fileArray4(char) : " + (char) byteArray[byteArray.length - 4]);
                        // outLog(chkCnt + " fileArray3(char) : " + (char) byteArray[byteArray.length - 3]);
                        // outLog(chkCnt + " fileArray2(char) : " + (char) byteArray[byteArray.length - 2]);
                        // outLog(chkCnt + " fileArray4 : " + byteArray[byteArray.length - 4]);
                        // outLog(chkCnt + " fileArray3 : " + byteArray[byteArray.length - 3]);
                        // outLog(chkCnt + " fileArray2 : " + byteArray[byteArray.length - 2]);
                        // }
                    }
                }
                else if (chkCnt >= 1 && m <= 10)
                {
                    outLog("** Buffer Read 10 Under chkCnt break : " + m + " / " + chkCnt + " **");
                    break;
                }
                else if (chkLoopCnt >= 3 && m <= 10)
                {
                    outLog("** Buffer Read 10 Under chkLoopCnt break : " + m + " / " + chkLoopCnt + " **");
                    break;
                }
                else
                {
                    chkLoopCnt++;
                }
                
                // 2015-08-25 종료구분자 인식 못할 시 길이 이용하여 루프 멈춤 조건 추가 (MS949, EUC-KR, BYTE검사 3개소 검사)
                if (nReceiveSize <= ret.getBytes("MS949").length - 10)
                {
                    outLog("** ReceiveSize MS949 Len Over break **");
                    break;
                }
                else if (nReceiveSize <= ret.length() - 10)
                {
                    outLog("** ReceiveSize Len Over break **");
                    break;
                }
                else if (nReceiveSize <= byteArray.length - 10)
                {
                    outLog("** ReceiveSize Byte Len Over break **");
                    break;
                }
                else
                {
                    // outLog("** chkCnt ** " + chkCnt);
                    // outLog("** nReceiveSize / MS949Len ** " + nReceiveSize + " / " + (ret.getBytes("MS949").length - 10));
                    // outLog("** nReceiveSize / Len ** " + nReceiveSize + " / " + (ret.length() - 10));
                    // outLog("** nReceiveSize / ByteLen ** " + nReceiveSize + " / " + (byteArray.length - 10));
                }
                
                if (chkLoopCnt > 15)
                {
                    outLog("** chkLoopCnt 15 Over break **");
                    break;
                }
            }
            
            // ret = bout.toString("EUC-KR"); //한글완성형에서 윗똠길 검색 안됨
            ret = bout.toString("MS949");
            outLog("** 결과(ExecCallGvia)  : " + ret);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            ret = e.toString();
        }
        catch (SocketTimeoutException ste)
        {
            ste.printStackTrace();
            ret = ste.toString();
        }
        catch (SocketException se)
        {
            se.printStackTrace();
            ret = se.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ret = e.toString();
        }
        finally
        {
            if (InStr != null) try
            {
                InStr.close();
            }
            catch (IOException e)
            {
                System.out.println("InputStream close :" + e);
            }
            if (bout != null) try
            {
                bout.close();
            }
            catch (IOException e)
            {
                System.out.println("ByteArrayOutputStream close :" + e);
            }
            if (OutStrWrit != null) try
            {
                OutStrWrit.close();
            }
            catch (IOException e)
            {
                System.out.println("OutputStreamWriter close :" + e);
            }
            if (wr != null) try
            {
                wr.close();
            }
            catch (IOException e)
            {
                System.out.println("BufferedWriter close :" + e);
            }
        }
        
        return ret;
    }
    
    /**
     * @Method : Address2
     * @Description : 엔진에서 나온 결과 배열화
     * @param strPost : 우편번호
     * @param strAddr : 주소
     * @param strEtcCd : 기타모드
     * @return outputColumns_Mult : 배치 처리 결과 배열
     *         0 :지번/새주소구분(입력값기준) ,1 :FLAG1,2 :FLAG2,
     *         정제 3 :우편번호,4 :우편번호일련번호,5 :기본주소,6 :상세주소
     *         전환 7 :우편번호,8 :우편번호일련번호,9 :기본주소,10:상세주소
     *         11:건물관리번호,
     *         12:시도,13:시군구,14:읍면동,15:리,
     *         16:산여부,
     *         17:주번지,18:부번지,
     *         19:X좌표,20:Y좌표,
     *         21:좌표부여레벨
     *         22:법정동코드,23:행정동코드
     *         24:괄호(참고) 주소,
     *         25:도로명 코드,
     *         26:읍/면/동 일련번호,
     *         27:지하 여부,
     *         28:건물 본번,29:건물 부번
     *         30:법정동명,31:행정동명,
     *         32:PNU코드,
     *         33:도로명,
     *         34:국가지점번호(지번),35:국가기초구역번호(지번),36:국가기초구역일련번호(지번),
     *         37:국가지점번호(도로),38:국가기초구역번호(도로),39:국가기초구역일련번호(도로),
     *         40:입력동구분,
     *         41:빌딩 매칭 정보,
     *         42:빌딩 동 정보,43:빌딩 호 정보,44:빌딩 층 정보,
     *         45:나머지(도로명),
     *         46:도로명건물명,47:도로명건물명상세,
     *         48:도로명주소PNU,
     *         49:안행부 배포-대표매칭여부,
     *         50:공동주택여부,
     *         51:부속건물명,52:부속정보명,
     *         53:금칙어관련,
     *         54:나머지(지번),
     *         55:기초구역번호부여레벨,56:주소파싱레벨,57:우편번호부여레벨,58:우편번호사용동타입,59:에러코드,60:정보변경코드,
     *         61:도로명-X좌표,62:도로명-Y좌표,
     *         63:멀티주소 Flag (0:일반,1:멀티)
     * 
     *         ioptMain 코드 값이 "C" 인 경우 영어주소 출력항목이 추가된다. [7개]
     *         64: 영문 시도명
     *         65: 영문 시군구명
     *         66: 영문 읍면동명
     *         67: 영문 법정리
     *         68: 영문 다량배달처
     *         69: 영문 도로명
     *         70: 영문 시군구건물명
     */
    public String[][] Address2(String txtType, String txtPost, String txtAddr1, String txtAddr2) throws Exception
    {
        String strEtcCd = "CVT"; // CVT : 정제, ENG : 정제 + 영어, BAT : 배치
        
        String ioptMain = ""; // 메인코드
        String ioptSub = ""; // 서브코드
        
        int multi_first_len = 0; // outputColumns_Mult 행 길이
        int multi_second_len = 0; // outputColumns_Mult 열 길이
        
        ioptMain = txtType.substring(0, 1);
        ioptSub = txtType.substring(2, 3);
        
        // //////// 열 길이 설정 start //////////
        if (ioptMain.equals("C"))
        {
            strEtcCd = "ENG";
            multi_second_len = 71;
        }
        else
        { // 3
            multi_second_len = 64;
            
            if (ioptSub.equals("x") || ioptSub.equals("y") || ioptSub.equals("z"))
            { // 배치용
                strEtcCd = "BAT";
            }
        }
        // ////////열 길이 설정 end //////////
        
        String[][] outputColumns_Mult = null;
        String[] outputColumns = new String[multi_second_len]; // 패킷에 따라 열 길이 틀려짐
        String[] errorOutputColumnsArr = new String[multi_second_len]; // 에러용
        errorOutputColumnsArr = setEmptyArray(errorOutputColumnsArr); // 배열 빈값 채우기(Null 없음)
        
        errorOutputColumnsArr[0] = "J";
        errorOutputColumnsArr[1] = "1";
        errorOutputColumnsArr[2] = "99";
        
        String tmpUMD = "";
        String tmpAddr = "";
        String tmpAddrEtc = "";
        String tmpAddrEtcNewaddr = "";
        String tmpStrNewaddrETC = "";
        
        String tmpPostcode = "";
        String tmpPostcodeSeq = "";
        String tmpAddr1 = "";
        String tmpAddr2 = "";
        String tmpAddr3 = "";
        
        String tmpTPostcode = "";
        String tmpTPostcodeSeq = "";
        String tmpTAddr1 = "";
        String tmpTAddr2 = "";
        String tmpTAddr3 = "";
        
        String tmpTDong = "";
        String tmpTRi = "";
        
        String tmpCX = "0";
        String tmpCY = "0";
        String tmpCL = "00";
        String tmpBdcode = "";
        String tmpBCode = "";
        String tmpHCode = "";
        String tmpRoadcode = "";
        String tmpRoadumdseq = "";
        String tmpRoadunder = "";
        String tmpRoadmn = "";
        String tmpRoadsn = "";
        String tmpRoadsubAddr = "";
        
        String tmpBdong = ""; // 30:법정동명
        String tmpHdong = ""; // 31:행정동명
        String tmpPnu = ""; // 32:PNU코드
        String tmpRoadname = ""; // 33:도로명
        String tmpNationPnum = ""; // 34:국가지점번호(지번)
        String tmpBsizonno = ""; // 35:국가기초구역번호(지번)
        String tmpBsizonnoSeq = ""; // 36:국가기초구역일련번호(지번)
        
        String tmpRoadNationPnum = ""; // 37:국가지점번호(도로)
        String tmpRoadBsizonno = ""; // 38:국가기초구역번호(도로)
        String tmpRoadBsizonnoSeq = ""; // 39:국가기초구역일련번호(도로)
        
        String tmpBuildinfo = ""; // 41:빌딩 매칭 정보
        String tmpBuilddong = ""; // 42:빌딩 동 정보
        String tmpBuildho = ""; // 43:빌딩 호 정보
        String tmpBuildstair = ""; // 44:빌딩 층 정보
        String tmpremain = ""; // 54: 나머지(지번)
        
        String tmpRoadBuild = ""; // 46:도로명건물
        String tmpRoadBuildDetail = ""; // 47:도로명건물상세
        String tmpRoadPNU = ""; // 48:도로명PNU
        
        String tmpRepreMatching = ""; // 49:안행부 배포-대표매칭여부
        String tmpApatHouse = ""; // 50: 공동주택여부
        
        String tmpAnnexBuild = ""; // 51: 부속건물
        String tmpAnnexInfo = ""; // 52: 부속정보
        
        String tmpProhibit = ""; // 53: 금칙어 관련
        
        String tmpRoremain = ""; // 45:나머지(도로명)
        
        String tmpNewPostLev = ""; // 55: 기초구역번호부여레벨
        String tmpAddrlev = ""; // 56: 주소파싱레벨 51
        String tmpPostLev = ""; // 57: 우편번호 부여레벨 52
        String tmpPostType = ""; // 58: 우편번호 사용 동탑입 53
        String tmpErrCd = ""; // 59: 에러코드 54
        String tmpDataChangeCd = ""; // 60: 정보변경코드 55
        
        String tmpSido_eng = ""; // 61: 영문 시도명
        String tmpSigungu_eng = ""; // 62: 영문 시군구명
        String tmpDong_eng = ""; // 63: 영문 읍면동명
        String tmpRi_eng = ""; // 64: 영문 법정리
        String tmpBuildnm_eng = ""; // 65: 영문 다량배달처
        String tmpRoadnm_eng = ""; // 66: 영문 도로명
        String tmpRoadBuildnm_eng = ""; // 67: 영문 시군구건물명
        
        String tmpRoadCX = "0"; // 68:도로명-X좌표
        String tmpRoadCY = "0"; // 69:도로명-Y좌표
        String tmpMultiFlag = "0"; // 70:멀티주소구분
        int nCharPos = 0;
        
        // if(strPost.equals("") && strAddr.length() < 4)
        // {
        // outputColumns_Mult = new String[1][56];
        // outputColumns_Mult[0] = errorOutputColumnsArr;
        // return outputColumns_Mult;
        // }
        
        String strResultTmp = ""; // 결과
        
        if (strEtcCd.equals("BAT"))
        { // batch의 경우 외부에서 결과 들어옴
            strResultTmp = txtAddr1;
        }
        else
        {
            // System.out.println("  ==> txtType  : " + txtType);
            // System.out.println("  ==> txtPost  : " + txtPost);
            // System.out.println("  ==> txtAddr1  : " + txtAddr1);
            // System.out.println("  ==> txtAddr2  : " + txtAddr2);
            String mkAddrSearch = MakeGviaInData(txtType, txtPost, txtAddr1, txtAddr2);
            // System.out.println("  ==> 입력1  : " + txtType);
            // System.out.println("  ==> 입력2  : " + mkAddrSearch);
            strResultTmp = ExecCallGvia(txtType, mkAddrSearch);
            // System.out.println("  ==> 결과  : " + strResultTmp);
        }
        
        strResultTmp = strResultTmp.trim();
        // strResultTmp = strResultTmp.replaceAll("__", "샵");
        // strResultTmp = strResultTmp.replaceAll("__", "숍");
        // strResultTmp = strResultTmp.replaceAll("__", "익");
        // strResultTmp = strResultTmp.replaceAll("__", "돔");
        
        if (strResultTmp.equals("") || strResultTmp == null)
        {
            outputColumns_Mult = new String[1][multi_second_len];
            outputColumns_Mult[0] = errorOutputColumnsArr;
            return outputColumns_Mult;
        }
        
        String[] addrSpl = strResultTmp.split(";", -1); // 20150807 수정 공백포함 자르기로 변경
        
        // System.out.println("  ==> 결과수량  : " + addrSpl.length);
        // 행 길이 설정 + 20150807 수정 공백포함 자르기로 변경에 따른 수정
        if (addrSpl.length == 1) multi_first_len = addrSpl.length;
        else if (addrSpl.length > 1) multi_first_len = addrSpl.length - 1;
        else multi_first_len = 0;
        
        // System.out.println("703_ioptMain : " + ioptMain);
        // System.out.println("704_ioptSub : " + ioptSub);
        // System.out.println("705_multi_first_len : " + multi_first_len);
        // System.out.println("706_multi_second_len : " + multi_second_len);
        
        // 멀티주소 체크 + // 20150807 수정 금칙어 관련 검사 추가
        if (multi_first_len > 1)
        {
            String[] prohibitChkColumn = addrSpl[multi_first_len - 1].split("[\u007C]");
            if (prohibitChkColumn[0].equals("금칙어"))
            {
                tmpProhibit = addrSpl[multi_first_len - 1];
                multi_first_len = multi_first_len - 1;
                if (multi_first_len > 1) tmpMultiFlag = "1";
            }
            else
            {
                tmpMultiFlag = "1";
            }
        }
        
        String[] addrColumns = addrSpl[0].split("[\u007C]"); // addrColumns[0]은 출력 프로토콜
        
        // 입력주소(정제) 멀티로 인하여 예외 처리 추가(A : 행정구역멀티, B : 건물 멀티)
        if (addrColumns[1].equals("A") || addrColumns[1].equals("B"))
        {
            multi_second_len = 15; // 열 길이 변경 처리
            String[][] outputColumns_EtcMultiResult = new String[multi_first_len][multi_second_len];
            outputColumns_EtcMultiResult = setEmptyArray(outputColumns_EtcMultiResult); // 배열 빈값 채우기(Null 없음)
            
            for (int arr_i = 0; arr_i < multi_first_len; arr_i++)
            {
                if (arr_i == 0)
                {
                    if (addrColumns.length == 4)
                    {
                        outputColumns_EtcMultiResult[0][0] = addrSpl[0]; // 앞단 정보 전문
                        outputColumns_EtcMultiResult[0][1] = addrColumns[0]; // 출력 프로토콜
                        outputColumns_EtcMultiResult[0][2] = addrColumns[1]; // 결과 코드
                        outputColumns_EtcMultiResult[0][3] = addrColumns[2]; // 입력주소 동정보 코드
                        outputColumns_EtcMultiResult[0][4] = addrColumns[3]; // 멀티주소 매칭 수
                        outputColumns_EtcMultiResult[0][5] = ""; // 나머지주소
                    }
                    else
                    {
                        outputColumns_EtcMultiResult[0][0] = addrSpl[0]; // 앞단 정보 전문
                        outputColumns_EtcMultiResult[0][1] = addrColumns[0]; // 출력 프로토콜
                        outputColumns_EtcMultiResult[0][2] = addrColumns[1]; // 결과 코드
                        outputColumns_EtcMultiResult[0][3] = addrColumns[2]; // 입력주소 동정보 코드
                        outputColumns_EtcMultiResult[0][4] = addrColumns[3]; // 멀티주소 매칭 수
                        outputColumns_EtcMultiResult[0][5] = addrColumns[4]; // 나머지주소
                    }
                }
                else
                {
                    String[] outputColumns_EtcMultiResultSplit = addrSpl[arr_i].split("[\u007C]");
                    
                    for (int arr_j = 0; arr_j < outputColumns_EtcMultiResultSplit.length; arr_j++)
                    {
                        outputColumns_EtcMultiResult[arr_i][arr_j] = outputColumns_EtcMultiResultSplit[arr_j];
                    }
                }
            }
            
            return outputColumns_EtcMultiResult;
        }
        
        // System.out.println("  ==> Main 결과  : " + addrSpl[0]);
        // System.out.println("  ==> Main 수량  : " + addrColumns.length);
        // System.out.println("  ==> addrColumns[1]  : " + addrColumns[1]);
        String strGubun = addrColumns[1].substring(1, 2); // 0000000K000300000356|112H0708050000000000000000010000000|서울특별시|강남구|
        // System.out.println("  ==> strGubun  : " + strGubun);
        String tmpInputDongL = addrColumns[1].substring(3, 4); // 입력동구분
        outputColumns[2] = addrColumns[1].substring(1, 3); // 주소 변환 결과 코드
        
        // 입력주소 타입 : 지번,도로명주소 구분
        if (tmpInputDongL.equals("S"))
        {
            outputColumns[0] = "S"; // 사서함주소
        }
        else if (strGubun.equals("5") || strGubun.equals("6") || strGubun.equals("7") || strGubun.equals("8"))
        {
            outputColumns[0] = "R"; // 도로명주소
        }
        else
        {
            outputColumns[0] = "J"; // 지번주소
        }
        
        // Flag : 플래그1 (0:정상, 1:불능, 2:매칭실패, 3:사서함)
        if (strGubun.equals("9") || strGubun.equals("7") || strGubun.equals("4"))
        {
            if (outputColumns[2].equals("45") && tmpInputDongL.equals("S")) outputColumns[1] = "3"; // 사서함
            else outputColumns[1] = "1"; // 불능
        }
        else if (strGubun.equals("1") || strGubun.equals("5") || strGubun.equals("2") || strGubun.equals("6"))
        {
            outputColumns[1] = "0"; // 정상(정제,전환 성공)
        }
        else
        {
            outputColumns[1] = "2"; // 매칭실패(정제성공, 전환불가 - 지번 없음 혹은 도로명 없음)
        }
        
        tmpAnnexBuild = addrColumns[46]; // 51: 부속건물
        tmpAnnexInfo = addrColumns[47]; // 52: 부속정보
        
        if (strEtcCd.equals("ENG"))
        {
            tmpSido_eng = addrColumns[48]; // 시도 영문
            tmpSigungu_eng = addrColumns[49];
            tmpDong_eng = addrColumns[50];
            tmpRi_eng = addrColumns[51];
            tmpBuildnm_eng = addrColumns[52];
            tmpRoadnm_eng = addrColumns[53];
            tmpRoadBuildnm_eng = addrColumns[54]; // 시군구건물명 영문
        }
        
        tmpAddrlev = addrColumns[1].substring(4, 6); // 주소파싱레벨
        tmpPostLev = addrColumns[1].substring(8, 10); // 우편번호부여레벨
        tmpPostType = addrColumns[1].substring(10, 11); // 우편번호사용동타입
        tmpErrCd = addrColumns[1].substring(11, 13); // 에러코드
        tmpDataChangeCd = addrColumns[1].substring(13, 14); // 정보변경코드
        tmpNewPostLev = addrColumns[1].substring(30, 32); // 기초구역번호부여레벨
        
        if (!addrColumns[24].trim().equals(""))
        {
            nCharPos = addrColumns[24].indexOf("()");
            if (nCharPos > 0)
            {
                if (nCharPos < (addrColumns[24].length() - 2))
                {
                    addrColumns[24] = addrColumns[24].substring(0, nCharPos) + addrColumns[24].substring(nCharPos + 2, addrColumns[24].length());
                }
                else
                {
                    addrColumns[24] = addrColumns[24].substring(0, nCharPos);
                }
            }
            nCharPos = addrColumns[24].indexOf("(,");
            if (nCharPos > 0)
            {
                if (nCharPos < (addrColumns[24].length() - 2))
                {
                    addrColumns[24] = addrColumns[24].substring(0, nCharPos + 1) + addrColumns[24].substring(nCharPos + 2, addrColumns[24].length());
                }
                else
                {
                    addrColumns[24] = addrColumns[24].substring(0, nCharPos);
                }
            }
            nCharPos = addrColumns[24].indexOf(",)");
            if (nCharPos > 0)
            {
                addrColumns[24] = addrColumns[24].substring(0, nCharPos) + addrColumns[24].substring(nCharPos + 1, addrColumns[24].length());
            }
            addrColumns[24] = addrColumns[24].replaceAll("  ", " ");
            addrColumns[24] = addrColumns[24].trim();
        }
        
        // ***********정제주소
        // (입력이 지번주소이고 지번 있음) or (입력이 새주소이고 지번 있고) == 지번주소 만들 수 있음
        // ==> 정제 주소 : (입력이 지번주소이고 지번 있음) or (입력이 새주소이고 새주소 결과가 있음)
        // System.out.println("  ==>    1  : " + outputColumns[0].trim());
        // System.out.println("  ==>    2  : " + addrColumns[2].trim());
        // if (!outputColumns[0].trim().equals("R") && !addrColumns[2].trim().equals(""))
        if (outputColumns[0].equals("J") && !addrColumns[2].trim().equals(""))
        { // YCK 2013.04.10
          // 구주소1
            if (addrColumns[14].length() == 6) // 2014-01-06 PCA 요청
            {
                tmpPostcode = addrColumns[14]; // 신규 우편번호
            }
            else if (addrColumns[34].length() == 6)
            {
                tmpPostcode = addrColumns[34]; //
            }
            
            if (addrColumns[15].length() == 3) // 2014-01-06 PCA 요청
            {
                tmpPostcodeSeq = addrColumns[15];
            }
            else if (addrColumns[35].length() == 3)
            {
                tmpPostcodeSeq = addrColumns[35];
            }
            
            tmpAddr1 = addrColumns[2] + " " + addrColumns[3]; // 시도 + 시군구
            // 2013.02.14 법정동으로 표현 변경
            // tmpAddr1 = tmpAddr1 + " " + addrColumns[4] + " " + addrColumns[5]; // + 읍면동 + 리
            if (!addrColumns[22].trim().equals("") && addrColumns[5].trim().equals("")) // 법정동명이 있고 리명칭이 없을 경우
            {
                tmpAddr1 = tmpAddr1 + " " + addrColumns[22].trim(); // + 법정동
                // System.out.println("  ==> tmpAddr1  : " + tmpAddr1);
            }
            else
            {
                tmpAddr1 = tmpAddr1 + " " + addrColumns[4] + " " + addrColumns[5]; // + 읍면동 + 리
            }
            // 2013.02.14 법정동으로 표현 변경
            tmpAddr1 = tmpAddr1.replaceAll("  ", " ");
            tmpAddr1 = tmpAddr1.trim(); // 끝에 리가 없는 경우 공백만 남어서
            // 구주소1
            
            // 구주소2
            if (!addrColumns[7].trim().equals(""))
            { // 주번지
                if (addrColumns[6].trim().equals("1"))
                {
                    tmpAddr2 = "산";
                }
                else
                {
                    tmpAddr2 = "";
                }
                
                if (!addrColumns[8].trim().equals(""))
                { // 부번지
                    tmpAddr2 = tmpAddr2 + addrColumns[7] + "-" + addrColumns[8];
                }
                else
                {
                    tmpAddr2 = tmpAddr2 + addrColumns[7];
                }
                tmpAddr2 = tmpAddr2 + " ";
            }
            
            tmpAddr2 = tmpAddr2 + addrColumns[24]; // 나머지주소 : 기존주소 이외의 주소
            tmpAddr2 = tmpAddr2.replaceAll("  ", " ");
            tmpAddr2 = tmpAddr2.replaceAll(",  ,", ", ");
            tmpAddr2 = tmpAddr2.replaceAll("()", "");
            tmpAddr2 = tmpAddr2.trim();
            // 구주소2
        }
        else
        {
            tmpRoadsubAddr = ""; // 2013.02.22
            
            // 새주소1
            if (addrColumns[34].length() == 6) // 2014-01-06 PCA 요청
            {
                tmpPostcode = addrColumns[34]; //
            }
            else if (addrColumns[14].length() == 6)
            {
                tmpPostcode = addrColumns[14]; //
            }
            
            if (addrColumns[35].length() == 3) // 2014-01-06 PCA 요청
            {
                tmpPostcodeSeq = addrColumns[35]; //
            }
            else if (addrColumns[15].length() == 3)
            {
                tmpPostcodeSeq = addrColumns[15]; //
            }
            
            tmpAddr1 = addrColumns[2] + " " + addrColumns[3];
            
            if (addrColumns[4].length() > 2)
            {
                tmpUMD = addrColumns[4].substring(addrColumns[4].length() - 1, addrColumns[4].length());
                if (tmpUMD.equals("읍") || tmpUMD.equals("면"))
                {
                    tmpAddr1 = tmpAddr1 + " " + addrColumns[4];
                }
            }
            
            tmpAddr1 = tmpAddr1 + " " + addrColumns[25];
            
            if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
            {
                if (addrColumns[28].trim().equals("1"))
                {
                    tmpAddr1 = tmpAddr1 + " 지하";
                }
                else if (addrColumns[28].trim().equals("2"))
                {
                    tmpAddr1 = tmpAddr1 + " 공중";
                }
                else
                {
                    tmpAddr1 = tmpAddr1 + " ";
                }
                tmpAddr1 = tmpAddr1 + addrColumns[29];
                
                if (!addrColumns[30].trim().equals("") && !addrColumns[30].trim().equals("0"))
                {
                    tmpAddr1 = tmpAddr1 + "-" + addrColumns[30];
                }
            }
            tmpAddr1 = tmpAddr1.replaceAll("  ", " ");
            tmpAddr1 = tmpAddr1.trim();
            // 새주소1
            
            // 새주소2=====================================================
            tmpAddrEtcNewaddr = "";
            if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
            {
                if (!addrColumns[38].trim().equals(""))
                {
                    tmpAddrEtcNewaddr = addrColumns[38];
                    tmpAddrEtcNewaddr = tmpAddrEtcNewaddr.trim();
                    
                    tmpAddr2 = tmpAddrEtcNewaddr;
                }
            }
            
            tmpAddr2 = tmpAddr2.replaceAll(" 번지", "번지");
            tmpAddr2 = tmpAddr2.replaceAll("  ", " ");
            tmpAddr2 = tmpAddr2.replaceAll("()", "");
            tmpAddr2 = tmpAddr2.trim();
            // 새주소2
            
            // 괄호(참고) 주소
            tmpAddr3 = "";
            if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0")) // 건물주번호
            {
                if (addrColumns[5].trim().equals("")) // 리 명칭이 없다면
                {
                    tmpAddr3 = addrColumns[22].trim();
                    
                    if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                    {
                        tmpAddr3 = tmpAddr3 + ", " + addrColumns[9].trim();
                    }
                    else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                    {
                        tmpAddr3 = tmpAddr3 + ", " + addrColumns[17].trim();
                    }
                    else if (!addrColumns[31].trim().equals("")) // 도로명빌딩정보
                    {
                        tmpAddr3 = tmpAddr3 + ", " + addrColumns[31].trim();
                    }
                    
                    /*
                     * 대전시청용
                     * if(!addrColumns[31].trim().equals("")) //도로명빌딩정보
                     * {
                     * tmpAddr3 = tmpAddr3 + ", " + addrColumns[31].trim();
                     * }
                     */
                }
                else
                {
                    if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                    {
                        tmpAddr3 = addrColumns[9].trim();
                    }
                    else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                    {
                        tmpAddr3 = addrColumns[17].trim();
                    }
                    else if (!addrColumns[31].trim().equals("")) // 도로명빌딩정보
                    {
                        tmpAddr3 = addrColumns[31].trim();
                    }
                    /*
                     * if(!addrColumns[31].trim().equals("")) //도로명빌딩정보
                     * {
                     * tmpAddr3 = addrColumns[31].trim();
                     * }
                     */
                }
            }
            
            if (!tmpAddr3.trim().equals(""))
            {
                // tmpAddr2 = tmpAddr2 + " (" + tmpAddr3 + ")";
                tmpRoadsubAddr = tmpAddr3;
            }
            // 괄호(참고) 주소
        }
        
        // System.out.println("tmpRoadsubAddr : " + tmpRoadsubAddr);
        outputColumns[3] = tmpPostcode;
        outputColumns[4] = tmpPostcodeSeq;
        outputColumns[5] = tmpAddr1;
        outputColumns[6] = tmpAddr2;
        // ***********정제주소
        // System.out.println("tmpAddr1 : " + tmpAddr1);
        
        // ***********변환주소
        outputColumns_Mult = new String[multi_first_len][multi_second_len];
        
        for (int i = 1; i <= multi_first_len; i++)
        {
            String[] outputColumns2 = new String[multi_second_len];
            
            outputColumns2[0] = outputColumns[0];
            outputColumns2[1] = outputColumns[1];
            outputColumns2[2] = outputColumns[2];
            outputColumns2[3] = outputColumns[3];
            outputColumns2[4] = outputColumns[4];
            outputColumns2[5] = outputColumns[5];
            outputColumns2[6] = outputColumns[6];
            
            tmpTPostcode = "";
            tmpTPostcodeSeq = "";
            tmpTAddr1 = "";
            tmpTAddr2 = "";
            tmpTAddr3 = "";
            tmpTDong = "";
            tmpTRi = "";
            tmpCX = "0";
            tmpCY = "0";
            tmpCL = "00";
            tmpBCode = "";
            tmpHCode = "";
            tmpBdcode = "";
            tmpRoadcode = "";
            tmpRoadumdseq = "";
            tmpRoadunder = "";
            tmpRoadmn = "";
            tmpRoadsn = "";
            // tmpRoadsubAddr = ""; //2013.02.22
            tmpBdong = "";
            tmpHdong = "";
            tmpPnu = "";
            tmpRoadname = "";
            tmpNationPnum = "";
            tmpBsizonno = "";
            tmpBsizonnoSeq = "";
            
            tmpRoadNationPnum = "";
            tmpRoadBsizonno = "";
            tmpRoadBsizonnoSeq = "";
            
            tmpBuildinfo = "";
            tmpBuilddong = "";
            tmpBuildho = "";
            tmpBuildstair = "";
            tmpremain = "";
            tmpRoremain = "";
            
            tmpRoadBuild = "";
            tmpRoadBuildDetail = "";
            tmpRoadPNU = "";
            
            tmpRepreMatching = "";
            tmpApatHouse = "";
            
            tmpRoadCX = "0";
            tmpRoadCY = "0";
            
            // System.out.println("outputColumns[0] : " + outputColumns[0]);
            if (i == 1)
            {
                if ((outputColumns[0].trim().equals("J") && !addrColumns[29].trim().equals("")) || (outputColumns[0].trim().equals("R") && !addrColumns[7].trim().equals("") && !addrColumns[4].trim().equals("")))
                {
                    if (outputColumns[0].trim().equals("R") && !addrColumns[7].trim().equals("") && !addrColumns[4].trim().equals(""))
                    {
                        // 구주소1
                        
                        // tmpTPostcode = addrColumns[14]; //신규 우편번호
                        if (addrColumns[14].length() == 6) // 2014-01-06 PCA 요청
                        {
                            tmpTPostcode = addrColumns[14]; // 신규 우편번호
                        }
                        else if (addrColumns[34].length() == 6)
                        {
                            tmpTPostcode = addrColumns[34]; //
                        }
                        
                        if (addrColumns[15].length() == 3) // 2014-01-06 PCA 요청
                        {
                            tmpTPostcodeSeq = addrColumns[15];
                        }
                        else if (addrColumns[35].length() == 3)
                        {
                            tmpTPostcodeSeq = addrColumns[35];
                        }
                        
                        tmpTAddr1 = addrColumns[2] + " " + addrColumns[3]; // 시도 + 시군구
                        // 2013.02.14 법정동으로 표현 변경
                        // tmpTAddr1 = tmpTAddr1 + " " + addrColumns[4] + " " + addrColumns[5]; // + 읍면동 + 리
                        if (!addrColumns[22].trim().equals("") && addrColumns[5].trim().equals("")) // 법정동명이 있고 리명칭이 없을 경우
                        {
                            tmpTAddr1 = tmpTAddr1 + " " + addrColumns[22].trim(); // + 법정동
                            // System.out.println("  ==> tmpTAddr1(2)  : " + tmpTAddr1);
                        }
                        else
                        {
                            tmpTAddr1 = tmpTAddr1 + " " + addrColumns[4] + " " + addrColumns[5]; // + 읍면동 + 리
                        }
                        // 2013.02.14 법정동으로 표현 변경
                        tmpTAddr1 = tmpTAddr1.replaceAll("  ", " ");
                        tmpTAddr1 = tmpTAddr1.trim();
                        // 구주소1
                        
                        // 구주소2
                        if (!addrColumns[7].trim().equals(""))
                        { // 주번지
                            if (addrColumns[6].trim().equals("1"))
                            {
                                tmpTAddr2 = "산";
                            }
                            else
                            {
                                tmpTAddr2 = "";
                            }
                            
                            if (!addrColumns[8].trim().equals(""))
                            { // 부번지
                                tmpTAddr2 = tmpTAddr2 + addrColumns[7] + "-" + addrColumns[8];
                            }
                            else
                            {
                                tmpTAddr2 = tmpTAddr2 + addrColumns[7];
                            }
                            tmpTAddr2 = tmpTAddr2 + " ";
                        }
                        tmpTAddr2 = tmpTAddr2 + addrColumns[24].replaceAll(" ()", " "); // 나머지주소 : 기존주소 이외의 주소
                        tmpAddrEtc = addrColumns[24].replaceAll(" ()", " ");
                        tmpAddrEtc = addrColumns[24].replaceAll("()", "");
                        tmpTAddr2.trim();
                        // 구주소2
                        
                        tmpTAddr2 = tmpTAddr2.replaceAll(" 번지", "번지");
                        tmpTAddr2 = tmpTAddr2.replaceAll("  ", " ");
                        tmpTAddr2 = tmpTAddr2.replaceAll("()", "");
                        tmpTAddr2 = tmpTAddr2.trim();
                    }
                    else
                    {
                        tmpRoadsubAddr = ""; // 2013.02.22
                        // 새주소1
                        
                        if (addrColumns[34].length() == 6) // 2014-01-06 PCA 요청
                        {
                            tmpTPostcode = addrColumns[34]; //
                        }
                        else if (addrColumns[14].length() == 6)
                        {
                            tmpTPostcode = addrColumns[14]; //
                        }
                        
                        if (addrColumns[35].length() == 3) // 2014-01-06 PCA 요청
                        {
                            tmpTPostcodeSeq = addrColumns[35]; //
                        }
                        else if (addrColumns[15].length() == 3)
                        {
                            tmpTPostcodeSeq = addrColumns[15]; //
                        }
                        
                        tmpTAddr1 = addrColumns[2] + " " + addrColumns[3];
                        
                        if (addrColumns[4].length() > 2)
                        {
                            tmpUMD = addrColumns[4].substring(addrColumns[4].length() - 1, addrColumns[4].length());
                            if (tmpUMD.equals("읍") || tmpUMD.equals("면"))
                            {
                                tmpTAddr1 = tmpTAddr1 + " " + addrColumns[4];
                            }
                        }
                        
                        tmpTAddr1 = tmpTAddr1 + " " + addrColumns[25];
                        
                        if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
                        {
                            if (addrColumns[28].trim().equals("1"))
                            {
                                tmpTAddr1 = tmpTAddr1 + " 지하";
                            }
                            else if (addrColumns[28].trim().equals("2"))
                            {
                                tmpTAddr1 = tmpTAddr1 + " 공중";
                            }
                            else
                            {
                                tmpTAddr1 = tmpTAddr1 + " ";
                            }
                            tmpTAddr1 = tmpTAddr1 + addrColumns[29];
                            
                            if (!addrColumns[30].trim().equals("") && !addrColumns[30].trim().equals("0"))
                            {
                                tmpTAddr1 = tmpTAddr1 + "-" + addrColumns[30];
                            }
                        }
                        
                        tmpTAddr1 = tmpTAddr1.replaceAll("  ", " ");
                        tmpTAddr1 = tmpTAddr1.trim();
                        // 새주소1
                        
                        // 새주소2
                        tmpAddrEtcNewaddr = "";
                        if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
                        {
                            if (!addrColumns[38].trim().equals(""))
                            {
                                tmpAddrEtcNewaddr = addrColumns[38];
                                tmpAddrEtcNewaddr = tmpAddrEtcNewaddr.trim();
                                
                                tmpTAddr2 = tmpAddrEtcNewaddr;
                            }
                        }
                        
                        // System.out.println("  ==> * tmpTAddr2  : " + tmpTAddr2);
                        
                        tmpTAddr2 = tmpTAddr2.replaceAll(" 번지", "번지");
                        tmpTAddr2 = tmpTAddr2.replaceAll("  ", " ");
                        tmpTAddr2 = tmpTAddr2.replaceAll("()", "");
                        tmpTAddr2 = tmpTAddr2.trim();
                        // 새주소2
                        
                        // 괄호(참고) 주소
                        tmpTAddr3 = "";
                        if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0")) // 건물주번호
                        {
                            if (addrColumns[5].trim().equals("")) // 리 명칭이 없다면
                            {
                                tmpTAddr3 = addrColumns[22].trim();
                                
                                if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                                {
                                    tmpTAddr3 = tmpTAddr3 + ", " + addrColumns[9].trim();
                                }
                                else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                                {
                                    tmpTAddr3 = tmpTAddr3 + ", " + addrColumns[17].trim();
                                }
                                else if (!addrColumns[31].trim().equals("")) // 도로명빌딩정보
                                {
                                    tmpTAddr3 = tmpTAddr3 + ", " + addrColumns[31].trim();
                                }
                                /*
                                 * if(!addrColumns[31].trim().equals("")) //도로명빌딩정보
                                 * {
                                 * tmpTAddr3 = tmpTAddr3 + ", " + addrColumns[31].trim();
                                 * }
                                 */
                            }
                            else
                            {
                                
                                if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                                {
                                    tmpTAddr3 = addrColumns[9].trim();
                                }
                                else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                                {
                                    tmpTAddr3 = addrColumns[17].trim();
                                }
                                else if (!addrColumns[31].trim().equals("")) // 도로명빌딩정보
                                {
                                    tmpTAddr3 = addrColumns[31].trim();
                                }
                                /*
                                 * if(!addrColumns[31].trim().equals("")) //도로명빌딩정보
                                 * {
                                 * tmpTAddr3 = addrColumns[31].trim();
                                 * }
                                 */
                            }
                        }
                        
                        if (!tmpTAddr3.trim().equals(""))
                        {
                            // tmpTAddr2 = tmpTAddr2 + " (" + tmpTAddr3 + ")";
                            tmpRoadsubAddr = tmpTAddr3;
                        }
                        // 괄호(참고) 주소
                        
                    }
                }
                tmpTDong = addrColumns[4];
                tmpTRi = addrColumns[5];
                
                tmpCX = addrColumns[18].trim();
                tmpCY = addrColumns[19].trim();
                tmpCL = addrColumns[1].substring(6, 8);
                tmpRoadCX = addrColumns[36].trim();
                tmpRoadCY = addrColumns[37].trim();
                
                /*
                 * K사 - 필요시 적용
                 * if( (tmpCL.equals("01") || tmpCL.equals("02") || tmpCL.equals("03") || tmpCL.equals("04") ) && (tmpRoadCX.equals("") || tmpRoadCX.equals("0")) ) tmpRoadCX = tmpCX;
                 * if( (tmpCL.equals("01") || tmpCL.equals("02") || tmpCL.equals("03") || tmpCL.equals("04") ) && (tmpRoadCY.equals("") || tmpRoadCY.equals("0")) ) tmpRoadCY = tmpCY;
                 */
                
                // 2014-10-12 도로명과 지번좌표를 따로 분류 - 도로 좌표가 있고 단건인 경우 도로명으로, 아니면 지번으로 : addrSpl.length == 2
                if (multi_first_len == 1 && !addrColumns[36].trim().equals("") && !addrColumns[37].trim().equals("0"))
                {
                    tmpRoadCX = addrColumns[36].trim();
                    tmpRoadCY = addrColumns[37].trim();
                    // tmpCL = "19";
                }
                
                // System.out.println("  ==> * XY  : " + addrColumns[1].substring(6,8) + ", "+addrColumns[18] + " | "+addrColumns[19] + ", "+addrColumns[36]+ " | "+addrColumns[37]);
                tmpBCode = addrColumns[23].trim();
                tmpHCode = addrColumns[21].trim();
                
                tmpBdong = addrColumns[22].trim();
                tmpHdong = addrColumns[20].trim();
                tmpPnu = addrColumns[23].trim() + addrColumns[6].trim() + lpad(addrColumns[7].trim(), 4, "0") + lpad(addrColumns[8].trim(), 4, "0");
                tmpRoadname = addrColumns[25].trim();
                tmpNationPnum = "가나00000000";
                tmpBsizonno = addrColumns[40].trim();
                tmpBsizonnoSeq = addrColumns[41].trim();
                
                tmpRoadNationPnum = "가나00000000";
                tmpRoadBsizonno = addrColumns[42].trim();
                tmpRoadBsizonnoSeq = addrColumns[43].trim();
                
                tmpRepreMatching = addrColumns[44].trim();
                tmpApatHouse = addrColumns[45].trim();
                
                // 법정동으로 표현 변경 s
                outputColumns2[14] = addrColumns[4]; // 읍면동
                if (UserDongFirst == 0)
                {
                    // if (!addrColumns[22].trim().equals("") && addrColumns[5].trim().equals("")) // 2013.02.14 법정동명이 있고 리명칭이 없을 경우
                    if (!addrColumns[22].trim().equals("") && addrColumns[4].trim().matches(".*[읍]$|.*[면]$")) // 20151124 법정동명이 있고 읍면인 경우
                    {
                        // outputColumns2[14] = addrColumns[22].trim(); // 2013.02.14 법정동
                        outputColumns2[14] = addrColumns[4]; // 읍면동
                    }
                    else
                    {
                        // outputColumns2[14] = addrColumns[4]; // 2013.02.14 읍면동
                        outputColumns2[14] = addrColumns[22].trim(); // 법정동
                    }
                }
                // 법정동으로 표현 변경 e
                outputColumns2[15] = addrColumns[5];
                if (addrColumns[6].length() > 0 && addrColumns[6].equals("1"))
                {
                    outputColumns2[16] = "1";
                }
                else
                // 2015.03.10 값에 Null이 들어가므로 0부여
                {
                    outputColumns2[16] = "0";
                }
                
                if (!addrColumns[7].trim().equals(""))
                {
                    outputColumns2[17] = "";
                    if (addrColumns[6].trim().equals("1"))
                    {
                        outputColumns2[17] = "산";
                    }
                    // outputColumns2[17] = outputColumns2[17] + addrColumns[7];
                    outputColumns2[17] = addrColumns[7];
                }
                else
                // 2015.03.10 값에 Null이 들어가므로 ""부여
                {
                    outputColumns2[17] = "";
                }
                
                if (addrColumns[7] == null || addrColumns[7].equals("")) // 2015.03.10 주번지 없을 시 부번지도 없음 처리
                {
                    outputColumns2[18] = "";
                }
                else
                // 2015.03.10 주번지 존재 시 부번지 없는 경우 0 처리
                {
                    if (addrColumns[8].equals(""))
                    {
                        outputColumns2[18] = "0";
                    }
                    else
                    {
                        outputColumns2[18] = addrColumns[8];
                    }
                }
                
                tmpBdcode = addrColumns[33];
                tmpRoadcode = addrColumns[26];
                tmpRoadmn = addrColumns[29];
                if (tmpRoadmn == null || tmpRoadmn.equals(""))
                { // 2015.03.10 건물본번 없을 시 부번도 없음 처리
                    tmpRoadsn = "";
                }
                else
                { // 2015.03.10 건물본번 존재 시 부번이 없는 경우 0 처리
                    tmpRoadsn = addrColumns[30];
                    if (tmpRoadsn.equals(""))
                    {
                        tmpRoadsn = "0";
                    }
                }
                tmpRoadumdseq = addrColumns[27];
                tmpRoadunder = addrColumns[28];
                
                tmpBuildinfo = addrColumns[9].trim().equals("") ? addrColumns[17].trim() : addrColumns[9].trim(); // 건물매칭정보
                tmpRoadBuild = addrColumns[31];
                tmpRoadBuildDetail = addrColumns[32];
                tmpRoadPNU = addrColumns[26]; // 도로명코드(12)+읍면동일련번호(2)+지하여부(1)+건물본번(5) + 건물부번(5)
                
                if (!addrColumns[26].trim().equals("")) // jsy 도로명코드가 없는경우의 처리
                {
                    if (!addrColumns[27].trim().equals(""))
                    {
                        tmpRoadPNU = tmpRoadPNU + addrColumns[27];
                        
                        if (!addrColumns[28].trim().equals(""))
                        {
                            tmpRoadPNU = tmpRoadPNU + addrColumns[28];
                        }
                        else
                        {
                            tmpRoadPNU = tmpRoadPNU + "0";
                        }
                        
                        if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
                        {
                            tmpRoadPNU = tmpRoadPNU + lpad(addrColumns[29], 5, "0");
                            
                            if (!addrColumns[30].trim().equals("") && !addrColumns[30].trim().equals("0"))
                            {
                                tmpRoadPNU = tmpRoadPNU + lpad(addrColumns[30], 5, "0");
                            }
                            else
                            {
                                tmpRoadPNU = tmpRoadPNU + "00000";
                            }
                        }
                        else
                        {
                            tmpRoadPNU = tmpRoadPNU + "0000000000";
                        }
                    }
                    else
                    {
                        tmpRoadPNU = tmpRoadPNU + "0000000000000";
                    }
                }
                else
                {
                    tmpRoadPNU = "";
                }
            }
            else
            { // 처음 이후 반복 부분
              // System.out.println("  ==> * addrSpl[i-1]  : " + addrSpl[i-1]);
                String[] outputColumnsSub = addrSpl[i - 1].split("[\u007C]", -1);
                // String[] outputColumnsSub = "인수봉로|113053108005|01|0|18||벽산라이브파크아파트||1130510100113540000000001|142776|002|313063|558148|삼양동|1130553400|미아동|1130510100|||Y|Y|||".split("[\u007C]");
                // 인수봉로|113053108005|01|0|18||벽산라이브파크아파트||1130510100113540000000001|142776|002|313063|558148|삼양동|1130553400|미아동|1130510100|||Y|Y|||
                
                tmpAddr = addrColumns[2] + " " + addrColumns[3]; // 시도 + 시군구
                
                if (tmpInputDongL.equals("N") || tmpInputDongL.equals("U")) // 지번주소 출력
                {
                    // System.out.println("  ==> 새주소입력 * outputColumnsSub[14]  : " + outputColumnsSub[14]);
                    if (outputColumnsSub.length > 0 && !outputColumnsSub[12].trim().equals(""))
                    {
                        if (outputColumnsSub[12].length() == 6) // 2014-01-06 PCA 요청
                        {
                            tmpTPostcode = outputColumnsSub[12];
                            tmpTPostcodeSeq = outputColumnsSub[13];
                        }
                        else if (addrColumns[14].length() == 6)
                        {
                            tmpTPostcode = addrColumns[14];
                            tmpTPostcodeSeq = addrColumns[15];
                        }
                        else if (addrColumns[34].length() == 6)
                        {
                            tmpTPostcode = addrColumns[34];
                            tmpTPostcodeSeq = addrColumns[35];
                        }
                    }
                    
                    tmpTAddr1 = tmpAddr;
                    
                    if (outputColumnsSub.length > 1 && !outputColumnsSub[0].trim().equals(""))
                    {
                        // 2013.02.14 법정동으로 표현 변경
                        // tmpTAddr1 = tmpTAddr1 + " " + outputColumnsSub[0]; //읍면동
                        if (!outputColumnsSub[10].trim().equals("") && outputColumnsSub[1].trim().equals("")) // 법정동명이 있고 리명칭이 없을 경우
                        {
                            tmpTAddr1 = tmpTAddr1 + " " + outputColumnsSub[10].trim(); // 법정동
                            // System.out.println("  ==> tmpTAddr1(3)  : " + tmpTAddr1);
                        }
                        else
                        {
                            tmpTAddr1 = tmpTAddr1 + " " + outputColumnsSub[0]; // 읍면동
                        }
                        // 2013.02.14 법정동으로 표현 변경
                        
                        if (outputColumnsSub.length > 1 && !outputColumnsSub[1].trim().equals(""))
                        {
                            tmpTAddr1 = tmpTAddr1 + " " + outputColumnsSub[1]; // 리
                        }
                    }
                    tmpTAddr1 = tmpTAddr1.replaceAll("  ", " ");
                    
                    if (outputColumnsSub.length > 2 && !outputColumnsSub[2].trim().equals(""))
                    {
                        if (outputColumnsSub[2].trim().equals("1"))
                        {
                            tmpTAddr2 = "산";
                        }
                        else
                        {
                            tmpTAddr2 = "";
                        }
                        
                        tmpTAddr2 = tmpTAddr2 + outputColumnsSub[3];
                        
                        if (outputColumnsSub.length > 4 && !outputColumnsSub[4].trim().equals(""))
                        {
                            tmpTAddr2 = tmpTAddr2 + "-" + outputColumnsSub[4];
                        }
                    }
                    // System.out.println("  ==> * =============  : " + tmpAddrEtc);
                    if (!tmpAddrEtc.equals(""))
                    {
                        tmpTAddr2 = tmpTAddr2 + " " + tmpAddrEtc;
                    }
                    
                    tmpBCode = outputColumnsSub[11].trim();
                    tmpHCode = outputColumnsSub[9].trim();
                    
                    tmpBdong = outputColumnsSub[10].trim();
                    tmpHdong = outputColumnsSub[8].trim();
                    tmpPnu = outputColumnsSub[11].trim() + outputColumnsSub[2].trim() + lpad(outputColumnsSub[3].trim(), 4, "0") + lpad(outputColumnsSub[4].trim(), 4, "0");
                    tmpRoadname = addrColumns[25].trim();
                    tmpNationPnum = "";
                    tmpBsizonno = outputColumnsSub[17].trim();
                    tmpBsizonnoSeq = outputColumnsSub[18].trim();
                    
                    tmpRoadNationPnum = "";
                    tmpRoadBsizonno = addrColumns[42].trim();
                    tmpRoadBsizonnoSeq = addrColumns[43].trim();
                    
                    tmpRepreMatching = outputColumnsSub[19].trim();
                    tmpApatHouse = outputColumnsSub[20].trim();
                    
                    tmpTDong = outputColumnsSub[0];
                    tmpTRi = outputColumnsSub[1];
                    
                    // 법정동으로 표현 변경 s
                    outputColumns2[14] = outputColumnsSub[0]; // 읍면동
                    if (UserDongFirst == 0)
                    {
                        // if (!outputColumnsSub[10].trim().equals("") && outputColumnsSub[1].trim().equals("")) // 2013.02.14 법정동명이 있고 리명칭이 없을 경우
                        if (!outputColumnsSub[10].trim().equals("") && outputColumnsSub[0].trim().matches(".*[읍]$|.*[면]$")) // 20151124 법정동명이 있고 읍면인 경우
                        {
                            // outputColumns2[14] = outputColumnsSub[10].trim(); // 2013.02.14 법정동
                            outputColumns2[14] = outputColumnsSub[0]; // 읍면동
                        }
                        else
                        {
                            // outputColumns2[14] = outputColumnsSub[0]; // 2013.02.14 읍면동
                            outputColumns2[14] = outputColumnsSub[10].trim(); // 법정동
                        }
                    }
                    // 법정동으로 표현 변경 e
                    outputColumns2[15] = outputColumnsSub[1];
                    if (outputColumnsSub[2].length() > 0 && outputColumnsSub[2].equals("1"))
                    {
                        outputColumns2[16] = "1";
                    }
                    else
                    // 2015.03.10 값에 Null이 들어가므로 0부여
                    {
                        outputColumns2[16] = "0";
                    }
                    
                    if (!outputColumnsSub[3].trim().equals(""))
                    { // 주번지
                        outputColumns2[17] = "";
                        if (outputColumnsSub[2].trim().equals("1"))
                        {
                            outputColumns2[17] = "산";
                        }
                        // outputColumns2[17] = outputColumns2[17] + outputColumnsSub[3];
                        outputColumns2[17] = outputColumnsSub[3];
                    }
                    
                    if (outputColumnsSub[3] == null || outputColumnsSub[3].equals("")) // 2015.03.10 주번지 없을 시 부번지도 없음 처리
                    {
                        outputColumns2[18] = "";
                    }
                    else
                    { // 2015.03.10 주번지 존재 시 부번지 없는 경우 0 처리
                        if (outputColumnsSub[4].equals(""))
                        {
                            outputColumns2[18] = "0";
                        }
                        else
                        {
                            outputColumns2[18] = outputColumnsSub[4];
                        }
                    }
                    
                    tmpBuildinfo = outputColumnsSub[5]; // 건물매칭정보
                    tmpRoadBuild = addrColumns[31];
                    tmpRoadBuildDetail = addrColumns[32];
                    tmpRoadPNU = addrColumns[26]; // 도로명코드(12)+읍면동일련번호(2)+지하여부(1)+건물본번(5) + 건물부번(5)
                    
                    if (!addrColumns[26].trim().equals("")) // jsy 도로명코드가 없는경우의 처리
                    {
                        if (!addrColumns[27].trim().equals(""))
                        {
                            tmpRoadPNU = tmpRoadPNU + addrColumns[27];
                            
                            if (!addrColumns[28].trim().equals(""))
                            {
                                tmpRoadPNU = tmpRoadPNU + addrColumns[28];
                            }
                            else
                            {
                                tmpRoadPNU = tmpRoadPNU + "0";
                            }
                            
                            if (!addrColumns[29].trim().equals("") && !addrColumns[29].trim().equals("0"))
                            {
                                tmpRoadPNU = tmpRoadPNU + lpad(addrColumns[29], 5, "0");
                                
                                if (!addrColumns[30].trim().equals("") && !addrColumns[30].trim().equals("0"))
                                {
                                    tmpRoadPNU = tmpRoadPNU + lpad(addrColumns[30], 5, "0");
                                }
                                else
                                {
                                    tmpRoadPNU = tmpRoadPNU + "00000";
                                }
                            }
                            else
                            {
                                tmpRoadPNU = tmpRoadPNU + "0000000000";
                            }
                        }
                        else
                        {
                            tmpRoadPNU = tmpRoadPNU + "0000000000000";
                        }
                    }
                    else
                    {
                        tmpRoadPNU = "";
                    }
                    
                    tmpRoadCX = addrColumns[36].trim();
                    tmpRoadCY = addrColumns[37].trim();
                    tmpCL = addrColumns[1].substring(6, 8);
                    
                    // 반복 부분이므로 도로명 좌표 기본 사용
                    if (outputColumnsSub.length > 14 && !outputColumnsSub[14].trim().equals("") && !outputColumnsSub[14].trim().equals("0"))
                    {
                        tmpCX = outputColumnsSub[14].trim();
                        tmpCY = outputColumnsSub[15].trim();
                        // tmpCL = "19";
                    }
                    
                    // 영문주소 출력시 추가한다
                    if (strEtcCd.equals("ENG"))
                    {
                        tmpDong_eng = outputColumnsSub[21].trim(); // 영문 읍면동명
                        tmpRi_eng = outputColumnsSub[22].trim(); // 영문 법정리
                        tmpBuildnm_eng = outputColumnsSub[23].trim(); // 영문 다량배달처
                    }
                }
                else
                { // 새주소 출력
                    tmpRoadsubAddr = ""; // 2013.02.22
                    // outputColumns2[4] = outputColumns[4];
                    // System.out.println(" 구주소입력  ==> * outputColumnsSub[11]  : " + outputColumnsSub[11]);
                    
                    if (outputColumnsSub.length > 9 && outputColumnsSub[9].length() == 6) // 2014-01-06 PCA 요청
                    {
                        tmpTPostcode = outputColumnsSub[9];
                        tmpTPostcodeSeq = outputColumnsSub[10];
                    }
                    else if (addrColumns[34].length() == 6)
                    {
                        tmpTPostcode = addrColumns[34];
                        tmpTPostcodeSeq = addrColumns[35];
                    }
                    else if (addrColumns[14].length() == 6)
                    {
                        tmpTPostcode = addrColumns[14];
                        tmpTPostcodeSeq = addrColumns[15];
                    }
                    
                    if (tmpUMD.equals("읍") || tmpUMD.equals("면"))
                    {
                        tmpTAddr1 = tmpAddr + " " + addrColumns[4] + " " + outputColumnsSub[0];
                    }
                    else
                    {
                        tmpTAddr1 = tmpAddr + " " + outputColumnsSub[0];
                    }
                    
                    if (outputColumnsSub.length > 4 && !outputColumnsSub[4].trim().equals(""))
                    {
                        if (outputColumnsSub[3].trim().equals("1"))
                        {
                            tmpTAddr1 = tmpTAddr1 + " 지하";
                        }
                        else if (outputColumnsSub[3].trim().equals("2"))
                        {
                            tmpTAddr1 = tmpTAddr1 + " 공중";
                        }
                        else
                        {
                            tmpTAddr1 = tmpTAddr1 + " ";
                        }
                        
                        tmpTAddr1 = tmpTAddr1 + outputColumnsSub[4];
                        
                        if (outputColumnsSub.length > 5 && !outputColumnsSub[5].trim().equals("") && !outputColumnsSub[5].trim().equals("0"))
                        {
                            tmpTAddr1 = tmpTAddr1 + "-" + outputColumnsSub[5];
                        }
                    }
                    tmpTAddr1 = tmpTAddr1.replaceAll("  ", " ");
                    // System.out.println("  ==> * =============  : " + tmpAddrEtcNewaddr);
                    
                    // 새주소2
                    if (!tmpAddrEtcNewaddr.equals(""))
                    {
                        tmpTAddr2 = tmpAddrEtcNewaddr; // 나머지주소 : 기존주소 이외의 주소
                    }
                    tmpTAddr2 = tmpTAddr2.replaceAll("()", "");
                    tmpTAddr2 = tmpTAddr2.trim();
                    // 새주소2
                    
                    // 괄호(참고) 주소
                    tmpStrNewaddrETC = "";
                    if (!outputColumnsSub[4].trim().equals("") && !outputColumnsSub[4].trim().equals("0")) // 건물주번호
                    {
                        if (addrColumns[5].trim().equals("")) // 리 명칭이 없다면
                        {
                            tmpStrNewaddrETC = addrColumns[22].trim();
                            
                            if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                            {
                                tmpStrNewaddrETC = tmpStrNewaddrETC + ", " + addrColumns[9].trim();
                            }
                            else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                            {
                                tmpStrNewaddrETC = tmpStrNewaddrETC + ", " + addrColumns[17].trim();
                            }
                            else if (!outputColumnsSub[6].trim().equals("")) // 도로명빌딩정보
                            {
                                tmpStrNewaddrETC = tmpStrNewaddrETC + ", " + outputColumnsSub[6].trim();
                            }
                            /*
                             * if(!outputColumnsSub[6].trim().equals("")) //도로명빌딩정보
                             * {
                             * tmpStrNewaddrETC = tmpStrNewaddrETC + ", " + outputColumnsSub[6].trim();
                             * }
                             */
                        }
                        else
                        {
                            
                            if (!addrColumns[9].trim().equals("")) // 빌딩매칭정보
                            {
                                tmpStrNewaddrETC = addrColumns[9].trim();
                            }
                            else if (!addrColumns[17].trim().equals("")) // 빌딩추정정보
                            {
                                tmpStrNewaddrETC = addrColumns[17].trim();
                            }
                            else if (!outputColumnsSub[6].trim().equals("")) // 도로명빌딩정보
                            {
                                tmpStrNewaddrETC = outputColumnsSub[6].trim();
                            }
                            /*
                             * if(!outputColumnsSub[6].trim().equals("")) //도로명빌딩정보
                             * {
                             * tmpStrNewaddrETC = outputColumnsSub[6].trim();
                             * }
                             */
                        }
                    }
                    
                    if (!tmpStrNewaddrETC.equals(""))
                    {
                        // tmpTAddr2 = tmpTAddr2 + " (" + tmpStrNewaddrETC + ")";
                        tmpRoadsubAddr = tmpStrNewaddrETC;
                    }
                    // 괄호(참고) 주소
                    
                    tmpBCode = addrColumns[23].trim();
                    tmpHCode = addrColumns[21].trim();
                    
                    tmpBdong = addrColumns[22].trim();
                    tmpHdong = addrColumns[20].trim();
                    tmpPnu = addrColumns[23].trim() + addrColumns[6].trim() + lpad(addrColumns[7].trim(), 4, "0") + lpad(addrColumns[8].trim(), 4, "0");
                    tmpRoadname = outputColumnsSub[0].trim();
                    tmpNationPnum = "";
                    tmpBsizonno = addrColumns[40].trim();
                    tmpBsizonnoSeq = addrColumns[41].trim();
                    
                    tmpRoadNationPnum = "";
                    tmpRoadBsizonno = outputColumnsSub[17].trim();
                    tmpRoadBsizonnoSeq = outputColumnsSub[18].trim();
                    
                    tmpRepreMatching = outputColumnsSub[19].trim();
                    tmpApatHouse = outputColumnsSub[20].trim();
                    
                    tmpTDong = addrColumns[3];
                    tmpTRi = addrColumns[4];
                    
                    // 법정동으로 표현 변경 s
                    outputColumns2[14] = addrColumns[4]; // 읍면동
                    if (UserDongFirst == 0)
                    {
                        // if (!addrColumns[22].trim().equals("") && addrColumns[5].trim().equals("")) // 2013.02.14 법정동명이 있고 리명칭이 없을 경우
                        if (!addrColumns[22].trim().equals("") && addrColumns[4].trim().matches(".*[읍]$|.*[면]$")) // 20151124 법정동명이 있고 읍면인 경우
                        {
                            // outputColumns2[14] = addrColumns[22].trim(); // 2013.02.14 법정동
                            outputColumns2[14] = addrColumns[4]; // 읍면동
                        }
                        else
                        {
                            // outputColumns2[14] = addrColumns[4]; // 2013.02.14 읍면동
                            outputColumns2[14] = addrColumns[22].trim(); // 법정동
                        }
                    }
                    // 법정동으로 표현 변경 e
                    outputColumns2[15] = addrColumns[5];
                    if (addrColumns[6].length() > 0 && addrColumns[6].equals("1"))
                    {
                        outputColumns2[16] = "1";
                    }
                    else
                    // 2015.03.10 값에 Null이 들어가므로 0부여
                    {
                        outputColumns2[16] = "0";
                    }
                    
                    if (!addrColumns[7].trim().equals(""))
                    {
                        outputColumns2[17] = "";
                        if (addrColumns[6].trim().equals("1"))
                        {
                            outputColumns2[17] = "산";
                        }
                        // outputColumns2[17] = outputColumns2[17] + addrColumns[7];
                        outputColumns2[17] = addrColumns[7];
                    }
                    else
                    // 2015.03.10 값에 Null이 들어가므로 ""부여
                    {
                        outputColumns2[17] = "";
                    }
                    
                    if (addrColumns[7] == null || addrColumns[7].equals("")) // 2015.03.10 주번지 없을 시 부번지도 없음 처리
                    {
                        outputColumns2[18] = "";
                    }
                    else
                    // 2015.03.10 주번지 존재 시 부번지 없는 경우 0 처리
                    {
                        if (addrColumns[8].equals(""))
                        {
                            outputColumns2[18] = "0";
                        }
                        else
                        {
                            outputColumns2[18] = addrColumns[8];
                        }
                    }
                    
                    tmpRoadBuild = outputColumnsSub[6];
                    tmpRoadBuildDetail = outputColumnsSub[7];
                    tmpRoadPNU = outputColumnsSub[1]; // 도로명코드(12)+읍면동일련번호(2)+지하여부(1)+건물본번(5) + 건물부번(5)
                    
                    if (!outputColumnsSub[1].trim().equals("")) // jsy 도로명코드가 없는경우의 처리
                    {
                        if (!outputColumnsSub[2].trim().equals(""))
                        {
                            tmpRoadPNU = tmpRoadPNU + outputColumnsSub[2];
                            
                            if (!outputColumnsSub[3].trim().equals(""))
                            {
                                tmpRoadPNU = tmpRoadPNU + outputColumnsSub[3];
                            }
                            else
                            {
                                tmpRoadPNU = tmpRoadPNU + "0";
                            }
                            
                            if (!outputColumnsSub[4].trim().equals("") && !outputColumnsSub[4].trim().equals("0"))
                            {
                                tmpRoadPNU = tmpRoadPNU + lpad(outputColumnsSub[4], 5, "0");
                                
                                if (!outputColumnsSub[5].trim().equals("") && !outputColumnsSub[5].trim().equals("0"))
                                {
                                    tmpRoadPNU = tmpRoadPNU + lpad(outputColumnsSub[5], 5, "0");
                                }
                                else
                                {
                                    tmpRoadPNU = tmpRoadPNU + "00000";
                                }
                            }
                            else
                            {
                                tmpRoadPNU = tmpRoadPNU + "0000000000";
                            }
                        }
                        else
                        {
                            tmpRoadPNU = tmpRoadPNU + "0000000000000";
                        }
                    }
                    else
                    {
                        tmpRoadPNU = "";
                    }
                    
                    tmpCX = addrColumns[18].trim();
                    tmpCY = addrColumns[19].trim();
                    tmpCL = addrColumns[1].substring(6, 8);
                    
                    // 반복 부분이므로 도로명 좌표 기본 사용
                    if (outputColumnsSub.length > 11 && !outputColumnsSub[11].trim().equals("") && !outputColumnsSub[11].trim().equals("0"))
                    {
                        tmpRoadCX = outputColumnsSub[11].trim();
                        tmpRoadCY = outputColumnsSub[12].trim();
                        // tmpCL = "19";
                    }
                    
                    // 영문주소 출력시 추가한다
                    if (strEtcCd.equals("ENG"))
                    {
                        tmpDong_eng = outputColumnsSub[21].trim(); // 영문 읍면동명
                        tmpRoadnm_eng = outputColumnsSub[22].trim(); // 영문 법정리
                        tmpRoadBuildnm_eng = outputColumnsSub[23].trim(); // 영문 다량배달처
                    }
                }
                
                tmpTAddr2.replaceAll("  ", " ");
                tmpTAddr1 = tmpTAddr1.replaceAll("  ", " ");
                
                tmpTAddr1 = tmpTAddr1.trim();
                tmpTAddr2 = tmpTAddr2.trim();
                
                if (!tmpInputDongL.equals("N") && !tmpInputDongL.equals("U"))
                {
                    tmpBdcode = outputColumnsSub[8];
                    tmpRoadcode = outputColumnsSub[1];
                    tmpRoadmn = outputColumnsSub[4];
                    if (tmpRoadmn == null || tmpRoadmn.equals("")) // 2015.03.10 건물본번 없을 시 부번도 없음 처리
                    {
                        tmpRoadsn = "";
                    }
                    else
                    // 2015.03.10 건물본번 존재 시 부번이 없는 경우 0 처리
                    {
                        tmpRoadsn = outputColumnsSub[5];
                        if (tmpRoadsn.equals(""))
                        {
                            tmpRoadsn = "0";
                        }
                    }
                    tmpRoadumdseq = outputColumnsSub[2];
                    tmpRoadunder = outputColumnsSub[3];
                }
                else
                {
                    tmpBdcode = addrColumns[33];
                    tmpRoadcode = outputColumnsSub[16].trim(); // 17번 항목으로 추가됨
                    tmpRoadmn = addrColumns[29];
                    if (tmpRoadmn == null || tmpRoadmn.equals("")) // 2015.03.10 건물본번 없을 시 부번도 없음 처리
                    {
                        tmpRoadsn = "";
                    }
                    else
                    // 2015.03.10 건물본번 존재 시 부번이 없는 경우 0 처리
                    {
                        tmpRoadsn = addrColumns[30];
                        if (tmpRoadsn.equals(""))
                        {
                            tmpRoadsn = "0";
                        }
                    }
                    tmpRoadumdseq = addrColumns[27];
                    tmpRoadunder = addrColumns[28];
                }
            }
            
            // tmpBuildinfo = addrColumns[9].trim();
            tmpBuilddong = addrColumns[10].trim();
            tmpBuildho = addrColumns[11].trim();
            tmpBuildstair = addrColumns[12].trim();
            // tmpremain = addrColumns[13].trim();
            // 20150616 나머지주소(건물동,호,층,파싱불가능주소) 만들기 s
            // tmpremain = MakeRemain(tmpBuilddong, tmpBuildho, tmpBuildstair, addrColumns[13].trim());
            // 20150616 나머지주소(건물동,호,층,파싱불가능주소) 만들기 e
            
            // 20150717 서버에서 내주는 상세주소 원본 유지하면서 나머지 만들기 s
            tmpremain = addrColumns[24].trim(); // 지번주소용 나머지(지번주소용 상세 주소에서 지번정보와 건물매청정보 제거)
            if (tmpremain != null && !tmpremain.equals(""))
            {
                String tmpMainBuildinfo = addrColumns[9].trim().equals("") ? addrColumns[17].trim() : addrColumns[9].trim(); // 건물매칭정보
                String tmpSanBunjiHo = "";
                if (outputColumns2[16].equals("1")) tmpSanBunjiHo = "산";
                if (!outputColumns2[17].equals(""))
                {
                    tmpSanBunjiHo += outputColumns2[17];
                    if (!outputColumns2[18].equals("") && !outputColumns2[18].equals("0")) tmpSanBunjiHo += ("-" + outputColumns2[18]);
                }
                
                if (!tmpSanBunjiHo.equals(""))
                {
                    if (tmpremain.matches("^" + tmpSanBunjiHo + "$")) tmpremain = tmpremain.replaceAll(tmpSanBunjiHo, "");
                    else if (tmpremain.matches("^(" + tmpSanBunjiHo + "\\s).*")) tmpremain = tmpremain.replaceAll("^(" + tmpSanBunjiHo + "\\s)", "");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpSanBunjiHo + "\\s*,*\\)).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpSanBunjiHo + "\\s*,*\\))", "");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpSanBunjiHo + "\\s*,+\\s*).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpSanBunjiHo + "\\s*,+\\s*)", "(");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpSanBunjiHo + "\\s+).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpSanBunjiHo + "\\s+)", "(");
                }
                
                if (i != 1)
                { // 지번상세 같은 경우 서버에서 내주는 원본 그대로 사용하며 멀티의 경우만 수정해준다.
                    if (tmpremain.matches("^" + tmpMainBuildinfo + "$")) tmpremain = tmpremain.replaceAll(tmpMainBuildinfo, "");
                    else if (tmpremain.matches("^(" + tmpMainBuildinfo + "\\s).*")) tmpremain = tmpremain.replaceAll("^(" + tmpMainBuildinfo + "\\s)", "");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpMainBuildinfo + "\\s*,*\\)).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpMainBuildinfo + "\\s*,*\\))", "");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpMainBuildinfo + "\\s*,+\\s*).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpMainBuildinfo + "\\s*,+\\s*)", "(");
                    else if (tmpremain.matches("^(\\(\\s*" + tmpMainBuildinfo + "\\s+).*")) tmpremain = tmpremain.replaceAll("^(\\(\\s*" + tmpMainBuildinfo + "\\s+)", "(");
                    
                    tmpremain = tmpBuildinfo.equals("") ? tmpremain : tmpBuildinfo + " " + tmpremain;
                }
            }
            else if (tmpremain != null && tmpremain.equals("") && i > 1)
            { // 나머지 정보가 없으며 멀티 일 경우 처리
                tmpremain = tmpBuildinfo.equals("") ? tmpremain : tmpBuildinfo + " " + tmpremain;
            }
            tmpRoremain = addrColumns[38].trim(); // 도로명주소용 나머지(도로명주소용 상세 주소에서 건물번호정보와 도로명건물명 제거)
            if (tmpRoremain != null && !tmpRoremain.equals(""))
            {
                String tmpUnderMnSn = "";
                if (tmpRoadunder.equals("1")) tmpUnderMnSn = "지하";
                else if (tmpRoadunder.equals("2")) tmpUnderMnSn = "공중";
                if (!tmpRoadmn.equals(""))
                {
                    tmpUnderMnSn += tmpRoadmn;
                    if (!tmpRoadsn.equals("") && !tmpRoadsn.equals("0")) tmpUnderMnSn += ("-" + tmpRoadsn);
                }
                
                if (!tmpUnderMnSn.equals(""))
                {
                    if (tmpRoremain.matches("^" + tmpUnderMnSn + "$")) tmpRoremain = tmpRoremain.replaceAll(tmpUnderMnSn, "");
                    else if (tmpRoremain.matches("^(" + tmpUnderMnSn + "\\s).*")) tmpRoremain = tmpRoremain.replaceAll("^(" + tmpUnderMnSn + "\\s)", "");
                }
                
                if (tmpRoremain.matches("^" + addrColumns[31] + "$")) tmpRoremain = tmpRoremain.replaceAll(addrColumns[31], "");
                else if (tmpRoremain.matches("^(" + addrColumns[31] + "\\s).*")) tmpRoremain = tmpRoremain.replaceAll("^(" + addrColumns[31] + "\\s)", "");
            }
            // 20150717 서버에서 내주는 상세주소 원본 유지하면서 나머지 만들기 e
            
            tmpAddr2 = tmpAddr2.replaceAll("  ", " ");
            tmpAddr2 = tmpAddr2.replaceAll("()", "");
            
            // System.out.println("  ==> * tmpTAddr2  : " + tmpTAddr2);
            outputColumns2[7] = tmpTPostcode;
            outputColumns2[8] = tmpTPostcodeSeq;
            outputColumns2[9] = tmpTAddr1;
            outputColumns2[10] = tmpTAddr2;
            outputColumns2[11] = addrColumns[33];
            outputColumns2[12] = addrColumns[2];
            outputColumns2[13] = addrColumns[3];
            
            // if (outputColumns2[16].equals("1")) // 2015.03.10 산처리(0:일반, 1:산)
            // {
            // outputColumns2[16] = "산";
            // }
            // else
            // {
            // outputColumns2[16] = "";
            // }
            
            outputColumns2[19] = tmpCX;
            outputColumns2[20] = tmpCY;
            outputColumns2[21] = tmpCL;
            outputColumns2[22] = tmpBCode;
            outputColumns2[23] = tmpHCode;
            // System.out.println("  ==> * tmpRoadsubAddr  : " + tmpRoadsubAddr);
            outputColumns2[24] = tmpRoadsubAddr;
            outputColumns2[25] = tmpRoadcode;
            outputColumns2[26] = tmpRoadumdseq;
            outputColumns2[27] = tmpRoadunder;
            // if (outputColumns2[27].equals("1")) // 2015.03.10 지하처리(0:일반, 1:지하, 2:공중)
            // {
            // outputColumns2[27] = "지하";
            // }
            // else if (outputColumns2[27].equals("2"))
            // {
            // outputColumns2[27] = "공중";
            // }
            // else
            // {
            // outputColumns2[27] = "";
            // }
            outputColumns2[28] = tmpRoadmn;
            outputColumns2[29] = tmpRoadsn;
            // if(outputColumns2[29].equals(""))
            // {
            // outputColumns2[29] = "0";
            // }
            
            outputColumns2[30] = tmpBdong;
            outputColumns2[31] = tmpHdong;
            
            if (tmpPnu == null || tmpPnu.length() != 19)
            {
                tmpPnu = "";
            }
            outputColumns2[32] = tmpPnu;
            outputColumns2[33] = tmpRoadname;
            
            if ((outputColumns2[0].trim().equals("R") && !outputColumns2[14].trim().equals("") && !outputColumns2[17].trim().equals("")) || (outputColumns2[0].trim().equals("J") && !outputColumns2[28].trim().equals("")))
            { // 입력이 도로명이고 전환 지번이 유효할 경우(읍면동이 존재하며 번지가 있음) 혹은 입력이 지번이고 전환 도로명이 유효할 경우(건물본번이 있음)
                if (tmpBsizonno.length() != 5) tmpBsizonno = tmpRoadBsizonno;
            }
            outputColumns2[34] = tmpNationPnum;
            outputColumns2[35] = tmpBsizonno;
            outputColumns2[36] = tmpBsizonnoSeq;
            outputColumns2[37] = tmpRoadNationPnum;
            outputColumns2[38] = tmpRoadBsizonno;
            outputColumns2[39] = tmpRoadBsizonnoSeq;
            
            outputColumns2[40] = tmpInputDongL;
            
            outputColumns2[41] = tmpBuildinfo;
            outputColumns2[42] = tmpBuilddong;
            outputColumns2[43] = tmpBuildho;
            outputColumns2[44] = tmpBuildstair;
            outputColumns2[45] = tmpRoremain;
            
            outputColumns2[46] = tmpRoadBuild;
            outputColumns2[47] = tmpRoadBuildDetail;
            
            if (tmpRoadPNU == null || tmpRoadPNU.length() != 25)
            {
                tmpRoadPNU = "";
            }
            outputColumns2[48] = tmpRoadPNU;
            
            outputColumns2[49] = tmpRepreMatching;
            outputColumns2[50] = tmpApatHouse;
            
            outputColumns2[51] = tmpAnnexBuild;
            outputColumns2[52] = tmpAnnexInfo;
            
            outputColumns2[53] = tmpProhibit;
            
            outputColumns2[54] = tmpremain;
            
            outputColumns2[55] = tmpNewPostLev;
            outputColumns2[56] = tmpAddrlev;
            outputColumns2[57] = tmpPostLev;
            outputColumns2[58] = tmpPostType;
            outputColumns2[59] = tmpErrCd;
            outputColumns2[60] = tmpDataChangeCd;
            
            outputColumns2[61] = tmpRoadCX;
            outputColumns2[62] = tmpRoadCY;
            outputColumns2[63] = tmpMultiFlag;
            
            if (strEtcCd.equals("ENG"))
            { // 영문주소 출력일 경우 적용한다
                outputColumns2[64] = tmpSido_eng;
                outputColumns2[65] = tmpSigungu_eng;
                outputColumns2[66] = tmpDong_eng;
                outputColumns2[67] = tmpRi_eng;
                outputColumns2[68] = tmpBuildnm_eng;
                outputColumns2[69] = tmpRoadnm_eng;
                outputColumns2[70] = tmpRoadBuildnm_eng;
            }
            
            // 20150707 고객사 요청에 따른 시/도 축약 s
            if (ShortSido == 1)
            {
                if (!outputColumns2[12].equals(""))
                {
                    String cvtSido = ShortMakeSido(outputColumns2[12]);
                    if (!cvtSido.equals("")) outputColumns2[12] = cvtSido;
                }
            }
            // 20150707 고객사 요청에 따른 시/도 축약 e
            
            // 20150618 고객사 설정에 따른 주소형태 변경 s
            if ((outputColumns2[40]).equals("N") || (outputColumns2[40]).equals("U") || (outputColumns2[40]).equals("S"))
            { // 입력 - 도로명주소
                if (!outputColumns2[17].trim().equals(""))
                { // 번지 존재 시
                    if (API_USER_VER.equals("MAEIL_DS"))
                    { // 고객사 전용(주소 형태가 변칙적일 경우 사용)
                        outputColumns2[9] = EasyMakeCustomAdd1(outputColumns2, API_USER_VER);
                        outputColumns2[10] = EasyMakeCustomAdd2(outputColumns2, API_USER_VER);
                    }
                    else
                    {
                        outputColumns2[9] = EasyMakeAdd1(outputColumns2); // 전환-기본1
                        outputColumns2[10] = EasyMakeAdd2(outputColumns2); // 전환-기본2
                    }
                }
                else
                {
                    outputColumns2[9] = ""; // 전환-기본1
                    outputColumns2[10] = ""; // 전환-기본2
                }
                
                outputColumns2[5] = EasyMakeRoAdd1(outputColumns2); // 정제-기본1
                outputColumns2[6] = EasyMakeRoAdd2(outputColumns2); // 정제-기본2
            }
            else
            {
                if (API_USER_VER.equals("MAEIL_DS"))
                { // 고객사 전용(주소 형태가 변칙적일 경우 사용)
                    outputColumns2[5] = EasyMakeCustomAdd1(outputColumns2, API_USER_VER);
                    outputColumns2[6] = EasyMakeCustomAdd2(outputColumns2, API_USER_VER);
                }
                else
                {
                    outputColumns2[5] = EasyMakeAdd1(outputColumns2); // 정제-기본1
                    outputColumns2[6] = EasyMakeAdd2(outputColumns2); // 정제-기본2
                }
                
                if (!outputColumns2[28].trim().equals(""))
                { // 본번 존재 시
                    outputColumns2[9] = EasyMakeRoAdd1(outputColumns2); // 전환-기본1
                    outputColumns2[10] = EasyMakeRoAdd2(outputColumns2); // 전환-기본2
                }
                else
                {
                    outputColumns2[9] = ""; // 전환-기본1
                    outputColumns2[10] = ""; // 전환-기본2
                }
            }
            
            outputColumns2[24] = EasyMakeRoRef(outputColumns2); // 참조주소
            // 20150618 고객사 설정에 따른 주소형태 변경 e
            
            outputColumns_Mult[i - 1] = outputColumns2;
        }
        // ***********변환주소
        
        return outputColumns_Mult;
    }
    
    /**
     * @Metod : Search2
     * @Description : 우편번호 및 행정구역 검색 배열화 함수
     * @param txtType : InOutProtocol
     * @param txtSearch : 검색하고자 하는 코드성 및 단어형 데이터(우편번호, PNU코드, 기초구역번호, 검색명 등)
     * @param txtPage1 : 검색하고자 하는 시작레코드 데이터(시작레코드 번호)
     * @param txtPage2 : 검색하고자 하는 출력레코드 데이터(출력 레코드 수)
     * @return
     */
    public String[][] Search2(String txtType, String txtSearch, String txtPage1, String txtPage2)
    {
        String[][] outputColumns_Mult = null;
        
        String ioptMain = ""; // 메인코드
        String ioptSub = ""; // 서브코드
        
        int multi_first_len = 0; // outputColumns_Mult 행 길이()
        int multi_second_len = 0; // outputColumns_Mult 열 길이( 열의 길이는 결과구분, 전체, 시작, 출력수량 으로 인하여 +4 상태임 )
        int fixed_len = 4; // 검색 고정 열 수량(결과구분, 전체레코드, 시작레코드, 반복레코드) : 행정구역 검색에서 변경 될 수 있음
        
        String gubun = ""; // 행정구역 검색 동코드 구분
        String Level = ""; // 행정구역 검색 행정구역 레벨
        
        String mkAddrSearch = MakeGviaInData(txtType, txtSearch, txtPage1, txtPage2);
        
        if (mkAddrSearch.equals("") || mkAddrSearch.equals("ERR_MK||||"))
        {
            outputColumns_Mult = new String[1][2];
            outputColumns_Mult[0][0] = "0";
            outputColumns_Mult[0][1] = "err|";
            return outputColumns_Mult;
        }
        
        String strResultTmp = ExecCallGvia(txtType, mkAddrSearch);
        
        String[] addrSpl = strResultTmp.split(";"); // 멀티결과 분리
        String[] addrColumns = addrSpl[0].split("[\u007C]"); // 결과 내 "|" 분리
        
        String strGubun = addrColumns[1]; // 결과구분
        String strToTRecord = addrColumns[2]; // 전체레코드 수
        String strStRecord = addrColumns[3]; // 시작레코드 수
        String strOutRecord = addrColumns[4]; // 반복레코드 수
        
        ioptMain = txtType.substring(0, 1);
        ioptSub = txtType.substring(2, 3);
        
        multi_first_len = addrSpl.length - 2;
        
        // 이중배열 행,열 길이 설정 start
        if (ioptMain.equals("5")) // 우편번호 검색
        {
            if (ioptSub.equals("A") || ioptSub.equals("C"))
            { // 지번주소용 우편번호 조회 결과 출력
                multi_second_len = 19; // 15
            }
            else if (ioptSub.matches("[LMNOPQRqrWwYyZ]"))
            { // 도로명주소 조회 결과 출력
                multi_second_len = 25;
            }
            else if (ioptSub.equals("D") || ioptSub.equals("F") || ioptSub.equals("H") || ioptSub.equals("J"))
            { // 도로명주소용 우편번호 조회 결과
                multi_second_len = 22; // 18
            }
            else if (ioptSub.equals("S") || ioptSub.equals("T"))
            { // 도로명주소용 기초구역번호 범위(Range) 조회 결과
                multi_second_len = 16; // 12
            }
            else if (ioptSub.equals("U"))
            { // 지번,도로명 통합 우편번호 검색 결과
                multi_second_len = 26; // 22
            }
            else if (ioptSub.equals("V") || ioptSub.equals("X"))
            { // 지번,도로명 통합 우편번호 건수 검색 결과
                multi_second_len = 39; // 35
            }
        }
        else if (ioptMain.equals("D")) // 우편번호 검색(영문 포함)
        {
            if (ioptSub.equals("A") || ioptSub.equals("C"))
            {
                multi_second_len = 24; // 20
            }
            else if (ioptSub.matches("[LMNOPQRqrWwYyZ]"))
            {
                multi_second_len = 30;
            }
            else if (ioptSub.equals("D") || ioptSub.equals("F") || ioptSub.equals("H") || ioptSub.equals("J"))
            {
                multi_second_len = 27; // 23
            }
            else if (ioptSub.equals("S") || ioptSub.equals("T"))
            {
                multi_second_len = 21; // 17
            }
            else if (ioptSub.equals("U"))
            {
                multi_second_len = 31; // 27
            }
            else if (ioptSub.equals("V") || ioptSub.equals("X"))
            { // 지번,도로명 통합 우편번호 건수 검색 결과
                multi_second_len = 39; // 35 영문 필요 없음
            }
        }
        else if (ioptMain.equals("6")) // 행정구역 검색
        {
            multi_second_len = 8; // 4 반복 길이는 2 이나 반복 안되는 고정 길이 2가 추가 존재함
        }
        else if (ioptMain.equals("E")) // 행정구역 검색(영문 포함)
        {
            multi_second_len = 9; // 5
        }
        
        // System.out.println("2290_ioptMain : " + ioptMain);
        // System.out.println("2291_ioptSub : " + ioptSub);
        // System.out.println("2292_multi_first_len : " + multi_first_len);
        // System.out.println("2293_multi_second_len : " + multi_second_len);
        // 이중배열 행,열 길이 설정 end
        
        // 정상, 오류 상태에 따른 배열 분배 start
        if (strGubun.equals("1")) // 정상
        {
            outputColumns_Mult = new String[multi_first_len][multi_second_len];
            
            for (int i = 0; i < multi_first_len; i++)
            {
                String[] outputColumnsTmp = new String[multi_second_len];
                
                outputColumnsTmp[0] = strGubun; // 결과구분
                outputColumnsTmp[1] = strToTRecord; // 전체레코드 수
                outputColumnsTmp[2] = strStRecord; // 시작레코드 수
                outputColumnsTmp[3] = strOutRecord; // 반복레코드 수
                
                String[] addrColumnsSub = addrSpl[i + 1].split("[\u007C]");
                
                if (ioptMain.equals("6") || ioptMain.equals("E"))
                {
                    if (i == 0)
                    {
                        gubun = addrColumnsSub[0].trim();
                        Level = addrColumnsSub[1].trim();
                    }
                    else
                    {
                        fixed_len = 6; // 행정구역 검색 반복 안되는 고정 길이 2가 추가 존재
                        outputColumnsTmp[4] = gubun;
                        outputColumnsTmp[5] = Level;
                    }
                }
                
                for (int k = 0; k < addrColumnsSub.length; k++)
                {
                    outputColumnsTmp[k + fixed_len] = addrColumnsSub[k].trim();
                }
                outputColumns_Mult[i] = outputColumnsTmp;
            }
        }
        else
        // 오류
        {
            outputColumns_Mult = new String[1][multi_second_len];
            
            String[] outputColumnsErr = new String[multi_second_len];
            
            for (int i = 0; i < multi_second_len; i++)
            {
                if (i == 0) outputColumnsErr[0] = "0";
                else outputColumnsErr[i] = "";
            }
            outputColumns_Mult[0] = outputColumnsErr;
        }
        // 정상, 오류 상태에 따른 배열 분배 end
        
        // 오류 검사
        // if (outputColumns_Mult.length == 0 || (outputColumns_Mult.length == 1 && outputColumns_Mult[0][0].equals("0")))
        if (outputColumns_Mult.length == 0)
        {
            outputColumns_Mult = new String[1][2];
            outputColumns_Mult[0][0] = "0";
            outputColumns_Mult[0][1] = "err|";
        }
        
        return outputColumns_Mult;
    }
    
    // ////////////////// gvia end ////////////////////
    
    // ////////////////// gvia_common start ////////////////////
    
    /**
     * @Description : Gvia protocol Body 데이터 형성(행정구역 전체 검색)
     * @param txtSearch : 검색어
     * @return String
     */
    public String MakeGviaSearchData(String txtSearch)
    {
        return MakeGviaSearchData("", "", "", "", txtSearch);
    }
    
    /**
     * @Description : Gvia protocol Body 데이터 형성(행정구역 시도 + 검색어 검색)
     * @param sido : 광역시/도
     * @param txtSearch : 검색어
     * @return String
     */
    public String MakeGviaSearchData(String sido, String txtSearch)
    {
        return MakeGviaSearchData(sido, "", "", "", txtSearch);
    }
    
    /**
     * @Description : Gvia protocol Body 데이터 형성(행정구역 시도 + 시군구 + 검색어 검색)
     * @param sido : 광역시/도
     * @param gungu : 시/군/구
     * @param txtSearch : 검색어
     * @return String
     */
    public String MakeGviaSearchData(String sido, String gungu, String txtSearch)
    {
        return MakeGviaSearchData(sido, gungu, "", "", txtSearch);
    }
    
    /**
     * @Description : Gvia protocol Body 데이터 형성(행정구역 시도 + 시군구 + 읍면동 + 검색어 검색)
     * @param sido : 광역시/도
     * @param gungu : 시/군/구
     * @param dong : 읍/면/동
     * @param txtSearch : 검색어
     * @return String
     */
    public String MakeGviaSearchData(String sido, String gungu, String dong, String txtSearch)
    {
        return MakeGviaSearchData(sido, gungu, dong, "", txtSearch);
    }
    
    /**
     * @Method MakeGviaSearchData
     * @data 2015.05.18
     * @Description : Gvia protocol Body 데이터 형성
     * @param sido : 광역시/도
     * @param gungu : 시/군/구
     * @param dong : 읍/면/동
     * @param ri : 리
     * @param txtSearch : 검색어
     * @return String
     */
    public String MakeGviaSearchData(String sido, String gungu, String dong, String ri, String txtSearch)
    {
        StringBuffer mkSearchData = new StringBuffer();
        String splitBar = "|";
        
        if (sido == null) sido = "";
        if (gungu == null) gungu = "";
        if (dong == null) dong = "";
        if (ri == null) ri = "";
        if (txtSearch == null) txtSearch = "";
        
        txtSearch = txtSearch.trim();
        
        mkSearchData.append(sido);
        mkSearchData.append(splitBar);
        mkSearchData.append(gungu);
        mkSearchData.append(splitBar);
        mkSearchData.append(dong);
        mkSearchData.append(splitBar);
        mkSearchData.append(ri);
        mkSearchData.append(splitBar);
        mkSearchData.append(txtSearch);
        
        return mkSearchData.toString();
    }
    
    /**
     * @Method MakeGviaInData
     * @data 2015.03.11
     * @param txtType : InOutProtocol
     * @param txtPostSearch : 검색하고자 하는 코드성 및 단어형 데이터(우편번호, PNU코드, 기초구역번호, 검색명)
     * @param txtAddrRecord1 : 검색하고자 하는 문자열 및 시작레코드 데이터(주소정보, 기본 주소정보, 시작레코드 번호)
     * @param txtAddrRecord2 : 검색하고자 하는 문자열 및 출력레코드 데이터(상세 주소정보, 출력 레코드 수)
     * @return mkAddrSearch : 데이터 길이가 포함되지 않은 패킷 정의가 끝난 검색용 데이터
     */
    public String MakeGviaInData(String txtType, String txtPostSearch, String txtAddrRecord1, String txtAddrRecord2)
    {
        StringBuffer sbResult = new StringBuffer();
        String mkAddrSearch = "";
        String ioptMain = ""; // 메인코드
        String ioptPage = ""; // paging코드
        String ioptSub = ""; // 서브코드
        String ioptVer = ""; // packet코드
        String splitBar = "|"; // 데이터 구분자
        String tmpAR1 = "1"; // paging 시작번호
        String tmpAR2 = "100"; // paging 출력 수
        String tmpPS = ""; // 행정구역 검색 문자열
        String stSptr = ""; // 검색어 시작구분자(SQL LITE용)
        String edSptr = ""; // 검색어 종료구분자(SQL LITE용)
        String oldSplitBar = "||||"; // [광역시도 + 시군구 + 읍면동 + 리 + 검색어]의 형태로 들어오지 않는경우 강제 데이터 처리
        String postErr = "ERR_MK||||"; // 데이터-GVIA우편번호검색 생성 오류
        String packErr = ""; // 헤더-프로토콜 입력 오류
        
        if (txtType == null || txtType.equals("")) return packErr;
        
        if (txtType.length() == 1)
        { // 킵버전용
            ioptMain = txtType;
            ioptPage = "0";
            ioptSub = "0";
            ioptVer = "0";
        }
        else if (txtType.length() == 4)
        { // New, NewH, NewH2(Gvia)
            ioptMain = txtType.substring(0, 1);
            ioptPage = txtType.substring(1, 2);
            ioptSub = txtType.substring(2, 3);
            ioptVer = txtType.substring(3, 4);
        }
        else
        {
            outLog("===== 코드입력 오류 =====");
            return packErr;
        }
        
        outLog("txtType : " + ioptMain + "/" + ioptPage + "/" + ioptSub + "/" + ioptVer);
        outLog("txtSearch : " + txtPostSearch + "/" + txtAddrRecord1 + "/" + txtAddrRecord2);
        
        if (txtPostSearch == null) txtPostSearch = "";
        if (txtAddrRecord1 == null) txtAddrRecord1 = "";
        if (txtAddrRecord2 == null) txtAddrRecord2 = "";
        
        if (ioptMain.equals("1")) // 킵(keep) 버전 - 우편번호 없는 주소정제
        {
            sbResult.append(txtAddrRecord1);
        }
        else if (ioptMain.equals("2")) // 킵(keep) 버전 - 우편번호 있는 주소정제
        {
            sbResult.append(txtPostSearch);
            sbResult.append(splitBar);
            sbResult.append(txtAddrRecord1);
        }
        else if (ioptMain.equals("3") || ioptMain.equals("C")) // 주소정제
        {
            if (ioptSub.equals("1") || ioptSub.equals("B") || ioptSub.equals("S"))
            {
                sbResult.append(txtPostSearch);
                sbResult.append(splitBar);
                sbResult.append(txtAddrRecord1);
            }
            else if (ioptSub.equals("2") || ioptSub.equals("7") || ioptSub.equals("C") || ioptSub.equals("D") || ioptSub.equals("T"))
            {
                sbResult.append(txtPostSearch);
                sbResult.append(splitBar);
                sbResult.append(txtAddrRecord1);
                sbResult.append(splitBar);
                sbResult.append(txtAddrRecord2);
            }
            else if (ioptSub.equals("5") || ioptSub.equals("6") || ioptSub.equals("F") || ioptSub.equals("S"))
            {
                sbResult.append(txtPostSearch);
                sbResult.append(splitBar);
                sbResult.append(txtAddrRecord1 + " " + txtAddrRecord2);
            }
            else
            {
                sbResult.append(txtAddrRecord1);
            }
        }
        else if (ioptMain.equals("5") || ioptMain.equals("D")) // 우편번호 검색
        {
            outLog("========== 우편번호검색 start ==========");
            
            if (ioptVer.equals("3"))
            {
                outLog("===== PACKET VER : SQL LITE start =====");
                
                String postLikeCode = "[ADdHJS]"; // 유사어(LIKE)검색 코드
                String postLongCode = "[LMNOPQRqrYyZWwX]"; // 개별출력항목 검색 코드(도로명 OR 도로명 + 건물본번 OR 도로명 + 건물본번 + 건물부번)
                String postCode = "[CF]"; // 구우편번호(6자리)
                String totCode = "[UV]"; // 통합검색
                
                if (ioptSub.matches(postLikeCode) || ioptSub.matches(totCode))
                { // 유사어(LIKE) 검색
                    stSptr = "";
                    edSptr = "*";
                }
                else if (ioptSub.matches(postLongCode) || ioptSub.matches(postCode))
                {
                    // 새우편번호(기초구역번호), 구우편번호(6자리), 도로명 OR 도로명 + 건물본번 OR 도로명 + 건물본번 + 건물부번 검색 코드
                    stSptr = "";
                    edSptr = "";
                }
                else
                { // 정명칭 검색
                    stSptr = "^";
                    edSptr = "";
                }
                
                txtPostSearch = txtPostSearch.replaceAll("[$]", "|"); // SQL LITE
                txtPostSearch = txtPostSearch.replaceAll("%24", ""); // DB검색 방식 일때 %24를 기준으로 분할, Engine검색 방식일 때 ""처리
                
                String[] tmpArr = txtPostSearch.split("[\u007C]");
                String tmpStr = "";
                boolean isOk = false;
                
                if (tmpArr.length == 1)
                {
                    tmpStr = oldSplitBar + stSptr + tmpArr[0] + edSptr;
                    isOk = true;
                }
                else if (tmpArr.length == 5)
                {
                    for (int i = 0; i < tmpArr.length; i++)
                    {
                        // outLog("tmpArr[" + i + "] : " + tmpArr[i]);
                        if (i == 0)
                        {
                            tmpStr = tmpArr[i];
                        }
                        else if (i == tmpArr.length - 1)
                        {
                            tmpStr = tmpStr + "|" + stSptr + tmpArr[tmpArr.length - 1] + edSptr;
                            isOk = true;
                        }
                        else
                        {
                            tmpStr = tmpStr + "|" + tmpArr[i];
                        }
                    }
                }
                
                if (isOk) txtPostSearch = tmpStr;
                else txtPostSearch = postErr;
                
                outLog("===== PACKET VER : SQL LITE end =====");
            }
            else
            {
                txtPostSearch = txtPostSearch.replaceAll("[$]", ""); // DB검색 방식 일때 $를 기준으로 분할, Engine검색 방식일 때 ""처리
                txtPostSearch = txtPostSearch.replaceAll("%24", ""); // DB검색 방식 일때 %24를 기준으로 분할, Engine검색 방식일 때 ""처리
            }
            
            if (ioptPage.equals("0")) // paging 사용안함
            {
                sbResult.append(txtPostSearch);
            }
            else if (ioptPage.equals("1")) // paging 사용함
            {
                if (ioptSub.equals("V") || ioptSub.equals("X")) // 통합검색에서 건수 검색은 paging 예외 처리
                {
                    sbResult.append(txtPostSearch);
                }
                else
                {
                    if (!txtAddrRecord1.equals("") && txtAddrRecord1 != null) // paging 시작 번호 존재 할 경우(없으면 설정 된 기본 값)
                    {
                        tmpAR1 = txtAddrRecord1;
                    }
                    
                    if (!txtAddrRecord2.equals("") && txtAddrRecord2 != null) // paging 출력 갯수 존재 할 경우(없으면 설정 된 기본 값)
                    {
                        tmpAR2 = txtAddrRecord2;
                    }
                    
                    sbResult.append(txtPostSearch);
                    sbResult.append(splitBar);
                    sbResult.append(tmpAR1);
                    sbResult.append(splitBar);
                    sbResult.append(tmpAR2);
                }
            }
            
            outLog("========== 우편번호검색 end ==========");
        }
        else if (ioptMain.equals("6") || ioptMain.equals("E")) // 행정구역 검색
        {
            tmpPS = txtPostSearch;
            
            if (!txtAddrRecord1.equals("") && txtAddrRecord1 != null) // paging 시작 번호 존재 할 경우(없으면 0)
            {
                tmpAR1 = txtAddrRecord1;
            }
            else
            {
                tmpAR1 = "0";
            }
            
            if (!txtAddrRecord2.equals("") && txtAddrRecord2 != null) // paging 출력 갯수 존재 할 경우(없으면 0)
            {
                tmpAR2 = txtAddrRecord2;
            }
            else
            {
                tmpAR2 = "0";
            }
            
            sbResult.append(tmpPS);
            sbResult.append(splitBar);
            sbResult.append(tmpAR1);
            sbResult.append(splitBar);
            sbResult.append(tmpAR2);
        }
        
        mkAddrSearch = sbResult.toString();
        outLog("mkAddrSearch : " + mkAddrSearch);
        return mkAddrSearch;
    }
    
    /**
     * @Method getBufSize
     * @data 2015.03.11
     * @Description : InOutProtocol에 따라 버퍼사이즈 반환시켜주는 함수
     * @param txtType : InOutProtocol
     * @return bufSize : IOPt 메인코드에 따라 버퍼사이즈 봔환
     */
    private int getGviaBufSize(String txtType)
    {
        int bufSize = 1024 * 15;
        String ioptMain = ""; // 메인코드
        
        ioptMain = txtType.substring(0, 1);
        
        if (ioptMain.equals("5") || ioptMain.equals("D"))
        {
            bufSize = 1024 * 10;
        }
        else if (ioptMain.equals("6") || ioptMain.equals("E"))
        {
            bufSize = 1024 * 10;
        }
        
        return bufSize;
    }
    
    /**
     * @Method getGviaExecMinLen
     * @data 2015.03.11
     * @Description : InOutProtocol에 따라 검색 가능 최소길이 봔환하는 함수
     * @param txtType : InOutProtocol
     * @return minLength : IOPt 메인코드에 따라 검색가능 최소길이 반환
     */
    private int getGviaExecMinLen(String txtType)
    {
        int minLength = 4;
        String ioptMain = ""; // 메인코드
        
        ioptMain = txtType.substring(0, 1);
        
        if (ioptMain.equals("5") || ioptMain.equals("D"))
        {
            minLength = 2;
        }
        else if (ioptMain.equals("6") || ioptMain.equals("E"))
        {
            minLength = 2;
        }
        
        return minLength;
    }
    
    /**
     * @Method getGviaExecMaxLen
     * @data 2015.04.16
     * @Description : InOutProtocol에 따라 검색 가능 최대길이 봔환하는 함수
     * @param txtType : InOutProtocol
     * @return minLength : IOPt 메인코드에 따라 검색가능 최대길이 반환
     */
    private int getGviaExecMaxLen(String txtType)
    {
        int maxLength = 3072; // 3byte
        String ioptMain = ""; // 메인코드
        String ioptSub = ""; // 서브코드
        
        ioptMain = txtType.substring(0, 1);
        ioptSub = txtType.substring(2, 3);
        
        if (ioptMain.equals("3"))
        {
            if (ioptSub.equals("x") || ioptSub.equals("y") || ioptSub.equals("z"))
            { // 배치용
                maxLength = 40960; // 40byte
            }
        }
        
        return maxLength;
    }
    
    /**
     * @Method cvtGviaHeadCode
     * @Description : 상황에 따라 헤더드를 변경한다.(GpointKeep버전 헤더코드를 Gvia버전에 맞는 헤더코드로 변환 등)
     * @param txtType : InOutProtocol 헤더코드
     * @param mkAddrSearch : 검색데이터
     * @return cvtType : 변환 완료된 헤더코드
     */
    private String cvtGviaHeadCode(String txtType, String mkAddrSearch)
    {
        String cvtType = "";
        String ioptMain = ""; // 메인코드
        String ioptPage = ""; // 페이지
        String ioptSub = ""; // 서브
        String ioptVer = ""; // 버전
        
        if (txtType != null && !txtType.equals(""))
        {
            ioptMain = txtType.substring(0, 1);
            ioptPage = txtType.substring(1, 2);
            ioptSub = txtType.substring(2, 3);
            ioptVer = txtType.substring(3, 4);
            
            if (txtType.length() == 1)
            {
                if (ioptMain.matches("1")) cvtType = "3002";
                else if (ioptMain.matches("2")) cvtType = "3012";
            }
            else if (txtType.length() == 4)
            {
                if ((ioptMain.matches("5") || ioptMain.matches("D")) && ioptSub.matches("W") && ioptVer.matches("3"))
                {
                    if (mkAddrSearch != null && !mkAddrSearch.equals(""))
                    {
                        int chkLen = (ioptPage.matches("0")) ? 5 : 7; // 페이징 사용 여부에 따라 검사길이가 틀려짐
                        String[] arrMkAddrSearch = mkAddrSearch.split("[\u007C]");
                        
                        if (arrMkAddrSearch.length == chkLen)
                        {
                            String searchData = arrMkAddrSearch[4];
                            if (searchData.length() == 5 && searchData.matches("[0-9]{5}"))
                            {
                                ioptSub = "L"; // 통합검색에서 우편번호가 들어올 시 우편번호 검색으로 변환
                            }
                        }
                    }
                }
                
                if ((ioptMain.matches("5") || ioptMain.matches("D")) && ioptVer.matches("3"))
                {
                    if (FrontLikeUse == 1)
                    { // 앞 유사검색 사용
                        if (ioptSub.matches("W")) ioptSub = "w";
                        else if (ioptSub.matches("Y")) ioptSub = "y";
                    }
                }
                
                cvtType = ioptMain + ioptPage + ioptSub + ioptVer;
            }
        }
        
        return cvtType;
    }
    
    /**
     * @Method chkGviaInjection
     * @Description : sql인젝션 검사
     * @param txtType : InOutProtocol 헤더코드
     * @param mkAddrSearch : 검색데이터
     * @return boolean : sql인젝션 검사 성공여부(true:이상없음, false:인젝션의심내용포함)
     */
    public boolean chkGviaInjection(String txtType, String mkAddrSearch)
    {
        // 인젝션 검사 문자열 유형 ( --, /*, /**/, '=', 'and', 'or', ?, %, * )
        // String[] injectionCodes = { "--", "/[*].*", "/[*].*[*]/", "'(\\s)*=(\\s)*'", "'(\\s)*[aA][nN][dD](\\s)*'", "'(\\s)*[oO][rR](\\s)*'", "[?]", "[%]", "[*]" };
        // 인젝션 검사 문자열 유형 ( --, /*, /**/, '=', 'and', 'or', ?, % )
        String[] injectionCodes = { "--", "/[*].+", "/[*].*[*]/", "'(\\s)*=(\\s)*'", "'(\\s)*[aA][nN][dD](\\s)*'", "'(\\s)*[oO][rR](\\s)*'", "[?]", "[%]" };
        String allRegx = ".*"; // 모든문자열
        String chkMainCode = "[5D6E7]"; // sql 이용하는 검색에서만 활성화
        
        String ioptMain = ""; // 메인코드
        
        if (txtType != null && !txtType.equals(""))
        {
            ioptMain = txtType.substring(0, 1);
            
            if (ioptMain.matches(chkMainCode) && mkAddrSearch != null && !mkAddrSearch.equals(""))
            { // 검색일 경우 검사 활성화
                for (int i = 0; i < injectionCodes.length; i++)
                {
                    // outLog("** " + i + "_chkGviaInjectionIndexOf : " + mkAddrSearch.matches(allRegx + injectionCodes[i] + allRegx));
                    if (mkAddrSearch.matches(allRegx + injectionCodes[i] + allRegx))
                    { // 검사 유형과 일치하는 문자열 존재 시 검색 실패 처리
                        outLog("** " + i + "_chkGviaInjection : Injection Error Out");
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * @Description : 1차배열 빈 값 채우기(null 없음)
     * @param arr : 1차배열
     * @return String[]
     */
    public String[] setEmptyArray(String[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == null) arr[i] = "";
        }
        return arr;
    }
    
    /**
     * @Description : 2차배열 빈 값 채우기(null 없음)
     * @param arr : 2차배열
     * @return String[][]
     */
    public String[][] setEmptyArray(String[][] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            for (int j = 0; j < arr[i].length; j++)
            {
                if (arr[i][j] == null) arr[i][j] = "";
            }
        }
        return arr;
    }
    
    /**
     * @Method outLog
     * @data 2015.04.14
     * @Description : 로그 출력 함수
     * @param log : 출력할 내용
     * @return
     */
    public void outLog(String log)
    {
        // 로그 사용 처리 시
        if (logUse)
        {
            System.out.println(log);
        }
    }
    
    public String ShortMakeSido(String oriSido)
    {
        String cvtSido = "";
        if (oriSido != null && !oriSido.equals(""))
        {
            if (oriSido.equals("서울특별시")) cvtSido = "서울";
            else if (oriSido.equals("부산광역시")) cvtSido = "부산";
            else if (oriSido.equals("대구광역시")) cvtSido = "대구";
            else if (oriSido.equals("인천광역시")) cvtSido = "인천";
            else if (oriSido.equals("광주광역시")) cvtSido = "광주";
            else if (oriSido.equals("대전광역시")) cvtSido = "대전";
            else if (oriSido.equals("울산광역시")) cvtSido = "울산";
            else if (oriSido.equals("세종특별자치시")) cvtSido = "세종";
            else if (oriSido.equals("경기도")) cvtSido = "경기";
            else if (oriSido.equals("강원도")) cvtSido = "강원";
            else if (oriSido.equals("충청북도")) cvtSido = "충북";
            else if (oriSido.equals("충청남도")) cvtSido = "충남";
            else if (oriSido.equals("전라북도")) cvtSido = "전북";
            else if (oriSido.equals("전라남도")) cvtSido = "전남";
            else if (oriSido.equals("경상북도")) cvtSido = "경북";
            else if (oriSido.equals("경상남도")) cvtSido = "경남";
            else if (oriSido.equals("제주특별자치도")) cvtSido = "제주";
        }
        
        return cvtSido;
    }
    
    public String EasyMakeAdd1(String[] arrResult)
    {
        String add1 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String sido = arrResult[12];
            String gungu = arrResult[13];
            String dong = arrResult[14];
            String ri = arrResult[15];
            String roadnm = arrResult[33];
            String san = arrResult[16];
            String bunji = arrResult[17];
            String ho = arrResult[18];
            String buildinfo = arrResult[41];
            String roadbuildnm = arrResult[46];
            // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
            String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : "";
            String remain = arrResult[54];
            
            add1 = EasyMakeAdd1(sido, gungu, dong, ri, roadnm, san, bunji, ho, buildnm, remain);
        }
        
        return add1;
    }
    
    public String EasyMakeAdd1(GviaAddressVo inEntity)
    {
        String add1 = "";
        
        String sido = inEntity.getAd_sido();
        String gungu = inEntity.getAd_gungu();
        String dong = inEntity.getAd_dong();
        String ri = inEntity.getAd_ri();
        String roadnm = inEntity.getAd_ro_roadnm();
        String san = inEntity.getAd_san();
        String bunji = inEntity.getAd_mn();
        String ho = inEntity.getAd_sn();
        String buildinfo = inEntity.getAd_buildinfo();
        String roadbuildnm = inEntity.getAd_ro_buildnm();
        // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
        String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : "";
        String remain = inEntity.getAd_remain();
        
        add1 = EasyMakeAdd1(sido, gungu, dong, ri, roadnm, san, bunji, ho, buildnm, remain);
        
        return add1;
    }
    
    public String EasyMakeAdd1(String sido, String gungu, String dong, String ri, String roadnm, String underSan, String mnBunji, String snHo, String buildnm, String remain)
    {
        String adDistrict = MakeDistrict("J", sido, gungu, dong, ri); // 행정구역 만들기
        String bunjiho = MakeMainSubNumber("J", underSan, mnBunji, snHo, ZeroUse); // 건물번호,지번 정보 만들기
        String add1 = MakeAddress1(adDistrict, bunjiho, AdFrontNumber); // 지번주소1 만들기
        return add1;
    }
    
    public String EasyMakeAdd2(String[] arrResult)
    {
        String add2 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String sido = arrResult[12];
            String gungu = arrResult[13];
            String dong = arrResult[14];
            String ri = arrResult[15];
            String roadnm = arrResult[33];
            String san = arrResult[16];
            String bunji = arrResult[17];
            String ho = arrResult[18];
            String buildinfo = arrResult[41];
            String roadbuildnm = arrResult[46];
            // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
            // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : "";
            String buildnm = "";
            String remain = arrResult[54];
            
            add2 = EasyMakeAdd2(sido, gungu, dong, ri, roadnm, san, bunji, ho, buildnm, remain);
        }
        
        return add2;
    }
    
    public String EasyMakeAdd2(GviaAddressVo inEntity)
    {
        String add2 = "";
        
        String sido = inEntity.getAd_sido();
        String gungu = inEntity.getAd_gungu();
        String dong = inEntity.getAd_dong();
        String ri = inEntity.getAd_ri();
        String roadnm = inEntity.getAd_ro_roadnm();
        String san = inEntity.getAd_san();
        String bunji = inEntity.getAd_mn();
        String ho = inEntity.getAd_sn();
        String buildinfo = inEntity.getAd_buildinfo();
        String roadbuildnm = inEntity.getAd_ro_buildnm();
        // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
        // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : "";
        String buildnm = "";
        String remain = inEntity.getAd_remain();
        
        add2 = EasyMakeAdd2(sido, gungu, dong, ri, roadnm, san, bunji, ho, buildnm, remain);
        
        return add2;
    }
    
    public String EasyMakeAdd2(String sido, String gungu, String dong, String ri, String roadnm, String underSan, String mnBunji, String snHo, String buildnm, String remain)
    {
        String bunjiho = MakeMainSubNumber("J", underSan, mnBunji, snHo, ZeroUse); // 건물번호,지번 정보 만들기
        String add2 = MakeAddress2(bunjiho, buildnm, remain, AdFrontNumber, AdRemainRemove, AdCommaUse); // 지번주소2 만들기
        return add2;
    }
    
    public String EasyMakeRoAdd1(String[] arrResult)
    {
        String roadd1 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String sido = arrResult[12];
            String gungu = arrResult[13];
            String dong = arrResult[14];
            String ri = arrResult[15];
            String roadnm = arrResult[33];
            String under = arrResult[27];
            String buildmn = arrResult[28];
            String buildsn = arrResult[29];
            String buildinfo = (arrResult[41] != null && !arrResult[41].equals("")) ? arrResult[41] : "";
            String roadbuildnm = (arrResult[46] != null && !arrResult[46].equals("")) ? arrResult[46] : "";
            String buildnm = "";
            if (UserBuildFirst == 0) buildnm = roadbuildnm;
            else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
            String remain = arrResult[45];
            
            roadd1 = EasyMakeRoAdd1(sido, gungu, dong, ri, roadnm, under, buildmn, buildsn, buildnm, remain);
        }
        
        return roadd1;
    }
    
    public String EasyMakeRoAdd1(GviaAddressVo inEntity)
    {
        String roadd1 = "";
        
        String sido = inEntity.getAd_sido();
        String gungu = inEntity.getAd_gungu();
        String dong = inEntity.getAd_dong();
        String ri = inEntity.getAd_ri();
        String roadnm = inEntity.getAd_ro_roadnm();
        String under = inEntity.getAd_ro_under();
        String buildmn = inEntity.getAd_ro_buildmn();
        String buildsn = inEntity.getAd_ro_buildsn();
        String buildinfo = (inEntity.getAd_buildinfo() != null && !inEntity.getAd_buildinfo().equals("")) ? inEntity.getAd_buildinfo() : "";
        String roadbuildnm = (inEntity.getAd_ro_buildnm() != null && !inEntity.getAd_ro_buildnm().equals("")) ? inEntity.getAd_ro_buildnm() : "";
        String buildnm = "";
        if (UserBuildFirst == 0) buildnm = roadbuildnm;
        else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
        String remain = inEntity.getAd_remain();
        
        roadd1 = EasyMakeRoAdd1(sido, gungu, dong, ri, roadnm, under, buildmn, buildsn, buildnm, remain);
        
        return roadd1;
    }
    
    public String EasyMakeRoAdd1(String sido, String gungu, String dong, String ri, String roadnm, String underSan, String mnBunji, String snHo, String buildnm, String remain)
    {
        String roDistrict = MakeDistrict("R", sido, gungu, dong, ri); // 행정구역 만들기
        String buildmnsn = MakeMainSubNumber("R", underSan, mnBunji, snHo, ZeroUse); // 건물번호, 지번 정보 만들기
        String roadd1 = MakeRoadAddress1(roDistrict, roadnm, buildmnsn, RoBackNumber); // 도로명주소1 만들기
        return roadd1;
    }
    
    public String EasyMakeRoAdd2(String[] arrResult)
    {
        String roadd2 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String sido = arrResult[12];
            String gungu = arrResult[13];
            String dong = arrResult[14];
            String ri = arrResult[15];
            String roadnm = arrResult[33];
            String under = arrResult[27];
            String buildmn = arrResult[28];
            String buildsn = arrResult[29];
            String buildinfo = (arrResult[41] != null && !arrResult[41].equals("")) ? arrResult[41] : "";
            String roadbuildnm = (arrResult[46] != null && !arrResult[46].equals("")) ? arrResult[46] : "";
            String buildnm = "";
            if (UserBuildFirst == 0) buildnm = roadbuildnm;
            else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
            String remain = arrResult[45];
            
            roadd2 = EasyMakeRoAdd2(sido, gungu, dong, ri, roadnm, under, buildmn, buildsn, buildnm, remain);
        }
        
        return roadd2;
    }
    
    public String EasyMakeRoAdd2(GviaAddressVo inEntity)
    {
        String roadd2 = "";
        
        String sido = inEntity.getAd_sido();
        String gungu = inEntity.getAd_gungu();
        String dong = inEntity.getAd_dong();
        String ri = inEntity.getAd_ri();
        String roadnm = inEntity.getAd_ro_roadnm();
        String under = inEntity.getAd_ro_under();
        String buildmn = inEntity.getAd_ro_buildmn();
        String buildsn = inEntity.getAd_ro_buildsn();
        String buildinfo = (inEntity.getAd_buildinfo() != null && !inEntity.getAd_buildinfo().equals("")) ? inEntity.getAd_buildinfo() : "";
        String roadbuildnm = (inEntity.getAd_ro_buildnm() != null && !inEntity.getAd_ro_buildnm().equals("")) ? inEntity.getAd_ro_buildnm() : "";
        String buildnm = "";
        if (UserBuildFirst == 0) buildnm = roadbuildnm;
        else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
        String remain = inEntity.getAd_remain();
        
        roadd2 = EasyMakeRoAdd2(sido, gungu, dong, ri, roadnm, under, buildmn, buildsn, buildnm, remain);
        
        return roadd2;
    }
    
    public String EasyMakeRoAdd2(String sido, String gungu, String dong, String ri, String roadnm, String underSan, String mnBunji, String snHo, String buildnm, String remain)
    {
        String buildmnsn = MakeMainSubNumber("R", underSan, mnBunji, snHo, ZeroUse); // 건물번호, 지번 정보 만들기
        String roadd2 = MakeRoadAddress2(buildmnsn, buildnm, remain, RoBackNumber, RoBuildUse, RoRemainRemove, RoCommaUse); // 도로명주소2 만들기
        return roadd2;
    }
    
    public String EasyMakeRoRef(String[] arrResult)
    {
        return EasyMakeRoRef(RefMode, arrResult);
    }
    
    public String EasyMakeRoRef(int vRefMode, String[] arrResult)
    {
        String roref = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String bdong = arrResult[30];
            String ri = arrResult[15];
            String under = arrResult[27];
            String buildmn = arrResult[28];
            String buildsn = arrResult[29];
            String san = arrResult[16];
            String bunji = arrResult[17];
            String ho = arrResult[18];
            String buildinfo = (arrResult[41] != null && !arrResult[41].equals("")) ? arrResult[41] : "";
            String roadbuildnm = (arrResult[46] != null && !arrResult[46].equals("")) ? arrResult[46] : "";
            String buildnm = "";
            if (UserBuildFirst == 0) buildnm = roadbuildnm;
            else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
            
            if (vRefMode == 0) roref = EasyMakeRoRefBasic(vRefMode, bdong, ri, under, buildmn, buildsn, buildnm);
            else if (vRefMode == 1) roref = EasyMakeRoRefBasic(vRefMode, bdong, ri, under, buildmn, buildsn, buildnm);
            else if (vRefMode == 2) roref = EasyMakeRoRefCustom1(vRefMode, bdong, ri, san, bunji, ho, buildnm);
            else if (vRefMode == 3) roref = EasyMakeRoRefCustom1(vRefMode, bdong, ri, san, bunji, ho, buildnm);
        }
        
        return roref;
    }
    
    public String EasyMakeRoRef(GviaAddressVo inEntity)
    {
        return EasyMakeRoRef(RefMode, inEntity);
    }
    
    public String EasyMakeRoRef(int vRefMode, GviaAddressVo inEntity)
    {
        String roref = "";
        
        String bdong = inEntity.getAd_bdongnm();
        String ri = inEntity.getAd_ri();
        String under = inEntity.getAd_ro_under();
        String buildmn = inEntity.getAd_ro_buildmn();
        String buildsn = inEntity.getAd_ro_buildsn();
        String san = inEntity.getAd_san();
        String bunji = inEntity.getAd_mn();
        String ho = inEntity.getAd_sn();
        String buildinfo = (inEntity.getAd_buildinfo() != null && !inEntity.getAd_buildinfo().equals("")) ? inEntity.getAd_buildinfo() : "";
        String roadbuildnm = (inEntity.getAd_ro_buildnm() != null && !inEntity.getAd_ro_buildnm().equals("")) ? inEntity.getAd_ro_buildnm() : "";
        String buildnm = "";
        if (UserBuildFirst == 0) buildnm = roadbuildnm;
        else if (UserBuildFirst == 1) buildnm = !buildinfo.equals("") ? buildinfo : roadbuildnm;
        
        if (vRefMode == 0) roref = EasyMakeRoRefBasic(vRefMode, bdong, ri, under, buildmn, buildsn, buildnm);
        else if (vRefMode == 1) roref = EasyMakeRoRefBasic(vRefMode, bdong, ri, under, buildmn, buildsn, buildnm);
        else if (vRefMode == 2) roref = EasyMakeRoRefCustom1(vRefMode, bdong, ri, san, bunji, ho, buildnm);
        else if (vRefMode == 3) roref = EasyMakeRoRefCustom1(vRefMode, bdong, ri, san, bunji, ho, buildnm);
        
        return roref;
    }
    
    public String EasyMakeRoRefBasic(int vRefMode, String bdong, String ri, String under, String buildmn, String buildsn, String buildnm)
    {
        String buildmnsn = MakeMainSubNumber("R", under, buildmn, buildsn, ZeroUse); // 건물번호, 지번 정보 만들기
        String roref = "";
        if (vRefMode == 0) roref = MakeRoRef(vRefMode, bdong, ri, buildmnsn, buildnm);
        else if (vRefMode == 1) roref = MakeRoRef(vRefMode, bdong, ri, buildmnsn, buildnm);
        
        return roref;
    }
    
    public String EasyMakeRoRefCustom1(int vRefMode, String bdong, String ri, String san, String bunji, String ho, String buildnm)
    {
        String bunjiho = MakeMainSubNumber("J", san, bunji, ho, ZeroUse); // 건물번호, 지번 정보 만들기
        String roref = "";
        if (vRefMode == 2) roref = MakeRoRef1(vRefMode, bdong, ri, bunjiho, buildnm);
        else if (vRefMode == 3) roref = MakeRoRef1(vRefMode, bdong, ri, bunjiho, buildnm);
        
        return roref;
    }
    
    public String MakeRoRef(int vRefMode, String bdong, String ri, String buildmnsn, String buildnm)
    {
        String outRoRef = "";
        String tmpRoRef = "";
        
        if (bdong == null) bdong = "";
        if (ri == null) ri = "";
        if (buildnm == null) buildnm = "";
        if (buildmnsn == null) buildmnsn = "";
        
        if (!buildmnsn.equals(""))
        {
            if (vRefMode == 0)
            {
                if (ri.equals(""))
                { // 리 명칭이 없다면
                    if (!bdong.equals("")) tmpRoRef = bdong;
                    if (!tmpRoRef.equals("")) if (!buildnm.equals("")) tmpRoRef = tmpRoRef + ", " + buildnm;
                }
                else
                {
                    tmpRoRef = buildnm;
                }
            }
            else if (vRefMode == 1)
            {
                if (ri.equals(""))
                { // 리 명칭이 없다면
                    if (!bdong.equals("")) tmpRoRef = bdong; // 법정동명
                    if (!tmpRoRef.equals("")) if (!buildnm.equals("")) tmpRoRef = tmpRoRef + ", " + buildnm; // 도로명건물명
                }
                else
                {
                    tmpRoRef = ri; // 리
                    if (!buildnm.equals("")) tmpRoRef = tmpRoRef + ", " + buildnm; // 도로명건물명
                }
            }
        }
        
        outRoRef = tmpRoRef.trim();
        return outRoRef;
    }
    
    public String MakeRoRef1(int vRefMode, String bdong, String ri, String bunjiho, String buildnm)
    {
        String outRoRef = "";
        String tmpRoRef = "";
        
        if (bdong == null) bdong = "";
        if (ri == null) ri = "";
        if (buildnm == null) buildnm = "";
        if (bunjiho == null) bunjiho = "";
        
        if (vRefMode == 2)
        {
            if (!bunjiho.equals(""))
            { // 주번지 존재 시
                if (ri.equals(""))
                {
                    if (!bdong.equals("")) tmpRoRef = bdong + " " + bunjiho; // 법정동명
                    else tmpRoRef = bunjiho;
                }
                else tmpRoRef = ri + " " + bunjiho; // 리
            }
        }
        else if (vRefMode == 3)
        {
            if (!ri.equals(""))
            {
                if (!buildnm.equals(""))
                {
                    tmpRoRef = ri + ", " + buildnm;
                }
                else
                {
                    if (!bunjiho.equals("")) tmpRoRef = ri + " " + bunjiho;
                }
            }
            else
            {
                if (!bdong.equals("") && !buildnm.equals(""))
                {
                    tmpRoRef = bdong + ", " + buildnm;
                }
                else
                {
                    if (!bunjiho.equals(""))
                    {
                        if (!bdong.equals("")) tmpRoRef = bdong + " " + bunjiho;
                        else tmpRoRef = bunjiho;
                    }
                }
            }
        }
        
        outRoRef = tmpRoRef.trim();
        return outRoRef;
    }
    
    public String MakeAddress1(String adDistrict, String bunjiho, int vAdFrontNumber)
    {
        String outJibunAdd1 = "";
        String tmpJibunAdd1 = "";
        boolean chk = false;
        
        if (adDistrict != null && !adDistrict.equals(""))
        {
            tmpJibunAdd1 = adDistrict;
            
            if (vAdFrontNumber == 0)
            {
                chk = true;
            }
            else if (vAdFrontNumber == 1)
            {
                tmpJibunAdd1 = tmpJibunAdd1 + " " + bunjiho;
                chk = true;
            }
        }
        
        if (chk) outJibunAdd1 = tmpJibunAdd1.trim();
        return outJibunAdd1;
    }
    
    public String MakeAddress2(String bunjiho, String buildnm, String remain, int vAdFrontNumber, int vAdRemainRemove)
    {
        return MakeAddress2(bunjiho, buildnm, remain, vAdFrontNumber, vAdRemainRemove, 0);
    }
    
    public String MakeAddress2(String bunjiho, String buildnm, String remain, int vAdFrontNumber, int vAdRemainRemove, int vAdCommaUse)
    {
        String outJibunAdd2 = "";
        String tmpJibunAdd2 = "";
        String tmpRemain = "";
        boolean chk = false;
        
        if (remain != null && !remain.equals("")) tmpRemain = remain;
        
        if ((bunjiho != null && !bunjiho.equals("")) || !tmpRemain.equals(""))
        {
            if (vAdFrontNumber == 0)
            {
                tmpJibunAdd2 = bunjiho;
                if (buildnm != null && !buildnm.equals(""))
                {
                    if (vAdCommaUse == 1) tmpJibunAdd2 = tmpJibunAdd2 + ", " + buildnm;
                    else tmpJibunAdd2 = tmpJibunAdd2 + " " + buildnm;
                    
                    if (vAdRemainRemove == 0) tmpJibunAdd2 = tmpJibunAdd2 + " " + tmpRemain;
                }
                else
                {
                    if (vAdRemainRemove == 0)
                    {
                        if (!tmpRemain.equals(""))
                        {
                            if (vAdCommaUse == 1) tmpJibunAdd2 = tmpJibunAdd2 + ", " + tmpRemain;
                            else tmpJibunAdd2 = tmpJibunAdd2 + " " + tmpRemain;
                        }
                    }
                }
                
                chk = true;
            }
            else if (vAdFrontNumber == 1)
            {
                if (buildnm != null)
                {
                    tmpJibunAdd2 = buildnm;
                    if (vAdRemainRemove == 0)
                    {
                        if (tmpJibunAdd2.equals("")) tmpJibunAdd2 = tmpRemain;
                        else tmpJibunAdd2 = tmpJibunAdd2 + " " + tmpRemain;
                    }
                    chk = true;
                }
            }
        }
        
        if (chk) outJibunAdd2 = tmpJibunAdd2.trim();
        return outJibunAdd2;
    }
    
    public String MakeRoadAddress1(String roDistrict, String roadnm, String buildmnsn, int vRoBackNumber)
    {
        String outRoadAdd1 = "";
        String tmpRoadAdd1 = "";
        boolean chk = false;
        
        if (roDistrict != null && !roDistrict.equals(""))
        {
            tmpRoadAdd1 = roDistrict;
            if (roadnm != null && !roadnm.equals(""))
            {
                tmpRoadAdd1 = tmpRoadAdd1 + " " + roadnm;
                chk = true;
                if (vRoBackNumber == 0)
                {
                    if (buildmnsn != null && !buildmnsn.equals(""))
                    {
                        tmpRoadAdd1 = tmpRoadAdd1 + " " + buildmnsn;
                    }
                }
            }
        }
        
        if (chk) outRoadAdd1 = tmpRoadAdd1.trim();
        return outRoadAdd1;
    }
    
    public String MakeRoadAddress2(String buildmnsn, String buildnm, String remain, int vRoBackNumber, int vRoBuildUse, int vRoRemainRemove)
    {
        return MakeRoadAddress2(buildmnsn, buildnm, remain, vRoBackNumber, vRoBuildUse, vRoRemainRemove, 0);
    }
    
    public String MakeRoadAddress2(String buildmnsn, String buildnm, String remain, int vRoBackNumber, int vRoBuildUse, int vRoRemainRemove, int vRoCommaUse)
    {
        String outRoadAdd2 = "";
        String tmpRoadAdd2 = "";
        String tmpBuildnm = "";
        String tmpRemain = "";
        boolean chk = false;
        
        if (buildnm != null && !buildnm.equals("")) tmpBuildnm = buildnm;
        if (remain != null && !remain.equals("")) tmpRemain = remain;
        
        if ((buildmnsn != null && !buildmnsn.equals("")) || !tmpRemain.equals(""))
        {
            if (vRoBackNumber == 0)
            {
                if (vRoBuildUse == 0)
                {
                    if (vRoRemainRemove == 0) tmpRoadAdd2 = tmpRemain;
                }
                else if (vRoBuildUse == 1)
                {
                    tmpRoadAdd2 = tmpBuildnm;
                    if (vRoRemainRemove == 0)
                    {
                        if (!tmpRoadAdd2.equals(""))
                        {
                            if (!tmpRemain.equals("")) tmpRoadAdd2 = tmpRoadAdd2 + " " + tmpRemain;
                        }
                        else
                        {
                            tmpRoadAdd2 = tmpRemain;
                        }
                    }
                }
                chk = true;
            }
            else if (vRoBackNumber == 1)
            {
                tmpRoadAdd2 = buildmnsn;
                if (vRoBuildUse == 0)
                {
                    if (vRoRemainRemove == 0)
                    {
                        if (!tmpRemain.equals(""))
                        {
                            if (vRoCommaUse == 1) tmpRoadAdd2 = tmpRoadAdd2 + ", " + tmpRemain;
                            else tmpRoadAdd2 = tmpRoadAdd2 + " " + tmpRemain;
                        }
                    }
                }
                else if (vRoBuildUse == 1)
                {
                    if (!tmpRoadAdd2.equals(""))
                    {
                        if (!tmpBuildnm.equals(""))
                        {
                            if (vRoCommaUse == 1) tmpRoadAdd2 = tmpRoadAdd2 + ", " + tmpBuildnm;
                            else tmpRoadAdd2 = tmpRoadAdd2 + " " + tmpBuildnm;
                        }
                    }
                    else
                    {
                        tmpRoadAdd2 = tmpBuildnm;
                    }
                    
                    if (vRoRemainRemove == 0)
                    {
                        if (!tmpRemain.equals(""))
                        {
                            tmpRoadAdd2 = tmpRoadAdd2 + " " + tmpRemain;
                        }
                    }
                }
                chk = true;
            }
        }
        
        if (chk) outRoadAdd2 = tmpRoadAdd2.trim();
        return outRoadAdd2;
    }
    
    public String MakeDistrict(String mode, String sido, String gungu, String dong, String ri)
    {
        return MakeDistrict(mode, sido, gungu, dong, ri, "");
    }
    
    public String MakeDistrict(String mode, String sido, String gungu, String dong, String ri, String roadnm)
    {
        String outDistrict = "";
        String tmpDistrict = "";
        
        if (sido != null && !sido.equals(""))
        {
            tmpDistrict = sido;
            if (gungu != null && !gungu.equals(""))
            {
                tmpDistrict = tmpDistrict + " " + gungu;
            }
            
            if (mode.equals("R"))
            {
                if (ri != null && !ri.equals("")) tmpDistrict = tmpDistrict + " " + dong;
                else if (dong != null && !dong.equals("") && (dong.matches(".*읍$|.*면$"))) tmpDistrict = tmpDistrict + " " + dong;
                if (roadnm != null && !roadnm.equals("")) tmpDistrict = tmpDistrict + " " + roadnm;
            }
            else if (mode.equals("J"))
            {
                if (dong != null && !dong.equals("")) tmpDistrict = tmpDistrict + " " + dong;
                if (ri != null && !ri.equals("")) tmpDistrict = tmpDistrict + " " + ri;
            }
        }
        
        outDistrict = tmpDistrict.trim();
        return outDistrict;
    }
    
    public String MakeMainSubNumber(String mode, String underSan, String mnBunji, String snHo, int vZeroUse)
    {
        String outMSNumber = "";
        String tmpMSNumber = "";
        
        if (mnBunji != null && !mnBunji.equals("") && !mnBunji.equals("0"))
        {
            if (mode.equals("R"))
            {
                if (underSan.equals("지하") || underSan.equals("공중")) tmpMSNumber = underSan;
                else if (underSan.equals("1")) tmpMSNumber = "지하";
                else if (underSan.equals("2")) tmpMSNumber = "공중";
            }
            else if (mode.equals("J"))
            {
                if (underSan.equals("산")) tmpMSNumber = underSan;
                else if (underSan.equals("1")) tmpMSNumber = "산";
            }
            
            if (!tmpMSNumber.equals("")) tmpMSNumber = tmpMSNumber + " ";
            tmpMSNumber = tmpMSNumber + mnBunji;
            
            if (vZeroUse == 0)
            {
                if (snHo != null && !snHo.equals("") && !snHo.equals("0")) tmpMSNumber = tmpMSNumber + "-" + snHo;
            }
            else if (vZeroUse == 1)
            {
                if (snHo != null && !snHo.equals("") && !snHo.equals("0")) tmpMSNumber = tmpMSNumber + "-" + snHo;
                else tmpMSNumber = tmpMSNumber + "-0";
            }
        }
        
        outMSNumber = tmpMSNumber.trim();
        return outMSNumber;
    }
    
    public String MakeRemain(String builddong, String buildho, String buildstair, String noparsing)
    {
        String outRemain = "";
        String tmpRemain = "";
        
        if (!noparsing.equals("")) tmpRemain = noparsing;
        if (!tmpRemain.equals(""))
        {
            if (!builddong.equals("")) tmpRemain = tmpRemain + " " + builddong + "동";
            if (!buildho.equals("")) tmpRemain = tmpRemain + " " + buildho + "호";
            if (!buildstair.equals("")) tmpRemain = tmpRemain + " " + buildstair + "층";
        }
        else
        {
            if (!builddong.equals("")) tmpRemain = builddong + "동";
            if (!tmpRemain.equals(""))
            {
                if (!buildho.equals(""))
                {
                    tmpRemain = tmpRemain + " " + buildho + "호";
                    if (!buildstair.equals("")) tmpRemain = tmpRemain + " " + buildstair + "층";
                }
                else
                {
                    if (!buildstair.equals("")) tmpRemain = tmpRemain + " " + buildstair + "층";
                }
            }
            else
            {
                if (!buildho.equals("")) tmpRemain = buildho + "호";
                if (!tmpRemain.equals(""))
                {
                    if (!buildstair.equals("")) tmpRemain = tmpRemain + " " + buildstair + "층";
                }
                else
                {
                    if (!buildstair.equals("")) tmpRemain = buildstair + "층";
                }
            }
        }
        
        outRemain = tmpRemain.trim();
        return outRemain;
    }
    
    // //////////////////gvia_common end ////////////////////
    
    // ////////////////// gvia_vo start ////////////////////
    
    /**
     * @param strPost : 우편번호
     * @param strAddr : 주소정보
     * @return GviaAddressMultVo
     */
    public GviaAddressMultVo AddressVo(String strPost, String strAddr)
    {
        GviaAddressMultVo entityList = new GviaAddressMultVo();
        entityList = AddressVo(strPost, strAddr, 0, 0);
        return entityList;
    }
    
    /**
     * @param strPost : 우편번호
     * @param strAddr : 주소정보
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @return GviaAddressMultVo
     */
    public GviaAddressMultVo AddressVo(String strPost, String strAddr, int engUse)
    {
        GviaAddressMultVo entityList = new GviaAddressMultVo();
        entityList = AddressVo(strPost, strAddr, engUse, 0);
        return entityList;
    }
    
    /**
     * @param strPost : 우편번호
     * @param strAddr : 주소정보
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @param nSearchFlag : 정제멀티사용여부(0:미사용, 1:사용)
     * @return GviaAddressMultVo
     */
    public GviaAddressMultVo AddressVo(String strPost, String strAddr, int engUse, int nSearchFlag)
    {
        GviaAddressMultVo entityList = new GviaAddressMultVo();
        
        String[][] arrResult = null;
        String txtType = "";
        
        String ioptMain = "3";
        String ioptPage = "0";
        String ioptSub = "0";
        String ioptVer = "2";
        
        if (strPost == null || (strPost.length() != 6 && strPost.length() != 5)) strPost = "";
        if (strAddr == null) strAddr = "";
        
        if (engUse == 1) ioptMain = "C";
        else ioptMain = "3";
        
        if (strPost.equals(""))
        {
            if (nSearchFlag == 1) ioptSub = "A";
            else if (strPost.length() == 5) ioptSub = "0";
            else ioptSub = "0";
        }
        else
        {
            if (nSearchFlag == 1) ioptSub = "B";
            else if (strPost.length() == 5) ioptSub = "1";
            else ioptSub = "1";
        }
        
        txtType = ioptMain + ioptPage + ioptSub + ioptVer;
        
        try
        {
            if (SocketConnect())
            {
                arrResult = Address2(txtType, strPost, strAddr, "");
                
                if (arrResult.length < 1)
                { // 주소정제가 실패하더라도 결과는 1개이상 나와야함
                    entityList.setErrorCode(ErrorCode.ERROR_EXECUTE_FAIL); // 요청실패
                    return entityList;
                }
                
                // arrResult[0][2] 의 결과가 "A-행정구역" 혹은 "B-건물"의 경우 정제멀티이다.
                if (arrResult[0][2].equals("A") || arrResult[0][2].equals("B"))
                {
                    entityList.setResult(1); // 정제멀티
                    for (int i = 1; i < arrResult.length; i++)
                    {
                        GviaAddressToSearchVo entitySearchList = new GviaAddressToSearchVo();
                        
                        entitySearchList.setFlag(arrResult[0][2]);
                        entitySearchList.setDonggubun(arrResult[0][3]);
                        entitySearchList.setResultcnt(arrResult[0][4]);
                        entitySearchList.setEtcAddr(arrResult[0][5]);
                        
                        entitySearchList.setPostno(arrResult[i][0]);
                        entitySearchList.setsido(arrResult[i][1]);
                        entitySearchList.setGungu(arrResult[i][2]);
                        entitySearchList.setDong(arrResult[i][3]);
                        entitySearchList.setRi(arrResult[i][4]);
                        entitySearchList.setRoadnm(arrResult[i][5]);
                        entitySearchList.setBdongnm(arrResult[i][7]);
                        entitySearchList.setHdongnm(arrResult[i][6]);
                        entitySearchList.setSan_under(arrResult[i][8]);
                        entitySearchList.setMn(arrResult[i][9]);
                        entitySearchList.setSn(arrResult[i][10]);
                        entitySearchList.setBuildnm(arrResult[i][11]);
                        
                        entityList.addSearchEntity(entitySearchList);
                    }
                }
                else
                {
                    for (int i = 0; i < arrResult.length; i++)
                    {
                        GviaAddressVo entityAddrList = new GviaAddressVo();
                        
                        entityAddrList.setAd_jr(arrResult[i][0]); // 지번/새주소구분(입력값기준, J:지번주소, R:도로명주소)
                        entityAddrList.setAd_resultflag(arrResult[i][1]); // FLAG1 (0:정상, 1:불능, 2:매칭실패, 3:사서함)
                        entityAddrList.setAd_resultcd(arrResult[i][2]); // FLAG2 (변환코드), G-VIA_InOut Protocol.doc 참조
                        
                        entityAddrList.setAd_sido(arrResult[i][12]);
                        entityAddrList.setAd_gungu(arrResult[i][13]);
                        entityAddrList.setAd_dong(arrResult[i][14]);
                        entityAddrList.setAd_ri(arrResult[i][15]);
                        entityAddrList.setAd_sancd(arrResult[i][16]); // 산구분
                        
                        if (entityAddrList.getAd_sancd().equals("1")) entityAddrList.setAd_san("산"); // 산여부
                        else entityAddrList.setAd_san(""); // 산여부
                        
                        entityAddrList.setAd_mn(arrResult[i][17]); // 주번지
                        entityAddrList.setAd_sn(arrResult[i][18]); // 부번지
                        
                        entityAddrList.setAd_buildinfo(arrResult[i][41]);
                        entityAddrList.setAd_builddong(arrResult[i][42]);
                        entityAddrList.setAd_buildho(arrResult[i][43]);
                        entityAddrList.setAd_buildstair(arrResult[i][44]);
                        entityAddrList.setAd_remain(arrResult[i][45]);
                        
                        if ((arrResult[i][40]).equals("N") || (arrResult[i][40]).equals("U") || (arrResult[i][40]).equals("S"))
                        { // 입력 - 도로명주소, 사서함
                            entityAddrList.setAd_postno(arrResult[i][7]);
                            entityAddrList.setAd_postseq(arrResult[i][8]);
                            entityAddrList.setAd_add1(arrResult[i][9]);
                            entityAddrList.setAd_add2(arrResult[i][10]);
                            entityAddrList.setAd_ro_postno(arrResult[i][3]);
                            entityAddrList.setAd_ro_postseq(arrResult[i][4]);
                            entityAddrList.setAd_ro_add1(arrResult[i][5]);
                            entityAddrList.setAd_ro_add2(arrResult[i][6]);
                        }
                        else
                        { // 입력 - 지번주소
                            entityAddrList.setAd_postno(arrResult[i][3]);
                            entityAddrList.setAd_postseq(arrResult[i][4]);
                            entityAddrList.setAd_add1(arrResult[i][5]);
                            entityAddrList.setAd_add2(arrResult[i][6]);
                            entityAddrList.setAd_ro_postno(arrResult[i][7]);
                            entityAddrList.setAd_ro_postseq(arrResult[i][8]);
                            entityAddrList.setAd_ro_add1(arrResult[i][9]);
                            entityAddrList.setAd_ro_add2(arrResult[i][10]);
                        }
                        
                        entityAddrList.setAd_bsizonno(arrResult[i][35]);
                        entityAddrList.setAd_ro_bsizonno(arrResult[i][38]);
                        entityAddrList.setAd_ro_ref(arrResult[i][24]); // 참조주소 기본형태(법정동, 도로명건물명)
                        
                        entityAddrList.setAd_dongcd(arrResult[i][40]); // 입력동구분
                        
                        entityAddrList.setAd_bdongcd(arrResult[i][22]);
                        entityAddrList.setAd_hdongcd(arrResult[i][23]);
                        entityAddrList.setAd_bdongnm(arrResult[i][30]);
                        entityAddrList.setAd_hdongnm(arrResult[i][31]);
                        entityAddrList.setAd_pnucd(arrResult[i][32]);
                        entityAddrList.setAd_cx(arrResult[i][19]);
                        entityAddrList.setAd_cy(arrResult[i][20]);
                        entityAddrList.setAd_lev(arrResult[i][21]);
                        
                        entityAddrList.setAd_ro_roadnm(arrResult[i][33]);
                        entityAddrList.setAd_ro_roadcd(arrResult[i][25]);
                        entityAddrList.setAd_ro_dongseq(arrResult[i][26]);
                        entityAddrList.setAd_ro_undercd(arrResult[i][27]);
                        
                        if (entityAddrList.getAd_ro_undercd().equals("1")) entityAddrList.setAd_ro_under("지하");
                        else if (entityAddrList.getAd_ro_undercd().equals("2")) entityAddrList.setAd_ro_under("공중");
                        else entityAddrList.setAd_ro_under("");
                        
                        entityAddrList.setAd_ro_buildmn(arrResult[i][28]);
                        entityAddrList.setAd_ro_buildsn(arrResult[i][29]);
                        entityAddrList.setAd_ro_buildnm(arrResult[i][46]);
                        entityAddrList.setAd_ro_builddtail(arrResult[i][47]);
                        entityAddrList.setAd_ro_buildmgno(arrResult[i][11]);
                        entityAddrList.setAd_ro_pnu(arrResult[i][48]);
                        entityAddrList.setAd_ro_cx(arrResult[i][61]);
                        entityAddrList.setAd_ro_cy(arrResult[i][62]);
                        
                        entityAddrList.setAd_reprematching(arrResult[i][49]);
                        entityAddrList.setAd_apathouse(arrResult[i][50]);
                        entityAddrList.setAd_annexbuild(arrResult[i][51]);
                        entityAddrList.setAd_annexinfo(arrResult[i][52]);
                        entityAddrList.setAd_addrlev(arrResult[i][56]);
                        entityAddrList.setAd_postlev(arrResult[i][57]);
                        entityAddrList.setAd_posttype(arrResult[i][58]);
                        entityAddrList.setAd_errcd(arrResult[i][59]);
                        entityAddrList.setAd_datachgcd(arrResult[i][60]);
                        entityAddrList.setAd_newpostlev(arrResult[i][55]);
                        
                        if (engUse == 1)
                        {
                            entityAddrList.setAd_eng_sido(arrResult[i][64]);
                            entityAddrList.setAd_eng_gungu(arrResult[i][65]);
                            entityAddrList.setAd_eng_dong(arrResult[i][66]);
                            entityAddrList.setAd_eng_ri(arrResult[i][67]);
                            // entityAddrList.setAd_eng_bd(arrResult[i][68]);
                            entityAddrList.setAd_eng_roadnm(arrResult[i][69]);
                            // entityAddrList.setAd_eng_sggbd(arrResult[i][70]);
                        }
                        
                        entityAddrList.setAd_ro_totaladdcnt("1");
                        entityAddrList.setAd_ro_repeatcnt(Integer.toString(arrResult.length));
                        
                        // 참조주소 확장 s
                        String tmpRef2 = "";
                        String tmpRef3 = "";
                        String tmpRef4 = "";
                        
                        // tmpRef2 생성s
                        if (!arrResult[i][28].equals("") && !arrResult[i][28].equals("0"))
                        { // 건물본번 존재 시
                            if (arrResult[i][15].equals(""))
                            { // 리 명칭이 없다면
                                tmpRef2 = arrResult[i][30]; // 법정동명
                                if (!arrResult[i][41].equals("")) tmpRef2 = !tmpRef2.equals("") ? tmpRef2 + ", " + arrResult[i][41] : arrResult[i][41]; // 빌딩매칭정보
                                else if (!arrResult[i][46].equals("")) tmpRef2 = !tmpRef2.equals("") ? tmpRef2 + ", " + arrResult[i][46] : arrResult[i][46]; // 도로명건물명
                            }
                            else
                            {
                                tmpRef2 = arrResult[i][15]; // 리
                                if (!arrResult[i][41].equals("")) tmpRef2 = tmpRef2 + ", " + arrResult[i][41]; // 빌딩매칭정보
                                else if (!arrResult[i][46].equals("")) tmpRef2 = tmpRef2 + ", " + arrResult[i][46]; // 도로명건물명
                            }
                        }
                        // tmpRef2 생성e
                        
                        // tmpRef3, tmpRef4 생성s
                        if (!arrResult[i][17].equals("") && !arrResult[i][17].equals("0"))
                        { // 주번지 존재 시
                            if (arrResult[i][15].equals(""))
                            { // 리 명칭이 없다면
                                tmpRef3 = arrResult[i][30]; // 법정동명
                                if (arrResult[i][16].equals("1")) tmpRef3 = tmpRef3 + " 산"; // 산 정보
                                    
                                tmpRef3 = tmpRef3 + " " + arrResult[i][17]; // 주번지
                                if (!arrResult[i][18].equals("") && !arrResult[i][18].equals("0")) tmpRef3 = tmpRef3 + "-" + arrResult[i][18]; // 부번지
                                    
                                tmpRef4 = tmpRef3;
                                if (!arrResult[i][41].equals("")) tmpRef4 = tmpRef4 + " " + arrResult[i][41]; // 빌딩매칭정보
                                else if (!arrResult[i][46].equals("")) tmpRef4 = tmpRef4 + " " + arrResult[i][46]; // 도로명건물명
                            }
                            else
                            {
                                tmpRef3 = arrResult[i][15]; // 리
                                if (arrResult[i][16].equals("1")) tmpRef3 = tmpRef3 + " 산"; // 산 정보
                                    
                                tmpRef3 = tmpRef3 + " " + arrResult[i][17]; // 주번지
                                if (!arrResult[i][18].equals("") && !arrResult[i][18].equals("0")) tmpRef3 = tmpRef3 + "-" + arrResult[i][18]; // 부번지
                                    
                                tmpRef4 = tmpRef3;
                                if (!arrResult[i][41].equals("")) tmpRef4 = tmpRef4 + " " + arrResult[i][41]; // 빌딩매칭정보
                                else if (!arrResult[i][46].equals("")) tmpRef4 = tmpRef4 + " " + arrResult[i][46]; // 도로명건물명
                            }
                        }
                        // tmpRef3, tmpRef4 생성e
                        
                        entityAddrList.setAd_ro_ref2(tmpRef2); // 참조주소 형태2(법정동/리, 도로명건물명)
                        entityAddrList.setAd_ro_ref3(tmpRef3); // 참조주소 형태3(법정동/리 산+번지+호)
                        entityAddrList.setAd_ro_ref4(tmpRef4); // 참조주소 형태4(법정동/리 산+번지+호 건물명)
                        // 참조주소 확장 e
                        
                        entityList.addAddrEntity(entityAddrList);
                    }
                    
                    // 20150807 금칙어 관련 추가 s
                    if (arrResult[0][53] != null && !arrResult[0][53].equals(""))
                    {
                        String[] prohibitChkColumn = arrResult[0][53].split("[\u007C]");
                        String[] prohibitWord = null;
                        if (prohibitChkColumn[0].equals("금칙어"))
                        {
                            if (prohibitChkColumn.length > 1)
                            {
                                prohibitWord = setEmptyArray(new String[prohibitChkColumn.length - 1]);
                                for (int j = 1; j < prohibitChkColumn.length; j++)
                                {
                                    prohibitWord[j - 1] = prohibitChkColumn[j];
                                }
                                
                                entityList.setIsProhibit(1);
                                entityList.setProhibitWord(prohibitWord);
                            }
                        }
                    }
                    // 20150807 금칙어 관련 추가 e
                }
                
                entityList.setErrorCode(ErrorCode.ERROR_SUCCESS); // 성공
            }
            else
            {
                entityList.setErrorCode(ErrorCode.ERROR_SOCKET_CONNECT); // 연결 실패
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            outLog("LinkGVIA > addressVo 오류");
        }
        finally
        {
            SocketClose();
        }
        
        return entityList;
    }
    
    /**
     * 행정구역 검색(페이징 미사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색코드
     * @return
     */
    public GviaDistrictMultVo DistrictSearchVo(String subCode, String txtSearch)
    {
        GviaDistrictMultVo entityList = new GviaDistrictMultVo();
        entityList = DistrictSearchVo(subCode, txtSearch, 0, 0, 0);
        return entityList;
    }
    
    /**
     * 행정구역 검색(페이징 미사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색코드
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @return
     */
    public GviaDistrictMultVo DistrictSearchVo(String subCode, String txtSearch, int engUse)
    {
        GviaDistrictMultVo entityList = new GviaDistrictMultVo();
        entityList = DistrictSearchVo(subCode, txtSearch, 0, 0, engUse);
        return entityList;
    }
    
    /**
     * 행정구역 검색(페이징 사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색코드
     * @param pageRecord : 페이지 시작 번호
     * @param outCnt : 한번에 출력하는 수량
     * @return
     */
    public GviaDistrictMultVo DistrictSearchVo(String subCode, String txtSearch, int pageRecord, int outCnt)
    {
        GviaDistrictMultVo entityList = new GviaDistrictMultVo();
        entityList = DistrictSearchVo(subCode, txtSearch, pageRecord, outCnt, 0);
        return entityList;
    }
    
    /**
     * 행정구역 검색(페이징 사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색코드
     * @param pageRecord : 페이지 시작 번호
     * @param outCnt : 한번에 출력하는 수량
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @return
     */
    public GviaDistrictMultVo DistrictSearchVo(String subCode, String txtSearch, int pageRecord, int outCnt, int engUse)
    {
        GviaDistrictMultVo entityList = new GviaDistrictMultVo();
        
        String txtType = "";
        
        String ioptMain = "6";
        String ioptPage = "1";
        String ioptSub = "0";
        String ioptVer = "2";
        
        String txtPage1 = "";
        String txtPage2 = "";
        
        String districtSubCodeList = "[ABCDE01]"; // 행정구역 요청 sub코드 리스트
        
        if (engUse == 1) ioptMain = "E";
        else ioptMain = "6";
        
        if (subCode == null || subCode.length() != 1 || !subCode.matches(districtSubCodeList))
        {
            entityList.setErrorCode(ErrorCode.ERROR_PACKET_HEADER_UNKNOWN);
            return entityList;
        }
        else
        {
            ioptSub = subCode;
            
            if (pageRecord < 0) pageRecord = 0;
            if (outCnt < 0) outCnt = 0;
            if (pageRecord == 0 || outCnt == 0) ioptPage = "0";
            
            if (ioptPage.equals("1"))
            {
                pageRecord = 1 + (outCnt * (pageRecord - 1));
            }
            
            txtPage1 = Integer.toString(pageRecord);
            txtPage2 = Integer.toString(outCnt);
            
            txtType = ioptMain + ioptPage + ioptSub + ioptVer;
        }
        
        try
        {
            if (SocketConnect())
            {
                String mkAddrSearch = MakeGviaInData(txtType, txtSearch, txtPage1, txtPage2);
                String ret = ExecCallGvia(txtType, mkAddrSearch);
                
                if (ret.length() < 1)
                {
                    entityList.setErrorCode(ErrorCode.ERROR_EXECUTE_FAIL); // 요청실패
                    return entityList;
                }
                
                String[] addrSpl = ret.split(";");
                String[] addrColumns = addrSpl[0].split("[\u007C]");
                
                String strGubun = addrColumns[1]; // 결과구분
                String strToTRecord = addrColumns[2]; // 전체레코드 수
                String strStRecord = addrColumns[3]; // 시작레코드 수
                String strOutRecord = addrColumns[4]; // 반복레코드 수
                
                entityList.setResultcd(strGubun);
                entityList.setTotrecord(strToTRecord);
                entityList.setStrecord(strStRecord);
                entityList.setRorecord(strOutRecord);
                
                if (strGubun.equals("1"))
                {
                    String gubun = "";
                    String Level = "";
                    for (int i = 1; i < addrSpl.length - 1; i++)
                    {
                        GviaDistrictVo entitySubList = new GviaDistrictVo();
                        entitySubList.setResultcd(strGubun);
                        entitySubList.setTotrecord(strToTRecord);
                        entitySubList.setStrecord(strStRecord);
                        entitySubList.setRorecord(strOutRecord);
                        
                        String[] addrColumnsSub = addrSpl[i].split("[\u007C]");
                        
                        if (i == 1)
                        {
                            gubun = addrColumnsSub[0].trim();
                            Level = addrColumnsSub[1].trim();
                            
                            entitySubList.setDonggb(gubun);
                            entitySubList.setHdonglvd(Level);
                            entitySubList.setRegioncd(addrColumnsSub[2].trim());
                            entitySubList.setRegionnm(addrColumnsSub[3].trim());
                            
                            if (engUse == 1)
                            {
                                entitySubList.setEngRegionnm(addrColumnsSub[4].trim());
                            }
                        }
                        else
                        {
                            entitySubList.setDonggb(gubun);
                            entitySubList.setHdonglvd(Level);
                            entitySubList.setRegioncd(addrColumnsSub[0].trim());
                            entitySubList.setRegionnm(addrColumnsSub[1].trim());
                            
                            if (engUse == 1)
                            {
                                entitySubList.setEngRegionnm(addrColumnsSub[2].trim());
                            }
                        }
                        entityList.addEntity(entitySubList);
                    }
                }
                
                entityList.setErrorCode(ErrorCode.ERROR_SUCCESS); // 성공
            }
            else
            {
                entityList.setErrorCode(ErrorCode.ERROR_SOCKET_CONNECT); // 연결 실패
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            outLog("LinkGVIA > DistrictSearchVo 오류");
        }
        finally
        {
            SocketClose();
        }
        
        return entityList;
    }
    
    /**
     * 우편번호(5자리) 검색 - 개별출력(페이징 미사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색어
     * @return
     */
    public GviaPostSearchNewMultVo PostSearchNewVo(String subCode, String txtSearch)
    {
        GviaPostSearchNewMultVo entityList = new GviaPostSearchNewMultVo();
        entityList = PostSearchNewVo(subCode, txtSearch, 0, 0, 0);
        return entityList;
    }
    
    /**
     * 우편번호(5자리) 검색 - 개별출력(페이징 미사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색어
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @return
     */
    public GviaPostSearchNewMultVo PostSearchNewVo(String subCode, String txtSearch, int engUse)
    {
        GviaPostSearchNewMultVo entityList = new GviaPostSearchNewMultVo();
        entityList = PostSearchNewVo(subCode, txtSearch, 0, 0, engUse);
        return entityList;
    }
    
    /**
     * 우편번호(5자리) 검색 - 개별출력(페이징 사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색어
     * @param pageRecord : 페이지 시작 번호
     * @param outCnt : 한번에 출력하는 수량
     * @return
     */
    public GviaPostSearchNewMultVo PostSearchNewVo(String subCode, String txtSearch, int pageRecord, int outCnt)
    {
        GviaPostSearchNewMultVo entityList = new GviaPostSearchNewMultVo();
        entityList = PostSearchNewVo(subCode, txtSearch, pageRecord, outCnt, 0);
        return entityList;
    }
    
    /**
     * 우편번호(5자리) 검색 - 개별출력(페이징 사용)
     * 
     * @param subCode : 검색모드
     * @param txtSearch : 검색어
     * @param pageRecord : 페이지 시작 번호
     * @param outCnt : 한번에 출력하는 수량
     * @param engUse : 영문추가출력사용여부(0:미사용, 1:사용)
     * @return
     */
    public GviaPostSearchNewMultVo PostSearchNewVo(String subCode, String txtSearch, int pageRecord, int outCnt, int engUse)
    {
        GviaPostSearchNewMultVo entityList = new GviaPostSearchNewMultVo();
        
        String txtType = "";
        
        String ioptMain = "5";
        String ioptPage = "1";
        String ioptSub = "0";
        String ioptVer = "3";
        
        String txtPage1 = "";
        String txtPage2 = "";
        
        String postSubCodeList = "[LMNOPQRqrWwYyZ]"; // 우편번호 검색-개별출력 리스트
        String countSubCodeList = "[X]"; // 우편번호 통합건수검색-개별출력 리스트
        
        if (engUse == 1) ioptMain = "D";
        else ioptMain = "5";
        
        if (subCode == null || subCode.length() != 1 || (!subCode.matches(postSubCodeList) && !subCode.matches(countSubCodeList)))
        {
            entityList.setErrorCode(ErrorCode.ERROR_PACKET_HEADER_UNKNOWN);
            return entityList;
        }
        else
        {
            if (pageRecord < 0) pageRecord = 0;
            if (outCnt < 0) outCnt = 0;
            if (pageRecord == 0 || outCnt == 0) ioptPage = "0";
            
            if (ioptPage.equals("1"))
            {
                pageRecord = 1 + (outCnt * (pageRecord - 1));
            }
            
            ioptSub = subCode;
            
            txtPage1 = Integer.toString(pageRecord);
            txtPage2 = Integer.toString(outCnt);
            
            txtType = ioptMain + ioptPage + ioptSub + ioptVer;
        }
        
        try
        {
            if (SocketConnect())
            {
                String mkAddrSearch = MakeGviaInData(txtType, txtSearch, txtPage1, txtPage2);
                String ret = ExecCallGvia(txtType, mkAddrSearch);
                
                if (ret.length() < 1)
                {
                    entityList.setErrorCode(ErrorCode.ERROR_EXECUTE_FAIL); // 요청실패
                    return entityList;
                }
                
                String[] addrSpl = ret.split(";");
                String[] addrColumns = addrSpl[0].split("[\u007C]");
                
                String strGubun = addrColumns[1]; // 결과구분
                String strToTRecord = addrColumns[2]; // 전체레코드 수
                String strStRecord = addrColumns[3]; // 시작레코드 수
                String strOutRecord = addrColumns[4]; // 반복레코드 수
                
                entityList.setResultcd(strGubun);
                entityList.setTotrecord(strToTRecord);
                entityList.setStrecord(strStRecord);
                entityList.setRorecord(strOutRecord);
                
                if (strGubun.equals("1"))
                {
                    if (ioptSub.matches(postSubCodeList))
                    { // 신우편번호 검색
                        entityList.setResult(0); // 우편범호 검색 모드 설정
                        for (int i = 1; i < addrSpl.length - 1; i++)
                        {
                            GviaPostSearchNewVo entitySubList = new GviaPostSearchNewVo();
                            entitySubList.setResultcd(strGubun);
                            entitySubList.setTotrecord(strToTRecord);
                            entitySubList.setStrecord(strStRecord);
                            entitySubList.setRorecord(strOutRecord);
                            
                            String[] addrColumnsSub = addrSpl[i].split("[\u007C]", -1);
                            
                            entitySubList.setBsizonno(addrColumnsSub[0].trim());
                            entitySubList.setPostno(addrColumnsSub[1].trim());
                            entitySubList.setPostseq(addrColumnsSub[2].trim());
                            entitySubList.setSido(addrColumnsSub[3].trim());
                            entitySubList.setGungu(addrColumnsSub[4].trim());
                            entitySubList.setDong(addrColumnsSub[5].trim());
                            entitySubList.setRi(addrColumnsSub[6].trim());
                            entitySubList.setRoadnm(addrColumnsSub[7].trim());
                            entitySubList.setUndercd(addrColumnsSub[8].trim());
                            
                            if (entitySubList.getUndercd().equals("1")) entitySubList.setUnder("지하");
                            else if (entitySubList.getUndercd().equals("2")) entitySubList.setUnder("공중");
                            else entitySubList.setUnder("");
                            
                            entitySubList.setBuildmn(addrColumnsSub[9].trim());
                            entitySubList.setBuildsn(addrColumnsSub[10].trim());
                            entitySubList.setBuildnm(addrColumnsSub[11].trim());
                            entitySubList.setRoadcd(addrColumnsSub[12].trim());
                            entitySubList.setDongseq(addrColumnsSub[13].trim());
                            entitySubList.setSancd(addrColumnsSub[14].trim());
                            
                            if (entitySubList.getSancd().equals("1")) entitySubList.setSan("산");
                            else entitySubList.setSan("");
                            
                            entitySubList.setBunji(addrColumnsSub[15].trim());
                            entitySubList.setHo(addrColumnsSub[16].trim());
                            entitySubList.setBdongcd(addrColumnsSub[17].trim());
                            entitySubList.setHdongcd(addrColumnsSub[18].trim());
                            entitySubList.setBdongnm(addrColumnsSub[19].trim());
                            entitySubList.setHdongnm(addrColumnsSub[20].trim());
                            
                            if (engUse == 1)
                            {
                                entitySubList.setEng_sido(addrColumnsSub[21].trim());
                                entitySubList.setEng_gungu(addrColumnsSub[22].trim());
                                entitySubList.setEng_dong(addrColumnsSub[23].trim());
                                entitySubList.setEng_ri(addrColumnsSub[24].trim());
                                entitySubList.setEng_roadnm(addrColumnsSub[25].trim());
                            }
                            
                            entityList.addEntity(entitySubList);
                        }
                    }
                    else if (ioptSub.matches(countSubCodeList))
                    { // 통합 건수검색
                        entityList.setResult(1); // 통합 건수검색 모드 설정
                        for (int i = 1; i < addrSpl.length - 1; i++)
                        {
                            GviaPostSearchTotCountVo entitySubList2 = new GviaPostSearchTotCountVo();
                            entitySubList2.setResultcd(strGubun);
                            entitySubList2.setTotrecord(strToTRecord);
                            entitySubList2.setStrecord(strStRecord);
                            entitySubList2.setRorecord(strOutRecord);
                            
                            String[] addrColumnsSub = addrSpl[i].split("[\u007C]");
                            
                            entitySubList2.setAd_jr(addrColumnsSub[0].trim());
                            entitySubList2.setStartrcd_se(addrColumnsSub[1].trim());
                            entitySubList2.setCount_se(addrColumnsSub[2].trim());
                            entitySubList2.setStartrcd_bs(addrColumnsSub[3].trim());
                            entitySubList2.setCount_bs(addrColumnsSub[4].trim());
                            entitySubList2.setStartrcd_dg(addrColumnsSub[5].trim());
                            entitySubList2.setCount_dg(addrColumnsSub[6].trim());
                            entitySubList2.setStartrcd_ic(addrColumnsSub[7].trim());
                            entitySubList2.setCount_ic(addrColumnsSub[8].trim());
                            entitySubList2.setStartrcd_gj(addrColumnsSub[9].trim());
                            entitySubList2.setCount_gj(addrColumnsSub[10].trim());
                            entitySubList2.setStartrcd_dj(addrColumnsSub[11].trim());
                            entitySubList2.setCount_dj(addrColumnsSub[12].trim());
                            entitySubList2.setStartrcd_us(addrColumnsSub[13].trim());
                            entitySubList2.setCount_us(addrColumnsSub[14].trim());
                            entitySubList2.setStartrcd_sj(addrColumnsSub[15].trim());
                            entitySubList2.setCount_sj(addrColumnsSub[16].trim());
                            entitySubList2.setStartrcd_gg(addrColumnsSub[17].trim());
                            entitySubList2.setCount_gg(addrColumnsSub[18].trim());
                            entitySubList2.setStartrcd_gw(addrColumnsSub[19].trim());
                            entitySubList2.setCount_gw(addrColumnsSub[20].trim());
                            entitySubList2.setStartrcd_cb(addrColumnsSub[21].trim());
                            entitySubList2.setCount_cb(addrColumnsSub[22].trim());
                            entitySubList2.setStartrcd_cn(addrColumnsSub[23].trim());
                            entitySubList2.setCount_cn(addrColumnsSub[24].trim());
                            entitySubList2.setStartrcd_jb(addrColumnsSub[25].trim());
                            entitySubList2.setCount_jb(addrColumnsSub[26].trim());
                            entitySubList2.setStartrcd_jn(addrColumnsSub[27].trim());
                            entitySubList2.setCount_jn(addrColumnsSub[28].trim());
                            entitySubList2.setStartrcd_gb(addrColumnsSub[29].trim());
                            entitySubList2.setCount_gb(addrColumnsSub[30].trim());
                            entitySubList2.setStartrcd_gn(addrColumnsSub[31].trim());
                            entitySubList2.setCount_gn(addrColumnsSub[32].trim());
                            entitySubList2.setStartrcd_jj(addrColumnsSub[33].trim());
                            entitySubList2.setCount_jj(addrColumnsSub[34].trim());
                            
                            entityList.addCountEntity(entitySubList2);
                        }
                    }
                }
                
                entityList.setErrorCode(ErrorCode.ERROR_SUCCESS); // 성공
            }
            else
            {
                entityList.setErrorCode(ErrorCode.ERROR_SOCKET_CONNECT); // 연결 실패
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            outLog("LinkGVIA > PostSearchNewVo 오류");
        }
        finally
        {
            SocketClose();
        }
        
        return entityList;
    }
    
    // ////////////////// gvia_vo end ////////////////////
    
    // ////////////////// gvia_custom start ////////////////////
    
    public String EasyMakeCustomAdd1(String[] arrResult, String extUser)
    { // EasyMakeAdd1 을 기반으로 사용자 주소형태를 만든다.
        String add1 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            String sido = arrResult[12];
            String gungu = arrResult[13];
            String dong = arrResult[14];
            String ri = arrResult[15];
            // String roadnm = arrResult[33];
            String san = arrResult[16];
            String bunji = arrResult[17];
            String ho = arrResult[18];
            String buildinfo = arrResult[41];
            // String roadbuildnm = arrResult[46];
            // String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
            String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : "";
            // String remain = arrResult[54];
            
            if (extUser.equals("MAEIL_DS"))
            { // 지번 주소유형 : 건물명에 아파트가 존재 시 기본주소에 건물명을 표시하며 상세주소에 지번정보와 건물명을 제거한다.
                String adDistrict = MakeDistrict("J", sido, gungu, dong, ri); // 행정구역 만들기
                String bunjiho = MakeMainSubNumber("J", san, bunji, ho, ZeroUse); // 건물번호,지번 정보 만들기
                if (bunjiho != null && !bunjiho.equals(""))
                {
                    if (adDistrict != null && !adDistrict.equals(""))
                    {
                        String tmpBuildName = "";
                        if (buildnm.matches(".*아파트$")) tmpBuildName = buildnm;
                        
                        add1 = adDistrict;
                        if (!tmpBuildName.equals("")) add1 = add1 + " " + tmpBuildName;
                    }
                }
            }
        }
        
        return add1;
    }
    
    public String EasyMakeCustomAdd2(String[] arrResult, String extUser)
    { // EasyMakeAdd2 을 기반으로 사용자 주소형태를 만든다.
        String add2 = "";
        
        if (arrResult != null && arrResult.length > 0)
        {
            // String sido = arrResult[12];
            // String gungu = arrResult[13];
            // String dong = arrResult[14];
            // String ri = arrResult[15];
            // String roadnm = arrResult[33];
            String san = arrResult[16];
            String bunji = arrResult[17];
            String ho = arrResult[18];
            String buildinfo = arrResult[41];
            String roadbuildnm = arrResult[46];
            String buildnm = (buildinfo != null && !buildinfo.equals("")) ? buildinfo : roadbuildnm;
            String remain = arrResult[45];
            
            if (extUser.equals("MAEIL_DS"))
            { // 지번 주소유형 : 건물명에 아파트가 존재 시 기본주소에 건물명을 표시하며 상세주소에 지번정보와 건물명을 제거한다.
                String bunjiho = MakeMainSubNumber("J", san, bunji, ho, ZeroUse); // 건물번호,지번 정보 만들기
                if (bunjiho != null && !bunjiho.equals(""))
                {
                    String tmpBuildName = "";
                    if (buildnm.matches(".*아파트$")) tmpBuildName = buildnm;
                    
                    if (tmpBuildName.equals(""))
                    {
                        add2 = bunjiho;
                        if (!buildnm.equals("")) add2 = add2 + " " + buildnm;
                        if (!remain.equals("")) add2 = add2 + " " + remain;
                    }
                    else
                    {
                        if (!remain.equals("")) add2 = remain;
                    }
                }
            }
        }
        
        return add2;
    }
    // ////////////////// gvia_custom end ////////////////////
}
