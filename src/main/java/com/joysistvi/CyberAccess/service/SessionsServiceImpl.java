package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Sessions;
import com.joysistvi.CyberAccess.repo.SessionsRepo;
import com.joysistvi.CyberAccess.repo.SessionsRepoImpl;

import java.sql.SQLException;
import java.util.List;

public class SessionsServiceImpl implements SessionsService {

    private final SessionsRepo repo;

    public SessionsServiceImpl() {
        this(new SessionsRepoImpl());
    }

    public SessionsServiceImpl(SessionsRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Sessions> getAll() throws SQLException {
        return repo.findAll();
    }

    @Override
    public Sessions getById(int id) throws SQLException {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void add(Sessions session) throws SQLException {

        if (session == null) {
            throw new IllegalArgumentException(
                    "Session cannot be null."
            );
        }

        if (session.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than 0."
            );
        }

        if (session.getComputerId() <= 0) {
            throw new IllegalArgumentException(
                    "Computer ID must be greater than 0."
            );
        }

        if (session.getDurationMinutes() == null ||
                session.getDurationMinutes() <= 0) {

            throw new IllegalArgumentException(
                    "Duration must be greater than 0 minutes."
            );
        }

        if (session.getDurationSeconds() == null ||
                session.getDurationSeconds() <= 0) {

            session.setDurationSeconds(
                    session.getDurationMinutes() * 60
            );
        }

        if (session.getStatus() == null ||
                session.getStatus().isBlank()) {

            session.setStatus("Ongoing");
        }

        if (session.getStartTime() == null) {

            session.setStartTime(
                    java.time.LocalDateTime.now()
            );
        }

        repo.save(session);
    }

    @Override
    public void update(Sessions session) throws SQLException {

        if (session == null) {
            throw new IllegalArgumentException(
                    "Session cannot be null."
            );
        }

        if (session.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid session ID."
            );
        }

        repo.update(session);
    }

    @Override
    public boolean remove(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid session ID."
            );
        }

        return repo.delete(id);
    }
}
