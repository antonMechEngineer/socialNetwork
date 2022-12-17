package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Notification;
import soialNetworkApp.model.entities.Person;
import soialNetworkApp.model.entities.interfaces.Notificationed;
import soialNetworkApp.model.enums.NotificationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notifications WHERE person_id = :id", nativeQuery = true)
    void notificationDelete(@Param("id") long id);

    Page<Notification> findAllByPersonAndIsReadIsFalse(Person person, Pageable pageable);

    List<Notification> findAllByPersonAndIsReadIsFalse(Person person);

    @Query(value = "FROM Notification WHERE notificationType = :type AND entity = :entity")
    Optional<Notification> findNotificationByEntity(@Param("type") NotificationTypes type, @Param("entity") Notificationed entity);
}