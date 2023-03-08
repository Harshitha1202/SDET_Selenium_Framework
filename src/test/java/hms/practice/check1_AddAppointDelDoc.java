package hms.practice;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.github.dockerjava.api.command.AttachContainerCmd.Exec;
import com.tyss.Generic_Utility.ExcelUtility;
import com.tyss.Generic_Utility.JavaUtility;
import com.tyss.Generic_Utility.WebDriverUtility;
import com.tyss.Generic_Utility.Constants.FrameWorkConstants;
import com.tyss.Generic_Utility.Enums.ExcelSheet.PropertyKey;
import com.tyss.Generic_Utility.ExternalFileUtility.PropertyUtility;
import com.tyss.Generic_Utility.ExternalFileUtility.VerificationUtility;
import com.tyss.Generic_Utility.ExternalFileUtility.WaitUtility;

import hms.objectRepository_Admin.AddDoctorPage;
import hms.objectRepository_Admin.AdminDashboardPage;
import hms.objectRepository_Admin.AdminLoginPage;
import hms.objectRepository_Admin.ManageDoctorPage;
import hms.objectRepository_Application.HMSHomePage;
import hms.objectRepository_Patient.PatientBookAppointmentPage;
import hms.objectRepository_Patient.PatientDashboardPage;
import hms.objectRepository_Patient.PatientLoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class check1_AddAppointDelDoc
{

	public static void main(String[] args) throws IOException 
	{
	
		WebDriverUtility webdriverutil=new WebDriverUtility();
		WaitUtility waitutil= new WaitUtility();
		VerificationUtility verificationutil= new VerificationUtility();
		
		 
		//commom data
		PropertyUtility propertyutil = new PropertyUtility(FrameWorkConstants.TEST_PROPERTY_FILEPATH);
		ExcelUtility excelutil = new ExcelUtility(FrameWorkConstants.EXCEL_PATH);
		String url = propertyutil.getPropertyData(PropertyKey.URL);
		String browser= propertyutil.getPropertyData(PropertyKey.BROWSER);
	    long timeout = Long.parseLong(propertyutil.getPropertyData(PropertyKey.TIMEOUT));
	    
	
	    //launch application
		WebDriver driver = webdriverutil.setBrowser(browser);
		driver.get(url);
		JavaUtility javautil= new JavaUtility(driver);
		webdriverutil.windowMaximize();
		waitutil.implicityWait(driver, timeout);
		
		String doctorUserName = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.DOCTOR_USERNAME));
		String doctorPassword = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.DOCTOR_PASSWORD));
		String adminUserName = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.ADMIN_USERNAME));
		String adminPassword = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.ADMIN_PASSWORD));
		String patientUserName = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.PATIENT_USERNAME));
		String patientPassword = javautil.Decrypt(propertyutil.getPropertyData(PropertyKey.PATIENT_PASSWORD));
		
		//fetch data from excel
		String exptestCaseName= "addAppointmentDelDoc";
		String sheetName= "testscriptdata";
		Map<String, String> map = excelutil.getData(sheetName, exptestCaseName);
		
		//POM // from obj repository
		HMSHomePage hmshomepage= new HMSHomePage(driver);
		AdminLoginPage alogin= new AdminLoginPage(driver);
		PatientDashboardPage mainpatdash= new PatientDashboardPage(driver);
		AddDoctorPage adddoc=new AddDoctorPage(driver);
		AdminDashboardPage admdashpage= new AdminDashboardPage(driver);
		PatientBookAppointmentPage bookapppage= new PatientBookAppointmentPage(driver);
		ManageDoctorPage managedoc= new ManageDoctorPage(driver);
		
		//HomePage
		String home = driver.findElement(By.xpath("//a[.='Hospital Management system']")).getText();
		verificationutil.verifyPage(hmshomepage.ApplicationHomeTitleVerificationAction(), map.get("application_homepage"));
		hmshomepage.Home_AdminAction();
		
		//admin login // from obj repository
		alogin.AdminLoginAction(adminUserName, adminPassword);
	    verificationutil.verifyPage(admdashpage.AdminHomePageVerificationAction(), map.get("admin_homepage"));
	    admdashpage.AdminDoctorAction();
	    admdashpage.AddDoctorAction();
	    adddoc.SelectDoctorDropDownAction(webdriverutil, "Oncologist");
	    String D_Email =map.get("docter_email")+javautil.getRandomNumber(1000)+"@gmail.com";
	    adddoc.AddDoctorAction(map.get("doctor_name"), map.get("clinic_address"), map.get("doctor _fees"),
	    		map.get("doctor_contactno"), D_Email, map.get("new_password"), map.get("confirm_password"));
	    webdriverutil.handleAlertAccept(driver);
	    admdashpage.AdminLogout();
	    
	    //login as patient // from obj repository
	    hmshomepage.Home_PatientAction();
	    PatientLoginPage plogin = new PatientLoginPage(driver);
	    plogin.PatientLoginAction(patientUserName, patientPassword);
	    verificationutil.verifyPage(mainpatdash.PatientHomePageVerification(), map.get("patient_homepage"));
	    mainpatdash.DashboardBookAppointmentAction();
	    bookapppage.DoctorspecializationDropDownAction(webdriverutil, "Oncologist");
	    bookapppage.DoctorDropDownAction(webdriverutil, "Suresh");
	    bookapppage.BookAppointmentAction(map.get("appointment_date"));
	    webdriverutil.handleAlertAccept(driver);
	    mainpatdash.PatientLogoutAction();	
	    
	    //admin login
	    hmshomepage.Home_AdminAction();
	  	alogin.AdminLoginAction(adminUserName, adminPassword);
	  	verificationutil.verifyPage(admdashpage.AdminHomePageVerificationAction(), map.get("admin_homepage"));
	    //del doctor  from obj repository
	    admdashpage.AdminDoctorAction();
	    admdashpage.ManageDoctorAction();
	    managedoc.DeleteDoctorAction();
	    webdriverutil.handleAlertAccept(driver);
	    verificationutil.verifyPage(managedoc.DoctorDeleteVerificationAction(), map.get("delete_doctor") );
	    webdriverutil.closeApplication();
		
		
		
	}	
		    
		
}
			
			
			
		
