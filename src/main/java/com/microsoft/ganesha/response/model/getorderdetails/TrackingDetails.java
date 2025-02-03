package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingDetails implements Serializable {

    private static final long serialVersionUID = -9127543790962596654L;

    private String date;
    private String city;
    private String state;
    private List<TrackingEvent> events;

}
