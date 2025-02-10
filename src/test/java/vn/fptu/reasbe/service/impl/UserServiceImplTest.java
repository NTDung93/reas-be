package vn.fptu.reasbe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.utils.mapper.UserMapper;

/**
 *
 * @author ntig
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private SearchUserRequest searchUserRequest;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    public void setUp() {
        searchUserRequest = new SearchUserRequest();
        searchUserRequest.setUserName("john_doe");
        searchUserRequest.setFullName("John Doe");
        searchUserRequest.setEmail("john.doe@example.com");

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setUserName("john_doe");
        userResponse.setFullName("John Doe");
        userResponse.setEmail("john.doe@example.com");

        user = new User();
        user.setId(1);
        user.setUserName("john_doe");
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    public void testSearchUserPagination() {
        // Arrange
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "userName";
        String sortDir = "ASC";

        List<User> users = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.searchUserPagination(any(SearchUserRequest.class), any(Pageable.class))).thenReturn(userPage);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        BaseSearchPaginationResponse<UserResponse> response = userServiceImpl.searchUserPagination(pageNo, pageSize, sortBy, sortDir, searchUserRequest);

        // Assert
        assertEquals(1, response.getContent().size());
        assertEquals("john_doe", response.getContent().get(0).getUserName());
        assertEquals("John Doe", response.getContent().get(0).getFullName());
        assertEquals("john.doe@example.com", response.getContent().get(0).getEmail());

        verify(userRepository, times(1)).searchUserPagination(any(SearchUserRequest.class), any(Pageable.class));
        verify(userMapper, times(1)).toUserResponse(any(User.class));
    }
}