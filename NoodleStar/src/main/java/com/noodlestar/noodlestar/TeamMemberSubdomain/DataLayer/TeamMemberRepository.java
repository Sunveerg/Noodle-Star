package com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TeamMemberRepository extends ReactiveMongoRepository<TeamMember, String> {
    Mono<TeamMember> findByTeamMemberId(String teamMemberId);

}
