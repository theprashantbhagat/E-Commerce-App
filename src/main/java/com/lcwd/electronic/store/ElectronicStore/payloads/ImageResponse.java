package com.lcwd.electronic.store.ElectronicStore.payloads;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {

    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;

}
