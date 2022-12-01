package main.repository;

import main.model.entities.Notification;
import main.model.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notifications WHERE person_id = :id", nativeQuery = true)
    void notificationDelete(@Param("id") long id);

    Page<Notification> findAllByPersonAndIsReadIsFalse(Person person, Pageable pageable);

    List<Notification> findAllByPersonAndIsReadIsFalse(Person person);
}
