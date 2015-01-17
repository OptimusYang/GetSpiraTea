import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;


public class DownloadSpiraTeam implements Runnable{
	private String username;
	private String password;
	private String filePath;
	private Reporter reporter;

	public void download() { 
		OutputStream out = null;
		try{
			reporter.report("Initializing...", 0);
		WebClient wc = new WebClient(BrowserVersion.FIREFOX_24);
		wc.getOptions().setJavaScriptEnabled(true); 
		wc.setAjaxController(new NicelyResynchronizingAjaxController());
		wc.getOptions().setCssEnabled(false);
		wc.getOptions().setThrowExceptionOnScriptError(false); 
		wc.getOptions().setTimeout(10000); 
		reporter.report("Initialization is complete.", 10);
		reporter.report("Connecting to SpiraTeam...", 60);
		HtmlPage page = wc.getPage("https://bdpteamprod.argogroupus.com/SpiraTeam/Login.aspx?ReturnUrl=%2fSpiraTeam%2f32%2fIncident%2fList.aspx");
		HtmlForm loginForm = page.getFormByName("actionlessForm"); 
		reporter.report("Successfully connected to SpiraTeam.", 62);
		reporter.report("Fill in Username/Password.", 63);
		HtmlInput  uelmt =  loginForm.getInputByName("mpLogin$cplMainContent$LoginUser$UserName");
		uelmt.setValueAttribute(username);
		HtmlInput pelmt =  loginForm.getInputByName("mpLogin$cplMainContent$LoginUser$Password");
		pelmt.setValueAttribute(password);
		HtmlSubmitInput loginBtn = loginForm.getInputByName("mpLogin$cplMainContent$LoginUser$btnLogin");  
		HtmlPage resultPage = loginBtn.click();
		if(resultPage.getUrl().toString().equals("https://bdpteamprod.argogroupus.com/SpiraTeam/Login.aspx?ReturnUrl=%2fSpiraTeam%2f32%2fIncident%2fList.aspx")){
			reporter.report("Your login attempt was not successful. Please try again.", 100);
			return;
		}
		if(resultPage.getElementById("cplMainContent_btnSignOffOthers") != null){
			reporter.report("Sign Off The Other Locations...", 94);
			HtmlAnchor link = (HtmlAnchor)resultPage.getElementById("cplMainContent_btnSignOffOthers");
			resultPage = link.click();

		}
		reporter.report("Page Initializing...", 94);
		Thread.sleep(10*1000);
		reporter.report("Page initialization is complete..", 95);
		HtmlTable res = (HtmlTable)resultPage.getElementById("cplMainContent_grdIncidentList");
		reporter.report("Get Incidents list successfully.", 96);
		reporter.report("Export to Excel...", 97);
		HSSFWorkbook workbook = new HSSFWorkbook();
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        HSSFSheet sheet = workbook.createSheet(date);
        int i=0;
        out = new FileOutputStream(filePath+"//SpiraTeam_"+date+".xls");
		for(HtmlTableRow row : res.getRows() ){
			int j=0;
			HSSFRow excelRow = sheet.createRow(i++);
			for(HtmlTableCell c : row.getCells()){
				HSSFCell cell = excelRow.createCell(j++);
				 cell.setCellValue(c.getTextContent());
			}
		}
		workbook.write(out);
		out.close();
		reporter.report("Export to Excel successfully.", 99);
		reporter.report("Successfully.", 100);
		} catch(Exception e){
			reporter.report(e.getLocalizedMessage()	, 100);
			e.printStackTrace();
		} finally{
			try {
				if(out != null){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the reporter
	 */
	public Reporter getReporter() {
		return reporter;
	}

	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}

	public void run() {
		download();
	}

}
