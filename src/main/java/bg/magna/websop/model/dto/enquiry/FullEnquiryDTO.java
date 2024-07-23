package bg.magna.websop.model.dto.enquiry;

import java.time.LocalDateTime;

public class FullEnquiryDTO {
    private long id;
    private String userFullName;
    private String userEmail;
    private String machineName;
    private String machineImageUrl;
    private String machineId;
    private String machineSerialNumber;
    private LocalDateTime createdOn;
    private String title;
    private String message;

    public FullEnquiryDTO() {
    }

    public FullEnquiryDTO(long id, String userFullName, String userEmail, String machineName, String machineImageUrl, String machineId, String machineSerialNumber, LocalDateTime createdOn, String title, String message) {
        this.id = id;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.machineName = machineName;
        this.machineImageUrl = machineImageUrl;
        this.machineId = machineId;
        this.machineSerialNumber = machineSerialNumber;
        this.createdOn = createdOn;
        this.title = title;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineImageUrl() {
        return machineImageUrl;
    }

    public void setMachineImageUrl(String machineImageUrl) {
        this.machineImageUrl = machineImageUrl;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineSerialNumber() {
        return machineSerialNumber;
    }

    public void setMachineSerialNumber(String machineSerialNumber) {
        this.machineSerialNumber = machineSerialNumber;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
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
