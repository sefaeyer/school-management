package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.repository.user.UserRepository;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    public final AuthenticationManager authenticationManager;
    public final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;


    // odev  -->   yukaridaki methodun service kismi yazilacak
    public ResponseEntity<AuthResponse> authenticateUser(LoginRequest loginRequest) {

        //Gelen request icindeki username ve password alinir
        String username= loginRequest.getUsername();
        String password= loginRequest.getPassword();

        //authenticationManager uzerinden kullaniciyi valide ediyoruz
        Authentication authentication=
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        //valide edilen kullaniciyi Context e atiyoruz
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //JWT Token olusturuyoruz
        String token = "Bearer "+ jwtUtils.generateJwtToken(authentication);

        //login islemi gerceklestirilen kullaniciya ulasiyoruz
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //Response olarak login islemini yapan kullaniciyi donecegimiz gerekli fieldlari setliyoruz
        //GrantedAuthority turundeki role yapisini String turune ceviriyoruz
        Set<String> roles = userDetails.getAuthorities().
                stream().
                map(GrantedAuthority::getAuthority).
                collect(Collectors.toSet());

        //bir kullanicinin birden fazla rolu olmayacagi icin ilk indexli elemani aliyoruz
        Optional<String> role = roles.stream().findFirst();

        //burada login islemini gerceklestiren kullanici bilgilerini response olarak
        // gonderecegimiz icin, gerekli bilgileri setliyoruz.
        AuthResponse.AuthResponseBuilder authResponse = AuthResponse.builder();
        authResponse.username(userDetails.getUsername());
        authResponse.token(token.substring(7));
        authResponse.name(userDetails.getName());
        authResponse.ssn(userDetails.getSsn());

        //role bilgisi varsa response nesnesindeki degiskeni setliyoruz   --> !! Burayi sor!!
        role.ifPresent(authResponse::role);

        //AuthResponse nesnesini ResponseEntity ile donduruyoruz
        return ResponseEntity.ok(authResponse.build());

    }



    // odev  -->   updatePassword  -> controller ve service
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {

        String userName = (String) request.getAttribute("username");
        User user = userRepository.findByUsername(userName);

        // Builtin attribute: Datalarının Değişmesi istenmeyen bir objenin builtIn değeri true olur --> !! burayi sor!!
        if (Boolean.TRUE.equals(user.getBuilt_in())){ // null degerleriyle calisirken guvenli bir yontemdir. Boolean.TRUE.equals
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // Eski password bilgisi dogru mu kontrolu yapiyoruz
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())){
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED);
        }

        //yeni sifre hashlenerek kaydediliyor
        String hashedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

    }
}
