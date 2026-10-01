package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.Sessions;
import com.joysistvi.CyberAccess.service.SessionsService;
import com.joysistvi.CyberAccess.service.SessionsServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class SessionsController {

    private final SessionsService service;

    public SessionsController() {
        this(new SessionsServiceImpl());
    }

    public SessionsController(SessionsService service) {
        this.service = service;
    }

    public List<Sessions> getAll() throws SQLException {
        return service.getAll();
    }

    public Sessions getById(int id) throws SQLException {
        return service.getById(id);
    }

    public void add(Sessions session) throws SQLException {
        service.add(session);
    }

    public void update(Sessions session) throws SQLException {
        service.update(session);
    }

    public boolean remove(int id) throws SQLException {
        return service.remove(id);
    }
}