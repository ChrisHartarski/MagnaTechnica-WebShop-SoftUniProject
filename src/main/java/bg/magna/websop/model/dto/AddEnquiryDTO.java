package bg.magna.websop.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AddEnquiryDTO {

    private String machineId;

    private String userId;

    private String userEmail;

    private String userFullName;

    @NotEmpty(message = "{enquiry.title.notEmpty}")
    @Size(min = 2, max = 100, message = "{enquiry.title.length}")
    private String title;

    @NotEmpty(message = "{enquiry.message.notEmpty}")
    @Size(max = 250, message = "{enquiry.message.length}")
    private String message;

    public AddEnquiryDTO() {
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
