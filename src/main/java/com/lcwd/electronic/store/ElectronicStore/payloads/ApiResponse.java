package com.lcwd.electronic.store.ElectronicStore.payloads;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private String message;
   // private String status;
    private boolean success;

   // private HttpStatus status;

}
