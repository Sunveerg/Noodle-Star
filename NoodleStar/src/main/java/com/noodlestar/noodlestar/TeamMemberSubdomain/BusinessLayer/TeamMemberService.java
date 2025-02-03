package com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer.TeamMemberResponseModel;
import reactor.core.publisher.Flux;

public interface TeamMemberService {

    Flux<TeamMemberResponseModel> getAllTeamMembers();
}

