package com.project.daeng_geun.dto;



import com.project.daeng_geun.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchDTO {

    private Long id;
    private Long likeCount;
    private String userName;
    private String petBreed;
    private Integer petAge;
    private String petGender;
    private String petPersonality;
    private String image;
    private String location;


    public static MatchDTO fromEntity(User user)
    {

        return MatchDTO.builder()
                .id(user.getId())
                .likeCount(user.getLikeCount())
                .userName(user.getNickname())
                .petBreed(user.getPetBreed())
                .petAge(user.getPetAge())
                .petGender(user.getPetGender())
                .petPersonality(user.getPetPersonality())
                .image(user.getImage())
                .location(user.getLocation())
                .build();
    }

}
