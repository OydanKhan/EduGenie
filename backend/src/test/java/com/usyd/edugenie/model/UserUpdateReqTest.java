package com.usyd.edugenie.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserUpdateReqTest {

    @Test
    void testNoArgsConstructor() {
        UserUpdateReq userUpdateReq = new UserUpdateReq();
        assertThat(userUpdateReq).isNotNull();
        assertThat(userUpdateReq.getFirstName()).isNull();
        assertThat(userUpdateReq.getLastName()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        UserUpdateReq userUpdateReq = new UserUpdateReq();
        userUpdateReq.setFirstName("John");
        userUpdateReq.setLastName("Doe");

        assertThat(userUpdateReq.getFirstName()).isEqualTo("John");
        assertThat(userUpdateReq.getLastName()).isEqualTo("Doe");
    }

    @Test
    void testToString() {
        UserUpdateReq userUpdateReq = new UserUpdateReq();
        userUpdateReq.setFirstName("Jane");
        userUpdateReq.setLastName("Doe");

        String expectedString = "UserUpdateReq(firstName=Jane, lastName=Doe)";
        assertThat(userUpdateReq.toString()).isEqualTo(expectedString);
    }
}
