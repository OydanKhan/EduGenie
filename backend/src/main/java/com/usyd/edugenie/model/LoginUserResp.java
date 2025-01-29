package com.usyd.edugenie.model;

import lombok.Data;


/**
 * @author caorong
 */
@Data
public class LoginUserResp {
    private String email;
    private String lastName;
    private String firstName;
    private String avatar;
}
