package Backend.HIFI.user;


import Backend.HIFI.user.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public void userFollow(User follower, User following) {
        followRepository.save(follower, following);
    }

    public Long saveUser(User user) {
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        User findUser = userRepository.findUserById(user.getId());
        if (findUser == user) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
}