package soialNetworkApp.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import soialNetworkApp.api.request.FindPostRq;
import soialNetworkApp.model.entities.Friendship;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.Post;
import soialNetworkApp.model.enums.FriendshipStatusTypes;
import soialNetworkApp.repository.FriendshipsRepository;
import soialNetworkApp.repository.PersonsRepository;
import soialNetworkApp.repository.PostsRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchPosts {

    private final PostsRepository postsRepository;

    private final PersonsRepository personsRepository;

    private final FriendshipsRepository friendshipsRepository;

    private final CommonSearchMethods commonSearchMethods;

    public Page<Post> findPosts(FindPostRq postRq, int offset, int perPage) {
        List<Person> personsWhoBLockedMe = getPersonsWhoBlockedMe();
        Pageable pageable = PageRequest.of(offset, perPage);
        Specification<Post> specification = PostSpecification.textLike(postRq.getText());
        if (personsWhoBLockedMe.size() != 0) {
            specification = specification.and(PostSpecification.excludeBlockedPosts(personsWhoBLockedMe));
        }
        if (postRq.getTags() != null) {
            specification = specification.and(PostSpecification.tagsContains(postRq.getTags()));
        }
        if (postRq.getDate_to() != null || postRq.getDate_from() != null) {
            specification = specification.and(PostSpecification
                    .datesBetween(commonSearchMethods.longToLocalDateTime(postRq.getDate_from()),
                            commonSearchMethods.longToLocalDateTime(postRq.getDate_to())));
        }
        if (postRq.getAuthor() != null) {
            List<Person> persons = commonSearchMethods.findPersonByNameAndLastNameContains(postRq.getAuthor());
            specification = specification.and(PostSpecification.postAuthorsIs(persons));
        }
        return postsRepository.findAll(specification, pageable);
    }

    private List<Person> getPersonsWhoBlockedMe() {
        Person me = personsRepository.findPersonByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        List<Friendship> friendships = friendshipsRepository.findFriendshipsByDstPersonIdAndFriendshipStatus(me.getId(), FriendshipStatusTypes.BLOCKED);
        List<Person> personsWhoBLockedMe = new ArrayList<>();
        friendships.forEach(friendship -> personsWhoBLockedMe.add(friendship.getSrcPerson()));
        return personsWhoBLockedMe;
    }
}
