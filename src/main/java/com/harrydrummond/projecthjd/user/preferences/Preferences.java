package com.harrydrummond.projecthjd.user.preferences;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preference")
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "preference")
    private Preference preference;

    public Preferences(Preference preference) {
        this.preference = preference;
    }
}