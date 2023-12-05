package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.constants.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.PaginationConstants;
import com.lcwd.electronic.store.ElectronicStore.constants.UrlConstants;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.payloads.ApiResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.payloads.PageableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL)
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    /**
     * @param userDto
     * @return
     * @apiNote this is related to create user
     * @author Prashant Bhagat
     * @since V1.0
     */
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Entering Request For Save User Data");
        UserDto createUserDto = this.userService.createUser(userDto);
        log.info("Completed Request For Save User Data");
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    /**
     * @param userDto
     * @param userId
     * @return
     * @apiNote this is related to update the user
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        log.info("Entering request for update user data with userId {}:", userId);
        UserDto updatedUser = this.userService.updateUser(userDto, userId);
        log.info("Completed request for update user data with userId {}:", userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * @param userId
     * @return
     * @apiNote this is related to delete the user
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        log.info("Entering request for delete the user with userId {}:", userId);
        this.userService.deleteUser(userId);
        ApiResponse response = ApiResponse.builder().message(AppConstants.DELETE_RESPONSE).success(true).status(HttpStatus.OK).build();
        log.info("Completed request for delete the user with userId {}:", userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param userId
     * @return
     * @apiNote this is related to get user by userId
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        log.info("Entering request for get the user data with userId {}:", userId);
        UserDto user = this.userService.getUserById(userId);
        log.info("Completed request for get the user data with userId {}:", userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @apiNote this is related to fetching All users
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/users")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.PAGE_SIZE, required = false) String sortDir) {
        log.info("Entering request for getting all users data");
        PageableResponse<UserDto> allUsers = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for getting all users data");
        return new ResponseEntity<PageableResponse<UserDto>>(allUsers, HttpStatus.OK);
    }

    /**
     * @param userEmail
     * @return
     * @apiNote this is related to get user by email
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/users/email/{userEmail}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
        log.info("Entering request for get user by email {}:", userEmail);
        UserDto userByEmail = this.userService.getUserByEmail(userEmail);
        log.info("Completed request for get user by email {}:", userEmail);
        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }

    /**
     * @param keyword
     * @return
     * @apiNote this is related to search the user with thw help of any keyword
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/users/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        log.info("Entering request for search the user with keyword {}:", keyword);
        List<UserDto> userDtos = this.userService.searchUser(keyword);
        log.info("Completed request for search the user with keyword {}:", keyword);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    /**
     * @param image
     * @param userId
     * @return
     * @throws IOException
     * @apiNote This method is related to upload user image
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @PostMapping("/users/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam MultipartFile image, @PathVariable String userId) throws IOException {
        log.info("Entering Request for upload the user image with user id:{}", userId);
        String file = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setUserImageName(file);
        userService.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(file).message("File uploaded").success(true).status(HttpStatus.CREATED).build();
        log.info("Completed Request for upload the user image with user id :{}", userId);
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    /**
     * @param userId
     * @param response
     * @throws IOException
     * @apiNote This method is related to download image
     * @author Prashant Bhagat
     * @since V 1.0
     */
    @GetMapping("/users/image/{userId}")
    public void downloadImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        log.info("Entering request for download the user image with user id :{}", userId);
        UserDto user = userService.getUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, user.getUserImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Completed request for download the user image with iser id :{}", userId);

    }
}
