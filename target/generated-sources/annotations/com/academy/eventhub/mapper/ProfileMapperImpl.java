package com.academy.eventhub.mapper;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.entity.Profile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-19T09:38:02+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public ProfileDTO toDTO(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setFirstName( profile.getFirstName() );
        profileDTO.setLastName( profile.getLastName() );
        profileDTO.setBio( profile.getBio() );
        profileDTO.setCity( profile.getCity() );
        profileDTO.setPhoto( profile.getPhoto() );

        return profileDTO;
    }

    @Override
    public Profile toEntity(ProfileDTO profileDTO) {
        if ( profileDTO == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstName( profileDTO.getFirstName() );
        profile.setLastName( profileDTO.getLastName() );
        profile.setBio( profileDTO.getBio() );
        profile.setCity( profileDTO.getCity() );
        profile.setPhoto( profileDTO.getPhoto() );

        return profile;
    }
}
