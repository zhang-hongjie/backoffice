package fr.zhj2074.backoffice.api.users;

import fr.zhj2074.backoffice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
public class UsersController {

    private final ListUsersCommand listUsers;
    private final GetUserCommand getUser;
    private final CreateUserCommand createUser;
    private final UpdateUserCommand updateUser;
    private final DeleteUserCommand deleteUser;

    @Autowired
    public UsersController(ListUsersCommand listUsers,
                           GetUserCommand getUser,
                           CreateUserCommand createUser,
                           UpdateUserCommand updateUser,
                           DeleteUserCommand deleteUser) {
        this.listUsers = listUsers;
        this.getUser = getUser;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.deleteUser = deleteUser;
    }

    @RequestMapping(method = GET, path = "/api/users")
    public List<UserRep> getUsers() {
        return listUsers.all().stream().map(UserRep::new).collect(toList());
    }

    @RequestMapping(method = GET, path = "/api/users/{id}")
    public UserRep getUser(@PathVariable int id) {
        User user = getUser.byId(id).orElseThrow(ResourceNotFoundException::new);
        return new UserRep(user);
    }

    @RequestMapping(method = POST, path = "/api/users")
    public void createUser(@Valid @RequestBody UserDataRep data) {
        createUser.execute(data);
    }

    @RequestMapping(method = PUT, path = "/api/users/{id}")
    public void updateUser(@PathVariable int id, @Valid @RequestBody UserDataRep data) {
        try {
            updateUser.execute(id, data);
        } catch (JdbcUpdateAffectedIncorrectNumberOfRowsException ignored) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(method = DELETE, path = "/api/users/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            deleteUser.execute(id);
        } catch (JdbcUpdateAffectedIncorrectNumberOfRowsException ignored) {
            throw new ResourceNotFoundException();
        }
    }
}
