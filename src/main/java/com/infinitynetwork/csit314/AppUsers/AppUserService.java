package com.infinitynetwork.csit314.AppUsers;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // Method to get all users with pagination
    public Page<AppUser> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appUserRepository.findAll(pageable);
    }

    public void updateUser(UpdateUserDTO userDTO) throws Exception {
        if (userDTO.getUserID() == null) {
            throw new Exception("User ID is null");
        }

        Optional<AppUser> optionalUser = appUserRepository.findById(userDTO.getUserID());
        if (!optionalUser.isPresent()) {
            throw new Exception("User not found");
        }

        AppUser existingUser = optionalUser.get();

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setLocked(userDTO.getLocked());

        if (userDTO.getLocked() != null) {
            existingUser.setEnabled(!userDTO.getLocked()); // If locked is true, set enabled to false, and vice versa
        }

        existingUser.setUserType(UserType.valueOf(userDTO.getUserType()));
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setUpdated_at(LocalDateTime.now());

        appUserRepository.save(existingUser);
    }

    public void deleteUser(Long id) throws Exception {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new Exception("Listing not found");
        }
        appUserRepository.deleteById(id);
    }
}
