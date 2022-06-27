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
    @Column(name = "user_id")
    private long id;

    private String bio;

    private String profilePicturePath;

    private String link;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public String getProfilePicturePath() {
        if (profilePicturePath != null && profilePicturePath.length() != 0) {
            return profilePicturePath;
        }
        return "../images/default-pfp.png";
    }
}