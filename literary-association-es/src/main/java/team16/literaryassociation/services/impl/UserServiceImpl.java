package team16.literaryassociation.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.literaryassociation.dto.GeoLocationDTO;
import team16.literaryassociation.model.User;
import team16.literaryassociation.repository.UserRepository;
import team16.literaryassociation.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void deleteUser(User user) {
        this.userRepository.delete(user);
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findOne(Long userId) {

        return userRepository.findById(userId).orElseGet(null);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public GeoLocationDTO returnGeoLocation(String city) {
        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();

        switch (city){
            case "Novi Sad":
                geoLocationDTO.setLat((float) 45.267136);
                geoLocationDTO.setLon((float) 19.833549);
                break;
            case "Beograd":
                geoLocationDTO.setLat((float) 44.787197);
                geoLocationDTO.setLon((float) 20.457273);
                break;
            case "Berlin":
                geoLocationDTO.setLat((float) 52.521992);
                geoLocationDTO.setLon((float) 13.413244);
                break;
            case "London":
                geoLocationDTO.setLat((float) 51.509865);
                geoLocationDTO.setLon((float) -0.118092);

            default: //Novi Sad
                geoLocationDTO.setLat((float) 45.267136);
                geoLocationDTO.setLon((float) 19.833549);
        }

        return geoLocationDTO;
    }
}
