package soialNetworkApp.repository;

import soialNetworkApp.model.entities.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {
    @Query(value = "SELECT * FROM captcha WHERE secret_code = :secretCode", nativeQuery = true)
    Optional<Captcha> findCaptchaBySecretCode(@Param("secretCode") String secretCode);

    @Query(value = "SELECT * FROM captcha WHERE time < (NOW() - INTERVAL '1 HOUR')", nativeQuery = true)
    List<Captcha> findOldCaptchas();
}

