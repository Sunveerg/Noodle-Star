package com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer.TeamMemberResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeamMemberService {

    Flux<TeamMemberResponseModel> getAllTeamMembers();

    Mono<Void> deleteTeamMember(String teamMemberId);
}

