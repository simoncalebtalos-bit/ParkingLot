package com.parking.parkinglot1.ejb;

import com.parking.parkinglot1.common.CarDto;
import com.parking.parkinglot1.entities.Car;
import com.parking.parkinglot1.entities.User;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class CarsBean {

    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

public List<CarDto> findAllCars() {
    LOG.info("findAllCars");
    try {
        TypedQuery<Car> typedQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);
        List<Car> cars = typedQuery.getResultList();
        return copyCarsToDto(cars);

    } catch (Exception ex) {
        throw new EJBException(ex);
    }
}

    private List<CarDto> copyCarsToDto(List<Car> cars) {
        List<CarDto> carsDto = new ArrayList<>();

        for (Car car : cars) {
            carsDto.add(new CarDto(
                    car.getId(),
                    car.getLicensePlate(),
                    car.getParkingSpot(),
                    car.getOwner().getUsername()
            ));
        }
        return carsDto;
    }

    public void createCar(String licensePlate, String parkingSpot, Long userId) {
        LOG.info("createCar");

        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        User user = entityManager.find(User.class, userId);
        user.getCars().add(car);
        car.setOwner(user);

        entityManager.persist(car);
    }


    public CarDto findById(Long carId) {
        LOG.info("findById");

        try {
            Car car = entityManager.find(Car.class, carId);
            if (car == null) {
                return null;   // or throw exception if you prefer
            }

            return new CarDto(
                    car.getId(),
                    car.getLicensePlate(),
                    car.getParkingSpot(),
                    car.getOwner().getUsername()
            );

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public void updateCar(Long carId, String licensePlate, String parkingSpot, Long userId) {
        LOG.info( "updateCar");

        Car car = entityManager.find(Car.class, carId);
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        // remove this car from the old owner
        User oldUser = car.getOwner();
        oldUser.getCars().remove(car);

        // add the car to its new owner
        User user = entityManager.find(User.class, userId);
        user.getCars().add(car);
        car.setOwner(user);
    }
}
