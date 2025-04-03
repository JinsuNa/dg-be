package com.project.daeng_geun.dto;

import com.project.daeng_geun.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SenderReceiverDTO {
    private Long id;
    private String nickname;
    private String image;

    public static SenderReceiverDTO fromEntity(User user) {
        return SenderReceiverDTO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .image(user.getImage())
                .build();
    }

}
