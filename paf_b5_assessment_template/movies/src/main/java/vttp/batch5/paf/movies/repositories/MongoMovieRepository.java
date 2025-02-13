package vttp.batch5.paf.movies.repositories;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.Movie;

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
    // public void batchInsertMovies(List<Movie> movies) {
    //     Document toInsert = new Document().append;
    //     Document newDoc = template.insert(toInsert, "imdb");
    //     ObjectId id = newDoc.getObjectId(newDoc);
    //     List<Document> docsToInsert = new LinkedList<>();
    //     docsToInsert.add(newDoc);
    //     template.insert(docsToInsert, "imdb");

    // }

    // TODO: Task 2.4
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // db.errors.insert
    //
    public void logError(List<String> ids, Exception ex) {
        Document newError = new Document().append("imdb_ids", ids).append("error", ex.getMessage()).append("timestamp", new Date());
        template.insert(newError, "errors");
    }

    // TODO: Task 3
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    // db.imdb.find( {} ).projection( { director_name: 1, movies_count: 1, total_revenue: 1, total_budget: 1} ).limit('input').sort( { movies_count: -1} )
    //

    public List<Document> getProlificDirectors(int num) {
        Query query = Query.query(new Criteria());
        List<Document> results = template.find(query, Document.class, "imdb");
        return results;
    }
}
