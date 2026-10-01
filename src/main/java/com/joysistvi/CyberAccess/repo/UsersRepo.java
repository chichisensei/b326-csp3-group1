package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsersRepo {

    List<Users> findAll() throws SQLException;

    Optional<Users> findById(int id) throws SQLException;

    Optional<Users> findByUsername(
            String username
    ) throws SQLException;

    boolean existsByUsername(
            String username
    ) throws SQLException;

    void save(Users user) throws SQLException;

    boolean update(Users user) throws SQLException;

    boolean remove(int id) throws SQLException;
}
