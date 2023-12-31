package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.validation.ImageNameValid;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @NotEmpty
    @Size(min = 4,message = "UserName Should Contain Atleast 4 Characters!!")
    private String name;

    @Email(regexp = "[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-z]+)+",message = "Email Address Is Not Valid")
    private String userEmail;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]*@[a-zA-Z0-9]+",message = "Invalid Password!! ")
    private String userPassword;

    @NotBlank
    @Size(min = 4 ,message = "Gender Should Be Minimum of 4 Characters")
    private String userGender;

    @NotBlank
    private String userAbout;

    @ImageNameValid
    private String userImageName;

    private Set<RoleDto> roles=new HashSet<>();
}
