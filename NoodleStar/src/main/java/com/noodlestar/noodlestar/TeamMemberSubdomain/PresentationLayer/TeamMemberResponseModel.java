package com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TeamMemberResponseModel {
    String teamMemberId;
    String name;
    String role;
    String bio;
    String photoUrl;
}
