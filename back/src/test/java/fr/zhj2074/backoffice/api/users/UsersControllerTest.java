package fr.zhj2074.backoffice.api.users;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersControllerTest extends IntegrationTest {

    private ListUsersCommand listUsers = mock(ListUsersCommand.class);
    private GetUserCommand getUser = mock(GetUserCommand.class);
    private CreateUserCommand createUser = mock(CreateUserCommand.class);
    private UpdateUserCommand updateUser = mock(UpdateUserCommand.class);
    private DeleteUserCommand deleteUser = mock(DeleteUserCommand.class);
    private UniqueEmailConstraintValidator uniqueEmailValidator = mock(UniqueEmailConstraintValidator.class);

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        ControllerTestAdvice controllerTestAdvice = new ControllerTestAdvice();
        controllerTestAdvice.uniqueEmailConstraintValidator = uniqueEmailValidator;
        mockMvc = MockMvcBuilders.standaloneSetup(new UsersController(listUsers, getUser, createUser, updateUser, deleteUser))
            .setControllerAdvice(controllerTestAdvice).build();
    }

    @Test
    public void get_users() throws Exception {
        when(listUsers.all()).thenReturn(newArrayList(new User(1, "foo", "login")));

        mockMvc.perform(get("/api/users")).andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void get_user_by_existing_id() throws Exception {
        when(getUser.byId(1)).thenReturn(Optional.of(new User(1, "foo", "login")));

        mockMvc.perform(get("/api/users/1")).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void get_user_given_unknown_id() throws Exception {
        when(getUser.byId(9)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/9")).andExpect(status().isNotFound());
    }

    @Test
    public void create_user() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(true);
        UserDataRep userRep = new UserDataRep("valid@email.com", "valid");

        mockMvc.perform(post("/api/users")
            .content(mapper.writeValueAsString(userRep))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void create_user_given_an_invalid_email_format() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(false);

        mockMvc.perform(post("/api/users")
            .content("{\"email\": \"not an email\"}")
            .header("Content-Type", "application/json"))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void create_user_given_non_unique_email() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(false);

        mockMvc.perform(post("/api/users")
            .content("{\"email\": \"existing@mail.com\"}")
            .header("Content-Type", "application/json"))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void update_user() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(true);

        mockMvc.perform(put("/api/users/1")
                .content("{\"email\": \"foo@foo.com\", \"login\": \"login\"}")
            .header("Content-Type", "application/json"))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void update_user_given_an_invalid_email() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(false);

        mockMvc.perform(put("/api/users/1")
            .content("{\"email\": \"not an email\"}")
            .header("Content-Type", "application/json"))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void update_user_given_non_unique_email() throws Exception {
        when(uniqueEmailValidator.isValid(anyString(), any())).thenReturn(false);

        mockMvc.perform(put("/api/users/1")
            .content("{\"email\": \"existing@mail.com\"}")
            .header("Content-Type", "application/json"))
            .andExpect(status().isBadRequest());
    }
}
