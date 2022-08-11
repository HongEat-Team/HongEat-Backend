package Backend.HIFI.auth.dto;

import Backend.HIFI.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private String name;
    private String description;
    private String image;
    private Boolean anonymous;
    // TODO 리뷰, 팔로우 추가 필요

    public UserProfileDto toUserProfileDto(User user) {
        // 비공개 유저 처리 해야함
        // 얼마나 비공개 할 것인지?
        return UserProfileDto.builder()
                .name(user.getName())
                .description(user.getDescription())
                .image(user.getImage())
                .anonymous(user.getAnonymous())
                .build();
    }
}
