package main.service.search;

import lombok.RequiredArgsConstructor;
import main.api.request.FindPostRq;
import main.model.entities.Person;
import main.model.entities.Post;
import main.repository.PostsRepository;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.StringJoiner;

@Component
@RequiredArgsConstructor
public class SearchPosts {
    private final PostsRepository postsRepository;
    private final CommonSearchMethods commonSearchMethods;
    private Long total;

    public List<Post> findPosts(FindPostRq postRq, int offset, int perPage) throws SQLException {
        Person person = null;
        StringJoiner tags = new StringJoiner(",");
        if (postRq.getAuthor() != null) {
            person = commonSearchMethods.findPersonByNameOrLastNameContains(postRq.getAuthor());
        }
        if (postRq.getTags() != null) {
            postRq.getTags().forEach(tag -> tags.add("'" + tag + "'"));
        }
        ResultSet posts = commonSearchMethods.getStatement().executeQuery("SELECT p.id FROM posts AS p" +
                " JOIN post2Tag p2t ON p2t.post_id = p.id" +
                " JOIN tags t ON p2t.tag_id = t.id" +
                " WHERE" +
                (postRq.getText() != null ? " post_text ~* '" + postRq.getText() + "'" : "") +
                (postRq.getDate_from() != null && postRq.getDate_to() != null ? " AND p.time BETWEEN '" + commonSearchMethods.longToLocalDateTime(postRq.getDate_from()) +
                        "' AND '" + commonSearchMethods.longToLocalDateTime(postRq.getDate_to()) + "'" : "") +
                (postRq.getAuthor() != null ? " AND p.author_id = " + (person == null ? null : person.getId()) : "") +
                (postRq.getTags() != null ? " AND t.tag IN (" + tags + ") GROUP BY p.id HAVING COUNT(p2t.tag_id) = " + postRq.getTags().size() : ""));
        List<Long> postsId = commonSearchMethods.getIdsFromResultSet(posts);
        total = (long) postsId.size();
        commonSearchMethods.closeStatementAndConnection();
        posts.close();
        return commonSearchMethods.getPageFromList(postsRepository.findAllById(postsId), offset, perPage);
    }

    public Long getTotal() {
        return total;
    }
}
