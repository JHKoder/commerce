package github.jhkoder.commerce.store.repository;


import github.jhkoder.commerce.store.domain.Store;
import github.jhkoder.commerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByUser(User user);

}
