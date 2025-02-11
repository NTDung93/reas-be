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
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.UserRepository;
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

    @Override
    public BaseSearchPaginationResponse<UserResponse> searchUserPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchUserRequest request) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return BaseSearchPaginationResponse.of(userRepository.searchUserPagination(request, pageable).map(userMapper::toUserResponse));
    }

    @Override
    public UserResponse createNewStaff(CreateStaffRequest request) {
        validateUser(request);

        User user = userMapper.toUser(request);

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        Role staffRole = roleRepository.findByName(RoleName.ROLE_STAFF)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.roleNotFound"));
        user.setRole(staffRole);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    private void validateUser(CreateStaffRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.usernameExist");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.emailExist");
        }
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.passwordNotMatch");
        }
    }
}
