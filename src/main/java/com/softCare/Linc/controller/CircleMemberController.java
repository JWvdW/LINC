package com.softCare.Linc.controller;

import com.softCare.Linc.model.CircleMember;
import com.softCare.Linc.model.User;
import com.softCare.Linc.service.CircleMemberServiceInterface;
import com.softCare.Linc.service.LincUserDetailServiceInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Lucas Blumers
 *
 * Handles circlemember interactions
 */

@Controller
public class CircleMemberController {

    private final CircleMemberServiceInterface circleMemberServiceInterface;
    private final LincUserDetailServiceInterface userInterface;
    private final CircleController circleController;


    public CircleMemberController(CircleMemberServiceInterface circleMemberServiceInterface, LincUserDetailServiceInterface userInterface, CircleController circleController) {
        this.circleMemberServiceInterface = circleMemberServiceInterface;
        this.userInterface = userInterface;
        this.circleController = circleController;
    }

    @PostMapping("/member/new")
    protected String newMember(@Valid @ModelAttribute("newMemberUser") User user, BindingResult result, Model model) {
        boolean userExists = userInterface.findByEmail(user.getEmailAddress()).isPresent();
        Optional<User> toBeMember = userInterface.findByEmail(user.getEmailAddress());
        if (!userExists){
            model.addAttribute("unknownEmail",true);
            return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
        }

        boolean noMemberYet = circleMemberServiceInterface.findByUserIdAndCircleId(toBeMember.get().getUserId(), circleController.currentCircle.getCircleId()).isEmpty();

        Optional<User> member = userInterface.findByEmail(user.getEmailAddress());
        if (member.isPresent() && noMemberYet){
            CircleMember circleMember = new CircleMember(member.get(),circleController.currentCircle,false,false  );
            circleMemberServiceInterface.save(circleMember);
        }
        return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
    }

    @PostMapping("/member/remove")
    protected String newMember(@ModelAttribute("circleMemberId") Long circleMemberId) {
        Optional<User> user = userInterface.findByUserId(circleMemberId);
        if (user.isPresent()){
            Optional<CircleMember> circleMember = circleMemberServiceInterface.findByUserIdAndCircleId(circleMemberId, circleController.currentCircle.getCircleId());
            if (circleMember.isPresent()){
                user.get().removeMember(circleMember.get());
                circleMemberServiceInterface.delete(circleMember.get());
            }
        }
        return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
    }

    @PostMapping({"/assignRole/admin"})
    protected String assignRoleAdmin(@ModelAttribute("circleMemberId") Long circleMemberId) {
        Optional<User> user = userInterface.findByUserId(circleMemberId);
        if (user.isPresent()) {
            Optional<CircleMember> circleMember = circleMemberServiceInterface.findByUserIdAndCircleId(circleMemberId, circleController.currentCircle.getCircleId());
            if (circleMember.get().isAdmin()) {
                circleMember.ifPresent(member -> member.setAdmin(false));
            } else {
                circleMember.ifPresent(member -> member.setAdmin(true));
            }
            circleMemberServiceInterface.save(circleMember.get());
        }
        return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
    }

    @PostMapping({"/assignRole/client"})
    protected String assignRoleClient(@ModelAttribute("circleMemberId") Long circleMemberId) {
        Optional<User> user = userInterface.findByUserId(circleMemberId);
        if (user.isPresent()) {
            Optional<CircleMember> circleMember = circleMemberServiceInterface.findByUserIdAndCircleId(circleMemberId, circleController.currentCircle.getCircleId());
            if (circleMember.get().isClient()) {
                circleMember.ifPresent(member -> member.setClient(false));
            } else {
                circleMember.ifPresent(member -> member.setClient(true));
            }
            circleMemberServiceInterface.save(circleMember.get());
        }
        return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
    }

    @PostMapping({"/changeRoles"})
    protected String changeRoles(@RequestParam(value = "circleMemberId")Long circleMemberId,
                                 @RequestParam(value = "admin", required = false) String admin,
                                 @RequestParam(value = "client", required = false) String client) {

        Optional<CircleMember> circleMember = circleMemberServiceInterface.findById(circleMemberId);
        if (circleMember.isPresent()){
            circleMember.get().setAdmin(!(admin == null));
            circleMember.get().setClient(!(client == null));
            circleMemberServiceInterface.save(circleMember.get());
        }
        return "redirect:/circle/" + circleController.currentCircle.getCircleId() +"#v-pills-users";
    }

}
