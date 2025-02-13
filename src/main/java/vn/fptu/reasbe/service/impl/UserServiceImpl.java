package vn.fptu.reasbe.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.SearchUserRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

/**
 *
 * @author ntig
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public BaseSearchPaginationResponse<UserResponse> searchUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchUserRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return BaseSearchPaginationResponse.of(userRepository.searchUserPagination(request, pageable).map(userMapper::toUserResponse));
    }

    @Override
    public UserResponse createNewStaff(CreateStaffRequest request) {
        validateCreateStaffRequest(request);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(getStaffRole());
        User savedUser = userRepository.save(user);
        sendAccountCreationEmail(savedUser, request.getPassword(), false);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse updateStaff(UpdateStaffRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotFound"));
        validateUpdateStaffRequest(request);
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        sendAccountCreationEmail(savedUser, request.getPassword(), true);
        return userMapper.toUserResponse(savedUser);
    }

    private Role getStaffRole() {
        return roleRepository.findByName(RoleName.ROLE_STAFF)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.roleNotFound"));
    }

    private void sendAccountCreationEmail(User user, String passwordBeforeHashed, boolean isUpdate) {
        String subject = isUpdate ? "[REAS] - Account Successfully Updated" : "[REAS] - Account Successfully Created";
        String greeting = isUpdate ? "updated" : "created";
        String content = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; }" +
                "th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "body { font-family: Arial, sans-serif; }" +
                ".highlight { font-weight: bold; color: red; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<p class='greeting'>Hi, " + user.getFullName() + ",</p>" +
                "<p>Your REAS system account has been successfully " + greeting + ".</p>" +
                "<p>Please log into the system with the following information:</p>" +
                "<table>" +
                "<tr><th>Username</th><td>" + user.getUserName() + "</td></tr>" +
                "<tr><th>Password</th><td class='highlight'>" + passwordBeforeHashed + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(user.getEmail(), subject, content);
    }

    private void validateCreateStaffRequest(CreateStaffRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUserName(request.getUserName()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.usernameExist");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.emailExist");
        }
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.passwordNotMatch");
        }
    }

    private void validateUpdateStaffRequest(UpdateStaffRequest request) {
        if (Boolean.TRUE.equals(userRepository.existsByUserNameAndIdIsNot(request.getUserName(), request.getId()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.usernameExist");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmailAndIdIsNot(request.getEmail(), request.getId()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.emailExist");
        }
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.passwordNotMatch");
        }
    }
}
