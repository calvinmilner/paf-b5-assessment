package vttp.batch5.paf.movies.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.File;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.json.data.JsonDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;
import net.sf.jasperreports.pdf.SimplePdfReportConfiguration;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;

@Service
public class MovieService {

  @Autowired
  private MongoMovieRepository mongoMovieRepo;

  // TODO: Task 2

  // TODO: Task 3
  // You may change the signature of this method by passing any number of
  // parameters
  // and returning any type
  public List<Document> getProlificDirectors(int count) {
    List<Document> newDocResults = new LinkedList<>();
    List<Document> results = mongoMovieRepo.getProlificDirectors(count);
    for (Document r : results) {
      Double value = 0.0;
      value = r.getDouble("total_revenue") - r.getDouble("total_budget");
      r.append("profit_loss", value);
      newDocResults.add(r);
    }
    return newDocResults;
  }

  // TODO: Task 4
  // You may change the signature of this method by passing any number of
  // parameters
  // and returning any type
  public void generatePDFReport() throws Exception {
    JsonDataSource reportDS = new JsonDataSource(new File("/data/director_movies_report.jrxml"));
    JsonDataSource directorsDS = new JsonDataSource(new File("/data/director_movies_report.jasper"));
    Map<String, Object> params = new HashMap<>();
    params.put("DIRECTOR_TABLE_DATASET", directorsDS);
    JasperReport report = JasperCompileManager.compileReport("");
    JasperPrint print = JasperFillManager.fillReport(report, params, reportDS);
    
    JRPdfExporter exporter = new JRPdfExporter();
    exporter.setExporterInput(new SimpleExporterInput(print));
    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("Report.pdf"));
    SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
    reportConfig.setSizePageToContent(true);
    reportConfig.setForceLineBreakPolicy(false);
    SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
    exportConfig.setMetadataAuthor("calvinmilner");
    exportConfig.setEncrypted(true);
    exportConfig.setAllowedPermissionsHint("PRINTING");
    exporter.setConfiguration(reportConfig);
    exporter.setConfiguration(exportConfig);
    exporter.exportReport();
  }

}
