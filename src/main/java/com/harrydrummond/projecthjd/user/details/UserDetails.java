package com.harrydrummond.projecthjd.user.details;

import com.harrydrummond.projecthjd.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class UserDetails {

    @Id
    private long id;

    private String bio;

    private String profilePicturePath;

    @OneToOne
    @MapsId
    private User user;

    public String getProfilePicturePath() {
        if (profilePicturePath != null && profilePicturePath.length() != 0) {
            return profilePicturePath;
        }
        return "images/default-pfp.png";
    }
}