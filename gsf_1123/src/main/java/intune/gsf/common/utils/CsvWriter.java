package intune.gsf.common.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsvWriter implements Runnable {

    /** log. */
    private static final Logger log = LoggerFactory.getLogger(CsvWriter.class);

    private List<String> titleList = null;
    private List<Map> dataList = null;
    private String fileName = null;
    private String dataSep = null;
    private boolean isTitle = true; // default=out

    // START SELLER & CP_MG Batch수정
    private String strLineHeader = null;
    private String strHeader = null;
    private String strFooter = null;
    // END SELLER & CP_MG Batch수정

    private CsvWriter() {}

    public CsvWriter(List<String> tiList, List<Map> daList, String fiName) {
        this(tiList, daList, fiName, null);
    }

    public CsvWriter(List<String> tiList, List<Map> daList, String fiName, String daSep) {
        this(tiList, daList, fiName, daSep, true);
    }

    public CsvWriter(List<String> tiList, List<Map> daList, String fiName, String daSep, boolean isTit) {

    // START SELLER & CP_MG Batch수정
        this(tiList, daList, fiName, daSep, isTit, null, null, null);
    }

    public CsvWriter(List<String> tiList, List<Map> daList, String fiName, String daSep, boolean isTit
                    , String strLineHeader, String strHeader, String strFooter) {
    // END SELLER & CP_MG Batch수정

        this.titleList = tiList;
        this.dataList = daList;
        this.fileName = fiName;
        this.dataSep = daSep;
        this.isTitle = isTit;

        // START SELLER & CP_MG Batch수정
        this.strLineHeader = strLineHeader;
        this.strHeader = strHeader;
        this.strFooter = strFooter;
        // END SELLER & CP_MG Batch수정
    }

    @Override
    public void run() {
        try {
        	long before1 = System.currentTimeMillis() ;
            writeCsvFile();
			long diffTime1 = System.currentTimeMillis() - before1;
			
			System.out.println("Process run time : " + diffTime1 + "(ms)");
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }

    private void writeCsvFile() throws IOException {
        StringBuffer title = new StringBuffer();
        if (CommonUtil.isEmpty(this.dataSep) ) {
            this.dataSep = ",";
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(this.fileName);
//            title.append("\uFEFF");

            if(CommonUtil.isNotEmpty(this.strHeader)) {
               // writer.append(this.strHeader + "\n");
                title.append(this.strHeader + "\n");
            }

            if (this.isTitle) {
                for (int i = 0; i < this.titleList.size(); i++) {
                    title.append(this.titleList.get(i) );

                    if (i != this.titleList.size() -1) {
                        title.append(this.dataSep);
                    }
                }

                writer.append(title.toString() + "\n");
            }

            for (Map obj : this.dataList) {
                StringBuffer value = new StringBuffer();

                for (int i = 0; i < this.titleList.size(); i++) {
                    if (obj.get(this.titleList.get(i) ) != null) {
                        // START SELLER & CP_MG Batch수정
//                        if(obj.get(this.titleList.get(i) ).toString().indexOf("\"") > -1){
//                            value.append(obj.get(this.titleList.get(i) ).toString() );
//                        }
//                        else{
//                            value.append("\t" + obj.get(this.titleList.get(i) ).toString() );
//                        }
                        if(CommonUtil.isNotEmpty(this.strLineHeader)) {
                            if(i ==0) {
                                value.append("D" + obj.get(this.titleList.get(i)).toString());
                            } else if(CommonUtil.isEmpty(obj.get(this.titleList.get(i)).toString())) {
                                value.append(" ");
                            } else if(CommonUtil.isNotEmpty(this.strLineHeader)) {
                                value.append(obj.get(this.titleList.get(i)).toString());
                            }
                        } else if(obj.get(this.titleList.get(i) ).toString().indexOf("\"") < 0){
                            value.append("\t" + obj.get(this.titleList.get(i) ).toString() );
                        } else {
                            value.append(obj.get(this.titleList.get(i) ).toString() );
                        }
                        // END SELLER & CP_MG Batch수정
                    }

                    if (i != this.titleList.size() -1) {
                        value.append(this.dataSep);
                    }
                }

                writer.append(value.toString() + "\n");
            }

            // START SELLER & CP_MG Batch수정
            if(CommonUtil.isNotEmpty(this.strFooter)) {
                writer.append(this.strFooter + "\n");
            }
            // END SELLER & CP_MG Batch수정

        } catch(IOException e) {
             throw e;
        } finally {
            writer.flush();
            writer.close();
        }

        log.info("CsvWriter is ended.");
    }

    public static String generateFilePath(String filePath, String originalFileName) {
        return generateFilePath(filePath, originalFileName, null);
    }

    public static String generateFilePath(String filePath, String originalFileName, String fileExt) {
        if (CommonUtil.isEmpty(filePath) ) {
            filePath = "/home01/jboss/download/csv/";
        }

        if (CommonUtil.isEmpty(fileExt) ) {
            fileExt = getExtension(originalFileName);
        }

        String now = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date() );
        int lastIndex = originalFileName.lastIndexOf(".");
        String organizedFileName = originalFileName.substring(0, lastIndex) + "_" + now + "." + fileExt.toLowerCase();
        return filePath + "/" + organizedFileName;
    }
    
    
	public static String getExtension(String fileName) {
		String ext = null;
		if (fileName != null) {
			int lastIndex = fileName.lastIndexOf(".");
			if (lastIndex > -1) {
				ext = fileName.substring(lastIndex + 1);
			}
		}
		return ext;
	}
}
