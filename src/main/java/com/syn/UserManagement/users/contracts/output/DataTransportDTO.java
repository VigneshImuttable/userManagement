package com.syn.UserManagement.users.contracts.output;

import lombok.Data;
import java.util.List;

@Data
public class DataTransportDTO {

    private List<Object> dataList;

    private Object data;

    private boolean Success;

    private boolean status = true;

    private String message;

    private String htmlString;

    private List<String> errorMessages;

}
