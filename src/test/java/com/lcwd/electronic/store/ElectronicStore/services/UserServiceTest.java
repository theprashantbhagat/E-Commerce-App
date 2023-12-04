package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    User user;

    @BeforeEach
    public void init(){
        user = User.builder()
                .userName("prashant")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();
    }

    @Test
    public void createUser() {

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        // UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        System.out.println(user1.getUserName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("prashant",user1.getUserName());

    }

    @Test
    public void updateUserTest(){
        String userId="";

        UserDto userDto = UserDto.builder()
                .userName("prashant bhagat")
                .userEmail("pb@gmail.com")
                .userGender("male")
                .userAbout("java dev")
                .userImageName("pb.png")
                .userPassword("abc")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updateUser = userService.updateUser(userDto, userId);
        System.out.println(updateUser.getUserName());
        Assertions.assertNotNull(userDto);
    }

    @Test
    public void deleteUserTest(){

        String userId="userabcd";

        Mockito.when(userRepository.findById("userabcd")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);

    }
    @Test
    public void getAllUserTest(){

      User  user1 = User.builder()
                .userName("pawan")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();

      User user2 = User.builder()
                .userName("kunal")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();

        List<User> userList = Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> allUsers = userService.getAllUsers(1,2,"name","asc");
        Assertions.assertEquals(3,allUsers.getContent().size());

    }
    @Test
    public void getUserByIdTest(){

        String userId="userIdabcd";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserById(userId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getUserName(),userDto.getUserName(),"name not matched");
    }
    @Test
    public void getUserByEmailTest(){

        String emailId="pb@gmail.com";
        Mockito.when(userRepository.findByUserEmail(emailId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getUserEmail(),userDto.getUserEmail(),"email not matched!!");
    }

    @Test
    public void searchUserTest(){
       User user = User.builder()
                .userName("prashant bhagat")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();

       User user1 = User.builder()
                .userName("santosh bikkad")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();

       User user2 = User.builder()
                .userName("atul bonde")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("abc")
                .build();

        String keyword="prashant";
        Mockito.when(userRepository.findByUserNameContaining(keyword)).thenReturn(Arrays.asList(user,user1,user2));
        List<UserDto> userDtos = userService.searchUser(keyword);
        Assertions.assertEquals(4,userDtos.size(),"size not matched");
    }










}
