package com.example.student.homemade;
import java.sql.Timestamp;

public class ReviewInfo {
    int ratings;
    String review;
    String reviewID;
    String reviewee;
    String reviewer;
    String timeAndDate;

    public ReviewInfo(int ratings, String review, String reviewID, String reviewee, String reviewer, String timeAndDate) {
        this.ratings = ratings;
        this.review = review;
        this.reviewID = reviewID;
        this.reviewee = reviewee;
        this.reviewer = reviewer;
        this.timeAndDate = timeAndDate;
    }
    public ReviewInfo(){

    }

    public int getRatings() {
        return ratings;
    }

    public String getReview() {
        return review;
    }

    public String getReviewID() {
        return reviewID;
    }

    public String getReviewee() {
        return reviewee;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public void setReviewee(String reviewee) {
        this.reviewee = reviewee;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }


}
