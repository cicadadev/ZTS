package gcp.external.kainess.gvia_util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Class        : ProptInfo
 * @Date         : 2015. 3. 17. 
 * @Author       : 조성연
 * @Description  :
 */

public class ProptInfo 
{
	private String PROPERTIY_PATH = "";
	private static final String PROPERTIY_FILE = "gvia_server.properties";
	private static Properties prop = null;
	
	private String GviaIp1;
	private int GviaPort1;
	
	private String GviaIp2;
	private int GviaPort2;
	
	private String GVersion;
	
	private String UploadUrl;
	private String BatchUrl;
	
	private String FtpUrl;
	private int FtpPort;
	private String FtpId;
	private String FtpPw;
	private String FtpHome;
	
	public ProptInfo(String proptLoc)
	{
		if(!SetProperties(proptLoc)){
			//
		}
	}
	
	public void setProptPath(String pROPERTIY_PATH)
	{
		PROPERTIY_PATH = pROPERTIY_PATH;
	}
	public String getProptPath()
	{
		return PROPERTIY_PATH;
	}
	public void setGviaIp1(String gviaIp1)
	{
		GviaIp1 = gviaIp1;
	}
	public String getGviaIp1()
	{
		return GviaIp1;
	}
	public void setGviaPort1(int gviaPort1)
	{
		GviaPort1 = gviaPort1;
	}
	public int getGviaPort1()
	{
		return GviaPort1;
	}
	public void setGviaIp2(String gviaIp2)
	{
		GviaIp2 = gviaIp2;
	}
	public String getGviaIp2()
	{
		return GviaIp2;
	}
	public void setGviaPort2(int gviaPort2)
	{
		GviaPort2 = gviaPort2;
	}
	public int getGviaPort2()
	{
		return GviaPort2;
	}
	public void setGVersion(String gVersion)
	{
		GVersion = gVersion;
	}
	public String getGVersion()
	{
		return GVersion;
	}
	public void setUploadUrl(String uploadUrl)
	{
		UploadUrl = uploadUrl;
	}
	public String getUploadUrl()
	{
		return UploadUrl;
	}
	public void setBatchUrl(String batchUrl)
	{
		BatchUrl = batchUrl;
	}
	public String getBatchUrl()
	{
		return BatchUrl;
	}
	public void setFtpUrl(String ftpUrl)
	{
		FtpUrl = ftpUrl;
	}
	public String getFtpUrl()
	{
		return FtpUrl;
	}
	public void setFtpPort(int ftpPort)
	{
		FtpPort = ftpPort;
	}
	public int getFtpPort()
	{
		return FtpPort;
	}
	public void setFtpId(String ftpId)
	{
		FtpId = ftpId;
	}
	public String getFtpId()
	{
		return FtpId;
	}
	public void setFtpPw(String ftpPw)
	{
		FtpPw = ftpPw;
	}
	public String getFtpPw()
	{
		return FtpPw;
	}
	public void setFtpHome(String ftpHome)
	{
		FtpHome = ftpHome;
	}
	public String getFtpHome()
	{
		return FtpHome;
	}
	
	
	
	/* 
	 * function : SetProperties()
	 * content	: G-Via 서버의 경로 및 포트 확인
	 * value 1.String strroot : PROPERTIY_FILE 이 있는 경로  
	 * return : boolean
	 */
	public boolean SetProperties(String strroot){
		//Properties : G 서버의 경로 및 포트 확인
		InputStream is = null;
		File profile = null; 
		FileInputStream fis = null;
		boolean bool = false;
		
		try
		{			
			String PROPERTIES_FILE = strroot + "/" + PROPERTIY_FILE;
			prop = new Properties();
			profile = new File(PROPERTIES_FILE);
							
			if (!profile.exists())  //Properties 파일이 없는 경우
			{
				System.out.println("Properties NOT");
			}
			else
			{
				fis = new FileInputStream(PROPERTIES_FILE);
				prop.load(new java.io.BufferedInputStream(fis));
				
				try{  setProptPath(strroot); } catch (NullPointerException e){ setProptPath(""); } catch (Exception e) { setProptPath(""); }
				
				try{  setGviaIp1(prop.getProperty("tcpip.ip1")); } catch (NullPointerException e){ setGviaIp1("127.0.0.1"); } catch (Exception e) { setGviaIp1("127.0.0.1"); }
				try{  setGviaPort1(Integer.parseInt(prop.getProperty("tcpip.port1"))); } catch (NullPointerException e){ setGviaPort1(7900); } catch (Exception e) { setGviaPort1(7900); }
				try{  setGviaIp2(prop.getProperty("tcpip.ip2")); } catch (NullPointerException e){ setGviaIp2("127.0.0.1"); } catch (Exception e) { setGviaIp2("127.0.0.1"); }
				try{  setGviaPort2(Integer.parseInt(prop.getProperty("tcpip.port2"))); } catch (NullPointerException e){ setGviaPort2(7900); } catch (Exception e) { setGviaPort2(7900); }

				try{  setGVersion(prop.getProperty("gpoint.ver")); } catch (NullPointerException e){ setGVersion(""); } catch (Exception e) { setGVersion(""); }
				
				try{  setUploadUrl(prop.getProperty("upload.url")); } catch (NullPointerException e){ setUploadUrl(""); } catch (Exception e) { setUploadUrl(""); }
				try{  setBatchUrl(prop.getProperty("user.url")); } catch (NullPointerException e){ setBatchUrl(""); } catch (Exception e) { setBatchUrl(""); }
				
				try{  setFtpUrl(prop.getProperty("ftp.url")); } catch (NullPointerException e){ setFtpUrl(""); } catch (Exception e) { setFtpUrl(""); }
				try{  setFtpPort(Integer.parseInt(prop.getProperty("ftp.port"))); } catch (NullPointerException e){ setFtpPort(0); } catch (Exception e) { setFtpPort(0); }
				try{  setFtpId(prop.getProperty("ftp.id")); } catch (NullPointerException e){ setFtpId(""); } catch (Exception e) { setFtpId(""); }
				try{  setFtpPw(prop.getProperty("ftp.pw")); } catch (NullPointerException e){ setFtpPw(""); } catch (Exception e) { setFtpPw(""); }
				try{  setFtpHome(prop.getProperty("ftp.home")); } catch (NullPointerException e){ setFtpHome(""); } catch (Exception e) { setFtpHome(""); }
				
				bool = true;
			}
		} catch (IOException e) {
			System.out.println("SetProperties IOException e:"+e);
			throw new RuntimeException("Not Found Properties File");
	    } finally {	    	
	    	if( fis != null ) try{ fis.close(); }catch(IOException e){System.out.println("Properties - FileInputStream close :"+e);}
	    	if( is != null ) try{ is.close(); }catch(IOException e){System.out.println("Properties - InputStream close :"+e);}
		}
	    
		return bool;
	}
}
