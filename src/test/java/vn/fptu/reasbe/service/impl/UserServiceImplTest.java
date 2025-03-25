package vn.fptu.reasbe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.user.Gender;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private SearchUserRequest searchUserRequest;
    private CreateStaffRequest createStaffRequest;
    private UpdateStaffRequest updateStaffRequest;
    private UserResponse userResponse;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        searchUserRequest = new SearchUserRequest();
        searchUserRequest.setUserName("john_doe");
        searchUserRequest.setFullName("John Doe");
        searchUserRequest.setEmail("john.doe@example.com");

        createStaffRequest = new CreateStaffRequest();
        createStaffRequest.setUserName("john_doe");
        createStaffRequest.setFullName("John Doe");
        createStaffRequest.setEmail("john.doe@example.com");
        createStaffRequest.setPhone("1234567890");
        createStaffRequest.setGender(Gender.MALE);
        createStaffRequest.setPassword("Password@123");
        createStaffRequest.setConfirmPassword("Password@123");

        updateStaffRequest = new UpdateStaffRequest();
        updateStaffRequest.setId(1);
        updateStaffRequest.setUserName("john_doe_updated");
        updateStaffRequest.setFullName("John Doe Updated");
        updateStaffRequest.setEmail("john.doe.updated@example.com");
        updateStaffRequest.setPhone("0987654321");
        updateStaffRequest.setGender(Gender.FEMALE);
        updateStaffRequest.setPassword("NewPassword@123");
        updateStaffRequest.setConfirmPassword("NewPassword@123");

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setUserName("john_doe"); // Ensure this matches the expected value
        userResponse.setFullName("John Doe");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setPhone("1234567890");

        user = new User();
        user.setId(1);
        user.setUserName("john_doe");
        user.setFullName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("hashedPassword");
        user.setPhone("1234567890");

        role = new Role();
        role.setId(1);
        role.setName(RoleName.ROLE_STAFF);
    }

    @Test
    void testSearchUserPagination() {
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

    @Test
    void testCreateNewStaff() {
        // Arrange
        when(userRepository.existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE)).thenReturn(false);
        when(userRepository.existsByUserNameAndStatusEntityEquals(createStaffRequest.getEmail(), StatusEntity.ACTIVE)).thenReturn(false);
        when(passwordEncoder.encode(createStaffRequest.getPassword())).thenReturn("hashedPassword");
        when(roleRepository.findByName(RoleName.ROLE_STAFF)).thenReturn(Optional.of(role));
        when(userMapper.toUser(any(CreateStaffRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse response = userServiceImpl.createNewStaff(createStaffRequest);

        // Assert
        assertEquals("john_doe", response.getUserName()); // Ensure this matches the expected value
        assertEquals("John Doe", response.getFullName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("1234567890", response.getPhone());

        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE);
        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEquals(createStaffRequest.getEmail(), StatusEntity.ACTIVE);
        verify(passwordEncoder, times(1)).encode(createStaffRequest.getPassword());
        verify(roleRepository, times(1)).findByName(RoleName.ROLE_STAFF);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserResponse(any(User.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testCreateNewStaff_UserNameExists() {
        // Arrange
        when(userRepository.existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE)).thenReturn(true);

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.createNewStaff(createStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.usernameExist", exception.getMessage());

        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE);
    }

    @Test
    void testCreateNewStaff_EmailExists() {
        // Arrange
        when(userRepository.existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE)).thenReturn(false);
        when(userRepository.existsByEmailAndStatusEntityEquals(createStaffRequest.getEmail(), StatusEntity.ACTIVE)).thenReturn(true);

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.createNewStaff(createStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.emailExist", exception.getMessage());

        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEquals(createStaffRequest.getUserName(), StatusEntity.ACTIVE);
        verify(userRepository, times(1)).existsByEmailAndStatusEntityEquals(createStaffRequest.getEmail(), StatusEntity.ACTIVE);
    }

    @Test
    void testCreateNewStaff_PasswordsDoNotMatch() {
        // Arrange
        createStaffRequest.setConfirmPassword("DifferentPassword");

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.createNewStaff(createStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.passwordNotMatch", exception.getMessage());
    }

    @Test
    void testUpdateStaff() {
        // Arrange
        when(userRepository.findById(updateStaffRequest.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByUserNameAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getUserName(), StatusEntity.ACTIVE, updateStaffRequest.getId())).thenReturn(false);
        when(userRepository.existsByUserNameAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getEmail(), StatusEntity.ACTIVE, updateStaffRequest.getId())).thenReturn(false);
        when(passwordEncoder.encode(updateStaffRequest.getPassword())).thenReturn("newHashedPassword");

        // Create an updated user object
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUserName("john_doe_updated");
        updatedUser.setFullName("John Doe Updated");
        updatedUser.setEmail("john.doe.updated@example.com");
        updatedUser.setPassword("newHashedPassword");
        updatedUser.setPhone("0987654321");

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Create an updated user response object
        UserResponse updatedUserResponse = new UserResponse();
        updatedUserResponse.setId(1);
        updatedUserResponse.setUserName("john_doe_updated");
        updatedUserResponse.setFullName("John Doe Updated");
        updatedUserResponse.setEmail("john.doe.updated@example.com");
        updatedUserResponse.setPhone("0987654321");

        when(userMapper.toUserResponse(any(User.class))).thenReturn(updatedUserResponse);

        // Act
        UserResponse response = userServiceImpl.updateStaff(updateStaffRequest);

        // Assert
        assertEquals("john_doe_updated", response.getUserName());
        assertEquals("John Doe Updated", response.getFullName());
        assertEquals("john.doe.updated@example.com", response.getEmail());
        assertEquals("0987654321", response.getPhone());

        verify(userRepository, times(1)).findById(updateStaffRequest.getId());
        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getUserName(), StatusEntity.ACTIVE, updateStaffRequest.getId());
        verify(userRepository, times(1)).existsByEmailAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getEmail(), StatusEntity.ACTIVE, updateStaffRequest.getId());
        verify(passwordEncoder, times(1)).encode(updateStaffRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserResponse(any(User.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateStaff_UserNotFound() {
        // Arrange
        when(userRepository.findById(updateStaffRequest.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.updateStaff(updateStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.userNotFound", exception.getMessage());

        verify(userRepository, times(1)).findById(updateStaffRequest.getId());
    }

    @Test
    void testUpdateStaff_UserNameExists() {
        // Arrange
        when(userRepository.findById(updateStaffRequest.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsByUserNameAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getUserName(), StatusEntity.ACTIVE, updateStaffRequest.getId())).thenReturn(true);

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.updateStaff(updateStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.usernameExist", exception.getMessage());

        verify(userRepository, times(1)).findById(updateStaffRequest.getId());
        verify(userRepository, times(1)).existsByUserNameAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getUserName(), StatusEntity.ACTIVE, updateStaffRequest.getId());
    }

    @Test
    void testUpdateStaff_EmailExists() {
        // Arrange
        when(userRepository.findById(updateStaffRequest.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsById( updateStaffRequest.getId())).thenReturn(true);

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.updateStaff(updateStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.emailExist", exception.getMessage());

        verify(userRepository, times(1)).findById(updateStaffRequest.getId());
        verify(userRepository, times(1)).existsByEmailAndStatusEntityEqualsAndIdIsNot(updateStaffRequest.getEmail(), StatusEntity.ACTIVE, updateStaffRequest.getId());
    }

    @Test
    void testUpdateStaff_PasswordsDoNotMatch() {
        // Arrange
        when(userRepository.findById(updateStaffRequest.getId())).thenReturn(Optional.of(user));
        updateStaffRequest.setConfirmPassword("DifferentPassword");

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.updateStaff(updateStaffRequest);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.passwordNotMatch", exception.getMessage());
    }

    @Test
    void testDeactivateStaff() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        Boolean result = userServiceImpl.deactivateStaff(user.getId());

        // Assert
        assertEquals(true, result);

        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeactivateStaff_UserNotFound() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ReasApiException exception = assertThrows(ReasApiException.class, () -> {
            userServiceImpl.deactivateStaff(user.getId());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.userNotFound", exception.getMessage());

        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void loadDetailInfoUser_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userServiceImpl.loadDetailInfoUser(1);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getUserName(), result.getUserName());

        verify(userRepository, times(1)).findById(1);
        verify(userMapper, times(1)).toUserResponse(user);
    }

    @Test
    void loadDetailInfoUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.empty());

        ReasApiException exception = assertThrows(ReasApiException.class, () -> userServiceImpl.loadDetailInfoUser(1));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.userNotFound", exception.getMessage());

        verify(userRepository, times(1)).findById(1);
        verifyNoInteractions(userMapper);
    }
}