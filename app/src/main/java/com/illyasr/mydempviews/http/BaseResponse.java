package com.illyasr.mydempviews.http;

/**
 * Created by jh352160 on 2017/9/8.
 */

public class BaseResponse<T> {
    /**
     * code : SUCCESS
     * data : T
     * msg : 成功
     * success : true
     */

    private int code;
    private T data;
    private String msg;
    private boolean success;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
