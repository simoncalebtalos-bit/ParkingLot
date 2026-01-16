package com.parking.parkinglot1.ejb;

import com.parking.parkinglot1.common.UserDto;
import com.parking.parkinglot1.entities.User;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UsersBean {

    private static final Logger LOG = Logger.getLogger(UsersBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    public List<UserDto> findAllUsers() {
        LOG.info("findAllUsers");
        try {
            TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u", User.class);
            List<User> users = typedQuery.getResultList();
            return copyUsersToDto(users);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }


    public List<String> findInvoicingNames() {
        LOG.info("findInvoicingNames");
        try {
            return entityManager.createQuery("SELECT u.username FROM User u", String.class)
                    .getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }


    public Integer countCars() {
        LOG.info("countCars");
        try {
            Long count = entityManager.createQuery("SELECT COUNT(c) FROM Car c", Long.class)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private List<UserDto> copyUsersToDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
            ));
        }
        return usersDto;
    }
}