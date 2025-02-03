package com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMember {


    @Id
    private String id;
    private String teamMemberId;
    private String name;
    private String role;
    private String bio;
    private String photoUrl;


}
