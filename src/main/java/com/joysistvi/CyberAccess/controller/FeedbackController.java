package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.Feedback;
import com.joysistvi.CyberAccess.service.FeedbackService;

import java.util.List;

public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public List<Feedback> handleViewAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    public boolean handleAddFeedback(Feedback feedback) {
        return feedbackService.addFeedback(feedback);
    }

    public boolean handleDeleteFeedback(int id) {
        return feedbackService.deleteFeedback(id);
    }
}