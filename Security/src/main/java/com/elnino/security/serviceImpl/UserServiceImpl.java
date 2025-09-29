package com.elnino.security.serviceImpl;

import com.elnino.security.domain.User;
import com.elnino.security.dto.Role;
import com.elnino.security.dto.UserDto;
import com.elnino.security.mapper.UserMapper;
import com.elnino.security.repository.UserRepository;
import com.elnino.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User loadByUserName(String userName) {
        System.out.println("--- Bắt đầu gọi repository ---");
        User user = userRepository.findUserByUserName(userName);
        System.out.println("--- Repository đã trả về User ---");
        if(user == null)
        {
            throw new UsernameNotFoundException(userName);
        }
        return user;
    }
    public User createUser(UserDto dto){
        if(userRepository.existsUserByName(dto.getName()))
            throw new ApplicationContextException("Đã có user tồn tại");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        HashSet<Role> roles = new HashSet<>();
//        dto.getRoles().forEach(role -> {
//            roles.add(role);
//        });
//        dto.setRole(Role.USER.name());
        return userRepository.save(userMapper.dtoConvert(dto));
    }
    @Override
    public List<UserDto> loadAllUser(){
        List<UserDto> lstUserDto = userRepository.findAll().stream().map(userMapper::convertEntity).toList();
        return lstUserDto;
    }
    @Override
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public boolean checkUserPermission(Authentication authentication, Long userId){
        String currentUsername = authentication.getName();

        // Kịch bản 1: Admin thì có toàn quyền
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return true; // Admin được phép làm mọi thứ
        }

        // Kịch bản 2: User chỉ được phép sửa profile của chính mình
        User userToModify = userRepository.findById(userId).orElse(null);
        if (userToModify == null) {
            return false; // User không tồn tại
        }

        // So sánh username của người đang đăng nhập với username của profile cần sửa
        return currentUsername.equals(userToModify.getUserName());
    }
}
