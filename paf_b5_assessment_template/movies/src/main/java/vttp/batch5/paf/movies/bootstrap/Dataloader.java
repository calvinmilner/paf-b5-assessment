package vttp.batch5.paf.movies.bootstrap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Component
public class Dataloader implements CommandLineRunner {

  @Value("${zip.name}")
  private String zipName;

  @Autowired
  private MySQLMovieRepository mySqlMovieRepo;

  @Autowired
  private MongoMovieRepository mongoMovieRepo;

  // TODO: Task 2
  @Override
  public void run(String... args) throws Exception {

    // String zipName = "/data/movies_post_2010.zip";
    List<Movie> movies = new LinkedList<>();
    ZipFile zipFile = new ZipFile(new File(zipName));
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      InputStream stream = zipFile.getInputStream(entry);
      InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
      Scanner inputStream = new Scanner(reader);

      while (inputStream.hasNext()) {
        String data = inputStream.nextLine(); // Gets a whole line
        JsonReader jreader = Json.createReader(new StringReader(data));
        JsonObject j = jreader.readObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Movie m = new Movie();
        double vote_average = j.containsKey("vote_average")
            && j.get("vote_average").getValueType() == JsonValue.ValueType.NUMBER
                ? (j.getJsonNumber("vote_average").doubleValue())
                : 0.0;
        float converted_vote_average = (float) vote_average;
        int vote_count = j.containsKey("vote_count") && j.get("vote_count").getValueType() == JsonValue.ValueType.NUMBER
            ? j.getInt("vote_count")
            : 0;
        String released_date = j.containsKey("released_date")
            && j.get("released_date").getValueType() == JsonValue.ValueType.STRING ? j.getString("released_date") : "";
        Integer revenue = j.containsKey("revenue") && j.get("revenue").getValueType() == JsonValue.ValueType.NUMBER
            ? j.getInt("revenue")
            : 0;
        Integer budget = j.containsKey("budget") && j.get("budget").getValueType() == JsonValue.ValueType.NUMBER
            ? j.getInt("budget")
            : 0;
        int runtime = j.containsKey("runtime") && j.get("runtime").getValueType() == JsonValue.ValueType.NUMBER
            ? j.getInt("runtime")
            : 0;
        Date cutOffDate = sdf.parse("2017-12-31");
        Date retrievedDate = sdf.parse(released_date);
        if (retrievedDate.after(cutOffDate)) {
          m.setVote_average(converted_vote_average);
          m.setVote_count(vote_count);
          m.setRelease_date(released_date);
          m.setRevenue(revenue);
          m.setBudget(budget);
          m.setRuntime(runtime);
          // if(movies.size() < 25) {
          // movies.add(m);
          // }
          // else {
          //  try {
          //    mySqlMovieRepo.batchInsertMovies(movies);
          //    mongoMovieRepo.batchInsertMovies(movies);
          //    movies.clear();
          //  } catch (Exception ex) {
          //    mongoMovieRepository.logError( ,ex);
          // }
        }

        // System.out.printf(">>> %s\n", data);
      }
      inputStream.close();
      reader.close();
      stream.close();
    }

  }

}
