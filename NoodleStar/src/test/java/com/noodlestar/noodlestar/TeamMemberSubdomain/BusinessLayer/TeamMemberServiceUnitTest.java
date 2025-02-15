package com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer;

import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMember;
import com.noodlestar.noodlestar.TeamMemberSubdomain.DataLayer.TeamMemberRepository;
import com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer.TeamMemberResponseModel;
import com.noodlestar.noodlestar.UserSubdomain.BusinessLayer.UserServiceImpl;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.User;
import com.noodlestar.noodlestar.UserSubdomain.DataLayer.UserRepository;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserRequestModel;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.auth0.Auth0Service;
import com.noodlestar.noodlestar.utils.EntityDTOUtil;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamMemberServiceUnitTest {


    @Mock
    private TeamMemberRepository teamMemberRepository;

    @InjectMocks
    private TeamMemberServiceImpl teamMemberService;

    TeamMember teamMember1 = TeamMember.builder()
            .teamMemberId(UUID.randomUUID().toString())
            .name("John Doe")
            .role("Cook")
            .bio("This is John's bio")
            .photoUrl("http://example.com/photo1.jpg")
            .build();

    TeamMember teamMember2 = TeamMember.builder()
            .teamMemberId(UUID.randomUUID().toString())
            .name("Jane Smith")
            .role("Chef")
            .bio("This is Jane's bio")
            .photoUrl("http://example.com/photo2.jpg")
            .build();

    @Test
    void testGetAllTeamMembers() {
        // Given
        TeamMember teamMember1 = TeamMember.builder()
                .teamMemberId(UUID.randomUUID().toString())
                .name("John Doe")
                .role("Cook")
                .bio("This is John's bio")
                .photoUrl("http://example.com/photo1.jpg")
                .build();

        TeamMember teamMember2 = TeamMember.builder()
                .teamMemberId(UUID.randomUUID().toString())
                .name("Jane Smith")
                .role("Chef")
                .bio("This is Jane's bio")
                .photoUrl("http://example.com/photo2.jpg")
                .build();

        // Assuming the repository is mocked and set up to return the list of team members
        List<TeamMember> teamMembers = Arrays.asList(teamMember1, teamMember2);

        // Mock the repository call
        when(teamMemberRepository.findAll()).thenReturn(Flux.fromIterable(teamMembers));

        // When
        Flux<TeamMemberResponseModel> result = teamMemberService.getAllTeamMembers();

        // Then
        StepVerifier.create(result)
                .expectNextMatches(teamMember -> teamMember.getName().equals("John Doe"))
                .expectNextMatches(teamMember -> teamMember.getName().equals("Jane Smith"))
                .verifyComplete();
    }

    @Test
    void whenDeleteTeamMember_thenDeletesTeamMember() {

        // Arrange
        String teamMemberId = teamMember1.getTeamMemberId();

        when(teamMemberRepository.findByTeamMemberId(teamMemberId)).thenReturn(Mono.just(teamMember1));

        when(teamMemberRepository.delete(teamMember1)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = teamMemberService.deleteTeamMember(teamMemberId);

        // Assert
        result.subscribe();

        // Verify
        verify(teamMemberRepository, times(1)).findByTeamMemberId(teamMemberId);
        verify(teamMemberRepository, times(1)).delete(teamMember1);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void whenDeleteTeamMemberNotFound_thenThrowException() {
        // Arrange
        String teamMemberId = "nonexistentId";

        when(teamMemberRepository.findByTeamMemberId(teamMemberId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(teamMemberService.deleteTeamMember(teamMemberId))
                .expectError(NotFoundException.class)
                .verify();

        // Verify
        verify(teamMemberRepository, times(1)).findByTeamMemberId(teamMemberId);
        verify(teamMemberRepository, times(0)).delete(any());
    }

}