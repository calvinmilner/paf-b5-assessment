package vttp.batch5.paf.movies.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import vttp.batch5.paf.movies.services.MovieService;

@Controller
@RequestMapping("/api")
public class MainController {

  @Autowired
  private MovieService movieServ;

  // TODO: Task 3

  @GetMapping("/summary")
  public ResponseEntity<String> getProlificDirectors(@RequestParam int count) {
    List<Document> results = movieServ.getProlificDirectors(1);
    JsonArrayBuilder ja = Json.createArrayBuilder();
    for (Document d : results) {
      JsonObject j = Json.createObjectBuilder().add("director_name", d.getString("director_name"))
          .add("movies_count", d.getInteger("movies_count", 0)).add("total_revenue", d.getDouble("total_revenue"))
          .add("total_budget", d.getDouble("total_budget")).add("profit_loss", d.getDouble("profit_loss")).build();
      ja.add(j);
    }
    JsonArray jArr = ja.build();
    return ResponseEntity.ok(jArr.toString());
  }

  // TODO: Task 4
}
