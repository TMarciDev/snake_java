package hu.elte.fi.progtech.snake.persistence.dao;

import hu.elte.fi.progtech.snake.persistence.entity.AbstractEntity;

import java.sql.SQLException;

public interface IEntityDao<E extends AbstractEntity> {
    void add(E entity) throws SQLException;
}
