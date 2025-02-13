package vttp.batch5.paf.movies.bootstrap;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch5.paf.movies.models.MongoMovie;
import vttp.batch5.paf.movies.models.SqlMovie;
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
    List<SqlMovie> sql_data = new LinkedList<>();
    List<MongoMovie> mongo_data = new LinkedList<>();
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
        SqlMovie m = new SqlMovie();
        MongoMovie mm = new MongoMovie();
        double vote_average = j.containsKey("vote_average")
            && j.get("vote_average").getValueType() == JsonValue.ValueType.NUMBER
                ? (j.getJsonNumber("vote_average").doubleValue())
                : 0;
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
        String retrievedDate = j.getString("release_date");
        if (retrievedDate.isBlank())
          continue;
        LocalDate checkDate = LocalDate.parse(retrievedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (checkDate.getYear() >= 2018) {
          m.setVote_average(vote_average);
          m.setVote_count(vote_count);
          m.setRelease_date(released_date);
          m.setRevenue(revenue);
          m.setBudget(budget);
          m.setRuntime(runtime);
          mm.setImdb_id(j.getString("imdb_id"));
          mm.setTitle(j.getString("title"));
          mm.setDirectors(j.getString("director"));
          mm.setOverview(j.getString("overview"));
          mm.setTagline(j.getString("tagline"));
          mm.setGenres(j.getString("genres"));
          mm.setImdb_rating(j.getInt("imdb_rating"));
          mm.setImdb_votes(j.getInt("imdb_votes"));
          if (sql_data.size() < 25 && mongo_data.size() < 25) {
            sql_data.add(m);
            mongo_data.add(mm);
          } else {
            try {
              mySqlMovieRepo.batchInsertMovies(sql_data);
              mongoMovieRepo.batchInsertMovies(mongo_data);
              sql_data.clear();
              mongo_data.clear();
            } catch (Exception ex) {
              List<String> errorId = new LinkedList<>();
              errorId.add(j.getString("imdb_id"));
              mongoMovieRepo.logError(errorId, ex);
            }
          }
        }
        // System.out.printf(">>> %s\n", data);
      }
      inputStream.close();
      reader.close();
      stream.close();
      zipFile.close();
    }
  }
}
