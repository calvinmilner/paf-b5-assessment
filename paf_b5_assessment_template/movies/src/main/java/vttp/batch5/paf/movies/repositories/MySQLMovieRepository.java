package vttp.batch5.paf.movies.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.models.SqlMovie;

@Repository
public class MySQLMovieRepository {

  @Autowired
  private JdbcTemplate template;

  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public int[] batchInsertMovies(List<SqlMovie> movies) {
    List<Object[]> params = movies.stream()
        .map(m -> new Object[] { m.getImdb_id(), m.getVote_average(), m.getVote_count(), m.getRelease_date(),
            m.getRevenue(), m.getBudget(), m.getRuntime() })
        .collect(Collectors.toList());
    int[] added = template.batchUpdate(
        "insert into imdb(imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime) values(?, ?, ?, ?, ?, ?, ?)",
        params);
    return added;
  }

  // TODO: Task 3
  // 
  // select director_name, movies_count, total_revenue, total_budget from imdb limit 'count' order by movies_count desc
  public List<SqlMovie> getImdb(int count) {
    List<SqlMovie> result = new LinkedList<>();
    SqlRowSet rs = template.queryForRowSet("select director_name, movies_count, total_revenue, total_budget from imdb limit ? order by movies_count desc", count);
    while(rs.next()) {
      SqlMovie movie = new SqlMovie(rs.getString("imdb_id"), rs.getDouble("vote_average"), rs.getInt("vote_count"), rs.getString("release_date"), rs.getInt("revenue"), rs.getInt("budget"), rs.getInt("runtime"));
      result.add(movie);
    }
    return result;
  }
}
