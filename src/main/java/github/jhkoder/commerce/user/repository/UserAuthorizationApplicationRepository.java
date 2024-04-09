package github.jhkoder.commerce.user.repository;

import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.domain.UserAuthorizationApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAuthorizationApplicationRepository extends JpaRepository<UserAuthorizationApplication, Long> {

    Optional<UserAuthorizationApplication> findByUser(User user);
    List<UserAuthorizationApplication> findByIsDeleted(OracleBoolean isDeleted);

}
