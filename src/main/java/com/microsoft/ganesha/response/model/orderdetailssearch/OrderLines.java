package com.microsoft.ganesha.response.model.orderdetailssearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"orderLineId", "pharmacyCode", "provider", "rxNumber", "refillId", "fillType", "lineType",
        "lineTypeId", "daysSupply", "unitSellingPrice", "orderLineStatusCode", "lineAmount", "context", "openFlag",
        "cancelledFlag", "pharmacyBilledIndicator", "drugDispensed", "quantity", "shipping", "holds", "billingInfos",
        "createDate", "lastUpdateDate", "orderCommunications", "statusInformation", "officeBasedException",
        "estimatedCopay", "estimationDate"})
public class OrderLines implements Serializable {

    private static final long serialVersionUID = 8164139726332316022L;

    @JsonProperty(value = "orderLineId")
    private String orderLineId;

    @JsonProperty(value = "pharmacyCode")
    private String pharmacyCode;

    @JsonProperty(value = "provider")
    private OrderLinesProvider provider;

    @JsonProperty(value = "rxNumber")
    private String rxNumber;

    @JsonProperty(value = "refillId")
    private String refillId;

    @JsonProperty(value = "fillType")
    private String fillType;

    @JsonProperty(value = "lineType")
    private String lineType;

    @JsonProperty(value = "lineTypeId")
    private String lineTypeId;

    @JsonProperty(value = "daysSupply")
    private String daysSupply;

    @JsonProperty(value = "unitSellingPrice")
    private String unitSellingPrice;

    @JsonProperty(value = "orderLineStatusCode")
    private String orderLineStatusCode;

    @JsonProperty(value = "lineAmount")
    private String lineAmount;

    @JsonProperty(value = "context")
    private String context;

    @JsonProperty(value = "openFlag")
    private String openFlag;

    @JsonProperty(value = "cancelledFlag")
    private String cancelledFlag;

    @JsonProperty(value = "pharmacyBilledIndicator")
    private String pharmacyBilledIndicator;

    @JsonProperty(value = "drugDispensed")
    private DrugDispensed drugDispensed;

    @JsonProperty(value = "quantity")
    private OrderLinesQuantity quantity;

    @JsonProperty(value = "shipping")
    private OrderLinesShipping shipping;

    @JsonProperty(value = "holds")
    private List<OrderLinesHolds> holds;

    @JsonProperty(value = "billingInfos")
    private List<BillingInfo> billingInfos;

    @JsonProperty(value = "createDate")
    private String createDate;

    @JsonProperty(value = "lastUpdateDate")
    private String lastUpdateDate;

    @JsonProperty(value = "officeBasedException")
    private String officeBasedException;

    @JsonProperty(value = "estimatedCopay")
    private String estimatedCopay;

    @JsonProperty(value = "estimationDate")
    private String estimationDate;
}
