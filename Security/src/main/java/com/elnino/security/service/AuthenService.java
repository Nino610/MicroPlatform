package com.elnino.security.service;

import com.elnino.security.domain.User;
import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthenService {
     String login(User user) throws JOSEException;
     AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException;
}
