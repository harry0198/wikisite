package com.harrydrummond.projecthjd.user.preferences;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long> {

    Preferences getByPreference(Preference role);
}