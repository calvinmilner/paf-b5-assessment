package vttp.batch5.paf.movies.repositories;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.MongoMovie;

@Repository
public class MongoMovieRepository {

    @Autowired
    private MongoTemplate template;

    // TODO: Task 2.3
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // db.imdb.insertMany()
    //
    public void batchInsertMovies(List<MongoMovie> data) {
        List<Document> docsToInsert = new LinkedList<>();
        for (MongoMovie mm : data) {
            Document toInsert = new Document().append("_id", mm.getImdb_id()).append("title", mm.getTitle())
                    .append("directors", mm.getDirectors()).append("overview", mm.getTagline())
                    .append("genres", mm.getGenres()).append("imdb_rating", mm.getImdb_rating())
                    .append("imdb_votes", mm.getImdb_votes());
            docsToInsert.add(toInsert);
        }
        template.insert(docsToInsert, "imdb");
    }

    // TODO: Task 2.4
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // db.errors.insert
    //
    public void logError(List<String> ids, Exception ex) {
        Document newError = new Document().append("imdb_ids", ids).append("error", ex.getMessage()).append("timestamp",
                new Date());
        template.insert(newError, "errors");
    }

    // TODO: Task 3
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // db.imdb.find( {} ).projection( { director_name: 1, movies_count: 1,
    // total_revenue: 1, total_budget: 1} ).limit('input').sort( { movies_count: -1}
    // )
    //

    public List<Document> getProlificDirectors(int count) {
        Query query = Query.query(Criteria.where("{}")).with(Sort.by(Sort.Direction.DESC, "movies_count")).limit(count);
        query.fields().exclude("_id").include("director_name", "movies_count", "total_revenue", "total_budget");
        List<Document> results = template.find(query, Document.class, "imdb");
        return results;
    }
}
