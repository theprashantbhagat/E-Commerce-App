package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.PageableHelper;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.RoleRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String path;

    @Value("${normal.role.id}")
    private String normalRoleId;

    @Value("${admin.role.id}")
    private String adminRoleId;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        log.info("Initiating Dao Call For Save User Data");
        User user = this.modelMapper.map(userDto, User.class);
        //fetch role of normal and set it to user
        Role role = roleRepository.findById(normalRoleId).get();
        user.getRoles().add(role);
        User saveUser = userRepository.save(user);

        log.info("Completed Dao Call For Save User Data");
        return this.modelMapper.map(saveUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto,String userId) {
        log.info("Initiating Dao call for update user data with userId {}:" , userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND +" Id " + userId));
        user.setUserName(userDto.getUserName());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserPassword(userDto.getUserPassword());
        user.setUserAbout(userDto.getUserAbout());
        user.setUserGender(userDto.getUserGender());
        user.setUserImageName(userDto.getUserImageName());
        User updatedUser = this.userRepository.save(user);
        log.info("Completed Dao call for update user data with userId {}:" ,userId);
        return this.modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Initiating Dao call for delete user data with userId {}:", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND +" Id " + userId));
        String imageName = user.getUserImageName();
        String fullPath = path + imageName;
        File file=new File(fullPath);
        if (file.exists()) {
            file.delete();
        }
        this.userRepository.delete(user);
        log.info("Completed Dao call for delete user data with userId {}:", userId);

    }

    @Override
    public PageableResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiating Dao call for getting All the users");
        Sort sort=sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = this.userRepository.findAll(pageable);
        PageableResponse<UserDto> response = PageableHelper.getPageableResponse(page, UserDto.class);
        log.info("Completed Dao call for getting all the users");
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        log.info("Initiating Dao call for get the user data with userId {}:",userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND +" Id "+ userId));
        log.info("Completed Dao call for get user data with userId {}:",userId);
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        log.info("Initiating Dao call for get user With userEmail {}:",userEmail);
        User user = this.userRepository.findByUserEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException(AppConstants.NOT_FOUND +" email "+userEmail));
        log.info("Completed Dao call for get user with userEmail {}:",userEmail);
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        log.info("Initiating Dao call for search the user with keyword {}:",keyword);
        List<User> users = this.userRepository.findByUserNameContaining(keyword);
        log.info("Completed Dao call for search the user with keyword {}:",keyword);
        return users.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {

        return userRepository.findByUserEmail(email);
    }


}
