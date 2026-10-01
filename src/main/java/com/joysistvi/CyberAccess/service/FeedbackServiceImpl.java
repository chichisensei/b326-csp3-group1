package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Feedback;
import com.joysistvi.CyberAccess.repo.FeedbackRepo;

import java.util.List;

public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepo feedbackRepo;

    public FeedbackServiceImpl(FeedbackRepo feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepo.getAllFeedback();
    }

    @Override
    public boolean addFeedback(Feedback feedback) {

        if (feedback == null) {
            System.out.println("Feedback is required.");
            return false;
        }

        if (feedback.getUserId() <= 0) {
            System.out.println("Invalid user ID.");
            return false;
        }

        if (feedback.getComment() == null ||
                feedback.getComment().trim().isEmpty()) {

            System.out.println("Comment is required.");
            return false;
        }

        String comment = feedback.getComment().trim();

        if (comment.length() > 30) {
            System.out.println(
                    "Comment must not exceed 30 characters."
            );
            return false;
        }

        feedback.setComment(comment);

        return feedbackRepo.addFeedback(feedback);
    }

    @Override
    public boolean deleteFeedback(int id) {

        if (id <= 0) {
            System.out.println("Invalid feedback ID.");
            return false;
        }

        return feedbackRepo.deleteFeedback(id);
    }
}