package com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer;


import com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer.TeamMemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/team-members")
@Validated
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "https://plankton-app-v6rpd.ondigitalocean.app"}, allowedHeaders = "content-Type", allowCredentials = "true")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    @GetMapping("")
    public Flux<TeamMemberResponseModel> getAllTeamMembers() {
        return teamMemberService.getAllTeamMembers();
    }

}
