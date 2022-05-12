package com.harrydrummond.projecthjd.user.details;

import com.harrydrummond.projecthjd.posts.image.Image;
import com.harrydrummond.projecthjd.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table
public class UserDetails {

    @Id
    private long id;

    private String bio;

    @OneToOne
    private Image profilePicture;

    @OneToOne
    private User user;


}