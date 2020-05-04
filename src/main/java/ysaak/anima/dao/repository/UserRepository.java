package ysaak.anima.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ysaak.anima.data.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
