package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Feedback;

import java.util.List;

public interface FeedbackService {

    List<Feedback> getAllFeedback();

    boolean addFeedback(Feedback feedback);

    boolean deleteFeedback(int id);
}