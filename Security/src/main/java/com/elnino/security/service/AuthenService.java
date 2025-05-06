package com.elnino.security.service;

import com.elnino.security.domain.User;
import com.elnino.security.dto.AuthenticationRequest;
import com.elnino.security.dto.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenService {
     AuthenticationResponse authentication(AuthenticationRequest request) throws JOSEException;

     AuthenticationResponse validateToken(AuthenticationRequest request) throws JOSEException, ParseException;
}
