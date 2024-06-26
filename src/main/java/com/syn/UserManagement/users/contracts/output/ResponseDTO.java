package com.syn.UserManagement.users.contracts.output;


import lombok.Data;

import java.util.List;

@Data
public class ResponseDTO {

    private String message;
    private List<String> messages;

}
