package api.utilities;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterNG implements IReporter {
	
	private ExtentReports extent;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		extent = new ExtentReports(outputDirectory + File.separator
				+ "Extent.html", true);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
			}
		}

		extent.flush();
		extent.close();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) {
		ExtentTest test;

		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = extent.startTest(result.getMethod().getMethodName());

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				if (result.getThrowable() != null) {
					test.log(status, result.getThrowable());
				} else {
					test.log(status, "Test " + status.toString().toLowerCase()
							+ "ed");
				}

				extent.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}
//public class ExtentReportManager {
//
//public ExtentSparkReporter sparkReporter;
//public ExtentReports extent;
//public ExtentTest test;
//
//String repName;
//
//public void onStart(ITestContext testcontext) {
//	
//	String timeStamp = new SimpleDateFormat("yyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
//	repName="Test-Report-"+timeStamp+".html";
//
//	sparkReporter=new ExtentSparkReporter("/Users/sjebamalai/Lilly_Workspace/APIAutomationFrameWork/reports"+repName);//specify location of the report
//	
//	sparkReporter.config().setDocumentTitle("RestAssuredAutomationProject");//Title of the report
//	sparkReporter.config().setReportName("pet store users API"); //name of the report
//	sparkReporter.config().setTheme(Theme.DARK);
//
//	extent = new ExtentReports();
//	extent.attachReporter(sparkReporter);
//	extent.setSystemInfo("Application", "pet store users API");
//	extent.setSystemInfo("Operating System", System.getProperty("os.name"));
//	extent.setSystemInfo("User Name", System.getProperty("user.name"));
//	extent.setSystemInfo("Environment", "QA");
//	extent.setSystemInfo("User", "Lilly");
//}
//public void onTestSuccess(ITestResult result) {
//	test = extent.createTest(result.getName());
//	test.assignCategory(result.getMethod().getGroups());
//	test.createNode(result.getName());
//	test.log(Status.PASS, "Test Passed");
//}
//public void OnTestFailure(ITestResult result) {
//	test = extent.createTest(result.getName());
//	test.createNode(result.getName());
//	test.assignCategory(result.getMethod().getGroups());
//	test.log(Status.FAIL, "Test Failed");
//	test.log(Status.FAIL, result.getThrowable().getMessage());	
//}
//public void onTestskipped(ITestResult result) {
//	test = extent.createTest(result.getName());
//	test.createNode(result.getName());
//	test.assignCategory(result.getMethod().getGroups());
//	test.log(Status.SKIP, "Test Failed");
//	test.log(Status.SKIP, result.getThrowable().getMessage());	
//}
//public void onFinish(ITestResult result) {
//	extent.flush();
//}
//}
