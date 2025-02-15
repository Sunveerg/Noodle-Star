package com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer;


import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMemberRepository;
import com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer.TeamMemberResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    public TeamMemberServiceImpl(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    @Override
    public Flux<TeamMemberResponseModel> getAllTeamMembers() {
        return teamMemberRepository.findAll()
                .map(EntityDTOUtil::toTeamMemberResponseDTO);
    }

    @Override
    public Mono<Void> deleteTeamMember(String teamMemberId) {
        return teamMemberRepository.findByTeamMemberId(teamMemberId)
                .switchIfEmpty(Mono.error(new NotFoundException("Re with ID '" + teamMemberId + "' not found.")))
                .flatMap(teamMemberRepository::delete);
    }

}

