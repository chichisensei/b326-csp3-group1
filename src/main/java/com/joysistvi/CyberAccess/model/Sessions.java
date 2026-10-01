package com.joysistvi.CyberAccess.model;

import java.time.LocalDateTime;

public class Sessions {

    private int id;
    private int userId;
    private int computerId;
    private Integer rateId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private String status;
    private Integer durationSeconds;

    public Sessions() {
    }

    public Sessions(
            int id,
            int userId,
            int computerId,
            Integer rateId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer durationMinutes,
            String status,
            Integer durationSeconds) {

        this.id = id;
        this.userId = userId;
        this.computerId = computerId;
        this.rateId = rateId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.durationSeconds = durationSeconds;
    }

    public Sessions(
            int userId,
            int computerId,
            Integer rateId,
            int durationMinutes) {

        this.userId = userId;
        this.computerId = computerId;
        this.rateId = rateId;
        this.durationMinutes = durationMinutes;
        this.durationSeconds = durationMinutes * 60;
        this.status = "Ongoing";
        this.startTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getComputerId() {
        return computerId;
    }

    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }

    public Integer getRateId() {
        return rateId;
    }

    public void setRateId(Integer rateId) {
        this.rateId = rateId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}