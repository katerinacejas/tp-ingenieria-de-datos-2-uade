package com.poliglota.repository.mongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.poliglota.model.mongo.User;
import java.util.Optional;
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
