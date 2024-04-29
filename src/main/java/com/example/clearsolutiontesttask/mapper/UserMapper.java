package com.example.clearsolutiontesttask.mapper;


import com.example.clearsolutiontesttask.dto.UpdateFieldRequest;
import com.example.clearsolutiontesttask.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper interface for mapping between User and UpdateFieldRequest objects.
 */
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl")
public interface UserMapper {
    /**
     * Maps fields from an UpdateFieldRequest to a User, ignoring null values.
     *
     * @param request The UpdateFieldRequest object containing the fields to map.
     * @param user    The User object to update with the mapped fields.
     * @return The updated User object.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromRequest(UpdateFieldRequest request, @MappingTarget User user);
}
