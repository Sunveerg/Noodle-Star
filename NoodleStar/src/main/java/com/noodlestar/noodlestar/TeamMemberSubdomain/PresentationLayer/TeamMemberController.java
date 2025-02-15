package com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer;


import com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer.TeamMemberService;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @DeleteMapping("/{teamMemberId}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable String teamMemberId) {
        return teamMemberService.deleteTeamMember(teamMemberId)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .onErrorResume(NotFoundException.class, e -> Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
    }

}
