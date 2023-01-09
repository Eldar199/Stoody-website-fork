package com.loam.stoody.repository.user.attributes;

import com.loam.stoody.model.user.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPrivacySettingsRepository extends JpaRepository<UserPrivacy, Integer> {
}