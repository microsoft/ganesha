package com.microsoft.ganesha.response.model.getorderdetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"debitAuthorizationId", "bankAccountNickName", "bankAccountNumber", "bankAccountType",
        "bankAccountId", "amount"})
public class AchDetail implements Serializable {

    private static final long serialVersionUID = -6435913917710992575L;

    @JsonProperty(value = "debitAuthorizationId")
    private String debitAuthorizationId;

    @JsonProperty(value = "bankAccountNickName")
    private String bankAccountNickName;

    @JsonProperty(value = "bankAccountNumber")
    private String bankAccountNumber;

    @JsonProperty(value = "bankAccountType")
    private String bankAccountType;

    @JsonProperty(value = "bankAccountId")
    private String bankAccountId;

    @JsonProperty(value = "amount")
    private String amount;
}
