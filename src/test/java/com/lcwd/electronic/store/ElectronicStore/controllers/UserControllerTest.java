package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;
    User user;
    Role role;

    String jwtTkn="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSb0BnbWFpbC5jb20iLCJleHAiOjE3MDM4ODMyNzEsImlhdCI6MTcwMzg2NTI3MX0.GczLsbTXpiVibfMPCYbyhILkHrmQKeZjeHobrRgmnNv0YFGrFe6icDEZoABzYZuICQmwLCHHucYSJHEEkyrahA";


    @BeforeEach
    public void init() {
        role = Role.builder().roleId("abc").roleName("NORMAL").build();
        user = User.builder()
                .name("prashant")
                .userEmail("Pb@gmail.com")
                .userGender("male")
                .userAbout("java developer")
                .userImageName("abc.png")
                .userPassword("Pass@123")
                .roles(Set.of(role))
                .build();

    }

    @Test
    public void createUserTest() throws Exception {
        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }

    private String convertObjectToJsonString(Object user)  {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Test
    public void updateUserTest() throws Exception {

        String userId = "userAbc";
        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/" + userId)
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getAllUserTest() throws Exception {

        UserDto userDto = UserDto.builder().name("prashant").userEmail("pb@gmail.com").userAbout("java developer").userGender("male").userPassword("abc").userImageName("pb.png").build();
        UserDto userDto1 = UserDto.builder().name("kunal").userEmail("kh@gmail.com").userAbout("soft tester").userGender("male").userPassword("def").userImageName("kh.png").build();
        UserDto userDto2 = UserDto.builder().name("manish").userEmail("Md@gmail.com").userAbout("Data scientist").userGender("male").userPassword("ghi").userImageName("md.png").build();
        PageableResponse<UserDto> pagResponse = new PageableResponse<>();

        pagResponse.setContent(Arrays.asList(userDto, userDto1, userDto2));
        pagResponse.setLastPage(false);
        pagResponse.setPageNumber(100);
        pagResponse.setPageSize(10);
        pagResponse.setTotalElements(1000);
        Mockito.when(userService.getAllUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pagResponse);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/")
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getUserByIdTest() throws Exception {

        String userId = "userabcd";
        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/" + userId)
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {

        String userId = "userabcd";
        Mockito.doNothing().when(userService).deleteUser(userId);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/users/" + userId)
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getUserByEmailTest() throws Exception {

        String email = "pb@gmail.com";
        UserDto dto = modelMapper.map(user, UserDto.class);
        Mockito.when(userService.getUserByEmail(Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/email/" + email)
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void searchUserTest() throws Exception {

        UserDto userDto1 = UserDto.builder()
                .name("santosh bikkad").userEmail("Pb@gmail.com")
                .userGender("male").userAbout("java developer")
                .userImageName("abc.png").userPassword("abc")
                .build();

        UserDto userDto2 = UserDto.builder()
                .name("atul").userEmail("Pb@gmail.com")
                .userGender("male").userAbout("java developer")
                .userImageName("abc.png").userPassword("abc")
                .build();

        String keyword = "atul";

        Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(Arrays.asList(userDto1, userDto2));

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/search/" + keyword)
                                .header(HttpHeaders.AUTHORIZATION, jwtTkn)
                )
                .andDo(print())
                .andExpect(status().isOk());


    }
}
