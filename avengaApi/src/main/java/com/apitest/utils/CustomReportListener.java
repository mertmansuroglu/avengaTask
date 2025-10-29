package com.apitest.utils;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomReportListener implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">\n");
        html.append("<title>Test Report</title>\n");
        html.append("<style>body{font-family:Arial,sans-serif;margin:24px;} h1{margin:0 0 8px;} table{border-collapse:collapse;width:100%;margin-top:16px;} th,td{border:1px solid #ddd;padding:8px;} th{background:#f5f5f5;text-align:left;} .pass{color:#1b5e20;font-weight:bold;} .fail{color:#b71c1c;font-weight:bold;} .skip{color:#e65100;font-weight:bold;} .meta{color:#666;font-size:12px;}</style>\n");
        html.append("</head><body>\n");

        html.append("<h1>Test Report</h1>\n");
        html.append("<div class=\"meta\">")
            .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .append("</div>\n");

        for (ISuite suite : suites) {
            html.append("<h2>").append(escape(suite.getName())).append("</h2>\n");

            Map<String, ISuiteResult> suiteResults = suite.getResults();
            for (Map.Entry<String, ISuiteResult> entry : suiteResults.entrySet()) {
                String testName = entry.getKey();
                ITestContext ctx = entry.getValue().getTestContext();

                int passed = ctx.getPassedTests().size();
                int failed = ctx.getFailedTests().size();
                int skipped = ctx.getSkippedTests().size();
                int total = passed + failed + skipped;

                html.append("<h3>").append(escape(testName)).append("</h3>\n");
                html.append("<div>Total: ").append(total).append(
                        " | <span class=\"pass\">Passed: ").append(passed).append("</span>"
                ).append(" | <span class=\"fail\">Failed: ").append(failed).append("</span>")
                 .append(" | <span class=\"skip\">Skipped: ").append(skipped).append("</span></div>\n");

                html.append("<table><thead><tr><th>#</th><th>Test</th><th>Status</th><th>Duration</th></tr></thead><tbody>\n");

                int index = 1;
                index = appendResults(html, ctx.getPassedTests().getAllResults(), index, "pass");
                index = appendResults(html, ctx.getFailedTests().getAllResults(), index, "fail");
                appendResults(html, ctx.getSkippedTests().getAllResults(), index, "skip");

                html.append("</tbody></table>\n");
            }
        }

        html.append("</body></html>");

        File outDir = new File(outputDirectory);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, "test-cases-report.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, StandardCharsets.UTF_8))) {
            writer.write(html.toString());
        } catch (IOException e) {
            System.err.println("Failed to write report: " + e.getMessage());
        }
    }

    private int appendResults(StringBuilder html, Collection<ITestResult> results, int startIndex, String cssClass) {
        int index = startIndex;
        for (ITestResult r : results) {
            String name = r.getMethod().getMethodName();
            long duration = r.getEndMillis() - r.getStartMillis();
            html.append("<tr>")
                .append("<td>").append(index++).append("</td>")
                .append("<td>").append(escape(name)).append("</td>")
                .append("<td class=\"").append(cssClass).append("\">")
                .append(cssClass.toUpperCase()).append("</td>")
                .append("<td>").append(duration).append("</td>")
                .append("</tr>\n");
        }
        return index;
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}


