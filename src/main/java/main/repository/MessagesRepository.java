package main.repository;
import main.model.entities.Dialog;
import main.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    Optional<Message> findMessageByDialogId(long dialogId);
}
