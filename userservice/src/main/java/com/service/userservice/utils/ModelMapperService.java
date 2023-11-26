package com.service.userservice.utils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class ModelMapperService {
    //Mapping Service to convert Entity to DTO
    private final ConversionService conversionService = DefaultConversionService.getSharedInstance();

    public <D> D map(Object input, Class<D> destinationType)  {
        //Method to Map single Object
        //Checking id Input is null or not if null then we return null
        Object source = input;
        if (input instanceof Optional) {
            Optional<?> optional = (Optional<?>) input;
            source = optional.orElse(null);
        }

        if(source==null){
            return null;
        }
        //Instantiating Class of destination and source
        var destinationObject = BeanUtils.instantiateClass(destinationType);
        var bwDestination = new BeanWrapperImpl();
        bwDestination.setConversionService(conversionService);
        bwDestination.setBeanInstance(destinationObject);

        var bwSource = new BeanWrapperImpl();
        bwSource.setConversionService(conversionService);
        bwSource.setBeanInstance(source);
        //Retrieving all property of source and saving in list destinationFields
        List<String> destinationFields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(destinationType)) {
            String name = propertyDescriptor.getName();
            destinationFields.add(name);
        }
        //Getting and Setting data from source to destination
        for (String field : destinationFields) {
            if (bwDestination.isWritableProperty(field) && bwSource.isReadableProperty(field)) {
                try {
                    bwDestination.setPropertyValue(field, bwSource.getPropertyValue(field));
                } catch (BeansException e) {
                    System.out.println("Error Occured in Conversion");
                }
            }
        }
        return destinationObject;
    }

    public  <S, T> List<T> mapToList(Iterable<S> source, Class<T> targetClass) {
        //This method is use to convert list of object to other object using stream and calling above method
        return StreamSupport.stream(
                        source.spliterator(), false)
                .map(element -> map(element, targetClass))
                .collect(Collectors.toList());
    }
}
