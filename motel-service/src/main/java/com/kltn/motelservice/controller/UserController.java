package com.kltn.motelservice.controller;

import com.kltn.motelservice.entity.Role;
import com.kltn.motelservice.entity.RoleName;
import com.kltn.motelservice.entity.User;
import com.kltn.motelservice.mapper.UserMapper;
import com.kltn.motelservice.model.AccountDto;
import com.kltn.motelservice.model.UserDTO;
import com.kltn.motelservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')") // only ADMIN can get list users
    public Page<UserDTO> getAlLUser(@PageableDefault(page = 0, size = 12) Pageable page) {
        return userService.selectPageOfUsers(page).map(mapper::entityToDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
//        if (!validRequest(auth, id)) throw new AccessDeniedException("Access dined");

        User user = userService.selectUserById(id);

        return mapper.entityToDTO(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#oauth2.hasAnyScope('read')") // for authenticated request (logged)
    public UserDTO updateProfile(@PathVariable("id") Long id,
                                 @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User user = userService.changeProfile(userDTO);

        return mapper.entityToDTO(user);
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDTO blockUser(@PathVariable("id") Long id) {
        User user = userService.blockUserById(id, true);

        return mapper.entityToDTO(user);
    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDTO unblockUser(@PathVariable("id") Long id) {
        User user = userService.blockUserById(id, false);

        return mapper.entityToDTO(user);
    }

    @PostMapping("/{id}/password")
    @PreAuthorize("#oauth2.hasAnyScope('read')") // for authenticated request (logged)
    public UserDTO changePasswordAUser(@PathVariable("id") Long id,
                                       @RequestParam("password") String newPassword,
                                       @RequestParam("oldPassword") String oldPassword,
                                       OAuth2Authentication auth) throws Exception {
        if (!validRequest(auth, id)) throw new AccessDeniedException("Access dined");

        User user = userService.changePassword(id, oldPassword, newPassword);

        return mapper.entityToDTO(user);
    }

    // registration flow

    @GetMapping("/check")
    public boolean checkExistUser(@RequestParam("email") String email){
        return (userService.selectUserByEmail(email) != null);
    }

    @PostMapping
    public UserDTO registerUserAccount(@RequestBody AccountDto accountDto) {
        User user = userService.registration(accountDto);
        return mapper.entityToDTO(user);
    }

//    @RequestMapping(value = "/u/registrationConfirm", method = RequestMethod.GET)
//    public String confirmRegistration(Locale locale, Model model,
//                                      @RequestParam(name = "token", defaultValue = "") String token) {
//
//        // tìm token trong db
//        Optional<VerificationToken> verificationToken
//                = Optional.of(token).map(userService::getVerificationToken);
//
//        // token không tồn tại
//        if (!verificationToken.isPresent()) {
//            String message = messages.getMessage("auth.message.invalidToken", null, locale);
//            model.addAttribute("message", message);
//            return "signup/badConfirmPage";
//        }
//
//        // kiểm tra liệu tài khoản này đã xác nhận chưa
//        Optional<User> user = verificationToken.map(VerificationToken::getUser);
//        if (user.get().isEnabled()) {
//            String message = messages.getMessage("message.activitedEmail", null, locale);
//            model.addAttribute("message", message);
//            return "signup/badConfirmPage";
//        }
//
//        Calendar cal = Calendar.getInstance();
//        // kiểm tra thời hạn token
//        if (verificationToken.map(VerificationToken::getExpiryDate).get()
//                .before(cal.getTime())) {
//            String message = messages.getMessage("auth.message.expired", null, locale);
//            model.addAttribute("message", message);
//            model.addAttribute("mailAddress", user.map(User::getEmail).get());
//            return "signup/badConfirmPage";
//        }
//
//        User u = user.orElse(new User());
//        u.setEnabled(true);
//        userService.saveRegisteredUser(u);
//        model.addAttribute("user", u);
//        return "signin/loginPage";
//    }

    //user request change profile or admin
    boolean validRequest(OAuth2Authentication auth, Long userId) {
        User authUser = userService.selectUserByEmail(auth.getName());

        // has ROLE_ADMIN or has userid equal userid request
        return authUser.getRoles().stream().map(Role::getName).anyMatch(roleName -> roleName == RoleName.ROLE_ADMIN)
                || authUser.getId().equals(userId);
    }
}
