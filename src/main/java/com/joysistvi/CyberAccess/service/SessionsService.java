package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Sessions;

import java.sql.SQLException;
import java.util.List;

public interface SessionsService {

    List<Sessions> getAll() throws SQLException;

    Sessions getById(int id) throws SQLException;

    void add(Sessions session) throws SQLException;

    void update(Sessions session) throws SQLException;

    boolean remove(int id) throws SQLException;
}