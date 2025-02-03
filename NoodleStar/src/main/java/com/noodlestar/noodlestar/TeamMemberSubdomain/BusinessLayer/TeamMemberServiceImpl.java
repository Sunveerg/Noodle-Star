package com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer;


import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMemberRepository;
import com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer.TeamMemberResponseModel;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


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

}

