package com.noodlestar.noodlestar.TeamMemberSubdomain.PresentationLayer;

import com.noodlestar.noodlestar.TeamMemberSubdomain.BusinessLayer.TeamMemberService;
import com.noodlestar.noodlestar.UserSubdomain.BusinessLayer.UserService;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserController;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserRequestModel;
import com.noodlestar.noodlestar.UserSubdomain.PresentationLayer.UserResponseModel;
import com.noodlestar.noodlestar.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class TeamMemberControllerUnitTest {

    @Mock
    private TeamMemberService teamMemberService;

    @InjectMocks
    private TeamMemberController teamMemberController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        webTestClient = WebTestClient.bindToController(teamMemberController).build(); // Bind controller manually
    }

    @Test
    void testGetAllTeamMembers() {
        // Given
        TeamMemberResponseModel teamMemberResponseModel1 = new TeamMemberResponseModel("1", "John Doe", "Cook", "This is John's bio", "http://example.com/photo1.jpg");
        TeamMemberResponseModel teamMemberResponseModel2 = new TeamMemberResponseModel("2", "Jane Smith", "Chef", "This is Jane's bio", "http://example.com/photo2.jpg");

        List<TeamMemberResponseModel> teamMemberResponseModels = Arrays.asList(teamMemberResponseModel1, teamMemberResponseModel2);

        // Mock the service call
        when(teamMemberService.getAllTeamMembers()).thenReturn(Flux.fromIterable(teamMemberResponseModels));

        // Perform GET request to the controller endpoint
        webTestClient.get()
                .uri("/api/v1/team-members")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TeamMemberResponseModel.class)
                .hasSize(2)
                .contains(teamMemberResponseModel1, teamMemberResponseModel2);

        // Verifying if the service method was called
        verify(teamMemberService, times(1)).getAllTeamMembers();
    }

}

