package vttp.batch5.paf.movies.services;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import vttp.batch5.paf.movies.repositories.MongoMovieRepository;

public class MovieService {

  @Autowired
  private MongoMovieRepository mongoMovieRepo;
  // TODO: Task 2

  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public List<Document> getProlificDirectors(int num) {
    return mongoMovieRepo.getProlificDirectors(num);
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() {

  }

}
