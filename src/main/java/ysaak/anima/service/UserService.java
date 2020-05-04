package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;
import ysaak.anima.dao.repository.UserRepository;
import ysaak.anima.data.User;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.UserErrorCode;
import ysaak.anima.rules.UserRules;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(final User user) throws FunctionalException {
        Preconditions.checkNotNull(user, "user is null");
        UserRules.validate(user);

        final User userToSave;

        if (StringUtils.isNotBlank(user.getId())) {
            // Updating data
            userToSave = userRepository.findById(user.getId())
                    .orElseThrow(() -> UserErrorCode.NOT_EXISTING_USER.functional(user.getId()));

            userToSave.setName(user.getName());
        }
        else {
            userToSave = user;
        }

        return userRepository.save(userToSave);
    }

    public Optional<User> findById(final String id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return CollectionUtils.toList(
                userRepository.findAll()
        );
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
