package soialNetworkApp.service.search;

import lombok.RequiredArgsConstructor;
import soialNetworkApp.api.request.FindPostRq;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.repository.PostsRepository;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class SearchPosts {
    private final PostsRepository postsRepository;
    private final CommonSearchMethods commonSearchMethods;
    private long total;

    public List<Post> findPosts(FindPostRq postRq, int offset, int perPage) throws SQLException {
        StringJoiner personIds = new StringJoiner(",");
        StringJoiner tags = new StringJoiner(",");
        List<Post> postsList;
        if (postRq.getAuthor() != null) {
            List<Person> personList = commonSearchMethods.findPersonByNameOrLastNameContains(postRq.getAuthor());
            personList.forEach(person -> personIds.add("'" + person.getId() + "'"));
        }
        if (postRq.getTags() != null) {
            postRq.getTags().forEach(tag -> tags.add("'" + tag + "'"));
        }
        ResultSet posts = commonSearchMethods.getStatement().executeQuery("SELECT p.id FROM posts AS p" +
                (postRq.getTags() != null ? " JOIN post2tag p2t ON p2t.post_id = p.id JOIN tags t ON p2t.tag_id = t.id" : "") +
                " WHERE" +
                (postRq.getText() != null ? " post_text ~* '" + postRq.getText() + "'" : "") +
                (postRq.getDate_from() != null && postRq.getDate_to() != null ? " AND p.time BETWEEN '" + commonSearchMethods.longToLocalDateTime(postRq.getDate_from()) +
                        "' AND '" + commonSearchMethods.longToLocalDateTime(postRq.getDate_to()) + "'" : "") +
                (postRq.getAuthor() != null ? " AND p.author_id IN (" + (personIds.length() == 0 ? null : personIds) + ")" : "") +
                (postRq.getTags() != null ? " AND t.tag IN (" + tags + ") GROUP BY p.id HAVING COUNT(p2t.tag_id) = " + postRq.getTags().size() : ""));
        List<Long> postsId = commonSearchMethods.getIdsFromResultSet(posts);
        postsList = postsRepository.findAllById(postsId);
        total = postsList.size();
        commonSearchMethods.closeStatementAndConnection();
        posts.close();
        return commonSearchMethods.getPageFromList(postsList, offset, perPage);
    }

    public Long getTotal() {
        return total;
    }
}
