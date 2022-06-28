package com.softCare.Linc.service;

import com.softCare.Linc.model.Circle;
import com.softCare.Linc.model.CircleMember;
import com.softCare.Linc.model.User;

import java.util.List;

public interface CircleMemberServiceInterface {


    void save(CircleMember circleMember);

    boolean isMember(User user, Circle circle);

    List<Circle> findAllCirclesWhereMemberOf(User user);
}
