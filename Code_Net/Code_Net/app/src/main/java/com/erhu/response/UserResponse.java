package com.erhu.response;

import com.erhu.entity.User;

public class UserResponse extends BaseResponse {

    private User result;

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }


}
