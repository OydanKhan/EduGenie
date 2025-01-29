package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserVerifyReqTest {

    @Test
    void testNoArgsConstructor() {
        UserVerifyReq userVerifyReq = new UserVerifyReq();
        assertThat(userVerifyReq).isNotNull();
        assertThat(userVerifyReq.getCredential()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        UserVerifyReq userVerifyReq = new UserVerifyReq();
        userVerifyReq.setCredential("test-credential");

        assertThat(userVerifyReq.getCredential()).isEqualTo("test-credential");
    }

    @Test
    void testToString() {
        UserVerifyReq userVerifyReq = new UserVerifyReq();
        userVerifyReq.setCredential("sample-credential");

        String expectedString = "UserVerifyReq(credential=sample-credential)";
        assertThat(userVerifyReq.toString()).isEqualTo(expectedString);
    }
}
