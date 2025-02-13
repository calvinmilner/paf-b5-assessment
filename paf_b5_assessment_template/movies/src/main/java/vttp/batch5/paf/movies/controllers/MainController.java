package vttp.batch5.paf.movies.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.services.MovieService;

@Controller
@RequestMapping
public class MainController {

  @Autowired
  private MovieService movieServ;

  // TODO: Task 3
  @GetMapping
  public void getProlificDirectors() {
    List<Document> results = movieServ.getProlificDirectors(1);
    JsonArrayBuilder jArr = Json.createArrayBuilder();
    for (Document d : results) {
      JsonObject j = Json.createObjectBuilder().add("director_name", d.getString("director_name"))
          .add("movies_count", d.getInteger("movies_count", 0)).add("total_revenue", d.getDouble("total_revenue"))
          .add("total_budget", d.getDouble("total_budget")).build();
      jArr.add(j);
    }
  }
  // TODO: Task 4

}
