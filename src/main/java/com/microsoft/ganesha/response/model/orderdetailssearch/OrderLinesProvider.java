package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"providerId", "npi", "firstName", "middleName", "lastName", "phone", "fax", "address"})
public class OrderLinesProvider implements Serializable {

    private static final long serialVersionUID = 5254055885529177402L;

    @JsonProperty(value = "providerId")
    private String providerId;

    @JsonProperty(value = "npi")
    private String npi;

    @JsonProperty(value = "firstName")
    private String firstName;

    @JsonProperty(value = "middleName")
    private String middleName;

    @JsonProperty(value = "lastName")
    private String lastName;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "fax")
    private String fax;

    @JsonProperty(value = "address")
    private Address address;

}
